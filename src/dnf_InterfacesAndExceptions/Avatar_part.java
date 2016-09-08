package dnf_InterfacesAndExceptions;

import java.io.Serializable;

public enum Avatar_part implements Equipable, Serializable
{
	CAP("모자 아바타"), HAIR("머리 아바타"), FACE("얼굴 아바타"), 
	NECK("목가슴 아바타"), COAT("상의 아바타"), BELT("벨트 아바타"),
	PANTS("하의 아바타"), SHOES("신발 아바타"), SKIN("피부 아바타"), AURA("오라 아바타");
	
	String name;
	
	Avatar_part(String name)
	{
		this.name=name;
	}
	
	public String getName()
	{
		return name;
	}
}

interface Equipable
{
}