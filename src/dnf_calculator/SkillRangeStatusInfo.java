package dnf_calculator;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class SkillRangeStatusInfo extends AbstractStatusInfo			// 범위형 스킬정보 저장 class
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1901149762006182983L;
	private int str;
	private int startRange;
	private int endRange;
	private boolean TP;
	public SkillRangeStatusInfo(int strength, int start, int end, boolean TP)
	{	
		str=strength;
		startRange=start;
		endRange=end;
		this.TP=TP;
	}
	public SkillRangeStatusInfo(int strength, int start, boolean TP)
	{	
		str=strength;
		startRange=start;
		endRange=start;
		this.TP=TP;
	}
	
	public void setInfo(int strength) { str=strength;}
	public void setInfo(double strength) { str=(int)strength;}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Integer");}
	public double getStatToDouble() {return (double)str;}
	public String getStatToString() {
		if(startRange==endRange) return ""+startRange;
		return ""+startRange+" ~ "+endRange;
	}
	public void setInfo(String name) throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->SkillRange");}
	public void setInfo(int start, int end)
	{
		startRange=start;
		endRange=end;
	}
	
	public boolean getTP() {return TP;}
}