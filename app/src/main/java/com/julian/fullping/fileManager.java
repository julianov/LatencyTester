package com.julian.fullping;

        import android.content.Context;
        import android.util.Log;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;

        import static android.content.Context.MODE_PRIVATE;

public class fileManager {

    String filename;

    public fileManager(){
    }

    public fileManager(String filename){
        this.filename=filename;
    }

    private void  set_name(String filename){
        this.filename=filename;
    }

    boolean if_exists(){
        File file = new File(filename);
        if (file.exists() && file.isFile())
            return true;
        else
            return false;
    }

    boolean delete(){
        File file = new File(filename);
        return file.delete();
    }

    boolean writeDataToFile(Context cont,String data) throws FileNotFoundException {
        Log.d("datos a escribir", data);
        try {
            FileOutputStream fos = cont.openFileOutput(filename, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(data);
            osw.flush();
            osw.close();
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    String readDataFromFile(Context cont) {
        try {
            FileInputStream fis = cont.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[100];
            String cadena = "";
            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                if (readString!=null){
                    cadena += readString;
                }
                inputBuffer = new char[100];
            }
            isr.close();
            Log.d("cadena", cadena);

            if(cadena!=null){
                return cadena;
            }else{
                return "";
            }

        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}