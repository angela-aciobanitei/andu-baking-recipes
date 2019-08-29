package com.ang.acb.bakeit.util;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ang.acb.bakeit.R;

/**
 * Used for testing fragments inside a fake activity.
 */
public class FragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content = new FrameLayout(this);
        content.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        content.setId(R.id.container);
        setContentView(content);
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment, "TEST")
                .commit();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment).commit();
    }
}
