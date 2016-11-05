package dnf_InterfacesAndExceptions;

public class StatusTypeMismatch extends Exception
{
	private static final long serialVersionUID = 553604655563758066L;

	public StatusTypeMismatch(String targetType)
	{
		super("ERROR : Status Type Mismatch - Target Type : "+targetType);
	}
}
