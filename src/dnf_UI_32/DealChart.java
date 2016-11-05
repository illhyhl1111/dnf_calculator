package dnf_UI_32;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
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
	private Label dealLabel;
	public final static int LISTSIZE=14;
	
	public Composite settingEvaluate;
	private Label evaluateLabel;
	private long representDamage;
	private long compareDamage;
	
	public DealChart(Composite parent, Characters character)
	{
		this.character=character;
		mainComposite = new Composite(parent, SWT.BORDER);
		mainComposite.setLayout(new RowLayout(SWT.VERTICAL));
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
		if(compareSetting!=null)
		{
			character.setItemSettings(compareSetting, true);
			
			for(Skill skill : character.getDamageSkillList())
				skillList.add(new DealInfo(mainComposite, skill, character, monster,
						Calculator.getDamage(skill, monster, character)));
		}
		else
		{			
			for(Skill skill : character.getDamageSkillList())
				skillList.add(new DealInfo(mainComposite, skill, character, monster));
		}		
		
		character.setItemSettings(Setting.getMagicalSealedSetting(character.getJob()), false);
		compareDamage = Calculator.getDamage(character.getRepresentSkill(), monster, character);
		character.setItemSettings(tempSetting, false);
		
		representDamage=0;
		for(DealInfo dInfo : skillList){
			dInfo.renew();
			if(dInfo.icon.getItem().getName().equals(character.getRepresentSkill().getName()))
				representDamage=dInfo.deal;
		}
		
		Collections.sort(skillList);
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
						prevInfo.setCompare(Calculator.getDamage(skill, monster, character));	
						break;
					}
				}
				if(prevInfo!=null) newList.add(prevInfo);
				else newList.add(new DealInfo(mainComposite, skill, character, monster,
						Calculator.getDamage(skill, monster, character)));
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
				else newList.add(new DealInfo(mainComposite, skill, character, monster));
			}
		}
		
		character.setItemSettings(Setting.getMagicalSealedSetting(character.getJob()), false);
		compareDamage = Calculator.getDamage(character.getRepresentSkill(), monster, character);
		character.setItemSettings(tempSetting, false);
		
		for(int i=0; i<skillList.size(); i++)
		{
			if(!character.getDamageSkillList().contains(skillList.get(i).icon.getItem())){
				skillList.get(i).getComposite().dispose();
				skillList.remove(i--);
			}
		}
		skillList = newList;
		
		representDamage=0;
		for(DealInfo dInfo : skillList){
			dInfo.renew();
			if(dInfo.icon.getItem().getName().equals(character.getRepresentSkill().getName()))
				representDamage=dInfo.deal;
		}
		
		Collections.sort(skillList);
		while(skillList.size()>LISTSIZE){
			skillList.getFirst().getComposite().dispose();
			skillList.removeFirst();
		}
		
		if(!skillList.isEmpty()){
			skillList.getFirst().getComposite().moveAbove(null);
			explain.moveAbove(null);
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
	
	public DealInfo(Composite parent, Skill skill, Characters character, Monster monster, long deal_compare)
	{
		deal=0;
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
	public DealInfo(Composite parent, Skill skill, Characters character, Monster monster)
	{
		this(parent, skill, character, monster, -1);
	}

	public void setMonster(Monster monster) {
		if(monster==null) return;
		this.monster=monster;
	}

	@Override
	public void renew(){
		icon.renewImage();
		
		deal = Calculator.getDamage(icon.getItem(), monster, character);
		
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
}
