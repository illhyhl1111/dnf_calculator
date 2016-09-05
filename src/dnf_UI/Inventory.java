package dnf_UI;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.Title;

public class Inventory {
	LinkedList<Equipment> itemList;
	ItemButton[] inventoryList;
	final static int inventoryCol=15;
	final static int inventoryRow=5;
	final static int inventorySize=inventoryCol*inventoryRow;
	private Composite inventoryComposite;
	Vault vault;
	Characters character;
	UserInfo userInfo;
	Composite parent;
	private Composite itemInfo;
	private Point itemInfoSize;
	private Composite setInfo;
	private Point setInfoSize;
	private Integer X0;
	private Integer Y0;
	private Boolean hasSetOption;
	
	public Inventory(Composite parent, Characters character, UserInfo userInfo)
	{
		this.itemList=character.userItemList.getAllEquipmentList();
		this.character=character;
		this.userInfo=userInfo;
		this.parent=parent;
		
		inventoryComposite = new Composite(parent, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=0;
		inventoryLayout.verticalSpacing=0;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		inventoryComposite.setLayout(inventoryLayout);
		
		inventoryList = new ItemButton[inventorySize];
	}
	
	public void setListener(Vault vault)
	{
		int index=0;
		inventoryList[0] = new ItemButton(inventoryComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
		Point buttonS = inventoryList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		inventoryList[0].getButton().dispose();
		Integer userY=userInfo.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		
		for(Equipment i : itemList){
			inventoryList[index] =
					new ItemButton(inventoryComposite, i, InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
			
			if(!i.getName().equals("이름없음"))
			{
				setDrop(inventoryList[index]);
				
				Integer indexBox = index;
				Integer xButton = (index%inventoryCol)*buttonS.x+2;
				Integer yButton = (int)(index/inventoryCol)*buttonS.y+userY+8;
				
				// add MouseDown Event - equip
				inventoryList[index].getButton().addListener(SWT.MouseDown, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(e.button==3 && inventoryList[indexBox].enabled){
			        		 if(vault.getShell()==null){
			        			 character.equip(i);
			        			 userInfo.renew();
			        		 }
			        		 else{
			        			 inventoryList[indexBox].enabled=false;
			        			 inventoryList[indexBox].renewImage();
			        			 if(userInfo.userItemInfo.equiped(i)){
			        				 character.unequip(i);
			        				 userInfo.renew();
			        				 if(!itemInfo.isDisposed()) itemInfo.dispose();
			        			 }
			        			 if(itemInfo!=null && !itemInfo.isDisposed()){
					        		 //System.out.println("Mouse Exited "+i.getName());
					        		 itemInfo.dispose();
					        		 if(hasSetOption) setInfo.dispose();
					        	 }
			        		 }
			        	 }
			        	 else if(e.button==1 && inventoryList[indexBox].enabled)
			        	 {
			        		ChangeItemStatus changeItem = new ChangeItemStatus(parent.getShell(), inventoryList[indexBox].getItem(), inventoryList[indexBox].hasSetOption());
			        		int result = changeItem.open();
							if (Window.OK == result) {
								inventoryList[indexBox].setItem(changeItem.item);
								userInfo.renew();
							}
							else if(result == 2)
							{
							//if(inventoryList[indexBox].hasSetOption()){
								SetName setName = ((Equipment)inventoryList[indexBox].getItem()).setName;
								ChangeSetOptionStatus changeSet = new ChangeSetOptionStatus((Shell)parent, setName, character.userItemList);
								if (Window.OK == changeSet.open()) {
									try {
										character.userItemList.setSetOptions(setName, changeSet.setOption);
										inventoryList[indexBox].setItem(changeItem.item);
										userInfo.renew();
									} catch (ItemFileNotFounded e1) {
										e1.printStackTrace();
									}				
								}
							}
			        	 }
			        	 //System.out.println("Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")");
			         }
			     });
				
				// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseEnter, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(inventoryList[indexBox].enabled){
			        		 //System.out.println("Mouse Entered "+i.getName());
			        		 itemInfo = new Composite(parent, SWT.BORDER);
			        		 GridLayout layout = new GridLayout(1, false);
			        		 layout.verticalSpacing=3;
			        		 itemInfo.setLayout(layout);
			        		 MakeComposite.setItemInfoComposite(itemInfo, inventoryList[indexBox].getItem());
			        		 itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			        		 itemInfo.moveAbove(null);
			        		 
			        		 boolean hasSet = inventoryList[indexBox].hasSetOption();
			        		 hasSetOption = hasSet;
			        		 if(hasSet){
			        			 setInfo = new Composite(parent, SWT.BORDER);
			        			 setInfo.setLayout(layout);
			        			 int setNum;
			        			 if(character.getSetOptionList().get( ((Equipment)inventoryList[indexBox].getItem()).setName)==null) setNum=0;
			        			 else setNum=character.getSetOptionList().get( ((Equipment)inventoryList[indexBox].getItem()).setName );
			        			 
			        			 MakeComposite.setSetInfoComposite(setInfo, inventoryList[indexBox].getItem(), setNum, character.userItemList);
				        		 setInfoSize = setInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				        		 setInfo.moveAbove(null);
			        		 }
			        		 
			        		 int x0;
			        		 int y0 = yButton-itemInfoSize.y-5;
			        		 if(hasSet)
			        			 x0 = xButton-InterfaceSize.ITEM_INFO_SIZE-InterfaceSize.SET_INFO_SIZE-5-InterfaceSize.SET_ITEM_INTERVAL;
			        		 else x0 = xButton-InterfaceSize.ITEM_INFO_SIZE-5;
			        		 if(x0<0) x0 = xButton+5;
			        		 if(y0<0) y0 = yButton+5;
			        		 itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
			        		 if(hasSet) setInfo.setBounds((e.x+x0+InterfaceSize.SET_ITEM_INTERVAL+InterfaceSize.ITEM_INFO_SIZE), (e.y+y0), InterfaceSize.SET_INFO_SIZE, setInfoSize.y);
			        		 X0=x0;
			        		 Y0=y0;
			        	 }
			         }
			     });
				
				// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(inventoryList[indexBox].enabled && !itemInfo.isDisposed()){
			        		 //System.out.println("Mouse Exited "+i.getName());
			        		 itemInfo.dispose();
			        		 if(hasSetOption) setInfo.dispose();
			        	 }
			         }
			     });
				
				// add MouseMove Event - move composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(inventoryList[indexBox].enabled && !itemInfo.isDisposed()){
			        		 //System.out.println("Mouse Move (button: " + e.button + " x: " + (e.x+x0) + " y: " + (e.y+y0) + ")");
			        		 itemInfo.setLocation((e.x+X0), (e.y+Y0));
			        		 if(hasSetOption) setInfo.setLocation((e.x+X0+InterfaceSize.SET_ITEM_INTERVAL+InterfaceSize.ITEM_INFO_SIZE), (e.y+Y0));
			        	 }
			         }
			     });
			}
			index++;
		}
		
		for(; index<inventorySize; index++)
			inventoryList[index] = new ItemButton(inventoryComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
	}
	
	public Composite getComposite() {return inventoryComposite;}
	
	public ItemButton getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton i : inventoryList)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}

	public void setDrop(final ItemButton itemButton) {

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
					if (itemButton.getItem() instanceof Equipment || itemButton.getItem() instanceof Title) {
						try {
							Card card = character.userItemList.getCard((String)event.data);
							if(itemButton.enabled)
							{
								boolean succeed;
								if(itemButton.getItem() instanceof Equipment) succeed= ((Equipment)itemButton.getItem()).setCard(card);
								else succeed= ((Equipment)itemButton.getItem()).setCard(card);
								if(succeed){
									userInfo.renew();
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
				}
			}
		});
	}
	
	public LinkedList<Equipment> getEnabledEquipment(LinkedList<Equip_part> part)
	{
		LinkedList<Equipment> enabledList = new LinkedList<Equipment>();
		
		for(ItemButton i : inventoryList)
			if(i.getItem() instanceof Equipment && part.contains(((Equipment)i.getItem()).part) && i.enabled) enabledList.add((Equipment)i.getItem());
		
		return enabledList;
	}
}
