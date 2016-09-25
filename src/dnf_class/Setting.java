package dnf_class;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.Avatar_part;
import dnf_InterfacesAndExceptions.Equip_part;

public class Setting implements java.io.Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -297704623308392100L;
	public HashMap<Equip_part, Equipment> equipmentList;
	static final int EQUIPNUM=11;
	public Weapon weapon;
	public HashMap<Avatar_part, Avatar> avatarList;
	static final int AVATARNUM=10;
	public Creature creature;
	public Title title;
	public Drape drape;
	
	public String setting_name;
	
	public Setting()
	{
		equipmentList = new HashMap<Equip_part, Equipment>();					//key : 장비 부위, value : 장비. 장비와 장비부위의 type이 같은곳에 장착됨
		avatarList = new HashMap<Avatar_part, Avatar>();
		
		for(Equip_part v : Equip_part.values())									//장비목록과 장착장비 초기화
			equipmentList.put(v, new Equipment(v));
		for(Avatar_part v : Avatar_part.values())
			avatarList.put(v,  new Avatar(v));
		
		weapon = new Weapon();
		creature = new Creature();
		drape = new Drape();
		title = new Title();
		
		setting_name="default";
	}
	
	public LinkedList<Item> getWholeItemList()
	{
		LinkedList<Item> list = new LinkedList<Item>();
		list.add(weapon);
		for(Item item : equipmentList.values())
			list.add(item);
		for(Item item : avatarList.values())
			list.add(item);
		list.add(creature);
		list.add(title);
		list.add(drape);
		
		return list;
	}
	
	private Item makeReplicate(Item item, String name)
	{
		Item i = (Item) item.clone();
		i.replicateNum=1;
		i.setName(i.getItemName() + "-복제 : "+name);
		i.setEnabled(true);
		
		int index=0;
		for(String s : i.explanation){
			if(s.contains("세팅 저장 당시의 아이템 정보입니다."))
				i.explanation.remove(index);
			index++;
		}
		i.explanation.add(name+" 세팅 저장 당시의 아이템 정보입니다.");
		return i;
	}
	
	public Setting makeClone(String name)
	{
		Setting replicate = new Setting();
		replicate.setting_name=name;
		
		replicate.creature = this.creature;
		replicate.drape = this.drape;
		
		for(Entry<Equip_part, Equipment> entry : equipmentList.entrySet())
			replicate.equipmentList.replace(entry.getKey(), (Equipment) makeReplicate(entry.getValue(), name));
		
		for(Entry<Avatar_part, Avatar> entry : avatarList.entrySet())
			replicate.avatarList.replace(entry.getKey(), (Avatar) makeReplicate(entry.getValue(), name));
		
		replicate.weapon=(Weapon) makeReplicate(weapon, name);
		replicate.title=(Title) makeReplicate(title, name);
			
		return replicate;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Setting)
			return ((Setting) o).setting_name.equals(setting_name);
		else return false;
	}
	
	@Override
	public Object clone()
	{
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
