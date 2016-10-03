package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;

public class Drape extends Item
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4070506482238398315L;

	public Drape(String name, String icon, Item_rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Drape(){ super();}
	
	@Override
	public String getTypeName() { return "휘장";}
}
