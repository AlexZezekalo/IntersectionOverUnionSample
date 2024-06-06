package com.zezekalo.iou.presentation.ui.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.zezekalo.iou.presentation.ui.base.BaseView
import com.zezekalo.iou.presentation.viewmodel.base.BaseViewModel
import java.lang.reflect.Method
import java.lang.reflect.Modifier

@Suppress("UNCHECKED_CAST")
@CheckResult
fun <VB : ViewBinding> BaseView<VB, *>.inflateBinding(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToRoot: Boolean = false
): VB =
    javaClass
        .getFunFromGenericClass<VB>(0, ::isInflateWithParentFun)
        .invoke(null, layoutInflater, parent, attachToRoot) as VB


private fun isInflateWithParentFun(method: Method): Boolean =
    method.run {
        Modifier.isStatic(modifiers) &&
                parameterTypes.size == 3 &&
                parameterTypes[0] == LayoutInflater::class.java &&
                parameterTypes[1] == ViewGroup::class.java &&
                parameterTypes[2] == Boolean::class.java
    }

@CheckResult fun <VM, SO> SO.createViewModel(position: Int = 1): VM
        where VM : BaseViewModel,
              SO : BaseView<*, VM>,
              SO : ViewModelStoreOwner =
    ViewModelProvider(this)[this::class.java.getGenericClass<VM>(position)]