package com.julian.fullping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Datos_guardados#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Datos_guardados extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListView lista;
    ArrayAdapter<String> adaptador;
    ArrayList<String> a;
    ArrayList<Integer> pos;

    Button volver;

    public Datos_guardados() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Datos_guardados.
     */
    // TODO: Rename and change types and number of parameters
    public static Datos_guardados newInstance(String param1, String param2) {
        Datos_guardados fragment = new Datos_guardados();
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_datos_guardados, container, false);


        fileManager archivo = new fileManager("fullping.txt");
        String datos= archivo.readDataFromFile(getActivity().getApplicationContext());

        final String array_datos [] = datos.split("\n");

        lista = (ListView) root.findViewById(R.id.lista);

        a = new ArrayList<String>(0);
        pos =new ArrayList<Integer>(0);

        for (int i = array_datos.length-1; i >= 0; i--) {
            a.add(array_datos[i].split("%") [1]+" - " + array_datos[i].split("%")[0]);
            pos.add(i);

        }

        //a.remove(0);

        adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, a);
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                         @Override
                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                             a.get(position);

                                             FragmentManager fm = getFragmentManager();

                                             if (fm != null) {
                                             Bundle bundle = new Bundle();
                                             bundle.putString("datos",array_datos[position]);
                                             Fragment fragment = new dato_seleccionado();
                                             fragment.setArguments(bundle);

                                             FragmentTransaction ft = fm.beginTransaction();
                                             ft.replace(R.id.container, fragment);
                                             ft.commit();
                                             }

                                         }
                                     }
        );

        return root;
    }
}