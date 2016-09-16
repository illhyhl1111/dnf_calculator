package dnf_calculator;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class SkillStatusInfo extends AbstractStatusInfo			// 스킬정보 저장 class
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6929844926351368607L;
	private int str;
	private String name;
	public SkillStatusInfo(int strength, String name)
	{	
		this.name=name;
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	public void setInfo(double strength) { str=(int)strength;}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Integer");}
	public double getStatToDouble() {return (double)str;}
	public String getStatToString() {return name;}
	public void setInfo(String name) { this.name=name;}
	public void setInfo(int start, int end) throws StatusTypeMismatch { throw new StatusTypeMismatch("SkillRange->Skill");}
}