package smw.infinity;

import java.io.Serializable;

public final class Version implements Serializable
{
	private static final long serialVersionUID = -3418229852179577302L;
	
	public static final byte CURRENT_REVISION_MINOR = 1;
	public static final byte CURRENT_REVISION_MAJOR = 0;
	public static final char CURRENT_BUILD = 'A';

	private byte minorRevision, majorRevision;
	private char build;
	
	public Version(byte minorRevision, byte majorRevision, char build)
	{
		this.minorRevision = minorRevision;
		this.majorRevision = majorRevision;
		this.build = build;
	}
	
	public byte getMajorRevision()
	{
		return majorRevision;
	}
	
	public byte getMinorRevision()
	{
		return minorRevision;
	}
	
	public char getBuild()
	{
		return build;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + build;
		result = prime * result + majorRevision;
		result = prime * result + minorRevision;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(getClass() != obj.getClass())
			return false;
		
		Version other = (Version) obj;
		
		if(build != other.build)
			return false;
		
		if(majorRevision != other.majorRevision)
			return false;
		
		if(minorRevision != other.minorRevision)
			return false;
		
		return true;
	}

	@Override
	public String toString()
	{
		return (majorRevision + "." + minorRevision + build);
	}
}