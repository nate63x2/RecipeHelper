package com.whitfield.nathan.recipehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nathan on 10/22/2016.
 * Updated 11/28/16
 */

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "YourRecipes";
    private static final int DB_VERSION = 1;

    RecipeDatabaseHelper (Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    public void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion < 1) {
            db.execSQL("CREATE TABLE RECIPE ("
                    + "_recipeId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "recipeName TEXT, "
                    + "recipeTime INTEGER, "
                    + "recipeTimeType TEXT, "
                    + "favorite INTEGER);");

            db.execSQL("CREATE TABLE INGREDIENT ("
                    + "_ingredientId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "ingredientName TEXT, "
                    + "ingredientNote TEXT, "
                    + "ingredientType TEXT);");

            db.execSQL("CREATE TABLE RECIPEINGREDIENT ("
                    + "quantity TEXT, "
                    + "recipeId INTEGER, "
                    + "ingredientId INTEGER, "
                    + "FOREIGN KEY(recipeId) REFERENCES RECIPE(_recipeId), "
                    + "FOREIGN KEY(ingredientId) REFERENCES INGREDIENT(_ingredientId));");
        }
    }

    public void insertRecipe(SQLiteDatabase db, String name, int time, String timeType, int favorite) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("recipeName", name);
        recipeValues.put("recipeTime", time);
        recipeValues.put("recipeTimeType", timeType);
        recipeValues.put("favorite", favorite);
        db.insert("RECIPE", null, recipeValues);
    }

    public void insertIngredient(SQLiteDatabase db, String name, String note, String type) {
        ContentValues ingredientValues = new ContentValues();
        ingredientValues.put("ingredientName", name);
        ingredientValues.put("ingredientNote", note);
        ingredientValues.put("ingredientType", type);
        db.insert("INGREDIENT", null, ingredientValues);
    }

    public void insertRecipeIngredient(SQLiteDatabase db, String quantity, int recipeID, int ingredientID) {
        ContentValues recipeIngredientValues = new ContentValues();
        recipeIngredientValues.put("quantity", quantity);
        recipeIngredientValues.put("recipeId", recipeID);
        recipeIngredientValues.put("ingredientId", ingredientID);
        db.insert("RECIPEINGREDIENT", null, recipeIngredientValues);
    }
}
