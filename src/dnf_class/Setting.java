package dnf_class;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_infomation.GetDictionary;

public class Setting implements java.io.Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -297704623308392100L;
	public HashMap<Equip_part, Equipment> equipmentList;
	static final int EQUIPNUM=11;
	public Weapon weapon;
	public HashMap<Equip_part, Avatar> avatarList;
	static final int AVATARNUM=10;
	public Creature creature;
	public Title title;
	public Drape drape;
	public String setting_name;
	
	private static final HashMap<Equip_part, Equipment> magicalSealedEquip = new HashMap<Equip_part, Equipment>();
	private static final HashMap<Equip_part, Avatar> superiorAvatar = new HashMap<Equip_part, Avatar>();
	private static final HashMap<Weapon_detailType, Weapon> magicalSealedWeapon = new HashMap<Weapon_detailType, Weapon>();
	
	public Setting()
	{
		equipmentList = new HashMap<Equip_part, Equipment>();					//key : 장비 부위, value : 장비. 장비와 장비부위의 type이 같은곳에 장착됨
		avatarList = new HashMap<Equip_part, Avatar>();
		
		for(Equip_part v : Equip_part.equipValues())									//장비목록과 장착장비 초기화
			if(v!=Equip_part.TITLE && v!=Equip_part.WEAPON) equipmentList.put(v, new Equipment(v));
		for(Equip_part v : Equip_part.avatarValues())
			avatarList.put(v,  new Avatar(v));
		
		weapon = new Weapon();
		creature = new Creature();
		drape = new Drape();
		title = new Title();
		
		setting_name="default";
		
		initMagicalEquipment();
	}
	
	public static void initMagicalEquipment()
	{
		if(magicalSealedEquip.size()!=0) return;
		Item_rarity rare = Item_rarity.RARE;
		String version = CalculatorVersion.DEFAULT;

		Equipment temp = new Equipment("마봉상의", rare, Equip_part.ROBE, Equip_type.NONE, 90, false, version);
		temp.vStat.addStatList("힘", 40);
		temp.vStat.addStatList("지능", 40);
		magicalSealedEquip.put(Equip_part.ROBE, temp);
		temp = new Equipment("마봉하의", rare, Equip_part.TROUSER, Equip_type.NONE, 90, false, version);
		temp.vStat.addStatList("힘", 40);
		temp.vStat.addStatList("지능", 40);
		magicalSealedEquip.put(Equip_part.TROUSER, temp);
		temp = new Equipment("마봉어깨", rare, Equip_part.SHOULDER, Equip_type.NONE, 90, false, version);
		temp.vStat.addStatList("힘", 32);
		temp.vStat.addStatList("지능", 32);
		magicalSealedEquip.put(Equip_part.SHOULDER, temp);
		temp = new Equipment("마봉벨트", rare, Equip_part.BELT, Equip_type.NONE, 90, false, version);
		temp.vStat.addStatList("힘", 24);
		temp.vStat.addStatList("지능", 24);
		magicalSealedEquip.put(Equip_part.BELT, temp);
		temp = new Equipment("마봉신발", rare, Equip_part.SHOES, Equip_type.NONE, 90, false, version);
		temp.vStat.addStatList("힘", 24);
		temp.vStat.addStatList("지능", 24);
		magicalSealedEquip.put(Equip_part.SHOES, temp);
		
		temp = new Equipment("마봉반지", rare, Equip_part.RING, Equip_type.ACCESSORY, 90, false, version);
		temp.vStat.addStatList("힘", 57);
		temp.vStat.addStatList("지능", 57);
		temp.vStat.addStatList("물크", 2.7);
		temp.vStat.addStatList("마크", 2.7);
		magicalSealedEquip.put(Equip_part.RING, temp);
		temp = new Equipment("마봉팔찌", rare, Equip_part.BRACELET, Equip_type.ACCESSORY, 90, false, version);
		temp.vStat.addStatList("힘", 39);
		temp.vStat.addStatList("물크", 4.4);
		magicalSealedEquip.put(Equip_part.BRACELET, temp);
		temp = new Equipment("마봉목걸이", rare, Equip_part.NECKLACE, Equip_type.ACCESSORY, 90, false, version);
		temp.vStat.addStatList("지능", 39);
		temp.vStat.addStatList("마크", 4.4);
		magicalSealedEquip.put(Equip_part.NECKLACE, temp);
		
		temp = new Equipment("마봉보장", rare, Equip_part.AIDEQUIPMENT, Equip_type.SPECIALEQUIP, 90, false, version);
		temp.vStat.addStatList("힘", 37);
		temp.vStat.addStatList("지능", 37);
		temp.vStat.addStatList("물공", 33);
		temp.vStat.addStatList("마공", 33);
		temp.vStat.addStatList("독공", 47);
		magicalSealedEquip.put(Equip_part.AIDEQUIPMENT, temp);
		temp = new Equipment("마봉마법석", rare, Equip_part.MAGICSTONE, Equip_type.SPECIALEQUIP, 90, false, version);
		temp.vStat.addStatList("힘", 56);
		temp.vStat.addStatList("지능", 56);
		temp.vStat.addStatList(Element_type.FIRE, 0, true, false, false);
		magicalSealedEquip.put(Equip_part.MAGICSTONE, temp);
		temp = new Equipment("마봉귀걸이", rare, Equip_part.EARRING, Equip_type.SPECIALEQUIP, 90, false, version);
		temp.vStat.addStatList("힘", 56);
		temp.vStat.addStatList("지능", 56);
		temp.vStat.addStatList("모속강", 6);
		temp.vStat.addStatList("물크", 90);
		temp.vStat.addStatList("마크", 90);
		magicalSealedEquip.put(Equip_part.EARRING, temp);
		
		
		for(Avatar avatar : GetDictionary.itemDictionary.avatarList)
			if(avatar.setName==SetName.AVATAR)
				superiorAvatar.put(avatar.part, avatar);
		
		Weapon temp2 = new Weapon("마봉소검", rare, Weapon_detailType.SWORD_SHORTSWORD, 90, false, version);
		temp2.vStat.addStatList("물공", 882);
		temp2.vStat.addStatList("마공", 1068);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addStatList("마크", 2);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉도", rare, Weapon_detailType.SWORD_KATANA, 90, false, version);
		temp2.vStat.addStatList("물공", 882);
		temp2.vStat.addStatList("마공", 975);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("지능", 95);
		temp2.vStat.addStatList("물크", 2);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉광검", rare, Weapon_detailType.SWORD_LIGHTSWORD, 90, false, version);
		temp2.vStat.addStatList("물공", 864);
		temp2.vStat.addStatList("마공", 836);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉둔기", rare, Weapon_detailType.SWORD_CLUB, 90, false, version);
		temp2.vStat.addStatList("물공", 1021);
		temp2.vStat.addStatList("마공", 882);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 95);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉대검", rare, Weapon_detailType.SWORD_LONGSWORD, 90, false, version);
		temp2.vStat.addStatList("물공", 1114);
		temp2.vStat.addStatList("마공", 836);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		
		temp2 = new Weapon("마봉권글", rare, Weapon_detailType.FITGHTER_BOXGLOVE, 90, false, version);
		temp2.vStat.addStatList("물공", 975);
		temp2.vStat.addStatList("마공", 882);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("물크", 2);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉너클", rare, Weapon_detailType.FIGHTER_KNUCKLE, 90, false, version);
		temp2.vStat.addStatList("물공", 882);
		temp2.vStat.addStatList("마공", 1068);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉클로", rare, Weapon_detailType.FIGHTER_CLAW, 90, false, version);
		temp2.vStat.addStatList("물공", 928);
		temp2.vStat.addStatList("마공", 928);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("지능", 32);
		temp2.vStat.addStatList("물크", 3);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉통파", rare, Weapon_detailType.FIGHTER_TONFA, 90, false, version);
		temp2.vStat.addStatList("물공", 882);
		temp2.vStat.addStatList("마공", 928);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("지능", 95);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉건틀릿", rare, Weapon_detailType.FIGHTER_GAUNTLET, 90, false, version);
		temp2.vStat.addStatList("물공", 1114);
		temp2.vStat.addStatList("마공", 836);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 95);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		
		temp2 = new Weapon("마봉로드", rare, Weapon_detailType.MAGE_ROD, 90, false, version);
		temp2.vStat.addStatList("물공", 836);
		temp2.vStat.addStatList("마공", 1021);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 95);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉스탭", rare, Weapon_detailType.MAGE_STAFF, 90, false, version);
		temp2.vStat.addStatList("물공", 882);
		temp2.vStat.addStatList("마공", 1114);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉봉", rare, Weapon_detailType.MAGE_POLE, 90, false, version);
		temp2.vStat.addStatList("물공", 1003);
		temp2.vStat.addStatList("마공", 836);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉창", rare, Weapon_detailType.MAGE_SPEAR, 90, false, version);
		temp2.vStat.addStatList("물공", 1114);
		temp2.vStat.addStatList("마공", 789);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 95);
		temp2.vStat.addStatList("물크", 2);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉빗자루", rare, Weapon_detailType.MAGE_BROOM, 90, false, version);
		temp2.vStat.addStatList("물공", 928);
		temp2.vStat.addStatList("마공", 1021);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		
		temp2 = new Weapon("마봉낫", rare, Weapon_detailType.PRIEST_SCYTHE, 90, false, version);
		temp2.vStat.addStatList("물공", 882);
		temp2.vStat.addStatList("마공", 928);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addStatList("물크", 2);
		temp2.vStat.addStatList("마크", 2);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉토템", rare, Weapon_detailType.PRIEST_TOTEM, 90, false, version);
		temp2.vStat.addStatList("물공", 975);
		temp2.vStat.addStatList("마공", 836);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 95);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉배액", rare, Weapon_detailType.PRIEST_BATTLEAXE, 90, false, version);
		temp2.vStat.addStatList("물공", 1114);
		temp2.vStat.addStatList("마공", 1068);			//염주의 마공 적용 (원래 마공 : 789)
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉십자가", rare, Weapon_detailType.PRIEST_CROSS, 90, false, version);
		temp2.vStat.addStatList("물공", 928);
		temp2.vStat.addStatList("마공", 882);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉염주", rare, Weapon_detailType.PRIEST_ROSARY, 90, false, version);
		temp2.vStat.addStatList("물공", 836);
		temp2.vStat.addStatList("마공", 1068);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 95);
		temp2.vStat.addStatList("마크", 2);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		
		temp2 = new Weapon("마봉장창", rare, Weapon_detailType.LANCE_PIKE, 90, false, version);
		temp2.vStat.addStatList("물공", 975);
		temp2.vStat.addStatList("마공", 836);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 95);
		temp2.vStat.addStatList("물크", 2);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉미늘창", rare, Weapon_detailType.LANCE_HALBERD, 90, false, version);
		temp2.vStat.addStatList("물공", 1114);
		temp2.vStat.addStatList("마공", 789);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		
		temp2 = new Weapon("마봉캐넌", rare, Weapon_detailType.GUN_HCANON, 90, false, version);
		temp2.vStat.addStatList("물공", 1075);
		temp2.vStat.addStatList("마공", 645);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 95);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉머스켓", rare, Weapon_detailType.GUN_MUSKET, 90, false, version);
		temp2.vStat.addStatList("물공", 989);
		temp2.vStat.addStatList("마공", 860);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("지능", 95);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉보우건", rare, Weapon_detailType.GUN_BOWGUN, 90, false, version);
		temp2.vStat.addStatList("물공", 774);
		temp2.vStat.addStatList("마공", 860);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("지능", 32);
		temp2.vStat.addStatList("물크", 3);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉자권", rare, Weapon_detailType.GUN_AUTOPISTOL, 90, false, version);
		temp2.vStat.addStatList("물공", 645);
		temp2.vStat.addStatList("마공", 946);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉리볼버", rare, Weapon_detailType.GUN_REVOLVER, 90, false, version);
		temp2.vStat.addStatList("물공", 920);
		temp2.vStat.addStatList("마공", 774);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("물크", 2);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		
		temp2 = new Weapon("마봉차크라웨펀", rare, Weapon_detailType.THIEF_CHAKRAWEAPON, 90, false, version);
		temp2.vStat.addStatList("물공", 762);
		temp2.vStat.addStatList("마공", 1021);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 95);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉단검", rare, Weapon_detailType.THIEF_DAGGER, 90, false, version);
		temp2.vStat.addStatList("물공", 869);
		temp2.vStat.addStatList("마공", 836);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("물크", 10);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉쌍검", rare, Weapon_detailType.THIEF_TWINSWORD, 90, false, version);
		temp2.vStat.addStatList("물공", 1005);
		temp2.vStat.addStatList("마공", 743);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("힘", 63);
		temp2.vStat.addStatList("물크", 5);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
		temp2 = new Weapon("마봉완드", rare, Weapon_detailType.THIEF_WAND, 90, false, version);
		temp2.vStat.addStatList("물공", 804);
		temp2.vStat.addStatList("마공", 1068);
		temp2.vStat.addStatList("독공", 452);
		temp2.vStat.addStatList("지능", 63);
		temp2.vStat.addStatList("마크", 5);
		temp2.vStat.addSkillRange(40, 45, 2, false);
		magicalSealedWeapon.put(temp2.weaponType, temp2);
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
		//i.setEnabled(true);
		
		int index=0;
		for(String s : i.explanation){
			if(s.contains("세팅 저장 당시의 아이템 정보입니다."))
				i.explanation.remove(index);
			index++;
		}
		i.explanation.add(name+" 세팅 저장 당시의 아이템 정보입니다.");
		return i;
	}
	
	public Setting saveToClone(String name)
	{
		Setting replicate = new Setting();
		replicate.setting_name=name;
		
		replicate.creature = this.creature;
		replicate.drape = this.drape;
		
		for(Entry<Equip_part, Equipment> entry : equipmentList.entrySet())
			replicate.equipmentList.replace(entry.getKey(), (Equipment) makeReplicate(entry.getValue(), name));
		
		for(Entry<Equip_part, Avatar> entry : avatarList.entrySet())
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
		else if(o instanceof String)
			return o.equals(setting_name);
		else return false;
	}
	
	public static Setting getMagicalSealedSetting(Job job)
	{
		Setting magicalSealedSetting = new Setting();
		magicalSealedSetting.equipmentList=magicalSealedEquip;
		magicalSealedSetting.avatarList=superiorAvatar;
		
		switch(job)
		{
		case LAUNCHER_F: case LAUNCHER_M:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.GUN_HCANON);
			break;
		case RANGER_F: case RANGER_M:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.GUN_REVOLVER);
			break;
		case DEMONSLAYER:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.SWORD_LONGSWORD);
			break;
		case SPITFIRE_M: case SPITFIRE_F:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.GUN_MUSKET);
			break;
		case ELEMENTALBOMBER:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.MAGE_STAFF);
			break;
		case EXORCIST:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.PRIEST_BATTLEAXE);
			break;
		case BATTLEMAGE:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.MAGE_SPEAR);
			break;
		case ELEMENTALMASTER:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.MAGE_STAFF);
			break;
		case STRIKER_F: case STRIKER_M:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.FITGHTER_BOXGLOVE);
			break;
		case VANGUARD:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.LANCE_HALBERD);
			break;
		case DUALIST:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.LANCE_PIKE);
			break;
		case ELVENKNIGHT:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.SWORD_LONGSWORD);
			break;
		case ROUGE:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.THIEF_TWINSWORD);
			break;
		case BUSERKER:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.SWORD_LONGSWORD);
			break;
		case SHADOWDANCER:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.THIEF_DAGGER);
			break;
		case NENMASTER_F:
			magicalSealedSetting.weapon=magicalSealedWeapon.get(Weapon_detailType.FIGHTER_KNUCKLE);
			break;
		default:
			break;
		}
		
		return magicalSealedSetting;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone()
	{
		Setting replicate;
		try {
			replicate = (Setting) super.clone();
			replicate.equipmentList = (HashMap<Equip_part, Equipment>) equipmentList.clone();
			replicate.avatarList = (HashMap<Equip_part, Avatar>) avatarList.clone();
			
			return replicate;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
