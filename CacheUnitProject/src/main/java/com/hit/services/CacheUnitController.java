package com.hit.services;

import com.hit.dm.DataModel;

public class CacheUnitController<T>
{
    @SuppressWarnings("rawtypes")
	CacheUnitService unitService;

    @SuppressWarnings("rawtypes")
	public CacheUnitController()
    {
        unitService = new CacheUnitService ();
    }

    @SuppressWarnings("unchecked")
	public boolean delete(DataModel<T>[] dataModels)
    {
        return unitService.delete (dataModels);
    }

    @SuppressWarnings("unchecked")
	public boolean update(DataModel<T>[] dataModels)
    {
        return unitService.update (dataModels);
    }

    @SuppressWarnings("unchecked")
	public DataModel<T>[] get(DataModel<T>[] dataModels)
    {
        return unitService.get (dataModels);
    }
}