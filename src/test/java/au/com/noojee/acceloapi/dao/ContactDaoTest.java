package au.com.noojee.acceloapi.dao;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import au.com.noojee.acceloapi.AcceloApi;
import au.com.noojee.acceloapi.AcceloSecret;
import au.com.noojee.acceloapi.entities.Contact;

public class ContactDaoTest
{

	@Test
	public void test()
	{
		AcceloSecret secret;
		try
		{
			secret = AcceloSecret.load();
			AcceloApi.getInstance().connect(secret);

			ContactDao daoContact = new ContactDao();
			//List<Contact> contacts = daoContact.getByPhone("0385089777");
			List<Contact> contacts = daoContact.getByPhone("0383208100");

			for (Contact contact : contacts)
			{
				System.out.println(contact);
			}
			

		}
		catch (Throwable e)
		{
			e.printStackTrace();
			fail("Excepton");
		}

	}

}
