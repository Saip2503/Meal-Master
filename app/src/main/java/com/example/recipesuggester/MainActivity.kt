package com.example.recipesuggester

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesuggester.adapter.RecipeAdapter
import com.example.recipesuggester.model.Recipe
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var ingredientsInput: EditText
    private lateinit var suggestButton: Button
    private lateinit var recipeList: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipes: List<Recipe>  // List to store recipes from the CSV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        ingredientsInput = findViewById(R.id.ingredientsInput)
        suggestButton = findViewById(R.id.suggestButton)
        recipeList = findViewById(R.id.recipeList)

        recipeList.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeAdapter(listOf())  // Initially, no recipes are suggested
        recipeList.adapter = recipeAdapter

        // Load recipes from CSV
        recipes = loadRecipesFromCSV()

        // Method to filter recipes based on user input
        fun getSuggestedRecipes(ingredients: String): List<Recipe> {
            val inputList = ingredients.split(",").map { it.trim().lowercase() }
            return recipes.filter { recipe ->
                inputList.any { it in recipe.ingredients.map { ingredient -> ingredient.lowercase() } }
            }
        }

        suggestButton.setOnClickListener {
            val inputIngredients = ingredientsInput.text.toString().trim()  // Retrieve text from EditText
            if (inputIngredients.isEmpty()) {
                Toast.makeText(this, "Please enter ingredients", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val suggestedRecipes = getSuggestedRecipes(inputIngredients)  // Pass input to the method
            if (suggestedRecipes.isEmpty()) {
                Toast.makeText(this, "No recipes found for the given ingredients", Toast.LENGTH_SHORT).show()
            } else {
                recipeAdapter.updateRecipes(suggestedRecipes)
            }
        }
    }

    // Function to read and parse the CSV file
    private fun loadRecipesFromCSV(): List<Recipe> {
        val recipeList = mutableListOf<Recipe>()
        val inputStream = assets.open("recipes.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.readLine() // Skip the header row

        reader.forEachLine { line ->
            // Using a regex to split by commas but ignore commas inside quotes
            val parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
            if (parts.size == 2) {
                val recipeName = parts[0].trim()
                val ingredients = parts[1].removeSurrounding("\"").split(",").map { it.trim() }  // Removing quotes and splitting ingredients
                recipeList.add(Recipe(recipeName, ingredients))
            }
        }

        reader.close()
        return recipeList
    }

}
