package com.zezekalo.iou.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.zezekalo.iou.presentation.ui.util.extensions.createViewModel
import com.zezekalo.iou.presentation.ui.util.extensions.inflateBinding
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel

abstract class BaseFragment<VB: ViewBinding, VM: BaseViewModel>: Fragment(), BaseView<VB, VM> {

    protected val  viewModel: VM by lazy (::createViewModel)

    private var binding: VB? = null

    @CheckResult
    override fun requireBinding(): VB = binding ?: error("Binding in fragment $this is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflateBinding(inflater, container, false)
        .also(::binding::set)
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireBinding().let { binding ->
            onViewBound(binding, savedInstanceState)
            listenViewModel(viewModel)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}