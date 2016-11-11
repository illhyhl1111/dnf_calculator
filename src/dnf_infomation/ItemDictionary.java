package dnf_infomation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_InterfacesAndExceptions.SetName;
import dnf_UI_32.Inventory;
import dnf_calculator.FunctionStatusList;
import dnf_calculator.StatusList;
import dnf_class.Avatar;
import dnf_class.Buff;
import dnf_class.Card;
import dnf_class.Creature;
import dnf_class.Drape;
import dnf_class.Emblem;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.ItemConstraint;
import dnf_class.Jam;
import dnf_class.SetOption;
import dnf_class.Setting;
import dnf_class.Title;
import dnf_class.Weapon;

public class ItemDictionary implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4213722159864758338L;
	public final LinkedList<Equipment> equipList;
	public final LinkedList<Title> titleList;
	public final LinkedList<SetOption> setOptionList;
	public final LinkedList<Card> cardList;
	public final LinkedList<Avatar> avatarList;
	public final LinkedList<Creature> creatureList;
	public final LinkedList<Drape> drapeList;
	public final LinkedList<Emblem> emblemList;
	public final LinkedList<Jam> jamList;
	public final LinkedList<Buff> buffList;
	
	public final Item[] userInventory;
	public static final int InventorySize = 225;
	public LinkedList<Setting> settingList;
	
	private String VERSION = CalculatorVersion.ITEM_VERSION;
	public static final String Directory = "data\\ItemDictionary-"+CalculatorVersion.ITEM_VERSION+".dfd";
	
	public ItemDictionary() 
	{
		equipList = new LinkedList<Equipment>();	
		
		try {
			EquipmentInfo.getInfo(equipList, EquipmentInfo.equipmentInfo());
			EquipmentInfo.getInfo(equipList, EquipmentInfo.weaponInfo());
		} catch (ParsingException e) {
			e.printStackTrace();
		}
		
		titleList = new LinkedList<Title>();	
		TitleInfo.getInfo(titleList);
		
		setOptionList = new LinkedList<SetOption>();
		SetOptionInfo.getInfo(setOptionList);
		
		cardList = new LinkedList<Card>();
		CardInfo.getInfo(cardList);
		
		avatarList = new LinkedList<Avatar>();
		AvatarInfo.getInfo(avatarList);
		
		creatureList = new LinkedList<Creature>();
		CreatureInfo.getInfo(creatureList);
		
		drapeList = new LinkedList<Drape>();
		
		emblemList = new LinkedList<Emblem>();
		EmblemInfo.getInfo(emblemList);
		
		jamList = new LinkedList<Jam>();
		
		buffList = new LinkedList<Buff>();
		BuffItemInfo.getInfo(buffList);
		
		userInventory = new Item[Inventory.inventoryCol*Inventory.inventoryRow];
		for(int i=0; i<InventorySize; i++)
			userInventory[i]=new Item();
		settingList = new LinkedList<Setting>();
	}
	
	public LinkedList<Item> getVaultItemList(Job job)
	{
		LinkedList<Item> list = new LinkedList<Item>();
		for(Equipment e : equipList)
			list.add(e);
		for(Title t : titleList)
			list.add(t);
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	
	public LinkedList<Item> getItemList(ItemConstraint constraint)
	{
		LinkedList<Item> list = new LinkedList<Item>();
		for(Equipment i : equipList){
			if(constraint.partList.contains(i.getPart()) && constraint.rarityList.contains(i.getRarity()) 
					&& (constraint.lowerLevel <= i.level && i.level <= constraint.upperLevel) )
			{
				if(i instanceof Weapon && ((Weapon) i).enabled(constraint.job)) list.add(i);
				else list.add(i);
			}
		}
		
		if(constraint.partList.contains(Equip_part.TITLE))
			for(Title i : titleList)
				list.add(i);
		
		return list;
	}
	
	/*@SuppressWarnings("unchecked")
	public LinkedList<Item>[] separateCardList(ItemConstraint[] constraintList)
	{
		LinkedList<?>[] list = new LinkedList<?>[constraintList.length+1];
		for(int i=0; i<constraintList.length+1; i++)
			list[i] = new LinkedList<Item>();
		
		LinkedList<Equip_part> partList = new LinkedList<Equip_part>();
		partList.add(Equip_part.RING);
		partList.add(Equip_part.NECKLACE);
		partList.add(Equip_part.BRACELET);
		partList.add(Equip_part.EARRING);
		partList.add(Equip_part.MAGICSTONE);
		partList.add(Equip_part.AIDEQUIPMENT);
		
		int i;
		for(Card c : cardList){
			if(c.available(partList)) ((LinkedList<Item>)list[constraintList.length]).add(c);
			for(i=0; i<constraintList.length; i++)
				if(c.available(constraintList[i].partList)){
					((LinkedList<Item>)list[i]).add(c);
			}
		}

		return (LinkedList<Item>[]) list;
	}*/
	
	@SuppressWarnings("unchecked")
	public LinkedList<Item>[] separateList(ItemConstraint[] constraintList)
	{
		LinkedList<?>[] list = new LinkedList<?>[constraintList.length+1];
		for(int i=0; i<constraintList.length+1; i++)
			list[i] = new LinkedList<Item>();
		
		int i;
		for(Equipment e : equipList){
			for(i=0; i<constraintList.length; i++){
				if(constraintList[i].equipInConstraint(e)){
					if(e instanceof Weapon && ((Weapon) e).enabled(constraintList[i].job)){
						((LinkedList<Item>)list[i]).add(e);
						break;
					}
					else{
						((LinkedList<Item>)list[i]).add(e);
						break;
					}
				}
			}
			if(i==constraintList.length)((LinkedList<Item>)list[constraintList.length]).add(e);
		}
		
		for(i=0; i<constraintList.length; i++){
			if(constraintList[i].partList.contains(Equip_part.TITLE)){
				for(Title t : titleList) ((LinkedList<Item>)list[i]).add(t);
				break;
			}
		}
		if(i==constraintList.length)
			for(Title t : titleList) ((LinkedList<Item>)list[i]).add(t);
		
		for(i=0; i<constraintList.length; i++){
			Collections.sort( (LinkedList<Item>)list[i] );
			Collections.reverse( (LinkedList<Item>)list[i] );
		}
		
		return (LinkedList<Item>[]) list;
	}
	
	public LinkedList<Item> getAllCardList()
	{
		LinkedList<Item> list = new LinkedList<Item>();
		for(Card e : cardList)
			list.add(e);
		Collections.sort(list);
		return list;
	}
	
	public LinkedList<Item> getAllEmblemList()
	{
		LinkedList<Item> list = new LinkedList<Item>();
		for(Emblem e : emblemList)
			list.add(e);
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	
	public LinkedList<Buff> getAllBuffList()
	{
		Collections.sort(buffList);
		return buffList;
	}
		
	public Equipment getEquipment(String name) throws ItemNotFoundedException
	{
		for(Equipment e : equipList)
			if(e.getName().equals(name)) return e;
		for(Item i : userInventory)
			if(i.getName().equals(name)) return (Equipment)i;
		throw new ItemNotFoundedException(name);
	}
	
	public Title getTitle(String name) throws ItemNotFoundedException
	{
		for(Title t : titleList)
			if(t.getName().equals(name)) return t;
		for(Item i : userInventory)
			if(i.getName().equals(name)) return (Title)i;
		throw new ItemNotFoundedException(name);
	}
	
	public Card getCard(String name) throws ItemNotFoundedException
	{
		for(Card e : cardList)
			if(e.getName().equals(name)) return e;
		throw new ItemNotFoundedException(name);
	}
	
	public LinkedList<SetOption> getSetOptions(SetName setName) throws ItemNotFoundedException
	{
		LinkedList<SetOption> temp = new LinkedList<SetOption>();
		for(SetOption s : setOptionList)
			if(s.getSetName()==setName) temp.add(s);
		
		if(temp.isEmpty()) throw new ItemNotFoundedException(setName.toString());
		Collections.sort(temp);
		return temp;
	}
	
	public Emblem getEmblem(String name) throws ItemNotFoundedException
	{
		for(Emblem e : emblemList)
			if(e.getName().equals(name)) return e;
		throw new ItemNotFoundedException(name);
	}
	
	public Avatar getAvatar(String name) throws ItemNotFoundedException {
		for(Avatar a : avatarList)
			if(a.getName().equals(name)) return a;
		for(Item i : userInventory)
			if(i.getName().equals(name)) return (Avatar)i;
		throw new ItemNotFoundedException(name);
	}
	
	public void setSetOptions(SetName setName, LinkedList<SetOption> list)
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
	
	public boolean itemInInventory(Item item){
		for(Item i : userInventory){
			if(i.getItemName().equals(item.getItemName())) return true;
		}
		return false;
	}
	
	public <T extends Item> LinkedList<Item> getSortedList(LinkedList<T> set)
	{
		LinkedList<Item> aList = new LinkedList<Item>();
		for(T t : set)
			aList.add(t);
		Collections.sort(aList);
		Collections.reverse(aList);
		return aList;
	}
	
	public Item makeReplicate(Item item, int index)
	{
		if(index<0) return null;
		
		LinkedList<Integer> replicateNumList = new LinkedList<Integer>();
		int num=1;
		
		for(Item i : userInventory)
		{
			if(i.getName().contains(item.getItemName()+"-복제")){
				replicateNumList.add(i.replicateNum);
			}
		}
		Collections.sort(replicateNumList);
		for(Integer i : replicateNumList)
			if(num==i) num++;
			else break;
		
		Item replicate = (Item) item.clone();
		replicate.replicateNum=num;
		String indexStr = "";
		if(num!=1) indexStr="("+num+")";
		replicate.setName(replicate.getItemName() + "-복제"+indexStr);
		
		userInventory[index]=replicate;
		return replicate;
	}
	
	public void updateVersion(ItemDictionary supremeDictionary, Job job)
	{
		if(supremeDictionary.getVERSION().compareTo(VERSION)>0){
			updateList(equipList, supremeDictionary.equipList, job);
			updateList(cardList, supremeDictionary.cardList);
			updateList(avatarList, supremeDictionary.avatarList);
			updateList(creatureList, supremeDictionary.creatureList);
			updateList(drapeList, supremeDictionary.drapeList);
			updateList(emblemList, supremeDictionary.emblemList);
			updateList(jamList, supremeDictionary.jamList);
			updateList(buffList, supremeDictionary.buffList);
			updateList(titleList, supremeDictionary.titleList);

			HashMap<String, SetOption> updateList = new HashMap<String, SetOption>();
			for(SetOption item : supremeDictionary.setOptionList)
				if(item.Version.compareTo(VERSION)>0) updateList.put(item.setName.getName()+" "+item.requireNum, item);
			
			for(SetOption item : setOptionList){
				SetOption update = updateList.remove(item.setName.getName()+" "+item.requireNum);
				if(update!=null){
					if(update.requireNum!=item.requireNum) System.out.println("asdf");
					try {
						item.fStat=(FunctionStatusList) update.fStat.clone();
						item.vStat=(StatusList) update.vStat.clone();
						item.dStat=(StatusList) update.dStat.clone();
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					item.Version=update.Version;
					item.explanation=update.explanation;
				}
			}
			
			for(SetOption item : updateList.values())
				setOptionList.add(item);
		}
		VERSION=supremeDictionary.getVERSION();
	}
	
	private <T extends Item> void updateList(LinkedList<T> list, LinkedList<T> supremeList)
	{
		updateList(list, supremeList, Job.NONE);
	}
	private <T extends Item> void updateList(LinkedList<T> list, LinkedList<T> supremeList, Job job)
	{
		HashMap<String, T> updateList = new HashMap<String, T>();
		for(T item : supremeList)
			if(item.Version.compareTo(VERSION)>0){
				if(item instanceof Weapon && !((Weapon)item).enabled(job));
				else updateList.put(item.getName(), item);
			}
		
		for(T item : list){
			T update = updateList.remove(item.getName());
			if(update!=null){
				try{
					StatusList temp = (StatusList) update.vStat.clone();
					for(int i=0; i<update.getItemStatIndex(); i++)
						temp.changeStat(i, item.vStat.statList.get(i).stat);
					item.vStat=temp;
					item.dStat=(StatusList) update.dStat.clone();
					item.fStat=(FunctionStatusList) update.fStat.clone();
					item.Version=update.Version;
					item.explanation=update.explanation;
					if(item instanceof Equipment){
						Equipment equip = (Equipment)item;
						equip.isRareItem = ((Equipment)update).isRareItem;
					}
					
					/*if(duplicateList!=null){
						int index = duplicateList.indexOf(item);
						if(index>=0){
							temp = (StatusList) update.vStat.clone();
							for(int i=0; i<update.getItemStatIndex(); i++)
								temp.changeStat(i, item.vStat.statList.get(i).stat);
							item.vStat=temp;
							duplicateList.get(index).dStat=(StatusList) update.dStat.clone();
							duplicateList.get(index).fStat=(FunctionStatusList) update.fStat.clone();
							duplicateList.get(index).Version=update.Version;
							duplicateList.get(index).explanation=update.explanation;
							if(item instanceof Equipment){
								Equipment equip = (Equipment)duplicateList.get(index);
								equip.isRareItem = ((Equipment)update).isRareItem;
							}
						}
					}*/
				}catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		for(T item : updateList.values())
			list.add(item);
	}
	
	public String getVERSION() { return VERSION;}
}

class SaveItemDictionary {
	public static void main(String[] args)
	{
		try{
			ItemDictionary itemDic = new ItemDictionary();
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ItemDictionary.Directory));
			out.writeObject(itemDic);
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}