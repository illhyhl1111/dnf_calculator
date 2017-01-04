package dnf_InterfacesAndExceptions;

import java.io.Serializable;
import java.util.ArrayList;

import dnf_class.Emblem;

public enum Equip_part implements Serializable
{
	TITLE("칭호", 1), SKILLTITLE("스킬칭호", 0),
	ROBE("상의", 12), TROUSER("하의", 11), SHOULDER("머리어깨",10),
	BELT("벨트", 9), SHOES("신발", 8), NECKLACE("목걸이", 7),
	BRACELET("팔찌", 6), RING("반지", 5), EARRING("귀걸이", 2),
	AIDEQUIPMENT("보조장비", 4), MAGICSTONE("마법석", 3), WEAPON("무기", 13),
	
	CREATURE("크리쳐", -12), DRAPE("휘장", -11),
	ACAP("모자 아바타", -10), AHAIR("머리 아바타", -9), AFACE("얼굴 아바타", -8), 
	ANECK("목가슴 아바타", -7), ACOAT("상의 아바타", -6), ABELT("벨트 아바타", -5),
	APANTS("하의 아바타", -4), ASHOES("신발 아바타", -3), ASKIN("피부 아바타", -2), AURA("오라 아바타", -1);
	
	String name;
	public final int order;
	
	Equip_part(String name, int order)
	{
		this.name=name;
		this.order=order;
	}
	
	public static Equip_part getPartFromOrder(int order)
	{
		for(Equip_part part : Equip_part.values())
			if(part.order==order) return part;
		return null;
	}
	
	public String getName() {return name;}
	
	public static Equip_part[] equipValues(){
		ArrayList<Equip_part> part = new ArrayList<Equip_part>();
		for(Equip_part p : Equip_part.values())
			if(p.order>=0) part.add(p);
		return part.toArray(new Equip_part[0]);
	}
	public static Equip_part[] avatarValues(){
		ArrayList<Equip_part> part = new ArrayList<Equip_part>();
		for(Equip_part p : Equip_part.values())
			if(p.order<0) part.add(p);
		return part.toArray(new Equip_part[0]);
	}
	
	public boolean equipable(Emblem emblem)
	{
		Emblem_type type = emblem.type;
		switch(this)
		{
		case ACAP: case AHAIR:
			if(type==Emblem_type.RED || type==Emblem_type.RED_GREEN || type==Emblem_type.MULTIPLE_COLOR) return true;
			return false;
		case AFACE: case ANECK:
			if(type==Emblem_type.YELLOW || type==Emblem_type.YELLOW_BLUE || type==Emblem_type.MULTIPLE_COLOR) return true;
			return false;
		case ACOAT: case APANTS:
			if(type==Emblem_type.GREEN || type==Emblem_type.RED_GREEN || type==Emblem_type.MULTIPLE_COLOR || type==Emblem_type.PLATINUM) return true;
			return false;
		case ASHOES: case ABELT:
			if(type==Emblem_type.BLUE || type==Emblem_type.YELLOW_BLUE || type==Emblem_type.MULTIPLE_COLOR) return true;
			return false;
		case AURA: case ASKIN:
			return true;
		default:
			return false;
		}
	}
}
