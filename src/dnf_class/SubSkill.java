package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.Skill_type;

public class SubSkill extends Skill{
	private static final long serialVersionUID = -5916420310709445310L;
	public String superSkillName;
	public Skill superSkill=null;

	public SubSkill(String name, String superSkill, Job job, int firstLevel, int maxLevel, int masterLevel, int levelInterval, String version)
	{
		super(name, Skill_type.SUBSKILL, job, firstLevel, maxLevel, masterLevel, levelInterval, Element_type.NONE, version);
		this.superSkillName=superSkill;
	}
	public SubSkill(String name, String superSkill, Character_type charType, int firstLevel, int maxLevel, int masterLevel, int levelInterval, String version)
	{
		super(name, Skill_type.SUBSKILL, charType, firstLevel, maxLevel, masterLevel, levelInterval, Element_type.NONE, version);
		this.superSkillName=superSkill;
	}
	
	public void setSuperSkill(LinkedList<Skill> skillList){
		for(Skill skill : skillList)
			if(skill.getName().equals(superSkillName)){
				superSkill=skill;
				for(SkillLevelInfo skillInfo : skill.skillInfo)
					skillInfo.percentList.put(getName(), 100);
				return;
			}
	}
	
	@Override
	public int getSkillLevel(boolean isDungeon, boolean isBurning){
		return superSkill.getSkillLevel(isDungeon, isBurning);
	}
}
