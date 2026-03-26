package com.gert.tfgdam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.model.Editorial
import com.gert.tfgdam.repository.EditorialRepository
import kotlinx.coroutines.launch
import java.io.IOException

class EditorialAdminViewModel : ViewModel() {
    private val repository = EditorialRepository()

    var editoriales by mutableStateOf<List<Editorial>>(emptyList())
        private set
    init {
        cargarEditoriales()
    }

    private fun cargarEditoriales() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    editoriales = response.body() ?: emptyList()
                } else {
                    editoriales = emptyList()
                }
            } catch (e: IOException) {
                editoriales = emptyList()
            }
        }
    }
}