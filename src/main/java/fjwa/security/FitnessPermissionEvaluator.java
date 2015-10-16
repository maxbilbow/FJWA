package fjwa.security;

import click.rmx.debug.OnlineBugger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.sql.DataSource;
import java.io.Serializable;

/**
 * Created by bilbowm on 29/09/2015.
 */

public class FitnessPermissionEvaluator implements PermissionEvaluator {

    private final DataSource datasource;

    private final OnlineBugger debug;

    public DataSource getDatasource() {
        return datasource;
    }

    public FitnessPermissionEvaluator(DataSource datasource, OnlineBugger debug)
    {
        this.datasource = datasource;
        this.debug = debug;
    }
//    public void setDatasource(DataSource datasource) {
//        this.datasource = datasource;
//    }

    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        System.err.println("FitnessPermissionEvaluator::hasPermission(1)");
//        debug.addLog("FitnessPermissionEvaluator::hasPermission(1)");
        JdbcTemplate template = new JdbcTemplate(datasource);

        Object [] args = {((User)auth.getPrincipal()).getUsername(),
                targetDomainObject.getClass().getName(),
                permission.toString()};

        int count = template.queryForObject("select count(*) from permissions p where " +
                "p.username = ? and p.target = ? and p.permission = ?", args, Integer.class);

        if(count == 1) {
            debug.addLog("Access granted: " + args[0]);
            return true;
        } else {
            debug.addLog("Access denied: " + args[0]);
            return false;
        }

    }

    public boolean hasPermission(Authentication arg0, Serializable id,
                                 String type, Object permission) {

        System.err.println("FitnessPermissionEvaluator::hasPermission(2)");
        debug.addFunException("FitnessPermissionEvaluator::hasPermission(2)");
        return false;
    }

}
