package app.scanner.calc.features

import android.graphics.Bitmap
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import app.scanner.calc.R
import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.bases.BaseState
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.calc.features.adapter.CalculatedAdapter
import app.scanner.domain.utils.CAMERA_SYSTEM
import app.scanner.domain.utils.FILE_SYSTEM
import app.scanner.domain.extension.getBitmap
import app.scanner.domain.extension.gone
import app.scanner.domain.extension.visible
import app.scanner.domain.filesystem.FileGallery
import app.scanner.domain.manager.CameraBuilder
import app.scanner.domain.model.MathData
import app.scanner.domain.toast
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.flow.collectLatest

class MainActivity : BaseActivity<ActivityMainBinding>
    (ActivityMainBinding::inflate) {

    private val openGallery: FileGallery by lazy { FileGallery(this.activityResultRegistry) }
    private val calcAdapter: CalculatedAdapter by lazy { CalculatedAdapter() }
    private val viewModel: MainViewModel by viewModels()
    private val listResult: MutableList<MathData>? = arrayListOf()
    private var isFile: Boolean = false
    private var savedBitmap: Bitmap? = null

    override fun setUpView() {
        super.setUpView()
        lifecycle.addObserver(openGallery)
        viewModel.verifyFlavors(getString(R.string.variant_type))

        binding.apply {
            imgClose.setOnClickListener {
                closeResult()
            }

            imgClear.setOnClickListener {
                listResult?.clear()
                calcAdapter.submitList(listResult)
                closeResult()
            }

            rvExpression.apply {
                adapter = calcAdapter
            }

            btnOpenSystem.setOnClickListener {
                when {
                    isFile -> readFileSystem()
                    else -> readCameraSystem()
                }
            }
        }
   }

    override fun setUpObserver() {
        super.setUpObserver()

        lifecycleScope.launchWhenStarted {
            viewModel.mainState.collectLatest { state ->
                when(state) {
                    is BaseState.VariantState -> handleSuccessState(state.data)
                }
            }
        }

        /** collect recognized text **/
        this.lifecycleScope.launchWhenStarted {
            viewModel.ocrState.collectLatest { state ->
                when(state) {
                    is BaseState.OnReadSuccess -> handleSuccessReader(state.data)
                    is BaseState.OnReadFailed -> handleFailedReader(state.error)
                }
            }
        }
    }

    private fun ActivityMainBinding.closeResult() {
        textInImageLayout.gone()
        previewImage.gone()
        btnOpenSystem.visible()
    }

    private fun readFileSystem() {
        openGallery.selectImage()
    }

    private fun readCameraSystem() {
        val recognizer = TextRecognition.getClient()
        binding.apply {
            savedBitmap = viewFinder.bitmap
            previewImage.visible()
            previewImage.setImageBitmap(viewFinder.bitmap!!)
            viewModel.recognizeExpression(
                recognizer = recognizer,
                bitmap = savedBitmap ?: root.context.getBitmap()
            )
        }
    }

    private fun handleFailedReader(error: String?) {
        toast("Error: $error")
    }

    private fun handleSuccessReader(data: Text) {
        binding.textInImageLayout.visible()
        lifecycleScope.launchWhenStarted {
            val rawData = viewModel.getProcessResult(data)
            val mathExp =  viewModel.getMathExpression(rawData)
            val mathResult = viewModel.getMathResult(mathExp)
            listResult?.add(MathData(
                id = savedBitmap?.generationId ?: 0,
                expression = mathExp,
                result = mathResult
            ))
            calcAdapter.submitList(listResult?.toList())
            binding.btnOpenSystem.gone()
        }
    }

    private fun handleSuccessState(state: String) {
        when(state) {
            CAMERA_SYSTEM -> requestPermission = openCamera()
            FILE_SYSTEM -> isFile = true
        }
    }

    private fun openCamera() {
        val camera = CameraBuilder(
            context = this,
            previewView = binding.viewFinder,
            lifecycleOwner = this
        )
        camera.build()
    }

    override fun onResume() {
        super.onResume()
        if(openGallery.isRead) {
            require(openGallery.getUri()!= null) { toast(getString(R.string.invalid_files)) }
            binding.previewImage.apply {
                visibility = View.VISIBLE
                setImageURI(openGallery.getUri())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraBuilder.closeCamera()
    }
}