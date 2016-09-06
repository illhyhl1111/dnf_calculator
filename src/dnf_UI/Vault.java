package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Item;
import dnf_infomation.GetItemDictionary;

public class Vault extends Dialog {
	LinkedList<Item> itemList;
	ItemButton[] vault;
	final static int vaultCol = 20;
	final static int vaultRow = 20;
	final static int vaultSize = vaultCol*vaultRow;
	private Composite vaultComposite;
	private ScrolledComposite scrollComposite;
	InventoryCardPack inventory;
	private Composite itemInfo;
	private Point itemInfoSize;
	private Composite setInfo;
	private Point setInfoSize;
	private Integer X0;
	private Integer Y0;
	private Boolean hasSetOption;
	
	Shell save;
	
	public Vault(Shell parent, LinkedList<Item> itemList, InventoryCardPack inventory)
	{
		super(parent);
		this.itemList=itemList;
		this.inventory=inventory;
	
		save = new Shell(Display.getCurrent());
		
		scrollComposite = new ScrolledComposite(save, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setAlwaysShowScrollBars(true);
        scrollComposite.addListener(SWT.Activate, new Listener() {
			public void handleEvent(Event e) {
				scrollComposite.setFocus();
			}
		});
		vaultComposite = new Composite(scrollComposite, SWT.BORDER);
		GridLayout inventoryLayout = new GridLayout(vaultCol, true);
		inventoryLayout.horizontalSpacing=0;
		inventoryLayout.verticalSpacing=0;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		vaultComposite.setLayout(inventoryLayout);
		
		vault = new ItemButton[vaultSize];
		
		int index=0;
		
		for(Item i : itemList){
			Integer indexBox = index;
			vault[index] = new ItemButton(vaultComposite, i, InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, true);
			
			if(!i.getName().equals("이름없음"))
			{
				// add MouseDown Event - get item - inventory to vault
				vault[index].getButton().addListener(SWT.MouseDown, new Listener() {
					@Override
			        public void handleEvent(Event e) {
						if(e.button==3){
							try{
								ItemButton temp = inventory.getItem(i.getName());
								temp.enabled=true;
								temp.renewImage();
							}
							catch(ItemNotFoundedException e1){
								e1.printStackTrace();
							}
						}
			        	 //System.out.println("Mouse Down (button: " + e.button + " x: " + e.x + " y: " + e.y + ")");
			        }
			    });
				
				// add MouseExit Event - dispose composite
				vault[index].getButton().addListener(SWT.MouseExit, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(vault[indexBox].enabled){
			        		 if(itemInfo!=null && !itemInfo.isDisposed()){
			        			 itemInfo.dispose();
			        			 if(hasSetOption) setInfo.dispose();
			        		 }
			        	 }
			         }
			     });
				
				// add MouseMove Event - move composite
				vault[index].getButton().addListener(SWT.MouseMove, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(vault[indexBox].enabled){
			        		 if(itemInfo!=null && !itemInfo.isDisposed()){
				        		 itemInfo.setLocation((e.x+X0), (e.y+Y0));
				        		 if(hasSetOption) setInfo.setLocation((e.x+X0+InterfaceSize.SET_ITEM_INTERVAL+InterfaceSize.ITEM_INFO_SIZE), (e.y+Y0));
			        		 }
			        	 }
			         }
			     });			
				index++;
			}
		}
		
		for(; index<vaultSize; index++)
			vault[index] = new ItemButton(vaultComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
		
		scrollComposite.setContent(vaultComposite);
		vaultComposite.setSize(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComposite.setMinSize(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite) super.createDialogArea(parent);
		scrollComposite.setParent(composite);
		
		Point buttonS = vault[0].getButton().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		
		int index=0;
		for(Item i : itemList){
			
			Integer indexBox = index;
			Integer xButton = (index%vaultCol)*buttonS.x+15;
			Integer yButton = (int)(index/vaultCol)*buttonS.y+17;
			
			if(!i.getName().equals("이름없음"))
			{
				// add MouseEnvet Event - make composite
				vault[index].getButton().addListener(SWT.MouseEnter, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(vault[indexBox].enabled){
			        		 //System.out.println("Mouse Entered "+i.getName());
			        		 itemInfo = new Composite(parent, SWT.BORDER);
			        		 GridLayout layout = new GridLayout(1, false);
			        		 layout.verticalSpacing=3;
			        		 itemInfo.setLayout(layout);
			        		 MakeComposite.setItemInfoComposite(itemInfo, vault[indexBox].getItem());
			        		 itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			        		 itemInfo.moveAbove(null);
			        		 
			        		 boolean hasSet = vault[indexBox].hasSetOption();
			        		 hasSetOption = hasSet;
			        		 if(hasSet){
			        			 setInfo = new Composite(parent, SWT.BORDER);
			        			 setInfo.setLayout(layout);
			        			 MakeComposite.setSetInfoComposite(setInfo, vault[indexBox].getItem(), 0, GetItemDictionary.itemDictionary);
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
			}
			index++;
		}
		return composite;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("금고금고금고");
	}

	@Override
	protected Point getInitialSize() {
	    return new Point(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).x+50, InterfaceSize.VAULT_SIZE_Y);
	}
	
	@Override
	protected void createButtonsForButtonBar(final Composite parent)
	{ 
	  GridLayout layout = (GridLayout)parent.getLayout();
	  layout.marginHeight = 0;
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {           
	    super.setShellStyle(SWT.CLOSE | SWT.MODELESS| SWT.BORDER | SWT.TITLE);
	    setBlockOnOpen(false);
	}
	
	@Override
	protected boolean isResizable() {
	    return true;
	}
	
	@Override
	public boolean close()
	{
		scrollComposite.setParent(save);
		for(ItemButton b : vault)
		{
			Listener[] list = b.getButton().getListeners(SWT.MouseEnter);
			if(list.length!=0)
				b.getButton().removeListener(SWT.MouseEnter, list[0]);
		}
		return super.close();
	}
	
	public ItemButton getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton i : vault)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}
}
