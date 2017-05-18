package dnf_infomation;

import java.util.HashMap;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_class.Equipment;

public class ReinforceInfo {
	
	private static int[] Epic90Dim = 
		{ 7,9,11,13,15,19,23,27,31,33,41,74,108,-1,-1,312,-1,-1 };
	private static int[] Epic85Dim = 
		{ 7,8,10,12,14,18,22,26,30,32,40,71,105,138,219,301,384,531 };
	private static int[] Epic80Dim = 
		{ 7,8,10,12,14,18,22,25,29,31,39,69,101,133,211,290,369,511 };
	private static int[] Epic75Dim =
		{ 7,8,10,12,14,17,21,25,29,30,37,66,97,128,203,279,355,491 };
	private static int[] Legendary90Dim =
		{ 7,8,10,12,14,18,21,26,30,31,39,69,102,134,-1,-1,-1, -1 };	//14강부터 모름
	private static int[] Legendary85Dim =
		{ 6,7,9,11,13,17,20,24,28,29,37,66,97,128,204,281,358, 495 };
	private static int[] Chronicle70Dim =
		{ 5,6,7,8,10,12,15,17,20,21,26,46,68,89,141,194,-1,-1 };		//13강 보장 60
	private static int[] Chronicle85Dim =
		{ 5,6,7,9,10,13,16,19,22,23,29,52,76,100,158,219,-1,-1 };
	private static int[] Unique90Dim =
		{ 6,7,9,11,13,17,19,23,27,28,35,63,93,-1,-1,-1,-1, -1 };	//13강부터 모름
	private static int[] Rare90Dim =
		{ -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, -1 };	//걍모름
	
	private static int[] Epic90Aid = 
		{ 0,4,6,8,10,13,15,17,20,23,26,48,74,97,-1,-1,-1,-1};
	private static int[] Epic85Aid = 
		{ 0,4,6,8,10,12,14,17,19,22,25,46,70,93,140,195,250,-1};
	private static int[] Epic80Aid = 
		{ 0,3,5,7,9,11,13,16,18,21,23,44,67,85,-1,-1,-1,-1 };
	private static int[] Epic75Aid =
		{ 0,3,5,7,9,11,13,15,18,20,22,42,64,85,128,-1,-1,-1 };
	private static int[] Legendary90Aid =
		{ 0,3,5,7,9,11,13,16,19,21,24,45,67,85,128,-1,-1,-1 };
	private static int[] Legendary85Aid =
		{ 0,3,5,7,9,11,13,15,18,20,23,43,64,86,-1,-1,-1,-1 };
	private static int[] Chronicle70Aid =
		{ 0,2,4,5,6,8,9,11,12,14,16,30,45,60,-1,-1,-1,-1 };		//13강 보장 60
	private static int[] Chronicle85Aid =
		{ 0,3,4,6,7,9,10,12,14,16,18,34,51,69,103,144,-1,-1 };
	
	private static int[] Epic90Earring_atk = 
		{ 0, 5, 7, 10, 13, 15, 19, 23, 26, 30, 39, 61, 91, -1, -1, 254, -1, -1 };
	private static int[] Epic90Earring_fix = 
		{ 0, 6, 9, 12, 15, 18, 22, 26, 30, 34, 45, 70, 105, -1, -1, -1, -1, -1 };
	private static int[] Legendary90Earring_atk = 
		{ 0, 4, 6, 9, -1, -1, -1, 21, 24, 29, 36, 56, 84, 112, -1, -1, -1, -1 };
	private static int[] Legendary90Earring_fix = 
		{ 0, 5, 8, 11, -1, -1, -1, 24, 28, 34, 41, 64, 97, 129, -1, -1, -1, -1 };
	private static int[] Unique90Earring_atk = 
		{ 0, 4, 6, 8, 10, 13, 16, 19, -1, -1, 33, 51, 77, -1, -1, -1, -1, -1 };
	private static int[] Unique90Earring_fix = 
		{ 0, 5, 7, 10, 12, 15, 18, 22, -1, -1, 38, 59, 89, -1, -1, -1, -1, -1 };
	private static int[] Rare90Earring_atk = 
		{ 0, 3, 4, 6, 8, 10, 13, 15, 17, 20, 26, 40, 61, 81, -1, -1, -1, -1 };
	private static int[] Rare90Earring_fix = 
		{ 0, 4, 6, 8, 10, 12, 15, 17, 20, 23, 30, 46, 70, 93, -1, -1, -1, -1 };
	
