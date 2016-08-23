package dnf_calculator;
import java.util.LinkedList;

class Characters
{
	Status villageStatus;
	Status dungeonStatus;
	LinkedList<Item> equipment;
	LinkedList<Item> avatar;
	Item creature;
	LinkedList<Item> doping;
	
	int level;
	
	public Characters(Status stat, int lev)
	{
		villageStatus=stat;
		level=lev;
	}
}