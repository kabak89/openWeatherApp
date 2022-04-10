package com.example.mvvm

/**
 * Предполагается использовать как часть ViewState.
 */
sealed class LoadingState<out E, out T> {
    object None : LoadingState<Nothing, Nothing>()
    object Loading : LoadingState<Nothing, Nothing>()
    data class Error<out E>(val error: E) : LoadingState<E, Nothing>()
    data class Success<out T>(val data: T) : LoadingState<Nothing, T>()

    companion object
}

fun LoadingState.Companion.Error() = LoadingState.Error(Unit)
fun LoadingState.Companion.Success() = LoadingState.Success(Unit)

fun LoadingState.Companion.fromLoading(isLoading: Boolean): LoadingState<Nothing, Unit> =
    if (isLoading) LoadingState.Loading else LoadingState.Success()

val LoadingState<*, *>.isLoading get() = this == LoadingState.Loading
val LoadingState<*, *>.isError get() = this is LoadingState.Error
val LoadingState<*, *>.isSuccess get() = this is LoadingState.Success

val <T> LoadingState<*, T>.dataOrNull get(): T? = (this as? LoadingState.Success)?.data

fun List<LoadingState<*, *>>.commonState(): LoadingState<Unit, Unit> = when {
    any { it is LoadingState.Error } -> LoadingState.Error()
    all { it is LoadingState.Success } -> LoadingState.Success()
    any { it is LoadingState.Loading } -> LoadingState.Loading
    else -> LoadingState.None
}

fun <E> List<LoadingState<E, *>>.commonStateWithError(): LoadingState<E, Unit> {
    val firstErrorState: LoadingState.Error<E>? =
        filterIsInstance<LoadingState.Error<E>>().firstOrNull()

    return when {
        firstErrorState != null -> LoadingState.Error(firstErrorState.error)
        all { it is LoadingState.Success } -> LoadingState.Success()
        any { it is LoadingState.Loading } -> LoadingState.Loading
        else -> LoadingState.None
    }
}