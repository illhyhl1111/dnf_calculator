package dnf_InterfacesAndExceptions;

public class ParsingException extends Exception
{
	private static final long serialVersionUID = 2485376205930965415L;

	public ParsingException(int index, Object object)
	{
		super("Parsing Error - index : "+index+", object : "+object.toString());
	}
}
