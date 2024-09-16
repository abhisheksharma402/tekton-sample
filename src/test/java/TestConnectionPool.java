import org.example.Connection;
import org.example.ConnectionPool;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestConnectionPool {

    @Test
    public void testGetAllConnections() throws Exception {
        ConnectionPool testPool = new ConnectionPool(10);
        Connection[] localConn = new Connection[10];
        int poolCount = 10;

        // Get all connections
        for (int i = 0; i < poolCount; i++) {
            localConn[i] = testPool.getConnection();
        }

        // Assert that all connections are used up
        assertEquals(0, testPool.remainingConnections);
    }

    @Test
    public void testGetExtraConnectionThrowsException() {
        ConnectionPool testPool = new ConnectionPool(10);

        // Get all connections
        for (int i = 0; i < 10; i++) {
            try {
                testPool.getConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Try one more connection, should throw exception
        Exception exception = assertThrows(Exception.class, testPool::getConnection);
        assertEquals("no connection available!", exception.getMessage());
    }

    @Test
    public void testReleaseAndClaimBack() throws Exception {
        ConnectionPool testPool = new ConnectionPool(10);
        Connection[] localConn = new Connection[10];

        // Get all connections
        for (int i = 0; i < 10; i++) {
            localConn[i] = testPool.getConnection();
        }

        // Release and claim back
        localConn[3].releaseConnection(testPool);
        localConn[3] = testPool.getConnection();

        // Assert that all connections are used up again
        assertEquals(0, testPool.remainingConnections);

        // Try to get an extra, should throw exception
        Exception exception = assertThrows(Exception.class, testPool::getConnection);
        assertEquals("no connection available!", exception.getMessage());
    }

    @Test
    public void testReleaseAllConnections() throws Exception {
        ConnectionPool testPool = new ConnectionPool(10);
        Connection[] localConn = new Connection[10];

        // Get all connections
        for (int i = 0; i < 10; i++) {
            localConn[i] = testPool.getConnection();
        }

        // Release all connections
        for (int i = 0; i < 10; i++) {
            testPool.releaseConnection(localConn[i]);
        }

        // Assert that all connections are released
        assertEquals(10, testPool.remainingConnections);
    }

    @Test
    public void testHogConnectionsAfterRelease() throws Exception {
        ConnectionPool testPool = new ConnectionPool(10);
        Connection[] localConn = new Connection[10];

        // Release all connections and then get all connections again
        for (int i = 0; i < 10; i++) {
            localConn[i] = testPool.getConnection();
        }
        for (int i = 0; i < 10; i++) {
            testPool.releaseConnection(localConn[i]);
        }

        for (int i = 0; i < 10; i++) {
            localConn[i] = testPool.getConnection();
        }

        // Assert that all connections are used up again
        assertEquals(0, testPool.remainingConnections);

        // Try to get an extra connection, should throw exception
        Exception exception = assertThrows(Exception.class, testPool::getConnection);
        assertEquals("no connection available!", exception.getMessage());
    }
}
