package top.jowanxu.coroutines

import RetrofitHelper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import asyncRequestSuspend
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.jowanxu.coroutines.bean.LoginResponse

class MainActivity : AppCompatActivity() {
    companion object {
        private const val USERNAME = "wangzailll"
        private const val PASSWORD = "wangzailll"
        private const val RESULT_NULL = "result null!"
    }

    /**
     * Login Call
     */
    private var userLogin: Call<LoginResponse>? = null

    /**
     * async return coroutine result
     */
    private var async: Deferred<Any>? = null

    /**
     * get Login Call
     */
    private fun getUserLogin() = RetrofitHelper
            .retrofitService
            .userLogin(USERNAME, PASSWORD)

    /**
     * OnClickListener
     */
    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.fab -> async(UI) {
                async?.apply {
                    if (isActive) {
                        cancelAndJoin()
                    }
                }
                async = async(CommonPool) {
                    // Delay two second，Simulate multiple request data
                    delay(2000)
                    try {
                        // Request
                        userLogin.run {
                            // If Call not empty
                            this?.run {
                                if (!isCanceled) {
                                    // cancel request
                                    cancel()
                                }
                                // Assignment
                                getUserLogin()
                            } ?: run {
                                // If Call empty
                                getUserLogin()
                            }
                        }.execute().body()
                    } catch (e: Throwable) {
                        // Return Throwable toString
                        e.toString()
                    }
                }
                // Get async result
                val await = async?.await()
                // Set TextView content
                text.apply {
                    text = when (await) {
                        is String -> await
                        is LoginResponse -> await.result.toString()
                        else -> RESULT_NULL
                    }
                }
            }
        }
    }

    /**
     * 异步
     */
    fun asyncRequestMethod() {
        val async = async {
            asyncRequestSuspend<Call<LoginResponse>> { cont ->
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener(onClickListener)
    }

}
