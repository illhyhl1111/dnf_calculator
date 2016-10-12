package dnf_UI_32;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dnf_InterfacesAndExceptions.Job;
import dnf_infomation.BriefCharacterInfo;
import dnf_infomation.GetDictionary;

public class SelectCharacter extends DnFComposite{
	
	public LinkedList<BriefCharacterInfo> characterList;
	private BriefCharacterInfo selectedName;
	private Shell shell;
	
	public SelectCharacter(Shell shell)
	{
		this.shell=shell;
		selectedName=null;
	}
	
	@SuppressWarnings("unchecked")
	public void setSelectionComposite()
	{
		/*Shell shell = new Shell(Display.getCurrent(), SWT.NONE);
		shell.setText("캐릭터 선택");
		shell.setLayout(new FormLayout());*/
		
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("data\\characterList.dfd"));
			Object temp = in.readObject();
			characterList = (LinkedList<BriefCharacterInfo>)temp;
			in.close();
		}catch(FileNotFoundException e)
		{
			characterList = new LinkedList<BriefCharacterInfo>();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		renew();
	}
	
	public void saveCharacterList()
	{
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data\\characterList.dfd"));
			out.writeObject(characterList);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BriefCharacterInfo getSelected() {return selectedName;}

	@Override
	public void renew() {
		for(Control control : shell.getChildren()) control.dispose();
		
		ScrolledComposite scrollComposite = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);
		scrollComposite.setExpandVertical(true);
		scrollComposite.setExpandHorizontal(true);
		scrollComposite.setAlwaysShowScrollBars(true);
		scrollComposite.setLayoutData(new FormData(750, 400));
		scrollComposite.addListener(SWT.Activate, new Listener() {
			public void handleEvent(Event e) {
				scrollComposite.setFocus();
			}
		});
		
		Composite charSelectionComposite = new Composite(scrollComposite, SWT.BORDER);
		charSelectionComposite.setLayout(new GridLayout(6, true));
		
		for(BriefCharacterInfo info : characterList){
			Composite character = new Composite(charSelectionComposite, SWT.NONE);
			RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
			rowLayout.fill=true;
			rowLayout.center=true;
			rowLayout.justify=true;
			character.setLayout(rowLayout);
			character.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			
			Button charButton = new Button(character, SWT.PUSH);
			charButton.setImage(GetDictionary.iconDictionary.get(info.job.charType.name()));
			charButton.setLayoutData(new RowData(110, SWT.DEFAULT));
			
			Label charLabel = new Label(character, SWT.CENTER);
			charLabel.setText("Lv."+info.level+" "+info.name+"\n"+info.job.getName());
			charLabel.setLayoutData(new RowData());
			
			charButton.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectedName=info;
					shell.dispose();
				}
			});
		}
		
		Button makeCharacterButton = new Button(shell, SWT.PUSH);
		makeCharacterButton.setText("캐릭터 생성");
		FormData buttonData = new FormData(100, 60);
		buttonData.top = new FormAttachment(scrollComposite, 10);
		buttonData.left = new FormAttachment(0, 5);
		makeCharacterButton.setLayoutData(buttonData);
		
		Combo deleteCharacterCombo = new Combo(shell, SWT.READ_ONLY);
		Text deleteCharacterText = new Text(shell, SWT.NONE);
		Button deleteCharacterButton = new Button(shell, SWT.PUSH);
		
		makeCharacterButton.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				SaveCharacterDialog dialog = new SaveCharacterDialog(shell, characterList);
				dialog.create();
				
				if (dialog.open() == Window.OK) {
					characterList.add(dialog.getInfo());
					saveCharacterList();
					setDeleteComboItems(deleteCharacterCombo);
					renew();
				}
			}
		});
		
		setDeleteComboItems(deleteCharacterCombo);
		deleteCharacterCombo.select(0);
		FormData delComboData = new FormData(100, 60);
		delComboData.top = new FormAttachment(scrollComposite, 20);
		delComboData.right = new FormAttachment(deleteCharacterText, -5);
		deleteCharacterCombo.setLayoutData(delComboData);
		
		
		deleteCharacterText.setText("삭제하려면 지금삭제를 입력하세오");
		FormData delTextData = new FormData(SWT.DEFAULT, 20);
		delTextData.top = new FormAttachment(scrollComposite, 20);
		delTextData.right = new FormAttachment(deleteCharacterButton, -5);
		deleteCharacterText.setLayoutData(delTextData);
		deleteCharacterText.addListener(SWT.MouseUp, new Listener() {
	        @Override
	        public void handleEvent(Event event) {
	        	if(deleteCharacterText.getText().equals("삭제하려면 지금삭제를 입력하세오"))
	        		deleteCharacterText.setText("");
	        }
		});
		
		deleteCharacterButton.setText("삭제");
		FormData delButtonData = new FormData(100, 60);
		delButtonData.top = new FormAttachment(scrollComposite, 10);
		delButtonData.right = new FormAttachment(scrollComposite, -5, SWT.RIGHT);
		deleteCharacterButton.setLayoutData(delButtonData);
		
		deleteCharacterButton.addListener(SWT.Selection, new Listener(){
			@Override
			public void handleEvent(Event event) {
				String confirm = deleteCharacterText.getText();
				deleteCharacterText.setText("");
				if(!confirm.equals("지금삭제")) return;
				String deleteChar = deleteCharacterCombo.getText();
				for(int i=0; i<characterList.size(); i++){
					if(characterList.get(i).name.equals(deleteChar)){
						characterList.remove(i);
						break;
					}
				}
				saveCharacterList();
				setDeleteComboItems(deleteCharacterCombo);
				
				File charFile = new File("data\\character_"+deleteChar+".dfd");
				if(charFile.exists()) charFile.delete();
				renew();
			}
		});
		
		scrollComposite.setContent(charSelectionComposite);
		scrollComposite.setMinSize(charSelectionComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		shell.layout();
	}
	
	private void setDeleteComboItems(Combo deleteCharacterCombo){
		String[] lists = new String[characterList.size()];
		int i=0;
		for(BriefCharacterInfo info : characterList)
			lists[i++] = info.name;
		deleteCharacterCombo.setItems(lists);
	}
}

