package dnf_InterfacesAndExceptions;

import java.io.Serializable;

public enum Equip_part implements Equipable, Serializable
{
	TITLE("칭호", 0),
	ROBE("상의", 11), TROUSER("하의", 10), SHOULDER("머리어깨", 9),
	BELT("벨트", 8), SHOES("신발", 7), NECKLACE("목걸이", 6),
	BRACELET("팔찌", 5), RING("반지", 4), EARRING("귀걸이", 1),
	AIDEQUIPMENT("보조장비", 3), MAGICSTONE("마법석", 2), WEAPON("무기", 12);
	
	String name;
	public final int order;
	
	Equip_part(String name, int order)
	{
		this.name=name;
		this.order=order;
	}
	
	public String getName() {return name;}
}
