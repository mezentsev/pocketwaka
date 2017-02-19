package com.kondenko.pocketwaka.api.services

import com.kondenko.pocketwaka.Const
import com.kondenko.pocketwaka.api.model.stats.Stats
import com.kondenko.pocketwaka.api.model.stats.DataWrapper
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import rx.Observable

interface StatsService {

    @GET("users/current/stats/{range}")
    fun getCurrentUserStats(
            @Header(Const.HEADER_BEARER_NAME) tokenHeaderValue: String,
            @Path("range") range: String
    ): Observable<DataWrapper>

}