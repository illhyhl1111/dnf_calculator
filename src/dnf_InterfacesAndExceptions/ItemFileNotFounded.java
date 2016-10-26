package dnf_InterfacesAndExceptions;

public class ItemFileNotFounded extends Exception
{
	private static final long serialVersionUID = -7653434088617290628L;

	public ItemFileNotFounded(String str)
	{
		super(str+" - not found");
	}
}
