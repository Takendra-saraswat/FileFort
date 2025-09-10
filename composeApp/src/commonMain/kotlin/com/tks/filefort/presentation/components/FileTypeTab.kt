package com.tks.filefort.presentation.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.koin.getScreenModel
import com.tks.filefort.domain.model.FileType
import com.tks.filefort.presentation.files.FileListScreenModel
import org.koin.core.parameter.parametersOf

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
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No ${title.lowercase()} found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Files will appear here when available",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "${uiState.files.size} ${title.lowercase()} found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

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