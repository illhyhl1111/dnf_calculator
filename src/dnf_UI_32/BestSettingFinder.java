package dnf_UI_32;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Dimension_stat;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_calculator.Calculator;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.Setting;
import dnf_class.Title;
import dnf_class.Weapon;

public class BestSettingFinder extends Dialog {
	
	private Shell shell;
	private Characters character;
	Combo settingCombo;
	BestSettingFinder thisDialog;
	GetBestSetting calculateThread;
	
	public BestSettingFinder(Shell shell, Characters character, Combo settings)
	{
		super(shell);
		this.shell=shell;
		this.character=character;
		this.settingCombo=settings;
		thisDialog = this;
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		RowLayout contentLayout = new RowLayout(SWT.VERTICAL);
		contentLayout.spacing=10;
		contentLayout.wrap=false;
		content.setLayout(contentLayout);
		
		Label guideLabel = new Label(content, SWT.WRAP);
		guideLabel.setText("\n ※인벤토리 내의 아이템과 재료를 조합하여 가장 강한 세팅 10개를 구합니다\n"
				+ "   장착하고 있는 부위의 아이템은 세팅에 고정시키며, 장착하지 않은 부위만 조합합니다\n\n"
				+ "   Tip) 가능한 장비 조합을 모두 계산해야하므로 시간이 오래 걸릴 수 있습니다\n"
				+ "        쓸 일이 없는 자명한 하위호환 장비는 창고에 넣어주시고,\n"
				+ "        에픽풀셋을 계산하는 경우 \'에픽풀셋만 사용하기\'를 쓰세요\n\n");
		guideLabel.setLayoutData(new RowData());
		
		Group resultGroup = new Group(content, SWT.NONE);
		resultGroup.setText("");
		resultGroup.setLayout(new FormLayout());
		
		Label antonLabel = new Label(resultGroup, SWT.NONE);
		antonLabel.setText(" 영혼조각 갯수 - ");
		FormData formData = new FormData();
		antonLabel.setLayoutData(formData);
		Text antonText = new Text(resultGroup, SWT.NONE);
		antonText.setText("0");
		antonText.addVerifyListener(new TextInputOnlyInteger());
		formData = new FormData(100, -1);
		formData.left = new FormAttachment(antonLabel, 5);
		antonText.setLayoutData(formData);
		
		Label lukeLabel = new Label(resultGroup, SWT.NONE);
		lukeLabel.setText("모놀리움 갯수 - ");
		formData = new FormData();
		formData.left = new FormAttachment(antonText, 25);
		lukeLabel.setLayoutData(formData);
		Text lukeText = new Text(resultGroup, SWT.NONE);
		lukeText.setText("0");
		lukeText.addVerifyListener(new TextInputOnlyInteger());
		formData = new FormData(100, -1);
		formData.left = new FormAttachment(lukeLabel, 5);
		lukeText.setLayoutData(formData);
		
		Label weaponLabel = new Label(resultGroup, SWT.NONE);
		weaponLabel.setText(" 구원의 이기 타입 - ");
		formData = new FormData(-1, 30);
		formData.top = new FormAttachment(lukeText, 25);
		weaponLabel.setLayoutData(formData);
		Combo weaponCombo = new Combo(resultGroup, SWT.READ_ONLY);
		LinkedList<String> items = new LinkedList<String>();
		for(Weapon_detailType type : Weapon_detailType.values())
			if(type.enabled(character.getJob())) items.add(type.getName());
		weaponCombo.setItems(items.toArray(new String[0]));
		weaponCombo.select(0);
		formData = new FormData();
		formData.left = new FormAttachment(weaponLabel, 5);
		formData.top = new FormAttachment(lukeText, 25);
		weaponCombo.setLayoutData(formData);
		
		Button armorSetbutton = new Button(resultGroup, SWT.CHECK);
		armorSetbutton.setText("에픽 방어구 풀셋만 사용");
		formData = new FormData();
		formData.left = new FormAttachment(weaponCombo, 25);
		formData.top = new FormAttachment(lukeText, 25);
		armorSetbutton.setLayoutData(formData);
		
		OptionSettingGroup weaponSetting = new OptionSettingGroup(true, resultGroup);
		formData = new FormData();
		formData.top = new FormAttachment(weaponCombo, 20);
		formData.left = new FormAttachment(0, 5);
		weaponSetting.mainGroup.setLayoutData(formData);
		
		OptionSettingGroup specialSetting = new OptionSettingGroup(false, resultGroup);
		formData = new FormData();
		formData.top = new FormAttachment(weaponSetting.mainGroup, 20);
		formData.left = new FormAttachment(0, 5);
		specialSetting.mainGroup.setLayoutData(formData);
		
		Button button = new Button(resultGroup, SWT.PUSH);
		button.setText("조합 갯수 구하기");
		formData = new FormData(100, 60);
		formData.right = new FormAttachment(100, -10);
		formData.top = new FormAttachment(specialSetting.mainGroup, 25);
		button.setLayoutData(formData);
		
		
		button.addListener(SWT.Selection, new Listener(){
			LinkedList<SettingMaterial> settings=null;
			Text nameText=null;
			
			@Override
			public void handleEvent(Event event) {							
				if(button.getText().equals("조합 갯수 구하기")){
					try{
						settings = makeCandidateSettings(Integer.valueOf(antonText.getText()), Integer.valueOf(lukeText.getText()),
								Weapon_detailType.stringToType(weaponCombo.getText()), armorSetbutton.getSelection(), weaponSetting, specialSetting);
					}catch(OutOfMemoryError e) {
						MessageDialog dialog = new MessageDialog(shell, "실★패", null,
							    "메모리가 주겄습니다ㅜ\n"
							    + "가능한 조합의 수를 줄여주세요",
							    MessageDialog.ERROR, new String[] { "ㅇㅋ" }, 0);
						dialog.open();
						thisDialog.close();
					}
					
					antonLabel.dispose();
					antonText.dispose();
					lukeLabel.dispose();
					lukeText.dispose();
					weaponLabel.dispose();
					weaponCombo.dispose();
					armorSetbutton.dispose();
					weaponSetting.mainGroup.dispose();
					specialSetting.mainGroup.dispose();
					
					Label numLabel = new Label(resultGroup, SWT.NONE);
					numLabel.setText(" 가능한 조합의 갯수는 총 "+settings.size()+"개 입니다.\n 실행하시겠습니까?");
					numLabel.setLayoutData(new FormData());
					
					Label nameLabel = new Label(resultGroup, SWT.NONE);
					nameLabel.setText(" 저장할 이름 입력 - ");
					FormData formData = new FormData();
					formData.top = new FormAttachment(numLabel, 25);
					nameLabel.setLayoutData(formData);
					nameText = new Text(resultGroup, SWT.NONE);
					nameText.setText("(세팅비교)");
					formData = new FormData(100, -1);
					formData.top = new FormAttachment(numLabel, 25);
					formData.left = new FormAttachment(nameLabel, 5);
					nameText.setLayoutData(formData);
					
					formData = new FormData(100, 60);
					formData.right = new FormAttachment(100, -10);
					formData.bottom = new FormAttachment(100, -10);
					button.setLayoutData(formData);
					button.setText("실행ㄱ");
					resultGroup.layout();
				}
				
				else{
					button.setText("계산중");
					button.setEnabled(false);
					nameText.setEnabled(false);
					
					ProgressBar pBar = new ProgressBar(resultGroup, SWT.SMOOTH);
					FormData formData = new FormData();
					formData.left = new FormAttachment(0, 5);
					formData.right = new FormAttachment(button, -5);
					formData.bottom = new FormAttachment(button, 0, SWT.BOTTOM);
					pBar.setLayoutData(formData);
					pBar.setSelection(0);
					pBar.setMaximum(settings.size());
					
					Label pLabel = new Label(resultGroup, SWT.NONE);
					formData = new FormData();
					formData.left = new FormAttachment(0, 5);
					formData.right = new FormAttachment(button, -5);
					formData.bottom = new FormAttachment(pBar, -5);
					pLabel.setLayoutData(formData);
					resultGroup.layout();
					
					final Display currentDisplay = Display.getCurrent();
					calculateThread = new GetBestSetting(currentDisplay, settings, nameText.getText(), pBar, pLabel);
					calculateThread.start();
				
					new Thread()
					{
						public void run() {
							while(calculateThread.isAlive())
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							
							currentDisplay.asyncExec(new Runnable() {
						        public void run() {
						        	if(calculateThread.stop){
						        		thisDialog.close();
						        		return;
						        	}
						        	
									LinkedList<String> newItems = new LinkedList<String>(Arrays.asList(settingCombo.getItems()));
									
									for(int index=0; index<character.userItemList.settingList.size(); index++){
										if(character.userItemList.settingList.get(index).setting_name.contains(nameText.getText()+" -")){
											character.userItemList.settingList.remove(index);
											newItems.remove(index--);
										}
									}
									for(Setting setting : calculateThread.getResult())
									{
										newItems.add(setting.setting_name);
										character.userItemList.settingList.add(setting);
									}
									
									settingCombo.setItems(newItems.toArray(new String[0]));
									
									MessageDialog dialog = new MessageDialog(shell, "성☆공", null,
										    "완료ㅎ",
										    MessageDialog.INFORMATION, new String[] { "ㅇㅋ" }, 0);
									dialog.open();
									thisDialog.close();
						        }
							});
						}
					}.start();
				}
			}
		});
		
		return content;
	}
	
