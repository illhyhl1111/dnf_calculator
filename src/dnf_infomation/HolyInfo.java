package dnf_infomation;

import dnf_InterfacesAndExceptions.UnknownInformationException;

public class HolyInfo {
	public static final int firstLevel_striking = 10;
	public static final int lastLevel_striking = 39;
	public static final int firstLevel_wisebless = 10;
	public static final int lastLevel_wisebless = 32;
	public static final int firstLevel_dawnbless = 10;
	public static final int lastLevel_dawnbless = 37;
	public static final int firstLevel_glorybless = 36;
	public static final int lastLevel_glorybless = 52;
	public static final int firstLevel_aporkalypse = 9;
	public static final int lastLevel_aporkalypse = 26;
	public static final int firstLevel_beliefAura = 15;
	public static final int lastLevel_beliefAura = 23;
	
	private static final int[] striking_atkInfo = 
		{8,9,11,12,13,14,16,17,18,19,21,22,23,24,26,27,28,29,31,32,33,35,36,37,38,40,41,42,43,45};
	private static final int[] striking_strInfo = 
		{15,19,22,27,30,33,37,40,45,48,52,55,58,63,66,70,73,77,81,84,88,91,95,99,102,106,109,113,117,120};
	private static final int[] wisebless_atkInfo = 
		{6,7,8,9,10,11,12,13,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29};
	private static final int[] wisebless_intInfo = 
		{26,30,35,39,45,49,54,58,64,68,73,78,82,87,91,97,101,106,111,116,120,124,130};
	private static final int[] dawnblessInfo = 
		{23,25,27,29,31,33,35,37,39,41,43,45,47,49,51,53,55,57,59,61,63,65,67,68,71,73,75,77};
	private static final int[] gloryblessInfo =
		{194,201,209,217,224,234,241,250,258,267,275,284,293,303,311,318,328};
	private static final int[] aporkalypseInfo = 
		{287,325,364,406,450,494,542,591,642,696,752,810,869,930,994,1059,1128,1197};
	private static final int[] beliefAuraInfo=
		{154,166,177,189,202,213,226,239,253};
	
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
