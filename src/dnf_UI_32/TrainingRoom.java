package dnf_UI_32;

import java.util.LinkedList;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Job;
import dnf_calculator.StatusList;
import dnf_class.Buff;
import dnf_class.Characters;
import dnf_class.Monster;
import dnf_class.MonsterOption;
import dnf_class.PartyCharacter;
import dnf_class.Saint;
import dnf_infomation.GetDictionary;
import dnf_infomation.HolyInfo;

public class TrainingRoom extends DnFComposite
{
	private ItemButton<Monster> monsterButton;
	private ItemButton<MonsterOption> subMonsterButton;
	DnFComposite superInfo;
	Characters character;
	Label[] partyCharacter;
	Character_type[] partyCharType;
	private LinkedList<Buff>[] buffList;
	
	@SuppressWarnings("unchecked")
	public TrainingRoom(Composite parent, DnFComposite superInfo, Characters character)
	{
		this.superInfo=superInfo;
		this.character=character;
		
		mainComposite = new Composite(parent, SWT.BORDER);
		mainComposite.setLayout(new FormLayout());
		
		buffList = (LinkedList<Buff>[]) new LinkedList<?>[3];
		partyCharacter = new Label[3];
		partyCharType = new Character_type[3];
		for(int i=0; i<buffList.length; i++){
			buffList[i] = new LinkedList<Buff>();
			partyCharacter[i] = new Label(mainComposite, SWT.NONE);
		}
		
		monsterButton = new ItemButton<Monster>(mainComposite, character.target, InterfaceSize.MONSTER_SIZE_X, InterfaceSize.MONSTER_SIZE_Y, true);
		subMonsterButton = new ItemButton<MonsterOption>(mainComposite, character.target.getMonsterOption(), InterfaceSize.SUB_MONSTER_SIZE_X, InterfaceSize.SUB_MONSTER_SIZE_Y, true);
		
		FormData formData = new FormData(InterfaceSize.MONSTER_SIZE_X, InterfaceSize.MONSTER_SIZE_Y);
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(0, 300);
		monsterButton.getButton().setLayoutData(formData);
		
		formData = new FormData(InterfaceSize.SUB_MONSTER_SIZE_X, InterfaceSize.SUB_MONSTER_SIZE_Y);
		formData.bottom = new FormAttachment(monsterButton.getButton(), -10, SWT.BOTTOM);
		formData.right = new FormAttachment(monsterButton.getButton(), -10, SWT.RIGHT);
		subMonsterButton.getButton().setLayoutData(formData);
		subMonsterButton.getButton().moveAbove(monsterButton.getButton());
		subMonsterButton.getButton().setVisible(!subMonsterButton.getItem().getName().contains("없음"));
		
		SetListener listenerGroup = new SetListener(monsterButton, character, superInfo, parent);
		monsterButton.getButton().addListener(SWT.MouseEnter, listenerGroup.makeMonsterInfoListener(superInfo.getComposite()));
		monsterButton.getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener());
		monsterButton.getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());
		
		listenerGroup = new SetListener(subMonsterButton, character, superInfo, parent);
		subMonsterButton.getButton().addListener(SWT.MouseEnter, listenerGroup.makeMonsterOptionInfoListener(superInfo.getComposite()));
		subMonsterButton.getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener());
		subMonsterButton.getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());
		
		SettingComposite settingComposite = new SettingComposite(this, character, buffList);
		formData = new FormData(InterfaceSize.TRAININGROOM_SETTING_SIZE_X, InterfaceSize.TRAININGROOM_SETTING_SIZE_Y);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		settingComposite.getComposite().setLayoutData(formData);
		
		for(int i=0; i<partyCharacter.length; i++){
			formData = new FormData(InterfaceSize.PARTY_SIZE_X, InterfaceSize.PARTY_SIZE_Y);
			formData.left = new FormAttachment(monsterButton.getButton(), 10+(10+InterfaceSize.PARTY_SIZE_X)*i);
			formData.top = new FormAttachment(settingComposite.getComposite(), 30);
			partyCharacter[i].setLayoutData(formData);
		}
		
		renew();
	}
	
	public Monster getMonster() {return monsterButton.getItem();}
	public void setMonster(Monster monster) 
	{
		character.target=monster;
		this.monsterButton.setItem(monster);
		this.subMonsterButton.setItem(monster.getMonsterOption());
		subMonsterButton.getButton().setVisible(!monster.getMonsterOption().getName().contains("없음"));
	}
	
	public LinkedList<Buff> getBuffList()
	{
		LinkedList<Buff> list = new LinkedList<Buff>();
		list.addAll(buffList[0]);
		list.addAll(buffList[1]);
		list.addAll(buffList[2]);
		
		return list;
	}

	@Override
	public void renew() {
		monsterButton.renewImage(true);
		subMonsterButton.renewImage(true);
		for(int i=0; i<3; i++)
			partyCharacter[i].setImage(GetDictionary.iconDictionary.get(partyCharType[i].name()+" - filp"));
	}
}


