package dnf_InterfacesAndExceptions;

import java.io.Serializable;

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
}

interface Equipable
{
}