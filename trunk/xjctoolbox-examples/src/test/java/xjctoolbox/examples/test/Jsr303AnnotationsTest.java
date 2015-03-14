package xjctoolbox.examples.test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.junit.Assert;
import org.junit.Test;

import examples.xjctoolbox.MinMaxOccursTest;
import examples.xjctoolbox.SimpleTypeTest;

public class Jsr303AnnotationsTest
{
	@Test
	public void testMinMaxOccurs()
	{
		Class<MinMaxOccursTest> cls = MinMaxOccursTest.class;
		
		assertAnnotation(false, cls, "e1", Valid.class);
		assertAnnotation(true,  cls, "e1", NotNull.class);
		assertAnnotation(false, cls, "e1", Size.class);
		
		assertAnnotation(false, cls, "e2", Valid.class);
		assertAnnotation(false, cls, "e2", NotNull.class);
		assertAnnotation(false, cls, "e2", Size.class);
		
		assertAnnotation(true,  cls, "e3", Valid.class);
		assertAnnotation(false, cls, "e3", NotNull.class);
		assertAnnotation(true,  cls, "e3", Size.class, param("min", 1));
		
		assertAnnotation(true,  cls, "e4", Valid.class);
		assertAnnotation(false, cls, "e4", NotNull.class);
		assertAnnotation(false, cls, "e4", Size.class);
		
		assertAnnotation(true,  cls, "e5", Valid.class);
		assertAnnotation(false, cls, "e5", NotNull.class);
		assertAnnotation(true,  cls, "e5", Size.class, param("min", 1), param("max", 10));
		
		assertAnnotation(true,  cls, "e6", Valid.class);
		assertAnnotation(false, cls, "e6", NotNull.class);
		assertAnnotation(true,  cls, "e6", Size.class, param("min", 5), param("max", 10));
		
		assertAnnotation(true,  cls, "e7", Valid.class);
		assertAnnotation(false, cls, "e7", NotNull.class);
		assertAnnotation(true,  cls, "e7", Size.class, param("min", 0), param("max", 10));
		
	}
	
	public void testSimpleType()
	{
		Class<SimpleTypeTest> cls = SimpleTypeTest.class;
		
	}
	
	protected void assertAnnotation(boolean b, Class<?> cls, String fieldName, Class<? extends Annotation> anntCls, AnnotationParam... params)
	{
		try
		{
			Field field = cls.getDeclaredField(fieldName);
			Annotation annt = field.getAnnotation(anntCls);
			
			Assert.assertEquals(fieldName, b, annt != null);
			for (AnnotationParam param : params)
			{
				Method m = anntCls.getMethod(param.key);
				Object actualValue = m.invoke(annt);
				
				Assert.assertEquals(fieldName + "/" + param.key, param.value, actualValue);
			}

		}
		catch (NoSuchMethodException | NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	protected AnnotationParam param(String key, Object value)
	{
		return new AnnotationParam(key, value);
	}
	
	private static class AnnotationParam
	{
		public final String key;
		public final Object value;
		
		public AnnotationParam(String key, Object value)
		{
			this.key = key;
			this.value = value;
		}
	}
}
