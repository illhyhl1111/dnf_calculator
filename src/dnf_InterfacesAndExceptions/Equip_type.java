package dnf_InterfacesAndExceptions;

public enum Equip_type
{
	FABRIC("천"), LEATHER("가죽"), MAIL("경갑"), HEAVY("중갑"), PLATE("판금"), NONE("없음");
	
	String name;
	
	Equip_type(String name)
	{
		this.name=name;
	}
	
	public String getName() {return name;}
}
