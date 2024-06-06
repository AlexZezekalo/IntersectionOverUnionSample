package com.zezekalo.iou.presentation.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.zezekalo.iou.presentation.ui.util.createViewModel
import com.zezekalo.iou.presentation.ui.util.inflateBinding
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel

abstract class BaseDialog<VB: ViewBinding, VM: BaseViewModel>: Fragment(), BaseView<VB, VM> {

    private val  viewModel: VM by lazy (::createViewModel)

    private var binding: VB? = null

    @CheckResult
    override fun requireBinding(): VB = binding ?: error("Binding in dialog $this is null")

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