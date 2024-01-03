package com.example.happyplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.R
import com.example.happyplaces.databinding.ActivityHappyPlaceDetailBinding
import com.example.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {

    var binding:ActivityHappyPlaceDetailBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        var happyPlaceDetailModel:HappyPlaceModel?=null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel=intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }
        if(happyPlaceDetailModel!=null){
            setSupportActionBar(binding?.tbHappyPlaceDetail)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title=happyPlaceDetailModel.title

            binding?.tbHappyPlaceDetail?.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            binding?.ivPlaceImage?.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            binding?.tvDescription?.text=happyPlaceDetailModel.description
            binding?.tvLocation?.text=happyPlaceDetailModel.location

        }
    }
}