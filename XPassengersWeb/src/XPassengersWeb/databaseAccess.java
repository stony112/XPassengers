package XPassengersWeb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

public class databaseAccess {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    XPassengersUtils utils = new XPassengersUtils();
    boolean dbInit = false;
    
    public void initDB() {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/xpassengers?" + "user=cs&password=Steppen3weihe&serverTimezone=UTC");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    	dbInit = true;
    }
    
    public ResultSet getResults(String table) throws SQLException {
    	if (!dbInit) {
    		initDB();
    	}
    	Statement get = connect.createStatement();
    	ResultSet results = get.executeQuery("SELECT * FROM " + table);
    	return results;
    }
    
    public ResultSet getSingleContent(String columns, String table, int id) throws SQLException {
    	initDB();
    	if (!dbInit) {
    		initDB();
    	}
    	Statement get = connect.createStatement();
    	ResultSet results = get.executeQuery("SELECT " + columns + " FROM " + table + " where id = " + id);
    	if (results.next()) {
    		return results;
    	}
    	return null;
    }
    
    public ResultSet getAirlinesAirplanesData(String columns, int airlineid) {
    	if (!dbInit) {
    		initDB();
    	}
    	Statement get;
		try {
			get = connect.createStatement();
			ResultSet results = get.executeQuery("SELECT " + columns + " FROM airlines_airplanes where airlineid = " + airlineid);
			return results;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return null;
    }
    
    public ResultSet getAirlinesAirplanesPlaneData(int airlineid, int airplaneid) throws SQLException {
    	if (!dbInit) {
    		initDB();
    	}
    	PreparedStatement getAirplane = connect.prepareStatement("SELECT * FROM airlines_airplanes WHERE airlineid = ? AND airplaneid = ?");
    	getAirplane.setInt(1, airlineid);
    	getAirplane.setInt(2, airplaneid);
    	ResultSet results = getAirplane.executeQuery();
    	if (results.next()) {
    		return results;
    	}
    	return null;
    }
    
    public HashMap<String,Object> getPlaneConfig(int airline, int airplane) throws SQLException {
    	if (!dbInit) {
    		initDB();
    	}
		ResultSet result = getAirlinesAirplanesPlaneData(airline,airplane);
		ResultSet planeData = getSingleContent("emptyweight,toweight,fuel,fuelType", "airplanes", airplane);
		double toweight = planeData.getDouble("toweight");
		double emptyweight = planeData.getDouble("emptyweight");
		double maxFuel = planeData.getDouble("fuel");
		String fuelType = planeData.getString("fuelType");
		double useableweight = toweight - emptyweight;
		
		int first = result.getInt("first");
		int business = result.getInt("business");
		int economy = result.getInt("economy");
		int quality = result.getInt("quality");
		int fuel = result.getInt("fuelquantity");
		String lastPos = result.getString("lastposition");
		String livery = result.getString("livery");
		HashMap<String,Object> planeConfig = new HashMap<String,Object>();
		planeConfig.put("economy", economy);
		planeConfig.put("business", business);
		planeConfig.put("first", first);
		planeConfig.put("quality", quality);
		planeConfig.put("fuel", fuel);
		planeConfig.put("lastPos", lastPos);
		planeConfig.put("livery", livery);
		planeConfig.put("useableWeight", useableweight);
		planeConfig.put("maxFuel", maxFuel);
		planeConfig.put("fuelType", fuelType);
		return planeConfig;
	}
    
    public void createAirline(String name, String homebase, String iata) throws SQLException {
    	if (!dbInit) {
    		initDB();
    	}
    	PreparedStatement createAirline = connect.prepareStatement("insert into airlines (name,homebase,iata,balance) values (?,?,?,?)");
    	createAirline.setString(1, name);
    	createAirline.setString(2, homebase);
    	createAirline.setString(3, iata);
    	createAirline.setFloat(4, 500000);
    	createAirline.executeUpdate();
    }
    
    public boolean checkFuelprice(String type, java.sql.Date date) {
    	try {
    		if (!dbInit) {
        		initDB();
        	}
			Statement get = connect.createStatement();
	    	ResultSet results = get.executeQuery("SELECT * FROM fuelprices where type = '" + type + "' and date = '" + date + "'");
	    	if (results.next()) {
	    		return true;
	    	}    	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
    }
    
    public float getLastFuelprice(String type) {
    	try {
    		if (!dbInit) {
        		initDB();
        	}
			Statement get = connect.createStatement();
	    	ResultSet results = get.executeQuery("SELECT * FROM fuelprices where type = '" + type + "' ORDER BY date DESC LIMIT 1" );
	    	if (results.next()) {
		    	float price = results.getFloat("price");
		    	return price;
	    	}
	    	return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return 0;
    }
    
    public float createFuelprice(String type) throws SQLException {
    	java.sql.Date date = utils.getSQLTodaysDate();
    	if (!dbInit) {
    		initDB();
    	}
    	if (!checkFuelprice(type,date)) {
    		float lastPrice = getLastFuelprice(type);
    		float newPrice;
    		if (lastPrice != 0) {
	    		float randPriceChange = ThreadLocalRandom.current().nextInt(-100, 100)/10;
	    		newPrice = lastPrice * (1+(randPriceChange/100));
    		} else {
    			newPrice = ThreadLocalRandom.current().nextFloat() + ThreadLocalRandom.current().nextInt(1, 2);
    		}
    		PreparedStatement createFuelprice = connect.prepareStatement("insert into fuelprices (price,date,type) values (?,?,?)");
    		createFuelprice.setFloat(1, newPrice);
    		createFuelprice.setDate(2, date);
    		createFuelprice.setString(3, type);
    		createFuelprice.executeUpdate();
    	}   
    	return getLastFuelprice(type);
    }
    
    public void createPilot(String firstname, String lastname, java.sql.Date birthday, int airline) throws SQLException {
    	if (!dbInit) {
    		initDB();
    	}
    	PreparedStatement createPilot = connect.prepareStatement("insert into pilots (firstname,lastname,birthday,airlineid) values (?,?,?,?)");
    	createPilot.setString(1, firstname);
    	createPilot.setString(2, lastname);
    	createPilot.setDate(3, birthday);
    	createPilot.setInt(4, airline);
    	createPilot.executeUpdate();
    }
    
    public void createAirplane(String name, double toweight, double fuel, double emptyweight, String path, int eng, int prop, float price, int seats, String fuelType) throws SQLException {
    	if (!dbInit) {
    		initDB();
    	}
    	PreparedStatement createAirplane = connect.prepareStatement("insert into airplanes(name,toweight,fuel,emptyweight,path,engines,props,price,seats,fuelType) values (?,?,?,?,?,?,?,?,?,?)"); 
    	createAirplane.setString(1, name);
    	createAirplane.setDouble(2, toweight);
    	createAirplane.setDouble(3, fuel);
    	createAirplane.setDouble(4, emptyweight);
    	createAirplane.setString(5, path);
    	createAirplane.setInt(6, eng);
    	createAirplane.setInt(7, prop);
    	createAirplane.setFloat(8, price);
    	createAirplane.setInt(9, seats);
    	createAirplane.setString(10,fuelType);
    	createAirplane.executeUpdate();
    }
    
    public void updatePlane(int activeAirline, int plane, int e, int b, int f) throws SQLException {
    	if (!dbInit) {
    		initDB();
    	}
    	PreparedStatement updatePlane = connect.prepareStatement("update airlines_airplanes set economy=?, business=?, first=? where airlineid=? and airplaneid=?");
    	updatePlane.setInt(1, e);
    	updatePlane.setInt(2, b);
    	updatePlane.setInt(3, f);
    	updatePlane.setInt(4, activeAirline);
    	updatePlane.setInt(5, plane);
    	updatePlane.executeUpdate();
    }
    
    public void buyPlane (int planeID, double newBalance) {
    	try {    		
    		if (!dbInit) {
        		initDB();
        	}
			ResultSet airplane = getSingleContent("fuel,seats", "airplanes", planeID);
			double fuel = airplane.getDouble("fuel");
			int economy = airplane.getInt("seats");
			int activeAirline = utils.getActiveAirline();
			PreparedStatement buyPlane = connect.prepareStatement("insert into airlines_airplanes (airlineid,airplaneid,quality,fuelquantity,economy) values (?,?,?,?,?)");
			buyPlane.setInt(1, activeAirline);
			buyPlane.setInt(2, planeID);
			buyPlane.setInt(3, 100);
			buyPlane.setDouble(4, fuel);
			buyPlane.setInt(5, economy);
			buyPlane.executeUpdate();
			updateBalance(newBalance);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}    	
    }
    
    public void sellPlane (int planeID, double newBalance) {
    	if (!dbInit) {
    		initDB();
    	}
    	int activeAirline = utils.getActiveAirline();
    	try {
    		PreparedStatement sellPlane = connect.prepareStatement("delete from airlines_airplanes where airlineid = ? and airplaneid = ?");
        	sellPlane.setInt(1, activeAirline);
        	sellPlane.setInt(2, planeID);
			sellPlane.executeUpdate();
			updateBalance(newBalance);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void updateBalance(double newBalance) {
    	if (!dbInit) {
    		initDB();
    	}
		try {
			PreparedStatement balance = connect.prepareStatement("update airlines set balance = ? where id = ?");
			balance.setDouble(1, newBalance);
			balance.setDouble(2, utils.getActiveAirline());
			balance.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void storeFuel(double fuelDif, String fuelType) {
		if (!dbInit) {
    		initDB();
    	}
		try {
			double availableFuel;
			String columnFuel;
			PreparedStatement storeFuel;
			if (fuelType == utils.avgas) {
				columnFuel = "availableFuelAvGas";
				storeFuel = connect.prepareStatement("update airlines set availableFuelAvGas = ? where id = ?");
			} else {
				columnFuel = "availableFuelJetA1";
				storeFuel = connect.prepareStatement("update airlines set availableFuelJetA1 = ? where id = ?");
			}
			String column = columnFuel + ",balance";
			ResultSet airline = getSingleContent(column, "airlines", utils.getActiveAirline());
			availableFuel = airline.getDouble(columnFuel);
			
			double newFuel = availableFuel - fuelDif;
			if (newFuel < 0) {
				double balance = airline.getDouble("balance");
				float fuelPrice = createFuelprice(fuelType);
				double fuelQuantity = newFuel * -1;
				double completeFuelprice = fuelPrice * fuelQuantity;
				double newBalance = balance - completeFuelprice;
				updateBalance(newBalance);
			}
			
			storeFuel.setDouble(1, newFuel);
			storeFuel.setInt(2, utils.getActiveAirline());
			storeFuel.executeUpdate();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void buyFuel(String fuelType, double newFuel) {
		PreparedStatement storeFuel;
		try {
			if (fuelType == utils.avgas) {
				storeFuel = connect.prepareStatement("update airlines set availableFuelAvGas = ? where id = ?");
			} else {
				storeFuel = connect.prepareStatement("update airlines set availableFuelJetA1 = ? where id = ?");
			}
			storeFuel.setDouble(1, newFuel);
			storeFuel.setInt(2, utils.getActiveAirline());
			storeFuel.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void updatePlaneFuel(int aircraftid, double newFuel) {
		if (!dbInit) {
    		initDB();
    	}
		try {
			PreparedStatement updatePlaneFuel = connect.prepareStatement("update airlines_airplanes set fuelquantity = ? where airlineid = ? and airplaneid = ?");
			updatePlaneFuel.setDouble(1, newFuel);
			updatePlaneFuel.setInt(2, utils.getActiveAirline());
			updatePlaneFuel.setInt(3, aircraftid);
			updatePlaneFuel.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    public void setPrices(int activeAirline, double first, double business, double economy, double cargo, double free) {
    	try {
    		if (!dbInit) {
        		initDB();
        	}
			PreparedStatement setPrices = connect.prepareStatement("update airlines set priceFirst = ?, priceBusiness = ?, priceEconomy = ?, priceCargo = ?, freeLuggage = ? where id = ?");
			setPrices.setDouble(1, first);
			setPrices.setDouble(2, business);
			setPrices.setDouble(3, economy);
			setPrices.setDouble(4, cargo);
			setPrices.setDouble(5, free);
			setPrices.setInt(6, activeAirline);
			setPrices.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public int countWhere(String table, String condition) {
    	try {
    		if (!dbInit) {
        		initDB();
        	}
			Statement get = connect.createStatement();
			ResultSet results = get.executeQuery("SELECT count(*) FROM " + table + " where " + condition);
	    	if (results.next()) {
	    		int counter = results.getInt(1);
	    		return counter;
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return 0;   	
    }

    // You need to close the resultSet
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }
}

