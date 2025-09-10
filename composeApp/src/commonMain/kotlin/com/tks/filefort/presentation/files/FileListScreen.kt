package com.tks.filefort.presentation.files
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.tks.filefort.domain.model.FileType
import com.tks.filefort.presentation.components.FileItemCard
import com.tks.filefort.presentation.components.FileTypeTab
import org.koin.core.parameter.parametersOf

data class FileListScreen(
    private val initialFileType: FileType
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val tabs = listOf(
            FileTypeTab(FileType.IMAGE, "Images", "📷"),
            FileTypeTab(FileType.VIDEO, "Videos", "🎥"),
            FileTypeTab(FileType.AUDIO, "Audios", "🎵"),
            FileTypeTab(FileType.DOCUMENT, "Documents", "📄"),
            FileTypeTab(FileType.APK, "Apps", "📱"),
            FileTypeTab(FileType.PDF, "PDFs", "📕")
        )

        val initialTab = tabs.find { it.fileType == initialFileType } ?: tabs.first()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Files",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            TabNavigator(initialTab) {
                Column(modifier = Modifier.padding(paddingValues)) {
                    ScrollableTabRow(
                        selectedTabIndex = it.current.options.index.toInt(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabs.forEach { tab ->
                            Tab(
                                selected = it.current == tab,
                                onClick = { it.current = tab },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(tab.icon)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(tab.title)
                                    }
                                }
                            )
                        }
                    }

                    it.current.Content()
                }
            }
        }
    }
}

class FileTypeTab(
    val fileType: FileType,
    val title: String,
    val icon: String
) : Tab {

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = when (fileType) {
                    FileType.IMAGE -> 0u
                    FileType.VIDEO -> 1u
                    FileType.AUDIO -> 2u
                    FileType.DOCUMENT -> 3u
                    FileType.APK -> 4u
                    FileType.PDF -> 5u
                    FileType.OTHER -> 6u
                },
                title = title
            )
        }

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<FileListScreenModel> { parametersOf(fileType) }
        val uiState by screenModel.uiState.collectAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.error != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.error!!,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                uiState.files.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = icon,
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No ${title.lowercase()} found",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.files) { file ->
                            FileItemCard(
                                fileItem = file,
                                onFavoriteClick = { screenModel.toggleFavorite(file) },
                                onSafeFolderClick = { screenModel.toggleSafeFolder(file) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}