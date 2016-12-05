package com.whitfield.nathan.recipehelper;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by Nathan on 12/2/2016.
 */

public class RecipeSearchCriteria extends Fragment {

    View rootView;
    LinearLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Set the view for the fragment and inflate the layout
        rootView = inflater.inflate(R.layout.search_criteria_fragment, container, false);

        //Set the layout of the fragment to linearLayour holding the recipe search criteria
        rootLayout = (LinearLayout)rootView.findViewById(R.id.criteriaLayout);

        Spinner timeSpinner = (Spinner)rootView.findViewById(R.id.timeSpinner);
        Spinner typeSpinner = (Spinner)rootView.findViewById(R.id.typeSpinner);
        Spinner ingredientSpinner = (Spinner)rootView.findViewById(R.id.ingredientSpinner);
        Button newIngredient = (Button)rootView.findViewById(R.id.newIngredientButton);

        newIngredient.setOnClickListener((View.OnClickListener) this);
        return rootView;
    }

    //Click button to add ingredient spinner
    public void onClick(View view) {
        //create new spinner object
        Spinner newIngredientSpinner = new Spinner(getContext());

        //add spinner to the layout
        rootLayout.addView(newIngredientSpinner);
    }
}
