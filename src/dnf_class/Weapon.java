package dnf_class;

import dnf_InterfacesAndExceptions.AddOn;
import dnf_InterfacesAndExceptions.Dimension_stat;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.Weapon_detailType;

public class Weapon extends Equipment{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2317758055158333589L;

	Weapon_detailType weaponType;
	int reforge;
	
	public Weapon(String name, String icon,Item_rarity rarity, Dimension_stat dimStat, 
			int reinforce, Card card, SetName setName, Weapon_detailType weaponType, int reforge)
	{	
		super(name, icon, rarity, Equip_part.WEAPON, dimStat, reinforce, card, setName, Equip_type.NONE);
		this.weaponType =weaponType;
		this.reforge=reforge;
	}
	public Weapon(String name, String icon, Item_rarity rarity, Weapon_detailType weaponType)
	{
		this(name, icon, rarity, Dimension_stat.NONE, 0, new Card("없음", null, Item_rarity.NONE, AddOn.NONE), SetName.NONE, weaponType, 0);
	}
	public Weapon() {
		super(Equip_part.WEAPON);
		this.weaponType=Weapon_detailType.NONE;
	}
	
	public boolean enabled(JobList job)
	{
		return weaponType.enabled(job);
	}
}
