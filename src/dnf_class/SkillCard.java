package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_calculator.StatusList;
import dnf_infomation.GetDictionary;

public class SkillCard extends Card{
	private static final long serialVersionUID = 3617125627597395816L;
	public LinkedList<String> skillList;
	private String skill;
	private int levelNum;
	
	public SkillCard(String name, Item_rarity rarity, String version)
	{
		super(name, rarity, version);
		skillList = new LinkedList<String>();
		skill=null;
		levelNum=1;
	}
	
	public SkillCard(){
		super();
		skillList = new LinkedList<String>();
		skill=null;
		levelNum=1;
	}
	
	@Override
	public boolean setSkillList(Job job){
		skillList = new LinkedList<String>();
		
		for(Skill skill : GetDictionary.getSkillList(job, 90)){
			if(skill.type!=Skill_type.TP && skill.maxLevel!=1 && skill.firstLevel!=1 && !skill.isOptionSkill() && !skill.isSubSkill()
					&& skill.firstLevel<=70 && skill.firstLevel!=50 && skill.firstLevel!=48 && skill.job==job)
				skillList.add(skill.getItemName());
		}
		skill = skillList.getFirst();
		vStat = new StatusList();
		vStat.addSkill(skill, levelNum);
		return true;
	}
	
	public String getSkill() {
		return skill;
	}
	public int getSkillLevel() {
		return levelNum;
	}

	public void setSkill(String skill, int level) {
		this.skill = skill;
		this.levelNum=level;
		vStat = new StatusList();
		vStat.addSkill(skill, level);
	}
}