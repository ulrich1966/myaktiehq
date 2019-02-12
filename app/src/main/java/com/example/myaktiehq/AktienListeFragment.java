package com.example.myaktiehq;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AktienListeFragment extends Fragment {

    public AktienListeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Activity activity;
        ArrayAdapter<String> aktienlisteAdapter = null;
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


        try {
            if (null != this.getActivity()) {
                activity = this.getActivity();
                aktienlisteAdapter = new ArrayAdapter<>(
                        activity,                               // Die aktuelle Umgebung (diese Activity)
                        R.layout.list_item_aktienliste,         // ID der XML-Layout Datei
                        R.id.list_item_aktienliste_textview,    // ID des TextViews
                        aktienListe);                           // Beispieldaten in einer ArrayList
            } else {
                throw new NullPointerException("Mist!");
            }
        } catch (NullPointerException e) {
            System.err.print(e.getMessage());
            // do somthing else ...
        }

        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);
        ListView aktenlisteListView = rootView.findViewById(R.id.listview_aktienlist);
        aktenlisteListView.setAdapter(aktienlisteAdapter);

        return rootView;
    }
}
