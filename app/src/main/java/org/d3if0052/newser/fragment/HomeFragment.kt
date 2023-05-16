package org.d3if0052.newser.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if0052.newser.R
import org.d3if0052.newser.adapter.MainAdapter
import org.d3if0052.newser.databinding.FragmentHomeBinding
import org.d3if0052.newser.model.Berita

@SuppressLint("NotifyDataSetChanged")
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var beritaAdapter: MainAdapter
    private lateinit var list: ArrayList<Berita>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        list = ArrayList()
        list.addAll(getData())
        beritaAdapter = MainAdapter(list)

        with(binding.rvBerita) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = beritaAdapter
        }

        beritaAdapter.run { notifyDataSetChanged() }

        return binding.root
    }

    private fun getData(): ArrayList<Berita> {
        return arrayListOf(
            Berita(
                "Polisi tangkap 21  tersangka narkoba di Bogor, \n sabu hingga 0bat keras disita.",
                "Newser - 13/05/2023",
                R.drawable.image_narkoba
            ),

            Berita(
                "Salah satu pulau terpadat di dunia ternyata \n milik Indonesia.",
                "Newser - 14/05/2023",
                R.drawable.image_pulau
            ),

            Berita(
                "Jokowi Belum Mau Komentari RUU TNI Soal \n Jabatan Sipil Diisi Militer.",
                "Newser - 15/05/2023",
                R.drawable.images_ruu_tni
            ),

            Berita(
                "Panglima TNI Klarifikasi 4 Pekerja BTS Kominfo \n Bukan Disandera KKB.",
                "Newser - 15/05/2023",
                R.drawable.images_panglima_tni
            )
        )
    }

    private fun filter(query: String) {
        val filteredList = arrayListOf<Berita>()

        for (item in list)
            if (item.title.lowercase().contains(query.lowercase())
            ) filteredList.add(item)

        if (filteredList.isEmpty()) Toast.makeText(
            requireContext(),
            "No data found!",
            Toast.LENGTH_SHORT
        ).show()
        else beritaAdapter.filtering(filteredList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)

        val searchItem = menu.findItem(R.id.search_view)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                filter(query)
                return false
            }
        })
    }
}