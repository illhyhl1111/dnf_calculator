package dnf_class;

import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_calculator.StatusList;

public class TPSkill extends Skill implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 640436407807049964L;
	public final String TPSkill_Target;
	
	public TPSkill(String name, String target, String icon, JobList job, int firstLevel, int maxLevel, int masterLevel, int levelIncrease)
	{
		super(name, Skill_type.TP, icon, job, firstLevel, maxLevel, masterLevel, 5);
		TPSkill_Target=target;
		
		for(int i=1; i<maxLevel; i++){
			SkillLevelInfo temp = new SkillLevelInfo(i);
			temp.stat.addSkill_damage(target, i*levelIncrease);
			skillInfo.add(temp);
		}
	}
	
	@Override
	public Object clone()
	{
		Skill temp=null;
		temp = (TPSkill) super.clone();
		return temp;
	}
}
