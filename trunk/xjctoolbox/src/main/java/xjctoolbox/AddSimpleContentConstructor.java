package xjctoolbox;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class AddSimpleContentConstructor extends AbstractProcessor
{
	public static final String FIELD_NAME = "value";
	public static final String METHOD_GETTER = "getValue";
	public static final String METHOD_SETTER = "setValue";
	public static final Set<String> METHOD_NAMES;
	static
	{
		Set<String> methodNames = new HashSet<String>();
		methodNames.add(METHOD_GETTER);
		methodNames.add(METHOD_SETTER);
		METHOD_NAMES = Collections.unmodifiableSet(methodNames);
	}
	public static final Map<String, PrimitiveConst> PRIMITIVES;
	static
	{
		Map<String, PrimitiveConst> primitives = new HashMap<String, PrimitiveConst>();
		primitives.put("byte", new PrimitiveConst(Byte.class, "parseByte"));
		primitives.put("short", new PrimitiveConst(Short.class, "parseShort"));
		primitives.put("int", new PrimitiveConst(Integer.class, "parseInt"));
		primitives.put("long", new PrimitiveConst(Long.class, "parseLong"));
		primitives.put("float", new PrimitiveConst(Float.class, "parseFloat"));
		primitives.put("double", new PrimitiveConst(Double.class, "parseDouble"));
		primitives.put("boolean", new PrimitiveConst(Boolean.class, "parseBoolean"));
		primitives.put("char", new PrimitiveConst(Character.class, "toString"));

		PRIMITIVES = Collections.unmodifiableMap(primitives);
	}
	public static final Set<String> WRAPPERS;
	static
	{
		
		Set<String> wrappers = new HashSet<String>();
		wrappers.add("Byte");
		wrappers.add("Short");
		wrappers.add("Integer");
		wrappers.add("Long");
		wrappers.add("Float");
		wrappers.add("Double");
		wrappers.add("Boolean");

		WRAPPERS = Collections.unmodifiableSet(wrappers);
	}
	
	protected String jsonSerializer;
	
	public String getJsonSerializer()
	{
		return jsonSerializer;
	}

	public void setJsonSerializer(String jsonSerializer)
	{
		this.jsonSerializer = jsonSerializer;
	}
	

	@Override
	public void process()
	{
		if (isSimpleContentContextType())
		{
			appendDefaultConstructor();
			appendConstructor();
			appendStringConstructor();
			appendToString();
			appendEquals();
			appendHashCode();
			if (jsonSerializer != null)
			{
				appendJsonSerializer();
			}
			
			log("Constructor appended.");
		}
	}
	
	protected boolean isSimpleContentContextType()
	{
		boolean methodCountMatches = getImplClass().methods().size() == 2;
		boolean fieldCountMatches = getImplClass().fields().size() == 1;
		
		return methodCountMatches && fieldCountMatches && methodNamesMatches() && fieldNameMatches();
	}
	
	protected boolean methodNamesMatches()
	{
		boolean result = true;
		for (JMethod m: getImplClass().methods())
		{
			if (!METHOD_NAMES.contains(m.name()))
			{
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	protected boolean fieldNameMatches()
	{
		return getImplClass().fields().get("value") != null;
	}
	
	protected void appendDefaultConstructor()
	{
		getImplClass().constructor(JMod.PUBLIC);
	}
	
	protected void appendConstructor()
	{
		JFieldVar field = getImplClass().fields().get(FIELD_NAME);
		JMethod cs = getImplClass().constructor(JMod.PUBLIC);
		cs.param(field.type(), FIELD_NAME);
		JBlock body = cs.body();
		body.assign(JExpr.refthis(FIELD_NAME), JExpr.ref(FIELD_NAME));
	}
	
	protected void appendStringConstructor()
	{
		JFieldVar field = getImplClass().fields().get(FIELD_NAME);
		String sType = field.type().name();
		if (!"String".equals(sType))
		{
			JMethod cs = getImplClass().constructor(JMod.PUBLIC);
			cs.param(String.class, FIELD_NAME);
			JBlock body = cs.body();
			
			JInvocation inv;
			JClass jClass = getCodeModel().ref(field.type().fullName());
			if (field.type().isPrimitive())
			{
				PrimitiveConst pc = PRIMITIVES.get(sType);
				inv = getCodeModel().ref(pc.wrapper).staticInvoke(pc.method).arg(JExpr.ref(FIELD_NAME));
			}
			else if (WRAPPERS.contains(sType))
			{
				inv = getCodeModel().ref(sType).staticInvoke("valueOf").arg(JExpr.ref(FIELD_NAME));
			}
			else
			{
				inv = JExpr._new(jClass).arg(JExpr.ref(FIELD_NAME));
			}
			
			body.assign(JExpr.refthis(FIELD_NAME), inv);
		}
	}
	
	protected void appendToString()
	{
		JFieldVar field = getImplClass().fields().get(FIELD_NAME);
		JMethod ts = getImplClass().method(JMod.PUBLIC, String.class, "toString");
		ts.annotate(Override.class);
		JBlock body = ts.body();
		
		if (field.type().isPrimitive())
		{
			body._return(field.plus(JExpr.lit("")));
		}
		else
		{
			JConditional cond = body._if(field.ne(JExpr._null()));
			JBlock ct = cond._then();
			JBlock ce = cond._else();
			
			ct._return(field.invoke("toString"));
			ce._return(JExpr.lit("null"));
		}
	}
	
	protected void appendEquals()
	{
		JFieldVar field = getImplClass().fields().get(FIELD_NAME);
		JMethod ts = getImplClass().method(JMod.PUBLIC, getCodeModel().BOOLEAN, "equals");
		JVar param = ts.param(Object.class, "o");
		ts.annotate(Override.class);
		JBlock body = ts.body();
		
		if (field.type().isPrimitive())
		{
			JConditional c1 = body._if(JExpr._this().eq(param));
			c1._then()._return(JExpr.TRUE);
			JConditional c2 = body._if(param.eq(JExpr._null()));
			c2._then()._return(JExpr.FALSE);
			JConditional c3 = body._if(param._instanceof(getImplClass()).not());
			c3._then()._return(JExpr.FALSE);
			JConditional c4 = body._if(field.ne(((JExpression) JExpr.cast(getImplClass(), param)).invoke(METHOD_GETTER)));
			c4._then()._return(JExpr.FALSE);
			
			body._return(JExpr.TRUE);
		}
		else
		{
			JConditional cond = body._if(field.ne(JExpr._null()).cand(param._instanceof(getImplClass())));
			JBlock ct = cond._then();
			JBlock ce = cond._else();
			
			ct._return(field.invoke("equals").arg(((JExpression) JExpr.cast(getImplClass(), param)).invoke(METHOD_GETTER)));
			ce._return(JExpr._super().invoke("equals").arg(param));
		}
	}
	
	protected void appendHashCode()
	{
		JFieldVar field = getImplClass().fields().get(FIELD_NAME);
		JMethod ts = getImplClass().method(JMod.PUBLIC, getCodeModel().INT, "hashCode");
		ts.annotate(Override.class);
		JBlock body = ts.body();

		if (field.type().isPrimitive())
		{
			JVar prime = body.decl(0, getCodeModel().INT, "prime", JExpr.lit(31));
			JVar result = body.decl(0, getCodeModel().INT, "result", JExpr.lit(1));
			body.assign(result, result.mul(prime).plus(field));
			body._return(result);
		}
		else
		{
			JConditional cond = body._if(field.ne(JExpr._null()));
			JBlock ct = cond._then();
			JBlock ce = cond._else();
			
			ct._return(field.invoke("hashCode"));
			ce._return(JExpr._super().invoke("hashCode"));
		}
	}
	
	protected void appendJsonSerializer()
	{
		JClass serializerImpl = getOutline().getModel().codeModel.ref(jsonSerializer);
		getImplClass().annotate(JsonSerialize.class).param("using", serializerImpl);
	}
	
	private static class PrimitiveConst
	{
		public Class<?> wrapper;
		public String method;
		
		public PrimitiveConst(Class<?> wrapper, String method)
		{
			this.wrapper = wrapper;
			this.method = method;
		}
	}
}
