package dnf_class;

import java.util.Iterator;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusList;

public class Skill extends IconObject implements Comparable<Skill>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6419035744147753371L;
	public Skill_type type;
	private int skillLevel;
	public final int firstLevel;
	public final int maxLevel;
	public final int masterLevel;
	public final int levelInterval;
	public final JobList job;
	public final Character_type char_type;
	
	public LinkedList<SkillLevelInfo> skillInfo;
	
	public boolean active_enabled;
	public boolean buff_enabled;
	
	public int villageLevel;
	public double villageIncrease;
	public int dungeonLevel;
	public double dungeonIncrease;
	
	public Skill(String name, Skill_type type, String icon, JobList job, int firstLevel, int maxLevel, int masterLevel, int levelInterval)
	{
		super();
		this.name=name;
		this.type=type;
		this.iconAddress=icon;
		this.firstLevel=firstLevel;
		this.maxLevel=maxLevel;
		this.masterLevel=masterLevel;
		this.levelInterval=levelInterval;
		this.job=job;
		char_type=null;
	
		skillInfo = new LinkedList<SkillLevelInfo>();
		skillInfo.add(new SkillLevelInfo(0));
		active_enabled = true;
		if(type==Skill_type.PASSIVE || type==Skill_type.TP) buff_enabled = true;
		else buff_enabled = false;
		
		this.villageLevel=0;
		this.villageIncrease=1;
		this.dungeonLevel=0;
		this.dungeonIncrease=1;
	}
	
	public Skill(String name, Skill_type type, String icon, Character_type charType, int firstLevel, int maxLevel, int masterLevel, int levelInterval)
	{
		this.name=name;
		this.type=type;
		this.iconAddress=icon;
		this.firstLevel=firstLevel;
		this.maxLevel=maxLevel;
		this.masterLevel=masterLevel;
		this.levelInterval=levelInterval;
		job=null;
		char_type=charType;
		
		skillInfo = new LinkedList<SkillLevelInfo>();
		skillInfo.add(new SkillLevelInfo(0));
		active_enabled = true;
		if(type==Skill_type.PASSIVE || type==Skill_type.TP) buff_enabled = true;
		else buff_enabled = false;
		
		this.villageLevel=0;
		this.villageIncrease=1;
		this.dungeonLevel=0;
		this.dungeonIncrease=1;
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
		if(type==Skill_type.PASSIVE || type==Skill_type.TP) return false;
		else return true;
	}
	public boolean isTPSkill()
	{
		if(type==Skill_type.TP) return true;
		else return false;
	}

	private SkillLevelInfo getSkillInfo(int level)
	{
		if(skillLevel==0) level=0;
		
		for(SkillLevelInfo info : skillInfo)
		{
			if(info.skillLevel==level){
				return info;
			}
		}
		
		SkillLevelInfo temp = new SkillLevelInfo(level);
		Iterator<SkillLevelInfo> iter = skillInfo.descendingIterator();
		SkillLevelInfo hSkill = iter.next();
		SkillLevelInfo h2Skill = iter.next();
		
		int levelDifference = level-hSkill.skillLevel;
		int diff = hSkill.skillLevel-h2Skill.skillLevel;
		
		temp.phy_atk=(hSkill.phy_atk-h2Skill.phy_atk)*levelDifference/diff;
		temp.phy_fix=(hSkill.phy_fix-h2Skill.phy_fix)*levelDifference/diff;
		temp.mag_atk=(hSkill.mag_atk-h2Skill.mag_atk)*levelDifference/diff;
		temp.mag_fix=(hSkill.mag_fix-h2Skill.mag_fix)*levelDifference/diff;
		
		if(this.hasBuff())
		{
			Iterator<StatusAndName> iter1 = hSkill.stat.statList.iterator();
			Iterator<StatusAndName> iter2 = h2Skill.stat.statList.iterator();
			for(StatusAndName s : temp.stat.statList)
			{
				StatusAndName s1 = iter1.next();
				StatusAndName s2 = iter2.next();
				
				try {
					s.stat.setInfo((s1.stat.getStatToDouble()-s2.stat.getStatToDouble())*levelDifference/diff);
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
			}
		}
		temp.fromDictionary=false;
		return temp;
	}
	
	public int masterSkill(int charLevel)
	{
		skillLevel = (int)((charLevel-firstLevel)/levelInterval);
		if(skillLevel>masterLevel) skillLevel = masterLevel;
		return skillLevel;
	}
	
	public int getMasterSkillLevel(int charLevel)
	{
		int skillLevel = (int)((charLevel-firstLevel)/levelInterval);
		if(skillLevel>masterLevel) skillLevel = masterLevel;
		return skillLevel;
	}
	
	public int getSkillLevel() { return skillLevel;}
	public void setSkillLevel(int skillLevel){
		this.skillLevel=skillLevel;
	}
	
	public void increaseLevel_char()
	{
		if(skillLevel<masterLevel)
			skillLevel++;
	}
	public void decreaseLevel_char()
	{
		if(skillLevel>0)
			skillLevel--;
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
	
	public boolean isInRange(int startLevel, int endLevel, boolean TPSkill)
	{
		if(startLevel<=firstLevel && firstLevel<=endLevel && TPSkill==isTPSkill()) return true;
		return false;
	}
	public boolean isInRange(int startLevel, int endLevel)
	{
		if(startLevel<=firstLevel && firstLevel<=endLevel) return true;
		return false;
	}

	@Override
	public int compareTo(Skill arg0) {
		if(arg0.firstLevel!=firstLevel) return firstLevel-arg0.firstLevel;
		else return name.compareTo(arg0.name);
	}
	
	@Override
	public Object clone()
	{
		Skill temp=null;
		try {
			temp = (Skill) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public SkillLevelInfo getSkillLevelInfo(boolean isDungeon)
	{
		int level = skillLevel;
		double increase;
		if(isDungeon){
			level+=dungeonLevel;
			increase=dungeonIncrease;
		}
		else{
			level+=villageLevel;
			increase=villageIncrease;
		}
		SkillLevelInfo temp = getSkillInfo(level);
		
		SkillLevelInfo returnValue = new SkillLevelInfo(level, (int)(temp.phy_atk*increase), temp.phy_fix*increase, (int)(temp.mag_atk*increase), temp.mag_fix*increase);
		try {
			returnValue.stat=(StatusList) temp.stat.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
}
