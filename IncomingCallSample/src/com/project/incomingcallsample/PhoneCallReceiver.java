/*
 * This class extends BroadCastReceiver 
 * its onReceive method will get called PHONE STATE will change
 * 
 * @Author Aditi Kulkarni
 */
package com.project.incomingcallsample;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
//http://stackoverflow.com/questions/15563921/detecting-an-incoming-call-coming-to-an-android-device
public class PhoneCallReceiver extends BroadcastReceiver{

	private int lastCallState = TelephonyManager.CALL_STATE_IDLE;
	private boolean isIncoming = false;
	private static String contactNum; 
	Intent audioRecorderService;
	/*
	 * (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 * @param android.content.Context
	 * @param android.content.Intent
	 * @return void
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
		{
			contactNum = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
		}
		else
		{
			String state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
			String phoneNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			
			int stateChange = 0;
			
			if(state.equals(TelephonyManager.EXTRA_STATE_IDLE))
			{
				stateChange = TelephonyManager.CALL_STATE_IDLE;
				if(isIncoming)
				{
					onIncomingCallEnded(context, phoneNumber);
				}
				else
				{
					onOutgoingCallEnded(context, phoneNumber);
				}
			}
			else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
			{
				stateChange = TelephonyManager.CALL_STATE_OFFHOOK;
				if(lastCallState != TelephonyManager.CALL_STATE_RINGING)
				{
					isIncoming = false;
					onOutgoingCallStarted(context, phoneNumber);
				}
				else
				{
					isIncoming = true;
					onIncomingCallAnswered(context, phoneNumber);
				}
			}
			else if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
			{
				stateChange = TelephonyManager.CALL_STATE_RINGING;
				lastCallState = stateChange;
				onIncomingCallReceived(context, contactNum);
			}
			
			//onCallStateChanged(context,stateChange,phoneNumber);
		}
		
	}
	
	private void onCallStateChanged(Context context, int stateChange, String phoneNumber) {
		//If there  is no change in state just return nothing to do here 
		if(lastCallState == stateChange)
		{
			return;
		}
		Log.d("State chnages",String.valueOf(stateChange));
		//This will check chnaged state and call respective method to perform desired action
		switch(stateChange)
		{
		
			case TelephonyManager.CALL_STATE_RINGING:
				isIncoming = true;
				contactNum = phoneNumber;
				lastCallState = stateChange;
				onIncomingCallReceived(context, contactNum);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				
				if(lastCallState != TelephonyManager.CALL_STATE_RINGING)
				{
					isIncoming = false;
					onOutgoingCallStarted(context, phoneNumber);
				}
				else
				{
					isIncoming = true;
					onIncomingCallAnswered(context, phoneNumber);
				}
				lastCallState = stateChange;
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if(isIncoming)
				{
					onIncomingCallEnded(context, phoneNumber);
				}
				else
				{
					onOutgoingCallEnded(context, phoneNumber);
				}
				lastCallState = stateChange;
				break;
			default:
				Toast.makeText(context, "Unknown State", Toast.LENGTH_LONG).show();
		}
		
	}

	
	protected void onIncomingCallStarted(Context ctx, String number)
	{
		Toast.makeText(ctx, "Incoming call is started", Toast.LENGTH_LONG).show();
		ctx.startService(new Intent(ctx,AudioRecorderSerivce.class));
		Log.d("PhoneCallReceiver:onIncomingCallStarted","Incoming call is started");
	}
	protected void onOutgoingCallStarted(Context ctx, String number)
	{
		Toast.makeText(ctx, "Outgoing call is started", Toast.LENGTH_LONG).show();
		ctx.startService(new Intent(ctx,AudioRecorderSerivce.class));
		Log.d("PhoneCallReceiver:onIncomingCallStarted","Outgoing call is started");
	}
	protected void onIncomingCallEnded(Context ctx, String number)
	{
		Toast.makeText(ctx, "Incoming call is ended", Toast.LENGTH_LONG).show();
		ctx.stopService(new Intent(ctx,AudioRecorderSerivce.class));
		Log.d("PhoneCallReceiver:onIncomingCallStarted","Incoming call is ended");
	}
	protected void onOutgoingCallEnded(Context ctx, String number)
	{
		Toast.makeText(ctx, "Outgoing call is ended", Toast.LENGTH_LONG).show();
		ctx.stopService(new Intent(ctx,AudioRecorderSerivce.class));
		Log.d("PhoneCallReceiver:onIncomingCallStarted","Outgoing call is ended");
	}
	protected void onIncomingCallReceived(Context ctx,String number)
	{
		Toast.makeText(ctx, "Incoming call is received", Toast.LENGTH_LONG).show();
		Log.d("PhoneCallReceiver:onIncomingCallStarted","Incoming call is received");
	}
	protected void onIncomingCallAnswered(Context ctx,String number)
	{
		Toast.makeText(ctx, "Incoming call is answered", Toast.LENGTH_LONG).show();
		Log.d("PhoneCallReceiver:onIncomingCallStarted","Incoming call is answered");
	}

}
