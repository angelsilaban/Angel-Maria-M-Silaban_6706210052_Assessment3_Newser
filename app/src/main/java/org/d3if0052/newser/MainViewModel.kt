package org.d3if0052.newser

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0052.newser.db.NewsDao
import org.d3if0052.newser.model.Berita
import org.d3if0052.newser.network.ApiStatus
import org.d3if0052.newser.network.BeritaApi


class MainViewModel (db: NewsDao) : ViewModel() {
//    val data = db.getAll()
    private var data = MutableLiveData<ArrayList<Berita>>()
    private val status = MutableLiveData<ApiStatus>()

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                data.postValue(BeritaApi.service.getNews())
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }
    fun getData(): LiveData<ArrayList<Berita>> = data
    fun getStatus(): LiveData<ApiStatus> = status

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