package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import dnf_InterfacesAndExceptions.Emblem_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Location;
import dnf_class.Avatar;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Emblem;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.Title;

public class Inventory extends DnFComposite
{
	Item[] itemList;
	ItemButton<Item>[] inventoryList;
	public final static int inventoryCol=15;
	public final static int inventoryRow=15;
	public final int inventorySize;
	Characters character;
	DnFComposite superInfo;
	Composite parent;
	Composite inventory;
	public final Location location;
	private SubInventory subInventory;
	private SubInventory emblemInventory=null;
	private Composite inventoryPack;

	Vault vault;

	@SuppressWarnings("unchecked")
	public Inventory(Composite parent, Characters character, DnFComposite superInfo, Location location)
	{
		this.itemList=character.userItemList.userInventory;
		inventorySize=itemList.length;
		this.character=character;
		this.superInfo=superInfo;
		this.parent=parent;
		this.location=location;
		
		mainComposite = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		((ScrolledComposite) mainComposite).setExpandVertical(true);
		((ScrolledComposite) mainComposite).setAlwaysShowScrollBars(true);
		mainComposite.addListener(SWT.Activate, new Listener() {
			public void handleEvent(Event e) {
				mainComposite.setFocus();
			}
		});
		
		inventoryPack = new Composite(mainComposite, SWT.NONE);
		inventoryPack.setLayout(new FormLayout());
		
		inventory = new Composite(inventoryPack, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=3;
		inventoryLayout.verticalSpacing=3;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		inventory.setLayout(inventoryLayout);
		inventory.setLayoutData(new FormData());
		inventory.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		
		if(location==Location.VILLAGE){
			subInventory = new SubInventory(inventoryPack, character, null, character.userItemList.getAllCardList(), 5);
			emblemInventory = new SubInventory(inventoryPack, character, null, character.userItemList.getAllEmblemList(), 4);
		}
		else subInventory = new SubInventory(inventoryPack, character, null, character.userItemList.getAllCardList(), 3);
		
		FormData subData = new FormData();
		subData.left = new FormAttachment(inventory, 5);
		subInventory.getComposite().setLayoutData(subData);
		
		if(location==Location.VILLAGE){
			subData = new FormData();
			subData.left = new FormAttachment(subInventory.getComposite(), 5);
			emblemInventory.getComposite().setLayoutData(subData);
		}
		
		inventoryList = (ItemButton<Item>[]) new ItemButton<?>[inventorySize];
		
		((ScrolledComposite) mainComposite).setContent(inventoryPack);
	}
	
	public void setListener(Vault vault)
	{
		this.vault=vault;
		setInventoryBlocks(vault);
		subInventory.setCardListener(this);
		if(emblemInventory!=null) emblemInventory.setEmblemListener(this);
		inventoryPack.setSize(inventoryPack.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		((ScrolledComposite) mainComposite).setMinSize(inventory.getSize());
		//((ScrolledComposite) mainComposite).setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	private void setInventoryBlocks(Vault vault)
	{	
		for(Control control : inventory.getChildren())
			control.dispose();
		for(int index=0; index<inventorySize; index++){
			Item i = itemList[index];
			if(i==null) i = new Item();
			inventoryList[index] = new ItemButton<Item>(inventory, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
			
			SetListener listenerGroup = new SetListener(inventoryList[index], character, superInfo, parent);
			inventoryList[index].getButton().addListener(SWT.MouseDown, listenerGroup.equipListener(vault, this));
			inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener(this));			// add MouseDoubleClick - modify
			inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(parent.getShell(), location));			// add MouseEnter Event - make composite
			inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 		// add MouseExit Event - dispose composite
			inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());			// add MouseMove Event - move composite
			listenerGroup.setItemDrag(index, Display.getCurrent(), (ScrolledComposite) mainComposite);
			setDrop(inventoryList[index], index);
		}
	}
		
	public ItemButton<Item> getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton<Item> i : inventoryList)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}
	
	public LinkedList<Item> getEnabledEquipment(LinkedList<Equip_part> part)
	{
		LinkedList<Item> enabledList = new LinkedList<Item>();
		
		for(ItemButton<Item> i : inventoryList)
			if(part.contains(i.getItem().getPart())) enabledList.add(i.getItem());
		
		return enabledList;
	}
	
	public int firstEmptyIndex()
	{
		int index=0;
		for(ItemButton<Item> i : inventoryList){
			if(i.getItem().getName().contains("없음")) return index;
			index++;
		}
		return -1;
	}
	
	public void addItem(Item item, int index)
	{
		inventoryList[index].setItem(item);
		character.userItemList.userInventory[index]=item;
		inventoryList[index].renewImage();
	}
	public void addItem(Item item)
	{
		int index=firstEmptyIndex();
		if(index>=0){
			inventoryList[index].setItem(item);
			character.userItemList.userInventory[index]=item;
			inventoryList[index].renewImage();
		}
	}
	
	public void removeItem(Item item)
	{
		int index=0;
		for(ItemButton<Item> i : inventoryList){
			if(i.getItem().getName().equals(item.getName())){
				i.setItem(new Item());
				character.userItemList.userInventory[index]=new Item();
				i.renewImage();
				return;
			}
			index++;
		}
	}
	public void removeItem(int index)
	{
		if(index>=0){
			inventoryList[index].setItem(new Item());
			character.userItemList.userInventory[index]=new Item();
			inventoryList[index].renewImage();
			return;
		}
	}
	
	public int getIndex(Item item)
	{
		int index=0;
		for(ItemButton<Item> i : inventoryList){
			if(i.getItem().getName().equals(item.getName())){
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public void setDrop(final ItemButton<Item> itemButton, int destIndex) {

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		DropTarget target = new DropTarget(itemButton.getButton(), operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {

				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				else if(event.data instanceof String){
					try{
						int sourceIndex = Integer.valueOf((String)event.data);
						Item destItem = itemButton.getItem();
						Item sourceItem = inventoryList[sourceIndex].getItem();
						itemList[destIndex] = sourceItem;
						itemList[sourceIndex] = destItem;
						
						itemButton.setItem(itemList[destIndex]);
						itemButton.renewImage();
						inventoryList[sourceIndex].setItem(itemList[sourceIndex]);
						inventoryList[sourceIndex].renewImage();
						
					}catch(NumberFormatException e){
						if (itemButton.getItem() instanceof Equipment || itemButton.getItem() instanceof Title) {
							try {
								String name = (String)event.data;
								if(name.contains("Card - ")) name = name.substring(7);
								else return;
								Card card = character.userItemList.getCard(name);
								if(itemButton.getItem().getIcon()!=null)
								{
									boolean succeed= itemButton.getItem().setCard(card);
									if(succeed){
										superInfo.renew();
										MessageDialog dialog = new MessageDialog(parent.getShell(), "성☆공", null,
											    "마법부여에 성공하였습니다!\n\n보주 : "+name+"\n아이템 : "+itemButton.getItem().getName(),
											    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
										dialog.open();
									}
									else{
										MessageDialog dialog = new MessageDialog(parent.getShell(), "실★패", null,
											    "마법부여에 실패하였습니다\n\n보주 : "+name+"\n가능한 장비 부위 : "+card.getPartToString()+"\n아이템 : "+itemButton.getItem().getName(),
											    MessageDialog.ERROR, new String[] { "납득" }, 0);
										dialog.open();
									}
								}
							} catch (ItemNotFoundedException e2) {
								e2.printStackTrace();
							}
						}
						
						else if (itemButton.getItem() instanceof Avatar) {
							try {
								String name = (String)event.data;
								if(name.contains("Emblem - ")) name = name.substring(9);
								else return;
								Emblem emblem = character.userItemList.getEmblem(name);
								boolean succeed=false;
								if(emblem.type==Emblem_type.PLATINUM) succeed = itemButton.getItem().setPlatinum(emblem);
								else{
									MessageDialog dialog = new MessageDialog(parent.getShell(), "!", null,
										    "적용할 엠블렘 번호를 선택하세요 : ",
										    MessageDialog.QUESTION, new String[] { "1번 엠블렘", "2번 엠블렘" }, 0);
									int result = dialog.open();
									if(result==0) succeed = itemButton.getItem().setEmblem1(emblem);
									else if(result==1) succeed = itemButton.getItem().setEmblem2(emblem);
								}
								
								if(succeed){
									superInfo.renew();
									MessageDialog dialog = new MessageDialog(parent.getShell(), "성☆공", null,
										    "엠블렘 장착에 성공하였습니다!\n\n엠블렘 : "+name+"\n아이템 : "+itemButton.getItem().getName(),
										    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
									dialog.open();
								}
								else{
									MessageDialog dialog = new MessageDialog(parent.getShell(), "실★패", null,
										    "엠블렘 장착에 실패하였습니다\n\n엠블렘 : "+name+"\n아이템 : "+itemButton.getItem().getName(),
										    MessageDialog.ERROR, new String[] { "납득" }, 0);
									dialog.open();
								}
							} catch (ItemNotFoundedException e2) {
								e2.printStackTrace();
							}
						}
					}
				}
			}
		});
	}

	@Override
	public void renew() {	
		setInventoryBlocks(vault);
	}
}
