package xjctoolbox;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

public abstract class AbstractProcessor
{
	protected Outline outline;
	protected ClassOutline classOutline;
	protected boolean verbose;
	
	public abstract void process();
	
	public Outline getOutline()
	{
		return outline;
	}
	
	public void setOutline(Outline outline)
	{
		this.outline = outline;
	}
	
	public ClassOutline getClassOutline()
	{
		return classOutline;
	}
	
	public void setClassOutline(ClassOutline classOutline)
	{
		this.classOutline = classOutline;
	}
	
	public boolean isVerbose()
	{
		return verbose;
	}
	
	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}
	
	public JDefinedClass getImplClass()
	{
		return classOutline.implClass;
	}
	
	public CClassInfo getTarget()
	{
		return classOutline.target;
	}
	
	public JCodeModel getCodeModel()
	{
		return getClassOutline().parent().getCodeModel();
	}
	
	protected void log(String s)
	{
		if (verbose)
		{
			System.out.print(getClassOutline().target);
			System.out.print(" - ");
			System.out.println(s);
		}
	}
}
