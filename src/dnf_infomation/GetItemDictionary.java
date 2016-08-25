package dnf_infomation;

import java.io.*;
import java.util.LinkedList;

import dnf_class.Equipment;
import dnf_class.SetOption;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;
import dnf_InterfacesAndExceptions.SetName;

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
		for(Equipment e : itemDictionary.equipList)
			if(e.getName().equals(name)) return e;
		throw new ItemFileNotFounded(name);
	}
	
	public static LinkedList<SetOption> getSetOptions(SetName setName) throws ItemFileNotReaded, ItemFileNotFounded
	{
		if(!readed) throw new ItemFileNotReaded();
		
		LinkedList<SetOption> temp = new LinkedList<SetOption>();
		for(SetOption s : itemDictionary.setOptionList)
			if(s.getSetName()==setName) temp.add(s);
		
		if(temp.isEmpty()) throw new ItemFileNotFounded(setName.toString());
		return temp;
	}
	
}
