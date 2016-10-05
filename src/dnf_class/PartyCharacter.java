package dnf_class;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_calculator.StatusList;

public class PartyCharacter extends IconObject{
	private static final long serialVersionUID = -5106982913896677040L;
	private final HashMap<String, HashMap<String, Buff>> buffList;					// (버프 이름, (특성 이름, 버프) )
	public final Job job;

	public PartyCharacter(String name, Job job)
	{
		super();
		setName(name);
		setIcon("image\\PartyCharacter\\"+name+".png");
		
		buffList = new HashMap<String, HashMap<String, Buff>>();
		this.job=job;
	}
	
	public void setBuff(String name, String feature, StatusList statList)
	{
		HashMap<String, Buff> buffMap = buffList.get(name);
		if(buffMap==null){
			buffMap = new HashMap<String, Buff>();
			buffList.put(name, buffMap);
		}
		Buff buff = new Buff(name, Item_rarity.NONE);
		buff.dStat = statList;
		buffMap.put(name+"/"+feature, buff);
	}
	public void setBuff(String name, StatusList statList)
	{
		setBuff(name, name, statList);
	}
	
	public String[] getBuffFeatureList(String name)
	{
		if(name==null) return new String[] {"--"};
		HashMap<String, Buff> buffMap = buffList.get(name);
		LinkedList<String> list = new LinkedList<String>();
		for(String str : buffMap.keySet())
			list.add(str);
		
		Collections.sort(list);
		return list.toArray(new String[0]);
	}
	
	public Buff getBuff(String buffName, String feature)
	{
		HashMap<String, Buff> buffMap = buffList.get(buffName);
		if(buffMap==null) return null;
		
		return buffMap.get(feature);
	}
	
	public Buff getBuff(String buffName)
	{
		HashMap<String, Buff> buffMap = buffList.get(buffName);
		if(buffMap==null || buffMap.keySet().size()!=1) return null;
		
		return buffMap.get(0);
	}
	
	public LinkedList<Buff> getBuffList(String feature1, String feature2)
	{
		LinkedList<Buff> list = new LinkedList<Buff>();
		for(HashMap<String, Buff> buffMap : buffList.values())
		{
			if(buffMap.size()==1) list.add(buffMap.values().iterator().next());
			else{
				for(Entry<String, Buff> entry : buffMap.entrySet()){
					if(entry.getKey().equals(feature1) || entry.getKey().equals(feature2)){
						list.add(entry.getValue());
						break;
					}
 				}
			}
		}
		
		return list;
	}
	
	public String getFeaturedBuff(int index)
	{
		for(Entry<String, HashMap<String,Buff>> entry : buffList.entrySet()){
			if(entry.getValue().size()>1){
				if(index==0) return entry.getKey();
				else{
					index--;
					continue;
				}
			}
		}
		return null;
	}
	
	public HashMap<String, HashMap<String, Buff>> getBuffHash()
	{
		return buffList;
	}
}