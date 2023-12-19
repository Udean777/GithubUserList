package com.example.githubuser.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.data.response.ResponseFollowersItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.FragmentFollowBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var username: String
    private lateinit var followAdapter: FollowAdapter

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.listFollow
        recyclerView.layoutManager = LinearLayoutManager(context)

        arguments?.let {
            username = it.getString(ARG_USERNAME, "")
        val position = arguments?.getInt(ARG_POSITION, 0)

        if (position == 1) {
            getFollowers(username)
        } else {
            getFollowing(username)
        }
        }

    }

    private fun listUsers(followersList: List<ResponseFollowersItem>){
        followAdapter = FollowAdapter(followersList)
        binding.listFollow.adapter = followAdapter
    }

    private fun getFollowers(username: String){
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowers(username)

        client.enqueue(object : Callback<List<ResponseFollowersItem>>{
            override fun onResponse(
                call: Call<List<ResponseFollowersItem>>,
                response: Response<List<ResponseFollowersItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val followersList = response.body()
                    followersList?.let {
                        listUsers(it)
                    }
                }else{
                    Log.e("FollowFragment", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseFollowersItem>>, t: Throwable) {
                showLoading(false)
                Log.e("FollowFragment", "Error: ${t.message}")
            }

        })
    }

    private fun getFollowing(username: String){
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowing(username)

        client.enqueue(object : Callback<List<ResponseFollowersItem>>{
            override fun onResponse(
                call: Call<List<ResponseFollowersItem>>,
                response: Response<List<ResponseFollowersItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful){
                    val followingList = response.body()
                    followingList?.let {
                        listUsers(it)
                    }
                }else {
                    Log.e("FollowFragment", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseFollowersItem>>, t: Throwable) {
                showLoading(false)
                Log.e("FollowFragment", "Error: ${t.message}")
            }

        })
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}
