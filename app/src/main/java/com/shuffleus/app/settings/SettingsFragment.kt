package com.shuffleus.app.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shuffleus.app.AppSettings
import com.shuffleus.app.R
import com.shuffleus.app.data.User
import com.shuffleus.app.databinding.FragmentSettingsBinding
import com.shuffleus.app.utils.AddPlayerCallbackListener
import com.shuffleus.app.utils.ViewModelResponseState
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(), AddPlayerCallbackListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.btnAddPlayer.setOnClickListener {
            AddPlayerFragment(this).show(childFragmentManager, "addPlayerDialog")
        }

        return binding.root
    }

    override fun onPlayerAdded(newUser:User){
        settingsViewModel.addUser(newUser)
    }

    override fun onStart() {
        super.onStart()

        settingsViewModel.getUsers().observe(viewLifecycleOwner){
            when(it){
                ViewModelResponseState.Idle -> doNothing()
                ViewModelResponseState.Loading -> doNothing()
                is ViewModelResponseState.Error -> doNothing()
                is ViewModelResponseState.Success -> handleUsers(it.content)
            }
        }

        lifecycleScope.launch{
            preparePickers()
        }
        settingsViewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun doNothing(){}

    private fun handleUsers(users: List<User>) {
        val adapter = UsersAdapter(users)
        binding.rvAllUsers.adapter = adapter
        binding.rvAllUsers.layoutManager = LinearLayoutManager(requireContext())
    }

    private suspend fun preparePickers(){
        val groupSizePicker = view?.findViewById<NumberPicker>(R.id.num_group_size)
        groupSizePicker?.minValue = 2
        groupSizePicker?.maxValue = settingsViewModel.getNumberOfActiveUsers()
        groupSizePicker?.wrapSelectorWheel = false
        groupSizePicker?.value = settingsViewModel.getGroupSize()
        groupSizePicker?.setOnValueChangedListener { picker, oldVal, newVal ->
            run {
                settingsViewModel.updateGroupSize(newVal)
            }
        }


        val groupNamePicker = view?.findViewById<NumberPicker>(R.id.num_group_names)
        val nameTypes = settingsViewModel.getNameTypes()
        groupNamePicker?.minValue = 1
        groupNamePicker?.maxValue = nameTypes.size
        groupNamePicker?.displayedValues = nameTypes.toTypedArray()
        groupNamePicker?.value = settingsViewModel.getGroupNamesIdx()
        groupNamePicker?.setOnValueChangedListener { picker, oldVal, newVal ->
            run {
                settingsViewModel.updateGroupsName(newVal)
            }
        }
    }

    companion object {
        /**
         * Factory method to create fragment instance. Framework requires empty default constructor.
         */
        @JvmStatic
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}