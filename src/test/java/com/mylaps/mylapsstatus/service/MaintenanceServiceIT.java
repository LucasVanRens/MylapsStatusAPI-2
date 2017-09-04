package com.mylaps.mylapsstatus.service;

import com.mylaps.mylapsstatus.Application;
import com.mylaps.mylapsstatus.model.Maintenance;
import java.sql.SQLException;
import java.util.List;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MaintenanceServiceIT {

    @Inject
    private MaintenanceService maintenanceService;

    @Test
    public void testSelectAllMaintenance() {
        final List<Maintenance> maintenances = maintenanceService.selectAllMaintenance();
        assertThat(maintenances.get(0).getSystemId()).isEqualTo(1);
    }

    @Test
    public void testSelectHistoryMaintenance() {
        final List<Maintenance> maintenances = maintenanceService.selectHistoryMaintenance();
        if (maintenances.size() >= 1) {
            assertThat(maintenances.get(0).getSystemId()).isEqualTo(43);
        } else {
            assertThat(1).isEqualTo(1);
        }
    }

    @Test
    public void testSingleMaintenance1() throws SQLException {
        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(20);
        assertThat(maintenance.getBeginDate()).isEqualTo("2012-12-12");
    }

    @Test
    public void testUpdateMaintenance1() throws SQLException {
        maintenanceService.updateMaintenance("20010727", "20010727", 20);
        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(20);
        maintenanceService.updateMaintenance("20121212", "20111111", 20);

        assertThat(maintenance.getBeginDate()).isEqualTo("2001-07-27");
    }

    @Test
    public void testUpdateMaintenance2() throws SQLException {
        maintenanceService.updateMaintenance("2001-07-27", "", 20);
        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(20);
        maintenanceService.updateMaintenance("20121212", "20111111", 20);

        assertThat(maintenance.getBeginDate()).isEqualTo("2012-12-12");
    }

    @Test
    public void testUpdateMaintenance3() throws SQLException {
        maintenanceService.updateMaintenance(null, null, 20);
        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(20);
        maintenanceService.updateMaintenance("20121212", "20111111", 20);

        assertThat(maintenance.getBeginDate()).isEqualTo("2012-12-12");
    }

    @Test
    public void testNewMaintenance1() throws SQLException {
        maintenanceService.newMaintenance("20010727", "20010727", 43);
        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(43);

        assertThat(maintenance.getBeginDate()).isEqualTo("2001-07-27");
        maintenanceService.deleteMaintenance(43);
    }

    @Test
    public void testNewMaintenance2() throws SQLException {
        maintenanceService.newMaintenance("", "2001-07-27", 43);
        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(43);

        assertThat(maintenance.getBeginDate()).isEqualTo(null);
        maintenanceService.deleteMaintenance(43);
    }

    @Test
    public void testNewMaintenance3() throws SQLException {
        maintenanceService.newMaintenance("null", "2001-07-27", 43);
        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(43);

        assertThat(maintenance.getBeginDate()).isEqualTo(null);
        maintenanceService.deleteMaintenance(43);
    }

    @Test
    public void testDeleteMaintenance1() throws SQLException {
        maintenanceService.newMaintenance("20010101", "20010101", 43);
        maintenanceService.deleteMaintenance(43);

        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(43);

        assertThat(maintenance.getBeginDate()).isEqualTo(null);
        maintenanceService.deleteMaintenance(43);
    }

    @Test
    public void testDeleteMaintenance2() throws SQLException {
        maintenanceService.newMaintenance("20010101", "20010101", 43);
        maintenanceService.deleteMaintenance(44);

        final Maintenance maintenance = maintenanceService.selectSingleMaintenance(43);

        assertThat(maintenance.getBeginDate()).isNotEqualTo(null);
        maintenanceService.deleteMaintenance(43);
    }

    @Test
    public void testClearHistory1() throws SQLException {
        final List<Maintenance> maintenances = maintenanceService.selectHistoryMaintenance();
        maintenanceService.newMaintenance("20030303", "20030303", 43);
        maintenanceService.clearHistory();
        final List<Maintenance> maintenances2 = maintenanceService.selectHistoryMaintenance();

        if (maintenances2.size() >= 1) {
            assertThat(maintenanceService.selectHistoryMaintenance().get(0).getBeginDate()).isEqualTo(null);
        } else {
            assertThat(1).isEqualTo(1);
        }

        for (Maintenance maintenance : maintenances) {
            maintenanceService.restoreMaintenanceHistory(maintenance.getBeginDate(), maintenance.getEndDate(), maintenance.getSystemId());
        }
    }

    @Test
    public void testRestoreMaintenanceHistory() throws SQLException {
        List<Maintenance> maintenances = maintenanceService.selectHistoryMaintenance();
        maintenanceService.clearHistory();

        for (Maintenance maintenance : maintenances) {
            maintenanceService.restoreMaintenanceHistory(maintenance.getBeginDate(), maintenance.getEndDate(), maintenance.getSystemId());
        }

        maintenances = maintenanceService.selectHistoryMaintenance();
        assertThat(maintenances.get(0).getSystemId()).isEqualTo(43);
    }

    @Test
    public void testSelectSingleHistoryMaintenance() {
        final List<Maintenance> maintenances = maintenanceService.selectSingleHistoryMaintenance(43);
        assertThat(maintenances.get(0).getSystemId()).isEqualTo(43);
    }
}