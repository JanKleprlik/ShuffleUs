package com.shuffleus.app.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.shuffleus.app.R
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository
import com.shuffleus.app.repository.room.RoomRepository
import com.shuffleus.app.utils.RemovePlayerCallbackListener

class UsersAdapter(users: List<User>, private val callbackListener: RemovePlayerCallbackListener): RecyclerView.Adapter<UserViewHolder>(){

    var users = users
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_settings, parent, false)

        return UserViewHolder(view, callbackListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position], this)
    }

    override fun getItemCount(): Int {
        return users.count()
    }
}

class UserViewHolder(private val view: View, private val callbackListener: RemovePlayerCallbackListener): RecyclerView.ViewHolder(view){
    private val repository: Repository by lazy { RoomRepository(view.context) }

    var txtName: TextView = view.findViewById(R.id.txt_name)
    var txtSurname: TextView = view.findViewById(R.id.txt_surname)
    var btnIsActive: CheckBox = view.findViewById(R.id.btn_isActive)
    var btnDelete: Button = view.findViewById(R.id.btn_delete)
    var imgAvatar: ImageView = view.findViewById(R.id.img_avatar)

    fun bind(user: User, adapter: UsersAdapter){
        txtName.text = user.name
        txtSurname.text = user.surname
        btnIsActive.isChecked = user.isActive


        btnIsActive.setOnClickListener{
            if (user.wasChanged == false){
                user.wasActive = user.isActive
            }
            user.isActive = user.isActive.not()
            user.wasChanged = true
            repository.updateUser(user)
        }

        btnDelete.setOnClickListener{
            repository.deleteUser(user)
            adapter.users = repository.getUsers()
            callbackListener.onPlayerDeleted(user);
        }

        Glide.with(view)
            .load("https://picsum.photos/64/64")
            .apply(RequestOptions.circleCropTransform()
                .signature(ObjectKey("${user.name}_${user.surname}")))
            .into(imgAvatar)
    }

}