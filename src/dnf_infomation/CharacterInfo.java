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
		
		/////퇴마
		temp = new CharInfoBox(Job.EXORCIST, 90, new StatusList());
		temp.statList.addStatList("힘", 722);
		temp.statList.addStatList("지능", 648);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////인파
		temp = new CharInfoBox(Job.EXORCIST, 90, new StatusList());
		temp.statList.addStatList("힘", 755);
		temp.statList.addStatList("지능", 646);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////배메
		temp = new CharInfoBox(Job.BATTLEMAGE, 90, new StatusList());
		temp.statList.addStatList("힘", 695);
		temp.statList.addStatList("지능", 712);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////엘마
		temp = new CharInfoBox(Job.ELEMENTALMASTER, 90, new StatusList());
		temp.statList.addStatList("힘", 598);
		temp.statList.addStatList("지능", 765);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////패황
		temp = new CharInfoBox(Job.STRIKER_M, 90, new StatusList());
		temp.statList.addStatList("힘", 780);
		temp.statList.addStatList("지능", 688);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);

		
		/////카이저
		temp = new CharInfoBox(Job.STRIKER_F, 90, new StatusList());
		temp.statList.addStatList("힘", 765);
		temp.statList.addStatList("지능", 598);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);

		/////자이언트
		temp = new CharInfoBox(Job.GRAPPLER_M, 90, new StatusList());
		temp.statList.addStatList("힘", 727);
		temp.statList.addStatList("지능", 640); // 안나와있음
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////워로드
		temp = new CharInfoBox(Job.VANGUARD, 90, new StatusList());
		temp.statList.addStatList("힘", 758);
		temp.statList.addStatList("지능", 604);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////듀얼리스트
		temp = new CharInfoBox(Job.DUALIST, 90, new StatusList());
		temp.statList.addStatList("힘", 721);
		temp.statList.addStatList("지능", 641);
		temp.statList.addStatList("독공", 966);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////엘븐
		temp = new CharInfoBox(Job.ELVENKNIGHT, 90, new StatusList());
		temp.statList.addStatList("힘", 744);
		temp.statList.addStatList("지능", 658);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////로그
		temp = new CharInfoBox(Job.ROUGE, 90, new StatusList());
		temp.statList.addStatList("힘", 750);
		temp.statList.addStatList("지능", 666);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////버서커
		temp = new CharInfoBox(Job.BUSERKER, 90, new StatusList());
		temp.statList.addStatList("힘", 762);
		temp.statList.addStatList("지능", 600);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////섀댄
		temp = new CharInfoBox(Job.SHADOWDANCER, 90, new StatusList());
		temp.statList.addStatList("힘", 765);
		temp.statList.addStatList("지능", 651);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);

		/////크리
		temp = new CharInfoBox(Job.CREATOR, 90, new StatusList());
		temp.statList.addStatList("힘", 960);
		temp.statList.addStatList("지능", 1052);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
		/////염제
		temp = new CharInfoBox(Job.NENMASTER_F, 90, new StatusList());
		temp.statList.addStatList("힘", 615);
		temp.statList.addStatList("지능", 748);
		temp.statList.addStatList("독공", 960);
		temp.statList.addStatList("모속강", 13);
		temp.statList.addStatList("물크", 8);
		temp.statList.addStatList("마크", 8);
		basicStatList.add(temp);
		
	}
}
