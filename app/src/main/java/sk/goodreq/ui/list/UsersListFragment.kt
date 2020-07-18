package sk.goodreq.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_users_list.*
import sk.goodreq.R
import sk.goodreq.databinding.FragmentUsersListBinding

@AndroidEntryPoint
class UsersListFragment : Fragment() {
    private val usersAdapter = UsersAdapter() {
        findNavController().navigate(UsersListFragmentDirections.actionUsersListFragmentToUserDetailFragment(it as java.lang.Long))
    }
    private val viewModel: UserListViewModel by viewModels()
    private lateinit var binding: FragmentUsersListBinding
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return DataBindingUtil.inflate<FragmentUsersListBinding>(inflater, R.layout.fragment_users_list, container, false).also { binding = it }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usersAdapter.addLoadStateListener {
            it.forEach { _, _, loadState ->
                handleLoadState(loadState)
            }
        }
        binding.usersList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usersAdapter
        }
        disposable = viewModel.users.subscribe {
            usersAdapter.submitData(lifecycle, it)

        }
        binding.swipeToRefresh.setOnRefreshListener {
            usersAdapter.refresh()
        }
    }

    private fun handleLoadState(loadState: LoadState) {
        when (loadState) {
            is LoadState.Error -> {
                showError(loadState.error)
            }
            is LoadState.Loading -> {
                binding.loading.visibility = View.VISIBLE
            }
            is LoadState.NotLoading -> {
                binding.loading.visibility = View.INVISIBLE
                binding.swipeToRefresh.isRefreshing = false
            }
        }
    }

    private fun showError(throwable: Throwable) {
        Log.e("List", "error", throwable)
        Snackbar.make(main, throwable.message ?: "Unknown error.", Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) { usersAdapter.refresh() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }
}