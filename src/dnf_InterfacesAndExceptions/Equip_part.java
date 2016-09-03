package dnf_InterfacesAndExceptions;

public enum Equip_part implements Equipable
{
	TITLE("칭호"),
	ROBE("상의"), TROUSER("하의"), SHOULDER("머리어깨"),
	BELT("벨트"), SHOES("신발"), NECKLACE("목걸이"),
	BRACELET("팔찌"), RING("반지"), EARRING("귀걸이"),
	AIDEQUIPMENT("보조장비"), MAGICSTONE("마법석"), WEAPON("무기");
	
	String name;
	
	Equip_part(String name)
	{
		this.name=name;
	}
	
	public String getName() {return name;}
}
