package dnf_UI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import dnf_class.Characters;
import dnf_infomation.GetItemDictionary;

public class VillageUI
{
	Characters character;
	UserInfo itemInfo;
	InventoryCardPack inventoryPack;
	Vault vault;
	SubInventory subInventory;
	
	Button vaultButton;
	Button batchModify;

	private Composite villageComposite;
	
	public VillageUI(Shell shell, Characters character)
	{
		this.character=character;
		
		villageComposite = new Composite(shell, SWT.BORDER);
		villageComposite.setLayout(new FormLayout());
		
		itemInfo = new UserInfo(villageComposite, character);
		
		TabFolder inventoryFolder = new TabFolder(villageComposite, SWT.NONE);
		
		inventoryPack = new InventoryCardPack(inventoryFolder, character, itemInfo);
		
		vault = new Vault(shell, character.userItemList.getVaultItemList(), inventoryPack);
		inventoryPack.setLinstener(vault, villageComposite);

		itemInfo.getComposite().setLayoutData(new FormData());

		FormData inventoryData = new FormData();
		inventoryData.top = new FormAttachment(itemInfo.getComposite(), 5);
		inventoryFolder.setLayoutData(inventoryData);
		
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
		
		BatchModifier batchModifier = new BatchModifier(shell, character, itemInfo, inventoryPack); 
		
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
	
	public void save(Characters character)
	{
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("character_"+character.name+".dfd"));
			out.writeObject(character);
			out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

class VillageUILoad
{
	public static void main(String[] args)
	{
		GetItemDictionary.readFile();

		Display display = new Display();

        Shell shell = new Shell(display);
        shell.setText("인포창");
        shell.setLayout(new FillLayout());
		
        Characters character;
        
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("character_"+"명속은거들뿐"+".dfd"));
			Object temp = in.readObject();
			character = (Characters)temp;

			in.close();
	        
			VillageUI villageUI = new VillageUI(shell, character);
			
	        display.addFilter(SWT.KeyDown, new Listener() {
	            @Override
	            public void handleEvent(Event event) {
	                char c = event.character;
	                //System.out.println(c);
	                if(c=='i'){
	                	if(villageUI.vault.getShell()==null)
	                		villageUI.vault.open();
	                	else
	                		villageUI.vault.close();
	                }
	            }
	        });
	        
	        shell.pack();
	        shell.open();
	        shell.addShellListener(new ShellAdapter(){
				@Override
				public void shellClosed(ShellEvent e) {
					villageUI.save(character);
				}
	        });
	        
	        while (!shell.isDisposed()) {
	            if (!display.readAndDispatch())
	                display.sleep();
	        }
	        display.dispose();
		}
		catch(IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}
}