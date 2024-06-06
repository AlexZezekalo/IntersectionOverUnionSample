package com.zezekalo.iou.presentation.ui.dialog

import android.os.Bundle
import com.zezekalo.iou.presentation.databinding.DialogInputDataBinding
import com.zezekalo.iou.presentation.ui.base.BaseDialog
import com.zezekalo.iou.presentation.viewmodel.InputDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputDataDialog: BaseDialog<DialogInputDataBinding, InputDataViewModel>() {

    override fun onViewBound(binding: DialogInputDataBinding, savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun listenViewModel(viewModel: InputDataViewModel) {
        TODO("Not yet implemented")
    }
}