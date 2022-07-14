package models;

public class UserLogin {

	private static int currentUserId;
	
	

	public static int getCurrentUserId() {
		return currentUserId;
	}

	public static void setCurrentUserId(int id) {
		currentUserId = id;
	}
	
}