package com.tks.filefort.data.repository

import com.tks.filefort.domain.model.*
import com.tks.filefort.domain.repository.FileRepository
import com.tks.filefort.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class FileRepositoryImpl : FileRepository {

    private val mockFiles = mutableListOf<FileItem>()
    private val favoriteFiles = mutableSetOf<String>()
    private val safeFiles = mutableSetOf<String>()

    init {
        generateMockFiles()
    }

    private fun generateMockFiles() {
        val fileTypes = FileType.entries
        repeat(100) { index ->
            val fileType = fileTypes.random()
            val fileName = when (fileType) {
                FileType.IMAGE -> "image_$index.jpg"
                FileType.VIDEO -> "video_$index.mp4"
                FileType.AUDIO -> "audio_$index.mp3"
                FileType.DOCUMENT -> "document_$index.docx"
                FileType.APK -> "app_$index.apk"
                FileType.PDF -> "document_$index.pdf"
                FileType.OTHER -> "file_$index.txt"
            }

            mockFiles.add(
                FileItem(
                    id = "file_$index",
                    name = fileName,
                    path = "/storage/emulated/0/$fileName",
                    size = Random.nextLong(1024, 100 * 1024 * 1024),
                    lastModified = Clock.System.now() - Random.nextLong(0, 365 * 24 * 60 * 60 * 1000L).milliseconds,
                    fileType = fileType,
                    mimeType = getMimeType(fileType),
                    isDirectory = false,
                    isFavorite = false,
                    isInSafeFolder = false
                )
            )
        }
    }

    private fun getMimeType(fileType: FileType): String {
        return when (fileType) {
            FileType.IMAGE -> "image/jpeg"
            FileType.VIDEO -> "video/mp4"
            FileType.AUDIO -> "audio/mp3"
            FileType.DOCUMENT -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            FileType.APK -> "application/vnd.android.package-archive"
            FileType.PDF -> "application/pdf"
            FileType.OTHER -> "text/plain"
        }
    }

    override suspend fun getStorageInfo(): StorageInfo {
        val totalSpace = 128L * 1024 * 1024 * 1024 // 128GB
        val freeSpace = 45L * 1024 * 1024 * 1024   // 45GB
        val usedSpace = totalSpace - freeSpace

        return StorageInfo(
            totalSpace = totalSpace,
            freeSpace = freeSpace,
            usedSpace = usedSpace
        )
    }

    override suspend fun getFileTypeInfo(): List<FileTypeInfo> {
        return FileType.entries.map { fileType ->
            val files = mockFiles.filter { it.fileType == fileType }
            FileTypeInfo(
                fileType = fileType,
                count = files.size,
                totalSize = files.sumOf { it.size }
            )
        }
    }

    override suspend fun getFilesByType(fileType: FileType): List<FileItem> {
        return mockFiles.filter { it.fileType == fileType }
            .map { file ->
                file.copy(
                    isFavorite = favoriteFiles.contains(file.id),
                    isInSafeFolder = safeFiles.contains(file.id)
                )
            }
    }

    override suspend fun getFavoriteFiles(): List<FileItem> {
        return mockFiles.filter { favoriteFiles.contains(it.id) }
            .map { it.copy(isFavorite = true) }
    }

    override suspend fun getSafeFiles(): List<FileItem> {
        return mockFiles.filter { safeFiles.contains(it.id) }
            .map { it.copy(isInSafeFolder = true) }
    }

    override suspend fun addToFavorites(fileItem: FileItem) {
        favoriteFiles.add(fileItem.id)
    }

    override suspend fun removeFromFavorites(fileId: String) {
        favoriteFiles.remove(fileId)
    }

    override suspend fun addToSafeFolder(fileItem: FileItem) {
        safeFiles.add(fileItem.id)
    }

    override suspend fun removeFromSafeFolder(fileId: String) {
        safeFiles.remove(fileId)
    }

    override suspend fun cleanupJunkFiles(): Long {
        // Mock cleanup - return size cleaned
        return Random.nextLong(100 * 1024 * 1024, 1024 * 1024 * 1024) // 100MB to 1GB
    }

    override suspend fun searchFiles(query: String): List<FileItem> {
        return mockFiles.filter {
            it.name.contains(query, ignoreCase = true)
        }.map { file ->
            file.copy(
                isFavorite = favoriteFiles.contains(file.id),
                isInSafeFolder = safeFiles.contains(file.id)
            )
        }
    }
}

class UserRepositoryImpl : UserRepository {
    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Mock login logic
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(
                    id = "user_${email.hashCode()}",
                    email = email,
                    name = email.substringBefore("@"),
                    isLoggedIn = true
                )
                currentUser = user
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<User> {
        return try {
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(
                    id = "user_${email.hashCode()}",
                    email = email,
                    name = name,
                    isLoggedIn = true
                )
                currentUser = user
                Result.success(user)
            } else {
                Result.failure(Exception("All fields are required"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logOut() {
        currentUser = null
    }

    override suspend fun getCurrentUser(): User? {
        return currentUser
    }

    override  fun isLoggedIn(): Flow<Boolean> {
        return flowOf(currentUser?.isLoggedIn ?: false)
    }
}