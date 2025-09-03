package com.tks.filefort.presentation.storage

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tks.filefort.domain.model.StorageInfo
import com.tks.filefort.domain.model.FileTypeInfo
import com.tks.filefort.domain.usecase.GetFileTypeInfoUseCase
import com.tks.filefort.domain.usecase.CleanupJunkFilesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StorageInfoScreenModel(
    private val initialStorageInfo: StorageInfo,
    private val getFileTypeInfoUseCase: GetFileTypeInfoUseCase,
    private val cleanupJunkFilesUseCase: CleanupJunkFilesUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(
        StorageInfoUiState(storageInfo = initialStorageInfo)
    )
    val uiState: StateFlow<StorageInfoUiState> = _uiState.asStateFlow()

    init {
        loadFileTypeInfo()
    }

    private fun loadFileTypeInfo() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val fileTypeInfo = getFileTypeInfoUseCase()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    fileTypeInfo = fileTypeInfo
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load file type information"
                )
            }
        }
    }

    fun performCleanup() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCleanupLoading = true,
                error = null,
                successMessage = null
            )

            try {
                val cleanedSize = cleanupJunkFilesUseCase()
                val sizeInMB = cleanedSize / (1024 * 1024)

                _uiState.value = _uiState.value.copy(
                    isCleanupLoading = false,
                    lastCleanupSize = cleanedSize,
                    successMessage = "Successfully cleaned ${sizeInMB}MB of junk files!"
                )

                // Refresh file type info after cleanup
                loadFileTypeInfo()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isCleanupLoading = false,
                    error = e.message ?: "Failed to cleanup files"
                )
            }
        }
    }
}

data class StorageInfoUiState(
    val isLoading: Boolean = false,
    val isCleanupLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val storageInfo: StorageInfo,
    val fileTypeInfo: List<FileTypeInfo> = emptyList(),
    val lastCleanupSize: Long? = null
)