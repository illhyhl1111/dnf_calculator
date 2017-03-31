package dnf_class;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.Skill_type;

public class TPSkill extends Skill implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 640436407807049964L;
	public final String[] TPSkill_Target;
	
	public TPSkill(String name, String target, Job job, int firstLevel, int maxLevel, int masterLevel, int levelIncrease, String version)
	{
		super(name, Skill_type.TP, job, firstLevel, maxLevel, masterLevel, 5, Element_type.NONE, false, version);
		TPSkill_Target = new String[0];
		TPSkill_Target[0]=target;
		
		if(levelIncrease>=0){
			for(int i=1; i<=maxLevel; i++){
				SkillLevelInfo temp = new SkillLevelInfo(i);
				temp.stat.addSkill_damage(target, i*levelIncrease);
				skillInfo.add(temp);
			}
		}
	}
	public TPSkill(String name, String[] targets, Job job, int firstLevel, int maxLevel, int masterLevel, int levelIncrease, String version)
	{
		super(name, Skill_type.TP, job, firstLevel, maxLevel, masterLevel, 5, Element_type.NONE, false, version);
		TPSkill_Target = targets;
		
		if(levelIncrease>=0){
			for(int i=1; i<=maxLevel; i++){
				SkillLevelInfo temp = new SkillLevelInfo(i);
				for(String target : targets) temp.stat.addSkill_damage(target, i*levelIncrease);
				skillInfo.add(temp);
			}
		}
	}
	
	public TPSkill(String name, String target, Character_type type, int firstLevel, int maxLevel, int masterLevel, int levelIncrease, String version)
	{
		super(name, Skill_type.TP, type, firstLevel, maxLevel, masterLevel, 5, Element_type.NONE, false, version);
		TPSkill_Target = new String[0];
		TPSkill_Target[0]=target;
		
		if(levelIncrease>=0){
			for(int i=1; i<=maxLevel; i++){
				SkillLevelInfo temp = new SkillLevelInfo(i);
				temp.stat.addSkill_damage(target, i*levelIncrease);
				skillInfo.add(temp);
			}
		}
	}
	public TPSkill(String name, String[] targets, Character_type type, int firstLevel, int maxLevel, int masterLevel, int levelIncrease, String version)
	{
		super(name, Skill_type.TP, type, firstLevel, maxLevel, masterLevel, 5, Element_type.NONE, false, version);
		TPSkill_Target = targets;
		
		if(levelIncrease>=0){
			for(int i=1; i<=maxLevel; i++){
				SkillLevelInfo temp = new SkillLevelInfo(i);
				for(String target : targets) temp.stat.addSkill_damage(target, i*levelIncrease);
				skillInfo.add(temp);
			}
		}
	}
	
	@Override
	public int compareTo(Skill arg0) {
		if(!(arg0 instanceof TPSkill)) return -1;
		if(arg0.firstLevel!=firstLevel) return firstLevel-arg0.firstLevel;
		else return getName().compareTo(arg0.getName());
	}
}
