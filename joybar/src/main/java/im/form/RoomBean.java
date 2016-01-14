package im.form;

public class RoomBean {

	String owner="2";
	String room_id="3";
	String  sessionId= "";
	String  signValue= "";
	String  title= "";
	String   token= "";
	String  type= "private";
	String userName= "gyj";
	int[] users;

	public int[] getUsers() {
		return users;
	}
	public void setUsers(int[] users) {
		this.users = users;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getRoom_id() {
		return room_id;
	}
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSignValue() {
		return signValue;
	}
	public void setSignValue(String signValue) {
		this.signValue = signValue;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
