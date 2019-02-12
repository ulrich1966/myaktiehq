package com.example.myaktiehq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AktienlisteFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            String [] aktienlisteArray = {
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

            ArrayAdapter<String> aktienlisteAdapter =
                    new ArrayAdapter<>(
                            getActivity(), // Die aktuelle Umgebung (diese Activity)
                            R.layout.list_item_aktienliste, // ID der XML-Layout Datei
                            R.id.list_item_aktienliste_textview, // ID des TextViews
                            aktienListe); // Beispieldaten in einer ArrayList

            View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);

            ListView aktienlisteListView = (ListView) rootView.findViewById(R.id.listview_aktienliste);
            aktienlisteListView.setAdapter(aktienlisteAdapter);

            return rootView;

        }
}
