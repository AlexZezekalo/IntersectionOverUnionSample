package com.zezekalo.iou.presentation.ui.util.extensions

import androidx.annotation.CheckResult
import androidx.annotation.IntRange
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.staticFunctions
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.isAccessible

/**
 * Find particular method for generic type [C], at the given genericPosition, by condition,
 * that provided by [conditionForSearchFun].
 *
 * @see getFunFromGenericClassOrNull
 *
 * @param genericPosition generic position in KClass<Position0, Position1, ...>
 * @param staticFun false search static and non static functions / true search only static functions
 * @param conditionForSearchFun provide condition for search method
 *
 * @return particular method of type [C].
 * If method isn't found - throw [KotlinNullPointerException].
 */
@CheckResult fun <C : Any> KClass<*>.getFunFromGenericClass(
    @IntRange(from = 0) genericPosition: Int,
    staticFun: Boolean = true,
    conditionForSearchFun: (KFunction<*>) -> Boolean,
): KFunction<C> =
    getFunFromGenericClassOrNull(genericPosition, staticFun, conditionForSearchFun)
        ?: throw NoSuchElementException()

/**
 * Find particular method for generic type [C], at the given genericPosition, by condition,
 * that provided by [conditionForSearchFun].
 *
 * @see getGenericClass
 *
 * @param genericPosition generic position in KClass<Position0, Position1, ...>
 * @param staticFun false search static and non static functions / true search only static functions
 * @param conditionForSearchFun provide condition for search method
 *
 * @return particular method of type [C]. If method isn't found - return null.
 */
@Suppress("UNCHECKED_CAST")
@CheckResult
fun <C : Any> KClass<*>.getFunFromGenericClassOrNull(
    @IntRange(from = 0) genericPosition: Int,
    staticFun: Boolean = true,
    conditionForSearchFun: (KFunction<*>) -> Boolean,
): KFunction<C>? =
    getGenericClass<C>(genericPosition)
        .let { if (staticFun) it.staticFunctions else it.functions }
        .firstOrNull(conditionForSearchFun) as KFunction<C>?
        ?: superclasses.asSequence().mapNotNull {
            it.getFunFromGenericClassOrNull<C>(
                genericPosition = genericPosition,
                staticFun = staticFun,
                conditionForSearchFun = conditionForSearchFun,
            )
        }.firstOrNull()

/**
 * Get instance of [KClass] for generic type [C] at the given genericPosition.
 *
 * @see [getGenericClassOrNull]
 *
 * @param genericPosition generic position in KClass<Position0, Position1, ...>
 *
 * @return implementation of T::class with the genericPosition. If class isn't found -
 * throw [NoSuchElementException]
 */
@CheckResult
fun <C : Any> KClass<*>.getGenericClass(@IntRange(from = 0) genericPosition: Int): KClass<C> {
    val result = getGenericClassOrNull<C>(genericPosition)

    if (result == null) {
        val newGenericPosition = genericPosition - 1

        if (newGenericPosition == -1) throw NoSuchElementException()

        return getGenericClass(newGenericPosition)
    }

    return result
}

/**
 * Get instance of [KClass] for generic type [C] at the given genericPosition.
 *
 * For example, you can use it to get a ViewModel.
 * ```
 * val provider = ViewModelProvider(this, viewModelFactory)
 * val viewModel = provider.get(this::class.getGenericClassOrNull(0).java)
 * ```
 * @param genericPosition generic position in KClass<Position0, Position1, ...>
 *
 * @return implementation of T::class with the genericPosition. If class isn't found - return null.
 */
@CheckResult
@Suppress("UNCHECKED_CAST")
fun <C : Any> KClass<*>.getGenericClassOrNull(
    @IntRange(from = 0) genericPosition: Int,
): KClass<C>? =
    supertypes
        .firstOrNull()
        ?.arguments
        ?.getOrNull(genericPosition)
        ?.type
        ?.classifier as? KClass<C>
        ?: superclasses
            .asSequence()
            .mapNotNull { it.getGenericClassOrNull<C>(genericPosition) }
            .firstOrNull()

@CheckResult
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> KClass<out Any>.getFirstPropertyByType(
    accessToPrivate: Boolean = false,
): KProperty1<Any, T?> {
    val property = this.memberProperties.first { it.returnType.classifier == T::class }
    property.isAccessible = accessToPrivate
    return property as KProperty1<Any, T?>
}

@CheckResult
inline fun <reified T : Any> KClass<out Any>.getFirstMutablePropertyByType(
    accessToPrivate: Boolean = false,
): KMutableProperty1<Any, T?> =
    this.getFirstPropertyByType<T>(accessToPrivate) as KMutableProperty1<Any, T?>

@CheckResult
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> KClass<out Any>.getFirstPropertyByTypeNonNull(
    accessToPrivate: Boolean = false,
): KProperty1<Any, T> = this.getFirstPropertyByType<T>(accessToPrivate) as KProperty1<Any, T>

@CheckResult
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> KClass<out Any>.getFirstMutablePropertyByTypeNonNull(
    accessToPrivate: Boolean = false,
): KMutableProperty1<Any, T> =
    this.getFirstPropertyByType<T>(accessToPrivate) as KMutableProperty1<Any, T>
