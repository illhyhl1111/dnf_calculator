package dnf_class;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;

public class Creature extends Item
{
	private static final long serialVersionUID = -8170582599826405449L;

	public Creature(String name, Item_rarity rarity, String version)
	{
		super(name, "image\\Creature\\"+name+".png", rarity, version);
	}
	public Creature(){ super();}
	
	@Override
	public String getTypeName() { return "크리쳐";}
	
	@Override
	public Equip_part getPart() {return Equip_part.CREATURE;}
}
