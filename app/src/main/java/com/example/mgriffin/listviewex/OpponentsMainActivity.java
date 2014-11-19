package com.example.mgriffin.listviewex;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class OpponentsMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
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
               super.onBackPressed();
                break;
        }
        return false;
    }
}
