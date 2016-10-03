package dnf_class;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_infomation.GetDictionary;

public class Weapon extends Equipment{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2317758055158333589L;

	public Weapon_detailType weaponType;
	private int reforge;
	
	public Weapon(String name, Item_rarity rarity, Card card, SetName setName, Weapon_detailType weaponType, int level, boolean isRare, int reforge)
	{	
		super(name, rarity, Equip_part.WEAPON, card, setName, Equip_type.WEAPON, level, isRare);
		this.setIcon("image\\Weapon\\"+name+".png");
		this.weaponType =weaponType;
		this.reforge=reforge;

		vStat.addStatList("물리방무뎀", new StatusInfo(0), true);
		vStat.addStatList("마법방무뎀", new StatusInfo(0), true);
		vStat.addStatList("재련독공", 0, true);
	}
	public Weapon(String name, Item_rarity rarity, Weapon_detailType weaponType, SetName setName, int level, boolean isRare)
	{
		this(name, rarity, new Card(), setName, weaponType, level, isRare, 0);
	}
	public Weapon(String name, Item_rarity rarity, Weapon_detailType weaponType, int level, boolean isRare)
	{
		this(name, rarity, new Card(), SetName.NONE, weaponType, level,isRare, 0);
	}
	public Weapon() {
		super(Equip_part.WEAPON);
		this.weaponType=Weapon_detailType.NONE;
		reforge=0;
		
		vStat.addStatList("물리방무뎀", new StatusInfo(0), true);
		vStat.addStatList("마법방무뎀", new StatusInfo(0), true);
		vStat.addStatList("재련독공", 0, true);
	}
	
	@Override
	public void setReinforce(int num) throws UnknownInformationException
	{
		super.setReinforce(num);
		try {			
			int ignIndex = getIgnIndex();
			if(ignIndex>=0){
				int ignPhy = GetDictionary.getReinforceInfo_phy(num, super.getRarity(), level, weaponType);
				int ignMag = GetDictionary.getReinforceInfo_mag(num, super.getRarity(), level, weaponType);
				vStat.statList.get(ignIndex).stat.setInfo(ignPhy);
				vStat.statList.get(ignIndex+1).stat.setInfo(ignMag);
			}
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	public void setReforge(int num) throws UnknownInformationException
	{
		reforge=num;
		try {	
			int refNum = GetDictionary.getReforgeInfo(num, super.getRarity(), level);
			vStat.statList.get(getReforgeIndex()).stat.setInfo(refNum);
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	public void setReforgeNum(int num) { reforge=num; }
	
	public int getReforge() {return reforge;}
	
	@Override
	public int getIgnIndex()
	{
		int iter=0;
		for(StatusAndName s : vStat.statList){
			if(s.equals("물리방무뎀")){
				return iter;
			}
			iter++;
		}
		return -1;
	}
	
	@Override
	public int getReforgeIndex()
	{
		int iter=0;
		for(StatusAndName s : vStat.statList){
			if(s.equals("재련독공")){
				return iter;
			}
			iter++;
		}
		return -1;
	}
	
	@Override
	public int getItemStatIndex()
	{
		if(getReforgeIndex()!=-1) return getReforgeIndex()+1;
		else if(getIgnIndex()!=-1) return getIgnIndex()+2;
		else if(getDimStatIndex()!=-1) return getDimStatIndex()+1;
		return 0;
	}
	
	public boolean enabled(Job job)
	{
		return weaponType.enabled(job);
	}
	
	@Override
	public String getTypeName() { return part.getName();}
	@Override
	public String getTypeName2() { return weaponType.getName();}
	
	@Override
	public int compareTo(Item arg) {
		if(!(arg instanceof Weapon)) return 1;			// 1.
		Weapon arg2 = (Weapon)arg;
		
		if(arg2.getRarity()!=this.getRarity()) return this.getRarity().rarity-arg2.getRarity().rarity;		// 2.
		if(arg2.level!=level) return arg2.level-level;														// 4.
		if(arg2.isRareItem!=isRareItem){
			if(isRareItem) return -1;
			else return 1;
		}
		if(arg2.weaponType!=weaponType) return arg2.weaponType.getName().compareTo(weaponType.getName());	// 3.
		if(arg2.setName!=setName){
			if(setName==SetName.NONE) return 1;
			else if(arg2.setName==SetName.NONE) return -1;
			return setName.compare(arg2.setName);										// 5.
		}
		if(!arg2.getName().equals(getName())) return arg2.getName().compareTo(this.getName());
		else return getReinforce()-arg2.getReinforce();													// 7.
	}
}
