package com.example.apitest.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pmuprojekat.data.Category

class CategoryViewModel: ViewModel() {
    val lstKategorije= MutableLiveData<List<Category>>()
    val kategorija=MutableLiveData<Category>()
}