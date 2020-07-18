package sk.goodreq.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import sk.goodreq.api.UsersService

@Module
@InstallIn(ApplicationComponent::class)
object ServicesModule {

    @Provides
    fun provideUsersService(okHttp: OkHttpClient): UsersService {
        return Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .client(okHttp)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UsersService::class.java)
    }
}