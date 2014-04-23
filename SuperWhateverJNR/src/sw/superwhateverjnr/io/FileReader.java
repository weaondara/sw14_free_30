package sw.superwhateverjnr.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileReader
{
	private final DataInputStream in;
	private final InputStream is;
	public FileReader(File f)
	{
		super();
		try
		{
			this.is = new FileInputStream(f);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		in=new DataInputStream(is);
	}
	public FileReader(InputStream is)
	{
		super();
		this.is = is;
		in=new DataInputStream(is);
	}
	
	public String readString()
    {
        try
        {
            return in.readUTF();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public boolean readBool()
    {
        try
        {
            return in.readBoolean();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public byte readByte()
    {
        try
        {
            return in.readByte();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public char readChar()
    {
        try
        {
            return in.readChar();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public double readDouble()
    {
        try
        {
            return in.readDouble();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public float readFloat()
    {
        try
        {
            return in.readFloat();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public int readInt()
    {
        try
        {
            return in.readInt();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public long readLong()
    {
        try
        {
            return in.readLong();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    public short readShort()
    {
        try
        {
            return in.readShort();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
