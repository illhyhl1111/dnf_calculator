package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_calculator.BooleanInfo;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.StatusAndName;

public class SwitchingSkill extends Skill{

	private static final long serialVersionUID = -4157992471163036535L;  
	
	public SwitchingSkill(String name, Job job, int firstLevel, int maxLevel, int masterLevel, int levelInterval, String version)
	{
		super(name, Skill_type.SWITCHING, job, firstLevel, maxLevel, masterLevel, levelInterval, Element_type.NONE, false, version);
		explanation.add("스킬트리 창에서 아이콘을 더블클릭하여 스위칭 수치를 설정하세요");
	}
	public SwitchingSkill(String name, Character_type charType, int firstLevel, int maxLevel, int masterLevel, int levelInterval, String version)
	{
		super(name, Skill_type.SWITCHING, charType, firstLevel, maxLevel, masterLevel, levelInterval, Element_type.NONE, false, version);
		explanation.add("스킬트리 창에서 아이콘을 더블클릭하여 스위칭 수치를 설정하세요");
	}
	
	public SwitchingSkill(String name, Skill_type type, Job job, int firstLevel, int maxLevel, int masterLevel, int levelInterval, String version)
	{
		super(name, type, job, firstLevel, maxLevel, masterLevel, levelInterval, Element_type.NONE, false, version);
		explanation.add("스킬트리 창에서 아이콘을 더블클릭하여 사용자 지정 수치를 설정하세요");
	}
	public SwitchingSkill(String name, Skill_type type, Character_type charType, int firstLevel, int maxLevel, int masterLevel, int levelInterval, String version)
	{
		super(name, type, charType, firstLevel, maxLevel, masterLevel, levelInterval, Element_type.NONE, false, version);
		explanation.add("스킬트리 창에서 아이콘을 더블클릭하여 사용자 지정 수치를 설정하세요");
	}
	
	@Override
	public SkillLevelInfo getSkillLevelInfo(boolean isDungeon, boolean isBurning)
	{
		if(getCharSkillLevel()==0) return skillInfo.getFirst();
		return skillInfo.getLast();
	}
	
	@Override
	public SkillLevelInfo getCanonicalSkillLevelInfo(boolean isDungeon, boolean isBurning)
	{
		if(getCharSkillLevel()==0) return skillInfo.getFirst();
		return skillInfo.getLast();
	}
	
	public int getModifyableNum()
	{
		int num = skillInfo.getLast().stat.statList.size();
		for(StatusAndName s : skillInfo.getLast().stat.statList)
			if(s.stat instanceof BooleanInfo) num--;
		if(skillInfo.getLast().hasPhy_per()) num++;
		if(skillInfo.getLast().hasPhy_fix()) num++;
		if(skillInfo.getLast().hasMag_per()) num++;
		if(skillInfo.getLast().hasMag_fix()) num++;
		return num;
	}
	
	public boolean setSkillPercent(double[] num)
	{
		if(num.length!=getModifyableNum()) return false;
		
		int index=0;
		if(skillInfo.getLast().hasPhy_per()) skillInfo.getLast().phy_atk=(int) Math.round(num[index++]);
		if(skillInfo.getLast().hasPhy_fix()) skillInfo.getLast().phy_fix=num[index++];
		if(skillInfo.getLast().hasMag_per()) skillInfo.getLast().mag_atk=(int) Math.round(num[index++]);
		if(skillInfo.getLast().hasMag_fix()) skillInfo.getLast().mag_fix=num[index++];
		for(StatusAndName s : skillInfo.getLast().stat.statList)
			try {
				if(s.stat instanceof SkillStatusInfo)
					((SkillStatusInfo)s.stat).setIncrease(num[index++]);
				else if(s.stat instanceof BooleanInfo);
				else s.stat.setInfo(num[index++]);
			} catch (StatusTypeMismatch e) {
				e.printStackTrace();
				return false;
			}
		return true;
	}
	
	public String[] getStatList()
	{
		LinkedList<StatusAndName> statlist = getSkillLevelInfo(true, false).stat.statList;
		String[] statList = new String[getModifyableNum()];
		int j=0;
		if(skillInfo.getLast().hasPhy_per()) statList[j++] = "물리 % 데미지";
		if(skillInfo.getLast().hasPhy_fix()) statList[j++] = "물리 고정 데미지";
		if(skillInfo.getLast().hasMag_per()) statList[j++] = "마법 % 데미지";
		if(skillInfo.getLast().hasMag_fix()) statList[j++] = "마법 고정 데미지";
		for(StatusAndName s : statlist){
			if(s.stat instanceof SkillStatusInfo)
				try {
					statList[j++] = s.stat.getStatToString()+" 데미지 증가 % ";
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
			else if (s.stat instanceof BooleanInfo);
			else statList[j++] = StatusAndName.getStatHash().get(s.name);
		}
		
		return statList;
	}
}