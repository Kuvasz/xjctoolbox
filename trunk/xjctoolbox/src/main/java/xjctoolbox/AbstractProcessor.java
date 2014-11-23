package xjctoolbox;

import java.lang.reflect.Field;
import java.math.BigInteger;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

public abstract class AbstractProcessor
{
	protected Outline outline;
	protected ClassOutline classOutline;
	protected boolean verbose;
	
	public abstract void process();
	
	public Outline getOutline()
	{
		return outline;
	}
	
	public void setOutline(Outline outline)
	{
		this.outline = outline;
	}
	
	public ClassOutline getClassOutline()
	{
		return classOutline;
	}
	
	public void setClassOutline(ClassOutline classOutline)
	{
		this.classOutline = classOutline;
	}
	
	public boolean isVerbose()
	{
		return verbose;
	}
	
	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}
	
	public JDefinedClass getImplClass()
	{
		return classOutline.implClass;
	}
	
	public CClassInfo getTarget()
	{
		return classOutline.target;
	}
	
	public JCodeModel getCodeModel()
	{
		return getClassOutline().parent().getCodeModel();
	}
	
	protected void log(String s)
	{
		if (verbose)
		{
			System.out.print(getClassOutline().target);
			System.out.print(" - ");
			System.out.println(s);
		}
	}
	
	protected Object getPropertySafe(Object source, String path)
	{
		try
		{
			return getProperty(source, path);
		}
		catch (Exception e)
		{
			
			return null;
		}
	}
	
	protected int getPropertyInt(Object source, String path)
	{
		Object prop = getProperty(source, path);
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
	
	protected Object getProperty(Object source, String path)
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
					prop = getProperty(nextSource, path.substring(separatorIndex + 1));
				}
			}
			catch (Exception e)
			{
				log(e.getMessage() + ", source = " + source.getClass().getName() + ", path = " + path);
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
}
