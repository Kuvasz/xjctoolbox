package xjctoolbox.helper;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;

public class Field
{
	protected JFieldVar fieldVar;

	public static Field getField(JDefinedClass implClass, String fieldName)
	{
		JFieldVar fieldVar = implClass.fields().get(fieldName);
		return new Field(fieldVar);
	}
	
	public Field(JFieldVar fieldVar)
	{
		if (fieldVar == null)
			throw new IllegalArgumentException("fieldVar is null");
		
		this.fieldVar = fieldVar;
	}
	
	public AnnotationWrapper annotate(Class<? extends Annotation> annotation)
	{
		JAnnotationUse annotationUse = null;
		if (!isAnnotated(annotation))
		{
			annotationUse = fieldVar.annotate(annotation);
		}
		return new AnnotationWrapper(annotationUse);
	}
	
	protected boolean isAnnotated(Class<? extends Annotation> annotation)
	{
		boolean annotated = false;
		Collection<JAnnotationUse> annotations =  fieldVar.annotations();
		for (JAnnotationUse ann : annotations)
		{
			JClass cls = ann.getAnnotationClass();
			if (cls.fullName().equals(annotation.getCanonicalName()))
			{
				annotated = true;
				break;
			}
		}
		
		return annotated;
	}
}
