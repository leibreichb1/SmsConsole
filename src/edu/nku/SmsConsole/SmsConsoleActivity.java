package edu.nku.SmsConsole;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SmsConsoleActivity extends Activity {
	
	private SharedPreferences myPrefs;
	private Button btn;
    public static final String REPORT_ON = "reporter";
	public static final String COMMAND = "command";
	public static final String RESPONSE = "response";
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btn = (Button) findViewById(R.id.btn);
        myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean( REPORT_ON, false );
        prefsEditor.commit();
        btn.setText( "Turn reporting on" );
    }
	
	public void changePref( View v ){
		boolean report = myPrefs.getBoolean( REPORT_ON, false );
		if( report ){
			SharedPreferences.Editor prefsEditor = myPrefs.edit();
	        prefsEditor.putBoolean( REPORT_ON, false );
	        prefsEditor.commit();
	        btn.setText( "Turn reporting on" );
		}
		else{
			SharedPreferences.Editor prefsEditor = myPrefs.edit();
	        prefsEditor.putBoolean( REPORT_ON, true );
	        prefsEditor.commit();
	        btn.setText( "Turn reporting off" );
		}
	}
}