//package fptu.edu.vn.training;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class DatabaseConnectionTest {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Test
//    void testDatabaseConnection() {
//        System.out.println("🔍 Testing MySQL Database Connection...");
//
//        assertNotNull(dataSource, "DataSource is NULL!");
//
//        try (Connection connection = dataSource.getConnection()) {
//            assertTrue(connection.isValid(2), "Connection is not valid!");
//
//            DatabaseMetaData metaData = connection.getMetaData();
//
//            System.out.println("Connection successful!");
//            System.out.println("Database Product: " + metaData.getDatabaseProductName());
//            System.out.println("Database Version: " + metaData.getDatabaseProductVersion());
//            System.out.println("JDBC URL: " + metaData.getURL());
//            System.out.println("Username: " + metaData.getUserName());
//            System.out.println("Driver: " + metaData.getDriverName());
//
//        } catch (Exception e) {
//            fail("Failed to connect to database: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void testSimpleQuery() {
//        System.out.println("Testing simple query...");
//
//        try {
//            Integer result = jdbcTemplate.queryForObject(
//                    "SELECT 1",
//                    Integer.class
//            );
//
//            assertEquals(1, result, "Query returned wrong result!");
//            System.out.println("Query test passed! Result: " + result);
//
//        } catch (Exception e) {
//            fail("Query failed: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void testDatabaseExists() {
//        System.out.println("Checking if database 'garage_management_system' exists...");
//
//        try {
//            String currentDatabase = jdbcTemplate.queryForObject(
//                    "SELECT DATABASE()",
//                    String.class
//            );
//
//            System.out.println("Current database: " + currentDatabase);
//            assertEquals("garage_management_system_v1", currentDatabase,
//                    "Wrong database! Expected 'garage_showroom'");
//
//        } catch (Exception e) {
//            fail("Failed to check database: " + e.getMessage());
//        }
//    }
//
//    @Test
//    void testShowTables() {
//        System.out.println("Listing all tables in database...");
//
//        try {
//            var tables = jdbcTemplate.queryForList(
//                    "SHOW TABLES",
//                    String.class
//            );
//
//            System.out.println("Found " + tables.size() + " table(s):");
//            tables.forEach(table -> System.out.println("  - " + table));
//
//        } catch (Exception e) {
//            fail("Failed to list tables: " + e.getMessage());
//        }
//    }
//}