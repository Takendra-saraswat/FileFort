package com.tks.filefort.domain.repository

import com.tks.filefort.domain.model.FileItem
import com.tks.filefort.domain.model.FileType
import com.tks.filefort.domain.model.FileTypeInfo
import com.tks.filefort.domain.model.StorageInfo
import com.tks.filefort.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FileRepository {
    suspend fun getStorageInfo(): StorageInfo
    suspend fun getFileTypeInfo(): List<FileTypeInfo>
    suspend fun getFilesByType(fileType: FileType): List<FileItem>
    suspend fun getFavoriteFiles(): List<FileItem>
    suspend fun getSafeFiles(): List<FileItem>
    suspend fun addToFavorites(fileItem: FileItem)
    suspend fun removeFromFavorites(fileId: String)
    suspend fun addToSafeFolder(fileItem:FileItem)
    suspend fun removeFromSafeFolder(fileId: String)
    suspend fun cleanupJunkFiles(): Long
    suspend fun searchFiles(query: String): List<FileItem>
}

interface UserRepository{
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logOut()
    suspend fun signUp(name: String, email: String, password: String): Result<User>
    suspend fun getCurrentUser(): User?
     fun isLoggedIn(): Flow<Boolean>
}