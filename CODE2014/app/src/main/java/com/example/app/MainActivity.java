package com.example.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements postListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    public void closeBlack(View v){
        EditText pass = (EditText)findViewById(R.id.password);
        EditText user = (EditText)findViewById(R.id.username);
        Button login = (Button)findViewById(R.id.login);
        RelativeLayout register = (RelativeLayout)findViewById(R.id.registerPage);
        register.setVisibility(View.INVISIBLE);
        login.setClickable(true);
        user.setEnabled(true);
        pass.setEnabled(true);
    }

    public void signUp(View v){
        EditText pass = (EditText)findViewById(R.id.newPass);
        EditText user = (EditText)findViewById(R.id.newUser);

        PostRequester pr = new PostRequester(this,0);
        pr.newRequest("http://www.isaidso.ca/code/registerNewUser.php",2);
        System.out.println("Username on Signup: " + user.getText().toString());
        pr.addParam("user",user.getText().toString());
        pr.addParam("pass",pass.getText().toString());
        pr.sendRequest();
    }

    @Override
    public void recievePostResults(String result, int channel) {
        if(result.equals("success")){
            EditText pass = (EditText)findViewById(R.id.password);
            EditText user = (EditText)findViewById(R.id.username);
            Button login = (Button)findViewById(R.id.login);
            RelativeLayout register = (RelativeLayout)findViewById(R.id.registerPage);
            register.setVisibility(View.INVISIBLE);
            login.setClickable(true);
            user.setEnabled(true);
            pass.setEnabled(true);

            CharSequence text = "Account created succesfully! Please log in.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }else{
            CharSequence text = "Something went wrong. "+result;
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        }
    }

    public void register(View v){
        RelativeLayout register = (RelativeLayout)findViewById(R.id.registerPage);
        register.setVisibility(View.VISIBLE);

        lockLogin();
    }

    // loginAttempt
    // Calls an external PHP script which attempts to login, it's output will either be success or
    // fail depending on if the passed credentials were valid
    public void attemptLogin(View v){
        new SubmitLogin().execute();
    }

    public void aboutPage(View v){
        ScrollView aboutPage = (ScrollView)findViewById(R.id.scrollView);
        aboutPage.setVisibility(View.VISIBLE);

        lockLogin();

    }

    public void showClaimScreen(View v){
        RelativeLayout claimPage = (RelativeLayout)findViewById(R.id.screen);
        //if peak is unclaimed
        RelativeLayout unclaimed = (RelativeLayout)findViewById(R.id.unclaimedPeak);
        unclaimed.setVisibility(View.VISIBLE);
        //if peak is claimed
        unclaimed.setVisibility(View.INVISIBLE);
        claimPage.setVisibility(View.VISIBLE);

    }

    public void returnToLogin(View v){
        ScrollView aboutPage = (ScrollView)findViewById(R.id.scrollView);
        aboutPage.setVisibility(View.INVISIBLE);

        resetLogin();
    }

    public void lockLogin(){

        EditText pass = (EditText)findViewById(R.id.password);
        EditText user = (EditText)findViewById(R.id.username);
        Button login = (Button)findViewById(R.id.login);
        Button about = (Button)findViewById(R.id.aboutButton);

        login.setClickable(false);
        user.setEnabled(false);
        pass.setEnabled(false);
        about.setEnabled(false);
    }

    public void resetLogin(){

        EditText pass = (EditText)findViewById(R.id.password);
        EditText user = (EditText)findViewById(R.id.username);
        Button login = (Button)findViewById(R.id.login);
        Button about = (Button)findViewById(R.id.aboutButton);

        login.setClickable(true);
        user.setEnabled(true);
        pass.setEnabled(true);
        about.setEnabled(true);
    }

    private class SubmitLogin extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... voids) {
            try {
                EditText user = (EditText)findViewById(R.id.username);
                EditText pass = (EditText)findViewById(R.id.password);

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.isaidso.ca/code/loginAttempt.php");

                // Add your data
                List<NameValuePair> params = new ArrayList<NameValuePair>(2);
                params.add(new BasicNameValuePair("user", user.getText().toString()));
                params.add(new BasicNameValuePair("pass", pass.getText().toString()));
                httppost.setEntity(new UrlEncodedFormEntity(params));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                sb.append(reader.readLine() + "\n");
                String line = "0";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
                String result11 = sb.toString();

                // parsing data
                return result11;
            } catch (Exception e) {
                System.out.println("ERROR " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            result = result.replace("\n","");
            if (result != null) {
                System.out.println(result + " - " + (result.toString().equals("success")));
                if(result.startsWith("success")){
                    Intent i = new Intent(getApplicationContext(), MainScreen.class);
                    String[] resultAr = result.split(",");
                    i.putExtra("USER_ID", resultAr[1]);
                    startActivity(i);
                }else{
                    CharSequence text = "Could not validate user " + result;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            } else {
                System.out.println("ERROR: ");
            }
        }
    }
}