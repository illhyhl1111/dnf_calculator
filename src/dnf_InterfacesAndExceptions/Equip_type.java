package dnf_InterfacesAndExceptions;

public enum Equip_type
{
	WEAPON("무기", 8), FABRIC("천", 7), LEATHER("가죽", 6), MAIL("경갑", 5), HEAVY("중갑", 4), PLATE("판금", 3),
	ACCESSORY("악세사리", 2), SPECIALEQUIP("특수장비", 1), NONE("없음", 0), ALL("모든재질", 7); 
	
	String name;
	public final int order;
	
	Equip_type(String name, int order)
	{
		this.name=name;
		this.order=order;
	}
	
	public String getName() {return name;}
}
