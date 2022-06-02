package com.example.pmuprojekat.aktivnosti

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.apitest.viewmodels.CategoryViewModel
import com.example.pmuprojekat.R
import com.example.pmuprojekat.apihandlers.NorthwindApiHandler
import com.example.pmuprojekat.data.ApiRoutes
import com.example.pmuprojekat.data.Category
import com.example.pmuprojekat.viewadapters.CategoryViewAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KategorijeActivity : AppCompatActivity() {
    val viewModel: CategoryViewModel by viewModels()
    var categoryViewAdapter: CategoryViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategorije)
        dohvatiKategorije(this, ApiRoutes.categories)
    }

    private fun dohvatiKategorije(ctx: Context, sUrl: String): List<Category>? {
        var categories: List<Category>? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(sUrl)
            if (result != null) {
                try {
                    // Parse result string JSON to data class
                    categories = Klaxon().parseArray(result)

                    withContext(Dispatchers.Main) {
                        viewModel.lstKategorije.value = categories
                        val lstKategorijeView = findViewById<RecyclerView>(R.id.lstKategorijeView)
                        lstKategorijeView.layoutManager = LinearLayoutManager(ctx)
                        categoryViewAdapter = CategoryViewAdapter(ctx, viewModel.lstKategorije)
                        lstKategorijeView.adapter = categoryViewAdapter
                    }
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }

        }
        return categories
    }

    fun prikaziUnosKategorije(view: View) {
        setContentView(R.layout.category_item)
        val categoryButton = findViewById<Button>(R.id.kategorijaAkcija)
        categoryButton.text = "Dodaj"
        val nazivKategorije = findViewById<EditText>(R.id.nazivKategorije)
        val opisKategorije = findViewById<EditText>(R.id.opisKategorije)
        categoryButton.setOnClickListener {
            val novaKategorija = Category(
                0,
                nazivKategorije.text.toString(),
                opisKategorije.text.toString()
            )
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val apiHandler = NorthwindApiHandler()
                    val result = apiHandler.postRequest(
                        ApiRoutes.categories,
                        Klaxon().toJsonString(novaKategorija)
                    )
                    if (result != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(getApplicationContext(), "Usepsno dodavanje kategorije", Toast.LENGTH_LONG)
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
    }





}