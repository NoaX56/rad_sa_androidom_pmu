package com.example.pmuprojekat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pmuprojekat.aktivnosti.KategorijeActivity
import com.example.pmuprojekat.aktivnosti.ProizvodiActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun otvoriAktivnostKategorije(view: View) {
        val intent= Intent(this,KategorijeActivity::class.java)
        startActivity(intent)
    }
    fun otvoriAktivnostProizvoda(view: View) {
        val intent= Intent(this,ProizvodiActivity::class.java)
        startActivity(intent)
    }

}