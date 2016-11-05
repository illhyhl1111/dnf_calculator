package dnf_UI_32;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import dnf_InterfacesAndExceptions.Dimension_stat;
import dnf_InterfacesAndExceptions.DnFColor;
import dnf_InterfacesAndExceptions.InterfaceSize;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Location;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_calculator.Calculator;
import dnf_calculator.ElementInfo;
import dnf_calculator.SkillRangeStatusInfo;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.Status;
import dnf_calculator.StatusAndName;
import dnf_class.Card;
import dnf_class.Characters;
import dnf_class.Emblem;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.Monster;
import dnf_class.MonsterOption;
import dnf_class.SetOption;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;
import dnf_class.Weapon;
import dnf_infomation.ItemDictionary;

public class MakeComposite {

	public static void setSetInfoComposite(Composite itemInfo, Item item, int setNum, ItemDictionary itemDictionary, boolean transparentBackground)
	{
		if(transparentBackground) itemInfo.setBackground(DnFColor.infoBackground_transparent);
		else itemInfo.setBackground(DnFColor.infoBackground);
		itemInfo.setBackgroundMode(SWT.INHERIT_FORCE);
		GridData leftData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		leftData.widthHint=InterfaceSize.SET_INFO_SIZE-10;
		
		Label name = new Label(itemInfo, SWT.WRAP);
		name.setText(item.getSetName().getName());
		name.setLayoutData(leftData);
		name.setForeground(DnFColor.INCHANT);
		
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
					leftData = new GridData(SWT.LEFT, SWT.TOP, true, false);
					leftData.widthHint=InterfaceSize.SET_INFO_SIZE-10;
					option.setLayoutData(leftData);
					option.setForeground(DnFColor.INCHANT);
					color = DnFColor.infoStat;
					enabled=true;
				}
				else
				{
					option = new Label(itemInfo, SWT.SEPARATOR | SWT.HORIZONTAL);
					option.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
					
					option = new Label(itemInfo, SWT.WRAP);
					option.setText("["+s.requireNum+"]세트 효과");
					leftData = new GridData(SWT.LEFT, SWT.TOP, true, false);
					leftData.widthHint=InterfaceSize.SET_INFO_SIZE-10;
					option.setLayoutData(leftData);
					option.setForeground(DnFColor.DISABLED);
					color = DnFColor.DISABLED;
					enabled=false;
				}
					
				for(StatusAndName s2 : s.vStat.statList)
					setText(itemInfo, s2, enabled, color, InterfaceSize.SET_INFO_SIZE-10);
				if(!s.dStat.statList.isEmpty())
				{
					option = new Label(itemInfo, SWT.WRAP);
					option.setText("\n――――――던전 입장 시 적용――――――\n\n");
					if(!enabled) option.setForeground(DnFColor.DISABLED);
					else option.setForeground(DnFColor.infoStat);
					for(StatusAndName s2 : s.dStat.statList)
						setText(itemInfo, s2, color, InterfaceSize.SET_INFO_SIZE-10);
				}
				
