package com.gert.tfgdam.feature.admin.libro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gert.tfgdam.core.network.ApiError
import com.gert.tfgdam.feature.admin.autor.model.Autor
import com.gert.tfgdam.feature.admin.editorial.model.Editorial
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.tipolibro.model.TipoLibro
import com.gert.tfgdam.feature.admin.autor.repository.AutorRepository
import com.gert.tfgdam.feature.admin.editorial.repository.EditorialRepository
import com.gert.tfgdam.feature.admin.genero.model.Genero
import com.gert.tfgdam.feature.admin.genero.repository.GeneroRepository
import com.gert.tfgdam.feature.admin.libro.repository.LibroRepository
import com.gert.tfgdam.feature.admin.tipolibro.repository.TipoLibroRepository
import com.google.gson.Gson
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class LibroAdminViewModel : ViewModel() {
    private val repository = LibroRepository()
    private val editorialRepository = EditorialRepository()
    private val autorRepository = AutorRepository()
    private val tipoLibroRepository = TipoLibroRepository()
    private val generoRepository = GeneroRepository()

    var idWidth by mutableIntStateOf(0)
        private set
    var isbnWidth by mutableIntStateOf(0)
        private set
    var tituloWidth by mutableIntStateOf(0)
        private set
    var editorialWidth by mutableIntStateOf(0)
        private set
    var autorWidth by mutableIntStateOf(0)
        private set
    var tipoLibroWidth by mutableIntStateOf(0)
        private set

    var idLibro by mutableStateOf("")
    var tituloLibro by mutableStateOf("")
    var isbnLibro by mutableStateOf("")
    var portadaLibro by mutableStateOf("")
    var editorialLibro by mutableStateOf<Editorial?>(null)
    var autorLibro by mutableStateOf<Autor?>(null)
    var tipoLibroLibro by mutableStateOf<TipoLibro?>(null)
    var fechaSalidaLibro by mutableStateOf("")
    var descripcionLibro by mutableStateOf("")
    var precioLibro by mutableStateOf("")
    var stockLibro by mutableStateOf("")
    var generosLibro by mutableStateOf<List<Genero>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var buscador by mutableStateOf("")
        private set
    private val buscadorFlow = MutableStateFlow("")
    var isLoadingBusqueda by mutableStateOf(false)

    var libros by mutableStateOf<List<Libro>>(emptyList())
        private set
    var librosFiltrados by mutableStateOf<List<Libro>>(emptyList())
        private set
    var listaEditoriales by mutableStateOf<List<Editorial>>(emptyList())
        private set
    var listaAutores by mutableStateOf<List<Autor>>(emptyList())
        private set
    var listaTipoLibros by mutableStateOf<List<TipoLibro>>(emptyList())
        private set
    var listaGeneros by mutableStateOf<List<Genero>>(emptyList())
        private set


    init {
        cargarLibros()
        observarBuscador()
    }

    private fun cargarLibros() {
        viewModelScope.launch {
            try {
                val response = repository.getAll()

                if (response.isSuccessful) {
                    libros = (response.body() ?: emptyList()).sortedBy { it.id }
                    calcularAnchuras()
                } else {
                    libros = emptyList()
                }
            } catch (e: IOException) {
                libros = emptyList()
            }
        }
    }

    fun onBuscadorChange(texto: String) {
        buscador = texto
        buscadorFlow.value = texto
        if (texto.isNotEmpty()) {
            isLoadingBusqueda = true
        }
    }

    @OptIn(FlowPreview::class)
    private fun observarBuscador() {
        viewModelScope.launch {
            buscadorFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect { texto ->
                    buscarLibros(texto)
                }
        }
    }

    private fun buscarLibros(texto: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPorBusqueda(texto)

                if (response.isSuccessful) {
                    librosFiltrados = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    librosFiltrados = emptyList()
                }
            } catch (e: IOException) {
                librosFiltrados = emptyList()
            } finally {
                isLoadingBusqueda = false
            }
        }
    }

    private fun cargarAutores() {
        viewModelScope.launch {
            try {
                val response = autorRepository.getAll()

                if (response.isSuccessful) {
                    listaAutores = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    listaAutores = emptyList()
                }
            } catch (e: IOException) {
                listaAutores = emptyList()
            }
        }
    }

    private fun cargarEditoriales() {
        viewModelScope.launch {
            try {
                val response = editorialRepository.getAll()

                if (response.isSuccessful) {
                    listaEditoriales = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    listaEditoriales = emptyList()
                }
            } catch (e: IOException) {
                listaEditoriales = emptyList()
            }
        }
    }

    private fun cargarTipoLibros() {
        viewModelScope.launch {
            try {
                val response = tipoLibroRepository.getAll()

                if (response.isSuccessful) {
                    listaTipoLibros = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    listaTipoLibros = emptyList()
                }
            } catch (e: IOException) {
                listaTipoLibros = emptyList()
            }
        }
    }

    fun cargarListasEditarYCrear() {
        cargarEditoriales()
        cargarAutores()
        cargarTipoLibros()
    }

    fun cargarGeneros() {
        viewModelScope.launch {
            try {
                val response = generoRepository.getAll()

                if (response.isSuccessful) {
                    listaGeneros = (response.body() ?: emptyList()).sortedBy { it.id }
                } else {
                    listaGeneros = emptyList()
                }
            } catch (e: IOException) {
                listaGeneros = emptyList()
            }
        }
    }

    fun crearLibro(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val libroNuevo = Libro(
                    isbn = isbnLibro.trim(),
                    portada = portadaLibro.trim(),
                    titulo = tituloLibro.trim(),
                    editorial = editorialLibro,
                    autor = autorLibro,
                    tipoLibro = tipoLibroLibro,
                    fechaSalida = fechaSalidaLibro.trim(),
                    descripcion = descripcionLibro.trim(),
                    precio = precioLibro.trim().toDoubleOrNull(),
                    stock = stockLibro.trim().toIntOrNull(),
                    generos = generosLibro
                )

                if (precioLibro.toDoubleOrNull() == null || stockLibro.toIntOrNull() == null) {
                    errorMessage = "Por favor, introduzca un número válido en el campo correspondiente"
                    isLoading = false
                    return@launch
                }

                if (isbnLibro.isEmpty() || tituloLibro.isEmpty() || editorialLibro == null || autorLibro == null || tipoLibroLibro == null || fechaSalidaLibro.isEmpty() || descripcionLibro.isEmpty() || precioLibro.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.create(libroNuevo)
                if (response.isSuccessful) {
                    cargarLibros()
                    restaurarCamposLibro(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al crear el libro"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposLibro(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun editarLibro(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val libroActualizado = Libro(
                    id = idLibro.toLong(),
                    isbn = isbnLibro.trim(),
                    portada = portadaLibro.trim(),
                    titulo = tituloLibro.trim(),
                    editorial = editorialLibro,
                    autor = autorLibro,
                    tipoLibro = tipoLibroLibro,
                    fechaSalida = fechaSalidaLibro.trim(),
                    descripcion = descripcionLibro.trim(),
                    precio = precioLibro.trim().toDoubleOrNull(),
                    stock = stockLibro.trim().toIntOrNull(),
                    generos = generosLibro
                )

                if (precioLibro.toDoubleOrNull() == null || stockLibro.toIntOrNull() == null) {
                    errorMessage = "Por favor, introduzca un número válido en el campo correspondiente"
                    isLoading = false
                    return@launch
                }

                if (isbnLibro.isEmpty() || tituloLibro.isEmpty() || editorialLibro == null || autorLibro == null || tipoLibroLibro == null || fechaSalidaLibro.isEmpty() || descripcionLibro.isEmpty() || precioLibro.isEmpty()) {
                    errorMessage = "Por favor, rellene todos los campos"
                    isLoading = false
                    return@launch
                }

                val response = repository.update(idLibro.toLong(), libroActualizado)
                if (response.isSuccessful) {
                    cargarLibros()
                    restaurarCamposLibro(null)

                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al editar el libro"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                restaurarCamposLibro(null)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun eliminarLibro(idLibro: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val response = repository.delete(idLibro)
                if (response.isSuccessful) {
                    cargarLibros()
                    delay(500)
                    onSuccess()
                } else {
                    val errorJson = response.errorBody()?.string()

                    errorMessage = try {
                        val jsonObject = JSONObject(errorJson ?: "")
                        jsonObject.getString("message")
                    } catch (e: Exception) {
                        "Error al eliminar el libro"
                    }
                }
            } catch (e: Exception) {
                val errorJson = e.message.toString()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)
                errorMessage = apiError.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    fun restaurarCamposLibro(libro: Libro? = null) {
        if (libro != null) {
            idLibro = libro.id.toString() ?: ""
            isbnLibro = libro.isbn ?: ""
            portadaLibro = libro.portada ?: ""
            tituloLibro = libro.titulo ?: ""
            editorialLibro = libro.editorial
            autorLibro = libro.autor
            tipoLibroLibro = libro.tipoLibro
            fechaSalidaLibro = libro.fechaSalida ?: ""
            descripcionLibro = libro.descripcion ?: ""
            precioLibro = libro.precio.toString() ?: ""
            stockLibro = libro.stock.toString() ?: ""
            generosLibro = libro.generos ?: emptyList()
        } else {
            idLibro = ""
            isbnLibro = ""
            portadaLibro = ""
            tituloLibro = ""
            editorialLibro = null
            autorLibro = null
            tipoLibroLibro = null
            fechaSalidaLibro = ""
            descripcionLibro = ""
            precioLibro = ""
            stockLibro = ""
            generosLibro = emptyList()
        }
    }

    private fun calcularAnchuras() {
        idWidth = libros.maxOfOrNull { it.id.toString().length } ?: 0
        isbnWidth = libros.maxOfOrNull { it.isbn?.length ?: 0 } ?: 0
        tituloWidth = libros.maxOfOrNull { it.titulo?.length ?: 0 } ?: 0
        autorWidth = libros.maxOfOrNull { it.autor?.nombre?.length ?: 0 } ?: 0
        editorialWidth = libros.maxOfOrNull { it.editorial?.nombre?.length ?: 0 } ?: 0
        tipoLibroWidth = libros.maxOfOrNull { it.tipoLibro?.nombre?.length ?: 0 } ?: 0
    }

    fun cambiarDeCharacteresADp(nChars: Int): Dp {
        return kotlin.comparisons.maxOf((nChars * 11).dp, 60.dp)
    }
}