class SaveCharacterDialog extends TitleAreaDialog {

    private Text text;
    private String name;
    private Combo jobCombo;
    private Job job;
    private Combo levelCombo;
    private int level;
    private Label warning;
    private Label contributor_name;
    private LinkedList<BriefCharacterInfo> characterList;

    public SaveCharacterDialog(Shell parentShell, LinkedList<BriefCharacterInfo> characterList) {
    	super(parentShell);
    	this.characterList=characterList;
    }

    @Override
    public void create() {
            super.create();
            setTitle("캐릭터 생성");
            setMessage("새로운 캐릭터를 생성합니다", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
            Composite area = (Composite) super.createDialogArea(parent);
            Composite container = new Composite(area, SWT.NONE);
            container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            GridLayout layout = new GridLayout(2, false);
            container.setLayout(layout);

            createInput(container);
            
            contributor_name = new Label(container, SWT.RIGHT);
            contributor_name.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
            contributor_name.setText("제작자 - 카시야스) 명속은거들뿐");
            warning = new Label(container, SWT.NONE);
            warning.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

            return area;
    }

    private void createInput(Composite container) {
            Label lbtName = new Label(container, SWT.NONE);
            lbtName.setText("세팅 이름(최대 6글자)");

            GridData dataInput = new GridData();
            dataInput.grabExcessHorizontalSpace = true;
            dataInput.horizontalAlignment = GridData.FILL;
            text = new Text(container, SWT.BORDER);
            text.setLayoutData(dataInput);
            
            Label lbtJob = new Label(container, SWT.NONE);
            lbtJob.setText("직업");
            jobCombo = new Combo(container, SWT.READ_ONLY);
            jobCombo.setLayoutData(dataInput);
            jobCombo.setItems(Job.getImplementedList());
            jobCombo.select(0);
            
            jobCombo.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					contributor_name.setText(Job.getJob(jobCombo.getText()).getContributor());
				}
            });
            
            Label lbtLevel = new Label(container, SWT.NONE);
            lbtLevel.setText("캐릭터 레벨");
            levelCombo = new Combo(container, SWT.READ_ONLY);
            levelCombo.setLayoutData(dataInput);
            levelCombo.setItems(new String[] {"90"});
            levelCombo.select(0);
    }
    
    @Override
    protected boolean isResizable() {
            return true;
    }

    // save content of the Text fields because they get disposed
    // as soon as the Dialog closes
    private boolean saveInput() {
    	name = text.getText();
    	if(name.length()>6) return false;
    	
    	for(BriefCharacterInfo info : characterList)
    		if(info.name.equals(name)) return false;
    	
    	return true;
    }

    @Override
    protected void okPressed() {
    	if(saveInput()){
    		job = Job.getJob(jobCombo.getText());
    		level = Integer.valueOf(levelCombo.getText());
    		super.okPressed();
    	}
    	else{
    		warning.setText("캐릭터의 이름은 최대 6글자입니다. 중복된 이름으로 저장할 수 없습니다");
    	}
    }

    public BriefCharacterInfo getInfo() {
            return new BriefCharacterInfo(name, job, level);
    }

}