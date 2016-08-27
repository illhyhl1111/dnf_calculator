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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_class.Item;

public class Vault extends Dialog {
	LinkedList<Item> itemList;
	ItemButton[] vault;
	final static int vaultCol = 20;
	final static int vaultRow = 20;
	final static int vaultSize = vaultCol*vaultRow;
	private Composite vaultComposite;
	private ScrolledComposite scrollComposite;
	Inventory inventory;
	
	public Vault(Shell parent, LinkedList<Item> itemList, Inventory inventory)
	{
		super(parent);
		this.itemList=itemList;
		this.inventory=inventory;
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		
		scrollComposite = new ScrolledComposite(content, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
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
			vault[index] = new ItemButton(vaultComposite, i, InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, true);
			if(!i.getName().equals("이름없음"))
			{
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
			}
			index++;
		}
		
		for(; index<vaultSize; index++)
			vault[index] = new ItemButton(vaultComposite, new Item(), InterfaceSize.INVENTORY_BUTTON_SIZE, InterfaceSize.INVENTORY_BUTTON_SIZE, false);
		
		scrollComposite.setContent(vaultComposite);
		vaultComposite.setSize(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComposite.setMinSize(vaultComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		return content;
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
	
	public ItemButton getItem(String name) throws ItemNotFoundedException
	{
		for(ItemButton i : vault)
		{
			if(i.getItem().getName().equals(name)) return i;
		}
		throw new ItemNotFoundedException(name);
	}
}
