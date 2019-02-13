package com.example.myaktiehq;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AktiendetailFragment extends Fragment {
    public AktiendetailFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aktiendetail, container, false);
        Intent empfangenerIntenet = getActivity().getIntent();
        if(empfangenerIntenet != null && empfangenerIntenet.hasExtra(Intent.EXTRA_TEXT)){
            ((TextView) rootView.findViewById(R.id.aktiendetail_text)).setText(empfangenerIntenet.getStringExtra(Intent.EXTRA_TEXT));
        }
        return rootView;
    }
}
