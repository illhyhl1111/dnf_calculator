package dnf_calculator;

import dnf_class.Characters;
import dnf_class.Monster;
import dnf_infomation.FunctionInfo;

public abstract class FunctionStat implements Cloneable {

	public final String functionName;
	public final static String ARGS_SPLIT = "/";
	
	public FunctionStat(String name){
		functionName = name;
	}

	public abstract StatusList function(Characters character, Monster monster, Object item, String[] args);
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	public static String[] parseArgs(String str)
	{
		if(str.contains("물리무기마스터리") || str.contains("마법무기마스터리"))
			return str.split(ARGS_SPLIT);
		
		else return null;
	}
	
	public static FunctionStat getFunction(String str)
	{
		String[] args = parseArgs(str);
		FunctionStat ret;
		if(args!= null && (args[0].equals("물리무기마스터리") || args[0].equals("마법무기마스터리")))
			ret = FunctionInfo.fStat.get(args[0]);
		else
			ret = FunctionInfo.fStat.get(str);
		if(ret==null)
			System.out.println(str);
		return ret;
	}
}
