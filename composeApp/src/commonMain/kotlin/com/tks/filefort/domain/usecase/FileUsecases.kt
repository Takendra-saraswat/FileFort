package com.tks.filefort.domain.usecase

import com.tks.filefort.domain.model.*
import com.tks.filefort.domain.repository.FileRepository
import com.tks.filefort.domain.repository.UserRepository

class GetStorageInfoUseCase(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(): StorageInfo {
        return fileRepository.getStorageInfo()
    }
}

class GetFileTypeInfoUseCase(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(): List<FileTypeInfo> {
        return fileRepository.getFileTypeInfo()
    }
}

class GetFilesByTypeUseCase(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(fileType: FileType): List<FileItem> {
        return fileRepository.getFilesByType(fileType)
    }
}

class ToggleFavoriteUseCase(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(fileItem: FileItem) {
        if (fileItem.isFavorite) {
            fileRepository.removeFromFavorites(fileItem.id)
        } else {
            fileRepository.addToFavorites(fileItem)
        }
    }
}

class ToggleSafeFolderUseCase(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(fileItem: FileItem) {
        if (fileItem.isInSafeFolder) {
            fileRepository.removeFromSafeFolder(fileItem.id)
        } else {
            fileRepository.addToSafeFolder(fileItem)
        }
    }
}

class CleanupJunkFilesUseCase(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(): Long {
        return fileRepository.cleanupJunkFiles()
    }
}

class SearchFilesUseCase(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(query: String): List<FileItem> {
        return fileRepository.searchFiles(query)
    }
}

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return userRepository.login(email, password)
    }
}

class SignUpUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<User> {
        return userRepository.signUp(name, email, password)
    }
}

class LogoutUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.logOut()
    }
}