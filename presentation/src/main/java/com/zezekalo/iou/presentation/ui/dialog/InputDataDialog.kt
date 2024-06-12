package com.zezekalo.iou.presentation.ui.dialog

import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.zezekalo.iou.domain.model.exception.CustomException
import com.zezekalo.iou.presentation.R
import com.zezekalo.iou.presentation.databinding.BoundingBoxInputLayoutBinding
import com.zezekalo.iou.presentation.databinding.DialogInputDataBinding
import com.zezekalo.iou.presentation.model.BoundingBoxUi
import com.zezekalo.iou.presentation.model.InputDataUi
import com.zezekalo.iou.presentation.ui.base.BaseDialog
import com.zezekalo.iou.presentation.ui.util.MinMaxInputFilter
import com.zezekalo.iou.presentation.ui.util.extensions.EMPTY
import com.zezekalo.iou.presentation.ui.util.extensions.hideKeyboard
import com.zezekalo.iou.presentation.ui.util.mapper.ThrowableToErrorMessageMapper
import com.zezekalo.iou.presentation.viewmodel.InputDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InputDataDialog: BaseDialog<DialogInputDataBinding, InputDataViewModel>() {

    private val args: InputDataDialogArgs by navArgs()

    @Inject lateinit var inputFilter: MinMaxInputFilter

    @Inject lateinit var mapper: ThrowableToErrorMessageMapper

    override fun onViewBound(binding: DialogInputDataBinding, savedInstanceState: Bundle?) {
        setInitValues(args.inputData, binding)
        setTitles(binding)
        setupInputFields(binding)
        setupListeners(binding)
    }

    private fun setInitValues(inputData: InputDataUi, binding: DialogInputDataBinding) {
        viewModel.setInputData(inputData)
        initBoundingBoxInput(inputData.groundTruthBoundingBox, binding.inputGroundTruthBoxCoordinatesLayout)
        initBoundingBoxInput(inputData.predictedBoundingBox, binding.inputPredictedBoxCoordinatesLayout)
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

            inputGroundTruthBoxCoordinatesLayout.boxBottomInput.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_NULL) {
                    inputPredictedBoxCoordinatesLayout.boxLeftInput.requestFocus()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
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
            inputGroundTruthBoxCoordinatesLayout.boxLeftInput.addTextChangedListener {
                viewModel.processGroundTruthLeftInput(it)
            }
            inputGroundTruthBoxCoordinatesLayout.boxTopInput.addTextChangedListener {
                viewModel.processGroundTruthTopInput(it)
            }
            inputGroundTruthBoxCoordinatesLayout.boxRightInput.addTextChangedListener {
                viewModel.processGroundTruthRightInput(it)
            }
            inputGroundTruthBoxCoordinatesLayout.boxBottomInput.addTextChangedListener {
                viewModel.processGroundTruthBottomInput(it)
            }
            inputPredictedBoxCoordinatesLayout.boxLeftInput.addTextChangedListener {
                viewModel.processPredictedLeftInput(it)
            }
            inputPredictedBoxCoordinatesLayout.boxTopInput.addTextChangedListener {
                viewModel.processPredictedTopInput(it)
            }
            inputPredictedBoxCoordinatesLayout.boxRightInput.addTextChangedListener {
                viewModel.processPredictedRightInput(it)
            }
            inputPredictedBoxCoordinatesLayout.boxBottomInput.addTextChangedListener {
                viewModel.processPredictedBottomInput(it)
            }

            closeButton.setOnClickListener {
                hideKeyboard()
                dismiss()
            }
            clearButton.setOnClickListener {
                viewModel.setInputData(InputDataUi.INITIAL)
                initBoundingBoxInput(BoundingBoxUi.EMPTY, inputGroundTruthBoxCoordinatesLayout)
                initBoundingBoxInput(BoundingBoxUi.EMPTY, inputPredictedBoxCoordinatesLayout)
            }
            applyButton.setOnClickListener { viewModel.validateInput() }
        }
    }

    override fun listenViewModel(viewModel: InputDataViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.inputError.collect(::onGeneralErrorGot) }

                launch { viewModel.groundTruthLeftError.collect {
                    onGeneralErrorGot(requireBinding().inputGroundTruthBoxCoordinatesLayout.boxLeftContainer, it)
                }}
                launch { viewModel.groundTruthTopError.collect {
                    onGeneralErrorGot(requireBinding().inputGroundTruthBoxCoordinatesLayout.boxTopContainer, it)
                }}
                launch { viewModel.groundTruthRightError.collect {
                    onGeneralErrorGot(requireBinding().inputGroundTruthBoxCoordinatesLayout.boxRightContainer, it)
                }}
                launch { viewModel.groundTruthBottomError.collect {
                    onGeneralErrorGot(requireBinding().inputGroundTruthBoxCoordinatesLayout.boxBottomContainer, it)
                }}
                launch { viewModel.predictedLeftError.collect {
                        onGeneralErrorGot(requireBinding().inputPredictedBoxCoordinatesLayout.boxLeftContainer, it)
                }}
                launch { viewModel.predictedTopError.collect {
                    onGeneralErrorGot(requireBinding().inputPredictedBoxCoordinatesLayout.boxTopContainer, it)
                }}
                launch { viewModel.predictedRightError.collect {
                    onGeneralErrorGot(requireBinding().inputPredictedBoxCoordinatesLayout.boxRightContainer, it)
                }}
                launch { viewModel.predictedBottomError.collect {
                    onGeneralErrorGot(requireBinding().inputPredictedBoxCoordinatesLayout.boxBottomContainer, it)
                }}
                launch { viewModel.validatedInputData.collect(::onValidatedInputDataReceived) }
            }
        }
    }

    private fun onGeneralErrorGot(hasGeneralError: Boolean) {
        requireBinding().applyButton.isEnabled = !hasGeneralError
    }

    private fun initBoundingBoxInput(
        box: BoundingBoxUi,
        boundingBoxInputLayoutBinding: BoundingBoxInputLayoutBinding
    ) {
        if (box != BoundingBoxUi.EMPTY) {
            boundingBoxInputLayoutBinding.run {
                boxLeftInput.setText(box.left.toString())
                boxTopInput.setText(box.top.toString())
                boxRightInput.setText(box.right.toString())
                boxBottomInput.setText(box.bottom.toString())
            }
        } else {
            boundingBoxInputLayoutBinding.run {
                boxLeftInput.setText(String.EMPTY)
                boxTopInput.setText(String.EMPTY)
                boxRightInput.setText(String.EMPTY)
                boxBottomInput.setText(String.EMPTY)
            }
        }
    }

    private fun onGeneralErrorGot(layout: TextInputLayout, exception: CustomException?) {
        layout.error = if (exception != null) mapper.map(exception) else String.EMPTY
    }

    private fun onValidatedInputDataReceived(inputData: InputDataUi?) {
        inputData?.let {
            setFragmentResult(REQUEST_KEY, bundleOf(INPUT_DATA to it))
            hideKeyboard()
            dismiss()
        }
    }

    companion object {

        const val INPUT_DATA = "input_data"
        const val REQUEST_KEY = "request_key"
    }
}