package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
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
	Composite parent;
	Composite itemInfo;
	
	public Inventory(Composite parent, LinkedList<Item> itemList, Characters character, UserInfo userInfo)
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
	
	public void setListener(Vault vault)
	{
		int index=0;
		inventoryList[0] = new ItemButton(inventoryComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
		Point buttonS = inventoryList[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		inventoryList[0].getButton().dispose();
		Integer userY=userInfo.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		
		for(Item i : itemList){
			inventoryList[index] = new ItemButton(inventoryComposite, i, InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
			if(!i.getName().equals("이름없음"))
			{
				Integer indexBox = index;
				Integer x0 = (index%inventoryCol)*buttonS.x;
				Integer y0 = (int)(index/inventoryCol)*buttonS.y+userY;
				
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
			        			 //TODO 장착된 아이템을 창고에 넣을때 장착해제
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
			        		 System.out.println("Mouse Entered "+i.getName());
			        		 itemInfo = new Composite(parent, SWT.BORDER);
			        		 itemInfo.setLayout(new RowLayout(SWT.VERTICAL));
			        		 inventoryList[indexBox].setItemInfoComposite(itemInfo);
			        		 itemInfo.setBounds((e.x+x0), (e.y+y0-300),  500,  300);
			        		 itemInfo.moveAbove(inventoryComposite);
			        		 itemInfo.moveAbove(userInfo.getComposite());
			        	 }
			         }
			     });
				
				// add MouseExit Event - dispose composite
				inventoryList[index].getButton().addListener(SWT.MouseExit, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(inventoryList[indexBox].enabled){
			        		 System.out.println("Mouse Exited "+i.getName());
			        		 itemInfo.dispose();
			        	 }
			         }
			     });
				
				// add MouseMove Event - move composite
				inventoryList[index].getButton().addListener(SWT.MouseMove, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(inventoryList[indexBox].enabled){
			        		 //System.out.println("Mouse Move (button: " + e.button + " x: " + (e.x+x0) + " y: " + (e.y+y0) + ")");
			        		 itemInfo.setLocation((e.x+x0), (e.y+y0-300));
			        		 itemInfo.moveAbove(inventoryComposite);
			        		 itemInfo.moveAbove(userInfo.getComposite());
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
}
