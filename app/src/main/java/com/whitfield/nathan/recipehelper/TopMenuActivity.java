package com.whitfield.nathan.recipehelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class TopMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_menu);

        //Create an OnItemClickLiestener
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        if (position == 0) {
                            Intent intent = new Intent(TopMenuActivity.this,
                                    CreateRecipe.class);
                            startActivity(intent);
                        }
                        if (position == 1) {
                            Intent intent = new Intent(TopMenuActivity.this,
                                    RecipeActivity.class);
                            startActivity(intent);
                        }
                        if (position == 2) {
                            Intent intent = new Intent(TopMenuActivity.this,
                                    FavoriteRecipes.class);
                            startActivity(intent);
                        }
                    }
                };

        // add listener to list view
        ListView listView = (ListView) findViewById(R.id.List_options);
        listView.setOnItemClickListener(itemClickListener);
    }
}
