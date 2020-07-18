package sk.goodreq.data

import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import sk.goodreq.api.UsersService
import sk.goodreq.model.User
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

const val PER_PAGE_SIZE = 5

class UsersDataSource constructor(private val usersService: UsersService) : RxPagingSource<Long, User>() {


    override fun loadSingle(params: LoadParams<Long>): Single<LoadResult<Long, User>> {
        return Single.defer {
            val page = params.key ?: 1
            usersService.getUsers(page, params.loadSize)
        }.map<LoadResult<Long, User>> { users ->
            val prev = users.page.takeIf { it > 1 }
            val next = users.page.takeIf { it < users.total_pages }?.plus(1)
            LoadResult.Page(users.data, prev, next)
        }.onErrorReturn {
            LoadResult.Error(it)
        }.subscribeOn(Schedulers.io())
    }

}