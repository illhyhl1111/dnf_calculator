package dnf_UI;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.MessageDialog;
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

import dnf_InterfacesAndExceptions.Emblem_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Emblem;
import dnf_class.Item;

public class Inventory 
{
	LinkedList<Item> itemList;
	ItemButton[] inventoryList;
	final static int inventoryCol=15;
	final static int inventoryRow=5;
	final static int inventorySize=inventoryCol*inventoryRow;
	private Composite inventoryComposite;
	Characters character;
	UserInfo userInfo;
	Composite parent;
	private Composite itemInfo;
	private Composite setInfo;
	final int mode;
	
	public Inventory(Composite parent, Characters character, UserInfo userInfo, LinkedList<Item> itemList, int mode)
	{
		this.itemList=itemList;
		this.character=character;
		this.userInfo=userInfo;
		this.parent=parent;
		this.mode=mode;
		
		inventoryComposite = new Composite(parent, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=0;
		inventoryLayout.verticalSpacing=0;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		inventoryComposite.setLayout(inventoryLayout);
		
		inventoryList = new ItemButton[inventorySize];
	}
	
	public void setListener(int mode, Composite background, Vault vault)
	{
		int index=0;
		inventoryList[0] = new ItemButton(inventoryComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
		Point buttonS = inventoryList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		inventoryList[0].getButton().dispose();
		Integer userY=userInfo.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		
		for(Item i : itemList)
		{
			inventoryList[index] =
					new ItemButton(inventoryComposite, i, InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE);
			
			if(!i.getName().equals("이름없음"))
			{
				setDrop(inventoryList[index], mode);

				Integer xButton = (index%inventoryCol)*buttonS.x+10;
				Integer yButton = (int)(index/inventoryCol)*buttonS.y+userY+38;
				
				SetListener listenerGroup = new SetListener(inventoryList[index], character, userInfo, itemInfo, setInfo, xButton, yButton, parent);
				
				if(mode==0) inventoryList[index].getButton().addListener(SWT.MouseDown, listenerGroup.equipListener(vault)); 			// add MouseDown Event - unequip
				else if(mode==1) inventoryList[index].getButton().addListener(SWT.MouseDown, listenerGroup.equipListener()); 			// add MouseDown Event - unequip
				inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener());			// add MouseDoubleClick - modify
				inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background));			// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 		// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());			// add MouseMove Event - move composite
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

	public void setDrop(final ItemButton itemButton, int mode) {

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
								userInfo.renew();
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
		
		for(ItemButton i : inventoryList)
			if(part.contains(i.getItem().getPart()) && i.getItem().getEnabled()) enabledList.add(i.getItem());
		
		return enabledList;
	}
}
