package com.example.myaktiehq;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class AktienlisteFragment extends Fragment {
    private static final String TAG = AktienlisteFragment.class.getName();
    private static final String STATE_DATA = "Finanzdaten";
    private ArrayAdapter<String> mAktienAdapter = null;
    private List<String> aktienListe = null;
    private ListView aktienlisteListView = null;
    private SwipeRefreshLayout mSwipeRefreshLayout = null;


    // Tag für das Logging des Fragment-Lifecycle definieren
    private final String LOG_TAG = AktienlisteFragment.class.getSimpleName();

    /**
     * Defaultconstructor. Ruft super() auf.
     */
    public AktienlisteFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart:");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume:");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy:");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach: Wird aufgerufen, wenn das Fragment mit der Activity verbunden wurde");
        Log.d(TAG, String.format("... Mit der Activity %s verbunden", activity.getClass().getName()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: Wird aufgerufen, wenn die onCreate() Methode der Activity abgeschlossen ist");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: Wird aufgerufen, um die View-Hierarchie des Fragments zu entfernen");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Wird aufgerufen, wenn die Verbindung zwischen Fragment und Activity gelöst wird");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Wird aufgerufen, um die View-Hierarchie des Fragments zu erstellen");
        boolean refresh = false;

        if (savedInstanceState != null) {
            // Wiederherstellen der Werte des gespeicherten Fragment-Zustands
            this.aktienListe = savedInstanceState.getStringArrayList(STATE_DATA);
            Log.d(TAG, "Zustand des Fragments wieder hergestellt.");
        } else {
            createAdapter(getContext());
        }


        /**
         * Instanziert und bindet eine XML-View(fragment_aktienliste.xml) Datei an eine Viewinstanz (android.view.View).
         * Diese View ist das RootElement, über das die Kindelemente bezogehn werden können.
         */
        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);

        /**
         * Elemente, die sich in der 'rootView' befinden, können über ihre Id erreicht werden.
         * fragment_aktienliste.xml hat z.B. eine android.widget.ListView mir der android id "@+id/frameLayout"
         * und kann hierüber referenziert werden.
         */
        aktienlisteListView = (ListView) rootView.findViewById(R.id.listview_aktienliste);
        /**
         * Die Methode setAdapter() android.widget.ListView konsumiert eine Implementation vom des
         * Interfaces android.widget.ListAdapter. In diesm Fall also einen android.widget.ArrayAdapter
         * und weist den der View zu.
         */
        aktienlisteListView.setAdapter(this.mAktienAdapter);
        /**
         * Zum Aufrufen der Detailansicht wird fuer die ListView-Elemente ein Listener implementiert ...
         */
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

        /**
         * Das in der fragment_aktienliste.xml angelegte SwipeRefreshLayout anhand der Id
         * einer Objektreferenz zuweisen.
         */
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout_aktienliste);
        /**
         * Dem OnRefreshListener-Objekt des SwipeRefreshLayout den Aufruf der der aktualisiereDaten Methode
         * zuweisen und dem SwipeRefreshLayout uebergeben.
         */
        mSwipeRefreshLayout.setOnRefreshListener(this::aktualisiereDaten);

        return rootView;
    }

    private void createAdapter(Context context) {
        /**
         * Kosumiert den aktuellen Context ...
         * >> getContext() -> Die aktuelle Umgebung (diese Activity)
         * ... Die Referenz auf eine Layoutdatei ...
         * >> R.layout.list_item_aktienliste -> layout/list_item_aktienliste.xml
         * ... das in dieser Datei befindliche TextView - Element referenziert
         *     über die darin angegebene Id ...
         * >> R.id.list_item_aktienliste_textview -> android:id="@+id/list_item_aktienliste_textview"
         *  ... und eine java.util.List mit dem Datenvorrat für das AdapterArray.
         * >> aktienListe -> Beispieldaten in einer ArrayList
         *
         * Der ArrayAdapter besorgt sich eine View (Element) aus einer Datei und füllt dieses mit
         * Werten.
         */
        //@formatter:off
        mAktienAdapter = new ArrayAdapter<>(
                                    context,
                                    R.layout.list_item_aktienliste,
                                    R.id.list_item_aktienliste_textview,
                                    getAktienListe());
        //@formatter:on
    }

    @Override
    public void onSaveInstanceState (Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState: Daten werden gespeichert");

        int anzahlElemente = mAktienAdapter.getCount();

        String [] aktienlisteArray = new String[anzahlElemente];
        for (int i=0; i < anzahlElemente; i++) {
            aktienlisteArray[i] = mAktienAdapter.getItem(i);
        }

        savedInstanceState.putStringArray(STATE_DATA, aktienlisteArray);
    }

    /**
     * Bei der Erzeugung dieses Frgagments wird festgelegt, dass Menue-Events verarbeitet werden sollen
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        super.setHasOptionsMenu(true);
    }

    /**
     * Fuer das Menue soll als root-View die Datei menu_aktienfragment.xml mit der darin als Sub-View
     * (TextView) vorhandene action_daten_aktualisieren verwndenden.
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_aktienfragment, menu);
    }

    /**
     * Aktualisieren der Daten ueber den Menueeintragen
     * @param item Das ausloesende Item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        HoleDatenTask holeDatenTask = new HoleDatenTask();

        switch (item.getItemId()) {
            case R.id.action_daten_aktualisieren:
                aktualisiereDaten();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Erzeugt eine neue Klasse (HoleDatenTask) und delegiert das Erzeugen einer neuer Inhaltsliste
     * an deren geerbter execute Methode.
     */
    private void aktualisiereDaten() {
        HoleDatenTask holeDatenTask = new HoleDatenTask();

        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefAktienlisteKey = getString(R.string.preference_aktienliste_key);
        String prefAktienlisteDefault = getString(R.string.preference_aktienliste_default);
        String aktienliste = sPrefs.getString(prefAktienlisteKey, prefAktienlisteDefault);

        String prefIndizemodusKey = getString(R.string.preference_indizemodus_key);
        Boolean indizemodus = sPrefs.getBoolean(prefIndizemodusKey, false);

        if (indizemodus) {
            String indizeliste = "^GDAXI,^TECDAX,^MDAXI,^SDAXI,^GSPC,^N225,^HSI,XAGUSD=X,XAUUSD=X";
            holeDatenTask.execute(indizeliste);
        }
        else {
            holeDatenTask.execute(aktienliste);
        }

        Toast.makeText(getActivity(), "Aktiendaten werden abgefragt!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Erzeugt eine neue java.util.List, füllt sie mit einigen java.lang.String - Objeketen
     * und gibt sie zurück
     *
     * @return java.util.List
     */
    @Deprecated
    private List<String> makeStartList() {
        List<String> list = new ArrayList<>();
        list.add("Adidas - Kurs: 73,45 €");
        list.add("Allianz - Kurs: 145,12 €");
        list.add("BASF - Kurs: 84,27 €");
        list.add("Bayer - Kurs: 128,60 €");
        list.add("Beiersdorf - Kurs: 80,55 €");
        list.add("BMW St. - Kurs: 104,11 €");
        list.add("Commerzbank - Kurs: 12,47 €");
        list.add("Continental - Kurs: 209,94 €");
        list.add("Daimler - Kurs: 84,33 €");
        return list;
    }

    public List<String> getAktienListe(){
        if(this.aktienListe == null){
            this.aktienListe = new ArrayList<>();
        }
        return this.aktienListe;
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
            Log.d(TAG, "doInBackground: " + anfrageString);
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
                Log.d(TAG, "doInBackground: " + aktiendatenXmlString);
                publishProgress(1, 1);
            } catch (IOException e) {
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
                mAktienAdapter.clear();
//                for (String aktienString : strings){
//                    mAktienlisteAdapter.add(aktienString);
//                }
                mAktienAdapter.addAll(strings);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Toast.makeText(getActivity(), String.format("%s von %s geladen", values[0], values[1]), Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        private String[] leseXmlAktiendatenAus(String xmlString) {
            Document doc;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));
                doc = db.parse(is);

            } catch (Exception e) {
                Log.e(TAG, "leseXmlAktiendatenAus: Error " + e);
                return null;
            }

            Element xmlAktiendaten = doc.getDocumentElement();
            NodeList aktienNodeListe = xmlAktiendaten.getElementsByTagName("row");

            int anzahlAktien = aktienNodeListe.getLength();
            int anzahlAktienParameter = aktienNodeListe.item(0).getChildNodes().getLength();

            String[] ausgabeArray = new String[anzahlAktien];
            String[][] alleAktienDatenArray = new String[anzahlAktien][anzahlAktienParameter];

            Node aktienParameter;
            String aktienParameterWert;
            for (int i = 0; i < anzahlAktien; i++) {
                NodeList aktienParameterListe = aktienNodeListe.item(i).getChildNodes();

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

                Log.d(TAG, "XML Output:" + ausgabeArray[i]);
            }

            return ausgabeArray;
        }
    }
}