package com.whitfield.nathan.recipehelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 11/28/2016.
 */

public class CreateRecipe extends AppCompatActivity {

    RelativeLayout createRecipeLayout;
    private List<EditText> ingredientsEditText = new ArrayList<EditText>();
    private List<EditText> notesEditText = new ArrayList<EditText>();
    private List<EditText> quantitiesEditText = new ArrayList<EditText>();
    private List<Spinner> ingredientTypesSpinner = new ArrayList<Spinner>();
    EditText recipeName;
    EditText recipeTime;
    Spinner recipeTimeType;
    Button newIngredientButton;

    int ingredientCount = 1;

    private SQLiteDatabase db;
    private Cursor recipeCursor;
    private RecipeDatabaseHelper recipeDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        //assign the newRecipeParts layout to a variable
        createRecipeLayout = (RelativeLayout)findViewById(R.id.newRecipeParts);

        //Create EditText objects
        recipeName = (EditText)findViewById(R.id.nameEditText);
        recipeTime = (EditText)findViewById(R.id.timeEditText);

        //Add first ingredient options to corresponding ArrayLists
        ingredientsEditText.add((EditText)findViewById(R.id.ingredientEditText));
        notesEditText.add((EditText)findViewById(R.id.noteEditText));
        quantitiesEditText.add((EditText)findViewById(R.id.quantityEditText));
        ingredientTypesSpinner.add((Spinner)findViewById(R.id.ingredientTypeSpinner));

        recipeTimeType = (Spinner)findViewById(R.id.timeSpinner);
        newIngredientButton = (Button)findViewById(R.id.newIngredientButton);

        //Setup database helper to open writable database
        try {
            recipeDatabaseHelper = new RecipeDatabaseHelper(this);
            db = recipeDatabaseHelper.getWritableDatabase();
        }
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //add ingredient specs on addIngredientClick
    public void onClickAddIngredient(View view) {
        ingredientCount++;

        //Add new views to appropriate array lists
        ingredientsEditText.add(new EditText(this));
        notesEditText.add(new EditText(this));
        quantitiesEditText.add(new EditText(this));
        ingredientTypesSpinner.add(new Spinner(this));

        //generate IDs for new views
        ingredientsEditText.get(ingredientsEditText.size() - 1).setId(View.generateViewId());
        notesEditText.get(notesEditText.size() - 1).setId(View.generateViewId());
        quantitiesEditText.get(quantitiesEditText.size() - 1).setId(View.generateViewId());
        ingredientTypesSpinner.get(ingredientTypesSpinner.size() - 1).setId(View.generateViewId());

        //set parameters for newIngredient
        LayoutParams ingredientParams = new LayoutParams(120, LayoutParams.WRAP_CONTENT);
        ingredientsEditText.get(ingredientsEditText.size() - 1).setInputType(InputType.TYPE_CLASS_TEXT);
        ingredientsEditText.get(ingredientsEditText.size() - 1).setEms(10);

        ingredientParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ingredientParams.addRule(RelativeLayout.BELOW, notesEditText.get(notesEditText.size() - 2).getId());

        //assign parameters to the new ingredients EditText
        ingredientsEditText.get(ingredientsEditText.size() - 1).setLayoutParams(ingredientParams);

        //Set parameters for new note EditText
        LayoutParams noteParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        notesEditText.get(notesEditText.size() - 1).setInputType(InputType.TYPE_CLASS_TEXT);
        notesEditText.get(notesEditText.size() - 1).setEms(10);

        noteParams.addRule(RelativeLayout.BELOW, ingredientsEditText.get(ingredientsEditText.size() - 1).getId());

        //assign parameters to the new note EditText
        notesEditText.get(notesEditText.size() - 1).setLayoutParams(noteParams);

        //Set parameters for new quantities EditText
        LayoutParams quantityParams = new LayoutParams(120, LayoutParams.WRAP_CONTENT);
        quantitiesEditText.get(quantitiesEditText.size() - 1).setInputType(InputType.TYPE_CLASS_TEXT);
        quantitiesEditText.get(quantitiesEditText.size() - 1).setEms(10);

        quantityParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        quantityParams.addRule(RelativeLayout.BELOW, notesEditText.get(notesEditText.size() - 2).getId());

        //assign parameters to the new quantity EditText
        quantitiesEditText.get(quantitiesEditText.size() - 1).setLayoutParams(quantityParams);

        //Set parameters for new ingredientType Spinner
        LayoutParams typeParams = new LayoutParams(120, LayoutParams.WRAP_CONTENT);

        typeParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        typeParams.addRule(RelativeLayout.ALIGN_BOTTOM, ingredientsEditText.get(ingredientsEditText.size() - 1).getId());
        typeParams.addRule(RelativeLayout.ALIGN_TOP, ingredientsEditText.get(ingredientsEditText.size() - 1).getId());

        //assign parameters to the new type Spinner
        ingredientTypesSpinner.get(ingredientTypesSpinner.size() - 1).setLayoutParams(typeParams);

        //Change add ingredient button layout
        LayoutParams addButtonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addButtonParams.addRule(RelativeLayout.BELOW, notesEditText.get(notesEditText.size() - 1).getId());

        //assign new parameters to the addIngredient button
        newIngredientButton.setLayoutParams(addButtonParams);

        //add new views to the createRecipe layout
        createRecipeLayout.addView(ingredientsEditText.get(ingredientsEditText.size() - 1));
        createRecipeLayout.addView(quantitiesEditText.get(quantitiesEditText.size() - 1));
        createRecipeLayout.addView(ingredientTypesSpinner.get(ingredientTypesSpinner.size() - 1));
        createRecipeLayout.addView(notesEditText.get(notesEditText.size() - 1));
    }

