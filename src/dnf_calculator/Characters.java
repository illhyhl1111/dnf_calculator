package dnf_calculator;
import java.util.LinkedList;
import java.util.HashMap;

class Characters
{
	Status villageStatus;														//마을스탯
	Status dungeonStatus;														//인던스탯
	HashMap<Equip_part, Equipment> equipmentList;
	static final int EQUIPNUM=11;
	HashMap<Avatar_part, Avatar> avatarList;
	static final int AVATARNUM=10;
	Creature creature;
	Title title;
	LinkedList<Consumeable> doping;
	int level;
	JobList job;
	//LinkedList<PassiveSkill> passive;											//TODO
	
	public Characters(int level)
	{
		equipmentList = new HashMap<Equip_part, Equipment>();					//key : 장비 부위, value : 장비. 장비와 장비부위의 type이 같은곳에 장착됨
		avatarList = new HashMap<Avatar_part, Avatar>();
		
		for(Equip_part v : Equip_part.values())									//장비목록과 장착장비 초기화
			equipmentList.put(v, new Equipment(v));
		for(Avatar_part v : Avatar_part.values())
			avatarList.put(v,  new Avatar(v));
		
		creature = new Creature();
		title = new Title();
		doping = new LinkedList<Consumeable>();
		this.level=level;
		job = JobList.UNIMPLEMENTED;
		//passive = new LinkedList<PassiveSkill>();								//TODO
		
		villageStatus = new Status();
		dungeonStatus = new Status();
	}
	
	public void equip(Item item)
	{
		if(item instanceof Equipment){
			Equipment equipment = (Equipment)item; 
			Equip_part part = equipment.part;
			equipmentList.replace(part, equipment);
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
		if(item instanceof Equipment)
		{
			Equipment equipment = (Equipment)item; 
			Equip_part part = equipment.part;
			equipmentList.replace(part, new Equipment(part));
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
	
	public void setStatus()
	{
		villageStatus = new Status();
		for(Equipment e : equipmentList.values())
			e.vStat.addListToStat(villageStatus);	
		try
		{
			dungeonStatus = (Status)villageStatus.clone();
			for(Equipment e : equipmentList.values())
				e.dStat.addListToStat(dungeonStatus);
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
	}
}

enum JobList
{
	UNIMPLEMENTED
}