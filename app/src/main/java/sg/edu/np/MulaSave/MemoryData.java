package sg.edu.np.MulaSave;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Memory class is to store data and a output stream for writing data into a file and also returning data based on the individual method
 * try catch to do error handling
 */
public final class MemoryData {
    public static void savedata(String data, Context context){
        try{
            FileOutputStream fileOutputStream = context.openFileOutput("datata.txt",Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void savelastmsgts(String data,String chatid, Context context){
        try{
            FileOutputStream fileOutputStream = context.openFileOutput(chatid+".txt",Context.MODE_PRIVATE);
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
    public static String getlastmsgts(Context context,String chatid){
        String data = "0";
        try{
            FileInputStream fis = context.openFileInput(chatid+".txt");
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




