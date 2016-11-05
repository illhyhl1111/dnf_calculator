package dnf_InterfacesAndExceptions;

public class UndefinedStatusKey extends Exception
{
	private static final long serialVersionUID = 822726563909899071L;

	public UndefinedStatusKey(String key)
	{
		super("ERROR : Undefined Input for Status Key - "+key);
	}
}