				if(!s.explanation.isEmpty()){
					Label stat = new Label(itemInfo, SWT.WRAP);
					stat.setText("");
				}
				for(String str : s.explanation){
					option = new Label(itemInfo, SWT.WRAP);
					option.setText(str);
					leftData = new GridData(SWT.LEFT, SWT.TOP, true, false);
					leftData.widthHint=InterfaceSize.SET_INFO_SIZE-10;
					option.setLayoutData(leftData);
					option.setForeground(DnFColor.infoExplanation);
				}
			}			
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
		catch (ItemNotFoundedException e) {
			System.out.println("미구현");
		}
	}
	
	public static void setItemInfoComposite(Composite itemInfo, Item item, Location location, Characters character)
	{	
		if(item.getName().contains("없음"))
		{
			itemInfo.dispose();
			return;
		}
		if(character.option.transparentBackground) itemInfo.setBackground(DnFColor.infoBackground_transparent);
		else itemInfo.setBackground(DnFColor.infoBackground);
		itemInfo.setBackgroundMode(SWT.INHERIT_FORCE);
		Label stat = new Label(itemInfo, SWT.WRAP);
		String temp = item.getName();
		if(item instanceof Equipment && ((Equipment)item).getReinforce()!=0) temp = "+"+((Equipment)item).getReinforce()+" "+temp;
		stat.setText(temp);
		GridData leftData = new GridData();
		leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
		stat.setLayoutData(leftData);
		
		Label rarity = new Label(itemInfo, SWT.WRAP);
		rarity.setText(item.getRarity().getName());
		rarity.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		
		//Color nameColor = new Color(itemInfo.getDisplay());
		switch(item.getRarity())
		{
		case EPIC:
			stat.setForeground(DnFColor.EPIC);
			rarity.setForeground(DnFColor.EPIC);
			break;
		case UNIQUE:
			stat.setForeground(DnFColor.UNIQUE);
			rarity.setForeground(DnFColor.UNIQUE);
			break;
		case LEGENDARY:
			stat.setForeground(DnFColor.LEGENDARY);
			rarity.setForeground(DnFColor.LEGENDARY);
			break;
		case RARE:
			stat.setForeground(DnFColor.RARE);
			rarity.setForeground(DnFColor.RARE);
			break;
		case CHRONICLE:
			stat.setForeground(DnFColor.CHRONICLE);
			rarity.setForeground(DnFColor.CHRONICLE);
			break;
		case UNCOMMON:
			stat.setForeground(DnFColor.UNCOMMON);
			rarity.setForeground(DnFColor.UNCOMMON);
			break;
		case COMMON:
			stat.setForeground(DnFColor.COMMON);
			rarity.setForeground(DnFColor.COMMON);
			break;
		default:
			stat.setForeground(DnFColor.COMMON);
			rarity.setForeground(DnFColor.COMMON);
			break;
		}
		
		if(location==Location.DUNGEON)
		{
			String compare = character.compareItem(item);
			stat.setText(temp+compare);
			if(compare.contains("▲")) stat.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
			else if(compare.contains("▼")) stat.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}
		
		Label type = new Label(itemInfo, SWT.WRAP);
		type.setText(item.getTypeName());
		type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		type.setForeground(DnFColor.infoStat);
		if(item.getTypeName2()!=null)
		{
			type = new Label(itemInfo, SWT.WRAP);
			type.setText(item.getTypeName2());
			type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
			type.setForeground(DnFColor.infoStat);
		}
		if(item instanceof Equipment){
			type = new Label(itemInfo, SWT.WRAP);
			type.setText(String.valueOf("레벨제한 "+ ((Equipment)item).level) );
			type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
			type.setForeground(DnFColor.infoStat);
		}
		
		try
		{
			StatusAndName dimStat;
			StatusAndName phyIgnStat;
			StatusAndName magIgnStat;
			StatusAndName reforgeStat;
			StatusAndName aidStat;
			StatusAndName[] earringStat = new StatusAndName[3];
			
			try{
				dimStat = item.vStat.statList.get(item.getDimStatIndex());
				
				if(((Equipment)item).getDimentionStat()!=Dimension_stat.NONE){
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText(" 차원의 "+StatusAndName.getStatHash().get(dimStat.name)+String.valueOf((int)dimStat.stat.getStatToDouble()));
					stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
					leftData = new GridData();
					leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
					stat.setLayoutData(leftData);
				}
			} catch(IndexOutOfBoundsException e){
				dimStat=null;
			}
			
			try{
				phyIgnStat = item.vStat.statList.get(item.getIgnIndex());
				magIgnStat = item.vStat.statList.get(item.getIgnIndex()+1);
				
				if(((Equipment)item).getReinforce()!=0){
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText("+"+((Equipment)item).getReinforce()+" 강화: 방어무시 물리 공격력 + "+((int)phyIgnStat.stat.getStatToDouble()) );
					leftData = new GridData();
					leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
					stat.setLayoutData(leftData);
					stat.setForeground(DnFColor.UNCOMMON);
					
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText("+"+((Equipment)item).getReinforce()+" 강화: 방어무시 마법 공격력 + "+((int)magIgnStat.stat.getStatToDouble()));
					leftData = new GridData();
					leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
					stat.setLayoutData(leftData);
					stat.setForeground(DnFColor.UNCOMMON);
				}
			} catch(IndexOutOfBoundsException e){
				phyIgnStat=null;
				magIgnStat=null;
			}
			
			try{
				reforgeStat = item.vStat.statList.get(item.getReforgeIndex());
				
				if( ((Weapon)item).getReforge()!=0 ){
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText("+"+((Weapon)item).getReforge()+" 재련: 독립 공격력 +"+String.valueOf((int)reforgeStat.stat.getStatToDouble()));
					leftData = new GridData();
					leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
					stat.setLayoutData(leftData);
					stat.setForeground(DnFColor.RARE);
				}
			} catch(IndexOutOfBoundsException e){
				reforgeStat=null;
			}
			
			try{
				aidStat = item.vStat.statList.get(item.getAidStatIndex());
				
				if(((Equipment)item).getReinforce()!=0){
					stat = new Label(itemInfo, SWT.WRAP);
					stat.setText(" 힘,지능,체력,정신력 +"+String.valueOf((int)aidStat.stat.getStatToDouble()));
					leftData = new GridData();
					leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
					stat.setLayoutData(leftData);
					stat.setForeground(DnFColor.UNCOMMON);
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
						leftData = new GridData();
						leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
						stat.setLayoutData(leftData);
						stat.setForeground(DnFColor.UNCOMMON);
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
				leftData = new GridData();
				leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
				stat.setLayoutData(leftData);
				stat.setText(((Card) item).getPartToString());
				stat.setForeground(DnFColor.infoStat);
			}
			else if(item instanceof Emblem)
			{
				stat = new Label(itemInfo, SWT.WRAP);
				leftData = new GridData();
				leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
				stat.setLayoutData(leftData);
				stat.setText( ((Emblem) item).type.getName());
				stat.setForeground(DnFColor.infoStat);
			}
			
			setStatusText(item.getItemStatIndex(), item, itemInfo, DnFColor.infoStat, InterfaceSize.ITEM_INFO_SIZE-10);
			
			if(!item.explanation.isEmpty()){
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText("");
			}
			for(String str : item.explanation){
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText(str);
				leftData = new GridData();
				leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
				stat.setLayoutData(leftData);
				stat.setForeground(DnFColor.infoExplanation);
			}
			
			if(item.getCard()!=null && !item.getCard().getName().contains("없음"))
			{
				Card card=item.getCard();
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText("");

				setStatusText(card, itemInfo, DnFColor.INCHANT, InterfaceSize.ITEM_INFO_SIZE-10);
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
					stat.setForeground(DnFColor.infoStat);
					leftData = new GridData();
					leftData.widthHint=InterfaceSize.ITEM_INFO_SIZE-10;
					stat.setLayoutData(leftData);

					setStatusText(emblem, itemInfo, DnFColor.INCHANT, InterfaceSize.ITEM_INFO_SIZE-10);
					i++;
				}
			}
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	public static void setSkillInfoComposite(Composite composite, Skill skill, Status stat, boolean isBurning, boolean transparentBackground)
	{
		if(transparentBackground) composite.setBackground(DnFColor.infoBackground_transparent);
		else composite.setBackground(DnFColor.infoBackground);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		if(skill.getName().contains("없음"))
		{
			composite.dispose();
			return;
		}
		
		GridData leftData = new GridData(SWT.FILL, SWT.TOP, true, false);
		leftData.widthHint=InterfaceSize.SKILL_INFO_SIZE-10;
		
		Label label = new Label(composite, SWT.WRAP);
		label.setLayoutData(leftData);
		String name;
		if(skill.getCharSkillLevel()!=0) name = skill.getName()+" Lv "+ skill.getSkillLevel(true, isBurning)
				+"("+skill.getCharSkillLevel()+" + "+ (skill.getSkillLevel(true, isBurning)-skill.getCharSkillLevel())+")";
		else name = skill.getName()+ "Lv 0";
		label.setText(name);
		if(skill.type==Skill_type.PASSIVE || skill.type==Skill_type.TP)
			label.setForeground(DnFColor.INCHANT);
		else if(skill.type==Skill_type.BUF_ACTIVE || skill.type==Skill_type.SWITCHING)
			label.setForeground(DnFColor.UNCOMMON);
		else
			label.setForeground(DnFColor.DARK_YELLOW);
		
		label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		leftData = new GridData(SWT.FILL, SWT.TOP, true, false);
		leftData.widthHint=InterfaceSize.SKILL_INFO_SIZE-10;
		label.setLayoutData(leftData);
		
		SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true, isBurning);
		
		if(skillInfo.phy_atk!=0 || skillInfo.phy_fix!=0){
			label = new Label(composite, SWT.WRAP);
			leftData = new GridData(SWT.FILL, SWT.TOP, true, false);
			leftData.widthHint=InterfaceSize.SKILL_INFO_SIZE-10;
			label.setLayoutData(leftData);
			String atk = "";
			if(skillInfo.phy_atk!=0) atk = String.valueOf(skillInfo.phy_atk)+"%";
			String fix = "";
			if(skillInfo.phy_fix!=0) fix = String.valueOf((int)(skillInfo.phy_fix*Calculator.getInfoIndependentATK(stat)));
			String add = "";
			if(skillInfo.phy_atk!=0 && skillInfo.phy_fix!=0) add = " + ";
			label.setText("물리공격력(총합) : "+atk+add+fix);
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			label.setForeground(DnFColor.infoStat);
		}
		
		if(skillInfo.mag_atk!=0 || skillInfo.mag_fix!=0){
			label = new Label(composite, SWT.WRAP);
			leftData = new GridData(SWT.FILL, SWT.TOP, true, false);
			leftData.widthHint=InterfaceSize.SKILL_INFO_SIZE-10;
			label.setLayoutData(leftData);
			String atk = "";
			if(skillInfo.mag_atk!=0) atk = String.valueOf(skillInfo.mag_atk)+"%";
			String fix = "";
			if(skillInfo.mag_fix!=0) fix = String.valueOf((int)(skillInfo.mag_fix*Calculator.getInfoIndependentATK(stat)));
			String add = "";
			if(skillInfo.mag_atk!=0 && skillInfo.mag_fix!=0) add = " + ";
			label.setText("마법공격력(총합) : "+atk+add+fix);
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			label.setForeground(DnFColor.infoStat);
		}
		
		if(skill.hasBuff())
		{
			if(skill.hasDamage()){
				label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
				label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			}
			
			label = new Label(composite, SWT.WRAP);
			leftData = new GridData(SWT.FILL, SWT.TOP, true, false);
			leftData.widthHint=InterfaceSize.SKILL_INFO_SIZE-10;
			label.setLayoutData(leftData);
			label.setText("[버프옵션]");
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			label.setForeground(DnFColor.infoStat);
			
			for(StatusAndName s : skillInfo.stat.statList)
				try {
					setText(composite, s, DnFColor.infoStat, SWT.DEFAULT);
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
		}
		
		if(!skillInfo.fromDictionary)
		{
			label = new Label(composite, SWT.WRAP);
			leftData = new GridData(SWT.FILL, SWT.TOP, true, false);
			leftData.widthHint=InterfaceSize.SKILL_INFO_SIZE-10;
			label.setLayoutData(leftData);
			label.setText("\nLv "+skill.getSkillLevel(true, isBurning)+" 에 대한 정보가 기록되어있지 않습니다. 가장 가까운 레벨로 추정합니다.");
			label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			label.setForeground(DnFColor.infoExplanation);
			
			label = new Label(composite, SWT.WRAP);
			label.setText("");
		}
		
		if(!skill.explanation.isEmpty()){
			label = new Label(composite, SWT.WRAP);
			label.setText("");
		}
		for(String str : skill.explanation){
			Label explain = new Label(composite, SWT.WRAP);
			explain.setText(str);
			leftData = new GridData(SWT.LEFT, SWT.TOP, true, false);
			leftData.widthHint=InterfaceSize.SKILL_INFO_SIZE-10;
			explain.setLayoutData(leftData);
			explain.setForeground(DnFColor.infoExplanation);
			
			label = new Label(composite, SWT.WRAP);
			label.setText("");
		}
		
		//composite.setSize(composite.computeSize(InterfaceSize.SKILL_INFO_SIZE, SWT.NONE));
		
	}
	
	public static void setMonsterInfoComposite(Composite itemInfo, Monster monster, boolean transparentBackground)
	{
		if(transparentBackground) itemInfo.setBackground(DnFColor.infoBackground_transparent);
		else itemInfo.setBackground(DnFColor.infoBackground);
		itemInfo.setBackgroundMode(SWT.INHERIT_FORCE);
		GridData leftData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		leftData.widthHint=InterfaceSize.MONSTER_INFO_SIZE-10;
		
		Label name = new Label(itemInfo, SWT.WRAP);
		name.setText(monster.getName());
		name.setLayoutData(leftData);
		name.setForeground(DnFColor.DARK_YELLOW);
		
		String[] statList = new String[]{
				"레벨", "타입", "체력", "", "물리방어력", "마법방어력", "화속저", "수속저", "명속저", "암속저", "", "카운터", "백어택", "난이도", "방깍제한"
			};
		
		for(String stat : statList)
			setMonsterText(itemInfo, monster, stat, DnFColor.infoStat, InterfaceSize.MONSTER_INFO_SIZE-10);
		
		for(String str : monster.explanation){
			Label exp = new Label(itemInfo, SWT.WRAP);
			exp.setText(str);
			leftData = new GridData();
			leftData.widthHint=InterfaceSize.SET_INFO_SIZE-10;
			exp.setLayoutData(leftData);
			exp.setForeground(DnFColor.infoExplanation);
		}
	}
	
	public static void setMonsterOptionInfoComposite(Composite itemInfo, MonsterOption option, boolean transparentBackground)
	{
		if(transparentBackground) itemInfo.setBackground(DnFColor.infoBackground_transparent);
		else itemInfo.setBackground(DnFColor.infoBackground);
		itemInfo.setBackgroundMode(SWT.INHERIT_FORCE);
		GridData leftData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		leftData.widthHint=InterfaceSize.MONSTER_INFO_SIZE-10;
		
		Label name = new Label(itemInfo, SWT.WRAP);
		name.setText(option.getName());
		name.setLayoutData(leftData);
		name.setForeground(DnFColor.INCHANT);
		
		for(StatusAndName s : option.monster.getAdditionalStatList().statList)
			try {
				setText(itemInfo, s, DnFColor.infoStat, InterfaceSize.MONSTER_INFO_SIZE);
			} catch (StatusTypeMismatch e) {
				e.printStackTrace();
			}
		
		if(!option.explanation.isEmpty()){
			Label stat = new Label(itemInfo, SWT.WRAP);
			stat.setText("");
		}
		for(String str : option.explanation){
			Label exp = new Label(itemInfo, SWT.WRAP);
			exp.setText(str);
			leftData = new GridData();
			leftData.widthHint=InterfaceSize.SET_INFO_SIZE-10;
			exp.setLayoutData(leftData);
			exp.setForeground(DnFColor.infoStat);
		}
	}
	
	private static void setMonsterText(Composite itemInfo, Monster monster, String name, Color textColor, int xSize)
	{
		String strength="";
		Label label;
		
		try{
			strength = String.format("%.1f", monster.getDoubleStat(name));
			if(strength.contains(".0"))
				strength=strength.substring(0, strength.length()-2);
		}
		catch(StatusTypeMismatch e)
		{
			try{
				boolean enabled = monster.getBool(name);
				if(enabled) strength = "O";
				else strength = "X";
			}catch(StatusTypeMismatch e2) {
				e2.printStackTrace();
			}catch(UndefinedStatusKey e3) {}
		}
		catch(UndefinedStatusKey e) {}
		
		label = new Label(itemInfo, SWT.WRAP);
		GridData gridData = new GridData();
		gridData.widthHint=xSize;
		label.setLayoutData(gridData); 
			
	
		if(name.equals("")) label.setText("");
		else if(name.equals("타입")){
			if(strength.equals("50")) label.setText(name+" : 보스");
			else if(strength.equals("75")) label.setText(name+" : 네임드");
			else label.setText(name+" : 일반");
		}
		else label.setText(name+" : "+strength);

		label.setForeground(textColor);
	}
	
	
	private static void setStatusText(Item item, Composite itemInfo, Color color, int xSize) throws StatusTypeMismatch
	{
		setStatusText(0, item, itemInfo, color, xSize);
	}
	
	private static void setStatusText(int startIndex, Item item, Composite itemInfo, Color color, int xSize) throws StatusTypeMismatch
	{
		for(StatusAndName s : item.vStat.statList.subList(startIndex, item.vStat.statList.size())){
			setText(itemInfo, s, color, xSize);
		}
		
		if(!item.dStat.statList.isEmpty() || !item.fStat.statList.isEmpty())
		{
			Label stat = new Label(itemInfo, SWT.WRAP);
			stat.setText("\n――――――던전 입장 시 적용――――――\n\n");
			stat.setForeground(DnFColor.infoExplanation);
			for(StatusAndName s : item.dStat.statList){
				setText(itemInfo, s, color, xSize);
			}
		}
	}
	
	private static void setText(Composite itemInfo, StatusAndName s, Color textColor, int xSize) throws StatusTypeMismatch
	{
		setText(itemInfo, s, true, textColor, xSize);
	}
	
	private static void setText(Composite itemInfo, StatusAndName s, boolean enable, Color textColor, int xSize) throws StatusTypeMismatch
	{
		String strength;
		Label stat;
		
		strength = String.format("%.1f", s.stat.getStatToDouble());
		if(s.stat instanceof ElementInfo && strength.equals("0.0"));
		else if(s.stat instanceof SkillStatusInfo && strength.equals("0.0"));
		else{
			if(strength.contains(".0"))
				strength=strength.substring(0, strength.length()-2);
			
			stat = new Label(itemInfo, SWT.WRAP);
			GridData gridData = new GridData();
			gridData.widthHint=xSize;
			stat.setLayoutData(gridData);
				
			String name = StatusAndName.getStatHash().get(s.name);
			if(s.name==StatList.SKILL || s.name==StatList.SKILL_RANGE)
				name = s.stat.getStatToString()+name;
			
			if(strength.contains("-")){
				if(name.contains("+")) stat.setText(name.substring(0, name.length()-1)+strength);
				else if(name.contains("-")) stat.setText(name.substring(0, name.length()-1)+"+"+strength.substring(1, strength.length()));
				else stat.setText(name+strength);
			}
			else stat.setText(name+strength);
			if(!enable || !s.enabled) stat.setForeground(DnFColor.DISABLED);
			else stat.setForeground(textColor);
			if(!s.enabled)
				stat.setText(stat.getText()+"(옵션 꺼짐)");
			
			if(s.stat instanceof SkillRangeStatusInfo && ((SkillRangeStatusInfo)s.stat).getTP())
			{
				stat.setText("TP - "+stat.getText());
			}
		}
		
		if(s.stat instanceof ElementInfo && ((ElementInfo)s.stat).getElementEnabled()==true)
		{
			stat = new Label(itemInfo, SWT.WRAP);
			GridData gridData = new GridData();
			gridData.widthHint=xSize;
			stat.setLayoutData(gridData);
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
			if(!enable || !s.enabled) stat.setForeground(DnFColor.DISABLED);
			else stat.setForeground(textColor);
			if(!s.enabled)
				stat.setText(stat.getText()+"(옵션 꺼짐)");
		}
		
		else if(s.stat instanceof SkillStatusInfo && ((SkillStatusInfo)s.stat).getIncrease()>1.0005)
		{
			stat = new Label(itemInfo, SWT.WRAP);
			GridData gridData = new GridData();
			gridData.widthHint=xSize;
			stat.setLayoutData(gridData);
			stat.setText(s.stat.getStatToString()+" 데미지 증가 + "+ String.format("%.1f", ((SkillStatusInfo)s.stat).getIncrease()));		
			if(!enable || !s.enabled) stat.setForeground(DnFColor.DISABLED);
			else stat.setForeground(textColor);
			if(!s.enabled)
				stat.setText(stat.getText()+"(옵션 꺼짐)");
		}
	}
}
