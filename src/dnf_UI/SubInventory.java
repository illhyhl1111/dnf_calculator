package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Characters;
import dnf_class.Item;

public class SubInventory 
{
	LinkedList<Item> itemList;
	ItemButton[] inventoryList;
	final static int inventoryCol=15;
	final static int inventoryRow=2;
	final static int inventorySize=inventoryCol*inventoryRow;
	private Composite inventoryComposite;
	Characters character;
	UserInfo userInfo;
	Composite parent;
	private Composite itemInfo;
	
	public SubInventory(Composite parent, Characters character, UserInfo userInfo, LinkedList<Item> itemList)
	{
		this.itemList=itemList;
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
	
	public void setListener(Inventory inventory, Composite background)
	{
		int index=0;
		inventoryList[0] = new ItemButton(inventoryComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
		Point buttonS = inventoryList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		inventoryList[0].getButton().dispose();
		Integer userY=userInfo.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT).y+inventory.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT).y+10;
		
		for(Item i : itemList){
			inventoryList[index] =
					new ItemButton(inventoryComposite, i, InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, true);
			
			if(!i.getName().equals("이름없음"))
			{
				Integer xButton = (index%inventoryCol)*buttonS.x+8;
				Integer yButton = (int)(index/inventoryCol)*buttonS.y+userY+33;
				
				SetListener listenerGroup = new SetListener(inventoryList[index], character, userInfo, itemInfo, null, xButton, yButton, parent);
				
				inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener());					// add MouseDoubleClick - modify
				inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background));		// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 				// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());					// add MouseMove Event - move composite
				listenerGroup.setDrag();
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
}
