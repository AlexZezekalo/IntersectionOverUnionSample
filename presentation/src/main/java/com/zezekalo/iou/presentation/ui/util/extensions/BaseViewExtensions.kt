package com.zezekalo.iou.presentation.ui.util.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.zezekalo.iou.presentation.ui.base.BaseView
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel
import kotlin.reflect.KFunction

@CheckResult
fun <VB : ViewBinding> BaseView<VB, *>.inflateBinding(
    layoutInflater: LayoutInflater,
    position: Int = 0,
): VB =
    this::class
        .getFunFromGenericClass<VB>(position, true, ::isInflateWithoutParentFun)
        .call(layoutInflater)

@Suppress("UNCHECKED_CAST")
@CheckResult
fun <VB : ViewBinding> BaseView<VB, *>.inflateBinding(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToRoot: Boolean = false
): VB =
    this::class
        .getFunFromGenericClass<VB>(0, true, ::isInflateWithParentFun)
        .call(layoutInflater, parent, attachToRoot) as VB


@CheckResult private fun isInflateWithoutParentFun(function: KFunction<*>): Boolean = function.run {
    parameters.size == 1 &&
            parameters[0].type.classifier == LayoutInflater::class
}

@CheckResult private fun isInflateWithParentFun(function: KFunction<*>): Boolean = function.run {
    parameters.size == 3 &&
            parameters[0].type.classifier == LayoutInflater::class &&
            parameters[1].type.classifier == ViewGroup::class &&
            parameters[2].type.classifier == Boolean::class
}

@CheckResult fun <VM, SO> SO.createViewModel(position: Int = 1): VM
        where VM : BaseViewModel,
              SO : BaseView<*, VM>,
              SO : ViewModelStoreOwner =
    ViewModelProvider(this)[this::class.getGenericClass<VM>(position).java]