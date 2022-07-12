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

    /**
     * FileGallery - is a class for opening gallery it extend using DefaultLifecycleObserver with
     * @param ActivityResultRegistry
     *
     * CalculatedAdapter - the purpose of this adapter is for the result value tabulated into list.
     *
     * MainViewModel - contains methods or function for opening ML text recognition and function such as get Math expression
     * from a given string then calculate it.
     * @param ReaderAction - is an interface and function implemented in ReaderRepository class.
     *
     * @param listResult - this is actually temp mutable list that holds the calculated value and math expression.
     *
     * @param isFileSystem - instead of writing multiple MainActivity for each product flavor, what I did was just create
     * a string in resources to and add flag like `cameraSystem`, `fileSystem` isFileSystem means
     * the current flavor is file system or browse to gallery only and the camera was set disabled
     * true - for isFileSystem is currently in fileSystem flavor.
     * false - is set if not in fileSystem Flavor.
     *
     * @param isScanFile - since the UI design was first the user browse the image then run Scan Input
     * to run ML Text Recognition.
     * true - if imageView contain image to be scan or text recognized.
     * false - if the state is browsing to gallery.
     *
     * @param saveBitmap - this is the container of bitmap after scanning using camera or browse image from gallery.
     * **/

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

    /**
     * if current state or flavor is FileSystem this readFileSystem is called to open the gallery
     * **/
    private fun readFileSystem() {
        openGallery.selectImage()
        isScanFile = false
    }

    /**
     * ** recognizeTextImage - is called when ever you need to scan or text recognized the image and its
     * used in fileSystem.  Or the current flavor or state is FileSystem
     * */
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

    /**
     * ** readFromCamera - is called when ever you need to scan or text recognized the image and its
     * used in cameraSystem. Or the current flavor or state is Camera
     * */

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

    /**
     * ** handleSuccessReader - after successfully scan the result text which is contains alphanumeric
     * the inside this function process to extract the math expression after extracting math expression
     * calculate or solve it then pass the value to listResult which is mutableList then submit the list to
     * adapter
     * */
    private fun handleSuccessReader(data: Text) {
        binding.textInImageLayout.visible()
        lifecycleScope.launchWhenStarted {
          try {
                val rawData = viewModel.getProcessResult(data)
                val mathExp =  viewModel.getMathExpression(rawData)
                val mathResult = viewModel.solveMathEquation(mathExp)
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

    /**
     * this function is called first the first time you open the app either the flavor is fileSystem
     * or Camera System it will prompt the user or run request permission.
     * @param state - (fileSystem, CameraSystem)
     *
     * @param requestPermission a property backfield with param as lambda expression as request permission camera
     * reside in BaseActivity Class.
     *
     * **/

    private fun verifyVariant(state: String) {
        when(state) {
            CAMERA_SYSTEM -> requestPermission = openCamera()
            FILE_SYSTEM -> isFileSystem = true
        }
    }

    /**
     * openCamera - this will initialized the camera and open
     * **/

    private fun openCamera() {
        val camera = CameraBuilder(
            context = this,
            previewView = binding.viewFinder,
            lifecycleOwner = this
        )
        camera.build()
    }

    /**
     * onClearData - is an alert dialog to clear the data.
     * **/

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

    /***
     * onResume this is where the image URI extracted from FileGallery and assigned directly to imageView
     * **/
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