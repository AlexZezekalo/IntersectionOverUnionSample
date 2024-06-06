package com.zezekalo.iou.presentation.ui.util

import androidx.annotation.CheckResult
import androidx.annotation.IntRange
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * Find particular method for generic type [C], at the given genericPosition, by condition,
 * that provided by [conditionForSearchFun].
 *
 * @see getFunFromGenericClassOrNull
 *
 * @param genericPosition generic position in Class<Position0, Position1, ...>
 * @param conditionForSearchFun provide condition for search method
 *
 * @return particular method of type [C]. If method isn't found - throw [KotlinNullPointerException].
 */
@CheckResult fun <C> Class<*>.getFunFromGenericClass(
    @IntRange(from = 0) genericPosition: Int,
    conditionForSearchFun: (Method) -> Boolean,
): Method = getFunFromGenericClassOrNull<C>(genericPosition, conditionForSearchFun)!!

/**
 * Find particular method for generic type [C], at the given genericPosition, by condition,
 * that provided by [conditionForSearchFun].
 *
 * @see getGenericClass
 *
 * @param genericPosition generic position in Class<Position0, Position1, ...>
 * @param conditionForSearchFun provide condition for search method
 *
 * @return particular method of type [C]. If method isn't found - return null.
 */
@CheckResult fun <C> Class<*>.getFunFromGenericClassOrNull(
    @IntRange(from = 0) genericPosition: Int,
    conditionForSearchFun: (Method) -> Boolean,
): Method? =
    getGenericClass<C>(genericPosition)
        .declaredMethods
        .firstOrNull(conditionForSearchFun)
            ?: superclass?.getFunFromGenericClassOrNull<C>(genericPosition, conditionForSearchFun)

/**
 * Get instance of [Class] for generic type [C] at the given genericPosition.
 *
 * @see [getGenericClassOrNull]
 *
 * @param genericPosition generic position in Class<Position0, Position1, ...>
 *
 * @return implementation of T::class.javaClass with the genericPosition. If class isn't found -
 * throw [NoSuchElementException]
 */
@CheckResult
fun <C> Class<*>.getGenericClass(@IntRange(from = 0) genericPosition: Int): Class<C> {
    val result = getGenericClassOrNull<C>(genericPosition)

    if (result == null) {
        val newGenericPosition = genericPosition - 1

        if (newGenericPosition == -1) throw NoSuchElementException()

        return getGenericClass(newGenericPosition)
    }

    return result
}

/**
 * Get instance of [Class] for generic type [C] at the given genericPosition.
 *
 * For example, you can use it to get a ViewModel.
 * ```
 * val provider = ViewModelProvider(this, viewModelFactory)
 * val viewModel = provider.get(javaClass.getGenericClassOrNull(0))
 * ```
 * @param genericPosition generic position in Class<Position0, Position1, ...>
 *
 * @return implementation of T::class.javaClass with the genericPosition. If class isn't found - return null
 */
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <C> Class<*>.getGenericClassOrNull(@IntRange(from = 0) genericPosition: Int): Class<C>? =
    ((genericSuperclass as? ParameterizedType?)
        ?.actualTypeArguments
        ?.getOrNull(genericPosition) as? Class<C>)
            ?: superclass?.getGenericClassOrNull(genericPosition)