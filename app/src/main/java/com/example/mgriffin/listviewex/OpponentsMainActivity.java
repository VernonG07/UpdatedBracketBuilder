package com.example.mgriffin.listviewex;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class OpponentsMainActivity extends ActionBarActivity {

    private Toolbar tbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponents_main);

//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

        tbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (tbar != null) {
            setSupportActionBar(tbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        if (Build.VERSION.SDK_INT >= 21) {
            tbar.setTransitionName("toolbar");
            Transition fade = new Explode();
//            fade.excludeTarget(R.id.btn_add_opponents, true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }

        Fragment newFragment;

        if (savedInstanceState != null) {
            newFragment = getFragmentManager().findFragmentByTag("round_frag");
        } else {
            newFragment = new OpponentsFragment();
            Bundle b = new Bundle();
            b.putInt("ROUND_NUMBER", 1);
            newFragment.setArguments(b);
            getFragmentManager().beginTransaction().add(android.R.id.content, newFragment, "round_frag").commit();
        }
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
}
