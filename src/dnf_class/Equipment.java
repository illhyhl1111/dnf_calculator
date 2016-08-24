package dnf_class;

import org.eclipse.swt.graphics.Image;

import dnf_InterfacesAndExceptions.AddOn;
import dnf_InterfacesAndExceptions.Dimension_stat;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.SetName;

@SuppressWarnings("serial")
public class Equipment extends Item
{
	Equip_part part;									//부위
	Dimension_stat dimStat;								//차원작
	int reinforce;										//강화수치
	Card card;											//보주
	SetName setName;									//셋옵이름
	Equip_type type;									//재질
	
	public Equipment(String name, Image icon,Item_rarity rarity, Equip_part part, Dimension_stat dimStat,
			int reinforce, Card card, SetName setName, Equip_type type)
	{	
		super(name, icon, rarity);
		this.part=part;
		this.dimStat=dimStat;
		this.reinforce=reinforce;
		this.card=card;
		this.setName=setName;
		this.type=type;
	}
	public Equipment(String name, Image icon, Item_rarity rarity, Equip_part part, Equip_type type)
	{
		this(name, icon, rarity, part, Dimension_stat.NONE, 0, new Card("없음", null, Item_rarity.NONE, AddOn.NONE), SetName.NONE, type);
	}
	public Equipment(Equip_part part) {
		super();
		this.part=part;
		type = Equip_type.NONE;
	}
	
	@Override
	public String toString()
	{
		String out;
		out = "name : " + super.getName() +"\nrarity : "+super.getRarity()+"\nreinforce : "+reinforce+"\ntype : "+type.toString();
		out += "\npart : "+part.toString()+"\ntype : "+type.toString()+"\n\n";
		return out;
	}
}
