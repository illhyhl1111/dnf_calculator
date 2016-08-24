package dnf_calculator;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class DoubleStatusInfo extends AbstractStatusInfo
{
	private double str;
	public DoubleStatusInfo(int strength)
	{
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	public void setInfo(double strength) {str=strength;}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Double");}
	public double getStatToDouble() {return str;}
}
