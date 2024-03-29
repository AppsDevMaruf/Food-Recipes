package com.maruf.foody.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maruf.foody.model.FoodRecipe
import com.maruf.foody.utils.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}