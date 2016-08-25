package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import dnf_InterfacesAndExceptions.ImageSize;
import dnf_class.Item;

public class Inventory {
	LinkedList<Item> itemList;
	ItemButton[] inventory;
	final static int inventoryCol = 20;
	final static int inventoryRow = 5;
	final static int inventorySize = inventoryCol*inventoryRow;
	private Composite inventoryComposite;
	
	public Inventory(Composite parent, LinkedList<Item> itemList)
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
		for(Item i : itemList)
			inventory[index++] = new ItemButton(inventoryComposite, i, ImageSize.BUTTON_SIZE, ImageSize.BUTTON_SIZE);
		
		for(; index<inventorySize; index++)
			inventory[index] = new ItemButton(inventoryComposite, new Item(), ImageSize.BUTTON_SIZE, ImageSize.BUTTON_SIZE);
	}
	
	public Composite getComposite() {return inventoryComposite;}
}
