package dnf_InterfacesAndExceptions;

@SuppressWarnings("serial")
public class ParsingException extends Exception
{
	public ParsingException(int index, Object object)
	{
		super("Parsing Error - index : "+index+", object : "+object.toString());
	}
}
