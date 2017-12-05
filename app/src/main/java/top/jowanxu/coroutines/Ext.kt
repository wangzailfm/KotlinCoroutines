
import android.util.Log
import kotlinx.coroutines.experimental.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.coroutines.bean.LoginResponse
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine


fun loge(tag: String, content: String?) {
    Log.e(tag, content ?: tag)
}

fun getUserLogin() = RetrofitHelper
        .retrofitService
        .userLogin("", "")

suspend fun <T> requestDataSuspend(block: (Continuation<T>) -> Unit) = suspendCoroutine<T> {
    block(it)
}

fun get() {
    val async = async {
        requestDataSuspend<Call<LoginResponse>> { cont ->
            getUserLogin().enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    cont.resume(call)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    cont.resumeWithException(t)
                }
            })
        }
    }
}