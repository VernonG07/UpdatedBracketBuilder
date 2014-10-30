package com.example.mgriffin.listviewex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class StartingBracketActivity extends Activity implements AddBracketDialogFragment.Listener {

    ListView bracketView;
    ArrayAdapter<String> bracketAdapter;
    List<String> bracketList;
    Set<String> bracketSet = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_bracket);
        bracketView = (ListView) findViewById(R.id.lv_items);
        bracketList = new ArrayList<String>();

        SharedPreferences sp = getSharedPreferences(PublicVars.SP_MAIN_KEY, Context.MODE_PRIVATE);
        Set<String> s = sp.getStringSet("BRACKET_SET", null);

        if (s != null) {
            for (String a : s) {
                bracketList.add(a);
            }
        }

        bracketAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bracketList);
        bracketView.setAdapter(bracketAdapter);
        bracketView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: remove item;

                bracketList.remove((int)l);
                bracketAdapter.notifyDataSetChanged();

                return true;
            }
        });
        bracketView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), OpponentsMainActivity.class);
                String selectedBracketName = bracketList.get((int)l);
                intent.putExtra("BRACKET_NAME", selectedBracketName);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_add:
                AddBracketDialogFragment df = new AddBracketDialogFragment();
                df.show(getFragmentManager(), null);
                df.setListener(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void returnData(String name) {
        bracketList.add(name);

        for (String t : bracketList) {
            bracketSet.add(t);
        }

        SharedPreferences sp = getSharedPreferences(PublicVars.SP_MAIN_KEY, Context.MODE_PRIVATE);
        sp.edit().putStringSet("BRACKET_SET", bracketSet).commit();

        bracketAdapter.notifyDataSetChanged();
    }
}


