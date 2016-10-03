package dnf_calculator;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class DoubleStatusInfo extends AbstractStatusInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4391339946726397345L;
	private double str;
	public DoubleStatusInfo(double strength)
	{
		str=strength;
	}
	public DoubleStatusInfo(int strength)
	{
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	public void setInfo(double strength) {str=strength;}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Double");}
	public double getStatToDouble() {return str;}
	public String getStatToString() throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->Double");}
	public void setInfo(String name) throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->Double");}
	public void setInfo(int start, int end) throws StatusTypeMismatch { throw new StatusTypeMismatch("SkillRange->Double");}
}
