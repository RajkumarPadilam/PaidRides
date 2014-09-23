package raj.products.paidrides;

import java.util.ArrayList;

import android.content.SharedPreferences;

public class StoreHouse {

	static UserObject userObject;
	static boolean ActivityVisible=true;
	static boolean PersonalMessagesVisible=false;
	

	static ArrayList<String> DefaultRiderName=new ArrayList<String>();
	static ArrayList<String> DefaultRiderPhoneNumber=new ArrayList<String>();
	static ArrayList<String> riderIDs=new ArrayList<String>();
	static ArrayList<String> riderNames=new ArrayList<String>();
	static ArrayList<String> riderPhoneNumbers=new ArrayList<String>();
	static ArrayList<String> riderRatings=new ArrayList<String>();
	static ArrayList<String> riderAvailability=new ArrayList<String>();
	static ArrayList<String> riderisMale=new ArrayList<String>();
	static ArrayList<String> message=new ArrayList<String>();
	static ArrayList<String> messageName=new ArrayList<String>();
	static ArrayList<String> messageID=new ArrayList<String>();
	static ArrayList<String> messagePhoneNumber=new ArrayList<String>();
	static ArrayList<String> messageisMale=new ArrayList<String>();
	static ArrayList<String> Housingmessage=new ArrayList<String>();
	static ArrayList<String> HousingmessageName=new ArrayList<String>();
	static ArrayList<String> HousingmessagePhoneNumber=new ArrayList<String>();
	static ArrayList<String> HousingRiderID=new ArrayList<String>();
	static ArrayList<String> HousingisMale=new ArrayList<String>();
	static ArrayList<String> personalmessage=new ArrayList<String>();
	static ArrayList<String> personalmessageName=new ArrayList<String>();
	static ArrayList<String> personalmessagePhoneNumber=new ArrayList<String>();
	static ArrayList<String> personalSenderID=new ArrayList<String>();
	static ArrayList<String> personalisMale=new ArrayList<String>();
	

	public static void clean(){
		cleanRiderNames();
		clearMessages();
		clearHousingMessages();
		clearPersonalMessages();
		cleanDefaultRiderNames();
	}
	
	public static void cleanDefaultRiderNames(){
		
		DefaultRiderName.clear();
		DefaultRiderPhoneNumber.clear();
	}
	public static void cleanRiderNames()
	{
		riderAvailability.clear();
		riderIDs.clear();
		riderNames.clear();
		riderPhoneNumbers.clear();
		riderRatings.clear();
		riderisMale.clear();
		
	}
	
	public static void clearMessages(){
		message.clear();
		messageName.clear();
		messageID.clear();
		messagePhoneNumber.clear();
		messageisMale.clear();
	}
	
	public static void clearHousingMessages(){
		Housingmessage.clear();
		HousingmessageName.clear();
		HousingmessagePhoneNumber.clear();
		HousingRiderID.clear();
		HousingisMale.clear();
	}
	
	public static void clearPersonalMessages(){
		personalmessage.clear();
		personalmessageName.clear();
		personalmessagePhoneNumber.clear();
		personalSenderID.clear();
		personalisMale.clear();
	}
	
	public static UserObject getUserObject() {
		return userObject;
	}

	public static void setUserObject(UserObject userObject) {
		StoreHouse.userObject = userObject;
	}
		
}
