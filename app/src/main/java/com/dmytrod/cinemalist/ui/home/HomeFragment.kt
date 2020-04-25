package com.dmytrod.cinemalist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dmytrod.cinemalist.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private val pageFactory = arrayOf<() -> Fragment>(
        { OngoingMoviesFragment.newInstance() }, { FavoriteMoviesFragment.newInstance() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager.adapter = MoviesFragmentAdapter(pageFactory, this)
    }

    class MoviesFragmentAdapter(
        private val pageFactory: Array<() -> Fragment>,
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = pageFactory.size

        override fun createFragment(position: Int): Fragment = pageFactory[position].invoke()

    }
}