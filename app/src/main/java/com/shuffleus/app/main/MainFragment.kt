package com.shuffleus.app.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shuffleus.app.R

class MainFragment: Fragment() {

    /**
     * Inflate view hierarchy to this fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    companion object {
        /**
         * Factory method to create fragment instance. Framework requires empty default constructor.
         */
        @JvmStatic
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}