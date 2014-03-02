package com.example.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PostRequester extends AsyncTask<Void, Void, String> {

    private String results;
    private HttpClient httpclient;
    private postListener listener;
    private int channel;
    HttpResponse response;
    HttpPost httppost;
    List<NameValuePair> params;

    public PostRequester(postListener pl, int ch){
        listener = pl;
        channel = ch;
    }

    public void newRequest(String url, int numParams) {
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost(url);
        params = new ArrayList<NameValuePair>(numParams);
    }

    public void addParam(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
    }

    public void sendRequest() {
        try{
            httppost.setEntity(new UrlEncodedFormEntity(params));
            this.execute();

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
        }
    }

    public String getResults() {
        return results;
    }

    @Override
    protected String doInBackground(Void... voids) {
        // Execute HTTP Post Request
        try{
            response = httpclient.execute(httppost);
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                String line = "0";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    System.out.println(line);
                }
                reader.close();
                return(sb.toString());
            } catch (Exception e) {
                return("ERROR");
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            return "ERROR";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
        result = result.replace("\n","");
        if (result != null) {
           results = result;
            listener.recievePostResults(results,channel);
        } else {
            System.out.println("ERROR: ");
        }
    }
}
