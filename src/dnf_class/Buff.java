package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;

public class Buff extends Item 
{
	private static final long serialVersionUID = 9074409084429656502L;
	public boolean enabled;
	
	public Buff(String name, Item_rarity rarity)
	{
		super(name, "image\\Buff\\"+name+".png", rarity);
		enabled=false;
	}
	public Buff(){ super();}
	
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof Buff){
			if(this.getName().equals(((Buff) other).getName())) return true;
		}
		else if(other instanceof String)
			if(this.getName().equals(other)) return true;
		return false;
	}
	
	@Override  
	public int hashCode() 
	{  
		return getName().toString().hashCode();  
	} 
	
	@Override
	public int compareTo(Item arg) {
		return arg.getName().compareTo(getName());
	}
	
	@Override
	public String getTypeName() { return "버프/도핑";}
}
