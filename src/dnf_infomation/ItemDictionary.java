package dnf_infomation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.SetOption;

public class ItemDictionary implements java.io.Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4213722159864758338L;
	public HashSet<Equipment> equipList;
	public HashSet<SetOption> setOptionList;
	//public HashSet<Item> etcList;
	
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
	
	public LinkedList<Item> getAllItemList()
	{
		LinkedList<Item> list = new LinkedList<Item>();
		for(Equipment e : equipList)
			list.add(e);
		//for(Item e : etcList)
			//list.add(e);
		return list;
	}
		
	public Equipment getEquipment(String name) throws ItemFileNotFounded
	{
		for(Equipment e : equipList)
			if(e.getName().equals(name)) return e;
		throw new ItemFileNotFounded(name);
	}
	
	public LinkedList<SetOption> getSetOptions(SetName setName) throws ItemFileNotFounded
	{
		LinkedList<SetOption> temp = new LinkedList<SetOption>();
		for(SetOption s : setOptionList)
			if(s.getSetName()==setName) temp.add(s);
		
		if(temp.isEmpty()) throw new ItemFileNotFounded(setName.toString());
		Collections.sort(temp);
		return temp;
	}
	
	public void setSetOptions(SetName setName, LinkedList<SetOption> list) throws ItemFileNotFounded
	{
		for(SetOption s : setOptionList)
		{
			if(s.getSetName()==setName){
				for(SetOption s2 : list){
					if(s2.requireNum==s.requireNum) s.changeOption(s2);
					break;
				}
			}
		}
	}
	
	public boolean addEquipment(Equipment equipment)
	{
		for(Equipment e : equipList)
			if(e.getName().equals(equipment.getName())){
				return false;
			}
		equipList.add(equipment);
		return true;
	}
	
	@Override
	public Object clone()
	{
		ItemDictionary itemDictionary;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("ItemDictionary.dfd"));
			Object temp = in.readObject();

			itemDictionary = (ItemDictionary)temp;
			in.close();
			return itemDictionary;
		}
		catch(FileNotFoundException e)
		{	
			itemDictionary = new ItemDictionary();
			SaveItemDictionary.main(null);
			return itemDictionary;
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}

class SaveItemDictionary {
	public static void main(String[] args)
	{
		try{
			ItemDictionary itemDic = new ItemDictionary();
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ItemDictionary.dfd"));
			out.writeObject(itemDic);
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}