	private static int[] Epic90Reforge=
		{ 0, 38, 57, 75, 113, 151, 245, 339, 471 };
	private static int[] Epic85Reforge=
		{ 0, 36, 54, 72, 108, 144, 233, 323, 449 };
	private static int[] Epic80Reforge=
		{ 0, 34, 51, 68, 102, 136, 221, 307, 426 };
	private static int[] Epic75Reforge=
		{ 0, 32, 48, 65, 97, 129, 210, 290, 403 };
	private static int[] Legendary90Reforge=
		{ 0, 35, 52, 70, 104, 139, 226, 313, 435 };
	private static int[] Legendary85Reforge=
		{ 0, 33, 50, 66, 99, 132, 215, 298, 413 };
	
	private static double[] reinforceInfo=
		{ 0, 1.8, 2.4, 3.3, 4.3, 5.3, 6.4, 7.6, 10.1, 13.5, 17.2,
				24.8, 33.805, 39.61, 45.31, 51.1, 56.8525, 62.611, 68.3695, 74.128, 79.8865};
	private static HashMap<Item_rarity, Double> rarityInfo = new HashMap<Item_rarity, Double>();
	private static HashMap<Item_rarity, Integer> rarityInfo2 = new HashMap<Item_rarity, Integer>();
	private static HashMap<Weapon_detailType, Double> weaponTypeInfo_phy = new HashMap<Weapon_detailType, Double>();
	private static HashMap<Weapon_detailType, Double> weaponTypeInfo_mag = new HashMap<Weapon_detailType, Double>();
	private static HashMap<Item_rarity, Integer> mastery_rarity = new HashMap<Item_rarity, Integer>();
	private static HashMap<Equip_part, Double> mastery_part = new HashMap<Equip_part, Double>();
	
	private static HashMap<Job, MasteryInfo> masteryMap = new HashMap<Job, MasteryInfo>();
	
	private static boolean readed=false;
	