class SettingComposite extends DnFComposite
{
	private final static String monsterDefaultSelection = "몬스터 설정";
	private final static String monsterOptionDefaultSelection = "부가조건 설정";
	private final static String partyDefaultSelection = "파티원 설정";
	private final static String partyOptionDefaultSelection = "부가조건 설정";
	TrainingRoom trainingRoom;
	private Characters character;
	private LinkedList<Buff>[] buffList;
	final Combo monsterCombo;
	final Combo monsterOptionCombo;
	final Combo[] partyCombo;
	final Combo[] partyOptionCombo1;
	final Combo[] partyOptionCombo2;
	
	
	public SettingComposite(TrainingRoom trainingRoom, Characters character, LinkedList<Buff>[] buffList)
	{
		this.trainingRoom=trainingRoom;
		this.character=character;
		this.buffList=buffList;
		mainComposite = new Composite(trainingRoom.getComposite(), SWT.BORDER);
		mainComposite.setLayout(new FormLayout());
		
		Group monsterSettings = new Group(mainComposite, SWT.NONE);
		monsterSettings.setText("몬스터 소환");
		monsterSettings.setLayout(new FormLayout());
		FormData groupData = new FormData();
		monsterSettings.setLayoutData(groupData);
		
		monsterCombo = new Combo(monsterSettings, SWT.READ_ONLY);
		String[] monsterList = new String[GetDictionary.charDictionary.monsterList.size()+1];
		monsterList[0]=monsterDefaultSelection;
		int index=1;
		for(Monster monster : GetDictionary.charDictionary.monsterList)
			monsterList[index++] = monster.getName();
		monsterCombo.setItems(monsterList);
		FormData formData = new FormData(150, SWT.DEFAULT);
		formData.top = new FormAttachment(0, 10);
		monsterCombo.setLayoutData(formData);
		
		monsterOptionCombo = new Combo(monsterSettings, SWT.READ_ONLY);
		formData = new FormData(150, SWT.DEFAULT);
		formData.top = new FormAttachment(monsterCombo, 0, SWT.TOP);
		formData.left = new FormAttachment(monsterCombo, 10);
		monsterOptionCombo.setLayoutData(formData);
		
		monsterCombo.setText(character.trainingRoomSeletion[0]);
		monsterComboSelected();
		monsterOptionCombo.setText(character.trainingRoomSeletion[1]);
		monsterSelectButtonPushed(false);
		
		final Button setMonsterButton = new Button(monsterSettings, SWT.PUSH);
		setMonsterButton.setText("소환");
		formData = new FormData(70, 30);
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(monsterOptionCombo, 10);
		setMonsterButton.setLayoutData(formData);
		
		for(int i=0; i<3; i++)
			trainingRoom.partyCharType[i] = Character_type.NONE;
		
		monsterCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				monsterComboSelected();
			}
	    });
		
		setMonsterButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				monsterSelectButtonPushed(true);
			}
		});
		
		
		Group partySettings = new Group(mainComposite, SWT.NONE);
		partySettings.setText("파티원 설정");
		partySettings.setLayout(new FormLayout());
		groupData = new FormData();
		groupData.top = new FormAttachment(monsterSettings, 10);
		partySettings.setLayoutData(groupData);
		
		
		partyCombo = new Combo[3];
		partyOptionCombo1 = new Combo[3];
		partyOptionCombo2 = new Combo[3];
		final Button[] setPartyButton = new Button[3];
		String[][] partyCharacterList = new String[3][];
		
		for(int i=0; i<3; i++){
			partyCombo[i] = new Combo(partySettings, SWT.READ_ONLY);
			partyCharacterList[i] = new String[1+character.userItemList.getPartyList(character.getJob()).size()];
			partyCharacterList[i][0] =partyDefaultSelection;
			index=1;
			for(PartyCharacter party : character.userItemList.getPartyList(character.getJob()))
				partyCharacterList[i][index++]=party.getName();
			partyCombo[i].setItems(partyCharacterList[i]);
			formData = new FormData(100, SWT.DEFAULT);
			if(i==0) formData.top = new FormAttachment(0, 10);
			else formData.top = new FormAttachment(partyCombo[i-1], 15);
			
			partyCombo[i].setLayoutData(formData);
			
			partyOptionCombo1[i] = new Combo(partySettings, SWT.READ_ONLY);
			formData = new FormData(110, SWT.DEFAULT);
			formData.top = new FormAttachment(partyCombo[i], 0, SWT.TOP);
			formData.left = new FormAttachment(partyCombo[i], 10);
			partyOptionCombo1[i].setLayoutData(formData);
			
			partyOptionCombo2[i] = new Combo(partySettings, SWT.READ_ONLY);
			formData = new FormData(110, SWT.DEFAULT);
			formData.top = new FormAttachment(partyCombo[i], 0, SWT.TOP);
			formData.left = new FormAttachment(partyOptionCombo1[i], 10);
			partyOptionCombo2[i].setLayoutData(formData);
			
			setPartyButton[i] = new Button(partySettings, SWT.PUSH);
			setPartyButton[i].setText("소환");
			formData = new FormData(50, 30);
			formData.top = new FormAttachment(partyCombo[i], -5, SWT.TOP);
			formData.left = new FormAttachment(partyOptionCombo2[i], 10);
			setPartyButton[i].setLayoutData(formData);
			
			final int partyIndex = i;
			partyCombo[partyIndex].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					partyComboSelected(partyIndex);
				}
		    });
			
			setPartyButton[partyIndex].addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					character.trainingRoomSeletion[2+partyIndex*3] = partyCombo[partyIndex].getText();
					character.trainingRoomSeletion[2+partyIndex*3+1] = partyOptionCombo1[partyIndex].getText();
					character.trainingRoomSeletion[2+partyIndex*3+2] = partyOptionCombo2[partyIndex].getText();
					
					partySelectButtonPushed(partyIndex, true);
				}
			});
			
		}
		for(int i=0; i<3; i++){
			partyCombo[i].setText(character.trainingRoomSeletion[2+i*3]);
			partyComboSelected(i);
			partyOptionCombo1[i].setText(character.trainingRoomSeletion[2+i*3+1]);
			partyOptionCombo2[i].setText(character.trainingRoomSeletion[2+i*3+2]);
			partySelectButtonPushed(i, false);
		}
	}
	
	private void monsterSelectButtonPushed(boolean renew){
		if(monsterCombo.getText().equals(monsterDefaultSelection)) return;
		
		try {
			Monster selectedMonster = GetDictionary.charDictionary.getMonsterInfo(monsterCombo.getText());
			selectedMonster.setSubMonster(monsterOptionCombo.getText());
			
			trainingRoom.setMonster(selectedMonster);
			character.trainingRoomSeletion[0]=monsterCombo.getText();
			character.trainingRoomSeletion[1]=monsterOptionCombo.getText();
			if(renew) trainingRoom.superInfo.renew();
		} catch (ItemNotFoundedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void monsterComboSelected(){
		if(monsterCombo.getText().equals(monsterDefaultSelection)){
			monsterOptionCombo.setEnabled(false);
			monsterOptionCombo.setItems(new String[] {monsterOptionDefaultSelection});
			monsterOptionCombo.select(0);
			return;
		}
		
		try {
			Monster monster = GetDictionary.charDictionary.getMonsterInfo(monsterCombo.getText());
			String[] optionList = new String[monster.monsterFeature.size()+1];
			optionList[0] = monsterOptionDefaultSelection;
		
			if(monster.monsterFeature.size()==0) monsterOptionCombo.setEnabled(false);
			else{
				int index=1;
				for(Entry<MonsterOption, StatusList> entry : monster.monsterFeature.entrySet())
					optionList[index++] = entry.getKey().getName();
				monsterOptionCombo.setEnabled(true);
			}
			monsterOptionCombo.setItems(optionList);
			monsterOptionCombo.select(0);
		} catch (ItemNotFoundedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void partySelectButtonPushed(int partyIndex, boolean renew)
	{
		if(partyCombo[partyIndex].getText().equals(partyDefaultSelection)){
			buffList[partyIndex] = new LinkedList<Buff>();
			trainingRoom.partyCharType[partyIndex] = Character_type.NONE;
			if(renew) trainingRoom.superInfo.renew();
			return;
		}
		
		try {
			PartyCharacter partyCharacter = character.userItemList.getPartyCharacter(partyCombo[partyIndex].getText());
			
			if(partyCharacter.job == Job.CRUSADER && partyOptionCombo1[partyIndex].getText().equals(Saint.settingFeatureName) && renew){
				SaintMaker dialog = new SaintMaker((Saint) partyCharacter);
				int result = dialog.open();
				if(result == Window.OK){
					buffList[partyIndex] =
							dialog.getSaint().getBuffList(partyOptionCombo1[partyIndex].getText(), partyOptionCombo2[partyIndex].getText());
					trainingRoom.partyCharType[partyIndex] = partyCharacter.job.charType;
					if(renew) trainingRoom.superInfo.renew();
				}
			}
			
			else{
				buffList[partyIndex] =
						partyCharacter.getBuffList(partyOptionCombo1[partyIndex].getText(), partyOptionCombo2[partyIndex].getText());
				trainingRoom.partyCharType[partyIndex] = partyCharacter.job.charType;
				if(renew) trainingRoom.superInfo.renew();
			}
					
		} catch (ItemNotFoundedException e1) {
			e1.printStackTrace();
		}
	}
	
	private void partyComboSelected(int partyIndex)
	{
		if(partyCombo[partyIndex].getText().equals(partyDefaultSelection)){
			partyOptionCombo1[partyIndex].setEnabled(false);
			partyOptionCombo1[partyIndex].setItems(new String[] {partyOptionDefaultSelection});
			partyOptionCombo1[partyIndex].select(0);
			partyOptionCombo2[partyIndex].setEnabled(false);
			partyOptionCombo2[partyIndex].setItems(new String[] {partyOptionDefaultSelection});
			partyOptionCombo2[partyIndex].select(0);
			return;
		}
		
		try {
			PartyCharacter party = character.userItemList.getPartyCharacter(partyCombo[partyIndex].getText());
			String[] optionList = party.getBuffFeatureList(0);
			if(optionList[0].equals("--")) partyOptionCombo1[partyIndex].setEnabled(false);
			else partyOptionCombo1[partyIndex].setEnabled(true);
			partyOptionCombo1[partyIndex].setItems(optionList);
			partyOptionCombo1[partyIndex].select(0);
			
			party = character.userItemList.getPartyCharacter(partyCombo[partyIndex].getText());
			optionList = party.getBuffFeatureList(1);
			if(optionList[0].equals("--")) partyOptionCombo2[partyIndex].setEnabled(false);
			else partyOptionCombo2[partyIndex].setEnabled(true);
			partyOptionCombo2[partyIndex].setItems(optionList);
			partyOptionCombo2[partyIndex].select(0);

		} catch (ItemNotFoundedException e1) {
			e1.printStackTrace();
		}
	}
	
	class SaintMaker extends Dialog
	{
		private Saint saint;
		private Label infoLabel;
		private Label[] name;
		private Text[] text;
		private Button[] button;
		private Combo[] combo;
		
		public SaintMaker(Saint saint){
			super(trainingRoom.getComposite().getShell());
			
			this.saint=saint;
			name = new Label[Saint.statNum];
			text = new Text[2];
			button = new Button[Saint.boolStatNum];
			combo = new Combo[Saint.intStatNum-2];
		}
		
		protected Control createDialogArea(Composite parent)
		{
			Composite content = (Composite) super.createDialogArea(parent);
			GridLayout contentLayout = new GridLayout(4, false);
			content.setLayout(contentLayout);
			
			infoLabel = new Label(content, SWT.NONE);
			infoLabel.setText("- 파티에 넣을 홀리를 설정하세요\n\n");
			infoLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
			
			int[] startNum = {
					0, HolyInfo.firstLevel_striking, HolyInfo.firstLevel_wisebless, HolyInfo.firstLevel_dawnbless,
					HolyInfo.firstLevel_glorybless, HolyInfo.firstLevel_aporkalypse, 1, 0, HolyInfo.firstLevel_beliefAura
			};
			int[] endNum = {
					9, HolyInfo.lastLevel_striking, HolyInfo.lastLevel_wisebless, HolyInfo.lastLevel_dawnbless,
					HolyInfo.lastLevel_glorybless, HolyInfo.lastLevel_aporkalypse, 7, 60, HolyInfo.lastLevel_beliefAura
			};
			
			for(int i=0; i<Saint.intStatNum; i++){
				name[i] = new Label(content, SWT.NONE);
				name[i].setText(Saint.intStatStr[i]);
				name[i].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
				
				if(i<2){
					text[i] = new Text(content, SWT.NONE);
					text[i].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
					text[i].addVerifyListener(new TextInputOnlyInteger());
				}
				else{
					combo[i-2] = new Combo(content, SWT.READ_ONLY);
					combo[i-2].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
					String[] items = new String[endNum[i-2]-startNum[i-2]+1];
					for(int index=0; index<items.length; index++)
						items[index] = String.valueOf(startNum[i-2]+index); 
					combo[i-2].setItems(items);
					combo[i-2].select(0);
				}
			}
			
			for(int i=0; i<Saint.boolStatNum; i++){
				name[i+Saint.intStatNum] = new Label(content, SWT.NONE);
				name[i+Saint.intStatNum].setText(Saint.boolStatStr[i]);
				name[i+Saint.intStatNum].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
				
				button[i] = new Button(content, SWT.CHECK);
				button[i].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
				button[i].setText("사용");
			}
			
			return content;
		}
		
		@Override
	    protected void okPressed()
		{
			saint.stat_sta = Integer.valueOf(text[0].getText());
			saint.stat_will = Integer.valueOf(text[1].getText());
			
			saint.bowNum = Integer.valueOf(combo[0].getText());
			saint.level_striking = Integer.valueOf(combo[1].getText());
			saint.level_wisebless = Integer.valueOf(combo[2].getText());
			saint.level_dawnbless = Integer.valueOf(combo[3].getText());
			saint.level_glorybless = Integer.valueOf(combo[4].getText());
			saint.level_aporkalypse = Integer.valueOf(combo[5].getText());
			saint.level_divine = Integer.valueOf(combo[6].getText());
			saint.divineStack = Integer.valueOf(combo[7].getText());
			saint.level_belief = Integer.valueOf(combo[8].getText());
			
			saint.hasSilence = button[0].getSelection();
			saint.hasBoilingBlood = button[1].getSelection();
			saint.hasSaviour = button[2].getSelection();
			saint.setDivine = button[3].getSelection();
			
			saint.setUserHolyBuff();
			
			super.okPressed();
		}
		
		public PartyCharacter getSaint() {return saint;}
		
		@Override
		protected void configureShell(Shell newShell) {
		    super.configureShell(newShell);
		    newShell.setText("홀리쟝 설정");
		}
		
	}
	
	@Override
	public void renew() {
		// TODO Auto-generated method stub
		
	}
}