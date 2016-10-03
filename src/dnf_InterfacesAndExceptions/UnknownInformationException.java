package dnf_InterfacesAndExceptions;

public class UnknownInformationException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4082226893909837199L;

	public UnknownInformationException()
	{
		super("unknown information required");
	}
}
