package dnf_infomation;

import java.io.*;
import java.util.LinkedList;

import dnf_class.Equipment;
import dnf_class.SetOption;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_InterfacesAndExceptions.Weapon_detailType;

public class GetItemDictionary
{
	public static ItemDictionary itemDictionary;
	static boolean readed=false;

	public static void readFile()
	{
		if(readed) return;
		
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("ItemDictionary.dfd"));
			Object temp = in.readObject();

			itemDictionary = (ItemDictionary)temp;
			
			readed=true;
			in.close();
		}
		catch(FileNotFoundException e)
		{	
			itemDictionary = new ItemDictionary();
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
		return itemDictionary.getEquipment(name);
	}
	
	public static LinkedList<SetOption> getSetOptions(SetName setName) throws ItemFileNotReaded, ItemFileNotFounded
	{
		if(!readed) throw new ItemFileNotReaded();
		return itemDictionary.getSetOptions(setName);
	}
	
	public static int getDimensionInfo (int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		return ReinforceInfo.getDimensionInfo(num, rarity, level);
	}
	
	public static int getReinforceAidInfo (int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		return ReinforceInfo.getAidStatInfo(num, rarity, level);
	}
	
	public static int getReinforceInfo_phy (int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		return ReinforceInfo.getReinforceWeaponInfo_phy(num, rarity, level, type);
	}
	
	public static int getReinforceInfo_mag (int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		return ReinforceInfo.getReinforceWeaponInfo_mag(num, rarity, level, type);
	}
}
