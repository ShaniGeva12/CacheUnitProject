package com.hit.services;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;
import com.hit.util.DataStat;

import javax.xml.crypto.Data;
import java.io.IOException;

@SuppressWarnings("unused")
public class CacheUnitService<T>
{

    @SuppressWarnings({ "rawtypes" })
	private CacheUnit cacheUnit;
	private DataStat dataStats;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public CacheUnitService()
    {
    	dataStats = DataStat.getInstance ();
        LRUAlgoCacheImpl<T, DataModel<T>> lru = new LRUAlgoCacheImpl<>(3);
        DaoFileImpl<T> daoFile = new DaoFileImpl<>("out.txt");

        this.cacheUnit = new CacheUnit (lru,daoFile);

        for (int i = 0; i < 150; i++)
        {
            Integer integer = i;
           daoFile.save(new DataModel(Long.valueOf(i), integer));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean delete(DataModel<T>[] dataModels)
    {
    	boolean isDelete = false;
    	
        DataModel[] returnModels = null;
        Long[] ids = new Long[dataModels.length];

        for (int i = 0; i <dataModels.length ; i++)
        {
            ids[i] = dataModels[i].getDataModelId ();
        }

        returnModels = cacheUnit.getDataModels (ids);

        for(DataModel model: returnModels)
        {
            model.setContent (null);
        }

        if(returnModels.length > 0)
        {
            isDelete = true;
        }

        return isDelete;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean update(DataModel<T>[] dataModels)
    {
    	boolean isUpdate = false;
    	 
        DataModel[] returnModels = null;
        Long[] ids = new Long[dataModels.length];

        for (int i = 0; i <dataModels.length ; i++)
        {
            ids[i] = dataModels[i].getDataModelId ();
        }

        returnModels = cacheUnit.getDataModels (ids);

        for (int i = 0; i <dataModels.length ; i++)
        {
            for (int j = 0; j <returnModels.length ; j++)
            {
                if(dataModels[i].getDataModelId ().equals (returnModels[j].getDataModelId ()))
                {
                    returnModels[j].setContent (dataModels[i].getContent ());
                    j = returnModels.length+1;
                }
            }
        }
        
        for(DataModel model: dataModels)
        {
            cacheUnit.updateFile (model);
        }
        
        if(dataModels.length > 0)
        {
            isUpdate = true;
        }

        return isUpdate;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel<T>[] get(DataModel<T>[] dataModels)
    {
        Long[] ids = new Long[dataModels.length];
        DataModel[] models = null;

        for (int i = 0; i < dataModels.length; i++)
        {
            ids[i] = dataModels[i].getDataModelId ();
        }

        models = cacheUnit.getDataModels (ids);

        return models;
    }
}