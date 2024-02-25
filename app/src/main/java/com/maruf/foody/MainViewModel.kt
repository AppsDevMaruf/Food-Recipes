package com.maruf.foody


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruf.foody.data.Repository
import com.maruf.foody.model.FoodRecipe
import com.maruf.foody.utils.NetworkResult
import com.maruf.foody.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext private val appContext: Context, private val repository: Repository) : ViewModel() {

    private val recipeResponse: MutableLiveData<NetworkResult<FoodRecipe>?> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()
        if (NetworkUtils.hasInternetConnection(appContext)) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipeResponse.value = handleFoodRecipesResponse(response)
            } catch (_: Exception) {
                recipeResponse.value = NetworkResult.Error("Recipes not found.")
            }

        } else {
            recipeResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }

            response.body()?.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }

            response.isSuccessful -> {
                return NetworkResult.Success(response.body()!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }


}