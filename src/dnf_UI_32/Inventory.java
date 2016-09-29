package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import dnf_InterfacesAndExceptions.Emblem_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Location;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Emblem;
import dnf_class.Item;

public class Inventory extends DnFComposite
{
	LinkedList<Item> itemList1;
	LinkedList<Item> itemList2;
	ItemButton<Item>[] inventoryList1;
	ItemButton<Item>[] inventoryList2;
	final static int inventoryCol=15;
	final static int inventory2Col=2;
	final static int inventoryRow=5;
	final static int inventorySize=inventoryCol*inventoryRow;
	final static int inventory2Size=inventory2Col*inventoryRow;
	Characters character;
	DnFComposite superInfo;
	Composite parent;
	Composite inventory1;
	Composite inventory2;
	private Composite itemInfo;
	private Composite setInfo;
	public final Location location;
	
	int mode;
	Vault vault;
	Composite background;
	
	@SuppressWarnings("unchecked")
	public Inventory(Composite parent, Characters character, DnFComposite superInfo, LinkedList<Item> itemList1, LinkedList<Item> itemList2, Location location)
	{
		this.itemList1=itemList1;
		this.itemList2=itemList2;
		this.character=character;
		this.superInfo=superInfo;
		this.parent=parent;
		this.location=location;
		
		mainComposite = new Composite(parent, SWT.NONE);
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns=2;
		mainLayout.horizontalSpacing=20;
		mainLayout.marginWidth=0;
		mainLayout.marginHeight=0;
		mainComposite.setLayout(mainLayout);
		
		inventory1 = new Composite(mainComposite, SWT.BORDER);
		inventory2 = new Composite(mainComposite, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=3;
		inventoryLayout.verticalSpacing=3;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		inventory1.setLayout(inventoryLayout);
		inventory1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		
		inventoryLayout = new GridLayout(inventory2Col, true);
		inventoryLayout.horizontalSpacing=3;
		inventoryLayout.verticalSpacing=3;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		inventory2.setLayout(inventoryLayout);
		inventory2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		
		inventoryList1 = (ItemButton<Item>[]) new ItemButton<?>[inventorySize];
		inventoryList2 = (ItemButton<Item>[]) new ItemButton<?>[inventory2Size];
	}
	
	public void setListener(int mode, Composite background, Vault vault)
	{
		this.mode=mode;
		this.vault=vault;
		this.background=background;
		setInventoryBlocks(mode, background, vault, false);
		setInventoryBlocks(mode, background, vault, true);
	}
	
	private void setInventoryBlocks(int mode, Composite background, Vault vault, boolean getUserItemMode)
	{
		int index=0;
		ItemButton<Item>[] inventoryList;
		LinkedList<Item> itemList;
		Composite inventory;
		int size;
		
		if(!getUserItemMode){
			inventoryList=inventoryList1;
			itemList=itemList1;
			inventory=inventory1;
			size=inventorySize;
		}
		else{
			inventoryList=inventoryList2;
			itemList=itemList2;
			inventory=inventory2;
			size=inventory2Size;
		}
		
		for(Control control : inventory.getChildren())
			control.dispose();
		
		for(Item i : itemList)
		{
			inventoryList[index] =
					new ItemButton<Item>(inventory, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
			
			if(!i.getName().equals("이름없음"))
			{
				if(mode!=2) setDrop(inventoryList[index], mode);

				SetListener listenerGroup = new SetListener(inventoryList[index], character, superInfo, itemInfo, setInfo, parent);
				
				if(mode==0) inventoryList[index].getButton().addListener(SWT.MouseDown, listenerGroup.equipListener(vault)); 			// add MouseDown Event - unequip
				else if(mode==1) inventoryList[index].getButton().addListener(SWT.MouseDown, listenerGroup.equipListener()); 			// add MouseDown Event - unequip
				else if(mode==2) inventoryList[index].getButton().addListener(SWT.MouseDown, listenerGroup.equipListener());
				inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener(this));			// add MouseDoubleClick - modify
				inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background, location));			// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 		// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());			// add MouseMove Event - move composite
			}
			index++;
		}
		
		for(; index<size; index++){
			inventoryList[index] = new ItemButton<Item>(inventory, new Item(), InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE, false);
		}
	}
		
	public ItemButton<Item> getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton<Item> i : inventoryList1)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		for(ItemButton<Item> i : inventoryList2)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}

	public void setDrop(final ItemButton<Item> itemButton, int mode) {

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
					if (mode==0) {
						try {
							Card card = character.userItemList.getCard((String)event.data);
							if(itemButton.getItem().getEnabled())
							{
								boolean succeed= itemButton.getItem().setCard(card);
								if(succeed){
									superInfo.renew();
									MessageDialog dialog = new MessageDialog(parent.getShell(), "성☆공", null,
										    "마법부여에 성공하였습니다!\n\n보주 : "+(String)event.data+"\n아이템 : "+itemButton.getItem().getName(),
										    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
									dialog.open();
								}
								else{
									MessageDialog dialog = new MessageDialog(parent.getShell(), "실★패", null,
										    "마법부여에 실패하였습니다\n\n보주 : "+(String)event.data+"\n가능한 장비 부위 : "+card.getPartToString()+"\n아이템 : "+itemButton.getItem().getName(),
										    MessageDialog.ERROR, new String[] { "납득" }, 0);
									dialog.open();
								}
							}
						} catch (ItemFileNotFounded e) {
							e.printStackTrace();
						}
					}
					
					else if (mode==1) {
						try {
							Emblem emblem = character.userItemList.getEmblem((String)event.data);
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
									    "엠블렘 장착에 성공하였습니다!\n\n엠블렘 : "+(String)event.data+"\n아이템 : "+itemButton.getItem().getName(),
									    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
								dialog.open();
							}
							else{
								MessageDialog dialog = new MessageDialog(parent.getShell(), "실★패", null,
									    "엠블렘 장착에 실패하였습니다\n\n엠블렘 : "+(String)event.data+"\n아이템 : "+itemButton.getItem().getName(),
									    MessageDialog.ERROR, new String[] { "납득" }, 0);
								dialog.open();
							}
						} catch (ItemFileNotFounded e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	public LinkedList<Item> getEnabledEquipment(LinkedList<Equip_part> part)
	{
		LinkedList<Item> enabledList = new LinkedList<Item>();
		
		for(ItemButton<Item> i : inventoryList1)
			if(part.contains(i.getItem().getPart()) && i.getItem().getEnabled()) enabledList.add(i.getItem());
		
		return enabledList;
	}

	@Override
	public void renew() {	
		setInventoryBlocks(mode, background, vault, true);
		inventory2.layout();
		parent.layout();
	}
}
