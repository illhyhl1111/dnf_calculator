package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Title extends Item
{
	public Title(String name, String icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Title(){ super();}
	
	@Override
	public String getTypeName() { return "칭호";}
}
