package com.zezekalo.iou.presentation.ui.base

import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

interface BaseView<VB : ViewBinding, VM : ViewModel> {
    @CheckResult
    fun requireBinding(): VB

    fun onViewBound(
        binding: VB,
        savedInstanceState: Bundle?,
    )

    fun listenViewModel(viewModel: VM)
}
