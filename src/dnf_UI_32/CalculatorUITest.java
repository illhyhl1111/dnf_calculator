package dnf_UI_32;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.Job;
import dnf_class.Characters;
import dnf_infomation.GetDictionary;

public class CalculatorUITest {
	public static void main(String[] args) {
		final boolean openSleak = false;
		Display display;
		
		if(openSleak)
		{
			DeviceData data = new DeviceData();
			data.tracking = true;
		    display = new Display(data);
		    Sleak sleak = new Sleak();
		    sleak.open();
		}
		else display = new Display();
		
		GetDictionary.readFile(Job.LAUNCHER_F);
		Characters character = new Characters(90, Job.LAUNCHER_F, "여런처");

		Shell shell = new Shell(display);
		final StackLayout stackLayout = new StackLayout();
		shell.setLayout(stackLayout);
		if(display.getBounds().height==900 && display.getBounds().width==1600){
			shell.setMinimumSize(1600, 900);
			shell.setMaximized(true);
		}
		else{
			shell.setMinimumSize(1600, 900);
			shell.setBounds(0, 0, 1600, 900);
		}
		
		VillageUI villageUI = new VillageUI(shell, character);
		DungeonUI dungeonUI = new DungeonUI(shell, character);
		SkillTree skillTree = new SkillTree(shell, character, villageUI);
		
		villageUI.makeComposite(skillTree);
	
		
		stackLayout.topControl = villageUI.getComposite();
		shell.setText("인포창");
		
		villageUI.get_toDungeonButton().addListener(SWT.Selection, event -> {
			villageUI.disposeContent();
			dungeonUI.makeComposite();
			stackLayout.topControl = dungeonUI.getComposite();
			skillTree.superInfo=dungeonUI;
			shell.setText("수련의 방");
			shell.layout();
		});
		
		dungeonUI.get_toVillageButton().addListener(SWT.Selection, event -> {
			dungeonUI.disposeContent();
			villageUI.makeComposite(skillTree);
			skillTree.superInfo=villageUI;
			stackLayout.topControl = villageUI.getComposite();
			shell.setText("인포창");
			shell.layout();
		});
		
		display.addFilter(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				char c = event.character;
				// System.out.println(c);
				if ((c == 'i' || c == 'I') && stackLayout.topControl == villageUI.getComposite()) {
					if (villageUI.getVault().getShell() == null)
						villageUI.getVault().open();
					else
						villageUI.getVault().close();
				}

				else if (c == 'k' || c == 'K') {
					if (skillTree.getShell() == null)
						skillTree.open();
					else
						skillTree.close();
				}
			}
		});
		
		shell.open();
		
		shell.addShellListener(new ShellAdapter(){
			@Override
			public void shellClosed(ShellEvent e) {
				try{
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("character_"+character.name+".dfd"));
					out.writeObject(character);
					out.close();
				}
				catch(IOException e2)
				{
					e2.printStackTrace();
				}
			}
		});
		
		while (!shell.isDisposed()) {
		    if (!display.readAndDispatch())
		        display.sleep();
		}
		display.dispose();
	}
}

class CalculatorUILoad
{
	public static void main(String[] args)
	{
		Display display = new Display();
		
        final Characters character;
        Characters readFile=null;
		try{
			try{
				ObjectInputStream in = new ObjectInputStream(new FileInputStream("character_"+"여런처"+".dfd"));
				Object temp = in.readObject();
				readFile = (Characters)temp;
				GetDictionary.readFile(readFile.getJob());
				in.close();
			}catch(FileNotFoundException e)
			{
				GetDictionary.readFile(Job.LAUNCHER_F);
				readFile = new Characters(90, Job.LAUNCHER_F, "여런처");
				System.out.println("저장");
			}
			finally{
				character=readFile;
			}
			
			Shell shell = new Shell(display);
			final StackLayout stackLayout = new StackLayout();
			shell.setLayout(stackLayout);
			if(display.getBounds().height==900 && display.getBounds().width==1600){
				shell.setMinimumSize(1600, 900);
				shell.setMaximized(true);
			}
			else{
				shell.setMinimumSize(1600, 900);
				shell.setBounds(0, 0, 1600, 900);
			}
	        
			VillageUI villageUI = new VillageUI(shell, character);
			DungeonUI dungeonUI = new DungeonUI(shell, character);
			SkillTree skillTree = new SkillTree(shell, character, villageUI);
			
			villageUI.makeComposite(skillTree);

			stackLayout.topControl = villageUI.getComposite();
			shell.setText("인포창");
			
			villageUI.get_toDungeonButton().addListener(SWT.Selection, event -> {
				villageUI.disposeContent();
				dungeonUI.makeComposite();
				stackLayout.topControl = dungeonUI.getComposite();
				skillTree.superInfo=dungeonUI;
				shell.setText("수련의 방");
				shell.layout();
			});
			
			dungeonUI.get_toVillageButton().addListener(SWT.Selection, event -> {
				dungeonUI.disposeContent();
				villageUI.makeComposite(skillTree);
				stackLayout.topControl = villageUI.getComposite();
				shell.setText("인포창");
				shell.layout();
			});
			
			display.addFilter(SWT.KeyDown, new Listener() {
				@Override
				public void handleEvent(Event event) {
					char c = event.character;
					// System.out.println(c);
					if ((c == 'i' || c == 'I') && stackLayout.topControl == villageUI.getComposite()) {
						if (villageUI.getVault().getShell() == null)
							villageUI.getVault().open();
						else
							villageUI.getVault().close();
					}

					else if (c == 'k' || c == 'K') {
						if (skillTree.getShell() == null)
							skillTree.open();
						else
							skillTree.close();
					}
				}
			});
			
			shell.open();
			
			shell.addShellListener(new ShellAdapter(){
				@Override
				public void shellClosed(ShellEvent e) {
					try{
						ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("character_"+character.name+".dfd"));
						out.writeObject(character);
						out.close();
					}
					catch(IOException e2)
					{
						e2.printStackTrace();
					}
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