package dnf_calculator;

import dnf_calculator.AbstractStatusInfo;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class StatusInfo extends AbstractStatusInfo			// int형 스탯정보 저장 class
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2025655576575273141L;
	private int str;										// private으로 바꿔야하지만 몰라 귀찮다 그냥 조심해야지
	public StatusInfo(int strength)
	{	
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	public void setInfo(double strength) { str=(int)strength;}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Integer");}
	public double getStatToDouble() {return (double)str;}
	public String getStatToString() throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->Integer");}
	public void setInfo(String name) throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->Integer");}
	public void setInfo(int start, int end) throws StatusTypeMismatch { throw new StatusTypeMismatch("SkillRange->Integer");}
}