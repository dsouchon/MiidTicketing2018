package com.example.dsouchon.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;



public class UserInfo extends AppCompatActivity {


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




    NfcAdapter nfcAdapter;





    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //hides progress bar

        ProgressBar mprogressbar;
        mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
        mprogressbar.setVisibility(View.GONE);

        final  AlertDialog ad=new AlertDialog.Builder(this).create();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter == null) {
            Intent intent = new Intent(UserInfo.this, MainActivity.class );
            finish();
            startActivity(intent);
            Toast.makeText(UserInfo.this,"Your device does not support this feature", Toast.LENGTH_LONG).show();

        }


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();


        Button btnScanTag = (Button)findViewById(R.id.buttonScanTag);
        btnScanTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MySOAPCallActivity cs = new MySOAPCallActivity();
                try{
                    TextView editTagNumber = (TextView) findViewById(R.id.editTagNumber);

                    String tagNo = editTagNumber.getText().toString();

                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                    // Get name and email from global/application context



                    TagParams params = new TagParams(cs, tagNo);

                    new CallSoapTicketValidForEvent().execute(params);


                }
                catch(Exception ex) {
                  //  ad.setTitle("Error!");
                  //  ad.setMessage(ex.toString());
                }
              //  ad.show();

            }
        });


    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(UserInfo.this,"Click Home Button to exit", Toast.LENGTH_SHORT).show();


    }


    private static class TagParams {
        MySOAPCallActivity foo;
        String TagNumber;




        TagParams(MySOAPCallActivity foo, String TagNumber) {
            this.foo = foo;
            this.TagNumber = TagNumber;


        }
    }



    public class CallSoapTicketValidForEvent extends AsyncTask<TagParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(TagParams... params) {
            return params[0].foo.CallUser(params[0].TagNumber);
        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            try {
                //TextView currentEventsResult =(TextView)findViewById(R.id.labelGetCurrentEventsResult);
                //currentEventsResult.setText(result);

                String[] rows = result.toString().split("\\n?\\n");

                TextView nameSurname = (TextView) findViewById(R.id.nameSurname);
                nameSurname.setText(rows[0].toUpperCase());

                if(nameSurname.length() > 0) {
                    ProgressBar mprogressbar;
                    mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
                    mprogressbar.setVisibility(View.GONE);


                }


            }
            catch (Exception ex)
            {
                //  String error = ex.getMessage();

            }
        }





    }




//NFC Stuff Start - COMMENTING OUT FOR DEBUGGING

    @Override
    protected void onNewIntent( Intent intent) {
        super.onNewIntent(intent);
        final  AlertDialog ad=new AlertDialog.Builder(this).create();




        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {

            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            String tagNo = "";

            if(parcelables != null && parcelables.length > 0){

                tagNo = readTextFromMessage((NdefMessage) parcelables[0]);
                TextView editTagNumber = (TextView) findViewById(R.id.editTagNumber);
                editTagNumber.setText(tagNo);

                //shows progress bar when tag is scanned
                ProgressBar mprogressbar;
                mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
                mprogressbar.setVisibility(View.VISIBLE);


                MySOAPCallActivity cs = new MySOAPCallActivity();
                //tagNo = editTagNumber.getText().toString();

                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                // Get name and email from global/application context
                String eventName  = globalVariable.getEventName();
                TagParams params = new TagParams(cs, tagNo);
                TextView nameSurname = (TextView) findViewById(R.id.nameSurname);
                nameSurname.setText("");





                new CallSoapTicketValidForEvent().execute(params);


            }else{
                Toast.makeText(this, "No Tag Data Found!", Toast.LENGTH_SHORT).show();

            }


            //this resests actiity
            Button scanbutton = (Button)findViewById(R.id.buttonScanTag);
            scanbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(),UserInfo.class);
                    startActivity(i);

                }
            });










        }
    }

    private String readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            return tagContent;
            //txtTagContent.setText(tagContent);






        } else
        {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    public String getTextFromNdefRecord(NdefRecord ndefRecord){
        String tagContent = null;
        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128 ) == 0) ? "UTF-8":"UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,payload.length - languageSize -1, textEncoding);

        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;

    }




    @Override
    protected void onResume(){

        super.onResume();

        enableForegroundDispatchSystem();

    }
    @Override
    protected void onPause(){

        super.onPause();
        disableForegroundDispatchSystem();

    }

    private void enableForegroundDispatchSystem(){
        Intent intent = new Intent(this, UserInfo.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem(){
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage)
    {
        try{
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if(ndefFormatable == null)
            {

                Toast.makeText(this, "Tag is not ndef formattable!", Toast.LENGTH_SHORT).show();


            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();

        }
        catch(Exception e){
            Log.e("formatTag", e.getMessage());
        }

    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage)
    {
        try{


            if(tag == null)
            {

                Toast.makeText(this, "Tag object cannot be null!", Toast.LENGTH_SHORT).show();

                return;
            }
            Ndef ndef = Ndef.get(tag);
            if (ndef == null)
            {
//format tag with the ndef format and write the message
                formatTag(tag, ndefMessage);


            }
            else
            {
                ndef.connect();
                if(!ndef.isWritable())
                {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;


                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();

            }
        }
        catch(Exception e){
            Log.e("writeNdefMessage", e.getMessage());
        }

    }


    private NdefRecord createTextRecord(String content)
    {
        try{
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("QTF-8");
            final byte[] text = content.getBytes("QTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte)(languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0 , textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());





        }
        catch(Exception e){

            Log.e("createTextRecord", e.getMessage());

        }
        return  null;
    }

    private NdefMessage createNdefMessage(String content)
    {
        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ ndefRecord});

        return ndefMessage;
    }

//NFC Stuff End



}

