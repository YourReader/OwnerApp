package com.example.ownerapp.Fragments.productFrags

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ownerapp.Adapters.CartAdapter
import com.example.ownerapp.Utils.Constants.ACCEPTED
import com.example.ownerapp.Utils.Constants.DECLINED
import com.example.ownerapp.data.Cart
import com.example.ownerapp.databinding.FragmentOrdersBinding
import com.example.ownerapp.di.components.DaggerFactoryComponent
import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.repository.MainRepository
import com.example.ownerapp.mvvm.viewmodles.MainViewModel

class Orders : Fragment() {
    private lateinit var cartAdapter: CartAdapter
    private lateinit var component: DaggerFactoryComponent
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        component = DaggerFactoryComponent.builder()
            .repositoryModule(RepositoryModule(requireContext()))
            .factoryModule(FactoryModule(MainRepository(requireContext())))
            .build() as DaggerFactoryComponent
        viewModel = ViewModelProviders.of(this, component.getFactory())
            .get(MainViewModel::class.java)
        cartAdapter = CartAdapter(requireContext())
        cartAdapter.setListener(object : CartAdapter.buttonListeners {
            @SuppressLint("NotifyDataSetChanged")
            override fun onAcceptListener(cart: Cart) {
                //Accepting the Order i.e. updating the status to ACCEPTED
                Toast.makeText(requireContext(), "Order Accepted", Toast.LENGTH_SHORT).show()
                viewModel.changeOrderStatus(cart, ACCEPTED)
            }

            override fun onDeleteListener(cart: Cart) {
                /*
                * Change the status to Declined i.e. Deleted  -->Done!
                * Show a dialog box before actually deleting the order -->Aditya's Work
                * once the order is deleted send a notification to user about it -->Will do in js
                * and tell owner to return the money to the user -->Aditya's Work
                */
                Toast.makeText(requireContext(), "Order Deleted", Toast.LENGTH_SHORT).show()
                viewModel.changeOrderStatus(cart, DECLINED)
            }
        })
        binding.apply {
            recyclerViewCartItems.apply {
                adapter = cartAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.allPendingOrder.observe(requireActivity()) {
            Log.d("OBSERVE", "init: Now Observe! $it")
            cartAdapter.submitList(it)
            cartAdapter.notifyDataSetChanged()
        }
    }

}