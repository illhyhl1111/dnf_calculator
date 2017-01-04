package dnf_class;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;

public class Drape extends Item
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4070506482238398315L;

	public Drape(String name, Item_rarity rarity, String version)
	{
		super(name, "image\\Drape\\"+name+".png", rarity, version);
	}
	public Drape(){ super();}
	
	@Override
	public String getTypeName() { return "휘장";}
	
	@Override
	public Equip_part getPart() {return Equip_part.DRAPE;}
}
