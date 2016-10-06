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
		enabled=true;
		selectionList = new HashMap<String, Entry<StatusList, Boolean>>();
		this.selectOneOption=selectOneOptionMode;
	}
	
	public void makeSelectionOption(String name, StatusList statList){
		selectionList.put(name, new AbstractMap.SimpleEntry<StatusList, Boolean>(statList, false));
	}
	
	public void setSelection(String name, boolean selected)
	{
		selectionList.get(name).setValue(selected);
		if(selectOneOption){
			if(selected) dStat = selectionList.get(name).getKey();
			else dStat = new StatusList();
		}
		else{
			if(selected) dStat.statList.addAll(selectionList.get(name).getKey().statList);
			else{
				dStat = new StatusList();
				for(Entry<StatusList, Boolean> entry : selectionList.values()){
					if(entry.getValue()) dStat.statList.addAll(entry.getKey().statList);
				}
			}
		}
	}
}
