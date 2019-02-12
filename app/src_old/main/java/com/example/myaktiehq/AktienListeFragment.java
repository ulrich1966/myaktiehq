package com.example.myaktiehq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AktienListeFragment extends Fragment {
    private static final String TAG = AktienListeFragment.class.getName();

    public AktienListeFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context activity = getActivity();
        List<String> aktienListe = null;

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

        Log.d(TAG, aktienlisteArray.toString());
        Log.d(TAG, "Activity "+activity);

        aktienListe = new ArrayList<>(Arrays.asList(aktienlisteArray));

        ArrayAdapter<String> aktienlisteAdapter =
                new ArrayAdapter<>(
                        activity,                          // Die aktuelle Umgebung (diese Activity)
                        R.layout.list_item_aktienliste,         // ID der XML-Layout Datei
                        R.id.list_item_aktienliste_textview,    // ID des TextViews
                        aktienListe);                           // Beispieldaten in einer ArrayList

        View rootView = inflater.inflate(R.layout.fragment_aktienliste, container, false);
        ListView aktenlisteListView = rootView.findViewById(R.id.listview_aktienliste);
        aktenlisteListView.setAdapter(aktienlisteAdapter);

        return rootView;
    }
}
