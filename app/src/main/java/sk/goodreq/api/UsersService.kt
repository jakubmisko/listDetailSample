package sk.goodreq.api

import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sk.goodreq.model.SingleUser
import sk.goodreq.model.User
import sk.goodreq.model.UsersPage

interface UsersService {

    @GET("users")
    fun getUsers(@Query("page") page: Long, @Query("per_page") perPage: Int): Single<UsersPage>

    @GET("users")
    fun getUser(@Query("id") id: Long): Single<SingleUser>
}