package com.mylaps.mylapsstatus.DAO;

import com.mylaps.mylapsstatus.model.Maintenance;
import com.mylaps.mylapsstatus.model.SystemObject;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SystemDAO {
    
    public SystemObject selectOneSystem(final String systemName) {
        final Connection dbCon;
        final Statement statement;
        final Maintenance system = new Maintenance();
        final String sqlStringSystem = "select * FROM SYSTEMS S LEFT JOIN MAINTENANCE M ON S.ID = M.SYSTEM_ID WHERE S.NAME = '" + systemName+ "'";

        try {
            dbCon = getDBConnection();
            assert dbCon != null;
            statement = dbCon.createStatement();
            final ResultSet resultSet = statement.executeQuery(sqlStringSystem);
            
            while (resultSet.next()) {
                system.setSystemId(resultSet.getInt("ID"));
                system.setSystemName(resultSet.getString("NAME"));
                system.setSystemStatus(resultSet.getString("STATUS"));
                system.setBeginDate(resultSet.getString("START_DATE"));
                system.setEndDate(resultSet.getString("END_DATE"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return system;
    }

    public List<SystemObject> selectAllSystems() {
        final Connection dbCon;
        final Statement statement;
        final List<SystemObject> systems = new ArrayList<>();
        final String sqlStringSystem = "select * FROM SYSTEMS S LEFT JOIN MAINTENANCE M ON S.ID = M.SYSTEM_ID";

        try {
            dbCon = getDBConnection();
            assert dbCon != null;
            statement = dbCon.createStatement();
            final ResultSet resultSet = statement.executeQuery(sqlStringSystem);

            while (resultSet.next()) {
                Maintenance system = new Maintenance();
                system.setSystemId(resultSet.getInt("ID"));
                system.setSystemName(resultSet.getString("NAME"));
                system.setSystemStatus(resultSet.getString("STATUS"));
                system.setBeginDate(resultSet.getString("START_DATE"));
                system.setEndDate(resultSet.getString("END_DATE"));
                systems.add(system);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return systems;
    }

    public boolean createNewSystem(String systemName, String systemStatus) {
        final Connection dbCon;
        final Statement statement;
        boolean complete = false;
        final String sqlString = "INSERT INTO SYSTEMS(NAME, STATUS) VALUES ('" + systemName + "', '" + systemStatus + "')";
        if (systemName != null && !systemName.isEmpty() && systemStatus != null && !systemStatus.isEmpty()) {
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

    public boolean deleteSystem(String systemName) {
        final Connection dbCon;
        final Statement statement;
        boolean complete = false;
        final String sqlString = "DELETE FROM SYSTEMS WHERE NAME = '" + systemName + "'";
        if (systemName != null && !systemName.isEmpty()) {
            try {
                MaintenanceDAO maintenanceDAO = new MaintenanceDAO();
                maintenanceDAO.deleteMaintenance(selectOneSystem(systemName).getSystemId());

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

    public boolean updateSystem(int systemId, String systemStatus) {
        final Connection dbCon;
        final Statement statement;
        boolean complete = false;
        final String sqlString = "UPDATE SYSTEMS SET STATUS = '" + systemStatus + "' WHERE ID = '" + systemId + "'";
        if (systemStatus != null && !systemStatus.isEmpty() && systemId != 0) {
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
}