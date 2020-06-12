package com.example.githubproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.githubproject.R
import com.example.githubproject.data.GithubUser
import com.example.githubproject.databinding.ItemGithubBinding
import com.example.githubproject.viewModel.GithubUserViewModel

class UserPagingAdapter(private val viewModel: GithubUserViewModel, itemCallback: DiffUtil.ItemCallback<GithubUser> = object : DiffUtil.ItemCallback<GithubUser>() {
    override fun areItemsTheSame(old: GithubUser, new: GithubUser): Boolean =
        old.id == new.id

    override fun areContentsTheSame(old: GithubUser, new: GithubUser): Boolean =
        old == new
    }): PagedListAdapter<GithubUser, BaseViewHolder>(itemCallback) {

    private val viewHolders: MutableList<BaseViewHolder> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = createBinding(parent, viewType)
        val viewHolder = BaseViewHolder(binding)
        binding.lifecycleOwner = viewHolder
        viewHolder.markCreated()
        viewHolders.add(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (position < itemCount) {
            bind(holder.binding, position)
            holder.binding.executePendingBindings()
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }

    private fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getBindingReference(parent, viewType),
            parent,
            false
        )
    }

    fun setLifecycleDestroyed() {
        viewHolders.forEach {
            it.markDestroyed()
        }
    }

    fun getBindingReference(parent: ViewGroup, viewType: Int): Int {
        return R.layout.item_github
    }

    fun bind(binding: ViewDataBinding, position: Int) {
        val data = getItem(position)
        when(binding){
            is ItemGithubBinding -> {
                binding.viewModel = viewModel
                binding.user = data
            }
        }
    }
}