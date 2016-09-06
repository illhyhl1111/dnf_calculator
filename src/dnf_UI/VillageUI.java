package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.ItemConstraint;
import dnf_infomation.GetItemDictionary;

public class VillageUI {
	Characters character;
	UserInfo itemInfo;
	InventoryCardPack inventoryPack;
	Vault vault;
	SubInventory subInventory;
	
	Button vaultButton;
	Button batchModify;

	private Composite villageComposite;
	

	public VillageUI(Shell shell, Characters character)
	{
		this.character=character;
		
		villageComposite = new Composite(shell, SWT.BORDER);
		villageComposite.setLayout(new FormLayout());
		
		itemInfo = new UserInfo(villageComposite, character);
		
		TabFolder inventoryFolder = new TabFolder(villageComposite, SWT.NONE);
		
		inventoryPack = new InventoryCardPack(inventoryFolder, character, itemInfo);
		
		vault = new Vault(shell, GetItemDictionary.itemDictionary.getVaultItemList(), inventoryPack);
		inventoryPack.setLinstener(vault, villageComposite);

		itemInfo.getComposite().setLayoutData(new FormData());

		FormData inventoryData = new FormData();
		inventoryData.top = new FormAttachment(itemInfo.getComposite(), 5);
		inventoryFolder.setLayoutData(inventoryData);
		
		vaultButton = new Button(villageComposite, SWT.PUSH);
		vaultButton.setText("금고 열기");
		FormData vaultButtonData = new FormData();
		vaultButtonData.left = new FormAttachment(itemInfo.getComposite(), 10);
		vaultButton.setLayoutData(vaultButtonData);
		
		vaultButton.addListener(SWT.Selection, new Listener(){
			 @Override
	         public void handleEvent(Event e) {
				 if(vault.getShell()==null)
             		vault.open();
             	else
             		vault.close();
			 }
		});
		
		batchModify = new Button(villageComposite, SWT.PUSH);
		batchModify.setText("일괄 강화/마법부여");
		FormData batchButtonData = new FormData();	
		batchButtonData.left = new FormAttachment(itemInfo.getComposite(), 10);
		batchButtonData.top = new FormAttachment(vaultButton, 10);
		batchModify.setLayoutData(batchButtonData);
		
		BatchModifier batchModifier = new BatchModifier(shell, character, itemInfo, inventoryPack); 
		
		batchModify.addListener(SWT.Selection, new Listener(){
			 @Override
	         public void handleEvent(Event e) {
				 if(batchModifier.getShell()==null)
					 batchModifier.open();
	             	else
	             		batchModifier.close();
			 }
		});
	}
	
	public Composite getComposite(){ return villageComposite; }
}

class InventoryCardPack
{
	Inventory[] inventoryList;
	SubInventory[] subInventoryList;
	Composite[] pack;
	TabItem[] inventoryTabList;
	TabFolder inventoryFolder;
	
	public InventoryCardPack(TabFolder inventoryFolder, Characters character, UserInfo itemInfo)
	{		
		this.inventoryFolder=inventoryFolder;
		String[] tabNameList = {"무기", "방어구 1", "악세서리 1", "악세서리 2", "기타"};
		final int inventoryListNum = tabNameList.length;
		
		inventoryList = new Inventory[inventoryListNum];
		subInventoryList = new SubInventory[inventoryListNum];
		inventoryTabList = new TabItem[inventoryListNum]; 
		pack = new Composite[inventoryListNum];
		ItemConstraint[] constraintList = new ItemConstraint[inventoryListNum-1];
		LinkedList<Item>[] itemList; 
		LinkedList<Item>[] cardList;
		
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
		
		itemList = character.userItemList.separateList(constraintList);
		cardList = character.userItemList.separateCardList(constraintList);
		
		RowLayout packLayout = new RowLayout(SWT.VERTICAL);
		packLayout.spacing=10;
		
		for(int i=0; i<inventoryListNum; i++)
		{
			pack[i] = new Composite(inventoryFolder, SWT.NONE);
			pack[i].setLayout(packLayout);
			
			inventoryTabList[i]= new TabItem(inventoryFolder, SWT.NONE);
			inventoryTabList[i].setText(tabNameList[i]);
			
			inventoryList[i] = new Inventory(pack[i], character, itemInfo, itemList[i]);
			subInventoryList[i] = new SubInventory(pack[i], character, itemInfo, cardList[i]);
			
			inventoryTabList[i].setControl(pack[i]);
		}
	}
	
	public void setLinstener(Vault vault, Composite background)
	{
		for(int i=0; i<inventoryList.length; i++)
		{
			inventoryList[i].setListener(vault, background);
			subInventoryList[i].setListener(inventoryList[i], background);
		}
	}
	
	public ItemButton getItem(String name) throws ItemNotFoundedException
	{
		for(Inventory inventory : inventoryList)
		{
			try{
				ItemButton temp = inventory.getItem(name);
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
			for(ItemButton i : inventory.inventoryList)
				if(part.contains(i.getItem().getPart()) && i.enabled) enabledList.add(i.getItem());
		}
		
		return enabledList;
	}
}