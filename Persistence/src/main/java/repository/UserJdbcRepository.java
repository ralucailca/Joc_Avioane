package repository;

import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class UserJdbcRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public UserJdbcRepository(Properties props){
        logger.info("Initializing CorectorJdbcRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }


    public User findUserPass(String user, String pass) {
        logger.traceEntry("finding agent with user "+user+" and password"+pass);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Users where username=? and password=?")){
            preStmt.setString(1,user);
            preStmt.setString(2,pass);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    String id = result.getString("id");
                    User corector=new User(user, pass);
                    corector.setId(id);
                    logger.traceExit(corector);
                    return corector;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No agent found with id user "+user+" and password"+pass);
        return null;
    }

    public User findOne(String s) {
        logger.traceEntry("finding agent with id {} ",s);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from Users where id=?")){
            preStmt.setString(1,s);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    String id = result.getString("id");
                    String user = result.getString("username");
                    String pass = result.getString("password");
                    User corector=new User(user, pass);
                    corector.setId(id);
                    logger.traceExit(corector);
                    return corector;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No corector found with id {}", s);
        return null;
    }
}
