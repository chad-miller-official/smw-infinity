package smw.infinity;

import java.io.Serializable;

public final class Version implements Serializable
{
	private static final long serialVersionUID = -3418229852179577302L;
	
	public static final byte CURRENT_VERSION = 1;	//0.1
	public static final char CURRENT_BUILD = 'A';

	/* First four bits represents major revision; last four bits represents minor revision */
	private byte version;
	
	/* Letters A-Z, as a number corresponding to its position in the ASCII table */
	private char build;
	
	public Version(byte version, char build)
	{
		this.version = version;
		this.build = build;
	}
	
	public byte getMajorRevision()
	{
		return (byte) ((version & 0xF0) >>> 4);
	}
	
	public byte getMinorRevision()
	{
		return (byte) (version & 0xF);
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
		result = prime * result + version;
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
		
		if(version != other.version)
			return false;
		
		return true;
	}
	
	@Override
	public String toString()
	{
		return (getMajorRevision() + "." + getMinorRevision() + getBuild());
	}
}