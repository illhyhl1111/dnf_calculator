package dnf_class;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_UI_32.Options;
import dnf_calculator.AbstractStatusInfo;
import dnf_calculator.Calculator;
import dnf_calculator.FunctionStat;
import dnf_calculator.SkillRangeStatusInfo;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.Status;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_calculator.TrackableStatus;
import dnf_infomation.BriefCharacterInfo;
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
	
	private Setting itemSetting;
	private HashMap<SetName, Integer> setOptionList;
	public LinkedList<Buff> buffList;
	private int level;
	private final Job job;
	public final String name;
	private final Character_type characterType;
	
	public CharacterDictionary characterInfoList;
	public ItemDictionary userItemList;
	
	private Skill representSkill;
	private boolean hasContract;
	private boolean isBurning;
	
	public boolean autoOptimize;
	private int autoOptimizeMode;
	private Item_rarity autoOptimizeRarity;
	
	String charImageAddress;
	
	public Monster target;
	public String[] trainingRoomSeletion;
	
	public Options option;

	public Characters(BriefCharacterInfo info)
	{
		this.name=info.name;
		characterInfoList = GetDictionary.getNewCharDictionary(info.job, info.level);
		
		itemSetting = new Setting();
		setOptionList = new HashMap<SetName, Integer>();						//key : 셋옵 이름, value : 셋옵 보유 개수
	
		buffList = new LinkedList<Buff>();
		this.setLevel(info.level);
		
		this.job = info.job;
		characterType = job.charType;
		
		villageStatus = new Status();
		dungeonStatus = new Status();
		
		userItemList = GetDictionary.getNewItemDictionary(job);
		autoOptimize = false;
		autoOptimizeRarity = Item_rarity.NONE;
		autoOptimizeMode = 0;
		
		try {
			target = characterInfoList.getMonsterInfo("강화기");
		} catch (ItemNotFoundedException e) {
			target = null;
			e.printStackTrace();
		}
		
		representSkill = getDamageSkillList().getLast();
		setContract(true);
		setBurning(false);
		
		trainingRoomSeletion = new String[] { "몬스터 설정", "부가조건 설정", "파티원 설정", "부가조건 설정", "부가조건 설정", 
				"파티원 설정", "부가조건 설정", "부가조건 설정", "파티원 설정", "부가조건 설정", "부가조건 설정"};
		
		option = new Options();
		
		for(Buff buff : userItemList.buffList){
			if(buff.enabled) buffList.add(buff);
		}	
		setStatus();
	}
	
	private void initStatus(boolean trackStat, Skill representSkill)
	{
		try {
			villageStatus = new Status(job, level);
			if(trackStat) dungeonStatus = new TrackableStatus(job, level, representSkill);
			else dungeonStatus = new Status(job, level);
		} catch (ItemNotFoundedException e) {
			e.printStackTrace();
		}
		if(isBurning()){
			dungeonStatus.addStat(StatList.STR, new StatusInfo(200));
			dungeonStatus.addStat(StatList.INT, new StatusInfo(200));
		}
		for(Skill s : characterInfoList.skillList)
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
	
	public boolean changeSkillEnable(Skill skill)
	{
		skill.setBuffEnabled(!skill.getBuffEnabled());
		if(skill.skillInfo.getLast().stat.findStat(StatList.CRT_PHY)!=null || skill.skillInfo.getLast().stat.findStat(StatList.CRT_MAG)!=null){
			if(autoOptimize) optimizeEmblem(autoOptimizeMode, autoOptimizeRarity);
			return true;
		}
		return false;
	}
	
	public Item equip(Item item, boolean updateStat)
	{
		Item previous=null;
		double crt_before=0, crt_after=0;
		try {
			crt_before = dungeonStatus.getStat("물크");
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
		
		if(item instanceof Weapon){
			Weapon temp = (Weapon)item;
			subtractSet(itemSetting.weapon);
		
			if(temp.enabled(job)){
				previous = itemSetting.weapon;
				itemSetting.weapon=temp;
			}
			else System.out.println("장착불가 - "+item.getName());
			
			addSet(temp);
		}
		
		else if(item instanceof Equipment){
			Equipment equipment = (Equipment)item; 
			
			previous = itemSetting.equipmentList.get(equipment.part);
			subtractSet(previous);
			itemSetting.equipmentList.replace(equipment.part, equipment);
			addSet(equipment);
		}
		else if(item instanceof Avatar){
			Avatar avatar = (Avatar)item; 
			Equip_part part = avatar.part;
			previous = itemSetting.avatarList.get(avatar.part);
			subtractSet(previous);
			itemSetting.avatarList.replace(part, avatar);
			
			addSet(avatar);
		}
		else if(item instanceof Buff){
			Buff buff = (Buff)item; 
			this.buffList.add(buff);
			previous = new Item();
		}
		else if(item instanceof Creature){
			previous = itemSetting.creature;
			itemSetting.creature = (Creature)item;
		}
		else if(item instanceof Title){
			previous = itemSetting.title;
			itemSetting.title = (Title)item;
		}
		else if(item instanceof Drape){
			previous = itemSetting.drape;
			itemSetting.drape = (Drape)item;
		}
		
		if(updateStat){
			setStatus();
			try {
				crt_after = dungeonStatus.getStat("물크");
			} catch (StatusTypeMismatch | UndefinedStatusKey e) {
				e.printStackTrace();
			}
			
			if(autoOptimize && (crt_before!=crt_after || (item instanceof Avatar) )) optimizeEmblem(autoOptimizeMode, autoOptimizeRarity);
		}
		
		return previous;
	}
	public Item equip(Item item) {return equip(item, true);}
	
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
			Equip_part part = avatar.part;
			
			if(itemSetting.avatarList.get(part).getName().equals(avatar.getName())){
				itemSetting.avatarList.replace(part, new Avatar(part));
				subtractSet(avatar);
				success = true;
			}
		}
		else if(item instanceof Buff){
			Buff consume = (Buff)item; 
			success = buffList.remove(consume);
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
		else if(item instanceof Drape){
			if(item.getName().equals(itemSetting.drape.getName())){
				itemSetting.drape = new Drape();
				success = true;
			}
		}
		
		setStatus();
		
		try {
			crt_after = dungeonStatus.getStat("물크");
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
		
		if(autoOptimize && (crt_before!=crt_after || (item instanceof Avatar) )) optimizeEmblem(autoOptimizeMode, autoOptimizeRarity);
		
		return success;
	}
	
	private void itemStatUpdate(Item item)
	{
		item.vStat.addListToStat(villageStatus);
		
		item.vStat.addListToStat(dungeonStatus, item.getName());
		item.dStat.addListToStat(dungeonStatus, item.getName());
		
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
		
		setOpion.vStat.addListToStat(dungeonStatus, setOpion.getSetName().getName());
		setOpion.dStat.addListToStat(dungeonStatus, setOpion.getSetName().getName());
	}
	
	public void setStatus(){ trackStatus(false, representSkill); }
	
	public void trackStatus(boolean trackStat, Skill trackingSkill)					// 순서 - 아이템장착 ->버프스킬->스탯
	{
		initStatus(trackStat, trackingSkill);
		
		//item fStat
		itemSetting.weapon.fStat.addListToStat(dungeonStatus, this, target, itemSetting.weapon);
		for(Equipment e : itemSetting.equipmentList.values())
			e.fStat.addListToStat(dungeonStatus, this, target, e);
		for(Buff buff : buffList)
			if(buff.enabled) buff.fStat.addListToStat(dungeonStatus, this, target, buff);
		
		for(Entry<SetName,Integer> e : setOptionList.entrySet())				//setOptionList(셋옵목록)에 포함된 모든 셋옵 e에 대해
		{
			try {
				LinkedList<SetOption> candidates = userItemList.getSetOptions(e.getKey());		//e에 해당되는 셋옵 목록 - candidates
				for(SetOption s : candidates)
				{
					if(s.isEnabled(e.getValue())) s.fStat.addListToStat(dungeonStatus, this, target, s);
				}
			} 
			catch (ItemNotFoundedException e1) {
				e1.printStackTrace();
			}
			
		}
		itemSetting.drape.fStat.addListToStat(dungeonStatus, this, target, itemSetting.drape);
		
		//item stat
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
			catch (ItemNotFoundedException e1) {
				e1.printStackTrace();
			}
			
		}
		
		itemStatUpdate(itemSetting.title);
		itemStatUpdate(itemSetting.creature);
		itemStatUpdate(itemSetting.drape);
		for(Buff buff : buffList)
			if(buff.enabled) itemStatUpdate(buff);
		
		target.getAdditionalStatList().addListToStat(dungeonStatus, target.getName());
		
		//skill - fStat, set level
		setSkillLevel(trackingSkill);
		
		//skill - stat
		for(Skill skill : characterInfoList.skillList){
			if(skill.buffEnabled(false)){
				SkillLevelInfo skillInfo = skill.getSkillLevelInfo(false, isBurning);
				skillInfo.stat.addListToStat(villageStatus);
				//if(!skillInfo.fStat.statList.isEmpty()) skillInfo.fStat.addListToStat(villageStatus, this, target, skill);
			}
			if(skill.buffEnabled(true)){
				SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true, isBurning);
				skillInfo.stat.addListToStat(dungeonStatus, skill.getName());
				//if(!skillInfo.fStat.statList.isEmpty()) skillInfo.fStat.addListToStat(dungeonStatus, this, target, skill);
			}
			//else if(skill.isOptionSkill()) skill.getSkillLevelInfo(true, isBurning).fStat.addListToStat(dungeonStatus, this, target, skill);
		}
	}
	
	//1. 아이템으로부터 스킬 레벨을 구한다.
	//2. 함수스탯을 실행시키고, 버프 스킬로부터 최종 스킬수치를 구한다.
	//3. 최종 수치를 스탯에 더한다.(이 함수 리턴 이후)
	//fStat을 포함한 스킬 A는, 스킬 A를 변경시키는 스킬 B의 영향을 제대로 받지 않는다.  
	private void setSkillLevel(Skill trackingSkill) 
	{
		//1.
		getSkillLevel(false, villageStatus.getSkillStatList());
		getSkillLevel(true, dungeonStatus.getSkillStatList());
		
		//마을스탯
		LinkedList<AbstractStatusInfo> list;
		for(Skill skill : characterInfoList.skillList){
			if(skill.buffEnabled(false)){
				list = new LinkedList<AbstractStatusInfo>();
				SkillLevelInfo skillInfo = skill.getSkillLevelInfo(false, isBurning);
				for(StatusAndName s : skillInfo.stat.statList)
					list.add(s.stat);
				getSkillLevel(false, list);
			}
		}
		
		//던전
		for(Skill skill : characterInfoList.skillList){
			if(skill.buffEnabled(true)){
				list = new LinkedList<AbstractStatusInfo>();
				SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true, isBurning);
				for(StatusAndName s : skillInfo.stat.statList)
					list.add(s.stat);
				
				for(String str : skillInfo.fStat.statList){
					FunctionStat fStat = FunctionStat.getFunction(str);
					String[] args = FunctionStat.parseArgs(str);
					for(StatusAndName s : fStat.function(this, target, skill, args).statList){
						list.add(s.stat);
						dungeonStatus.trackStat(s.name, s.stat, skill.getName());
					}
				}
				getSkillLevel(true, list);
			}
			else if(skill.isOptionSkill()){
				list = new LinkedList<AbstractStatusInfo>();
				SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true, isBurning);
				for(String str : skillInfo.fStat.statList){
					FunctionStat fStat = FunctionStat.getFunction(str);
					String[] args = FunctionStat.parseArgs(str);
					for(StatusAndName s : fStat.function(this, target, skill, args).statList){
						list.add(s.stat);
						dungeonStatus.trackStat(s.name, s.stat, skill.getName());
					}
				}
				getSkillLevel(true, list);
			}
		}
	}
	
	private void getSkillLevel(boolean isDungeon, LinkedList<AbstractStatusInfo> list)
	{
		for(AbstractStatusInfo statInfo : list){
			try {
				if(statInfo instanceof SkillStatusInfo){
					String name = statInfo.getStatToString();
					for(Skill group : characterInfoList.skillList){
						if(group.isEqualTo(name)){
							if(isDungeon){
								group.dungeonLevel += (int)statInfo.getStatToDouble();
								//if(!group.isOptionSkill())
								group.dungeonIncrease *=(100+((SkillStatusInfo)statInfo).getIncrease())/100.0;
							}
							else{
								group.villageLevel += (int)statInfo.getStatToDouble();
								//if(!group.isOptionSkill())
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
					for(Skill group : characterInfoList.skillList){
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
					equip(castedItem, false);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else{
			for(Item item : itemSetting.getWholeItemList())
				equip(item, false);
		}
		
		setStatus();
		if(autoOptimize) optimizeEmblem(autoOptimizeMode, autoOptimizeRarity);
	}
	
	public LinkedList<Skill> getBuffSkillList()
	{
		LinkedList<Skill> list = new LinkedList<Skill>();
		for(Skill skill : characterInfoList.skillList)
		{
			if(skill.isEnableable() && (skill.isOptionSkill() || skill.getActiveEnabled())){
				list.add(skill);
			}
		}
		return list;
	}
	
	public LinkedList<Skill> getDamageSkillList()
	{
		LinkedList<Skill> list = new LinkedList<Skill>();
		for(Skill skill : characterInfoList.skillList)
		{
			if(skill.hasDamage() && skill.getActiveEnabled() && skill.type!=Skill_type.OPTION
					&& !skill.isSubSkill() && skill.type!=Skill_type.ACTIVE_NOMARK){
				list.add(skill);
			}
		}
		return list;
	}
	
	public LinkedList<Skill> getRepresentableSkillList()
	{
		LinkedList<Skill> list = new LinkedList<Skill>();
		for(Skill skill : characterInfoList.skillList)
		{
			if(skill.hasDamage() && !skill.isOptionSkill() && !skill.isSubSkill() && skill.type!=Skill_type.ACTIVE_NOMARK){
				list.add(skill);
			}
		}
		return list;
	}
	
	public LinkedList<Skill> getTrackingSkillList()
	{
		LinkedList<Skill> list = new LinkedList<Skill>();
		for(Skill skill : characterInfoList.skillList)
			if(skill.hasDamage()){
				list.add(skill);
			}
		return list;
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
				for(Buff buff : buffList){
					if(!buff.enabled) continue;
					crt-=buff.vStat.getStatSum(StatList.CRT_PHY);
					crt-=buff.dStat.getStatSum(StatList.CRT_PHY);
				}
				stat=userItemList.getEmblem(name+"붉은빛 엠블렘[힘]");
				critical=userItemList.getEmblem(name+"녹색빛 엠블렘[물리크리티컬]");
				if(rarity!=Item_rarity.UNCOMMON) dual=userItemList.getEmblem(name+"듀얼 엠블렘[힘 + 물리크리티컬]");
			}
			else{
				crt = dungeonStatus.getStat("마크");
				for(Buff buff : buffList){
					if(!buff.enabled) continue;
					crt-=buff.vStat.getStatSum(StatList.CRT_MAG);
					crt-=buff.dStat.getStatSum(StatList.CRT_MAG);
				}
				stat=userItemList.getEmblem(name+"붉은빛 엠블렘[지능]");
				critical=userItemList.getEmblem(name+"녹색빛 엠블렘[마법크리티컬]");
				if(rarity!=Item_rarity.UNCOMMON) dual=userItemList.getEmblem(name+"듀얼 엠블렘[지능 + 마법크리티컬]");
			}
			crt-=getEmblemCRT(mode);
			
			Avatar avatar;
			//힘엠블렘이 듀얼/물크 엠블렘보다 효율이 낮음
			Equip_part[] order = {Equip_part.ACAP, Equip_part.AHAIR, Equip_part.ACOAT, Equip_part.APANTS, Equip_part.AURA, Equip_part.ASKIN};
			
			if(rarity!=Item_rarity.UNCOMMON){
				//모든 부위에 우선 듀얼엠블렘 장착
				for(Equip_part part : order){
					avatar = itemSetting.avatarList.get(part);
					if(!avatar.getName().contains("없음")){
						avatar.setEmblem1(dual);
						avatar.setEmblem2(dual);
						crt+=2*redGreenCrt;
					}
				}
				
				if(Double.compare(crt, 97+redGreenCrt)>=0)	//초과
				{
					for(Equip_part part : order){
						avatar = itemSetting.avatarList.get(part);
						if(!avatar.getName().contains("없음")){
							if(avatar.setEmblem1(stat)) crt-=redGreenCrt;
							if(Double.compare(crt, 97+redGreenCrt)<0) break;
							
							if(avatar.setEmblem2(stat)) crt-=redGreenCrt;
							if(Double.compare(crt, 97+redGreenCrt)<0) break;
						}
					}
				}
				else if(Double.compare(crt, 97)<0)	//미달
				{
					for(Equip_part part : order){
						avatar = itemSetting.avatarList.get(part);
						if(!avatar.getName().contains("없음")){
							if(avatar.setEmblem1(critical)) crt+=greenCrt-redGreenCrt;
							if(Double.compare(crt, 97)>=0) break;
							
							if(avatar.setEmblem2(critical)) crt+=greenCrt-redGreenCrt;
							if(Double.compare(crt, 97)>=0) break;
						}
					}
				}
			}
			
			//빛작
			else{
				for(Equip_part part : order){
					avatar = itemSetting.avatarList.get(part);
					if(!avatar.getName().contains("없음")){
						if(!avatar.setEmblem1(stat)){
							avatar.setEmblem1(critical);
							crt+=greenCrt;
						}
						if(!avatar.setEmblem2(stat)){
							avatar.setEmblem2(critical);
							crt+=greenCrt;
						}
					}
				}
				
				if(Double.compare(crt, 97)<0)	//미달
				{
					for(Equip_part part : new Equip_part[] {Equip_part.AURA, Equip_part.ASKIN}){
						avatar = itemSetting.avatarList.get(part);
						if(!avatar.getName().contains("없음")){
							avatar.setEmblem1(critical);
							crt+=greenCrt;
							if(Double.compare(crt, 97)>=0) break;
							
							avatar.setEmblem2(critical);
							crt+=greenCrt;
							if(Double.compare(crt, 97)>=0) break;
						}
					}
				}
			}
			
			setStatus();
	
		}catch(StatusTypeMismatch | UndefinedStatusKey | ItemNotFoundedException e) {
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
	
	public String compareItem(Item item)
	{
		long damage_now = Calculator.getDamage(representSkill, target, this);
		
		Item previous = equip(item);
		if(previous==null) return " (착용불가)";
		else if(previous.getName().equals(item.getName())) return "";
		
		long damage_comp = Calculator.getDamage(representSkill, target, this);
		String result;
		if(Double.compare(damage_now, 0.0)==0) result = "-";
		else{
			double compare= ((double)(damage_comp-damage_now))/damage_now*100;
			if(Double.compare(compare, 0.0)>0) result = " (▲ "+ Double.parseDouble(String.format("%.2f", compare))+"%)";
			else if(Double.compare(compare, 0.0)<0) result = " (▼ "+ Double.parseDouble(String.format("%.2f", compare*-1))+"%)";
			else result = "";
		}
		equip(previous);
		return result;
	}
	
	public void updateDictionary(){
		if(!GetDictionary.itemDictionary.getVERSION().equals(userItemList.getVERSION())){
			userItemList.updateVersion(GetDictionary.itemDictionary, job);
			setStatus();
		}
		
		if(!GetDictionary.charDictionary.getVERSION().equals(characterInfoList.getVERSION())){ 
			characterInfoList=GetDictionary.getNewCharDictionary(job, level);
			trainingRoomSeletion = new String[] { "몬스터 설정", "부가조건 설정", "파티원 설정", "부가조건 설정", "부가조건 설정", 
					"파티원 설정", "부가조건 설정", "부가조건 설정", "파티원 설정", "부가조건 설정", "부가조건 설정"};
			setStatus();
		}
	}
	
	
	public int getLevel() { return level; }
	public void setLevel(int level) { this.level = level; }	
	public Job getJob() { return job; }
	public Character_type getCharType() { return characterType; }
	public String getCharImageAddress() {return charImageAddress;}
	public void setCharImageAddress(String charImageAddress) {this.charImageAddress = charImageAddress;}
	public HashMap<Equip_part, Equipment> getEquipmentList() {return itemSetting.equipmentList;}
	public HashMap<Equip_part, Avatar> getAvatarList() {return itemSetting.avatarList;}
	public HashMap<SetName, Integer> getSetOptionList() {return setOptionList;}
	public Creature getCreature() {return itemSetting.creature;}
	public Title getTitle() {return itemSetting.title;}
	public LinkedList<Buff> getDoping() {return buffList;}
	public Weapon getWeapon() {return itemSetting.weapon;}
	public void setWeapon(Weapon weapon) {this.itemSetting.weapon = weapon;}
	public void setCreature(Creature creature) {this.itemSetting.creature = creature;}
	public void setTitle(Title title) {this.itemSetting.title = title;}
	public Drape getDrape() {return itemSetting.drape;}
	public void setDrape(Drape drape) {this.itemSetting.drape = drape;}
	public String getSettingName() {return itemSetting.setting_name;}
	public Setting getItemSetting() { return itemSetting; }
	public Skill getRepresentSkill() {return representSkill;}
	public void setRepresentSkill(String skillName) {
		for(Skill skill : characterInfoList.skillList)
			if(skill.getItemName().equals(skillName)){
				representSkill=skill;
				return;
			}
	}
	
	public void setSkillLevel(LinkedList<Skill> list)
	{
		if(list.size()!=characterInfoList.skillList.size()) return; 
		Iterator<Skill> iter = list.iterator();
		for(Skill s : characterInfoList.skillList)
		{
			s.setSkillLevel(iter.next().getCharSkillLevel());
		}
	}
	
	public LinkedList<Skill> getSkillList()
	{
		return characterInfoList.skillList;
	}

	public void setAutoOptimizeMode(int autoOptimizeMode) {
		this.autoOptimizeMode = autoOptimizeMode;
	}
	public int getAutoOptimizeMode() {return autoOptimizeMode;}

	public void setAutoOptimizeRarity(Item_rarity autoOptimizeRarity) {
		this.autoOptimizeRarity = autoOptimizeRarity;
	}
	public Item_rarity getAutoOptimizeRarity() {return autoOptimizeRarity;}

	public boolean isBurning() {
		return isBurning;
	}

	public void setBurning(boolean isBurning) {
		this.isBurning = isBurning;
	}

	public boolean hasContract() {
		return hasContract;
	}

	public void setContract(boolean hasContract) {
		this.hasContract = hasContract;
	}
}
