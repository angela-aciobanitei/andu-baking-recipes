package com.ang.acb.bakeit.ui.recipelist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.ang.acb.bakeit.R;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(
                R.id.main_fragment_container,
                RecipeListFragment.newInstance());
        transaction.commit();
        Timber.d("FragmentRecipeList added");
    }
}
