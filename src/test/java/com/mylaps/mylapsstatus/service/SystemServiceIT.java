package com.mylaps.mylapsstatus.service;

import com.mylaps.mylapsstatus.Application;
import com.mylaps.mylapsstatus.model.SystemObject;
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
public class SystemServiceIT {

    @Inject
    private SystemService systemService;

    @Test
    public void testOneSystem() throws SQLException {
        final SystemObject systemObject = systemService.oneSystem("bibtag");
        assertThat(systemObject.getSystemName()).isEqualTo("bibtag");
    }

    @Test
    public void testSelectAllSystems() throws SQLException {
        final List<SystemObject> systems = systemService.selectAllSystems();
        assertThat(systems.get(0).getSystemName()).isEqualTo("ccnet");
    }

    @Test
    public void testUpdateSystem1() throws SQLException {
        systemService.updateSystem(1, "up");
        final SystemObject systemObject = systemService.oneSystem("bibtag");
        systemService.updateSystem(1, "down");

        assertThat(systemObject.getSystemStatus()).isEqualTo("up");
    }

    @Test
    public void testUpdateSystem2() throws SQLException {
        systemService.updateSystem(1, null);
        final SystemObject systemObject = systemService.oneSystem("bibtag");
        systemService.updateSystem(1, "down");

        assertThat(systemObject.getSystemStatus()).isEqualTo("down");
    }

    @Test
    public void testNewSystem1() throws SQLException {
        systemService.createNewSystem("testName", "up");
        final SystemObject systemObject = systemService.oneSystem("testName");

        assertThat(systemObject.getSystemName()).isEqualTo("testName");
        systemService.deleteSystem("testName");
    }

    @Test
    public void testNewSystem2() throws SQLException {
        systemService.createNewSystem("testName", "");
        final SystemObject systemObject = systemService.oneSystem("testName");

        assertThat(systemObject.getSystemName()).isNotEqualTo("testName");
        systemService.deleteSystem("testName");
    }

    @Test
    public void testNewSystem3() throws SQLException {
        systemService.createNewSystem("", "up");
        final SystemObject systemObject = systemService.oneSystem("testName");

        assertThat(systemObject.getSystemStatus()).isNotEqualTo("up");
        systemService.deleteSystem("testName");
    }

    @Test
    public void testNewSystem4() throws SQLException {
        systemService.createNewSystem("testName", null);
        final SystemObject systemObject = systemService.oneSystem("testName");

        assertThat(systemObject.getSystemName()).isNotEqualTo("testName");
        systemService.deleteSystem("testName");
    }

    @Test
    public void testDeleteSystem1() throws SQLException {
        systemService.createNewSystem("testName", "up");
        systemService.deleteSystem("testName");
        final SystemObject systemObject = systemService.oneSystem("testName");
        
        assertThat(systemObject.getSystemName()).isEqualTo(null);
    }
    
    @Test
    public void testDeleteSystem2() throws SQLException {
        systemService.createNewSystem("testName", "up");
        systemService.deleteSystem(null);
        final SystemObject systemObject = systemService.oneSystem("testName");
        
        assertThat(systemObject.getSystemName()).isEqualTo("testName");
        systemService.deleteSystem("testName");
    }
}
