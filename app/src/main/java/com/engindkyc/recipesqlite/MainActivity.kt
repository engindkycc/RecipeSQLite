package com.engindkyc.recipesqlite

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import com.engindkyc.recipesqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
        private lateinit var binding : ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
                val menuInflater = menuInflater
                menuInflater.inflate(R.menu.food_add,menu)
                return super.onCreateOptionsMenu(menu)

        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {

                if(item.itemId == R.id.food_add_item) {

                        val action = ListFragmentDirections.actionListFragmentToSpecificationFragment("menudengeldim",5)
                        Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)

                }
                return super.onOptionsItemSelected(item)
        }


}