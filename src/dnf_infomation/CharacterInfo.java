package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Job;
import dnf_calculator.StatusList;

public class CharacterInfo {
	public static void getInfo(HashSet<CharInfoBox> infoList)
	{
		CharInfoBox temp;
		Job job;
		
		////////// 거너
		/////여런처
		job = Job.LAUNCHER_F;
		
		//90레벨
		temp = new CharInfoBox(job, 90, new StatusList());
		temp.statList.addStatList("힘", 1000);
		temp.statList.addStatList("지능", 800);
		temp.statList.addStatList("독공", 1000);
		temp.statList.addStatList("모속강", 13);
		infoList.add(temp);
	}
}
