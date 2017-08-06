package de.de.samplehungergamesplugin;

import de.de.samplehungergamesplugin.annotations.NotNull;

import java.sql.*;

/**
 *
 */
public final class Database {

	static {
		try{
			//Register the Driver class indirectly
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException classNotFoundEx){
			throw new Error("Could not found mysql's Driver class!", classNotFoundEx);
		}
	}

	private static final String USERNAME = "SampleHGPlugin";
	private static final String PASSWORD = "";
	private static final String URL_PREFIX = "jdbc:mysql://localhost:7169/dbs/SampleHGPlugin/";

	private Connection dbConnection;


	Database(@NotNull String databaseName) throws Error{
		try{
			dbConnection = DriverManager.getConnection(URL_PREFIX + databaseName, USERNAME, PASSWORD);
			dbConnection.prepareStatement("CREATE DATABASE IF NOT EXISTS " + databaseName + ";");
		}catch(SQLException sqlEx){
			throw new Error("Could not establish connection to database: " + databaseName + " !", sqlEx);
		}
	}

	void execute(String sql) throws Error{
		try {
			dbConnection.prepareStatement(sql).execute();
		}catch(SQLException sqlEx){
			throw new Error("Something gone wrong with this statement: \"" + sql + "\" !", sqlEx);
		}
	}


	ResultSet query(String sql) throws Error{
		try {
			return dbConnection.prepareStatement(sql).executeQuery();
		}catch (SQLException sqlEx){
			throw new Error("Could not obtain results using SQL statement: \"" + sql + "\" !");
		}
	}


	void update(String sql) throws Error{
		try{
			dbConnection.prepareStatement(sql).executeUpdate();
		}catch(SQLException sqlEx){
			throw new Error("Could not update database with following statement: \"" + sql +"\" !", sqlEx);
		}
	}


	void close() throws Error{
		try {
			if(!dbConnection.isClosed())
				dbConnection.close();
		}catch(SQLException sqlEx){
			throw new Error("Could not close connection to the database!", sqlEx);
		}
	}

	//Close the connection on gc automatically
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}
}
