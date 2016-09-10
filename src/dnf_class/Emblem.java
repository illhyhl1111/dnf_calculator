package dnf_class;

import dnf_InterfacesAndExceptions.Emblem_type;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Emblem extends Item
{
	public Emblem_type type;
	public Emblem(String name, String icon, Item_rarity rarity, Emblem_type type)
	{
		super(name, icon, rarity);
		this.type=type;
	}
	public Emblem() {
		super("엠블렘 없음", null, Item_rarity.NONE);
		type = Emblem_type.NONE;
	}
	
	@Override
	public String getTypeName() { return "엠블렘";}
	
	public boolean equipable(Avatar avatar)
	{
		return avatar.part.equipable(this);
	}
	
	//////정렬순서
	// 1. 종류 : 장비->칭호->보주->아바타->엠블렘->크리쳐->비장비
	// 2. 등급 : 플티->찬란->화려->빛나는
	// 3. 타입 : 붉->녹->붉녹->노->파->노파->다색
	// 4. 이름 : 사전순
	
	@Override
	public int compareTo(Item arg) {																		// 1.
		if(! (arg instanceof Emblem)){
			if(arg instanceof Creature | arg instanceof Consumeable) return 1;
			else return -1;
		}
		
		if(arg.getRarity()!=this.getRarity()) return this.getRarity().rarity-arg.getRarity().rarity;		// 2. 
		if(((Emblem)arg).type!=type) return type.order-((Emblem)arg).type.order;							// 3.
		else return arg.getName().compareTo(this.getName());												// 4.
	}
}
