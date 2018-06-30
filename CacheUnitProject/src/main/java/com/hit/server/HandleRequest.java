package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.util.DataStat;
import com.hit.services.CacheUnitController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;

public class HandleRequest<T> implements Runnable
{

    private Socket socket;
    @SuppressWarnings("rawtypes")
	private CacheUnitController unitController;

    private Request<DataModel<T>[]> socketRequest;

    DataStat dataStats;

    public HandleRequest(Socket s, @SuppressWarnings("rawtypes") CacheUnitController controller)
    {
        this.socket = s;
        this.unitController = controller;

        dataStats = DataStat.getInstance ();

    }


    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	@Override
    public void run()
    {
        boolean statsRequest = false;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        Gson gson = new GsonBuilder ().create ();
        try
        {
            inputStream = new ObjectInputStream (socket.getInputStream ());
            outputStream = new ObjectOutputStream (socket.getOutputStream ());
        } catch (IOException e)
        {
            e.printStackTrace ();
        }
        String inputString;

        DataModel[] model = null;
        String command;
        DataModel<T>[] body;

        try
        {
            Type ref = new TypeToken<Request<DataModel<T>[]>> ()
            {
            }.getType ();

            inputString = (String) inputStream.readObject ();

            if(inputString.equals ("stats"))
            {
                statsRequest = true;
            }else
            {
                socketRequest = new Gson().fromJson(inputString, ref);
            }

        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace ();
        }


        if(!statsRequest)
        {
            Map headers = socketRequest.getHeaders ();

            command = (String) headers.get ("action");

            body = socketRequest.getBody ();


            if(command.equals ("GET"))
            {

                DataModel[] dataModels = unitController.get (body);

                String gsonString = gson.toJson (dataModels);
                try
                {
                    outputStream.writeObject (gsonString);
                    outputStream.flush ();
                } catch (IOException e)
                {
                    e.printStackTrace ();
                }


            }else if(command.equals ("DELETE"))
            {

                boolean delete = unitController.delete (body);

                String s = gson.toJson (delete);

                try
                {
                    outputStream.writeObject (s);
                    outputStream.flush ();
                } catch (IOException e)
                {
                    e.printStackTrace ();
                }

            }else if(command.equals ("UPDATE"))
            {

                boolean update = unitController.update (body);

                String s = gson.toJson (update);

                try
                {
                    outputStream.writeObject (s);
                    outputStream.flush ();
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
        }else
        {
            String jsonString = gson.toJson (dataStats);
            try
            {

                outputStream.writeObject (jsonString);
            } catch (IOException e)
            {
                e.printStackTrace ();
            }
        }


        try
        {
            outputStream.close ();
            inputStream.close ();

        } catch (IOException e)
        {
            e.printStackTrace ();
        }

    }

}
   
       