    //When the save button is clicked, store information in the database
    public void onClickSave(View v) {
        int lastRecipeId;
        int lastIngredientId;

        //insert Recipe into recipe table
        recipeDatabaseHelper.insertRecipe(db, recipeName.getText().toString(), Integer.parseInt(recipeTime.getText().toString()),
                                          recipeTimeType.getSelectedItem().toString(), 0);

        //get last recipe id
        recipeCursor = db.rawQuery("SELECT _recipeId FROM RECIPE ORDER BY _recipeId DESC LIMIT 1", null);
        recipeCursor.moveToLast();
        lastRecipeId = recipeCursor.getInt(0);
        recipeCursor.close();

        //for each ingredient added to the recipe, add the ingredient to the ingredient table
        for(int x = ingredientCount; x >= 1; x--) {
            recipeDatabaseHelper.insertIngredient(db, ingredientsEditText.get(ingredientsEditText.size() - x).getText().toString(),
                    notesEditText.get(notesEditText.size() - x).getText().toString(),
                    ingredientTypesSpinner.get(ingredientTypesSpinner.size() - x).getSelectedItem().toString());

            //get id of last ingredient entered.
            recipeCursor = db.rawQuery("SELECT _ingredientId FROM INGREDIENT ORDER BY _ingredientId DESC LIMIT 1", null);
            recipeCursor.moveToLast();
            lastIngredientId = recipeCursor.getInt(0);

            //add values to recipeIngredient juction table
            recipeDatabaseHelper.insertRecipeIngredient(db, quantitiesEditText.get(quantitiesEditText.size() - x).getText().toString(),
                    lastRecipeId, lastIngredientId);
        }

        recipeCursor.close();
        db.close();

        Intent intent = new Intent(CreateRecipe.this,
                TopMenuActivity.class);
        startActivity(intent);
    }

    //when the cancel button is clicked
    public void onClickCancel(View view) {
        Intent intent = new Intent(CreateRecipe.this,
                TopMenuActivity.class);
        startActivity(intent);
    }
}
