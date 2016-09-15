package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatList;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.FunctionStat;
import dnf_calculator.StatusInfo;
import dnf_calculator.StatusList;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;
import dnf_class.SetOption;

public class SetOptionInfo {
	public static void getInfo(HashSet<SetOption> equipList)
	{
		SetOption temp;
		
		//////////에픽
		/////천
		//불마력
		
		/////중갑
		temp = new SetOption("거미 여왕의 숨결 세트", SetName.SPIDERQUEEN, 4);
		temp.explanation.add("방깍제한 36000을 위한 더미셋옵입니다 무시ㄱ");
		temp.fStat.statList.add(new FunctionStat() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6285990968608784856L;
			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				int num=0;
				StatusList statList = new StatusList();
				if(character.getEquipmentList().get(Equip_part.ROBE).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.ROBE).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(character.getEquipmentList().get(Equip_part.TROUSER).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.TROUSER).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(character.getEquipmentList().get(Equip_part.SHOULDER).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.SHOULDER).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(character.getEquipmentList().get(Equip_part.BELT).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.BELT).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(character.getEquipmentList().get(Equip_part.SHOES).getSetName()==SetName.SPIDERQUEEN)
					if(character.getEquipmentList().get(Equip_part.SHOES).dStat.findStat(StatList.DEF_DEC_FIXED_PHY).enabled) num++;
				
				if(num==5) statList.addStatList("고정물방깍", -24000);
				if(num==5) statList.addStatList("고정마방깍", -24000);
				if(num==4) statList.addStatList("고정물방깍", -12000);
				if(num==4) statList.addStatList("고정마방깍", -12000);
				
				return statList;
			}
		});
		equipList.add(temp);
		
		temp = new SetOption("거미 여왕의 숨결 세트", SetName.SPIDERQUEEN, 5);
		temp.vStat.addStatList("증뎀", new StatusInfo(45));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(10));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(10));
		equipList.add(temp);
		
		//금계
		temp = new SetOption("금지된 계약 세트", SetName.FORBIDDENCONTRACT, 5);
		equipList.add(temp);
		
		
		
		
		/////////////아바타
		//레압
		temp = new SetOption("레어 아바타 세트", SetName.RAREAVATAR, 3);
		temp.vStat.addStatList("힘", new StatusInfo(20));
		temp.vStat.addStatList("지능", new StatusInfo(20));
		equipList.add(temp);
		
		temp = new SetOption("레어 아바타 세트", SetName.RAREAVATAR, 5);
		temp.explanation.add("데미지 증가 효과 없음");
		equipList.add(temp);
		
		temp = new SetOption("레어 아바타 세트", SetName.RAREAVATAR, 8);
		temp.vStat.addStatList("힘", new StatusInfo(20));
		temp.vStat.addStatList("지능", new StatusInfo(20));
		equipList.add(temp);
		
		//상압
		temp = new SetOption("상급 아바타 세트", SetName.AVATAR, 3);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		equipList.add(temp);
		
		temp = new SetOption("상급 아바타 세트", SetName.AVATAR, 5);
		temp.explanation.add("데미지 증가 효과 없음");
		equipList.add(temp);
		
		temp = new SetOption("상급 아바타 세트", SetName.AVATAR, 8);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		equipList.add(temp);
	}
}
