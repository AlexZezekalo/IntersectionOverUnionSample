package com.zezekalo.iou.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zezekalo.iou.domain.model.BoundingBox
import com.zezekalo.iou.domain.model.GroundTruthBoundingBox
import com.zezekalo.iou.domain.model.OutputData
import com.zezekalo.iou.domain.model.PredictedBoundingBox
import com.zezekalo.iou.domain.model.UnionBox
import com.zezekalo.iou.presentation.R
import com.zezekalo.iou.presentation.databinding.BoundingBoxTextLayoutBinding
import com.zezekalo.iou.presentation.databinding.FragmentGraphBinding
import com.zezekalo.iou.presentation.model.BoundingBoxUi
import com.zezekalo.iou.presentation.model.InputDataUi
import com.zezekalo.iou.presentation.ui.base.BaseFragment
import com.zezekalo.iou.presentation.ui.dialog.InputDataDialog
import com.zezekalo.iou.presentation.ui.util.extensions.showBoxCoordinate
import com.zezekalo.iou.presentation.ui.util.extensions.showIoU
import com.zezekalo.iou.presentation.ui.util.mapper.ThrowableToErrorMessageMapper
import com.zezekalo.iou.presentation.viewmodel.GraphViewModel
import com.zezekalo.iou.presentation.viewmodel.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GraphFragment : BaseFragment<FragmentGraphBinding, GraphViewModel>() {
    @Inject lateinit var throwableToErrorMessageMapper: ThrowableToErrorMessageMapper

    override fun onViewBound(
        binding: FragmentGraphBinding,
        savedInstanceState: Bundle?,
    ) {
        with(binding) {
            groundTruthBoxCoordinatesLayout.boxTitle.text = getString(R.string.ground_truth_title)
            predictedBoxCoordinatesLayout.boxTitle.text = getString(R.string.predicted_title)
            unionBoxCoordinatesLayout.boxTitle.text = getString(R.string.union_box_title)

            clearButton.setOnClickListener(::clearData)
            inputButton.setOnClickListener(::navigateToInputDataDialog)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun clearData(view: View) {
        viewModel.clearData()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun navigateToInputDataDialog(view: View) {
        val inputData = viewModel.getInputData()
        val action = GraphFragmentDirections.actionGraphFragmentToInputDataDialog(inputData)
        findNavController().navigate(action).also {
            setInputDataResultListener()
        }
    }

    private fun setInputDataResultListener() {
        setFragmentResultListener(InputDataDialog.REQUEST_KEY) { _, bundle ->
            clearFragmentResultListener(InputDataDialog.REQUEST_KEY)
            @Suppress("DEPRECATION")
            val inputData = bundle.getParcelable<InputDataUi>(InputDataDialog.INPUT_DATA)
            inputData?.let { viewModel.setInputData(it) }
        }
    }

    override fun listenViewModel(viewModel: GraphViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiState.collect(::onUiStateChange) }
                launch { viewModel.inputDataUi.collect(::onInputDataChanged) }
            }
        }
    }

    private fun onInputDataChanged(inputData: InputDataUi) {
        onGroundTruthBoundingBoxChanged(inputData.groundTruthBoundingBox)
        onPredictedBoundingBoxChanged(inputData.predictedBoundingBox)
    }

    private fun onGroundTruthBoundingBoxChanged(box: BoundingBoxUi) {
        requireBinding().groundTruthBoxCoordinatesLayout.also { showBoundingBoxCoordinates(box, it) }
    }

    private fun onPredictedBoundingBoxChanged(box: BoundingBoxUi) {
        requireBinding().predictedBoxCoordinatesLayout.also { showBoundingBoxCoordinates(box, it) }
    }

    private fun showBoundingBoxCoordinates(
        box: BoundingBox,
        binding: BoundingBoxTextLayoutBinding,
    ) {
        with(binding) {
            boxLeftText.text = box.left.showBoxCoordinate(getString(R.string.left_hint))
            boxTopText.text = box.top.showBoxCoordinate(getString(R.string.top_hint))
            boxRightText.text = box.right.showBoxCoordinate(getString(R.string.right_hint))
            boxBottomText.text = box.bottom.showBoxCoordinate(getString(R.string.bottom_hint))
        }
    }

    private fun onUiStateChange(uiState: UiState) {
        when (uiState) {
            UiState.Clear -> onOutputDataReceived(null)
            is UiState.Error -> showError(uiState.throwable)
            is UiState.Success -> onOutputDataReceived(uiState.data)
        }
    }

    private fun onOutputDataReceived(outputData: OutputData?) {
        showUnionBox(outputData?.unionBox)
        showIntersectionOverUnion(outputData?.intersectionOverUnion)
        drawBoxes(outputData)
    }

    private fun showUnionBox(unionBox: UnionBox?) {
        requireBinding().unionBoxCoordinatesLayout.also { layout ->
            unionBox?.let { box ->
                showBoundingBoxCoordinates(box, layout)
            }
            layout.root.isVisible = unionBox != null
        }
    }

    private fun showIntersectionOverUnion(iou: Float?) {
        requireBinding().run {
            intersectionOverUnion.isVisible = iou != null
            intersectionOverUnion.text = iou?.showIoU(getString(R.string.intersection_over_union))
        }
    }

    private fun drawBoxes(outputData: OutputData?) {
        requireBinding().run {
            val groundTruthBoundingBox = outputData?.inputData?.groundTruthBoundingBox ?: GroundTruthBoundingBox.INIT
            val predictedBoundingBox = outputData?.inputData?.predictedBoundingBox ?: PredictedBoundingBox.INIT
            customGraphView.setBoxes(
                groundTruthBoundingBox = groundTruthBoundingBox,
                predictedBoundingBox = predictedBoundingBox,
                unionBox = outputData?.unionBox,
            )
        }
    }

    private fun showError(throwable: Throwable) {
        Snackbar.make(requireBinding().root, throwableToErrorMessageMapper.map(throwable), Snackbar.LENGTH_LONG).show()
    }
}
