package dnf_class;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;

@SuppressWarnings("serial")
public class Title extends Item
{
	private Card card;										//보주
	public final Equip_part part;							//부위
	public Title(String name, String icon, Item_rarity rarity, Card card)
	{
		super(name, icon, rarity);
		this.card=card;
		part=Equip_part.TITLE;
	}
	public Title(){
		super();
		card=new Card();
		part=Equip_part.TITLE;
	}
	
	@Override
	public String getTypeName() { return "칭호";}
	
	@Override
	public Card getCard() {
		return card;
	}
	
	@Override
	public Equip_part getPart() {return part;}
	
	public boolean setCard(Card card) {
		if(card.available(this)){
			this.card=card;
			return true;
		}
		return false;
	}
}
