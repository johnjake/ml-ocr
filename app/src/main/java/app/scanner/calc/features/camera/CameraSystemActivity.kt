package app.scanner.calc.features.camera

import app.scanner.calc.R
import app.scanner.calc.bases.BaseActivity
import app.scanner.calc.databinding.ActivityMainBinding
import app.scanner.domain.extension.toast

class CameraSystemActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun setUpView() {
        super.setUpView()
        toast(getString(R.string.variant_type))
    }
}