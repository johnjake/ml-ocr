package app.scanner.calc.features

import android.view.View
import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.domain.filesystem.FileGallery
import app.scanner.domain.manager.CameraBuilder

class MainActivity : BaseActivity<ActivityMainBinding>
    (ActivityMainBinding::inflate) {

    private val openGallery: FileGallery by lazy { FileGallery(this.activityResultRegistry) }

    override fun setUpView() {
        super.setUpView()
       lifecycle.addObserver(openGallery)
       binding.btnOpenSystem.setOnClickListener {
            openGallery.selectImage()
       }
   }
    override fun onResume() {
        super.onResume()
        if(openGallery.init) {
            binding.previewImage.apply {
                visibility = View.VISIBLE
                setImageURI(openGallery.getUri())
            }
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
    override fun onDestroy() {
        super.onDestroy()
        CameraBuilder.closeCamera()
    }
}