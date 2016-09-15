package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;

public class SkillInfo_gunner {
	public static void getInfo(HashSet<Skill> skillList)
	{
		Skill skill;
		SkillLevelInfo levelInfo;
		String icon=null;
		JobList job;
		
		//////////// 여런처
		job = JobList.LAUNCHER_F;
		
		///임시
		skill = new Skill("임시스킬", Skill_type.ACTIVE, icon, job, 25, 50, 40, 2);
		
		levelInfo = new SkillLevelInfo(32, 10000, 100, 0, 0);
		skill.skillInfo.add(levelInfo);
		levelInfo = new SkillLevelInfo(33, 10100, 101, 0, 0);
		skill.skillInfo.add(levelInfo);
		levelInfo = new SkillLevelInfo(34, 10200, 102, 0, 0);
		skill.skillInfo.add(levelInfo);
		levelInfo = new SkillLevelInfo(35, 10300, 103, 0, 0);
		skill.skillInfo.add(levelInfo);
		levelInfo = new SkillLevelInfo(36, 10400, 104, 0, 0);
		skill.skillInfo.add(levelInfo);
		
		skillList.add(skill);
		
		
		skill = new Skill("임시패시브", Skill_type.PASSIVE, icon, job, 75, 20, 20, 3);
		
		levelInfo = new SkillLevelInfo(5, 0, 0, 0, 0);
		levelInfo.stat.addStatList("물공", 240);
		levelInfo.stat.addStatList("독공", 320);
		skill.skillInfo.add(levelInfo);
		levelInfo = new SkillLevelInfo(6, 0, 0, 0, 0);
		levelInfo.stat.addStatList("물공", 270);
		levelInfo.stat.addStatList("독공", 350);
		skill.skillInfo.add(levelInfo);
		levelInfo = new SkillLevelInfo(7, 0, 0, 0, 0);
		levelInfo.stat.addStatList("물공", 290);
		levelInfo.stat.addStatList("독공", 380);
		skill.skillInfo.add(levelInfo);
		levelInfo = new SkillLevelInfo(8, 0, 0, 0, 0);
		levelInfo.stat.addStatList("물공", 320);
		levelInfo.stat.addStatList("독공", 410);
		skill.skillInfo.add(levelInfo);
		levelInfo = new SkillLevelInfo(9, 0, 0, 0, 0);
		levelInfo.stat.addStatList("물공", 340);
		levelInfo.stat.addStatList("독공", 440);
		skill.skillInfo.add(levelInfo);
		
		skillList.add(skill);
	}
}
