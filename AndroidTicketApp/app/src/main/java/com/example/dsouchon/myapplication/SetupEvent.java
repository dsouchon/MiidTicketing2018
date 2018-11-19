package com.example.dsouchon.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SetupEvent extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(SetupEvent.this,"Please Logout before exiting", Toast.LENGTH_SHORT).show();




    }





    private Spinner spinner2;

    public void UnsetEvent(View view) {

        Local.Set(getApplicationContext(), "EventName", "0");
        Local.Set(getApplicationContext(), "EventImage", "0");


        Intent intent = new Intent(SetupEvent.this, MainActivity.class );
        startActivity(intent);
    }



    public void MainMenu(View view) {



        Intent intent = new Intent(SetupEvent.this, MainActivity.class );
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_event);
        final AlertDialog ad=new AlertDialog.Builder(this).create();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        String user = Local.Get(getApplicationContext(), "UserName");
        String pwd = Local.Get(getApplicationContext(), "Password");
        MySOAPCallActivity cs = new MySOAPCallActivity();
        LoginParams params = new LoginParams(cs, user, pwd);
        new CallSoapGetCurrentEvents().execute(params);


       // Button buttonUnsetEvent = (Button)findViewById(R.id.buttonUnsetEvent);
        Button buttonMainMenu = (Button)findViewById(R.id.buttonMainMenu);
    //    buttonUnsetEvent.setEnabled(false);

        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner2.setEnabled(true);

        Spinner spinner = (Spinner)findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                MySOAPCallActivity cs = new MySOAPCallActivity();

                String eventName = ((Spinner) findViewById(R.id.spinner2)).getSelectedItem().toString();

                Local.Set(getApplicationContext(), "EventName", eventName);

                // Calling Application class (see application tag in AndroidManifest.xml)
                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                globalVariable.setEventName(eventName);

                EventImageParams params = new EventImageParams(cs, eventName);

                new CallSoapGetEventImage().execute(params);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup_event, menu);
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

        if (id == R.id.Logoffmenu){
            startActivity(new Intent(this,MainLogin.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public class CallSoapGetEventImage extends AsyncTask<EventImageParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(EventImageParams... params) {
            return params[0].foo.GetEventImage(params[0].eventName);
        }

        protected void onPostExecute(String result) {


            ImageView imageViewEventImage = (ImageView)findViewById(R.id.imageViewEventImage);
            byte[] decodedString = Base64.decode(result, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewEventImage.setImageBitmap(decodedByte);

            // Calling Application class (see application tag in AndroidManifest.xml)
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.setEventImage(result);
            Local.Set(getApplicationContext(), "EventImage", result);





        }





    }

    private static class EventImageParams {
        MySOAPCallActivity foo;
        String eventName;



        EventImageParams (MySOAPCallActivity foo, String eventName) {
            this.foo = foo;

            this.eventName = eventName;

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
    public class CallSoapGetCurrentEvents extends AsyncTask<LoginParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(LoginParams... params) {
            return params[0].foo.GetCurrentEvents(params[0].username, params[0].password);
        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed

            //TextView currentEventsResult =(TextView)findViewById(R.id.labelGetCurrentEventsResult);
            //currentEventsResult.setText(result);


            String[] RowData = result.toString().split("\\|");
            List<String> spinnerArray = new ArrayList<String>();
            for(int x=0;x<RowData.length;x++)
            {
                spinnerArray.add(RowData[x].toString());
            }

            spinner2 = (Spinner) findViewById(R.id.spinner2);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetupEvent.this, R.layout.spinnerstyle, spinnerArray);

            adapter.setDropDownViewResource(R.layout.spinnerstyledrop);
            spinner2.setAdapter(adapter);



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

            TextView loginResult =(TextView)findViewById(R.id.labelLoginResult);
            loginResult.setText(result);

         //   Button buttonUnsetEvent = (Button)findViewById(R.id.buttonUnsetEvent);
        //    buttonUnsetEvent.setEnabled(true);

            Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
            spinner2.setEnabled(true);



        }



    }
}
