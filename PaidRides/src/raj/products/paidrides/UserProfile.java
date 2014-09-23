package raj.products.paidrides;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UserProfile extends Activity {

	  private RadioGroup radioAvailability, postMessagesGroup;
	  private RadioButton radioAvailable;
	  private Button btnSubmit;
	  int available=0;
	  JSONObject json;
	  boolean postMessageBoolean=false;
	  String postMessage;
	  ProgressDialog  ringProgressDialog,ringProgressDialog1;
	  Handler hand=new Handler();
	  EditText etPostMessage;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		btnSubmit=(Button)findViewById(R.id.submit_changes1);
		radioAvailability=(RadioGroup)findViewById(R.id.radioAvailabilityGroup1);
		
		postMessagesGroup=(RadioGroup)findViewById(R.id.radioPostMessageGroup1);
		
		etPostMessage=(EditText)findViewById(R.id.etPostMessage1);
		
		//if(StoreHouse)
		
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(radioAvailability.getCheckedRadioButtonId()==R.id.radioAvailable)
					available=1;
					//Toast.makeText(getApplicationContext(), "selection is : availavle", Toast.LENGTH_SHORT).show();
				else
					available=0;
				
				changeAvailability();
				
				if(!(etPostMessage.getText().toString().trim().length()<2))
				{
					postMessage=etPostMessage.getText().toString().trim();
					postMessageBoolean=true;
					
					if(postMessagesGroup.getCheckedRadioButtonId()==R.id.MessageBoardPost)
					showMessageDialog(postMessage, 2);//postMessage();
					else
					showMessageDialog(postMessage, 1); //postHousingMessage();
					
				}else{postMessageBoolean=false;}
				
				
					//Toast.makeText(getApplicationContext(), "selection is : busy", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void changeAvailability()
    {
       	
        ringProgressDialog = ProgressDialog.show(UserProfile.this, "Please wait ...", "Saving Changes ...", true);
        ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.changeAvailability(StoreHouse.getUserObject().getUserID(), available+"");
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog.dismiss();
        	                ringProgressDialog=null;
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                     {  	
        	                           /*JSONArray riders=json.getJSONArray("riders");
        	                           StoreHouse.clean();
        	                            for(int i=0;i<riders.length();i++)
        	                            {
        	                            	StoreHouse.riderIDs.add(i, riders.getJSONObject(i).getString("RIDER_ID"));
        	                            	StoreHouse.riderAvailability.add(i, riders.getJSONObject(i).getString("ISAVAILABLE"));
        	                            	StoreHouse.riderPhoneNumbers.add(i, riders.getJSONObject(i).getString("PHONE"));
        	                            	StoreHouse.riderNames.add(i, riders.getJSONObject(i).getString("USERNAME"));
        	                            	StoreHouse.riderRatings.add(i, riders.getJSONObject(i).getString("RATING"));
        	                            	StoreHouse.isMale.add(i, riders.getJSONObject(i).getString("ISMALE"));
        	                            	
        	                            }
        	                            */
        	                            //Finishing the present activity to go back to dashboard
		        	                	 if(!postMessageBoolean)		
		        	                	 finish();
        	                       }
        	                 else
     	                 	{
     	                 		hand.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getApplicationContext(), "Coulnd't process request", Toast.LENGTH_SHORT).show();
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
	
	public void postMessage()
    {
       	
		ringProgressDialog1 = ProgressDialog.show(UserProfile.this, "Please wait ...", "Saving Changes ...", true);
        ringProgressDialog1.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.insertMessage(StoreHouse.getUserObject().getUserID(), postMessage);
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog1.dismiss();
        	                ringProgressDialog1=null;
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                     {  	
        	                           //Finishing the present activity to go back to dashboard
        	                	 		finish();
        	                       }
        	                 else
     	                 	{
     	                 		hand.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getApplicationContext(), "Error while processing", Toast.LENGTH_SHORT).show();
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

	public void postHousingMessage()
    {
       	
		ringProgressDialog1 = ProgressDialog.show(UserProfile.this, "Please wait ...", "Saving Changes ...", true);
        ringProgressDialog1.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.insertHousingMessage(StoreHouse.getUserObject().getUserID(), postMessage);
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog1.dismiss();
        	                ringProgressDialog1=null;
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                     {  	
        	                          
        	                	 		finish();
        	                       }
        	                    }
        	                } catch (JSONException e) {
        	                    e.printStackTrace();
        	                }
        	            }
        	        }).start();
        }
	
	public void showMessageDialog(final String message, final int type)
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		if(type==1)
			alert.setTitle("Posting in Housing Section");
		else
			alert.setTitle("Posting in Message Board");
		
		alert.setMessage(message);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			if(type==1)
				postHousingMessage();
			else
				postMessage();
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}
}
