package dnf_infomation;

import java.util.Arrays;
import java.util.HashSet;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Equipment;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_calculator.FunctionStat;
import dnf_calculator.StatusList;

public class EquipInfo_heavy {
	
	public static void getInfo(HashSet<Equipment> equipList, Object[] data) throws ParsingException
	{
		int i=0;
		String name=null;
		String icon=null;
		Item_rarity rarity=null;
		Equip_part part=null;
		Equip_type type=null;
		SetName setName=null;
		int level=0;
		String[] stat=null;
		
		Equipment equipment;
		Object temp;
		
		while(i<data.length)
		{
			name = (String) data[i++];
			icon = (String) data[i++];
			
			//레어리티
			temp = data[i++];
			if(temp instanceof Item_rarity) rarity = (Item_rarity) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//부위
			temp = data[i++];
			if(temp instanceof Equip_part) part = (Equip_part) temp;
			else if(temp.equals(""));	//이전 값 유지
			else if(temp.equals("--")){
				int order = part.order-1;
				part = Equip_part.getPartFromOrder(order);
			}
			else throw new ParsingException(i-1, temp);
			
			//재질
			temp = data[i++];
			if(temp instanceof Equip_type) type = (Equip_type) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//셋옵
			temp = data[i++];
			if(temp instanceof SetName) setName = (SetName) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//레벨
			temp = data[i++];
			if(temp instanceof Integer) level = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			equipment = new Equipment(name, icon, rarity, part, type, setName, level);
			
			//아이템 스탯
			while(true)
			{	
				try{
					temp = data[i++];
					if(temp==null) break;
					
					else if(temp instanceof FunctionStat)
						equipment.fStat.statList.add((FunctionStat) temp);
					
					else{
						stat = ((String)temp).split(" ");					
						switch(stat[0])
						{
						case "ㄷ": case "d":
							parseStat(Arrays.copyOfRange(stat, 1, stat.length), equipment.dStat);
							break;
						case "설명":
							String explanation = ((String)temp).substring(3);
							equipment.explanation.add(explanation);
							break;
						case "ㅁ": case "v":
							parseStat(Arrays.copyOfRange(stat, 1, stat.length), equipment.vStat);
							break;
						default:
							parseStat(stat, equipment.vStat);
							break;
						}
					}
				}
				catch(Exception e)
				{
					throw new ParsingException(i-1, temp);
				}
			}
			
			equipList.add(equipment);
		}
	}
	
	private static void parseStat(String[] data, StatusList list)
	{
		boolean changeable = false;
		boolean enableable = false;
		
		if(data.length>2 && (data[data.length-1].equals("가변") || data[data.length-2].equals("가변")) ) changeable = true;
		if(data.length>2 && (data[data.length-1].equals("선택") || data[data.length-2].equals("선택")) ) enableable = true;
		
		if(data[0].contains("스킬"))
		{
			String[] skillRange = data[1].split("-");
			try{
				int start = Integer.valueOf(skillRange[0]);
				int end = start;
				if(skillRange.length>1) end = Integer.valueOf(skillRange[1]);
				boolean TP=false;
				if(data[0].equals("TP스킬")) TP=true;
				list.addSkillRange(start, end, Integer.valueOf(data[2]), TP);
			}
			catch(NumberFormatException e) {
				list.addSkill(data[1], Integer.valueOf(data[2]));
			}
		}
		else if(!data[1].contains("."))
			list.addStatList(data[0], Integer.valueOf(data[1]), changeable, enableable);
		else
			list.addStatList(data[0], Double.valueOf(data[1]), changeable, enableable);	
	}
	
	public static Object[] equipInfo_heavy()
	{
		String path = "image\\sprite_item_new_equipment_05_harmor\\";
		
		Object[] data = new Object[] {
			
			/////////에픽
				
			/////미다홀
			"미지의 다크홀 상의", path+"미닼상의.png", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.HEAVY, SetName.DARKHOLE, 80,
			"힘 44 가변", "물크 4", "지능 32 가변", "마크 4", "스킬 45 3", "스킬 60 2", null,
			"미지의 다크홀 하의", path+"미닼하의.png", "", "--", "", "", "",
			"힘 44 가변", "물크 4", "지능 32 가변", "마크 4", "스킬 45 3", "스킬 60 2", null,
			"미지의 다크홀 어깨", path+"미닼어깨.png", "", "--", "", "", "",
			"힘 35 가변", "지능 25 가변", "증뎀 12", null,
			"미지의 다크홀 벨트", path+"미닼벨트.png", "", "--", "", "", "",
			"힘 26 가변", "지능 20 가변", "ㄷ 물크 15", "ㄷ 마크 15", null,
			"미지의 다크홀 신발", path+"미닼벨트.png", "", "--", "", "", "",
			"힘 81 가변", "지능 75 가변", null,
			/////거미
			"타란튤라 상의", path+"거미상의.png", "", Equip_part.ROBE, "", SetName.SPIDERQUEEN, 85,
			"힘 200 가변", "지능 188 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
			"킹바분 하의", path+"거미하의.png", "", "--", "", "", "",
			"힘 200 가변", "지능 188 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
			"골리앗 버드이터 어깨", path+"거미어깨.png", "", "--", "", "", "",
			"힘 147 가변", "지능 138 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
			"로즈헤어 벨트", path+"거미벨트.png", "", "--", "", "", "",
			"힘 138 가변", "지능 130 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
			"인디언 오너멘탈 신발", path+"거미신발.png", "", "--", "", "", "",
			"힘 138 가변", "지능 130 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
			/////금계
			"피의 맹약 상의", path+"금계상의.png", "", Equip_part.ROBE, "", SetName.FORBIDDENCONTRACT, "",
			"힘 266 가변", "지능 254 가변", "d 힘 200 가변", "d 지능 200 가변", null,
			"마나의 서약 하의", path+"금계하의.png", "", "--", "", "", "",
			"힘 46 가변", "지능 34 가변", "물크 15", "마크 15", "ㄷ 물크 20 가변", "ㄷ 마크 20 가변", null,
			"마력의 계약 숄더", path+"금계어깨.png", "", "--", "", "", "",
			"힘 37 가변", "지능 28 가변", "증뎀 15", null,
			"체력의 협약 벨트", path+"금계벨트.png", "", "--", "", "", "",
			"힘 28 가변", "지능 20 가변", "ㄷ 물공 100 가변", "ㄷ 마공 100 가변", "ㄷ 독공 100 가변", null,
			"피의 조약 부츠", path+"금계신발.png", "", "--", "", "", "",
			"힘 28 가변", "지능 20 가변", "모속강 14", "ㄷ 물공 80", "ㄷ 마공 80", "ㄷ 독공 100", null,
		};
		
		return data;
	}
