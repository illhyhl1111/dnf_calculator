package dnf_UI_32;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_calculator.Calculator;
import dnf_class.Characters;
import dnf_class.Monster;
import dnf_class.Setting;
import dnf_class.Skill;

public class DealChart extends DnFComposite {
	Characters character;
	LinkedList<DealInfo> skillList;
	private Monster monster;
	private Setting compareSetting;
	private Composite explain;
	private Composite settings;
	private Label dealLabel;
	public final static int LISTSIZE=14;
	
	public Composite settingEvaluate;
	private Label evaluateLabel;
	private long representDamage;
	private long compareDamage;
	private int mode=1;
	private int pageNumber=1;
	private int totalPageNumber=1; 
	
	public DealChart(Composite parent, Characters character)
	{
		this.character=character;
		mainComposite = new Composite(parent, SWT.BORDER);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.fill=true;
		mainComposite.setLayout(layout);
		mainComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		mainComposite.setBackgroundMode(SWT.INHERIT_FORCE);
		
		skillList = new LinkedList<DealInfo>();
		monster = character.target;
		compareSetting = null;
		
		settingEvaluate = new Composite(parent, SWT.BORDER);
		evaluateLabel = new Label(settingEvaluate, SWT.CENTER);
		evaluateLabel.setBounds(0, 0, 120, 18);
		evaluateLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setDealChart()
	{
		for(Control composite : mainComposite.getChildren())
			composite.dispose();
		
		settings = new Composite(mainComposite, SWT.NONE);
		settings.setLayout(new FormLayout());
		Combo modeSelectionCombo = new Combo(settings, SWT.READ_ONLY);
		modeSelectionCombo.setItems(new String[] { "일반", "추뎀 노크리", "추뎀 크리" });
		modeSelectionCombo.setText("일반");
		mode=1;
		pageNumber=1;
		FormData formData = new FormData();
		formData.left = new FormAttachment(50, -50);
		formData.right = new FormAttachment(50, 50);
		modeSelectionCombo.setLayoutData(formData);
		
		final Button leftButton = new Button(settings, SWT.ARROW | SWT.LEFT);
		formData = new FormData();
		formData.left = new FormAttachment(0, 10);
		leftButton.setLayoutData(formData);
		final Button rightButton = new Button(settings, SWT.ARROW | SWT.RIGHT);
		formData = new FormData();
		formData.right = new FormAttachment(100, -10);
		rightButton.setLayoutData(formData);
		
		modeSelectionCombo.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent event) {
				switch(modeSelectionCombo.getText()){
				case "일반": mode=1; break;
				case "추뎀 노크리": mode=2; break;
				case "추뎀 크리": mode=3; break;
				}
				renew();
			}
		});
		
		leftButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent event) {
				pageNumber--;
				if(pageNumber<=0) pageNumber=1;
				else renew();
			}
		});
		
		rightButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent event) {
				pageNumber++;
				if(pageNumber>totalPageNumber) pageNumber=totalPageNumber;
				else renew();
			}
		});
		
		explain = new Composite(mainComposite, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.marginBottom=0;
		layout.marginTop=0;
		layout.wrap=false;
		layout.justify=true;
		explain.setLayout(layout);
		
		Label icon = new Label(explain, SWT.BORDER);
		icon.setText("스킬");
		icon.setAlignment(SWT.CENTER);
		icon.setLayoutData(new RowData(InterfaceSize.SKILL_BUTTON_SIZE, 15));
		
		dealLabel = new Label(explain, SWT.BORDER);
		String compareName = "( 비교 대상 없음 )";
		if(compareSetting!=null) compareName="( vs "+compareSetting.setting_name+" )";
		dealLabel.setText("데미지 "+compareName);
		dealLabel.setAlignment(SWT.CENTER);
		dealLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_DEALSIZE_X, 15));
		
		Label hpLabel = new Label(explain, SWT.BORDER);
		hpLabel.setText("깎은 체력량(%)");
		hpLabel.setAlignment(SWT.CENTER);
		hpLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_HPSIZE_X, 15));
		
		skillList = new LinkedList<DealInfo>();
		
		Setting tempSetting = (Setting) character.getItemSetting().clone();
		character.setItemSettings(Setting.getMagicalSealedSetting(character.getJob()), false);
		compareDamage = Calculator.getDamage(character.getRepresentSkill(), monster, character, mode);
		character.setItemSettings(tempSetting, false);
		
		if(compareSetting!=null)
		{
			character.setItemSettings(compareSetting, true);
			
			for(Skill skill : character.getDamageSkillList())
				skillList.add(new DealInfo(mainComposite, skill, character, monster,
						Calculator.getDamage(skill, monster, character, mode), mode));
		}
		else
		{			
			for(Skill skill : character.getDamageSkillList())
				skillList.add(new DealInfo(mainComposite, skill, character, monster, mode));
		}		
		
		representDamage=0;
		for(DealInfo dInfo : skillList){
			dInfo.renew(mode);
			if(dInfo.icon.getItem().getName().equals(character.getRepresentSkill().getName()))
				representDamage=dInfo.deal;
		}
		
		Collections.sort(skillList);
		totalPageNumber=(skillList.size()-1)/LISTSIZE+1;
		while(skillList.size()>LISTSIZE){
			skillList.getFirst().getComposite().dispose();
			skillList.removeFirst();
		}
		
		if(skillList.size()>1){
			Iterator<DealInfo> iter = skillList.iterator();
			DealInfo prev=iter.next();
			while(iter.hasNext())
			{
				DealInfo temp=iter.next();
				temp.getComposite().moveAbove(prev.getComposite());
				prev=temp;
			}
		}
		mainComposite.layout();
		
		renewEvaluate();
	}
	
	private void renewEvaluate()
	{
		int compare = -1;
		if(compareDamage!=0) compare = (int) (representDamage*100/compareDamage);
		
		if(compare==-1) evaluateLabel.setText("세팅 스코어 : -");
		else evaluateLabel.setText("세팅 스코어 : "+compare);
		
		//System.out.println(compareDamage);
		//System.out.println(representDamage);
		if(compare<500)
			evaluateLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		else if(compare<1250)
			evaluateLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		else if(compare<2000)
			evaluateLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));
		else if(compare<2500)
			evaluateLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA));
		else if(compare<3000)
			evaluateLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		else if(compare<3500)
			evaluateLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
		else
			evaluateLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		if(compare>=2000)
			evaluateLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		else
			evaluateLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setCompareSetting(Setting setting)
	{
		this.compareSetting=setting;
		renew();
	}
	
	public void setMonster(Monster monster) {this.monster=monster;}

	@Override
	public void renew() {
	
		LinkedList<DealInfo> newList = new LinkedList<DealInfo>();
		Setting tempSetting = (Setting) character.getItemSetting().clone();
		
		character.setItemSettings(Setting.getMagicalSealedSetting(character.getJob()), false);
		compareDamage = Calculator.getDamage(character.getRepresentSkill(), monster, character, mode);
		character.setItemSettings(tempSetting, false);
		
		if(compareSetting!=null)
		{
			dealLabel.setText("데미지 "+"( vs "+compareSetting.setting_name+" )");
			character.setItemSettings(compareSetting, true);
			
			for(Skill skill : character.getDamageSkillList())
			{
				DealInfo prevInfo = null;
				for(DealInfo dInfo : skillList){
					if(dInfo.icon.getItem().getName().equals(skill.getName())){
						prevInfo=dInfo;
						prevInfo.setMonster(monster);
						prevInfo.setCompare(Calculator.getDamage(skill, monster, character, mode));	
						break;
					}
				}
				if(prevInfo!=null) newList.add(prevInfo);
				else newList.add(new DealInfo(mainComposite, skill, character, monster,
						Calculator.getDamage(skill, monster, character, mode), mode));
			}
		}
		else
		{
			for(Skill skill : character.getDamageSkillList())
			{
				DealInfo prevInfo = null;
				for(DealInfo dInfo : skillList){
					if(dInfo.icon.getItem().getName().equals(skill.getName())){
						prevInfo=dInfo;
						prevInfo.setMonster(monster);
						prevInfo.setCompare(-1);	
						break;
					}
				}
				if(prevInfo!=null) newList.add(prevInfo);
				else newList.add(new DealInfo(mainComposite, skill, character, monster, mode));
			}
		}
		
		for(int i=0; i<newList.size(); i++)
		{
			if(!character.getDamageSkillList().contains(newList.get(i).icon.getItem())){
				newList.get(i).getComposite().dispose();
				newList.remove(i--);
			}
		}
		skillList = newList;
		
		representDamage=0;
		for(DealInfo dInfo : skillList){
			dInfo.renew(mode);
			if(dInfo.icon.getItem().getName().equals(character.getRepresentSkill().getName()))
				representDamage=dInfo.deal;
		}
		
		Collections.sort(skillList);
		totalPageNumber=(skillList.size()-1)/LISTSIZE+1;
		for(int i=0; i<(pageNumber-1)*LISTSIZE; i++){
			skillList.getLast().getComposite().dispose();
			skillList.removeLast();
		}
		
		while(skillList.size()>LISTSIZE){
			skillList.getFirst().getComposite().dispose();
			skillList.removeFirst();
		}
		
		if(!skillList.isEmpty()){
			skillList.getFirst().getComposite().moveAbove(null);
			explain.moveAbove(null);
			settings.moveAbove(null);
		}
		
		if(skillList.size()>1){
			Iterator<DealInfo> iter = skillList.iterator();
			DealInfo prev=iter.next();
			while(iter.hasNext())
			{
				DealInfo temp=iter.next();
				temp.getComposite().moveAbove(prev.getComposite());
				prev=temp;
			}
		}
		
		mainComposite.layout();
		renewEvaluate();
	}
}

