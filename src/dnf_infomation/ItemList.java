package dnf_infomation;

import java.util.HashSet;

import dnf_class.Equipment;
import dnf_calculator.StatusInfo;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;

public class ItemList {												// 기본 아이템 파일 제작 (유저 디자인과는 분리된 파일)
	
	public HashSet<Equipment> equipList = new HashSet<Equipment>();
	
	public ItemList()
	{
		EquipInfo_heavy.getInfo(equipList);

		//증적
		temp = new Equipment("탐식의 증적", null, Item_rarity.EPIC, Equip_part.AIDEQUIPMENT, Equip_type.NONE);
		temp.vStat.addStatList("힘", new StatusInfo(128));
		temp.vStat.addStatList("지능", new StatusInfo(128));
		temp.vStat.addStatList("무기물공", new StatusInfo(110));
		temp.vStat.addStatList("무기마공", new StatusInfo(110));
		temp.vStat.addStatList("독공", new StatusInfo(149));
		equipList.add(temp);
	}
}
