package com.example.myaktiehq;

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

public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        ArrayAdapter<String> aktenListAdater = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_aktienliste,
                R.id.list_item_aktienliste_txtview,
                aktienListe
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView aktenListView = rootView.findViewById(R.id.listView_aktienliste);
        aktenListView.setAdapter(aktenListAdater);

        return inflater.inflate(R.layout.fragment_main, container, false);

    }
}
