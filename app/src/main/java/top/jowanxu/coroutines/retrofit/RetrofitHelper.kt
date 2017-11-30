
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private const val TAG = "RetrofitHelper"
    private const val CONTENT_PRE = "OkHttp: "
    private const val CONNECT_TIMEOUT = 60L
    private const val READ_TIMEOUT = 10L

    val retrofitService: RetrofitService =
            RetrofitHelper.getService(Constant.REQUEST_BASE_URL, RetrofitService::class.java)

    /**
     * 创建Retrofit
     */
    private fun create(url: String):Retrofit {
        //新建log拦截器
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            loge(TAG,  CONTENT_PRE + it)
        })
        //日志显示级别
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        // okHttpClientBuilder
        val okHttpClientBuilder = with(OkHttpClient().newBuilder()) {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            //OkHttp进行添加拦截器loggingInterceptor
            addInterceptor(loggingInterceptor)
        }

        return RetrofitBuild(url = url,
                client = okHttpClientBuilder.build(),
                gsonFactory = GsonConverterFactory.create()).retrofit
    }

    /**
     * 获取ServiceApi
     */
    private fun <T> getService(url: String, service: Class<T>): T =
            create(url).create(service)

}

class RetrofitBuild(url: String, client: OkHttpClient, gsonFactory: GsonConverterFactory) {
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(gsonFactory)
            .build()
}