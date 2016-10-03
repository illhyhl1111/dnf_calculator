package dnf_InterfacesAndExceptions;

public enum Item_rarity
{
	NONE("등급 없음", 0), COMMON("커먼", 1), UNCOMMON("언커먼", 2), RARE("레어", 3),
	CHRONICLE("크로니클", 4), UNIQUE("유니크", 5), LEGENDARY("레전더리", 6), EPIC("에픽", 7),
	AVATAR("상급 아바타", 2);
	
	String name;
	public final int rarity;
	
	Item_rarity(String name, int rarity)
	{
		this.name=name;
		this.rarity=rarity;
	}
	
	public String getName() {return name;}
}
