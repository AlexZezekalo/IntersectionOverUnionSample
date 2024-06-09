package com.zezekalo.iou.presentation.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.zezekalo.iou.presentation.R
import com.zezekalo.iou.presentation.ui.util.extensions.createViewModel
import com.zezekalo.iou.presentation.ui.util.extensions.inflateBinding
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel

abstract class BaseDialog<VB: ViewBinding, VM: BaseViewModel>: DialogFragment(), BaseView<VB, VM> {

    protected val  viewModel: VM by lazy (::createViewModel)

    private var binding: VB? = null

    @CheckResult
    override fun requireBinding(): VB = binding ?: error("Binding in dialog $this is null")

    protected open val isCanceledOnTouchOutside = false

    protected open val style: Int = STYLE_NO_TITLE

    override fun getTheme(): Int = R.style.DialogStyle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(style, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : AppCompatDialog(requireContext(), theme){}.apply {
            setCanceledOnTouchOutside(isCanceledOnTouchOutside)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflateBinding(inflater, container, false)
        .also(::binding::set)
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewBound(requireBinding(), savedInstanceState)
        listenViewModel(viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}