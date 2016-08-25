package dnf_class;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.Avatar_part;
import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.SetName;
import dnf_calculator.Status;
import dnf_infomation.GetItemDictionary;

public class Characters
{
	public Status villageStatus;														//마을스탯
	public Status dungeonStatus;														//인던스탯
	private HashMap<Equip_part, Equipment> equipmentList;
	static final int EQUIPNUM=11;
	private Weapon weapon;
	private HashMap<Avatar_part, Avatar> avatarList;
	static final int AVATARNUM=10;
	
	private HashMap<SetName, Integer> setOptionList;
	private Creature creature;
	private Title title;
	private LinkedList<Consumeable> doping;
	private int level;
	private final JobList job;
	private final Character_type characterType;
	
	String charImageAddress;
	
	//LinkedList<PassiveSkill> passive;											//TODO

	public Characters(int level, JobList job)
	{
		equipmentList = new HashMap<Equip_part, Equipment>();					//key : 장비 부위, value : 장비. 장비와 장비부위의 type이 같은곳에 장착됨
		setOptionList = new HashMap<SetName, Integer>();						//key : 셋옵 이름, value : 셋옵 보유 개수
		avatarList = new HashMap<Avatar_part, Avatar>();
		
		for(Equip_part v : Equip_part.values())									//장비목록과 장착장비 초기화
			equipmentList.put(v, new Equipment(v));
		for(Avatar_part v : Avatar_part.values())
			avatarList.put(v,  new Avatar(v));
		
		weapon = new Weapon();
		creature = new Creature();
		title = new Title();
		doping = new LinkedList<Consumeable>();
		this.setLevel(level);
		
		this.job = job;
		characterType = job.charType;
		
		//passive = new LinkedList<PassiveSkill>();								//TODO
		
		villageStatus = new Status();
		dungeonStatus = new Status();
	}

	
	public void equip(Item item)
	{
		if(item instanceof Weapon){
			Weapon temp = (Weapon)item;
			if(temp.enabled(job)) weapon=temp;
			else System.out.println("장착불가");
		}
		
		else if(item instanceof Equipment){
			Equipment equipment = (Equipment)item; 
			Equip_part part = equipment.part;
			equipmentList.replace(part, equipment);
			
			if(equipment.setName!=SetName.NONE){								//세트아이템
				if(setOptionList.containsKey(equipment.setName))				
					setOptionList.replace(equipment.setName, setOptionList.get(equipment.setName)+1);		//이미 등록된 셋옵 -> 1 추가
				else setOptionList.put(equipment.setName, 1);												//셋옵 등록
			}
				
		}
		else if(item instanceof Avatar){
			Avatar avatar = (Avatar)item; 
			Avatar_part part = avatar.part;
			avatarList.replace(part, avatar);
		}
		else if(item instanceof Consumeable){
			Consumeable consume = (Consumeable)item; 
			doping.add(consume);
		}
		else if(item instanceof Creature) creature = (Creature)item;
		else if(item instanceof Title) title = (Title)item;
		
		setStatus();
	}
	
	public void unequip(Item item)
	{
		if(item instanceof Weapon){
			Weapon weapon = (Weapon)item;
			this.weapon = new Weapon();
			
			if(weapon.setName!=SetName.NONE){								//세트아이템
				if(setOptionList.get(weapon.setName)==1)
					setOptionList.remove(weapon.setName);
				else setOptionList.replace(weapon.setName, setOptionList.get(weapon.setName)-1);		//이미 등록된 셋옵 -> 1 감소
			}
		}
		
		else if(item instanceof Equipment)
		{
			Equipment equipment = (Equipment)item; 
			Equip_part part = equipment.part;
			equipmentList.replace(part, new Equipment(part));
			
			if(equipment.setName!=SetName.NONE){								//세트아이템
				if(setOptionList.get(equipment.setName)==1)
					setOptionList.remove(equipment.setName);
				else setOptionList.replace(equipment.setName, setOptionList.get(equipment.setName)-1);		//이미 등록된 셋옵 -> 1 감소
			}
		}
		else if(item instanceof Avatar){
			Avatar avatar = (Avatar)item; 
			Avatar_part part = avatar.part;
			avatarList.replace(part, new Avatar(part));
		}
		else if(item instanceof Consumeable){
			Consumeable consume = (Consumeable)item; 
			doping.remove(consume);
		}
		else if(item instanceof Creature) creature = new Creature();
		else if(item instanceof Title) title = new Title();
		
		setStatus();
	}
	
	private void statUpdate(Item item)
	{
		item.vStat.addListToStat(villageStatus);
		
		item.vStat.addListToStat(dungeonStatus);
		item.dStat.addListToStat(dungeonStatus);
	}
	
	public void setStatus()
	{
		villageStatus = new Status();
		dungeonStatus = new Status();
		
		statUpdate(weapon);
		
		for(Equipment e : equipmentList.values())
			statUpdate(e);														//equipmentList(장비목록)에 포함된 모든 장비 스탯 더하기
		
		for(Entry<SetName,Integer> e : setOptionList.entrySet())				//setOptionList(셋옵목록)에 포함된 모든 셋옵 e에 대해
		{
			try {
				LinkedList<SetOption> candidates = GetItemDictionary.getSetOptions(e.getKey());		//e에 해당되는 셋옵 목록 - candidates
				for(SetOption s : candidates)
				{
					if(s.isEnabled(e.getValue())) statUpdate(s);									//셋옵에 요구되는 장착수를 넘었을 때 셋옵 스탯 더하기
				}
			} 
			catch (ItemFileNotFounded | ItemFileNotReaded e1) {
				e1.printStackTrace();
			}
			
		}
		
		statUpdate(title);
		statUpdate(creature);
		
		//TODO 아바타, 도핑, 패시브스킬 장착
	}

	public int getLevel() { return level; }
	public void setLevel(int level) { this.level = level; }	
	public JobList getJob() { return job; }
	public Character_type getCharType() { return characterType; }
	public String getCharImageAddress() {return charImageAddress;}
	public void setCharImageAddress(String charImageAddress) {this.charImageAddress = charImageAddress;}
	public HashMap<Equip_part, Equipment> getEquipmentList() {return equipmentList;}
	public HashMap<Avatar_part, Avatar> getAvatarList() {return avatarList;}
	public HashMap<SetName, Integer> getSetOptionList() {return setOptionList;}
	public Creature getCreature() {return creature;}
	public Title getTitle() {return title;}
	public LinkedList<Consumeable> getDoping() {return doping;}
	public Weapon getWeapon() {return weapon;}
	public void setWeapon(Weapon weapon) {this.weapon = weapon;}
	public void setCreature(Creature creature) {this.creature = creature;}
	public void setTitle(Title title) {this.title = title;}
}
