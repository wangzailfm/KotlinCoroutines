
import android.util.Log
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine


fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

suspend fun <T> asyncRequestSuspend(block: (Continuation<T>) -> Unit) = suspendCoroutine<T> {
    block(it)
}
