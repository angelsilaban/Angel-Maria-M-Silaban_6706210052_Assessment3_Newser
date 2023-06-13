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
        data.value = initData()
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val result = BeritaApi.service.getBerita()
                Log.d("MainViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    private fun initData(): ArrayList<Berita> {
        return arrayListOf(
            Berita(
                "Polisi tangkap 21  tersangka narkoba di Bogor, \n sabu hingga 0bat keras disita.",
                "Newser - 16/05/2023",
                R.drawable.image_narkoba
            ),

            Berita(
                "Salah satu pulau terpadat di dunia ternyata \n milik Indonesia.",
                "Newser - 16/05/2023",
                R.drawable.image_pulau
            ),

            Berita(
                "Jokowi Belum Mau Komentari RUU TNI Soal \n Jabatan Sipil Diisi Militer.",
                "Newser - 17/05/2023",
                R.drawable.images_ruu_tni
            ),

            Berita(
                "Panglima TNI Klarifikasi 4 Pekerja BTS Kominfo \n Bukan Disandera KKB.",
                "Newser - 17/05/2023",
                R.drawable.images_panglima_tni
            )
        )
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