package dnf_class;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_calculator.BuffCalculator;
import dnf_calculator.StatusList;

public class Saint extends PartyCharacter{
	
	private static final long serialVersionUID = -697331249064044381L;
	public int stat_sta=0;
	public int stat_will=0;
	public int bowNum=0;
	public boolean hasSilence=false;
	public boolean hasBoilingBlood=false;
	public boolean hasSaviour=false;
	public boolean setDivine=false;
	public int level_striking=0;
	public int level_wisebless=0;
	public int level_dawnbless=0;
	public int level_glorybless=0;
	public int level_aporkalypse=0;
	public int level_divine=0;
	public int level_belief=0;
	public int divineStack=0;

	public Saint()
	{
		super("홀리", Job.CRUSADER);
	}
	
	public void setStat(int[] intStat, boolean[] boolStat, String setting)
	{
		if(intStat.length!=11 || boolStat.length!=4) return;
		stat_sta=intStat[0];
		stat_will=intStat[1];
		bowNum=intStat[2];
		level_striking=intStat[3];
		level_wisebless=intStat[4];
		level_dawnbless=intStat[5];
		level_glorybless=intStat[6];
		level_aporkalypse=intStat[7];
		level_divine=intStat[8];
		level_belief=intStat[9];
		divineStack=intStat[10];
		hasSilence=boolStat[0];
		hasBoilingBlood=boolStat[1];
		hasSaviour=boolStat[2];
		setDivine=boolStat[3];
		
		setBuff("영광의 축복", setting, BuffCalculator.glorybless(this));
		setBuff("스트라이킹", setting, BuffCalculator.striking(this));
		setBuff("지혜의 축복", setting, BuffCalculator.wisebless(this));
		setBuff("여명의 축복", setting, BuffCalculator.dawnbless(this));
		setBuff("아포칼립스", setting, BuffCalculator.aprokalypse(this));
	}
	
	@Override
	public LinkedList<Buff> getBuffList(String setting, String feature)
	{
		LinkedList<Buff> list = new LinkedList<Buff>();
		for(Entry<String, HashMap<String, Buff>> entry : getBuffHash().entrySet())
		{
			for(Entry<String, Buff> entry2 : entry.getValue().entrySet()){
				if(entry2.getKey().equals(feature) || entry2.getKey().equals(setting)){
					list.add(entry2.getValue());
					break;
				}
			}
		}
		
		return list;
	}
	
	@Override
	public void setBuff(String name, String feature, StatusList statList)
	{
		HashMap<String, Buff> buffMap = getBuffHash().get(name);
		if(buffMap==null){
			buffMap = new HashMap<String, Buff>();
			getBuffHash().put(name, buffMap);
		}
		Buff buff = new Buff(name, Item_rarity.NONE);
		buff.dStat = statList;
		buffMap.put(feature, buff);
	}
	@Override
	public void setBuff(String name, StatusList statList)
	{
		setBuff(name, name, statList);
	}
	
	@Override
	public String[] getBuffFeatureList(int index)
	{
		if(index==1){
			HashMap<String, Buff> buffMap = getBuffHash().get("신념의 오라");
			LinkedList<String> list = new LinkedList<String>();
			for(String str : buffMap.keySet())
				list.add(str);
			
			Collections.sort(list);
			return list.toArray(new String[0]);
		}
		else if(index==0) {
			HashMap<String, Buff> buffMap = getBuffHash().get("영광의 축복");
			LinkedList<String> list = new LinkedList<String>();
			for(String str : buffMap.keySet())
				list.add(str);
			
			Collections.sort(list);
			return list.toArray(new String[0]);
		}
		return null;
	}
	
}
