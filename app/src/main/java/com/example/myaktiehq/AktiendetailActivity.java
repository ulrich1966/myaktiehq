package com.example.myaktiehq;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class AktiendetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktiendetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aktiendetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(this, "Settings wurde gedrückt", Toast.LENGTH_SHORT);
                break;
            case R.id.action_starte_browser:
                Toast.makeText(this, "Starte Browser wurde gedrückt", Toast.LENGTH_SHORT);
                zeigeWebSite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void zeigeWebSite() {
        Intent empfangenerIntent = this.getIntent();
        String webseitenUrl = "";
        if(null != empfangenerIntent && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)){
            String aktienInfo  = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
            int pos = aktienInfo.indexOf(":");
            String symbol = aktienInfo.substring(0, pos);
            webseitenUrl = "http://finance.yahoo.com/q?s=" + symbol;
        }
        Uri webseitenUri = Uri.parse(webseitenUrl);
        Intent intenet = new Intent(Intent.ACTION_VIEW, webseitenUri);
    }
}