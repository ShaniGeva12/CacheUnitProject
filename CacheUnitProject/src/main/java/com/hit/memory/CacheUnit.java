package com.hit.memory;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;

public class CacheUnit<T>
extends java.lang.Object 
{
	
	com.hit.algorithm.IAlgoCache<java.lang.Long, DataModel<T>> alg;
	IDao<java.io.Serializable,DataModel<T>> dao;
	
	public CacheUnit
	(com.hit.algorithm.IAlgoCache<java.lang.Long, DataModel<T>> algo,
            IDao<java.io.Serializable,DataModel<T>> dao)
	{
		this.alg = algo;
		this.dao = dao;
	}
	
	public DataModel<T>[] getDataModels(java.lang.Long[] ids) 
			throws java.lang.ClassNotFoundException, java.io.IOException 
	{
		//List<DataModel<T>[]> pages = new ArrayList<DataModel<T>[]>(10);
		@SuppressWarnings("unchecked")
		DataModel<T>[] dmArray=new DataModel[ids.length];
		int dmsindex=0;
		DataModel<T> dm;
		
		for(int i=0; i<ids.length;i++)
		{
			if(alg.getElement(ids[i])==null)		
				//the DataModels is not on the cache
			{
				dm=dao.find(ids[i]);
				if(dm!=null)						
					// == if we found this id in the Dao
				{
				dmArray[dmsindex++]=dm;				
				//insert to the DataModels array
				if(!alg.IsFull()) 
				{		
					alg.putElement(ids[i], dm);	
					//if the cache is not full, 
					//just input it to the cache without any problems.
				}
				else 
				{								
					//==the cache is full- we need to do replacements
						DataModel<T> moveToDao=alg.putElement(ids[i], dm);		
						//takes the output from cache and save it on the Dao
						dao.save(moveToDao);
				}
				}
			}
			else
				dmArray[dmsindex++]=alg.getElement(ids[i]);		
			//if the cache contains the id from the "ids" array
		}
		return dmArray;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateFile(DataModel model)
	{
		dao.save (model);
	}

	
}
