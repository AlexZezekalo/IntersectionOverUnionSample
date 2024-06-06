package com.zezekalo.iou.presentation.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

abstract class BaseViewModel : ViewModel(){

    val scope = viewModelScope
}