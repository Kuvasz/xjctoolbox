package xjctoolbox.examples.test;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

import examples.xjctoolbox.User;
import examples.xjctoolbox.UserId;
import examples.xjctoolbox.Vehicle;

public class XmlMappingTest
{
	public static final String XML = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + 
			"<ns2:User xmlns:ns2=\"http://xjctoolbox.examples\">" +
			"<Id>UID</Id>" +
			"<Name>UName</Name>" +
			"<Vehicles>" +
				"<Name>V1</Name>" +
			"</Vehicles>" +
			"<Vehicles>" +
				"<Name>V2</Name>" +
			"</Vehicles>" +
		"</ns2:User>";
	
	@Test
	public void testMarshal() throws JAXBException
	{
		User user = newUser();
		
		JAXBContext ctx = JAXBContext.newInstance(User.class);
		
		Marshaller m = ctx.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		
		StringWriter out = new StringWriter();
		m.marshal(user, out);
		out.flush();
		
		Assert.assertEquals(XML, out.toString());
	}
	
	@Test
	public void testUnmarshal() throws JAXBException
	{
		JAXBContext ctx = JAXBContext.newInstance(User.class);
		Unmarshaller um = ctx.createUnmarshaller();
		User user = (User) um.unmarshal(new ByteArrayInputStream(XML.getBytes()));
		
		Assert.assertNotNull(user);
		Assert.assertEquals(new UserId("UID"), user.getId());
		Assert.assertEquals("UName", user.getName());
		Assert.assertEquals(2, user.getVehicles().size());
		Assert.assertEquals("V1", user.getVehicles().get(0).getName());
	}
	
	public User newUser()
	{
		User user = new User();
		user.setId(new UserId("UID"));
		user.setName("UName");
		user.getVehicles().add(toVehicle("V1"));
		user.getVehicles().add(toVehicle("V2"));
		
		return user;
	}
	
	protected Vehicle toVehicle(String name)
	{
		Vehicle v = new Vehicle();
		v.setName(name);
		
		return v;
	}
}
