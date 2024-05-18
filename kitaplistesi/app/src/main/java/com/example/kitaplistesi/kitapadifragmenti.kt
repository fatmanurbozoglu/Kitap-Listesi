package com.example.kitaplistesi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kitaplistesi.databinding.FragmentKitapadifragmentiBinding
import androidx.recyclerview.widget.LinearLayoutManager


class kitapadifragmenti : Fragment() {
    var bookTitleList = ArrayList<String>()
    var idList = ArrayList<Int>()
    private lateinit var listeAdapter: kitapListesiRecyclerAdapter


    private lateinit var binding : FragmentKitapadifragmentiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKitapadifragmentiBinding.inflate(layoutInflater)
        return binding.root
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_kitapadifragmenti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeAdapter = kitapListesiRecyclerAdapter(bookTitleList,idList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = listeAdapter

        getDataSqLite()
    }

    fun getDataSqLite(){
        try {
            context?.let {
                val database = it.openOrCreateDatabase("BOOKS", Context.MODE_PRIVATE,null)
                val cursor = database.rawQuery("SELECT * FROM books", null)
                val bookNameIndex = cursor.getColumnIndex("bookName")
                val idIndex = cursor.getColumnIndex("id")

                bookTitleList.clear()
                idList.clear()

                while (cursor.moveToNext()){
                    bookTitleList.add(cursor.getString(bookNameIndex))
                    idList.add(cursor.getInt(idIndex))
                }
                listeAdapter.notifyDataSetChanged()
                cursor.close()
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}