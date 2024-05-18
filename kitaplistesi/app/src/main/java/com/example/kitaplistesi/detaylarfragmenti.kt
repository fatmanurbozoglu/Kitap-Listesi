package com.example.kitaplistesi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import  android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.kitaplistesi.databinding.FragmentDetaylarfragmentiBinding
import java.io.ByteArrayOutputStream
//import java.lang.Exception
import androidx.navigation.Navigation.findNavController
import kotlin.Exception


@Suppress("DEPRECATION")
class detaylarfragment : Fragment() {
    //var secilenGorsel : Uri? = null
    var selectedBitmap: Bitmap? = null

    private lateinit var binding: FragmentDetaylarfragmentiBinding

    private val permissionStorage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // versiyonu kontrol ettık tıramısu(13 versiyonu icin)
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) // 13 altı versiyon izinleri için
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetaylarfragmentiBinding.inflate(layoutInflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_detaylarfragmenti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let {
            if (!(ContextCompat.checkSelfPermission(it, permissionStorage[0]) == PackageManager.PERMISSION_GRANTED)) {
                requestPermissionLauncher.launch(permissionStorage) // eger izin verilmemişse izni iste
            }
        }

        binding.button.setOnClickListener {
            saveToSqLite(it)
            val action= detaylarfragmentDirections.actionDetaylarfragmentiToKitapadifragmenti() // hata verirse navigation_graph.xml kısmını kontrol et
            findNavController(it).navigate(action)
        }

        binding.imageView.setOnClickListener {

            //gorselSec(it)
            getContent.launch("image/*")
        }
        arguments?.let {
            var gelenBilgi = detaylarfragmentArgs.fromBundle(it).bilgiler
            if (gelenBilgi.equals("I came from the menu")){
                // yeni bir kitap eklemeye geldi
                binding.bookNameTextView.setText("")
                binding.detailTextView.setText("")
                binding.button.visibility = View.VISIBLE

                val gorselSecmeArkaPlani = BitmapFactory.decodeResource(context?.resources,R.drawable.gorselsecimi)
                binding.imageView.setImageBitmap(gorselSecmeArkaPlani)
            }
            else{
                // daha önce olusturulan kitap bilgilerini görmeye geldi

                binding.button.visibility = View.INVISIBLE
                val secilenId = detaylarfragmentArgs.fromBundle(it).id
                context?.let {
                    try {
                        val db = it.openOrCreateDatabase("KİTAPLAR",Context.MODE_PRIVATE,null)
                        var cursor = db.rawQuery("SELECT * FROM kitaplar WHERE id = ?", arrayOf(secilenId.toString()))

                        val kitapAdiIndex = cursor.getColumnIndex("kitapAdi")
                        val kitapDetayIndex = cursor.getColumnIndex("kitapDetay")
                        val gorselIndex = cursor.getColumnIndex("gorsel")


                        while (cursor.moveToNext()){
                            binding.bookNameTextView.setText(cursor.getString(kitapAdiIndex))
                            binding.detailTextView.setText(cursor.getString(kitapDetayIndex))
                            val byteDizisi = cursor.getBlob(gorselIndex)
                            val bitmap = BitmapFactory.decodeByteArray(byteDizisi,0,byteDizisi.size)
                            binding.imageView.setImageBitmap(bitmap)
                        }
                        cursor.close()
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            }
        }

    }


    fun saveToSqLite(view: View){
        // SQLİTE' a kaydetme
        val bookName = binding.bookNameTextView.text.toString()
        val bookNameDetail = binding.detailTextView.text.toString()

        if (selectedBitmap != null){
            val smallBitmap = reduceBitmapSize(selectedBitmap!!, 300)
            // Bitmap veriyi kaydetme
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50, outputStream)
            val byteArray = outputStream.toByteArray()
            try {
                context?.let {
                    val database = it.openOrCreateDatabase("BOOKS", Context.MODE_PRIVATE,null)
                    database.execSQL("CREATE TABLE IF NOT EXISTS books(id INTEGER PRİMARY KEY, bookName VARCHAR, bookNameDetail VARCHAR, image BLOB)")

                    val sqlString = "INSERT INTO books (bookName,bookNameDetail, image) VALUES (? ,? ,?)"
                    val statement = database.compileStatement(sqlString)

                    statement.bindString(1, bookName)
                    statement.bindString(2, bookNameDetail)
                    statement.bindBlob(3, byteArray)
                    statement.execute()
                }
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

    }
    

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->

        if (Build.VERSION.SDK_INT >= 28){ // versiyonu kontrol ettik
            val source = ImageDecoder.createSource(requireContext().contentResolver, result!!)
            selectedBitmap= ImageDecoder.decodeBitmap(source)
            binding.imageView.setImageBitmap(selectedBitmap)
        }else{
            selectedBitmap= MediaStore.Images.Media.getBitmap(requireContext().contentResolver, result)
            binding.imageView.setImageBitmap(selectedBitmap)
        }
    }

    fun reduceBitmapSize(kullanicininSectigiBitmap: Bitmap, maximumBoyut : Int): Bitmap{
        var widht = kullanicininSectigiBitmap.width
        var height = kullanicininSectigiBitmap.height

        val bitmapOrani : Double = widht.toDouble() / height.toDouble()
        if (bitmapOrani > 1){
            // gorsel yatay
            widht = maximumBoyut
            var kisaltilmisHeight = widht / bitmapOrani
            height = kisaltilmisHeight.toInt()

        }
        else{
            //gorsel dıkey
            height = maximumBoyut
            var kisaltilmisWidth = height * bitmapOrani
            widht = kisaltilmisWidth.toInt()

        }
        return Bitmap.createScaledBitmap(selectedBitmap!!,widht,height,true)
    }

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        var isGranted = true

        for (items in map) {
            if (!items.value) {
                isGranted = false
            }
        }

        if (isGranted) {
            // izin verilirse ekrara toast mesajı verir
            //Toast.makeText(this, "izin verildi", Toast.LENGTH_LONG).show()
        }
    }


}
