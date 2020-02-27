package XPassengersWeb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class databaseAccess {
    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    XPassengersUtils utils = new XPassengersUtils();
    boolean dbInit = false;
    
    public void initDB() {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/xpassengers?" + "user=xpassengers&password=Steppen3weihe&serverTimezone=UTC");
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
    
    public void insertSQLSet(HashMap<String, Object> map, StringBuilder builder) {
    	builder.append(" set ");
    	int counter = 1;
    	int mapSize = map.size();
    	for (String i : map.keySet()) {
    		builder.append(i);
    		builder.append("=");
    		Object value = map.get(i);
    		if (value instanceof String) {
    			builder.append("'");
    			builder.append(value);
    			builder.append("'");
    		} else if (value instanceof java.sql.Date) {
    			builder.append("STR_TO_DATE('");
    			builder.append(value);
    			builder.append("', '%Y-%m-%d')");
    		} else {
    			builder.append(value);
    		}
    		if (counter < mapSize) {
    			builder.append(", ");
    		}
    		counter++;
    	}
    }
    
    public void insertSQLWhere(HashMap<String, Object> map, StringBuilder builder) {
    	builder.append(" where ");
    	int counter = 1;
    	int mapSize = map.size();
    	for(String i : map.keySet()) {
    		builder.append(i);
    		builder.append("=");
    		builder.append(map.get(i));
    		if (counter < mapSize) {
        		builder.append(" and ");
        	}
    		counter++;
    	}    	
    	
    }
    
	public void createTable(HashMap<String, String> columns, String tablename) {
		if (!dbInit) {
    		initDB();
    	}
		int counter = 1;
		int mapSize = columns.size();
		StringBuilder createTable = new StringBuilder();
		createTable.append("CREATE TABLE ");
		createTable.append(tablename);
		createTable.append(" id INT NOT NULL AUTO_INCREMENT, ");
		for (String i : columns.keySet()) {
			String value = columns.get(i);
			createTable.append(i);
			createTable.append(" ");
			createTable.append(value);
			if (counter < mapSize) {
    			builder.append(", ");
    		}
			counter++;
		}
		createTable.append(")");

		PreparedStatement update;
		try {
			update = connect.prepareStatement(createTable.toString());
			update.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addColumns(HashMap<String, Object> columns, String tablename) throws SQLException {
		if (!dbInit) {
    		initDB();
    	}

		int counter = 1;
		int mapSize = columns.size();
		StringBuilder alterTable = new StringBuilder();
		alterTable.append("ALTER TABLE ");
		alterTable.append(tablename);
		
		for (String i : columns.keySet()) {
			String value = columns.get(i);
			alterTable.append(" ADD COLUMN ");
			alterTable.append(i);
			alterTable.append(" ");
			if (value instanceof String) {
				alterTable.append(" VARCHAR(255)");
			} else if (value instanceof int) {
				alterTable.append(" INT(10)");
			} else if (value instanceof java.sql.Date) {
				alterTable.append(" DATE");
			} else {
				System.out.println("can't add column to table " + tablename + " with datatype " value.getClass());
			}
			if (counter < mapSize) {
    			builder.append(", ");
    		}
			counter++;
		}

		PreparedStatement update;
		update = connect.prepareStatement(alterTable.toString());
		update.executeUpdate();
	}

    public ResultSet select(String table, String columns, HashMap<String, Object> wheres) {
    	if (!dbInit) {
    		initDB();
    	}
    	StringBuilder selectStatement = new StringBuilder();
    	selectStatement.append("select ");
    	selectStatement.append(columns);
    	selectStatement.append(" from ");
    	selectStatement.append(table);
    	insertSQLWhere(wheres, selectStatement);
    	Statement get;
		try {
			get = connect.createStatement();
			ResultSet results = get.executeQuery(selectStatement.toString());
	    	if (results != null) {
	    		return results;
	    	}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    	return null;
    }
    
    public void update(String table, HashMap<String, Object> values, int id) {
    	if (!dbInit) {
    		initDB();
    	}
    	StringBuilder updateStatement = new StringBuilder();
    	updateStatement.append("update ");
    	updateStatement.append(table);
    	insertSQLSet(values, updateStatement);
    	updateStatement.append(" where id = ");
    	updateStatement.append(id);
    	PreparedStatement update;
		try {
			update = connect.prepareStatement(updateStatement.toString());
			update.executeUpdate();
		} catch (SQLException e) {
			try {
				addColumns(values, table);
				update = connect.prepareStatement(updateStatement.toString());
				update.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace;
			}			
		}    	
    }
    
    public void update(String table, HashMap <String, Object> values, HashMap<String, Object> wheres) {
    	if (!dbInit) {
    		initDB();
    	}
		StringBuilder updateStatement = new StringBuilder();
    	updateStatement.append("update ");
    	updateStatement.append(table);
    	insertSQLSet(values, updateStatement);
    	insertSQLWhere(wheres, updateStatement);
    	PreparedStatement update;
    	try {
    		update = connect.prepareStatement(updateStatement.toString());
    		update.executeUpdate();
    	} catch (SQLException e) {
			e.printStackTrace;
    		/* addColumns(values, table);
			update = connect.prepareStatement(updateStatement.toString());
			update.executeUpdate(); */
    	}
    }

	public void delete(String table, HashMap<String,Object> wheres) {
		if (!dbInit) {
    		initDB();
    	}
		StringBuilder updateStatement = new StringBuilder();
		updateStatement.append("delete from ");
		updateStatement.append(table);
		insertSQLWhere(wheres, updateStatement);
		PreparedStatement update;
		try {
			update = connect.prepareStatement(updateStatement.toString());
			update.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace;
		}
	}
    
    public long insert(String table, HashMap<String, Object> values) {
    	if (!dbInit) {
    		initDB();
    	}
    	StringBuilder updateStatement = new StringBuilder();
    	updateStatement.append("insert into ");
    	updateStatement.append(table);
    	insertSQLSet(values, updateStatement);
    	PreparedStatement update;
		try {
			update = connect.prepareStatement(updateStatement.toString());
			update.executeUpdate();
			ResultSet generatedKeys = update.getGeneratedKeys();
            if (generatedKeys.next()) {
               return generatedKeys.getLong(1);
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return 0;
    }
    
    public ResultSet getAirlinesAirplanesPlaneData(int airlineid, int airplaneid) throws SQLException {
 		HashMap<String,Object> getAirplane = new HashMap<String,Object>();
		getAirplane.put("airlineid", airlineid);
		getAirplane.put("airplaneid", airplaneid);
		ResultSet results = select("airlines_airplanes", "*", getAirplane);
    	if (results.next()) {
    		return results;
    	}
    	return null;
    }
    
    public HashMap<String,Object> getPlaneConfig(int airline, int airplane) throws SQLException {
		ResultSet result = getAirlinesAirplanesPlaneData(airline,airplane);
		ResultSet planeData = getSingleContent("emptyweight,toweight,fuel,engType", "airplanes", airplane);
		double toweight = planeData.getDouble("toweight");
		double emptyweight = planeData.getDouble("emptyweight");
		double maxFuel = planeData.getDouble("fuel");
		ResultSet engData = getSingleContent("fueltype", "enginetypes", planeData.getInt("engType"));
		String fuelType = engData.getString("fueltype");
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
    
    public long createNewFlight(String from, String to, int cargo, int fuel, int airplaneID, int first, int business, int economy, int valueableCargo) {
    	try {
			HashMap<String, Object> createFlight = new HashMap<String, Object>();
			createFlight.put("fromICAO", from);
			createFlight.put("toICAO", to);
			createFlight.put("pilotid", utils.getActivePilot());
			createFlight.put("airlineid", utils.getActiveAirline());
			createFlight.put("cargo", cargo);
			createFlight.put("fuel", fuel);
			createFlight.put("airplaneid", airplaneID);
			createFlight.put("firstclass", first);
			createFlight.put("businessclass", business);
			createFlight.put("economyclass", economy);
			createFlight.put("valueableCargo", valueableCargo);
			createFlight.put("date", utils.getSQLTodaysDate());
			return insert("flights", createFlight);

    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
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
    	if (!checkFuelprice(type,date)) {
    		float lastPrice = getLastFuelprice(type);
    		float newPrice;
    		if (lastPrice != 0) {
	    		float randPriceChange = ThreadLocalRandom.current().nextInt(-100, 100)/10;
	    		newPrice = lastPrice * (1+(randPriceChange/100));
    		} else {
    			newPrice = ThreadLocalRandom.current().nextFloat() + ThreadLocalRandom.current().nextInt(1, 2);
    		}

			HashMap<String,Object> createFuelprice = new HashMap<String,Object>();
			createFuelprice.put("price", newPrice);
			createFuelprice.put("date", date);
			createFuelprice.put("type", type);
			insert("fuelprices", createFuelprice);
    	}   
    	return getLastFuelprice(type);
    }
    
   
    
    public int checkLicense(int curLicense, int points, float flighthours) {
     	int license = -1;
    	try {
    		if (curLicense < 24) {
    			ResultSet newLicense = getSingleContent("*", "licenses", curLicense + 1);
				if (newLicense.getFloat("hours") < flighthours) {
					return newLicense.getInt("id");
				}
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return license;
    }
    
    public void updatePlane(int activeAirline, int plane, int e, int b, int f) throws SQLException {
		HashMap<String,Object> updatePlane = new HashMap<String,Object>();
		updatePlane.put("economy", e);
		updatePlane.put("business", b);
		updatePlane.put("first", f);

		HashMap<String,Object> updatePlaneWheres = new HashMap<String,Object>();
		updatePlaneWheres.put("airlineid", activeAirline);
		updatePlaneWheres.put("airplaneid", plane);

		update("airlines_airplanes", updatePlane, updatePlaneWheres);
    }
    
    public void buyPlane (int planeID, double newBalance) {
    	try {    		
			ResultSet airplane = getSingleContent("fuel,seats", "airplanes", planeID);
			double fuel = airplane.getDouble("fuel");
			int economy = airplane.getInt("seats");
			int activeAirline = utils.getActiveAirline();
			HashMap<String,Object> buyPlane = new HashMap<String,Object>();
			buyPlane.put("airlineid", activeAirline);
			buyPlane.put("airplaneid", planeID);
			buyPlane.put("quality", 100);
			buyPlane.put("fuelquantity", fuel);
			buyPlane.put("economy", economy);
			insert("airlines_airplanes", buyPlane);
			updateBalance(newBalance);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}    	
    }
    
    public void sellPlane (int planeID, double newBalance) {
    	int activeAirline = utils.getActiveAirline();
    	try {
			HashMap<String,Object> sellPlane = new HashMap<String,Object>();
			sellPlane.put("airlineid", activeAirline);
			sellPlane.put("airplaneid", planeID);
			delete("airlines_airplanes", sellPlane);
			updateBalance(newBalance);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void updateBalance(double newBalance) {
		try {
			HashMap<String,Object> balance = new HashMap<String,Object>();
			balance.put("balance", newBalance);
			update("airlines", balance, utils.getActiveAirline());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void storeFuel(double fuelDif, String fuelType) {
		try {
			double availableFuel;
			String columnFuel;
			PreparedStatement storeFuel;
			
			if (fuelType == utils.avgas) {
				columnFuel = "availableFuelAvGas";
			} else {
				columnFuel = "availableFuelJetA1";
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
			HashMap<String,Object> storeFuel = new HashMap<String,Object>();
			storeFuel.put(columnFuel, newFuel);
			update("airlines", storeFuel, utils.getActiveAirline);
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
		try {
			HashMap<String, Object> updatePlaneFuel = new HashMap<String, Object>();
			updatePlaneFuel.put("fuelquantity", newFuel);
			HashMap<String, Object> updatePlaneFuelWheres = new HashMap<String, Object>();
			updatePlaneFuelWheres.put("airlineid", utils.getActiveAirline()));
			updatePlaneFuelWheres.put("airplaneid", aircraftid);
			update("airlines_airplanes", updatePlaneFuel, updatePlaneFuelWheres);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    public void setPrices(int activeAirline, double first, double business, double economy, double cargo, double free) {
    	try {
			HashMap<String, Object> setPrices = new HashMap<String,Object>();
			setPrices.put("priceFirst", first);
			setPrices.put("priceBusiness", business);
			setPrices.put("priceEconomy", economy);
			setPrices.put("priceCargo", cargo);
			setPrices.put("freeLuggage", free);

			update("airlines", setPrices, activeAirline);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void importAiports(int id, String icao, String type, String name, int elevation, String iata) {
		HashMap<String,Object> importAirports = new HashMap<String,Object>();
		importAirports.put("id", id);
		importAirports.put("icao", icao);
		importAirports.put("type", type);
		importAirports.put("name", name);
		importAirports.put("elevation", elevation):
		importAirports.put("iata", iata);
    	try {
			insert("airports", importAirports);
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				importAirports.remove("id");
				update("airports", importAirports, id);
			}
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

