package dnf_InterfacesAndExceptions;

public enum Item_rarity
{
	NONE("등급 없음"), COMMON("커먼"), UNCOMMON("언커먼"), RARE("레어"),
	CHRONICLE("크로니클"), UNIQUE("유니크"), LEGENDARY("레전더리"), EPIC("에픽");
	
	String name;
	
	Item_rarity(String name)
	{
		this.name=name;
	}
	
	public String getName() {return name;}
}
