package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Equipment;

class EquipInfo_necklace {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Equip_part part = Equip_part.NECKLACE;
		Equip_type type = Equip_type.NONE;
		
		//////////에픽
		/////슈스
		
		/////정마
		
		/////얼공
		
		/////단일
		//무탐형
		
		temp = new Equipment("무한한 탐식의 형상", null, Item_rarity.EPIC, part, type);
		temp.vStat.addStatList("지능", new StatusInfo(41), true);
		temp.vStat.addStatList("추뎀", new StatusInfo(25));
		equipList.add(temp);
	}
}

class EquipInfo_bracelet {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Equip_part part = Equip_part.BRACELET;
		Equip_type type = Equip_type.NONE;
		
		//////////에픽
		/////슈스
		
		/////정마
		
		/////얼공
		
		/////단일
		
		
		//////////레전
		/////거형
		
		/////단일
		temp = new Equipment("필리르 - 꺼지지 않는 화염", null, Item_rarity.EPIC, part, type);
		temp.vStat.addStatList("힘", new StatusInfo(37), true);
		temp.vStat.addStatList("화속", new ElementInfo(10), true);
		temp.dStat.addStatList("화속", new ElementInfo(true, 32), false, true);
		equipList.add(temp);
	}
}

class EquipInfo_ring {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Equip_part part = Equip_part.RING;
		Equip_type type = Equip_type.NONE;
		
		//////////에픽
		/////슈스
		
		/////정마
		
		/////얼공
		
		/////단일
		//무탐잔
		
		temp = new Equipment("무한한 탐식의 잔재", null, Item_rarity.EPIC, part, type);
		temp.vStat.addStatList("힘", new StatusInfo(62), true);
		temp.vStat.addStatList("지능", new StatusInfo(62), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(10), true);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(10), true);
		temp.vStat.addStatList("크증뎀", new StatusInfo(30));
		equipList.add(temp);
	}
}