	class OptionSettingGroup
	{
		Group mainGroup;
		private Button[] dimensionList;
		private Spinner reinforce;
		private Spinner reforge;
		boolean isWeapon;
		private ArrayList<Combo> cardList;
		
		public OptionSettingGroup(boolean isWeapon, Composite parent)
		{
			this.isWeapon=isWeapon;
			mainGroup = new Group(parent, SWT.NONE);
			if(isWeapon) mainGroup.setText("구원의 이기 설정");
			else mainGroup.setText("헤블론의 군주 설정");
			mainGroup.setLayout(new FormLayout());
			
			Group selectModeComposite = new Group (mainGroup, SWT.NO_RADIO_GROUP);
			selectModeComposite.setText("변이된 왜곡서");
			FormData selectModeData = new FormData();
			selectModeData.top = new FormAttachment(mainGroup, InterfaceSize.MARGIN);
			selectModeData.left = new FormAttachment(0, InterfaceSize.MARGIN);
			selectModeComposite.setLayoutData(selectModeData);
			selectModeComposite.setLayout (new GridLayout(5, true));
			
			Listener radioGroup = event -> {
				Control [] children = selectModeComposite.getChildren ();
				for (int j=0; j<children.length; j++) {
					Control child = children [j];
					if (child instanceof Button) {
						Button button1 = (Button) child;
						if ((button1.getStyle() & SWT.RADIO) != 0) button1.setSelection (false);
					}
				}
				Button button2 = (Button) event.widget;
				button2.setSelection (true);
			};
		
			GridData buttonData = new GridData();
			buttonData.horizontalAlignment = GridData.CENTER;
			buttonData.grabExcessHorizontalSpace = true;
			
			dimensionList = new Button[5];
			String[] dimensionNameList = {"없음", "힘", "지능", "체력", "정신력"};
			
			for(int i=0; i<dimensionList.length; i++)
			{
				dimensionList[i] = new Button (selectModeComposite, SWT.RADIO);
				dimensionList[i].setText(dimensionNameList[i]);
				dimensionList[i].addListener(SWT.Selection, radioGroup);
				dimensionList[i].setLayoutData(buttonData);
			}
			dimensionList[0].setSelection(true);
			
			
			Group reinforceComposite = new Group (mainGroup, SWT.NONE);
			reinforceComposite.setText("강화/증폭");
			FormData reinforceData = new FormData();
			reinforceData.top = new FormAttachment(0, InterfaceSize.MARGIN);
			reinforceData.left = new FormAttachment(selectModeComposite, InterfaceSize.MARGIN);
			reinforceComposite.setLayoutData(reinforceData);
			reinforceComposite.setLayout (new FillLayout());
			
			reinforce = new Spinner(reinforceComposite, SWT.READ_ONLY);
		    reinforce.setMinimum(0);
		    reinforce.setMaximum(17);
		    reinforce.setSelection(0);
		    reinforce.setIncrement(1);
		    reinforce.setPageIncrement(5);
		    
		    if(isWeapon){
			    Group reforgeComposite = new Group (mainGroup, SWT.NONE);
			    reforgeComposite.setText("재련");
				FormData reforgeData = new FormData();
				reforgeData.top = new FormAttachment(0, InterfaceSize.MARGIN);
				reforgeData.left = new FormAttachment(reinforceComposite, InterfaceSize.MARGIN);
				reforgeComposite.setLayoutData(reforgeData);
				reforgeComposite.setLayout (new FillLayout());
				
				reforge = new Spinner(reforgeComposite, SWT.READ_ONLY);
				reforge.setMinimum(0);
				reforge.setMaximum(8);
				reforge.setSelection(0);
				reforge.setIncrement(1);
			    reforge.setPageIncrement(1);
			    
			    cardList= new ArrayList<Combo>();
			    Combo weaponCombo = new Combo(mainGroup, SWT.READ_ONLY);
			    weaponCombo.setItems(new String[] {"무기 보주 설정", "메델(힘 50/물공 20)", "잡보주(힘 30)", "잡보주(물공 20)"});
			    FormData comboData = new FormData();
			    comboData.left = new FormAttachment(50, -70);
			    comboData.top = new FormAttachment(reforgeComposite, 30);
			    weaponCombo.setLayoutData(comboData);
			    weaponCombo.select(0);
			    cardList.add(weaponCombo);
		    }
		    
		    else{
		    	cardList= new ArrayList<Combo>();
		    	Combo aidCombo = new Combo(mainGroup, SWT.READ_ONLY);
		    	aidCombo.setItems(new String[] {"보조장비 보주 설정", "노블(물 34/독 42/물크 2)", "노블(마 34/독 42/마크 2)", "무제(물공 12/물크 1)", "무제(마공 12/마크 1)"});
				FormData comboData = new FormData();
				comboData.left = new FormAttachment(0, 5);
				comboData.top = new FormAttachment(reinforceComposite, 30);
				aidCombo.setLayoutData(comboData);
				aidCombo.select(0);
				cardList.add(aidCombo);	
				
				Combo stoneCombo = new Combo(mainGroup, SWT.READ_ONLY);
				stoneCombo.setItems(new String[] {"마법석 보주 설정", "심카(모속강 15)", "심카(모속강 12)", "네이트람(모속 5/물마크 1)"});
				comboData = new FormData();
				comboData.left = new FormAttachment(aidCombo, 5);
				comboData.top = new FormAttachment(reinforceComposite, 30);
				stoneCombo.setLayoutData(comboData);
				stoneCombo.select(0);
				cardList.add(stoneCombo);
				
				Combo earringCombo = new Combo(mainGroup, SWT.READ_ONLY);
				earringCombo.setItems(new String[] {"귀걸이 보주 설정", "루크(힘/지능 125)", "루크(힘/지능 75)", "루크(힘/지능 50)"});
				comboData = new FormData();
				comboData.left = new FormAttachment(stoneCombo, 5);
				comboData.top = new FormAttachment(reinforceComposite, 30);
				earringCombo.setLayoutData(comboData);
				earringCombo.select(0);
				cardList.add(earringCombo);
		    }
		}
		
