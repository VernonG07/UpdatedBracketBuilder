package com.example.mgriffin.listviewex;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MenuItem;

import com.example.mgriffin.public_references.PublicVars;


public class OpponentsMainActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponents_main);

        initializeToolbar();
        api21Level();
        startNextFragment(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void initializeToolbar () {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    private void api21Level() {
        if (Build.VERSION.SDK_INT >= 21) {
            toolbar.setTransitionName(PublicVars.TRANSITION_TOOLBAR);
            Transition fade = new Explode();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }
    }

    private void startNextFragment(Bundle savedInstanceState) {
        Fragment newFragment;

        if (savedInstanceState != null) {
            newFragment = getFragmentManager().findFragmentByTag(PublicVars.FRAG_TAG_ROUND);
        } else {
            newFragment = new OpponentsFragment();
            Bundle b = new Bundle();
            b.putInt(PublicVars.FRAGMENT_EXTRA_ROUND_NUMBER, 1);
            newFragment.setArguments(b);
            getFragmentManager().beginTransaction().add(android.R.id.content, newFragment, PublicVars.FRAG_TAG_ROUND).commit();
        }
    }
}
