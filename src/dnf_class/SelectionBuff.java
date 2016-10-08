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
	
	public SelectionBuff(String name, Item_rarity rarity, boolean selectOneOptionMode)
	{
		super(name, rarity);
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
		dStat = new StatusList();
		for(i=0; i<name.length; i++){
			selectionList.get(name[i]).setValue(selected[i]);
			if(selected[i]){
				if(selectOneOption)
					dStat = selectionList.get(name[i]).getKey();
				else dStat.statList.addAll(selectionList.get(name[i]).getKey().statList);
					
			}
		}
	}
}
