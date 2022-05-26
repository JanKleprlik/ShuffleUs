package com.shuffleus.app.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.shuffleus.app.data.User
import com.shuffleus.app.databinding.UserItemSettingsBinding
import com.shuffleus.app.repository.Repository
import com.shuffleus.app.repository.room.RoomRepository
import kotlinx.coroutines.launch

class UsersAdapter(users: List<User>, private val callbackListener: SettingsFragment): RecyclerView.Adapter<UserViewHolder>(){

    // MVVM
    private var _binding: UserItemSettingsBinding? = null
    private val binding: UserItemSettingsBinding
        get() = _binding!!

    var users = users
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        _binding = UserItemSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UserViewHolder(binding, callbackListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position], this)
    }

    override fun getItemCount(): Int {
        return users.count()
    }
}

class UserViewHolder(private val binding: UserItemSettingsBinding, private val callbackListener: SettingsFragment): RecyclerView.ViewHolder(binding.root){
    private val repository: Repository by lazy { RoomRepository(callbackListener.requireActivity().application) }

    private var txtName: TextView = binding.txtName
    private var txtSurname: TextView = binding.txtSurname
    private var btnIsActive: CheckBox = binding.btnIsActive
    private var btnDelete: Button = binding.btnDelete
    private var imgAvatar: ImageView = binding.imgAvatar

    fun bind(user: User, adapter: UsersAdapter){
        txtName.text = user.name
        txtSurname.text = user.surname
        btnIsActive.isChecked = user.isActive


        btnIsActive.setOnClickListener{
            if (!user.wasChanged){
                user.wasActive = user.isActive
            }
            user.isActive = user.isActive.not()
            user.wasChanged = true

            callbackListener.lifecycleScope.launch{
                repository.updateUser(user)
            }
        }

        btnDelete.setOnClickListener{
            callbackListener.lifecycleScope.launch{
                repository.deleteUser(user)
                adapter.users = repository.getUsers()
            }
            callbackListener.onPlayerDeleted(user)
        }

        Glide.with(binding.root)
            .load("https://picsum.photos/64/64")
            .apply(RequestOptions.circleCropTransform()
                .signature(ObjectKey("${user.name}_${user.surname}")))
            .into(imgAvatar)
    }

}