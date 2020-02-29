package XPassengersWeb;

import java.util.HashMap;

public class ImportLicenses {
	static HashMap<String, Integer> licensesHours = new HashMap<String, Integer>();
	static HashMap<String, String> licensesNames = new HashMap<String, String>();
	
	static {
		licensesHours.put("c0", 0);
		licensesHours.put("c1", 5);
		licensesHours.put("c2", 12);
		licensesHours.put("c3", 22);
		licensesHours.put("c4", 35);
		licensesHours.put("c5", 51);
		licensesHours.put("c6", 70);
		licensesHours.put("c7", 91);
		licensesHours.put("c8", 115);
		licensesHours.put("c9", 142);
		licensesHours.put("c10", 172);
		licensesHours.put("c11", 205);
		licensesHours.put("c12", 241);
		licensesHours.put("c13", 280);
		licensesHours.put("c14", 321);
		licensesHours.put("c15", 365);
		licensesHours.put("c16", 412);
		licensesHours.put("c17", 462);
		licensesHours.put("c18", 515);
		licensesHours.put("c19", 571);
		licensesHours.put("c20", 630);
		licensesHours.put("c21", 691);
		licensesHours.put("c22", 755);
		licensesHours.put("c23", 822);
		
		licensesNames.put("c0", "Pilot");
		licensesNames.put("c1", "Commercial Pilot");
		licensesNames.put("c2", "IFR Commercial Pilot");
		licensesNames.put("c3", "Second Officer");
		licensesNames.put("c4", "Senior Second Officer");
		licensesNames.put("c5", "Master Second Officer");
		licensesNames.put("c6", "First Officer");
		licensesNames.put("c7", "Senior First Officer");
		licensesNames.put("c8", "Master First Officer");
		licensesNames.put("c9", "Captain");
		licensesNames.put("c10", "Senior Captain");
		licensesNames.put("c11", "Flight Captain");
		licensesNames.put("c12", "Senior Flight Captain");
		licensesNames.put("c13", "ATP Captain");
		licensesNames.put("c14", "Senior ATP Captain");
		licensesNames.put("c15", "Master ATP Captain");
		licensesNames.put("c16", "Senior Master ATP Captain");
		licensesNames.put("c17", "Command ATP Captain");
		licensesNames.put("c18", "Senior Command ATP Captain");
		licensesNames.put("c19", "Fleet Captain");
		licensesNames.put("c20", "Senior Fleet Captain");
		licensesNames.put("c21", "Command Fleet Captain");
		licensesNames.put("c22", "Senior Command Fleet Captain");
		licensesNames.put("c23", "Flying Legend");
	}
	
	public static void main(String[] args) {
		DatabaseAccess dao = new DatabaseAccess();
		dao.importLicenses();
	}
}
