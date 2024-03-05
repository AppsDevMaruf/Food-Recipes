package com.maruf.foody.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.maruf.foody.MainViewModel
import com.maruf.foody.adapters.RecipesAdapter
import com.maruf.foody.databinding.FragmentRecipesBinding
import com.maruf.foody.utils.Constants
import com.maruf.foody.utils.NetworkResult
import com.maruf.foody.utils.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private  var binding: FragmentRecipesBinding? = null
    private val mainViewModel by viewModels<MainViewModel>()
    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        readDatabase()
        return binding!!.root
    }

    private fun readDatabase() {
        mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
            if (database.isNotEmpty()) {
                Log.d("Offline","readDatabase: ")
                loadDataFromCache()
            } else {
                requestApiData()
            }
        }

    }

    private fun requestApiData() {
        Log.d("Online Api call","readDatabase: ")
        mainViewModel.getRecipes(mainViewModel.applyQueries())
        mainViewModel.recipeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        Log.d("TAG", "requestApiData: ${it.results.size}")
                        mAdapter.updateList(it.results)
                    }

                    Toast.makeText(requireContext(), "response.data  ${response.data?.results?.size}", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Error -> {
                    if (response.message==Constants.NO_INTERNET_CONNECTION_MGS){
                        showErrorMgs()

                    }else{
                        hideErrorMgs()
                        loadDataFromCache()
                    }
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }
        }
    }

    private fun showErrorMgs() { binding?.errorImageView?.visibility = View.VISIBLE
        binding?.errorTextView?.visibility = View.VISIBLE
    }

    private fun hideErrorMgs() {
        binding?.errorImageView?.visibility = View.GONE
        binding?.errorTextView?.visibility = View.GONE
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.updateList(database[0].foodRecipe.results)
                    hideShimmerEffect()
                }
            }
        }

    }

    private fun setupRecyclerView() {
        binding?.recyclerview?.adapter = mAdapter

    }

    private fun showShimmerEffect() {
        binding?.recyclerview?.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding?.recyclerview?.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
