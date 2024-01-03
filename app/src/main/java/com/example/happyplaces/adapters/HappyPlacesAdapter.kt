package com.example.happyplaces.adapters

import DatabaseHandler
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activities.AddHappyPLaceActivity
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.databinding.ItemHappyPlaceBinding
import com.example.happyplaces.models.HappyPlaceModel

 class HappyPlacesAdapter(
     val context: Context,
     var list: ArrayList<HappyPlaceModel>,

):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

     private var onClickListener:OnClickListener?=null

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
            holder.itemView.setOnClickListener{
                if (onClickListener !=null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }
     fun removeAt(position: Int){
         val dbHandler=DatabaseHandler(context)
         val delete=dbHandler.deleteHappyPlace(list[position])
         if (delete>0){
             list.removeAt(position)
             notifyItemRemoved(position)
         }
     }
     fun notifyEditItem(activity: Activity, position: Int, requetCode: Int){
         val intent=Intent(context,AddHappyPLaceActivity::class.java)
         intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
         activity.startActivityForResult(intent,requetCode)
         notifyItemChanged(position)
     }
     fun setOnclickListener(onClickListener: OnClickListener){
         this.onClickListener=onClickListener
     }

     interface OnClickListener{
         fun onClick(position: Int,model: HappyPlaceModel){

         }
     }

}