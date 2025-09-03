package com.tks.filefort.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable

data class StorageInfo(
    val totalSpace: Long,
    val freeSpace: Long,
    val usedSpace: Long
) {
    val usedSpacePercentage: Int
        get() = if (totalSpace > 0) ((usedSpace.toDouble() / totalSpace.toDouble()) * 100).toInt() else 0
    val freeSpacePercentage: Int
        get() = 100 - usedSpacePercentage

}

@Serializable
data class FileTypeInfo(
    val fileType: FileType,
    val count: Int,
    val totalSize: Long
)
    @Serializable
    data class FileItem(
        val id: String,
        val name: String,
        val path: String,
        val size: Long,
        val lastModified: Instant,
        val fileType: FileType,
        val mimeType: String? = null,
        val isDirectory: Boolean = false,
        val isFavorite: Boolean = false,
        val isInSafeFolder: Boolean = false


    )


    @Serializable
    enum class FileType {
        IMAGE,
        VIDEO,
        AUDIO,
        DOCUMENT,
        APK,
        PDF,
        OTHER
    }

    @Serializable
    data class MenuOption(
        val id: String,
        val title: String,
        val icon: String,
        val isEnabled: Boolean = true
    )

    @Serializable
    data class User(
        val id: String,
        val email: String,
        val name: String,
        val isLoggedIn: Boolean = false
    )

    sealed class UiState<out T> {
        object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val message: String) : UiState<Nothing>()
    }

    sealed class NavigationEvent {
        object NavigateToHome : NavigationEvent()
        object NavigateToLogin : NavigationEvent()
        object NavigateToSignUp : NavigationEvent()
        object NavigateToCleanup : NavigationEvent()
        data class NavigateToStorageInfo(val storageInfo: StorageInfo) : NavigationEvent()
        data class NavigateToFileList(val fileType: FileType) : NavigationEvent()
        object NavigateToFavorites : NavigationEvent()
        object NavigateToSafeFolder : NavigationEvent()
    }



