package com.pemmob.digimonexplorer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pemmob.digimonexplorer.data.model.Digimon
import com.pemmob.digimonexplorer.data.model.DigimonSkill
import com.pemmob.digimonexplorer.ui.theme.attributeColor
import com.pemmob.digimonexplorer.ui.viewmodel.DetailUiState
import com.pemmob.digimonexplorer.ui.viewmodel.DigimonDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    digimonId: Int,
    onBack: () -> Unit,
    viewModel: DigimonDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(digimonId) { viewModel.loadDetail(digimonId) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val current = uiState
                    if (current is DetailUiState.Success) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = current.digimon.name,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "#%03d".format(current.digimon.id),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Text("Detail Digimon", style = MaterialTheme.typography.titleLarge)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is DetailUiState.Loading -> CircularProgressIndicator()
                is DetailUiState.Error -> ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.loadDetail(digimonId, force = true) }
                )
                is DetailUiState.Success -> DetailContent(state.digimon)
            }
        }
    }
}

@Composable
private fun DetailContent(digimon: Digimon) {
    val accent = attributeColor(digimon.attribute)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Kartu gambar pastel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(accent.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = digimon.imageUrl,
                contentDescription = digimon.name,
                modifier = Modifier.size(220.dp)
            )
        }

        // Chip: Level, Attribute, Type
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            item { InfoChip(text = digimon.level, accent = accent, filled = false) }
            item { InfoChip(text = digimon.attribute, accent = accent, filled = true) }
            item { InfoChip(text = digimon.type, accent = accent, filled = false) }
        }

        // Kartu info 2 x 2
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    InfoPair("Level", digimon.level, Modifier.weight(1f))
                    InfoPair("Attribute", digimon.attribute, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    InfoPair("Type", digimon.type, Modifier.weight(1f))
                    InfoPair("Rilis", digimon.releaseDate ?: "-", Modifier.weight(1f))
                }
            }
        }

        digimon.description?.let { desc ->
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Deskripsi",
                    style = MaterialTheme.typography.titleMedium,
                    color = accent
                )
                Text(text = desc, style = MaterialTheme.typography.bodyLarge)
            }
        }

        if (digimon.skills.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Skill",
                    style = MaterialTheme.typography.titleMedium,
                    color = accent
                )
                digimon.skills.take(5).forEach { skill ->
                    SkillCard(skill = skill, accent = accent)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun InfoChip(text: String, accent: Color, filled: Boolean) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (filled) accent else accent.copy(alpha = 0.18f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (filled) Color.White else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun InfoPair(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SkillCard(skill: DigimonSkill, accent: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = accent.copy(alpha = 0.12f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = skill.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            skill.description?.takeIf { it.isNotBlank() }?.let { desc ->
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}