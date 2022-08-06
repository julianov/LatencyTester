package com.julian.fullping.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.julian.fullping.R;
import com.julian.fullping.guardar_datos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    private TextView txt;
    private Button btn;
    private Button btn_save;

    EditText gethost;
    TextView maximo, minimo, promedio, visualizacion, pingvalue, paquetesenviados, paquetesperdidos;

    TextView tv1, tv2, tv3, tv4, tv5;

    ArrayList<Entry> yValues ;
    float [] valoresGrafica ;
    LineData data;
    LineChart grafica;

    String host;
    float average;
    float max;
    float min;
    float packagelost;
    double enviados;

    SharedPreferences sharedPreferences1, sharedPreferences2, sharedPreferences3;

    long periodo;
    String size;

    //interstical
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            if (sharedPreferences3.getInt("cantidad", 0) == 0 || (sharedPreferences3.getInt("cantidad", 0) > (int) enviados)) {
                if (checkWifiOnAndConnected()) {
                    float delay = (float) getLatency(host);
                    if (delay <= 0) {
                        packagelost++;
                    }
                    if (delay > 0) {

                        enviados++;
                        pingvalue.setText(String.valueOf(delay) + " ms");
                        graficar(delay);
                        valores(delay);
                        timerHandler.postDelayed(this, periodo);
                    } else {
                        timerHandler.postDelayed(this, periodo);
                    }
                } else {
                    timerHandler.removeCallbacks(timerRunnable);
                }
            }
            else{
                btn.setText("Stop");
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btn = (Button) root.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn.getText().equals("Ping")) {
                    if(checkWifiOnAndConnected())
                    {
                        btn_save.setVisibility(View.VISIBLE);

                        enviados=0;
                        host = gethost.getText().toString();
                        if (executeCommand()) {
                            visualizacion.setAlpha(1);
                            maximo.setAlpha(1);
                            minimo.setAlpha(1);
                            promedio.setAlpha(1);
                            pingvalue.setAlpha(1);
                            paquetesperdidos.setAlpha(1);
                            paquetesenviados.setAlpha(1);
                            tv1.setAlpha(1);
                            tv2.setAlpha(1);
                            tv3.setAlpha(1);
                            tv4.setAlpha(1);
                            tv5.setAlpha(1);

                            visualizacion.setText("Ping to " + host);
                            timerHandler.postDelayed(timerRunnable, 0);
                            btn.setText("Stop");
                            gethost.getText().clear();
                        }
                        else{

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Bad IP or Host domain name");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            // Toast.makeText(this, "Bad IP or Host domain name", Toast.LENGTH_SHORT).setTextColor(Color.RED).show();

                        }
                    }
                    else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("You are not connected to Wi-Fi network");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        // Toast.makeText(this, "You are not connected to Wi-Fi network", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    timerHandler.removeCallbacks(timerRunnable);
                    btn.setText("Ping");
                    visualizacion.setAlpha(0);
                    pingvalue.setAlpha(0);
                    average=0;
                    min=0;
                    max=0;
                }
            }
        });

        btn_save = (Button) root.findViewById(R.id.button5);
        btn_save.setVisibility(View.INVISIBLE);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();

                if (fm != null) {
                    // Perform the FragmentTransaction to load in the list tab content.
                    // Using FragmentTransaction#replace will destroy any Fragments
                    // currently inside R.id.fragment_content and add the new Fragment
                    // in its place.


                    String datos=host+"%"+String.valueOf(enviados)+"%"+String.valueOf(average)+"%"+String.valueOf(min)+"%"+String.valueOf(max)+"%"+String.valueOf(packagelost);
                    Bundle bundle = new Bundle();
                    bundle.putString("datos",datos);
                    Fragment fragment = new guardar_datos();
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container, fragment);
                    ft.commit();
                }
            }
        });

        gethost=(EditText) root.findViewById(R.id.editText);
        host=null;

        grafica= (LineChart) root.findViewById(R.id.LineChart);
        valoresGrafica = new float[50];
        grafica.setDragEnabled(true);
        grafica.getDescription().setEnabled(false); //set invisible description label
        yValues = new ArrayList<>();

        average=0;
        max=0;
        min=0;
        packagelost=0;
        enviados=0;

        maximo=(TextView) root.findViewById(R.id.textViewMax);
        minimo=(TextView) root.findViewById(R.id.textViewMin);
        promedio=(TextView) root.findViewById(R.id.textViewAverage);
        visualizacion=(TextView) root.findViewById(R.id.textView7);
        pingvalue=(TextView) root.findViewById(R.id.textView4);
        paquetesperdidos=(TextView) root.findViewById(R.id.textView11) ;
        paquetesenviados = (TextView) root.findViewById(R.id.textView10);

        tv1=(TextView) root.findViewById(R.id.textView) ;
        tv2=(TextView) root.findViewById(R.id.textView2) ;
        tv3=(TextView) root.findViewById(R.id.textView3) ;
        tv4=(TextView) root.findViewById(R.id.textView9) ;
        tv5=(TextView) root.findViewById(R.id.textView8) ;


        visualizacion.setAlpha(0);
        maximo.setAlpha(0);
        minimo.setAlpha(0);
        promedio.setAlpha(0);
        pingvalue.setAlpha(0);
        paquetesperdidos.setAlpha(0);
        paquetesenviados.setAlpha(0);
        tv1.setAlpha(0);
        tv2.setAlpha(0);
        tv3.setAlpha(0);
        tv4.setAlpha(0);
        tv5.setAlpha(0);

        sharedPreferences1=getActivity().getApplicationContext().getSharedPreferences("periodo",getContext().MODE_PRIVATE);
        sharedPreferences2=getActivity().getApplicationContext().getSharedPreferences("tamaño",getContext().MODE_PRIVATE);
        sharedPreferences3=getActivity().getApplicationContext().getSharedPreferences("cantidad",getContext().MODE_PRIVATE);

        periodo=(long) sharedPreferences1.getInt("periodo", 500);
        size=Integer.toString( sharedPreferences2.getInt("tamaño", 32));

        MobileAds.initialize(getContext(), "ca-app-pub-3241473434204149~3838592335");

        return root;
    }


    public double getLatency(String ipAddress) {
        String pingCommand = "/system/bin/ping -c " + "1" + " -s "+size+" " + ipAddress;
        String inputLine = "";
        double avgRtt = 0;
        timerHandler.removeCallbacks(timerRunnable);
        try {
            // execute the command on the environment interface
            Process process = Runtime.getRuntime().exec(pingCommand);
            // gets the input stream to get the output of the executed command

            int mExitValue =process.waitFor();
            if(mExitValue!=0)
            {
                return -1;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            inputLine = bufferedReader.readLine();
            if(inputLine==null)
            {
                timerHandler.postDelayed(timerRunnable, periodo);
                return 0;
            }
            while ((inputLine != null)) {
                if (inputLine.length() > 0 && inputLine.contains("avg")) {  // when we get to the last line of executed ping command
                    break;
                }
                inputLine = bufferedReader.readLine();
            }
            //cerramos el buffer
            bufferedReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            timerHandler.postDelayed(timerRunnable, periodo);
            return 0;
        } catch (InterruptedException e) {
            //this one is the last I did
            e.printStackTrace();
        }

        // Extracting the average round trip time from the inputLine string
        String afterEqual = inputLine.substring(inputLine.indexOf("="), inputLine.length()).trim();
        String afterFirstSlash = afterEqual.substring(afterEqual.indexOf('/') + 1, afterEqual.length()).trim();
        String strAvgRtt = afterFirstSlash.substring(0, afterFirstSlash.indexOf('/'));
        avgRtt = Double.valueOf(strAvgRtt);

        timerHandler.postDelayed(timerRunnable,periodo);
        return avgRtt;

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///FIN PING


    private boolean executeCommand(){
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 "+host);
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue "+mExitValue);
            if(mExitValue==0){
                return true;
            }else{
                return false;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println(" Exception:"+e);
        }
        return false;

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////graficar
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public void graficar (float valor)
    {

        grafica.clear();
        for (int i = 0; i < 49; i++) {
            valoresGrafica[i] = valoresGrafica[i + 1];
        }
        valoresGrafica[49] = (float) valor;


        yValues.clear();
        for (int i = 0; i < 50; i++) {
            yValues.add(new Entry(i, valoresGrafica[i]));

        }
        LineDataSet set1 = new LineDataSet(yValues, "");
        set1.setFillAlpha(110);
        set1.setColor(Color.GREEN); //para que la gráfica sea roja
        set1.setLineWidth(3f); //cambiamos el grosor de la linea
        set1.setCircleRadius(1);
        set1.setDrawValues(false);
        set1.setCircleColor(Color.GREEN);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);


        //eliminamos el eje y derecho, así nos queda solamente el izquierdo.
        YAxis rightAxis = grafica.getAxisRight();
        rightAxis.setEnabled(false);

        //eliminamos datos del eje x
        XAxis xAxis = grafica.getXAxis();
        xAxis.setEnabled(false);

        grafica.getAxisLeft().setAxisLineColor(Color.GREEN);
        grafica.getAxisLeft().setTextColor(Color.GREEN);
        grafica.setData(data);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////FIN graficar
    /////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////Valores promedios
    /////////////////////////////////////////////////////////////////////////////////////////////////

    public void valores(float delay)
    {

        average=0;

        for (int i=0; i< 50; i++)
        {
            if(valoresGrafica[i]> max)
            {
                max=valoresGrafica[i];
            }
            if(min==0&&valoresGrafica[i]>0)
            {
                min=valoresGrafica[i];
            }
            if(min!=0&&valoresGrafica[i]<min)
            {
                min=valoresGrafica[i];
            }
            average+=valoresGrafica[i];

        }
        average/=50;

        paquetesperdidos.setText(String.format("%.0f", packagelost));
        paquetesenviados.setText(String.format("%.0f",enviados));
        maximo.setText(String.valueOf(max));
        minimo.setText(String.valueOf(min));
        promedio.setText(String.format("%.3f",average));
    }

    protected void noWifi()
    {

        // Toast.makeText(this, "No Wifi connection", Toast.LENGTH_SHORT).show();
        timerHandler.removeCallbacks(timerRunnable);
        btn.setText("Ping");
        visualizacion.setAlpha(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Wifi connection. It is not possible to send ICMP package");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private boolean checkWifiOnAndConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }
        else{
            return false;
        }

    }

    public void wifilosed()
    {
        btn.setText("Ping");
        Toast.makeText(getContext(), "Wi-Fi connectivity was lost", Toast.LENGTH_SHORT).show();

    }

    public void stop_everything(){
        timerHandler.removeCallbacks(timerRunnable);
        btn.setText("Ping");
        visualizacion.setAlpha(0);
        pingvalue.setAlpha(0);
        average=0;
        min=0;
        max=0;
    }


}