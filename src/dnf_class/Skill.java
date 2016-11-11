package dnf_class;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_calculator.FunctionStatusList;
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
	public final Job job;
	public final Character_type char_type;
	public final Element_type element;
	public LinkedList<String> explanation;						//설명
	
	public LinkedList<SkillLevelInfo> skillInfo;
	
	private boolean active_enabled;
	private boolean buff_enabled;
	
	public int villageLevel;
	public double villageIncrease;
	public int dungeonLevel;
	public double dungeonIncrease;
	
	public String Version;
	
	public Skill(String name, Skill_type type, Job job, int firstLevel, int maxLevel, int masterLevel, int levelInterval, Element_type element, String version)
	{
		super();
		this.setName(name);
		this.setIcon("image\\Skill\\"+job.charType.name()+"\\"+name+".png");
		this.type=type;
		this.firstLevel=firstLevel;
		this.maxLevel=maxLevel;
		this.masterLevel=masterLevel;
		this.levelInterval=levelInterval;
		this.job=job;
		this.element=element;
		char_type=null;
	
		skillInfo = new LinkedList<SkillLevelInfo>();
		skillInfo.add(new SkillLevelInfo(0));
		active_enabled = true;
		buff_enabled = false;
		
		this.villageLevel=0;
		this.villageIncrease=1;
		this.dungeonLevel=0;
		this.dungeonIncrease=1;
		
		explanation = new LinkedList<String>();
		
		Version=version;
	}
	
	public Skill(String name, Skill_type type, Character_type charType, int firstLevel, int maxLevel, int masterLevel, int levelInterval, Element_type element, String version)
	{
		super();
		this.setName(name);
		this.setIcon("image\\Skill\\COMMON\\"+name+".png");
		this.type=type;
		this.firstLevel=firstLevel;
		this.maxLevel=maxLevel;
		this.masterLevel=masterLevel;
		this.levelInterval=levelInterval;
		this.element=element;
		job=null;
		char_type=charType;
		
		skillInfo = new LinkedList<SkillLevelInfo>();
		skillInfo.add(new SkillLevelInfo(0));
		active_enabled = true;
		buff_enabled = false;
		
		this.villageLevel=0;
		this.villageIncrease=1;
		this.dungeonLevel=0;
		this.dungeonIncrease=1;
		
		explanation = new LinkedList<String>();
		Version=version;
	}
	
	public Skill() {
		super();
		firstLevel=0;
		maxLevel=0;
		masterLevel=0;
		levelInterval=0;
		job = null;
		char_type=Character_type.ALL;
		element=Element_type.NONE;
		
		explanation = new LinkedList<String>();
		Version = CalculatorVersion.DEFAULT;
	}

	public boolean isSkillOfChar(Job job)
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
		if(type==Skill_type.ACTIVE || type==Skill_type.DAMAGE_BUF || type==Skill_type.OPTION || type==Skill_type.SUBSKILL) return true;
		else if(type==Skill_type.SWITCHING){
			if(skillInfo.getLast().hasPhy_per()) return true;
			if(skillInfo.getLast().hasPhy_fix()) return true;
			if(skillInfo.getLast().hasMag_per()) return true;
			if(skillInfo.getLast().hasMag_fix()) return true;
			return false;
		}
		else return false;
	}
	public boolean isTPSkill()
	{
		if(type==Skill_type.TP) return true;
		else return false;
	}
	public boolean isOptionSkill()
	{
		if(type==Skill_type.OPTION) return true;
		else return false;
	}
	public boolean isSubSkill()
	{
		if(type==Skill_type.SUBSKILL) return true;
		else return false;
	}
	public boolean isEnableable()
	{
		if(type==Skill_type.BUF_ACTIVE || type==Skill_type.DAMAGE_BUF || type==Skill_type.SWITCHING || type==Skill_type.OPTION) return true;
		else return false;
	}
	
	public boolean buffEnabled(boolean isDungeon)
	{
		if((type==Skill_type.PASSIVE || type==Skill_type.TP) && active_enabled) return true;
		else if(isEnableable() && buff_enabled && isDungeon) return true;
		return false;
	}

	private SkillLevelInfo getSkillInfo(int level)
	{	
		for(SkillLevelInfo info : skillInfo)
		{
			if(info.skillLevel==level){
				return info;
			}
		}
		System.out.println(getName()+" Lv "+ level+" 스킬정보 없음");
		
		SkillLevelInfo temp = new SkillLevelInfo(level);
		Iterator<SkillLevelInfo> iter = skillInfo.descendingIterator();
		SkillLevelInfo hSkill = iter.next();
		SkillLevelInfo h2Skill = iter.next();
		
		int levelDifference = level-hSkill.skillLevel;
		int diff = hSkill.skillLevel-h2Skill.skillLevel;
		
		temp.phy_atk=hSkill.phy_atk+(hSkill.phy_atk-h2Skill.phy_atk)*levelDifference/diff;
		temp.phy_fix=hSkill.phy_fix+(hSkill.phy_fix-h2Skill.phy_fix)*levelDifference/diff;
		temp.mag_atk=hSkill.mag_atk+(hSkill.mag_atk-h2Skill.mag_atk)*levelDifference/diff;
		temp.mag_fix=hSkill.mag_fix+(hSkill.mag_fix-h2Skill.mag_fix)*levelDifference/diff;
		temp.indep_level=hSkill.indep_level+(hSkill.indep_level-h2Skill.indep_level)*levelDifference/diff;
		
		for(Entry<String, Integer> entry : hSkill.percentList.entrySet()){
			int v1 = entry.getValue();
			int v2 = h2Skill.percentList.get(entry.getKey());
			temp.percentList.put(entry.getKey(), v1+(v1-v2)*levelDifference/diff);
		}
		
		
		try {
			temp.stat = (StatusList) hSkill.stat.clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		
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
	
	public int masterSkill(int charLevel, boolean hasContract)
	{
		if(hasContract) charLevel+=5;
		skillLevel = (int)((charLevel-firstLevel)/levelInterval)+1;
		if(skillLevel>masterLevel) skillLevel = masterLevel;
		active_enabled=true;
		return skillLevel;
	}
	
	public int getMasterSkillLevel(int charLevel)
	{
		int skillLevel = (int)((charLevel-firstLevel)/levelInterval)+1;
		if(skillLevel>masterLevel) skillLevel = masterLevel;
		return skillLevel;
	}
	
	public int getCharSkillLevel() { return skillLevel;}
	public void setSkillLevel(int skillLevel){
		this.skillLevel=skillLevel;
		if(skillLevel==0) active_enabled=false;
		else active_enabled=true;
	}
	
	public int getSkillLevel(boolean isDungeon, boolean isBurning){
		if(skillLevel==0) return 0;
		int level = skillLevel;
		if(isOptionSkill());
		else if(isDungeon)
			level+=dungeonLevel;
		else
			level+=villageLevel;
		if(isBurning && type!=Skill_type.TP) level+=2;
		return level<maxLevel ? level : maxLevel; 
	}
	
	public void increaseLevel_char()
	{
		if(skillLevel<masterLevel)
			skillLevel++;
		active_enabled=true;
	}
	public void decreaseLevel_char()
	{
		if(skillLevel>0)
			skillLevel--;
		if(skillLevel==0) active_enabled=false;
	}
	
	public boolean getActiveEnabled() {return active_enabled;}
	public void setActiveEnabled(boolean enabled){this.active_enabled=enabled;}
	public boolean getBuffEnabled() {return buff_enabled;}
	public void setBuffEnabled(boolean enabled){this.buff_enabled=enabled;}
	
	public boolean isEqualTo(String name)
	{
		if (getName().equals(name)) return true;
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
		if(arg0 instanceof TPSkill) return 1;
		if(arg0.firstLevel!=firstLevel) return firstLevel-arg0.firstLevel;
		else return getName().compareTo(arg0.getName());
	}
	
	@SuppressWarnings("unchecked")
	public SkillLevelInfo getSkillLevelInfo(boolean isDungeon, boolean isBurning)
	{
		int level = getSkillLevel(isDungeon, isBurning);
		double increase;
		if(isOptionSkill()) increase=1.0;
		else if(isDungeon)
			increase=dungeonIncrease;
		else
			increase=villageIncrease;
		SkillLevelInfo temp = getSkillInfo(level);
		
		SkillLevelInfo returnValue = new SkillLevelInfo(level, (int)(temp.phy_atk*increase), temp.phy_fix*increase, (int)(temp.mag_atk*increase), temp.mag_fix*increase);
		returnValue.fromDictionary=temp.fromDictionary;
		returnValue.indep_level=temp.indep_level;
		returnValue.percentList=(HashMap<String, Integer>) temp.percentList.clone();
		try {
			returnValue.stat=(StatusList) temp.stat.clone();
			returnValue.fStat=(FunctionStatusList) temp.fStat.clone();
			if(Double.compare(increase, 1.0)!=0 && !hasDamage())
			{
				try {
					for(StatusAndName s : returnValue.stat.statList)
						s.stat.increaseStat(villageIncrease);
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public SkillLevelInfo getSkillLevelInfo(int skillLevel)
	{
		for(SkillLevelInfo info : skillInfo)
			if(info.skillLevel==skillLevel) return info;
		return null;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Skill) return getName().equals(((Skill) o).getName());
		else if(o instanceof String) return getName().equals(o);
		else return false;
	}
}