	private static void readInfo(){
		rarityInfo.put(Item_rarity.EPIC, 1.45); rarityInfo2.put(Item_rarity.EPIC, 14);
		rarityInfo.put(Item_rarity.LEGENDARY, 1.35); rarityInfo2.put(Item_rarity.LEGENDARY, 13);
		rarityInfo.put(Item_rarity.UNIQUE, 1.25); rarityInfo2.put(Item_rarity.UNIQUE, 12);
		rarityInfo.put(Item_rarity.CHRONICLE, 1.1); rarityInfo2.put(Item_rarity.CHRONICLE, 11);
		rarityInfo.put(Item_rarity.RARE, 1.0); rarityInfo2.put(Item_rarity.RARE, 10);
		
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_HCANON, 1.0054); weaponTypeInfo_mag.put(Weapon_detailType.GUN_HCANON, 0.9676);
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_MUSKET, 1.0); weaponTypeInfo_mag.put(Weapon_detailType.GUN_MUSKET, 0.9865);
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_REVOLVER, 0.9883); weaponTypeInfo_mag.put(Weapon_detailType.GUN_REVOLVER, 0.9793);
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_BOWGUN, 0.9784); weaponTypeInfo_mag.put(Weapon_detailType.GUN_BOWGUN, 0.9865);
		weaponTypeInfo_phy.put(Weapon_detailType.GUN_AUTOPISTOL, 0.9676); weaponTypeInfo_mag.put(Weapon_detailType.GUN_AUTOPISTOL, 0.9946);
		
		weaponTypeInfo_phy.put(Weapon_detailType.SWORD_LONGSWORD, 1.018); weaponTypeInfo_mag.put(Weapon_detailType.SWORD_LONGSWORD, 0.9910);
		weaponTypeInfo_phy.put(Weapon_detailType.SWORD_CLUB, 1.009); weaponTypeInfo_mag.put(Weapon_detailType.SWORD_CLUB, 1.0045);
		weaponTypeInfo_phy.put(Weapon_detailType.SWORD_SHORTSWORD, 0.9955); weaponTypeInfo_mag.put(Weapon_detailType.SWORD_SHORTSWORD, 1.0135);
		weaponTypeInfo_phy.put(Weapon_detailType.SWORD_KATANA, 0.9955); weaponTypeInfo_mag.put(Weapon_detailType.SWORD_KATANA, 1.0045);
		weaponTypeInfo_phy.put(Weapon_detailType.SWORD_LIGHTSWORD, 0.9937); weaponTypeInfo_mag.put(Weapon_detailType.SWORD_LIGHTSWORD, 0.9910);
		
		weaponTypeInfo_phy.put(Weapon_detailType.MAGE_BROOM, 1.0); weaponTypeInfo_mag.put(Weapon_detailType.MAGE_BROOM, 1.009);
		weaponTypeInfo_phy.put(Weapon_detailType.MAGE_POLE, 1.0072); weaponTypeInfo_mag.put(Weapon_detailType.MAGE_POLE, 0.9910);
		weaponTypeInfo_phy.put(Weapon_detailType.MAGE_ROD, 0.9910); weaponTypeInfo_mag.put(Weapon_detailType.MAGE_ROD, 1.009);
		weaponTypeInfo_phy.put(Weapon_detailType.MAGE_SPEAR, 1.018); weaponTypeInfo_mag.put(Weapon_detailType.MAGE_SPEAR, 0.9865);
		weaponTypeInfo_phy.put(Weapon_detailType.MAGE_STAFF, 0.9955); weaponTypeInfo_mag.put(Weapon_detailType.MAGE_STAFF, 1.009);
		
		weaponTypeInfo_phy.put(Weapon_detailType.PRIEST_CROSS, 1.0); weaponTypeInfo_mag.put(Weapon_detailType.PRIEST_CROSS, 0.9955);
		weaponTypeInfo_phy.put(Weapon_detailType.PRIEST_ROSARY, 0.9910); weaponTypeInfo_mag.put(Weapon_detailType.PRIEST_ROSARY, 1.0135);
		weaponTypeInfo_phy.put(Weapon_detailType.PRIEST_TOTEM, 1.0045); weaponTypeInfo_mag.put(Weapon_detailType.PRIEST_TOTEM, 0.9910);
		weaponTypeInfo_phy.put(Weapon_detailType.PRIEST_SCYTHE, 1.0045); weaponTypeInfo_mag.put(Weapon_detailType.PRIEST_SCYTHE, 1.0045);
		weaponTypeInfo_phy.put(Weapon_detailType.PRIEST_BATTLEAXE, 1.018); weaponTypeInfo_mag.put(Weapon_detailType.PRIEST_BATTLEAXE, 0.9865);
		
		weaponTypeInfo_phy.put(Weapon_detailType.FIGHTER_CLAW, 1.0); weaponTypeInfo_mag.put(Weapon_detailType.FIGHTER_CLAW, 1.0);
		weaponTypeInfo_phy.put(Weapon_detailType.FIGHTER_GAUNTLET, 1.018); weaponTypeInfo_mag.put(Weapon_detailType.FIGHTER_GAUNTLET, 0.9910);
		weaponTypeInfo_phy.put(Weapon_detailType.FIGHTER_KNUCKLE, 0.9955); weaponTypeInfo_mag.put(Weapon_detailType.FIGHTER_KNUCKLE, 1.0135);
		weaponTypeInfo_phy.put(Weapon_detailType.FIGHTER_TONFA, 0.9955); weaponTypeInfo_mag.put(Weapon_detailType.FIGHTER_TONFA, 1.0);
		weaponTypeInfo_phy.put(Weapon_detailType.FITGHTER_BOXGLOVE, 1.0045); weaponTypeInfo_mag.put(Weapon_detailType.FITGHTER_BOXGLOVE, 0.9955);
		
		weaponTypeInfo_phy.put(Weapon_detailType.LANCE_HALBERD, 1.018); weaponTypeInfo_mag.put(Weapon_detailType.LANCE_HALBERD, 0.9865);
		weaponTypeInfo_phy.put(Weapon_detailType.LANCE_PIKE, 1.0045); weaponTypeInfo_mag.put(Weapon_detailType.LANCE_PIKE, 0.9910);
		
		weaponTypeInfo_phy.put(Weapon_detailType.THIEF_CHAKRAWEAPON, 0.9755); weaponTypeInfo_mag.put(Weapon_detailType.THIEF_CHAKRAWEAPON, 1.009);
		weaponTypeInfo_phy.put(Weapon_detailType.THIEF_DAGGER, 0.9865); weaponTypeInfo_mag.put(Weapon_detailType.THIEF_DAGGER, 0.9901);
		weaponTypeInfo_phy.put(Weapon_detailType.THIEF_TWINSWORD, 1.0018); weaponTypeInfo_mag.put(Weapon_detailType.THIEF_TWINSWORD, 0.9820);
		weaponTypeInfo_phy.put(Weapon_detailType.THIEF_WAND, 0.9829); weaponTypeInfo_mag.put(Weapon_detailType.THIEF_WAND, 1.0135);
		
		mastery_rarity.put(Item_rarity.EPIC, 17);
		mastery_rarity.put(Item_rarity.LEGENDARY, 14);
		mastery_rarity.put(Item_rarity.UNIQUE, 11);
		mastery_rarity.put(Item_rarity.CHRONICLE, 8);
		mastery_rarity.put(Item_rarity.RARE, 5);
		mastery_rarity.put(Item_rarity.UNCOMMON, 0);
		mastery_rarity.put(Item_rarity.COMMON, -3);
		
		mastery_part.put(Equip_part.ROBE, 0.3);
		mastery_part.put(Equip_part.TROUSER, 0.25);
		mastery_part.put(Equip_part.SHOULDER, 0.2);
		mastery_part.put(Equip_part.BELT, 0.1);
		mastery_part.put(Equip_part.SHOES, 0.15);
		
		// 패치 이후로 부위 외에 의미 없음
		masteryMap.put(Job.WEAPONMASTER, new MasteryInfo(Equip_type.MAIL, 36, 0, 1.8, 0, 0, 0));
		masteryMap.put(Job.SOULMASTER, new MasteryInfo(Equip_type.FABRIC, 0, 24, 0, 1.2, 0, 0));
		masteryMap.put(Job.BUSERKER, new MasteryInfo(Equip_type.HEAVY, 32, 0, 1.6, 0, 0, 0));
		masteryMap.put(Job.ASURA, new MasteryInfo(Equip_type.PLATE, 0, 20, 0, 1, 0, 0));
		masteryMap.put(Job.NENMASTER_F, new MasteryInfo(Equip_type.FABRIC, 0, 40, 0, 2, 10, 0));
		masteryMap.put(Job.NENMASTER_M, new MasteryInfo(Equip_type.FABRIC, 0, 24, 0, 1.2, 0, 0));
		masteryMap.put(Job.STRIKER_F, new MasteryInfo(Equip_type.MAIL, 40, 0, 2, 0, 5, 0));
		masteryMap.put(Job.STRIKER_M, new MasteryInfo(Equip_type.MAIL, 40, 0, 2, 0, 5, 0));
		masteryMap.put(Job.STREETFIGHTER_F, new MasteryInfo(Equip_type.HEAVY, 24, 24, 1.2, 1.2, 0, 0));
		masteryMap.put(Job.STREETFIGHTER_M, new MasteryInfo(Equip_type.HEAVY, 24, 24, 1.2, 1.2, 0, 0));
		masteryMap.put(Job.GRAPPLER_F, new MasteryInfo(Equip_type.MAIL, 30, 0, 1.5, 0, 0, 0));
		masteryMap.put(Job.GRAPPLER_M, new MasteryInfo(Equip_type.MAIL, 30, 0, 1.5, 0, 0, 0));
		masteryMap.put(Job.RANGER_F, new MasteryInfo(Equip_type.LEATHER, 24, 0, 1.2, 0, 15, 0));
		masteryMap.put(Job.RANGER_M, new MasteryInfo(Equip_type.LEATHER, 24, 0, 1.2, 0, 15, 0));
		masteryMap.put(Job.LAUNCHER_F, new MasteryInfo(Equip_type.HEAVY, 30, 0, 1.5, 0, 0, 0));
		masteryMap.put(Job.LAUNCHER_M, new MasteryInfo(Equip_type.HEAVY, 30, 0, 1.5, 0, 0, 0));
		masteryMap.put(Job.MECHANIC_F, new MasteryInfo(Equip_type.FABRIC, 0, 24, 0, 1.2, 0, 0));
		masteryMap.put(Job.MECHANIC_M, new MasteryInfo(Equip_type.FABRIC, 0, 24, 0, 1.2, 0, 0));
		masteryMap.put(Job.SPITFIRE_F, new MasteryInfo(Equip_type.LEATHER, 24, 24, 1.2, 1.2, 10, 10));
		masteryMap.put(Job.SPITFIRE_M, new MasteryInfo(Equip_type.LEATHER, 24, 24, 1.2, 1.2, 10, 10));
		masteryMap.put(Job.ELEMENTALMASTER, new MasteryInfo(Equip_type.FABRIC, 0, 30, 0, 1.5, 0, 3));
		masteryMap.put(Job.ELEMENTALBOMBER, new MasteryInfo(Equip_type.FABRIC, 0, 33, 0, 1.5, 0, 3));
		masteryMap.put(Job.SUMMONER, new MasteryInfo(Equip_type.FABRIC, 0, 24, 0, 1.2, 0, 0));
		masteryMap.put(Job.BATTLEMAGE, new MasteryInfo(Equip_type.LEATHER, 20, 20, 1, 1, 5, 0));
		masteryMap.put(Job.WITCH, new MasteryInfo(Equip_type.LEATHER, 0, 30, 0, 1.5, 0, 5));
		masteryMap.put(Job.CRUSADER, new MasteryInfo(Equip_type.PLATE, 0, 2, 0, 0.5, 0, 0));
		masteryMap.put(Job.INFIGHTER, new MasteryInfo(Equip_type.MAIL, 40, 0, 2, 0, 0, 0));
		masteryMap.put(Job.EXORCIST, new MasteryInfo(Equip_type.PLATE, 30, 0, 1.5, 0, 0, 0));
		masteryMap.put(Job.ROUGE, new MasteryInfo(Equip_type.LEATHER, 30, 0, 1.5, 0, 10, 0));
		masteryMap.put(Job.SHADOWDANCER, new MasteryInfo(Equip_type.LEATHER, 30, 0, 1.5, 0, 10, 0));
		masteryMap.put(Job.NECROMENCER, new MasteryInfo(Equip_type.MAIL, 0, 30, 0, 1.5, 0, 0));
		masteryMap.put(Job.DEMONSLAYER, new MasteryInfo(Equip_type.HEAVY, 30, 0, 1.5, 0, 10, 0));
		masteryMap.put(Job.VANGUARD, new MasteryInfo(Equip_type.HEAVY, 32, 0, 1.6, 0, 10, 0));
		masteryMap.put(Job.ELVENKNIGHT, new MasteryInfo(Equip_type.PLATE, 30, 0, 1.5, 0, 10, 0));
		masteryMap.put(Job.CREATOR, new MasteryInfo(Equip_type.PLATE, 0, 20, 0, 1, 0, 0));
		readed=true;
	}
	
	public static int getReinforceWeaponInfo_phy(int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		if(!readed) readInfo();
		return (int) ( (level+rarityInfo2.get(rarity))/8*reinforceInfo[num]*rarityInfo.get(rarity)*weaponTypeInfo_phy.get(type)*1.1);
	}
	public static int getReinforceWeaponInfo_mag(int num, Item_rarity rarity, int level, Weapon_detailType type) throws UnknownInformationException
	{
		if(!readed) readInfo();
		return (int) ( (level+rarityInfo2.get(rarity))/8*reinforceInfo[num]*rarityInfo.get(rarity)*weaponTypeInfo_mag.get(type)*1.1);
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
		case LEGENDARY:
			if(level==90){
				atk=Legendary90Earring_atk[num];
				fix=Legendary90Earring_fix[num];
			}
			break;
		case UNIQUE:
			if(level==90){
				atk=Unique90Earring_atk[num];
				fix=Unique90Earring_fix[num];
			}
			break;
		case RARE:
			if(level==90){
				atk=Rare90Earring_atk[num];
				fix=Rare90Earring_fix[num];
			}
			break;
		default:
			break;
		}
		if(atk==-1 || fix==-1) throw new UnknownInformationException();
		//return new int[] {atk, fix};
		return new int[] {fix, fix}; //강화패치
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
		case UNIQUE:
			if(level==90) temp=Unique90Dim[num];
			break;
		case RARE:
			if(level==90) temp=Rare90Dim[num];
			break;
		default:
			if(num==0) temp=3;			//TODO
		}
		
		if(temp==-1){
			if(num==0) return 0;
			else throw new UnknownInformationException();
		}
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
		
		if(temp==-1){
			if(num==0) return 0;
			else throw new UnknownInformationException();
		}
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
		
		if(temp==-1){
			if(num==0) return 0;
			else throw new UnknownInformationException();
		}
		return temp;
	}

	public static int getMastery_strInfo(Job job, Equipment equipment)
	{
		if(!readed) readInfo();
		if(equipment.getName().contains("없음")) return 0;
		
		double rate = 1.0;
		MasteryInfo info = masteryMap.get(job);
		if(info==null || (info.type!=equipment.getEquipType() && equipment.getEquipType()!=Equip_type.ALL)){
			if(job==Job.EXORCIST && (equipment.getEquipType()==Equip_type.HEAVY || equipment.getEquipType()==Equip_type.FABRIC))
				info = new MasteryInfo(Equip_type.HEAVY, 20, 0, 1, 0, 0, 0);
			else rate = 0.4;
		}
		//return (int)( (info.basic_str+info.inc_str*(mastery_rarity.get(equipment.getRarity())+equipment.getReinforce()/3+equipment.level))*mastery_part.get(equipment.getPart()) );
		if(info.basic_str==0) return 0;
		return (int) ((20+2.5*(mastery_rarity.get(equipment.getRarity())+equipment.getReinforce()/3+equipment.level))*mastery_part.get(equipment.getPart())*rate);
	}
	public static int getMastery_intInfo(Job job, Equipment equipment)
	{
		if(!readed) readInfo();
		if(equipment.getName().contains("없음")) return 0;
		
		double rate = 1.0;
		MasteryInfo info = masteryMap.get(job);
		if(info==null || (info.type!=equipment.getEquipType() && equipment.getEquipType()!=Equip_type.ALL)){
			if(job==Job.EXORCIST && (equipment.getEquipType()==Equip_type.HEAVY || equipment.getEquipType()==Equip_type.FABRIC))
				info = new MasteryInfo(Equip_type.FABRIC, 0, 24, 0, 1.2, 0, 0);
			else rate = 0.4;
		}
		//return (int)( (info.basic_int+info.inc_int*(mastery_rarity.get(equipment.getRarity())+equipment.getReinforce()/3+equipment.level))*mastery_part.get(equipment.getPart()) );
		if(info.basic_int==0) return 0;
		return (int) ((20+2.5*(mastery_rarity.get(equipment.getRarity())+equipment.getReinforce()/3+equipment.level))*mastery_part.get(equipment.getPart())*rate);
	}
	public static double getMastery_phyCrtInfo(Job job, Equipment equipment)
	{
		if(!readed) readInfo();
		if(equipment.getName().contains("없음")) return 0;
		
		double rate = 1.0;
		MasteryInfo info = masteryMap.get(job);
		if(info==null || (info.type!=equipment.getEquipType() && equipment.getEquipType()!=Equip_type.ALL))
			rate = 0.5;
		if(info.basic_str==0) return 0;
		return 10*rate*mastery_part.get(equipment.getPart());
	}
	public static double getMastery_magCrtInfo(Job job, Equipment equipment)
	{
		if(!readed) readInfo();
		if(equipment.getName().contains("없음")) return 0;
		
		double rate = 1.0;
		MasteryInfo info = masteryMap.get(job);		
		if(info==null || (info.type!=equipment.getEquipType() && equipment.getEquipType()!=Equip_type.ALL))
			rate = 0.5;
		if(info.basic_int==0) return 0;
		return 10*rate*mastery_part.get(equipment.getPart());
	}
}

class MasteryInfo{
	Equip_type type;
	int basic_str;
	int basic_int;
	int phy_crt;
	int mag_crt;
	double inc_str;
	double inc_int;
	
	public MasteryInfo(Equip_type type, int basic_str, int basic_int, double inc_str, double inc_int, int phy_crt, int mag_crt){
		this.type=type;
		this.basic_str=basic_str;
		this.basic_int=basic_int;
		this.inc_str=inc_str;
		this.inc_int=inc_int;
		this.phy_crt=phy_crt;
		this.mag_crt=mag_crt;
	}
}