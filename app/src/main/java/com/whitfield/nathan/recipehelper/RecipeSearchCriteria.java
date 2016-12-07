package com.whitfield.nathan.recipehelper;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 12/2/2016.
 */

public class RecipeSearchCriteria extends Fragment {

    View rootView;
    LinearLayout rootLayout;
    Button newIngredient;
    private List<Spinner> ingredientSpinners = new ArrayList<Spinner>();

    int ingredientCount = 1;

    public interface onSearchClickListener {
        public void searchClick(String ingredientType, String[] ingredients);
    }

    onSearchClickListener searchClickListener;

    public RecipeSearchCriteria() {
        //default constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Set the view for the fragment and inflate the layout
        rootView = inflater.inflate(R.layout.search_criteria_fragment, container, false);

        //Set the layout of the fragment to linearLayour holding the recipe search criteria
        rootLayout = (LinearLayout)rootView.findViewById(R.id.criteriaLayout);

        Spinner timeSpinner = (Spinner)rootView.findViewById(R.id.timeSpinner);
        final Spinner typeSpinner = (Spinner)rootView.findViewById(R.id.typeSpinner);
        ingredientSpinners.add((Spinner)rootView.findViewById(R.id.ingredientSpinner));
        newIngredient = (Button)rootView.findViewById(R.id.newIngredientButton);
        Button searchButton = (Button)rootView.findViewById(R.id.apply_search_button);

        //set onclick listener to send ingredient types and ingredient names to RecipeActivity
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeSpinner.getSelectedItem().toString();
                String[] ingredientNames = new String[ingredientCount];

                for(int x = ingredientCount; x >= 1; x--) {
                    ingredientNames[ingredientCount - x] = ingredientSpinners.get(ingredientSpinners.size() - x).toString();
                }
                searchClickListener.searchClick(type, ingredientNames);
            }
        });
        return rootView;
    }

    //Click button to add ingredient spinner
    public void onClickNewSearchIngredient(View view) {
        ingredientCount++;

        //create new spinner object
        ingredientSpinners.add(new Spinner(getContext()));

        //set layout parameters for new spinner
        LayoutParams ingredientSpinnerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        ingredientSpinners.get(ingredientSpinners.size() - 1).setLayoutParams(ingredientSpinnerParams);

        //add spinner to the layout
        rootLayout.addView(ingredientSpinners.get(ingredientSpinners.size() - 1));
    }

}
