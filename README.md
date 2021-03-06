# acceloapi
## Java api for the Accelo CRM

As the name says, this is an Java API that makes it easier to talk to the Accelo REST API.

You can see the documentation for the Accelo REST API here:

https://api.accelo.com/docs/?_ga=2.216987606.191295230.1509320720-1689776969.1496361497

You can access the full java for the Noojee Accelo API here:

https://bsutton.github.io/acceloapi/index.html

The java api provides a simply to use interface to the Accelo REST API.

The following gets all of the tickets for a contract which are open or were closed after the start of last month.
	
	LocalDate lastMonth = now.minusMonths(1).withDayOfMonth(1);
	filter.where(filter.eq(Ticket_.contract, contract.getId())
		.and(filter.after(Ticket_.date_closed, lastMonth).or(filter.eq(Ticket_.date_closed, Expression.DATEZERO))));
	List<Ticket> tickets = new TicketDao().getByFilter(filter);

### limiting results
To protect the accelo servers from being overloaded, the filters by default are limited to returning 50 entities.
You can control the no. of entities that are returned by using the limit clause.

	filter.limit(100);

Be a good citizen and think before you increase the filter limit.

### understanding DAO

The Noojee Accelo API loosely uses the  Data Access Objects (DAO) programming pattern (my appologies to the Java gods for my mis-use).

For each entity (Ticket, Company, Contact etc) there is a Dao class, an Entity and a Meta data class class.

For example:
	
	TicketDao
	Ticket
	Ticket_
	
To access a ticket you must go through the TicketDao class. All high level business logic for tickets is also contained in the TicketDao class.
Its worth exploring each of the Daos as the tend to have a collection of useful funtions like:

	Duration billable = new TicketDao.getBillable();
	Duration billable = new TicketDao.getNonBillable();
	
### Standard Dao methods

Each Dao class supports a standard set of common funtions.

	new TicketDao().getById(ticket_id);
	new TicketDao().getByFilter(filter);
	new TicketDao().getAll(); // use with care!!!
	new TicketDao().update(ticket);
	new TicketDao().delete(ticket);
		
### Meta data for field access

To make certain you are always trying to access a valid field the Entity Meta data class has a complete list of valid fields:

You start by creating a filter for a specific entity. In this case the Staff entity.
	AcceloFilter<Staff> filter = new AcceloFilter<>();

You then can reference any of the filterable Staff fields using the meta data class Staff_.
		
	AcceloFilter<Staff> filter = new AcceloFilter<>();
	filter.where(filter.eq(Staff_.email, staffEmailAddress));
	
I should note that Accelo has a fairly restrictive set of fields you can search via. Look at the meta data class (Staff_ in this case) to see what you can search by.

### Missing entities

At this point the API is not complete in that it does support all Accelo entities.
You can however fairly easily extend the Noojee Accelo Api to support new entities.

To add support for a new Entity you need to create a Dao class (e.g. ContributorDao) and an entity class (Contributor). S
Copy one of the existing entities and dao classes to get started. The primary piece of work is populating the Entity class with a full set of fields returned from the Accelo REST API.
### Examples:

Fetching data from the Accelo servers is rather slow so the library is heavily cached.
By default the cache holds 10,000 queries which expire after 10 minutes.

The Api automatically caches the results of each query (filter) so that if you run the same query again, the results will come back from the cache.

When you run a query the individual entities are also added to the cache using their id as a key. Subsequent calls using the entities id will retrieve the results from the cache. 	

### Cache Example

Fetch all tickets for the given company:

	Company company = new CompanyDao().getByName("Noojee Contact Solutions");
	filter.where(filter.against(Ticket_.company, company.getId())
		.and(filter.after(Ticket_.date_closed, dayBefore).or(filter.eq(Ticket_.date_closed, Expression.DATEZERO))));
	List<Ticket> tickets = new TicketDao().getByFilter(filter);

All tickets returned are now cached.

if we run the same query
	List<Ticket> tickets = new TicketDao().getByFilter(filter);

Then there will be no api calls to the accelo server.
We can also get the individual ticket by id and again this will be returned from the cache:

	Ticket ticketFromPriorQuery = tickets.get(0);
	Ticket ticket = new TicketDao().getById(ticketFromPriorQuery.getId());

### Bypass the cache
Sometimes you need to bypass the cache to get the latest version from Accelo.
To do this you need to use a filter and set it to refresh the cache by adding a call to 'refreshCache()'.

	filter.where(filter.against(AgainstType_.contract, contract.getId())
		.and(filter.after(Ticket.date_closed, dayBefore).or(filter.eq(Ticket_.date_closed, Expression.DATEZERO))))
		.refreshCache();

