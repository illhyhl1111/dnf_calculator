package dnf_calculator;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class SkillStatusInfo extends AbstractStatusInfo			// 스킬정보 저장 class
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6929844926351368607L;
	private int level;
	private double increase;
	private String name;
	public SkillStatusInfo(int level, double increase, String name)
	{	
		this.name=name;
		this.level=level;
		this.increase=increase;
	}
	
	public void setInfo(int strength) { level=strength;}
	public void setInfo(double strength) { level=(int)Math.round(strength);}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Integer");}
	public double getStatToDouble() {return (double)level;}
	public String getStatToString() {return name;}
	public void setInfo(String name) { this.name=name;}
	public void setInfo(int start, int end) throws StatusTypeMismatch { throw new StatusTypeMismatch("SkillRange->Skill");}
	
	public double getIncrease() {return increase;}
	public void setIncrease(double increase) { this.increase=increase;}
	public void increaseStat(double strength) {increase = increase*strength;}
}