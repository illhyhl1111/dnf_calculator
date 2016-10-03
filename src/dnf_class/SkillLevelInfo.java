package dnf_class;

import dnf_calculator.FunctionStatusList;
import dnf_calculator.StatusList;

public class SkillLevelInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6250081996764840494L;
	public int skillLevel;
	public StatusList stat;
	public FunctionStatusList fStat;
	public int phy_atk;
	public int mag_atk;
	public double phy_fix;
	public double mag_fix;
	public boolean fromDictionary;
	
	public SkillLevelInfo(int level, int phy_atk, double phy_fix, int mag_atk, double mag_fix)
	{
		skillLevel=level;
		this.phy_atk=phy_atk;
		this.phy_fix=phy_fix;
		this.mag_atk=mag_atk;
		this.mag_fix=mag_fix;
		stat = new StatusList();
		fStat = new FunctionStatusList();
		fromDictionary=true;
	}
	
	public SkillLevelInfo(int level)
	{
		skillLevel=level;
		this.phy_atk=0;
		this.phy_fix=0;
		this.mag_atk=0;
		this.mag_fix=0;
		stat = new StatusList();
		fStat = new FunctionStatusList();
		fromDictionary=true;
	}
	
	public boolean hasPhy_per(){
		if(phy_atk==0) return false;
		return true;
	}
	public boolean hasPhy_fix(){
		if(phy_fix==0) return false;
		return true;
	}
	public boolean hasMag_per(){
		if(mag_atk==0) return false;
		return true;
	}
	public boolean hasMag_fix(){
		if(mag_fix==0) return false;
		return true;
	}
}

class TPSkillLevelInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4333299195866799759L;
	public int skillLevel;
	public int increase;
	
	public TPSkillLevelInfo(int level, int increase)
	{
		this.skillLevel=level;
		this.increase=increase;
	}
	
}