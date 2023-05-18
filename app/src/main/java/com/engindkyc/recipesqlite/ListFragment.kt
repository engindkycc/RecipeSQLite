package com.engindkyc.recipesqlite

import android.annotation.SuppressLint
import android.content.AbstractThreadedSyncAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.engindkyc.recipesqlite.databinding.FragmentListBinding
import java.sql.Blob

private var _binding : FragmentListBinding ?= null
private val binding get() = _binding!!

class ListFragment : Fragment() {

    var foodNameList = ArrayList<String>()
    var foodIdList   = ArrayList<Int>()
    //var foodMaterialList = ArrayList<String>()
    //var foodSpecificationList = ArrayList<String>()

    private lateinit var listAdapter: ListRecyclerAdapter
    //var imageFoodList = ArrayList<Blob>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater,container,false)
        val view = binding.root
        return  view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sqlDataImport()


        listAdapter = ListRecyclerAdapter(foodNameList , foodIdList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = listAdapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sqlDataImport(){

        activity?.let {

            try {

                val database = it.openOrCreateDatabase("Foods",Context.MODE_PRIVATE,null)
                val cursor = database.rawQuery("SELECT * FROM food",null)
                val foodIdIndex = cursor.getColumnIndex("id")
                //val imageFoodIndex = cursor.getColumnIndex("imagefood")
                val foodNameIndex = cursor.getColumnIndex("foodname")
                val foodMaterialIndex = cursor.getColumnIndex("foodmaterial")
                val foodSpecificationIndex = cursor.getColumnIndex("foodspecification")

                //imageFoodList.clear()
                foodIdList.clear()
                foodNameList.clear()
                //foodMaterialList.clear()
                //foodSpecificationList.clear()


                while (cursor.moveToNext()){

                        //println(cursor.getInt(foodId))
                        //println(cursor.getBlob(imageFoodIndex))
                        //println(cursor.getString(foodNameIndex))
                        //println(cursor.getString(foodMaterialIndex))
                        //println(cursor.getString(foodSpecificationIndex))
                        foodIdList.add(cursor.getInt(foodIdIndex))
                        foodNameList.add(cursor.getString(foodNameIndex))
                        //foodMaterialList.add(cursor.getString(foodMaterialIndex))
                        //foodSpecificationList.add(cursor.getString(foodSpecificationIndex))
                        //imageFoodList.add(cursor.getInt(imageFoodIndex)))


                }

                listAdapter.notifyDataSetChanged()
                cursor.close()

            }catch (e: Exception){
                e.printStackTrace()

            }

        }




    }

    

}