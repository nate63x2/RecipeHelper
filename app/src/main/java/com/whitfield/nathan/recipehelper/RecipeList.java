package com.whitfield.nathan.recipehelper;

import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Nathan on 10/22/2016.
 */

public class RecipeList extends ListFragment {

    private SQLiteDatabase db;
    private Cursor recipeCursor;

    static interface RecipeListListener {
        void itemClicked(long id);
    };

    private RecipeListListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //set layout
        final View rootView = inflater.inflate(R.layout.fragment_list_recipe, container, false);
        //Create listView object
        ListView recipesList = (ListView)rootView.findViewById(R.id.list_recipes);

        //Create an OnItemClickLiestener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView, View v, int position, long id) {

                    }
                };

        //populate recipes from recipeCursor

        try {
            SQLiteOpenHelper recipeDatabaseHelper = new RecipeDatabaseHelper(getContext());
            db = recipeDatabaseHelper.getReadableDatabase();
            recipeCursor = db.query("RECIPE",
                    new String[] {"_recipeId", "NAME"}, "*", null, null, null, null);

            CursorAdapter recipeAdapter =
                    new SimpleCursorAdapter(getContext(),
                            android.R.layout.simple_expandable_list_item_1,
                            recipeCursor,
                            new String[] {"NAME"},
                            new int[]{android.R.id.text1}, 0);

            recipesList.setAdapter(recipeAdapter);
        }
        catch (SQLiteException e) {
            Toast toast = Toast.makeText(getContext(), "Database Unavailable" , Toast.LENGTH_SHORT);
            toast.show();
        }

        //navigate to RecipeActivity if reipe is clicked
        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                Intent intent = new Intent(getContext(), RecipeActivity.class);
                intent.putExtra(RecipeActivity.EXTRA_RECIPENUM, (int)id);
                startActivity(intent);
            }
        });

        return rootView;
    }


}
