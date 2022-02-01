package com.shuffleus.app.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.shuffleus.app.R
import com.shuffleus.app.data.User
import com.shuffleus.app.utils.AddPlayerCallbackListener


class AddPlayerFragment(private val callbackListener: AddPlayerCallbackListener) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_player, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener{dismiss()}
        toolbar.title = getString(R.string.add_new_player_info)

        val nameText = view.findViewById<EditText>(R.id.edit_txt_new_name)
        val surnameText = view.findViewById<EditText>(R.id.edit_txt_new_surname)

        val addBtn = view.findViewById<Button>(R.id.btn_confirm_add_player)

        addBtn.setOnClickListener{
            val user = User(nameText.text.toString(), surnameText.text.toString(), true, wasChanged = true, wasActive = false)
            callbackListener.onPlayerAdded(user)
            dismiss()
        }
    }
}