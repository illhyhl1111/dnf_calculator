package dnf_infomation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_InterfacesAndExceptions.SetName;
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
import dnf_class.PartyCharacter;
import dnf_class.SetOption;
import dnf_class.Setting;
import dnf_class.Title;
import dnf_class.Weapon;

public class ItemDictionary implements java.io.Serializable, Cloneable
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
	public final LinkedList<PartyCharacter> partyList;
	
	public final LinkedList<Equipment> equipList_user;
	public final LinkedList<Title> titleList_user;
	public final LinkedList<Avatar> avatarList_user;
	public final LinkedList<Setting> settingList;
	
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
		
		partyList = new LinkedList<PartyCharacter>();
		PartyCharacterInfo.getInfo(partyList);
		
		equipList_user = new LinkedList<Equipment>();
		titleList_user = new LinkedList<Title>();
		avatarList_user = new LinkedList<Avatar>();
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
	
	@SuppressWarnings("unchecked")
	public LinkedList<Item>[] separateCardList(ItemConstraint[] constraintList)
	{
		LinkedList<?>[] list = new LinkedList<?>[constraintList.length+1];
		for(int i=0; i<constraintList.length+1; i++)
			list[i] = new LinkedList<Item>();
		
		int i;
		for(Card c : cardList){
			for(i=0; i<constraintList.length; i++){
				if(c.available(constraintList[i].partList))
					((LinkedList<Item>)list[i]).add(c);
			}
		}
		
		return (LinkedList<Item>[]) list;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<Item>[] separateList(ItemConstraint[] constraintList, boolean getUserItemMode)
	{
		LinkedList<?>[] list = new LinkedList<?>[constraintList.length+1];
		for(int i=0; i<constraintList.length+1; i++)
			list[i] = new LinkedList<Item>();
		
		int i;
		LinkedList<Equipment> equipList;
		if(getUserItemMode) equipList = equipList_user;
		else equipList=this.equipList;
		for(Equipment e : equipList){
			for(i=0; i<constraintList.length; i++){
				if(constraintList[i].partList.contains(e.getPart()) && constraintList[i].rarityList.contains(e.getRarity()) 
						&& (constraintList[i].lowerLevel <= e.level && e.level <= constraintList[i].upperLevel) )
					{
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
		//for(Item e : etcList)
			//list.add(e);
		Collections.sort(list);
		return list;
	}
	
	public LinkedList<Buff> getAllBuffList()
	{
		Collections.sort(buffList);
		return buffList;
	}
	
	public LinkedList<PartyCharacter> getPartyList(Job job)
	{
		LinkedList<PartyCharacter> list = new LinkedList<PartyCharacter>();
		for(PartyCharacter b : partyList)
			if(b.job!=job) list.add(b);					//TODO 홀리제외
		
		return list;
	}
		
	public Equipment getEquipment(String name) throws ItemNotFoundedException
	{
		for(Equipment e : equipList)
			if(e.getName().equals(name)) return e;
		for(Equipment e : equipList_user)
			if(e.getName().equals(name)) return e;
		throw new ItemNotFoundedException(name);
	}
	
	public Title getTitle(String name) throws ItemNotFoundedException
	{
		for(Title t : titleList)
			if(t.getName().equals(name)) return t;
		for(Title t : titleList_user)
			if(t.getName().equals(name)) return t;
		throw new ItemNotFoundedException(name);
	}
	
	public Card getCard(String name) throws ItemNotFoundedException
	{
		for(Card e : cardList)
			if(e.getName().equals(name)) return e;
		throw new ItemNotFoundedException(name);
	}
	
	public PartyCharacter getPartyCharacter(String name) throws ItemNotFoundedException
	{
		for(PartyCharacter p : partyList)
			if(p.getName().equals(name)) return p;
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
		for(Avatar a : avatarList_user)
			if(a.getName().equals(name)) return a;
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
	
	public <T extends Item> LinkedList<Item> getSortedList(LinkedList<T> set)
	{
		LinkedList<Item> aList = new LinkedList<Item>();
		for(T t : set)
			aList.add(t);
		Collections.sort(aList);
		Collections.reverse(aList);
		return aList;
	}
	
	public Item makeReplicate(Item item, int leftInventoryButtonNum)
	{
		if(leftInventoryButtonNum<=0) return null;
		
		LinkedList<? extends Item> itemList;
		if(item instanceof Equipment) itemList=equipList_user;
		else if(item instanceof Title) itemList=titleList_user;
		else if(item instanceof Avatar) itemList=avatarList_user;
		else return null;
		
		LinkedList<Integer> replicateNumList = new LinkedList<Integer>();
		int num=1;
		
		for(Item i : itemList)
		{
			if(i.getName().contains(item.getName()+"-복제")){
				replicateNumList.add(i.replicateNum);
			}
		}
		Collections.sort(replicateNumList);
		for(Integer i : replicateNumList)
			if(num==i) num++;
			else break;
		
		Item replicate = (Item) item.clone();
		replicate.replicateNum=num;
		String index = "";
		if(num!=1) index="("+num+")";
		replicate.setName(replicate.getItemName() + "-복제"+index);
		replicate.setEnabled(true);
		
		if(item instanceof Equipment) equipList_user.add((Equipment)replicate);
		else if(item instanceof Title) titleList_user.add((Title)replicate);
		else if(item instanceof Avatar) avatarList_user.add((Avatar)replicate);
		
		return replicate;
	}
	
	public boolean deleteReplicate(Item item)
	{
		boolean result=false;
		if(item instanceof Equipment) result = equipList_user.remove(item);
		else if(item instanceof Title) result = titleList_user.remove(item);
		else if(item instanceof Avatar) result = avatarList_user.remove(item);
		return result;
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