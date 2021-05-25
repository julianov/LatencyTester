package com.julian.fullping.ui.gallery;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.julian.fullping.R;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private AdView mAdView;

    EditText periodicidad, bytes, cantidadmax;
    Button apply;
    SharedPreferences sharedPreferences1, sharedPreferences2, sharedPreferences3;

    boolean volver;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });


        MobileAds.initialize(getContext(), "ca-app-pub-3241473434204149~3838592335");

        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        periodicidad = (EditText)root.findViewById(R.id.a1);
        bytes = (EditText) root.findViewById(R.id.a2);
        cantidadmax = (EditText) root.findViewById(R.id.a3);

        apply = (Button) root.findViewById(R.id.button2);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int periodo, cantidad;
                int tamaño;

                periodo=Integer.parseInt(periodicidad.getText().toString());
                tamaño= Integer.parseInt(bytes.getText().toString());
                cantidad=Integer.parseInt(cantidadmax.getText().toString() );

                if(periodo>0 && periodo<10000)
                {
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putInt("periodo", periodo);
                    editor1.commit();

                }
                if(tamaño> 10 && tamaño <65535)
                {
                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                    editor2.putInt("tamaño", tamaño);
                    editor2.commit();

                }

                SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                editor3.putInt("cantidad", cantidad);
                editor3.commit();


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("The changes have been applied");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        sharedPreferences1=getActivity().getApplicationContext().getSharedPreferences("periodo",getContext().MODE_PRIVATE);
        sharedPreferences2=getActivity().getApplicationContext().getSharedPreferences("tamaño",getContext().MODE_PRIVATE);
        sharedPreferences3=getActivity().getApplicationContext().getSharedPreferences("cantidad",getContext().MODE_PRIVATE);



        if(sharedPreferences1.getInt("periodo", 500)==500)
        {
            periodicidad.setText("500");
        }
        else {
            periodicidad.setText(String.valueOf(sharedPreferences1.getInt("periodo", 500)));
        }

        if(sharedPreferences2.getInt("tamaño", 32)==32)
        {
            bytes.setText("32");
        }
        else {
            bytes.setText(String.valueOf(sharedPreferences2.getInt("tamaño", 32)));
        }

        if(sharedPreferences3.getInt("cantidad", 0)==0)
        {
            cantidadmax.setText("0");
        }
        else {
            cantidadmax.setText(String.valueOf(sharedPreferences3.getInt("cantidad", 0)));
        }

        return root;
    }
}