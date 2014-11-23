package xjctoolbox;

import java.util.List;

import org.w3c.dom.Node;

import com.sun.codemodel.JClass;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXPluginCustomization;

public class CustomAppinfo extends AbstractProcessor
{
	public static final String APPINFO_IMPLEMENTS = "implements";
	public static final String APPINFO_VALIDATOR = "validator";
	public static final String APPINFO_XMLJAVATYPEADAPTER = "xmlJavaTypeAdapter";
	public static final String NODE_INTERFACE = "interface";
	public static final String NODE_ANNOTATION = "annotation";
	
	@Override
	public void process()
	{
		processClassAppinfo();
		
	}
	
	protected void processClassAppinfo()
	{
		Object source = getProperty(getTarget(), "source");
		Object annotation1 = getProperty(source, "annotation");
		Object annotation2 = getProperty(annotation1, "annotation");
		List<?> decl = (List<?>) getProperty(annotation2, "decls");

		if (decl != null)
		{
			for (Object o : decl)
			{
				if (o instanceof BIXPluginCustomization)
				{
					BIXPluginCustomization cust = (BIXPluginCustomization) o;
					String localPart = cust.getName().getLocalPart();
					log("xsd:appinfo found: <" + localPart + ">");
					
					if (localPart.equals(APPINFO_IMPLEMENTS))
					{
						Node node = cust.element.getAttributes().getNamedItem(NODE_INTERFACE);
						log("implemented interface = " + node.getTextContent());
						appendClassInterface(node.getTextContent());
					}
				}
			}
		}
	}
	
	protected void appendClassInterface(String name)
	{
		JClass iface = getOutline().getModel().codeModel.ref(name);
		getImplClass()._implements(iface);
	}
	
}
