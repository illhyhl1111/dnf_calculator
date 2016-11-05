package dnf_class;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.CalculatorVersion;
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
	public static final String settingFeatureName = "맞춤형 홀리쟝";
	public static final int statNum = 15;
	public static final int intStatNum = 11;
	public static final int boolStatNum = 4;
	
	public static final String[] boolStatStr = {
		"사일 3 보유", "끓피 보유", "구원의 이기 보유", "2각 사용"
	};
	public static final String[] intStatStr = {
		"체력", "정력", "바우 보유숫자", "스킹 레벨", "지축 레벨", "여축 레벨", "영축 레벨", "아포 레벨", "2각 레벨", "2각 스택수", "신념의 오라 레벨"
	};

	public Saint()
	{
		super("홀리", Job.CRUSADER);
	}
	
	public void setStat(int[] intStat, boolean[] boolStat, String setting, String version)
	{
		if(intStat.length!=intStatNum || boolStat.length!=boolStatNum) return;
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
		
		setBuff("영광의 축복", setting, BuffCalculator.glorybless(this), version);
		setBuff("스트라이킹", setting, BuffCalculator.striking(this), version);
		setBuff("지혜의 축복", setting, BuffCalculator.wisebless(this), version);
		setBuff("여명의 축복", setting, BuffCalculator.dawnbless(this), version);
		setBuff("아포칼립스", setting, BuffCalculator.aprokalypse(this), version);
	}
	
	public void setUserHolyBuff()
	{
		setBuff("영광의 축복", settingFeatureName, BuffCalculator.glorybless(this), CalculatorVersion.CHARACTER_VERSION);
		setBuff("스트라이킹", settingFeatureName, BuffCalculator.striking(this), CalculatorVersion.CHARACTER_VERSION);
		setBuff("지혜의 축복", settingFeatureName, BuffCalculator.wisebless(this), CalculatorVersion.CHARACTER_VERSION);
		setBuff("여명의 축복", settingFeatureName, BuffCalculator.dawnbless(this), CalculatorVersion.CHARACTER_VERSION);
		setBuff("아포칼립스", settingFeatureName, BuffCalculator.aprokalypse(this), CalculatorVersion.CHARACTER_VERSION);
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
	public void setBuff(String name, String feature, StatusList statList, String version)
	{
		HashMap<String, Buff> buffMap = getBuffHash().get(name);
		if(buffMap==null){
			buffMap = new HashMap<String, Buff>();
			getBuffHash().put(name, buffMap);
		}
		Buff buff = new Buff(name, Item_rarity.NONE, version);
		buff.dStat = statList;
		buffMap.put(feature, buff);
	}
	@Override
	public void setBuff(String name, StatusList statList, String version)
	{
		setBuff(name, name, statList, version);
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
