package dnf_class;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.StatusAndName;

public class SwitchingSkill extends Skill{

	private static final long serialVersionUID = -4157992471163036535L;  
	
	public SwitchingSkill(String name, Job job, int firstLevel, int maxLevel, int masterLevel, int levelInterval, String version)
	{
		super(name, Skill_type.SWITCHING, job, firstLevel, maxLevel, masterLevel, levelInterval, Element_type.NONE, version);
		explanation.add("스킬트리 창에서 아이콘을 더블클릭하여 스위칭 수치를 설정하세오");
	}
	public SwitchingSkill(String name, Character_type charType, int firstLevel, int maxLevel, int masterLevel, int levelInterval, String version)
	{
		super(name, Skill_type.SWITCHING, charType, firstLevel, maxLevel, masterLevel, levelInterval, Element_type.NONE, version);
		explanation.add("스킬트리 창에서 아이콘을 더블클릭하여 스위칭 수치를 설정하세오");
	}
	
	@Override
	public SkillLevelInfo getSkillLevelInfo(boolean isDungeon, boolean isBurning)
	{
		return skillInfo.getLast();
	}
	
	public int getModifyableNum()
	{
		return skillInfo.getLast().stat.statList.size();
	}
	
	public boolean setSkillPercent(double[] num)
	{
		if(num.length!=getModifyableNum()) return false;
		
		int index=0;
		for(StatusAndName s : skillInfo.getLast().stat.statList)
			try {
				if(s.stat instanceof SkillStatusInfo)
					((SkillStatusInfo)s.stat).setIncrease(num[index++]);
				
				else s.stat.setInfo(num[index++]);
			} catch (StatusTypeMismatch e) {
				e.printStackTrace();
				return false;
			}
		return true;
	}
}
