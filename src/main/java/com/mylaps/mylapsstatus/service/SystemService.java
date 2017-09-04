package com.mylaps.mylapsstatus.service;

import com.mylaps.mylapsstatus.DAO.SystemDAO;
import com.mylaps.mylapsstatus.model.SystemObject;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SystemService {

   private final SystemDAO systemDAO;
    
    public SystemService(final SystemDAO systemDAO) {
        this.systemDAO = systemDAO;
    }

    public SystemObject oneSystem(final String systemName) throws SQLException {
        return systemDAO.selectOneSystem(systemName);
    }

    public List<SystemObject> selectAllSystems() throws SQLException {
        return systemDAO.selectAllSystems();
    }

    public boolean createNewSystem(final String systemName, final String systemStatus) throws SQLException {
        return systemDAO.createNewSystem(systemName, systemStatus);
    }

    public boolean deleteSystem(final String systemName) throws SQLException {
        return systemDAO.deleteSystem(systemName);
    }

    public boolean updateSystem(final int systemId, final String systemStatus) throws SQLException {
        return systemDAO.updateSystem(systemId, systemStatus);
    }
}
