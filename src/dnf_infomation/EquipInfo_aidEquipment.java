package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_class.Equipment;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.StatusInfo;

/*class EquipInfo_aidEquipment {
	
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Item_rarity rarity;
		String image ="image\\sprite_item_new_equipment_10_support\\support.img\\";
		Equip_part part = Equip_part.AIDEQUIPMENT;
		Equip_type type = Equip_type.NONE;
		int level;
		
		//////////에픽
		rarity = Item_rarity.EPIC;
		
		///85제
		level=85;
		//증적
		temp = new Equipment("탐식의 증적", image+"증적.png", rarity, part, type, level);
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
		Item_rarity rarity;
		String image ="image\\sprite_item_new_equipment_11_magicstone\\magicstone.img\\";
		Equip_part part = Equip_part.MAGICSTONE;
		Equip_type type = Equip_type.NONE;
		int level;
		
		//////////에픽
		rarity = Item_rarity.EPIC;
		
		///85제
		level=85;
		//근원
		temp = new Equipment("탐식의 근원", image+"근원.png", rarity, part, type, level);
		temp.vStat.addStatList("힘", new StatusInfo(61), true);
		temp.vStat.addStatList("지능", new StatusInfo(61), true);
		temp.vStat.addStatList("모속강", new StatusInfo(45));
		equipList.add(temp);
	}
}

class EquipInfo_earring {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		Equipment temp;
		Item_rarity rarity;
		String image ="image\\sprite_item_new_equipment_11_magicstone\\magicstone.img\\";
		Equip_part part = Equip_part.EARRING;
		Equip_type type = Equip_type.NONE;
		int level = 90;
		
		//////////레어
		rarity = Item_rarity.RARE;
		
		//근원
		temp = new Equipment("임시귀걸이", null, rarity, part, type, level);
		temp.vStat.addStatList("힘", new StatusInfo(50), true);
		temp.vStat.addStatList("지능", new StatusInfo(50), true);
		temp.vStat.addStatList("무기물공", new StatusInfo(30), true);
		temp.vStat.addStatList("무기마공", new StatusInfo(30), true);
		temp.vStat.addStatList("독공", new StatusInfo(30), true);
		equipList.add(temp);
	}
}*/