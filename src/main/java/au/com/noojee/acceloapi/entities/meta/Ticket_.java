package au.com.noojee.acceloapi.entities.meta;

/** 
 *
 *          DO NOT MODIFY 
 *
 * This code is generated by au.com.noojee.acceloapi.entities.generator.FieldMetaDataGenerator
 *
 * The generator use @AcceloField annotations to determine what fields to include in the Meta data.
 *
 *          DO NOT MODIFY 
 *
 */
import au.com.noojee.acceloapi.entities.meta.fieldTypes.FilterField;
import java.time.LocalDate;
import au.com.noojee.acceloapi.entities.Priority.NoojeePriority;
import java.time.LocalDateTime;
import au.com.noojee.acceloapi.entities.Ticket;
import au.com.noojee.acceloapi.entities.types.EntityStatus;
import au.com.noojee.acceloapi.entities.Ticket.Standing;


public class Ticket_ 
{

	public static FilterField<Ticket, Integer> _class = new FilterField<>("class"); 
	public static FilterField<Ticket, Integer> affiliation = new FilterField<>("affiliation"); 
	public static FilterField<Ticket, Integer> assignee = new FilterField<>("assignee"); 
	public static FilterField<Ticket, Integer> closed_by = new FilterField<>("closed_by"); 
	public static FilterField<Ticket, String> contact_number = new FilterField<>("contact_number"); 
	public static FilterField<Ticket, Integer> contract = new FilterField<>("contract"); 
	public static FilterField<Ticket, String> custom_id = new FilterField<>("custom_id"); 
	public static FilterField<Ticket,LocalDateTime>date_closed = new FilterField<>("date_closed"); 
	public static FilterField<Ticket,LocalDate>date_due = new FilterField<>("date_due"); 
	public static FilterField<Ticket,LocalDateTime>date_opened = new FilterField<>("date_opened"); 
	public static FilterField<Ticket,LocalDateTime>date_started = new FilterField<>("date_started"); 
	public static FilterField<Ticket,LocalDateTime>date_submitted = new FilterField<>("date_submitted"); 
	public static FilterField<Ticket, Integer> id = new FilterField<>("id"); 
	public static FilterField<Ticket, NoojeePriority> issue_priority = new FilterField<>("issue_priority"); 
	public static FilterField<Ticket, EntityStatus> issue_status = new FilterField<>("issue_status"); 
	public static FilterField<Ticket, Integer> issue_type = new FilterField<>("issue_type"); 
	public static FilterField<Ticket, Integer> opened_by = new FilterField<>("opened_by"); 
	public static FilterField<Ticket, Integer> referrer_id = new FilterField<>("referrer_id"); 
	public static FilterField<Ticket, String> referrer_type = new FilterField<>("referrer_type"); 
	public static FilterField<Ticket, Integer> resolution = new FilterField<>("resolution"); 
	public static FilterField<Ticket, Integer> resolved_by = new FilterField<>("resolved_by"); 
	public static FilterField<Ticket, Standing> standing = new FilterField<>("standing"); 
	public static FilterField<Ticket, Integer> submitted_by = new FilterField<>("submitted_by"); 

}
