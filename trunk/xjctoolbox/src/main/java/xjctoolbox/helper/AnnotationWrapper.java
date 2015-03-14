package xjctoolbox.helper;

import java.math.BigDecimal;

import com.sun.codemodel.JAnnotationUse;

public class AnnotationWrapper
{
	protected JAnnotationUse annotationUse;
	
	public AnnotationWrapper(JAnnotationUse annotationUse)
	{
		this.annotationUse = annotationUse;
	}
	
	public AnnotationWrapper param(String name, int value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, boolean value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, byte value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, char value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, Class<?> value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, double value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, Enum value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, float value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, long value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, short value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, String value)
	{
		if (annotationUse != null)
		{
			annotationUse.param(name, value);
		}
		return this;
	}
	
	public AnnotationWrapper param(String name, BigDecimal value)
	{
		if (annotationUse != null)
		{
			boolean isInteger = value.signum() == 0 || value.scale() <= 0 || value.stripTrailingZeros().scale() <= 0;
			if (isInteger)
			{
				annotationUse.param(name, value.intValue());
			}
			else
			{
				annotationUse.param(name, value.doubleValue());
			}
		}
		return this;
	}
}
