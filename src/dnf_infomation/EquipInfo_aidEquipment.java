package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_class.Equipment;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.StatusInfo;

class EquipInfo_aidEquipment {
	
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Equip_part part = Equip_part.AIDEQUIPMENT;
		Equip_type type = Equip_type.NONE;
		
		//////////에픽
		
		//증적
		temp = new Equipment("탐식의 증적", null, Item_rarity.EPIC, part, type);
		temp.vStat.addStatList("힘", new StatusInfo(128), true);
		temp.vStat.addStatList("지능", new StatusInfo(128), true);
		temp.vStat.addStatList("무기물공", new StatusInfo(110), true);
		temp.vStat.addStatList("무기마공", new StatusInfo(110), true);
		temp.vStat.addStatList("독공", new StatusInfo(149), true);
		equipList.add(temp);
	}
}

class EquipInfo_magicStone {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Equip_part part = Equip_part.MAGICSTONE;
		Equip_type type = Equip_type.NONE;
		
		//////////에픽
		
		//근원
		temp = new Equipment("탐식의 근원", null, Item_rarity.EPIC, part, type);
		temp.vStat.addStatList("힘", new StatusInfo(61), true);
		temp.vStat.addStatList("지능", new StatusInfo(61), true);
		temp.vStat.addStatList("모속강", new StatusInfo(45));
		equipList.add(temp);
	}
}