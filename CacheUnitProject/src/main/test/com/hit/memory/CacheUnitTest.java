package com.hit.memory;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

@SuppressWarnings("unused")
public class CacheUnitTest
{

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
    public void getDataModels()
    {
        LRUAlgoCacheImpl<Long, DataModel<Integer>> lru = new LRUAlgoCacheImpl<>(25);
        DaoFileImpl<Integer> daoFile = new DaoFileImpl<>("output.txt");

		CacheUnit<Integer> cacheUnit = new CacheUnit(lru, daoFile);

        for (int i = 0; i < 27; i++)
        {
          lru.putElement(Long.valueOf(i),new DataModel(Long.valueOf(i),i));
        }

        for (int i = 0; i < 150; i++)
        {
           int integer = i;
           daoFile.save(new DataModel(Long.valueOf(i), integer));
        }

        Long[] ids = {Long.valueOf(8), Long.valueOf(66), 
        		Long.valueOf(10), Long.valueOf(12),
        		Long.valueOf(1), Long.valueOf(12)};
        DataModel<Integer>[] dataModels = null;

        try
        {
            dataModels = cacheUnit.getDataModels(ids);
        } 
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Here are our Pages (DataModels) : \n");
        for (DataModel model: dataModels)
        {
            System.out.println("Page ID = " + model.getDataModelId() + " , Page content = "+model.getContent());
        }


    }

}