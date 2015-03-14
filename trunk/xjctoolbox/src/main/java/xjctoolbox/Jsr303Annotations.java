package xjctoolbox;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import xjctoolbox.helper.AnnotationWrapper;
import xjctoolbox.helper.Element;
import xjctoolbox.helper.Field;
import xjctoolbox.helper.SimpleType;
import xjctoolbox.helper.Value;

import com.sun.tools.xjc.model.CElementPropertyInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CValuePropertyInfo;
import com.sun.xml.xsom.impl.ElementDecl;

public class Jsr303Annotations extends AbstractProcessor
{
	@Override
	public void process()
	{
		for (CPropertyInfo property : getTarget().getProperties())
		{
			if (property instanceof CElementPropertyInfo)
			{
				Element element = new Element((CElementPropertyInfo) property);
				Field field = Field.getField(getImplClass(), element.getFieldName());
				
				processElement(element, field);
			}
			else if (property instanceof CValuePropertyInfo)
			{
				Value value = new Value((CValuePropertyInfo) property);
				Field field = Field.getField(getImplClass(), value.getFieldName());
				
				processValue(value, field);
			}
		}
	}
	
	protected void processElement(Element element, Field field)
	{
		processMinMaxOccurs(element, field);
		processDeclaration(element, field);
	}
	
	protected void processValue(Value value, Field field)
	{
		SimpleType simpleType = value.getSimpleType();
		if (simpleType != null)
		{
			processSimpleType(simpleType, field);
		}
	}
	
	protected void processMinMaxOccurs(Element element, Field field)
	{
		int minOccurs = element.getMinOccurs();
		int maxOccurs = element.getMaxOccurs();
		
		boolean empty = minOccurs == 0;
		boolean list = maxOccurs != 1;
		
		if (list)
		{
			field.annotate(Valid.class);
			if (maxOccurs > 1)
			{
				field.annotate(Size.class).param("min", Math.max(minOccurs, 0)).param("max", maxOccurs);	
			}
			else if (!empty)
			{
				field.annotate(Size.class).param("min", Math.max(minOccurs, 1));	
			}
		}
		else if (!empty)
		{
			field.annotate(NotNull.class);
		}
	}
	
	protected void processDeclaration(Element element, Field field)
	{
		ElementDecl declaration = element.getDeclaration();
		if (declaration != null)
		{
			boolean complexType = declaration.getType().isComplexType();
			if (complexType)
			{
				field.annotate(Valid.class);
			}
			
			SimpleType simpleType = element.getSimpleType();
			if (simpleType != null && !complexType)
			{
				processSimpleType(simpleType, field);
			}
		}
	}
	
	protected void processSimpleType(SimpleType simpleType, Field field)
	{
		if (simpleType.getMinInclusive() != null)
		{
			field.annotate(DecimalMin.class).param("value", "" + simpleType.getMinInclusive());
		}
		else if (simpleType.getMinExclusive() != null)
		{
			field.annotate(DecimalMin.class).param("value", "" + simpleType.getMinExclusive()).param("inclusive", false);
		}
		
		if (simpleType.getMaxInclusive() != null)
		{
			field.annotate(DecimalMax.class).param("value", "" + simpleType.getMaxInclusive());
		}
		else if (simpleType.getMaxExclusive() != null)
		{
			field.annotate(DecimalMax.class).param("value", "" + simpleType.getMaxExclusive()).param("inclusive", false);
		}
		
		if (simpleType.getTotalDigits() != null || (simpleType.getFractionDigits() != null && simpleType.getFractionDigits() > 0))
		{
			AnnotationWrapper aw = field.annotate(Digits.class);
				
			if (simpleType.getFractionDigits() > 0)
			{
				int integer = simpleType.getTotalDigits() != null ? simpleType.getTotalDigits() - simpleType.getFractionDigits() : 0; 
				
				aw.param("integer", integer);
				aw.param("fraction", simpleType.getFractionDigits());
			}
			else if (simpleType.getTotalDigits() != null)
			{
				aw.param("integer", simpleType.getTotalDigits());
				aw.param("fraction", 0);
			}
		}
		
		if (simpleType.getLength() != null)
		{
			field.annotate(Size.class)
				.param("min", simpleType.getLength())
				.param("max", simpleType.getLength());
		}
		
		if (simpleType.getMinLength() != null || simpleType.getMaxLength() != null)
		{
			field.annotate(Size.class)
				.param("min", simpleType.getMinLength() != null ? simpleType.getMinLength() : 0)
				.param("max", simpleType.getMaxLength());
		}
		
		if (simpleType.getPattern() != null)
		{
			field.annotate(Pattern.class).param("regexp", simpleType.getPattern());
		}
	}
}
