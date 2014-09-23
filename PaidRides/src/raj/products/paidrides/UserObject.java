package raj.products.paidrides;

public class UserObject {

	private String UserID;
	private String LastMesgId;
	
	public String getLastMesgId() {
		return LastMesgId;
	}

	public void setLastMesgId(String lastMesgId) {
		LastMesgId = lastMesgId;
	}

	public UserObject(String ID)
	{
		this.UserID=ID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}
}
