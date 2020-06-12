package com.example.githubproject.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubproject.R


class BaseViewHolder constructor(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root),
    LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private var wasPaused: Boolean = false

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    fun markCreated() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun markAttach() {
        if (wasPaused) {
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
            wasPaused = false
        } else {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }

    fun markDetach() {
        wasPaused = true
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun markDestroyed() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}

@BindingAdapter("imageUrl")
fun applyImage(view: ImageView, avatarUrl: String?) {
    if (!avatarUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(avatarUrl)
            .placeholder(R.drawable.ic_circle_gray_r180_bg)
            .apply(RequestOptions().circleCrop())
            .into(view)
    }
}