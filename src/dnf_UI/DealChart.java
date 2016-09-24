package dnf_UI;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
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
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;
import dnf_infomation.GetDictionary;

public class DealChart extends DnFComposite {
	Characters character;
	LinkedList<DealInfo> skillList;
	Monster monster;
	
	public DealChart(Composite parent, Characters character)
	{
		this.character=character;
		mainComposite = new Composite(parent, SWT.BORDER);
		mainComposite.setLayout(new RowLayout(SWT.VERTICAL));
		
		skillList = new LinkedList<DealInfo>();
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
		dealLabel.setText("데미지");
		dealLabel.setAlignment(SWT.CENTER);
		dealLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_DEALSIZE_X, 15));
		
		Label hpLabel = new Label(explain, SWT.BORDER);
		hpLabel.setText("깎은 체력량(%)");
		hpLabel.setAlignment(SWT.CENTER);
		hpLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_HPSIZE_X, 15));
		
		skillList = new LinkedList<DealInfo>();
		
		for(Skill skill : character.getSkillList())
			if(skill.hasDamage() && skill.getActiveEnabled()){
				skillList.add(new DealInfo(mainComposite, skill, character, monster));
			}
		
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
	Label dealLabel;
	Label hpLabel;
	long deal;
	private long deal_compare;
	
	public DealInfo(Composite parent, Skill skill, Characters character, Monster monster)
	{
		deal=0;
		deal_compare=-1;
		this.skill=skill;
		this.character=character;
		
		mainComposite = new Composite(parent, SWT.NONE);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.wrap=false;
		layout.justify=true;
		mainComposite.setLayout(layout);
		
		icon = new Label(mainComposite, SWT.NONE);
		icon.setLayoutData(new RowData(InterfaceSize.SKILL_BUTTON_SIZE, InterfaceSize.SKILL_BUTTON_SIZE));
		icon.setAlignment(SWT.CENTER);
		
		dealLabel = new Label(mainComposite, SWT.NONE);
		dealLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_DEALSIZE_X, InterfaceSize.SKILL_BUTTON_SIZE));
		dealLabel.setAlignment(SWT.CENTER);
		
		hpLabel = new Label(mainComposite, SWT.NONE);
		hpLabel.setLayoutData(new RowData(InterfaceSize.DEALCHART_HPSIZE_X, InterfaceSize.SKILL_BUTTON_SIZE));
		hpLabel.setAlignment(SWT.CENTER);
		
		setMonster(monster);
		mainComposite.layout();
	}

	public void setMonster(Monster monster) {
		if(monster==null) return;
		this.monster=monster;
		renew();
	}

	@Override
	public void renew(){
		icon.setImage(GetDictionary.iconDictionary.get(skill.getName()));
		
		SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true);
		if(skillInfo.hasPhy_per()) deal += Calculator.percentDamage_physical(skillInfo.phy_atk, monster, character);
		if(skillInfo.hasPhy_fix()) deal += Calculator.fixedDamage_physical(skillInfo.phy_fix, monster, character);
		if(skillInfo.hasMag_per()) deal += Calculator.percentDamage_magical(skillInfo.mag_atk, monster, character);
		if(skillInfo.hasMag_fix()) deal += Calculator.fixedDamage_magical(skillInfo.phy_atk, monster, character);
		
		String compareStr;
		if(deal_compare==-1){
			compareStr = " (비교 대상 없음)";
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
	
	public void setCompare(long deal) {deal_compare=deal;}

	@Override
	public int compareTo(DealInfo o) {
		if(deal>o.deal) return 1;
		else if(deal==o.deal) return 0;
		else return -1;
	}
}
