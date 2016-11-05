package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Item;

public class SubInventory extends DnFComposite
{
	LinkedList<Item> itemList;
	ItemButton<Item>[] inventoryList;
	final int inventoryCol;
	final int inventoryRow;
	final int inventorySize;
	Characters character;
	UserInfo userInfo;
	Composite parent;

	@SuppressWarnings("unchecked")
	public SubInventory(Composite parent, Characters character, UserInfo userInfo, LinkedList<Item> itemList, int col)
	{
		this.itemList=itemList;
		this.character=character;
		this.userInfo=userInfo;
		this.parent=parent;
		this.inventoryCol=col;
		
		mainComposite = new Composite(parent, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=3;
		inventoryLayout.verticalSpacing=3;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		mainComposite.setLayout(inventoryLayout);
		
		mainComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		
		//inventorySize = inventoryCol*inventoryRow<itemList.size() ? inventoryCol*inventoryRow : itemList.size();
		if(col==3) inventoryRow=20;
		else inventoryRow=15;
		inventorySize = inventoryCol*inventoryRow;
		inventoryList = (ItemButton<Item>[]) new ItemButton<?>[inventorySize];
	}
	
	public void setCardListener(Inventory inventory)
	{
		int index=0;
		int prevOrder=Equip_part.WEAPON.order;
		for(Item i : itemList){
			if(prevOrder!=((Card)i).availableOrder())
				for(; index%inventoryCol!=0; index++)
					inventoryList[index] = new ItemButton<Item>(mainComposite, new Item(), InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
			inventoryList[index] = new ItemButton<Item>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
			
			if(!i.getName().equals("이름없음"))
			{
				SetListener listenerGroup = new SetListener(inventoryList[index], character, userInfo, parent);
				
				inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener(null));					// add MouseDoubleClick - modify
				inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(parent.getShell()));		// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 				// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());					// add MouseMove Event - move composite
				listenerGroup.setCardDrag(Display.getCurrent(), (ScrolledComposite) inventory.getComposite());
			}
			index++;
			prevOrder=((Card) i).availableOrder();
		}
		
		for(; index%inventoryCol!=0; index++)
			inventoryList[index] = new ItemButton<Item>(mainComposite, new Item(), InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
			
	}
	
	public void setEmblemListener(Inventory inventory){
		int index=0;
		for(Item i : itemList){
			inventoryList[index] = new ItemButton<Item>(mainComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
			
			if(!i.getName().equals("이름없음"))
			{
				SetListener listenerGroup = new SetListener(inventoryList[index], character, userInfo, parent);
				
				inventoryList[index].getButton().addListener(SWT.MouseDoubleClick, listenerGroup.modifyListener(null));					// add MouseDoubleClick - modify
				inventoryList[index].getButton().addListener(SWT.MouseEnter, listenerGroup.makeItemInfoListener(parent.getShell()));		// add MouseEnter Event - make composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener()); 				// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());					// add MouseMove Event - move composite
				listenerGroup.setCardDrag(Display.getCurrent(), (ScrolledComposite) inventory.getComposite());
			}
			index++;
		}
		for(; index%inventoryCol!=0; index++)
			inventoryList[index] = new ItemButton<Item>(mainComposite, new Item(), InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE);
	}
	
	public ItemButton<Item> getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton<Item> i : inventoryList)
		{
			if(i.getItem()==null) break;
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}

	@Override
	public void renew() {
	}
}
