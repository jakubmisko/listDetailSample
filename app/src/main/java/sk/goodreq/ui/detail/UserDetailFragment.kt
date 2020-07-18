package sk.goodreq.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_detail.*
import sk.goodreq.R
import sk.goodreq.databinding.FragmentUserDetailBinding
import sk.goodreq.databinding.FragmentUsersListBinding
import sk.goodreq.model.User

@AndroidEntryPoint
class UserDetailFragment : Fragment() {
    private lateinit var binding: FragmentUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()
    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentUserDetailBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            handleState(it)
        })
        viewModel.loadUser(args.userId.toLong())
        binding.refresh.setOnClickListener {
            viewModel.loadUser(args.userId.toLong())
        }
    }

    private fun handleState(state: UserDetailState) {
        when (state) {
            UserDetailState.Loading -> showLoading()
            is UserDetailState.Success -> renderUser(state)
            is UserDetailState.Error -> showError(state)

        }
    }

    private fun showError(state: UserDetailState.Error) {
        binding.error.text = state.throwable.message
        binding.loading.visibility = View.INVISIBLE
        binding.emailLogo.visibility = View.INVISIBLE
        binding.avatar.setImageResource(R.drawable.ic_baseline_person_24)
    }

    private fun renderUser(state: UserDetailState.Success) {
        binding.user = state.user
        binding.emailLogo.visibility = View.VISIBLE
        binding.loading.visibility = View.INVISIBLE

        Glide.with(this)
            .load(state.user.avatar)
            .fallback(R.drawable.ic_baseline_person_24)
            .circleCrop()
            .into(binding.avatar)
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.error.text = ""
        binding.emailLogo.visibility = View.INVISIBLE
    }

    sealed class UserDetailState {
        object Loading : UserDetailState()
        data class Success(val user: User) : UserDetailState()
        data class Error(val throwable: Throwable) : UserDetailState()
    }
}