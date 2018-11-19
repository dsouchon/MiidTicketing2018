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

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity21 extends AppCompatActivity {





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
        setContentView(R.layout.activity_main21);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //hides progress bar

        ProgressBar mprogressbar;
        mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
        mprogressbar.setVisibility(View.INVISIBLE);

        Button scanbutton = (Button)findViewById(R.id.buttonScanTag);
        scanbutton.setVisibility(View.INVISIBLE);

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter == null) {
            Intent intent = new Intent(MainActivity21.this, MainActivity.class );
            finish();
            startActivity(intent);
            Toast.makeText(MainActivity21.this,"Your device does not support this feature", Toast.LENGTH_LONG).show();

        }


        //Make yes no buttons invisible

        Button buttonNo = (Button)findViewById(R.id.buttonNo);

        buttonNo.setVisibility(View.GONE);



        if(Local.isSet(getApplicationContext(), "EventName")) {
            TextView textEvent = (TextView) findViewById(R.id.textEventName);
            textEvent.setText(Local.Get(getApplicationContext(), "EventName"));
        }

        final  AlertDialog ad=new AlertDialog.Builder(this).create();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        // Get name and email from global/application context
        final String eventImageString  = globalVariable.getEventImage();

        if(eventImageString != null && eventImageString.length() > 0) {
            ImageView imageViewEventImage = (ImageView) findViewById(R.id.imageViewEventImage);
            byte[] decodedString = Base64.decode(eventImageString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewEventImage.setImageBitmap(decodedByte);
        }
        else {
            Intent intent = new Intent(MainActivity21.this, SetupEvent.class );
            startActivity(intent);

        }



        Button btnScanTag = (Button)findViewById(R.id.buttonScanTag);
        btnScanTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MySOAPCallActivity cs = new MySOAPCallActivity();
                try{
                    EditText editTagNumber = (EditText)findViewById(R.id.editTagNumber);

                    String tagNo = editTagNumber.getText().toString();

                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                    // Get name and email from global/application context
                    final String eventName  = globalVariable.getEventName();

                    if(eventName.length() > 0) {



                        TagParams params = new TagParams(cs, tagNo, eventName);

                        new CallSoapTicketValidForEvent().execute(params);
                    }

                }
                catch(Exception ex) {
                    ad.setTitle("Error!");
                    ad.setMessage(ex.toString());
                }
                ad.show();

            }
        });




        Button buttonYes = (Button)findViewById(R.id.buttonYes);
        buttonYes.setVisibility(View.INVISIBLE);
        buttonYes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MySOAPCallActivity cs = new MySOAPCallActivity();
                try{

                    //Make yes no buttons invisible
                    Button buttonYes = (Button)findViewById(R.id.buttonYes);
                    Button buttonNo = (Button)findViewById(R.id.buttonNo);
                    buttonYes.setVisibility(View.INVISIBLE);
                    buttonNo.setVisibility(View.INVISIBLE);
                    Button scanbutton = (Button)findViewById(R.id.buttonScanTag);


                    EditText editTagNumber = (EditText)findViewById(R.id.editTagNumber);

                    String tagNo = editTagNumber.getText().toString();

                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                    // Get name and email from global/application context
                    final String eventName  = globalVariable.getEventName();

                    if(eventName.length() > 0) {


                        //TextView hidden = (TextView) findViewById(R.id.labelScanResult);

                        //String Reason = hidden.getText().toString();

                        String Reason = "Allow Access";

                        AllowDenyParams params = new AllowDenyParams(cs, tagNo, Reason, false, eventName);

                        new CallSoapAllowDenyAccess().execute(params);

                        //this shows button scan next tag button once user has verfied ticket

                        scanbutton.setVisibility(View.VISIBLE);


                    }
                }
                catch(Exception ex) {
                    //     ad.setTitle("Error!");
                    //        ad.setMessage(ex.toString());
                }
                //      ad.show();

            }
        });






    }

    public void ButtonNo_Click(View view)
    {

        TextView labelScanResult = (TextView)findViewById(R.id.labelScanResult);
        TextView nameSurname = (TextView)findViewById(R.id.nameSurname);
        TextView idNumber = (TextView)findViewById(R.id.idNumber);
        TextView ticketClass = (TextView)findViewById(R.id.ticketClass);
        EditText editTagNumber = (EditText)findViewById(R.id.editTagNumber);

        labelScanResult.setText("");
        nameSurname.setText("");
        idNumber.setText("");
        ticketClass.setText("");
        editTagNumber.setText("");

        //Make yes no buttons invisible
        Button buttonYes = (Button)findViewById(R.id.buttonYes);
        Button buttonNo = (Button)findViewById(R.id.buttonNo);
        buttonYes.setVisibility(View.INVISIBLE);
        buttonNo.setVisibility(View.INVISIBLE);

    }


    //Logic business after the web service complete here
