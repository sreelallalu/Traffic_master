package appcmpt.example.Traffic.n;

public class profile {


	String user_id="ii";
	String qcode="hjgjh";
	String type="jk";

	public profile() {
	}

	public profile(String user_id, String qcode, String type) {
		this.user_id = user_id;
		this.qcode = qcode;
		this.type = type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getQcode() {
		return qcode;
	}

	public void setQcode(String qcode) {
		this.qcode = qcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}