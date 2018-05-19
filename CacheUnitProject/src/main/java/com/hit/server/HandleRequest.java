package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;

public class HandleRequest<T> implements Runnable
{

    Socket socket;
    CacheUnitController unitController;

    Request<DataModel<T>[]> socketRequest;

    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    Gson gson;

    Type ref;


    public HandleRequest(Socket s, CacheUnitController controller)
    {
        this.socket = s;
        this.unitController = controller;

        try
        {
            inputStream = new ObjectInputStream (socket.getInputStream ());
            outputStream = new ObjectOutputStream (socket.getOutputStream ());
        } catch (IOException e)
        {
            e.printStackTrace ();
        }

        gson = new GsonBuilder ().create ();

    }


    @SuppressWarnings({ "rawtypes" })
	@Override
    public void run()
    {
       @SuppressWarnings("rawtypes")
	String inputString;

        DataModel[] model = null;
        String command;
        DataModel<T>[] body;
        try
        {
            ref = new TypeToken<Request<DataModel<T>[]>> (){}.getType();

            inputString = (String) inputStream.readObject ();

            socketRequest = new Gson().fromJson(inputString, ref);

        } catch (IOException e)
        {
            e.printStackTrace ();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace ();
        }

        Map headers = socketRequest.getHeaders ();

        command = (String) headers.get ("action");

        body = socketRequest.getBody ();


        if(command.equals ("GET"))
        {

            DataModel[] dataModels = unitController.get (body);

            for(DataModel dataModel: dataModels)
            {
                try
                {
                    String outputGson = gson.toJson (dataModel);
                    outputStream.writeObject (outputGson);
                } catch (IOException e)
                {
                    e.printStackTrace ();
                }
            }


        }else if(command.equals ("DELETE"))
        {

            boolean delete = unitController.delete (body);

            try
            {
                outputStream.writeObject (delete);
            } catch (IOException e)
            {
                e.printStackTrace ();
            }

        }else if(command.equals ("UPDATE"))
        {

            boolean update = unitController.update (body);

            try
            {
                outputStream.writeObject (update);
            } catch (IOException e)
            {
                e.printStackTrace ();
            }

        }else
        {
            try
            {
                outputStream.writeObject ("Unknown Action");
                outputStream.flush ();
            } catch (IOException e)
            {
                e.printStackTrace ();
            }
        }



    }


    private void writeToOutputStream(String s)
    {
        try
        {
            outputStream.writeObject (s);
        } catch (IOException e)
        {
            e.printStackTrace ();
        }
    }
}