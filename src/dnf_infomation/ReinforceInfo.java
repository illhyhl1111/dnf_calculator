package dnf_infomation;

import java.util.HashMap;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_InterfacesAndExceptions.Weapon_detailType;

public class ReinforceInfo {
	
	private static int[] Epic90Dim = 
		{ 7,8,10,12,14,18,22,27,30,32,40,74,108,138,219,301,384,531 };
	private static int[] Epic85Dim = 
		{ 7,8,10,12,14,18,22,26,30,32,40,71,105,138,219,301,384,531 };
	private static int[] Epic80Dim = 
		{ 7,8,10,12,14,18,22,25,29,31,39,69,101,133,211,290,369,511 };
	private static int[] Epic75Dim =
		{ 7,8,10,12,14,17,21,25,29,30,37,66,97,128,203,279,355,491 };
	private static int[] Legendary90Dim =
		{ 6,7,9,11,13,17,20,24,28,29,37,66,97,128,204,281,358, 495 };
	private static int[] Legendary85Dim =
		{ 6,7,9,11,13,17,20,24,28,29,37,66,97,128,204,281,358, 495 };
	private static int[] Chronicle70Dim =
		{ 5,6,7,8,10,12,15,17,20,21,26,46,68,89,141,194,-1,-1 };		//13강 보장 60
	private static int[] Chronicle85Dim =
		{ 5,6,7,9,10,13,16,19,22,23,29,52,76,100,158,219,-1,-1 };
	
	private static int[] Epic90Aid = 
		{ 0,4,6,8,10,12,14,17,19,22,25,46,70,93,140,195,250,-1};
	private static int[] Epic85Aid = 
		{ 0,4,6,8,10,12,14,17,19,22,25,46,70,93,140,195,250,-1};
	private static int[] Epic80Aid = 
		{ 0,3,5,7,9,11,13,16,18,21,23,44,67,-1,-1,-1,-1,-1 };
	private static int[] Epic75Aid =
		{ 0,3,5,7,9,11,13,15,18,20,22,42,64,85,128,-1,-1,-1 };
	private static int[] Legendary90Aid =
		{ 0,3,5,7,9,11,13,15,18,20,23,43,64,86,-1,-1,-1,-1 };
	private static int[] Legendary85Aid =
		{ 0,3,5,7,9,11,13,15,18,20,23,43,64,86,-1,-1,-1,-1 };
	private static int[] Chronicle70Aid =
		{ 0,2,4,5,6,8,9,11,12,14,16,30,45,60,-1,-1,-1,-1 };		//13강 보장 60
	private static int[] Chronicle85Aid =
		{ 0,3,4,6,7,9,10,12,14,16,18,34,51,69,103,144,-1,-1 };
	
	private static int[] Epic90Earring_atk = 
		{ 0, 5, 7, -1, -1, 15, -1, 23, -1, -1, 39, 61, 91, -1, -1, -1, -1, -1 };
	private static int[] Epic90Earring_fix = 
		{ 0, 6, 9, -1, -1, 18, -1, 26, -1, -1, 45, 70, 105, -1, -1, -1, -1, -1 };
	
	private static int[] Epic90Reforge=
		{ 0, 36, 54, 72, 108, 144, 233, 323, 449 };
	private static int[] Epic85Reforge=
		{ 0, 36, 54, 72, 108, 144, 233, 323, 449 };
	private static int[] Epic80Reforge=
		{ 0, 34, 51, 68, 102, 136, 221, 307, 426 };
	private static int[] Epic75Reforge=
		{ 0, 32, 48, 65, 97, 129, 210, 290, 403 };
	private static int[] Legendary90Reforge=
		{ 0, 33, 50, 66, 99, 132, 215, 298, 413 };
	private static int[] Legendary85Reforge=
		{ 0, 33, 50, 66, 99, 132, 215, 298, 413 };
	
	private static int[] reinforceInfo=
		{ 0, 1, 2, 3, 4, 6, 8, 10, 12, 14, 17, 33, 50, 67, 108, 150, 192, 267, 360, 417, 500};
	private static HashMap<Item_rarity, Double> rarityInfo = new HashMap<Item_rarity, Double>();
	private static HashMap<Item_rarity, Integer> rarityInfo2 = new HashMap<Item_rarity, Integer>();
	private static HashMap<Weapon_detailType, Double> weaponTypeInfo_phy = new HashMap<Weapon_detailType, Double>();
	private static HashMap<Weapon_detailType, Double> weaponTypeInfo_mag = new HashMap<Weapon_detailType, Double>();
	private static boolean readed=false;
	
