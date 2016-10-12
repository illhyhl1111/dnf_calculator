package dnf_UI_32;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_class.Characters;
import dnf_infomation.GetDictionary;

public class CalculatorUITest {
	public static void main(String[] args)
	{
		Display display;
		final boolean openSleak = true;
		
		if(openSleak)
		{
			DeviceData data = new DeviceData();
			data.tracking = true;
		    display = new Display(data);
		    Sleak sleak = new Sleak();
		    sleak.open();
		}
		else display = new Display();
		
		Shell selectionShell = new Shell(display);
		selectionShell.setSize(800, 550);
		selectionShell.setText("캐릭터 선택");
		selectionShell.setLayout(new FormLayout());
		
		Label loadingLabel = new Label(selectionShell, SWT.CENTER);
		loadingLabel.setText("데이터를 읽는 중입니다 ㄱㄷ");
		loadingLabel.setLayoutData(new FormData());
		selectionShell.open();
		GetDictionary.readFile();
		
		SelectCharacter selectionSet = new SelectCharacter(selectionShell);
		selectionSet.setSelectionComposite();
		while (!selectionShell.isDisposed()) {
		    if (!display.readAndDispatch())
		        display.sleep();
		}
		if(selectionSet.getSelected()==null) return;
		
		Characters character = new Characters(selectionSet.getSelected());
		GetDictionary.getSkillIcon(character.getJob());

		TerminateSignal terminateSignal = new TerminateSignal();
		makeMainShell(display, character, terminateSignal);
		
		while (!terminateSignal.isTerminated()) {
		    if (!display.readAndDispatch())
		        display.sleep();
		}
		display.dispose();
	}
	
	public static void makeMainShell(Display display, Characters character, TerminateSignal terminateSignal)
	{
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
		shell.open();
		
		VillageUI villageUI = new VillageUI(shell, character);
		DungeonUI dungeonUI = new DungeonUI(shell, character);
		SkillTree skillTree = new SkillTree(shell, character, villageUI);
		Vault vault = new Vault(shell, character);
		
		villageUI.makeComposite(skillTree, vault);
	
		
		stackLayout.topControl = villageUI.getComposite();
		shell.setText("인포창");
		
		villageUI.get_toDungeonButton().addListener(SWT.Selection, event -> {
			villageUI.disposeContent();
			dungeonUI.makeComposite(vault);
			stackLayout.topControl = dungeonUI.getComposite();
			skillTree.superInfo=dungeonUI;
			shell.setText("수련의 방");
			shell.layout();
		});
		
		dungeonUI.get_toVillageButton().addListener(SWT.Selection, event -> {
			dungeonUI.disposeContent();
			villageUI.makeComposite(skillTree, vault);
			skillTree.superInfo=villageUI;
			stackLayout.topControl = villageUI.getComposite();
			shell.setText("인포창");
			shell.layout();
		});
		
		villageUI.get_selectCharacterButton().addListener(SWT.Selection, event ->{
			terminateSignal.selectionShellterminated=false;
			shell.dispose();
			Shell newShell = new Shell(display);
			newShell.setSize(800, 550);
			newShell.setText("캐릭터 선택");
			newShell.setLayout(new FormLayout());
			
			SelectCharacter selectionSet = new SelectCharacter(newShell);
			selectionSet.setSelectionComposite();
			newShell.open();
			while (!newShell.isDisposed()) {
			    if (!display.readAndDispatch())
			        display.sleep();
			}
			
			if(selectionSet.getSelected()==null){
				terminateSignal.selectionShellterminated=true;
				return;
			}
			else{
				terminateSignal.mainShellterminated=false;
				terminateSignal.selectionShellterminated=true;
				Characters newCharacter = new Characters(selectionSet.getSelected());
				GetDictionary.getSkillIcon(newCharacter.getJob());
				makeMainShell(display, newCharacter, terminateSignal);
			}
		});
		
		Listener keyFilter = new Listener() {
			@Override
			public void handleEvent(Event event) {
				char c = event.character;
				if ((c == 'i' || c == 'I' || c == 'ㅑ')) {
					if (vault.getShell() == null)
						vault.open();
					else
						vault.close();
				}

				else if (c == 'k' || c == 'K' || c == 'ㅏ') {
					if (skillTree.getShell() == null)
						skillTree.open();
					else
						skillTree.close();
				}
			}
		};
		display.addFilter(SWT.KeyDown, keyFilter);
		
		shell.layout();
		
		shell.addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent e) {
				try{
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data\\character_"+character.name+".dfd"));
					out.writeObject(character);
					out.close();
				}
				catch(IOException e2)
				{
					e2.printStackTrace();
				}
				for(Shell shell : display.getShells()) shell.dispose();
				display.removeFilter(SWT.KeyDown, keyFilter);
				terminateSignal.mainShellterminated=true;
			}			
		});
	}
}

