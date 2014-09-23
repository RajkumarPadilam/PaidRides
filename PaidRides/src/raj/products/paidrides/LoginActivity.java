package raj.products.paidrides;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class LoginActivity extends Activity {
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
    JSONObject json;
    ProgressDialog  ringProgressDialog;
    SharedPreferences preferences;
    boolean firstTimeUsage=false;
    Handler hand=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
 
        // Importing all assets like buttons, text fields
        inputEmail = (EditText) findViewById(R.id.loginEmail1);
        inputPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        
        preferences = getSharedPreferences("MyPreference", Context.MODE_PRIVATE);
        
        if(preferences.contains("SecondVisit")){
        	firstTimeUsage=false;
        }else
        {
        	firstTimeUsage=true;
        }
        
        if(preferences.contains("USERNAME") && preferences.contains("PASSWORD"))
        {
        	//StoreHouse.myPreference=preferences;
        	
        	if(!preferences.getString("USERNAME", "1234").equals("LOGOUT"))
    		processRequest(preferences.getString("USERNAME", "1234"), preferences.getString("PASSWORD", "1234"));
        }
        
        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if(inputEmail.getText().toString().length()<2)
            		return;
            	processRequest(inputEmail.getText().toString(),inputPassword.getText().toString());
            }
        });
 
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
            }
        });
    }
    
    public void processRequest(final String userName, final String password)
    {
       	
        ringProgressDialog = ProgressDialog.show(LoginActivity.this, "Please wait ...", "Loggin in ...", true);
        ringProgressDialog.setCancelable(true);
        
        final UserFunctions userFunction = new UserFunctions();
        	       new Thread(new Runnable() {
        	        	@Override
        	        	public void run() {
        	                try {
        	                	json = userFunction.login(userName, password);
        	                } catch (Exception e) {            }
        	                
        	                ringProgressDialog.dismiss();
        	                ringProgressDialog=null;
        	                try {
        	                    if (json.getString(Constants.SUCCESS) != null) {
        	                        //loginErrorMsg.setText("");
        	                        String res = json.getString(Constants.SUCCESS); 
        	                 
        	                 if(Integer.parseInt(res) == 1)
        	                     {
        	                            // user successfully logged in
        	                            String UserID=json.getString("ID");
        	                            UserObject object=new UserObject(UserID);
        	                            StoreHouse.userObject=object;
        	                        	
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
        	                        
        	                        Editor myEditor=preferences.edit();
        	                        myEditor.putString("USERNAME", userName);
        	                        myEditor.putString("PASSWORD",password);
        	                        myEditor.commit();
        	                        
        	                        if(firstTimeUsage)
        	                        {
        	                        	Editor edit=preferences.edit();
        	                        	edit.putString("SecondVisit", "true");
        	                        	edit.commit();
        	                        	
        	                        	Intent dashboard = new Intent(getApplicationContext(), Tutorial.class);
    	   	                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	   	                            startActivity(dashboard);
        	                        }
        	                        else
        	                        {
        	                        	Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
    	   	                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	   	                            startActivity(dashboard);	
        	                        }
	   	                            finish();
        	                       }
		        	                 else
		     	                 	{
		     	                 		hand.post(new Runnable() {
												@Override
												public void run() {
													Toast.makeText(getApplicationContext(), "Error While Loggin in", Toast.LENGTH_SHORT).show();
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
