package com.example.kitaplistesi


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation.findNavController
import com.example.kitaplistesi.databinding.ActivityMainBinding

//action barı eklemek için values - themes kısmına gecıp themes.xml kısmından noactıonbar kısmını kaldır
// android.permission.READ_MEDIA_IMAGES 13 ve üzeri için izinlerde
// android.permission.WRITE_EXTERNAL_STORAGE 13 ve altı izinler için
// android.permission.READ_EXTERNAL_STORAGE 13 ve altı izinler için

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    // versiyonları kontrol ettık ona gore 13 ve ustu için ve altı için hangi izinleri kontrol edecegını yazdık
    // kullanılacak izinleri androıdManifest.xml de ekledık
    /*private val permissionStorage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // versiyonu kontrol ettık tıramısu(13 versiyonu icin)
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) // 13 altı versiyon izinleri için
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // parantez dısındaki ! icerideki kodun değilini alır
       /* if (!(ContextCompat.checkSelfPermission(this, permissionStorage[0]) == PackageManager.PERMISSION_GRANTED)) {
            requestPermissionLauncher.launch(permissionStorage) // eger izin verilmemişse izni iste
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater= menuInflater
        menuInflater.inflate(R.menu.add_book,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.add_book_item){
            val action = kitapadifragmentiDirections.actionKitapadifragmentiToDetaylarfragmenti("I came from the menu",0)
            findNavController(this, R.id.fragmentContainerView).navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }



    // requestPermissionLauncher izin isteme işlemini gerçekleştirebilir ve izin sonuçlarını daha basit bir şekilde yönetebilirsiniz.
    // RequestMultiplePermissions birden fazla izin işleminde kullanılır
    // registerForActivityResult Activity veya Fragment içinde başlatılan bir etkinlik veya izin isteği gibi(etkinlik sonuçları, izin sonuçları, kamera görüntüsü alma sonuçları vb.) sonuçları işlemek için kullanılır
    // ActivityResultContracts işlem sonuçlarını yönetmek
    /*val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        var isGranted = true

        for (items in map) {
            if (!items.value) {
                isGranted = false
            }
        }

        if (isGranted) {
            // izin verilirse ekrara toast mesajı verir
            Toast.makeText(this, "izin verildi",Toast.LENGTH_LONG).show()
        }
    }*/


}