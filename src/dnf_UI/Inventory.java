package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import dnf_InterfacesAndExceptions.ImageSize;
import dnf_class.Characters;
import dnf_class.Item;

public class Inventory {
	LinkedList<Item> itemList;
	ItemButton[] inventory;
	final static int inventoryCol = 20;
	final static int inventoryRow = 5;
	final static int inventorySize = inventoryCol*inventoryRow;
	private Composite inventoryComposite;
	
	public Inventory(Composite parent, LinkedList<Item> itemList, Characters character, UserInfo userInfo)
	{
		this.itemList=itemList;
		inventoryComposite = new Composite(parent, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=0;
		inventoryLayout.verticalSpacing=0;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		inventoryComposite.setLayout(inventoryLayout);
		
		inventory = new ItemButton[inventorySize];
		
		int index=0;
		for(Item i : itemList){
			inventory[index] = new ItemButton(inventoryComposite, i, ImageSize.INVENTORY_BUTTON_SIZE, ImageSize.INVENTORY_BUTTON_SIZE);
			if(!i.getName().equals("이름없음"))
			{
				inventory[index].getButton().addListener(SWT.MouseDown, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(e.button==3){
			        		 character.equip(i);
			        		 userInfo.renew();
			        	 }
			        	 //System.out.println("Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")");
			         }
			     });
			}
			index++;
		}
		
		for(; index<inventorySize; index++)
			inventory[index] = new ItemButton(inventoryComposite, new Item(), ImageSize.INVENTORY_BUTTON_SIZE, ImageSize.INVENTORY_BUTTON_SIZE);
	}
	
	public Composite getComposite() {return inventoryComposite;}
}
