package ro.pub.cs.systems.eim.practical02.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;
import ro.pub.cs.systems.eim.practical02.general.Constants;
import ro.pub.cs.systems.eim.practical02.general.Utilities;
import ro.pub.cs.systems.eim.practical02.model.WeatherForecastInformation;

public class CommunicationThread extends Thread{
    private ServerThread serverThread;
    private Socket socket;

    /**
     * Take the serverThread as parameter in the constructor.
     * Take the socket as parameter as well.
     * @param serverThread
     * @param socket
     */
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {

            /*We use the input and output streams available on the socket
            * to read or write data.*/
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");

            String key = bufferedReader.readLine();
            String value = bufferedReader.readLine();
            String requestType = bufferedReader.readLine();
            if (key == null || key.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }


            HashMap<String, String> data = serverThread.getData();

            if(requestType.equals("GET") && key.equals("unixtime")) {
                String responseValue = null;

                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";


                HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                if (httpGetEntity != null) {
                    pageSourceCode = EntityUtils.toString(httpGetEntity);
                }


                if (pageSourceCode == null) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    return;
                } else
                    Log.i(Constants.TAG, pageSourceCode);

                // Updated for openweather API
                JSONObject content = new JSONObject(pageSourceCode);

                responseValue = content.getString(key);

                String response = "";
                if(data.containsKey("unixtime")) {
                    int valCache = Integer.parseInt(data.get("unixtime"));
                    int valOnServer = Integer.parseInt(responseValue);
                    if(valOnServer - valCache >= 60) {
                        response = "60 seconds passed, updating entry from hashtable";
                        serverThread.setData(key, responseValue);
                        printWriter.println(response);
                        printWriter.flush();
                    }
                    else {
                        response = String.valueOf(valCache);
                        printWriter.println(response);
                        printWriter.flush();
                    }
                }
                else {
                    printWriter.println(responseValue);
                    printWriter.flush();
                }
            }
            else {
                if (requestType.equals("GET")) {
                    String responseValue = null;
                    if (data.containsKey(key)) {
                        Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
                        responseValue = data.get(key);
                    } else {
                        Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                        HttpClient httpClient = new DefaultHttpClient();
                        String pageSourceCode = "";


                        HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
                        HttpResponse httpGetResponse = httpClient.execute(httpGet);
                        HttpEntity httpGetEntity = httpGetResponse.getEntity();
                        if (httpGetEntity != null) {
                            pageSourceCode = EntityUtils.toString(httpGetEntity);
                        }


                        if (pageSourceCode == null) {
                            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                            return;
                        } else
                            Log.i(Constants.TAG, pageSourceCode);

                        // Updated for openweather API
                        JSONObject content = new JSONObject(pageSourceCode);

                        responseValue = content.getString(key);

                        if (responseValue != null)
                            serverThread.setData(key, responseValue);

                        if (responseValue == null) {
                            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Response Info is null!");
                            return;
                        }
                    }

                    String result = responseValue;
                    printWriter.println(result);
                    printWriter.flush();
                } else {
                    serverThread.setData(key, value);
                    printWriter.println(serverThread.getData().get(key));
                    printWriter.flush();
                }
            }
        } catch (IOException | JSONException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
