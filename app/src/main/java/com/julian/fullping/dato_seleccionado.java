package com.julian.fullping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.julian.fullping.ui.home.HomeFragment;

import java.io.FileNotFoundException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dato_seleccionado#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dato_seleccionado extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String date;
    String host;
    float average;
    float max;
    float min;
    float packagelost;
    double enviados;

    TextView tdate;
    TextView thost;
    TextView taverage;
    TextView tmax;
    TextView tmin;
    TextView tpackagelost;
    TextView tenviados;

    Button home,delete;

    public dato_seleccionado() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dato_seleccionado.
     */
    // TODO: Rename and change types and number of parameters
    public static dato_seleccionado newInstance(String param1, String param2) {
        dato_seleccionado fragment = new dato_seleccionado();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

            date=mParam1.split("%")[0];
            host=mParam1.split("%")[1];
            average= Float.parseFloat(mParam1.split("%")[3]);
            max= Float.parseFloat(mParam1.split("%")[5]);
            min= Float.parseFloat(mParam1.split("%")[4]);
            packagelost= Float.parseFloat(mParam1.split("%")[6]);
            enviados= Double.parseDouble(mParam1.split("%")[2]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_dato_seleccionado, container, false);

        thost = (TextView) root.findViewById(R.id.textView30);
        tdate = (TextView) root.findViewById(R.id.textView31);
        taverage = (TextView) root.findViewById(R.id.textView32);
        tmax = (TextView) root.findViewById(R.id.textView33);
        tmin = (TextView) root.findViewById(R.id.textView34);
        tpackagelost = (TextView) root.findViewById(R.id.textView35);
        tenviados = (TextView) root.findViewById(R.id.textView36);

        thost.setText(host);
        tdate.setText("DATE:\n\n"+date);
        taverage.setText("AVERAGE DELAY:\n\n"+String.valueOf(average));
        tmax.setText("MAX. DELAY:\n\n"+String.valueOf(max));
        tmin.setText("MIN. DELAY:\n\n"+String.valueOf(min));
        tpackagelost.setText("PACKAGE LOST:\n\n"+String.valueOf(packagelost));
        tenviados.setText("PACKAGE SENT:\n\n"+String.valueOf(enviados));

        home=(Button) root.findViewById(R.id.button6);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();

                if (fm != null) {
                    // Perform the FragmentTransaction to load in the list tab content.
                    // Using FragmentTransaction#replace will destroy any Fragments
                    // currently inside R.id.fragment_content and add the new Fragment
                    // in its place.
                    Fragment fragment = new HomeFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container, fragment);
                    ft.commit();
                }
            }
        });
        delete=(Button) root.findViewById(R.id.button7);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileManager archivo = new fileManager("fullping.txt");
                String datos = archivo.readDataFromFile(getActivity().getApplicationContext());
                String array_datos [] = datos.split("\n");

                String nuevos_datos="";
                for (int i = 0; i<array_datos.length; i++ ){

                    if (array_datos[i].split("%")[0]==date && array_datos[i].split("%")[1]==host && Float.parseFloat(array_datos[i].split("%")[2])==enviados ){

                    }else{
                        nuevos_datos+=array_datos[i];
                    }
                }

                try {
                    archivo.writeDataToFile(getActivity().getApplicationContext(), nuevos_datos);
                    Toast.makeText(getActivity().getApplicationContext(), "DATA DELETED", Toast.LENGTH_LONG).show();
                    FragmentManager fm = getFragmentManager();

                    if (fm != null) {
                        // Perform the FragmentTransaction to load in the list tab content.
                        // Using FragmentTransaction#replace will destroy any Fragments
                        // currently inside R.id.fragment_content and add the new Fragment
                        // in its place.
                        Fragment fragment = new HomeFragment();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.container, fragment);
                        ft.commit();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });


        return root;
    }
}