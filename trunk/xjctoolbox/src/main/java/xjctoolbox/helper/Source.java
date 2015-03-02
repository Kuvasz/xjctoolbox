package xjctoolbox.helper;

import java.lang.reflect.Field;
import java.math.BigInteger;

// TODO notNull is not common
// TODO logger

public class Source
{
	protected Object source;
	
	public Source(Object source)
	{
		this.source = source;
	}
	
	/*
	public Object getPropertySafe(String path)
	{
		notNull(path, "path is null");
		
		try
		{
			return getProperty(source, path);
		}
		catch (Exception e)
		{
			
			return null;
		}
	}
	*/
	
	public int getInt(String path)
	{
		notNull(path, "path is null");
		
		Object prop = get(source, path);
		if (prop instanceof BigInteger)
		{
			return ((BigInteger) prop).intValue();
		}
		else if (prop instanceof Integer)
		{
			return (Integer) prop;
		}
		else
		{
			throw new IllegalArgumentException("unknown type " + prop.getClass());
		}
	}
	
	public Object get(String path)
	{
		notNull(path, "path is null");
		
		return get(source, path);
	}
	
	public Source getSource(String path)
	{
		notNull(path, "path is null");
		
		Object source = get(path);
		return new Source(source);
	}
	
	protected Object get(Object source, String path)
	{
		Object prop = null;
		if (source != null)
		{
			try
			{
				int separatorIndex = path.indexOf(".");
				if (separatorIndex < 0)
				{
					Field field = getField(source.getClass(), path, true);
					prop = field.get(source);
				}
				else
				{
					Field field = getField(source.getClass(), path.substring(0, separatorIndex), false);
					Object nextSource = field.get(source);
					prop = get(nextSource, path.substring(separatorIndex + 1));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				//log(e.getMessage() + ", source = " + source.getClass().getName() + ", path = " + path);
			}
		}
		
		return prop;
	}
	
	protected Field getField(Class<?> source, String property, boolean drillDown) throws NoSuchFieldException, SecurityException
	{
		Field result = null;
		
		Class<?> currentSource = source;
		do
		{
			try
			{
				result = currentSource.getDeclaredField(property);
			}
			catch (NoSuchFieldException e)
			{
				// no action
			}
			currentSource = currentSource.getSuperclass();
		}
		while (currentSource != null && result == null && drillDown);

		if (result != null)
		{
			result.setAccessible(true);
		}
		
		return result;
	}
	
	protected void notNull(String value, String msg)
	{
		if (value == null)
		{
			throw new IllegalArgumentException(msg);
		}
	}
}
