package app.scanner.calc.bases

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import app.scanner.calc.hilt.ActivityEntry

abstract class BaseActivity<T : ViewBinding>(
    private val setUpViewBinding: (LayoutInflater) -> T
) : ActivityEntry() {
    lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setUpViewBinding(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
        setUpView()
    }
    open fun setUpObserver() {}
    open fun setUpView() {}
}
