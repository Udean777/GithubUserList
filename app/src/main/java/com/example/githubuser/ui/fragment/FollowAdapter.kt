package com.example.githubuser.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.response.ResponseFollowersItem
import com.example.githubuser.databinding.ListItemBinding

class FollowAdapter(private val followerList: List<ResponseFollowersItem>): RecyclerView.Adapter<FollowAdapter.MyViewHolder>() {

     class MyViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ResponseFollowersItem) {
            binding.usernameTextView.text = "${user.login}"
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.ivPicture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val follower = followerList[position]
        holder.bind(follower)
    }
}