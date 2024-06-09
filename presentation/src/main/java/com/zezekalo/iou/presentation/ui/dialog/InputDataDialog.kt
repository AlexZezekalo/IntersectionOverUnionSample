package com.zezekalo.iou.presentation.ui.dialog

import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.navArgs
import com.zezekalo.iou.presentation.R
import com.zezekalo.iou.presentation.databinding.BoundingBoxInputLayoutBinding
import com.zezekalo.iou.presentation.databinding.DialogInputDataBinding
import com.zezekalo.iou.presentation.ui.base.BaseDialog
import com.zezekalo.iou.presentation.ui.util.MinMaxIntentFilter
import com.zezekalo.iou.presentation.viewmodel.InputDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class InputDataDialog: BaseDialog<DialogInputDataBinding, InputDataViewModel>() {

    private val args: InputDataDialogArgs by navArgs()

    @Inject lateinit var inputFilter: MinMaxIntentFilter

    override fun onViewBound(binding: DialogInputDataBinding, savedInstanceState: Bundle?) {
        viewModel.setInputData(args.inputData)

        setTitles(binding)
        setupInputFields(binding)
        setupListeners(binding)
    }

    private fun setTitles(binding: DialogInputDataBinding) {
        with(binding) {
            inputGroundTruthBoxCoordinatesLayout.boxTitle.text = getString(R.string.ground_truth_subtitle)
            inputPredictedBoxCoordinatesLayout.boxTitle.text = getString(R.string.predicted_subtitle)
        }
    }

    private fun setupInputFields(binding: DialogInputDataBinding) {
        with(binding) {
            val arrayOfFilters: Array<InputFilter> = arrayOf(inputFilter)
            setInputFilter(inputGroundTruthBoxCoordinatesLayout, arrayOfFilters)
            setInputFilter(inputPredictedBoxCoordinatesLayout, arrayOfFilters)

            inputGroundTruthBoxCoordinatesLayout.boxBottomInput.nextFocusForwardId = inputPredictedBoxCoordinatesLayout.boxLeftInput.id
            inputPredictedBoxCoordinatesLayout.boxBottomInput.imeOptions = EditorInfo.IME_ACTION_DONE

            inputGroundTruthBoxCoordinatesLayout.boxBottomInput.setOnEditorActionListener { view, actionId, event ->
                Timber.i("OnEditorActionListener: 1 actionId = $actionId, event = $event")
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_NULL) {
                    Timber.i("OnEditorActionListener: 2 actionId = $actionId, event = $event")
                    inputPredictedBoxCoordinatesLayout.boxLeftInput.requestFocus()
                    true
                }
                false
            }
        }
    }

    private fun setInputFilter(binding: BoundingBoxInputLayoutBinding, arrayOfFilters: Array<InputFilter>) {
        with(binding) {
            boxLeftInput.filters = arrayOfFilters
            boxTopInput.filters = arrayOfFilters
            boxRightInput.filters = arrayOfFilters
            boxBottomInput.filters = arrayOfFilters
        }
    }

    private fun setupListeners(binding: DialogInputDataBinding) {
        with(binding) {
            closeButton.setOnClickListener { dismiss() }
            applyButton.setOnClickListener {  }
            clearButton.setOnClickListener {  }
        }
    }

    override fun listenViewModel(viewModel: InputDataViewModel) {

    }
}