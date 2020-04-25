package com.dmytrod.cinemalist.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dmytrod.cinemalist.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_welcome, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginWithFB.setOnClickListener {
            Snackbar.make(view, "Not implemented yet", Snackbar.LENGTH_SHORT)
                .show()
        }
        skip.setOnClickListener {
            findNavController().navigate(R.id.openHome)
        }
    }
}