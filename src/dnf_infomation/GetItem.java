package dnf_infomation;

import java.util.HashSet;

import dnf_class.Equipment;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;

public class GetItem
{
	static HashSet<Equipment> equipList;
	static boolean readed=false;
	
	public static void readFile()
	{
		if(readed) return;
		
		//read file
		equipList = (new ItemList()).equipList;
		readed=true;
	}
	
	public static Equipment getEquipment(String name) throws ItemFileNotReaded, ItemFileNotFounded
	{
		if(!readed) throw new ItemFileNotReaded();
		for(Equipment e : equipList)
			if(e.getName().equals(name)) return e;
		throw new ItemFileNotFounded(name);
	}
}
