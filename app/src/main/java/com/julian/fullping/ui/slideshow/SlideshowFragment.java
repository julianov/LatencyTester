package com.julian.fullping.ui.slideshow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.julian.fullping.BuildConfig;
import com.julian.fullping.R;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    Button compartir;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        compartir= (Button) root.findViewById(R.id.button3);
            compartir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Network latency tester - Full Ping");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Network latency tester app clicke here to visit https://play.google.com/store/apps/details?id=com.julian.fullping");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });

        return root;
    }



}