		public void setWeaponOption(Weapon weapon)
		{
			try{
				weapon.setReforge(reforge.getSelection());
			}catch(UnknownInformationException e){
				weapon.setReforgeNum(reforge.getSelection());
			}
			
			String selected=null;
			Dimension_stat selectedStat=Dimension_stat.NONE;
			for(Button b : dimensionList)
				if(b.getSelection()){
					selected=b.getText();
					break;
				}
			if(selected.equals("없음")) selectedStat=Dimension_stat.NONE;
			else if(selected.equals("힘")) selectedStat=Dimension_stat.STR;
			else if(selected.equals("지능")) selectedStat=Dimension_stat.INT;
			else if(selected.equals("체력")) selectedStat=Dimension_stat.STA;
			else if(selected.equals("정신력")) selectedStat=Dimension_stat.WILL;
			try{
				weapon.setDimension(selectedStat);
			}catch(UnknownInformationException e){
				weapon.setDimensionType(selectedStat);
			}
			
			try{
				weapon.setReinforce(reinforce.getSelection());
			}catch(UnknownInformationException e){
				weapon.setReinforceNum(reinforce.getSelection());
			}
			
			Card temp = new Card("임시", Item_rarity.NONE, CalculatorVersion.DEFAULT);
			temp.addPart(Equip_part.WEAPON);
			switch(cardList.get(0).getText())
			{
			case "메델(힘 50/물공 20)":
				temp.vStat.addStatList("힘", 50);
				temp.vStat.addStatList("물공", 20);
				weapon.setCard(temp);
				break;
			case "잡보주(힘 30)":
				temp.vStat.addStatList("힘", 30);
				weapon.setCard(temp);
				break;
			case "잡보주(물공 20)":
				temp.vStat.addStatList("물공", 20);
				weapon.setCard(temp);
				break;
			default:
				break;
			}
		}
		
