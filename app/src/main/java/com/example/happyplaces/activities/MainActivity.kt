package com.example.happyplaces.activities

import DatabaseHandler
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.adapters.HappyPlacesAdapter
import com.example.happyplaces.databinding.ActivityMainBinding
import com.example.happyplaces.models.HappyPlaceModel
import com.happyplaces.utils.SwipeToDeleteCallback
import pl.kitek.rvswipetodelete.SwipeToEditCallback


class MainActivity : AppCompatActivity() {
    var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    binding?.fabAddHappyPlace?.setOnClickListener{
        val intent=Intent(this, AddHappyPLaceActivity::class.java)
        startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
    }
        getHappyPlaceListFromLocalDB()
    }

    private fun setupHappyPlacesRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>){
        binding?.rvHappyPlacesList?.layoutManager=LinearLayoutManager(this)
        binding?.rvHappyPlacesList?.setHasFixedSize(true)

        val placesAdapter=HappyPlacesAdapter(this,happyPlaceList)
        binding?.rvHappyPlacesList?.adapter=placesAdapter

        placesAdapter.setOnclickListener(object :HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent=Intent(this@MainActivity,
                    HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,model)
                startActivity(intent)

            }
        })

        var editSwipeHandler= object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter=binding?.rvHappyPlacesList?.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity,viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }
        val editItemTouchHelper=ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding?.rvHappyPlacesList)
        var deleteSwipeHandler= object :SwipeToDeleteCallback(this){

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter=binding?.rvHappyPlacesList?.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyPlaceListFromLocalDB()

            }
        }
        val deleteItemTouchHelper=ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding?.rvHappyPlacesList)
    }

    private fun getHappyPlaceListFromLocalDB(){
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if (resultCode== Activity.RESULT_OK){
                getHappyPlaceListFromLocalDB()
            }
        }
        else{
            Log.e("Activity","Cancell or BackPressed")
        }
    }

    companion object{
        var ADD_PLACE_ACTIVITY_REQUEST_CODE=1
        var EXTRA_PLACE_DETAILS="extra place details"
    }

}