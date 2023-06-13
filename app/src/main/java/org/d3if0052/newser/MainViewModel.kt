package org.d3if0052.newser

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0052.newser.db.NewsDao
import org.d3if0052.newser.model.Berita
import org.d3if0052.newser.network.BeritaApi


class MainViewModel (db: NewsDao) : ViewModel() {
//    val data = db.getAll()
    private var data = MutableLiveData<ArrayList<Berita>>()

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                data.postValue(BeritaApi.service.getBerita())
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }
    fun getData(): LiveData<ArrayList<Berita>> = data
}

class MainViewModelFactory(
    private val db: NewsDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class") }
}