package sg.edu.np.MulaSave;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class Memorydata {
    public static void savedata(String data, Context context){
        try{
            FileOutputStream fileOutputStream = context.openFileOutput("datata.txt",Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void savelastmsgts(String data, Context context){
        try{
            FileOutputStream fileOutputStream = context.openFileOutput("lastmsgts.txt",Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void savename(String data, Context context){
        try{
            FileOutputStream fileOutputStream = context.openFileOutput("namee.txt",Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getdata(Context context){
        String data = "";
        try{
            FileInputStream fis = context.openFileInput("datata.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!=null){
                sb.append(line);
            }
            data = sb.toString();

        }catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }
    public static String getname(Context context){
        String data = "";
        try{
            FileInputStream fis = context.openFileInput("namee.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!=null){
                sb.append(line);
            }
            data = sb.toString();

        }catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }
    public static String getlastmsgts(Context context){
        String data = "0";
        try{
            FileInputStream fis = context.openFileInput("lastmsgts.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!=null){
                sb.append(line);
            }
            data = sb.toString();

        }catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }

}




