package sk.goodreq.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import sk.goodreq.api.UsersService
import sk.goodreq.data.UsersDataSource

typealias UsersDataSourceFactory = () -> UsersDataSource

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataSourceModule {

    @Provides
    fun provideUsersDataSourceFactory(usersService: UsersService): UsersDataSourceFactory {
        return { UsersDataSource(usersService) }
    }
}