package com.maruf.foody.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maruf.foody.MainViewModel
import com.maruf.foody.adapters.RecipesAdapter
import com.maruf.foody.databinding.FragmentRecipesBinding
import com.maruf.foody.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private lateinit var binding: FragmentRecipesBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        requestApiData()
        return binding.root
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(mainViewModel.applyQueries())
        mainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        Log.d("TAG", "requestApiData: ${it.results.size}")
                        mAdapter.updateList(it.results) }

                    Toast.makeText(requireContext(), "response.data  ${response.data?.results?.size}", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter

    }

    private fun showShimmerEffect() {
        binding.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.recyclerview.hideShimmer()
    }
}
