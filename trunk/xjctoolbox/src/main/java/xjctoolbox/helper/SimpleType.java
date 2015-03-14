package xjctoolbox.helper;

import java.math.BigDecimal;

import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSSimpleType;

public class SimpleType
{
	public static final String DEFAULT_MIN = "-2147483648";
    public static final String DEFAULT_MAX = "2147483647";
    public static final String DEFAULT_TOTAL_DIGITS = "0";
	
	protected XSSimpleType simpleType;
	
	public SimpleType(XSSimpleType simpleType)
	{
		this.simpleType = simpleType;
	}
	
	public BigDecimal getMinExclusive()
	{
		XSFacet facet = simpleType.getFacet("minExclusive");
		boolean valid = facet != null;// && !DEFAULT_MIN.equals(facet.getValue().value);
		
		return valid ? new BigDecimal(facet.getValue().value) : null;
	}
	
	public BigDecimal getMinInclusive()
	{
		XSFacet facet = simpleType.getFacet("minInclusive");
		boolean valid = facet != null;// && !DEFAULT_MIN.equals(facet.getValue().value);
		
		return valid ? new BigDecimal(facet.getValue().value) : null;
	}
	
	public BigDecimal getMaxExclusive()
	{
		XSFacet facet = simpleType.getFacet("maxExclusive");
		boolean valid = facet != null;// && !DEFAULT_MAX.equals(facet.getValue().value);
		
		return valid ? new BigDecimal(facet.getValue().value) : null;
	}
	
	public BigDecimal getMaxInclusive()
	{
		XSFacet facet = simpleType.getFacet("maxInclusive");
		boolean valid = facet != null;// && !DEFAULT_MAX.equals(facet.getValue().value);
		
		return valid ? new BigDecimal(facet.getValue().value) : null;
	}
	
	public Integer getTotalDigits()
	{
		XSFacet facet = simpleType.getFacet("totalDigits");
		boolean valid = facet != null && !DEFAULT_TOTAL_DIGITS.equals(facet.getValue().value);
		
		return valid ? Integer.parseInt(facet.getValue().value) : null;
	}
	
	public Integer getFractionDigits()
	{
		XSFacet facet = simpleType.getFacet("fractionDigits");
		return facet != null ? Integer.parseInt(facet.getValue().value) : null;
	}
	
	public Integer getLength()
	{
		XSFacet facet = simpleType.getFacet("length");
		return facet != null ? Integer.parseInt(facet.getValue().value) : null;
	}
	
	public Integer getMinLength()
	{
		XSFacet facet = simpleType.getFacet("minLength");
		return facet != null ? Integer.parseInt(facet.getValue().value) : null;
	}
	
	public Integer getMaxLength()
	{
		XSFacet facet = simpleType.getFacet("maxLength");
		return facet != null ? Integer.parseInt(facet.getValue().value) : null;
	}
	
	public String getPattern()
	{
		XSFacet facet = simpleType.getFacet("pattern");
		return facet != null ? facet.getValue().value : null;
	}
}
