package xjctoolbox;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
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
	
	@Override
	public void process()
	{
		if (isSimpleContentContextType())
		{
			appendDefaultConstructor();
			appendConstructor();
			appendToString();
			appendEquals();
			appendHashCode();
			
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
}
