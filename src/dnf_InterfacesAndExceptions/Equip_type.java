package dnf_InterfacesAndExceptions;

public enum Equip_type
{
	FABRIC("천", 5), LEATHER("가죽", 4), MAIL("경갑", 3), HEAVY("중갑", 2), PLATE("판금", 1), NONE("없음", 0);
	
	String name;
	public final int order;
	
	Equip_type(String name, int order)
	{
		this.name=name;
		this.order=order;
	}
	
	public String getName() {return name;}
}
