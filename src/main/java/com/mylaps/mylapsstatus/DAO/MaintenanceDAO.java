package com.mylaps.mylapsstatus.DAO;

import com.mylaps.mylapsstatus.model.Maintenance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MaintenanceDAO {

    public List<Maintenance> selectAllMaintenance() {
        final Connection dbCon;
        final Statement statement;
        final List<Maintenance> systems = new ArrayList<>();
        final String sqlString = "SELECT * FROM MAINTENANCE ORDER BY SYSTEM_ID";
        try {
            dbCon = getDBConnection();
            assert dbCon != null;
            statement = dbCon.createStatement();
            final ResultSet resultSet = statement.executeQuery(sqlString);

            while (resultSet.next()) {
                Maintenance currentMaintenance = new Maintenance();
                currentMaintenance.setSystemId(resultSet.getInt("SYSTEM_ID"));
                currentMaintenance.setId(resultSet.getInt("ID"));
                currentMaintenance.setBeginDate(resultSet.getString("START_DATE"));
                currentMaintenance.setEndDate(resultSet.getString("END_DATE"));
                systems.add(currentMaintenance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return systems;
    }

    public List<Maintenance> selectHistoryMaintenance() {
        final Connection dbCon;
        final Statement statement;
        final List<Maintenance> systems = new ArrayList<>();
        final String sqlString = "SELECT * FROM MAINTENANCE_HISTORY ORDER BY SYSTEM_ID";
        try {
            dbCon = getDBConnection();
            assert dbCon != null;
            statement = dbCon.createStatement();
            final ResultSet resultSet = statement.executeQuery(sqlString);

            while (resultSet.next()) {
                Maintenance currentMaintenance = new Maintenance();
                currentMaintenance.setSystemId(resultSet.getInt("SYSTEM_ID"));
                currentMaintenance.setId(resultSet.getInt("ID"));
                currentMaintenance.setBeginDate(resultSet.getString("START_DATE"));
                currentMaintenance.setEndDate(resultSet.getString("END_DATE"));
                systems.add(currentMaintenance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return systems;
    }

    public List<Maintenance> selectSingleHistoryMaintenance(final int systemId) {
        final Connection dbCon;
        final Statement statement;
        final List<Maintenance> systems = new ArrayList<>();
        final String sqlString = "SELECT * FROM MAINTENANCE_HISTORY WHERE SYSTEM_ID = "+systemId;
        try {
            dbCon = getDBConnection();
            assert dbCon != null;
            statement = dbCon.createStatement();
            final ResultSet resultSet = statement.executeQuery(sqlString);

            while (resultSet.next()) {
                Maintenance currentMaintenance = new Maintenance();
                currentMaintenance.setSystemId(resultSet.getInt("SYSTEM_ID"));
                currentMaintenance.setId(resultSet.getInt("ID"));
                currentMaintenance.setBeginDate(resultSet.getString("START_DATE"));
                currentMaintenance.setEndDate(resultSet.getString("END_DATE"));
                systems.add(currentMaintenance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return systems;
    }
    
    public boolean newMaintenance(final String startDate, final String endDate, final int systemId) throws SQLException {
        final Connection dbCon;
        final Statement statement;
        boolean complete = false;
        final String sqlString = "INSERT INTO MAINTENANCE(START_DATE, END_DATE, SYSTEM_ID) VALUES ('" + startDate + "', '" + endDate + "', " + systemId + ")";
        final String sqlStringHistory = "INSERT INTO MAINTENANCE_HISTORY(START_DATE, END_DATE, SYSTEM_ID) VALUES ('" + startDate + "', '" + endDate + "', " + systemId + ")";

        if (systemId != 0 && startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            try {                
                dbCon = getDBConnection();
                assert dbCon != null;
                statement = dbCon.createStatement();
                statement.executeUpdate(sqlString);
                statement.executeUpdate(sqlStringHistory);
                complete = true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return complete;
    }

    public boolean restoreMaintenanceHistory(final String startDate, final String endDate, final int systemId) throws SQLException {
        final Connection dbCon;
        final Statement statement;
        boolean complete = false;
        final String sqlStringHistory = "INSERT INTO MAINTENANCE_HISTORY(START_DATE, END_DATE, SYSTEM_ID) VALUES ('" + startDate + "', '" + endDate + "', " + systemId + ")";

        if (systemId != 0 && startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            try {
                dbCon = getDBConnection();
                assert dbCon != null;
                statement = dbCon.createStatement();
                statement.executeUpdate(sqlStringHistory);
                complete = true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return complete;
    }
    
    public boolean updateMaintenance(final String startDate, final String endDate, final int systemId) throws SQLException {
        final Connection dbCon;
        final Statement statement;
        boolean complete = false;
        final String sqlString = "UPDATE MAINTENANCE SET START_DATE = '"+startDate+"', END_DATE = '"+endDate+"' WHERE SYSTEM_ID = "+systemId;
        final String sqlStringHistory = "UPDATE MAINTENANCE_HISTORY SET START_DATE = '"+startDate+"', END_DATE = '"+endDate+"' WHERE SYSTEM_ID = "+systemId;

        if (systemId != 0 && startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            try {                
                dbCon = getDBConnection();
                assert dbCon != null;
                statement = dbCon.createStatement();
                statement.executeUpdate(sqlString);
                statement.executeUpdate(sqlStringHistory);
                complete = true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return complete;
    }
    
    //Deletes maintenance belonging to a system
    public boolean deleteMaintenance(final int systemId) throws SQLException {
        final Connection dbCon;
        final Statement statement;
        boolean complete = false;
        final String sqlString = "DELETE FROM MAINTENANCE WHERE SYSTEM_ID = '" + systemId + "'";

        if (systemId != 0) {
            try {
                dbCon = getDBConnection();
                assert dbCon != null;
                statement = dbCon.createStatement();
                statement.executeUpdate(sqlString);
                complete = true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return complete;
    }

    public boolean clearHistory() throws SQLException {
        final Connection dbCon;
        final Statement statement;
        boolean complete = false;
        final String sqlString = "TRUNCATE TABLE MAINTENANCE_HISTORY";

        try {
            dbCon = getDBConnection();
            assert dbCon != null;
            statement = dbCon.createStatement();
            statement.executeUpdate(sqlString);
            complete = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return complete;
    }
    
    private Connection getDBConnection() {
        Connection dbConnection;
        try {
            final String passwordString = "l2u0c0a1S.root";
            final String userString = "root";
            final String connectionString = "jdbc:mysql://localhost:3306/systems?useSSL=false";
            dbConnection = DriverManager.getConnection(connectionString, userString, passwordString);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Maintenance selectSingleMaintenance(int systemId) {
        final Connection dbCon;
        final Statement statement;
        final Maintenance system = new Maintenance();
        final String sqlStringSystem = "SELECT * FROM MAINTENANCE where SYSTEM_ID = " + systemId;

        try {
            dbCon = getDBConnection();
            assert dbCon != null;
            statement = dbCon.createStatement();
            final ResultSet resultSet = statement.executeQuery(sqlStringSystem);
            
            while (resultSet.next()) {
                system.setSystemId(resultSet.getInt("SYSTEM_ID"));
                system.setId(resultSet.getInt("ID"));
                system.setBeginDate(resultSet.getString("START_DATE"));
                system.setEndDate(resultSet.getString("END_DATE"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return system;
    }
}
