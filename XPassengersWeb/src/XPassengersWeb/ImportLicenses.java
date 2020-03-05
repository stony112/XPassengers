package XPassengersWeb;

import java.util.HashMap;

public class ImportLicenses {
	static HashMap<Integer, Integer> licensesHours = new HashMap<Integer, Integer>();
	static HashMap<Integer, String> licensesNames = new HashMap<Integer, String>();
	
	static {
		licensesHours.put(0, 0);
		licensesHours.put(1, 5);
		licensesHours.put(2, 12);
		licensesHours.put(3, 22);
		licensesHours.put(4, 35);
		licensesHours.put(5, 51);
		licensesHours.put(6, 70);
		licensesHours.put(7, 91);
		licensesHours.put(8, 115);
		licensesHours.put(9, 142);
		licensesHours.put(10, 172);
		licensesHours.put(11, 205);
		licensesHours.put(12, 241);
		licensesHours.put(13, 280);
		licensesHours.put(14, 321);
		licensesHours.put(15, 365);
		licensesHours.put(16, 412);
		licensesHours.put(17, 462);
		licensesHours.put(18, 515);
		licensesHours.put(19, 571);
		licensesHours.put(20, 630);
		licensesHours.put(21, 691);
		licensesHours.put(22, 755);
		licensesHours.put(23, 822);
		
		licensesNames.put(0, "Pilot");
		licensesNames.put(1, "Commercial Pilot");
		licensesNames.put(2, "IFR Commercial Pilot");
		licensesNames.put(3, "Second Officer");
		licensesNames.put(4, "Senior Second Officer");
		licensesNames.put(5, "Master Second Officer");
		licensesNames.put(6, "First Officer");
		licensesNames.put(7, "Senior First Officer");
		licensesNames.put(8, "Master First Officer");
		licensesNames.put(9, "Captain");
		licensesNames.put(10, "Senior Captain");
		licensesNames.put(11, "Flight Captain");
		licensesNames.put(12, "Senior Flight Captain");
		licensesNames.put(13, "ATP Captain");
		licensesNames.put(14, "Senior ATP Captain");
		licensesNames.put(15, "Master ATP Captain");
		licensesNames.put(16, "Senior Master ATP Captain");
		licensesNames.put(17, "Command ATP Captain");
		licensesNames.put(18, "Senior Command ATP Captain");
		licensesNames.put(19, "Fleet Captain");
		licensesNames.put(20, "Senior Fleet Captain");
		licensesNames.put(21, "Command Fleet Captain");
		licensesNames.put(22, "Senior Command Fleet Captain");
		licensesNames.put(23, "Flying Legend");
	}
	
	public static void main(String[] args) {
		DatabaseAccess dao = new DatabaseAccess();
		dao.importLicenses();
	}
}
