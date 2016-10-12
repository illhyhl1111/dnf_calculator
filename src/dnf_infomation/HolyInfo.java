package dnf_infomation;

import dnf_InterfacesAndExceptions.UnknownInformationException;

public class HolyInfo {
	public static final int firstLevel_striking = 29;
	public static final int lastLevel_striking = 29;
	public static final int firstLevel_wisebless = 26;
	public static final int lastLevel_wisebless = 26;
	public static final int firstLevel_dawnbless = 26;
	public static final int lastLevel_dawnbless = 26;
	public static final int firstLevel_glorybless = 38;
	public static final int lastLevel_glorybless = 45;
	public static final int firstLevel_aporkalypse = 13;
	public static final int lastLevel_aporkalypse = 18;
	public static final int firstLevel_beliefAura = 15;
	public static final int lastLevel_beliefAura = 15;
	
	private static final int[] striking_atkInfo = {32};
	private static final int[] striking_strInfo = {84};
	private static final int[] wisebless_atkInfo = {23};
	private static final int[] wisebless_intInfo = {101};
	private static final int[] dawnblessInfo = {55};
	private static final int[] gloryblessInfo =
		{ 209, 217, 224, 234, 241, 250, 258, 267 };
	private static final int[] aporkalypseInfo = 
		{ 450, 494, 542, 591, 642, 696 };
	private static final int[] beliefAuraInfo=
		{ 154 };
	
	public static int getStriking_atk(int level) throws UnknownInformationException
	{ 
		try{
			return striking_atkInfo[level-firstLevel_striking]; 
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new UnknownInformationException();
		}
	}
	public static int getStriking_str(int level) throws UnknownInformationException
	{ 
		try{
			return striking_strInfo[level-firstLevel_striking]; 
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new UnknownInformationException();
		}
	}
	
	public static int getWisebless_atk(int level) throws UnknownInformationException
	{ 
		try{
			return wisebless_atkInfo[level-firstLevel_wisebless]; 
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new UnknownInformationException();
		}
	}
	
	public static int getWisebless_int(int level) throws UnknownInformationException
	{ 
		try{
			return wisebless_intInfo[level-firstLevel_wisebless]; 
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new UnknownInformationException();
		}
	}

	public static int getDawnblessInfo(int level) throws UnknownInformationException
	{ 
		try{
			return dawnblessInfo[level-firstLevel_dawnbless]; 
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new UnknownInformationException();
		}
	}
	public static int getGloryblessInfo(int level) throws UnknownInformationException
	{ 
		try{
			return gloryblessInfo[level-firstLevel_glorybless]; 
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new UnknownInformationException();
		}
	}
	public static int getAprokalypseInfo(int level) throws UnknownInformationException
	{ 
		try{
			return aporkalypseInfo[level-firstLevel_aporkalypse]; 
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new UnknownInformationException();
		}
	}
	
	public static int getBeliefAuraInfo(int level) throws UnknownInformationException
	{ 
		try{
			return beliefAuraInfo[level-firstLevel_beliefAura]; 
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new UnknownInformationException();
		}
	}
}
