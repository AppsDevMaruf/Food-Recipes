package com.maruf.foody


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruf.foody.data.Repository
import com.maruf.foody.model.FoodRecipe
import com.maruf.foody.utils.Constants.Companion.API_KEY
import com.maruf.foody.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.maruf.foody.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.maruf.foody.utils.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.maruf.foody.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.maruf.foody.utils.Constants.Companion.QUERY_API_KEY
import com.maruf.foody.utils.Constants.Companion.QUERY_DIET
import com.maruf.foody.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.maruf.foody.utils.Constants.Companion.QUERY_NUMBER
import com.maruf.foody.utils.Constants.Companion.QUERY_TYPE
import com.maruf.foody.utils.NetworkResult
import com.maruf.foody.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext private val appContext: Context, private val repository: Repository) : ViewModel() {

    val recipeResponse: MutableLiveData<NetworkResult<FoodRecipe>?> = MutableLiveData()
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

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
    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
    /*fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }*/

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