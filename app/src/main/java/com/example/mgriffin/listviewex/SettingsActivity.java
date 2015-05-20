package com.example.mgriffin.listviewex;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mgriffin.public_references.PublicVars;


public class SettingsActivity extends ActionBarActivity {

    int something;
    boolean somethingBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment newFragment;
        if (savedInstanceState != null) {
            newFragment = getFragmentManager().findFragmentByTag(PublicVars.FRAG_TAG_SETTINGS);
        } else {
            newFragment = new SettingsFragment();
            getFragmentManager().beginTransaction().add(android.R.id.content, newFragment, PublicVars.FRAG_TAG_SETTINGS).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            startActivity(new Intent(SettingsActivity.this, StartingBracketActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this, StartingBracketActivity.class));
        finish();
    }
}
