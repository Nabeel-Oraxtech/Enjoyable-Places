package com.example.happyplaces.activities

import DatabaseHandler
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplaces.adapters.HappyPlacesAdapter
import com.example.happyplaces.databinding.ActivityMainBinding
import com.example.happyplaces.models.HappyPlaceModel


class MainActivity : AppCompatActivity() {
    var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    binding?.fabAddHappyPlace?.setOnClickListener{
        val intent=Intent(this, AddHappyPLaceActivity::class.java)
        startActivity(intent)
    }
        getHappyPlaceListFromLocalDB()
    }

    fun setupHappyPlacesRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>){
        binding?.rvHappyPlacesList?.layoutManager=LinearLayoutManager(this)
        binding?.rvHappyPlacesList?.setHasFixedSize(true)

        val placesAdapter=HappyPlacesAdapter(this,happyPlaceList)
        binding?.rvHappyPlacesList?.adapter=placesAdapter
    }

    fun getHappyPlaceListFromLocalDB(){
        val dbHandler=DatabaseHandler(this)
        val getHappyPlaceList : ArrayList<HappyPlaceModel> = dbHandler.addHappyPlaceList()
        if(getHappyPlaceList.size >0){
           binding?.rvHappyPlacesList?.visibility=View.VISIBLE
            binding?.tvNoPlaceFound?.visibility=View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList )
            }
        else{
            binding?.rvHappyPlacesList?.visibility=View.GONE
            binding?.tvNoPlaceFound?.visibility=View.VISIBLE
        }
        }
    }
