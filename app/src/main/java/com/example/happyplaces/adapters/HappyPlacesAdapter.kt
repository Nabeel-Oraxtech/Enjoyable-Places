package com.example.happyplaces.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.databinding.ItemHappyPlaceBinding
import com.example.happyplaces.models.HappyPlaceModel

 class HappyPlacesAdapter(
     val context: Context,
     var list: ArrayList<HappyPlaceModel>
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
      class MyViewHolder(binding: ItemHappyPlaceBinding):RecyclerView.ViewHolder(binding.root){
          val ivPlaceImage=binding.ivPlaceImage
          val tvTitle=binding.tvTitle
          val tvDescription=binding.tvDescription
      }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemHappyPlaceBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.ivPlaceImage.setImageURI(Uri.parse(model.image))
            holder.tvTitle.text = model.title
            holder.tvDescription.text = model.description
        }
    }

}