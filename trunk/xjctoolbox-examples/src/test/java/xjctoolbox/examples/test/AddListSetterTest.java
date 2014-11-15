package xjctoolbox.examples.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import examples.xjctoolbox.User;
import examples.xjctoolbox.Vehicle;

public class AddListSetterTest
{
	@Test
	public void test01()
	{
		User user = new User();
		user.getVehicles().add(toVehicle("V1"));
		user.getVehicles().add(toVehicle("V2"));
		
		Assert.assertArrayEquals(new String[] {"V1", "V2"}, toArray(user.getVehicles()));
		
		List<Vehicle> vs = toVehicles("V3", "V4");
		user.setVehicles(vs);
		
		Assert.assertArrayEquals(new String[] {"V3", "V4"}, toArray(user.getVehicles()));
	}
	
	protected Vehicle toVehicle(String name)
	{
		Vehicle v = new Vehicle();
		v.setName(name);
		
		return v;
	}
	
	protected List<Vehicle> toVehicles(String... names)
	{
		List<Vehicle> vs = new ArrayList<Vehicle>(names.length);
		for (String s: names)
		{
			Vehicle v = new Vehicle();
			v.setName(s);
			vs.add(v);
		}
		
		return vs;
	}
	
	protected String[] toArray(List<Vehicle> vs)
	{
		String[] names = new String[vs.size()];
		for (int i = 0; i < vs.size(); i++)
		{
			names[i] = vs.get(i).getName();
		}
		
		return names;
	}
}
