package raj.products.paidrides;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import raj.products.paidrides.GhostRiders.MyAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageBoard extends Activity {

	Button refreshMessages, callRider, msgRider;
	ListView Messages;
	ProgressDialog  ringProgressDialog;
    JSONObject json;
    MyAdapter adapter;
    UserFunctions userFunction;
    Handler hand=new Handler();
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_board);
		
		userFunction=new UserFunctions();
		
		refreshMessages=(Button)findViewById(R.id.refreshMessages);
		Messages=(ListView)findViewById(R.id.Messages);
		
		adapter=new MyAdapter(this, StoreHouse.messageName);
		Messages.setAdapter(adapter);
		
		refreshMessages.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshMessages();
			}
		});

		callRider=(Button)findViewById(R.id.MsgCallRider);
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
			
			if(StoreHouse.getUserObject().getUserID().equals(StoreHouse.messageID.get(position1)))
			{
				msgRider.setVisibility(View.GONE);
				callRider.setVisibility(View.GONE);
			
			}
			
			callRider.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Toast.makeText(MessageBoard.this, "position is :"+position1, Toast.LENGTH_SHORT).show();
					if(StoreHouse.messagePhoneNumber.get(position1).length()<5)
					{
						Toast.makeText(getApplicationContext(), "Sorry number not available", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent i=new Intent();
					i.setAction(Intent.ACTION_CALL);
					i.setData(Uri.parse("tel:"+StoreHouse.messagePhoneNumber.get(position1)));
					startActivity(i);
				}
			});
			
			msgRider.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Toast.makeText(MessageBoard.this, "position is :"+position1, Toast.LENGTH_SHORT).show();
					//postPersonalMessage(StoreHouse.HousingRiderID.get(position1),"Hey wassup");
					showMessageDialog(position1);
					
				}
			});
			
			viewHolder.setText(row, StoreHouse.message.get(position).toString()+" - "+StoreHouse.messageName.get(position).toString(), StoreHouse.messageisMale.get(position).toString());
			return row;
			
		}
	
		
		private class MyViewHolder
		{
			TextView messageItem;
			ImageView riderPic;
			
			public void setText(View row,String text,String isMale)
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
	
	public void refreshMessages()
    {  	
        ringProgressDialog = ProgressDialog.show(MessageBoard.this, "Please wait ...", "Fetching Posts ...", true);
        ringProgressDialog.setCancelable(true);
        
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
        	                            hand.post(new Runnable() {
											@Override
											public void run() {
												adapter.refreshItems(StoreHouse.messageName);
											}
										});
        	                       }
        	                 else
     	                 	{
     	                 		hand.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(getApplicationContext(), "Error while Processing", Toast.LENGTH_SHORT).show();
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
       	
		ringProgressDialog = ProgressDialog.show(MessageBoard.this, "Please wait ...", "Saving Changes ...", true);
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
											Toast.makeText(MessageBoard.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
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
		  postPersonalMessage(StoreHouse.messageID.get(position1),value);
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
