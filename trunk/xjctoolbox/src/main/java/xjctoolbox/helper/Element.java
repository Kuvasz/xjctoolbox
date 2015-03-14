package xjctoolbox.helper;

import java.math.BigInteger;

import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.impl.ElementDecl;
import com.sun.xml.xsom.impl.ParticleImpl;
import com.sun.xml.xsom.impl.parser.DelayedRef;

public class Element
{
	protected CElementPropertyInfo propertyInfo;
	
	protected ParticleImpl particle;
	
	public Element(CElementPropertyInfo propertyInfo)
	{
		if (propertyInfo == null)
			throw new IllegalArgumentException("propertyInfo is null");
		
		this.propertyInfo = propertyInfo;
		
		init();
	}
	
	protected void init()
	{
		particle = (ParticleImpl) propertyInfo.getSchemaComponent();
	}
	
	public CElementPropertyInfo getPropertyInfo()
	{
		return propertyInfo;
	}
	
	public ParticleImpl getParticle()
	{
		return particle;
	}
	
	public int getMinOccurs()
	{
		BigInteger minOccurs = particle.getMinOccurs();
		return minOccurs != null ? minOccurs.intValue() : 1;
	}
	
	public int getMaxOccurs()
	{
		BigInteger maxOccurs = particle.getMaxOccurs();
		return maxOccurs != null ? maxOccurs.intValue() : -1;
	}
	
	public ElementDecl getDeclaration()
	{
		XSTerm term = particle.getTerm();
		
		ElementDecl decl = null;
		if (term instanceof ElementDecl)
		{
			decl = (ElementDecl) term;
		}
		else if (term instanceof DelayedRef.Element)
		{
			XSElementDecl xsElementDecl = ((DelayedRef.Element) term).get ();
			decl = (ElementDecl) xsElementDecl;
		}
		return decl;
	}
	
	public SimpleType getSimpleType()
	{
		XSSimpleType simpleType = null;
		ElementDecl declaration = getDeclaration();
		XSType type = declaration.getType();
		if (type instanceof XSSimpleType)
		{
			simpleType = (XSSimpleType) type;
		}
		else if (type.getBaseType () instanceof XSSimpleType)
		{
			simpleType = (XSSimpleType) type.getBaseType();
		}
		
		return new SimpleType(simpleType);
	}
	
	public String getFieldName()
	{
		return propertyInfo.getName(false);
	}
}
