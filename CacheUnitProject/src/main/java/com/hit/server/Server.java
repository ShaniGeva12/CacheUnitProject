package com.hit.server;

import com.hit.services.CacheUnitController;
import com.hit.util.DataStat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Observer
{
    private ServerSocket socket;
    private boolean serverIsRunning;
    
    @SuppressWarnings("unused")
	private boolean startCommand = false;
    
    @SuppressWarnings("rawtypes")
	CacheUnitController unitController;
    
    Executor threadPool;

    DataStat dataStats;

    @SuppressWarnings("rawtypes")
	public Server()
    {
        unitController = new CacheUnitController ();
        threadPool = Executors.newFixedThreadPool (100);
        
        dataStats = DataStat.getInstance ();
    }

    
    @SuppressWarnings("rawtypes")
	void start()
    {
        serverIsRunning = true;

        try
        {
            socket = new ServerSocket (1234);
        } catch (IOException e)
        {
            e.printStackTrace ();
        }
        Socket accept = null;

        while(serverIsRunning)
        {
            try
            {
                System.out.println ("Waiting For Client");
                accept = socket.accept ();
                dataStats.addRequest ();
            } catch (IOException e)
            {
                e.printStackTrace ();
            }

            Thread thread = new Thread
                    (new HandleRequest(accept,unitController));
            threadPool.execute (thread);
        }
        ((ExecutorService )threadPool).shutdown();

    }

    @Override
    public void update(Observable o, Object arg)
    {
        String inputCommand = (String) arg;

        if(inputCommand.equals ("Start"))
        {
            System.out.println ("Starting Server...");
            start ();
        }

        else
        {
        	if(serverIsRunning) 
        	{
        		System.out.println ("Shutdown Server...");
                serverIsRunning = false;
                try
                {
                    socket.close ();
                } catch (IOException e)
                {
                    e.printStackTrace ();
                }
                System.out.println ("Shutdown Server...");
        	}
        	else {
        		System.out.println("Server not running");
        	}
            
        }

    }
}