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
	TabFolder villageFolder;
	Characters character;
	
	EquipmentInfoUI equipUI;
	TabItem equipTab;
	EquipmentInfoUI avatarUI;
	TabItem avatarTab;
	
	VillageUI(Shell shell, Characters character)
	{
		this.character=character;
		villageFolder = new TabFolder(shell, SWT.NONE);
		
		equipTab = new TabItem(villageFolder, SWT.NONE);
		String str1 = "장비";
		equipTab.setText(str1);
		equipUI = new EquipmentInfoUI(villageFolder, character, 0);
		equipTab.setControl(equipUI.getComposite());
		
		avatarTab = new TabItem(villageFolder, SWT.NONE);
		String str2 = "아바타/크리쳐/휘장";
		avatarTab.setText(str2);
		avatarUI = new EquipmentInfoUI(villageFolder, character, 1);
		avatarTab.setControl(avatarUI.getComposite());
		
		villageFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				if(villageFolder.getSelection()[0].getText().equals(str1)) equipUI.itemInfo.renew();
				else if(villageFolder.getSelection()[0].getText().equals(str2)) avatarUI.itemInfo.renew();
			}
		});
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
	
	public Vault getVault() {return equipUI.vault;}
	public SkillTree getSkillTree() {return equipUI.skillTree;}
}

class VillageUILoad
{
	public static void main(String[] args)
	{
		GetDictionary.readFile();

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