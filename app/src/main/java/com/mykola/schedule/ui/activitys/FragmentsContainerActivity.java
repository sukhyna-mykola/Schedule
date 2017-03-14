package com.mykola.schedule.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mykola.schedule.R;

/**
 * Created by mykola on 04.03.17.
 */

public abstract class FragmentsContainerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_fragments);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragments_container);
        if (fragment == null) {
            fragment = getFragment();
            fm.beginTransaction().add(R.id.fragments_container, fragment)
                    .commit();
        }
    }

    public  abstract Fragment getFragment();
}
