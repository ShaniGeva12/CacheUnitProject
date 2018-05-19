package com.hit.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Scanner;

@SuppressWarnings("unused")
public class CLI extends Observable implements Runnable
{

    private OutputStream outputStream;
    private InputStream inputStream;
    Scanner scanner;
    PrintStream printStream;

    public CLI(java.io.InputStream in, java.io.OutputStream out)
    {
        this.inputStream = in;
        this.outputStream = out;

        scanner = new Scanner (inputStream);
        printStream = new PrintStream (outputStream);

    }

    @Override
    public void run()
    {
        String command;

        while(true)
        {

            printStream.println ("Please Enter Your Command: ");
            command = scanner.nextLine ();


            if(command.equals ("Start") || command.equals ("start") || command.equals ("START"))
            {
                setChanged ();
                notifyObservers ("Start");
                clearChanged ();
            }
            else if(command.equals ("Shutdown") || command.equals ("shutdown") || command.equals ("SHUTDOWN"))
            {

                setChanged ();
                notifyObservers ("Shutdown");
            }
            else
            {
                printStream.println ("Unknown Command");
                command = "";
            }


        }
    }
}