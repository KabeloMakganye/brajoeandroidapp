package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Button btnHit;
    public TextView txtJson;
    ProgressDialog pd;
    public static String ab="h";
    public void setAb(String v) {
        this.ab=v;
    }
    public String getAb() {
        return this.ab;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // EditText edtTextName = findViewById(R.id.editTextTextPersonName);
        TextView txtJson = findViewById(R.id.txtMessage);
        EditText edtTxtName = findViewById(R.id.editTextTextPersonName);
        EditText edtTxtPassword = findViewById(R.id.editTextTextPassword);

        btnHit =findViewById(R.id.btnHello);

        btnHit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(edtTxtName.getText().toString().length() == 0 || edtTxtPassword.getText().toString().length() == 0) {
                    txtJson.setText("Enter all fields");
                }else{
                    Log.d("desss", "onClick: " + edtTxtName.getText().toString().length());
                    new JsonTask().execute("https://kabelodatabase.herokuapp.com/get_user/" + edtTxtName.getText().toString());
                }
            }
        });

        //TODO: add button here
    }
  /* public void onBtnclick(View view) {


        EditText edtTxtName = findViewById(R.id.editTextTextPersonName);

        TextView txtHello = findViewById(R.id.txtMessage);
        // txtHello.setText("Welcome "+edtTxtName.getText().toString());

    } */

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(true);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Log.d("internet status:", String.valueOf(connection.getResponseCode()));
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnknownHostException e){
                // e.printStackTrace();
                return "121212";
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "404";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            TextView txtJson = findViewById(R.id.txtMessage);
            if (result.length() == 3)
            {
                txtJson.setText("Invalid user");
            } else if(result == "121212") {
                txtJson.setText("Internet issues");
            } else {
                txtJson.setText(result);
            }

            Log.d("Testing :" ,result +" length: "+ String.valueOf(result.length()));
        }
    }
}