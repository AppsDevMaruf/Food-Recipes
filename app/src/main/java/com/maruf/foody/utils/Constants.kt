package com.maruf.foody.utils

class Constants {
    companion object{
        const val BASE_URL = "https://api.spoonacular.com/"
        const val API_KEY = "ed3978e87f74425d9bff78e54852c416"


        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // Bottom Sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        // ROOM Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
        const val  NO_INTERNET_CONNECTION_CODE = 123
        const val  RECIPES_NOT_FOUND_CODE = 124
        const val  NO_INTERNET_CONNECTION_MGS = "No Internet Connection."
        const val  RECIPES_NOT_FOUND_MGS = "Recipes not found."


    }
}