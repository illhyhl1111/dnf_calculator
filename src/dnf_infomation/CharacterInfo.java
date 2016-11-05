package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Job;
import dnf_calculator.StatusList;

public class CharacterInfo {
	public static void getInfo(LinkedList<CharInfoBox> basicStatList)
	{
		CharInfoBox temp;
		Job job;
		
		////////// 거너
		/////여런처
		job = Job.LAUNCHER_F;
		
		//90레벨
		temp = new CharInfoBox(job, 90, new StatusList());
		temp.statList.addStatList("힘", 757);
		temp.statList.addStatList("지능", 606);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////여렝거
		job = Job.RANGER_F;
		//90레벨
		temp = new CharInfoBox(job, 90, new StatusList());
		temp.statList.addStatList("힘", 757);
		temp.statList.addStatList("지능", 606);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////검마
		job = Job.DEMONSLAYER;
		//90레벨
		temp = new CharInfoBox(job, 90, new StatusList());
		temp.statList.addStatList("힘", 757);
		temp.statList.addStatList("지능", 606);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
	}
}