class DealInfo extends DnFComposite implements Comparable<DealInfo>{
	private Characters character;
	private Monster monster;
	ItemButton<Skill> icon;
	CLabel dealLabel;
	CLabel hpLabel;
	long deal;
	private long deal_compare;
	private int mode;
	
	public DealInfo(Composite parent, Skill skill, Characters character, Monster monster, long deal_compare, int mode)
	{
		deal=0;
		this.mode=mode;
		this.deal_compare=deal_compare;
		this.character=character;
		
		mainComposite = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.wrap=false;
		layout.justify=true;
		layout.marginWidth=3;
		mainComposite.setLayout(layout);
		
		icon = new ItemButton<Skill>(mainComposite, skill, InterfaceSize.SKILL_BUTTON_SIZE, InterfaceSize.SKILL_BUTTON_SIZE);
		icon.getButton().setLayoutData(new RowData(InterfaceSize.SKILL_BUTTON_SIZE, InterfaceSize.SKILL_BUTTON_SIZE));
		icon.getButton().setAlignment(SWT.CENTER);
		
		SetListener listenerGroup = new SetListener(icon, character, null, parent);
		icon.getButton().addListener(SWT.MouseEnter, listenerGroup.makeSkillInfoListener(parent.getShell()));
		icon.getButton().addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener());
		icon.getButton().addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());
		
		dealLabel = new CLabel(mainComposite, SWT.NONE);
		dealLabel.setMargins(0, 7, 0, 0);
		dealLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_DEALSIZE_X, InterfaceSize.SKILL_BUTTON_SIZE));
		dealLabel.setAlignment(SWT.CENTER);
		
		hpLabel = new CLabel(mainComposite, SWT.NONE);
		hpLabel.setMargins(0, 7, 0, 0);
		hpLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_HPSIZE_X, InterfaceSize.SKILL_BUTTON_SIZE));
		hpLabel.setAlignment(SWT.CENTER);
		
		setMonster(monster);
		mainComposite.layout();
	}
	public DealInfo(Composite parent, Skill skill, Characters character, Monster monster, int mode)
	{
		this(parent, skill, character, monster, -1, mode);
	}

	public void setMonster(Monster monster) {
		if(monster==null) return;
		this.monster=monster;
	}

	public void renew(int mode){
		this.mode=mode;
		icon.renewImage();
		
		deal = Calculator.getDamage(icon.getItem(), monster, character, mode);
		
		String compareStr;
		if(deal_compare==-1){
			compareStr = " ( 비교 대상 없음 )";
			dealLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		}
		else if(deal>deal_compare){
			dealLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
			double diff = ( (((double)deal)/deal_compare) -1)*100;
			compareStr = " (+"+Double.parseDouble(String.format("%.1f", diff))+"%)";
		}
		else if(deal==deal_compare){
			compareStr = " (-)";
			dealLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		}
		else{
			dealLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
			double diff = ( (((double)deal)/deal_compare) -1)*100;
			compareStr = " ("+Double.parseDouble(String.format("%.1f", diff))+"%)";
		}
		
		String dealStr = Long.toString(deal);
		String newStr;
		if(dealStr.length()>8){
			newStr = dealStr.substring(0, dealStr.length()-8)+"억 ";
			newStr += dealStr.substring(dealStr.length()-8, dealStr.length()-4)+"만 ";
			newStr += dealStr.substring(dealStr.length()-4);
		}
		else if(dealStr.length()>4){
			newStr = dealStr.substring(0, dealStr.length()-4)+"만 ";
			newStr += dealStr.substring(dealStr.length()-4);
		}
		else newStr = dealStr;
		dealLabel.setText(newStr+compareStr);
		
		try {
			long hp = monster.getLongStat("체력");
			String text;
			if(hp==0) text = "-";
			else text = String.valueOf(Double.parseDouble(String.format("%.2f", ((double)deal)/hp*100))) +"%";			
			hpLabel.setText(text);
			hpLabel.setForeground(dealLabel.getForeground());
		} catch (UndefinedStatusKey | StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	public void setCompare(long deal) {
		deal_compare=deal;
		
	}

	@Override
	public int compareTo(DealInfo o) {
		if(deal>o.deal) return 1;
		else if(deal==o.deal) return 0;
		else return -1;
	}
	@Override
	public void renew() {
		renew(mode);	
	}
}
