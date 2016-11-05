package dnf_calculator;

import dnf_calculator.AbstractStatusInfo;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class LongStatusInfo extends AbstractStatusInfo			// int형 스탯정보 저장 class
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6319519661345465470L;
	private long str;										// private으로 바꿔야하지만 몰라 귀찮다 그냥 조심해야지
	public LongStatusInfo(long strength)
	{	
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	public void setInfo(double strength) { str=Math.round(strength);}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Long");}
	public void setInfo(long strength) { str=strength;}
	public double getStatToDouble() {return (double)str;}
	public String getStatToString() throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->Long");}
	public void setInfo(String name) throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->Long");}
	public void setInfo(int start, int end) throws StatusTypeMismatch { throw new StatusTypeMismatch("SkillRange->Long");}
	public void increaseStat(double strength) {str=(int) (str*(strength+100.0)/100.0);}
}