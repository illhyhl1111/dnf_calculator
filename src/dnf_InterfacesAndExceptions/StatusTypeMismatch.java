package dnf_InterfacesAndExceptions;

@SuppressWarnings("serial")
public class StatusTypeMismatch extends Exception
{
	public StatusTypeMismatch(String targetType)
	{
		super("ERROR : Status Type Mismatch - Target Type : "+targetType);
	}
}
