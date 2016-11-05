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
/*
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
	
	@SuppressWarnings("unchecked")
	public void setDungeonMode(DungeonUI dungeonUI)
	{
		String[] tabNameList = {"무기", "방어구 1", "방어구 2", "방어구 3", "퀘전방어구", "악세서리1", "악세서리 2", "퀘전악세", "특수장비1", "특수장비2", "칭호", "기타", "아바타", "크리쳐/휘장"};
		final int inventoryListNum = tabNameList.length;
		
		inventoryList = new Inventory[inventoryListNum];
		subInventoryList = null;
		inventoryTabList = new TabItem[inventoryListNum]; 
		pack = new Composite[inventoryListNum];
		
		ItemConstraint[] constraintList = setConstraintList(tabNameList.length-1-2, character);
		LinkedList<Item>[] equipList = character.userItemList.separateList(constraintList);
		//LinkedList<Item>[] userEquipList = character.userItemList.separateList(constraintList, true);
		
		LinkedList<?>[] itemList = new LinkedList<?>[equipList.length+2];
		for(int i=0; i<equipList.length; i++)
			itemList[i]=equipList[i];
		itemList[equipList.length] = character.userItemList.getSortedList(character.userItemList.avatarList);
		itemList[equipList.length+1] = character.userItemList.getSortedList(character.userItemList.creatureList);
		//itemList[equipList.length+2] = character.userItemList.getSortedList(character.userItemList.drapeList); 
		
		/*LinkedList<?>[] userItemList = new LinkedList<?>[equipList.length+2];
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

			inventoryList[i] = new Inventory(pack[i], character, dungeonUI, (LinkedList<Item>) itemList[i], Location.DUNGEON);
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
		
		ItemConstraint[] constraintList = setConstraintList(tabNameList.length-1, character);
		
		LinkedList<Item>[] itemList = character.userItemList.separateList(constraintList);
		//LinkedList<Item>[] userItemList = character.userItemList.separateList(constraintList, true);
		LinkedList<Item>[] cardList = character.userItemList.separateCardList(constraintList);
		
		RowLayout packLayout = new RowLayout(SWT.VERTICAL);
		packLayout.spacing=10;
		
		for(int i=0; i<inventoryListNum; i++)
		{
			pack[i] = new Composite(mainComposite, SWT.NONE);
			pack[i].setLayout(packLayout);
			
			inventoryTabList[i]= new TabItem((TabFolder) mainComposite, SWT.NONE);
			inventoryTabList[i].setText(tabNameList[i]);
			
			inventoryList[i] = new Inventory(pack[i], character, itemInfo, itemList[i], Location.VILLAGE);
			subInventoryList[i] = new SubInventory(pack[i], character, itemInfo, cardList[i]);
			
			inventoryTabList[i].setControl(pack[i]);
		}
	}
	/*
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
			for(ItemButton<Item> i : inventory.inventoryList)
				if(part.contains(i.getItem().getPart())) enabledList.add(i.getItem());
		}
		
		return enabledList;
	}

	@Override
	public void renew() {
	}
}
*/