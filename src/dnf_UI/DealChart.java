package dnf_UI;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
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
import dnf_class.SkillLevelInfo;
import dnf_infomation.GetDictionary;

public class DealChart extends DnFComposite {
	Characters character;
	LinkedList<DealInfo> skillList;
	Monster monster;
	private Setting compareSetting;
	
	public DealChart(Composite parent, Characters character)
	{
		this.character=character;
		mainComposite = new Composite(parent, SWT.BORDER);
		mainComposite.setLayout(new RowLayout(SWT.VERTICAL));
		
		skillList = new LinkedList<DealInfo>();
		monster = new Monster("default");
		compareSetting = null;
	}
	
	public void setDealChart(Monster monster)
	{
		for(Control composite : mainComposite.getChildren())
			composite.dispose();
		
		Composite explain = new Composite(mainComposite, SWT.NONE);
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
		
		Label dealLabel = new Label(explain, SWT.BORDER);
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
		
		if(compareSetting!=null)
		{
			Setting tempSetting = (Setting) character.getItemSetting().clone();
			character.setItemSettings(compareSetting, true);
			
			for(Skill skill : character.getSkillList())
				if(skill.hasDamage() && skill.getActiveEnabled()){
					skillList.add(new DealInfo(mainComposite, skill, character, monster,
							Calculator.getDamage(skill.getSkillLevelInfo(true), monster, character)));
				}
			
			character.setItemSettings(tempSetting, false);
		}
		else
		{
			for(Skill skill : character.getSkillList())
				if(skill.hasDamage() && skill.getActiveEnabled()){
					skillList.add(new DealInfo(mainComposite, skill, character, monster));
				}
		}
		for(DealInfo dInfo : skillList)
			dInfo.renew();
		
		if(skillList.size()>1){
			Collections.sort(skillList);
			Iterator<DealInfo> iter = skillList.iterator();
			DealInfo prev=iter.next();
			while(iter.hasNext())
			{
				DealInfo temp=iter.next();
				temp.getComposite().moveAbove(prev.getComposite());
				prev=temp;
			}
		}
		
		this.monster=monster;
		
		mainComposite.layout();
	}
	
	public void setCompareSetting(Setting setting)
	{
		this.compareSetting=setting;
		renew();
	}

	@Override
	public void renew() {
		setDealChart(monster);
	}
}

class DealInfo extends DnFComposite implements Comparable<DealInfo>{
	Skill skill;
	private Characters character;
	private Monster monster;
	Label icon;
	CLabel dealLabel;
	CLabel hpLabel;
	long deal;
	private long deal_compare;
	
	public DealInfo(Composite parent, Skill skill, Characters character, Monster monster, long deal_compare)
	{
		deal=0;
		this.deal_compare=deal_compare;
		this.skill=skill;
		this.character=character;
		
		mainComposite = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.wrap=false;
		layout.justify=true;
		layout.marginWidth=3;
		mainComposite.setLayout(layout);
		
		icon = new Label(mainComposite, SWT.NONE);
		icon.setLayoutData(new RowData(InterfaceSize.SKILL_BUTTON_SIZE, InterfaceSize.SKILL_BUTTON_SIZE));
		icon.setAlignment(SWT.CENTER);
		
		dealLabel = new CLabel(mainComposite, SWT.NONE);
		dealLabel.setMargins(0, 15, 0, 0);
		dealLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_DEALSIZE_X, InterfaceSize.SKILL_BUTTON_SIZE));
		dealLabel.setAlignment(SWT.CENTER);
		
		hpLabel = new CLabel(mainComposite, SWT.NONE);
		hpLabel.setMargins(0, 15, 0, 0);
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
		icon.setImage(GetDictionary.iconDictionary.get(skill.getName()));
		
		SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true);
		deal = Calculator.getDamage(skillInfo, monster, character);
		
		String compareStr;
		if(deal_compare==-1){
			compareStr = " ( 비교 대상 없음 )";
			dealLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		}
		else if(deal>deal_compare){
			dealLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
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
		dealLabel.setText(Long.toString(deal)+compareStr);
		
		try {
			int hp = monster.getStat("체력");
			String text;
			if(hp==0) text = "-";
			else text = String.valueOf(Double.parseDouble(String.format("%.2f", ((double)deal)/hp*100)));			
			hpLabel.setText(text+"%");
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
