package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionPool {
    int connections;

    public int remainingConnections;
    List<Connection> freeConnections = new ArrayList<Connection>();
    public ConnectionPool(int n){
        connections = n;
        remainingConnections = n;
    }

    public Connection getConnection() throws Exception{
        if(this.remainingConnections == 0) {
            throw new Exception("no connection available!");
        }

        this.remainingConnections--;
        if(!freeConnections.isEmpty()){
            Connection newConn = freeConnections.get(freeConnections.size()-1);

            freeConnections.remove(freeConnections.size()-1);
            return newConn;
        }

        return new Connection(this.remainingConnections);

    }

    public void releaseConnection(Connection connection) {
        this.remainingConnections++;
        freeConnections.add(connection);
        return;
    }
}
