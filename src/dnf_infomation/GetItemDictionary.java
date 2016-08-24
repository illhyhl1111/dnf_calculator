package dnf_infomation;

import java.util.HashSet;
import java.io.*;

import dnf_class.Equipment;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;

public class GetItemDictionary
{
	static HashSet<Equipment> equipDictionary;
	static boolean readed=false;
	
	@SuppressWarnings("unchecked")
	public static void readFile()
	{
		if(readed) return;
		
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("ItemDictionary.dfd"));
			Object temp = in.readObject();
			
			if(temp instanceof HashSet<?>)
				equipDictionary = (HashSet<Equipment>)temp;
			else {
				in.close();
				throw new ClassNotFoundException();
			}
			
			readed=true;
			in.close();
		}
		catch(FileNotFoundException e)
		{	
			equipDictionary = new HashSet<Equipment>();
		
			EquipInfo_sword.getInfo(equipDictionary);
			EquipInfo_fighter.getInfo(equipDictionary);
			EquipInfo_gun.getInfo(equipDictionary);
			EquipInfo_mage.getInfo(equipDictionary);
			EquipInfo_priest.getInfo(equipDictionary);
			EquipInfo_thief.getInfo(equipDictionary);
			EquipInfo_lance.getInfo(equipDictionary);
			
			EquipInfo_fabric.getInfo(equipDictionary);
			EquipInfo_leather.getInfo(equipDictionary);
			EquipInfo_mail.getInfo(equipDictionary);
			EquipInfo_heavy.getInfo(equipDictionary);
			EquipInfo_plate.getInfo(equipDictionary);
				
			EquipInfo_necklace.getInfo(equipDictionary);
			EquipInfo_bracelet.getInfo(equipDictionary);
			EquipInfo_ring.getInfo(equipDictionary);
			EquipInfo_aidEquipment.getInfo(equipDictionary);
			EquipInfo_magicStone.getInfo(equipDictionary);
			readed=true;
			
			SaveItemDictionary.main(null);
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Equipment getEquipment(String name) throws ItemFileNotReaded, ItemFileNotFounded
	{
		if(!readed) throw new ItemFileNotReaded();
		for(Equipment e : equipDictionary)
			if(e.getName().equals(name)) return e;
		throw new ItemFileNotFounded(name);
	}
}
