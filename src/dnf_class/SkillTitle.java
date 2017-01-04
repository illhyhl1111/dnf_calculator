package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;

public class SkillTitle extends Title{

	private static final long serialVersionUID = -293533481131361279L;
	
	public SkillTitle(String name, Item_rarity rarity, Card card, String version)
	{
		super(name, rarity, card, version);
	}
	
	public SkillTitle(String name, Item_rarity rarity, String version)
	{
		super(name, rarity, version);
	}
	public SkillTitle() {super();}
	
	@Override
	public int compareTo(Item arg) {
		if(!(arg instanceof SkillTitle)){
			if(arg instanceof Equipment || arg instanceof Title) return -1;
			else return 1;
		}
		return arg.getName().compareTo(this.getName());
	}
}
