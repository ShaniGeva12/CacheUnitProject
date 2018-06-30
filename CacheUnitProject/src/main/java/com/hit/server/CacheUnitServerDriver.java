package com.hit.server;

import com.hit.util.CLI;
import com.hit.util.DataStat;

@SuppressWarnings("unused")
public class CacheUnitServerDriver
{
    public static void main(String[] args)
    {
        CLI cli = new CLI(System.in, System.out);
        Server server = new Server();
        cli.addObserver(server);
        Thread thread = new Thread (cli);


        thread.start ();
    }
}