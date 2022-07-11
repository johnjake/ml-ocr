package app.scanner.calc.features

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import app.scanner.calc.R
import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.bases.BaseState
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.domain.CAMERA_SYSTEM
import app.scanner.domain.FILE_SYSTEM
import app.scanner.domain.filesystem.FileGallery
import app.scanner.domain.manager.CameraBuilder
import app.scanner.domain.toast
import kotlinx.coroutines.flow.collectLatest

class MainActivity : BaseActivity<ActivityMainBinding>
    (ActivityMainBinding::inflate) {

    private val openGallery: FileGallery by lazy { FileGallery(this.activityResultRegistry) }
    private val viewModel: MainViewModel by viewModels()
    private var isFile: Boolean = false

    override fun setUpView() {
        super.setUpView()
        lifecycle.addObserver(openGallery)
        viewModel.verifyFlavors(getString(R.string.variant_type))

        binding.btnOpenSystem.setOnClickListener {
            if(isFile) openGallery.selectImage()
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
    }

    private fun handleSuccessState(state: String) {
        when(state) {
            CAMERA_SYSTEM -> requestPermission = cameraBuilder()
            FILE_SYSTEM -> isFile = true
        }
    }

    private fun cameraBuilder() {
        val camera = CameraBuilder(
            context = this,
            previewView = binding.viewFinder,
            lifecycleOwner = this
        )
        camera.build()
    }

    override fun onResume() {
        super.onResume()
        if(openGallery.init) {
            require(openGallery.getUri()!= null) { toast("Invalid image file!") }
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