//Do the thing that modify the UI in a function like this
    private void onTaskCompleted(Object _response)
    {


    }

    public void openSetupNow(View view) {
        Intent in = new Intent(MainActivity21.this, MainActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Toast.makeText(MainActivity21.this,"Click Home Button to exit", Toast.LENGTH_SHORT).show();




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

    private static class TagParams {
        MySOAPCallActivity foo;
        String tagNumber;
        String eventName;



        TagParams(MySOAPCallActivity foo, String tagNumber, String eventName) {
            this.foo = foo;
            this.tagNumber = tagNumber;
            this.eventName = eventName;

        }
    }

    private static class AllowDenyParams {
        MySOAPCallActivity foo;
        String TagNumber;
        String ReasonForBlocking;
        boolean Block;
        String EventName;


        AllowDenyParams(MySOAPCallActivity foo, String TagNumber, String ReasonForBlocking, boolean Block, String EventName) {
            this.foo = foo;
            this.TagNumber = TagNumber;
            this.ReasonForBlocking = ReasonForBlocking;
            this.Block = Block;
            this.EventName = EventName;

        }
    }







    public class CallSoapTicketValidForEvent extends AsyncTask<TagParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(TagParams... params) {
            return params[0].foo.TicketValidForEvent(params[0].tagNumber, params[0].eventName);
        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            try {
                //TextView currentEventsResult =(TextView)findViewById(R.id.labelGetCurrentEventsResult);
                //currentEventsResult.setText(result);


                TextView labelScanResult = (TextView) findViewById(R.id.labelScanResult);
                TextView idNumber = (TextView) findViewById(R.id.idNumber);

                //sets tick button to invisible, tick will show whe idNumber > 1




                Button buttonYes = (Button)findViewById(R.id.buttonYes);
                buttonYes.setVisibility(View.INVISIBLE);




                if (result.toLowerCase().contains("no valid ticket") || result.toLowerCase().contains("not found") || result.toLowerCase().contains("no active"))//"no valid ticket"
                {
                    labelScanResult.setText(result);
                    //hides progress bar when information is complete
                    ProgressBar mprogressbar;
                    mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
                    mprogressbar.setVisibility(View.INVISIBLE);

                    //changes bar color after scan
                    TextView rl = (TextView)findViewById(R.id.labelScanResult);
                    rl.setBackgroundColor(Color.parseColor("#be1c1d"));
                    rl.setTextColor(Color.parseColor("#ffffff"));



                } else {
                    labelScanResult.setText("Success!" + " Allow entry?");

                    //TableLayout tableLayout = (TableLayout) findViewById(R.id.tab);
                    //tableLayout.removeAllViews();

                    //changes bar color after scan
                    TextView rl = (TextView)findViewById(R.id.labelScanResult);
                    rl.setBackgroundColor(Color.parseColor("#0c9d16"));
                    rl.setTextColor(Color.parseColor("#ffffff"));


                    CircleImageView placeholder = (CircleImageView) findViewById(R.id.imageViewProfilePic);
                    placeholder.setBackgroundResource(0);


                    String[] rows = result.toString().split("\\n?\\n");
                    String profilePic = rows[3];

                    if (profilePic.length() > 0) {
                        ImageView imageViewProfilePic = (ImageView) findViewById(R.id.imageViewProfilePic);
                        byte[] decodedString = Base64.decode(profilePic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageViewProfilePic.setImageBitmap(decodedByte);


                        //hides progress bar when information is complete
                        ProgressBar mprogressbar;
                        mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
                        mprogressbar.setVisibility(View.INVISIBLE);
                    }


                    TextView nameSurname = (TextView) findViewById(R.id.nameSurname);
                    nameSurname.setText(rows[0].toUpperCase());
                    idNumber.setText(rows[1].toUpperCase());
                    TextView ticketClass = (TextView) findViewById(R.id.ticketClass);
                    ticketClass.setText(rows[2].toUpperCase());


                    //shows tick icon when id number value is greater then 1
                    if (idNumber.length() > 1) {

                        buttonYes.setVisibility(View.VISIBLE);








                    }








                }
            }
            catch (Exception ex)
            {
                String error = ex.getMessage();

            }
        }





    }


    public class CallSoapAllowDenyAccess extends AsyncTask<AllowDenyParams, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(AllowDenyParams... params) {
            return params[0].foo.AllowDenyAccess(params[0].TagNumber, params[0].ReasonForBlocking, params[0].Block, params[0].EventName);
        }

        protected void onPostExecute(String result) {

            //TextView labelFinalConfirmation = (TextView)findViewById(R.id.labelFinalConfirmation);
            //labelFinalConfirmation.setText(result);

            TextView labelScanResult = (TextView)findViewById(R.id.labelScanResult);
            labelScanResult.setText(result);



            //TableLayout tableLayout = (TableLayout) findViewById(R.id.tab);
            //tableLayout.removeAllViews();

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
                EditText editTagNumber = (EditText)findViewById(R.id.editTagNumber);
                editTagNumber.setText(tagNo);

                //shows progress bar when tag is scanned

                ProgressBar mprogressbar;
                mprogressbar = (ProgressBar) findViewById(R.id.progressbar);
                mprogressbar.setVisibility(View.VISIBLE);



                MySOAPCallActivity cs = new MySOAPCallActivity();
                try{


                    //tagNo = editTagNumber.getText().toString();

                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

                    // Get name and email from global/application context
                    String eventName  = globalVariable.getEventName();
                    if(Local.isSet(getApplicationContext(), "EventName"))
                    {
                        eventName = Local.Get(getApplicationContext(), "EventName");

                    }
                    if(eventName.length() > 0) {

                        TagParams params = new TagParams(cs, tagNo, eventName);

                        //Make yes no buttons visible

                        Button buttonNo = (Button)findViewById(R.id.buttonNo);
                        buttonNo.setVisibility(View.VISIBLE);

                        TextView nameSurname = (TextView) findViewById(R.id.nameSurname);
                        nameSurname.setText("");
                        TextView idNumber = (TextView) findViewById(R.id.idNumber);
                        idNumber.setText("");
                        TextView ticketClass = (TextView) findViewById(R.id.ticketClass);
                        ticketClass.setText("");



                        new CallSoapTicketValidForEvent().execute(params);
                    }

                }
                catch(Exception ex) {
                    //         ad.setTitle("Error!");
                    ad.setMessage(ex.toString());
                }
                //      ad.show();



            }else{
                Toast.makeText(this, "No Tag Data Found!", Toast.LENGTH_SHORT).show();

            }


//this resests actiity so  tag info is deleted if person is not allowed entry
            Button nobutton = (Button)findViewById(R.id.buttonNo);



            nobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent i = new Intent(getApplicationContext(),MainActivity21.class);
                    startActivity(i);




                }
            });

            Button scanbutton = (Button)findViewById(R.id.buttonScanTag);



            scanbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent i = new Intent(getApplicationContext(),MainActivity21.class);
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
        Intent intent = new Intent(this, MainActivity21.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
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

