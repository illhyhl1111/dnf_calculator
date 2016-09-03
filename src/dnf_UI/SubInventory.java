package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Characters;
import dnf_class.Item;

public class SubInventory {
	LinkedList<Item> itemList;
	ItemButton[] inventoryList;
	final static int inventoryCol=15;
	final static int inventoryRow=2;
	final static int inventorySize=inventoryCol*inventoryRow;
	private Composite inventoryComposite;
	Characters character;
	UserInfo userInfo;
	Composite parent;
	Inventory inventory;
	private Composite itemInfo;
	private Point itemInfoSize;
	private Integer X0;
	private Integer Y0;
	
	public SubInventory(Composite parent, Characters character, UserInfo userInfo, Inventory inventory)
	{
		this.itemList=character.userItemList.getAllCardList();
		this.character=character;
		this.userInfo=userInfo;
		this.parent=parent;
		this.inventory=inventory;
		
		inventoryComposite = new Composite(parent, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(inventoryCol, true);
		inventoryLayout.horizontalSpacing=0;
		inventoryLayout.verticalSpacing=0;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		inventoryComposite.setLayout(inventoryLayout);
		
		inventoryList = new ItemButton[inventorySize];
	}
	
	public void setListener()
	{
		int index=0;
		inventoryList[0] = new ItemButton(inventoryComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
		Point buttonS = inventoryList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		inventoryList[0].getButton().dispose();
		Integer userY=userInfo.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT).y+inventory.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT).y+20;
		
		for(Item i : itemList){
			inventoryList[index] =
					new ItemButton(inventoryComposite, i, InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, true);
			
			if(!i.getName().equals("이름없음"))
			{
				setDrag(inventoryList[index]);
				
				Integer indexBox = index;
				Integer xButton = (index%inventoryCol)*buttonS.x+2;
				Integer yButton = (int)(index/inventoryCol)*buttonS.y+userY+8;
				
				// add MouseDown Event - equip
				inventoryList[index].getButton().addListener(SWT.MouseDown, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(e.button==3 && inventoryList[indexBox].enabled){
			        		 //TODO
			        	 }
			        	 /*else if(e.button==1 && inventoryList[indexBox].enabled)
			        	 {
			        		ChangeItemStatus changeItem = new ChangeItemStatus((Shell)parent, inventoryList[indexBox].getItem(), false);
			        		int result = changeItem.open();
							if (Window.OK == result) {
								inventoryList[indexBox].setItem(changeItem.item);
								if(userInfo.userItemInfo.equiped(i)) character.setStatus();
								userInfo.renew();
							}
			        	 }*/
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
			        		 inventoryList[indexBox].setItemInfoComposite(itemInfo);
			        		 itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			        		 itemInfo.moveAbove(null);
			        		 
			        		 int x0;
			        		 int y0 = yButton-itemInfoSize.y-5;
			        		 x0 = xButton-InterfaceSize.ITEM_INFO_SIZE-5;
			        		 if(x0<0) x0 = xButton+5;
			        		 if(y0<0) y0 = yButton+5;
			        		 itemInfo.setBounds((e.x+x0), (e.y+y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
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
			        	 }
			         }
			     });
			}
			index++;
		}
		
		for(; index<inventorySize; index++)
			inventoryList[index] = new ItemButton(inventoryComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
	}
	
	public void setDrag(final ItemButton itemButton) {

		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		final DragSource source = new DragSource(itemButton.getButton(), operations);
		source.setTransfer(types);
		source.addDragListener(new DragSourceAdapter() {
			public void dragStart(DragSourceEvent event) {
				event.doit = (itemButton.getButton().getImage() != null);

				// getting control widget - Composite in this case
				Button composite = (Button) ((DragSource) event.getSource()).getControl();
				// Getting dimensions of this widget
				Point compositeSize = composite.getSize();
				// creating new GC
				GC gc = new GC(composite);
				// Creating new Image
				Image image = new Image(Display.getCurrent(), compositeSize.x, compositeSize.y);
				// Rendering widget to image
				gc.copyArea(image, 0, 0);
				// Setting widget to DnD image
				event.image = image;
				
				itemInfo.dispose();
			}

			public void dragSetData(DragSourceEvent event) {
				event.data = itemButton.getItem().getName();
			}
		});
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
