package com.pemmob.digimonexplorer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.digimonexplorer.data.model.Digimon
import com.pemmob.digimonexplorer.data.repository.DigimonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(val digimon: Digimon) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class DigimonDetailViewModel : ViewModel() {
    private val repository = DigimonRepository()
    private var currentId: Int? = null

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadDetail(id: Int, force: Boolean = false) {
        if (!force && currentId == id) return
        currentId = id
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                _uiState.value = DetailUiState.Success(repository.getDigimonDetail(id))
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error("Gagal memuat detail. (${e.localizedMessage})")
            }
        }
    }
}
