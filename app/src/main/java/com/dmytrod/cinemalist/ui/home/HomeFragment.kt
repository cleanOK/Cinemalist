package com.dmytrod.cinemalist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dmytrod.cinemalist.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private val pageFactory = Page.values()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = MoviesFragmentAdapter(pageFactory, this)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(pageFactory[position].pageName)
        }.attach()
    }

    private enum class Page(@StringRes val pageName: Int, val createInstance: () -> Fragment) {
        ONGOING(R.string.ongoing_movies_title, { OngoingMoviesFragment.newInstance() }),
        FAVORITE(R.string.favorite_movies_title, { FavoriteMoviesFragment.newInstance() })
    }

    private class MoviesFragmentAdapter(private val pageFactory: Array<Page>, fragment: Fragment) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = pageFactory.size

        override fun createFragment(position: Int): Fragment =
            pageFactory[position].createInstance.invoke()
    }
}