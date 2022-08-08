package com.julian.fullping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.julian.fullping.ui.home.HomeFragment;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link guardar_datos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class guardar_datos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "datos";

    // TODO: Rename and change types of parameters
    private String mParam1;

    String host;
    float average;
    float max;
    float min;
    float packagelost;
    double enviados;

    TextView thost;
    TextView taverage;
    TextView tmax;
    TextView tmin;
    TextView tpackagelost;
    TextView tenviados;

    Button save;

    public guardar_datos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment guardar_datos.
     */
    // TODO: Rename and change types and number of parameters
    public static guardar_datos newInstance(String param1, String param2) {
        guardar_datos fragment = new guardar_datos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            host=mParam1.split("%")[0];
            average= Float.parseFloat(mParam1.split("%")[2]);
            max= Float.parseFloat(mParam1.split("%")[4]);
            min= Float.parseFloat(mParam1.split("%")[3]);
            packagelost= Float.parseFloat(mParam1.split("%")[5]);
            enviados= Double.parseDouble(mParam1.split("%")[1]);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_guardar_datos, container, false);

        thost = (TextView) root.findViewById(R.id.textView21);
        taverage = (TextView) root.findViewById(R.id.textView22);
        tmax = (TextView) root.findViewById(R.id.textView23);
        tmin = (TextView) root.findViewById(R.id.textView24);
        tpackagelost = (TextView) root.findViewById(R.id.textView25);
        tenviados = (TextView) root.findViewById(R.id.textView26);

        thost.setText(host);
        taverage.setText(String.valueOf(average)+" ms");
        tmax.setText(String.valueOf(max)+" ms");
        tmin.setText(String.valueOf(min)+" ms");
        tpackagelost.setText(String.valueOf(packagelost));
        tenviados.setText(String.valueOf(enviados));

        save=(Button) root.findViewById(R.id.button4);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileManager archivo = new fileManager("fullping.txt");
                Log.d("dato", String.valueOf(archivo.if_exists() ));

                String datos= archivo.readDataFromFile(getActivity().getApplicationContext());

                if (datos!=null && datos!=""){

                    Log.d("dato", "aca debería llegar porque ya existe algo");

                    //String datos= archivo.readDataFromFile(getActivity().getApplicationContext());

                    Calendar calendar = Calendar.getInstance();
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String date = dateFormat.format(calendar.getTime());

                    int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    int seconds = calendar.get(Calendar.SECOND);

                    String hora= String.valueOf(hour24hrs)+":"+String.valueOf(minutes)+":"+String.valueOf(seconds);

                    String datos_nuevos=date+" - "+hora+"%"+host+"%"+String.valueOf(enviados)+"%"+String.valueOf(average)+"%"+String.valueOf(min)+"%"+String.valueOf(max)+"%"+String.valueOf(packagelost)+"\n";
                    datos+=datos_nuevos;

                    try {
                        archivo.writeDataToFile(getActivity().getApplicationContext(), datos);

                        Toast.makeText(getActivity().getApplicationContext(), "DATA SAVED", Toast.LENGTH_LONG).show();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                        Fragment fragment = new Datos_guardados();
                        ft.replace(R.id.nav_host_fragment, fragment).addToBackStack("tag").hide(guardar_datos.this);
                        ft.commit();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                        Fragment fragment = new HomeFragment();
                        ft.replace(R.id.container, fragment).addToBackStack("tag").hide(guardar_datos.this);
                        ft.commit();

                    }

                }else{

                    Calendar calendar = Calendar.getInstance();
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String date = dateFormat.format(calendar.getTime());

                    int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    int seconds = calendar.get(Calendar.SECOND);

                    String hora= String.valueOf(hour24hrs)+":"+String.valueOf(minutes)+":"+String.valueOf(seconds);


                    String datos_nuevos=date+" - "+hora+"%"+host+"%"+String.valueOf(enviados)+"%"+String.valueOf(average)+"%"+String.valueOf(min)+"%"+String.valueOf(max)+"%"+String.valueOf(packagelost)+"\n";

                    try {
                        archivo.writeDataToFile(getActivity().getApplicationContext(), datos_nuevos);

                        Toast.makeText(getActivity().getApplicationContext(), "DATA SAVED", Toast.LENGTH_LONG).show();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                        Fragment fragment = new Datos_guardados();
                        ft.replace(R.id.nav_host_fragment, fragment).addToBackStack("tag").hide(guardar_datos.this);
                        ft.commit();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                        Fragment fragment = new HomeFragment();
                        ft.replace(R.id.container, fragment).addToBackStack("tag").hide(guardar_datos.this);
                        ft.commit();

                    }


                }



            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
}