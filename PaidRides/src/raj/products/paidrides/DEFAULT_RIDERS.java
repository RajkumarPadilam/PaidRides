package raj.products.paidrides;

import java.util.ArrayList;

import org.json.JSONObject;

import raj.products.paidrides.GhostRiders.MyAdapter;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DEFAULT_RIDERS extends Activity {

	ListView riders;
	Button refresh;
	ProgressDialog  ringProgressDialog;
    JSONObject json;
    MyAdapter adapter;
    Handler hand=new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default__riders);
		
		riders=(ListView)findViewById(R.id.default_riders);
		
		

		adapter=new MyAdapter(this, StoreHouse.DefaultRiderName);
		riders.setAdapter(adapter);
		
		Toast.makeText(getApplicationContext(), "Click on any Rider to Call him", Toast.LENGTH_LONG).show();
		
		riders.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Toast.makeText(getApplicationContext(), "position is :"+arg2, Toast.LENGTH_LONG).show();
				Intent i=new Intent();
				i.setAction(Intent.ACTION_CALL);
				i.setData(Uri.parse("tel:"+StoreHouse.DefaultRiderPhoneNumber.get(arg2)));
				startActivity(i);
			}
		});
		
	}
	
	class MyAdapter extends BaseAdapter
	{

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
				row=inflater.inflate(R.layout.default_rider_item, null);				
				holder=new MyViewHolder();
				row.setTag(holder);
			}
			else
			{
				holder=(MyViewHolder) row.getTag();
			}

			holder.setName(row, names.get(position).toString());
			
			return row;
		}
		
		private class MyViewHolder{
				TextView riderName;
				
				public void setName(View v, String text){
					riderName=(TextView)v.findViewById(R.id.defaultRiderName1);
					riderName.setText(text);
				}
		 }	
	 }
}
