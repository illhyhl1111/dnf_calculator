package dnf_infomation;

import java.util.Arrays;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;
import dnf_class.Skill;
import dnf_class.Weapon;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_calculator.CalculateElement;
import dnf_calculator.FunctionStat;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_calculator.StatusList;

public class EquipmentInfo {
	
	public static void getInfo(LinkedList<Equipment> equipList, Object[] data) throws ParsingException
	{
		int i=0;
		String name=null;
		Item_rarity rarity=null;
		Equip_part part=null;
		boolean isWeapon=false;
		Equip_type type=null;
		Weapon_detailType weaponType=null;
		SetName setName=null;
		int level=0;
		boolean isRare=false;
		boolean optionDisable=false; 	// Disable init option 
		String version=null;
		String[] stat=null;
		
		Equipment equipment;
		Object temp="first";
		
		while(i<data.length)
		{
			name = (String) data[i++];
			try{
				//레어리티
				temp = data[i++];
				if(temp instanceof Item_rarity) rarity = (Item_rarity) temp;
				else if(temp.equals(""));	//이전 값 유지
				else throw new ParsingException(i-1, temp);
				
				//부위
				temp = data[i++];
				if(temp instanceof Equip_part){
					part = (Equip_part) temp;
					if(part==Equip_part.WEAPON) isWeapon=true;
					else isWeapon=false;
				}
				else if(temp.equals(""));	//이전 값 유지
				else if(temp.equals("--")){
					int order = part.order-1;
					part = Equip_part.getPartFromOrder(order);
					if(part==Equip_part.WEAPON) isWeapon=true;
					else isWeapon=false;
				}
				else throw new ParsingException(i-1, temp);
				
				//재질
				temp = data[i++];
				if(temp instanceof Equip_type && !isWeapon) type = (Equip_type) temp;
				else if(temp instanceof Weapon_detailType && isWeapon) weaponType = (Weapon_detailType) temp;
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
				
				//제작
				temp = data[i++];
				if(temp instanceof Boolean) isRare = (boolean) temp;
				else if(temp.equals(""));	//이전 값 유지
				else throw new ParsingException(i-1, temp);
				
				if(data[i] instanceof Boolean)
					optionDisable = (Boolean) data[i++];
				else optionDisable = false;
				
				if(data[i] instanceof String && ((String)data[i]).contains("ver_"))
					version = (String) data[i++];
				else
					version = CalculatorVersion.VER_1_0_a;
				
				if(isWeapon) equipment = new Weapon(name, rarity, weaponType, setName, level, isRare, version);
				else equipment = new Equipment(name, rarity, part, type, setName, level, isRare, version);
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println(data[i-1]);
				System.out.println(data[i-2]);
				System.out.println(data[i-3]);
				System.out.println(data[i-4]);
				System.out.println(data[i-5]);
				throw new ParsingException(i-1, temp);
			}
	
			try{
			
				//아이템 스탯
				while(true)
				{	
					temp = data[i++];
					if(temp==null) break;
					
					else if(temp instanceof FunctionStat)
						equipment.fStat.statList.add((FunctionStat) temp);
					
					else{
						stat = ((String)temp).split(" ");					
						switch(stat[0])
						{
						case "ㄷ": case "d":
							Parser.parseStat(Arrays.copyOfRange(stat, 1, stat.length), equipment.dStat, equipment.fStat);
							break;
						case "설명":
							String explanation = ((String)temp).substring(3);
							equipment.explanation.add(explanation);
							break;
						case "ㅁ": case "v":
							Parser.parseStat(Arrays.copyOfRange(stat, 1, stat.length), equipment.vStat, equipment.fStat);
							break;
						default:
							Parser.parseStat(stat, equipment.vStat, equipment.fStat);
							break;
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("앞1 : "+data[i-2]);
				System.out.println("앞2 : "+data[i-3]);
				System.out.println("뒤1 : "+data[i]);
				System.out.println("뒤2 : "+data[i+1]);
				throw new ParsingException(i-1, temp);
			}
			if(optionDisable){
				for(StatusAndName s : equipment.vStat.statList)
					if(s.enableable) s.enabled = false;
				for(StatusAndName s : equipment.dStat.statList)
					if(s.enableable) s.enabled = false;
			}
			equipList.add(equipment);
		}
	}
	
	public static Object[] equipmentInfo()
	{
		FunctionStat fStat[] = new FunctionStat[20];
		
		//익스포젼 헤비 각반
		fStat[0] = new FunctionStat() {
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Equipment equipment =(Equipment)item;
				if(equipment.dStat.statList.get(0).enabled && equipment.dStat.statList.get(1).enabled)
					equipment.dStat.statList.get(0).enabled=false;
				return new StatusList();
			}
		};
		
		//집척목, 암칼반
		fStat[1] = new FunctionStat() {
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				int count=0;
				Equipment equipment =(Equipment)item;
				for(int i=0; i<4; i++)
					if(equipment.dStat.statList.get(i).enabled) count++;
				
				if(count>1){
					for(int i=0; i<3; i++)
						equipment.dStat.statList.get(i).enabled=false;
					equipment.dStat.statList.get(3).enabled=true;
				}
				return new StatusList();
			}
		};
		
		//황홀경
		fStat[2] = new FunctionStat() {
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Equipment equipment =(Equipment)item;
				int num = equipment.getReinforce();
				if(num>12) num=12;
				StatusList statList = new StatusList();
				for(StatusAndName s: equipment.dStat.statList)
				statList.addStatList(s.name, new StatusInfo(num));
				return statList;
			}
		};
		
		//흑백마음
		fStat[3] = new FunctionStat() {
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				statList.addStatList(type, 55, false, false, false);
				return statList;
			}
		};
		