	private static void readInfo(){
		rarityInfo.put(Item_rarity.EPIC, 1.61); rarityInfo2.put(Item_rarity.EPIC, 13);
		rarityInfo.put(Item_rarity.LEGENDARY, 1.4); rarityInfo2.put(Item_rarity.LEGENDARY, 13);
		rarityInfo.put(Item_rarity.UNIQUE, 1.25); rarityInfo2.put(Item_rarity.UNIQUE, 12);
		rarityInfo.put(Item_rarity.CHRONICLE, 1.1); rarityInfo2.put(Item_rarity.CHRONICLE, 11);
		rarityInfo.put(Item_rarity.RARE, 1.0); rarityInfo2.put(Item_rarity.RARE, 10);
		
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_HCANON, 1.06); weaponTypeInfo_mag.put(Weapon_detailType.GUN_HCANON, 0.64);
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_MUSKET, 1.0); weaponTypeInfo_mag.put(Weapon_detailType.GUN_MUSKET, 0.85);
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_REVOLVER, 0.87); weaponTypeInfo_mag.put(Weapon_detailType.GUN_REVOLVER, 0.77);
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_BOWGUN, 0.75); weaponTypeInfo_mag.put(Weapon_detailType.GUN_BOWGUN, 0.85);
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_AUTOPISTOL, 0.6); weaponTypeInfo_mag.put(Weapon_detailType.GUN_AUTOPISTOL, 0.94);
		
		readed=true;
	}
	
	public static int getReinforceWeaponInfo_phy(int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		if(!readed) readInfo();
		return (int) ( (level+rarityInfo2.get(rarity))/8*reinforceInfo[num]*rarityInfo.get(rarity)*weaponTypeInfo_phy.get(type) );
	}
	public static int getReinforceWeaponInfo_mag(int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		if(!readed) readInfo();
		return (int) ( (level+rarityInfo2.get(rarity))/8*reinforceInfo[num]*rarityInfo.get(rarity)*weaponTypeInfo_mag.get(type) );
	}
	
	public static int[] getEarringStatInfo(int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		int atk=-1, fix=-1;
		if(num>17) throw new UnknownInformationException();
		switch(rarity)
		{
		case EPIC:
			if(level==90){
				atk=Epic90Earring_atk[num];
				fix=Epic90Earring_fix[num];
			}
			break;
		default:
			atk=num;
			fix=(int)(atk*1.1);
		}
		if(atk==-1 || fix==-1) throw new UnknownInformationException();
		return new int[] {atk, fix};
	}
	
	public static int getDimensionInfo(int num, Item_rarity rarity, int level) throws UnknownInformationException
	{
		int temp=-1;
		if(num>17) throw new UnknownInformationException();
		switch(rarity)
		{
		case EPIC:
			if(level==90) temp=Epic90Dim[num];
			else if(level==85) temp=Epic85Dim[num];
			else if(level==80) temp=Epic80Dim[num];
			else if(level==75) temp=Epic75Dim[num];
			break;
		case LEGENDARY:
			if(level==90) temp=Legendary90Dim[num];
			if(level==85) temp=Legendary85Dim[num];
			else temp=Legendary85Dim[num];
			break;
		case CHRONICLE:
			if(level==85 || level==86) temp=Chronicle85Dim[num];
			if(level==86 && num>11) temp++;
			else if(level==70) temp=Chronicle70Dim[num];
			break;
		default:
			if(num==0) temp=3;			//TODO
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
			if(level==90) temp=Epic90Aid[num];
			else if(level==85) temp=Epic85Aid[num];
			else if(level==80) temp=Epic80Aid[num];
			else if(level==75) temp=Epic75Aid[num];
			break;
		case LEGENDARY:
			if(level==90) temp=Legendary90Aid[num];
			else if(level==85) temp=Legendary85Aid[num];
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
	
	public static int getReforgeInfo(int num, Item_rarity rarity, int level)  throws UnknownInformationException
	{
		int temp=-1;
		if(num>8) throw new UnknownInformationException();
		switch(rarity)
		{
		case EPIC:
			if(level==90) temp=Epic90Reforge[num];
			else if(level==85) temp=Epic85Reforge[num];
			else if(level==80) temp=Epic80Reforge[num];
			else if(level==75) temp=Epic75Reforge[num];
			break;
		case LEGENDARY:
			if(level==90) temp=Legendary90Reforge[num];
			if(level==85) temp=Legendary85Reforge[num];
			break;
		default:
			if(num==0) temp=(level-10)*num;			//TODO
		}
		
		if(temp==-1) throw new UnknownInformationException();
		return temp;
	}

}
