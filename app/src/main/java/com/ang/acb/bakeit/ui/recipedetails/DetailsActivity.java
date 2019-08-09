package com.ang.acb.bakeit.ui.recipedetails;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ang.acb.bakeit.R;

import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;
import static com.ang.acb.bakeit.ui.recipelist.MainActivity.INVALID_RECIPE_ID;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID);
        if (recipeId.equals(INVALID_RECIPE_ID)) {
            Timber.d("Wrong recipe id.");
            return;
        }
    }

}
