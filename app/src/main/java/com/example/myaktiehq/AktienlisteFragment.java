package com.example.myaktiehq;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
        List<String> aktienListe = null;

        /**
         * Für den Start wird für die anzuzeigene Liste ein Array definiert
         */
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

        aktienListe = new ArrayList<>(Arrays.asList(aktienlisteArray));

        /**
         * getActivity() -> Die aktuelle Umgebung (diese Activity)
         * R.layout.list_item_aktienliste -> ID der XML-Layout Datei
         * R.id.list_item_aktienliste_textview ->ID des TextViews
         * aktienListe -> Beispieldaten in einer ArrayList
         */
        mAktienlisteAdapter = new ArrayAdapter<>(
                getActivity(), // Die aktuelle Umgebung (diese Activity)
                R.layout.list_item_aktienliste, // ID der XML-Layout Datei
                R.id.list_item_aktienliste_textview, // ID des TextViews
                aktienListe); // Beispieldaten in einer ArrayList


        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);

        aktienlisteListView = (ListView) rootView.findViewById(R.id.listview_aktienliste);
        aktienlisteListView.setAdapter(mAktienlisteAdapter);

        aktienlisteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String aktienInfo = (String) adapterView.getItemAtPosition(position);
                Toast.makeText(getActivity(), aktienInfo, Toast.LENGTH_SHORT).show();
                Intent aktiendetailIntent = new Intent(getActivity(), AktiendetailActivity.class);
                aktiendetailIntent.putExtra(Intent.EXTRA_TEXT, aktienInfo);
                startActivity(aktiendetailIntent);
            }
        });

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
        HoleDatenTask holeDatenTask = new HoleDatenTask();

        switch (item.getItemId()) {
            case R.id.action_daten_aktualisieren:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sharedPreferencesKey = getString(R.string.preference_aktienliste_key);
                String sharedPreferencesDefault = getString(R.string.preference_aktienliste_default);
                String aktenListe = sharedPreferences.getString(sharedPreferencesKey, sharedPreferencesDefault);

                String prefIndizemodusKey = getString(R.string.preference_indizemodus_key);
                Boolean indizemodus = sharedPreferences.getBoolean(prefIndizemodusKey, false);

                /**
                 * Liste der Indizes, die per Request-ULR-Parameter geladen werden sollen
                 */
                if (indizemodus) {
                    String indizeliste = "^GDAXI,^TECDAX,^MDAXI,^SDAXI,^GSPC,^N225,^HSI,XAGUSD=X,XAUUSD=X";
                    holeDatenTask.execute(indizeliste);
                } else {
                    holeDatenTask.execute(aktenListe);
                }

                Toast.makeText(getActivity(), R.string.toast_aktie_reqest, Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class HoleDatenTask extends AsyncTask<String, Integer, String[]> {
        private final String TAG = AsyncTask.class.getName();

        @Override
        protected String[] doInBackground(String... strings) {
            if (strings == null && strings.length == 0) {
                return null;
            }
            final String URL_PARAMETER = "http://www.programmierenlernenhq.de/tools/query.php";
            String symbols = strings[0];
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
                Log.v(TAG, "doInBackground: " + aktiendatenXmlString);
                publishProgress(1, 1);
            } catch (IOException e) { // Beim Holen der Daten trat ein Fehler auf, daher Abbruch
                Log.e(TAG, "doInBackground: Error ", e);
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "doInBackground: Error", e);
                    }
                }
            }
            return leseXmlAktiendatenAus(aktiendatenXmlString);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings != null) {
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

        @org.jetbrains.annotations.Nullable
        private String[] leseXmlAktiendatenAus(String xmlString) {
            Document doc;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));
                doc = db.parse(is);

            } catch (ParserConfigurationException e) {
                Log.e(TAG, "leseXmlAktiendatenAus: Error " + e);
                return null;
            } catch (SAXException e) {
                Log.e(TAG, "leseXmlAktiendatenAus: Error " + e);
                return null;
            } catch (IOException e) {
                Log.e(TAG, "leseXmlAktiendatenAus: Error " + e);
                return null;
            }

            Element xmlAktiendaten = doc.getDocumentElement();
            NodeList aktienListe = xmlAktiendaten.getElementsByTagName("row");

            int anzahlAktien = aktienListe.getLength();
            int anzahlAktienParameter = aktienListe.item(0).getChildNodes().getLength();

            String[] ausgabeArray = new String[anzahlAktien];
            String[][] alleAktienDatenArray = new String[anzahlAktien][anzahlAktienParameter];

            Node aktienParameter;
            String aktienParameterWert;
            for (int i = 0; i < anzahlAktien; i++) {
                NodeList aktienParameterListe = aktienListe.item(i).getChildNodes();

                for (int j = 0; j < anzahlAktienParameter; j++) {
                    aktienParameter = aktienParameterListe.item(j);
                    aktienParameterWert = aktienParameter.getFirstChild().getNodeValue();
                    alleAktienDatenArray[i][j] = aktienParameterWert;
                }

                ausgabeArray[i] = alleAktienDatenArray[i][0];                // symbol
                ausgabeArray[i] += ": " + alleAktienDatenArray[i][4];         // price
                ausgabeArray[i] += " " + alleAktienDatenArray[i][2];          // currency
                ausgabeArray[i] += " (" + alleAktienDatenArray[i][8] + ")";   // percent
                ausgabeArray[i] += " - [" + alleAktienDatenArray[i][1] + "]"; // name

                Log.v(TAG, "XML Output:" + ausgabeArray[i]);
            }

            return ausgabeArray;
        }
    }
}