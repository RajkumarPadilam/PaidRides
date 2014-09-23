package raj.products.paidrides;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import raj.products.paidrides.MessageBoard.MyAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalMessage extends Activity {

	@Override
	protected void onStart() {
		StoreHouse.PersonalMessagesVisible=true;
		super.onStart();
	}

	@Override
	protected void onStop() {
		StoreHouse.PersonalMessagesVisible=false;
		super.onStop();
	}

	ListView messages;
	MyAdapter adapter;
	Button refreshMessages, callRider, msgRider, clearMsgs;
    JSONObject json;
	ProgressDialog  ringProgressDialog;
	UserFunctions userFunction;
	Handler hand=new Handler();

    MyReceiver myReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_message);
		
		userFunction=new UserFunctions();
		
		messages=(ListView)findViewById(R.id.PersonalMessageList);
		refreshMessages=(Button)findViewById(R.id.refreshPersonalMessages);
		clearMsgs=(Button)findViewById(R.id.clearMsgs);
		
		myReceiver=new MyReceiver();
		
		refreshMessages.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshPersonalMessages();
			}
		});
		
		clearMsgs.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearPersonalMessages();
			}
		});
		
		adapter=new MyAdapter(this, StoreHouse.personalmessage);
		messages.setAdapter(adapter);
	
		IntentFilter filter=new IntentFilter();
		filter.addAction("REFRESHMESSAGES");
		registerReceiver(myReceiver, filter);
		
	}

	
	public class MyAdapter extends BaseAdapter
	{
		Context context;
		ArrayList messages;
		LayoutInflater inflater;

		MyAdapter(Context context, ArrayList messages)
		{
			this.context=context;
			this.messages=messages;
			inflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		}
		
		public void refreshItems(ArrayList messages)
		{
			this.messages=messages;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return messages.size();
		}

		@Override
		public Object getItem(int arg0) {
			return messages.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			
			final int position1=position;
			View row=convertView;
			MyViewHolder viewHolder;
			
			if(row==null)
			{
				viewHolder=new MyViewHolder();
				row=inflater.inflate(R.layout.message_item, null);
				row.setTag(viewHolder);
			}
			else
			{
				viewHolder=(MyViewHolder) row.getTag();
			}
			
			callRider=(Button)row.findViewById(R.id.MsgCallRider);
			msgRider=(Button)row.findViewById(R.id.msg);
			
			if(StoreHouse.getUserObject().getUserID().equals(StoreHouse.personalSenderID.get(position1)))
			{
				msgRider.setVisibility(View.GONE);
				callRider.setVisibility(View.GONE);
			
			}
			msgRider.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					showMessageDialog(position1);
					
				}
			});
			
			callRider.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Toast.makeText(MessageBoard.this, "position is :"+position1, Toast.LENGTH_SHORT).show();
					Intent i=new Intent();
					i.setAction(Intent.ACTION_CALL);
					i.setData(Uri.parse("tel:"+StoreHouse.personalmessagePhoneNumber.get(position1)));
					startActivity(i);
				}
			});
			
			viewHolder.setText(row, StoreHouse.personalmessage.get(position).toString()+" - "+StoreHouse.personalmessageName.get(position).toString()
					,StoreHouse.personalisMale.get(position).toString());
			return row;
			
		}
	
		
		private class MyViewHolder
		{
			TextView messageItem;
			ImageView riderPic;
			
			public void setText(View row,String text, String isMale)
			{
				messageItem=(TextView)row.findViewById(R.id.MesgPosted);
				messageItem.setText(text);
				riderPic=(ImageView)row.findViewById(R.id.MsgRiderPic);
				
				if(isMale.equals("1"))
					riderPic.setImageResource(R.drawable.boy);
				else
					riderPic.setImageResource(R.drawable.girl);
			}
		}
	}

	public void refreshPersonalMessages()
    {  	
        ringProgressDialog = ProgressDialog.show(PersonalMessage.this, "Please wait ...", "Fetching messages ...", true);
        ringProgressDialog.setCancelable(true);
        
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
        	                            hand.post(new Runnable() {
											@Override
											public void run() {
												adapter.refreshItems(StoreHouse.personalmessage);
											}
										});
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
	
	public void postPersonalMessage(final String receiver_ID,final String message)
    {
       	
		ringProgressDialog = ProgressDialog.show(PersonalMessage.this, "Please wait ...", "Saving Changes ...", true);
        ringProgressDialog.setCancelable(true);
      
        new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.insertPersonalMessage(receiver_ID, StoreHouse.getUserObject().getUserID(),message);
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog.dismiss();
        	                ringProgressDialog=null;
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                      {  	
        	                	 	hand.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(PersonalMessage.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
										}
									});
        	                       }
        	                 else
     	                 	{
     	                 		hand.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getApplicationContext(), "Error while sending message", Toast.LENGTH_SHORT).show();
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
	
	public void showMessageDialog(final int position1)
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Post Message");
		alert.setMessage("Message");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  postPersonalMessage(StoreHouse.personalSenderID.get(position1),value);
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}

	public void clearPersonalMessages()
    {  	
        ringProgressDialog = ProgressDialog.show(PersonalMessage.this, "Please wait ...", "", true);
        ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.clearPersonalMessages(StoreHouse.getUserObject().getUserID());
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog.dismiss();
        	                ringProgressDialog=null;
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                     {  	
        	                       StoreHouse.clearPersonalMessages();
        	                       hand.post(new Runnable() {
										@Override
										public void run() {
											adapter.refreshItems(StoreHouse.personalmessage);
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
			if(arg1.getStringExtra("TEXT").equals("REFRESH"))
				refreshPersonalMessages();
		}
	}

}
