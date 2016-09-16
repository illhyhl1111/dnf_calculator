package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.SkillInfoNotFounded;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_calculator.StatusList;

public class Skill implements java.io.Serializable, Comparable<Skill>, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6419035744147753371L;
	public Skill_type type;
	public final String name;
	public final String iconAdress;
	private int skillLevel;
	public final int firstLevel;
	public final int maxLevel;
	public final int masterLevel;
	public final int levelInterval;
	public final JobList job;
	public final Character_type char_type;
	
	public StatusList stat;
	public int phy_atk;
	public int mag_atk;
	public int phy_fix;
	public int mag_fix;
	public LinkedList<SkillLevelInfo> skillInfo;
	
	public boolean active_enabled;
	public boolean buff_enabled;
	
	public Skill(String name, Skill_type type, String icon, JobList job, int firstLevel, int maxLevel, int masterLevel, int levelInterval)
	{
		this.name=name;
		this.type=type;
		iconAdress=icon;
		this.firstLevel=firstLevel;
		this.maxLevel=maxLevel;
		this.masterLevel=masterLevel;
		this.levelInterval=levelInterval;
		this.job=job;
		char_type=null;
		
		stat=null;
		phy_atk=0;
		mag_atk=0;
		phy_fix=0;
		mag_fix=0;
		skillInfo = new LinkedList<SkillLevelInfo>();
		active_enabled = true;
		if(type==Skill_type.PASSIVE) buff_enabled = true;
		else buff_enabled = false;
	}
	
	public Skill(String name, Skill_type type, String icon, Character_type charType, int firstLevel, int maxLevel, int masterLevel, int levelInterval)
	{
		this.name=name;
		this.type=type;
		iconAdress=icon;
		this.firstLevel=firstLevel;
		this.maxLevel=maxLevel;
		this.masterLevel=masterLevel;
		this.levelInterval=levelInterval;
		job=null;
		char_type=charType;
		
		stat=null;
		phy_atk=0;
		mag_atk=0;
		phy_fix=0;
		mag_fix=0;
		skillInfo = new LinkedList<SkillLevelInfo>();
		active_enabled = true;
		if(type==Skill_type.PASSIVE) buff_enabled = true;
		else buff_enabled = false;
	}
	
	public boolean isSkillOfChar(JobList job)
	{
		if(this.job!=null) return (this.job==job);
		else {
			if(char_type==Character_type.ALL) return true;
			else return (char_type==job.charType);
		}
	}
	
	public boolean hasBuff()
	{
		if(type==Skill_type.ACTIVE) return false;
		else return true;
	}
	public boolean hasDamage()
	{
		if(type==Skill_type.PASSIVE) return false;
		else return true;
	}
	
	public void getSkillInfo(int skillLevel) throws SkillInfoNotFounded
	{
		this.skillLevel=skillLevel;
		getSkillInfo();
	}
	public void getSkillInfo() throws SkillInfoNotFounded
	{
		for(SkillLevelInfo info : skillInfo)
		{
			if(info.skillLevel==skillLevel){
				phy_atk=info.phy_atk;
				phy_fix=info.phy_fix;
				mag_atk=info.mag_atk;
				mag_fix=info.mag_fix;
				stat=info.stat;
				return;
			}
		}
		throw new SkillInfoNotFounded(name, skillLevel);
	}
	
	public int masterSkill(int charLevel) throws SkillInfoNotFounded
	{
		skillLevel = (int)((charLevel-firstLevel)/levelInterval);
		if(skillLevel>masterLevel) skillLevel = masterLevel;
		getSkillInfo();
		return skillLevel;
	}
	
	public int getSkillLevel() { return skillLevel;}
	public void setSkillLevel(int skillLevel) throws SkillInfoNotFounded {
		this.skillLevel=skillLevel;
		getSkillInfo();
	}
	public void increaseLevel() throws SkillInfoNotFounded
	{
		if(skillLevel<masterLevel) skillLevel++;
		getSkillInfo();
	}
	public void increaseLevel(int inc) throws SkillInfoNotFounded
	{
		if(skillLevel+inc<=masterLevel) skillLevel+=inc;
		getSkillInfo();
	}
	
	public boolean getActiveEnabled() {return active_enabled;}
	public void setActiveEnabled(boolean enabled){this.active_enabled=enabled;}
	public boolean getBuffEnabled() {return buff_enabled;}
	public void setBuffEnabled(boolean enabled){this.buff_enabled=enabled;}
	
	public boolean isEqualTo(String name)
	{
		if (this.name.equals(name)) return true;
		return false;
	}
	
	public boolean isInRange(int startLevel, int endLevel)
	{
		if(startLevel<=firstLevel && firstLevel<=endLevel) return true;
		return false;
	}

	@Override
	public int compareTo(Skill arg0) {
		if(arg0.skillLevel!=skillLevel) return skillLevel-arg0.skillLevel;
		else return arg0.name.compareTo(name);
	}
	
	@Override
	public Object clone()
	{
		Skill temp=null;
		try {
			temp = (Skill) super.clone();
			temp.stat = (StatusList) this.stat.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}
}
