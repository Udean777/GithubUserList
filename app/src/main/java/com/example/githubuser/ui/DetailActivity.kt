package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.ResponseDetail
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.ui.fragment.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var username: String = ""

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        username = intent.getStringExtra("username") ?: ""
        if (username.isNotEmpty()){
            getUsersDetail(username)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 2f
    }

    private fun getUsersDetail(username: String){
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUsers(username)

        client.enqueue(object: Callback<ResponseDetail>{
            override fun onResponse(
                call: Call<ResponseDetail>,
                response: Response<ResponseDetail>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val userdetail = response.body()
                    if (userdetail != null){
                        displayUsersDetails(userdetail)
                    }else{
                        Log.e("DetailActivity", "${response.message()}")
                    }
                }else{
                    Log.e("DetailActivity", "${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                showLoading(false)
                Log.e("DetailActivity", "${t.message}")
            }
        })
    }

    private fun displayUsersDetails(userDetail: ResponseDetail){
        with(binding){
            tvUsername.text = userDetail.login
            tvUsernameItalic.text = userDetail.login
            Glide.with(this@DetailActivity)
                .load(userDetail.avatarUrl)
                .circleCrop()
                .into(profilePicture)
            tvFollowers.text = "${userDetail.followers} Followers"
            tvFollowing.text = "${userDetail.following} Following"
        }
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}