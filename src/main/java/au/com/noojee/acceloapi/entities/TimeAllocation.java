package au.com.noojee.acceloapi.entities;

import java.time.Duration;
import java.time.LocalDateTime;

import org.joda.money.Money;

import au.com.noojee.acceloapi.entities.meta.fieldTypes.BasicFilterField;
import au.com.noojee.acceloapi.entities.meta.fieldTypes.DateFilterField;
import au.com.noojee.acceloapi.entities.types.AgainstType;
import au.com.noojee.acceloapi.util.Conversions;

public class TimeAllocation extends AcceloEntity<TimeAllocation>
{
	
	@BasicFilterField
	private String standing;	// string	The standing of the logged time.
	private Long billable;		// integer	The amount of billable time logged, in seconds.
	private Long nonbillable;	// integer	The amount of nonbillable time logged, in seconds.
	private float charged;		// decimal	The rate charged for billable work.
	private String comments;	// string	Any comments made against the logged time.
	
	private AgainstType against_type;
	private int against_id;
	
	@DateFilterField
	private LocalDateTime date_locked ;	// unix ts	The date the activity was locked, that is, when the logged time was approved for invoicing.
	@DateFilterField
	private LocalDateTime date_created ;	// unix ts	The date the time was logged.
	
	
	public String getStanding()
	{
		return standing;
	}
	
	public Duration getBillable()
	{
		return Duration.ofSeconds(billable);
	}
	
	public Duration getNonbillable()
	{
		return Duration.ofSeconds(nonbillable);
	}
	
	public Money getCharged()
	{
		return Conversions.asMoney(charged);
	}
	
	public String getComments()
	{
		return comments;
	}
	
	public LocalDateTime getDateTimeLocked()
	{
		return date_locked;
	}
	
	public LocalDateTime getDateTimeCreated()
	{
		return date_created;
	}

	public AgainstType getAgainst_type()
	{
		return against_type;
	}

	public void setAgainstType(AgainstType againsType, int againstId)
	{
		this.against_type = againsType;
		this.against_id = againstId;
	}

	public int getAgainst_id()
	{
		return against_id;
	}

	@Override
	public String toString()
	{
		return "TimeAllocation [standing=" + standing + ", billable=" + billable + ", nonbillable=" + nonbillable
				+ ", charged=" + charged + ", comments=" + comments + ", against_type=" + against_type + ", against_id="
				+ against_id + ", date_locked=" + date_locked + ", date_created=" + date_created + "]";
	}
}
