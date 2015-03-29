package xjctoolbox;

import java.io.IOException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;

public class AddSimpleContentConstructorPlugin extends Plugin
{
	public static final String OPTION_NAME = "XXjcToolboxAddSimpleContentConstructor";
	public static final String ARG_SERIALIZER = OPTION_NAME + ":jsonSerializer=";
	
	protected String serializer = null;

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
			AddSimpleContentConstructor processor = new AddSimpleContentConstructor();
			processor.setOutline(outline);
			processor.setClassOutline(classOutline);
			processor.setVerbose(true);
			processor.setJsonSerializer(serializer);
			
			processor.process();
		}
		
		return true;
	}
	
	@Override
	public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException
	{
		String arg = args[i];
		int count = 0;
		int index = arg.indexOf(ARG_SERIALIZER);
		if (index > 0)
		{
			this.serializer = arg.substring (ARG_SERIALIZER.length () + 1);
			count++;
		}
		
		return count;
	}
}
