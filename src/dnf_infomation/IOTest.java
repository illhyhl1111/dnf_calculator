package dnf_infomation;

import java.io.*;
import java.util.HashSet;

import dnf_class.Equipment;

public class IOTest {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException
	{
		try{
			HashSet<Equipment> equipList = new HashSet<Equipment>();
			
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
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ItemDictionary.dfd"));
			out.writeObject(equipList);
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		HashSet<Equipment> equipDictionary;
		
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("ItemDictionary.dfd"));
			Object temp = in.readObject();
			
			if(temp instanceof HashSet<?>)
				equipDictionary = (HashSet<Equipment>)temp;
			else {
				in.close();
				throw new ClassNotFoundException();
			}
			in.close();
			for(Equipment e : equipDictionary)
				System.out.println(e);
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
	}
}
