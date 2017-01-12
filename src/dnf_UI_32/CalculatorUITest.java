package dnf_UI_32;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_class.Characters;
import dnf_infomation.BriefCharacterInfo;
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
		selectionShell.setSize(1024, 768);
		selectionShell.setText("캐릭터 선택");
		selectionShell.setLayout(new FormLayout());
		
		Label loadingLabel = new Label(selectionShell, SWT.CENTER);
		loadingLabel.setText("로오오오딩중");
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
			dungeonUI.makeComposite(skillTree, vault);
			stackLayout.topControl = dungeonUI.getComposite();
			shell.setText("수련의 방");
			shell.layout();
		});
		
		dungeonUI.get_toVillageButton().addListener(SWT.Selection, event -> {
			dungeonUI.disposeContent();
			villageUI.makeComposite(skillTree, vault);
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

class NewVersion extends TitleAreaDialog {

    private String[] explain;
    private String URL;
    private String version;
    public boolean readed;

    public NewVersion(Shell parentShell, String version, String URL, ArrayList<String> explain) {
    	super(parentShell);
    	this.version = version;
    	this.explain=explain.toArray(new String[0]);
    	this.URL=URL;
    	readed=false;
    }

    @Override
    public void create() {
            super.create();
            setTitle("업데이트 알림");
            setMessage("새로운 버전이 나왔습니다. 와와", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
            Composite area = (Composite) super.createDialogArea(parent);
            Composite container = new Composite(area, SWT.NONE);
            container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            GridLayout layout = new GridLayout(2, false);
            container.setLayout(layout);

            createInput(container);
            return area;
    }

    private void createInput(Composite container) {
    	Label versionLabel = new Label(container, SWT.NONE);
    	versionLabel.setText("최근 버전 : "+version);
    	versionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
    	
    	Label currentVersionLabel = new Label(container, SWT.NONE);
    	currentVersionLabel.setText("현재 버전 : "+CalculatorVersion.MAIN_VERSION);
    	currentVersionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));

    	NewVersion thisShell = this;
    	Label URLLabel = new Label(container, SWT.NONE);
    	URLLabel.setText("다운로드 링크 : ");
    	Link link = new Link(container, SWT.NONE);
    	link.setText("<A>"+URL+"</A>");
    	link.addSelectionListener(new SelectionAdapter(){
    		@Override
	        public void widgetSelected(SelectionEvent e) {
    			System.out.println("You have selected: "+e.text);
    			Program.launch(e.text);
				readed=true;
				thisShell.close();
	        }
	    });
    	        
    	Label explLabel = new Label(container, SWT.NONE);
		explLabel.setText("\n\n최근 변경사항 : ");
		explLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
    	for(String expl : explain){
    		explLabel = new Label(container, SWT.NONE);
    		explLabel.setText(expl);
    		explLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
    	}
    }
    
    @Override
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.CANCEL_ID, "안해요 안해", false);
	}
}

class CalculatorUILoad
{
	public static void main(String[] args)
	{
		Display display = new Display();
		Shell selectionShell = new Shell(display);
		
		SelectCharacter selectionSet = new SelectCharacter(selectionShell);
		
		URL url;
		try {
			// get URL content
			url = new URL("https://raw.githubusercontent.com/illhyhl1111/dnf_calculator/master/RecentVersion");
			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
			String recentVersion = br.readLine();
			if(!recentVersion.equals(CalculatorVersion.MAIN_VERSION)){
				String URL = br.readLine();
				ArrayList<String> explain = new ArrayList<String>();
				while(true){
					String temp = br.readLine();
					if(temp==null) break;
					else explain.add(temp);
				}
				NewVersion updateShell = new NewVersion(selectionShell, recentVersion, URL, explain);
				updateShell.create();
				updateShell.open();
				
				if(updateShell.readed) selectionShell.dispose();
			}
			
		} catch (UnknownHostException e) {
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		if(!selectionShell.isDisposed()){
			GetDictionary.readFile();
			selectionSet.setSelectionComposite();
		}
		while (!selectionShell.isDisposed()) {
		    if (!display.readAndDispatch())
		        display.sleep();
		}
		if(selectionSet.getSelected()==null) return;
		
		Characters character;
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\character_"+BriefCharacterInfo.getFileName(selectionSet.getSelected().name)+".dfd"));
			Object temp = in.readObject();
			character = (Characters)temp;
			character.updateDictionary();
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
		shell.setBackgroundImage(GetDictionary.getBackground(character.getJob(), shell));
		VillageUI villageUI = new VillageUI(shell, character);
		Canvas loading = new Canvas(shell, SWT.NO_REDRAW_RESIZE | SWT.TRANSPARENT);
		
		loading.setBounds(0, 0, 150, shell.getClientArea().height);
		loading.addPaintListener(new PaintListener() {
	        public void paintControl(PaintEvent e) {
	         e.gc.drawImage(GetDictionary.loadingImage,5,shell.getClientArea().height-55);
	        }
	    });
		stackLayout.topControl=loading;
		shell.open();
		
		DungeonUI dungeonUI = new DungeonUI(shell, character);
		SkillTree skillTree = new SkillTree(shell, character, villageUI);
		Vault vault = new Vault(shell, character);
		
		villageUI.makeComposite(skillTree, vault);
		
		stackLayout.topControl = villageUI.getComposite();
		shell.setText("인포창");
		
		villageUI.get_toDungeonButton().addListener(SWT.Selection, event -> {
			stackLayout.topControl=loading;
			shell.layout();
			villageUI.disposeContent();
			dungeonUI.makeComposite(skillTree, vault);
			stackLayout.topControl = dungeonUI.getComposite();
			shell.layout();
		});
		
		dungeonUI.get_toVillageButton().addListener(SWT.Selection, event -> {
			stackLayout.topControl=loading;
			shell.layout();
			dungeonUI.disposeContent();
			villageUI.makeComposite(skillTree, vault);
			stackLayout.topControl = villageUI.getComposite();
			skillTree.superInfo=villageUI;
			shell.layout();
		});
		
		villageUI.get_selectCharacterButton().addListener(SWT.Selection, event ->{
			terminateSignal.selectionShellterminated=false;
			shell.dispose();
			Shell newShell = new Shell(display);
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
					ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\character_"+BriefCharacterInfo.getFileName(selectionSet.getSelected().name)+".dfd"));
					Object temp = in.readObject();
					newCharacter = (Characters)temp;
					newCharacter.updateDictionary();
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
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data\\character_"+BriefCharacterInfo.getFileName(character.name)+".dfd"));
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