package app.scanner.calc.features.filesystem

import android.graphics.Bitmap
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import app.scanner.calc.R
import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.bases.BaseState
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.calc.features.MainViewModel
import app.scanner.calc.features.adapter.CalculatedAdapter
import app.scanner.domain.extension.getBitmap
import app.scanner.domain.extension.getDefaultBitmap
import app.scanner.domain.extension.gone
import app.scanner.domain.extension.toast
import app.scanner.domain.extension.visible
import app.scanner.domain.filesystem.FileGallery
import app.scanner.domain.model.MathData
import app.scanner.domain.utils.EMPTY
import app.scanner.domain.utils.showClearDialog
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.util.Random

class FileSystemActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val openGallery: FileGallery by lazy {
        FileGallery(this.activityResultRegistry)
    }
    private val calcAdapter: CalculatedAdapter by lazy {
        CalculatedAdapter()
    }
    private val viewModel: MainViewModel by viewModels()
    private val listResult: MutableList<MathData>? = arrayListOf()
    private var savedBitmap: Bitmap? = null
    private lateinit var recognizer: TextRecognizer
    private var isScanFile: Boolean = false

    override fun setUpView() {
        super.setUpView()
        lifecycle.addObserver(openGallery)

        binding.apply {
            btnOpenSystem.setOnClickListener {
                when {
                    !isScanFile -> readFileSystem()
                    else -> recognizeTextImage()
                }
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
        if (!isScanFile) binding.btnOpenSystem.text = getString(R.string.open_gallery)
    }

    private fun readFileSystem() {
        openGallery.selectImage()
    }

    private fun recognizeTextImage() {
        isScanFile = false
        recognizer = TextRecognition.getClient()
        binding.apply {
            previewImage.visible()
            savedBitmap = previewImage.getBitmap()
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
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (openGallery.isRead) {
            require(openGallery.getUri() != null) { toast(getString(R.string.invalid_files)) }
            isScanFile = true
            binding.previewImage.apply {
                visibility = View.VISIBLE
                setImageURI(openGallery.getUri())
            }
            binding.btnOpenSystem.text = getString(R.string.scan_input)
        }
    }
}
