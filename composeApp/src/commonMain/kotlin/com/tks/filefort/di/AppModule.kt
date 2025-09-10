package com.tks.filefort.di

import com.tks.filefort.data.repository.FileRepositoryImpl
import com.tks.filefort.data.repository.UserRepositoryImpl
import com.tks.filefort.domain.repository.FileRepository
import com.tks.filefort.domain.repository.UserRepository
import com.tks.filefort.domain.usecase.CleanupJunkFilesUseCase
import com.tks.filefort.domain.usecase.GetFileTypeInfoUseCase
import com.tks.filefort.domain.usecase.GetFilesByTypeUseCase
import com.tks.filefort.domain.usecase.GetStorageInfoUseCase
import com.tks.filefort.domain.usecase.LoginUseCase
import com.tks.filefort.domain.usecase.LogoutUseCase
import com.tks.filefort.domain.usecase.SearchFilesUseCase
import com.tks.filefort.domain.usecase.SignUpUseCase
import com.tks.filefort.domain.usecase.ToggleFavoriteUseCase
import com.tks.filefort.domain.usecase.ToggleSafeFolderUseCase
import com.tks.filefort.presentation.files.FileListScreenModel
import com.tks.filefort.presentation.home.HomeScreenModel
import com.tks.filefort.presentation.storage.StorageInfoScreenModel
import org.koin.dsl.module

val appModule = module {

    single<FileRepository> { FileRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl() }
    // Use Cases

    single { GetStorageInfoUseCase(get()) }
    single { GetFileTypeInfoUseCase(get()) }
    single { GetFilesByTypeUseCase(get()) }
    single { ToggleFavoriteUseCase(get()) }
    single { ToggleSafeFolderUseCase(get()) }
    single { CleanupJunkFilesUseCase(get()) }
    single { SearchFilesUseCase(get()) }
    single { LoginUseCase(get()) }
    single { SignUpUseCase(get()) }
    single { LogoutUseCase(get()) }

    // Screen Models
    factory { HomeScreenModel(get(), get()) }
    factory { (fileType: com.tks.filefort.domain.model.FileType) ->
        FileListScreenModel(fileType, get(), get(), get())
    }
    factory { (storageInfo: com.tks.filefort.domain.model.StorageInfo) ->
        StorageInfoScreenModel(storageInfo, get(), get())
    }
} // <-- Moved this closing brace up

expect fun platformModule(): org.koin.core.module.Module
// Removed the extra closing brace from here
