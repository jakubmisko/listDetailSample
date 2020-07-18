package sk.goodreq.ui.list

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava2.flowable
import sk.goodreq.data.PER_PAGE_SIZE
import sk.goodreq.di.UsersDataSourceFactory

class UserListViewModel @ViewModelInject constructor(
    private val dataSourceFactory: UsersDataSourceFactory,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val users =  createStream()

    private fun createStream() =
        Pager(
            config = PagingConfig(
                pageSize = PER_PAGE_SIZE,
                initialLoadSize = PER_PAGE_SIZE
            ),
            pagingSourceFactory = dataSourceFactory

        ).flowable
}