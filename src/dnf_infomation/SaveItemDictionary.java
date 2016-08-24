package dnf_infomation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import dnf_class.Equipment;

public class SaveItemDictionary {
	public static void main(String[] args)
	{
		try{
			HashSet<Equipment> equipList = new HashSet<Equipment>();
			
			EquipInfo_sword.getInfo(equipList);
			EquipInfo_fighter.getInfo(equipList);
			EquipInfo_gun.getInfo(equipList);
			EquipInfo_mage.getInfo(equipList);
			EquipInfo_priest.getInfo(equipList);
			EquipInfo_thief.getInfo(equipList);
			EquipInfo_lance.getInfo(equipList);
			
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
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ItemDictionary.dfd"));
			out.writeObject(equipList);
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}