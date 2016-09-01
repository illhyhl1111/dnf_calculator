package dnf_infomation;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_InterfacesAndExceptions.Weapon_detailType;

public class ReinforceInfo {
	
	private static int[] Epic85Dim = 
		{ 7,8,10,12,14,18,22,26,30,32,40,71,105,138,219,301,384,530 };
	private static int[] Epic80Dim = 
		{ 7,8,10,12,14,18,22,25,29,31,39,69,101,133,211,290,369,511 };
	private static int[] Epic75Dim =
		{ 7,8,10,12,14,17,21,25,29,30,37,66,97,128,203,279,355,491 };
	private static int[] Legendary85Dim =
		{ 6,7,9,11,13,17,20,24,28,29,37,66,97,128,204,281,358,-1 };
	private static int[] Chronicle70Dim =
		{ 5,6,7,8,10,12,15,17,20,21,26,46,68,89,141,194,-1,-1 };		//13강 보장 60
	private static int[] Chronicle85Dim =
		{ 5,6,7,9,10,13,16,19,22,23,29,52,76,100,158,219,-1,-1 };
	
	private static int[] Epic85Aid = 
		{ 0,4,6,8,10,12,14,17,19,22,25,46,70,93,140,195,250,-1};
	private static int[] Epic80Aid = 
		{ 0,3,5,7,9,11,13,16,18,21,23,44,67,-1,-1,-1,-1,-1 };
	private static int[] Epic75Aid =
		{ 0,3,5,7,9,11,13,15,18,20,22,42,64,-1,-1,-1,-1,-1 };
	private static int[] Legendary85Aid =
		{ 0,3,5,7,9,11,13,15,18,20,23,43,64,86,-1,-1,-1,-1 };
	private static int[] Chronicle70Aid =
		{ 0,2,4,5,6,8,9,11,12,14,16,30,45,60,-1,-1,-1,-1 };		//13강 보장 60
	private static int[] Chronicle85Aid =
		{ 0,3,4,6,7,9,10,12,14,16,18,34,51,69,103,144,-1,-1 };
	
	public static int getReinforceWeaponInfo_phy(int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		return num;
	}
	public static int getReinforceWeaponInfo_mag(int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		return num;
	}
	
	public static int getDimensionInfo(int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		int temp=-1;
		if(num>17) throw new UnknownInformationException();
		switch(rarity)
		{
		case EPIC:
			if(level==85) temp=Epic85Dim[num];
			else if(level==80) temp=Epic80Dim[num];
			else if(level==75) temp=Epic75Dim[num];
			break;
		case LEGENDARY:
			if(level==85) temp=Legendary85Dim[num];
			break;
		case CHRONICLE:
			if(level==85 || level==86) temp=Chronicle85Dim[num];
			if(level==86 && num>11) temp++;
			else if(level==70) temp=Chronicle70Dim[num];
			break;
		default:
			break;
		}
		
		if(temp==-1) throw new UnknownInformationException();
		return temp;
	}
	
	public static int getAidStatInfo(int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		int temp=-1;
		switch(rarity)
		{
		case EPIC:
			if(level==85) temp=Epic85Aid[num];
			else if(level==80) temp=Epic80Aid[num];
			else if(level==75) temp=Epic75Aid[num];
			break;
		case LEGENDARY:
			if(level==85) temp=Legendary85Aid[num];
			break;
		case CHRONICLE:
			if(level==85 || level==86) temp=Chronicle85Aid[num];
			if(level==86 && num>11) temp++;
			else if(level==70) temp=Chronicle70Aid[num];
			break;
		default:
			break;
		}
		
		if(temp==-1) throw new UnknownInformationException();
		return temp;
	}

}
