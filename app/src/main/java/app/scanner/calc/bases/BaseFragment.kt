package app.scanner.calc.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import app.scanner.calc.hilt.FragmentEntry

abstract class BaseFragment<T : ViewBinding>(
    private val setUpViewBinding: (LayoutInflater) -> T
) : FragmentEntry() {

    lateinit var binding: T

    var customBackPressCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = setUpViewBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
        setUpView()
        loadContent()
        addViewListeners()
        setupAdapter(view)
    }
    fun baseActivity() = (requireActivity() as BaseActivity<*>)
    open fun setUpObserver() {}
    open fun setUpView() {}
    open fun loadContent() {}
    open fun setupAdapter(view: View) {}
    open fun addViewListeners() {}
}
