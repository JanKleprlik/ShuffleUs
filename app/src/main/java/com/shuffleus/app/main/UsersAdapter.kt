package com.shuffleus.app.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.shuffleus.app.R
import com.shuffleus.app.data.User
import com.shuffleus.app.databinding.LectureItemBinding
import com.shuffleus.app.databinding.UserItemDefaultBinding
import com.shuffleus.app.schedule.LectureViewHolder
import com.shuffleus.app.utils.inflate

class UsersAdapter(): RecyclerView.Adapter<UserViewHolder>(){

    // MVVM
    private var _binding: UserItemDefaultBinding? = null
    private val binding: UserItemDefaultBinding
        get() = _binding!!

    var users = emptyList<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        _binding = UserItemDefaultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.count()
    }
}

class UserViewHolder(private val binding: UserItemDefaultBinding): RecyclerView.ViewHolder(binding.root){

    var txtName: TextView = binding.txtNameDef
    var txtSurname: TextView = binding.txtSurnameDef
    var imgAvatar: ImageView = binding.imgAvatar

    fun bind(user: User){
        txtName.text = user.name
        txtSurname.text = user.surname
        Glide.with(binding.root)
            .load("https://picsum.photos/64/64")
            .apply(
                RequestOptions.circleCropTransform()
                .signature(ObjectKey("${user.name}_${user.surname}")))
            .into(imgAvatar)
    }

}