The following query will now flush the cache (just for this query) and refetch the data from the accelo server.

	List<Ticket> tickets = new TicketDao().getByFilter(filter);

# Initialise the Accelo Api

You need to create a json file which contains the Accelo REST API auth details. The json file must be on the class path and be called:

	secrets/accelosecrets.json
	
	a good spot is:
	
	src/main/resources/secrets/accelosecrets.json
	
The file must be formatted as:

	{"fqdn":"yourdomain.api.accelo.com", "client_id":"your acccelo client id", "client_secret":"your accelo client secret"}

	AcceloApi.getInstance().connect(AcceloSecret.load());
	
Once connected you are now ready to start making queries to the Accelo REST Api.

#### Get a company by name

    String companyName = "Some company name";
    Company company = new CompanyDao().getByName(companyName);

#### Get the Retainer contract

    Contract contract = new ContractDao().getActiveContract(company);

#### Get a list of contract periods for the retainer.

    List<ContractPeriod> periods = new ContractPeriodDao().getContractPeriods(contract);

#### Get a list of tickets attached to the contract.

    List<Ticket> tickets = new TicketDao().getByContract(contract);

#### Get a ticket by its no.

    Ticket ticket = new TicketDao().getById(123);

#### Get a Staff member by their email

    Staff staff = new StaffDao().getByEmail("staffmember@myorg.com.au");

#### Get a contact by their name

    Contact contact = new ContactDao().getContact("firstname", "lastname");

### Building Filters

The Accelo REST API uses Json for filters/sending and recieving data. 

The Noojee Accelo API frees you up from dealing with json instead using a fluent api to build your filters.

#### Create a fitler to match on a Staff email address:

	AcceloFilter<Staff> filter = new AcceloFilter<>();
	filter.where(filter.eq(Staff_.email, staffEmailAddress));

All filters start with a call to '.where' which then has a series of nested expresssions.
The expression can be combined using '.and' and '.or' methods with any level of nesting.

Once you have built a filter you pass the filter to the Dao class that handles the entities you want to return.

You will use getByFilter most of the time.

	AcceloFilter<Staff> filter = new AcceloFilter<>();
	filter.where(filter.eq(Staff_.email, staffEmailAddress));
	List<Staff> = new StaffDao().getByFilter(filter);

#### Get a staff member by id

	AcceloFilter<Staff> filter = new AcceloFilter<>();
	filter.where(filter.eq(Staff_.id, staff_id));
	List<Staff> = new StaffDao().getByFilter(filter);
	
of course in this simple case you should use the built in getById method that all Dao classes support.
	
	int staff_id = 1;
	Staff = new StaffDao().getById(staff_id);

#### Using the search filter

Accelo also supports 'search' criteria. A search criteria is essentally a full text search on the selected field.
You should note that on a small set of fields are supported by the search filters and for the most part those fields
can't be used in a normal filter.

Use the accelo 'search' filter get a company by name:

	AcceloFilter<Company> filter = new AcceloFilter<>();
	filter.where(new Search(companyName));
	List<Company> = new CompanyDao().getByFilter(filter);
	
Search filters can't be combined with other expressions such as .and .or Eq etc.

#### Filter using Against

Accelo's processing of the 'Against' field is just weird, so we have a special expression type called 'Against' to handle these.

Filter using Against expression field with a type of company and an company id.

	AcceloFilter<Company> filters = new AcceloFilter<>();
	filters.where(filters.against(AgainstType_.company, company.getId()));
	List<Company> = new CompanyDao().getByFilter(filter);

#### Additional examples
Search for tickets by company and contact id

	AcceloFilter<Ticket> filter = new AcceloFilter<>();
	filter.where(filter.against(AgainstType_.company, companyId))
		.and(filter.eq(Ticket_.contact_id, contactId));
	List<Ticket> = new TicketDao().getByFilter(filter);

Search for for two companies 

	AcceloFilter<Ticket> filter = new AcceloFilter<>();
	filter.where(filter.against(AgainstType_.company, 1))
		.or(filter.against(AgainstType_.company, 2));
	List<Ticket> = new CompanyDao().getByFilter(filter);
	
	
Fetch Tickets which are against (owned) by a company with id 1 or id 2.

	AcceloFilter<Ticket> filter = new AcceloFilter<>();
	filter.where(filter.against(AgainstType_.company, 1, 2)));
	List<Ticket> = new TicketDao().getByFilter(filter);   
