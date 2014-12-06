package xjctoolbox;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

public class AddSimpleContentConstructorPlugin extends Plugin
{
	public static final String OPTION_NAME = "XXjcToolboxAddSimpleContentConstructor";

	@Override
	public String getOptionName()
	{
		return OPTION_NAME;
	}

	@Override
	public String getUsage()
	{
		return "-" + OPTION_NAME + ": Appends constructors to simple content types\n";
	}

	@Override
	public boolean run(Outline outline, Options options, ErrorHandler errorHandler) throws SAXException
	{
		for (ClassOutline classOutline : outline.getClasses())
		{
			AbstractProcessor processor = new AddSimpleContentConstructor();
			processor.setOutline(outline);
			processor.setClassOutline(classOutline);
			processor.setVerbose(true);
			
			processor.process();
		}
		
		return true;
	}
}
