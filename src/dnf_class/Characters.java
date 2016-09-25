package dnf_class;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.Avatar_part;
import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_calculator.AbstractStatusInfo;
import dnf_calculator.SkillRangeStatusInfo;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.Status;
import dnf_calculator.StatusAndName;
import dnf_infomation.CharacterDictionary;
import dnf_infomation.GetDictionary;
import dnf_infomation.ItemDictionary;

public class Characters implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2820101776203426270L;
	public Status villageStatus;														//마을스탯
	public Status dungeonStatus;														//인던스탯
	
	private LinkedList<Skill> skillList;
	private Setting itemSetting;
	private HashMap<SetName, Integer> setOptionList;
	public LinkedList<Consumeable> doping;
	private int level;
	private final JobList job;
	public final String name;
	private final Character_type characterType;
	
	public CharacterDictionary characterInfoList;
	public ItemDictionary userItemList;
	
	public boolean autoOptimize;
	private int autoOptimizeMode;
	private Item_rarity autoOptimizeRarity;
	
	String charImageAddress;
	
	public Monster target;

	public Characters(int level, JobList job, String name)
	{
		this.name=name;
		characterInfoList = (CharacterDictionary) GetDictionary.charDictionary.clone();
		skillList = characterInfoList.getSkillList(job, level);
		
		itemSetting = new Setting();
		setOptionList = new HashMap<SetName, Integer>();						//key : 셋옵 이름, value : 셋옵 보유 개수
	
		doping = new LinkedList<Consumeable>();
		this.setLevel(level);
		
		this.job = job;
		characterType = job.charType;
		
		villageStatus = new Status();
		dungeonStatus = new Status();
		
		userItemList = (ItemDictionary) GetDictionary.itemDictionary.clone();
		autoOptimize = false;
		autoOptimizeRarity = Item_rarity.NONE;
		autoOptimizeMode = 0;
		
		target = null;

		setStatus();
	}
	
	private void initStatus()
	{
		try {
			villageStatus = new Status(job, level);
			dungeonStatus = new Status(job, level);
		} catch (ItemNotFoundedException e) {
			e.printStackTrace();
		}
		for(Skill s : skillList)
		{
			s.dungeonLevel=0;
			s.villageLevel=0;
			try {
				s.dungeonIncrease=(100+dungeonStatus.getStat(StatList.DAM_SKILL))/100.0;
				s.villageIncrease=(100+villageStatus.getStat(StatList.DAM_SKILL))/100.0;
			} catch (StatusTypeMismatch e) {
				e.printStackTrace();
			}
			
		}
	}

	private void addSet(Item item)
	{
		if(item.getSetName()!=SetName.NONE){								//세트아이템
			if(setOptionList.containsKey(item.getSetName()))				
				setOptionList.replace(item.getSetName(), setOptionList.get(item.getSetName())+1);		//이미 등록된 셋옵 -> 1 추가
			else setOptionList.put(item.getSetName(), 1);												//셋옵 등록
		}
	}
	
	private void subtractSet(Item item)
	{
		if(item.getSetName()!=SetName.NONE){								//세트아이템
			if(setOptionList.get(item.getSetName())==1)
				setOptionList.remove(item.getSetName());
			else 
				setOptionList.replace(item.getSetName(), setOptionList.get(item.getSetName())-1);		//이미 등록된 셋옵 -> 1 감소
		}
	}
	
	public void equip(Item item)
	{
		double crt_before=0, crt_after=0;
		try {
			crt_before = dungeonStatus.getStat("물크");
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
		
		if(item instanceof Weapon){
			Weapon temp = (Weapon)item;
			subtractSet(itemSetting.weapon);
		
			if(temp.enabled(job)) itemSetting.weapon=temp;
			else System.out.println("장착불가 - "+item.getName());
			
			addSet(temp);
		}
		
		else if(item instanceof Equipment){
			Equipment equipment = (Equipment)item; 
			
			subtractSet(itemSetting.equipmentList.get(equipment.part));
			itemSetting.equipmentList.replace(equipment.part, equipment);
			addSet(equipment);
		}
		else if(item instanceof Avatar){
			Avatar avatar = (Avatar)item; 
			Avatar_part part = avatar.part;
			subtractSet(itemSetting.avatarList.get(avatar.part));
			itemSetting.avatarList.replace(part, avatar);
			
			addSet(avatar);
		}
		else if(item instanceof Consumeable){
			Consumeable consume = (Consumeable)item; 
			doping.add(consume);
		}
		else if(item instanceof Creature) itemSetting.creature = (Creature)item;
		else if(item instanceof Title) itemSetting.title = (Title)item;
		
		setStatus();
		
		try {
			crt_after = dungeonStatus.getStat("물크");
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
		
		if(autoOptimize && crt_before!=crt_after) optimizeEmblem(autoOptimizeMode, autoOptimizeRarity);
	}
	
	public boolean unequip(Item item)
	{
		boolean success=false;
		double crt_before=0, crt_after=0;
		try {
			crt_before = dungeonStatus.getStat("물크");
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
		
		if(item instanceof Weapon){
			Weapon weapon = (Weapon)item;
			
			if(weapon.getName().equals(itemSetting.weapon.getName())){
				itemSetting.weapon = new Weapon();
				subtractSet(weapon);
				success = true;
			}
		}
		
		else if(item instanceof Equipment)
		{
			Equipment equipment = (Equipment)item; 
			Equip_part part = equipment.part;
			
			if(itemSetting.equipmentList.get(part).getName().equals(equipment.getName())){
				itemSetting.equipmentList.replace(part, new Equipment(part));
				subtractSet(equipment);
				success = true;
			}
		}
		else if(item instanceof Avatar){
			Avatar avatar = (Avatar)item; 
			Avatar_part part = avatar.part;
			
			if(itemSetting.avatarList.get(part).getName().equals(avatar.getName())){
				itemSetting.avatarList.replace(part, new Avatar(part));
				subtractSet(avatar);
				success = true;
			}
		}
		else if(item instanceof Consumeable){
			Consumeable consume = (Consumeable)item; 
			success = doping.remove(consume);
		}
		else if(item instanceof Creature){
			if(item.getName().equals(itemSetting.creature.getName())){
				itemSetting.creature = new Creature();
				success = true;
			}
		}
		else if(item instanceof Title){
			if(item.getName().equals(itemSetting.title.getName())){
				itemSetting.title = new Title();
				success = true;
			}
		}
		
		setStatus();
		
		try {
			crt_after = dungeonStatus.getStat("물크");
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
		
		if(autoOptimize && crt_before!=crt_after) optimizeEmblem(autoOptimizeMode, autoOptimizeRarity);
		
		return success;
	}
	
	private void itemStatUpdate(Item item)
	{
		item.vStat.addListToStat(villageStatus);
		
		item.vStat.addListToStat(dungeonStatus);
		item.dStat.addListToStat(dungeonStatus);
		
		if(item.getCard()!=null)
			itemStatUpdate(item.getCard());
		else if(item.getEmblem()!=null)
		{
			for(Emblem e : item.getEmblem())
				itemStatUpdate(e);
		}
			
	}
	
	private void itemStatUpdate(SetOption setOpion)
	{
		setOpion.vStat.addListToStat(villageStatus);
		
		setOpion.vStat.addListToStat(dungeonStatus);
		setOpion.dStat.addListToStat(dungeonStatus);
	}
	
	public void setStatus()													// 순서 - 아이템장착 ->버프스킬->스탯
	{
		initStatus();
		
		itemStatUpdate(itemSetting.weapon);
		
		for(Equipment e : itemSetting.equipmentList.values())
			itemStatUpdate(e);														//equipmentList(장비목록)에 포함된 모든 장비 스탯 더하기
		
		for(Avatar a : itemSetting.avatarList.values()) itemStatUpdate(a);
		
		for(Entry<SetName,Integer> e : setOptionList.entrySet())				//setOptionList(셋옵목록)에 포함된 모든 셋옵 e에 대해
		{
			try {
				LinkedList<SetOption> candidates = userItemList.getSetOptions(e.getKey());		//e에 해당되는 셋옵 목록 - candidates
				for(SetOption s : candidates)
				{
					if(s.isEnabled(e.getValue())) itemStatUpdate(s);									//셋옵에 요구되는 장착수를 넘었을 때 셋옵 스탯 더하기
				}
			} 
			catch (ItemFileNotFounded e1) {
				e1.printStackTrace();
			}
			
		}
		
		itemStatUpdate(itemSetting.title);
		itemStatUpdate(itemSetting.creature);
		
		itemSetting.weapon.fStat.addListToStat(dungeonStatus, this, target, itemSetting.weapon);
		for(Equipment e : itemSetting.equipmentList.values())
			e.fStat.addListToStat(dungeonStatus, this, target, e);
		for(Entry<SetName,Integer> e : setOptionList.entrySet())				//setOptionList(셋옵목록)에 포함된 모든 셋옵 e에 대해
		{
			try {
				LinkedList<SetOption> candidates = userItemList.getSetOptions(e.getKey());		//e에 해당되는 셋옵 목록 - candidates
				for(SetOption s : candidates)
				{
					if(s.isEnabled(e.getValue())) s.fStat.addListToStat(dungeonStatus, this, target, null);
				}
			} 
			catch (ItemFileNotFounded e1) {
				e1.printStackTrace();
			}
			
		}
		//TODO 도핑, 휘장
		
		setSkillLevel();
	}
	
	private void setSkillLevel()
	{
		getSkillLevel(false, villageStatus.getSkillStatList());
		getSkillLevel(true, dungeonStatus.getSkillStatList());
		
		LinkedList<AbstractStatusInfo> list = new LinkedList<AbstractStatusInfo>();
		for(Skill skill : skillList){
			if(skill.hasBuff()){
				for(StatusAndName s : skill.getSkillLevelInfo(false).stat.statList)
					list.add(s.stat);
			}
		}
		getSkillLevel(false, list);
		
		for(Skill skill : skillList){
			if(skill.hasBuff()){
				for(StatusAndName s : skill.getSkillLevelInfo(true).stat.statList)
					list.add(s.stat);
			}
		}
		getSkillLevel(true, list);
		
		for(Skill skill : skillList){
			if(skill.type==Skill_type.PASSIVE && skill.getBuffEnabled())
				skill.getSkillLevelInfo(false).stat.addListToStat(villageStatus);
			if(skill.hasBuff() && skill.getBuffEnabled())
				skill.getSkillLevelInfo(true).stat.addListToStat(dungeonStatus);
		}
	}
	
	private void getSkillLevel(boolean isDungeon, LinkedList<AbstractStatusInfo> list)
	{
		for(AbstractStatusInfo statInfo : list){
			try {
				if(statInfo instanceof SkillStatusInfo){
					String name = statInfo.getStatToString();
					for(Skill group : skillList){
						if(group.isEqualTo(name)){
							if(isDungeon){
								group.dungeonLevel += (int)statInfo.getStatToDouble();
								group.dungeonIncrease *=(100+((SkillStatusInfo)statInfo).getIncrease())/100.0;
							}
							else{
								group.villageLevel += (int)statInfo.getStatToDouble();
								group.villageIncrease *=(100+((SkillStatusInfo)statInfo).getIncrease())/100.0;
							}
							break;
						}
					}
				}
				
				else if(statInfo instanceof SkillRangeStatusInfo){
					String[] range = statInfo.getStatToString().split(" ~ ");
					boolean flag = false; 
					int endNum;
					if(range.length==2) endNum = Integer.parseInt(range[1]);
					else endNum = Integer.parseInt(range[0]);
					for(Skill group : skillList){
						if(group.isInRange(Integer.parseInt(range[0]), endNum, ((SkillRangeStatusInfo)statInfo).getTP())){
							flag = true;
							if(isDungeon) group.dungeonLevel += (int)statInfo.getStatToDouble();
							else group.villageLevel += (int)statInfo.getStatToDouble();
						}
						else if(!group.isInRange(Integer.parseInt(range[0]), endNum) && flag) break;
					}
				}
				
			}catch (StatusTypeMismatch e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setItemSettings(Setting itemSetting, boolean makeClone) {
		if(makeClone){
			try{
				String theType = "dnf_class.Item";
				Class<? extends Item> theClass;
				theClass = Class.forName(theType).asSubclass(Item.class);
				
				for(Item item : itemSetting.getWholeItemList()){
					Object clone = item.clone();
					Item castedItem = theClass.cast(clone);
					equip(castedItem);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else{
			for(Item item : itemSetting.getWholeItemList())
				equip(item);
		}
	}
	
	public void optimizeEmblem(int mode, Item_rarity rarity)
	{
		double crt, redGreenCrt, greenCrt;
		String name;
		Emblem stat, critical, dual=new Emblem();
		
		switch(rarity)
		{
		case UNIQUE:
			redGreenCrt=1.5;
			greenCrt=3;
			name="찬란한 ";
			break;
		case RARE:
			redGreenCrt=1.1;
			greenCrt=2.2;
			name="화려한 ";
			break;
		case UNCOMMON:
			redGreenCrt=0;
			greenCrt=1.2;
			name="빛나는 ";
			break;
		default:
			redGreenCrt=0;
			greenCrt=0;
			name="error - emblem rarity";
			break;
		}
		
		try{
			if(mode==0){
				crt = dungeonStatus.getStat("물크");
				stat=userItemList.getEmblem(name+"붉은빛 엠블렘[힘]");
				critical=userItemList.getEmblem(name+"녹색빛 엠블렘[물리크리티컬]");
				if(rarity!=Item_rarity.UNCOMMON) dual=userItemList.getEmblem(name+"듀얼 엠블렘[힘 + 물리크리티컬]");
			}
			else{
				crt = dungeonStatus.getStat("마크");
				stat=userItemList.getEmblem(name+"붉은빛 엠블렘[지능]");
				critical=userItemList.getEmblem(name+"녹색빛 엠블렘[마법크리티컬]");
				if(rarity!=Item_rarity.UNCOMMON) dual=userItemList.getEmblem(name+"듀얼 엠블렘[지능 + 마법크리티컬]");
			}
			crt-=getEmblemCRT(mode);
			
			Avatar avatar;
			//힘엠블렘이 듀얼/물크 엠블렘보다 효율이 낮음
			//힘엠블렘을 장착가능한 부위부터 물크엠블렘로 변환 ->힘엠블렘 장착이 불가능한 상하의는 가장 나중에
			Avatar_part[] order = {Avatar_part.CAP, Avatar_part.HAIR, Avatar_part.AURA, Avatar_part.SKIN, Avatar_part.COAT, Avatar_part.PANTS};
			
			for(int i=0; i<order.length; i++)
			{
				if(itemSetting.avatarList.get(order[i]).getName().contains("없음")) continue;
				else avatar = userItemList.getAvatar(itemSetting.avatarList.get(order[i]).getName());
				
				//첫번째 엠블렘
				if(crt>=97-4*redGreenCrt) {															// 크리 초과시 (상하의로 최소 4*듀얼물크만큼은 크리가 오름)
					if((order[i]==Avatar_part.COAT || order[i]==Avatar_part.PANTS) && rarity!=Item_rarity.UNCOMMON){
						avatar.setEmblem1(dual);																		// 상의, 하의 - 힘물크 듀얼
						crt+=redGreenCrt;
					}
					else avatar.setEmblem1(stat);																		// 나머지 - 힘엠블렘
				}
				else if(crt>=97-5*redGreenCrt){																			// 듀얼크리만큼 크리 미달시
					avatar.setEmblem1(dual);
					crt+=redGreenCrt;
				}
				else{																									// 크리 미달시
					if((order[i]==Avatar_part.CAP || order[i]==Avatar_part.HAIR) && rarity!=Item_rarity.UNCOMMON){
						avatar.setEmblem1(dual);																		// 머리, 모자 - 힘물크 듀얼
						crt+=redGreenCrt;
					}
					else{
						avatar.setEmblem1(critical);																	// 나머지 - 크리엠블렘
						crt+=greenCrt;
					}
				}
				
				//두번째 엠블렘
				if(crt>=97-4*redGreenCrt) {																				// 크리 초과시
					if((order[i]==Avatar_part.COAT || order[i]==Avatar_part.PANTS) && rarity!=Item_rarity.UNCOMMON){
						avatar.setEmblem2(dual);																		// 상의, 하의 - 힘물크 듀얼
						crt+=redGreenCrt;
					}
					else avatar.setEmblem2(stat);																		// 나머지 - 힘엠블렘
				}
				else if(crt>=97-5*redGreenCrt){																			// 듀얼크리만큼 크리 미달시
					avatar.setEmblem2(dual);
					crt+=redGreenCrt;
				}
				else{																									// 크리 미달시
					if((order[i]==Avatar_part.CAP || order[i]==Avatar_part.HAIR) && rarity!=Item_rarity.UNCOMMON){
						avatar.setEmblem2(dual);																		// 머리, 모자 - 힘물크 듀얼
						crt+=redGreenCrt;
					}
					else{
						avatar.setEmblem2(critical);																	// 나머지 - 크리엠블렘
						crt+=greenCrt;
					}
				}
			}
			
		}catch(StatusTypeMismatch | UndefinedStatusKey | ItemFileNotFounded e) {
			e.printStackTrace();
		}
	
	}
	
	public double getEmblemCRT(int mode)
	{
		double temp=0;
		for(Avatar avatar : itemSetting.avatarList.values())
		{
			for(Emblem emblem : avatar.getEmblem())
			{
				if(mode==0) temp+=emblem.vStat.getStatSum(StatList.CRT_PHY);
				else temp+=emblem.vStat.getStatSum(StatList.CRT_MAG);
			}
		}
		return temp;
	}

	public int getLevel() { return level; }
	public void setLevel(int level) { this.level = level; }	
	public JobList getJob() { return job; }
	public Character_type getCharType() { return characterType; }
	public String getCharImageAddress() {return charImageAddress;}
	public void setCharImageAddress(String charImageAddress) {this.charImageAddress = charImageAddress;}
	public HashMap<Equip_part, Equipment> getEquipmentList() {return itemSetting.equipmentList;}
	public HashMap<Avatar_part, Avatar> getAvatarList() {return itemSetting.avatarList;}
	public HashMap<SetName, Integer> getSetOptionList() {return setOptionList;}
	public Creature getCreature() {return itemSetting.creature;}
	public Title getTitle() {return itemSetting.title;}
	public LinkedList<Consumeable> getDoping() {return doping;}
	public Weapon getWeapon() {return itemSetting.weapon;}
	public void setWeapon(Weapon weapon) {this.itemSetting.weapon = weapon;}
	public void setCreature(Creature creature) {this.itemSetting.creature = creature;}
	public void setTitle(Title title) {this.itemSetting.title = title;}
	public Drape getDrape() {return itemSetting.drape;}
	public void setDrape(Drape drape) {this.itemSetting.drape = drape;}
	public String getSettingName() {return itemSetting.setting_name;}
	public Setting getItemSetting() { return itemSetting; }
	
	public void setSkillLevel(LinkedList<Skill> list)
	{
		Iterator<Skill> iter = list.iterator();
		for(Skill s : skillList)
		{
			s.setSkillLevel(iter.next().getSkillLevel());
		}
	}
	
	public LinkedList<Skill> getSkillList()
	{
		return skillList;
	}

	public void setAutoOptimizeMode(int autoOptimizeMode) {
		this.autoOptimizeMode = autoOptimizeMode;
	}

	public void setAutoOptimizeRarity(Item_rarity autoOptimizeRarity) {
		this.autoOptimizeRarity = autoOptimizeRarity;
	}
}
