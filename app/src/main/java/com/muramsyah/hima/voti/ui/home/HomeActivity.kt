package com.muramsyah.hima.voti.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.snackbar.Snackbar
import com.muramsyah.hima.voti.R
import com.muramsyah.hima.voti.core.data.Resource
import com.muramsyah.hima.voti.core.sf.AppSharedPreference
import com.muramsyah.hima.voti.core.ui.HomeAdapter
import com.muramsyah.hima.voti.databinding.ActivityHomeBinding
import com.muramsyah.hima.voti.ui.detail.calon.DetailCalonFragment
import com.muramsyah.hima.voti.ui.login.LoginActivity
import com.muramsyah.hima.voti.ui.settings.SettingsActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var adapter: HomeAdapter

    private var homeActivityBinding: ActivityHomeBinding? = null
    private val binding get() = homeActivityBinding!!

    private val sharedPreference: AppSharedPreference by inject()

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeActivityBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        adapter = HomeAdapter()
        binding.rvCalonKahim.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCalonKahim.setHasFixedSize(true)

        initViewModel()

        adapter.onClick = { data ->
            val detailCalonFragment = DetailCalonFragment()
            val bundle = Bundle()
            bundle.putParcelable(DetailCalonFragment.EXTRA_DATA, data)
            detailCalonFragment.arguments = bundle
            detailCalonFragment.show(supportFragmentManager, DetailCalonFragment.TAG)
        }

        binding.cvStatistic.setOnClickListener {
//            DetailStatisticFragment().show(supportFragmentManager, DetailStatisticFragment.TAG)
            Snackbar.make(binding.root, "Coming Soon Feature", Snackbar.LENGTH_SHORT).show()
        }

        binding.refreshContent.setOnRefreshListener {
            initViewModel()
        }

        if (!sharedPreference.getIsStarted()) {
            initStarting()
            sharedPreference.setIsStarted(true)
        } else {
            return
        }
    }

    private fun initViewModel() {
        viewModel.getCandidates().observe(this, { resources ->
            when (resources) {
                is Resource.Loading -> {
                    setProgressBar(true)
                }
                is Resource.Success -> {
                    setProgressBar(false)
                    adapter.setData(resources.data)
                    binding.rvCalonKahim.adapter = adapter
                    binding.rvCalonKahim.setHasFixedSize(true)
                }
                is Resource.Error -> {
                    setProgressBar(false)
                    Toast.makeText(this, resources.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.getMahasiswa().observe(this, { resource ->
            if (resource != null && resource is Resource.Success) {
                binding.tvNama.text = resource.data!!.nama
            }
        })

        binding.refreshContent.isRefreshing = false
    }

    private fun initStarting() {
        // 1
        TapTargetSequence(this)
            // 2
            .targets(
                TapTarget.forView(binding.cvStatistic, resources.getString(R.string.title_taptarget_statistic),resources.getString(R.string.description_taptarget_statistic))
                    .cancelable(false)
                    .transparentTarget(true)
                    .targetRadius(70),

                TapTarget.forView(binding.rvCalonKahim, resources.getString(R.string.title_taptarget_calonkahim), resources.getString(R.string.description_taptarget_calonkahim))
                    .cancelable(false)
                    .transparentTarget(true)
                    .targetRadius(150))
            // 3
            .listener(object : TapTargetSequence.Listener {
                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                // 4
                override fun onSequenceFinish() {
                    Snackbar.make(binding.root, resources.getString(R.string.title_let_vote), Snackbar.LENGTH_LONG).show()
                }
                // 5
                override fun onSequenceCanceled(lastTarget: TapTarget) {}
            })
            // 6
            .start()
    }

    private fun setProgressBar(isProgress: Boolean) {
        if (isProgress) {
            binding.progressBar.root.visibility = View.VISIBLE
            binding.tvNama.visibility = View.GONE
            binding.cvStatistic.visibility = View.GONE
            binding.rvCalonKahim.visibility = View.GONE
            binding.tvWelcome.visibility = View.GONE
            binding.tvTitleCalonKahim.visibility = View.GONE
        } else {
            binding.progressBar.root.visibility = View.GONE
            binding.tvNama.visibility = View.VISIBLE
            binding.cvStatistic.visibility = View.VISIBLE
            binding.rvCalonKahim.visibility = View.VISIBLE
            binding.tvWelcome.visibility = View.VISIBLE
            binding.tvTitleCalonKahim.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_logout -> {
                viewModel.logoutUser()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }

            R.id.action_tentang -> {
//                Snackbar.make(binding.root, "Coming Soon Feature", Snackbar.LENGTH_SHORT).show()
                val intent = Intent(this@HomeActivity, SettingsActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        homeActivityBinding = null
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }
}