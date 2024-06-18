package entities;

import java.io.Serializable;
/**
 * Represents a user in the park management system.
 * This class stores the user's credentials and permissions.
 */
public class User implements Serializable {
		private String UserName; 
        private String Password; 
        private String ParkName; 
        private String Permission;
        /**
         * Constructs a new User instance with specified username and password.
         * Park name and permission are initialized to empty strings and can be set later.
         * 
         * @param userName The username of the user.
         * @param password The password of the user.
         */
        public User(String userName, String password) {
			UserName = userName;
			Password = password;
			ParkName = "";
			Permission = "";

		}
		public String getUserName() {
			return UserName;
		}

		public void setUserName(String userName) {
			UserName = userName;
		}

		public String getPassword() {
			return Password;
		}

		public void setPassword(String password) {
			Password = password;
		}

		public String getParkName() {
			return ParkName;
		}

		public void setParkName(String parkName) {
			ParkName = parkName;
		}

		public String getPermission() {
			return Permission;
		}

		public void setPermission(String permission) {
			Permission = permission;
		}

		@Override
		public String toString() {
			return  UserName + "," + Password + "," + ParkName + "," + Permission ;
		}
		
		
        
        


}
