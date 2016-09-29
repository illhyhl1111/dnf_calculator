package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Location;
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
		
		//80~85 에픽방어구
		num++;
		constraintList[num] = new ItemConstraint(80, 85, character.getJob());
		constraintList[num].partList.add(Equip_part.ROBE);
		constraintList[num].partList.add(Equip_part.TROUSER);
		constraintList[num].partList.add(Equip_part.SHOULDER);
		constraintList[num].partList.add(Equip_part.BELT);
		constraintList[num].partList.add(Equip_part.SHOES);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//80~85 에픽악세보법
		num++;
		constraintList[num] = new ItemConstraint(80, 85, character.getJob());
		constraintList[num].partList.add(Equip_part.RING);
		constraintList[num].partList.add(Equip_part.BRACELET);
		constraintList[num].partList.add(Equip_part.NECKLACE);
		constraintList[num].partList.add(Equip_part.AIDEQUIPMENT);
		constraintList[num].partList.add(Equip_part.MAGICSTONE);
		constraintList[num].partList.add(Equip_part.EARRING);
		constraintList[num].rarityList.add(Item_rarity.EPIC);
		
		//85 레전악세보법
		num++;
		constraintList[num] = new ItemConstraint(85, 85, character.getJob());
		constraintList[num].partList.add(Equip_part.RING);
		constraintList[num].partList.add(Equip_part.BRACELET);
		constraintList[num].partList.add(Equip_part.NECKLACE);
		constraintList[num].partList.add(Equip_part.AIDEQUIPMENT);
		constraintList[num].partList.add(Equip_part.MAGICSTONE);
		constraintList[num].partList.add(Equip_part.EARRING);
		constraintList[num].rarityList.add(Item_rarity.LEGENDARY);
		
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
		String[] tabNameList = {"무기", "방어구 1", "악세서리 1", "악세서리 2", "칭호", "기타", "아바타", "크리쳐", "휘장"};
		final int inventoryListNum = tabNameList.length;
		
		inventoryList = new Inventory[inventoryListNum];
		subInventoryList = null;
		inventoryTabList = new TabItem[inventoryListNum]; 
		pack = new Composite[inventoryListNum];
		
		ItemConstraint[] constraintList = setConstraintList(tabNameList.length-1-3);
		LinkedList<Item>[] equipList = character.userItemList.separateList(constraintList, false);
		LinkedList<Item>[] userEquipList = character.userItemList.separateList(constraintList, true);
		
		LinkedList<?>[] itemList = new LinkedList<?>[equipList.length+3];
		for(int i=0; i<equipList.length; i++)
			itemList[i]=equipList[i];
		itemList[equipList.length] = character.userItemList.getHashSetToLinkedList(character.userItemList.avatarList);
		itemList[equipList.length+1] = character.userItemList.getHashSetToLinkedList(character.userItemList.creatureList);
		itemList[equipList.length+2] = character.userItemList.getHashSetToLinkedList(character.userItemList.drapeList); 
		
		LinkedList<?>[] userItemList = new LinkedList<?>[equipList.length+3];
		for(int i=0; i<userEquipList.length; i++)
			userItemList[i]=userEquipList[i];
		userItemList[userEquipList.length] = character.userItemList.getHashSetToLinkedList(character.userItemList.avatarList_user);
		userItemList[userEquipList.length+1] = new LinkedList<Creature>();
		userItemList[userEquipList.length+2] = new LinkedList<Drape>(); 
		
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
		return new String[] {"아바타", "크리쳐", "휘장"};
	}
	public int getAvatarTabIndex()
	{
		return inventoryTabList.length-3;
	}
	
	public void setEquipmentMode(UserInfo itemInfo)
	{
		String[] tabNameList = {"무기", "방어구 1", "악세서리 1", "악세서리 2", "칭호", "기타"};
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
		String[] tabNameList = {"아바타", "크리쳐", "휘장"};
		final int inventoryListNum = tabNameList.length;
		
		inventoryList = new Inventory[inventoryListNum];
		subInventoryList = new SubInventory[inventoryListNum];
		inventoryTabList = new TabItem[inventoryListNum]; 
		pack = new Composite[inventoryListNum];
		LinkedList<?>[] itemList = {
				character.userItemList.getHashSetToLinkedList(character.userItemList.avatarList),
				character.userItemList.getHashSetToLinkedList(character.userItemList.creatureList),
				character.userItemList.getHashSetToLinkedList(character.userItemList.drapeList)}; 
		LinkedList<?>[] cardList = {
				character.userItemList.getHashSetToLinkedList(character.userItemList.emblemList),
				new LinkedList<Item>(), 
				character.userItemList.getHashSetToLinkedList(character.userItemList.jamList) };
		LinkedList<?>[] userItemList = {
				character.userItemList.getHashSetToLinkedList(character.userItemList.avatarList_user),
				new LinkedList<Creature>(),
				new LinkedList<Drape>() 
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
	
	public void setVaultListener(Vault vault, Composite background)
	{
		for(int i=0; i<inventoryList.length; i++)
		{
			inventoryList[i].setListener(0, background, vault);
			subInventoryList[i].setListener(inventoryList[i], background);
		}
	}
	
	public void setListener(Composite background)
	{
		for(int i=0; i<inventoryList.length; i++)
		{
			inventoryList[i].setListener(1, background, null);
			subInventoryList[i].setListener(inventoryList[i], background);
		}
	}
	
	public void setDungeonListener(Composite background)
	{
		for(int i=0; i<inventoryList.length; i++)
		{
			inventoryList[i].setListener(2, background, null);
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
