package com.dmytrod.cinemalist.data.remote.model

import androidx.annotation.StringRes
import com.dmytrod.cinemalist.R

sealed class RemoteError(@StringRes val errorMessageRes: Int) {
    object NoInternet : RemoteError(R.string.no_internet)
    object ServerFailure : RemoteError(R.string.server_error)
    object Unexpected : RemoteError(R.string.something_went_wrong)
    //TODO add ApiError
}