class CalculatorUILoad
{
	public static void main(String[] args)
	{
		Display display = new Display();
		
		Shell selectionShell = new Shell(display);
		selectionShell.setSize(800, 550);
		selectionShell.setText("캐릭터 선택");
		selectionShell.setLayout(new FormLayout());
		
		Label loadingLabel = new Label(selectionShell, SWT.CENTER);
		loadingLabel.setText("데이터를 읽는 중입니다 ㄱㄷ");
		FormData loadData = new FormData();
		loadData.top = new FormAttachment(0, 270);
		loadData.left = new FormAttachment(0, 330);
		loadingLabel.setLayoutData(loadData);
		selectionShell.open();
		GetDictionary.readFile();
		
		SelectCharacter selectionSet = new SelectCharacter(selectionShell);
		selectionSet.setSelectionComposite();
		while (!selectionShell.isDisposed()) {
		    if (!display.readAndDispatch())
		        display.sleep();
		}
		if(selectionSet.getSelected()==null) return;
		
		Characters character;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\character_"+selectionSet.getSelected().name+".dfd"));
			Object temp = in.readObject();
			character = (Characters)temp;
			in.close();
		}catch(FileNotFoundException e)
		{
			character = new Characters(selectionSet.getSelected());
			System.out.println("저장");
		}catch(ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
			return;
		}
		GetDictionary.getSkillIcon(character.getJob());
		TerminateSignal terminateSignal = new TerminateSignal();
		makeMainShell(display, character, terminateSignal);
		
		while (!terminateSignal.isTerminated()) {
		    if (!display.readAndDispatch())
		        display.sleep();
		}
		display.dispose();
	}
	
	public static void makeMainShell(Display display, Characters character, TerminateSignal terminateSignal)
	{
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
		shell.open();
		
		VillageUI villageUI = new VillageUI(shell, character);
		DungeonUI dungeonUI = new DungeonUI(shell, character);
		SkillTree skillTree = new SkillTree(shell, character, villageUI);
		Vault vault = new Vault(shell, character);
		
		villageUI.makeComposite(skillTree, vault);
	
		
		stackLayout.topControl = villageUI.getComposite();
		shell.setText("인포창");
		
		villageUI.get_toDungeonButton().addListener(SWT.Selection, event -> {
			villageUI.disposeContent();
			dungeonUI.makeComposite(vault);
			stackLayout.topControl = dungeonUI.getComposite();
			skillTree.superInfo=dungeonUI;
			shell.setText("수련의 방");
			shell.layout();
		});
		
		dungeonUI.get_toVillageButton().addListener(SWT.Selection, event -> {
			dungeonUI.disposeContent();
			villageUI.makeComposite(skillTree, vault);
			skillTree.superInfo=villageUI;
			stackLayout.topControl = villageUI.getComposite();
			shell.setText("인포창");
			shell.layout();
		});
		
		villageUI.get_selectCharacterButton().addListener(SWT.Selection, event ->{
			terminateSignal.selectionShellterminated=false;
			shell.dispose();
			Shell newShell = new Shell(display);
			newShell.setSize(800, 550);
			newShell.setText("캐릭터 선택");
			newShell.setLayout(new FormLayout());
			
			SelectCharacter selectionSet = new SelectCharacter(newShell);
			selectionSet.setSelectionComposite();
			newShell.open();
			while (!newShell.isDisposed()) {
			    if (!display.readAndDispatch())
			        display.sleep();
			}
			
			if(selectionSet.getSelected()==null){
				terminateSignal.selectionShellterminated=true;
				return;
			}
			else{
				terminateSignal.mainShellterminated=false;
				terminateSignal.selectionShellterminated=true;
				Characters newCharacter;
				try{
					ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\character_"+selectionSet.getSelected().name+".dfd"));
					Object temp = in.readObject();
					newCharacter = (Characters)temp;
					in.close();
				}catch(FileNotFoundException e)
				{
					newCharacter = new Characters(selectionSet.getSelected());
					System.out.println("저장");
				}catch(ClassNotFoundException | IOException e)
				{
					e.printStackTrace();
					return;
				}
				GetDictionary.getSkillIcon(newCharacter.getJob());
				makeMainShell(display, newCharacter, terminateSignal);
			}
		});
		
		Listener keyFilter = new Listener() {
			@Override
			public void handleEvent(Event event) {
				char c = event.character;
				if ((c == 'i' || c == 'I' || c == 'ㅑ')) {
					if (vault.getShell() == null)
						vault.open();
					else
						vault.close();
				}

				else if (c == 'k' || c == 'K' || c == 'ㅏ') {
					if (skillTree.getShell() == null)
						skillTree.open();
					else
						skillTree.close();
				}
			}
		};
		display.addFilter(SWT.KeyDown, keyFilter);
		
		shell.layout();
		
		shell.addDisposeListener(new DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent e) {
				try{
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data\\character_"+character.name+".dfd"));
					out.writeObject(character);
					out.close();
				}
				catch(IOException e2)
				{
					e2.printStackTrace();
				}
				for(Shell shell : display.getShells()) shell.dispose();
				display.removeFilter(SWT.KeyDown, keyFilter);
				terminateSignal.mainShellterminated=true;
			}			
		});
	}
}

class TerminateSignal
{
	boolean mainShellterminated;
	boolean selectionShellterminated;
	TerminateSignal(){
		this.mainShellterminated=false;
		this.selectionShellterminated=true;
	}
	
	public boolean isTerminated()
	{
		return mainShellterminated && selectionShellterminated;
	}
}