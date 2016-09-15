package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.SkillInfoNotFounded;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_calculator.StatusList;

public class Skill implements java.io.Serializable{
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
				return;
			}
		}
		throw new SkillInfoNotFounded(name, skillLevel);
	}
	
	public int skillMaster(int charLevel)
	{
		skillLevel = (int)((charLevel-firstLevel)/levelInterval);
		if(skillLevel>masterLevel) skillLevel = masterLevel;
		return skillLevel;
	}
	
	public int getSkillLevel() { return skillLevel;}
	public void setSkillLevel(int skillLevel) {this.skillLevel=skillLevel;}
	public void increaseLevel()
	{
		if(skillLevel<masterLevel) skillLevel++;
	}
	public void increaseLevel(int inc)
	{
		if(skillLevel+inc<=masterLevel) skillLevel+=inc;
	}
}
