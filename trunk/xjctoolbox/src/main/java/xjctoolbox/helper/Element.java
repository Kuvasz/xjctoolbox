package xjctoolbox.helper;

import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.xml.xsom.impl.ElementDecl;
import com.sun.xml.xsom.impl.ParticleImpl;

public class Element
{
	protected CElementPropertyInfo propertyInfo;
	
	protected ParticleImpl particle;
	protected Source particeSource;
	
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
		particeSource = new Source(particle);
	}
	
	public CElementPropertyInfo getPropertyInfo()
	{
		return propertyInfo;
	}
	
	public ParticleImpl getParticle()
	{
		return particle;
	}
	
	public Source getSource()
	{
		return particeSource;
	}
	
	public int getMinOccurs()
	{
		return particeSource.getInt("minOccurs");
	}
	
	public int getMaxOccurs()
	{
		return particeSource.getInt("maxOccurs");
	}
	
	public ElementDecl getElementDecl()
	{
		Object term = particeSource.get("term");
		return term instanceof ElementDecl ? (ElementDecl) term : null;
	}
}
