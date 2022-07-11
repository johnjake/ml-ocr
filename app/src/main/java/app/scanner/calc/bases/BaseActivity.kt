package app.scanner.calc.bases

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import app.scanner.calc.hilt.HiltActivityEntry
import app.scanner.domain.utils.REQUEST_CODE_PERMISSIONS
import app.scanner.domain.utils.REQUIRED_PERMISSIONS
import app.scanner.domain.extension.toast

abstract class BaseActivity<T : ViewBinding>(
    private val setUpViewBinding: (LayoutInflater) -> T
) : HiltActivityEntry() {
    lateinit var binding: T

    private var cameraUnit: Unit? = null
    var requestPermission: Unit? get() = cameraUnit
        set(value) {
            cameraUnit = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setUpViewBinding(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
        setUpView()
        setUpCamera()
    }

    private fun requestAndroidPermission() {
        ActivityCompat.requestPermissions(
            this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setUpCamera() {
        if (allPermissionsGranted()) {
            cameraUnit
        } else {
            requestAndroidPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraUnit
            } else {
                toast("Camera Permission Denied")
            }
        }
    }
    open fun setUpObserver() {}
    open fun setUpView() {}
}
