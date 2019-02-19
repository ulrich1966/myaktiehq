package com.example.myaktiehq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    boolean exit = false;
    private static int calls = 0;
    private static final String CALLS_KEY = "calls";
    public MainActivity(){
        super();
    }

    /*
    onStart() – Die Activity ist kurz davor sichtbar zu werden.
    onResume() – Die Activity ist sichtbar geworden und hat den Benutzer-Fokus.
    onPause() – Eine andere Activity erhält den Fokus. Diese Activity wird pausiert.
    onStop() – Die Activity ist nicht mehr sichtbar und wird nun gestoppt.
    onRestart() – Die Activity wird neu gestartet. Als Nächstes wird onStart() aufgerufen.
    onDestroy() – Die Activity wird zerstört werden. Hier endet der Lebenszyklus.
    onSaveInstanceState(Bundle outState) – Die Activity wird in den nächsten Momenten gestoppt. Jetzt kann der aktuelle Zustand als Sammlung von Key-Value Paaren gespeichert werden.
    onRestoreInstanceState(Bundle savedInstanceState) – Die Activity wird wieder hergestellt, nachdem sie aufgrund von Speichermangel zerstört wurde. Dieser Callback wird nur aufgerufen, wenn das Bundle nicht null ist. Der Aufruf erfolgt unmittelbar nach onStart().
     */

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Die Activity wird erstellt. Hier beginnt der Lebenszyklus.");
        if(null != bundle && 0 != bundle.getInt(CALLS_KEY)){
            Log.d(TAG, String.format("gespeicherte Aufrufe: %d", bundle.getInt(CALLS_KEY)));
        } else {
            Log.d(TAG, "No Entry for Calls in Bundle");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Die Activity ist kurz davor sichtbar zu werden.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Die Activity ist sichtbar geworden und hat den Benutzer-Fokus.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Eine andere Activity erhält den Fokus. Diese Activity wird pausiert.");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Die Activity ist nicht mehr sichtbar und wird nun gestoppt.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Die Activity wird neu gestartet. Als Nächstes wird onStart() aufgerufen.");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: Die Activity wird zerstört werden. Hier endet der Lebenszyklus.");
        super.onDestroy();
        if (exit){
            System.exit(0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.d(TAG, "onSaveInstanceState: Die Activity wird in den nächsten Momenten gestoppt. " +
                "Jetzt kann der aktuelle Zustand als Sammlung von Key-Value Paaren gespeichert werden.");
        calls++;
        Log.d(TAG, String.format("Aufrufe: %d", calls));
        bundle.putInt(CALLS_KEY, calls);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.d(TAG, "onRestoreInstanceState: Die Activity wird wieder hergestellt, nachdem sie " +
                "aufgrund von Speichermangel zerstört wurde. Dieser Callback wird nur aufgerufen, " +
                "wenn das Bundle nicht null ist. Der Aufruf erfolgt unmittelbar nach onStart().");
        int calls = bundle.getInt(CALLS_KEY);
        Log.d(TAG, String.format("gespeicherte Aufrufe: %s", calls));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu: Das Menue wird erstellt");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean success = false;
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, R.string.toast_setting_msg , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,EinstellungenActivity.class));
                success = true;
                break;
            case R.id.action_kill:
                Toast.makeText(this, R.string.toast_setting_kill , Toast.LENGTH_SHORT).show();
                this.finish();
                success = true;
                this.exit = true;
                break;
            default:
                success = super.onOptionsItemSelected(item);
        }
        Log.d(TAG, String.format("onOptionsItemSelected: Das Item [%s] aus dem Menue wurde gewaehlt.", item.getTitle()));
        return success;
    }


}
