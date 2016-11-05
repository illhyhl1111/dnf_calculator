package dnf_class;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.StatusList;

public class SelectionBuff extends Buff{
	
	private static final long serialVersionUID = -3979890127531382689L;
	public final boolean selectOneOption;
	public HashMap<String, Entry<StatusList, Boolean>> selectionList;
	
	public SelectionBuff(String name, Item_rarity rarity, boolean selectOneOptionMode, String version)
	{
		super(name, rarity, version);
		selectionList = new HashMap<String, Entry<StatusList, Boolean>>();
		this.selectOneOption=selectOneOptionMode;
		
		explanation.add("더블클릭하여 설정을 변경하세오");
	}
	
	public void makeSelectionOption(String name, StatusList statList){
		selectionList.put(name, new AbstractMap.SimpleEntry<StatusList, Boolean>(statList, false));
	}
	
	public void setSelection(String[] name, boolean[] selected)
	{
		int i;
		StatusList stat;
		if(getName().equals("모험단") || getName().equals("길드 스탯")){
			vStat = new StatusList();
			stat = vStat;
		}
		else {
			dStat = new StatusList();
			stat = dStat;
		}
		for(i=0; i<name.length; i++){
			selectionList.get(name[i]).setValue(selected[i]);
			if(selected[i]){
				stat.statList.addAll(selectionList.get(name[i]).getKey().statList);
			}
		}
	}
}
