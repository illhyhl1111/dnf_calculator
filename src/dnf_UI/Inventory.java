package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Characters;
import dnf_class.Item;

public class Inventory {
	LinkedList<Item> itemList;
	ItemButton[] inventoryList;
	final static int inventoryCol = 15;
	final static int inventoryRow = 5;
	final static int inventorySize = inventoryCol*inventoryRow;
	private Composite inventoryComposite;
	Vault vault;
	Characters character;
	UserInfo userInfo;
	
	public Inventory(Composite parent, LinkedList<Item> itemList, Characters character, UserInfo userInfo)
	{
		this.itemList=itemList;
		this.character=character;
		this.userInfo=userInfo;
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
		for(Item i : itemList){
			inventoryList[index] = new ItemButton(inventoryComposite, i, InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
			if(!i.getName().equals("이름없음"))
			{
				Integer indexBox = index;
				inventoryList[index].getButton().addListener(SWT.MouseDown, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(e.button==3 || inventoryList[indexBox].enabled){
			        		 if(vault.getShell()==null){
			        			 character.equip(i);
			        			 userInfo.renew();
			        		 }
			        		 else{
			        			 inventoryList[indexBox].enabled=false;
			        			 inventoryList[indexBox].renewImage();
			        		 }
			        	 }
			        	 //System.out.println("Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")");
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
}
