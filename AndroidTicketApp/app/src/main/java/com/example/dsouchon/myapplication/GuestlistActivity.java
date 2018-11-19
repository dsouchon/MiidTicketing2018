package com.example.dsouchon.myapplication;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GuestlistActivity extends AppCompatActivity {



    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(GuestlistActivity.this,"Select Home to go back.", Toast.LENGTH_SHORT).show();


    }

    String[] item;
    ArrayList<String> listItem;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestlist);
        Button buttonEventSetup = (Button)findViewById(R.id.buttonEventSetup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);





        if(Local.isSet(getApplicationContext(), "EventName"))
        {
            String event =  Local.Get(getApplicationContext(), "EventName");
            TextView textEvent = (TextView) findViewById(R.id.textEventName);
            textEvent.setText( "Tickets purchased for " + event);

            //Call ticketholder list here into string variable
            MySOAPCallActivity cs = new MySOAPCallActivity();
            String username = Local.Get(getApplicationContext(), "UserName");
            String password = Local.Get(getApplicationContext(), "Password");
            String eventName = Local.Get(getApplicationContext(), "EventName");


            // Calling Application class (see application tag in AndroidManifest.xml)
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setEventName(eventName);


            GetTicketHolderForEventParams params = new GetTicketHolderForEventParams(cs,username , password, eventName);

            new CallSoapGetTicketHoldersForEvent().execute(params);



        }




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.homebutton){
            startActivity(new Intent(this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }





    public void Login(View view) {
        final AlertDialog ad=new AlertDialog.Builder(this).create();
        MySOAPCallActivity cs = new MySOAPCallActivity();
        try {
            EditText userName = (EditText) findViewById(R.id.editUserName);
            EditText password = (EditText) findViewById(R.id.editPassword);
            String user = userName.getText().toString();
            String pwd = password.getText().toString();
            LoginParams params = new LoginParams(cs, user, pwd);

            Local.Set(getApplicationContext(), "UserName", user);
            Local.Set(getApplicationContext(), "Password", pwd);

            new CallSoapLogin().execute(params);
           // new CallSoapGetCurrentEvents().execute(params);

        } catch (Exception ex) {
            ad.setTitle("Error!");
            ad.setMessage(ex.toString());
        }
        ad.show();
    }



    public class CallSoapGetTicketHoldersForEvent extends AsyncTask<GetTicketHolderForEventParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(GetTicketHolderForEventParams... params) {
            return params[0].foo.GetTicketHoldersForEvent(params[0].eventname,params[0].username,params[0].password);
        }

        protected void onPostExecute(String result) {

            // Calling Application class (see application tag in AndroidManifest.xml)
            Local.Set(getApplicationContext(), "TicketHolders", result);

             //sets list view for ticket holder information
            String[] RowData = result.toString().split(";");

            List<String> TicketholdersArray = new ArrayList<>();
            for(int x=0;x<RowData.length;x++)
            {
                TicketholdersArray.add(RowData[x].toString());

            }

            ListView listView1 = (ListView) findViewById(R.id.your_list_view_id);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(GuestlistActivity.this, R.layout.liststyle, TicketholdersArray);
            listView1.setAdapter(adapter);



        }

    }

    private static class GetTicketHolderForEventParams {
        MySOAPCallActivity foo;

        String eventname;
        String username;
        String password;


        GetTicketHolderForEventParams (MySOAPCallActivity foo, String eventname, String username, String password) {
            this.foo = foo;

            this.eventname = eventname;
            this.username =  username;
            this.password = password;

        }
    }




    public class CallSoapLogin extends AsyncTask<LoginParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(LoginParams... params) {
            return params[0].foo.Login(params[0].username, params[0].password);
        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            try {

            TextView loginResult =(TextView)findViewById(R.id.labelLoginResult);
            loginResult.setVisibility(View.VISIBLE);
            loginResult.setText(result);

           // Button buttonUnsetEvent = (Button)findViewById(R.id.buttonUnsetEvent);
           // buttonUnsetEvent.setEnabled(true);

            //Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
            //spinner2.setEnabled(true);

            boolean LoginSuccessful = false;



            if(result.toLowerCase().contains("success"))
            {
                LoginSuccessful = true;
            }



            if (LoginSuccessful)
            {
                String user = Local.Get(getApplicationContext(), "UserName");
                Local.Set(getApplicationContext(), "LoggedIn", user);
                LinearLayout layoutLoggedIn = (LinearLayout)findViewById(R.id.layoutLoggedIn);
                layoutLoggedIn.setVisibility(View.VISIBLE);



                LinearLayout layoutLogIn = (LinearLayout)findViewById(R.id.layoutLogIn);
                layoutLogIn.setVisibility(View.VISIBLE);




            }



            } catch (Exception ex) {
               String e3 = ex.toString();
            }

        }



    }
    private static class LoginParams {
        MySOAPCallActivity foo;
        String username;
        String password;


        LoginParams(MySOAPCallActivity foo, String username, String password) {
            this.foo = foo;
            this.username = username;
            this.password = password;

        }
    }



}
