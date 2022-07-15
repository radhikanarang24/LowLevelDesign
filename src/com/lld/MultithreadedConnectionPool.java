package com.lld;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
https://www.dbtsai.com/blog/2013/java-concurrent-dynamic-object-pool-for-non-thread-safe-objects-using-blocking-queue/

https://leetcode.com/discuss/interview-question/990739/Design-Connection-Pool-Multithreaded
*/
public class MultithreadedConnectionPool {
    private BlockingQueue<Connection> pool;
    private int maxPoolSize;
    private int initialPoolSize;
    private int currentPoolSize;

    private String dbUrl, dbUser;
    private String dbPassword;

    MultithreadedConnectionPool(int maxSize,int initialSize,String dbUrl, String dbUser, String dbPassword, String driverClassName) throws SQLException, ClassNotFoundException {
        if(initialSize>maxSize || initialSize<1 || maxSize<1)
        {
            throw new IllegalArgumentException("Invalid arguments");
        }
        this.maxPoolSize=maxSize>0?maxSize:10;
        this.initialPoolSize=initialSize;
        this.dbUrl=dbUrl;
        this.dbUser=dbUser;
        this.dbPassword=dbPassword;

        this.pool=new LinkedBlockingQueue<Connection>(maxPoolSize);
        initPooledConnections(driverClassName);
    }

    private void initPooledConnections(String driverClassName) throws ClassNotFoundException, SQLException
    {
        Class.forName(driverClassName);
        for(int i=0;i<initialPoolSize;i++)
        {
            openPoolConnection();
        }
    }

    private synchronized void openPoolConnection() throws SQLException
    {
        if(currentPoolSize==maxPoolSize)
        {
            return;
        }

        Connection con= DriverManager.getConnection(dbUrl,dbUser,dbPassword);
        pool.offer(con);
        currentPoolSize++;
    }

    private synchronized Connection borrowConnection() throws SQLException,InterruptedException
    {
        if(pool.peek()==null && currentPoolSize<maxPoolSize)
            openPoolConnection();

        return pool.take();
    }


    public static void main(String[] args) {
    }
}