		//조테카
		fStat[4] = new FunctionStat() {
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - ") && character.getItemSetting().weapon.dStat.statList.getLast().enabled)
					statList.addStatList("스증뎀", 12.5);
				else if(character.getItemSetting().weapon.getName().startsWith("창성의 구원자 - ") && character.getItemSetting().weapon.dStat.statList.getLast().enabled)
					statList.addStatList("스증뎀", (137.0/122.0-1)*100);
				return statList;
			}
		};
		
		//조로크
		fStat[5] = new FunctionStat() {
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Weapon weapon = character.getItemSetting().weapon;
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - ") || character.getItemSetting().weapon.getName().startsWith("창성의 구원자 - ")){
					try {
						int phy = (int)Math.round(weapon.vStat.findStat(StatList.WEP_PHY).stat.getStatToDouble()*0.15);
						int mag = (int)Math.round(weapon.vStat.findStat(StatList.WEP_MAG).stat.getStatToDouble()*0.15);
						int ind = (int)Math.round(weapon.vStat.findStat(StatList.WEP_IND).stat.getStatToDouble()*0.18);
						statList.addStatList("물공", phy);
						statList.addStatList("마공", mag);
						statList.addStatList("독공", ind);
					} catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				return statList;
			}
		};
		
		//조그네스
		fStat[6] = new FunctionStat() {
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - "))
					statList.addStatList("스증뎀", (155.0/135.0-1)*100);
				else if(character.getItemSetting().weapon.getName().startsWith("창성의 구원자 - "))
					statList.addStatList("스증뎀", (160.0/140.0-1)*100);
				return statList;
			}
		};
		
		//고스로리 드레스
		fStat[7] = new FunctionStat() {
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Equipment equipment =(Equipment)item;
				int num = equipment.getReinforce();
				if(num<11)
					statList.addStatList("모속강", num);
				else
					statList.addStatList("모속강", num>15 ? 20 : 10+(num-10)*2);
				return statList;
			}
		};
		
		//불꽃너울
		fStat[8] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				int epic=0;
				int legendary=0;			
				switch(character.getItemSetting().weapon.getRarity()){
				case EPIC:
					epic++;
					break;
				case LEGENDARY:
					legendary++;
					break;
				default:
					break;
				}				
				for(Equipment e : character.getItemSetting().equipmentList.values())
				{
					switch(e.getRarity()){
					case EPIC:
						epic++;
						break;
					case LEGENDARY:
						legendary++;
						break;
					default:
						break;
					}
				}
				statList.addStatList("화속강", epic*3+legendary*2);
				return statList;
			}
		};
		
		//파워드 철갑
		fStat[9] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.LAUNCHER_F){
					for(Skill skill : character.getSkillList()){
						if(skill.getName().equals("중화기 마스터리")){
							if(skill.getActiveEnabled()){
								Double buffAmount;
								try {
									buffAmount = skill.getSkillLevelInfo(true, character.isBurning()).stat.findStat(StatList.BUF_INC).stat.getStatToDouble();
									statList.addStatList("증뎀버프", ((buffAmount+100+5)/(buffAmount+100)-1)*100);
								} catch (StatusTypeMismatch e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				return statList;
			}
		};
		
		//목각
		fStat[10] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Character_type type = character.getJob().charType;
				if(type!=Character_type.DEMONICLANCER && type!=Character_type.GUNNER_F && type!=Character_type.GUNNER_M &&
						type!=Character_type.MAGE_F && type!=Character_type.MAGE_M){
					statList.addSkillRange(45, 45, 1, false);
				}
				return statList;
			}
		};
		
		//나가자라
		fStat[11] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Equipment equip = (Equipment)item;
				int reinforce = equip.getReinforce()>15 ? 15 : equip.getReinforce();
				if(equip.getPart()==Equip_part.ROBE) statList.addStatList("추뎀", reinforce);
				else if(equip.getPart()==Equip_part.SHOULDER) statList.addStatList("스증뎀", reinforce);
				return statList;
			}
		};
	
		//칠죄종
		fStat[12] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Equipment equip = (Equipment)item;
				switch(equip.getPart()){
				case ROBE:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==45) statList.addSkill_damage(skill.getName(), 12);
					break;
				case TROUSER:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==40) statList.addSkill_damage(skill.getName(), 12);
					break;
				case SHOULDER:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==35) statList.addSkill_damage(skill.getName(), 15);
					break;
				case BELT:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==30) statList.addSkill_damage(skill.getName(), 15);
					break;
				case SHOES:
					for(Skill skill : character.getSkillList())
						if(skill.firstLevel==25) statList.addSkill_damage(skill.getName(), 15);
					break;
				default:
					break;
				}
				return statList;
			}
		};
		
		/* Format
		"", "", Equip_part.ROBE, "", SetName., , ,
		"힘  가변", "지능  가변", null,
		"", "", "--", "", "", "", "",
		"힘  가변", "지능  가변", null,
		"", "", "--", "", "", "", "",
		"힘  가변", "지능  가변", null,
		"", "", "--", "", "", "", "",
		"힘  가변", "지능  가변", null,
		"", "", "--", "", "", "", "",
		"힘  가변", "지능  가변", null,
		*/
		
		
		Object[] data = new Object[] {
			
				////////////////////// 에픽
				//////////천
				/////닼고
				"다크 고스 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.FABRIC, SetName.DARKGOTH, 80, false,
				"지능 48 가변", "ㄷ 암 30 가변", "ㄷ 마공 150 가변", "ㄷ 독공 150 가변", "스킬 70 2", null,
				"다크 고스 하의", "", "--", "", "", "", "",
				"지능 48 가변", "ㄷ 암 30 가변", "ㄷ 지능 300 가변", "스킬 60 2", null,
				"다크 고스 숄더", "", "--", "", "", "", "",
				"지능 39 가변", "스킬 40-50 1", "ㄷ 암 30 가변", null,
				"다크 고스 벨트", "", "--", "", "", "", "",
				"지능 29 가변", "ㄷ 마크 20", "ㄷ 암 30 가변", null,
				"다크 고스 샌들", "", "--", "", "", "", "",
				"지능 29 가변", "ㄷ 암 30 가변", null,
				/////불마력
				"마나 번 로브", "", Equip_part.ROBE, "", SetName.BURNINGSPELL, 85, "",
				"지능 51 가변", "ㄷ 마공뻥 15 선택", "ㄷ 독공뻥 15 선택", null,
				"매직 번 트라우저", "", "--", "", "", "", "",
				"지능 48 가변", "ㄷ 크증뎀 15 선택", null,
				"스펠 번 숄더 패드", "", "--", "", "", "", "",
				"지능 41 가변", "ㄷ 마공뻥 12 선택", "ㄷ 독공뻥 12 선택", null,
				"엘리멘탈 번 새쉬", "", "--", "", "", "", "",
				"지능 31 가변", "ㄷ 마크 20 선택", null,
				"소울 번 슈즈", "", "--", "", "", "", "",
				"지능 31 가변", "ㄷ 모속강 20 선택", null,
				/////드로퍼
				"프리징 컷 로브", "", Equip_part.ROBE, "", SetName.DROPPER, 85, "",
				"지능 51 가변", "수속 12 가변", "마크 5", "ㄷ 수속깍 44 선택", null,
				"플레임 드랍 트라우저", "", "--", "", "", "", "",
				"지능 51 가변", "화속 12 가변", "마크 5", "ㄷ 화속깍 44 선택", null,
				"레이 디크리즈 숄더", "", "--", "", "", "", "",
				"지능 85 가변", "명속 12 가변", "마크 5", "ㄷ 명속깍 44 선택", null,
				"다크니스 로우 새쉬", "", "--", "", "", "", "",
				"지능 75 가변", "암속 12 가변", "마크 5", "ㄷ 암속깍 44 선택", null,
				"페이스 다운 슈즈", "", "--", "", "", "", "",
				"지능 75 가변", "모속 30", null,
				/////단일
				"풀 브라이트 로브", "", Equip_part.ROBE, "", SetName.NONE, 90,  "",
				"힘 110 가변", "지능 163 가변", "모속강 35 가변", null,
				"글래시 오브 실크 하의", "", "--", "", "", "", "",
				"힘 83 가변", "지능 135 가변", "물크 12", "마크 12", "스킬 1-70 1", null,
				"인비지블 케이프", "", "--", "", "", "", "",
				"지능 43 가변", "증뎀 15", "모공증 3", null,
				"실크 패브릭 벨트", "", "--", "", "", "", "",
				"지능 32 가변", "크증뎀 18", null,
				"옵틱 파이버 슈즈", "", "--", "", "", "", "",
				"지능 32 가변", "물공 99 가변", "마공 99 가변", "독공 113 가변", "모속강 18 가변", null,
				/////오기일
				"오기일의 색동 저고리", "", Equip_part.ROBE, "", SetName.OGGEILL, 90, "",
				"힘 83 가변", "지능 135 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				"오기일의 스란 치마", "", "--", "", "", "", "",
				"힘 83 가변", "지능 135 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				"오기일의 비단 장옷", "", "--", "", "", "", "",
				"힘 83 가변", "지능 125 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				"오기일의 짧은 고름", "", "--", "", "", "", "",
				"힘 83 가변", "지능 114 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				"오기일의 꽃 버선", "", "--", "", "", "", "",
				"힘 83 가변", "지능 114 가변", "모속강 14 가변", "물크 5", "마크 5", "모공증 3", null,
				/////게슈펜슈트
				"망상의 파라노이아", "", Equip_part.ROBE, "", SetName.GESPENST, 90, true, 
				"지능 53 가변", "ㄷ 힘 -50", "ㄷ 지능 -50", "ㄷ 스킬 1-85 1 선택", "ㄷ 스킬 1-85 1 선택", "설명 현실은 곧 꿈이 되고", "설명 꿈은 곧 현실이 되니", null,
				"애착의 나르시즘", "", "--", "", "", "", "", 
				"지능 53 가변", "물크 5", "마크 5", "모속강 45 가변", "설명 벗어날 수 없는 애증에 빠져", null,
				"붕괴의 게슈탈트", "", "--", "", "", "", "", 
				"지능 43 가변", "%물방깍_템 25 선택", "%마방깍_템 25 선택", "설명 현실과 꿈의 경계가 붕괴할 때", null,
				"죽음의 타나토스", "", "--", "", "", "", "", 
				"지능 32 가변", "ㄷ 화속깍 35 선택", "ㄷ 수속깍 35 선택", "ㄷ 명속깍 35 선택", "ㄷ 암속깍 35 선택", "설명 스스로를 죽음으로 옭아멜 것이다.",  null,
				"인격의 페르소나", "", "--", "", "", "", "", 
				"지능 32 가변", "물공 61 가변", "마공 61 가변", "독공 69 가변", "모공증 15", "설명 네 안의 또 다른 네가.", null,
				
				//////////가죽
				/////카멜
				"교활한 카멜레온 가죽 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.LEATHER, SetName.CHAMELEON, 80, false,
				"힘 41 가변", "지능 41 가변", "물크 3", "마크 3", "ㄷ 고정물방깍 8000 가변", null,
				"민첩한 카멜레온 가죽 하의", "", "--", "", "", "", "",
				"힘 41 가변", "지능 41 가변", "ㄷ 물크 30 가변", "ㄷ 마크 30 가변", null,
				"날렵한 카멜레온 가죽 숄더", "", "--", "", "", "", "",
				"힘 32 가변", "지능 32 가변", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", null,
				"은밀한 카멜레온 가죽 벨트", "", "--", "", "", "", "",
				"힘 24 가변", "지능 24 가변", null,
				"재빠른 카멜레온 가죽 신발", "", "--", "", "", "", "",
				"힘 24 가변", "지능 24 가변", null,
				/////택틱
				"택틱컬 커맨더 상의", "", Equip_part.ROBE, "", SetName.TACTICAL, 85, "",
				"힘 76 가변", "지능 76 가변", "ㄷ 물공 80", "ㄷ 마공 80", "ㄷ 독공 80", null,
				"택틱컬 리더 하의", "", "--", "", "", "", "",
				"힘 76 가변", "지능 76 가변", "ㄷ 물크 12", "ㄷ 마크 12", null,
				"택틱컬 오피서 어깨", "", "--", "", "", "", "",
				"힘 67 가변", "지능 67 가변", null,
				"택틱컬 로드 벨트", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 모속강 20", null,
				"택틱컬 치프 신발", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 힘 120", "ㄷ 지능 120", null,
				/////암살
				"밤의 그림자 상의", "", Equip_part.ROBE, "", SetName.ASSASSIN, 85, "",
				"힘 92 가변", "지능 92 가변", "증뎀 18", null,
				"붉은 송곳니 하의", "", "--", "", "", "", "",
				"힘 92 가변", "지능 92 가변", "크증뎀 15", null,
				"어둠의 칼날 어깨", "", "--", "", "", "", "",
				"힘 67 가변", "지능 67 가변", "물크 5", "마크 5", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", null,
				"죽음의 장막 벨트", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 물공 100", "ㄷ 마공 100", "ㄷ 독공 100", null,
				"황천의 바람 신발", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "모속강 8 선택", "모속강 8 선택", null,
				/////단일
				"용맹한 범무늬 가죽 재킷", "", Equip_part.ROBE, "", SetName.NONE, 90, "",
				"힘 45 가변", "지능 45 가변", "ㄷ 추뎀 12 선택", null,
				"바다의 포식자 가죽 바지", "", "--", "", "", "", "",
				"힘 45 가변", "지능 45 가변", "물공 110 가변", "마공 110 가변", "독공 127 가변", "ㄷ 물공뻥 7", "ㄷ 마공뻥 7", "ㄷ 독공뻥 7", null,
				"강인한 레이온 갈기 숄더", "", "--", "", "", "", "",
				"힘 35 가변", "지능 35 가변", "추크증 12", null,
				"저먼채널 레이 휩 벨트", "", "--", "", "", "", "",
				"힘 26 가변", "지능 26 가변", "물공 132 가변", "마공 132 가변", "독공 152 가변", "증뎀 10", null,
				"창공의 알바트로스 깃털 부츠", "", "--", "", "", "", "",
				"힘 26 가변", "지능 26 가변", "물크 5", "마크 5", "크증뎀 15", null,
				/////블랙포멀, 신사
				"블랙 포멀 재킷", "", Equip_part.ROBE, "", SetName.BLACKFORMAL, 90, "",
				"힘 177 가변", "지능 177 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				"블랙 포멀 팬츠", "", "--", "", "", "", "",
				"힘 177 가변", "지능 177 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				"블랙 포멀 숄더패드", "", "--", "", "", "", "",
				"힘 167 가변", "지능 167 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				"블랙 포멀 벨트", "", "--", "", "", "", "",
				"힘 158 가변", "지능 158 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				"블랙 포멀 부츠", "", "--", "", "", "", "",
				"힘 158 가변", "지능 158 가변", "물공 66 가변", "마공 66 가변", "독공 76 가변", "추크증 4", null,
				/////핀드
				"니힐룸의 이공간", "", Equip_part.ROBE, "", SetName.FIENDVENATOR, 90, true, 
				"힘 45 가변", "지능 45 가변", "모속강 18 가변", "추뎀 12", "설명 만물의 시간을 보내며", "설명 이 순간만을 기다렸다", null,
				"니겔루스의 초합금", "", "--", "", "", "", "",
				"힘 45 가변", "지능 45 가변", "암속강 18 가변", "ㄷ 물공뻥 12", "ㄷ 마공뻥 12", "ㄷ 독공뻥 12", "설명 누구도 감당할 수 없는", "설명 무력을 가진 자", null,
				"갈바누스의 성장", "", "--", "", "", "", "", 
				"힘 35 가변", "지능 35 가변", "명속강 18 가변", "모공증 12", "설명 에너지의 주인이자", "설명 그 자체인 자", null,
				"위로르의 증기", "", "--", "", "", "", "", 
				"힘 26 가변", "지능 26 가변", "수속강 18 가변", "추증뎀 12", "설명 눈이 보이지만 잡을 수 없고", "설명 그 어디에도 존재하는 자", null,
				"루벨루스의 염화", "", "--", "", "", "", "", 
				"힘 26 가변", "지능 26 가변", "화속강 18 가변", "추크증 12", "설명 끊임없는 불길로", "설명 모든 것을 태우는 자", null,

				/////////경갑
				/////서브마린
				"서브마린 볼케이노 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.MAIL, SetName.SUBMARINE, 80, false,
				"힘 48 가변", "지능 32 가변", "ㄷ 물공뻥 8 선택", "ㄷ 마공뻥 8 선택", "ㄷ 독공뻥 8 선택", null,
				"서브마린 볼케이노 하의", "", "--", "", "", "", "",
				"힘 48 가변", "지능 32 가변", "ㄷ 물공뻥 8 선택", "ㄷ 마공뻥 8 선택", "ㄷ 독공뻥 8 선택", null,
				"서브마린 볼케이노 어깨", "", "--", "", "", "", "",
				"힘 39 가변", "지능 25 가변", "물크 15", "마크 15", "스킬 45-50 1", null,
				"서브마린 볼케이노 벨트", "", "--", "", "", "", "",
				"힘 29 가변", "지능 20 가변", "ㄷ 화속강 40", "ㄷ 화속부여", null,
				"서브마린 볼케이노 신발", "", "--", "", "", "", "",
				"힘 29 가변", "지능 20 가변", "화속강 24", null,
				/////자수
				"라이트니스 오토 상의", "", Equip_part.ROBE, "", SetName.NATURALGARDIAN, 85, "",
				"힘 51 가변", "지능 34 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "명속강 24 가변", null,
				"파이어니스 오토 하의", "", "--", "", "", "", "",
				"힘 51 가변", "지능 34 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "화속강 24 가변", null,
				"블랙니스 오토 어깨", "", "--", "", "", "", "",
				"힘 41 가변", "지능 28 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "암속강 24 가변", null,
				"아이니스 오토 벨트", "", "--", "", "", "", "",
				"힘 41 가변", "지능 28 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "수속강 24 가변", null,
				"윈드니스 오토 신발", "", "--", "", "", "", "",
				"힘 31 가변", "지능 20 가변", "물공 65 가변", "마공 65 가변", "독공 74 가변", "물크 3", "마크 3", "모속강 14 가변", null,
				/////아이실드
				"사일런스 테이커 상의", "", Equip_part.ROBE, "", SetName.EYESHIELD, 85, "",
				"힘 51 가변", "지능 34 가변", "증뎀 18", null,
				"뉴타입 어태커 하의", "", "--", "", "", "", "",
				"힘 51 가변", "지능 34 가변", "크증뎀 15", "백증뎀 20", null,
				"디펜스 리시버 보호구", "", "--", "", "", "", "",
				"힘 371 가변", "지능 358 가변", null,
				"타이트엔드 블로커 벨트", "", "--", "", "", "", "",
				"힘 31 가변", "지능 20 가변", "물크 15", "마크 15", null,
				"테일백 러너 부츠", "", "--", "", "", "", "",
				"힘 31 가변", "지능 20 가변", "설명 난다", null,
				/////단일
				"이블 립 메일", "", Equip_part.ROBE, "", SetName.NONE, 90, "",
				"힘 53 가변", "지능 35 가변", "ㄷ 힘뻥 10", "ㄷ 지능뻥 10", "모공증 5", null,
				"빅 세크럼 각반", "", "--", "", "", "", "",
				"힘 174 가변", "지능 156 가변", "스킬 15-40 1", "추증뎀 5", null,
				"스카퓰러 본 숄더", "", "--", "", "", "", "",
				"힘 43 가변", "지능 29 가변", "물크 18", "마크 18", "ㄷ 물공뻥 8", "ㄷ 마공뻥 8", "ㄷ 독공뻥 8", null,
				"럼버 스켈 웨이스트", "", "--", "", "", "", "",
				"힘 32 가변", "지능 21 가변", "증뎀 18", null,
				"타이비아 본 부츠", "", "--", "", "", "", "",
				"힘 87 가변", "지능 76 가변", "스킬 50-80 2", null,
				/////황갑
				"눈부신 황금 갑주 상의", "", Equip_part.ROBE, "", SetName.GOLDENARMOR, 90, "",
				"힘 130 가변", "지능 112 가변", "모속강 22", "추증뎀 5", null,
				"눈부신 황금 갑주 하의", "", "--", "", "", "", "",
				"힘 130 가변", "지능 112 가변", "모속강 22", "추크증 5", null,
				"눈부신 황금 갑주 어깨", "", "--", "", "", "", "",
				"힘 120 가변", "지능 106 가변", "모속강 22", "모공증 5", null,
				"눈부신 황금 갑주 허리", "", "--", "", "", "", "",
				"힘 109 가변", "지능 98 가변", "모속강 22", "ㄷ 물공뻥 5", "ㄷ 마공뻥 5", "ㄷ 독공뻥 5", null,
				"눈부신 황금 갑주 신발", "", "--", "", "", "", "",
				"힘 109 가변", "지능 98 가변", "모속강 22", "ㄷ 힘뻥 5", "ㄷ 지능뻥 5", null,
				/////초대륙
				"초대륙 - 발바라의 대지", "", Equip_part.ROBE, "", SetName.SUPERCONTINENT, 90, true, 
				"힘 53 가변", "지능 35 가변", "스킬 1-85 1", "스킬 15-48 1", "설명 모든 세계는 이곳으로 부터 시작되니", null,
				"초대륙 - 판게아의 지진", "", "--", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"힘 53 가변", "지능 35 가변", "추증뎀 16", "ㄷ 물크 7 선택", "ㄷ 마크 7 선택", "설명 아직 끝나지 않았음을..", null, 
				"초대륙 - 파노티아의 화산", "", "--", "", "", "", "", 
				"힘 43 가변", "지능 29 가변", "추크증 18", "설명 억겁의 세월을 보내며", null,
				"초대륙 - 로디니아의 용암", "", "--", "", "", "", "", 
				"힘 32 가변", "지능 21 가변", "ㄷ 물크 2 선택", "ㄷ 마크 2 선택", "ㄷ 모공증 18 선택", "설명 셀수 없는 탄생과 죽음이 지나가고", null,
				"초대륙 - 케놀랜드의 지각", "", "--", "", "", "", "", 
				"힘 32 가변", "지능 21 가변", "ㄷ 물크 2 선택", "ㄷ 마크 2 선택", "ㄷ 물공뻥 18 선택", "ㄷ 마공뻥 18 선택", "ㄷ 독공뻥 18 선택", "설명 살아남은 모든 것들의 진화는", null,
				
				/////////중갑
				/////미다홀
				"미지의 다크홀 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.HEAVY, SetName.DARKHOLE, 80, false,
				"힘 44 가변", "물크 4", "지능 32 가변", "마크 4", "스킬 45 3", "스킬 60 2", null,
				"미지의 다크홀 하의", "", "--", "", "", "", "",
				"힘 44 가변", "물크 4", "지능 32 가변", "마크 4", "스킬 45 3", "스킬 60 2", null,
				"미지의 다크홀 어깨", "", "--", "", "", "", "",
				"힘 35 가변", "지능 25 가변", "증뎀 12", null,
				"미지의 다크홀 벨트", "", "--", "", "", "", "",
				"힘 26 가변", "지능 20 가변", "ㄷ 물크 15", "ㄷ 마크 15", null,
				"미지의 다크홀 슈즈", "", "--", "", "", "", "",
				"힘 81 가변", "지능 75 가변", null,
				/////거미
				"타란튤라 상의", "", Equip_part.ROBE, "", SetName.SPIDERQUEEN, 85, "",
				"힘 200 가변", "지능 188 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				"킹바분 하의", "", "--", "", "", "", "",
				"힘 200 가변", "지능 188 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				"골리앗 버드이터 어깨", "", "--", "", "", "", "",
				"힘 147 가변", "지능 138 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				"로즈헤어 벨트", "", "--", "", "", "", "",
				"힘 138 가변", "지능 130 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				"인디언 오너멘탈 신발", "", "--", "", "", "", "",
				"힘 138 가변", "지능 130 가변", "ㄷ 고정물방깍 12000 선택", "ㄷ 고정마방깍 12000 선택", "설명 거미셋의 최대 방깍량 : 36000", null,
				/////금계
				"피의 맹약 상의", "", Equip_part.ROBE, "", SetName.FORBIDDENCONTRACT, "", "",
				"힘 266 가변", "지능 254 가변", "d 힘 200 가변", "d 지능 200 가변", null,
				"마나의 서약 하의", "", "--", "", "", "", "",
				"힘 46 가변", "지능 34 가변", "물크 15", "마크 15", "ㄷ 물크 20 가변", "ㄷ 마크 20 가변", null,
				"마력의 계약 숄더", "", "--", "", "", "", "",
				"힘 37 가변", "지능 28 가변", "증뎀 15", null,
				"체력의 협약 벨트", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "ㄷ 물공 100 가변", "ㄷ 마공 100 가변", "ㄷ 독공 100 가변", null,
				"피의 조약 부츠", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "모속강 14", "ㄷ 물공 80", "ㄷ 마공 80", "ㄷ 독공 100", null,
				/////단일
				"리엑터 코어 메일", "", Equip_part.ROBE, "", SetName.NONE, 90, "",
				"힘 48 가변", "지능 35 가변", "증뎀 12", "크증뎀 10", null,
				"익스포젼 헤비 각반", "", "--", "", "", "", "",
				"힘 48 가변", "지능 35 가변", "ㄷ 모공증 15 선택", "ㄷ 스증 15 선택", fStat[0], "설명 옵션 2개 모두 선택시 스증옵션 적용", null,
				"컨테미네이션 폴드런", "", "--", "", "", "", "",
				"힘 122 가변", "지능 111 가변", "모공증 12 선택", null,
				"퓨어로드 코일", "", "--", "", "", "", "",
				"힘 112 가변", "지능 103 가변", "ㄷ 물공 100", "ㄷ 마공 100", "ㄷ 독공 115", "ㄷ 물공 80 선택", "ㄷ 마공 80 선택", "ㄷ 독공 92 선택", null,
				"멜트다운 사바톤", "", "--", "", "", "", "",
				"힘 525 가변", "지능 516 가변", null,
				/////고대전쟁의 여신
				"천년전쟁 영웅의 체인 레지스트", "", Equip_part.ROBE, "", SetName.ANCIENTWAR, "", "",
				"힘 213 가변", "지능 200 가변", "물크 10", "마크 10", "스킬 75 2", null,
				"천년전쟁 영웅의 그리브", "", "--", "", "", "", "",
				"힘 213 가변", "지능 200 가변", "물크 10", "마크 10", "스킬 48 2", null,
				"천년전쟁 영웅의 체인 숄더", "", "--", "", "", "", "",
				"힘 40 가변", "지능 29 가변", "모속강 26 가변", "물크 10", "마크 10", null,
				"천년전쟁 영웅의 체인 벨트", "", "--", "", "", "", "",
				"힘 30 가변", "지능 21 가변", "모속강 26 가변", "물크 10", "마크 10", null,
				"천년전쟁 영웅의 체인슈즈", "", "--", "", "", "", "",
				"힘 30 가변", "지능 21 가변", "모속강 26 가변", "물크 10", "마크 10", null,
				/////나자라라
				"역린의 마나스빈", "", Equip_part.ROBE, "", SetName.NAGARAJA, 90, true, 
				"힘 48 가변", "지능 35 가변", "추뎀 7", fStat[11], "설명 강화/증폭 1마다 추가데미지 1% 증가(최대 15강)", "설명  ", "설명 모든 것을 혼돈에 빠뜨려", null,
				"유해교반의 바스키", "", "--", "", "", "", "",
				"힘 48 가변", "지능 35 가변", "화속강 14 가변", "ㄷ 물공뻥 13", "ㄷ 마공뻥 13", "ㄷ 독공뻥 13", "설명 끝없는 욕심의 무게가", null,
				"해룡왕 사가라", "", "--", "", "", "", "",
				"힘 40 가변", "지능 29 가변", "수속강 14 가변", fStat[11], "설명 강화/증폭 1마다 스킬 데미지 1% 증가(최대 15강)", "설명  ", "설명 죽음의 끝에 홀로남아", null,
				"자객의 탁샤카", "", "--", "", "", "", "",
				"힘 30 가변", "지능 21 가변", "암속강 14 가변", "ㄷ 물크 25 선택", "ㄷ 마크 25 선택", "설명 모든 것을 집어 삼키리라", null,
				"거련의 우트파라카", "", "--", "", "", "", "",
				"힘 30 가변", "지능 21 가변", "명속강 14 가변", "스킬 48-80 2", "설명 평화는 오래 가지 못하였으니", null,
				
				/////////판금
				/////인피티니
				"인피니티 레퀴엠 판금 상의", Item_rarity.EPIC, Equip_part.ROBE, Equip_type.PLATE, SetName.INFINITY, 80, false,
				"힘 41 가변", "지능 41 가변", "모속강 12", "스킬 70 1", null,
				"인피니티 레퀴엠 판금 하의", "", "--", "", "", "", "",
				"힘 41 가변", "지능 41 가변", "모속강 12", "스킬 60 1", null,
				"인피니티 레퀴엠 판금 아미스", "", "--", "", "", "", "",
				"힘 32 가변", "지능 32 가변", "모속강 10", null,
				"인피니티 레퀴엠 판금 코일", "", "--", "", "", "", "",
				"힘 24 가변", "지능 24 가변", "모속강 10", null,
				"인피니티 레퀴엠 판금 부츠", "", "--", "", "", "", "",
				"힘 24 가변", "지능 24 가변", "모속강 10", null,
				/////마소
				"마력의 폭풍우", "", Equip_part.ROBE, "", SetName.MAELSTORM, 85, "",
				"힘 43 가변", "지능 43 가변", "증뎀 10", null,
				"영력의 회오리", "", "--", "", "", "", "",
				"힘 43 가변", "지능 43 가변", "스킬 85 1", null,
				"마법의 대격변", "", "--", "", "", "", "",
				"힘 34 가변", "지능 34 가변", "스킬 30-45 2", "스킬 기본기 숙련 % 150", null,
				"마나의 소용돌이", "", "--", "", "", "", "",
				"힘 25 가변", "지능 25 가변", "스킬 80 1", null,
				"정수의 태풍", "", "--", "", "", "", "",
				"힘 113 가변", "지능 113 가변", "물크 5", "마크 5", "ㄷ 물공뻥 4 선택", "ㄷ 마공뻥 4 선택", "ㄷ 독공뻥 4 선택", null,
				/////풀플
				"플레이트 파워아머 상의", "", Equip_part.ROBE, "", SetName.FULLPLATE, 85, "",
				"힘 98 가변", "지능 98 가변", "ㄷ 물공 100 선택", "ㄷ 마공 100 선택", "ㄷ 독공 100 선택", null,
				"플레이트 매직아머 하의", "", "--", "", "", "", "",
				"힘 98 가변", "지능 98 가변", "ㄷ 물공 100 선택", "ㄷ 마공 100 선택", "ㄷ 독공 100 선택", null,
				"플레이트 레인지아머 보호대", "", "--", "", "", "", "",
				"힘 34 가변", "지능 34 가변", "물크 7", "마크 7", "ㄷ 크증뎀 15 선택", null,
				"플레이트 앱솔루트아머 벨트", "", "--", "", "", "", "",
				"힘 190 가변", "지능 190 가변", null,
				"플레이트 윙아머 부츠", "", "--", "", "", "", "",
				"힘 25 가변", "지능 25 가변", "물크 10", "마크 10", null,
				/////단일
				"라이트 로즈 판금 상갑", "", Equip_part.ROBE, "", SetName.NONE, 90, "",
				"힘 45 가변", "지능 45 가변", "모속강 24 가변", "ㄷ 모속강 18 선택",  null,
				"섀도우 사파이어 판금 하갑", "", "--", "", "", "", "",
				"힘 45 가변", "지능 45 가변", "증뎀 18", null,
				"스모키 토파즈 판금 숄더", "", "--", "", "", "", "",
				"힘 35 가변", "지능 35 가변", "물공 88 가변", "마공 88 가변", "독공 101 가변", "스킬 40-50 2", null,
				"선 토파즈 판금 코일", "", "--", "", "", "", "",
				"힘 466 가변", "지능 466 가변", null,
				"페리도트 판금 그리브", "", "--", "", "", "", "",
				"힘 26 가변", "지능 26 가변", "스킬 5 2", "ㄷ 힘뻥 7", "ㄷ 지능뻥 7", null,
				/////센츄리온
				"메탈라인 아머", "", Equip_part.ROBE, "", SetName.CENTURYONHERO, 90, "",
				"힘 155 가변", "지능 155 가변", "물크 7", "마크 7", "스킬 5-30 1", null,
				"메탈라인 각반", "", "--", "", "", "", "",
				"힘 155 가변", "지능 155 가변", "물크 7", "마크 7", "스킬 5-30 1", null,
				"메탈라인 폴드런", "", "--", "", "", "", "",
				"힘 145 가변", "지능 145 가변", "물크 7", "마크 7", "스킬 45-50 1", null,
				"메탈라인 플레이트 코일", "", "--", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"힘 136 가변", "지능 136 가변", "물크 7", "마크 7", "스킬 45-50 1", null,
				"메탈라인 그리브", "", "--", "", "", "", "",
				"힘 136 가변", "지능 136 가변", "물크 7", "마크 7", "스킬 35-48 1", null,
				/////칠죄종
				"오만에 가득찬 눈", "", Equip_part.ROBE, "", SetName.SEVENSINS, 90, true,
				"힘 100 가변", "지능 100 가변", "스킬 스트라이킹 3", "스킬 스트라이킹 % 15", "ㄷ 적방무 15 선택", fStat[12], "설명 45레벨 스킬 공격력 12% 증가", "설명  ", "설명 오만함에 잠기기 시작한 자는", null,
				"폭식하는 입", "", "--", "", "", "", "", 
				"힘 100 가변", "지능 100 가변", "스킬 지혜의 축복 3", "스킬 지혜의 축복 % 15", "ㄷ 물공뻥 15 선택", "ㄷ 마공뻥 15 선택", "ㄷ 독공뻥 15 선택",
				fStat[12], "설명 40레벨 스킬 공격력 12% 증가", "설명  ", "설명 주체할 수 없는 탐의 말로는", null,
				"탐식을 쥐는 손", "", "--", "", "", "", "", 
				"힘 211 가변", "지능 211 가변", "스킬 크로스 크래쉬 3", "스킬 크로스 크래쉬 % 20", "ㄷ 스증 8", fStat[12], "설명 35레벨 스킬 공격력 15% 증가", "설명  ", "설명 모든 것을 가지려는 욕심과", null,
				"질투를 말하는 혀", "", "--", "", "", "", "",
				"힘 81 가변", "지능 81 가변", "스킬 여명의 축복 3", "스킬 여명의 축복 % 20", "모공증 12", fStat[12], "설명 30레벨 스킬 공격력 15% 증가", "설명  ", "설명 모든 것을 질투하기 시작하고", null,
				"나태함을 가진 발", "", "--", "", "", "", "",
				"힘 81 가변", "지능 81 가변", "물크 25", "마크 25", fStat[12], "설명 25레벨 스킬 공격력 15% 증가", "설명  ", "설명 결국 모든 것을 잃을 지어니", null,
				
				/////////악세
				/////슈스
				"슈퍼 스타 네클레스", Item_rarity.EPIC, Equip_part.NECKLACE, Equip_type.ACCESSORY, SetName.SUPERSTAR, 80, false,
				"지능 40 가변", "모속강 12 가변", null,
				"슈퍼 스타 암릿", "", "--", "", "", "", "",
				"힘 40 가변", "모속강 12 가변", null,
				"슈퍼 스타 링", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "모속강 12 가변", null,
				/////얼공
				"차가운 공주의 목걸이", "", Equip_part.NECKLACE, "", SetName.ICEQUEEN, 85, "",
				"지능 41 가변", "수속강 30 가변", null,
				"냉정한 공주의 팔찌", "", "--", "", "", "", "",
				"힘 41 가변",  "수속강 22 가변", "ㄷ 수속부여", null,
				"싸늘한 공주의 반지", "", "--", "", "", "", "",
				"힘 62 가변", "지능 62 가변", "수속강 30 가변", null,
				/////정마
				"정제된 혼돈의 마석 목걸이", "", Equip_part.NECKLACE, "", SetName.REFINEDSTONE, 85, "",
				"지능 41 가변", "ㄷ 증뎀 20 선택", null,
				"정제된 파괴의 마석 팔찌", "", "--", "", "", "", "",
				"힘 41 가변", "ㄷ 스증뎀 5", null,
				"정제된 망각의 마석 반지", "", "--", "", "", "", "",
				"힘 62 가변", "지능 62 가변", null,
				/////85단일
				"칼날 여왕의 목걸이", "", Equip_part.NECKLACE, "", SetName.NONE, 85, "",
				"지능 41 가변", "ㄷ 증뎀 20 선택", null,
				"집행인의 척살 목걸이", "", "", "", "", "", "",
				"지능 41 가변", "물크 7", "마크 7", "ㄷ 증뎀 10 선택", "ㄷ 증뎀 20 선택", "ㄷ 증뎀 30 선택",
				"ㄷ 증뎀 19 선택", fStat[1], "설명 증뎀 옵션에 2개 이상이 체크될 경우, 기댓값(19%)으로 설정됩니다", null,
				"피묻은 수갑", "", "--", "", "", "", "",
				"힘 283 가변", "지능 242 가변", "ㄷ 물크 30 선택", "ㄷ 마크 30 선택", null,
				"화염술사의 포락 팔찌", "", "", "", "", "", "",
				"힘 96 가변", "지능 55 가변", "화속강 12 가변", "물크 10", "마크 10", "ㄷ 증뎀 20 선택", null,
				"암살자의 칼날 반지", "", "--", "", "", "", "",
				"힘 62 가변", "지능 62 가변", "물크 7", "마크 7", "ㄷ 크증뎀 10 선택", "ㄷ 크증뎀 20 선택", "ㄷ 크증뎀 30 선택",
				"ㄷ 크증뎀 19 선택", fStat[1], "설명 크증뎀 옵션에 2개 이상이 체크될 경우, 기댓값(19%)으로 설정됩니다", null,
				"탐식의 형상", "", Equip_part.NECKLACE, "", "", "", "",
				"지능 41 가변", "추뎀 9", null,
				"무한한 탐식의 형상", "", "", "", "", "", true,
				"지능 41 가변", "추뎀 25", null,
				"탐식의 얼개", "", "--", "", "", "", false,
				"힘 41 가변", "물크 5", "마크 5", "증뎀 15", null,
				"무한한 탐식의 얼개", "", "", "", "", "", true,
				"힘 41 가변", "물크 10", "마크 10", "증뎀 30", null,
				"탐식의 잔재", "", "--", "", "", "", false,
				"힘 62 가변", "지능 62 가변", "물크 5", "마크 5", "크증뎀 15", null,
				"무한한 탐식의 잔재", "", "", "", "", "", true,
				"힘 62 가변", "지능 62 가변", "물크 10", "마크 10", "크증뎀 30", null,
				"이기의 조력자 - 쿠로", "", Equip_part.NECKLACE, "", "", "", false,
				"지능 41 가변", null,
				"이기의 조력자 - 마테카", "", "--", "", "", "", "",
				"힘 41 가변", fStat[4], "설명 구원의 이기 무기 개방시 스킬데미지 증가량 20->35%", "설명 창성의 구원자 무기 개방시 스킬데미지 증가량 22->37%", null,
				"이기의 조력자 - 네르베",  "", "--", "", "", "", "",
				"힘 62 가변", "지능 62 가변", "설명 그냥 예의상 구현해봄", null,
				
				/////90단일
				"카프리 엠퍼 네클리스", "", Equip_part.NECKLACE, "", SetName.NONE, 90, false,
				"지능 43 가변", "ㄷ 모공증 20 가변", null,
				"사이얀 홉 암릿", "", "--", "", "", "", "",
				"힘 43 가변", "물공 105 가변", "마공 105 가변", "독공 120 가변", "ㄷ 스증뎀 8", null,
				"아쿠아 게이트 링", "", "--", "", "", "", "",
				"힘 64 가변", "지능 64 가변", "모속강 30 가변", "물크 15", "마크 15", null,
				/////이정표
				"하늘의 이정표 : 루크바", "", Equip_part.NECKLACE, "", SetName.SKYTRAVELER, 90, "",
				"지능 43 가변", "모속강 18 가변", "물크 10", "마크 10", "모공증 3", null,
				"하늘의 등대 : 쉐다르", "", "--", "", "", "", "",
				"힘 43 가변", "모속강 18 가변", "물크 10", "마크 10", "모공증 3", null,
				"하늘의 길잡이 : 카프", "", "--", "", "", "", "",
				"힘 64 가변", "지능 64 가변", "모속강 18 가변", "물크 10", "마크 10", "모공증 3", null,
				/////황홀경
				"반짝임의 향기", "", Equip_part.NECKLACE, "", SetName.ECSTATICSENCE, 90, true,
				"지능 43 가변", "ㄷ 모공증 5", fStat[2], "설명 강화/증폭 수치가 1 증가할 때마다 모든 공격력 1% 추가 증가 (최대 12강까지 증가)", null,
				"샛별의 숨소리", "", "--", "", "", "", "",
				"힘 43 가변", "ㄷ 스증뎀 5", fStat[2], "설명 강화/증폭 수치가 1 증가할 때마다 스킬 공격력 1% 추가 증가 (최대 12강까지 증가)", null,
				"물소리의 기억", "", "--", "", "", "", "",
				"힘 64 가변", "지능 64 가변", "ㄷ 물공뻥 5", "ㄷ 마공뻥 5", "ㄷ 독공뻥 5", fStat[2], "설명 강화/증폭 수치가 1 증가할 때마다 물리, 마법, 독립 공격력 1% 추가 증가 (최대 12강까지 증가)", null,

				/////////특수장비
				/////보장
				"각성한 자의 각오 -", "", Equip_part.AIDEQUIPMENT, Equip_type.SPECIALEQUIP, SetName.NONE, 80, false,
				"힘 88 가변", "지능 88 가변", "설명 스킬 정보 미구현", null,
				"XX의 XX 장갑", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "물공 135 가변", "마공 135 가변", "설명 거너 극옵 - 물공 110 마공 100", "설명 도적  극옵 - 물공 114 마공 135", null,
				"지벤 황국의 완장", "", "", "", "", "", "",
				"힘 143 가변", "지능 143 가변", null,
				"시간 여행자의 은시계", "", "", "", "", 85, "",
				"힘 40 가변", "지능 40 가변", "스킬 1-80 1", null,
				"고명한 장군의 전략서", "", "", "", "", "", "",
				"힘 146 가변", "지능 146 가변", "스킬 85 1", "스킬 48-50 1", "TP스킬 1-85 1", null,
				"탐식의 증적", "", "", "", "", "", "",
				"힘 128 가변", "지능 128 가변", "물공 110 가변", "마공 110 가변", "독공 149 가변", null,
				"무한한 탐식의 증적", "", "", "", "", "", true,
				"힘 205 가변", "지능 205 가변", "물공 198 가변", "마공 198 가변", "독공 259 가변", null,
				"이기의 조력자 - 로크", "", "", "", "", "", false,
				"힘 40 가변", "지능 40 가변", fStat[5], "설명 안톤 무기 물리/마법 공격력 15% 증가", "설명 안톤 무기 독립 공격력 18% 증가(재련 미포함)", null,
				"피쉬 볼 라인", "", "", "", "", 90, false,
				"힘 42 가변", "지능 42 가변", "물크 5", "마크 5", "스킬 45-80 2", null,
				"흑백의 경계 : 가면", "", "", "", "", "", "",
				"힘 42 가변", "지능 42 가변", "ㄷ 물공뻥 5", "ㄷ 마공뻥 5", "ㄷ 독공뻥 5", "ㄷ 물크 30 선택", "ㄷ 마크 30 선택", "설명 함정카드 발동", null,
				"파르스의 황금잔", "", "", "", "", "", true,
				"힘 42 가변", "지능 42 가변", "모속강 18 가변", "ㄷ 힘뻥 18 선택", "ㄷ 지능뻥 18 선택", "설명 운빨", null,
				/////마법석
				"폐왕의 눈물", "", Equip_part.MAGICSTONE, "", "", 85, false,
				"힘 123 가변", "지능 123 가변", "추뎀 18", null,
				"탐식의 근원", "", "", "", "", "", "",
				"힘 61 가변", "지능 61 가변", "모속강 45 가변", null,
				"무한한 탐식의 근원", "", "", "", "", "", true,
				"힘 61 가변", "지능 61 가변", "모속강 70 가변", null,
				"이기의 조력자 - 아그네스", "", "", "", "", "", false,
				"힘 121 가변", "지능 121 가변", fStat[6], "설명 구원의 이기 스킬 데미지 증가량 35->55%", "설명 창성의 구원자 스킬 데미지 증가량 40->60%", null,
				"무한한 탐식의 기원", "", "", "", "", "",  true,
				"힘 242 가변", "지능 242 가변", null,
				"비뮤트 스톤", "", "", "", "", 90, false,
				"힘 118 가변", "지능 118 가변", "모공증 18", null,
				"잭오랜턴의 기억", "", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "화속강 55 가변", null,
				"잭프로스트의 기억", "", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "수속강 55 가변", null,
				"플로레상의 기억", "", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "명속강 55 가변", null,
				"플루토의 기억", "", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "암속강 55 가변", null,
				"카발라의 기억", "", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "모속강 55 가변", null,
				"흑백의 경계 : 마음", "", "", "", "", "", "",
				"힘 63 가변", "지능 63 가변", fStat[3], "설명 화/수/명/암 속성 강화 중 높은 속성강화 55 증가", null,
				"로제타스톤", "", "", "", "", "", true,
				"힘 63 가변", "지능 63 가변", "모속강 18 가변", "ㄷ 스증뎀 18 선택", "설명 ㅈ망", null,
				
				//////////귀걸이
				/////레어
				"마법의 줄타나이트 이어링", Item_rarity.RARE, Equip_part.EARRING, "", "", 90, false,
				"힘 56 가변", "지능 56 가변", "모속강 6 가변", "힘 32 가변", "지능 32 가변", null,
				/////유니크
				"성물 : 모락스의 귀걸이", Item_rarity.UNIQUE, "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "모속강 11 가변", "물크 2", "마크 2", null,
				"레인보우 이어링", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "모속강 12 가변", "힘 100 선택", "지능 100 선택", null,
				/////레전더리
				"브라키움 기어링", Item_rarity.LEGENDARY, "", "", "", "", "",
				"힘 62 가변", "지능 62 가변", "물공 121 가변", "마공 121 가변", "독공 139 가변", "물크 10", "마크 10", null,
				"슈베르티", "", "", "", "", "", "", CalculatorVersion.VER_1_1_g,
				"힘 128 가변", "지능 128 가변", "모속강 18 가변", "모공증 3", "설명 슬퍼 말거라...", "설명 뜨면 많이 슬플듯..", null,
				"큐빅 오브 식스테일", "", "", "", "", "", "", CalculatorVersion.VER_1_1_g,
				"물크 6", "마크 6", "추증뎀 6", "추크증 6", "설명 더 많이 더욱 아름답게!", null,
				"골드 볼텍스", "", "", "", "", "", "", CalculatorVersion.VER_1_1_g,
				"힘 62 가변", "지능 62 가변", "모속강 24 가변", "설명 찬란한 금빛의 아름다움이여", "설명 속도 최강 볼텍스", null,
				"무언의 건설자 귀걸이", "", "", "", SetName.TACITCONSTRUCTOR, "", "",
				"힘 62 가변", "지능 62 가변", "물공 149 가변", "마공 149 가변", "독공 171 가변", null,
				"청초의 유클레이스", "", "", "", SetName.TWIlIGHT, "", "", CalculatorVersion.VER_1_1_g,
				"힘 117 가변", "지능 117 가변", "물공 121 가변", "마공 121 가변", "독공 139 가변", "설명 늦은해를 뒤로하고 어둠이 찾아온다", null,

				/////에픽
				"브라이들 펄", Item_rarity.EPIC, "", "", SetName.NONE, "", "",
				"힘 63 가변", "지능 63 가변", "물공 165 가변", "마공 165 가변", "독공 189 가변", "추크증 8", null,
				"흑백의 경계 : 혼돈", "", "", "", "", "", "",
				"힘 184 가변", "지능 184 가변", "ㄷ 물공뻥 15 선택", "ㄷ 마공뻥 15 선택", "ㄷ 독공뻥 15", null,
				"바벨로니아의 상징", "", "", "", "", "", true,
				"힘 63 가변", "지능 63 가변", "모속강 18 가변", "ㄷ 모공증 18 선택", "설명 겜", "설명 수련의 방에서 제대로 작동하지 않습니다", null,
				
				/////군주
				"루멘 바실리움", Item_rarity.EPIC, Equip_part.AIDEQUIPMENT, "", SetName.MONARCHOFHEVELON, 90, true, 
				"힘 42 가변", "지능 41 가변", "모공증 20", "설명 빛은 구원이 아니며, 어둠 또한 안식이 아니다", null,
				"테네브레 누스", "", Equip_part.EARRING, "", "", "", "", 
				"힘 117 가변", "지능 116 가변", "ㄷ 물공뻥 22", "ㄷ 마공뻥 22", "ㄷ 독공뻥 22", "설명 빛과 어둠의 조화를 나 헤블론의 군주가 맹세하노라", null,
				"솔리움 폰스", "", Equip_part.MAGICSTONE, "", "", "", "", 
				"힘 62 가변", "지능 62 가변", "스킬 1-85 1", "ㄷ 스증뎀 10", "설명 그 무한한 영광의 끝을 느껴보아라", null,
				
				/////////////테스트용
				/*, "테스트 아이템", Item_rarity.EPIC, Equip_part.NECKLACE, Equip_type.NONE, SetName.NONE, 90, false,
				"힘 5000 가변", "지능 5000 가변", "물공 3000 가변", "독공 3000 가변", "재련독공 1000 가변", "물리방무뎀 3000 가변", "화속 500 가변", "수속 500 가변", "명속 500 가변", "암속 500 가변", "모속 500 가변", "화속부여 선택", "수속부여 선택", "명속부여 선택", "암속부여 선택",
				"힘뻥 100 가변", "물공뻥 100 가변", "독공뻥 100 가변", "증뎀 100 가변", "크증뎀 100 가변", "추뎀 100 가변", "화추뎀 50 가변", "수추뎀 50 가변", "명추뎀 50 가변", "암추뎀 50 가변", "화속깍 100 가변", "수속깍 100 가변", "명속깍 100 가변", "암속깍 100 가변", null,
				"테스트 아이템2", Item_rarity.EPIC, "--", Equip_type.NONE, SetName.NONE, 90, false,
				"투함포항 100 가변", "모공증 100 가변", "적방무 30 가변", "추증뎀 100 가변", "추크증 100 가변", "스증뎀 100 가변", "물크 200 가변", "마크 200 가변", "크리저항감소 100 가변", "물리마스터리2 100 가변", "마법마스터리2 100 가변", "증뎀버프 100 가변", "증뎀버프 100 가변",
				"크증뎀버프 100 가변", "크증뎀버프 100 가변", "고정물방깍 200000 가변", "%물방깍_템 100 가변", "%물방깍_스킬 100 가변", null,
				*/
				
				///////////////////////레전더리
				////////////천
				/////85
				"치프사스의 블랙 코트", Item_rarity.LEGENDARY, Equip_part.ROBE, Equip_type.FABRIC, SetName.NONE, 85, false,
				"힘 22 가변", "지능 72 가변", "ㄷ 암속강 20", "ㄷ 암속부여", null,
				"다크 바일론 상의", "", "", "", SetName.NONE, "", "",
				"지능 50 가변", "마크 8", "ㄷ 화속강 15", "ㄷ 수속강 15", "ㄷ 명속강 15", "ㄷ 암속강 15", null,
				"무신의 기운이 담긴 천 상의", "", "", "", SetName.GODOFFIGHT, "", "",
				"지능 50 가변", "스킬 20-45 2", null,
				"선동하는 자의 광기", "", "--", "", SetName.NONE, "", "",
				"지능 50 가변", "마크 5", "ㄷ 마크 19 가변 선택", null,
				"총잡이의 멋스러운 청바지", "", "", "", SetName.NONE, "", "",
				"지능 50 가변", "모속강 18 가변", null,
				"블러드 버블", "", "--", "", SetName.NONE, "", "",
				"지능 40 가변", "ㄷ 크증뎀 12 선택", null,
				"화형의 모로스", "", "--", "", SetName.NONE, "", "",
				"지능 30 가변", "화속강 22 가변", null,
				"하이퍼 메카타우의 붉은숄", "", "", "", SetName.NONE, "", "",
				"지능 30 가변", "ㄷ 증뎀 10 선택", null,
				"그림시커 교단의 신발", "", "--", "", SetName.NONE, "", "",
				"지능 30 가변", "암속강 14 가변", "스킬 보이드 3", "스킬 보이드 강화 1", "스킬 암전 3", "스킬 암전 강화 1", "설명 교베누", null,
				"슬레이프니르", "", "", "", SetName.NONE, "", "",
				"지능 74 가변", "ㄷ 마공 25 선택", null,
				/////90
				"고스로리 드레스", "", Equip_part.ROBE, "", SetName.NONE, 90, "",
				"지능 52 가변", "모속강 14 선택", fStat[7], "설명 강화/증폭 수치 1마다 모속강 1 증가(10강까지)", "설명 강화/증폭 수치 1마다 모속강 2 증가(11~15강)", null,
				"악동의 호박바지", "", "--", "", SetName.NONE, "", "",
				"지능 52 가변", "물크 5", "마크 5", "스킬 1-45 1", null,
				"얀티스 루마 숄더", "", "--", "", SetName.NONE, "", "",
				"지능 42 가변", "물크 10", "마크 10", "크증뎀 10", null,
				"광대의 슬픔", "", "--", "", SetName.NONE, "", "",
				"지능 31 가변", "물크 5", "마크 5", "물공뻥 8", "마공뻥 8", "독공뻥 8", null,
				"오스 부츠", "", "--", "", SetName.NONE, "", "",
				"힘 50 가변", "지능 80 가변", "ㄷ 힘뻥 10 가변", "ㄷ 지능뻥 10 가변", null,
				
				//////////가죽
				/////85
				"라비네터의 영혼의 두루마리", "", Equip_part.ROBE, Equip_type.LEATHER, SetName.NONE, 85, "",
				"힘 91 가변", "지능 91 가변", "암속강 10 가변", "설명 공격시 300px 범위내의 적 HP 5% 감소", null,
				"무신의 기운이 담긴 가죽 상의", "", "", "", SetName.GODOFFIGHT, "", "",
				"힘 42 가변", "지능 42 가변", "스킬 20-45 2", null,
				"음산한 기운의 스웨이드 튜닉", "", "", "", SetName.NONE, "", "",
				"힘 42 가변", "지능 42 가변", "스킬 48-80 1", null,
				"빌라이 비장의 파일럿 상의", "", "", "", SetName.NONE, "", "",
				"힘 42 가변", "지능 42 가변", "ㄷ 물크 30 선택", "ㄷ 마크 30 선택", null,
				"음산한 바람의 스웨이드 그리브", "", "--", "", SetName.NONE, "", "",
				"힘 42 가변", "지능 42 가변", "스킬 1-45 1", null,
				"불길한 거대 박쥐 바지", "", "", "", SetName.NONE, "", "",
				"힘 42 가변", "지능 42 가변", "암속강 18 가변", "ㄷ 암속깍 25 선택", null,
				"섬뜩한 눈동자", "", Equip_part.BELT, "", SetName.NONE, "", "",
				"힘 24 가변", "지능 24 가변", "ㄷ 힘 100 선택", "ㄷ 지능 100 선택", null,
				"카르텔의 장교 군화", "", "--", "", SetName.NONE, "", "",
				"힘 24 가변", "지능 24 가변", "물크 3", "마크 3", "ㄷ 고정물방깍 8000 선택", null,
				"솔리드 워커", "", "", "", SetName.NONE, "", "",
				"힘 24 가변", "지능 24 가변", "ㄷ %물방깍_템 5 선택", "ㄷ %마방깍_템 5 선택", "설명 마하킥, 니들 소베트 카운터 공격시 7초동안 적 방어력 5% 감소", null,
				/////90
				"미스트랄 워터 슈트", "", Equip_part.ROBE, "", SetName.NONE, 90, false,
				"힘 43 가변", "지능 43 가변", "ㄷ 추뎀 8 선택", "ㄷ 힘 110 선택", "ㄷ 지능 110 선택", null,
				"하부브 토푸스 팬츠", "", "--", "", "", "", "",
				"힘 43 가변", "지능 43 가변", "ㄷ 스킬 15-30 1 선택", "ㄷ 스킬 35-50 1 선택", "ㄷ 모공증 4 선택", "설명 모든 공격력 증가 옵션 쿨/지속 : 30/20", null,
				"블랙 디오라마 숄더", "", "--", "", "", "", "",
				"힘 35 가변", "지능 35 가변", "ㄷ 증뎀 12 선택", null,
				"카쉬파의 은둔자", "", "--", "", "", "", "",
				"힘 81 가변", "지능 81 가변", "크증뎀 12", null,
				"초강화 발목토시", "", "--", "", "", "", "",
				"힘 26 가변", "지능 26 가변", "설명 슈아셔틀", null,
				
				///////////경갑
				/////85
				"파괴의 갑옷", "", Equip_part.ROBE, Equip_type.MAIL, SetName.NONE, 85, false,
				"힘 105 가변", "지능 88 가변", "설명 공격시 2% 확률로 7초동안 적 방어구 파괴(미구현)", null,
				"흑화의 흉갑", "", "", "", SetName.NONE, "", "",
				"힘 75 가변", "지능 50 가변", "물크 7", "마크 7", "설명 상의 경갑 방어구 마스터리 효과 2배 증가(미구현)", null,
				"무신의 기운이 담긴 경갑 상의", "", "", "", SetName.GODOFFIGHT, "", "",
				"힘 50 가변", "지능 33 가변", "스킬 20-45 2", null,				
				"타우 캡틴의 거대한 뼈갑옷", "", "--", "", SetName.NONE, "", "",
				"힘 50 가변", "지능 33 가변", "ㄷ 힘 250 가변", "ㄷ 지능 250 가변", "ㄷ 물크 11 가변", "ㄷ 마크 11 가변", null,
				"흑화의 하갑", "", "", "", SetName.NONE, "", "",
				"힘 50 가변", "지능 33 가변", "ㄷ 암속강 30 가변", null,
				"불꽃너울 오버맨틀", "", "--", "", "", "", "",
				"힘 84 가변", "지능 70 가변", fStat[8], "설명 에픽 등급 1개 장착당 화속강 3 증가", "설명 레전더리 등급 1개 장착당 화속강 2 증가", null,
				"죽은 기장의 골견갑", "", "", "", SetName.NONE, "", "",
				"힘 40 가변", "지능 26 가변", "암속강 12 가변", "ㄷ 암속강 30 가변", null,
				"맥 다운 헤비 버클", "", "--", "", "", "", "",
				"힘 173 가변", "지능 20 가변", null,
				"경비책임자의 해골 벨트", "", "", "", SetName.NONE, "", "",
				"힘 63 가변", "지능 53 가변", "물크 3", "마크 3", "크증뎀 9", null,
				"불꽃너울 오버슈즈", "", "--", "", "", "", "",
				"힘 74 가변", "지능 64 가변", fStat[8], "설명 에픽 등급 1개 장착당 화속강 3 증가", "설명 레전더리 등급 1개 장착당 화속강 2 증가", null,
				/////90
				"악검의 붉은 그림자", "", Equip_part.ROBE, "", SetName.NONE, 90, false,
				"힘 52 가변", "지능 34 가변", "물크 10", "마크 10", "추크증 4", null,
				"야신의 월광 각반", "", "--", "", "", "", "",
				"힘 52 가변", "지능 34 가변", "스킬 85 1", "ㄷ 물공뻥 13", "ㄷ 마공뻥 13", "ㄷ 독공뻥 13", null,
				"검은악몽 숄더", "", "--", "", "", "", "",
				"힘 152 가변", "지능 138 가변", "모속강 18 가변", null,
				"뇌전 룸부스", "", "--", "", "", "", "",
				"힘 31 가변", "지능 21 가변", "ㄷ 모속강 24 가변", null,
				"아누비스 세라믹 부츠", "", "--", "", "", "", "",
				"힘 31 가변", "지능 21 가변", "ㄷ 모속강 24", null,
				
				////////////중갑, 퀘전
				/////85
				"진 : 프로 싸움꾼의 중갑 상의", "", Equip_part.ROBE, Equip_type.ALL, SetName.REAL_PROFIGHTER_HARMOR, 85, false, CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "스킬 30-40 2", null,
				"진 : 프로 싸움꾼의 중갑 하의", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 188 가변", "지능 33 가변", "마크 3", "설명 힘과 마크를 올려주는 정체불명의 옵션", null,
				"진 : 프로 싸움꾼의 중갑 어깨", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 36 가변", "지능 169 가변", null,
				"진 : 프로 싸움꾼의 중갑 허리", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "물크 3", null,
				"진 : 프로 싸움꾼의 중갑 신발", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "설명 내가 이걸 왜 구현하고있지", null,
				
				"무신의 기운이 담긴 중갑 상의", "", Equip_part.ROBE, "", SetName.GODOFFIGHT, 85, false, CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "스킬 20-45 2", null,
				"무신의 기운이 담긴 중갑 하의", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 100 가변", "지능 88 가변", "스킬 1-45 1", null,
				"무신의 기운이 담긴 중갑 어깨", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 36 가변", "지능 26 가변", "물크 5", "마크 5", "ㄷ 물공 55", "ㄷ 마공 55", "ㄷ 독공 67", "설명 쿨/지속 20/15", null,
				"무신의 기운이 담긴 중갑 허리", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "모속강 5", "ㄷ 물공 55", "ㄷ 마공 55", "ㄷ 독공 67", "설명 쿨/지속 20/15", null,
				"무신의 기운이 담긴 중갑 신발", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 61 가변", "지능 53 가변", "ㄷ 물공 55", "ㄷ 마공 55", "ㄷ 독공 67", "설명 쿨/지속 20/15", null,
				
				"그라시아 가문의 유산 - 중갑 상의", "", Equip_part.ROBE, "", SetName.GRACIA, 85, false, CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "명속강 16 가변", null,
				"그라시아 가문의 유산 - 중갑 하의", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "명속강 16 가변", null,
				"그라시아 가문의 유산 - 중갑 어깨", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 36 가변", "지능 26 가변", "명속강 10 가변", "ㄷ 힘 80 선택 가변", "ㄷ 지능 80 선택 가변", "ㄷ 명속강 5 선택 가변", null, 
				"그라시아 가문의 유산 - 중갑 허리", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "명속강 10 가변", "ㄷ 힘 80 선택 가변", "ㄷ 지능 80 선택 가변", "ㄷ 명속강 5 선택 가변", null,
				"그라시아 가문의 유산 - 중갑 신발", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "명속강 10 가변", "ㄷ 힘 80 선택 가변", "ㄷ 지능 80 선택 가변", "ㄷ 명속강 5 선택 가변", null,
				
				"해신의 저주를 받은 체인 메일", "", Equip_part.ROBE, "", SetName.CURSEOFSEAGOD, 85, false, CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "수속강 7 가변", "ㄷ 힘 80", "ㄷ 지능 80", null,
				"해신의 저주를 받은 체인 레깅스", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "수속강 7 가변", "스킬 1-45 1", null,
				"해신의 저주를 받은 체인 맨틀", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 36 가변", "지능 26 가변", "수속강 7 가변", "물크 4", "마크 4", null,
				"해신의 저주를 받은 체인 코일", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "수속강 7 가변", null,
				"해신의 저주를 받은 체인 사바톤", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "수속강 7 가변", null,
				
				"애끓는 비탄의 스케일 메일", "", Equip_part.ROBE, "", SetName.DEVASTEDGRIEF, 85, false, CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "ㄷ 힘 100", "ㄷ 지능 100", null,
				"애끓는 비탄의 스케일 레깅스", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "ㄷ 물공 38", "ㄷ 마공 38", "ㄷ 독공 59", null,
				"애끓는 비탄의 스케일 숄더", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 64 가변", "지능 54 가변", null,
				"애끓는 비탄의 스케일 벨트", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "ㄷ 모속강 10", null,
				"애끓는 비탄의 스케일 사바톤", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "ㄷ 물크 8", "ㄷ 마크 8", null,
				
				"위대한 영광의 중갑 상의", "", Equip_part.ROBE, "", SetName.GREATGLORY, 85, false, CalculatorVersion.VER_1_1_b,
				"힘 199 가변", "지능 187 가변", "물크 5", "마크 5", null,
				"위대한 영광의 중갑 하의", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b, 
				"힘 199 가변", "지능 187 가변", "물크 5", "마크 5", null,
				"위대한 영광의 중갑 어깨", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 190 가변", "지능 180 가변", "물크 5", "마크 5", null,
				"위대한 영광의 중갑 허리", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 182 가변", "지능 174 가변", "물크 5", "마크 5", null,
				"위대한 영광의 중갑 신발", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 105 가변", "지능 97 가변", "ㄷ 모속강 10 선택", null,
				
				"붉게 물든 서녘의 중갑 상의", "", Equip_part.ROBE, "", SetName.ROMANTICE, 85, false, CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "물공 50 가변", "마공 50 가변", "독공 75 가변", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", "설명 물마크 증가는 상, 하의 중 하나만 적용", null,
				"붉게 물든 서녘의 중갑 하의", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 45 가변", "지능 33 가변", "물공 50 가변", "마공 50 가변", "독공 75 가변", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", "설명 물마크 증가는 상, 하의 중 하나만 적용", null,
				"붉게 물든 서녘의 중갑 어깨", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 36 가변", "지능 26 가변", "물공 50 가변", "마공 50 가변", "독공 75 가변", null,
				"붉게 물든 서녘의 중갑 허리", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "물공 50 가변", "마공 50 가변", "독공 75 가변", null,
				"붉게 물든 서녘의 중갑 신발", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_b,
				"힘 28 가변", "지능 20 가변", "물공 50 가변", "마공 50 가변", "독공 75 가변", null,
				
				"강완의 체인 메일", "", Equip_part.ROBE, Equip_type.HEAVY, SetName.NONE, 85, false,
				"힘 67 가변", "지능 50 가변", "물크 7", "마크 7", "설명 상의 중갑 방어구 마스터리 효과 2배 증가(미구현)", null,
				"돌격대장의 오토매틱 파워드 철갑", "", "", "", SetName.NONE, "", "",
				"힘 45 가변", "지능 33 가변", "ㄷ 힘 60 가변", "스킬 X-1 익스트루더 % 10", "스킬 충전 레이저 라이플 % 20", fStat[9], "설명 중화기 마스터리 공격력 증가율 5% 추가증가", null, 
				"강완의 체인 레깅스", "", "--", "", "", "", "",
				"힘 133 가변", "지능 121 가변", "ㄷ 물크 20 가변", "ㄷ 마크 20 가변", "ㄷ 물공 50 가변", "ㄷ 마공 50 가변", "ㄷ 독공 60 가변", null,
				"돌격대장의 철기갑 각반", "", "", "", "", "", "",
				"힘 45 가변", "지능 45 가변", "스킬 다크 브레이크 2", "스킬 다크 플레임 소드 2", "스킬 일루젼 슬래쉬 2", "스킬 웨이브 스핀 2", "설명 버프 옵션은 스위칭 수치이므로 미구현", null,
				"카르텔의 두꺼운 강철 어깨", "", "--", "", "", "", "",
				"힘 36 가변", "지능 26 가변", "스킬 그라운드 킥 % 10", "스킬 그라운드 태클 % 10", "ㄷ 물크 10 가변", "ㄷ 마크 10 가변", "ㄷ 증뎀 10 선택", null,
				"돌격대장의 만능 벨트", "", "--", "", "", "", "",
				"힘 83 가변", "지능 75 가변", "크증뎀 9", null,
				"케인의 맹공", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "ㄷ 힘 60 선택", "ㄷ 물크 7 선택", "ㄷ 마크 7 선택", "ㄷ 물공 55 선택", "ㄷ 마공 55 선택", "ㄷ 독공 70 선택", null,
				"앱솔루트 필드", "", "", "", SetName.NONE, "", "",
				"힘 28 가변", "지능 20 가변", "ㄷ 화속깍 22 가변", "ㄷ 수속깍 22 가변", "ㄷ 명속깍 22 가변", "ㄷ 암속깍 22 가변", null,
				
				/////90
				"루베오 로리카", "", Equip_part.ROBE, "", SetName.NONE, 90, false,
				"힘 47 가변", "지능 34 가변", "적방무 12", null,
				"드라코 스케일 사바톤", "", "--", "", "", "", "",
				"힘 47 가변", "지능 34 가변", "ㄷ 증뎀 18 선택", null,
				"마력 증폭 장치", "", "--", "", "", "", "",
				"힘 39 가변", "지능 28 가변", "모속강 10 가변", "스증뎀 10", null,
				"볼케닉 록 코일", "", "--", "", "", "", "",
				"힘 29 가변", "지능 21 가변", "ㄷ 물공뻥 8 선택", "ㄷ 마공뻥 8 선택", "ㄷ 독공뻥 8 선택", null,
				"오토메일 페스", "", "--", "", "", "", "",
				"힘 29 가변", "지능 21 가변", "ㄷ 모공증 10 선택", null,
				
				///////////////판금
				/////85
				"쿠르지프의 전투갑", "", Equip_part.ROBE, Equip_type.PLATE, SetName.NONE, 85, false,
				"힘 75 가변", "지능 75 가변", "물공뻥 3", "마공뻥 3", null,
				"무신의 기운이 담긴 판금 상의", "", "", "", SetName.GODOFFIGHT, "", "",
				"힘 42 가변", "지능 42 가변", "스킬 20-45 2", null,	
				"초전도 흑철 상갑", "", "", "", SetName.NONE, "", "",
				"힘 42 가변", "지능 42 가변", "ㄷ 물공뻥 5 선택", "ㄷ 마공뻥 5 선택", "ㄷ 독공뻥 5 선택", null,
				"초전도 흑철 하갑", "", "--", "", "", "", "",
				"힘 42 가변", "지능 42 가변", "ㄷ 물공뻥 5 선택", "ㄷ 마공뻥 5 선택", "ㄷ 독공뻥 5 선택", null,
				"끓는 마그마호른 폴드론", "", "--", "", "", "", "",
				"힘 33 가변", "지능 33 가변", "물크 12", "마크 12", "ㄷ 화속강 15 선택", null,
				"돌격대장의 오토매틱 파워드 견갑", "", "", "", SetName.NONE, "", "",
				"힘 33 가변", "지능 33 가변", "ㄷ 힘 140 선택", "ㄷ 지능 140 선택", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", null,
				"다크루브 폼 코일", "", "--", "", "", "", "",
				"힘 24 가변", "지능 24 가변", "화속강 22 가변", null,
				
				/////90
				"마키나 코르", "", Equip_part.ROBE, "", SetName.NONE, 90, false,
				"힘 43 가변", "지능 43 가변", "설명 이건 뭐하는애지;;", null,
				"초합금 브라키움 그리브", "", "--", "", "", "", "",
				"힘 43 가변", "지능 43 가변", "물공 116 가변", "마공 116 가변", "독공 132 가변", "모속강 12 가변", null,
				"드라코 루마 폴드론", "", "--", "", "", "", "",
				"힘 277 가변", "지능 277 가변", null,
				"콰트로 카시테룸 코일", "", "--", "", "", "", "",
				"힘 26 가변", "지능 26 가변", "추증뎀 6", "추크증 6", null,
				"램퍼드 티타니아", "", "--", "", "", "", "",
				"힘 26 가변", "지능 26 가변", "ㄷ 화속깍 25 선택", "ㄷ 수속깍 25 선택", "ㄷ 명속깍 25 선택", "ㄷ 암속깍 25 선택", null,
				
				////////////악세
				/////80 이하
				"필리르 - 차고 넘치는 분노", "", Equip_part.NECKLACE, Equip_type.ACCESSORY, SetName.NONE, 80, false,
				"지능 37 가변", "ㄷ 힘뻥 15 선택", "ㄷ 물크 3 선택", null,
				"필리르 - 꺼지지 않는 화염", "", "--", "", "", "", "",
				"힘 37 가변", "화속강 10 가변", "ㄷ 화속강 32 선택", "ㄷ 화속부여 선택", null,
				"필리르 - 냉철한 판단", "", "--", "", "", "", "",
				"힘 56 가변", "지능 56 가변", "ㄷ 지능뻥 15 선택", "ㄷ 마크 3 선택", null,
				
				"엔헤르자 - 전사의 투지", "", Equip_part.NECKLACE, "", "", 30, false,
				"힘 44 가변", "물크 4", "ㄷ 물크 10 가변", "ㄷ 힘 120 가변", null,
				"미른 - 지혜의 샘", "", "", "", "", "", "",
				"지능 62 가변", "마크 4", "ㄷ 마크 10 가변", "ㄷ 지능 120 가변", null,
				"엔헤르자 - 전장의 전율", "", "--", "", "", 40, "",
				"힘 55 가변", "증뎀 8 선택", null,
				"미른 - 잔혹한 결단", "", "", "", "", 40, "",
				"힘 22 가변", "지능 33 가변", "증뎀 8 선택", null,
				"이그드람 - 자연의 근원", "", Equip_part.NECKLACE, "", "", 60, "",
				"힘 33 가변", "지능 63 가변", "모속강 18 가변", null,
				"빌다르 - 복수의 맹세", "", "", "", "", "", "",
				"지능 30 가변", "물크 2", "마크 2", "증뎀 9", null,
				"빌다르 - 복수의 비수", "", Equip_part.BRACELET, "", "", "", "",
				"힘 34 가변", "물크 3", "증뎀 10 선택", null,
				"이그드람 - 용의 승천", "", Equip_part.BRACELET, "", "", "", "",
				"힘 34 가변", "마크 3", "증뎀 10 선택", null,
				
				/////85
				//단품
				"칸디둠넥스의 방출 목걸이", "", Equip_part.NECKLACE, "", "", 85, "",
				"지능 40 가변", "힘뻥 8", null,
				"불의 기사단의 무구", "", "", "", "", "", "",
				"지능 40 가변", "화속강 24 가변", "설명 공격시 20% 확률로 화속성 50000 추가 데미지(미구현)", null,
				"케인의 유골", "", "", "", "", "", "",
				"힘 55 가변", "지능 56 가변", "물크 15", "마크 15", "ㄷ 크증뎀 8 선택", null,
				"얼음 기사단의 무구", "", "", "", "", "", "",
				"지능 40 가변", "수속강 24 가변", "설명 공격시 20% 확률로 수속성 50000 추가 데미지(미구현)", null,
				"니그룸넥스의 방출 목걸이", "", "", "", "", "", "",
				"지능 40 가변", "지능뻥 8", null,
				"플레임 오퍼링 네클레스", "", "", "", "", "", "",
				"지능 40 가변", "화속강 14 가변", "ㄷ 화속강 12 선택", "ㄷ 화속강 12 선택", null,
				"아포피스의 칼날", "", "", "", "", "", "",
				"지능 40 가변", null,
				"저주가 깃든 해골 목걸이", "", "", "", "", "", "",
				"힘 99 가변", "지능 -26 가변", "암속강 22 가변", "물크 5", "마크 -2", "ㄷ 힘 100 선택", "ㄷ 물크 10 선택", null,
				"고동치는 자의 위대함", "", "", "", "", "", "",
				"지능 40 가변", "ㄷ 힘 100", "ㄷ 지능 100", "ㄷ 물크 5", "ㄷ 마크 5", null,
				"죽은 주술사의 집념", "", "", "", "", "", "",
				"지능 40 가변", "암속강 18 가변", "ㄷ 지능 100 가변", null,
				"심해에 뜬 달", "", "", "", "", "", "",
				"지능 40 가변", "수속강 22 가변", "암속강 22 가변", "물크 2", "마크 2", "ㄷ 수속부여 선택", "ㄷ 암속부여 선택", null,
				"절망을 부르는 울음소리", "", "", "", "", "", "",
				"지능 40 가변", "수속강 26 가변", "암속강 26 가변", "ㄷ 암속강 20 가변", null,
				"역류된 마력의 목걸이", "", "", "", "", "", "",
				"지능 40 가변", "스킬 쇼타임 2", "스킬 마나 폭주 2", "증뎀 10", null,
				"그림시커의 검은 로자리", "", "", "", "", "", "",
				"지능 40 가변", "암속강 24 가변", "설명 공격 시 10000 암속성 추가 데미지(미구현)", null,
				"쿠르지프의 금목걸이", "", "", "", "", "", "",
				"지능 40 가변", "스킬 Rx-78 랜드러너 % 20", "스킬 Ez-8 카운트다운 % 10", "스킬 공중 전투 메카 : 템페스터 % 10",
				"스킬 공중 전폭 메카 : 게일포스 % 10", "스킬 메카 드롭 % 10", "스킬 Ez-10 카운터어택 % 10", null,
				"왕의 길을 지키는 성스러운 얼음", "", "", "", "", "", "",
				"지능 40 가변", "수속강 24 가변", "ㄷ 수속강 20 가변", null,
				"위엄의 끝에 선 위대함", "", "", "", "", "", "",
				"지능 40 가변", "ㄷ 힘 100", "ㄷ 지능 100", "ㄷ 물크 5", "ㄷ 마크 5", "ㄷ 독공 54", "설명 위..위엄넘치는 옵션!", null,
				
				"빛의 기사단의 무구", "", "--", "", "", "", "",
				"힘 40 가변", "명속강 5 가변", "설명 공격시 20% 확률로 명속성 50000 추가 데미지(미구현)", null,
				"검은 안개의 질병 팔찌", "", "", "", "", "", "",
				"힘 40 가변", "스킬 독 바르기 1", "스킬 독병 투척 2", "ㄷ 물크 20 가변", "ㄷ 마크 20 가변", null,
				"플루의 악령 빙의 팔찌", "", "", "", "", "", "",
				"힘 40 가변", "암속강 10 가변", "ㄷ %물방깍_템 5", "ㄷ %마방깍_템 5", null,
				"파수꾼의 기능성 팔찌", "", "", "", "", "", "",
				"힘 40 가변", "물크 10", "마크 10", "ㄷ %물방깍_템 6", "ㄷ %마방깍_템 6", null,
				"왕의 길을 지키는 성스러운 빛", "", "", "", "", "", "",
				"힘 40 가변", "명속강 24 가변", null,
				"주술사의 사술 고리", "", "", "", "", "", "",
				"힘 40 가변", "ㄷ 물크 7", "ㄷ 마크 7", "ㄷ 힘 50", "ㄷ 지능 50", null,
				"하이퍼 메카타우의 손목관절", "", "", "", "", "", "",
				"힘 111 가변", "증뎀 11", null,
				"인간 근육 팔찌", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"힘 40 가변", "ㄷ 물크 15 가변", "ㄷ 물공 105 가변", "ㄷ 힘 50 선택", null,
				"파수꾼의 나침반 팔찌", "", "", "", "", "", "",
				"힘 40 가변", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", null,
				"얼어붙은 자의 굳건함", "", "", "", "", "", "",
				"힘 40 가변", "ㄷ 수속깍 20 선택", null,
				"빛나는 자의 엄격함", "", "", "", "", "", "",
				"힘 40 가변", "명속강 12 가변", "ㄷ 명속부여 선택", null,
				
				"제농의 심장", "", "--", "", "", "", "",
				"힘 191 가변", "ㄷ 힘 100 선택", "ㄷ 힘 100 선택", null,
				"킬조의 심장", "", "", "", "", "", "",
				"힘 59 가변", "지능 191 가변", "ㄷ 지능 100 선택", "ㄷ 지능 100 선택", null,
				"유리스의 결심", "", "", "", "", "", "",
				"힘 92 가변", "지능 92 가변", "설명 공격 시 3% 확률로 차원참 시전", null,
				"도굴왕 최후의 보물", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "물크 5", "마크 5", "ㄷ 추뎀 10 가변", null,
				"몽환기장의 유령반지", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 물공뻥 5 선택", "ㄷ 마공뻥 5 선택", "ㄷ 독공뻥 5 선택", "ㄷ 힘 100 선택", "ㄷ 지능 100 선택", null,
				"왕의 길을 지키는 성스러운 불", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "화속강 26 가변", "ㄷ 화추뎀 5 선택", null,
				"검은연기의 이빨", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "모속강 12 가변", "ㄷ 크증뎀 15 선택", null,
				"오염된 원혼의 반지 - X", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 증뎀 15 선택", null,
				"휘몰아치는 자의 날카로움", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 물크 10 가변", "ㄷ 마크 10 가변", "ㄷ 물공 30 가변", "ㄷ 마공 30 가변", "ㄷ 독공 40 가변", null,
				"타오르는 자의 화려함", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "화속강 26 가변", null,
				"악령의 그림자", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 화속깍 20 선택", "ㄷ 수속깍 20 선택", "ㄷ 명속깍 20 선택", "ㄷ 암속깍 20 선택", null,
				"어둠 속에서 빛나는 눈", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 추뎀 15 선택", null,
				"파수꾼 리더의 링", "", "", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 물공 50 선택", "ㄷ 힘 70 선택", "ㄷ 물크 10 선택", "ㄷ 마공 50 선택", "ㄷ 지능 70 선택", "ㄷ 마크 10 선택", null,

				//거형
				"거대한 형상을 담은 목걸이", "", Equip_part.NECKLACE, "", SetName.HUGEFORM, 85, "",
				"지능 40 가변", "모속강 18 가변", null,
				"거대한 형상을 담은 팔찌", "", "--", "", "", "", "",
				"힘 40 가변", "물크 12", "마크 12", null,
				"거대한 형상을 담은 반지", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "물공 40 가변", "마공 40 가변", "독공 51 가변", null,
				//프쌈
				"진 : 프로 싸움꾼의 목걸이", "", Equip_part.NECKLACE, "", SetName.REAL_PROFIGHTER_ACCESSORY, "", "",
				"지능 40 가변", "마크 4", null,
				"진 : 프로 싸움꾼의 팔찌", "", "--", "", "", "", "",
				"힘 40 가변", "물크 4", null,
				"진 : 프로 싸움꾼의 반지", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", null,
				"진 : 프로 싸움꾼의 보조장비", "", "--", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "물공 72 가변", "마공 72 가변", null,
				"진 : 프로 싸움꾼의 마법석", "", "--", "", "", "", "",
				"힘 124 가변", "지능 124 가변", "모속강 12 가변", "설명 옵션 진짜 졸렬한듯...", null,
				//무신
				"무신의 기운이 담긴 목걸이", "", Equip_part.NECKLACE, "", SetName.GODOFFIGHT, "", "",
				"지능 40 가변", "모속강 6 가변", "ㄷ 모속강 10 선택", null,
				"무신의 기운이 담긴 팔찌", "", "--", "", "", "", "",
				"지능 40 가변", "물크 3", "마크 3", "ㄷ 모속강 10 선택", null,
				"무신의 기운이 담긴 반지", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 모속강 10 선택", null,
				"무신의 기운이 담긴 보조장비", "", "--", "", "", "", "",
				"힘 72 가변", "지능 72 가변", "물공 105 가변", "마공 105 가변", "독공 127 가변", null,
				"무신의 기운이 담긴 마법석", "", "--", "", "", "", "",
				"힘 108 가변", "지능 108 가변", "모속강 36 가변", null,
				//위영
				"위대한 영광의 목걸이", "", Equip_part.NECKLACE, "", SetName.GREATGLORY, "", "",
				"지능 40 가변", "모속강 16 가변", "물크 3", "마크 3", null,
				"위대한 영광의 팔찌", "", "--", "", "", "", "",
				"힘 40 가변", "물크 2", "마크 2", "증뎀 10", "크증뎀 5", null,
				"위대한 영광의 반지", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "물공 55 가변", "마공 55 가변", "독공 83 가변", "물크 5", "마크 5", null,
				"위대한 영광의 완장", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_a,
				"힘 89 가변", "지능 89 가변", "물공 77 가변", "마공 77 가변", "독공 116 가변", null,
				"위대한 영광의 상징", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_a,
				"힘 108 가변", "지능 108 가변", "물공 77 가변", "마공 77 가변", "독공 116 가변", null,
				//비탄
				"애끓는 비탄의 목걸이", "", Equip_part.NECKLACE, "", SetName.DEVASTEDGRIEF, "", "",
				"지능 40 가변", "ㄷ 지능 40 가변", "ㄷ 마크 8 가변", null,
				"애끓는 비탄의 팔찌", "", "--", "", "", "", "",
				"힘 40 가변", "ㄷ 힘 40 가변", "ㄷ 물크 8 가변", null,
				"애끓는 비탄의 반지", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "모속강 -2 가변", "ㄷ 모속강 20 가변", null,
				"애끓는 비탄의 메달", "", "--", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "ㄷ 추뎀 6 선택", "설명 파티원이 4명일때 챔피언, 네임드 몬스터 공격 시 추가데미지", null,
				"애끓는 비탄의 눈물", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 추뎀 6 선택", "설명 파티원이 4명일때 보스 몬스터 공격 시 추가데미지", null,
				//그라시아
				"그라시아 가문의 유산 - 목걸이", "", Equip_part.NECKLACE, "", SetName.GRACIA, "", "",
				"지능 40 가변", "ㄷ 명속깍 15", null,
				"그라시아 가문의 유산 - 팔찌", "", "--", "", "", "", "",
				"힘 40 가변", "ㄷ 명속깍 15", null,
				"그라시아 가문의 유산 - 반지", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 명속깍 15", null,
				"그라시아 가문의 유산 - 의지", "", "--", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "명속강 12 가변", "ㄷ 명속강 18 선택 가변", null,
				"그라시아 가문의 유산 - 정화석", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "명속강 12 가변", "ㄷ 명속강 18 선택 가변", null,
				//해신
				"해신의 저주를 받은 목걸이", "", Equip_part.NECKLACE, "", SetName.CURSEOFSEAGOD, "", "",
				"지능 40 가변", "수속강 12 가변", null,
				"해신의 저주를 받은 팔찌", "", "--", "", "", "", "",
				"힘 40 가변", "수속강 12 가변", null,
				"해신의 저주를 받은 반지", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "수속강 12 가변", null,
				"해신의 저주를 받은 심해의 기운", "", "--", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "수속강 18 가변", "ㄷ 수속깍 15 선택 가변", null,
				"해신의 저주를 받은 심해석", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "수속강 18 가변", "ㄷ 수속깍 15 선택 가변", null,
				//서녘
				"붉게 물든 서녘의 목걸이", "", Equip_part.NECKLACE, "", SetName.ROMANTICE, "", "", CalculatorVersion.VER_1_1_a,
				"지능 40 가변", "물공뻥 4", "마공뻥 4", "독공뻥 4", "ㄷ 물공뻥 6 선택", "ㄷ 마공뻥 6 선택", "ㄷ 독공뻥 6 선택", "설명 추가 옵션은 목걸이, 팔찌, 반지 최대 1중첩", null,
				"붉게 물든 서녘의 팔찌", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_a,
				"힘 40 가변", "물공뻥 4", "마공뻥 4", "독공뻥 4", "ㄷ 물공뻥 6 선택", "ㄷ 마공뻥 6 선택", "ㄷ 독공뻥 6 선택", "설명 추가 옵션은 목걸이, 팔찌, 반지 최대 1중첩", null,
				"붉게 물든 서녘의 반지", "", "--", "", "", "", "", CalculatorVersion.VER_1_1_a,
				"힘 59 가변", "물공뻥 4", "마공뻥 4", "독공뻥 4", "ㄷ 물공뻥 6 선택", "ㄷ 마공뻥 6 선택", "ㄷ 독공뻥 6 선택", "설명 추가 옵션은 목걸이, 팔찌, 반지 최대 1중첩", null,
				"붉게 물든 서녘의 모자", "", "--", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "스킬 48-50 1", "크증뎀 8", null,
				"붉게 물든 서녘의 보석", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 힘 85", "ㄷ 지능 85", null,
				//길드악세
				"플레임 필드 네클레스", "", Equip_part.NECKLACE, "", SetName.GUILDACCESSORY_FIRE, "", "",
				"지능 40 가변", "화속강 8 가변", null,
				"불의 원념 팔찌", "", "--", "", "", "", "",
				"힘 40 가변", "화속강 8 가변", null,
				"제일검 아인 : 화염의 링", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "화속강 8 가변", null,
				"블리자드 뉴클러스 네클레스", "", Equip_part.NECKLACE, "", SetName.GUILDACCESSORY_WATER, "", "",
				"지능 40 가변", "수속강 8 가변", null,
				"프로즌 크리스탈 암릿", "", "--", "", "", "", "",
				"힘 40 가변", "수속강 8 가변", null,
				"아이스 프리즘 링", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "수속강 8 가변", null,
				
				//////////90제
				"베르딜의 물고기 팬턴트", "", Equip_part.NECKLACE, "", SetName.NONE, 90, false,
				"지능 42 가변", "물크 10", "마크 10", null,
				"스쿨디의 피쉬볼 암릿", "", "--", "", "", "", "",
				"힘 42 가변", "모속강 14 가변", null,
				"엘다의 트윈 피쉬 링", "", "--", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "물공 77 가변", "마공 77 가변", "독공 94 가변", null,

				"사방신 봉인구", "", Equip_part.NECKLACE, "", SetName.NONE, 90, false,
				"지능 42 가변", "모속강 18 가변", "추뎀 7", null,
				"베키의 병아리반 팔찌", "", "--", "", "", "", "",
				"힘 42 가변", "지능 42 가변", "증뎀 10", "크증뎀 10", "설명 철컹철컹", null,
				"제미누스 트윈링", "", "--", "", "", "", "",
				"힘 63 가변", "지능 63 가변", "설명 30초마다 자신을 포함한 랜덤한 파티원의 스킬 중\n쿨타임이 30초 이내의 모든 스킬 쿨타임 30% 감소", null,
				
				///////////////특수장비
				"크레이지 이반 선동자의 폭탄", "", Equip_part.AIDEQUIPMENT, Equip_type.SPECIALEQUIP, SetName.NONE, 85, false,
				"힘 39 가변", "지능 39 가변", "화속강 18 선택", "ㄷ %물방깍_템 10", "ㄷ %마방깍_템 10", null,
				"타우 캡틴의 힘의 근원", "", "", "", "", "", "",
				"힘 88 가변", "지능 88 가변", "ㄷ 힘 60 선택", "ㄷ 지능 60 선택", "ㄷ 물크 3 선택", "ㄷ 마크 3 선택", null,
				"람누의 채찍", "", "", "", "", "", "",
				"힘 55 가변", "지능 55 가변", "마공 110 가변", null,
				"고로의 망치", "", "", "", "", "", "",
				"힘 55 가변", "지능 55 가변", "물공 110 가변", null,
				"하이퍼 메카 타우의 기계심장", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "물공 116 가변", "마공 116 가변", "ㄷ 화속강 20 선택", null,
				"하이퍼 메카 타우의 연산장치", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "ㄷ 마크 20 가변", null,
				"아포피스의 검집", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "물공 88 가변", "마공 88 가변", "스킬 발도 1", "스킬 발도 강화 1", "스킬 리 귀검술 1",
				"스킬 마검발현 1", "스킬 폭명기검 1", "스킬 폭명기검 강화 1", "설명 신속한 무기 교체/신기의 손놀림의 무기 공격력 5% 증가, 물크 5% 증가", null,
				"민병대장의 완장", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "ㄷ 힘 80", "ㄷ 지능 80", "ㄷ 물공 40", "ㄷ 마공 40", "ㄷ 독공 55", null,
				"목각인형 제조서 : X", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "물공 53 가변", "마공 53 가변", "독공 69 가변", "스킬 광충노도 강화 1", "스킬 비트 드라이브 강화 1", "스킬 정크 스핀 강화 1", "스킬 와일드 캐넌 스파이크 강화 1",
				"스킬 기호지세 강화 1", "스킬 헥토파스칼 킥 강화 1", "스킬 니들 스핀 강화 1", "스킬 와일드 캐넌 스파이크 강화 1", "스킬 엑셀 스트라이크 강화 1", "스킬 기요틴 강화 1", "스킬 비기 : 염무개화 강화 1",
				"스킬 암살 강화 1", "스킬 환영검무 강화 1", "스킬 흑염의 칼라 강화 1", "스킬 아웃레이지 브레이크 강화 1", "스킬 부동명왕진 강화 1", "스킬 악즉참 강화 1", "스킬 검마격살 강화 1", "스킬 디스트로이어 강화 1",
				"스킬 혜성만리 강화 1", "스킬 분쇄 강화 1", "스킬 파이널 어택 강화 1", "스킬 정의의 심판 강화 1", "스킬 허리케인 롤 강화 1", "스킬 식신의 군 강화 1", "스킬 무쌍격 강화 1", "스킬 어둠의 권능 강화 1", fStat[10],
				"설명 귀검사/나이트/격투가/도적/프리스트 45레벨 스킬 Lv+1", "설명 아이고 죽겠다", null,
				"돌격대장의 어택 맥시마이저", "", "", "", "", "", "",
				"힘 121 가변", "지능 121 가변", "물공 122 가변", "마공 122 가변", "물크 7", "마크 7", null,
				"만개하는 흑연의 기운", "", "", "", "", "", "",
				"힘 94 가변", "지능 94 가변", "독공 165 가변", "ㄷ 암속강 15 선택", null,
				"더러운 보좌관의 갈비뼈", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", null,
				"그림시커 사제의 얼굴 가리개", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "암속강 18 가변", null,
				"만병을 퍼뜨리는 딱딱한 혀", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "물공 88 가변", "마공 88 가변", "독공 110 가변", "물크 5", "마크 5", null,
				"얼어붙은 휘장", "", "", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "수속강 18 가변", "ㄷ 수속깍 10 선택", null,
				"파묻힌 비명의 기억", "", "", "", SetName.BURIEDSCREAM, "", "",
				"힘 39 가변", "지능 39 가변", "물공 51 가변", "마공 51 가변", "독공 77 가변", "스킬 40-70 1", null,
				"인펙터 마니카이", "", "", "", SetName.NONE, 90, "",
				"힘 41 가변", "지능 41 가변", null,
				"무언의 건설자 장갑", "", "", "", SetName.TACITCONSTRUCTOR, "", "",
				"힘 41 가변", "지능 41 가변", "물크 9", "마크 9", "스킬 48 2", "스킬 85 1", null,
				"달콤한 허니스트로", "", "", "", SetName.TWIlIGHT, "", "", CalculatorVersion.VER_1_1_g,
				"힘 68 가변", "지능 68 가변", "물공 72 가변", "마공 72 가변", "독공 88 가변", "스킬 1-45 1", 
				"설명 지는녘을 보며 먹는 꿀은 너무나도 달콤하지", null,
				
				"아포피스의 눈", "", Equip_part.MAGICSTONE, "", SetName.NONE, 90, "",
				"힘 58 가변", "지능 58 가변", "물크 3", "마크 3", "ㄷ %물방깍_템 10 선택", "ㄷ %마방깍_템 10 선택", null,
				"응축된 사념구", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "독공 110 가변", null,
				"하트넥의 선혈구", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "ㄷ 추뎀 10 선택", "설명 추뎀 옵션의 조건은 출혈입니다", null,
				"폭주하는 위장자의 봉인구", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "암속강 24 가변", "ㄷ 추뎀 10 선택", "설명 추뎀 옵션의 조건은 백어택 공격시 입니다", null,
				"돌격대장의 코어 스톤", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "모속강 40 가변", null,
				"세상속의 작은 진리", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "모속강 29 가변", "ㄷ 모속강 10 선택", "ㄷ 모속강 10 선택", null,
				"저주로 물든 빙혼석", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "수속강 24 가변", "암속강 24 가변", "ㄷ 수속깍 15 선택", "ㄷ 암속깍 15 선택", null,
				"군주의 빛나는 위엄", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "명속강 22 가변", "ㄷ 힘 50 선택", "ㄷ 지능 50 선택", "ㄷ 독공 75 선택", "ㄷ 명속부여 선택", null,
				"만병을 퍼뜨리는 굳은 심장", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "모속강 40 가변", "물크 5", "마크 5", null,
				"악령 포식자의 핵", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "암속강 24 가변", "ㄷ 물공 50 선택", "ㄷ 마공 50 선택", "설명 물마공 증가의 조건은 피격입니다", null,
				"봉인된 거대 유령의 시선", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "암속강 30 가변", "ㄷ 암속깍 20 선택", "설명 암속깍의 조건은 캐스팅시 5%입니다", null,
				"그림시커의 빛나는 눈동자", "", "", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "암속강 24 가변", null,
				"루멘 칼리고", "", "", "",  "", 90, "",
				"힘 150 가변", "지능 150 가변", "추뎀 10", null,
				"무언의 건설자 수석", "", "", "", SetName.TACITCONSTRUCTOR, "", "",
				"힘 62 가변", "지능 62 가변", "ㄷ 스증뎀 12 선택", null, 
				"고대의 그라나투스", "", "", "", SetName.TWIlIGHT, "", "",  CalculatorVersion.VER_1_1_g,
				"힘 62 가변", "지능 62 가변", "ㄷ 스증뎀 10 선택", "설명 오늘 밤은 이르게 찾아노는구나..", "설명 ..은 대체 ...", null,
				
				///////////// 90 퀘전
				////////방어구
				"투기 상의", Item_rarity.LEGENDARY, Equip_part.ROBE, Equip_type.ALL, SetName.EKERN, 90, false, true, CalculatorVersion.VER_1_1_g,
				"힘 52 가변", "지능 52 가변", 
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 하의", "", "--", Equip_type.ALL, "", "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 52 가변", "지능 52 가변", 
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 어깨", "", "--", Equip_type.ALL, "", "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 42 가변", "지능 42 가변", 
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 벨트", "", "--", Equip_type.ALL, "", "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 31 가변", "지능 31 가변", 
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 신발", "", "--", Equip_type.ALL, "", "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 31 가변", "지능 31 가변", 
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				////////악세
				"투기 목걸이", "", Equip_part.NECKLACE, Equip_type.ACCESSORY, "" , "", "", true, CalculatorVersion.VER_1_1_g,
				"지능 42 가변",
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 팔찌", "", "--", Equip_type.ACCESSORY, "" , "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 42 가변",
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 반지", "", "--", Equip_type.ACCESSORY, "" , "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 63 가변", "지능 63 가변",
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 보조장비", "", "--", Equip_type.ACCESSORY, "" , "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 41 가변", "지능 41 가변",
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 마법석", "", "--", Equip_type.ACCESSORY, "" , "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 62 가변", "지능 62 가변",
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,
				"투기 귀걸이", "", Equip_part.EARRING, Equip_type.ACCESSORY, "" , "", "", true, CalculatorVersion.VER_1_1_g,
				"힘 62 가변", "지능 62 가변",
				"힘 40 가변 선택", "지능 40 가변 선택",
				"물공 124 가변 선택", "마공 124 가변 선택", "독공 154 가변 선택", "물크 18 가변 선택", "마크 18 가변 선택",
				"화속강 24 가변 선택", "수속강 24 가변 선택", "명속강 24 가변 선택", "암속강 24 가변 선택", "모속강 24 가변 선택", null,

				
				
				/////////기타
				"타오르는 정령의 반지", Item_rarity.EPIC, Equip_part.RING, Equip_type.ACCESSORY, SetName.NONE, 75, false,
				"힘 77 가변", "지능 77 가변", "화속강 35 가변", null,
				"얼어붙은 정령의 반지", Item_rarity.EPIC, Equip_part.RING, Equip_type.ACCESSORY, SetName.NONE, 75, false,
				"힘 77 가변", "지능 77 가변", "수속강 35 가변", null,
				"눈부신 정령의 반지", Item_rarity.EPIC, Equip_part.RING, Equip_type.ACCESSORY, SetName.NONE, 75, false,
				"힘 77 가변", "지능 77 가변", "명속강 35 가변", null,
				"칠흑같은 정령의 반지", Item_rarity.EPIC, Equip_part.RING, Equip_type.ACCESSORY, SetName.NONE, 75, false,
				"힘 77 가변", "지능 77 가변", "암속강 35 가변", null,
				"펠 로스 글로리", Item_rarity.UNIQUE, Equip_part.NECKLACE, "", "", 55, false,
				"지능 28 가변", "ㄷ 힘뻥 11 선택", null,
				"잊혀진 펠 로스 글로리", "", "", "", "", 65, "",
				"지능 31 가변", "ㄷ 힘뻥 10 선택", "설명 골든 펠 로스 글로리와 동일합니다", null,
				"데 로스 글로리", "", "", "", "", 60, "",
				"지능 28 가변", "ㄷ 힘뻥 10 선택", null,
				"에이션트 엘븐 링", "", Equip_part.RING, "", "", 55, "",
				"힘 41 가변", "지능 41 가변", "ㄷ 지능뻥 9 선택", null,
				"잊혀진 에이션트 엘븐 링", "", Equip_part.RING, "", "", 65, "",
				"힘 45 가변", "지능 45 가변", "ㄷ 지능뻥 8 선택", null,
				"할기의 본링", "", Equip_part.RING, "", "", 40, "",
				"힘 69 가변", "지능 35 가변", "물크 3", "ㄷ 물공뻥 50 선택", null,
				"강력한 자의 절대반지", "", Equip_part.RING, "", "", 50, "",
				"힘 43 가변", "지능 43 가변", "ㄷ 힘뻥 10", null,
				"현명한 자의 절대반지", "", Equip_part.RING, "", "", 50, "",
				"힘 43 가변", "지능 43 가변", "ㄷ 지능뻥 10", null,
				"영혼 추적장치", Item_rarity.EPIC, Equip_part.NECKLACE, "", "", 55, false, CalculatorVersion.VER_1_1_b,
				"지능 30 가변", "ㄷ 물공뻥 20 선택", "ㄷ 마공뻥 20 선택", "ㄷ 독공뻥 20 선택", null,
		};
		
		return data;
	}
	
	public static Object[] weaponInfo()
	{
		FunctionStat fStat[] = new FunctionStat[70];
		
		//다중선택시 마지막 옵션으로 적용(단리연산항)
		FunctionStat multiCheck = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				int count=0;
				double sum=0;
				Equipment equipment = (Equipment)item;
				int num = equipment.dStat.statList.size();
				for(int i=0; i<num; i++){
					try {
						if(equipment.dStat.statList.get(i).enabled){
							count++;
							if(i!=num-1) sum-=equipment.dStat.statList.get(i).stat.getStatToDouble();
						}
						else if(i==num-1) sum+=equipment.dStat.statList.get(i).stat.getStatToDouble();
					}
					catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				if(count>1) statList.addStatList(equipment.dStat.statList.getFirst().name, sum);
				return statList; 
			}
		};
		
		//이기 속성
		FunctionStat enableElement = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				StatusList statList = new StatusList();
				statList.addStatList(type, 0, true, false, false);
				return statList;
			}
		};
		
		//레홀
		fStat[0] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				double percent=0;
				for(Skill skill : character.getSkillList())
				{
					if(skill.getName().equals("충전 레이저 라이플")){
						try {
							percent = skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.getFirst().stat.getStatToDouble();
							break;
						} catch (StatusTypeMismatch e) {
							e.printStackTrace();
							break;
						}
					}
				}
				double inc = (100+percent*1.3)/(100+percent);
				StatusList statList = new StatusList();
				statList.addSkill_damage("레이저 라이플", inc);
				
				return statList;
			}
		};
		
		//실불
		fStat[1] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				//if(monster.getStat("종족"))			//TODO
				return statList;
			}
		};
		
		//골드럭스
		fStat[2] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.RANGER_F || character.getJob()==Job.RANGER_M)
					statList.addSkillRange(1, 80, 1, false);
				
				int sum=0;
				int epic=0;
				int legendary=0;
				int unique=0;				
				switch(character.getItemSetting().weapon.getRarity()){
				case EPIC:
					epic++;
					break;
				case LEGENDARY:
					legendary++;
					break;
				case UNIQUE:
					unique++;
					break;
				default:
					break;
				}				
				for(Equipment e : character.getItemSetting().equipmentList.values())
				{
					switch(e.getRarity()){
					case EPIC:
						epic++;
						break;
					case LEGENDARY:
						legendary++;
						break;
					case UNIQUE:
						unique++;
						break;
					default:
						break;
					}
				}
				
				sum+= (epic<=6 ? epic*9 : 54);
				epic= sum/9;
				sum+= (legendary<=6-epic ? legendary*7 : 7*(6-epic));
				legendary= (legendary<=6-epic ? legendary : 6-epic);
				sum+= (unique<=6-epic-legendary ? unique*7 : 7*(6-epic-legendary));
				
				statList.addStatList("추뎀", sum);
				return statList;
			}
		};
		
		//이온 리펄서
		fStat[3] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getJob().charType==Character_type.GUNNER_F) statList.addStatList("명속강", 40);
				else if(character.getJob().charType==Character_type.GUNNER_M) statList.addStatList("화속강", 40);
				return statList;
			}
		};
		
		//도굴왕
		fStat[4] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.LAUNCHER_F){
					for(Skill skill : character.getSkillList()){
						if(skill.getName().equals("중화기 마스터리")){
							if(skill.getActiveEnabled()){
								double buffAmount;
								int skillLevel;
								try {
									buffAmount = skill.getSkillLevelInfo(true, character.isBurning()).stat.findStat(StatList.BUF_INC).stat.getStatToDouble();
									statList.addStatList("증뎀버프", ((buffAmount+100+10)/(buffAmount+100) -1)*100 );
									skillLevel = skill.getSkillLevel(true, character.isBurning());
									if(skillLevel>=10 && skillLevel<15) statList.addStatList("물공뻥", 2);
									else statList.addStatList("물공뻥", 3);
								} catch (StatusTypeMismatch e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				return statList;
			}
		};
		
		//테슬라
		fStat[5] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.LAUNCHER_F){
					statList.addSkill("M-3 화염방사기", 3);
					statList.addSkill("화염 강타", 4);
				}
				else if(character.getJob()==Job.LAUNCHER_M){
					statList.addSkill("레이저 라이플", 3);
					statList.addSkill("충전 레이저 라이플", 4);
				}
				return statList;
			}
		};
		
		//무게추(18%), 뉴링턴(18%), 히게-히자키리(20%)
		fStat[6] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				int value = 18;
				if(weapon.getName().equals("히게-히자키리")) value=20;
				switch(type){
				case FIRE:
					statList.addStatList("화추뎀", value);
					break;
				case WATER:
					statList.addStatList("수추뎀", value);
					break;
				case LIGHT:
					statList.addStatList("명추뎀", value);
					break;
				case DARKNESS:
					statList.addStatList("암추뎀", value);
					break;
				default:
					statList.addStatList("화추뎀", value);
					break;
				}
				return statList;
			}
		};
		//천총
		fStat[7] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.CHAOS){
					statList.addStatList("지능", 120);
					statList.addStatList("독공", 75);
				}
				return statList;
			}
		};
		
		//암별
		fStat[8] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Element_type type = CalculateElement.getLargestType(character.dungeonStatus);
				StatusList statList = new StatusList();
				statList.addStatList(type, 25, false, false, false);
				return statList;
			}
		};
		
		//염화도
		fStat[9] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				boolean enabled_35 = weapon.dStat.statList.get(0).enabled;
				boolean enabled_10 = weapon.dStat.statList.get(1).enabled;
				boolean enabled_7 = weapon.dStat.statList.get(2).enabled;
				boolean enabled_5 = weapon.dStat.statList.get(3).enabled;
				if(enabled_7 && enabled_35) statList.addStatList("화추뎀", -35);
				if(enabled_5 && enabled_10) statList.addStatList("화추뎀", -10);
				return statList;
			}
		};
		
		//트리니티
		fStat[10] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				boolean enabled_33 = weapon.dStat.statList.get(0).enabled;
				boolean enabled_333 = weapon.dStat.statList.get(1).enabled;
				boolean enabled_21 = weapon.dStat.statList.get(2).enabled;
				if(enabled_21 && enabled_33) statList.addStatList("추뎀", -33);
				if(enabled_21 && enabled_333) statList.addStatList("추뎀", -333);
				return statList;
			}
		};
		
		//거누형
		fStat[11] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				if(weapon.getReinforce()>15) statList.addStatList("추뎀", 15);
				else statList.addStatList("추뎀", weapon.getReinforce());
				//TODO 맹룡 베기수 1 감소, 딜 50% 증가
				return statList;
			}
		};
		
		//이빨
		fStat[12] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				int checked=0;
				for(StatusAndName s: weapon.dStat.statList)
					if(s.enabled) checked++;
				if(checked>=4) statList.addStatList("추뎀", -18);
				else statList.addStatList("추뎀", -checked*5);
				return statList;
			}
		};
		
		//귀면도
		fStat[13] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				try {
					int STR = (int) character.dungeonStatus.getStat(StatList.STR);
					int INT = (int) character.dungeonStatus.getStat(StatList.INT);
					if(STR>INT){
						statList.addStatList(StatList.INT, new StatusInfo(-300));
						statList.addStatList(StatList.STR, new StatusInfo(300));
					}
					else{
						statList.addStatList(StatList.INT, new StatusInfo(300));
						statList.addStatList(StatList.STR, new StatusInfo(-300));
					}
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				for(Skill skill : character.getSkillList())
					if(skill.firstLevel==40 || skill.firstLevel==50)
						statList.addSkill_damage(skill.getName(), 30);
				return statList;
			}
		};
		
		//신라비
		fStat[14]= new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				int water=0, dark=0;
				try {
					water = (int) (character.dungeonStatus.getStat(StatList.ELEM_WATER)
							+character.dungeonStatus.getStat(StatList.ELEM_ALL));
					dark = (int) (character.dungeonStatus.getStat(StatList.ELEM_DARKNESS)
							+character.dungeonStatus.getStat(StatList.ELEM_ALL));
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				double dark_sum=0, water_sum=0, normal_sum=0;
				boolean dark_flag=false, water_flag=false;
				Equipment equipment = (Equipment)item;
				for(int i=0; i<7; i++){
					try {
						if(equipment.dStat.statList.get(i).enabled){
							if(i<3){
								if(!dark_flag && dark>=80) dark_flag=true;
								else dark_sum+=equipment.dStat.statList.get(i).stat.getStatToDouble();
							}
							else if(i<6){
								if(!water_flag && (water>=80 && dark<80)) water_flag=true;
								else water_sum+=equipment.dStat.statList.get(i).stat.getStatToDouble();
							}
							else if(dark_flag || water_flag || (dark<80 && water<80)) 
								normal_sum+=equipment.dStat.statList.get(i).stat.getStatToDouble();
						}
					}
					catch (StatusTypeMismatch e) {
						e.printStackTrace();
					}
				}
				statList.addStatList(StatList.DAM_ADD_WATER, -water_sum);
				statList.addStatList(StatList.DAM_ADD_DARKNESS, -dark_sum);
				statList.addStatList(StatList.DAM_ADD, -normal_sum);
				return statList;
			}
		};
		
		//프리스트 스킬증가
		fStat[15]= new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Weapon weapon = (Weapon)item;
				StatusList statList = new StatusList();
				if(weapon.getName().equals("행성파괴자")){
					if(!character.getJob().equals(Job.EXORCIST)){
						int count=0;
						for(StatusAndName s: weapon.dStat.statList)
							if(s.enabled) count++;
						statList.addSkillRange(1, 85, -count, false);
					}
				}
				else if(weapon.getName().equals("음양사천")){
					if(!character.getJob().equals(Job.EXORCIST)){
						statList.addSkillRange(48, 80, -2, false);
					}
				}
				else if(weapon.getName().equals("탐라선인석")){
					if(!character.getJob().equals(Job.INFIGHTER)){
						statList.addSkillRange(1, 85, -1, false);
					}
				}
				return statList;
			}
		};
		
		//소울 디바우링
		fStat[16] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				int darkness =0;
				StatusList statList = new StatusList();
				try {
					darkness = (int) (character.dungeonStatus.getStat(StatList.ELEM_ALL)+character.dungeonStatus.getStat(StatList.ELEM_DARKNESS));
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				if(darkness>=120) statList.addStatList("마공뻥", 16);
				return statList;
			}
		};
		
		//크림슨로드, 서킷 세레브레이트
		fStat[17] = new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Weapon weapon = (Weapon)item;
				int inc1 = weapon.getReinforce()*3;
				if(inc1>30) inc1=30;
				int inc2 = (weapon.getReinforce()-10)*5;
				if(inc2>10) inc2=10;
				else if(inc2<0) inc2=0;
				int inc3 = weapon.getReforge()*3;
				
				StatusList statList = new StatusList();
				statList.addStatList("증뎀", inc1);
				statList.addStatList("스증뎀", inc2);
				statList.addStatList("추뎀", inc3);
				return statList;
			}
		};
		
		//래쿤배큠
		fStat[18]= new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(!character.getJob().equals(Job.CREATOR))
					statList.addSkillRange(1, 85, 1, false);
				return statList;
			}
		};
		
		//웨리
		fStat[18]= new FunctionStat(){
			private static final long serialVersionUID = 1L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Weapon weapon = (Weapon)item;
				try {
					if(Double.compare(weapon.dStat.statList.getFirst().stat.getStatToDouble(), 64)==0
							|| Double.compare(weapon.dStat.statList.getLast().stat.getStatToDouble(), 64)==0) {
						if(character.getJob().equals(Job.CREATOR))
							statList.addSkill("창조의 공간", 2);
						else statList.addSkillRange(85, 85, 2, false);
					}
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			}
		};
		
		/* Format
		"", "", "", Weapon_detailType., "", 80, false,
		"물공  가변", "마공  가변", "독공 611 가변", "힘  가변", "지능  가변",
		null,
		"", "", "", "", "", 85, "",
		"물공  가변", "마공  가변", "독공 648 가변", "힘  가변", "지능  가변", 
		null,
		"", "", "", "", "", "", "",
		"물공  가변", "마공  가변", "독공 648 가변", "힘  가변", "지능  가변", 
		null,
		"구원의 이기 - ", "", "", "", "", "", true,
		"물공  가변", "마공  가변", "독공 648 가변", "힘  가변", "지능  가변", 
		"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
		"", "", "", "", "", 90, false,
		"물공  가변", "마공  가변", "독공 686 가변", "힘  가변", "지능  가변", 
		null,
		"창성의 구원자 - ", "", "", "", "", 90, true,
		"물공  가변", "마공  가변", "독공 686 가변", "힘  가변", "지능  가변", 
		"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
		*/
		
		Object[] data = new Object[] {
				//////////////////거너
				///////////에픽
				/////핸드캐넌
				"붐 엔드 붐", Item_rarity.EPIC, Equip_part.WEAPON, Weapon_detailType.GUN_HCANON, SetName.NONE, 80, false,
				"물공 1157 가변", "마공 694 가변", "독공 611 가변", "힘 101 가변", "화속부여", "화속 22 가변", "스킬 슈타이어 대전차포 % 50",
				"ㄷ 스킬 슈타이어 대전차포 % 100 선택", "ㄷ 스킬 슈타이어 대전차포 % 30 선택", multiCheck, "설명 평균값과 2발 설정 모두를 선택할 경우 평균값으로 계산됩니다.", null,
				"우요의 황금 캐넌", "", "", "", "", 85, "",
				"물공 1230 가변", "마공 738 가변", "독공 648 가변", "힘 107 가변",
				"모속강 30 가변", "추뎀 20", "ㄷ 물공뻥 12 선택", "ㄷ 마공뻥 12 선택", "ㄷ 독공뻥 12 선택", null,
				"레이저 홀릭", "", "", "", "", "", "",
				"물공 1230 가변", "마공 738 가변", "독공 648 가변", "힘 107 가변",
				"명속강 35 가변", "스킬 레이저 라이플 5", "스킬 에인션트 트리거 2", "스킬 새틀라이트 빔 2", "스킬 충전 레이저 라이플 2", "스킬 충전 레이저 라이플 % 30", "명추뎀 10", null,
				"구원의 이기 - 핸드캐넌", "", "", "", "", "", true,
				"물공 1230 가변", "마공 738 가변", "독공 648 가변", "힘 107 가변",
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"거포 우르반", "", "", "", "", 90, false,
				"물공 1302 가변", "마공 781 가변", "독공 686 가변", "힘 112 가변", "ㄷ 힘뻥 20", "추뎀 40", null,
				"창성의 구원자 - 핸드캐넌", "", "", "", "", "", true,
				"물공 1302 가변", "마공 781 가변", "독공 686 가변", "힘 112 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				/////머스켓
				"화염의 닐 스나이퍼", "", "", Weapon_detailType.GUN_MUSKET, "", 80, false, CalculatorVersion.VER_1_0_d,
				"물공 1065 가변", "마공 926 가변", "독공 611 가변", "힘 68 가변", "지능 101 가변",
				"화속강 24 가변", "스킬 록 온 서포트 3", "설명 록 온 서포트 공격시 35% 화속성 추가 데미지(미구현)", "설명 평타 공격 시 7% 화속성 추가 데미지", null, 	//TODO 닐스구현
				"코드넘버 608", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_d, 
				"물공 1131 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 107 가변", 
				"스킬 M18 클레이모어 5", "스킬 G-61 중력류탄 2", "ㄷ 증뎀 10 선택", "추뎀 22", null,
				"룰 오브 썸", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1131 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 107 가변", 
				"물크 10", "마크 10", "스킬 G-14 파열류탄 4", "스킬 G-35L 섬광류탄 4", "스킬 G-18C 빙결류탄 4", "스킬 네이팜 탄 4", "스킬 유탄 마스터리 1", "스킬 증명의 열쇠 1", "스킬 전장의 영웅 1",
				"스킬 G-14 파열류탄 % 25", "스킬 G-35L 섬광류탄 % 25", "스킬 G-18C 빙결류탄 % 25", "스킬 G-38ARG 반응류탄 % 25", "스킬 G-96 열압력유탄 % 25", null,
				"구원의 이기 - 머스켓", "", "", "", "", "", true,
				"물공 1131 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 107 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"Code N : 오라클", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_d,
				"물공 1198 가변", "마공 1042 가변", "독공 686 가변", "힘 75 가변", "지능 112 가변", 
				"물크 25", "마크 25", "스킬 증명의 열쇠 3", "스킬 전장의 영웅 3", "ㄷ 스증뎀 30", "TP스킬 1-85 1", null,
				"창성의 구원자 - 머스켓", "", "", "", "", "", true,
				"물공 1198 가변", "마공 1042 가변", "독공 686 가변", "힘 75 가변", "지능 112 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				/////리볼버
				"외톨이 잭볼버", "", "", Weapon_detailType.GUN_REVOLVER, "", 80, false,
				"물공 1114 가변", "마공 834 가변", "독공 691 가변", "힘 68 가변",
				"물크 15", "스킬 니들 소배트 4", "스킬 탑스핀 2", "스킬 소닉 스파이크 2", "스킬 건블레이드 2", "스킬 마하킥 4", "스킬 윈드밀 2", "스킬 에어레이드 2", "스킬 스타일리쉬 1", null,
				"총열개조 웨블리 마크", "", "", "", "", 80,  "",
				"물공 991 가변", "마공 834 가변", "독공 611 가변", "힘 68 가변", 
				"화속부여", "물크 2", "스킬 데스 바이 리볼버 2", "스킬 난사 % 35", "스킬 권총의 춤 % 35", "ㄷ 화속추 10 선택", null,
				"로드 오브 레인저", "", "", "", "", 85,  "",
				"물공 1053 가변", "마공 886 가변", "독공 648 가변", "힘 72 가변",
				"물크 12", "마크 10", "증뎀 60 가변", null, 
				"실버 불렛", "", "", "", "", "",  "", 
				"물공 1053 가변", "마공 886 가변", "독공 648 가변", "힘 72 가변",
				"명속강 35 가변", "명속부여", fStat[1], "설명 언데드, 악마, 정령 타입 적 공격 시 18% 추가 데미지(미구현)", "설명 은탄 탄 무제한으로 변경", null,
				"구원의 이기 - 리볼버", "", "", "", "", "", true,
				"물공 1053 가변", "마공 886 가변", "독공 648 가변", "힘 72 가변", "물크 2",
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"골드 럭스", "", "", "", "", 90, false,
				"물공 1114 가변", "마공 937 가변", "독공 686 가변", "힘 75 가변", 
				"물크 2", fStat[2], "설명 레인저 1~80 레벨 모든 스킬 Lv +1(특성 스킬 제외)", "설명 장착 중인 장비 레어리티에 따른 추가데미지 효과 발생", "설명 에픽-9% / 레전더리-7% / 유니크-5% (최대 6부위)", null,
				"창성의 구원자 - 리볼버", "", "", "", "", "", true,
				"물공 1114 가변", "마공 937 가변", "독공 686 가변", "힘 75 가변", "물크 2",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				/////보우건
				"폭풍의 역살", "", "", Weapon_detailType.GUN_BOWGUN, "", 80, false, CalculatorVersion.VER_1_0_d,
				"물공 834 가변", "마공 926 가변", "독공 611 가변", "힘 68 가변", "지능 34 가변",
				"명속강 24 가변", "명속부여", "물크 3", "스킬 은탄 3", "스킬 G-35L 섬광류탄 3", "스킬 병기 숙련 3", "ㄷ 명속깍 15 선택", null,
				"제네럴 보우건", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_d,
				"물공 886 가변", "마공 983 가변", "독공 648 가변", "힘 237 가변", "지능 200 가변", 
				"물크 3", "스킬 병기 숙련 5", "스킬 특성탄 5", "스킬 은탄 5", "스킬 오버 차지 5", "스킬 강화탄 2", "추뎀 22", "ㄷ 물공뻥 6", "ㄷ 마공뻥 6", "ㄷ 독공뻥 6", null,
				"얼음 불꽃의 보우건", "", "", "", "", "", "",
				"물공 886 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 35 가변", 
				"화속강 40 가변", "수속강 40 가변", "화속부여", "수속부여", "물크 3", "ㄷ 추뎀 15 선택", "ㄷ 추뎀 15 선택", null,
				"구원의 이기 - 보우건", "", "", "", "", "", true,
				"물공 886 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 35 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"헬 하보크", "", "", "", "", 90,  false, CalculatorVersion.VER_1_0_d,
				"물공 937 가변", "마공 1042 가변", "독공 686 가변", "힘 75 가변", "지능 37 가변", 
				"물크 13", "마크 10", "스킬 병기 숙련 5", "추크증 40", null,
				"창성의 구원자 - 보우건", "", "", "", "", "", true,
				"물공 937 가변", "마공 1042 가변", "독공 686 가변", "힘 75 가변", "지능 37 가변", "물크 3",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				/////자동권총
				"반자동 셔플렉터", "", "", Weapon_detailType.GUN_AUTOPISTOL, "", 80, false,
				"물공 694 가변", "마공 1019 가변", "독공 611 가변", "지능 68 가변",
				"스킬 RX-78 랜드러너 3", "스킬 Ez-8 카운트다운 3", "스킬 트랜스폼 : G-0 배틀로이드 2", "스킬 G 익스텐션 3", "스킬 게이볼그 펀치 2", "스킬 최후의 투지 3", "ㄷ 명속 12", "ㄷ 화속 12", "스킬 RX-78 랜드러너 % 50", null,
				"마이스터의 분노", "", "", "", "", 85,  "",
				"물공 738 가변", "마공 1082 가변", "독공 648 가변", "지능 72 가변", 
				"설명 로보틱스 힘, 지능, 스킬 공격력 증가율 40% 증가(미구현)", "스킬 메카드롭 % 30", "스킬 트랜스 폼 : G-1 코로나 % 25", "스킬 트랜스 폼 : G-2 롤링썬더 % 25", "스킬 트랜스 폼 : G-3 랩터 % 25", null,
				"오픈 파이어", "", "", "", "", "",  "",
				"물공 738 가변", "마공 1082 가변", "독공 648 가변", "지능 72 가변", 
				"화속강 35 가변", "명속강 35 가변", "스킬 RX-78 랜드러너 2", "스킬 Ez-8 카운트다운 2", "스킬 Ex-S 바이퍼 2", "스킬 공중 전폭 메카 : 게일 포스 2", "스킬 로봇 전폭 강화 2", "스킬 리미트 오버!! 2",
				"스킬 공중 전투 메카 : 템페스터 2", "ㄷ 화속강 12 선택", "ㄷ 명속강 12 선택", "ㄷ 마공뻥 15 선택", null,
				"구원의 이기 - 자동권총", "", "", "", "", "", true,
				"물공 738 가변", "마공 1082 가변", "독공 648 가변", "지능 72 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"이온 리펄서", "", "", "", "", 90,  false,
				"물공 781 가변", "마공 1146 가변", "독공 686 가변", "지능 141 가변",
				"ㄷ 스증 40", fStat[3], "설명 캐릭터가 거너(여)일 경우 명속성 강화 40 증가", "설명 캐릭터가 거너(남)일 경우 화속성 강화 40 증가", null,
				"창성의 구원자 - 자동권총", "", "", "", "", "", true,
				"물공 781 가변", "마공 1146 가변", "독공 686 가변", "지능 141 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				///////////레전더리
				/////핸드캐넌
				"돌격대장의 슈퍼 캐넌", Item_rarity.LEGENDARY, "", Weapon_detailType.GUN_HCANON, "", 85, false,
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 101 가변",
				"스킬 중화기 마스터리 3", "스킬 FM-31 그레네이드 런처 % 25", "스킬 FM-92 스팅어 % 44","스킬 FM-92 mk2 랜서 % 35", "스킬 양자 폭탄 % 22", null,
				"그라인딩 오버필드", "", "", "", "", "", "",
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 101 가변", 
				"ㄷ 힘 50 선택", "ㄷ 물공뻥 5 선택", "ㄷ 독공뻥 4 선택", "ㄷ 힘 50 선택", "ㄷ 물공뻥 5 선택", "ㄷ 독공뻥 4 선택",
				"ㄷ 힘 110 선택", "ㄷ 물공뻥 14 선택", "ㄷ 독공뻥 12 선택", null,
				"도굴왕이 숨겨둔 천계의 유물", "", "", "", "", "", "",
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 101 가변", 
				"스킬 화염 강타 % 20", "스킬 레이저 라이플 % 20", fStat[4], "설명 중화기 마스터리 핸드캐넌 공격력 증가율 20% 증가", "설명 중화기 마스터리 공격력 증가율 10% 추가 증가", null,
				"카르텔 에어머신 기관포", "", "", "", "", "", "",
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 145 가변", 
				"스킬 슈타이어 대전차포 % 20", "설명 M-137 개틀링건 초당 발사수 2발 증가, 발사시간 1초 증가(미구현)", "설명 FM-그레네이드 런처 유탄 발사 수 2 증가(미구현)", null,
				"진혼의 캐넌", "", "", "", "", "", "",
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 134 가변", 
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 캐넌", "", "", "", "", "", "",
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 162 가변", 
				"물크 7", "마크 7", "추뎀 16", null, 
				"플루의 집념", "", "", "", "", "", "", 
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 101 가변", 
				"ㄷ 힘 250 선택", "ㄷ 물크 25 선택", null,
				"테라 : 리컨스트럭션 캐넌", "", "", "", "", 90, "",
				"물공 1234 가변", "마공 740 가변", "독공 642 가변", "힘 107 가변", 
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"테슬라 캐넌", "", "", "", "", "", "",
				"물공 1234 가변", "마공 740 가변", "독공 642 가변", "힘 107 가변", 
				"증뎀 20", fStat[5], "설명 여런 - 화방+3 화강+4", "설명 남런 - 레이저+5 충레라+2", null,
				
				/////리볼버
				"진혼의 리볼버", "", "", Weapon_detailType.GUN_REVOLVER, "", 85, false,
				"물공 997 가변", "마공 838 가변", "독공 607 가변", "힘 101 가변",
				"물크 6", "마크 4", "추뎀 11", null,
				"리버레이션 리볼버", "", "", "", "", "", "",
				"물공 997 가변", "마공 838 가변", "독공 607 가변", "힘 129 가변",
				"물크 9", "마크 7", "추뎀 16", null,
				"맥스의 육혈포", "", "", "", "", "", "",
				"물공 997 가변", "마공 838 가변", "독공 607 가변", "힘 68 가변",
				"물크 2", "스킬 난사 2", "스킬 헤드샷 2", "스킬 권총의 춤 2", "스킬 난사 % 16.67", "스킬 권총의 춤 % 25", "ㄷ 스킬 헤드샷 % 40 가변", "ㄷ 스킬 난사 % 40 가변", "ㄷ 스킬 권총의 춤 % 40 가변", null,
				"뒤틀린 공간의 탄", "", "", "", "", "", "",
				"물공 997 가변", "마공 838 가변", "독공 607 가변", "힘 68 가변",
				"물크 2", "설명 헤드샷, 웨스턴 파이어 시전 시 전방으로 타이오릭의 충격파 발생", null,
				"눈 뜬 악몽의 오탁", "", "", "", "", "", "",
				"물공 997 가변", "마공 838 가변", "독공 607 가변", "힘 68 가변",
				"물크 2", "암속강 14 가변", "암속부여", "스킬 데스 바이 리볼버 2", "ㄷ 물크 10 선택", "ㄷ 크증뎀 15 선택", null,
				"광란의 무음 : 신속", "", "", "", "", "", "",
				"물공 997 가변", "마공 838 가변", "독공 607 가변", "힘 68 가변",
				"물크 2", "암속강 26 가변", "암속부여", "설명 공격시 3% 확률로 카모플라쥬, 카모플라쥬 상태일 때 공격시 10% 확률로 적의 HP 30% 감소", null,
				"테라 : 리컨스트럭션 리볼버", "", "", "", "", 90, "",
				"물공 1057 가변", "마공 889 가변", "독공 642 가변", "힘 72 가변",
				"물크 14", "마크 12", "스킬 1-85 1", null,
				"스카이 리퀴드", "", "", "", "", "", "",
				"물공 1057 가변", "마공 889 가변", "독공 642 가변", "힘 72 가변",
				"물크 2", "수속강 30 가변", "수속부여", "수추뎀 8", "설명 패스트 드로우, 킬 포인트 쿨타임 25% 감소", null,
				
				/////머스켓
				"진혼의 머스켓", "", "", Weapon_detailType.GUN_MUSKET, "", 85, false, CalculatorVersion.VER_1_0_d,
				"물공 1070 가변", "마공 931 가변", "독공 607 가변", "힘 101 가변", "지능 134 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 머스켓", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1070 가변", "마공 931 가변", "독공 607 가변", "힘 129 가변", "지능 162 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"맥스의 골드 머스켓", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1120 가변", "마공 974 가변", "독공 607 가변", "힘 68 가변", "지능 101 가변",
				"물크 5", "마크 5", "스킬 병기 숙련 1", "스킬 오버 차지 2", "설명 공중사격 공격력 50% 증가, 발사수 5 증가, 공중사격 시전 시 슈퍼아머 버프 발동(미구현)",  null,
				"아이언 필러 머스켓", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1070 가변", "마공 931 가변", "독공 607 가변", "힘 68 가변", "지능 101 가변",
				"스킬 관통탄 % 22", "설명 ??? : 철갑탄님 커맨더좀 제때제때 주세요", null,
				"하이퍼 메카 타우의 강화소총", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1070 가변", "마공 931 가변", "독공 607 가변", "힘 68 가변", "지능 101 가변",
				"화속강 22 가변", "화속부여", "스킬 병기 숙련 3", "스킬 유탄 마스터리 2", "스킬 병기강화 2", null,
				"빌라이 비장의 저격 장총", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1165 가변", "마공 931 가변", "독공 607 가변", "힘 68 가변", "지능 101 가변",
				"스킬 교차 사격 2", "스킬 록 온 서포트 3", "스킬 교차 사격 % 10", "ㄷ 물크 100 선택", "ㄷ 추뎀 25 선택", "설명 록 온 서포트 시전 시 5초동안 물크 100% 증가, 40% 확률로 25% 추가데미지", null,
				"테라 : 리컨스트럭션 머스켓", "", "", "", "", 90, "", CalculatorVersion.VER_1_0_d,
				"물공 1136 가변", "마공 988 가변", "독공 642 가변", "힘 72 가변", "지능 107 가변", 
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"A.B. 램퍼드 라이플", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1136 가변", "마공 988 가변", "독공 642 가변", "힘 72 가변", "지능 107 가변",
				"물크 8", "마크 8", "크증뎀 22", null,
				
				//////보우건
				"진혼의 보우건", "", "", Weapon_detailType.GUN_BOWGUN, "", 85, false, CalculatorVersion.VER_1_0_d,
				"물공 838 가변", "마공 931 가변", "독공 607 가변", "힘 101 가변", "지능 67 가변",
				"물크 7", "마크 4", "추뎀 11", null,
				"리버레이션 보우건", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 838 가변", "마공 931 가변", "독공 607 가변", "힘 129 가변", "지능 95 가변",
				"물크 10", "마크 7", "추뎀 16", null,
				"라비네터의 악몽", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 838 가변", "마공 931 가변", "독공 607 가변", "힘 68 가변", "지능 34 가변",
				"물크 7", "마크 7", "ㄷ 크증뎀 22 선택", "ㄷ 추뎀 22 선택", "설명 크증뎀, 추뎀 조건 : 암흑상태의 적 공격 시", null,
				"잠식되는 검은 불꽃", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 838 가변", "마공 931 가변", "독공 607 가변", "힘 68 가변", "지능 34 가변", "물크 3",
				"스킬 폭발탄 % 15", "스킬 듀얼 플리커 1", "스킬 병기강화 1", "설명 원래는 듀플 병강 스증 2% 추가증가지만 몰라 귀찮아요", null,
				"소리없는 절규의 날카로움", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 838 가변", "마공 931 가변", "독공 607 가변", "힘 68 가변", "지능 34 가변",
				"물크 11", "마크 8", "명속강 24 가변", "크증뎀 17", null,
				"카르텔의 강철 와이어 보우건", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 838 가변", "마공 931 가변", "독공 607 가변", "힘 68 가변", "지능 34 가변",
				"물크 8", "마크 6", "크증뎀 20", null,
				"테라 : 리컨스트럭션 보우건", "", "", "", "", 90, "", CalculatorVersion.VER_1_0_d,
				"물공 889 가변", "마공 988 가변", "독공 642 가변", "힘 72 가변", "지능 35 가변", 
				"물크 15", "마크 12", "스킬 1-85 1", null,
				"블랙 아르쿠스", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 889 가변", "마공 988 가변", "독공 642 가변", "힘 72 가변", "지능 35 가변",
				"물크 3", "ㄷ 힘뻥 15", "ㄷ 지능뻥 15", "추뎀 15", null,
				
				/////////////////귀검사
				/////////에픽
				/////대검
				"데빌 오브 플레어", Item_rarity.EPIC, Equip_part.WEAPON, Weapon_detailType.SWORD_LONGSWORD, "", 80, "",
				"물공 1198 가변", "마공 898 가변", "독공 611 가변", "힘 68 가변",
				"화속강 22 가변", "화속부여", "ㄷ 화속강 15", "화추뎀 11 선택", "화추뎀 24 선택", "설명 화추뎀 기댓값 : 10.5%", null,
				"별운검", "", "", "", "", 85, "",
				"물공 1295 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", 
				"증뎀 50", "ㄷ 힘뻥 -20 선택", null,
				"양검 : 간장", "", "", "", "", "", "",
				"물공 1267 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변",
				"화속강 40 가변", "화속부여", "ㄷ 명속부여 선택", "ㄷ 물크 10 선택", "ㄷ 화속강 30 선택", "ㄷ 명속강 70 선택", "ㄷ 크증뎀 15 선택", "ㄷ 화추뎀 12 선택", null,
				"구원의 이기 - 대검", "", "", "", "", "", true,
				"물공 1267 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"성검 : 엑스칼리버", "", "", "", "", 90, false,
				"물공 1337 가변", "마공 1002 가변", "독공 686 가변", "힘 75 가변", 
				"모속강 100 가변", "스증뎀 18", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"창성의 구원자 - 대검", "", "", "", "", "", true,
				"물공 1337 가변", "마공 1002 가변", "독공 686 가변", "힘 75 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				/////둔기
				"분노한 헤라클레스", "", "", Weapon_detailType.SWORD_CLUB, "", 80, false,
				"물공 1098 가변", "마공 948 가변", "독공 611 가변", "힘 101 가변",
				"ㄷ 힘 500 가변", "ㄷ 힘 800 가변", null,
				"미스틸테인", "", "", "", "", 85, "",
				"물공 1162 가변", "마공 1003 가변", "독공 648 가변", "힘 167 가변", 
				"암속강 50 가변", "암속부여", "추뎀 22", null,
				"양얼의 나뭇가지", "", "", "", "", "", "",
				"물공 1162 가변", "마공 1003 가변", "독공 648 가변", "힘 107 가변", 
				"스킬 어둠의 둔기 마스터리 2", "스킬 48 1", "스킬 85 1", "ㄷ 증뎀 30 선택", "ㄷ 힘 125 가변", "ㄷ 추뎀 20 가변", null,
				"구원의 이기 - 둔기", "", "", "", "", "", true,
				"물공 1162 가변", "마공 1003 가변", "독공 648 가변", "힘 107 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"세계의 무게추", "", "", "", "", 90, false,
				"물공 1224 가변", "마공 1058 가변", "독공 686 가변", "힘 112 가변", 
				"암속강 40 가변", "명속강 40 가변", "암속부여", "명속부여", "스킬 75-85 1", fStat[6], "설명 자신의 가장 높은 속성의 추가데미지 18%", null,
				"창성의 구원자 - 둔기", "", "", "", "", 90, true,
				"물공 1224 가변", "마공 1058 가변", "독공 686 가변", "힘 112 가변", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				/////소검
				"본 레드 드래곤", "", "", Weapon_detailType.SWORD_SHORTSWORD, "", 80, false,
				"물공 948 가변", "마공 1147 가변", "독공 611 가변", "지능 68 가변", "마크 2",
				"화속강 30 가변", "화속부여", "ㄷ 물공뻥 10 선택", "ㄷ 마공뻥 10 선택", "ㄷ 독공뻥 10 선택", "설명 공격 시 5% 확률로 적의 HP 20% 감소", null,
				"천총운검", "", "", "", "", 85, "",
				"물공 1003 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", "마크 2", 
				"스킬 심안 3", "스킬 충실한 종 3", "스킬 형질전환 3", "스킬 20-50 2", "추뎀 27", fStat[7], "설명 브레인 스톰 시전 시 150px 범위내 파티원의 지능 120, 독공 75 증가 오라", null,
				"음검 : 막야", "", "", "", "", "", "",
				"물공 1003 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", "마크 2", 
				"수속강 40 가변", "수속부여", "ㄷ 암속부여 선택", "ㄷ 마크 10 선택", "ㄷ 수속강 30 선택", "ㄷ 암속강 70 선택", "ㄷ 크증뎀 15 선택", "ㄷ 수추뎀 12 선택", null,
				"구원의 이기 - 소검", "", "", "", "", "", true,
				"물공 1003 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", "마크 2", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"암흑의 별", "", "", "", "", 90, false,
				"물공 1058 가변", "마공 1280 가변", "독공 686 가변", "지능 75 가변", "마크 17", 
				"물크 15", "모공증 42", fStat[8], "설명 자신의 가장 높은 속성강화 수치의 속성 강화 25 증가", null,
				"창성의 구원자 - 소검", "", "", "", "", 90, true,
				"물공 1058 가변", "마공 1280 가변", "독공 686 가변", "지능 75 가변", "마크 2", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				/////도
				"블리츠 쟝닝", "", "", Weapon_detailType.SWORD_KATANA, "", 80, false,
				"물공 948 가변", "마공 1048 가변", "독공 611 가변", "힘 68 가변", "지능 101 가변", "물크 2",
				"명속강 24 가변", "명속부여", "ㄷ 추뎀 20 선택", "ㄷ 명속강 20 가변", null,
				"염화도 - 카라벨라", "", "", Weapon_detailType.SWORD_KATANA, "", 80, false,
				"물공 948 가변", "마공 1048 가변", "독공 611 가변", "힘 68 가변", "지능 101 가변", "물크 2",
				"화속강 24 가변", "화속부여", "ㄷ 화추뎀 35 선택", "ㄷ 화추뎀 10 선택", "ㄷ 화추뎀 7 선택", "ㄷ 화추뎀 5 선택", "ㄷ 화속강 5 선택", "ㄷ 화속강 5 선택", fStat[9], 
				"설명 화추뎀 7%(35% 옵션의 기댓값) 선택시 35% 옵션은 적용되지 않습니다", "설명 화추뎀 5%(10% 옵션의 기댓값) 선택시 10% 옵션은 적용되지 않습니다", null,
				"트리니티 이터니아", "", "", "", "", 85, "",
				"물공 1053 가변", "마공 1164 가변", "독공 680 가변", "힘 72 가변", "지능 107 가변", "물크 2", 
				"ㄷ 추뎀 33 선택", "ㄷ 추뎀 333 선택", "ㄷ 추뎀 21 선택", "ㄷ 물공뻥 9 가변", "ㄷ 마공뻥 9 가변", "ㄷ 독공뻥 9 가변", fStat[10], 
				"설명 스킬 사용 시 33% 확률로 33초 이내의 스킬 중 쿨타임이 가장 높은 스킬 쿨타임 초기화", "설명 추뎀 기댓값 수치(21%)을 사용설정할 경우 나머지 추뎀옵션 선택은 무시됩니다", null,
				"명도 마사무네", "", "", "", "", "", "",
				"물공 1003 가변", "마공 1109 가변", "독공 648 가변", "힘 72 가변", "지능 107 가변", 
				"명속부여", "물크 20", "마크 20", "스킬 오버드라이브 2", "스킬 어둠의 도 마스터리 2", "스킬 신검합일 2", "크증뎀 32", null,
				"마법검 : 칠지도", "", "", "", "", "", "",
				"물공 1003 가변", "마공 1219 가변", "독공 789 가변", "힘 72 가변", "지능 272 가변", "물크 2", 
				"마크 15", "ㄷ 지능 700 선택", "설명 공격 시 흑염검, 부동명왕진, 디스트로이어, 웨이브 스핀의 남은 쿨타임 20% 감소", null,
				"구원의 이기 - 도", "", "", "", "", "", true,
				"물공 1003 가변", "마공 1109 가변", "독공 648 가변", "힘 72 가변", "지능 107 가변", "물크 2", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"요도 : 무라마사", "", "", "", "", 90, false,
				"물공 1058 가변", "마공 1169 가변", "독공 686 가변", "힘 75 가변", "지능 112 가변", 
				"암속부여", "물크 7", "마크 7", "적방무 20", "추증뎀 35", null,
				"창성의 구원자 - 도", "", "", "", "", 90, true,
				"물공 1058 가변", "마공 1169 가변", "독공 686 가변", "힘 75 가변", "지능 112 가변", "물크 2", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				/////광검
				"고통의 여왕의 비명", "", "", Weapon_detailType.SWORD_LIGHTSWORD, "", 80, false,
				"물공 928 가변", "마공 898 가변", "독공 611 가변", "힘 68 가변",
				"암속강 30 가변", "암속부여", "스킬 리 귀검술 2", "스킬 어둠의 광검 마스터리 3", "스킬 양의공 5", "ㄷ 물공뻥 15", "ㄷ 마공뻥 15", "ㄷ 독공뻥 15", null,
				"뇌검 : 고룬", "", "", "", "", 85, "",
				"물공 982 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", 
				"명속강 45 가변", "명속부여", "물크 10", "마크 10", "명추뎀 8", null,
				"발뭉", "", "", "", "", "", "",
				"물공 982 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변",  
				"스킬 오버드라이브 2", "스킬 어둠의 광검 마스터리 2", "스킬 오기조원 2", "ㄷ 증뎀 60 가변", null,
				"구원의 이기 - 광검", "", "", "", "", "", true,
				"물공 982 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변",  
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"쉘 오브 브리니클", "", "", "", "", 90, false,
				"물공 1035 가변", "마공 1002 가변", "독공 686 가변", "힘 75 가변", 
				"수속강 35 가변", "수속부여", "스킬 극 귀검술, 참철식 2", "스킬 삼매진화 2", "ㄷ 수속깍 22 선택", "수추뎀 15", null,
				"창성의 구원자 - 광검", "", "", "", "", 90, true,
				"물공 1035 가변", "마공 1002 가변", "독공 686 가변", "힘 75 가변", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				///////////레전더리
				/////대검
				"진혼의 그레이트 소드", Item_rarity.LEGENDARY, "", Weapon_detailType.SWORD_LONGSWORD, "", 85, false,
				"물공 1202 가변", "마공 901 가변", "독공 607 가변", "힘 101 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 그레이트 소드", "", "", "", "", "", "",
				"물공 1202 가변", "마공 901 가변", "독공 607 가변", "힘 129 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"거대 누골의 형상", "", "", "", "", "", "",
				"물공 1202 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변",
				"ㄷ 추뎀 12 선택", fStat[11], "설명 강화/증폭 수치 1당 추가데미지 1% 증가(최대 15강)", "설명 맹룡단공참 스킬 최대 베기수 1 감소, 공격력 50% 증가(미구현)", null,
				"파괴의 용암석 대검", "", "", "", "", "", "",
				"물공 1202 가변", "마공 901 가변", "독공 607 가변", "힘 310 가변",
				"화속부여", "설명 어퍼 슬래쉬, 어퍼실드, 승천검, 어퍼 시전 시 마그토늄 파이브의 일격 사용", null,
				"브로딘 왕의 보검", "", "", "", "", "", "",
				"물공 1268 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변",
				"ㄷ 증뎀 15 선택", null,
				"악령 봉인 집행검", "", "", "", "", "", "",
				"물공 1202 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변",
				"암속강 22 가변", "암속부여", "ㄷ 암속추 15 선택", "설명 암속성 추가데미지 발동조건 : 악마형, 언데드 몬스터", null,
				"테라 : 리컨스트럭션 그레이트 소드", "", "", "", "", 90, "",
				"물공 1269 가변", "마공 953 가변", "독공 642 가변", "힘 72 가변",
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"매화육궁", "", "", "", "", "", "",
				"물공 1269 가변", "마공 953 가변", "독공 642 가변", "힘 72 가변",
				"물크 15", "마크 15", "스킬 45 3", "추뎀 18", null,
				/////둔기
				"진혼의 크래셔", Item_rarity.LEGENDARY, "", Weapon_detailType.SWORD_CLUB, "", 85, false,
				"물공 1102 가변", "마공 952 가변", "독공 607 가변", "힘 134 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 크래셔", "", "", "", "", "", "",
				"물공 1102 가변", "마공 952 가변", "독공 607 가변", "힘 162 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"타르바자 퀘이커", "", "", "", "", "", "",
				"물공 1102 가변", "마공 952 가변", "독공 607 가변", "힘 101 가변",
				"화속강 18 가변", "화속부여", "ㄷ 화추뎀 20 선택", null,
				"누골의 거대한 가시", "", "", "", "", "", "",
				"물공 1102 가변", "마공 952 가변", "독공 607 가변", "힘 101 가변",
				"ㄷ 추뎀 15 선택", null,
				"공작 유리스의 아귀", "", "", "", "", "", "",
				"물공 1102 가변", "마공 952 가변", "독공 607 가변", "힘 101 가변",
				"설명 피깍쳐에오", null,
				"해머왕 브레이커", "", "", "", "", "", "",
				"물공 1102 가변", "마공 952 가변", "독공 607 가변", "힘 101 가변",
				"ㄷ 추뎀 15 선택", null,
				"테라 : 리컨스트럭션 크래셔", "", "", "", "", 90, "",
				"물공 1164 가변", "마공 1005 가변", "독공 642 가변", "힘 107 가변",
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"슬링숏 스매쉬", "", "", "", "", "", "",
				"물공 1164 가변", "마공 1005 가변", "독공 642 가변", "힘 107 가변",
				"스킬 둔기 마스터리 3", "스킬 어둠의 둔기 마스터리 3", "스킬 파쇄의 둔기 마스터리 3", "모공증 12", "추뎀 10", "ㄷ 추뎀 10 선택", "설명 추뎀 조건 : 인간형 타입", null,
				/////소검
				"진혼의 소드", Item_rarity.LEGENDARY, "", Weapon_detailType.SWORD_SHORTSWORD, "", 85, false,
				"물공 952 가변", "마공 1152 가변", "독공 607 가변", "지능 101 가변",
				"물크 4", "마크 6", "추뎀 11", null,
				"리버레이션 소드", "", "", "", "", "", "",
				"물공 952 가변", "마공 1152 가변", "독공 607 가변", "지능 129 가변",
				"물크 7", "마크 9", "추뎀 16", null,
				"킬조의 영혼검", "", "", "", "", "", "",
				"물공 952 가변", "마공 1152 가변", "독공 607 가변", "지능 68 가변", "마크 2",
				"수속강 22 가변", "수속부여", "ㄷ 추뎀 25 선택", "ㄷ 추뎀 13 선택", "스킬 소검 마스터리 2", "스킬 귀참 2", "스킬 툼스톤 2", "스킬 카잔 2", "스킬 냉기의 사야 2",
				"스킬 흑염의 칼라 2", "스킬 파동검 빙인 3", "스킬 어둠의 소검 마스터리 2", "스킬 다크 슬래쉬 2", "스킬 다크 폴 2", "스킬 속성의 소검 마스터리 2", "스킬 이보브 3",
				"스킬 리버레이트 - 벅샷 3", "스킬 브레인 스톰 3", "스킬 파이널 어택 3", multiCheck, "설명 추뎀 기댓값(13%) 선택시 기댓값으로 적용", null,
				"마력이 담긴 날카로운 이빨", "", "", "", "", "", "",
				"물공 952 가변", "마공 1152 가변", "독공 607 가변", "지능 68 가변", "마크 2",
				"추뎀 18", "ㄷ 화속강 30 선택", "ㄷ 수속강 30 선택", "ㄷ 명속강 30 선택", "ㄷ 암속강 30 선택", "설명 속성강화 옵션 체크 하나당 추뎀 5% 감소", fStat[12], null,
				"미스트 단원의 소검", "", "", "", "", "", "",
				"물공 952 가변", "마공 1152 가변", "독공 607 가변", "지능 68 가변", "마크 2",
				"ㄷ 크증뎀 20 선택", null,
				"킬조의 얼음유령 소검", "", "", "", "", "", "",
				"물공 952 가변", "마공 1152 가변", "독공 607 가변", "지능 68 가변", "마크 2",
				"수속강 18 가변", "ㄷ 추뎀 10 선택", "설명 추가데미지 옵션의 조건 : 빙결상태의 적", null,
				"테라 : 리컨스트럭션 소드", "", "", "", "", 90, "",
				"물공 1005 가변", "마공 1217 가변", "독공 642 가변", "지능 72 가변",
				"물크 12", "마크 14", "스킬 1-85 1", null,
				"퓨리피온 소드", "", "", "", "", "", "",
				"물공 1148 가변", "마공 1360 가변", "독공 806 가변", "지능 72 가변", "마크 2",
				"스킬 1-45 1", "모공증 5", "설명 공격 시 3% 확률로 쿨타임 100초 이하의 모든 스킬 쿨타임 20% 감소(쿨타임 25초)", null,
				/////도
				"진혼의 블레이드", Item_rarity.LEGENDARY, "", Weapon_detailType.SWORD_KATANA, "", 85, false,
				"물공 952 가변", "마공 1052 가변", "독공 607 가변", "힘 101 가변", "지능 134 가변",
				"물크 6", "마크 4", "추뎀 11", null,
				"리버레이션 블레이드", "", "", "", "", "", "",
				"물공 952 가변", "마공 1052 가변", "독공 607 가변", "힘 129 가변", "지능 162 가변",
				"물크 9", "마크 7", "추뎀 16", null,
				"찬란한 불꽃의 샤벨", "", "", "", "", "", "",
				"물공 1021 가변", "마공 1128 가변", "독공 657 가변", "힘 68 가변", "지능 101 가변", "물크 2",
				"화속부여", "ㄷ 화속강 20 가변", "ㄷ 크증뎀 20 선택", null,
				"타우 캡틴의 날카로운 늑골", "", "", "", "", "", "",
				"물공 952 가변", "마공 1052 가변", "독공 607 가변", "힘 68 가변", "지능 101 가변",
				"물크 10", "마크 8", "ㄷ 크증뎀 20 선택", null,
				"카르텔의 톱날 블레이드", "", "", "", "", "", "",
				"물공 952 가변", "마공 1052 가변", "독공 607 가변", "힘 68 가변", "지능 101 가변", "물크 2",
				"ㄷ 힘 100 가변", "ㄷ 지능 100 가변", "ㄷ 물크 10 가변", "ㄷ 마크 10 가변", "추뎀 14", null,
				"무명전사의 도", "", "", "", "", "", "",
				"물공 952 가변", "마공 1052 가변", "독공 607 가변", "힘 112 가변", "지능 167 가변",
				"물크 15", "마크 15", "스킬 1-20 3", "ㄷ 힘 100 가변", "ㄷ 지능 100 가변", "ㄷ 추뎀 5 선택", "설명 힘/지능 옵션 조건 : 적 처치시 두당 +2", "설명 추가데미지 조건 : 네임드 몬스터", null,
				"테라 : 리컨스트럭션 블레이드", "", "", "", "", "", "",
				"물공 1005 가변", "마공 1111 가변", "독공 642 가변", "힘 72 가변", "지능 107 가변",
				"물크 14", "마크 12", "스킬 1-85 1", null,
				"자켈리네 일검 : 귀면도", "", "", "", "", 90, "",
				"물공 1005 가변", "마공 1111 가변", "독공 642 가변", "힘 72 가변", "지능 107 가변", "물크 2",
				"설명 힘,지능 중 낮은 스탯을 높은 스탯으로 변환(최대 300)", "설명 40 ~ 45Lv 스킬 공격력 30% 증가", "추크증 7", fStat[13], null,
				/////광검
				"진혼의 빔소드", Item_rarity.LEGENDARY, "", Weapon_detailType.SWORD_LIGHTSWORD, "", 85, false,
				"물공 932 가변", "마공 901 가변", "독공 607 가변", "힘 101 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 빔소드", "", "", "", "", "", "",
				"물공 932 가변", "마공 901 가변", "독공 607 가변", "힘 129 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"사나운 원념의 폭발", "", "", "", "", "", "",
				"물공 932 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변",
				"암속강 22 가변", "암속부여", "물크 10", null,
				"어둠에 잠식된 빛", "", "", "", "", "", "",
				"물공 977 가변", "마공 945 가변", "독공 637 가변", "힘 68 가변",
				"ㄷ 암속부여 선택", "ㄷ 힘뻥 7 선택", "ㄷ 암속강 12 선택", "ㄷ 물공뻥 10 선택", "ㄷ 암속강 25 선택", null, 
				"빛으로 향하는 명예", "", "", "", "", "", "",
				"물공 932 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변",
				"명속강 16 가변", "명속부여", "ㄷ 추뎀 10 선택", null,
				"더러운 파문에 흔들리는 암검", "", "", "", "", "", "",
				"물공 932 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변",
				"암속강 14 가변", "암속부여", "스킬 물리 백 어택 5", "ㄷ 추뎀 15 선택", null,
				"테라 : 리컨스트럭션 빔소드", "", "", "", "", "", "",
				"물공 985 가변", "마공 953 가변", "독공 642 가변", "힘 72 가변",
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"악검 데스렌더", "", "", "", "", 90, "",
				"물공 985 가변", "마공 953 가변", "독공 642 가변", "힘 72 가변",
				"스킬 극 귀검술, 참철식 1", "스킬 어둠의 광검 마스터리 2", "스킬 삼매진화 1", "크증뎀 20", "추크증 10", "설명 아니 이럴거면 그냥 크증뎀 30을 달지", null,
				
				////////////////마법사
				/////////에픽
				/////창
				"황실 근위대의 총검", Item_rarity.EPIC, "", Weapon_detailType.MAGE_SPEAR, "", 80, false, CalculatorVersion.VER_1_0_d,
				"물공 1198 가변", "마공 848 가변", "독공 611 가변", "힘 101 가변",
				"물크 10", "스킬 쇄패 3", "스킬 트윙클 스매쉬 2", "스킬 황룡천공 4", "스킬 쉬카리 3", "스킬 럼블독 2", "스킬 커럽션 4", "크증뎀 20", null,
				"미완성 인피니티 피어스", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_d, 
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 107 가변", "물크 2", 
				"스킬 1-80 2", "ㄷ 추뎀 15 선택", "ㄷ 추뎀 10 선택", null,
				"전장의 여신의 창", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 107 가변", "물크 2", 
				"스킬 배틀 그루브 2", "스킬 창 마스터리 1", "스킬 어밴던 2", "스킬 블러드 1", "증뎀 35", "스킬 일기당천 % 30", "스킬 커럽션 % 15", "스킬 팽 % 15", null,
				"구원의 이기 - 창", "", "", "", "", "", true, CalculatorVersion.VER_1_0_d,
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 107 가변", "물크 2", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"쥬빌런스 혼", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_f,
				"물공 1337 가변", "마공 946 가변", "독공 686 가변", "힘 112 가변", "물크 2", 
				"추뎀 25", "추크증 30", "스킬 퀘이사 익스플로젼 % 10", "스킬 일기당천 쇄패 % 10", "스킬 체페슈 % 10", "스킬 블러드 스트림 % 10", null,
				"창성의 구원자 - 창", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_d,
				"물공 1337 가변", "마공 946 가변", "독공 686 가변", "힘 112 가변", "물크 2",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////봉
				"체스 나이트", "", "", Weapon_detailType.MAGE_POLE, "", 80, false, CalculatorVersion.VER_1_0_d,
				"물공 1078 가변", "마공 898 가변", "독공 611 가변", "힘 68 가변", "지능 68 가변",
				"암속강 22 가변", "암속부여", "스킬 봉 마스터리 3", "스킬 휘몰아치는 질풍의 봉 마스터리 3", "스킬 강습유성타 % 40", "스킬 스톰 스트라이크 % 40", "ㄷ 추뎀 10 선택", null,
				"마스터 오브 체이서", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_f, 
				"물공 1140 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", "지능 72 가변", 
				"스킬 오토 체이서 2", "스킬 체이서 에볼루션 1", "스킬 윈드니스 2", "스킬 휘몰아치는 질풍의 봉 마스터리 1", 
				"스킬 일반 체이서 % -30", "스킬 사도화 체이서 % -30", "스킬 테아나 체이서 % -30", "스킬 체이서 에볼루션 % 60",
				"설명 체이서 생성 시 왕체이서 생성, 사출 수 1 증가, 오토 체이서 체이서 추가 생성확률 30% 증가",
				"설명 윈드니스 스킬 시전 시 물공 30% 증가, 모속강 27 증가(미구현)", null,
				"러브러브! 크리스탈 트윙클 매직샷", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1140 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", "지능 72 가변", 
				"스킬 테아나 변신~!! 1", "스킬 배틀 그루브 2", "스킬 봉 마스터리 2", "스킬 볼텍스 허리케인 1", "스킬 풍신의 힘 2", "스킬 휘몰아치는 질풍의 봉 마스터리 2",
				"증뎀 20", "추뎀 14", "추뎀 1 선택", "추뎀 1 선택", "설명 아바타 인벤토리에 아바타 갯수 7개 이하 : 추뎀 14%, 21개 이상 : 추뎀 16%", null,
				"구원의 이기 - 봉", "", "", "", "", "", true, CalculatorVersion.VER_1_0_d,
				"물공 1140 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", "지능 72 가변",  
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"케세라세라 : 해피 ~!", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_d,
				"물공 1202 가변", "마공 1002 가변", "독공 686 가변", "힘 75 가변", "지능 75 가변", 
				"추뎀 17", "ㄷ 모공증 10 선택", "ㄷ 모공증 10 선택", "ㄷ 스증뎀 10 선택", "ㄷ 스증뎀 10 선택", "설명 때때로 캐릭터가 정박아가 됩니다", null,
				"창성의 구원자 - 봉", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_d,
				"물공 1202 가변", "마공 1002 가변", "독공 686 가변", "힘 75 가변", "지능 75 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////로드
				"에어 로드", "", "", Weapon_detailType.MAGE_ROD, "", 80, false, CalculatorVersion.VER_1_0_d,
				"물공 898 가변", "마공 1098 가변", "독공 611 가변", "지능 101 가변",
				"수속강 22 가변", "명속강 22 가변", "스킬 환수 강화 오라 2", "스킬 피어스 오브 아이스 3", "추뎀 12", "스킬 정령소환 : 글레어린 % 40", "스킬 정령소환 : 아퀘리스 % 40", "설명 피어스 오브 아이스 파편 개수 1 증가(미구현)", null,
				"정령왕의 수호", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_d, 
				"물공 950 가변", "마공 1162 가변", "독공 648 가변", "지능 107 가변", 
				"모속강 22 가변", "스킬 정령소환 : 정령왕 에체베리아 3", "스킬 환수 정화 3", "스킬 정령소환 : 데드 멀커 2", "스킬 정령소환 : 글레어린 2", "스킬 정령소환 : 아퀘리스 2", "스킬 정령소환 : 플레임 헐크 2", "스킬 환수 강화 오라 2",
				"스킬 환수 폭주 2", "스킬 피어스 오브 아이스 2", "스킬 아이스 빅 해머 2", "스킬 블리자드 스톰 3", "스킬 공명 3", "스킬 툰드라의 가호 3", "스킬 아이스크래프트 1", "스증뎀 10",
				"설명 정령왕 에체베리아 최대 소환 1 증가", "설명 블리자드 스톰 냉기 폭풍 개수 2 증가, 폭발 공격력 30% 증가(미구현)", null,
				"양치기의 로드", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 950 가변", "마공 1162 가변", "독공 648 가변", "지능 107 가변", 
				"모속강 18 가변", "ㄷ 마크 25 선택", "ㄷ 마크 10 선택", "설명 늑태가 낙타낳다!", null,
				"구원의 이기 - 로드", "", "", "", "", "", true, CalculatorVersion.VER_1_0_d,
				"물공 950 가변", "마공 1162 가변", "독공 648 가변", "지능 107 가변",  
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"뉴링턴 로드", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_d,
				"물공 1002 가변", "마공 1373 가변", "독공 857 가변", "지능 112 가변", 
				"모속강 30 가변", fStat[6], "설명 자신의 가장 높은 속성의 추가데미지 18%", null, 
				"창성의 구원자 - 로드", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_d,
				"물공 1002 가변", "마공 1224 가변", "독공 686 가변", "지능 112 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////스탭
				"악어새의 신축 둥지", "", "", Weapon_detailType.MAGE_STAFF, "", 80, false, CalculatorVersion.VER_1_0_d,
				"물공 948 가변", "마공 1302 가변", "독공 663 가변", "지능 178 가변",
				"ㄷ 모속강 25 선택", "ㄷ 물크 12 선택", "ㄷ 마크 12 선택", null,
				"샤이닝 인텔리전스", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_d, 
				"물공 1003 가변", "마공 1267 가변", "독공 648 가변", "지능 72 가변", 
				"명속강 40 가변", "마크 20", "스킬 쇼타임 3", "스킬 고대의 도서관 5", "스킬 속성 발동 5", "스킬 마나 폭주 3", "ㄷ 명속강 20 선택", "추뎀 16 선택", null,
				"스탭 오브 위저드", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1003 가변", "마공 1267 가변", "독공 648 가변", "지능 282 가변", 
				"모속강 26 가변", "스킬 고대의 기억 5", "ㄷ 마공뻥 12", "ㄷ 독공뻥 12", "ㄷ 지능뻥 15", null,
				"구원의 이기 - 스태프", "", "", "", "", "", true, CalculatorVersion.VER_1_0_d,
				"물공 1003 가변", "마공 1267 가변", "독공 648 가변", "지능 72 가변",  
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"웨리 : 리미트 브레이커", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_g,
				"물공 1058 가변", "마공 1337 가변", "독공 686 가변", "지능 75 가변", 
				"ㄷ 마공뻥 64 가변", "ㄷ 독공뻥 64 가변", fStat[18], "설명 마력 증폭 최대 중첩 상태(64%)일 때 2차각성 스킬 Lv +2", null, 
				"창성의 구원자 - 스태프", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_d,
				"물공 1058 가변", "마공 1337 가변", "독공 686 가변", "지능 75 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////빗자루
				"핸드메이드 빗자루", "", "", Weapon_detailType.MAGE_BROOM, "", 80, false, CalculatorVersion.VER_1_0_d,
				"물공 998 가변", "마공 1098 가변", "독공 611 가변", "지능 68 가변",
				"모속강 18 가변", "스킬 변이 파리채 3", "스킬 대왕 파리채 2", "스킬 차원 : 차원광 3", "스킬 차원 : 패러다임 디토네이션 2", "스킬 화염 3", "스킬 냉기 2", "추뎀 15",
				"설명 파리채, 대왕 파리채, 성난 불길 가열로, 메가 드릴, 플로레 컬라이더, 반중력 기동장치 실패율 10% 추가 감소, 대성공률 10% 추가 증가", "설명 운석 낙하, 아이스 플레이트, 윈드 스톰 게이지 소모량 25% 감소",
				"설명 괴리 : 패러사이트 스웜 지속 공격 시간 2초 증가", "스킬 차원 : 포지트론 블래스트 % 20", null, 
				"인어의 파리채", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_d, 
				"물공 1056 가변", "마공 1162 가변", "독공 648 가변", "지능 72 가변", 
				"스킬 고대의 도서관 1", "스킬 스위트 캔디바 1", "스킬 도그마 디바이스 1", "스킬 경계망상 1", "스킬 재현 1", "스킬 변이 1", "스킬 1-80 2", "스킬 괴리 : 칙잇 % 15", "증뎀 22", null,
				"래쿤 배큠", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1056 가변", "마공 1162 가변", "독공 648 가변", "지능 72 가변", 
				"모속강 18 가변", "스킬 15-20 3", fStat[18], "설명 크리에이터 1-85레벨 스킬 +1", "스킬 퓨전 크레프트 % 30", "스킬 괴리 : 형용할 수 없는 공포 % 30", "추뎀 12", null,
				"스노우 프린세스", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1056 가변", "마공 1162 가변", "독공 648 가변", "지능 72 가변", 
				"수속강 40 가변", "수속부여", "스킬 잭프로스트와 친하게 지내기 5", "스킬 도그마 디바이스 3", "스킬 냉기 5", "ㄷ 수속깍 10 선택", "ㄷ 추뎀 20 선택", "ㄷ 마크 12 선택", null,
				"마나 브룸", "", "", "", "", "", "", CalculatorVersion.VER_1_0_d,
				"물공 1056 가변", "마공 1162 가변", "독공 648 가변", "지능 72 가변", 
				"ㄷ 지능 210 가변", "ㄷ 마공 280 가변", "ㄷ 독공 350 가변", "추뎀 20", null,
				"구원의 이기 - 빗자루", "", "", "", "", "", true, CalculatorVersion.VER_1_0_d,
				"물공 1056 가변", "마공 1162 가변", "독공 648 가변", "지능 72 가변",  
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"글레이프니르", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_d,
				"물공 1113 가변", "마공 1466 가변", "독공 983 가변", "지능 75 가변", 
				"스증뎀 15", "적방무 25", null, 
				"창성의 구원자 - 빗자루", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_d,
				"물공 1113 가변", "마공 1224 가변", "독공 686 가변", "지능 75 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				////////////레전
				/////창
				"진혼의 스피어", Item_rarity.LEGENDARY, "", Weapon_detailType.MAGE_SPEAR, "", 85, false, CalculatorVersion.VER_1_0_f,
				"물공 1202 가변", "마공 851 가변", "독공 607 가변", "힘 134 가변",
				"물크 6", "마크 4", "추뎀 11", null,
				"리버레이션 스피어", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1202 가변", "마공 851 가변", "독공 607 가변", "힘 162 가변",
				"물크 9", "마크 7", "추뎀 16", null,
				"돌격대장의 살육창", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1202 가변", "마공 851 가변", "독공 607 가변", "힘 101 가변",
				"물크 15", "크증뎀 22", "ㄷ 추뎀 10 선택", "설명 추뎀 조건 : 출혈상태의 적 공격 시", null,
				"라바 파나카", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1202 가변", "마공 851 가변", "독공 607 가변", "힘 200 가변",
				"물크 2", "스킬 쇄패 % 10", "스킬 트윙클 스매쉬 % 10", "스킬 강습유성타 % 10", "스킬 천지쇄패 % 10", "스킬 일기당천 쇄패 % 10",
				"스킬 용아 % 30", "스킬 원무곤 % 30", "스킬 대시공격 % 30", "스킬 오퍼링 % 10", "스킬 토먼트 % 10",
				"스킬 팽 % 10", "스킬 머로더 % 10", "설명 뇌연격, 진 뇌연역, 황룡천공, 황룡난무, 타이머 밤 찌르기 공격력 30% 증가(미구현)", null,
				"카르텔의 군용 스피어", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1202 가변", "마공 851 가변", "독공 607 가변", "힘 101 가변",
				"물크 2", "ㄷ 추뎀 15 선택", "스킬 천격 % 10", "스킬 용아 % 10", "스킬 윈드 스트라이크 % 10", "스킬 릴리악 % 10", null,
				"환영의 불길한 송곳니", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1202 가변", "마공 851 가변", "독공 607 가변", "힘 101 가변",
				"암속강 24 가변", "암속부여", "물크 2", "스킬 일반 체이서 5", "스킬 쉬카리 3", "스킬 오퍼링 2", "ㄷ 추뎀 10 선택", null,
				"테라 : 리컨스트럭션 스피어", "", "", "", "", 90, "", CalculatorVersion.VER_1_0_f,
				"물공 1269 가변", "마공 900 가변", "독공 642 가변", "힘 107 가변", 
				"물크 14", "마크 12", "스킬 1-85 1", null,
				"토푸스 텔룸", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1269 가변", "마공 900 가변", "독공 642 가변", "힘 107 가변",
				"물크 2", "화속강 24 가변", "화속부여", "추뎀 10", null,
				
				/////봉
				"진혼의 스틱", "", "", Weapon_detailType.MAGE_POLE, "", 85, false, CalculatorVersion.VER_1_0_f,
				"물공 1081 가변", "마공 901 가변", "독공 607 가변", "힘 101 가변", "지능 101 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 스틱", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1081 가변", "마공 901 가변", "독공 607 가변", "힘 129 가변", "지능 129 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"민병대장의 싯누런 뿔", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1081 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변", "지능 68 가변",
				"ㄷ 고정물방깍 10000 선택", "ㄷ 고정마방깍 10000 선택", "ㄷ 추뎀 10 선택", "설명 추뎀 조건 : 출혈 상태의 적 공격시", null,
				"카르텔의 쌍두독수리 봉", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1081 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변", "지능 129 가변",
				"스킬 일반 체이서 % 15", "스킬 퓨전 체이서 % 15", "설명 체이서 무/명/수/화/암, 퓨전 체이서 버프 증가율 20% 증가(미구현)",
				"스킬 윈드니스 % 15", "스킬 회오리 바람 % 20", "스킬 스톰 스트라이크 % 20", null,
				"루브룸 스톤 폴", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1081 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변", "지능 68 가변",
				"스킬 일반 체이서 % 15", "스킬 퓨전 체이서 % 30", "스킬 휘몰아 치는 바람 % 15", "스킬 폭풍의눈 % 15",
				"ㄷ 지능 80", "ㄷ 힘 150", "설명 싹쓸바람, 삭풍 타격횟수 2 증가(미구현)", null, //TODO, 트윙클 옵션?
				"메카타우의 기계 척추", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1081 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변", "지능 68 가변",
				"스킬 일반 체이서 % 18", "ㄷ %마방깍_템 6 선택", "ㄷ %마방깍_템 6 선택", "ㄷ %마방깍_템 6 선택", "ㄷ %마방깍_템 10 선택", "ㄷ %마방깍_템 10 선택",
				"스킬 윈드 블래스터 % 15", "스킬 몰아치는 바람 % 15", "스킬 싹쓸바람 % 15", "ㄷ %물방깍_템 6 선택", "ㄷ %물방깍_템 6 선택",
				"설명 안톤 레이드, 각성 안톤 던전에서는 방어력 감소 효과가 작용하지 않습니다", null,
				"테라 : 리컨스트럭션 스틱", "", "", "", "", 90, "", CalculatorVersion.VER_1_0_f,
				"물공 1143 가변", "마공 953 가변", "독공 642 가변", "힘 72 가변", "지능 72 가변", 
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"레드 폴 크라운", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1143 가변", "마공 953 가변", "독공 642 가변", "힘 237 가변", "지능 237 가변",
				"스킬 테아나 변신~!! 2", "스킬 볼텍스 허리케인 2", "ㄷ 물공뻥 12 선택", "ㄷ 마공뻥 12 선택", "ㄷ 독공뻥 12 선택", "ㄷ 추뎀 10 선택", null,
				
				/////로드
				"진혼의 로드", "", "", Weapon_detailType.MAGE_ROD, "", 85, false, CalculatorVersion.VER_1_0_f,
				"물공 901 가변", "마공 1102 가변", "독공 607 가변", "지능 134 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 로드", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 901 가변", "마공 1102 가변", "독공 607 가변", "지능 162 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"라비네터의 검은심장 로드", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 901 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"수속부여", "암속부여", "스킬 정령소환 : 스토커 3", "스킬 프로즌웨폰 마스터리 3", "ㄷ 암추뎀 20 선택", "ㄷ 암추뎀 15 선택", "ㄷ 암추뎀 10 선택",
				"ㄷ 수추뎀 20 선택", "ㄷ 수추뎀 15 선택", "ㄷ 수추뎀 10 선택", "ㄷ 추뎀 10 선택",
				"설명 선택한 추뎀 옵션 중 가장 높은 수치 하나만 적용됩니다", "설명 -자신의 암속성 강화가 80 이상일 때", "설명 MP가 90% 이상일 경우 20% 암속성 추가데미지", "설명 MP가 70% 이상일 경우 15% 암속성 추가데미지",
				"설명 MP가 50% 이상일 경우 10% 암속성 추가데미지", "설명 MP가 50% 이하일 경우 10% 추가데미지", "설명 -자신의 수속성 강화가 80 이상, 암속성 강화가 80 이하일 때", "설명 MP가 90% 이상일 경우 20% 수속성 추가데미지",
				"설명 MP가 70% 이상일 경우 15% 수속성 추가데미지", "설명 MP가 50% 이상일 경우 10% 수속성 추가데미지", "설명 MP가 50% 이하일 경우 10% 추가데미지", fStat[14], null,
				"원혼의 울부짖는 형상", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 901 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"스킬 정령소환 : 스토커 2", "스킬 정령소환 : 데드 멀커 2", "스킬 중급 정령 : 셰이드 2", "스킬 설화연창 2", "스킬 브로큰 애로우 2", "스킬 아이스 오브 2",
				"ㄷ 암속강 20", "스킬 스토커 % 20", "ㄷ 수속깍 15 선택", "스킬 설화연창 % 20", "스킬 회전투창 % 20", null, //TODO 스토커, 데드멀커 옵션
				"어드밴스드 스피릿 로드", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 901 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"ㄷ 지능 180 선택", "ㄷ 마크 10 선택", "ㄷ 마공뻥 12 선택", null,
				"나잘로의 주술봉", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 901 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"스킬 환수 강화 오라 2", "스킬 환수 폭주 2", "스킬 프로즌웨폰 마스터리 2", "스킬 블리자드 스톰 3", "스킬 마계화 아우쿠소 % 30", "스킬 브로큰 애로우 % 15",
				"스킬 아이스 빅 해머 % 25", null, //TODO 아우쿠소 소환수 1 증가
				"테라 : 리컨스트럭션 로드", "", "", "", "", 90, "", CalculatorVersion.VER_1_0_f,
				"물공 953 가변", "마공 1164 가변", "독공 642 가변", "지능 107 가변", 
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"포이즌 블룸 로드", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 953 가변", "마공 1164 가변", "독공 642 가변", "지능 107 가변",
				"추뎀 18", "ㄷ 증뎀 12 선택", null,
				
				/////스태프
				"진혼의 스태프", "", "", Weapon_detailType.MAGE_STAFF, "", 85, false, CalculatorVersion.VER_1_0_f,
				"물공 952 가변", "마공 1202 가변", "독공 607 가변", "지능 101 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 스태프", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 952 가변", "마공 1202 가변", "독공 607 가변", "지능 129 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"나잘로의 사술 지팡이", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 952 가변", "마공 1202 가변", "독공 607 가변", "지능 68 가변",
				"화속강 18 가변", "암속강 22 가변", "ㄷ 마크 10 선택", "ㄷ 힘 150 선택", "ㄷ 지능 150 선택", null,
				"찢겨지는 마나의 비명소리", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 952 가변", "마공 1202 가변", "독공 607 가변", "지능 68 가변",
				"모속강 18 가변", "스킬 엘레멘탈 번 3", "스킬 마나 폭주 3", 
				"설명 엘레멘탈 번 지능 상승량, 스킬 공격력 20% 증가(수치는 스킬에 직접 입력)", "설명 마나폭주 공격력 증가율 20% 증가(수치는 스킬에 직접 입력)", null,
				"주술사의 비틀린 팔", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 952 가변", "마공 1202 가변", "독공 607 가변", "지능 123 가변",
				"설명 환수 강화 오라 물리/마법 공격력 증가비율 30% 증가(미구현)", null,
				"르네의 첫 번째 지팡이", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 952 가변", "마공 1307 가변", "독공 607 가변", "지능 173 가변",
				"암속부여", "ㄷ 증뎀 20 선택", "설명 증뎀조건 : 암흑 상태의 적 공격시", null,
				"테라 : 리컨스트럭션 스태프", "", "", "", "", 90, "", CalculatorVersion.VER_1_0_f,
				"물공 1005 가변", "마공 1269 가변", "독공 642 가변", "지능 72 가변", 
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"마나엠퍼 스태프", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1005 가변", "마공 1269 가변", "독공 642 가변", "지능 72 가변",
				"ㄷ 지능뻥 18", "ㄷ 모속강 30", null,
				
				/////빗자루
				"진혼의 브러시", "", "", Weapon_detailType.MAGE_BROOM, "", 85, false, CalculatorVersion.VER_1_0_f,
				"물공 1002 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 브러시", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1002 가변", "마공 1102 가변", "독공 607 가변", "지능 129 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"울부짖는 타우의 갈기털", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1002 가변", "마공 1102 가변", "독공 607 가변", "지능 68 가변",
				"ㄷ 추뎀 15 선택", "설명 근접 무기 공격 시 데미지 20% 증가(미구현)", "설명 화염, 냉기, 방해, 수호, 바람 게이지 25% 증가", null,
				"기괴한 괴충 꼬리", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1002 가변", "마공 1102 가변", "독공 607 가변", "지능 68 가변",
				"화속강 18 가변", "스킬 플루토와 친하게 지내기 2", "스킬 괴리 : 디멘션 할로우 2", "스킬 화염 2", "설명 웜홀쳐가 됩니다", null, 
				"하트넥의 꼬리털 빗자루", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1002 가변", "마공 1102 가변", "독공 607 가변", "지능 129 가변",
				"ㄷ 화속깍 20 선택", "ㄷ 수속깍 20 선택", "ㄷ 명속깍 20 선택", "ㄷ 암속깍 20 선택", "ㄷ 추뎀 10 선택", "설명 추뎀조건 : 출혈상태의 적", null,
				"검은새의 불길한 깃털 빗자루", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1002 가변", "마공 1102 가변", "독공 607 가변", "지능 68 가변",
				"암속부여", "암속강 18 가변", "스킬 플루토와 친하게 지내기 2", "스킬 괴리 : 디멘션 할로우 2", "스킬 바람 2", "ㄷ 암속추 10 선택", null,
				"테라 : 리컨스트럭션 브러시", "", "", "", "", 90, "", CalculatorVersion.VER_1_0_f,
				"물공 1058 가변", "마공 11164 가변", "독공 642 가변", "지능 72 가변", 
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"굉음의 파쇄꾼", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1058 가변", "마공 11164 가변", "독공 642 가변", "지능 72 가변",
				"스킬 메가 드릴 4", "스킬 차원 : 포지트론 블래스트 4", "스킬 윈드 프레스 4", "스킬 포지트론 블래스트 % 30", "스킬 메가 드릴 % 30",
				"스킬 윈드 프레스 % 40", "설명 메가 드릴, 포지트론 블래스트 쿨타임 20% 감소", "설명 바람 게이지 회복속도 25% 감소", "추뎀 10", null,
				
				///////////////프리스트
				/////////에픽
				/////배액
				"학살의 단두대", Item_rarity.EPIC, "", Weapon_detailType.PRIEST_BATTLEAXE, "", 80, false, CalculatorVersion.VER_1_0_f,
				"물공 1289 가변", "마공 913 가변", "독공 656 가변", "힘 58 가변",
				"스킬 난격 % 30", "스킬 난격 % 33.09", "스킬 참격 % 30", "스킬 참격 % 27", "ㄷ 물크 15 가변", null,
				"디스트럭션", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_d,
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 72 가변", 
				"스킬 거병 마스터리 3", "ㄷ 힘 125 가변", "ㄷ %물방깍_템 30 가변", "설명 챔피언 몬스터에게는 1/3, 보스 및 헬몹에게는 1/6의 효과 적용", "설명 안톤 레이드, 각성안톤 던전에서는 적용되지 않습니다",
				"ㄷ 추뎀 15 선택", "스킬 황룡의 진격 % 15 선택", "설명 추뎀 조건 : 보스, 네임드, 챔피언 몹 공격시", "설명 황룡격 스증은 물딜에만 적용됩니다 마딜은 꺼주세요", null,
				"하이퍼리온", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 237 가변", 
				"화속부여", "물크 20", "스킬 거병 마스터리 2", "스킬 열정의 챠크라 3", "추뎀 10", "크증뎀 15", null,
				"구원의 이기 - 배틀액스", "", "", "", "", "", true, CalculatorVersion.VER_1_0_f,
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 72 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"행성파괴자", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_f,
				"물공 1337 가변", "마공 946 가변", "독공 686 가변", "힘 75 가변", 
				"ㄷ 스킬 1-85 1 선택", "ㄷ 스킬 1-85 1 선택", "ㄷ 스킬 1-85 1 선택", "ㄷ 스킬 1-85 1 선택", fStat[15], "설명 스킬레벨 증가는 퇴마사에게만 적용", "추증뎀 10", null,
				"창성의 구원자 - 배틀액스", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_f,
				"물공 1337 가변", "마공 946 가변", "독공 686 가변", "힘 75 가변", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				"명강성주", "", "", Weapon_detailType.PRIEST_ROSARY, "", 80, false, CalculatorVersion.VER_1_0_f,
				"물공 898 가변", "마공 1147 가변", "독공 611 가변", "지능 101 가변",
				"명속강 30 가변", "명속부여", "스킬 낙뢰부 3", "ㄷ 명속강 30 선택", "스킬 낙뢰부 % 18.18", "설명 낙뢰부 한번에 떨어뜨리는 번개 최대수 2 증가(미구현)", null,
				"우요의 황금 염주", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_f,
				"물공 950 가변", "마공 1214 가변", "독공 648 가변", "지능 107 가변", 
				"모속강 30 가변", "마크 2", "추뎀 20", "ㄷ 물공뻥 12 선택", "ㄷ 마공뻥 12 선택", "ㄷ 독공뻥 12 선택", null,
				"백팔 뇌주", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 950 가변", "마공 1214 가변", "독공 648 가변", "지능 107 가변",  
				"명속강 40 가변", "마크 2", "스킬 성불 5", "스킬 낙뢰부 5", "스킬 공의 식신 - 백호 5", "스킬 광명의 챠크라 3", "스킬 신의 챠크라 1", "스킬 신선의 경지 1", 
				"스킬 성불 % 33.33", "스킬 낙뢰부 % 10", "설명 낙뢰부 최대 번개수 2 증가(미구현)", null,
				"구원의 이기 - 염주", "", "", "", "", "", true, CalculatorVersion.VER_1_0_f,
				"물공 950 가변", "마공 1214 가변", "독공 648 가변", "지능 107 가변", "마크 2",
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"음양사천", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_f,
				"물공 1002 가변", "마공 1280 가변", "독공 686 가변", "지능 112 가변", 
				"마크 10", "ㄷ 스킬 48-80 2", "추뎀 40", fStat[15], "설명 스킬레벨 증가는 퇴마사에게만 적용", null,
				"창성의 구원자 - 염주", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_f,
				"물공 1002 가변", "마공 1280 가변", "독공 686 가변", "지능 112 가변", "마크 2",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				"청룡대", "", "", Weapon_detailType.PRIEST_TOTEM, "", 80, false, CalculatorVersion.VER_1_0_f,
				"물공 1048 가변", "마공 898 가변", "독공 611 가변", "힘 101 가변",
				"명속강 30 가변", "스킬 윌 드라이버 2", "스킬 허리케인 롤 3", "ㄷ 명속강 20 선택", "설명 허리케인 롤 공격 시 명속성 20% 추가 데미지(미구현)", null,
				"쿵 : 쿵타", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_f,
				"물공 1109 가변", "마공 950 가변", "독공 648 가변", "힘 228 가변", 
				"스킬 더킹 바디블로 % 25", "스킬 헤븐리 컴비네이션 % 30", "추뎀 30", null,
				"풍운뇌우", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1109 가변", "마공 950 가변", "독공 648 가변", "힘 228 가변", 
				"명속강 40 가변", "명속부여", "스킬 핵펀치 % 30", "명추뎀 8", null,
				"구원의 이기 - 토템", "", "", "", "", "", true, CalculatorVersion.VER_1_0_f,
				"물공 1109 가변", "마공 950 가변", "독공 648 가변", "힘 228 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"탐라선인석", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_f,
				"물공 1169 가변", "마공 1002 가변", "독공 686 가변", "힘 112 가변", 
				"ㄷ 스킬 1-85 1", "추크증 25", fStat[15], "설명 스킬레벨 증가는 인파이터에게만 적용", "설명 드라이아웃 공격력 증가율 15% 추가 증가(미구현)", null,
				"창성의 구원자 - 토템", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_f,
				"물공 1169 가변", "마공 1002 가변", "독공 686 가변", "힘 112 가변", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				"처형자의 낫", "", "", Weapon_detailType.PRIEST_SCYTHE, "", 80, false, CalculatorVersion.VER_1_0_f,
				"물공 948 가변", "마공 998 가변", "독공 611 가변", "힘 68 가변", "지능 68 가변",
				"암속부여", "물크 20", "마크 20", "크증뎀 22", null,
				"얼어붙은 공진의 낫", "", "", Weapon_detailType.PRIEST_SCYTHE, "", 80, false, CalculatorVersion.VER_1_0_f,
				"물공 948 가변", "마공 998 가변", "독공 611 가변", "힘 68 가변", "지능 68 가변",
				"수속강 17 가변", "암속강 17 가변", "수속부여", "암속부여", "물크 2", "마크 2", "스킬 어둠의 권능 3", "스킬 환청 3", "추뎀 12", "설명 메타몰시스 잔영 공격력 비율 15% 증가(미구현)", null,
				"선고 : 사신의 낫", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_f,
				"물공 1003 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 72 가변", 
				"암속강 40 가변", "암속부여", "물크 2", "마크 10", "ㄷ 추뎀 30 선택", "설명 공격 시 2% 확률로 적의 HP 20% 감소", null,
				"앙그라 마이뉴", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1003 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 72 가변", 
				"물크 2", "마크 9", "스킬 악마화 2", "스킬 환청 2", "스킬 추락하는 영혼 3", "스킬 리퍼 % 30", "ㄷ 암속깍 22", "암추뎀 10", null,
				"구원의 이기 - 낫", "", "", "", "", "", true, CalculatorVersion.VER_1_0_f,
				"물공 1003 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 72 가변", "물크 2", "마크 2",
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"소울 디바우링", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_f,
				"물공 1058 가변", "마공 1113 가변", "독공 686 가변", "힘 75 가변", "지능 75 가변", 
				"물크 2", "마크 2", "암속추 25", fStat[16], "설명 암속성 강화 120 이상일 때 마법 공격력 16% 증가", "설명 마나관리하세여", null,
				"창성의 구원자 - 낫", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_f,
				"물공 1058 가변", "마공 1113 가변", "독공 686 가변", "힘 75 가변", "지능 75 가변", "물크 2", "마크 2", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				"뿌리깊은 십자가", "", "", Weapon_detailType.PRIEST_CROSS, "", 80, false, CalculatorVersion.VER_1_0_f,
				"물공 998 가변", "마공 948 가변", "독공 611 가변", "지능 68 가변",
				"스킬 보호의 징표 3", "스킬 신성한 빛 3", "스킬 영광의 축복 5", "스킬 생명의 원천 4", "스킬 힐 윈드 4", "스킬 성령의 메이스 2", null,
				"라바룸", "", "", "", "", 85, "", CalculatorVersion.VER_1_0_f,
				"물공 1056 가변", "마공 1003 가변", "독공 648 가변", "지능 72 가변", 
				"스킬 빛의 복수 4", "스킬 영광의 축복 5", "스킬 신념의 오라 3", "스킬 보호의 징표 1", "스킬 아포칼립스 1", "스킬 성령의 메이스 2", null,
				"저주 받은 십자가 : 토루아", "", "", "", "", "", "", CalculatorVersion.VER_1_0_f,
				"물공 1056 가변", "마공 1003 가변", "독공 648 가변", "지능 72 가변", 
				"암속부여", "스킬 영광의 축복 4", "ㄷ 추뎀 40 선택", null,
				"구원의 이기 - 십자가", "", "", "", "", "", true, CalculatorVersion.VER_1_0_f,
				"물공 1056 가변", "마공 1003 가변", "독공 648 가변", "지능 72 가변", 
				"설명 스트라이킹 물리 공격력 증가량 20 증가(미구현)", "설명 지혜의 축복 마법 공격력 증가량 10 증가, 지능 증가량 20 증가(미구현)", "설명 영광의 축복 힘, 지능 증가량 20 증가(미구현)",
				"설명 해방시 - 스트라이킹 물리 공격력 5 증가, 힘 10 증가(미구현)", "설명 해방시 - 지혜의 축복 마법공격력 3 증가, 지능 5 증가(미구현)", "설명 해방시 - 영광의 축복 힘, 지능 5 증가(미구현)",
				"스킬 아포칼립스 % -100 선택", "설명 해방시 - 아포칼립스 지속시간 6초 증가", null,
				"썬더 크로스 : 유피테르", "", "", "", "", 90, false, CalculatorVersion.VER_1_0_f,
				"물공 1113 가변", "마공 1058 가변", "독공 686 가변", "지능 75 가변", 
				"명속부여", "스킬 아포칼립스 2", "스킬 영광의 축복 5", "스킬 스트라이킹 5", "스킬 지혜의 축복 5", "스킬 여명의 축복 5", "스킬 썬더 해머 : 유피테르 3", "스킬 성령의 메이스 3", "설명 젭발 로사우라에서 딴십자가쓰세요", null,
				"창성의 구원자 - 십자가", "", "", "", "", 90, true, CalculatorVersion.VER_1_0_f,
				"물공 1113 가변", "마공 1058 가변", "독공 686 가변", "지능 75 가변", 
				"설명 스트라이킹 물리 공격력 증가량 25 증가, 힘 10 증가(미구현)", "설명 지혜의 축복 마법 공격력 증가량 13 증가, 지능 증가량 25 증가(미구현)", "설명 영광의 축복 힘, 지능 증가량 25 증가(미구현)",
				"설명 해방시 - 모든 스킬 쿨타임 15% 감소", "스킬 아포칼립스 % -100 선택", "설명 아포칼립스 지속시간 6초 증가", null,
				
				
				/////////////////격투가
				//////////에픽
				/////너클
				"너클 바일런스", Item_rarity.EPIC, "", Weapon_detailType.FIGHTER_KNUCKLE, "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 948 가변", "마공 1241 가변", "독공 704 가변", "지능 68 가변",
				"명속강 24 가변", "스킬 20-35 4", "설명 나선의 넨 개수 1 증가(미구현)", null,
				"마나증강", "", "", "", "", 80, "", CalculatorVersion.VER_1_1_d,
				"물공 948 가변", "마공 1147 가변", "독공 611 가변", "지능 68 가변",
				"ㄷ 마공 300 가변", "ㄷ 지능 300 가변", "ㄷ 마크 20 선택", null,
				"라이키리", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_d,
				"물공 1003 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", 
				"명속강 45 가변", "명속부여", "스킬 뇌명 : 사나운 빛의 넨수 3", "스킬 기공장 3", "스킬 광충노도 3", "스킬 축기 3", 
				"스킬 기공장 3", "스킬 기호지세 3", "스킬 광풍나선력 3", "증뎀 18", "설명 기공장 다단히트 속도 25% 증가(미구현)", null,
				"베르세르크", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1003 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", 
				"ㄷ 물공뻥 60 가변", "ㄷ 마공뻥 60 가변", "ㄷ 독공뻥 60 가변", "설명 방어력 60% 감소", null,
				"구원의 이기 - 너클", "", "", "", "", "", true, CalculatorVersion.VER_1_1_d,
				"물공 1003 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"넨 클러스터", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_d,
				"물공 1058 가변", "마공 1280 가변", "독공 686 가변", "지능 75 가변", 
				"명속강 45 가변", "명속부여", "ㄷ 추뎀 10 선택", "ㄷ 명추뎀 16 선택", null,
				"창성의 구원자 - 너클", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_d,
				"물공 1058 가변", "마공 1280 가변", "독공 686 가변", "지능 75 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////건틀릿
				"타락한 악마의 손", "", "", Weapon_detailType.FIGHTER_GAUNTLET, "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 1198 가변", "마공 898 가변", "독공 611 가변", "힘 101 가변",
				"암속강 22 가변", "암속부여", "증뎀 12", "ㄷ 추뎀 23 선택", "ㄷ 암속강 15", "설명 추뎀 조건 : 악마 타입의 적 공격시", null,
				"맹호연환장", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_d,
				"물공 1267 가변", "마공 950 가변", "독공 648 가변", "힘 107 가변", 
				"물크 10", "ㄷ 물공뻥 20 선택", "ㄷ 독공뻥 20 선택", "증뎀 20", "설명 스킬 시전 시 15초동안 공격속도 50% 증가", null,
				"미완성 인피니티 건틀릿", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1267 가변", "마공 950 가변", "독공 648 가변", "힘 107 가변", 
				"스킬 1-80 2", "ㄷ 추뎀 15 선택", "ㄷ 추뎀 10 선택", null,
				"룰렛러시안", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1322 가변", "마공 950 가변", "독공 701 가변", "힘 107 가변", 
				"ㄷ 증뎀 66 가변 선택", "ㄷ 증뎀 55 선택", "ㄷ 증뎀 50 선택", "ㄷ 증뎀 40 선택", null,
				"구원의 이기 - 건틀릿", "", "", "", "", "", true, CalculatorVersion.VER_1_1_d,
				"물공 1267 가변", "마공 950 가변", "독공 648 가변", "힘 107 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"프라이머리 임펙션", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_d,
				"물공 1579 가변", "마공 1002 가변", "독공 983 가변", "힘 112 가변", 
				"추뎀 45", null,
				"창성의 구원자 - 건틀릿", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_d,
				"물공 1337 가변", "마공 1002 가변", "독공 686 가변", "힘 112 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////클로
				"원귀의 손톱", "", "", Weapon_detailType.FIGHTER_CLAW, "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 998 가변", "마공 998 가변", "독공 611 가변", "힘 68 가변", "지능 34 가변",
				"물크 3", "스킬 클로 마스터리 5", "ㄷ 추뎀 25 선택", "ㄷ 추뎀 15 선택", "설명 추뎀 조건 : 언데드, 인간형 타입 적 공격 시", null,
				"타르위 자리체", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_d,
				"물공 1056 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 35 가변", "물크 3",
				"스킬 도발 2", "스킬 혈관을 흐르는 맹독 2", "스킬 용독술 2", "스킬 도발 2", "스킬 천수천안 2", "스킬 룰 브레이크 2", "ㄷ 추뎀 30 선택", "설명 적 상변저 25 감소(미구현)", null,
				"악마의 갈퀴 : 이그노어", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1056 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 35 가변", "물크 3", 
				"스킬 클로 마스터리 1", "ㄷ 힘 300 가변", "ㄷ 지능 300 가변", "ㄷ 추뎀 30 선택", "설명 중독, 화상, 출혈데미지 25% 증가(미구현)", "설명 상변저 50 오라(미구현)", "설명 추뎀 조건 : 악마 타입의 적 공격시", null,
				"구원의 이기 - 클로", "", "", "", "", "", true, CalculatorVersion.VER_1_1_d,
				"물공 1056 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 35 가변", "물크 3", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"흑월랑아", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_d,
				"물공 1113 가변", "마공 1113 가변", "독공 686 가변", "힘 75 가변", "지능 37 가변", "물크 3", 
				"스킬 클로 마스터리 3", "모공증 35", "추뎀 18", "설명 중독, 화상, 출혈데미지 18% 증가", null,
				"창성의 구원자 - 클로", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_d,
				"물공 1113 가변", "마공 1113 가변", "독공 686 가변", "힘 75 가변", "지능 37 가변", "물크 3",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////권글
				"원귀의 손톱", "", "", Weapon_detailType.FITGHTER_BOXGLOVE, "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 1048 가변", "마공 948 가변", "독공 611 가변", "힘 68 가변",
				"ㄷ 물공 300 가변", "ㄷ 힘 300 가변", "ㄷ 물크 20 선택", null,
				"파울 키드니블로", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_d,
				"물공 1109 가변", "마공 1003 가변", "독공 648 가변", "힘 72 가변",
				"물크 15", "설명 백물크 30(미구현)", "스킬 급소 지정 1", "스킬 리미트 브레이크 1", "스킬 화력개방 1", "크증뎀 30", "ㄷ 크증뎀 50 선택", null,
				"그림자 유랑가", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1109 가변", "마공 1003 가변", "독공 648 가변", "힘 72 가변", 
				"암속강 30 가변", "암속부여", "ㄷ 증뎀 30", null,
				"N.O.V.A", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1109 가변", "마공 1003 가변", "독공 648 가변", "힘 237 가변", 
				"명속강 35 가변", "명속부여", "물크 10", "명속추 8", "설명 라이트 익스플로젼이 발생합니다", null,
				"구원의 이기 - 권투글러브", "", "", "", "", "", true, CalculatorVersion.VER_1_1_d,
				"물공 1109 가변", "마공 1003 가변", "독공 648 가변", "힘 72 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"퀸스 벳즈", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_d,
				"물공 1169 가변", "마공 1058 가변", "독공 686 가변", "힘 75 가변",
				"스킬 사상최강의 로킥 2", "스킬 화염의 각 2", "ㄷ 모공증 48 가변", "ㄷ 물공뻥 12", "ㄷ 독공뻥 12", null,
				"창성의 구원자 - 권투글러브", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_d,
				"물공 1169 가변", "마공 1058 가변", "독공 686 가변", "힘 75 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////통파
				"마나 미스릴 바", "", "", Weapon_detailType.FIGHTER_TONFA, "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 948 가변", "마공 998 가변", "독공 611 가변", "힘 68 가변", "지능 101",
				"ㄷ 추뎀 15 선택", "ㄷ 추뎀 10 선택", "ㄷ 추뎀 5 선택", null,
				"우요의 골든 통파", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_d,
				"물공 1003 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 107",
				"모속강 30 가변", "추뎀 20", "ㄷ 물공뻥 12 선택", "ㄷ 마공뻥 12 선택", "ㄷ 독공뻥 12 선택", null,
				"습격의 드라우프니르", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1003 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 107", 
				"스킬 와일드 캐넌 스파이크 % 30", "스킬 광충노도 % 30", "스킬 기호지세 % 30", "스킬 비트 드라이브 % 30", "스킬 헥토파스칼 킥 % 30", "스킬 정크 스핀 % 30", "스킬 니들 스핀 % 30", "증뎀 40", null,
				"구원의 이기 - 통파", "", "", "", "", "", true, CalculatorVersion.VER_1_1_d,
				"물공 1003 가변", "마공 1056 가변", "독공 648 가변", "힘 72 가변", "지능 107", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"킬 아르니스 협곡", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_d,
				"물공 1058 가변", "마공 1113 가변", "독공 686 가변", "힘 185 가변", "지능 222",
				"물크 12", "마크 12", "ㄷ 물공뻥 50", "ㄷ 마공뻥 50", "ㄷ 독공뻥 50", "설명 중독, 화상, 출혈데미지 50% 증가(미구현)", null,
				"창성의 구원자 - 통파", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_d,
				"물공 1169 가변", "마공 1058 가변", "독공 686 가변", "힘 75 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				
				///////////////마창사
				//////////에픽
				/////장창
				"빛의 심판자", Item_rarity.EPIC, "", Weapon_detailType.LANCE_PIKE, "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 1048 가변", "마공 898 가변", "독공 611 가변", "힘 101", "물크 2",
				"명속강 35 가변", "명속부여", "설명 75콤보 이상일 때 특정 스킬 쿨타임 초기화(메조싸이클론, 슈트롬, 파동연환창, 공아, 스월링 스피어)", null,
				"대나무창", Item_rarity.EPIC, "", Weapon_detailType.LANCE_PIKE, "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 1048 가변", "마공 898 가변", "독공 611 가변", "힘 101", "물크 2",
				"스킬 1-80 3", "ㄷ 추뎀 10", "설명 추뎀 조건 : 인간형 타입 적 공격시", "크증뎀 15", "설명 세계평등", null,  
				"철등사모", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_d,
				"물공 1109 가변", "마공 950 가변", "독공 648 가변", "힘 107 가변", "물크 2",
				"스킬 미라지 스트림 2", "추뎀 15", "ㄷ 증뎀 36 가변", null,
				"청월령", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1109 가변", "마공 950 가변", "독공 648 가변", "힘 107 가변", "물크 2", 
				"수속강 29 가변", "수속부여", "ㄷ 고정물방깍 158000 가변", "수속추 35 가변", "설명 수속추 확률 : 30%", null,
				"구원의 이기 - 장창", "", "", "", "", "", true, CalculatorVersion.VER_1_1_d,
				"물공 1109 가변", "마공 950 가변", "독공 648 가변", "힘 107 가변", "물크 2", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"호룡담", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_d,
				"물공 1169 가변", "마공 1002 가변", "독공 686 가변", "힘 112 가변", "물크 2",
				"스킬 미라지 스트림 3", "스킬 무형의 창술사 4", "추증뎀 30", "추크증 12", null,
				"창성의 구원자 - 장창", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_d,
				"물공 1169 가변", "마공 1058 가변", "독공 686 가변", "힘 75 가변",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", "설명 원본 아이콘 가지신분 제보 받습니다", null,
				
				/////미늘창
				"광염의 극", Item_rarity.EPIC, "", Weapon_detailType.LANCE_HALBERD, "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 1198 가변", "마공 848 가변", "독공 611 가변", "힘 156",
				"화속강 45 가변", "화속부여", "ㄷ 화속깍 10 선택", "설명 특정 스킬 공격 시 강-력한 파이어 익스플로젼 발생", null,
				"검은 재앙 - 절명", Item_rarity.EPIC, "", "", "", 80, false, CalculatorVersion.VER_1_1_d,
				"물공 1198 가변", "마공 848 가변", "독공 611 가변", "힘 68",
				"암속강 30 가변", "암속부여", "ㄷ 스증뎀 20 선택", "ㄷ 암속깍 25 선택", "설명 피격시 50% 확률로 자신을 암흑상태로 만듬", "설명 암흑 상태일 때 시야 패널티 50%, 적중률 패널티 10% 감소, 스증뎀 20%", null,  
				"방천극", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_d,
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 72",
				"스킬 마창 지배 2", "증뎀 40", "설명 마창 지배 1단계 HP비율 40% 증가, 2단계 비율 30% 증가", null,
				"황룡언월도", "", "", "", "", "", "", CalculatorVersion.VER_1_1_d,
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 72", 
				"물크 20", "ㄷ 물공뻥 15", "ㄷ 크증뎀 25 선택", "설명 크증뎀 조건 : 네임드, 챔피언, 보스몹 공격 시", null,
				"구원의 이기 - 미늘창", "", "", "", "", "", true, CalculatorVersion.VER_1_1_d,
				"물공 1267 가변", "마공 898 가변", "독공 648 가변", "힘 72", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"크림슨 로드", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_d,
				"물공 1337 가변", "마공 946 가변", "독공 648 가변", "힘 75",
				fStat[17], "설명 강화/증폭 수치 1마다 증뎀 3% 증가(최대 30%)", "설명 11강화/증폭부터 1 증가할때마다 스증뎀 5% 증가(최대 10%)", "설명 재련 수치 1마다 추뎀 3% 증가(최대 24%)", "설명 키리 : ^^", "설명 준 : ^ㅗ^", null,
				"창성의 구원자 - 미늘창", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_d,
				"물공 1337 가변", "마공 946 가변", "독공 648 가변", "힘 75",
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", "설명 원본 아이콘 가지신분 제보 받습니다", null,
				
				//////////////////도적
				///////////에픽
				/////단검
				"스파이럴 스핀", Item_rarity.EPIC, "", Weapon_detailType.THIEF_DAGGER, "", 80, false, CalculatorVersion.VER_1_1_f,
				"물공 936 가변", "마공 898 가변", "독공 611 가변", "힘 68 가변", "물크 10",
				"스킬 허리케인 3", "스킬 버티컬 스파이럴 3", "스킬 데스 허리케인 3", "스킬 어슬랜트 스파이럴 2", "스킬 나락 떨구기 5", "스킬 칼날돌풍 3", "ㄷ 추뎀 12 선택", null,
				"실버 스피릿", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_f,
				"물공 993 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", "물크 10", 
				"명속강 35 가변", "명속부여", "스킬 실버스트림 2", "스킬 히트엔드 2", "스킬 문아크 3", "스킬 암살 2", "스킬 암살자의 마음가짐 2", "스킬 배후습격 1", "ㄷ 힘 200 선택", "ㄷ 물공 150 선택",
				"ㄷ 물크 5 선택", "설명 물크 5%는 배후습격 크리 증가입니다", "설명 실버스트림 쿨타임 30% 감소, 히트엔드 사용시 남는 연계점수 1 증가", "설명 암살 쿨타임 20% 감소", null,
				"찬란한 여왕의 은장도", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 993 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", "물크 10", 
				"명속부여", "스킬 히트엔드 2", "스킬 날카로운 단검 2", "ㄷ 증뎀 60 가변", null,
				"구원의 이기 - 단검", "", "", "", "", "", true, CalculatorVersion.VER_1_1_f,
				"물공 993 가변", "마공 950 가변", "독공 648 가변", "힘 72 가변", "물크 10", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"월광검", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_f,
				"물공 1052 가변", "마공 1002 가변", "독공 686 가변", "힘 75 가변", "물크 10", 
				"ㄷ 물공뻥 18", "추증뎀 20", "ㄷ 스증뎀 18 선택", "설명 아 달 어딨음 ㅡ", null,
				"창성의 구원자 - 단검", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_f,
				"물공 1052 가변", "마공 1002 가변", "독공 686 가변", "힘 75 가변", "물크 10", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////쌍검
				"흑미쌍검", "", "", Weapon_detailType.THIEF_TWINSWORD, "", 80, false, CalculatorVersion.VER_1_1_f,
				"물공 1082 가변", "마공 799 가변", "독공 611 가변", "힘 68 가변", "물크 5",
				"암속강 22 가변", "암속부여", "스킬 어슬랜트 스파이럴 2", "스킬 쌍검 마스터리 2", "추뎀 10", "ㄷ 추뎀 15", "설명 추뎀 15% 조건 : 암흑 상태의 적 공격시", null,
				"스파이럴 애쉬", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_f,
				"물공 1147 가변", "마공 845 가변", "독공 648 가변", "힘 72 가변", "물크 5", 
				"스킬 쌍검 마스터리 3", "증뎀 35", "설명 어슬랜트 스파이럴 쿨타임 30% 감소", null,
				"디 엔드", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1147 가변", "마공 845 가변", "독공 648 가변", "힘 72 가변", "물크 5", 
				"수속강 24 가변", "수속부여", "스킬 히트엔드 % 50", null,
				"오버 더 페이트", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1147 가변", "마공 845 가변", "독공 648 가변", "힘 72 가변", "물크 5", 
				"스킬 쌍검 마스터리 2", "ㄷ 추뎀 20", "ㄷ 추뎀 10 선택", "ㄷ 추뎀 10 선택", "크증뎀 10", "설명 HP 60% 이상 적 공격시 20% 추뎀", "설명 HP 20~60% 적 공격시 30% 추뎀", "설명 HP 20% 이하 적 공격시 40% 추뎀", null,
				"구원의 이기 - 쌍검", "", "", "", "", "", true, CalculatorVersion.VER_1_1_f,
				"물공 1147 가변", "마공 845 가변", "독공 648 가변", "힘 72 가변", "물크 5", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"히게-히자키리", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_f,
				"물공 1212 가변", "마공 891 가변", "독공 686 가변", "힘 75 가변", "물크 5", 
				"화속강 30 가변", "수속강 30 가변", "화속부여", "수속부여", "ㄷ 물공뻥 8", "ㄷ 독공뻥 8", fStat[6], "설명 가장 높은 속성 추가 데미지 20%", null,
				"창성의 구원자 - 쌍검", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_f,
				"물공 1212 가변", "마공 891 가변", "독공 686 가변", "힘 75 가변", "물크 5", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////완드
				"파멸의 철퇴", "", "", Weapon_detailType.THIEF_WAND, "", 80, false, CalculatorVersion.VER_1_1_f,
				"물공 867 가변", "마공 1147 가변", "독공 611 가변", "지능 68 가변", "마크 5",
				"스킬 발라크르의 맹약 2", "스킬 언홀리 마스터리 2", "스킬 학살의 발라크르 % 10", "설명 발라크르의 야망 2타 공격력 25% 증가(미구현)", "설명 기요틴 2타 충격파 공격력 35% 증가(미구현)", 
				"설명 팬텀스트롬 피니시 공격력 45% 증가(미구현)", "설명 분기 암경파 폭발 공격력 55% 증가(미구현)", null,
				"로드 오브 다크니스", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_f,
				"물공 922 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", "마크 5", 
				"스킬 학살의 발라크르 강력 4", "스킬 학살의 발라크르 강령 % 15", "스킬 발라크르의 야망 % 40", "스킬 기요틴 % 40", "추뎀 10", "설명 발라크르의 야망, 기요틴 쿨타임 15% 감소", null,
				"프린스 오브 스파이더", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 922 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", "마크 5", 
				"암속강 35 가변", "추뎀 20", "설명 니콜라스 지속시간 200초 감소, 흑사진 지속시간 3초 증가", "설명 흡기암경파, 분기암경파 쿨타임 20% 감소", null,
				"구원의 이기 - 완드", "", "", "", "", "", true, CalculatorVersion.VER_1_1_f,
				"물공 922 가변", "마공 1214 가변", "독공 648 가변", "지능 72 가변", "마크 5", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"딘 호크모트", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_f,
				"물공 977 가변", "마공 1280 가변", "독공 686 가변", "지능 75 가변", "마크 5", 
				"암속강 35 가변", "스킬 언홀리 마스터리 3", "암속추 22", "설명 60초마다 HP 5% 감소", null,
				"창성의 구원자 - 완드", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_f,
				"물공 977 가변", "마공 1280 가변", "독공 686 가변", "지능 75 가변", "마크 5", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				
				/////차크라웨펀
				"화광충천 : 멸", "", "", Weapon_detailType.THIEF_CHAKRAWEAPON, "", 80, false, CalculatorVersion.VER_1_1_f,
				"물공 821 가변", "마공 1098 가변", "독공 611 가변", "지능 101 가변",
				"화속강 18 가변", "스킬 열화천도 % 10", "설명 열화천도 시전 시 쿨타임이 적용되지 않음(쿨타임 15초), 시전시 0.2초동안 인술 인 개당 충전시간 75% 감소", null,
				"크리드 오브 닌자", "", "", Weapon_detailType.THIEF_CHAKRAWEAPON, "", 80, false, CalculatorVersion.VER_1_1_f,
				"물공 821 가변", "마공 1098 가변", "독공 611 가변", "지능 101 가변",
				"마크 10", "크증뎀 30", "설명 공격 시 5% 확률로 10초동안 카모플라쥬 시전", null,
				"화둔의 비기 : 폭영", "", "", "", "", 85, "", CalculatorVersion.VER_1_1_f,
				"물공 873 가변", "마공 1162 가변", "독공 648 가변", "지능 107 가변", 
				"스킬 암영술 2", "스킬 인법 : 잔영 남기기 2", "증뎀 18", "스킬 인법 : 잔영 남기기 % 18", "설명 인법 : 잔영 남기기 잔영 회복시간 30% 감소", null,
				"여섯세계의 순환", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 873 가변", "마공 1328 가변", "독공 648 가변", "지능 107 가변", 
				"마크 20", "스킬 인법 : 육도윤회 3", "스킬 인법 : 육도윤회 강화 1", "ㄷ 마공뻥 15", "설명 인법 : 육도윤회 녹화 시간 30% 증가", "설명 인술 인 1개당 충전시간 75% 감소", null,
				"구원의 이기 - 차크라 웨펀", "", "", "", "", "", true, CalculatorVersion.VER_1_1_f,
				"물공 873 가변", "마공 1162 가변", "독공 648 가변", "지능 107 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"서킷 세레브레이트", "", "", "", "", 90, false, CalculatorVersion.VER_1_1_f,
				"물공 925 가변", "마공 1224 가변", "독공 686 가변", "지능 112 가변", 
				fStat[17], "설명 강화/증폭 수치 1마다 증뎀 3% 증가(최대 30%)", "설명 11강화/증폭부터 1 증가할때마다 스증뎀 5% 증가(최대 10%)", "설명 재련 수치 1마다 추뎀 3% 증가(최대 24%)", "설명 키리 : ^^", "설명 준 : ^ㅗ^", null,
				"창성의 구원자 - 차크라 웨펀", "", "", "", "", 90, true, CalculatorVersion.VER_1_1_f,
				"물공 925 가변", "마공 1224 가변", "독공 686 가변", "지능 112 가변", 
				"ㄷ 스증뎀 40", "ㄷ 스증뎀 22 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
			
				///////////////레전
				/////단검
				"진혼의 대거", Item_rarity.LEGENDARY, "", Weapon_detailType.THIEF_DAGGER, "", 85, false, CalculatorVersion.VER_1_1_f,
				"물공 941 가변", "마공 901 가변", "독공 607 가변", "힘 101 가변",
				"물크 14", "마크 4", "추뎀 11", null,
				"리버레이션 대거", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 941 가변", "마공 901 가변", "독공 607 가변", "힘 129 가변",
				"물크 17", "마크 7", "추뎀 16", null,
				"디리지에의 독니", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 941 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변", "물크 18",
				"ㄷ 추뎀 20 선택", "설명 추가데미지 조건 : 중독 상태의 적 공격시", null,
				"왜곡된 시간의 절", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 941 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변", "물크 10",
				"ㄷ 추뎀 20 선택", "설명 추가데미지 조건 : 타이오릭의 흡기구슬이 생성된 적 공격시", "설명 타이오릭의 흡기구슬이 생성된 적 처치시 스킬 쿨타임 5% 감소", null, 
				"스컬케인의 집념", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 941 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변", "물크 10",
				"암속부여", "스킬 칼날돌풍 % 34", "설명 허리케인 지속시간 30% 증가, 다단히트수 3 증가(미구현)", "설명 평타 시 데미지 40% 증가", "설명 공격 시 1% 확률로 HP 2% 감소", null,
				"람누의 보이지 않는 칼날", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 941 가변", "마공 901 가변", "독공 607 가변", "힘 68 가변", "물크 10",
				"ㄷ 크증뎀 10", null,
				"테라 : 리컨스트럭션 대거", "", "", "", "", 90, "", CalculatorVersion.VER_1_1_f,
				"물공 997 가변", "마공 953 가변", "독공 642 가변", "힘 72 가변", 
				"물크 22", "마크 12", "스킬 1-85 1", null,
				"렛드 루마", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 997 가변", "마공 953 가변", "독공 642 가변", "힘 72 가변", "물크 10",
				"스킬 단검 마스터리 3", "스킬 날카로운 단검 3", "ㄷ 물크 12", "크증뎀 18", null,
				
				/////쌍검
				"진혼의 트윈소드", "", "", Weapon_detailType.THIEF_TWINSWORD, "", 85, false, CalculatorVersion.VER_1_1_f,
				"물공 1087 가변", "마공 801 가변", "독공 607 가변", "힘 101 가변",
				"물크 9", "마크 4", "추뎀 11", null,
				"리버레이션 트윈소드", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1087 가변", "마공 801 가변", "독공 607 가변", "힘 129 가변",
				"물크 12", "마크 7", "추뎀 16", null,
				"피를 부르는 쌍검", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1087 가변", "마공 801 가변", "독공 607 가변", "힘 68 가변", "물크 10",
				"ㄷ 물공 150 가변", "추뎀 8", null,
				"타오르는 불꽃의 잔상", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1087 가변", "마공 801 가변", "독공 607 가변", "힘 68 가변", "물크 5",
				"화속부여", "ㄷ 화속추 12 가변", null, 
				"악몽이 구현된 이빨", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1258 가변", "마공 801 가변", "독공 607 가변", "힘 68 가변", "물크 10",
				"암속강 14 가변", "암속부여", "설명 공속 -10%", null,
				"흉터로 기억되는 자", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1087 가변", "마공 801 가변", "독공 607 가변", "힘 68 가변", "물크 10",
				"암속강 18 가변", "암속부여", "ㄷ 크증뎀 15 선택", "설명 크증뎀 조건 : 출혈상태의 적 공격시", null,
				"테라 : 리컨스트럭션 트윈소드", "", "", "", "", 90, "", CalculatorVersion.VER_1_1_f,
				"물공 1151 가변", "마공 847 가변", "독공 642 가변", "힘 72 가변", 
				"물크 17", "마크 12", "스킬 1-85 1", null,
				"듀오 쿠프스 혼", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 1151 가변", "마공 847 가변", "독공 642 가변", "힘 72 가변", "물크 5",
				"스킬 쌍검 마스터리 1", "추크증 10", "크증뎀 20", null,
				
				/////완드
				"진혼의 완드", "", "", Weapon_detailType.THIEF_WAND, "", 85, false, CalculatorVersion.VER_1_1_f,
				"물공 871 가변", "마공 1152 가변", "독공 607 가변", "지능 101 가변",
				"물크 4", "마크 9", "추뎀 11", null,
				"리버레이션 완드", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 871 가변", "마공 1152 가변", "독공 607 가변", "지능 129 가변",
				"물크 7", "마크 12", "추뎀 16", null,
				"독을 뿜는 자의 유골", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 871 가변", "마공 1152 가변", "독공 607 가변", "지능 68 가변", "마크 5",
				"ㄷ 추뎀 25 선택", "설명 추뎀 조건 : 중독상태의 적 공격시", "설명 망자의 원한 쿨타임 20% 감소", null,
				"피로 물든 상처", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 871 가변", "마공 1152 가변", "독공 607 가변", "지능 68 가변", "마크 5",
				"ㄷ 크증뎀 22 선택", "ㄷ 지능 50 가변", null, 
				"꿈틀대는 더러운 사념", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 871 가변", "마공 1152 가변", "독공 607 가변", "지능 68 가변", "마크 5",
				"암속강 22 가변", "ㄷ 추뎀 10 선택", "설명 추가데미지 조건 : 상태이상에 걸린 적 공격시", null,
				"주술사의 아홉 해골", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 871 가변", "마공 1152 가변", "독공 607 가변", "지능 68 가변", "마크 5",
				"스킬 암흑의 의식 2", "스킬 야행혼 % 20", "스킬 발라크르의 야망 % 20", "스킬 기요틴 % 20", "설명 니콜라스 지속시간 150초 감소, 150초동안 니콜라스의 분신 소환", null,
				"테라 : 리컨스트럭션 완드", "", "", "", "", 90, "", CalculatorVersion.VER_1_1_f,
				"물공 925 가변", "마공 1217 가변", "독공 642 가변", "지능 72 가변", 
				"물크 12", "마크 17", "스킬 1-85 1", null,
				"투니쿨라 마누스", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 925 가변", "마공 1217 가변", "독공 642 가변", "지능 72 가변", "마크 5",
				"증뎀 25", "스킬 익스큐서너 슬래쉬 % 35", null,
				
				/////차크라웨펀
				"진혼의 차크라 웨펀", "", "", Weapon_detailType.THIEF_CHAKRAWEAPON, "", 85, false, CalculatorVersion.VER_1_1_f,
				"물공 826 가변", "마공 1102 가변", "독공 607 가변", "지능 134 가변",
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 차크라 웨펀", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 826 가변", "마공 1102 가변", "독공 607 가변", "지능 162 가변",
				"물크 7", "마크 7", "추뎀 16", null,
				"화둔 : 염화멸섬", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 826 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"화속강 18 가변", "화속추 12", null,
				"용암충의 섬뜩한 이빨", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 826 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"화속강 14 가변", "화속부여", "설명 쿠나이 연마 쿨타임 감소율 30% 증가", "설명 암영술 기폭찰 공격력 15% 증가(미구현)", null, 
				"고대의 병기", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 826 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"스킬 쿠나이 던지기 1", "스킬 나선 대차륜 쿠나이 2", "스킬 쿠나이 연마 2", "설명 쿨타임 25% 감소", null,
				"오토매틱 대차륜 쿠나이", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 826 가변", "마공 1102 가변", "독공 607 가변", "지능 101 가변",
				"스킬 나선 대차륜 쿠나이 3", "스킬 나선 대차륜 쿠나이 % 20", "설명 나선 대차륜 쿠나이 회전 다단히트 최대 시간 50% 감소(미구현)", 
				"설명 나선 대차륜 쿠나이 시전 시 0.2초동안 인술 인 1개당 충전시간 75% 감소", null,
				"테라 : 리컨스트럭션 차크라 웨펀", "", "", "", "", 90, "", CalculatorVersion.VER_1_1_f,
				"물공 877 가변", "마공 1164 가변", "독공 642 가변", "지능 107 가변", 
				"물크 12", "마크 12", "스킬 1-85 1", null,
				"달빛의 그림자", "", "", "", "", "", "", CalculatorVersion.VER_1_1_f,
				"물공 877 가변", "마공 1164 가변", "독공 642 가변", "지능 107 가변",
				"스킬 야타의 거울 5", "스킬 암영술 3", "스킬 쿠나이 연마 1", "설명 인법 : 바꿔치기 쿨타임 25% 감소", "설명 인법 : 잔영 남기기 잔영 회복 타임 10% 감소", null,
		};
		
		return data;
	}
}
