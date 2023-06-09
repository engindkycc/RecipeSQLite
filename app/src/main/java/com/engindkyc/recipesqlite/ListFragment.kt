package com.engindkyc.recipesqlite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.engindkyc.recipesqlite.databinding.FragmentListBinding

private var _binding : FragmentListBinding ?= null
private val binding get() = _binding!!

class ListFragment : Fragment() {

    private var foodNameList = ArrayList<String>()
    private var foodIdList   = ArrayList<Int>()
    private lateinit var listAdapter: ListRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

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
                val foodNameIndex = cursor.getColumnIndex("foodname")

                foodIdList.clear()
                foodNameList.clear()

                while (cursor.moveToNext()){

                        foodIdList.add(cursor.getInt(foodIdIndex))
                        foodNameList.add(cursor.getString(foodNameIndex))

                }

                listAdapter.notifyDataSetChanged()
                cursor.close()

            }catch (e: Exception){
                e.printStackTrace()

            }

        }

    }

}