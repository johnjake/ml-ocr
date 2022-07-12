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
import app.scanner.domain.extension.getBitmap
import app.scanner.domain.extension.getDefaultBitmap
import app.scanner.domain.extension.gone
import app.scanner.domain.extension.roundOff
import app.scanner.domain.extension.toast
import app.scanner.domain.extension.visible
import app.scanner.domain.filesystem.FileGallery
import app.scanner.domain.manager.CameraBuilder
import app.scanner.domain.model.MathData
import app.scanner.domain.utils.CAMERA_SYSTEM
import app.scanner.domain.utils.FILE_SYSTEM
import app.scanner.domain.utils.showClearDialog
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import kotlin.math.roundToInt
import kotlin.math.roundToLong


class MainActivity : BaseActivity<ActivityMainBinding>
    (ActivityMainBinding::inflate) {

    private val openGallery: FileGallery by lazy { FileGallery(this.activityResultRegistry) }
    private val calcAdapter: CalculatedAdapter by lazy { CalculatedAdapter() }
    private val viewModel: MainViewModel by viewModels()
    private val listResult: MutableList<MathData>? = arrayListOf()
    private var isFileSystem: Boolean = false
    private var isScanFile: Boolean = false
    private var savedBitmap: Bitmap? = null

    override fun setUpView() {
        super.setUpView()
        lifecycle.addObserver(openGallery)

        verifyVariant(getString(R.string.variant_type))

        binding.apply {

            imgClose.setOnClickListener {
                closeResult()
            }

            imgClear.setOnClickListener {
                onClearData()
            }

            rvExpression.apply {
                adapter = calcAdapter
            }

            btnOpenSystem.setOnClickListener {
                when {
                    isFileSystem -> {
                        when {
                            !isScanFile -> readFileSystem()
                            else -> recognizeTextImage()
                        }
                    }
                    else -> {
                        binding.btnOpenSystem.text = getString(R.string.scan_input)
                        readFromCamera()
                    }
                }
            }
        }
   }

    override fun setUpObserver() {
        super.setUpObserver()

        /** collect recognized text **/
        this.lifecycleScope.launchWhenStarted {
            viewModel.ocrState.collectLatest { state ->
                when(state) {
                    is BaseState.OnReadSuccess -> handleSuccessReader(state.data)
                    is BaseState.OnReadFailed -> handleFailedReader(state.error)
                    else -> { Timber.e(getString(R.string.unknown_error)) }
                }
            }
        }
    }

    private fun ActivityMainBinding.closeResult() {
        textInImageLayout.gone()
        previewImage.gone()
        btnOpenSystem.visible()
        if(!isScanFile && isFileSystem) binding.btnOpenSystem.text = getString(R.string.open_gallery)
    }

    private fun readFileSystem() {
        openGallery.selectImage()
        isScanFile = false
    }

    private fun recognizeTextImage() {
        isScanFile = false
        val recognizer = TextRecognition.getClient()
        binding.apply {
            previewImage.visible()
            savedBitmap = previewImage.getBitmap()
            viewModel.recognizeExpression(
                recognizer = recognizer,
                bitmap = savedBitmap ?: root.context.getDefaultBitmap()
            )
        }
    }

    private fun readFromCamera() {
        val recognizer = TextRecognition.getClient()
        binding.apply {
            savedBitmap = viewFinder.bitmap
            previewImage.visible()
            previewImage.setImageBitmap(viewFinder.bitmap!!)
            viewModel.recognizeExpression(
                recognizer = recognizer,
                bitmap = savedBitmap ?: root.context.getDefaultBitmap()
            )
        }
    }

    private fun handleFailedReader(error: String?) {
        toast("Error: $error")
    }

    private fun handleSuccessReader(data: Text) {
        binding.textInImageLayout.visible()
        lifecycleScope.launchWhenStarted {
          try {
                val rawData = viewModel.getProcessResult(data)
                val mathExp =  viewModel.getMathExpression(rawData)
                val mathResult = viewModel.getMathResult(mathExp)
                listResult?.add(MathData(
                    id = savedBitmap?.generationId ?: 0,
                    expression = mathExp,
                    result = mathResult.roundOff()
                ))
                calcAdapter.submitList(listResult?.toList())
                binding.btnOpenSystem.gone()
            } catch (ex: Exception) {
                toast(getString(R.string.unknown_error))
            }
        }
    }

    private fun verifyVariant(state: String) {
        when(state) {
            CAMERA_SYSTEM -> requestPermission = openCamera()
            FILE_SYSTEM -> isFileSystem = true
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

    private fun onClearData() {
        val message = getString(R.string.clear_all_data)
        val cancel = getString(R.string.cancel)
        showClearDialog(
            context = this,
            title = getString(R.string.popup_clear),
            message = message,
            positiveButtonText = getString(R.string.confirm_yes),
            negativeButtonText = cancel,
            hasNegativeButton = true,
            isCancellable = false,
            positiveButtonClicked = {
                listResult?.clear()
                calcAdapter.submitList(listResult)
                binding.closeResult()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if(openGallery.isRead) {
            require(openGallery.getUri()!= null) { toast(getString(R.string.invalid_files)) }
            isScanFile = true
            binding.previewImage.apply {
                visibility = View.VISIBLE
                setImageURI(openGallery.getUri())
            }
            binding.btnOpenSystem.text = getString(R.string.scan_input)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraBuilder.closeCamera()
    }
}