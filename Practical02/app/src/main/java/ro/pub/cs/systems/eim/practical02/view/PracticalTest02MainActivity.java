package ro.pub.cs.systems.eim.practical02.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practical02.network.ClientThread;
import ro.pub.cs.systems.eim.practical02.general.Constants;
import ro.pub.cs.systems.eim.practical02.R;
import ro.pub.cs.systems.eim.practical02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText keyEditText = null;
    private EditText valueEditText = null;
    private Button getRequestButton = null;
    private Button postRequestButton = null;
    private TextView weatherForecastTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        /**
         * What we do:
         * Basically we start the server when clicking the connect button.
         * Upon starting the server we wait for connections/requests from clients.
         * @param view
         */
        @Override
        public void onClick(View view) {
            /*Extract serverPort from editText*/
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            /*Start the server on a separate thread.*/
            serverThread.start();
        }

    }

//    private GetWeatherForecastButtonClickListener getWeatherForecastButtonClickListener = new GetWeatherForecastButtonClickListener();
//    private class GetWeatherForecastButtonClickListener implements Button.OnClickListener {
//
//        @Override
//        public void onClick(View view) {
//            String clientAddress = clientAddressEditText.getText().toString();
//            String clientPort = clientPortEditText.getText().toString();
//            if (clientAddress == null || clientAddress.isEmpty()
//                    || clientPort == null || clientPort.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (serverThread == null || !serverThread.isAlive()) {
//                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String city = cityEditText.getText().toString();
//            String informationType = informationTypeSpinner.getSelectedItem().toString();
//            if (city == null || city.isEmpty()
//                    || informationType == null || informationType.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            weatherForecastTextView.setText(Constants.EMPTY_STRING);
//
//            clientThread = new ClientThread(
//                    clientAddress, Integer.parseInt(clientPort), city, informationType, weatherForecastTextView
//            );
//            clientThread.start();
//        }
//
//    }

//    private GetCurrencyInfoButtonClickListener getCurrencyInfoButtonClickListener  = new GetCurrencyInfoButtonClickListener();
//    private class GetCurrencyInfoButtonClickListener implements Button.OnClickListener {
//
//        @Override
//        public void onClick(View view) {
//            String clientAddress = clientAddressEditText.getText().toString();
//            String clientPort = clientPortEditText.getText().toString();
//            if (clientAddress == null || clientAddress.isEmpty()
//                    || clientPort == null || clientPort.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (serverThread == null || !serverThread.isAlive()) {
//                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            String currencyInfo = informationTypeSpinner.getSelectedItem().toString();
//            if (currencyInfo == null || currencyInfo.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (currency) should be filled", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//
//            currencyTextView.setText(Constants.EMPTY_STRING);
//
//            clientThread = new ClientThread(
//                    clientAddress, Integer.parseInt(clientPort), currencyInfo, currencyTextView
//            );
//            clientThread.start();
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_main);

        //Resources used by the server
        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        //Resources used by the client
        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        keyEditText = (EditText)findViewById(R.id.key_edit_text);
        valueEditText = (EditText)findViewById(R.id.value_edit_text);

        getRequestButton = (Button)findViewById(R.id.get_button);
        postRequestButton = (Button)findViewById(R.id.post_button);
        //getWeatherForecastButton.setOnClickListener(getWeatherForecastButtonClickListener);
        weatherForecastTextView = (TextView)findViewById(R.id.weather_forecast_text_view);
    }

//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
//        setContentView(R.layout.activity_practical_test02_main);
//
//        //Resources used by the server
//        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
//        connectButton = (Button)findViewById(R.id.connect_button);
//        connectButton.setOnClickListener(connectButtonClickListener);
//
//        //Resources used by the client
//        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
//        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
//        informationTypeSpinner = (Spinner)findViewById(R.id.information_type_spinner);
//        getCurrencyButton = (Button)findViewById(R.id.get_currency);
//        getCurrencyButton.setOnClickListener(getCurrencyInfoButtonClickListener);
//        currencyTextView = (TextView)findViewById(R.id.currency_text_view);
//    }


    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}