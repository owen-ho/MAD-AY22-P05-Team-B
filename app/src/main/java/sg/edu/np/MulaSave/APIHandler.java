package sg.edu.np.MulaSave;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class APIHandler {

    private static final String TAG = APIHandler.class.getSimpleName();

    public APIHandler(){
    }

    public String httpServiceCall(String requestUrl){
        String result=null;
        try{
            //Reads inputted URL based on API chosen and establishes connection
            URL url = new URL(requestUrl);
            URLConnection connection = url.openConnection();

            //Use InputStream to read information from URL
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            result = convertResultToString(inputStream);
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (ProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    private String convertResultToString(InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String li;

        while (true){
            try{
                if ((li = bufferedReader.readLine()) != null){//Reads data line by line to add to a string
                    stringBuilder.append(li);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();
        }
    }

}
