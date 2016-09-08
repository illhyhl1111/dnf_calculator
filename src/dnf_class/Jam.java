package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Jam extends Item
{
	public Jam(String name, String icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Jam() {
		super("잼 없음", null, Item_rarity.NONE);
	}
	
	@Override
	public String getTypeName() { return "잼";}
}