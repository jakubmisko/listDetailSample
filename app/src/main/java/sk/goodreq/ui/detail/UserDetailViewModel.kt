package sk.goodreq.ui.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import sk.goodreq.api.UsersService
import java.util.concurrent.TimeUnit

class UserDetailViewModel @ViewModelInject constructor(
    private val usersService: UsersService,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableLiveData<UserDetailFragment.UserDetailState>()
    val state: LiveData<UserDetailFragment.UserDetailState> = _state
    private var disposable: Disposable? = null

    fun loadUser(userId: Long) {
        disposable?.dispose()
        disposable = usersService.getUser(userId)
            .map<UserDetailFragment.UserDetailState> {
                UserDetailFragment.UserDetailState.Success(it.data)
            }.onErrorReturn {
                UserDetailFragment.UserDetailState.Error(it)
            }.doOnSubscribe {
                _state.postValue(UserDetailFragment.UserDetailState.Loading)
            }
            .subscribeOn(Schedulers.io())
            .subscribe { state ->
                _state.postValue(state)
            }
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}