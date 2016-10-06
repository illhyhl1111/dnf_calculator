package dnf_calculator;

import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_class.Saint;
import dnf_infomation.HolyInfo;

public class BuffCalculator {
	
	private final static double devideNum = 630.0;
	private final static double devideNum_apro = 750.0;
	
	public static StatusList striking(Saint character)
	{
		StatusList statList = new StatusList();
		int str, atk, atkInc=0, strInc=0;
		if(character.bowNum>5) atkInc+=23;
		if(character.hasSilence) atkInc+=13;
		if(character.hasSaviour){
			atkInc+=25;
			strInc+=10;
		}
		else if(character.hasBoilingBlood) atkInc+=20;
		try{
			atk=(int) (staIncrease(character)*(HolyInfo.getStriking_atk(character.level_striking)+atkInc));
			str=(int) (staIncrease(character)*(HolyInfo.getStriking_str(character.level_striking)+strInc));
			statList.addStatList("힘", str);
			statList.addStatList("물공", atk);
		} catch(UnknownInformationException e){
			e.printStackTrace();
		}
		return statList;
	}
	
	public static StatusList wisebless(Saint character)
	{
		StatusList statList = new StatusList();
		int intel, atk, atkInc=0, intInc=0;
		if(character.bowNum>5){
			atkInc+=14;
			intInc+=20;
		}
		if(character.hasSilence){
			atkInc+=7;
			intInc+=12;
		}
		if(character.hasSaviour){
			atkInc+=13;
			intInc+=25;
		}
		else if(character.hasBoilingBlood){
			atkInc+=10;
			intInc+=20;
		}
		try{
			atk=(int) (willIncrease(character)*(HolyInfo.getWisebless_atk(character.level_wisebless)+atkInc));
			intel=(int) (willIncrease(character)*(HolyInfo.getWisebless_int(character.level_wisebless)+intInc));
			statList.addStatList("지능", intel);
			statList.addStatList("마공", atk);
		} catch(UnknownInformationException e){
			e.printStackTrace();
		}
		return statList;
	}
	
	public static StatusList dawnbless(Saint character)
	{
		StatusList statList = new StatusList();
		int fix;
		try{
			fix=(int) (dawnIncrease(character)*(HolyInfo.getDawnblessInfo(character.level_dawnbless))*1.5);
			statList.addStatList("독공", fix);
		} catch(UnknownInformationException e){
			e.printStackTrace();
		}
		return statList;
	}
	
	public static StatusList glorybless(Saint character)
	{
		StatusList statList = new StatusList();
		int str, intel, strInc=0, intInc=0;
		if(character.bowNum>8){
			strInc+=50;
			intInc+=44;
		}
		try{
			str=(int) (staIncrease(character)*(HolyInfo.getGloryblessInfo(character.level_glorybless)+strInc));
			intel=(int) (willIncrease(character)*(HolyInfo.getGloryblessInfo(character.level_glorybless)+intInc));
			statList.addStatList("힘", str);
			statList.addStatList("지능", intel);
		} catch(UnknownInformationException e){
			e.printStackTrace();
		}
		return statList;
	}
	
	public static StatusList aprokalypse(Saint character)
	{
		StatusList statList = new StatusList();
		int str, intel;
		try{
			str=(int) (staIncrease_apro(character)*(HolyInfo.getAprokalypseInfo(character.level_aporkalypse)));
			intel=(int) (willIncrease_apro(character)*(HolyInfo.getAprokalypseInfo(character.level_aporkalypse)));
			statList.addStatList("힘", str);
			statList.addStatList("지능", intel);
		} catch(UnknownInformationException e){
			e.printStackTrace();
		}
		return statList;
	}
	
	
	private static double staIncrease(Saint character) throws UnknownInformationException {
		int stat=character.stat_sta+114+HolyInfo.getBeliefAuraInfo(character.level_belief);
		if(character.setDivine) stat+=character.divineStack*(4+character.level_divine);
		return stat/devideNum +1.0;
	}
	private static double willIncrease(Saint character) throws UnknownInformationException {
		int stat=character.stat_will+114+HolyInfo.getBeliefAuraInfo(character.level_belief);
		if(character.setDivine) stat+=character.divineStack*(4+character.level_divine);
		return stat/devideNum +1.0;
	}
	private static double dawnIncrease(Saint character) throws UnknownInformationException {
		int stat=character.stat_will+character.stat_sta+114*2+HolyInfo.getBeliefAuraInfo(character.level_belief)*2;
		if(character.setDivine) stat+=2*character.divineStack*(4+character.level_divine);
		return stat/2/devideNum +1.0;
	}
	private static double staIncrease_apro(Saint character) throws UnknownInformationException {
		int stat=character.stat_sta+114+HolyInfo.getBeliefAuraInfo(character.level_belief);
		return stat/devideNum_apro +1.0;
	}
	private static double willIncrease_apro(Saint character) throws UnknownInformationException {
		int stat=character.stat_will+114+HolyInfo.getBeliefAuraInfo(character.level_belief);
		return stat/devideNum_apro +1.0;
	}
}
