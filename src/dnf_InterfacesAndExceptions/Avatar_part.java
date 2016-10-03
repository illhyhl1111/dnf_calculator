package dnf_InterfacesAndExceptions;

import java.io.Serializable;

import dnf_class.Emblem;

public enum Avatar_part implements Equipable, Serializable
{
	CAP("모자 아바타", 9), HAIR("머리 아바타", 8), FACE("얼굴 아바타", 7), 
	NECK("목가슴 아바타", 6), COAT("상의 아바타", 5), BELT("벨트 아바타", 4),
	PANTS("하의 아바타", 3), SHOES("신발 아바타", 2), SKIN("피부 아바타", 1), AURA("오라 아바타", 0);
	
	String name;
	public final int order;
	
	Avatar_part(String name, int order)
	{
		this.name=name;
		this.order=order;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean equipable(Emblem emblem)
	{
		Emblem_type type = emblem.type;
		switch(this)
		{
		case CAP: case HAIR:
			if(type==Emblem_type.RED || type==Emblem_type.RED_GREEN || type==Emblem_type.MULTIPLE_COLOR) return true;
			return false;
		case FACE: case NECK:
			if(type==Emblem_type.YELLOW || type==Emblem_type.YELLOW_BLUE || type==Emblem_type.MULTIPLE_COLOR) return true;
			return false;
		case COAT: case PANTS:
			if(type==Emblem_type.GREEN || type==Emblem_type.RED_GREEN || type==Emblem_type.MULTIPLE_COLOR || type==Emblem_type.PLATINUM) return true;
			return false;
		case SHOES: case BELT:
			if(type==Emblem_type.BLUE || type==Emblem_type.YELLOW_BLUE || type==Emblem_type.MULTIPLE_COLOR) return true;
			return false;
		case AURA: case SKIN:
			return true;
		default:
			return false;
		}
	}
}

interface Equipable
{
}