		public void setSpecialEquipOption(Equipment special)
		{
			String selected=null;
			Dimension_stat selectedStat=Dimension_stat.NONE;
			for(Button b : dimensionList)
				if(b.getSelection()){
					selected=b.getText();
					break;
				}
			if(selected.equals("없음")) selectedStat=Dimension_stat.NONE;
			else if(selected.equals("힘")) selectedStat=Dimension_stat.STR;
			else if(selected.equals("지능")) selectedStat=Dimension_stat.INT;
			else if(selected.equals("체력")) selectedStat=Dimension_stat.STA;
			else if(selected.equals("정신력")) selectedStat=Dimension_stat.WILL;
			try{
				special.setDimension(selectedStat);
			}catch(UnknownInformationException e){
				special.setDimensionType(selectedStat);
			}
			
			try{
				special.setReinforce(reinforce.getSelection());
			}catch(UnknownInformationException e){
				special.setReinforceNum(reinforce.getSelection());
			}
			
			Card temp = new Card("임시", Item_rarity.NONE, CalculatorVersion.DEFAULT);
			if(special.getPart()==Equip_part.AIDEQUIPMENT){
				temp.addPart(Equip_part.AIDEQUIPMENT);
				switch(cardList.get(0).getText())
				{
				case "노블(물 34/독 42/물크 2)":
					temp.vStat.addStatList("물공", 34);
					temp.vStat.addStatList("독공", 42);
					temp.vStat.addStatList("물크", 2);
					special.setCard(temp);
					break;
				case "노블(마 34/독 42/마크 2)":
					temp.vStat.addStatList("마공", 34);
					temp.vStat.addStatList("독공", 42);
					temp.vStat.addStatList("마크", 2);
					special.setCard(temp);
					break;
				case "무제(물공 12/물크 1)":
					temp.vStat.addStatList("물공", 12);
					temp.vStat.addStatList("물크", 1);
					special.setCard(temp);
					break;
				case "무제(마공 12/마크 1)":
					temp.vStat.addStatList("마공", 12);
					temp.vStat.addStatList("마크", 1);
					special.setCard(temp);
					break;
				default:
					break;
				}
			}
			
			else if(special.getPart()==Equip_part.MAGICSTONE){
				temp.addPart(Equip_part.MAGICSTONE);
				switch(cardList.get(1).getText())
				{
				case "심카(모속강 15)": 
					temp.vStat.addStatList("모속강", 15);
					special.setCard(temp);
					break;
				case "심카(모속강 12)":
					temp.vStat.addStatList("모속강", 15);
					special.setCard(temp);
					break;
				case "네이트람(모속 5/물마크 1)":
					temp.vStat.addStatList("모속강", 5);
					temp.vStat.addStatList("물크", 1);
					temp.vStat.addStatList("마크", 1);
					special.setCard(temp);
					break;
				default:
					break;
				}
			}
			
			else if(special.getPart()==Equip_part.EARRING){
				temp.addPart(Equip_part.EARRING);
				switch(cardList.get(2).getText())
				{
				case "루크(힘/지능 125)":
					temp.vStat.addStatList("힘", 125);
					temp.vStat.addStatList("지능", 125);
					special.setCard(temp);
					break;
				case "루크(힘/지능 75)":
					temp.vStat.addStatList("힘", 75);
					temp.vStat.addStatList("지능", 75);
					special.setCard(temp);
					break;
				case "루크(힘/지능 50)":
					temp.vStat.addStatList("힘", 50);
					temp.vStat.addStatList("지능", 50);
					special.setCard(temp);
					break;
				default:
					break;
				}
			}
		}
	}
	
