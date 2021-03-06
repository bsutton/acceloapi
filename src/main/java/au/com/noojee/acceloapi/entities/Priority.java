package au.com.noojee.acceloapi.entities;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Priority extends AcceloEntity<Priority>
{
	
	static public enum NoojeePriority
	{
		Critical(1), Urgent(2), Standard(3), Low(4), None(5);
		
		
		private int id;

		NoojeePriority(int id)
		{
			this.id = id;
		}

		public static NoojeePriority valueOf(long id)
		{
			for (NoojeePriority priority : NoojeePriority.values())
			{
				if (id == priority.id)
					return priority;
			}
			throw new IllegalArgumentException("Unknown Priority passed. Value: "  + id);
		}
		
		@Override
		public String toString()
		{
			return name();
		}

		public int getId()
		{
			return id;
		}
		
	}
	
	// In order of increasing urgency
	public enum Color { grey, blue, green, orange, red };
	
	@SuppressFBWarnings
	private String title;	// A name for the priority.
	@SuppressFBWarnings
	private Color color;		// The color of the priority when displayed on the deployment. The colors, in order of increasing urgency a “grey”, “blue”, “green”, “orange”, “red”.
	private int factor;		// A number representing the urgency of the priority. 5 is “Extreme”, 1 is “None”.
	



	public String getTitle()
	{
		return title;
	}


	
	public Color getColor()
	{
		return color;
	}


	public int getFactor()
	{
		return factor;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Priority [title=" + title + ", color=" + color + ", factor=" + factor + ", getId()=" + getId() + "]";
	}



	


}
