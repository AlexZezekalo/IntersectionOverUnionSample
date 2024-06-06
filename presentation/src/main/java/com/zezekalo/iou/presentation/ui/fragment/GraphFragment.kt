package com.zezekalo.iou.presentation.ui.fragment

import android.os.Bundle
import com.zezekalo.iou.presentation.R
import com.zezekalo.iou.presentation.databinding.FragmentGraphBinding
import com.zezekalo.iou.presentation.ui.base.BaseFragment
import com.zezekalo.iou.presentation.viewmodel.GraphViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraphFragment : BaseFragment<FragmentGraphBinding, GraphViewModel>() {

    override fun onViewBound(binding: FragmentGraphBinding, savedInstanceState: Bundle?) {
        with(binding) {
            groundTruthBoxCoordinatesLayout.boxTitle.text = getString(R.string.ground_truth_title)
            predictedBoxCoordinatesLayout.boxTitle.text = getString(R.string.predicted_title)
            unionBoxCoordinatesLayout.boxTitle.text = getString(R.string.union_box_title)
        }
    }

    override fun listenViewModel(viewModel: GraphViewModel) {

    }
}