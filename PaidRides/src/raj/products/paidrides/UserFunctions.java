package raj.products.paidrides;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
 
import android.content.Context;
 
public class UserFunctions {
     
    private JSONParser jsonParser;    
    //private static String processURL = "http://10.0.2.2/paid_rides/core.php";
    private static String processURL = "http://spdatabase.x10.mx/QA/core.php";
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
    
    public JSONObject login(String userName, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "LOGIN"));
        params.add(new BasicNameValuePair("USERNAME", userName));
        params.add(new BasicNameValuePair("PASSWORD", password));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject register(String userName, String password, String phone, String isMale, String isRider){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "REGISTER"));
        params.add(new BasicNameValuePair("USERNAME", userName));
        params.add(new BasicNameValuePair("PASSWORD", password));
        params.add(new BasicNameValuePair("PHONE", phone));
        params.add(new BasicNameValuePair("ISMALE", isMale));
        params.add(new BasicNameValuePair("ISRIDER", isRider));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject getDetails(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "DETAILS"));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject getDefaultRiders(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "DEFAULT_RIDERS"));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject updateRating(String rider_id, String rating){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "RATING"));
        params.add(new BasicNameValuePair("RIDER_ID", rider_id));
        params.add(new BasicNameValuePair("RATING", rating));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject insertMessage(String rider_id, String message){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "INSERT_MESSAGE"));
        params.add(new BasicNameValuePair("RIDER_ID", rider_id));
        params.add(new BasicNameValuePair("MESSAGE", message));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject getMessages(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "GET_MESSAGES"));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject insertHousingMessage(String rider_id, String message){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "INSERT_HOUSING_MESSAGE"));
        params.add(new BasicNameValuePair("RIDER_ID", rider_id));
        params.add(new BasicNameValuePair("MESSAGE", message));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject getHousingMessages(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "GET_HOUSING_MESSAGES"));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject insertPersonalMessage(String receiver_id, String sender_id, String message){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "INSERT_PERSONAL_MESSAGE"));
        params.add(new BasicNameValuePair("RECEIVER_ID", receiver_id));
        params.add(new BasicNameValuePair("SENDER_ID", sender_id));
        params.add(new BasicNameValuePair("MESSAGE", message));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject getPersonalMessages(String rider_id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "GET_PERSONAL_MESSAGES"));
        params.add(new BasicNameValuePair("RIDER_ID", rider_id));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject getLastPersonalMessageId(String rider_id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "GET_LAST_PERSONAL_MESSAGE_ID"));
        params.add(new BasicNameValuePair("RIDER_ID", rider_id));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject clearPersonalMessages(String rider_id){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "CLEAR_PERSONAL_MESSAGES"));
        params.add(new BasicNameValuePair("RIDER_ID", rider_id));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
    
    public JSONObject changeAvailability(String rider_id, String available){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("TAG", "AVAILABILITY"));
        params.add(new BasicNameValuePair("RIDER_ID", rider_id));
        params.add(new BasicNameValuePair("AVAILABLE", available));
        JSONObject json = jsonParser.getJSONFromUrl(processURL, params);
        return json;
    }
     
}