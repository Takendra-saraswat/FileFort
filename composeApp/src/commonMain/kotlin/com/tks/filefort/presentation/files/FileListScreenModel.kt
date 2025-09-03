package com.tks.filefort.presentation.files

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tks.filefort.domain.model.FileType
import com.tks.filefort.domain.model.FileItem
import com.tks.filefort.domain.usecase.GetFilesByTypeUseCase
import com.tks.filefort.domain.usecase.ToggleFavoriteUseCase
import com.tks.filefort.domain.usecase.ToggleSafeFolderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FileListScreenModel(
    private val fileType: FileType,
    private val getFilesByTypeUseCase: GetFilesByTypeUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val toggleSafeFolderUseCase: ToggleSafeFolderUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(FileListUiState())
    val uiState: StateFlow<FileListUiState> = _uiState.asStateFlow()

    init {
        loadFiles()
    }

    private fun loadFiles() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val files = getFilesByTypeUseCase(fileType)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    files = files
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load files"
                )
            }
        }
    }

    fun toggleFavorite(fileItem: FileItem) {
        screenModelScope.launch {
            try {
                toggleFavoriteUseCase(fileItem)
                // Update the file in the current list
                val updatedFiles = _uiState.value.files.map { file ->
                    if (file.id == fileItem.id) {
                        file.copy(isFavorite = !file.isFavorite)
                    } else {
                        file
                    }
                }
                _uiState.value = _uiState.value.copy(files = updatedFiles)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update favorite: ${e.message}"
                )
            }
        }
    }

    fun toggleSafeFolder(fileItem: FileItem) {
        screenModelScope.launch {
            try {
                toggleSafeFolderUseCase(fileItem)
                // Update the file in the current list
                val updatedFiles = _uiState.value.files.map { file ->
                    if (file.id == fileItem.id) {
                        file.copy(isInSafeFolder = !file.isInSafeFolder)
                    } else {
                        file
                    }
                }
                _uiState.value = _uiState.value.copy(files = updatedFiles)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update safe folder: ${e.message}"
                )
            }
        }
    }

    fun searchFiles(query: String) {
        if (query.isBlank()) {
            loadFiles()
            return
        }

        screenModelScope.launch {
            try {
                val allFiles = getFilesByTypeUseCase(fileType)
                val filteredFiles = allFiles.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                _uiState.value = _uiState.value.copy(files = filteredFiles)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to search files: ${e.message}"
                )
            }
        }
    }
}

data class FileListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val files: List<FileItem> = emptyList()
)