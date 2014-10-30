package com.example.mgriffin.listviewex;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;


public class OpponentsMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().add(android.R.id.content, new OpponentsFragment()).commit();
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
            SharedPreferences sp = getSharedPreferences(PublicVars.SP_MAIN_KEY, Context.MODE_PRIVATE);
            Set<String> winnerSet = sp.getStringSet(PublicVars.SP_WINNER_LIST, null);

            getFragmentManager().beginTransaction().replace(android.R.id.content, new OpponentsFragment()).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
