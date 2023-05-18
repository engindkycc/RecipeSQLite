package com.engindkyc.recipesqlite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.engindkyc.recipesqlite.databinding.FragmentSpecificationBinding
import java.io.ByteArrayOutputStream


class SpecificationFragment : Fragment() {
    var pickedPhoto : Uri? = null
    var pickedBitMap : Bitmap? = null
    private var _binding: FragmentSpecificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpecificationBinding.inflate(inflater,container,false)
        val view = binding.root
        return  view

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            foodSave(it)

        }

        binding.imageView.setOnClickListener {
            photoChoice(it)
        }

        arguments?.let{

            var gelenBilgi = SpecificationFragmentArgs.fromBundle(it).info

            if(gelenBilgi.equals("menudengeldim")){
                //Yeni bir yemek eklemeye geldi
                binding.foodName.setText("")
                binding.foodMaterial.setText("")
                binding.foodSpecification.setText("")
                binding.buttonSave.visibility = View.VISIBLE

                val imageChooseBackground = BitmapFactory.decodeResource(context?.resources,R.drawable.choicephoto)
                binding.imageView.setImageBitmap(imageChooseBackground)
            }else{
                //daha önce oluşturulan yemeği görmeye geldi
                binding.buttonSave.visibility = View.INVISIBLE
                val chooseId = SpecificationFragmentArgs.fromBundle(it).id

                context?.let {

                    try{

                        val db = it.openOrCreateDatabase("Foods",Context.MODE_PRIVATE,null)
                        val cursor = db.rawQuery("SELECT * FROM food WHERE id = ?", arrayOf(chooseId.toString()))

                        val foodNameIndex = cursor.getColumnIndex("foodname")
                        val foodMaterialIndex = cursor.getColumnIndex("foodmaterial")
                        val foodImage = cursor.getColumnIndex("imagefood")
                        val foodSpecification = cursor.getColumnIndex("foodspecification")

                        while(cursor.moveToNext()){

                            binding.foodName.setText(cursor.getString(foodNameIndex))
                            binding.foodMaterial.setText(cursor.getString(foodMaterialIndex))
                            binding.foodSpecification.setText(cursor.getString(foodSpecification))

                            val byteArray = cursor.getBlob(foodImage)
                            val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                            binding.imageView.setImageBitmap(bitmap)

                        }
                        cursor.close()

                    }catch (e:Exception){
                        e.printStackTrace()


                    }


                }

            }
        }

    }


    private fun foodSave(view: View){
        //SQLite a kaydetme
        val foodName = binding.foodName.text.toString()
        val foodMaterial = binding.foodMaterial.text.toString()
        val foodSpecification = binding.foodSpecification.text.toString()

        if(pickedPhoto != null){

            val smallBitmap = createSmallBitmap(pickedBitMap!!,300)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()


            try {

                context?.let {

                    val database = it.openOrCreateDatabase("Foods",Context.MODE_PRIVATE,null)
                    database.execSQL("CREATE TABLE IF NOT EXISTS food (id INTEGER PRIMARY KEY , imagefood BLOB , foodname VARCHAR , foodmaterial VARCHAR , foodspecification VARCHAR )")
                    val sqlString = "INSERT INTO food (imagefood , foodname , foodmaterial , foodspecification ) VALUES (? , ? , ? , ?)"
                    val statement = database.compileStatement(sqlString)
                    statement.bindBlob(1, byteArray)
                    statement.bindString(2,foodName)
                    statement.bindString(3,foodMaterial)
                    statement.bindString(4,foodSpecification)
                    statement.execute()
                }



            }catch (e: Exception){

                e.printStackTrace()

            }

            val action = SpecificationFragmentDirections.actionSpecificationFragmentToListFragment()
            Navigation.findNavController(view).navigate(action)

        }



    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun photoChoice(view: View){

        //Context Compat API lar arasındaki uyumsuzluğu gidermek için kullanılır.
        //CheckSelfPermission ile izin kontrol edilir.
        //context applicationContext verilir.
        //Daha sonra hangi iznin kontrol edileceği yazılır.
        //İzin verilmedi izin istememiz gerekiyor.
        //izin zzaten verilmiş , tekrar istemeden galeriye git

        activity?.let {

            if(ContextCompat.checkSelfPermission(it.applicationContext,android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(it,arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                    1)

            } else {

                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)

            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data

            try {
                context?.let{

                    if(pickedPhoto != null ) {

                        if(Build.VERSION.SDK_INT >= 28){

                            val source = ImageDecoder.createSource(it.contentResolver, pickedPhoto!!)
                            pickedBitMap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(pickedBitMap)
                        }
                        pickedBitMap = MediaStore.Images.Media.getBitmap(it.contentResolver,pickedPhoto)
                        binding.imageView.setImageBitmap(pickedBitMap)
                    }
                }

            }catch (e: Exception){
                e.printStackTrace()

            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //Create small bitmap
    fun createSmallBitmap(userChooseBitmap: Bitmap , maxDimension: Int) : Bitmap{

        var width = userChooseBitmap.width
        var height = userChooseBitmap.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()

        if(bitmapRatio > 1) {
            //görsel yataydır.
            width = maxDimension
            val abridgedHeight = width / bitmapRatio
            height = abridgedHeight.toInt()

        }else{
            //görsel dikeydir.
            height = maxDimension
            val abridgedWidth = height  * bitmapRatio
            width = abridgedWidth.toInt()

        }

        return  Bitmap.createScaledBitmap(userChooseBitmap,width,height,true)

    }


}








