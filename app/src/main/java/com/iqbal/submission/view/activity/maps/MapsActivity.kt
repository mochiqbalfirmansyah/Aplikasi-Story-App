package com.iqbal.submission.view.activity.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.iqbal.submission.R
import com.iqbal.submission.data.api.response.ListStoryItem
import com.iqbal.submission.data.api.response.LoginResult
import com.iqbal.submission.data.local.pref.UserPreference
import com.iqbal.submission.data.local.pref.dataStore
import com.iqbal.submission.databinding.ActivityMapsBinding
import com.iqbal.submission.utils.Result
import com.iqbal.submission.view.activity.login.LoginActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels {
        MapsViewModelFactory.getInstance(this)
    }

    private lateinit var pref: UserPreference
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference.getInstance(dataStore)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        lifecycleScope.launch {
            pref.getToken().collect { token ->
                if (token.isEmpty()) {
                    Toast.makeText(this@MapsActivity, "Token is null or empty", Toast.LENGTH_SHORT).show()
                    return@collect
                }

                val tokenString = "Bearer $token"
                // Log token for debugging
                Log.d("Token", "Token: $tokenString")

                viewModel.getMaps().observe(this@MapsActivity) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val mapsData = it.data.listStory
                                addManyMarker(mapsData!!)
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@MapsActivity,
                                    resources.getString(R.string.story_notFound),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addManyMarker(maps: List<ListStoryItem>) {
        maps.forEach {
            if (it.lat != null && it.lon != null) {
                val latLng = LatLng(it.lat, it.lon)
                mMap.addMarker(MarkerOptions().position(latLng).title(it.name))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
