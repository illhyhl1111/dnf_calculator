package dnf_InterfacesAndExceptions;

@SuppressWarnings("serial")
public class UndefinedStatusKey extends Exception
{
	public UndefinedStatusKey(String key)
	{
		super("ERROR : Undefined Input for Status Key - "+key);
	}
}
