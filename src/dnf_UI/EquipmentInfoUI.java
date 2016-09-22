package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;

import dnf_InterfacesAndExceptions.Location;
import dnf_class.Characters;

public class EquipmentInfoUI extends DnFComposite{
	Characters character;
	UserInfo itemInfo;
	InventoryCardPack inventoryPack;
	Vault vault;

	Button vaultButton;
	Button batchModify;
	Button emblemModity;
	Button skillButton;
	
	public EquipmentInfoUI(TabFolder parent, Characters character, Vault vault, SkillTree skillTree, int mode)
	{
		this.character=character;
		this.vault=vault;
		
		mainComposite = new Composite(parent, SWT.BORDER);
		mainComposite.setLayout(new FormLayout());
		
		itemInfo = new UserInfo(mainComposite, character, Location.VILLAGE, mode);
		
		TabFolder inventoryFolder = new TabFolder(mainComposite, SWT.NONE);
		
		inventoryPack = new InventoryCardPack(inventoryFolder, character);
		
		if(mode==0){
			inventoryPack.setEquipmentMode(itemInfo);
			vault.setInventoryPack(inventoryPack);
			inventoryPack.setVaultListener(vault, mainComposite);
		}
		else{
			inventoryPack.setAvatarMode(itemInfo);
			inventoryPack.setListener(mainComposite);
		}

		itemInfo.getComposite().setLayoutData(new FormData());

		FormData inventoryData = new FormData();
		inventoryData.top = new FormAttachment(itemInfo.getComposite(), 5);
		inventoryFolder.setLayoutData(inventoryData);
		
		skillButton = new Button(mainComposite, SWT.PUSH);
		skillButton.setText("스킬트리");
		FormData skillButtonData = new FormData();
		skillButtonData.left = new FormAttachment(itemInfo.getComposite(), 10);
		skillButton.setLayoutData(skillButtonData);
		
		skillButton.addListener(SWT.Selection, new Listener(){
			 @Override
	         public void handleEvent(Event e) {
				 if(skillTree.getShell()==null)
					skillTree.open();
           	else
           		skillTree.close();
			 }
		});
		
		if(mode==0){
			vaultButton = new Button(mainComposite, SWT.PUSH);
			vaultButton.setText("금고 열기");
			FormData vaultButtonData = new FormData();
			vaultButtonData.left = new FormAttachment(itemInfo.getComposite(), 10);
			vaultButtonData.top = new FormAttachment(skillButton);
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
			
			batchModify = new Button(mainComposite, SWT.PUSH);
			batchModify.setText("일괄 강화/마법부여");
			FormData batchButtonData = new FormData();	
			batchButtonData.left = new FormAttachment(itemInfo.getComposite(), 10);
			batchButtonData.top = new FormAttachment(vaultButton, 10);
			batchModify.setLayoutData(batchButtonData);
			
			BatchModifier batchModifier = new BatchModifier(parent.getShell(), character, itemInfo, inventoryPack); 
			
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
		
		else if(mode==1)
		{
			emblemModity = new Button(mainComposite, SWT.PUSH);
			emblemModity.setText("엠블렘 옵션 최적화");
			FormData emblemButtonData = new FormData();
			emblemButtonData.top = new FormAttachment(skillButton);
			emblemButtonData.left = new FormAttachment(itemInfo.getComposite(), 10);
			emblemModity.setLayoutData(emblemButtonData);
			
			AvatarModifier avatarModifier = new AvatarModifier(parent.getShell(), character, itemInfo);
			
			emblemModity.addListener(SWT.Selection, new Listener(){
				 @Override
		         public void handleEvent(Event e) {
					 if(avatarModifier.getShell()==null)
						 avatarModifier.open();
	             	else
	             		avatarModifier.close();
				 }
			});
		}
	}

	@Override
	public void renew() {		
	}
}
