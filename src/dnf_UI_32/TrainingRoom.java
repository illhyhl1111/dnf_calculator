package dnf_UI_32;

import java.util.LinkedList;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_calculator.StatusList;
import dnf_class.Characters;
import dnf_class.Monster;
import dnf_class.MonsterOption;
import dnf_class.Skill;
import dnf_infomation.GetDictionary;

public class TrainingRoom extends DnFComposite
{
	private ItemButton<Monster> monsterButton;
	private ItemButton<MonsterOption> subMonsterButton;
	DnFComposite superInfo;
	private Composite itemInfo;
	private Composite setInfo;
	Characters character;
	private LinkedList<Skill> buffList1;
	private LinkedList<Skill> buffList2;
	private LinkedList<Skill> buffList3;
	
	public TrainingRoom(Composite parent, DnFComposite superInfo, Characters character)
	{
		this.superInfo=superInfo;
		this.character=character;
		
		buffList1 = new LinkedList<Skill>();
		buffList2 = new LinkedList<Skill>();
		buffList3 = new LinkedList<Skill>();
		
		mainComposite = new Composite(parent, SWT.BORDER);
		mainComposite.setLayout(new FormLayout());
		
		monsterButton = new ItemButton<Monster>(mainComposite, character.target, InterfaceSize.MONSTER_SIZE_X, InterfaceSize.MONSTER_SIZE_Y, true);
		subMonsterButton = new ItemButton<MonsterOption>(mainComposite, character.target.getMonsterOption(), InterfaceSize.SUB_MONSTER_SIZE_X, InterfaceSize.SUB_MONSTER_SIZE_Y, true);
		
		FormData formData = new FormData();
		formData.bottom = new FormAttachment(100, -5);
		formData.right = new FormAttachment(0, 300);
		monsterButton.getButton().setLayoutData(formData);
		
		formData = new FormData();
		formData.bottom = new FormAttachment(monsterButton.getButton(), -10, SWT.BOTTOM);
		formData.right = new FormAttachment(monsterButton.getButton(), -10, SWT.RIGHT);
		subMonsterButton.getButton().setLayoutData(formData);
		subMonsterButton.getButton().moveAbove(monsterButton.getButton());
		subMonsterButton.getButton().setVisible(!subMonsterButton.getItem().getName().contains("없음"));
		
		SetListener listenerGroup = new SetListener(monsterButton, character, superInfo, itemInfo, setInfo, parent);
		monsterButton.getButton().addListener(SWT.MouseEnter, listenerGroup.makeMonsterInfoListener(superInfo.getComposite()));
		monsterButton.getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener());
		monsterButton.getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());
		
		listenerGroup = new SetListener(subMonsterButton, character, superInfo, itemInfo, setInfo, parent);
		subMonsterButton.getButton().addListener(SWT.MouseEnter, listenerGroup.makeMonsterOptionInfoListener(superInfo.getComposite()));
		subMonsterButton.getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener());
		subMonsterButton.getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());
		
		SettingComposite settingComposite = new SettingComposite(this, character);
		formData = new FormData(InterfaceSize.TRAININGROOM_SETTING_SIZE_X, InterfaceSize.TRAININGROOM_SETTING_SIZE_Y);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		settingComposite.getComposite().setLayoutData(formData);
		
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
	
	public LinkedList<Skill> getBuffList()
	{
		LinkedList<Skill> list = new LinkedList<Skill>();
		list.addAll(buffList1);
		list.addAll(buffList2);
		list.addAll(buffList3);
		
		return list;
	}

	@Override
	public void renew() {
		monsterButton.renewImage(true);
	}
}


class SettingComposite extends DnFComposite
{
	private final static String monsterDefaultSelection = "몬스터 설정";
	private final static String monsterOptionDefaultSelection = "부가조건 설정";
	private Characters character;
	
	public SettingComposite(TrainingRoom trainingRoom, Characters character)
	{
		mainComposite = new Composite(trainingRoom.getComposite(), SWT.BORDER);
		mainComposite.setLayout(new FormLayout());
		
		Group monsterSettings = new Group(mainComposite, SWT.NONE);
		monsterSettings.setText("몬스터 소환");
		monsterSettings.setLayout(new FormLayout());
		FormData groupData = new FormData();
		monsterSettings.setLayoutData(groupData);
		
		final Combo monsterCombo = new Combo(monsterSettings, SWT.READ_ONLY);
		String[] monsterList = new String[GetDictionary.charDictionary.monsterList.size()+1];
		monsterList[0]=monsterDefaultSelection;
		int index=1;
		for(Monster monster : GetDictionary.charDictionary.monsterList)
			monsterList[index++] = monster.getName();
		monsterCombo.setItems(monsterList);
		FormData formData = new FormData(150, SWT.DEFAULT);
		formData.top = new FormAttachment(0, 10);
		monsterCombo.select(0);
		monsterCombo.setLayoutData(formData);
		
		final Combo monsterOptionCombo = new Combo(monsterSettings, SWT.READ_ONLY);
		monsterOptionCombo.setEnabled(false);
		monsterOptionCombo.setItems(new String[] {monsterOptionDefaultSelection});
		formData = new FormData(150, SWT.DEFAULT);
		formData.top = new FormAttachment(monsterCombo, 0, SWT.TOP);
		formData.left = new FormAttachment(monsterCombo, 10);
		monsterOptionCombo.select(0);
		monsterOptionCombo.setLayoutData(formData);
		
		final Button setMonsterButton = new Button(monsterSettings, SWT.PUSH);
		setMonsterButton.setText("소환");
		formData = new FormData(70, 30);
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(monsterOptionCombo, 10);
		setMonsterButton.setLayoutData(formData);
		
		monsterCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
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
	    });
		
		setMonsterButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(monsterCombo.getText().equals(monsterDefaultSelection)) return;
				
				try {
					Monster selectedMonster = GetDictionary.charDictionary.getMonsterInfo(monsterCombo.getText());
					selectedMonster.setSubMonster(monsterOptionCombo.getText());
					
					trainingRoom.setMonster(selectedMonster);
					trainingRoom.superInfo.renew();
				} catch (ItemNotFoundedException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void renew() {
		// TODO Auto-generated method stub
		
	}
}