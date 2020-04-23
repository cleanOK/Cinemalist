package com.dmytrod.cinemalist.ui

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData

class DataBindingAdapters {
    companion object {

        @JvmStatic
        @BindingAdapter("hide")
        fun View.hide(result: LiveData<Boolean>) {
            visibility = if (result.value == true) View.GONE else View.VISIBLE
        }

        @JvmStatic
        @BindingAdapter("show")
        fun View.show(result: LiveData<Boolean>) {
            visibility = if (result.value == true) View.VISIBLE else View.GONE
        }

    }
}
