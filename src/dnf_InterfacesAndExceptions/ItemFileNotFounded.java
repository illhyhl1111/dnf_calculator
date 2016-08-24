package dnf_InterfacesAndExceptions;

@SuppressWarnings("serial")
public class ItemFileNotFounded extends Exception
{
	public ItemFileNotFounded(String str)
	{
		super(str+" - not found");
	}
}
