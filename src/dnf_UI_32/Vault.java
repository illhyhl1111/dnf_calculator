package dnf_UI_32;

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

import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Location;
import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Characters;
import dnf_class.Item;

public class Vault extends Dialog 
{
	LinkedList<Item> itemList;
	ItemButton<Item>[] vault;
	final static int vaultCol = 20;
	final static int vaultRow = 20;
	final static int vaultSize = vaultCol*vaultRow;
	private Composite vaultComposite;
	private ScrolledComposite scrollComposite;
	private Composite itemInfo;
	private Integer X0;
	private Integer Y0;
	private InventoryCardPack inventory;
	static final int mouseInterval_hor = 7;
	static final int mouseInterval_ver = 5;
	private Characters character;
	
	Shell save;
	
	@SuppressWarnings("unchecked")
	public Vault(Shell parent, Characters character)
	{
		super(parent);
		this.character=character;
		this.itemList=character.userItemList.getVaultItemList(character.getJob());
	
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
		inventoryLayout.horizontalSpacing=3;
		inventoryLayout.verticalSpacing=3;
		inventoryLayout.marginHeight=0;
		inventoryLayout.marginWidth=0;
		vaultComposite.setLayout(inventoryLayout);
		
		vault = (ItemButton<Item>[]) new ItemButton<?>[vaultSize];
		
		int index=0;
		SetName prevSet=SetName.NONE;
		Equip_type prevType=Equip_type.WEAPON;
		
		for(Item i : itemList){
			if(i.getEquipType()!=prevType && prevSet!=i.getSetName()){
				for(; index%vaultCol!=0; index++)
					vault[index] = new ItemButton<Item>(vaultComposite, new Item(), InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE, false);
			}
				
			vault[index] = new ItemButton<Item>(vaultComposite, i, InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE, true);
			if(!i.getName().equals("이름없음"))
			{
				// add MouseExit Event - dispose composite
				vault[index].getButton().addListener(SWT.MouseExit, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(itemInfo!=null && !itemInfo.isDisposed()){
		        			 itemInfo.dispose();
		        		 }
			         }
			     });
				
				// add MouseMove Event - move composite
				vault[index].getButton().addListener(SWT.MouseMove, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(itemInfo!=null && !itemInfo.isDisposed()){
			        		 itemInfo.setLocation((e.x+X0), (e.y+Y0));
		        		 }
			         }
			     });
				
				index++;
				prevType=i.getEquipType();
				prevSet=i.getSetName();
			}
		}
		
		for(; index<vaultSize; index++)
			vault[index] = new ItemButton<Item>(vaultComposite, new Item(), InterfaceSize.INFO_BUTTON_SIZE, InterfaceSize.INFO_BUTTON_SIZE, false);
		
		scrollComposite.setContent(vaultComposite);
		vaultComposite.setSize(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComposite.setMinSize(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	private void setMousePoint(Event e, Composite background, Point itemInfoSize)
	{
		Point mousePoint = ((Control) e.widget).toDisplay(0, 0);
    	mousePoint.x-=background.toDisplay(0, 0).x;
    	mousePoint.y-=background.toDisplay(0, 0).y;
   		
    	Y0=mousePoint.y-itemInfoSize.y-mouseInterval_hor;
		X0 = mousePoint.x-InterfaceSize.ITEM_INFO_SIZE-mouseInterval_ver;
		if(X0<0) X0 = mousePoint.x+mouseInterval_ver;
		if(Y0<0) Y0 = mousePoint.y+mouseInterval_hor;
	}
	
	public void setInventoryPack(InventoryCardPack inventory)
	{
		this.inventory=inventory;
	}
	
	protected Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite) super.createDialogArea(parent);
		scrollComposite.setParent(composite);

		int index=-1;
		for(Item i : itemList){
			
			if(!i.getName().contains("없음"))
			{
				while(vault[++index].getItem().getName().contains("없음"));
				
				// add MouseDown Event - get item - inventory to vault
				vault[index].getButton().addListener(SWT.MouseDown, new Listener() {
					@Override
			        public void handleEvent(Event e) {
						if(e.button==3){
							try{
								ItemButton<Item> temp = inventory.getItem(i.getName());
								temp.getItem().setEnabled(true);
								temp.renewImage(true);
							}
							catch(ItemNotFoundedException e1){
								e1.printStackTrace();
							}
						}
			        }
			    });
				
				// add MouseEnter Event - make composite
				vault[index].getButton().addListener(SWT.MouseEnter, new Listener() {
			         @Override
			         public void handleEvent(Event e) {
			        	 if(i.getName().contains("없음")) return;
			        	
			        	Point itemInfoSize=null;

			        	itemInfo = new Composite(composite.getShell(), SWT.BORDER);
		        		GridLayout layout = new GridLayout(1, false);
		        		layout.verticalSpacing=3;
		        		itemInfo.setLayout(layout);
		        		MakeComposite.setItemInfoComposite(itemInfo, i, Location.VILLAGE, character);
		        		itemInfoSize = itemInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		        		itemInfo.moveAbove(null);
		        		 
		        		setMousePoint(e, composite.getShell(), itemInfoSize);
		        		itemInfo.setBounds((e.x+X0), (e.y+Y0), InterfaceSize.ITEM_INFO_SIZE, itemInfoSize.y);
			        }
			    });
			}
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
	    return new Point(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).x+60, InterfaceSize.VAULT_SIZE_Y);
	}
	
	@Override
	protected void createButtonsForButtonBar(final Composite parent)
	{ 
	  GridLayout layout = (GridLayout)parent.getLayout();
	  layout.marginHeight = 0;
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {           
	    super.setShellStyle(SWT.CLOSE | SWT.MODELESS| SWT.BORDER | SWT.TITLE | SWT.SHELL_TRIM);
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
		for(ItemButton<Item> b : vault)
		{
			Listener[] list = b.getButton().getListeners(SWT.MouseEnter);
			if(list.length!=0)
				b.getButton().removeListener(SWT.MouseEnter, list[0]);
		}
		return super.close();
	}
	
	public ItemButton<Item> getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton<Item> i : vault)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}
}
