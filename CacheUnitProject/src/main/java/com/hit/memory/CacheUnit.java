package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import com.hit.util.DataStat;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class CacheUnit<T>
extends java.lang.Object 
{
	
	com.hit.algorithm.IAlgoCache<java.lang.Long, DataModel<T>> alg;
	IDao<java.io.Serializable,DataModel<T>> dao;
	
	DataStat dataStats;
	
	public CacheUnit
	(com.hit.algorithm.IAlgoCache<java.lang.Long, DataModel<T>> algo,
            IDao<java.io.Serializable,DataModel<T>> dao)
	{
		this.alg = algo;
		this.dao = dao;
		
		dataStats = DataStat.getInstance ();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel<T>[] getDataModels(java.lang.Long[] ids)
	{

		ArrayList<DataModel<T>> listOfEntitys = new ArrayList<>();
		DataModel<T> entity;

		ArrayList<Long> arrayIds = new ArrayList<>();

		ArrayList<Long> removeId = new ArrayList<>();

		for(Long l: ids)
		{
			arrayIds.add(l);
			dataStats.addModelsRequest ();
		}


		for(int i = 0; i < arrayIds.size(); i++)
		{
			entity = (DataModel<T>) alg.getElement(arrayIds.get(i));

			if(entity != null)
			{
				removeId.add(arrayIds.get(i));
				listOfEntitys.add(entity);
			}
		}

		for(Long id: removeId)
		{
			arrayIds.remove(id);
		}


		for (int i = 0; i < arrayIds.size(); i++)
		{
			Long id = arrayIds.get(i);
			DataModel<T> tempData;
			tempData = (DataModel<T>) dao.find(id);

			if (tempData != null) {
				//dao.delete(tempData);
				listOfEntitys.add(tempData);
				removeId.add(id);
			}

			DataModel resultObject = null;

			if(tempData != null)
			{
				resultObject = (DataModel) alg.putElement(tempData.getDataModelId(), tempData);
			}
			if (resultObject != null)
			{
				dao.save(resultObject);
				dataStats.addModelSwap ();
			}

		}


		DataModel<T>[] arrOfDataModels = new DataModel[listOfEntitys.size()];

		for (int i = 0; i < listOfEntitys.size(); i++)
		{
			arrOfDataModels[i] = listOfEntitys.get(i);
		}

		return arrOfDataModels;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateFile(DataModel model)
	{
		dao.save (model);
	}

}