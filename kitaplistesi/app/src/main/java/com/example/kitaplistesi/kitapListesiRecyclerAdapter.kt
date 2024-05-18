package com.example.kitaplistesi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.kitaplistesi.databinding.RecyclerRowBinding

class kitapListesiRecyclerAdapter(val kitapAdiListesi:ArrayList<String>, val idListesi : ArrayList<Int>) : RecyclerView.Adapter<kitapListesiRecyclerAdapter.kitapHolder>() {
    private lateinit var binding : RecyclerRowBinding

    class kitapHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): kitapHolder {
        // hangi row u hangi tasarımla olusturulacagını belirttiğimiz fonksiyon

        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerRowBinding.inflate(inflater, parent, false)
        return kitapHolder(binding)
    }

    override fun getItemCount(): Int { // kac tane olusturulacagını belirttiğimiz fonksıyon

        return kitapAdiListesi.size
    }

    override fun onBindViewHolder(holder: kitapHolder, position: Int) { // holderı  kullanarak recycler_row içerisinde neler yazılacagını belirttiğimiz kısım
        holder.binding.kitapAdiText.text= kitapAdiListesi[position]
        holder.binding.root.setOnClickListener{
            val action = kitapadifragmentiDirections.actionKitapadifragmentiToDetaylarfragmenti("recyclerdengeldim",idListesi[position])
            Navigation.findNavController(it).navigate(action)

        }


    }
}