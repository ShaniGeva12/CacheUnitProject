package com.hit.dm;

public class DataModel<T>
extends java.lang.Object
implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private T content;

	public DataModel(java.lang.Long id, T content) 
	{
		this.id = id;
		this.content = content;
	}
	
	public int hashCode()
	{
		return super.hashCode();
		
		//return this.id.intValue(); //hashCode is the ID number
	}
	
	public boolean equals(java.lang.Object obj)
	{
		if(obj.hashCode() == this.hashCode())
			return true;
		return false;
	}
	
	public java.lang.String toString()
	{
		return "Data Model ID = " + this.id.toString() ;
	}
	
	public java.lang.Long getDataModelId()
	{
		return this.id;
	}
	
	public void setDataModelId(java.lang.Long id)
	{
		this.id = id;
	}
	
	public T getContent()
	{
		return this.content;
	}
	
	public void setContent(T content)
	{
		this.content = content; 
	}
}
