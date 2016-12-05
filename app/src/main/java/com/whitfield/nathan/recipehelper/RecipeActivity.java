package com.whitfield.nathan.recipehelper;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Nathan on 11/30/2016.
 */

public class RecipeActivity extends Activity implements RecipeList.RecipeListListener{

    //Set database and cursor objets
    private SQLiteDatabase db;
    private Cursor recipeCursor;
    private ListView recipesList;

    //fill list view with results in the cursor
    CursorAdapter recipeAdapter =
            new SimpleCursorAdapter(RecipeActivity.this,
                    android.R.layout.simple_expandable_list_item_1,
                    recipeCursor,
                    new String[] {"NAME"},
                    new int[]{android.R.id.text1}, 0);

    public static final String EXTRA_RECIPENUM = "recipeNum";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);

        //Setup the RecipeList fragment in the frameLayout container
        RecipeList listOfRecipes = new RecipeList();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.recipe_container, listOfRecipes);
        ft.addToBackStack(null);
        ft.commit();

        recipesList = (ListView)findViewById(R.id.list_recipes);

        //call method to handle new intents from searching
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //when a new intent comes in, call handle intent (comes from searching)
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        //if search is run, create the query
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //Handle query
            String query = intent.getStringExtra(SearchManager.QUERY);
            //show results from the query
            showResults(query);
        }
    }

    private void showResults(String query) {

        try {
            //Get readable database
            SQLiteOpenHelper recipeDatabaseHelper = new RecipeDatabaseHelper(this);
            db = recipeDatabaseHelper.getReadableDatabase();

            //Fill cursor with results from the search query
            recipeCursor = db.query("RECIPE",
                    new String[] {"_recipeId", "NAME"}, "*",
                    new String[] {"LIKE", query, "%"}, null, null, null);



            recipesList.setAdapter(recipeAdapter);
        }
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Unavailable" , Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void itemClicked(long id) {

    }
}
