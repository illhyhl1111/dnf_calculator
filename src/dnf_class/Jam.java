package dnf_class;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Item_rarity;

public class Jam extends Item
{
	private static final long serialVersionUID = 3598457075495764348L;

	public Jam(String name, String icon, Item_rarity rarity, String version)
	{
		super(name, icon, rarity, version);
	}
	public Jam() {
		super("잼 없음", null, Item_rarity.NONE, CalculatorVersion.DEFAULT);
	}
	
	@Override
	public String getTypeName() { return "잼";}
}