package raj.products.paidrides;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class MyService extends Service{

	UserFunctions userFunctions;
	JSONObject json;
	MyTimerTask mytask;
	Timer task;
	NotifyServiceReceiver myReceiver;
	

    ProgressDialog  ringProgressDialog;
    Handler hand=new Handler();

	@Override
	public void onCreate() {
		
		userFunctions=new UserFunctions();
		json=new JSONObject();
		
		mytask=new MyTimerTask();
		task=new Timer();
		myReceiver=new NotifyServiceReceiver();
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(myReceiver);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("StopMyService");
		registerReceiver(myReceiver, intentFilter);
		
		task.schedule(mytask, 0, 10000);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void broadCastNotification()
	{
		NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		Intent i=new Intent(getApplicationContext(),PersonalMessage.class);
		PendingIntent pintent=PendingIntent.getActivity(this, 0, i, 0);
		
		NotificationCompat.Builder myBuilder=new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.inbox)
		.setContentTitle("New Message from "+StoreHouse.personalmessageName.get(0))
		.setTicker(StoreHouse.personalmessageName.get(0)+" sent you a message")
		.setContentText("Click here to view the new message");
		
		myBuilder.setContentIntent(pintent);
		
		Notification myNotification=myBuilder.build();
		myNotification.flags|=Notification.FLAG_AUTO_CANCEL;
		
		manager.notify(20, myNotification);
	}
	
	public void sendNotification()
	{
		if(StoreHouse.ActivityVisible)
		{
			updateUI();
			return;
		}else if(StoreHouse.PersonalMessagesVisible)
		{
			refresfUI();
		}else
		getPersonalMessages();  //put notification in the notification bar
		
	}
	
	public void updateUI()
	{
		Intent i=new Intent();
		i.setAction("UPDATEBUTTON");
		i.putExtra("TEXT", "UPDATE");
		sendBroadcast(i);
	}
	
	public void refresfUI()
	{
		Intent i=new Intent();
		i.setAction("REFRESHMESSAGES");
		i.putExtra("TEXT", "REFRESH");
		sendBroadcast(i);
	}
	
	public class MyTimerTask extends TimerTask
	{

		@Override
		public void run() {
			
    		boolean newMessage=false;
    		
            try {
            	json = userFunctions.getLastPersonalMessageId(StoreHouse.getUserObject().getUserID());
            } catch (Exception e) {            }
            
            try 
            {
                 if (json.getString(Constants.SUCCESS) != null) 
                 {
                    //loginErrorMsg.setText("");
                    String res = json.getString(Constants.SUCCESS); 
             
	                 if(Integer.parseInt(res) == 1)
	                     {  	
	                           String ID=json.getString("ID");
	                           if(StoreHouse.getUserObject().getLastMesgId()==null)
	                           {
	                        	   StoreHouse.getUserObject().setLastMesgId(ID);
	                        	   newMessage=true;
	                           }
	                           else if(!StoreHouse.getUserObject().getLastMesgId().equals(ID))
	                           {
	                        	   StoreHouse.getUserObject().setLastMesgId(ID);
	                        	   newMessage=true;
	                           }
	                           else 
	                           {
	                        	   newMessage=false;
	                           }
	                     }
	                 if(newMessage)
	                 {
	                	 sendNotification();
	                 }
                  }
            } catch (JSONException e) {
                e.printStackTrace();
            }
		}
		
	}
	
	public void getPersonalMessages()
    {  	
		
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.getPersonalMessages(StoreHouse.getUserObject().getUserID());
        	                } catch (Exception e) {            }
        	                
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                     {  	
        	                           JSONArray riders=json.getJSONArray("MESSAGES");
        	                           
        	                           	StoreHouse.clearPersonalMessages();
        	                            for(int i=0;i<riders.length();i++)
        	                            {
        	                            	StoreHouse.personalmessage.add(i, riders.getJSONObject(i).getString("MESSAGE"));
        	                            	StoreHouse.personalisMale.add(i, riders.getJSONObject(i).getString("ISMALE"));
        	                            	StoreHouse.personalSenderID.add(i, riders.getJSONObject(i).getString("SENDER_ID"));
        	                            	StoreHouse.personalmessageName.add(i, riders.getJSONObject(i).getString("NAME"));
        	                            	StoreHouse.personalmessagePhoneNumber.add(i, riders.getJSONObject(i).getString("PHONE"));
        	                            }
        	                            	broadCastNotification();    
        	                       }
        	                 else
     	                 	{
     	                 		hand.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
										}
									});
     	                 	}
        	                    }
        	                } catch (JSONException e) {
        	                    e.printStackTrace();
        	                }
        	            }
        	        }).start();
    		} 	

	
	public class NotifyServiceReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			 int rqs = arg1.getIntExtra("RQS", 0);
			 if (rqs == 2){
			  stopSelf();	
		  }
	   }
	}	
}
