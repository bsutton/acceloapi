package au.com.noojee.acceloapi.dao;

import java.time.LocalDate;
import java.util.List;

import au.com.noojee.acceloapi.AcceloException;
import au.com.noojee.acceloapi.AcceloResponseList;
import au.com.noojee.acceloapi.EndPoint;
import au.com.noojee.acceloapi.entities.Company;
import au.com.noojee.acceloapi.entities.Contact;
import au.com.noojee.acceloapi.entities.Contract;
import au.com.noojee.acceloapi.entities.ContractPeriod;
import au.com.noojee.acceloapi.filter.AcceloFilter;
import au.com.noojee.acceloapi.filter.expressions.After;
import au.com.noojee.acceloapi.filter.expressions.Against;
import au.com.noojee.acceloapi.filter.expressions.Before;
import au.com.noojee.acceloapi.filter.expressions.Empty;
import au.com.noojee.acceloapi.filter.expressions.Eq;
import au.com.noojee.acceloapi.filter.expressions.Expression;

public class ContractDao extends AcceloDao<Contract>
{

	/**
	 * Find an active Contract Period for the given phone no. We also trying
	 * stripping the area code in case the phone no. is stored as just eight
	 * digits.
	 * @param phone
	 * 
	 * @return
	 * @throws AcceloException
	 */
	public Contract getActiveContractByPhone(String phone) throws AcceloException
	{
		Contract active = null;

		if (phone.length() != 10)
			throw new AcceloException("The phone number must be 10 digits long, found '" + phone + "'");

		List<Contact> contacts = new ContactDao().getByPhone(phone);
		if (contacts.size() == 0)
		{
			// Didn't find by the phone number.
			// List try trimming off the area code and see if that gives a
			// match.

			contacts = new ContactDao().getByPhone(phone.substring(2));
		}

		// If we get multiple matches we are just going to use the first one.
		// Its not the end of the world if we register against the company.
		if (contacts.size() >= 1)
		{
			Contact contact = contacts.get(0);

			Company company = new ContactDao().getDefaultCompany(contact);
			List<Contract> contracts = this.getByCompany(company);

			// find the first contract with a non-expired contract_period
			for (Contract contract : contracts)
			{
				List<ContractPeriod> periods = new ContractPeriodDao().getByContract(contract);
				for (ContractPeriod period : periods)
				{
					LocalDate expires = period.getDateExpires();
					LocalDate now = LocalDate.now();
					if (expires.isBefore(now)) // expired
						continue;

					LocalDate commenced = period.getDateCommenced();
					if (commenced.isAfter(now))
						continue; // hasn't started yet.

					active = contract;
					break;
				}
			}

		}

		return active;

	}

	/**
	 * Find an Active Contract for the given company.
	 * 
	 * This will return a random contract if the company has more than one activie contract.
	 * @param Company
	 *            - we only us the Id of the company.
	 * 
	 * @return
	 * @throws AcceloException
	 */
	public Contract getActiveContract(Company company) throws AcceloException
	{
		Contract active = null;

		List<Contract> contracts = this.getByCompany(company);

		// find the first contract with a non-expired contract_period
		for (Contract contract : contracts)
		{
			List<ContractPeriod> periods = new ContractPeriodDao().getByContract(contract);
			for (ContractPeriod period : periods)
			{
				LocalDate expires = period.getDateExpires();
				LocalDate now = LocalDate.now();
				if (expires.isBefore(now)) // expired
					continue;

				LocalDate commenced = period.getDateCommenced();
				if (commenced.isAfter(now))
					continue; // hasn't started yet.

				active = contract;
				break;
			}
		}

		return active;

	}

	/**
	 * Get a complete list of the active contracts.
	 * 
	 * @param acceloApi
	 * @throws AcceloException
	 */
	public List<Contract> getActiveContracts() throws AcceloException
	{
		// Get all contracts where the expiry date is after today or null and the
		// start date is before today
		AcceloFilter filter = new AcceloFilter();
		filter.where(new Before("date_started", LocalDate.now())
				.and(new After("date_expires", LocalDate.now()).or(new Empty("date_expires")).or(new Eq("date_expires", Expression.DATEZERO))));

		return getByFilter(filter);
	}

	public List<Contract> getByCompany(Company company) throws AcceloException
	{
		List<Contract> contracts;
		AcceloFilter filters = new AcceloFilter();
		filters.where(new Against("company", company.getId()));
		contracts = getByFilter(filters);
		return contracts;
	}

	public class ResponseList extends AcceloResponseList<Contract>
	{
	}


	@Override
	protected Class<ContractDao.ResponseList> getResponseListClass()
	{
		return ContractDao.ResponseList.class;
	}

	@Override
	protected EndPoint getEndPoint()
	{
		return EndPoint.contracts;
	}

}