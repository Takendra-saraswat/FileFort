package com.tks.filefort.presentation.home
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.tks.filefort.domain.model.*
import com.tks.filefort.domain.usecase.GetStorageInfoUseCase
import com.tks.filefort.domain.usecase.GetFileTypeInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val getStorageInfoUseCase: GetStorageInfoUseCase,
    private val getFileTypeInfoUseCase: GetFileTypeInfoUseCase
) : ScreenModel {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        screenModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val storageInfo = getStorageInfoUseCase()
                val fileTypeInfo = getFileTypeInfoUseCase()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    storageInfo = storageInfo,
                    fileTypeInfo = fileTypeInfo,
                    quickAccessItems = getQuickAccessItems(),
                    bottomItems = getBottomItems()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun onStorageInfoClicked() {
        _uiState.value.storageInfo?.let { storageInfo ->
            _navigationEvent.value = NavigationEvent.NavigateToStorageInfo(storageInfo)
        }
    }

    fun onFileTypeClicked(fileType: FileType) {
        _navigationEvent.value = NavigationEvent.NavigateToFileList(fileType)
    }

    fun onBottomItemClicked(itemId: String) {
        when (itemId) {
            "favorites" -> _navigationEvent.value = NavigationEvent.NavigateToFavorites
            "safe_folder" -> _navigationEvent.value = NavigationEvent.NavigateToSafeFolder
        }
    }

    fun onMenuItemClicked(menuId: String) {
        when (menuId) {
            "login" -> _navigationEvent.value = NavigationEvent.NavigateToLogin
            "signup" -> _navigationEvent.value = NavigationEvent.NavigateToSignUp
            "cleanup" -> _navigationEvent.value = NavigationEvent.NavigateToCleanup
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    private fun getQuickAccessItems(): List<QuickAccessItem> {
        return listOf(
            QuickAccessItem("images", "Images", "📷", FileType.IMAGE),
            QuickAccessItem("videos", "Videos", "🎥", FileType.VIDEO),
            QuickAccessItem("audios", "Audios", "🎵", FileType.AUDIO),
            QuickAccessItem("documents", "Documents", "📄", FileType.DOCUMENT),
            QuickAccessItem("apps", "Apps", "📱", FileType.APK),
            QuickAccessItem("downloads", "Downloads", "⬇️", FileType.OTHER)
        )
    }

    private fun getBottomItems(): List<BottomItem> {
        return listOf(
            BottomItem("favorites", "Favorites", "⭐"),
            BottomItem("safe_folder", "Safe Folder", "🔒")
        )
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val storageInfo: StorageInfo? = null,
    val fileTypeInfo: List<FileTypeInfo> = emptyList(),
    val quickAccessItems: List<QuickAccessItem> = emptyList(),
    val bottomItems: List<BottomItem> = emptyList(),
    val menuOptions: List<MenuOption> = listOf(
        MenuOption("login", "Login", "👤"),
        MenuOption("signup", "Sign Up", "📝"),
        MenuOption("cleanup", "Clean Up", "🧹"),
        MenuOption("settings", "Settings", "⚙️"),
        MenuOption("about", "About", "ℹ️")
    )
)

data class QuickAccessItem(
    val id: String,
    val title: String,
    val icon: String,
    val fileType: FileType
)

data class BottomItem(
    val id: String,
    val title: String,
    val icon: String
)