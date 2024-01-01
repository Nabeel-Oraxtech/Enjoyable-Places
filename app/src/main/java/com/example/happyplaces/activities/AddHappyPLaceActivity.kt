package com.example.happyplaces.activities

import DatabaseHandler
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.happyplaces.R

import com.example.happyplaces.databinding.ActivityAddHappyPlaceBinding
import com.example.happyplaces.models.HappyPlaceModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class AddHappyPLaceActivity : AppCompatActivity(),View.OnClickListener{

    private var cal=Calendar.getInstance()
    private lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    private var saveImageToInternalStorage:Uri?=null
    private var mLatitude:Double=0.0
    private var mLongitude:Double=0.0
    var binding:ActivityAddHappyPlaceBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.tbAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.tbAddPlace?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        dateSetListener=DatePickerDialog.OnDateSetListener {
              view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR,year)
            cal.set(Calendar.MONTH,month)
            cal.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDateView()
        }
        updateDateView()
        binding?.etDate?.setOnClickListener(this)
        binding?.tvAddImage?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.etDate ->{
                DatePickerDialog(this,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.tvAddImage ->{
                val pictureDialog=AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureItems= arrayOf("Select from Gallery","Take from Camera")
                pictureDialog.setItems(pictureItems){
                        _, which ->
                when(which){
                    0 -> choosePhotoFromGallery()
                    1 ->takePhotoFromCamera()
                }
                }
                pictureDialog.show()
            }
            R.id.btnSave ->{
                when{
                    binding?.etTitle?.text.isNullOrEmpty () ->{
                        Toast.makeText(this,"Please Enter the Title",Toast.LENGTH_SHORT).show()
                    }
                    binding?.etLocation?.text.isNullOrEmpty () ->{
                        Toast.makeText(this,"Please Enter the Location",Toast.LENGTH_SHORT).show()
                    }
                    binding?.etDescription?.text.isNullOrEmpty () ->{
                        Toast.makeText(this,"Please Enter the Description",Toast.LENGTH_SHORT).show()
                    }
                    saveImageToInternalStorage==null ->{
                        Toast.makeText(this,"Please Add the Image",Toast.LENGTH_SHORT).show()
                    }
                    else ->{
                       val happyPlaceModel=HappyPlaceModel(
                           0,
                           binding?.etTitle?.text.toString(),
                           saveImageToInternalStorage.toString(),
                           binding?.etDescription?.text.toString(),
                           binding?.etDate?.text.toString(),
                           binding?.etLocation.toString(),
                           mLatitude,
                           mLongitude
                       )
                        val dbHandler=DatabaseHandler(this)
                        val addHappyPlace=dbHandler.addHappyPlace(happyPlaceModel)
                        if(addHappyPlace >0){
                            Toast.makeText(this,"Data has been added Successfully",Toast.LENGTH_LONG).show()
                            finish()
                        }

                    }

                }


            }
        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentUri = data.data
                    try {
                        val selectedImageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
                        saveImageToInternalStorage= saveImageToInternalStorage(selectedImageBitmap)

                        Log.e("Saved Image","path::$saveImageToInternalStorage")

                        binding?.ivPlaceImage?.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "Failed to load the image from gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else if (requestCode == CAMERA) {
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                 saveImageToInternalStorage= saveImageToInternalStorage(thumbnail)

                Log.e("Saved Image","path::$saveImageToInternalStorage")
                binding?.ivPlaceImage?.setImageBitmap(thumbnail)
            }
        }
    }

    private fun takePhotoFromCamera(){

        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(
                report : MultiplePermissionsReport?)
            {
                if(report!!.areAllPermissionsGranted())
                {
                    val galleryIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(galleryIntent, CAMERA)
                }

            }

            override fun onPermissionRationaleShouldBeShown(permissions:MutableList<PermissionRequest>?,token: PermissionToken?)
            {
                rationalDialogForPermissions()

            }

        }).onSameThread().check()

    }

     private fun choosePhotoFromGallery(){
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {


            override fun onPermissionsChecked(
                report : MultiplePermissionsReport?)
            {
                if(report!!.areAllPermissionsGranted())
                {
                val galleryIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }

            }

            override fun onPermissionRationaleShouldBeShown(permissions:MutableList<PermissionRequest>?,token: PermissionToken?)
            {
                rationalDialogForPermissions()

            }

        }).onSameThread().check()
    }
    fun rationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage(""+
            "It looks like you have denied permissions, which are required for this feature." +
                    " It can be enabled under Application Setting")
            .setPositiveButton("GOTO SETTINGS")
            { _,_ ->
                try{
                    val intent=Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri= Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }
                catch (e:ActivityNotFoundException){
                    e.printStackTrace()

                }
            }
            .setNegativeButton("CANCEL"){ dialog, _ ->
                dialog.dismiss()
            }.show()
    }
    fun updateDateView(){
        var format = "dd.MM.yyyy"
        var sdf = SimpleDateFormat(format, Locale.getDefault())
        binding?.etDate?.setText(sdf.format(cal.time).toString())
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap):Uri{
        val wrapper =ContextWrapper(applicationContext)
        var file =wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")
        try {

            val stream:OutputStream=FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
           e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }
    companion object{
        private const val GALLERY=1
        private const val CAMERA=2
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
    }
}