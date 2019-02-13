package com.example.myaktiehq;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AktienlisteFragment extends Fragment {
    private static final String TAG = AktienlisteFragment.class.getName() + "\n\b-->";
    private ArrayAdapter<String> mAktienlisteAdapter = null;
    private ListView aktienlisteListView = null;

    public AktienlisteFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        String[] aktienlisteArray = {
                "Adidas - Kurs: 73,45 €",
                "Allianz - Kurs: 145,12 €",
                "BASF - Kurs: 84,27 €",
                "Bayer - Kurs: 128,60 €",
                "Beiersdorf - Kurs: 80,55 €",
                "BMW St. - Kurs: 104,11 €",
                "Commerzbank - Kurs: 12,47 €",
                "Continental - Kurs: 209,94 €",
                "Daimler - Kurs: 84,33 €"
        };

        List<String> aktienListe = new ArrayList<>(Arrays.asList(aktienlisteArray));

        mAktienlisteAdapter = new ArrayAdapter<>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.list_item_aktienliste, // ID der XML-Layout Datei
                        R.id.list_item_aktienliste_textview, // ID des TextViews
                        aktienListe); // Beispieldaten in einer ArrayList

        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);

        aktienlisteListView = (ListView) rootView.findViewById(R.id.listview_aktienliste);
        aktienlisteListView.setAdapter(mAktienlisteAdapter);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        // Menü bekannt geben, dadurch kann unser Fragment Menü-Events verarbeiten
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_aktienfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        // Wir prüfen, ob Menü-Element mit der ID "action_daten_aktualisieren"
        // ausgewählt wurde und geben eine Meldung aus
        int id = item.getItemId();
        if (id == R.id.action_daten_aktualisieren) {
            HoleDatenTask holeDatenTask = new HoleDatenTask();
            holeDatenTask.execute("Aktie");
            Toast.makeText(getActivity(), "Aktualisieren gedrückt!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class HoleDatenTask extends AsyncTask<String, Integer, String[]> {
        private final String TAG = AsyncTask.class.getName();

        @Override
        protected String[] doInBackground(String... strings) {
            if(strings == null && strings.length == 0){
                return null;
            }
            final String URL_PARAMETER = "http://www.programmierenlernenhq.de/tools/query.php";
            String symbols = "DAI.DE,BMW.DE";

            String anfrageString = String.format("%s?s=%s", URL_PARAMETER, symbols);
            Log.v(TAG, "doInBackground: " + anfrageString);

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String aktiendatenXmlString = "";

            try {
                URL url = new URL(anfrageString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    aktiendatenXmlString += line + "\n";
                }
                if (aktiendatenXmlString.length() == 0) {
                    return null;
                }
                Log.v(TAG, "Aktiendaten XML-String: " + aktiendatenXmlString);
                publishProgress(1,1);

            } catch (IOException e) { // Beim Holen der Daten trat ein Fehler auf, daher Abbruch
                Log.e(TAG, "Error ", e);
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            // Hier parsen wir später die XML Aktiendaten

            return null;
//
//            String[] ergebinsArray = new String[20];
//            for (int i = 0; i < ergebinsArray.length; i++) {
//                ergebinsArray[i] = String.format("%s_%s", strings[0], (i+1));
//                if(i%5 == 4){
//                    publishProgress(i+1, 20);
//                }
//                try {
//                    Thread.sleep(600);
//                } catch (InterruptedException e){
//                    Log.e(TAG, "doInBackground: Error", e);
//                }
//            }
//            return ergebinsArray;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if(strings != null){
                mAktienlisteAdapter.clear();
//                for (String aktienString : strings){
//                    mAktienlisteAdapter.add(aktienString);
//                }
                mAktienlisteAdapter.addAll(strings);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Toast.makeText(getActivity(), String.format("%s von %s geladen", values[0], values[1]), Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }
    }
}