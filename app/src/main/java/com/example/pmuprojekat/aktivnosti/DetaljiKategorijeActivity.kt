package com.example.pmuprojekat.aktivnosti

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.beust.klaxon.Klaxon
import com.example.pmuprojekat.R
import com.example.pmuprojekat.apihandlers.NorthwindApiHandler
import com.example.pmuprojekat.data.ApiRoutes
import com.example.pmuprojekat.data.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetaljiKategorijeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_item)
        val cId = intent.extras!!["cId"]
        dohvatiKategorije(ApiRoutes.categories + "/${cId}")

    }

    fun obrisiKategoriju(cId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiHandler = NorthwindApiHandler()
                val result = apiHandler.deleteRequest(ApiRoutes.categories + "/${cId}")
                if (result != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            getApplicationContext(),
                            "Usepsno obrisana kategorija",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                } else {
                    print("Error: Get request returned no response")
                }
            } catch (err: Error) {
                print("Error when parsing JSON: " + err.localizedMessage)
            }
        }
    }

    fun snimanjeIzmena(cId: Int, cName: String, cDesc: String) {
        val izmenjenaKategorija = Category(
            cId,
            cName,
            cDesc
        )
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiHandler = NorthwindApiHandler()
                val result = apiHandler.putRequest(
                    ApiRoutes.categories + "/${cId}",
                    Klaxon().toJsonString(izmenjenaKategorija)
                )
                if (result != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            getApplicationContext(),
                            "Usepsno izmenjeni podaci",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                } else {
                    print("Error: Get request returned no response")
                }
            } catch (err: Error) {
                print("Error when parsing JSON: " + err.localizedMessage)
            }
        }
    }

    fun dohvatiKategorije(sUrl: String): Category? {
        var category: Category? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    category = Klaxon().parse(result)

                    withContext(Dispatchers.Main) {
                        val nazivKategorije = findViewById<EditText>(R.id.nazivKategorije)
                        nazivKategorije.setText(category!!.categoryName)
                        val opisKategorije = findViewById<EditText>(R.id.opisKategorije)
                        opisKategorije.setText(category!!.description)
                        val brisanjeKategorije = findViewById<Button>(R.id.kategorijaAkcija2)
                        brisanjeKategorije.text = "Obrisi"
                        brisanjeKategorije.isVisible = true
                        brisanjeKategorije.setOnClickListener { obrisiKategoriju(category!!.categoryId) }
                        val snimiIzmene = findViewById<Button>(R.id.kategorijaAkcija)
                        snimiIzmene.text = "Snimi"
                        snimiIzmene.setOnClickListener {
                            snimanjeIzmena(
                                category!!.categoryId,
                                nazivKategorije.text.toString(),
                                opisKategorije.text.toString()
                            )
                        }

                    }
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }

        }
        return category
    }
}