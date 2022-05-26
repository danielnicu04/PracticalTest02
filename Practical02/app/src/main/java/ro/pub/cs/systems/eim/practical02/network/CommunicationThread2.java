//package ro.pub.cs.systems.eim.practical02.network;
//
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.HashMap;
//
//import cz.msebera.android.httpclient.HttpEntity;
//import cz.msebera.android.httpclient.HttpResponse;
//import cz.msebera.android.httpclient.client.HttpClient;
//import cz.msebera.android.httpclient.client.methods.HttpGet;
//import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
//import cz.msebera.android.httpclient.util.EntityUtils;
//import ro.pub.cs.systems.eim.practical02.general.Constants;
//import ro.pub.cs.systems.eim.practical02.general.Utilities;
//import ro.pub.cs.systems.eim.practical02.model.CurrencyInfo;
//import ro.pub.cs.systems.eim.practical02.model.WeatherForecastInformation;
//
//public class CommunicationThread2 extends Thread {
//    private ServerThread serverThread;
//    private Socket socket;
//
//    /**
//     * Take the serverThread as parameter in the constructor.
//     * Take the socket as parameter as well.
//     * @param serverThread
//     * @param socket
//     */
//    public CommunicationThread2(ServerThread serverThread, Socket socket) {
//        this.serverThread = serverThread;
//        this.socket = socket;
//    }
//
//    @Override
//    public void run() {
//        if (socket == null) {
//            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
//            return;
//        }
//        try {
//
//            /*We use the input and output streams available on the socket
//             * to read or write data.*/
//            BufferedReader bufferedReader = Utilities.getReader(socket);
//            PrintWriter printWriter = Utilities.getWriter(socket);
//            if (bufferedReader == null || printWriter == null) {
//                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
//                return;
//            }
//            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client ( currency !");
//
//            String currency = bufferedReader.readLine();
//            if (currency == null || currency.isEmpty()) {
//                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client ( currency!");
//                return;
//            }
//
//
//            HashMap<String, CurrencyInfo> data = serverThread.getData();
//
//            CurrencyInfo currencyInfo = null;
//            if (data.containsKey("Currency")) {
//                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
//                //weatherForecastInformation = data.get(city);
//                currencyInfo = data.get("Currency");
//            } else {
//                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
//                HttpClient httpClient = new DefaultHttpClient();
//                String pageSourceCode = "";
//                if(false) {
////                    HttpPost httpPost = new HttpPost(Constants.WEB_SERVICE_ADDRESS);
////                    List<NameValuePair> params = new ArrayList<>();
////                    params.add(new BasicNameValuePair("q", city));
////                    params.add(new BasicNameValuePair("mode", Constants.WEB_SERVICE_MODE));
////                    params.add(new BasicNameValuePair("APPID", Constants.WEB_SERVICE_API_KEY));
////                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
////                    httpPost.setEntity(urlEncodedFormEntity);
////                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
////
////                    pageSourceCode = httpClient.execute(httpPost, responseHandler);
//                } else {
//                    HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
//                    HttpResponse httpGetResponse = httpClient.execute(httpGet);
//                    HttpEntity httpGetEntity = httpGetResponse.getEntity();
//                    if (httpGetEntity != null) {
//                        pageSourceCode = EntityUtils.toString(httpGetEntity);
//                    }
//                }
//
//                if (pageSourceCode == null) {
//                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
//                    return;
//                } else
//                    Log.i(Constants.TAG, pageSourceCode );
//
//                // Updated for openweather API
//                if (false) {
////                    Document document = Jsoup.parse(pageSourceCode);
////                    Element element = document.child(0);
////                    Elements elements = element.getElementsByTag(Constants.SCRIPT_TAG);
////                    for (Element script : elements) {
////                        String scriptData = script.data();
////                        if (scriptData.contains(Constants.SEARCH_KEY)) {
////                            int position = scriptData.indexOf(Constants.SEARCH_KEY) + Constants.SEARCH_KEY.length();
////                            scriptData = scriptData.substring(position);
////                            JSONObject content = new JSONObject(scriptData);
////                            JSONObject currentObservation = content.getJSONObject(Constants.CURRENT_OBSERVATION);
////                            String temperature = currentObservation.getString(Constants.TEMPERATURE);
////                            String windSpeed = currentObservation.getString(Constants.WIND_SPEED);
////                            String condition = currentObservation.getString(Constants.CONDITION);
////                            String pressure = currentObservation.getString(Constants.PRESSURE);
////                            String humidity = currentObservation.getString(Constants.HUMIDITY);
////                            weatherForecastInformation = new WeatherForecastInformation(
////                                    temperature, windSpeed, condition, pressure, humidity
////                            );
////                            serverThread.setData(city, weatherForecastInformation);
////                            break;
////                        }
////                    }
//                } else {
//                    JSONObject content = new JSONObject(pageSourceCode);
//                    JSONObject usdJSON = content.getJSONObject("bpi").getJSONObject("USD");
//                    JSONObject eurJSON = content.getJSONObject("bpi").getJSONObject("EUR");
//
//                    double usd = usdJSON.getDouble("rate_float");
//                    double eur = eurJSON.getDouble("rate_float");
//
//                    currencyInfo = new CurrencyInfo(eur, usd);
//
//                    serverThread.setData("Currency", currencyInfo);
//                }
//            }
//
//            if (currencyInfo == null) {
//                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Weather Forecast Information is null!");
//                return;
//            }
//
//            String result = null;
//            switch(currency) {
//                case Constants.EUR:
//                    result = String.valueOf(currencyInfo.getEUR());
//                    break;
//                case Constants.USD:
//                    result = String.valueOf(currencyInfo.getUSD());
//                    break;
//                default:
//                    result = "[COMMUNICATION THREAD] Wrong information type (all / temperature / wind_speed / condition / humidity / pressure)!";
//            }
//            printWriter.println(result);
//            printWriter.flush();
//        } catch (IOException ioException) {
//            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
//            if (Constants.DEBUG) {
//                ioException.printStackTrace();
//            }
//        } catch (JSONException jsonException) {
//            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
//            if (Constants.DEBUG) {
//                jsonException.printStackTrace();
//            }
//        } finally {
//            if (socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException ioException) {
//                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
//                    if (Constants.DEBUG) {
//                        ioException.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//}
