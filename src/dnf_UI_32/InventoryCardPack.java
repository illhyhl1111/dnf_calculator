package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Location;
import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Characters;
import dnf_class.Creature;
import dnf_class.Drape;
import dnf_class.Item;
import dnf_class.ItemConstraint;

public class InventoryCardPack extends DnFComposite
{
	Inventory[] inventoryList;
	SubInventory[] subInventoryList;
	Composite[] pack;
	TabItem[] inventoryTabList;
	Characters character;
	
	public InventoryCardPack(TabFolder mainComposite, Characters character)
	{		
		this.mainComposite=mainComposite;
		this.character=character;
	}
	
	private ItemConstraint[] setConstraintList(int length)
	{
		ItemConstraint[] constraintList = new ItemConstraint[length];
		
		int num;
		//75~85 에픽무기
		num=0;
		constraintList[num] = new ItemConstraint(75, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.WEAPON);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		
		//80~85 에픽방어구
		num++;
		constraintList[num] = new ItemConstraint(80, 85, character.getJob());
		constraintList[num].partList.add(Equip_part.ROBE);
		constraintList[num].partList.add(Equip_part.TROUSER);
		constraintList[num].partList.add(Equip_part.SHOULDER);
		constraintList[num].partList.add(Equip_part.BELT);
		constraintList[num].partList.add(Equip_part.SHOES);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//90 에픽방어구
		num++;
		constraintList[num] = new ItemConstraint(90, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.ROBE);
		constraintList[num].partList.add(Equip_part.TROUSER);
		constraintList[num].partList.add(Equip_part.SHOULDER);
		constraintList[num].partList.add(Equip_part.BELT);
		constraintList[num].partList.add(Equip_part.SHOES);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//85~90 레전방어구(단일)
		num++;
		constraintList[num] = new ItemConstraint(85, 90, character.getJob());
		constraintList[num].typeList.add(Equip_type.FABRIC);
		constraintList[num].typeList.add(Equip_type.LEATHER);
		constraintList[num].typeList.add(Equip_type.MAIL);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].typeList.add(Equip_type.HEAVY);
		constraintList[num].typeList.add(Equip_type.PLATE);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].setList.add(SetName.NONE);
		
		//85~90 레전방어구(세트)
		num++;
		constraintList[num] = new ItemConstraint(85, 90, character.getJob());
		constraintList[num].typeList.add(Equip_type.FABRIC);
		constraintList[num].typeList.add(Equip_type.LEATHER);
		constraintList[num].typeList.add(Equip_type.MAIL);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].typeList.add(Equip_type.HEAVY);
		constraintList[num].typeList.add(Equip_type.PLATE);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].setList.add(SetName.BURIEDSCREAM);
		constraintList[num].setList.add(SetName.CURSEOFSEAGOD);
		constraintList[num].setList.add(SetName.DEVASTEDGRIEF);
		constraintList[num].setList.add(SetName.GODOFFIGHT);
		constraintList[num].setList.add(SetName.GRACIA);
		constraintList[num].setList.add(SetName.GREATGLORY);
		constraintList[num].setList.add(SetName.GUILDACCESSORY_FIRE);
		constraintList[num].setList.add(SetName.GUILDACCESSORY_WATER);
		constraintList[num].setList.add(SetName.HUGEFORM);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_ACCESSORY);
		constraintList[num].setList.add(SetName.ROMANTICE);
		constraintList[num].setList.add(SetName.ROOTOFDISEASE);
		
		//80~90 에픽 악세(단일)
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.RING);
		constraintList[num].partList.add(Equip_part.BRACELET);
		constraintList[num].partList.add(Equip_part.NECKLACE);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//80~90 레전 악세(단일)
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.RING);
		constraintList[num].partList.add(Equip_part.BRACELET);
		constraintList[num].partList.add(Equip_part.NECKLACE);
		constraintList[num].setList.add(SetName.NONE);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		
		//80~90 레전 악세(세트)
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.RING);
		constraintList[num].partList.add(Equip_part.BRACELET);
		constraintList[num].partList.add(Equip_part.NECKLACE);
		constraintList[num].setList.add(SetName.BURIEDSCREAM);
		constraintList[num].setList.add(SetName.CURSEOFSEAGOD);
		constraintList[num].setList.add(SetName.DEVASTEDGRIEF);
		constraintList[num].setList.add(SetName.GODOFFIGHT);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_FABRIC);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_HARMOR);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_LEATHER);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_MAIL);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_PLATE);
		constraintList[num].setList.add(SetName.GRACIA);
		constraintList[num].setList.add(SetName.GREATGLORY);
		constraintList[num].setList.add(SetName.GUILDACCESSORY_FIRE);
		constraintList[num].setList.add(SetName.GUILDACCESSORY_WATER);
		constraintList[num].setList.add(SetName.HUGEFORM);
		constraintList[num].setList.add(SetName.REAL_PROFIGHTER_ACCESSORY);
		constraintList[num].setList.add(SetName.ROMANTICE);
		constraintList[num].setList.add(SetName.ROOTOFDISEASE);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		
		//80~90 특수장비
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.AIDEQUIPMENT);
		constraintList[num].partList.add(Equip_part.MAGICSTONE);
		constraintList[num].partList.add(Equip_part.EARRING);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		num++;
		constraintList[num] = new ItemConstraint(80, 90, character.getJob());
		constraintList[num].partList.add(Equip_part.AIDEQUIPMENT);
		constraintList[num].partList.add(Equip_part.MAGICSTONE);
		constraintList[num].partList.add(Equip_part.EARRING);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		constraintList[num].rarityList.add(Item_rarity.UNIQUE);
		constraintList[num].rarityList.add(Item_rarity.RARE);
		
		//칭호
		num++;
		constraintList[num] = new ItemConstraint(0, 0, character.getJob());
		constraintList[num].partList.add(Equip_part.TITLE);
		constraintList[num].rarityList.add(Item_rarity.RARE);
		
		return constraintList;
	}
	
	@SuppressWarnings("unchecked")
	public void setDungeonMode(DungeonUI dungeonUI)
	{
		String[] tabNameList = {"무기", "방어구 1", "방어구 2", "방어구 3", "퀘전방어구", "악세서리1", "악세서리 2", "퀘전악세", "특수장비1", "특수장비2", "칭호", "기타", "아바타", "크리쳐/휘장"};
		final int inventoryListNum = tabNameList.length;
		
		inventoryList = new Inventory[inventoryListNum];
		subInventoryList = null;
		inventoryTabList = new TabItem[inventoryListNum]; 
		pack = new Composite[inventoryListNum];
		
		ItemConstraint[] constraintList = setConstraintList(tabNameList.length-1-2);
		LinkedList<Item>[] equipList = character.userItemList.separateList(constraintList, false);
		LinkedList<Item>[] userEquipList = character.userItemList.separateList(constraintList, true);
		
		LinkedList<?>[] itemList = new LinkedList<?>[equipList.length+2];
		for(int i=0; i<equipList.length; i++)
			itemList[i]=equipList[i];
		itemList[equipList.length] = character.userItemList.getSortedList(character.userItemList.avatarList);
		itemList[equipList.length+1] = character.userItemList.getSortedList(character.userItemList.creatureList);
		//itemList[equipList.length+2] = character.userItemList.getSortedList(character.userItemList.drapeList); 
		
		LinkedList<?>[] userItemList = new LinkedList<?>[equipList.length+2];
		for(int i=0; i<userEquipList.length; i++)
			userItemList[i]=userEquipList[i];
		userItemList[userEquipList.length] = character.userItemList.getSortedList(character.userItemList.avatarList_user);
		userItemList[userEquipList.length+1] = new LinkedList<Creature>();
		//userItemList[userEquipList.length+2] = new LinkedList<Drape>(); 
		
		RowLayout packLayout = new RowLayout(SWT.VERTICAL);
		packLayout.spacing=10;
		
		for(int i=0; i<inventoryListNum; i++)
		{
			pack[i] = new Composite(mainComposite, SWT.NONE);
			pack[i].setLayout(packLayout);
			
			inventoryTabList[i]= new TabItem((TabFolder) mainComposite, SWT.NONE);
			inventoryTabList[i].setText(tabNameList[i]);

			inventoryList[i] = new Inventory(pack[i], character, dungeonUI, (LinkedList<Item>) itemList[i], (LinkedList<Item>)userItemList[i], Location.DUNGEON);
			inventoryTabList[i].setControl(pack[i]);
		}
	}
	
	public String[] getAvatarModeList()
	{
		return new String[] {"아바타", "크리쳐/휘장"};
	}
	public int getAvatarTabIndex()
	{
		return inventoryTabList.length-2;
	}
	
	public void setEquipmentMode(UserInfo itemInfo)
	{
		String[] tabNameList = {"무기", "방어구 1", "방어구 2", "방어구 3", "퀘전방어구", "악세서리1", "악세서리2", "퀘전악세", "특수장비1", "특수장비2", "칭호", "기타"};
		final int inventoryListNum = tabNameList.length;
		
		inventoryList = new Inventory[inventoryListNum];
		subInventoryList = new SubInventory[inventoryListNum];
		inventoryTabList = new TabItem[inventoryListNum]; 
		pack = new Composite[inventoryListNum];
		
		ItemConstraint[] constraintList = setConstraintList(tabNameList.length-1);
		
		LinkedList<Item>[] itemList = character.userItemList.separateList(constraintList, false);
		LinkedList<Item>[] userItemList = character.userItemList.separateList(constraintList, true);
		LinkedList<Item>[] cardList = character.userItemList.separateCardList(constraintList);
		
		RowLayout packLayout = new RowLayout(SWT.VERTICAL);
		packLayout.spacing=10;
		
		for(int i=0; i<inventoryListNum; i++)
		{
			pack[i] = new Composite(mainComposite, SWT.NONE);
			pack[i].setLayout(packLayout);
			
			inventoryTabList[i]= new TabItem((TabFolder) mainComposite, SWT.NONE);
			inventoryTabList[i].setText(tabNameList[i]);
			
			inventoryList[i] = new Inventory(pack[i], character, itemInfo, itemList[i], userItemList[i], Location.VILLAGE);
			subInventoryList[i] = new SubInventory(pack[i], character, itemInfo, cardList[i]);
			
			inventoryTabList[i].setControl(pack[i]);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setAvatarMode(UserInfo itemInfo)
	{
		String[] tabNameList = {"아바타", "크리쳐"};
		final int inventoryListNum = tabNameList.length;
		
		inventoryList = new Inventory[inventoryListNum];
		subInventoryList = new SubInventory[inventoryListNum];
		inventoryTabList = new TabItem[inventoryListNum]; 
		pack = new Composite[inventoryListNum];
		LinkedList<?>[] itemList = {
				character.userItemList.getSortedList(character.userItemList.avatarList),
				character.userItemList.getSortedList(character.userItemList.creatureList)};//,
				//character.userItemList.getSortedList(character.userItemList.drapeList)}; 
		LinkedList<?>[] cardList = {
				character.userItemList.getSortedList(character.userItemList.emblemList),
				character.userItemList.getSortedList(character.userItemList.jamList) };
		LinkedList<?>[] userItemList = {
				character.userItemList.getSortedList(character.userItemList.avatarList_user),
				new LinkedList<Creature>()//,
				//new LinkedList<Drape>() 
		};
		
		RowLayout packLayout = new RowLayout(SWT.VERTICAL);
		packLayout.spacing=10;
		
		for(int i=0; i<inventoryListNum; i++)
		{
			pack[i] = new Composite(mainComposite, SWT.NONE);
			pack[i].setLayout(packLayout);
			
			inventoryTabList[i]= new TabItem((TabFolder) mainComposite, SWT.NONE);
			inventoryTabList[i].setText(tabNameList[i]);
			
			inventoryList[i] = new Inventory(pack[i], character, itemInfo, (LinkedList<Item>) itemList[i], (LinkedList<Item>) userItemList[i], Location.VILLAGE);
			subInventoryList[i] = new SubInventory(pack[i], character, itemInfo, (LinkedList<Item>) cardList[i]);
			
			inventoryTabList[i].setControl(pack[i]);
		}
	}
	
	public void setVaultListener(Vault vault)
	{
		for(int i=0; i<inventoryList.length; i++)
		{
			inventoryList[i].setListener(0, vault);
			subInventoryList[i].setListener(inventoryList[i]);
		}
	}
	
	public void setListener()
	{
		for(int i=0; i<inventoryList.length; i++)
		{
			inventoryList[i].setListener(1, null);
			subInventoryList[i].setListener(inventoryList[i]);
		}
	}
	
	public void setDungeonListener(Vault vault)
	{
		vault.setInventoryPack(this);
		for(int i=0; i<inventoryList.length; i++)
		{
			inventoryList[i].setListener(2, vault);
		}
	}
	
	public ItemButton<Item> getItem(String name) throws ItemNotFoundedException
	{
		for(Inventory inventory : inventoryList)
		{
			try{
				ItemButton<Item> temp = inventory.getItem(name);
				return temp;
			}
			catch(ItemNotFoundedException e){}
		}
		throw new ItemNotFoundedException(name);
	}
	
	public LinkedList<Item> getEnabledEquipment(LinkedList<Equip_part> part)
	{
		LinkedList<Item> enabledList = new LinkedList<Item>();
		
		for(Inventory inventory : inventoryList)
		{
			for(ItemButton<Item> i : inventory.inventoryList1)
				if(part.contains(i.getItem().getPart()) && i.getItem().getEnabled()) enabledList.add(i.getItem());
			for(ItemButton<Item> i : inventory.inventoryList2)
				if(part.contains(i.getItem().getPart()) && i.getItem().getEnabled()) enabledList.add(i.getItem());
		}
		
		return enabledList;
	}

	@Override
	public void renew() {
	}
}
