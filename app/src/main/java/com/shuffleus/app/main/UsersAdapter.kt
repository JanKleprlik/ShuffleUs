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

class UsersAdapter(users: List<User>): RecyclerView.Adapter<UserViewHolder>(){

    var users = users
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_default, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.count()
    }
}

class UserViewHolder(view: View): RecyclerView.ViewHolder(view){

    var view = view
    var txtName: TextView = view.findViewById(R.id.txt_name_def)
    var txtSurname: TextView = view.findViewById(R.id.txt_surname_def)
    var imgAvatar: ImageView = view.findViewById(R.id.img_avatar)

    fun bind(user: User){
        txtName.text = user.name
        txtSurname.text = user.surname
        Glide.with(view)
            .load("https://picsum.photos/64/64")
            .apply(
                RequestOptions.circleCropTransform()
                .signature(ObjectKey("${user.name}_${user.surname}")))
            .into(imgAvatar)
    }

}