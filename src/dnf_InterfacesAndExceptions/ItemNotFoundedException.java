package dnf_InterfacesAndExceptions;

public class ItemNotFoundedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4287003492623109943L;
	
	public ItemNotFoundedException(String name)
	{
		super("Item "+name+" not found");
	}
}
