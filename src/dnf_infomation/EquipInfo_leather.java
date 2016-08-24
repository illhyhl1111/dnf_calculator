package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.StatusInfo;
import dnf_class.Equipment;

public class EquipInfo_leather {
	public static void getInfo(HashSet<Equipment> equipList)
	{
		//증적
		Equipment temp = new Equipment("탐식의 증적", null, Item_rarity.EPIC, Equip_part.AIDEQUIPMENT, Equip_type.NONE);
		temp.vStat.addStatList("힘", new StatusInfo(128));
		temp.vStat.addStatList("지능", new StatusInfo(128));
		temp.vStat.addStatList("무기물공", new StatusInfo(110));
		temp.vStat.addStatList("무기마공", new StatusInfo(110));
		temp.vStat.addStatList("독공", new StatusInfo(149));
		equipList.add(temp);
	}
}
