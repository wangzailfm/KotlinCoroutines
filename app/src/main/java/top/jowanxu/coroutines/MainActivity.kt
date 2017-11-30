package top.jowanxu.coroutines

import RetrofitHelper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import retrofit2.Call
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
                            // If Call empty
                            if (this == null){
                                getUserLogin()
                            } else {
                                // If Call not empty
                                if (!isCanceled) {
                                    // cancel request
                                    NonCancellable.cancel()
                                }
                                // Assignment
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
                text.text = when (await) {
                    is String -> await
                    is LoginResponse -> await.result.toString()
                    else -> RESULT_NULL
                }
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
