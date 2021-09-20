package com.example.ownerapp.activities

import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.R
import com.example.ownerapp.data.Plan
import com.example.ownerapp.databinding.ActivityAddNewPlanBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.PlanRepository
import com.example.ownerapp.mvvm.viewmodles.NewPlanViewModel

class AddNewPlan : AppCompatActivity() {
    private lateinit var viewModel: NewPlanViewModel
    private lateinit var component: DaggerFactoryComponent
    lateinit var binding: ActivityAddNewPlanBinding
    var onceClicked = false
    var arraylistType = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        val durationType = resources.getStringArray(R.array.durationType)
        arraylistType = ArrayList<String>(listOf(*resources.getStringArray(R.array.durationType)))
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdownitem, durationType)
        binding.typeTime.setAdapter(arrayAdapter)

        Log.d(TAG, "onCreate: ${arraylistType.size}")

        binding.btnConPlan.setOnClickListener {
            val name = binding.planNameEdit.text.toString()
            val desc = binding.descPlanEdit.text.toString()
            val timeNumber = binding.numberDays.text.toString()
            val type = binding.typeTime.text.toString()
            val fees = binding.feesPlan.text.toString()

            if (name.isNotEmpty() && desc.isNotEmpty() && timeNumber.isNotEmpty() && type.isNotEmpty() && fees.isNotEmpty()) {
                if (arraylistType.contains(type)) {
                    if (!onceClicked) {
                        viewModel.repository.addNewPlan(
                            Plan(
                                name,
                                desc,
                                timeNumber,
                                type,
                                fees
                            )
                        )
                        onceClicked = true
                        viewModel.repository.sendUserToMainActivity()
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Wrong Duration Type", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill the Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun init(){
        //Toolbar stuff
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.my_statusbar_color)

        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(this))
            .factoryModule(FactoryModule(PlanRepository(this)))
            .build() as DaggerFactoryComponent

        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(NewPlanViewModel::class.java)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        resources.configuration.uiMode = Configuration.UI_MODE_NIGHT_YES
        setTheme(R.style.Theme_AppTheme_Dark)
    }
}