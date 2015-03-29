package xjctoolbox.examples;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class CustomSerializer extends JsonSerializer<Object>
{
	private static final String METHOD_NAME = "getValue";
	
	protected Map<Class<?>, Method> cache = new ConcurrentHashMap<Class<?>, Method>();
	
	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException
	{
		try
		{
			Object simpleValue = getSimpleValue(value);
			jgen.writeObject(simpleValue);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	protected Object getSimpleValue(Object o) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Class<?> cls = o.getClass();
		Method method = cache.get(cls);
		if (method == null)
		{
			method = cls.getMethod(METHOD_NAME);
			cache.put(cls, method);
		}

		return method.invoke(o);
	}
}
