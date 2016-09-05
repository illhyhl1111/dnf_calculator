package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_class.Characters;
import dnf_infomation.GetItemDictionary;

public class VillageUI {
	UserInfo itemInfo;
	Inventory inventory;
	Vault vault;
	SubInventory subInventory;
	
	Button vaultButton;
	Button batchModify;

	private Composite villageComposite;

	public VillageUI(Shell shell, Characters character)
	{
		villageComposite = new Composite(shell, SWT.BORDER);
		villageComposite.setLayout(new FormLayout());
		
		itemInfo = new UserInfo(villageComposite, character);
		inventory = new Inventory(villageComposite, character, itemInfo);
		vault = new Vault(shell, GetItemDictionary.itemDictionary.getAllEquipmentList(), inventory);
		inventory.setListener(vault);
		subInventory = new SubInventory(villageComposite, character, itemInfo, inventory);
		subInventory.setListener();

		itemInfo.getComposite().setLayoutData(new FormData());

		FormData inventoryData = new FormData();
		inventoryData.top = new FormAttachment(itemInfo.getComposite(), 5);
		inventory.getComposite().setLayoutData(inventoryData);

		FormData subInventoryData = new FormData();
		subInventoryData.top = new FormAttachment(inventory.getComposite(), 10);
		subInventory.getComposite().setLayoutData(subInventoryData);
		
		vaultButton = new Button(villageComposite, SWT.PUSH);
		vaultButton.setText("금고 열기");
		FormData vaultButtonData = new FormData();
		vaultButtonData.left = new FormAttachment(itemInfo.getComposite(), 10);
		vaultButton.setLayoutData(vaultButtonData);
		
		vaultButton.addListener(SWT.Selection, new Listener(){
			 @Override
	         public void handleEvent(Event e) {
				 if(vault.getShell()==null)
             		vault.open();
             	else
             		vault.close();
			 }
		});
		
		batchModify = new Button(villageComposite, SWT.PUSH);
		batchModify.setText("일괄 강화/마법부여");
		FormData batchButtonData = new FormData();	
		batchButtonData.left = new FormAttachment(itemInfo.getComposite(), 10);
		batchButtonData.top = new FormAttachment(vaultButton, 10);
		batchModify.setLayoutData(batchButtonData);
		
		BatchModifier batchModifier = new BatchModifier(shell, character, itemInfo, inventory); 
		
		batchModify.addListener(SWT.Selection, new Listener(){
			 @Override
	         public void handleEvent(Event e) {
				 if(batchModifier.getShell()==null)
					 batchModifier.open();
	             	else
	             		batchModifier.close();
			 }
		});
	}
	
	public Composite getComposite(){ return villageComposite; }
}
