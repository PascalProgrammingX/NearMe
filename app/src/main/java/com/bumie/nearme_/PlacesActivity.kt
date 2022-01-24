package com.bumie.nearme_

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumie.nearme_.databinding.ActivityPlacesBinding

class PlacesActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPlacesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        val placesArray = intent.getParcelableArrayListExtra<Datum>("places")
        Log.d("debugger: DISPLAY", placesArray?.size.toString())
        val viewPlacesAdapter = ViewPlacesAdapter(this, placesArray!!)
        binding.recyclerView.adapter = viewPlacesAdapter

    }

}