package app.scanner.calc.features.filesystem

import android.graphics.Bitmap
import android.view.View
import app.scanner.calc.R
import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.calc.features.adapter.CalculatedAdapter
import app.scanner.domain.extension.getBitmap
import app.scanner.domain.extension.getDefaultBitmap
import app.scanner.domain.extension.gone
import app.scanner.domain.extension.roundOff
import app.scanner.domain.extension.toast
import app.scanner.domain.extension.visible
import app.scanner.domain.filesystem.FileGallery
import app.scanner.domain.model.MathData
import app.scanner.domain.utils.EMPTY
import app.scanner.domain.utils.INVALID_MATH_EXPRESSION
import app.scanner.domain.utils.PATTERN_MATH
import app.scanner.domain.utils.showClearDialog
import bsh.Interpreter
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer

class FileSystemActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val openGallery: FileGallery by lazy { FileGallery(this.activityResultRegistry) }
    private val calcAdapter: CalculatedAdapter by lazy { CalculatedAdapter() }
    private val listResult: MutableList<MathData>? = arrayListOf()
    private var isScanFile: Boolean = false
    private var savedBitmap: Bitmap? = null
    private lateinit var recognizer: TextRecognizer

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

    private fun ActivityMainBinding.closeResult() {
        textInImageLayout.gone()
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
        binding.textInImageLayout.visible()
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(inputImage).addOnSuccessListener { text ->
            val textResult = processResult(text)
            val mathExp = getExpression(textResult)
            val result = solveMathEquation(mathExp)
            listResult?.add(
                MathData(
                    id = savedBitmap?.generationId ?: 0,
                    expression = mathExp,
                    result = result.roundOff()
                )
            )
            calcAdapter.submitList(listResult?.toList())
            binding.btnOpenSystem.gone()
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

    private fun getExpression(expression: String): String {
        val mathExpression = expression.replace(PATTERN_MATH.toRegex(), EMPTY)
        return when {
            mathExpression.isNotEmpty() -> mathExpression
            else -> INVALID_MATH_EXPRESSION
        }
    }

    private fun solveMathEquation(mathExpression: String): String {
        return try {
            val interpreter = Interpreter()
            interpreter.eval("result =$mathExpression")
            interpreter["result"].toString()
        } catch (ex: Exception) {
            "0"
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
                binding.closeResult()
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
