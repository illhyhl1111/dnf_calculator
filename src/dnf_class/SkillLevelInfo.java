package dnf_class;

import dnf_calculator.StatusList;

public class SkillLevelInfo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6250081996764840494L;
	public int skillLevel;
	public StatusList stat;
	public int phy_atk;
	public int mag_atk;
	public int phy_fix;
	public int mag_fix;
	
	public SkillLevelInfo(int level, int phy_atk, int phy_fix, int mag_atk, int mag_fix)
	{
		skillLevel=level;
		this.phy_atk=phy_atk;
		this.phy_fix=phy_fix;
		this.mag_atk=mag_atk;
		this.mag_fix=mag_fix;
		stat = new StatusList();
	}
}
