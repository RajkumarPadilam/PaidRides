package raj.products.paidrides;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
 
public class RegisterActivity extends Activity {
    Button btnRegister;
    Button btnLinkToLogin;
    EditText userName;
    EditText phoneNumber;
    EditText passwordET,passwordConfirmET;
    RadioGroup Gender, IsRider;
    TextView registerErrorMsg;
    UserFunctions userFunction = new UserFunctions();
    Handler hand=new Handler();
    ProgressDialog  ringProgressDialog;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1);
 
        // Importing all assets like buttons, text fields
        userName = (EditText) findViewById(R.id.regETUsername);
        phoneNumber = (EditText) findViewById(R.id.regETPhone);
        passwordET = (EditText) findViewById(R.id.regETPassword);
        passwordConfirmET = (EditText) findViewById(R.id.regETConfirmPassword);
        Gender=(RadioGroup)findViewById(R.id.Gender);
        IsRider=(RadioGroup)findViewById(R.id.IsRider);
        
        btnRegister = (Button) findViewById(R.id.SignUp);
        btnLinkToLogin = (Button) findViewById(R.id.Back);
         
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {         
            public void onClick(View view) {
                String name = userName.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String confirmPassword = passwordConfirmET.getText().toString().trim();
                String gender="";
                String isrider="";
                
                if(Gender.getCheckedRadioButtonId()==R.id.regBoy)
                gender="1";
                else
                gender="0";
                
                if(IsRider.getCheckedRadioButtonId()==R.id.regYes)
                    isrider="1";
                    else
                    isrider="0";
                
                if( (phone.trim().length()>0) && (phone.trim().length()<10))
                {
            		Toast.makeText(RegisterActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
            		return;
                }
                
                if( (name.trim().length()<2))
                {
            		Toast.makeText(RegisterActivity.this, "Enter Valid UserName", Toast.LENGTH_SHORT).show();
            		return;
                }
                
                if( (password.trim().length()<2) )
                {
            		Toast.makeText(RegisterActivity.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
            		return;
                }
                
                if( (confirmPassword.trim().length()<2) )
                {
            		Toast.makeText(RegisterActivity.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
            		return;
                }
                else if(!confirmPassword.equals(password))
                {
                	Toast.makeText(RegisterActivity.this, "Confirm Password doesn't match", Toast.LENGTH_SHORT).show();
            		return;
                }
                
                if(isrider.equals("1"))
                {
                	if(phone.trim().length()<10)
                		{
                		Toast.makeText(RegisterActivity.this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                		phoneNumber.requestFocus();
                		return;
                		}
                }
                processRegistration(name, phone, password,gender,isrider);
            	}
            });
        
		     // Link to Login Screen
		        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
		 
		            public void onClick(View view) {
		                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		                startActivity(i);
		                finish();
		            }
		        });
 
        }
 
               
        public void processRegistration(final String userName, final String phone, final String password, final String isMale, final String isRider)
        {
        	
        	ringProgressDialog = ProgressDialog.show(RegisterActivity.this, "Please wait ...", "Loggin in ...", true);
            ringProgressDialog.setCancelable(true);
            
            new Thread(new Runnable() {
				@Override
				public void run() {
		            // check for login response
		            try {
		            	JSONObject json = userFunction.register(userName, password, phone, isMale, isRider);
		            	
		            	ringProgressDialog.dismiss();
		            	ringProgressDialog=null;
		                
		            	if (json.getString(Constants.SUCCESS) != null) {
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
		                        Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);		                         
		                        // Close all views before launching Dashboard
		                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                        startActivity(dashboard);
		                        finish();
		                    }
       	              else
	                 	{
	                 		hand.post(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), "Error during Registration..", Toast.LENGTH_SHORT).show();
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