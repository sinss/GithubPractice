package com.example.githubproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.githubproject.adapter.UserPagingAdapter
import com.example.githubproject.databinding.ActivityMainBinding
import com.example.githubproject.manager.NetworkState
import com.example.githubproject.manager.Status
import com.example.githubproject.viewModel.GithubUserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val listViewModel: GithubUserViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        val adapter = UserPagingAdapter(listViewModel)
        binding.listView.adapter = adapter
        listViewModel.pagedList.observe(this, Observer {
            adapter.submitList(it)
        })

        listViewModel.initLoadState.observe(this, Observer { state ->
            binding.loadingView.visibility =
                if (state == NetworkState.LOADING) View.VISIBLE else View.GONE
        })

        listViewModel.networkState.observe(this, Observer { state ->
            if (state.status == Status.FAILED) showToast(state.msg)
        })

        listViewModel.focusGithubUser.observe(this, Observer { data ->
            //TODO: Display user detail page
        })

        listViewModel.getGitHubUsers()
    }

    private fun showToast(msg: String?) {
        Toast.makeText(this, msg ?: "", Toast.LENGTH_LONG).show()
    }
}