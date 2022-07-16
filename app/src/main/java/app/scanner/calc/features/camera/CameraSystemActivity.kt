package app.scanner.calc.features.camera

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import app.scanner.calc.R
import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.bases.BaseState
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.calc.features.MainViewModel
import app.scanner.calc.features.adapter.CalculatedAdapter
import app.scanner.domain.extension.getDefaultBitmap
import app.scanner.domain.extension.gone
import app.scanner.domain.extension.toast
import app.scanner.domain.extension.visible
import app.scanner.domain.manager.CameraBuilder
import app.scanner.domain.model.MathData
import app.scanner.domain.utils.EMPTY
import app.scanner.domain.utils.REQUEST_CODE_PERMISSIONS
import app.scanner.domain.utils.REQUIRED_PERMISSIONS
import app.scanner.domain.utils.showClearDialog
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.util.Random

class CameraSystemActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {

    private val viewModel: MainViewModel by viewModels()
    private val calcAdapter: CalculatedAdapter by lazy { CalculatedAdapter() }
    private val listResult: MutableList<MathData>? = arrayListOf()
    private var savedBitmap: Bitmap? = null
    private lateinit var recognizer: TextRecognizer

    override fun setUpView() {
        super.setUpView()
        setUpCamera()
        binding.apply {
            btnOpenSystem.setOnClickListener {
                readFromCamera()
            }

            rvExpression.apply {
                adapter = calcAdapter
            }

            imgClose.setOnClickListener {
                closeResult()
            }

            imgClear.setOnClickListener {
                onClearData()
            }
        }
    }

    override fun setUpObserver() {
        super.setUpObserver()
        lifecycleScope.launchWhenStarted {
            viewModel.ocrState.collectLatest { state ->
                when (state) {
                    is BaseState.ShowLoader -> binding.progress.visible()
                    is BaseState.HideLoader -> {
                        delay(300)
                        binding.progress.gone()
                    }
                    is BaseState.OnSuccess -> handleSuccess(state.data)
                    is BaseState.OnFailure -> handleFailed(state.error)
                }
            }
        }
    }

    private fun handleSuccess(data: MathData) {
        listResult?.add(data)
        calcAdapter.submitList(listResult?.toList())
    }

    private fun handleFailed(error: String?) {
        toast(error ?: getString(R.string.unknown_error))
    }

    private fun ActivityMainBinding.closeResult() {
        cardReaderLayout.gone()
        previewImage.gone()
        btnOpenSystem.visible()
    }

    private fun requestAndroidPermission() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setUpCamera() {
        if (allPermissionsGranted()) {
            runCamera()
        } else {
            requestAndroidPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                runCamera()
            } else {
                toast(getString(R.string.camera_denied))
            }
        }
    }

    private fun readFromCamera() {
        recognizer = TextRecognition.getClient()
        binding.apply {
            savedBitmap = viewFinder.bitmap
            previewImage.visible()
            previewImage.setImageBitmap(viewFinder.bitmap!!)
            getTextRecognition(savedBitmap ?: root.context.getDefaultBitmap())
        }
    }

    private fun getTextRecognition(bitmap: Bitmap) {
        binding.cardReaderLayout.visible()
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        when {
            this::recognizer.isInitialized -> {
                recognizer.process(inputImage).addOnSuccessListener { text ->
                    val textResult = processResult(text)
                    viewModel.getResult(textResult, savedBitmap?.generationId ?: Random().nextInt(50))
                    calcAdapter.submitList(listResult?.toList())
                    binding.btnOpenSystem.gone()
                }.addOnFailureListener { e ->
                    e.printStackTrace()
                }
            }
            else -> toast(getString(R.string.recognizer_initialized))
        }
    }

    private fun processResult(recognizeText: Text): String {
        var resultText: String = EMPTY
        for (block in recognizeText.textBlocks) {
            for (line in block.lines) {
                resultText += line.text + " \n"
            }
            resultText += "\n"
        }
        return resultText
    }

    private fun runCamera() {
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
}
