package xjctoolbox;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xjctoolbox.helper.Source;

import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXPluginCustomization;

public class PrimitiveToWrapper extends AbstractProcessor
{
	public static final String APPINFO_KEEP_PRIMITIVES = "keepPrimitives";
	
	public static final String SETTER = "set";
	public static final String GETTER = "get";
	public static final String GETTER_BOOLEAN = "is";
	public static final String BOOLEAN_WRAPPER = "Boolean";
	public static final Map<String, Class<?>> PRIMITIVES;
	static
	{
		Map<String, Class<?>> primitives = new HashMap<String, Class<?>>();
		primitives.put("byte", Byte.class);
		primitives.put("short", Short.class);
		primitives.put("int", Integer.class);
		primitives.put("long", Long.class);
		primitives.put("float", Float.class);
		primitives.put("double", Double.class);
		primitives.put("boolean", Boolean.class);
		primitives.put("char", Character.class);

		PRIMITIVES = Collections.unmodifiableMap(primitives);
	}
	
	@Override
	public void process()
	{
		if (!keepPrimitives())
		{
			Map<String, JMethod> methods = getMethods();
			Map<String, JFieldVar> fields = getImplClass().fields();
			for (JFieldVar field : fields.values())
			{
				if (field.type().isPrimitive())
				{
					convertToWrapper(field, methods);
				}
				else if (BOOLEAN_WRAPPER.equals(field.type().name()))
				{
					convertBooleanIsToGet(field, methods);
				}
			}
		}
	}
	
	protected Map<String, JMethod> getMethods()
	{
		Collection<JMethod> methods = getImplClass().methods();
		Map<String, JMethod> result = new HashMap<String, JMethod>();
		
		for (JMethod method : methods)
		{
			result.put(method.name(), method);
		}
		
		return result;
	}
	
	protected void convertToWrapper(JFieldVar field, Map<String, JMethod> methods)
	{
		JType type = field.type();
		field.type(getWrapperJType(field.type()));
		
		String fieldName = getFieldName(field);
		boolean isBoolean = type.name() == "boolean";
		
		String setterMethodName = SETTER + fieldName;
		JMethod setterMethod = methods.get(setterMethodName);
		convertSetterMethodToWrapper(setterMethod);
		
		String getterMethodName = isBoolean ? GETTER_BOOLEAN + fieldName : GETTER + fieldName;
		JMethod getterMethod = methods.get(getterMethodName);
		convertGetterMethodToWrapper(getterMethod);
		
		if (isBoolean)
		{
			changeBooleanIsToGet(getterMethod, field);
		}
	}
	
	protected void convertBooleanIsToGet(JFieldVar field, Map<String, JMethod> methods)
	{
		String fieldName = getFieldName(field);
		String getterMethodName = GETTER_BOOLEAN + fieldName;
		JMethod getterMethod = methods.get(getterMethodName);
		if (getterMethod != null)
		{
			changeBooleanIsToGet(getterMethod, field);
		}
	}

	protected String getFieldName(JFieldVar field)
	{
		String name = field.name();
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
	
	protected void convertSetterMethodToWrapper(JMethod method)
	{
		JVar param = method.params().get(0);
		param.type(getWrapperJType(param.type()));
	}
	
	protected void convertGetterMethodToWrapper(JMethod method)
	{
		method.type(getWrapperJType(method.type()));
	}
	
	protected JType getWrapperJType(JType primitiveType)
	{
		Class<?> wrapper = PRIMITIVES.get(primitiveType.name());
		return getCodeModel()._ref(wrapper);
	}
	
	/*
	 * Boolean isBoolean() is not a valid JavaBean so sometimes Boolean getBoolean() can be useful.
	 */
	protected void changeBooleanIsToGet(JMethod method, JFieldVar field)
	{
		String fieldName = getFieldName(field);
		String newMethodName = GETTER + fieldName;
		
		method.name(newMethodName);
	}
	
	// TODO almost same method as in CustomAppinfo
	protected boolean keepPrimitives()
	{
		boolean result = false;
		
		List<?> decl = (List<?>) new Source(getTarget())
			.getSource("source")
			.getSource("annotation")
			.getSource("annotation")
			.get("decls");

		if (decl != null)
		{
			for (Object o : decl)
			{
				if (o instanceof BIXPluginCustomization)
				{
					BIXPluginCustomization cust = (BIXPluginCustomization) o;
					String localPart = cust.getName().getLocalPart();
					log("xsd:appinfo found: <" + localPart + ">");
					
					if (localPart.equals(APPINFO_KEEP_PRIMITIVES))
					{
						result = true;
					}
				}
			}
		}
		
		return result;
	}
}
