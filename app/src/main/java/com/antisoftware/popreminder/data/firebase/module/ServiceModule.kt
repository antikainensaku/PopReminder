package com.antisoftware.popreminder.data.firebase.module

import com.antisoftware.popreminder.data.firebase.AccountService
import com.antisoftware.popreminder.data.firebase.ConfigurationService
import com.antisoftware.popreminder.data.firebase.LogService
import com.antisoftware.popreminder.data.firebase.StorageService
import com.antisoftware.popreminder.data.firebase.implementation.AccountServiceImpl
import com.antisoftware.popreminder.data.firebase.implementation.ConfigurationServiceImpl
import com.antisoftware.popreminder.data.firebase.implementation.LogServiceImpl
import com.antisoftware.popreminder.data.firebase.implementation.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService
}
