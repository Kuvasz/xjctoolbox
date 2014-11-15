package xjctoolbox;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.xml.xsom.impl.ParticleImpl;

public class AddListSetter extends AbstractProcessor
{
	@Override
	public void process()
	{
		for (CPropertyInfo property : getTarget().getProperties())
		{
			if (property instanceof CElementPropertyInfo)
			{
				CElementPropertyInfo elementProperty = (CElementPropertyInfo) property;
				ParticleImpl particle = (ParticleImpl) elementProperty.getSchemaComponent();
				int maxOccurs = getPropertyInt(particle, "maxOccurs");
				
				boolean unbounded = maxOccurs < 0;
				if (unbounded)
				{
					appendSetList(elementProperty);
				}
			}
		}
	}
	
	protected void appendSetList(CElementPropertyInfo property)
	{
		String fieldName = property.getName(false);
		JFieldVar field = getImplClass().fields().get(fieldName);
		String methodName = generateMethodName(fieldName);
		
		JMethod setter = getImplClass().method(JMod.PUBLIC, getCodeModel().VOID, methodName);
		setter.param(field.type(), fieldName);
		JBlock body = setter.body();
		body.assign(JExpr.refthis(fieldName), JExpr.ref(fieldName));
	}
	
	protected String generateMethodName(String fieldName)
	{
		return "set" + Character.toUpperCase(fieldName.charAt(0)) + (fieldName.length() > 1 ? fieldName.substring(1) : "");
	}
}
