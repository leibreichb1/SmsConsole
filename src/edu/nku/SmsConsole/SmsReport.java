package edu.nku.SmsConsole;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SmsReport extends Activity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reporter);
        
        TextView cmdText = (TextView) findViewById( R.id.commField );
        TextView respText = (TextView) findViewById( R.id.resp );
        String cmd = getIntent().getStringExtra( SmsConsoleActivity.COMMAND );
        String resp = getIntent().getStringExtra( SmsConsoleActivity.RESPONSE );
        cmdText.setText( cmd );
        respText.setText( resp );
    }
}