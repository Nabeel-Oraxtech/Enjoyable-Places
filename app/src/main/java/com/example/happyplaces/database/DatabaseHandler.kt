package com.example.happyplaces.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context):
      SQLiteOpenHelper(context,DATABASE_NAME ,null, DATABASE_VERSION){
          companion object{
              private const val DATABASE_VERSION=1
              private const val DATABASE_NAME="HappyPlacesDatabase"
              private const val TABLE_HAPPY_PLACE ="HappyPlacesTable"

              private const val KEY_ID="id"
              private const val KEY_TITLE="title"
              private const val KEY_IMAGE="image"
              private const val KEY_DESCRIPTION="description"
              private const val KEY_DATE="date"
              private const val KEY_LOCATION="location"
              private const val KEY_LATITUDE="latitude"
              private const val KEY_LONGITUDE="longitude"

          }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_HAPPY_PLACE_TABLE =
            ("CREATE TABLE" + TABLE_HAPPY_PLACE + "("
                + KEY_ID +"INTEGER PRIMARY KEY,"
                + KEY_TITLE +"TEXT,"
                + KEY_IMAGE +"TEXT,"
                + KEY_DESCRIPTION +"TEXT,"
                + KEY_DATE +"TEXT,"
                + KEY_LOCATION +"TEXT,"
                + KEY_LATITUDE +"TEXT,"
                + KEY_LONGITUDE +"TEXT)")
        db?.execSQL(CREATE_HAPPY_PLACE_TABLE)
    }
      }
