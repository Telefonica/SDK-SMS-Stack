package com.example.lucferbux.smstcpsdk;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smstcplibrary.SMSTCPController;
import com.example.smstcplibrary.SMSTCPLayer;

public class MainActivity extends AppCompatActivity {

    String message;
    TextView mSMSTextView;
    TextView mSMSTextDestinataryView;

    SMSTCPMessageApp smsListener;
    TextView mSMSDisplayID;
    TextView mSMSDisplayKey;

    TextView mSMSDisplaySyn;
    TextView mSMSDisplayAck;
    TextView mSMSDisplayPsh;
    TextView mSMSDisplayFin;
    TextView mSMSDisplaySbegin;
    TextView mSMSDisplayCipher;

    Switch mAckSWitch;


    TextView mSMSDisplayData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSMSTextView = (TextView) findViewById(R.id.tv_sms_content);
        mSMSTextDestinataryView = (TextView) findViewById(R.id.tv_sms_dest);
        mSMSDisplayID = (TextView) findViewById(R.id.tv_id_display);
        mSMSDisplayKey = (TextView) findViewById(R.id.tv_key_display);
        mSMSDisplayData = (TextView) findViewById(R.id.tv_data_app_display);
        mSMSDisplaySyn = (TextView) findViewById(R.id.tv_syn_display);
        mSMSDisplayAck = (TextView) findViewById(R.id.tv_ack_display);
        mSMSDisplayPsh = (TextView) findViewById(R.id.tv_psh_display);
        mSMSDisplayFin = (TextView) findViewById(R.id.tv_fin_display);
        mSMSDisplaySbegin = (TextView) findViewById(R.id.tv_sBegin_display);
        mSMSDisplayCipher = (TextView) findViewById(R.id.tv_cipher_display);
        mAckSWitch = findViewById((R.id.response_switch));


        String[] phoneNo = {mSMSTextDestinataryView.getText().toString()};

        smsListener = new SMSTCPMessageApp(this,0);

        smsListener.setListener(new SMSTCPMessageApp.ListenerApp() {
            @Override
            public void onTextProcessed(final String message) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mSMSDisplayData.setText(message);
                    }
                });

            }
            @Override
            public void onSMSRecived(SMSTCPLayer smstcpLayer) {
                mSMSDisplayID.setText(Integer.toString(smstcpLayer.id));
                mSMSDisplayKey.setText(Integer.toString(smstcpLayer.key));
                mSMSDisplaySyn.setText(Integer.toString(smstcpLayer.syn));
                mSMSDisplayAck.setText(Integer.toString(smstcpLayer.ack));
                mSMSDisplayPsh.setText(Integer.toString(smstcpLayer.psh));
                mSMSDisplayFin.setText(Integer.toString(smstcpLayer.fin));
                mSMSDisplaySbegin.setText(Integer.toString(smstcpLayer.sBegin));
                mSMSDisplayCipher.setText(Integer.toString(smstcpLayer.cipher));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });
    }

    private void sendSMS() {
        String[] phoneNo = {mSMSTextDestinataryView.getText().toString()};
        smsListener.sendNewMessage(mSMSTextView.getText().toString(), phoneNo, mAckSWitch.isChecked());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Request failed, please accept to make the app work", Toast.LENGTH_LONG).show();

                }
                return;
            }
        }

    }


}
