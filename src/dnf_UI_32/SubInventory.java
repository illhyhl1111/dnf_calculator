package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Characters;
import dnf_class.Item;

public class SubInventory extends DnFComposite
{
	LinkedList<Item> itemList;
	ItemButton<Item>[] inventoryList;
	final static int inventoryCol=15;
	final static int inventoryRow=2;
	final static int inventorySize=inventoryCol*inventoryRow;
	Characters character;
	UserInfo userInfo;
	Composite parent;

	@SuppressWarnings("unchecked")
	public SubInventory(Composite parent, Characters character, UserInfo userInfo, LinkedList<Item> itemList)
	{
		this.itemList=itemList;
		this.character=character;
		this.userInfo=userInfo;
		this.parent=parent;
		
		mainComposite = new Composite(parent, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=3;
		inventoryLayout.verticalSpacing=3;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		mainComposite.setLayout(inventoryLayout);
		
		inventoryList = (ItemButton<Item>[]) new ItemButton<?>[inventorySize];
	}
	
	public void setListener(Inventory inventory, Composite background)
	{
		int index=0;
		
		for(Item i : itemList){
			inventoryList[index] =
					new ItemButton<Item>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE, true);
			
			if(!i.getName().equals("이름없음"))
			{
				SetListener listenerGroup = new SetListener(inventoryList[index], character, userInfo, parent);
				
				inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener(null));					// add MouseDoubleClick - modify
				inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(background));		// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 				// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());					// add MouseMove Event - move composite
				listenerGroup.setItemDrag();
			}
			index++;
		}
		
		for(; index<inventorySize; index++)
			inventoryList[index] = new ItemButton<Item>(mainComposite, new Item(), InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE, false);
	}
	
	public ItemButton<Item> getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton<Item> i : inventoryList)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}

	@Override
	public void renew() {
	}
}
