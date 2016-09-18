package dnf_UI;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import dnf_InterfacesAndExceptions.Dimension_stat;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_calculator.Calculator;
import dnf_calculator.ElementInfo;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.Status;
import dnf_calculator.StatusAndName;
import dnf_class.Card;
import dnf_class.Emblem;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.SetOption;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;
import dnf_infomation.ItemDictionary;

public class MakeComposite {

	public static void setSetInfoComposite(Composite itemInfo, Item item, int setNum, ItemDictionary itemDictionary)
	{
		Label name = new Label(itemInfo, SWT.WRAP);
		name.setText(item.getSetName().getName());
		name.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_GREEN));
		
		Label option;
		
		try
		{
			LinkedList<SetOption> setOptionList = itemDictionary.getSetOptions(item.getSetName());
			
			for(SetOption s : setOptionList)
			{
				Color color;
				boolean enabled;
				if(s.isEnabled(setNum))
				{
					option = new Label(itemInfo, SWT.SEPARATOR | SWT.HORIZONTAL);
					option.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
					
					option = new Label(itemInfo, SWT.WRAP);
					option.setText("["+s.requireNum+"]세트 효과");
					option.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_GREEN));
					color = itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLACK);
					enabled=true;
				}
				else
				{
					option = new Label(itemInfo, SWT.SEPARATOR | SWT.HORIZONTAL);
					option.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
					
					option = new Label(itemInfo, SWT.WRAP);
					option.setText("["+s.requireNum+"]세트 효과");
					option.setEnabled(false);
					color = itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLACK);
					enabled=false;
				}
					
				for(StatusAndName s2 : s.vStat.statList)
					setText(itemInfo, s2, enabled, color);
				if(!s.dStat.statList.isEmpty() || !s.fStat.explanation.isEmpty())
				{
					option = new Label(itemInfo, SWT.WRAP);
					option.setText("\n――――――던전 입장 시 적용――――――\n\n");
					option.setEnabled(enabled);
					for(StatusAndName s2 : s.dStat.statList)
						setText(itemInfo, s2, enabled, color);
					for(String str : item.fStat.explanation){
						option = new Label(itemInfo, SWT.WRAP);
						option.setText(str);
						option.setForeground(color);
						option.setEnabled(enabled);
					}
				}
			
				if(!s.explanation.isEmpty()) option = new Label(itemInfo, SWT.WRAP);
				for(String str : s.explanation){
					option = new Label(itemInfo, SWT.WRAP);
					option.setText(str);
					option.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				}
			}			
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
		catch (ItemFileNotFounded e) {
			System.out.println("미구현");
		}
	}
	
	public static void setItemInfoComposite(Composite itemInfo, Item item)
	{	
		Label stat = new Label(itemInfo, SWT.WRAP);
		String temp = item.getName();
		if(item instanceof Equipment && ((Equipment)item).getReinforce()!=0) temp = "+"+((Equipment)item).getReinforce()+" "+temp;
		stat.setText(temp);
		
		Label rarity = new Label(itemInfo, SWT.WRAP);
		rarity.setText(item.getRarity().getName());
		rarity.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		
		//Color nameColor = new Color(itemInfo.getDisplay());
		switch(item.getRarity())
		{
		case EPIC:
			stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			rarity.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			break;
		case UNIQUE:
			stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			rarity.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			break;
		case LEGENDARY:
			stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			rarity.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			break;
		case RARE:
			stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			rarity.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			break;
		default:
		}
		
		Label type = new Label(itemInfo, SWT.WRAP);
		type.setText(item.getTypeName());
		type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		if(item.getTypeName2()!=null)
		{
			type = new Label(itemInfo, SWT.WRAP);
			type.setText(item.getTypeName2());
			type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		}
		if(item instanceof Equipment){
			type = new Label(itemInfo, SWT.WRAP);
			type.setText(String.valueOf("레벨제한 "+ ((Equipment)item).level) );
			type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		}
		
		try
		{
			StatusAndName dimStat;
			StatusAndName phyIgnStat;
			StatusAndName magIgnStat;
			StatusAndName aidStat;
			StatusAndName[] earringStat = new StatusAndName[3];
			
			try{
				dimStat = item.vStat.statList.get(item.getDimStatIndex());
				
				if(((Equipment)item).getDimentionStat()!=Dimension_stat.NONE){
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText(" 차원의 "+StatusAndName.getStatHash().get(dimStat.name)+String.valueOf((int)dimStat.stat.getStatToDouble()));
					stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
				}
			} catch(IndexOutOfBoundsException e){
				dimStat=null;
			}
			
			try{
				phyIgnStat = item.vStat.statList.get(item.getIgnIndex());
				magIgnStat = item.vStat.statList.get(item.getIgnIndex()+1);
				
				if(((Equipment)item).getReinforce()!=0){
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText(StatusAndName.getStatHash().get(phyIgnStat.name)+String.valueOf((int)phyIgnStat.stat.getStatToDouble()));
					stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLUE));
					
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText(StatusAndName.getStatHash().get(magIgnStat.name)+String.valueOf((int)magIgnStat.stat.getStatToDouble()));
					stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				}
			} catch(IndexOutOfBoundsException e){
				phyIgnStat=null;
				magIgnStat=null;
			}
			
			try{
				aidStat = item.vStat.statList.get(item.getAidStatIndex());
				
				if(((Equipment)item).getReinforce()!=0){
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText(" 힘,지능,체력,정신력 +"+String.valueOf((int)aidStat.stat.getStatToDouble()));
					stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				}
			} catch(IndexOutOfBoundsException e){
				aidStat=null;
			}
			
			try{
				earringStat[0] = item.vStat.statList.get(item.getEarringStatIndex());
				earringStat[1] = item.vStat.statList.get(item.getEarringStatIndex()+1);
				earringStat[2] = item.vStat.statList.get(item.getEarringStatIndex()+2);
				
				if(((Equipment)item).getReinforce()!=0){
					for(int i=0; i<3; i++){
						stat = new Label(itemInfo, SWT.WRAP);
						stat.setText(" "+StatusAndName.getStatHash().get(earringStat[i].name)+String.valueOf((int)earringStat[i].stat.getStatToDouble()));
						stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLUE));
					}
				}
			} catch(IndexOutOfBoundsException e){
				earringStat[0]=null;
				earringStat[1]=null;
				earringStat[2]=null;
			}
			
			stat = new Label(itemInfo, SWT.SEPARATOR | SWT.HORIZONTAL);
			stat.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			
			if(item instanceof Card)
			{
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText(((Card) item).getPartToString());
			}
			else if(item instanceof Emblem)
			{
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText( ((Emblem) item).type.getName());
			}
			
			setStatusText(item.getItemStatIndex(), item, itemInfo, itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			
			if(item.getCard()!=null)
			{
				Card card=item.getCard();
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText("");

				setStatusText(card, itemInfo, itemInfo.getDisplay().getSystemColor(SWT.COLOR_GREEN));
			}
			else if(item.getEmblem()!=null)
			{
				Emblem[] emblemList = item.getEmblem();
				String[] stringList = {"엠블렘 1", "엠블렘 2", "플래티넘 엠블렘"};
				int i=0;
				for(Emblem emblem : emblemList)
				{
					if(emblem.getName().contains("없음")) continue;
					stat = new Label(itemInfo, SWT.WRAP);				
					stat.setText("\n"+stringList[i]);

					setStatusText(emblem, itemInfo, itemInfo.getDisplay().getSystemColor(SWT.COLOR_GREEN));
					i++;
				}
			}
			
			if(!item.explanation.isEmpty()) stat = new Label(itemInfo, SWT.WRAP);
			for(String str : item.explanation){
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText(str);
				stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			}
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	public static void setSkillInfoComposite(Composite composite, Skill skill, Status stat)
	{
		Label label = new Label(composite, SWT.WRAP);
		String name;
		if(skill.getSkillLevel()!=0) name = skill.getName()+" Lv "+(skill.getSkillLevel()+skill.dungeonLevel)
				+"("+skill.getSkillLevel()+" + "+ skill.dungeonLevel+")";
		else name = skill.getName()+ "Lv 0";
		label.setText(name);
		if(skill.type==Skill_type.PASSIVE)
			label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		else
			label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
		
		label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		
		SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true);
		
		if(skillInfo.phy_atk!=0 || skillInfo.phy_fix!=0){
			label = new Label(composite, SWT.WRAP);
			String atk = "";
			if(skillInfo.phy_atk!=0) atk = String.valueOf(skillInfo.phy_atk)+"%";
			String fix = "";
			if(skillInfo.phy_fix!=0) fix = String.valueOf((int)(skillInfo.phy_fix*Calculator.getInfoIndependentATK(stat)));
			String add = "";
			if(skillInfo.phy_atk!=0 && skillInfo.phy_fix!=0) add = " + ";
			label.setText("물리공격력(총합) : "+atk+add+fix);
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		}
		
		if(skillInfo.mag_atk!=0 || skillInfo.mag_fix!=0){
			label = new Label(composite, SWT.WRAP);
			String atk = "";
			if(skillInfo.mag_atk!=0) atk = String.valueOf(skillInfo.mag_atk)+"%";
			String fix = "";
			if(skillInfo.mag_fix!=0) fix = String.valueOf((int)(skillInfo.mag_fix*Calculator.getInfoIndependentATK(stat)));
			String add = "";
			if(skillInfo.mag_atk!=0 && skillInfo.mag_fix!=0) add = " + ";
			label.setText("마법공격력(총합) : "+atk+add+fix);
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		}
		
		if(skill.hasBuff())
		{
			if(skill.hasDamage()){
				label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
				label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			}
			
			label = new Label(composite, SWT.WRAP);
			label.setText("[버프옵션]");
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			
			for(StatusAndName s : skillInfo.stat.statList)
				try {
					setText(composite, s, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
		}
		
		if(!skillInfo.fromDictionary)
		{
			label = new Label(composite, SWT.WRAP);
			label.setText("\nLv "+skill.getSkillLevel()+" 에 대한 정보가 기록되어있지 않습니다. 대충 가장 가까운 레벨로 추정합니다.");
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		}
		composite.setSize(composite.computeSize(SWT.NONE, SWT.NONE));
	}
	
	
	private static void setStatusText(Item item, Composite itemInfo, Color color) throws StatusTypeMismatch
	{
		setStatusText(0, item, itemInfo, color);
	}
	
	private static void setStatusText(int startIndex, Item item, Composite itemInfo, Color color) throws StatusTypeMismatch
	{
		for(StatusAndName s : item.vStat.statList.subList(startIndex, item.vStat.statList.size()))
			setText(itemInfo, s, color);
		
		if(!item.dStat.statList.isEmpty() || !item.fStat.statList.isEmpty())
		{
			Label stat = new Label(itemInfo, SWT.WRAP);
			stat.setText("\n――――――던전 입장 시 적용――――――\n\n");
			stat.setForeground(color);
			for(StatusAndName s : item.dStat.statList)
				setText(itemInfo, s, color);
			
			for(String s : item.fStat.explanation){
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText(s);
				stat.setForeground(color);
			}
		}
	}
	
	private static void setText(Composite itemInfo, StatusAndName s, Color textColor) throws StatusTypeMismatch
	{
		setText(itemInfo, s, true, textColor);
	}
	
	private static void setText(Composite itemInfo, StatusAndName s, boolean enable, Color textColor) throws StatusTypeMismatch
	{
		String strength;
		Label stat;
		
		strength = String.valueOf(s.stat.getStatToDouble());
		if(s.stat instanceof ElementInfo && strength.equals("0.0"));
		else if(s.stat instanceof SkillStatusInfo && strength.equals("0.0"));
		else{
			if(strength.contains(".0"))
				strength=strength.substring(0, strength.length()-2);
			
			stat = new Label(itemInfo, SWT.WRAP);
				
			String name = StatusAndName.getStatHash().get(s.name);
			if(s.name==StatList.SKILL || s.name==StatList.SKILL_RANGE)
				name = s.stat.getStatToString()+name;
			
			if(strength.contains("-")){
				if(name.contains("+")) stat.setText(name.substring(0, name.length()-1)+strength);
				else if(name.contains("-")) stat.setText(name.substring(0, name.length()-1)+"+"+strength.substring(1, strength.length()));
				else stat.setText(name+strength);
			}
			else stat.setText(name+strength);
			stat.setEnabled(enable && s.enabled);
			if(!s.enabled)
				stat.setText(stat.getText()+"(옵션 꺼짐)");
			
			stat.setForeground(textColor);
		}
		
		if(s.stat instanceof ElementInfo && ((ElementInfo)s.stat).getElementEnabled()==true)
		{
			stat = new Label(itemInfo, SWT.WRAP);
			switch(s.name)
			{
			case StatList.ELEM_FIRE:
				stat.setText(" 무기에 화속성 부여");
				break;
			case StatList.ELEM_WATER:
				stat.setText(" 무기에 수속성 부여");
				break;
			case StatList.ELEM_LIGHT:
				stat.setText(" 무기에 명속성 부여");
				break;
			case StatList.ELEM_DARKNESS:
				stat.setText(" 무기에 암속성 부여");
				break;
			}
			stat.setEnabled(enable && s.enabled);
			if(!s.enabled)
				stat.setText(stat.getText()+"(옵션 꺼짐)");
			
			stat.setForeground(textColor);
		}
		
		else if(s.stat instanceof SkillStatusInfo && ((SkillStatusInfo)s.stat).getIncrease()>1.0005)
		{
			stat = new Label(itemInfo, SWT.WRAP);
			stat.setText(s.stat.getStatToString()+" 데미지 증가 + "+((SkillStatusInfo)s.stat).getIncrease());		
			stat.setEnabled(enable && s.enabled);
			if(!s.enabled)
				stat.setText(stat.getText()+"(옵션 꺼짐)");
		}
	}
}
