package xjctoolbox.examples.test;

import org.junit.Assert;
import org.junit.Test;

import examples.xjctoolbox.User;
import examples.xjctoolbox.UserId;

public class AddSimpleContentConstructorTest
{
	@Test
	public void test01()
	{
		UserId uid1 = new UserId("UID");
		UserId uid2 = new UserId();
		uid2.setValue("UID");
		UserId uid3 = new UserId("UID3");
		
		User user = new User();
		user.setId(uid1);
		user.setName("UID");
		
		Assert.assertEquals(uid1, uid2);
		Assert.assertNotEquals(uid1, uid3);
		Assert.assertEquals(uid1.hashCode(), uid2.hashCode());
	}
}
