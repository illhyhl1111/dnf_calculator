package dnf_infomation;

import java.util.HashSet;

import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.SetOption;

public class ItemDictionary implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4213722159864758338L;
	HashSet<Equipment> equipList;
	HashSet<SetOption> setOptionList;
	HashSet<Item> etcList;
	
	public ItemDictionary() 
	{
		equipList = new HashSet<Equipment>();	
		
		//EquipInfo_sword.getInfo(equipList);
		//EquipInfo_fighter.getInfo(equipList);
		WeaponInfo_gun.getInfo(equipList);
		//EquipInfo_mage.getInfo(equipList);
		//EquipInfo_priest.getInfo(equipList);
		//EquipInfo_thief.getInfo(equipList);
		//EquipInfo_lance.getInfo(equipList);
		
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
		
		setOptionList = new HashSet<SetOption>();
		SetOptionInfo.getInfo(setOptionList);
	}
}
