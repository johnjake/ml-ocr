package app.scanner.calc.features.camera

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.scanner.calc.R
import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.domain.extension.toast
import app.scanner.domain.manager.CameraBuilder
import app.scanner.domain.utils.REQUEST_CODE_PERMISSIONS
import app.scanner.domain.utils.REQUIRED_PERMISSIONS

class CameraSystemActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {
    override fun setUpView() {
        super.setUpView()
        setUpCamera()
    }

    override fun setUpObserver() {
        super.setUpObserver()
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

    private fun runCamera() {
        val camera = CameraBuilder(
            context = this,
            previewView = binding.viewFinder,
            lifecycleOwner = this
        )
        camera.build()
    }
}
