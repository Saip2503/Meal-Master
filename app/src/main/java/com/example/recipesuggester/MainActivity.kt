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

class MainActivity : AppCompatActivity() {

    private lateinit var ingredientsInput: EditText
    private lateinit var suggestButton: Button
    private lateinit var recipeList: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter

    // A sample list of recipes for local testing.
    private val recipes = listOf(
        Recipe("Pasta", listOf("pasta", "tomato", "cheese", "olive oil")),
        Recipe("Salad", listOf("lettuce", "tomato", "cucumber", "olive oil", "lemon")),
        Recipe("Omelette", listOf("egg", "cheese", "onion", "butter")),
        Recipe("Grilled Cheese Sandwich", listOf("bread", "cheese", "butter")),
        Recipe("Tacos", listOf("tortilla", "tomato", "lettuce", "cheese", "beef")),
        Recipe("Pizza", listOf("dough", "tomato", "cheese", "olive oil", "basil")),
        Recipe("Fried Rice", listOf("rice", "egg", "carrot", "soy sauce", "onion")),
        Recipe("Chicken Stir-fry", listOf("chicken", "soy sauce", "garlic", "bell pepper", "onion")),
        Recipe("Pancakes", listOf("flour", "egg", "milk", "butter", "sugar")),
        Recipe("Guacamole", listOf("avocado", "tomato", "onion", "lime", "cilantro")),
        Recipe("Spaghetti Bolognese", listOf("spaghetti", "ground beef", "tomato", "onion", "garlic")),
        Recipe("Caesar Salad", listOf("lettuce", "croutons", "parmesan", "caesar dressing", "chicken")),
        Recipe("Mac and Cheese", listOf("macaroni", "cheese", "milk", "butter")),
        Recipe("Vegetable Soup", listOf("carrot", "celery", "onion", "potato", "tomato")),
        Recipe("Chili", listOf("ground beef", "tomato", "beans", "onion", "garlic", "chili powder")),
        Recipe("Beef Stew", listOf("beef", "carrot", "potato", "onion", "beef broth")),
        Recipe("Scrambled Eggs", listOf("egg", "butter", "salt", "pepper")),
        Recipe("French Toast", listOf("bread", "egg", "milk", "sugar", "butter")),
        Recipe("Chocolate Cake", listOf("flour", "cocoa powder", "sugar", "egg", "butter")),
        Recipe("Hamburger", listOf("ground beef", "bun", "lettuce", "tomato", "cheese")),
        Recipe("Quiche", listOf("egg", "cheese", "onion", "milk", "pastry")),
        Recipe("Sushi", listOf("rice", "seaweed", "fish", "soy sauce", "avocado")),
        Recipe("Chicken Curry", listOf("chicken", "onion", "garlic", "curry powder", "coconut milk")),
        Recipe("Lasagna", listOf("lasagna noodles", "tomato", "cheese", "ground beef", "onion")),
        Recipe("Garlic Bread", listOf("bread", "garlic", "butter", "parsley")),
        Recipe("Falafel", listOf("chickpeas", "onion", "garlic", "parsley", "cumin")),
        Recipe("Hummus", listOf("chickpeas", "tahini", "garlic", "lemon", "olive oil")),
        Recipe("Caprese Salad", listOf("tomato", "mozzarella", "basil", "olive oil", "balsamic vinegar")),
        Recipe("Meatballs", listOf("ground beef", "breadcrumbs", "egg", "onion", "garlic")),
        Recipe("Clam Chowder", listOf("clams", "potato", "celery", "onion", "cream"))
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        ingredientsInput = findViewById(R.id.ingredientsInput)
        suggestButton = findViewById(R.id.suggestButton)
        recipeList = findViewById(R.id.recipeList)

        recipeList.layoutManager = LinearLayoutManager(this)
        recipeAdapter = RecipeAdapter(listOf())  // Initially, no recipes are suggested
        recipeList.adapter = recipeAdapter

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
}
