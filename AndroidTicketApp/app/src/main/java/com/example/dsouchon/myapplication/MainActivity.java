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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {




    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(MainActivity.this,"Please Logout before exiting!", Toast.LENGTH_SHORT).show();




    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonEventSetup = (Button)findViewById(R.id.buttonEventSetup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);





        if(Local.isSet(getApplicationContext(), "LoggedIn"))
        {
            String loggedInUser = Local.Get(getApplicationContext(), "LoggedIn");
            if(loggedInUser.length()>0)
            {

                buttonEventSetup.setVisibility(View.VISIBLE);
            }
            else
            {

                buttonEventSetup.setVisibility(View.GONE);

            }
        }
        else
        {
            buttonEventSetup.setVisibility(View.GONE);

        }




        if(Local.isSet(getApplicationContext(), "EventName"))
        {
            String event =  Local.Get(getApplicationContext(), "EventName");
            TextView textEvent = (TextView) findViewById(R.id.textEventName);
            textEvent.setText( "Great! " + event + "is now active.");
            Button buttonAccessControl = (Button)findViewById(R.id.buttonAccessControl);
            buttonAccessControl.setEnabled(true);

            //HIDES SET EVENT BUTTON WHEN EVENT IS SET
            if (textEvent.length() > 1) {

                buttonEventSetup.setVisibility(View.INVISIBLE);


            }

            if (textEvent.length() > 1) {

                buttonAccessControl.setVisibility(View.VISIBLE);


            }






        }
        else
        {
            Button buttonAccessControl = (Button) findViewById(R.id.buttonAccessControl);
            buttonAccessControl.setEnabled(false);
        }

        if(Local.isSet(getApplicationContext(), "EventImage"))
        {
            TextView textEvent = (TextView) findViewById(R.id.textEventName);
            String result =  Local.Get(getApplicationContext(), "EventImage");

            ImageView imageViewEventImage = (ImageView)findViewById(R.id.imageViewEventImage);
            byte[] decodedString = Base64.decode(result, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewEventImage.setImageBitmap(decodedByte);

        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void setupEvent(View view) {
        Intent intent = new Intent(MainActivity.this, SetupEvent.class );
        finish();
        startActivity(intent);
    }

    public void Guestlist(View view) {
        Intent intent = new Intent(MainActivity.this, GuestlistActivity.class );
        finish();
        startActivity(intent);


    }

    public void buttoninfo(View view) {


        Intent intent = new Intent(MainActivity.this, UserInfo.class );
        finish();
        startActivity(intent);
    }



    public void ScanCode(View view) {


        Intent intent = new Intent(MainActivity.this, MainActivity2.class );
        finish();
        startActivity(intent);

    }

    public void accessControl(View view) {
        Button buttonEventSetup = (Button)findViewById(R.id.buttonEventSetup);
        buttonEventSetup.setVisibility(View.GONE);

        Intent intent = new Intent(MainActivity.this, MainActivity21.class );
        finish();
        startActivity(intent);
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

                Button buttonEventSetup = (Button)findViewById(R.id.buttonEventSetup);
                buttonEventSetup.setVisibility(View.VISIBLE);

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
