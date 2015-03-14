package xjctoolbox.helper;

import com.sun.tools.xjc.model.CValuePropertyInfo;
import com.sun.xml.xsom.impl.RestrictionSimpleTypeImpl;

public class Value
{
	protected CValuePropertyInfo propertyInfo;
	
	protected RestrictionSimpleTypeImpl schemaComponent;
	
	public Value(CValuePropertyInfo propertyInfo)
	{
		if (propertyInfo == null)
			throw new IllegalArgumentException("propertyInfo is null");
		
		this.propertyInfo = propertyInfo;
		
		init();
	}
	
	protected void init()
	{
		schemaComponent = (RestrictionSimpleTypeImpl) propertyInfo.getSchemaComponent();
	}
	
	public CValuePropertyInfo getPropertyInfo()
	{
		return propertyInfo;
	}
	
	public RestrictionSimpleTypeImpl getSchemaComponent()
	{
		return schemaComponent;
	}
	
	public SimpleType getSimpleType()
	{
		SimpleType simpleType = null;
		if (schemaComponent.isSimpleType())
		{
			simpleType = new SimpleType(schemaComponent);
		}
		
		return simpleType;
	}
	
	public String getFieldName()
	{
		return propertyInfo.getName(false);
	}
}
