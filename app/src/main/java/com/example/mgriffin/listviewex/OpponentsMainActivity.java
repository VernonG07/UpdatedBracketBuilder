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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bracket_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Fragment nextRoundFragment = new OpponentsFragment();
            Bundle b = new Bundle();
            b.putInt("ROUND_NUMBER", 2);
            nextRoundFragment.setArguments(b);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_in);

            ft.addToBackStack(null).replace(android.R.id.content, nextRoundFragment, "round_frag").commit();

            return true;
        } else if (id == android.R.id.home) {
            //TODO: if round 1, go to main activity
            //TODO: if round > 1, go to previous fragment

            Intent startingActivity = new Intent(getApplicationContext(), StartingBracketActivity.class);
            startActivity(startingActivity);

            //getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }
}
