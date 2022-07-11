package app.scanner.calc.features

import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.domain.manager.CameraBuilder

class MainActivity : BaseActivity<ActivityMainBinding>
    (ActivityMainBinding::inflate) {

   override fun setUpView() {
        super.setUpView()
       requestPermission = cameraBuilder()
    }

    private fun cameraBuilder() {
        val camera = CameraBuilder(
            context = this,
            previewView = binding.viewFinder,
            lifecycleOwner = this
        )
        camera.build()
    }
}