package com.pemmob.digimonexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.digimonexplorer.data.model.Digimon
import com.pemmob.digimonexplorer.data.repository.DigimonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DigimonUiState {
    object Loading : DigimonUiState
    data class Success(val digimons: List<Digimon>) : DigimonUiState
    data class Error(val message: String) : DigimonUiState
}

class DigimonListViewModel : ViewModel() {
    private val repository = DigimonRepository()

    private val _uiState = MutableStateFlow<DigimonUiState>(DigimonUiState.Loading)
    val uiState: StateFlow<DigimonUiState> = _uiState.asStateFlow()

    init {
        fetchDigimon()
    }

    fun fetchDigimon() {
        viewModelScope.launch {
            _uiState.value = DigimonUiState.Loading
            try {
                val data = repository.getDigimonList()
                _uiState.value = if (data.isEmpty()) {
                    DigimonUiState.Error("Data Digimon kosong")
                } else {
                    DigimonUiState.Success(data)
                }
            } catch (e: Exception) {
                _uiState.value = DigimonUiState.Error("Gagal memuat data. Periksa koneksi internet. (${e.localizedMessage})")
            }
        }
    }
}
