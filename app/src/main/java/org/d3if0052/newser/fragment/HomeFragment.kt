package org.d3if0052.newser.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if0052.newser.HomePageActivity
import org.d3if0052.newser.MainViewModel
import org.d3if0052.newser.MainViewModelFactory
import org.d3if0052.newser.R
import org.d3if0052.newser.databinding.FragmentHomeBinding
import org.d3if0052.newser.db.NewsDatabase
import org.d3if0052.newser.model.Berita

import org.d3if0052.newser.network.ApiStatus
import org.d3if0052.newser.ui.main.MainAdapter

@SuppressLint("NotifyDataSetChanged")
class HomeFragment : Fragment(), View.OnClickListener {

    private val viewModel: MainViewModel by lazy {
        val db = NewsDatabase.getInstance(requireContext())
        ViewModelProvider(this, MainViewModelFactory(db.newsDao))[MainViewModel::class.java]
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var beritaAdapter: MainAdapter
    private lateinit var list: ArrayList<Berita>
    private val isLinearLayout = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

//        list = ArrayList()
//        beritaAdapter = MainAdapter(list)
//        list.addAll(getData())



        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner) {
            beritaAdapter = MainAdapter(it)

            with(binding.rvBerita) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = beritaAdapter
            }

            beritaAdapter.run { notifyDataSetChanged() }
        }
        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }
        viewModel.scheduleUpdater(requireActivity().application)

        (activity as AppCompatActivity).supportActionBar?.title = "Newser"
        (activity as HomePageActivity).hideUpButton()

        val buttonLihatBerita: Button = view.findViewById(R.id.button_lihat_berita)

        buttonLihatBerita.setOnClickListener(this)
    }

    override fun onClick(walah: View?) {
        if(walah?.id == R.id.button_lihat_berita) {
            val lihatBeritaUtamaFragment = LihatBeritaUtamaFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.content, lihatBeritaUtamaFragment, LihatBeritaUtamaFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                }
            }
            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                HomePageActivity.PERMISSION_REQUEST_CODE
            )
        }
    }

}