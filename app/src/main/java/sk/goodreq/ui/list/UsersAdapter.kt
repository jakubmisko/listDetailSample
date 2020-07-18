package sk.goodreq.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sk.goodreq.R
import sk.goodreq.databinding.RowUserBinding
import sk.goodreq.model.User

typealias OnUserClick = (Long) -> Unit

class UsersAdapter(private val onClick: OnUserClick) : PagingDataAdapter<User, UserViewHolder>(UserDiff) {
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { user ->
            holder.bind(user, onClick)
        } ?: holder.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent)
    }

}

class UserViewHolder(
    parent: ViewGroup,
    private val binding: RowUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_user, parent, false)
) : RecyclerView.ViewHolder(binding.root) {
    private val emptyUser = User(-1L, "", "", "", "")

    fun bind(user: User, onClick: OnUserClick) {
        binding.user = user

        Glide.with(itemView.context)
            .load(user.avatar)
            .placeholder(R.drawable.ic_baseline_person_24)
            .thumbnail()
            .circleCrop()
            .into(binding.avatar)

        binding.root.setOnClickListener {
            onClick(user.id)
        }
    }

    fun clear() {
        bind(emptyUser, {})
    }
}

object UserDiff : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
}
