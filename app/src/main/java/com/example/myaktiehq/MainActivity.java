package com.example.myaktiehq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean success;
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Setting wurde gedrückt", Toast.LENGTH_SHORT).show();
                success = true;
                break;
            default:
                success = super.onOptionsItemSelected(item);

        }
        return success;
    }
}
