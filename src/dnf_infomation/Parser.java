package dnf_infomation;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import dnf_calculator.ElementInfo;
import dnf_calculator.FunctionStat;
import dnf_calculator.FunctionStatusList;
import dnf_calculator.StatusList;
import dnf_class.Characters;
import dnf_class.Monster;

public class Parser {
	public static double parseForm(String data, double prev)
	{
		if(data.startsWith("+"))
			return prev+Double.parseDouble(data);
		
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			Object result = engine.eval(data);
			if(result instanceof Integer){
				int temp = (int) result;
				return (double) temp;
			}
			return (double) result;
		} catch (NumberFormatException | ScriptException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static double parseStat(String[] data, StatusList list, FunctionStatusList fList)
	{
		boolean changeable = false;
		boolean enableable = false;
		int selectOptionCount=0;
		
		if(data.length>2 && (data[data.length-1].equals("가변") || data[data.length-2].equals("가변")) ){
			changeable = true;
			selectOptionCount++;
		}
		if(data.length>2 && (data[data.length-1].equals("선택") || data[data.length-2].equals("선택")) ){
			enableable = true;
			selectOptionCount++;
		}
		
		if(data[0].contains("부여"))
		{
			if(data.length>1 && data[1].equals("선택")) enableable = true; 
			switch(data[0])
			{
			case "화속부여":
				list.addStatList("화속", new ElementInfo(true, 0), changeable, enableable);
				break;
			case "수속부여":
				list.addStatList("수속", new ElementInfo(true, 0), changeable, enableable);
				break;
			case "명속부여":
				list.addStatList("명속", new ElementInfo(true, 0), changeable, enableable);
				break;
			case "암속부여":
				list.addStatList("암속", new ElementInfo(true, 0),  changeable, enableable);
				break;
			}
			
			return -1;
		}
		
		else if(data[0].equals("무기마스터리")){
			fList.statList.add(new FunctionStat(){
				private static final long serialVersionUID = 7114802204195525723L;

				@Override
				public StatusList function(Characters character, Monster monster, Object item) {
					StatusList statList = new StatusList();
					if(character.getItemSetting().weapon.weaponType.getName().equals(data[2])){
						statList.addStatList("물리마스터리", Double.valueOf(data[1]));
						statList.addStatList("마법마스터리", Double.valueOf(data[1]));
					}
					return statList;
				};
			});
			return Double.valueOf(data[1]);
		}
		else if(data[0].equals("무기마스터리_방무")){
			fList.statList.add(new FunctionStat(){
				private static final long serialVersionUID = -7647925551477189386L;

				@Override
				public StatusList function(Characters character, Monster monster, Object item) {
					StatusList statList = new StatusList();
					if(character.getItemSetting().weapon.weaponType.getName().equals(data[2])){
						statList.addStatList("물리방무뻥", Double.valueOf(data[1]));
						statList.addStatList("마법방무뻥", Double.valueOf(data[1]));
					}
					return statList;
				};
			});
			return Double.valueOf(data[1]);
		}
		
		else if(data[0].contains("스킬") && !data[0].contains("방깍_스킬"))
		{
			String[] skillRange = data[1].split("-");
			try{
				int start = Integer.valueOf(skillRange[0]);
				int end = start;
				if(skillRange.length>1) end = Integer.valueOf(skillRange[1]);
				boolean TP=false;
				if(data[0].equals("TP스킬")) TP=true;
				list.addSkillRange(start, end, Integer.valueOf(data[2]), TP, changeable, enableable);
				return Integer.valueOf(data[2]);
			}
			catch(NumberFormatException e) {
				String skillName = data[1];
				for(int i=2; i<data.length-selectOptionCount-1; i++){
					if(data[i].equals("%")) break;
					skillName = skillName+" "+data[i];
				}
				
				if(data[data.length-selectOptionCount-2].equals("%")){
					list.addSkill_damage(skillName, parseForm(data[data.length-selectOptionCount-1], 0), changeable, enableable);
					return parseForm(data[data.length-selectOptionCount-1], 0);
				}
				else{
					list.addSkill(skillName, (int)Math.round(parseForm(data[data.length-selectOptionCount-1], 0)), changeable, enableable);
					return parseForm(data[data.length-selectOptionCount-1], 0);
				}
			}
		}
		else if(!data[1].contains(".")){
			list.addStatList(data[0], Integer.valueOf(data[1]), changeable, enableable);
			return Double.valueOf(data[1]);
		}
		else{
			list.addStatList(data[0], Double.valueOf(data[1]), changeable, enableable);
			return Double.valueOf(data[1]);
		}
	}
}
