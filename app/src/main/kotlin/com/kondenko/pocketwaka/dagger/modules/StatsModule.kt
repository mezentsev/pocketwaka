package com.kondenko.pocketwaka.dagger.modules

import com.kondenko.pocketwaka.dagger.PerScreen
import com.kondenko.pocketwaka.dagger.qualifiers.Api
import com.kondenko.pocketwaka.data.stats.service.StatsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
open class StatsModule {

    @Provides
    @PerScreen
    open fun provideStatsService(@Api retrofit: Retrofit): StatsService {
        return retrofit.create(StatsService::class.java)
    }

}