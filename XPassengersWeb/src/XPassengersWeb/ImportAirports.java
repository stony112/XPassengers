package XPassengersWeb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImportAirports {

	public static void main(String[] args) {
		String filepath = "E:\\Downloads\\airports.csv";
		try {
			DatabaseAccess dao = new DatabaseAccess();
			BufferedReader csvReader = new BufferedReader(new FileReader(filepath));
			String row;
			while ((row = csvReader.readLine()) != null) {
				row = row.replace("\"", "");
				String[] data = row.split(",");
				if (!data[0].equals("id")) {
					int id = Integer.parseInt(data[0]);
					String icao = data[1];
					String type = data[2];
					String name = data[3];
					int elevation = 0;
					String iata = "";
					try {
						if (!data[6].isEmpty()) {
							elevation = Integer.parseInt(data[6]);
						}
						try {
							iata = data[13];
						} catch (ArrayIndexOutOfBoundsException a) {
							iata = "";
						}
					} catch (NumberFormatException n) {
						try {
							if (!data[7].isEmpty()) {
								elevation = Integer.parseInt(data[6]);
							}
							try {
								iata = data[14];
							} catch (ArrayIndexOutOfBoundsException a) {
								iata = "";
							}
						} catch (NumberFormatException m) {
							for (int i = 0; i < data.length; i++) {
								System.out.println(i);
								System.out.println(data[i]);
							}
						}
					}
					dao.importAiports(id, icao, type, name, elevation, iata);
					csvReader.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
