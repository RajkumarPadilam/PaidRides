package raj.products.paidrides;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

public class InsertMessage extends Activity {


    ProgressDialog  ringProgressDialog;
    Handler hand=new Handler();
    JSONObject json=new JSONObject();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button UserMessages=(Button)findViewById(R.id.PM2);
		UserMessages.setBackgroundResource(R.drawable.inboxnew);
		
		getPersonalMessages();
	}

	
	public void getPersonalMessages()
    {  	
        ringProgressDialog = ProgressDialog.show(InsertMessage.this, "Please wait ...", "Fetching Messsages ...", true);
        ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.getPersonalMessages(StoreHouse.getUserObject().getUserID());
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog.dismiss();
        	                ringProgressDialog=null;
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
        	                            Intent i=new Intent(InsertMessage.this, PersonalMessage.class);
        	            				startActivity(i);
        	            				finish();
        	                       }
        	                 else
     	                 	{
     	                 		hand.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
											finish();
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
}
