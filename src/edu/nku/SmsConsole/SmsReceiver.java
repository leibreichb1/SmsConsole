package edu.nku.SmsConsole;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver{

	@Override
    public void onReceive(Context context, Intent intent) 
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String from = "";
        String message = "";
        String line = "";
        String response = "";
        
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                from = msgs[i].getOriginatingAddress();
                message = msgs[i].getMessageBody();
            
	            if( message.startsWith("COMMAND:")){
	            	String[] cmd = message.split( ":" );
	            	try {
						Process runCommand = Runtime.getRuntime().exec( cmd[1] );
						DataInputStream dIn = new DataInputStream( runCommand.getInputStream() );
						
						runCommand.waitFor();
						//make sure process wasn't terminated
						if( runCommand.exitValue() != 255 ){
							try {
								//read all lines from stream
								while( (line = dIn.readLine()) != null && !line.equals("") ){
									response += line + "\n";
								}
								SmsManager sms = SmsManager.getDefault();
								try{
									ArrayList<String> list = sms.divideMessage(response);
									sms.sendMultipartTextMessage(from, null, list, null, null );
								} catch(NullPointerException e1){
									e1.printStackTrace();
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						SharedPreferences myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
						boolean report = myPrefs.getBoolean(SmsConsoleActivity.REPORT_ON, false );
						if( report ){
							NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
							Notification notification = new Notification(R.drawable.notification_face, "Notify", System.currentTimeMillis());
							notification.flags |= Notification.FLAG_AUTO_CANCEL;
							notification.defaults |= Notification.DEFAULT_ALL;
							Intent notifIntent = new Intent( context, SmsReport.class );
							notifIntent.putExtra( SmsConsoleActivity.COMMAND, cmd[1] );
							notifIntent.putExtra( SmsConsoleActivity.RESPONSE, response );
							PendingIntent pendIntent = PendingIntent.getActivity( context, 0, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);
							notification.setLatestEventInfo(context, "New Command was run", "SMSConsole", pendIntent );
							mManager.notify(0, notification);  
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	abortBroadcast();
	            }
            }
        }                         
    }

}
