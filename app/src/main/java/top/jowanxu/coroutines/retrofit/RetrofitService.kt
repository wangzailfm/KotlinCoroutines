
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import top.jowanxu.coroutines.bean.LoginResponse

/**
 * Retrofit请求api
 */
interface RetrofitService {

    /**
     * 登录
     * key 用户申请的appkey
     * username 用户名
     * password 用户密码
     */
    @GET("login")
    fun userLogin(
            @Query("username") username: String,
            @Query("password") password: String,
            @Query("key") key: String = Constant.KEY
    ): Call<LoginResponse>

}