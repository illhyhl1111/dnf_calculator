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
		
		/////남스핏
		temp = new CharInfoBox(Job.SPITFIRE_M, 90, new StatusList());
		temp.statList.addStatList("힘", 697);
		temp.statList.addStatList("지능", 696);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////여스핏
		temp = new CharInfoBox(Job.SPITFIRE_F, 90, new StatusList());
		temp.statList.addStatList("힘", 697);
		temp.statList.addStatList("지능", 696);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////우럭
		temp = new CharInfoBox(Job.ELEMENTALBOMBER, 90, new StatusList());
		temp.statList.addStatList("힘", 600);
		temp.statList.addStatList("지능", 780);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////남런
		temp = new CharInfoBox(Job.LAUNCHER_M, 90, new StatusList());
		temp.statList.addStatList("힘", 757);
		temp.statList.addStatList("지능", 606);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////남렝
		temp = new CharInfoBox(Job.RANGER_M, 90, new StatusList());
		temp.statList.addStatList("힘", 757);
		temp.statList.addStatList("지능", 606);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
	}
}
