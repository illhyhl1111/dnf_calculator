package dnf_class;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;

public class ExclusiveBuff extends Buff {
	private static final long serialVersionUID = -8870195247551577184L;

	public final Job[] targetList;
	
	public ExclusiveBuff(String name, Item_rarity rarity, Job[] targetList, String version){
		super(name, rarity, version);
		this.targetList=targetList;
	}
	
	@Override
	public boolean isTarget(Job job){
		for(Job j : targetList){
			if(j==job) return true;
		}
		return false;
	}
}
