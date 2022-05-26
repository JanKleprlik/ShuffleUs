package com.shuffleus.app.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.shuffleus.app.R
import com.shuffleus.app.data.User
import com.shuffleus.app.databinding.FragmentAddPlayerBinding
import com.shuffleus.app.utils.AddPlayerCallbackListener


class AddPlayerFragment(private val callbackListener: AddPlayerCallbackListener) : DialogFragment() {

    // MVVM
    private var _binding: FragmentAddPlayerBinding? = null
    private val binding: FragmentAddPlayerBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlayerBinding.inflate(inflater, container, false)
        return binding.root
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
        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener{dismiss()}
        toolbar.title = getString(R.string.add_new_player_info)

        val nameText = binding.editTxtNewName
        val surnameText = binding.editTxtNewSurname

        val addBtn = binding.btnConfirmAddPlayer

        addBtn.setOnClickListener{
            val user = User(nameText.text.toString(), surnameText.text.toString(), true, wasChanged = true, wasActive = false)
            callbackListener.onPlayerAdded(user)
            dismiss()
        }
    }
}