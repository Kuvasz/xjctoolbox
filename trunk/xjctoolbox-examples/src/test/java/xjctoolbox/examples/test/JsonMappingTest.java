package xjctoolbox.examples.test;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import examples.xjctoolbox.User;
import examples.xjctoolbox.UserId;
import examples.xjctoolbox.Vehicle;

public class JsonMappingTest
{
	public static final String JSON =
		"{\"Id\":\"UID\"," +
		"\"Name\":\"UName\","+
		"\"Vehicles\":[" +
			"{\"Name\":\"V1\"}," + 
			"{\"Name\":\"V2\"}" + 
		"]}";
	
	private static ObjectMapper objectMapper;
	
	@BeforeClass
	public static void init()
	{
		JaxbAnnotationIntrospector jaxbIntrospector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
        AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();
		
		objectMapper = new ObjectMapper();
		objectMapper.setAnnotationIntrospector(new AnnotationIntrospectorPair(jaxbIntrospector, jacksonIntrospector));
	}
	
	// TODO
	@Test
	@Ignore
	public void testMarshal() throws JsonGenerationException, JsonMappingException, IOException
	{
		User user = newUser();
		
		StringWriter out = new StringWriter();
		objectMapper.writeValue(out, user);
		out.flush();
		System.out.println("## " + out.toString() + " ##");
		System.out.println("## " + JSON + " ##");
		
		Assert.assertEquals(JSON, out.toString());
		
		//objectMapper.readValue(response.getBody(), clazz);
	}
	
	@Test
	public void testUnmarshal() throws JsonGenerationException, JsonMappingException, IOException
	{
		User user = objectMapper.readValue(JSON, User.class);
		
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
