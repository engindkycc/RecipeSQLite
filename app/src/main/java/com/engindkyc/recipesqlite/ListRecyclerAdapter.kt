package com.engindkyc.recipesqlite

import android.R
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.engindkyc.recipesqlite.databinding.RecyclerRowBinding


class ListRecyclerAdapter(val foodList:ArrayList<String> , val idList:ArrayList<Int> ) : RecyclerView.Adapter<ListRecyclerAdapter.FoodHolder>() {


    class FoodHolder (val binding:RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        return FoodHolder(RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        holder.binding.recyclerRowText.text = foodList[position]
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToSpecificationFragment("recyclerdangeldim", idList[position])
            Navigation.findNavController(it).navigate(action)


        }

    }


}