	class SettingMaterial
	{
		int antonSoul;
		int monolium;
		Setting setting;
		SetName setName;
		public SettingMaterial(int antonSoul, int monolium, Setting setting, SetName setName)
		{
			this.antonSoul=antonSoul;
			this.monolium=monolium;
			this.setting=setting;
			this.setName=setName;
		}
		public SettingMaterial(int antonSoul, int monolium, Setting setting)
		{
			this.antonSoul=antonSoul;
			this.monolium=monolium;
			this.setting=setting;
			this.setName=null;
		}
	}


	class GetBestSetting extends Thread
	{
		private Display display;
		private LinkedList<SettingMaterial> settingList;
		private String compName;
		private ProgressBar pBar;
		private Label pLabel;
		private TreeMap<Long, Setting> settingTree;
		private Setting prevSetting;
		public boolean stop;
		
		GetBestSetting(Display display, LinkedList<SettingMaterial> settingList, String compName, ProgressBar pBar, Label pLabel)
		{
			this.display=display;
			this.settingList=settingList;
			this.compName=compName;
			this.pBar=pBar;
			this.pLabel=pLabel;
			
			settingTree = new TreeMap<Long, Setting>();
			prevSetting = (Setting) character.getItemSetting().clone();
			stop=false;
		}
		
		@Override
		public void run(){
			final long startTime = System.nanoTime();
			for (int i = 0; i < settingList.size() && !stop; i++) {
				try {
					if(i%1000==0) Thread.sleep(10);
				} catch (InterruptedException e) {}
			
				character.setItemSettings(settingList.get(i).setting, false);
				long damage = Calculator.getDamage(character.getRepresentSkill(), character.target, character);
				settingTree.put(damage, settingList.get(i).setting);
								
				if(i%100==0){
					final int j=i;
				    display.asyncExec(new Runnable() {
				    	public void run() {
				    		if (pBar.isDisposed()) return;
				    		pBar.setSelection(j);
				    		int estimatedTime = (int) ((System.nanoTime() - startTime)/1000000000L);
				    		double progressed = (double)j/settingList.size();
				    		int leftTime;
				    		if(progressed==0) leftTime=9999999;
				    		else leftTime = (int) (estimatedTime*(1-progressed)/progressed);
				    		
				    		pLabel.setText(j+"/"+settingList.size()+" 완료 ("+(int)(progressed*100)+"%)"
				    				+ " | 남은시간 : 약 " +String.format("%02d분 %02d초", leftTime/60, leftTime%60));
				    	}
				    });
				}
			}
			
			if(stop) character.setItemSettings(prevSetting, false);
		}
		
