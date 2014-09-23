package raj.products.paidrides;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	@Override
	protected void onStart() {
		StoreHouse.ActivityVisible=true;
		super.onStart();
	}

	@Override
	protected void onStop() {
		StoreHouse.ActivityVisible=false;
		super.onStop();
	}

	Button GhostRiders, ShareRides, logout, MessageBoard, AllRiders, Housing, UserMessages;
    JSONObject json;
    ProgressDialog  ringProgressDialog;
    Handler hand=new Handler();
    Intent startMyService;
    MyReceiver myReceiver;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startMyService=new Intent(this,MyService.class);
		myReceiver=new MyReceiver();
		
		GhostRiders=(Button)findViewById(R.id.ridersBT3);
		ShareRides=(Button)findViewById(R.id.settings3);
		MessageBoard=(Button)findViewById(R.id.msgBoardbt3);
		Housing=(Button)findViewById(R.id.housing3);
		UserMessages=(Button)findViewById(R.id.PM2);;
		
		logout=(Button)findViewById(R.id.logout1);
				
		ShareRides.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.this,UserProfile.class);
				startActivity(i);
			}
		});
		
		UserMessages.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserMessages.setBackgroundResource(R.drawable.inbox);
				getPersonalMessages();
			}
		});
		
		/*AllRiders.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.this,Tutorial.class);
				startActivity(i);
				finish();
				if(StoreHouse.DefaultRiderName.size()<1)
				getDefaultRiders();
				else
				{
					Intent i=new Intent(MainActivity.this, DEFAULT_RIDERS.class);
    				startActivity(i);
				}
					
			}
		});*/
		
		GhostRiders.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDetails();
			}
		});
		
		MessageBoard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getMessages();
			}
		});
		
		Housing.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getHousingMessages();
			}
		});
		
		
		logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StoreHouse.clean();
				
				
				if(getSharedPreferences("MyPreference", Context.MODE_PRIVATE).contains("USERNAME"))
					getSharedPreferences("MyPreference", Context.MODE_PRIVATE).edit().clear().commit();
				
				stopService();
				
				Intent i=new Intent(MainActivity.this, LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				
				finish();
			}
		});
		
		//Intent i=new Intent(this,MyService.class);
		startService(startMyService);
		
		IntentFilter filter=new IntentFilter();
		filter.addAction("UPDATEBUTTON");
		registerReceiver(myReceiver, filter);
	}
	
	public void stopService()
	{
		  Intent intent = new Intent();
		  intent.setAction("StopMyService");
		  intent.putExtra("RQS", 2);
		  sendBroadcast(intent);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		StoreHouse.clean();
		stopService();
	}
	
	
	public void getDetails()
    {  	
        ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Fetching Riders ...", true);
        ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.getDetails();
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog.dismiss();
        	                ringProgressDialog=null;
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                     {  	
        	                           JSONArray riders=json.getJSONArray("riders");
        	                           StoreHouse.cleanRiderNames();
        	                            for(int i=0;i<riders.length();i++)
        	                            {
        	                            	StoreHouse.riderIDs.add(i, riders.getJSONObject(i).getString("RIDER_ID"));
        	                            	StoreHouse.riderAvailability.add(i, riders.getJSONObject(i).getString("ISAVAILABLE"));
        	                            	StoreHouse.riderPhoneNumbers.add(i, riders.getJSONObject(i).getString("PHONE"));
        	                            	StoreHouse.riderNames.add(i, riders.getJSONObject(i).getString("USERNAME"));
        	                            	StoreHouse.riderRatings.add(i, riders.getJSONObject(i).getString("RATING"));
        	                            	StoreHouse.riderisMale.add(i, riders.getJSONObject(i).getString("ISMALE"));
        	                            	
        	                            }
        	                            Intent i=new Intent(MainActivity.this, GhostRiders.class);
        	            				startActivity(i);
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
	
/*	public void getDefaultRiders()
    {  	
        ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Fetching Riders ...", true);
        ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.getDefaultRiders();
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog.dismiss();
        	                ringProgressDialog=null;
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                     {  	
        	                           JSONArray riders=json.getJSONArray("default_riders");
        	                           StoreHouse.cleanDefaultRiderNames();
        	                            for(int i=0;i<riders.length();i++)
        	                            {
        	                            	StoreHouse.DefaultRiderPhoneNumber.add(i, riders.getJSONObject(i).getString("PHONE"));
        	                            	StoreHouse.DefaultRiderName.add(i, riders.getJSONObject(i).getString("USERNAME"));
        	                            }
        	                            Intent i=new Intent(MainActivity.this, DEFAULT_RIDERS.class);
        	            				startActivity(i);
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
        }*/
	
	public void getMessages()
    {  	
        ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Fetching Posts ...", true);
        ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.getMessages();
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
        	                           
        	                           	StoreHouse.clearMessages();
        	                            for(int i=0;i<riders.length();i++)
        	                            {
        	                            	StoreHouse.message.add(i, riders.getJSONObject(i).getString("MESSAGE"));
        	                            	StoreHouse.messageisMale.add(i, riders.getJSONObject(i).getString("ISMALE"));
        	                            	StoreHouse.messageName.add(i, riders.getJSONObject(i).getString("NAME"));
        	                            	StoreHouse.messageID.add(i, riders.getJSONObject(i).getString("RIDER_ID"));
        	                            	StoreHouse.messagePhoneNumber.add(i, riders.getJSONObject(i).getString("PHONE"));
        	                            }
        	                            Intent i=new Intent(MainActivity.this, MessageBoard.class);
        	            				startActivity(i);
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
	
	public void getHousingMessages()
    {  	
        ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Fetching Posts ...", true);
        ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.getHousingMessages();
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
        	                           
        	                           	StoreHouse.clearHousingMessages();
        	                            for(int i=0;i<riders.length();i++)
        	                            {
        	                            	StoreHouse.Housingmessage.add(i, riders.getJSONObject(i).getString("MESSAGE"));
        	                            	StoreHouse.HousingisMale.add(i, riders.getJSONObject(i).getString("ISMALE"));
        	                            	StoreHouse.HousingRiderID.add(i, riders.getJSONObject(i).getString("RIDER_ID"));
        	                            	StoreHouse.HousingmessageName.add(i, riders.getJSONObject(i).getString("NAME"));
        	                            	StoreHouse.HousingmessagePhoneNumber.add(i, riders.getJSONObject(i).getString("PHONE"));
        	                            }
        	                            Intent i=new Intent(MainActivity.this, Housing.class);
        	            				startActivity(i);
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
	
	public void getPersonalMessages()
    {  	
        ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Fetching Messsages ...", true);
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
        	                            Intent i=new Intent(MainActivity.this, PersonalMessage.class);
        	            				startActivity(i);
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
	
	public void getLastPersonalMessageId()
    {  	
        //ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Fetching Messsages ...", true);
        //ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	        		
        	        		boolean newMessage=false;
        	        		
        	                try {
        	                	json = userFunction.getLastPersonalMessageId(StoreHouse.getUserObject().getUserID());
        	                } catch (Exception e) {            }
        	                
        	//                ringProgressDialog.dismiss();
        	  //              ringProgressDialog=null;
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
		        	                	    hand.post(new Runnable() {
												@Override
												public void run() {
													UserMessages.setBackgroundResource(R.drawable.inboxnew);
													Toast.makeText(getApplicationContext(), "New Message Available", Toast.LENGTH_SHORT).show();
												}
											});
		        	                 }
		        	                 else
		        	                 {
		        	                	 hand.post(new Runnable() {
												@Override
												public void run() {
													UserMessages.setBackgroundResource(R.drawable.inbox);
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

	private class MyReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if(arg1.getStringExtra("TEXT").equals("UPDATE"))
			UserMessages.setBackgroundResource(R.drawable.inboxnew);
		}
	}
}
