package dnf_UI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
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
import org.eclipse.swt.widgets.TabItem;

import dnf_class.Characters;
import dnf_infomation.GetDictionary;

public class VillageUI
{
	private TabFolder villageFolder;
	
	private EquipmentInfoUI equipUI;
	private TabItem equipTab;
	private EquipmentInfoUI avatarUI;
	private TabItem avatarTab;
	private Composite villageComposite;
	private Button toDungeonButton;
	private Button selectCharacterButton;
	private Characters character;
	private Vault vault;
	
	VillageUI(Shell shell, Characters character)
	{
		this.character=character;
		villageComposite = new Composite(shell, SWT.NONE);
		villageComposite.setLayout(new FormLayout());
		
		vault = new Vault(shell, character.userItemList.getVaultItemList(character.getJob()));
		
		toDungeonButton = new Button(villageComposite, SWT.PUSH);
		toDungeonButton.setText("수련의 방 입장");
		
		selectCharacterButton = new Button(villageComposite, SWT.PUSH);
		selectCharacterButton.setText("캐릭터 선택");
		
	}
	
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
	
	public void renew()
	{
		villageFolder = new TabFolder(villageComposite, SWT.NONE);
		villageFolder.setLayoutData(new FormData());
		
		equipTab = new TabItem(villageFolder, SWT.NONE);
		String str1 = "장비";
		equipTab.setText(str1);
		equipUI = new EquipmentInfoUI(villageFolder, character, vault, 0);
		equipTab.setControl(equipUI.getComposite());
		
		avatarTab = new TabItem(villageFolder, SWT.NONE);
		String str2 = "아바타/크리쳐/휘장";
		avatarTab.setText(str2);
		avatarUI = new EquipmentInfoUI(villageFolder, character, vault, 1);
		avatarTab.setControl(avatarUI.getComposite());
		
		villageFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				if(villageFolder.getSelection()[0].getText().equals(str1)) equipUI.itemInfo.renew();
				else if(villageFolder.getSelection()[0].getText().equals(str2)) avatarUI.itemInfo.renew();
			}
		});
		
		FormData dungeonBData = new FormData(100, 100);
		dungeonBData.left = new FormAttachment(villageFolder, 10);
		toDungeonButton.setLayoutData(dungeonBData);
		
		FormData selectCharBData = new FormData(100, 100);
		selectCharBData.left = new FormAttachment(villageFolder, 10);
		selectCharBData.top = new FormAttachment(toDungeonButton, 10);
		selectCharacterButton.setLayoutData(selectCharBData);
		
		villageComposite.layout();
	}
	
	public void disposeContent()
	{
		villageFolder.dispose();
	}
	
	public Vault getVault() {return equipUI.vault;}
	public SkillTree getSkillTree() {return equipUI.skillTree;}
	public Composite getComposite() {return villageComposite;}
	public Button get_toDungeonButton() {return toDungeonButton;}
	public Button get_selectCharacterButton() {return selectCharacterButton;}
}

class VillageUILoad
{
	public static void main(String[] args)
	{
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
			GetDictionary.readFile(character.getJob());
	        
			VillageUI villageUI = new VillageUI(shell, character);
			
	        display.addFilter(SWT.KeyDown, new Listener() {
	            @Override
	            public void handleEvent(Event event) {
	                char c = event.character;
	                //System.out.println(c);
	                if(c=='i'){
	                	if(villageUI.getVault().getShell()==null)
	                		villageUI.getVault().open();
	                	else
	                		villageUI.getVault().close();
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