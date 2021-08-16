package com.example.ownerapp.Fragments

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.example.ownerapp.databinding.FragmentBranchesBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Branches : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentBranchesBinding? = null
    private val binding get() = _binding!!
    lateinit var currentUser: FirebaseUser
    var mAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBranchesBinding.inflate(inflater, container, false)
        init()
        binding.fabAddBranch.setOnClickListener {
            val inputEditTextField = EditText(requireContext())
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Create New Branch")
                .setMessage("Enter name of Branch")
                .setView(inputEditTextField)
                .setPositiveButton(
                    "Create"
                ) { _, _ ->
                    val branchName = inputEditTextField.text.toString()
                    if (branchName.isNotEmpty()) {
                        viewModel.addNewBranch(branchName)
                    }
                    Log.d(TAG, "onCreateView: $branchName")
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }


        return binding.root
    }

    private fun init() {
        val component: DaggerFactoryComponent = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel =
            ViewModelProviders.of(this@Branches, component.getFactory())
                .get(MainViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!

    }
}

