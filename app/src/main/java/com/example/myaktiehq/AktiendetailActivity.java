package com.example.myaktiehq;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class AktiendetailActivity extends AppCompatActivity {
    private static final String TAG = AktiendetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktiendetail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aktiendetail, menu);
        Intent shareIntent = makeShareIntent(menu);
        ShareActionProvider sAP = makeSap(menu);
        if (sAP != null ) {
            sAP.setShareIntent(shareIntent);
        } else {
            String LOG_TAG = AktiendetailActivity.class.getSimpleName();
            Log.d(LOG_TAG, "Kein ShareActionProvider vorhanden!");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
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

    /**
     * Besorgt sich einen festgelegten Intent (hier einen Browser)
     */
    private void zeigeWebSite() {
        Intent empfangenerIntent = this.getIntent();
        String webseitenUrl = "";
        if (null != empfangenerIntent && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String aktienInfo = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
            String symbol = aktienInfo.substring(0, aktienInfo.indexOf(":")).trim();
            webseitenUrl = String.format("http://finance.yahoo.com/q?s=%s", symbol);
        }
        Uri webseitenUri = Uri.parse(webseitenUrl);
        Log.d(TAG, String.format("Refenrenzierte Seite: %s", webseitenUri.toString()));
        Intent intent = new Intent(Intent.ACTION_VIEW, webseitenUri);
        if (null != intent.resolveActivity(getPackageManager())) {
            startActivity(intent);
        } else {
            String msg = AktiendetailActivity.class.getSimpleName();
            Log.d(TAG, "Keine Web-App installiert!");
            Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
    }

    /**
     * Erstellt einen Intent fuer das Teilen - Menueeintrag
     * @param menu
     * @return
     */
    private Intent makeShareIntent(Menu menu) {
        String aktienInfo = "";
        Intent empfangenerIntent = this.getIntent();
        if (empfangenerIntent != null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)) {
            aktienInfo = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Daten zu: " + aktienInfo);
        return intent;
    }

    /**
     * Erstellt einen ShareActionProvider fuer den Teilenmenueeintrag
     * @param menu
     * @return
     */
    private ShareActionProvider makeSap(Menu menu) {
        MenuItem shareMenuItem = menu.findItem(R.id.action_teile_aktiendaten);
        ShareActionProvider sAP = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
        return sAP;
    }
}