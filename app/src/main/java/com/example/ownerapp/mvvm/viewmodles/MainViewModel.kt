package com.example.ownerapp.mvvm.viewmodles

import androidx.lifecycle.ViewModel
import com.example.ownerapp.data.Product
import com.example.ownerapp.data.ProductCategory
import com.example.ownerapp.mvvm.repository.MainRepository

class MainViewModel constructor(var repository: MainRepository) : ViewModel() {


    val allCategories = repository.getCategoriesInfo()

    fun fetchAllBranches() = repository.fetchBranches()

    fun signOut() {
        repository.signOut()
    }

    fun sendUserToLoginActivity() {
        repository.sendUserToLoginActivity()
    }

    fun getAllPlans() = repository.fetchAllPlans()
    fun sendUserToViewPlanActivity() = repository.sendUserToViewPlanActivity()
    fun addProduct(product: Product) {
        repository.addProduct(product)
    }

    fun addCategory(category: ProductCategory) = repository.addCategory(category)
    fun loadProducts(name: String) = repository.loadAllProducts(name)
}