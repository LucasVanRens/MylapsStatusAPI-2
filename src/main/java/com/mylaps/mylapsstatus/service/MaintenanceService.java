package com.mylaps.mylapsstatus.service;

import com.mylaps.mylapsstatus.DAO.MaintenanceDAO;
import com.mylaps.mylapsstatus.model.Maintenance;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MaintenanceService {

    private final MaintenanceDAO maintenanceDAO;

    public MaintenanceService(final MaintenanceDAO maintenanceDAO) {
        this.maintenanceDAO = maintenanceDAO;
    }

    public boolean newMaintenance(final String startDate, final String endDate, final int systemId) throws SQLException {
        return maintenanceDAO.newMaintenance(startDate, endDate, systemId);
    }

    public boolean updateMaintenance(final String startDate, final String endDate, final int systemId) throws SQLException {
        return maintenanceDAO.updateMaintenance(startDate, endDate, systemId);
    }

    public boolean deleteMaintenance(final int systemId) throws SQLException {
        return maintenanceDAO.deleteMaintenance(systemId);
    }

    public List<Maintenance> selectAllMaintenance() {
        return maintenanceDAO.selectAllMaintenance();
    }

    public Maintenance selectSingleMaintenance(int systemId) {
        return maintenanceDAO.selectSingleMaintenance(systemId);
    }

    public boolean clearHistory() throws SQLException {
        return maintenanceDAO.clearHistory();
    }

    public List<Maintenance> selectHistoryMaintenance() {
        return maintenanceDAO.selectHistoryMaintenance();
    }

    public boolean restoreMaintenanceHistory(final String startDate, final String endDate, final int systemId) throws SQLException {
        return maintenanceDAO.restoreMaintenanceHistory(startDate, endDate, systemId);
    }

    public List<Maintenance> selectSingleHistoryMaintenance(final int systemId) {
        return maintenanceDAO.selectSingleHistoryMaintenance(systemId);
    }
}