		public LinkedList<Setting> getResult(){
			LinkedList<Setting> top10Settings = new LinkedList<Setting>();
			NavigableMap<Long, Setting> nMap = settingTree.descendingMap();
			Iterator<Entry<Long, Setting>> iter = nMap.entrySet().iterator();
			for(int i=0; i<10; i++){
				if(iter.hasNext()){
					top10Settings.add(iter.next().getValue().saveToClone(compName+" -"+(i+1)+"위"));
				}
				else break;
			}
			
			character.setItemSettings(prevSetting, false);
			return top10Settings;
		}	
	}
	
	public LinkedList<SettingMaterial> makeCandidateSettings(int antonSoul, int monolium, Weapon_detailType saviorType, boolean armorSetOnly,
			OptionSettingGroup weaponSetting, OptionSettingGroup specialSetting) throws OutOfMemoryError
	{
		LinkedList<SettingMaterial> settingList = new LinkedList<SettingMaterial>();		
		settingList.add(new SettingMaterial(antonSoul, monolium, (Setting) character.getItemSetting().clone()));
		
		LinkedList<SettingMaterial> newList = new LinkedList<SettingMaterial>();
		HashMap<Equip_part, ArrayList<Item>> equipList = new HashMap<Equip_part, ArrayList<Item>>();
		
		ArrayList<Equip_part> armorList = new ArrayList<Equip_part>(); 
		armorList.add(Equip_part.ROBE); armorList.add(Equip_part.TROUSER); armorList.add(Equip_part.SHOULDER);
		armorList.add(Equip_part.BELT); armorList.add(Equip_part.SHOES);
		
		Equipment[] anton = new Equipment[13];  
		for(int i=0; i<13; i++) anton[i]=null;
		
		HashMap<Equip_type, Equipment[]> luke = new HashMap<Equip_type, Equipment[]>();
		luke.put(Equip_type.FABRIC, anton.clone());
		luke.put(Equip_type.LEATHER, anton.clone());
		luke.put(Equip_type.MAIL, anton.clone());
		luke.put(Equip_type.HEAVY, anton.clone());
		luke.put(Equip_type.PLATE, anton.clone());
		luke.put(Equip_type.NONE, anton.clone());
		
		for(Equip_part part : Equip_part.values()){
			if(part==Equip_part.TITLE){
				if(character.getItemSetting().title.getName().contains("없음"))
					equipList.put(part, new ArrayList<Item>());
			}
			else if(part==Equip_part.WEAPON){
				if(character.getItemSetting().weapon.getName().contains("없음"))
					equipList.put(part, new ArrayList<Item>());
			}
			
			else if(character.getItemSetting().equipmentList.get(part).getName().contains("없음"))
				equipList.put(part, new ArrayList<Item>());
		}
		
		if(armorSetOnly){
			SetName prevSet = null;
			for(int i=0; i<5; i++){
				Equipment equip = character.getItemSetting().equipmentList.get(armorList.get(i));
				if(!equip.getName().contains("없음")){
					if(prevSet==null) prevSet=equip.setName;
					else if(prevSet!=equip.setName) return new LinkedList<SettingMaterial>();
				}
			}		
			
			if(prevSet==SetName.NONE) return new LinkedList<SettingMaterial>();
			settingList.getFirst().setName=prevSet;
		}
		
		for(Equipment e : character.userItemList.equipList){
			if(e.enabled){
				ArrayList<Item> list = equipList.get(e.getPart());
				if(list!=null){
					if(armorList.contains(e.getPart()) && settingList.getFirst().setName!=null){
						if(e.getSetName()==settingList.getFirst().setName) list.add(e);
					}
					else if(armorSetOnly && e.getSetName()!=SetName.NONE) list.add(e); 
					else list.add(e);
				}
				else continue;
				
				//업그레이드
				try{
					switch(e.part){
					case NECKLACE: case BRACELET: case RING:
					case AIDEQUIPMENT: case MAGICSTONE:
						if(e.getName().contains("탐식의 ") && !e.getName().contains("무한한 ")){
							Equipment upgrade = character.userItemList.getEquipment("무한한 "+e.getName());
							if(!upgrade.enabled){
								Equipment superEquip = (Equipment)upgrade.clone();
								superEquip.setCard(e.getCard());
								superEquip.setReinforce(e.getReinforce());
								superEquip.setDimension(e.getDimentionStat());
								anton[e.part.order] = superEquip;
							}
						}
						break;
						
					case WEAPON:
						if(e.getName().contains("구원의 이기 - ")){
							Equipment upgrade = character.userItemList.getEquipment("창성의 구원자 - "+e.getName().substring(9));
							if(!upgrade.enabled){
								Weapon superEquip = (Weapon)upgrade.clone();
								superEquip.setCard(e.getCard());
								superEquip.setReinforce(e.getReinforce());
								superEquip.setReforge(((Weapon)e).getReforge());
								superEquip.setDimension(e.getDimentionStat());
								luke.get(Equip_type.NONE)[e.part.order] = superEquip;
							}
						}
						break;
						
					case ROBE: case TROUSER: case SHOULDER:
					case BELT: case SHOES:
						Equipment upgrade=null;
						if(e.setName.equals(SetName.OGGEILL)){
							for(Equipment equip : character.userItemList.equipList){
								if(equip.setName.equals(SetName.GESPENST) && equip.part==e.part){
									upgrade = equip;
									break;
								}
							}
						}
						else if(e.setName.equals(SetName.BLACKFORMAL)){
							for(Equipment equip : character.userItemList.equipList){
								if(equip.setName.equals(SetName.FIENDVENATOR) && equip.part==e.part){
									upgrade = equip;
									break;
								}
							}
						}
						else if(e.setName.equals(SetName.GOLDENARMOR)){
							for(Equipment equip : character.userItemList.equipList){
								if(equip.setName.equals(SetName.SUPERCONTINENT) && equip.part==e.part){
									upgrade = equip;
									break;
								}
							}
						}
						else if(e.setName.equals(SetName.ANCIENTWAR)){
							for(Equipment equip : character.userItemList.equipList){
								if(equip.setName.equals(SetName.NAGARAJA) && equip.part==e.part){
									upgrade = equip;
									break;
								}
							}
						}
						else if(e.setName.equals(SetName.CENTURYONHERO)){
							for(Equipment equip : character.userItemList.equipList){
								if(equip.setName.equals(SetName.SEVENSINS) && equip.part==e.part){
									upgrade = equip;
									break;
								}
							}
						}
						else break;
						
						if(!upgrade.enabled){
							Equipment superEquip = (Equipment)upgrade.clone();
							superEquip.setCard(e.getCard());
							superEquip.setReinforce(e.getReinforce());
							superEquip.setDimension(e.getDimentionStat());
							luke.get(e.getEquipType())[e.part.order] = superEquip;
						}
						break;
					default:
						break;
					}
				}catch (ItemNotFoundedException | UnknownInformationException e2){
					e2.printStackTrace();
				}
			}
			//생성
			else{
				if(equipList.get(e.getPart())==null) continue;
				try{
					switch(e.part){
					case WEAPON:
						if(e.getName().contains("구원의 이기 - ") && ((Weapon)e).weaponType==saviorType){
							Weapon newEquip = (Weapon)e.clone();
							weaponSetting.setWeaponOption(newEquip);
							anton[e.part.order] = newEquip;
							
							Equipment upgrade = character.userItemList.getEquipment("창성의 구원자 - "+e.getName().substring(9));
							if(!upgrade.enabled){
								Weapon superEquip = (Weapon)upgrade.clone();
								weaponSetting.setWeaponOption(newEquip);
								luke.get(Equip_type.NONE)[e.part.order] = superEquip;
							}
						}
						break;
					case AIDEQUIPMENT: case MAGICSTONE: case EARRING:
						if(e.getName().equals("루멘 바실리움") || e.getName().equals("솔리움 폰스") || e.getName().equals("테네브레 누스")){
							Equipment newEquip = (Equipment)e.clone();
							specialSetting.setSpecialEquipOption(newEquip);
							luke.get(Equip_type.NONE)[e.part.order] = newEquip;
						}
						break;
					default:
						break;
					}
				}catch(ItemNotFoundedException e1){
					e1.printStackTrace();
				}
			}
		}
		for(Title t : character.userItemList.titleList){
			if(t.enabled){
				ArrayList<Item> list = equipList.get(Equip_part.TITLE);
				if(list!=null) list.add(t);
			}
		}
			
		for(Equipment e : character.userItemList.equipList_user){
			if(e.enabled){
				ArrayList<Item> list = equipList.get(e.getPart());
				if(list!=null) list.add(e);
			}
		}
				
		Setting temp;
		boolean flag=true;
		SetName newSet=null;
		
		for(Equip_part part : equipList.keySet()){
			for(SettingMaterial s : settingList){
				for(Item equip : equipList.get(part)){
					temp=(Setting) s.setting.clone();
					if(part==Equip_part.WEAPON) temp.weapon=(Weapon)equip;
					else if(part==Equip_part.TITLE) temp.title=(Title)equip;
					else{
						if(armorSetOnly && armorList.contains(part)){
							if(s.setName==null) newSet=equip.getSetName();
							else if(s.setName!=equip.getSetName()) flag=false;
						}
						temp.equipmentList.replace(part, (Equipment)equip);
					}
					if(flag==false) flag=true;
					else if(newSet!=null){
						newList.add(new SettingMaterial(s.antonSoul, s.monolium, temp, newSet));
						newSet=null;
					}
					else newList.add(new SettingMaterial(s.antonSoul, s.monolium, temp, s.setName));
				}
				
				switch(part){
				case WEAPON:
					if(anton[part.order]!=null && s.antonSoul>=20){
						temp=(Setting) s.setting.clone();
						temp.weapon=(Weapon) anton[part.order];
						newList.add(new SettingMaterial(s.antonSoul-20, s.monolium, temp, s.setName));
						
						if(luke.get(Equip_type.NONE)[part.order]!=null && s.monolium>=128){
							temp=(Setting) s.setting.clone();
							temp.weapon= (Weapon) luke.get(Equip_type.NONE)[part.order];
							newList.add(new SettingMaterial(s.antonSoul-20, s.monolium-128, temp, s.setName));
						}
					}
					else if(anton[part.order]==null && luke.get(Equip_type.NONE)[part.order]!=null && s.monolium>=128){
						temp=(Setting) s.setting.clone();
						temp.weapon=(Weapon) luke.get(Equip_type.NONE)[part.order];
						newList.add(new SettingMaterial(s.antonSoul, s.monolium-128, temp, s.setName));
					}
					break;
					
				case ROBE: case TROUSER: case SHOULDER:
				case BELT: case SHOES:
					for(Equipment[] lukeUpgrade : luke.values()){
						if(lukeUpgrade[part.order]!=null && s.monolium>=34){
							temp=(Setting) s.setting.clone();
							temp.equipmentList.replace(part, lukeUpgrade[part.order]);
							if(armorSetOnly){
								if(s.setName==null) newSet=lukeUpgrade[part.order].getSetName();
								else if(s.setName!=lukeUpgrade[part.order].getSetName()) flag=false;
							}
							if(flag==false) flag=true;
							else if(newSet!=null){
								newList.add(new SettingMaterial(s.antonSoul, s.monolium-34, temp, newSet));
								newSet=null;
							}
							else newList.add(new SettingMaterial(s.antonSoul, s.monolium-34, temp, s.setName));
						}
					}
					break;
					
				case NECKLACE: case BRACELET: case RING:
					if(anton[part.order]!=null && s.antonSoul>=10){
						temp=(Setting) s.setting.clone();
						temp.equipmentList.replace(part, anton[part.order]);
						newList.add(new SettingMaterial(s.antonSoul-10, s.monolium, temp, s.setName));
					}
					break;
					
				case AIDEQUIPMENT: case MAGICSTONE:
					if(anton[part.order]!=null && s.antonSoul>=10){
						//if(!s.setting.weapon.getName().equals("거포 우르반"))
						//	System.out.println("헿");
						temp=(Setting) s.setting.clone();
						temp.equipmentList.replace(part, anton[part.order]);
						newList.add(new SettingMaterial(s.antonSoul-10, s.monolium, temp, s.setName));
					}
				case EARRING:
					if(luke.get(Equip_type.NONE)[part.order]!=null && s.monolium>=100){
						temp=(Setting) s.setting.clone();
						temp.equipmentList.replace(part, luke.get(Equip_type.NONE)[part.order]);
						newList.add(new SettingMaterial(s.antonSoul, s.monolium-100, temp, s.setName));
					}
					break;
				default:
					break;
				}
			}
			
			settingList = newList;
			newList = new LinkedList<SettingMaterial>();
		}	
		
		if(armorSetOnly){
			for(int i=0; i<settingList.size(); i++){
				SettingMaterial curr = settingList.get(i);
				ArrayList<SetName> setList = new ArrayList<SetName>();
				for(int j=0; j<5; j++) setList.add(curr.setting.equipmentList.get(armorList.get(j)).setName);
				
				if(! setList.stream().allMatch(e -> e.equals(curr.setName)) )
					settingList.remove(i--);
			}
		}
		return settingList;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("세팅 최적화하기");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.CANCEL_ID, "나가기", false);
	}
	
	@Override
	protected void cancelPressed(){
		if(calculateThread!=null) calculateThread.stop=true;
		super.cancelPressed();
	}
}
