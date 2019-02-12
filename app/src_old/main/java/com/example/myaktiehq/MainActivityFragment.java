package com.example.myaktiehq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Bla Bla
 */
public class MainActivityFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_aktienliste);
        System.out.println("done");
    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean success;
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Setting wurde gedrückt", Toast.LENGTH_SHORT).show();
                ListView lv = findViewById(R.id.listview_aktienlist);
                success = true;
                //bla
                break;
            default:
                success = super.onOptionsItemSelected(item);

        }
        return success;
    }
     */
}
