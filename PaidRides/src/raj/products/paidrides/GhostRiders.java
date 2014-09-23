package raj.products.paidrides;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GhostRiders extends Activity {

	ListView riders;
	Button refresh,AllRiders2;
	ProgressDialog  ringProgressDialog;
    JSONObject json;
    MyAdapter adapter;
    Handler hand=new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ghost_riders);
		
		riders=(ListView)findViewById(R.id.Riders);
		refresh=(Button)findViewById(R.id.refresh);
		AllRiders2=(Button)findViewById(R.id.AllRiders2);
		
		adapter=new MyAdapter(this, StoreHouse.riderNames);
		riders.setAdapter(adapter);
		
		Toast.makeText(getApplicationContext(), "Click on any Rider to Call him", Toast.LENGTH_LONG).show();
		
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh();
			}
		});
		
		AllRiders2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(StoreHouse.DefaultRiderName.size()<1)
					getDefaultRiders();
					else
					{
						Intent i=new Intent(GhostRiders.this, DEFAULT_RIDERS.class);
	    				startActivity(i);
					}
			}
		});
	
		
		riders.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Toast.makeText(getApplicationContext(), "position is :"+arg2, Toast.LENGTH_LONG).show();
				Intent i=new Intent();
				i.setAction(Intent.ACTION_CALL);
				i.setData(Uri.parse("tel:"+StoreHouse.riderPhoneNumbers.get(arg2)));
				startActivity(i);
			}
		});
	}
	
	
	
	class MyAdapter extends BaseAdapter{

		Context myContext;
		ArrayList names;
		LayoutInflater inflater;
		
		public MyAdapter(Context context, ArrayList names){
			myContext=context;
			this.names=names;
			inflater=(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		}
		
		public void refreshItems(ArrayList names)
		{
			this.names=names;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return names.size();
		}

		@Override
		public Object getItem(int position) {
			return names.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			View row=convertView;
			MyViewHolder holder;
			
			if(row==null)
			{
				row=inflater.inflate(R.layout.rider_item, null);				
				holder=new MyViewHolder();
				row.setTag(holder);
			}
			else
			{
				holder=(MyViewHolder) row.getTag();
			}

			holder.setName(row, names.get(position).toString(), StoreHouse.riderAvailability.get(position).toString(),
					StoreHouse.riderisMale.get(position).toString());
			
			return row;
		}
		
		private class MyViewHolder{
				TextView riderName;
				ImageView riderAvailability;
				ImageView riderPic;
				
				public void setName(View v, String text, String availability, String isMale){
					riderName=(TextView)v.findViewById(R.id.riderName);
					riderName.setText(text);
					riderAvailability=(ImageView)v.findViewById(R.id.riderAvailability);
					riderPic=(ImageView)v.findViewById(R.id.riderPic);
					
					if(isMale.equals("1"))
						riderPic.setImageResource(R.drawable.boy);
					else
						riderPic.setImageResource(R.drawable.girl);
					
					if(availability.equals("1"))
						riderAvailability.setImageResource(R.drawable.green);
					else
						riderAvailability.setImageResource(R.drawable.red);
				}
		 }	
	}
	
    public void refresh()
    {
       	
        ringProgressDialog = ProgressDialog.show(GhostRiders.this, "Please wait ...", "Fetching Riders ...", true);
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
        	                            hand.post(new Runnable() {
											@Override
											public void run() {
												adapter.refreshItems(StoreHouse.riderNames);
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

	public void getDefaultRiders()
    {  	
        ringProgressDialog = ProgressDialog.show(GhostRiders.this, "Please wait ...", "Fetching Riders ...", true);
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
        	                            Intent i=new Intent(GhostRiders.this, DEFAULT_RIDERS.class);
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

}
