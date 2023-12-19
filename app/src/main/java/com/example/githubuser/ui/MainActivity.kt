package com.example.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.response.ResponseSearch
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var searchInput: String = "Arif"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.listUsers.layoutManager = layoutManager
        val items = DividerItemDecoration(this, layoutManager.orientation)
        binding.listUsers.addItemDecoration(items)

        with(binding){
            searchView.setupWithSearchBar(searchBar)

            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchInput = searchView.text.toString()
                findUsers()
                searchView.hide()
                false
            }
        }
        findUsers()
    }

    private fun findUsers(){
        showLoading(true)

        val client = ApiConfig.getApiService().searchUsers(searchInput)

        client.enqueue(object : Callback<ResponseSearch>{
            override fun onResponse(
                call: Call<ResponseSearch>,
                response: Response<ResponseSearch>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val resBody = response.body()
                    if (resBody != null) {
                        setUsersData(resBody.items)
                    } else {
                        Log.e("MainActivity", "onResponse: Response body is null")
                    }
                } else {
                    Log.e("MainActivity", "onResponse error: ${response.code()}")
                }
            }


            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                showLoading(false)
                Log.e("MainActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun setUsersData(userData: List<ItemsItem>){
        val adapter = UsersAdapter {user ->
            navigateToDetailActivity(user.login)
        }
        adapter.submitList(userData)
        binding.listUsers.adapter = adapter
    }

    private fun navigateToDetailActivity(username: String){
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}