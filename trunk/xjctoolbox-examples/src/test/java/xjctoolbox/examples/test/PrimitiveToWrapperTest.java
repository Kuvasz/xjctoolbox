package xjctoolbox.examples.test;

import org.junit.Assert;
import org.junit.Test;

import examples.xjctoolbox.Types1;
import examples.xjctoolbox.Types2;

public class PrimitiveToWrapperTest
{
	@Test
	public void testWrappers01()
	{
		Types1 types1 = new Types1();
		
		types1.setTboolean(null);
		Assert.assertNull(types1.getTboolean());
		
		types1.setTbyte(null);
		Assert.assertNull(types1.getTbyte());
		
		types1.setTshort(null);
		Assert.assertNull(types1.getTshort());
		
		types1.setTint(null);
		Assert.assertNull(types1.getTint());
		
		types1.setTlong(null);
		Assert.assertNull(types1.getTlong());
		
		types1.setTfloat(null);
		Assert.assertNull(types1.getTfloat());
		
		types1.setTdouble(null);
		Assert.assertNull(types1.getTdouble());
	}
	
	@SuppressWarnings("null")
	@Test
	public void testPrimitives01()
	{
		Types2 types1 = new Types2();
		
		try
		{
			types1.setTboolean((Boolean) null);
			Assert.fail();
		}
		catch (NullPointerException e)
		{
		}
		Assert.assertFalse(types1.isTboolean());
		
		try
		{
			types1.setTbyte((Byte) null);
			Assert.fail();
		}
		catch (NullPointerException e)
		{
		}
		Assert.assertEquals((byte) 0, types1.getTbyte());
		
		try
		{
			types1.setTshort((Short) null);
			Assert.fail();
		}
		catch (NullPointerException e)
		{
		}
		Assert.assertEquals((short) 0, types1.getTshort());
		
		try
		{
			types1.setTint((Integer) null);
			Assert.fail();
		}
		catch (NullPointerException e)
		{
		}
		Assert.assertEquals((int) 0, types1.getTint());
		
		try
		{
			types1.setTlong((Long) null);
			Assert.fail();
		}
		catch (NullPointerException e)
		{
		}
		Assert.assertEquals((long) 0, types1.getTlong());
		
		try
		{
			types1.setTfloat((Float) null);
			Assert.fail();
		}
		catch (NullPointerException e)
		{
		}
		Assert.assertEquals((float) 0, types1.getTfloat(), 0);
		
		try
		{
			types1.setTdouble((Double) null);
			Assert.fail();
		}
		catch (NullPointerException e)
		{
		}
		Assert.assertEquals((double) 0, types1.getTdouble(), 0);
	}
}
