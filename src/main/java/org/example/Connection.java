package org.example;

public class Connection {
    int connId;

    Connection(int id){
        connId = id;
    }
    public Connection getConnection(ConnectionPool connectionPool) throws Exception {
        return connectionPool.getConnection();
    }

    public void releaseConnection(ConnectionPool connectionPool) {
        connectionPool.releaseConnection(this);
    }

}
