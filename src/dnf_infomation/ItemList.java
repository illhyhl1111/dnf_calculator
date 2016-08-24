package dnf_infomation;

import java.util.HashSet;

import dnf_class.Equipment;

public class ItemList {												// 기본 아이템 파일 제작 (유저 디자인과는 분리된 파일)
	
	public HashSet<Equipment> equipList = new HashSet<Equipment>();
	
	public ItemList()
	{
		EquipInfo_fabric.getInfo(equipList);
		EquipInfo_leather.getInfo(equipList);
		EquipInfo_mail.getInfo(equipList);
		EquipInfo_heavy.getInfo(equipList);
		EquipInfo_plate.getInfo(equipList);
		EquipInfo_necklace.getInfo(equipList);
		EquipInfo_bracelet.getInfo(equipList);
		EquipInfo_ring.getInfo(equipList);
		EquipInfo_aidEquipment.getInfo(equipList);
		EquipInfo_magicStone.getInfo(equipList);
	}
}
