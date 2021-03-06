package au.com.noojee.acceloapi.filter;

import java.time.LocalDate;

import au.com.noojee.acceloapi.entities.AcceloEntity;
import au.com.noojee.acceloapi.entities.meta.fieldTypes.FilterField;

class Empty<E extends AcceloEntity<E>> extends Expression
{
	private FilterField<E, LocalDate> field;

	public Empty(FilterField<E, LocalDate> field)
	{
		this.field = field;
	}
	
	@Override
	public Expression copy()
	{
		Empty<E> empty = new Empty<>(this.field);
		
		return empty;
	}


	@Override
	public String toJson()
	{
		String json = "\"empty\": [";
		json += "\"" + field.getFieldName() + "\"";

		json += "]";

		return json;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Empty other = (Empty) obj;
		if (field == null)
		{
			if (other.field != null)
				return false;
		}
		else if (!field.equals(other.field))
			return false;
		return true;
	}
}