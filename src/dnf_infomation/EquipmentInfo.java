package dnf_infomation;

import java.util.Arrays;
import java.util.LinkedList;

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
				
				//레벨
				temp = data[i++];
				if(temp instanceof Boolean) isRare = (boolean) temp;
				else if(temp.equals(""));	//이전 값 유지
				else throw new ParsingException(i-1, temp);
				
				if(isWeapon) equipment = new Weapon(name, rarity, weaponType, setName, level, isRare);
				else equipment = new Equipment(name, rarity, part, type, setName, level, isRare);
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
				throw new ParsingException(i-1, temp);
			}
			equipList.add(equipment);
		}
	}
	
	public static Object[] equipmentInfo()
	{
		FunctionStat fStat[] = new FunctionStat[11];
		
		//익스포젼 헤비 각반
		fStat[0] = new FunctionStat() {
			private static final long serialVersionUID = -4590364678263064444L;
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
			private static final long serialVersionUID = 4036286523104766974L;
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
			private static final long serialVersionUID = -7780279158612957210L;			
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
			private static final long serialVersionUID = -7780279158612957210L;		
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
			private static final long serialVersionUID = -7055122057802138808L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - "))
					statList.addStatList("스증뎀", 12.5);
				return statList;
			}
		};
		
		//조로크
		fStat[5] = new FunctionStat() {
			private static final long serialVersionUID = -4289570411103128538L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Weapon weapon = character.getItemSetting().weapon;
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - ")){
					try {
						int phy = (int)(weapon.vStat.findStat(StatList.WEP_PHY).stat.getStatToDouble()*0.15+0.00001);
						int mag = (int)(weapon.vStat.findStat(StatList.WEP_MAG).stat.getStatToDouble()*0.15+0.00001);
						int ind = (int)(weapon.vStat.findStat(StatList.WEP_IND).stat.getStatToDouble()*0.18+0.00001);
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
			private static final long serialVersionUID = -1627633319961139363L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.getName().startsWith("구원의 이기 - "))
					statList.addStatList("스증뎀", (155.0/135.0-1)*100);
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
			private static final long serialVersionUID = 5505622447332872280L;

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
			private static final long serialVersionUID = 7481039452146325482L;
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
		
		fStat[10] = new FunctionStat(){
			private static final long serialVersionUID = 3985320867418376540L;
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
				"설명 (미구현)", null,
				"애착의 나르시즘", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"붕괴의 게슈탈트", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"죽음의 타나토스", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"인격의 페르소나", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				
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
				"힘 92 가변", "지능 92 가변", "크증뎀 18", null,
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
				"힘 26 가변", "지능 26 가변", "물공 132 가변", "마공 132 가변", "독공 132 가변", "증뎀 10", null,
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
				"설명 (미구현)", null,
				"니겔루스의 초합금", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"갈바누스의 성장", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"위로르의 증기", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"루벨루스의 염화", "", "--", "", "", "", "",
				"설명 (미구현)", null,

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
				"설명 (미구현)", null,
				"초대륙 - 판게아의 지진", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"초대륙 - 파노티아의 화산", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"초대륙 - 로디니아의 용암", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"초대륙 - 케놀랜드의 지각", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				
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
				"힘 48 가변", "지능 35 가변", "ㄷ 모공증 15 선택", "ㄷ 스증 15 선택", fStat[0], "설명 옵션 2개 모두 선택시 스증옵션만 적용", null,
				"컨테미네이션 폴드런", "", "--", "", "", "", "",
				"힘 122 가변", "지능 111 가변", "모공증 12 선택", null,
				"퓨어로드 코일", "", "--", "", "", "", "",
				"힘 112 가변", "지능 103 가변", "ㄷ 물공 100 가변", "ㄷ 마공 100 가변", "ㄷ 독공 100 가변", "ㄷ 물공 80", "ㄷ 마공 80", "ㄷ 독공 80", null,
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
				"설명 (미구현)", null,
				"유해교반의 바스키", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"해룡왕 사가라", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"자객의 탁샤카", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"거련의 우트파라카", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				
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
				"힘 34 가변", "지능 34 가변", "물크 7", "마크 7", "ㄷ 크증뎀 15", null,
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
				"메탈라인 플레이트 코일", "", "--", "", "", "", "",
				"힘 136 가변", "지능 136 가변", "물크 7", "마크 7", "스킬 45-40 1", null,
				"메탈라인 그리브", "", "--", "", "", "", "",
				"힘 136 가변", "지능 136 가변", "물크 7", "마크 7", "스킬 35-48 1", null,
				/////칠죄종
				"오만에 가득찬 눈", "", Equip_part.ROBE, "", SetName.SEVENSINS, 90, true,
				"설명 (미구현)", null,
				"폭식하는 입", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"탐식을 쥐는 손", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"질투를 말하는 혀", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				"나태함을 가진 발", "", "--", "", "", "", "",
				"설명 (미구현)", null,
				
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
				"지능 41 가변", "수속강 22 가변", "ㄷ 수속부여", null,
				"냉정한 공주의 팔찌", "", "--", "", "", "", "",
				"힘 41 가변", "수속강 30 가변", null,
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
				"힘 41 가변", fStat[4], "설명 구원의 이기 무기 개방시 스킬데미지 증가량 20->35%", null,
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
				"힘 42 가변", "지능 42 가변", "ㄷ 물공뻥 5", "ㄷ 마공뻥 5", "ㄷ 독공뻥 5", "ㄷ 물크 30 선택", "ㄷ 마크 30 선택", "설명 함정카드 발동", "설명 딴딴딴따 따~라라 따~라라", null,
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
				"힘 121 가변", "지능 121 가변", fStat[6], "설명 구원의 이기 스킬 데미지 증가량 35->55%", null,
				"무한한 탐식의 기원", "", "", "", "", "",  "",
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
				"무언의 건설자 귀걸이", "", "", "", SetName.TACITCONSTRUCTOR, "", "",
				"힘 62 가변", "지능 62 가변", "물공 149 가변", "마공 149 가변", "독공 171 가변", null,
				/////에픽
				"브라이들 펄", Item_rarity.EPIC, "", "", SetName.NONE, "", "",
				"힘 63 가변", "지능 63 가변", "물공 165 가변", "마공 165 가변", "독공 189 가변", "추크증 8", null,
				"흑백의 경계 : 혼돈", "", "", "", "", "", "",
				"힘 184 가변", "지능 184 가변", "ㄷ 물공뻥 15 선택", "ㄷ 마공뻥 15 선택", "ㄷ 독공뻥 15", null,
				"바벨로니아의 상징", "", "", "", "", "", true,
				"힘 63 가변", "지능 63 가변", "모속강 18 가변", "모공증 18 가변", "설명 겜", "설명 수련의 방에서 제대로 작동하지 않습니다", null,
				
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
				
				////////////중갑
				/////85
				"진 : 프로 싸움꾼의 중갑 상의", "", Equip_part.ROBE, Equip_type.HEAVY, SetName.REAL_PROFIGHTER_HARMOR, 85, false,
				"힘 45 가변", "지능 33 가변", "스킬 30-40 2", null,
				"진 : 프로 싸움꾼의 중갑 하의", "", "--", "", "", "", "",
				"힘 188 가변", "지능 33 가변", "마크 3", "설명 힘과 마크를 올려주는 정체불명의 옵션", null,
				"진 : 프로 싸움꾼의 중갑 어깨", "", "--", "", "", "", "",
				"힘 36 가변", "지능 169 가변", null,
				"진 : 프로 싸움꾼의 중갑 허리", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "물크 3", null,
				"진 : 프로 싸움꾼의 중갑 신발", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "설명 내가 이걸 왜 구현하고있지", null,
				
				"무신의 기운이 담긴 중갑 상의", "", Equip_part.ROBE, "", SetName.GODOFFIGHT, 85, false,
				"힘 45 가변", "지능 33 가변", "스킬 20-45 2", null,
				"무신의 기운이 담긴 중갑 하의", "", "--", "", "", "", "",
				"힘 100 가변", "지능 88 가변", "스킬 1-45 1", null,
				"무신의 기운이 담긴 중갑 어깨", "", "--", "", "", "", "",
				"힘 36 가변", "지능 26 가변", "물크 5", "마크 5", "ㄷ 물공 55", "ㄷ 마공 55", "ㄷ 독공 67", "설명 쿨/지속 20/15", null,
				"무신의 기운이 담긴 중갑 허리", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "모속강 5", "ㄷ 물공 55", "ㄷ 마공 55", "ㄷ 독공 67", "설명 쿨/지속 20/15", null,
				"무신의 기운이 담긴 중갑 신발", "", "--", "", "", "", "",
				"힘 61 가변", "지능 53 가변", "ㄷ 물공 55", "ㄷ 마공 55", "ㄷ 독공 67", "설명 쿨/지속 20/15", null,
				
				"그라시아 가문의 유산 - 중갑 상의", "", Equip_part.ROBE, "", SetName.GRACIA, 85, false,
				"힘 45 가변", "지능 33 가변", "명속강 16 가변", null,
				"그라시아 가문의 유산 - 중갑 하의", "", "--", "", "", "", "",
				"힘 45 가변", "지능 33 가변", "명속강 16 가변", null,
				"그라시아 가문의 유산 - 중갑 어깨", "", "--", "", "", "", "",
				"힘 36 가변", "지능 26 가변", "명속강 10 가변", "ㄷ 힘 80 선택 가변", "ㄷ 지능 80 선택 가변", "ㄷ 명속강 5 선택 가변", null, 
				"그라시아 가문의 유산 - 중갑 허리", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "명속강 10 가변", "ㄷ 힘 80 선택 가변", "ㄷ 지능 80 선택 가변", "ㄷ 명속강 5 선택 가변", null,
				"그라시아 가문의 유산 - 중갑 신발", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "명속강 10 가변", "ㄷ 힘 80 선택 가변", "ㄷ 지능 80 선택 가변", "ㄷ 명속강 5 선택 가변", null,
				
				"해신의 저주를 받은 체인 메일", "", Equip_part.ROBE, "", SetName.CURSEOFSEAGOD, 85, false,
				"힘 45 가변", "지능 33 가변", "수속강 7 가변", "ㄷ 힘 80", "ㄷ 지능 80", null,
				"해신의 저주를 받은 체인 레깅스", "", "--", "", "", "", "",
				"힘 45 가변", "지능 33 가변", "수속강 7 가변", "스킬 1-45 1", null,
				"해신의 저주를 받은 체인 맨틀", "", "--", "", "", "", "",
				"힘 36 가변", "지능 26 가변", "수속강 7 가변", "물크 4", "마크 4", null,
				"해신의 저주를 받은 체인 코일", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "수속강 7 가변", null,
				"해신의 저주를 받은 체인 사바톤", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "수속강 7 가변", null,
				
				"애끓는 비탄의 스케일 메일", "", Equip_part.ROBE, "", SetName.DEVASTEDGRIEF, 85, false,
				"힘 45 가변", "지능 33 가변", "ㄷ 힘 100", "ㄷ 지능 100", null,
				"애끓는 비탄의 스케일 레깅스", "", "--", "", "", "", "",
				"힘 45 가변", "지능 33 가변", "ㄷ 물공 38", "ㄷ 마공 38", "ㄷ 독공 59", null,
				"애끓는 비탄의 스케일 숄더", "", "--", "", "", "", "",
				"힘 64 가변", "지능 54 가변", null,
				"애끓는 비탄의 스케일 벨트", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "ㄷ 모속강 10", null,
				"애끓는 비탄의 스케일 사바톤", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", "ㄷ 물크 8", "ㄷ 마크 8", null,
				
				"위대한 영광의 중갑 상의", "", Equip_part.ROBE, "", SetName.GREATGLORY, 85, false,
				"힘 199 가변", "지능 33 가변", "물크 5", null,
				"위대한 영광의 중갑 하의", "", "--", "", "", "", "",
				"힘 45 가변", "지능 187 가변", "마크 5", null,
				"위대한 영광의 중갑 어깨", "", "--", "", "", "", "",
				"힘 190 가변", "지능 26 가변", "물크 5", null,
				"위대한 영광의 중갑 허리", "", "--", "", "", "", "",
				"힘 28 가변", "지능 174 가변", "마크 5", null,
				"위대한 영광의 중갑 신발", "", "--", "", "", "", "",
				"힘 105 가변", "지능 97 가변", "ㄷ 모속강 10 선택", null,
				
				"붉게 물든 서녘의 중갑 상의", "", Equip_part.ROBE, "", SetName.ROMANTICE, 85, false,
				"힘 45 가변", "지능 33 가변", "물공 55 가변", "독공 83 가변", "물크 10", null,
				"붉게 물든 서녘의 중갑 하의", "", "--", "", "", "", "",
				"힘 45 가변", "지능 33 가변", "마공 55 가변", "독공 83 가변", "마크 10", null,
				"붉게 물든 서녘의 중갑 어깨", "", "--", "", "", "", "",
				"힘 36 가변", "지능 26 가변", null,
				"붉게 물든 서녘의 중갑 허리", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", null,
				"붉게 물든 서녘의 중갑 신발", "", "--", "", "", "", "",
				"힘 28 가변", "지능 20 가변", null,
				
				"강완의 체인 메일", "", Equip_part.ROBE, "", SetName.NONE, 85, false,
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
				"힘 37 가변", "화속강 10 가변", "ㄷ 화속강 32 선택", "ㄷ 화속부여", null,
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
				"인간 근육 팔찌", "", "", "", "", "", "",
				"힘 40 가변", "ㄷ 물크 15 가변", "ㄷ 힘 105 가변", "ㄷ 힘 50 선택", null,
				"파수꾼의 나침반 팔찌", "", "", "", "", "", "",
				"힘 40 가변", "ㄷ 물크 10 선택", "ㄷ 마크 10 선택", null,
				"얼어붙은 자의 굳건함", "", "", "", "", "", "",
				"힘 40 가변", "ㄷ 수속깍 20 선택", null,
				"빛나는 자의 엄격함", "", "", "", "", "", "",
				"힘 40 가변", "명속강 12 가변", "ㄷ 명속부여", null,
				
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
				"위대한 영광의 완장", "", "--", "", "", "", "",
				"힘 39 가변", "지능 39 가변", "물공 77 가변", "마공 77 가변", "독공 116 가변", null,
				"위대한 영광의 상징", "", "--", "", "", "", "",
				"힘 58 가변", "지능 58 가변", "물공 77 가변", "마공 77 가변", "독공 116 가변", null,
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
				"붉게 물든 서녘의 목걸이", "", Equip_part.NECKLACE, "", SetName.ROMANTICE, "", "",
				"지능 40 가변", "ㄷ 마공뻥 10 선택", null,
				"붉게 물든 서녘의 팔찌", "", "--", "", "", "", "",
				"힘 40 가변", "ㄷ 물공뻥 10 선택", null,
				"붉게 물든 서녘의 반지", "", "--", "", "", "", "",
				"힘 59 가변", "지능 59 가변", "ㄷ 독공뻥 10 선택", null,
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
				"힘 63 가변", "지능 63 가변", "설명 30초마다 자신을 포함한 랜덤한 파티원의 스킬 중 쿨타임이 30초 이내의 모든 스킬 쿨타임 30% 감소..?", null,
				
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
				
				"아포피스의 눈", "", Equip_part.MAGICSTONE, "", "", 90, "",
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
				"루멘 칼리고", "", "", "", "", 90, "",
				"힘 150 가변", "지능 150 가변", "추뎀 10", null,
				"무언의 건설자 수석", "", "", "", SetName.TACITCONSTRUCTOR, "", "",
				"힘 62 가변", "지능 62 가변", "ㄷ 스증뎀 12 선택", null,
				
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
		};
		
		return data;
	}
	
	public static Object[] weaponInfo()
	{
		FunctionStat fStat[] = new FunctionStat[20];
		
		//다중선택시 마지막 옵션으로 적용
		FunctionStat multiCheck = new FunctionStat(){
			private static final long serialVersionUID = -7055122057802138808L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				int count=0;
				Equipment equipment = (Equipment)item;
				int num = equipment.dStat.statList.size();
				for(int i=0; i<num; i++)
					if(equipment.dStat.statList.get(i).enabled) count++;
								
				if(count>1){
					for(int i=0; i<num-1; i++)
						equipment.dStat.statList.get(i).enabled=false;
					equipment.dStat.statList.get(num-1).enabled=true;
				}
				return new StatusList();
			}
		};
		
		//이기 속성
		FunctionStat enableElement = new FunctionStat(){
			private static final long serialVersionUID = -4289570411103128538L;

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
			private static final long serialVersionUID = -4289570411103128538L;

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
			private static final long serialVersionUID = 4683501406496269136L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				//if(monster.getStat("종족"))			//TODO
				return statList;
			}
		};
		
		//골드럭스
		fStat[2] = new FunctionStat(){
			private static final long serialVersionUID = -6086275352177792432L;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.RANGER_F) 		//TODO
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
			private static final long serialVersionUID = 8871754132243738754L;

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
			private static final long serialVersionUID = -7856672477333603458L;
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
			private static final long serialVersionUID = 7854162089450295598L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				if(character.getJob()==Job.LAUNCHER_F){
					statList.addSkill("M-3 화염방사기", 3);
					statList.addSkill("화염 강타", 4);
				}
				/*else if(character.getJob()==Job.LAUNCHER_M){
					statList.addSkill("레이저 라이플", 3);
					statList.addSkill("충전 레이저 라이플", 4);
				}*/
				return statList;
			}
		};
		
		/* Format
		"", "", "", Weapon_detailType., "", 80, "",
		"물공  가변", "마공  가변", "독공  가변", "힘  가변", "지능  가변",
		null,
		"", "", "", "", "", 85, "",
		"물공  가변", "마공  가변", "독공  가변", "힘  가변", "지능  가변", 
		null,
		"", "", "", "", "", "", "",
		"물공  가변", "마공  가변", "독공  가변", "힘  가변", "지능  가변", 
		null,
		"구원의 이기 - ", "", "", "", "", "", true,
		"물공  가변", "마공  가변", "독공  가변", "힘  가변", "지능  가변", 
		null,
		"", "", "", "", "", 90, false,
		"물공  가변", "마공  가변", "독공  가변", "힘  가변", "지능  가변", 
		null,
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
				/////머스켓
				"화염의 닐 스나이퍼", "", "", Weapon_detailType.GUN_MUSKET, "", 80, "",
				"물공 1065 가변", "마공 926 가변", "독공 611 가변", "힘 68 가변", "지능 101 가변",
				"화속강 24 가변", "스킬 닐 스나이핑 3", "설명 닐 스나이핑 공격시 35% 화속성 추가 데미지", "설명 구현상의 문제로 해당 수치의 스증뎀으로 적용(데미지는 정상적으로 계산됨)", null, 	//TODO 닐스구현
				"코드넘버 608", "", "", "", "", 85, "",
				"물공 1131 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 107 가변", 
				"스킬 플래시 마인 5", "스킬 M-61 마인 2", "ㄷ 증뎀 10 선택", "추뎀 22", null,
				"룰 오브 썸", "", "", "", "", "", "",
				"물공 1131 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 107 가변", 
				"물크 10", "마크 10", "스킬 G-14 파열류탄 4", "스킬 G-35L 섬광류탄 4", "스킬 G-18C 빙결류탄 4", "스킬 광자탄 4", "스킬 유탄 마스터리 1", "스킬 증명의 열쇠 1",
				"스킬 네이팜탄 4", "스킬 탄 마스터리 2", "스킬 전장의 영웅 1", "스킬 G-14 파열류탄 % 25", "스킬 G-35L 섬광류탄 % 25", "스킬 G-18C 빙결류탄 % 25", "스킬 G-38ARG 반응류탄 % 25", "스킬 G-96 열압력유탄 % 25", null,
				"구원의 이기 - 머스켓", "", "", "", "", "", true,
				"물공 1131 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 107 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"Code N : 오라클", "", "", "", "", 90, false,
				"물공 1198 가변", "마공 1042 가변", "독공 686 가변", "힘 75 가변", "지능 112 가변", 
				"물크 25", "마크 25", "스킬 유탄 마스터리 3", "스킬 전장의 영웅 3", "ㄷ 스증뎀 30", "TP스킬 1-85 1", null,
				/////리볼버
				"외톨이 잭볼버", "", "", Weapon_detailType.GUN_REVOLVER, "", 80,  "",
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
				"물공 1053 가변", "마공 886 가변", "독공 648 가변", "힘 72 가변",
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"골드 럭스", "", "", "", "", 90, false,
				"물공 1114 가변", "마공 937 가변", "독공 686 가변", "힘 75 가변", 
				"물크 2", fStat[2], "설명 레인저 1~80 레벨 모든 스킬 Lv +1(특성 스킬 제외)", "설명 장착 중인 장비 레어리티에 따른 추가데미지 효과 발생", "설명 에픽-9% / 레전더리-7% / 유니크-5% (최대 6부위)", null,
				/////보우건
				"폭풍의 역살", "", "", Weapon_detailType.GUN_BOWGUN, "", 80,  "",
				"물공 834 가변", "마공 926 가변", "독공 611 가변", "힘 68 가변", "지능 34 가변",
				"명속강 24 가변", "명속부여", "물크 3", "스킬 은탄 3", "스킬 G-35L 섬광류탄 3", "스킬 보우건 마스터리 3", "ㄷ 명속깍 15 선택", null,
				"제네럴 보우건", "", "", "", "", 85, "",
				"물공 886 가변", "마공 983 가변", "독공 648 가변", "힘 237 가변", "지능 200 가변", 
				"물크 3", "스킬 보우건 마스터리 5", "스킬 냉동탄 5", "스킬 은탄 5", "스킬 작열탄 5", "스킬 철갑탄 5", "스킬 강화탄 2", "스킬 탄 마스터리 2", "추뎀 22", "ㄷ 물공뻥 6", "ㄷ 마공뻥 6", "ㄷ 독공뻥 6", null,
				"얼음 불꽃의 보우건", "", "", "", "", "", "",
				"물공 886 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 35 가변", 
				"화속강 40 가변", "수속강 40 가변", "화속부여", "수속부여", "물크 3", "ㄷ 추뎀 15 선택", "ㄷ 추뎀 15 선택", null,
				"구원의 이기 - 보우건", "", "", "", "", "", true,
				"물공 886 가변", "마공 983 가변", "독공 648 가변", "힘 72 가변", "지능 35 가변", 
				"ㄷ 스증뎀 35", "ㄷ 스증뎀 20 선택", enableElement, "설명 자신의 가장 높은 속성강화 수치의 속성을 무기에 부여한다.", null,
				"헬 하보크", "", "", "", "", 90,  false,
				"물공 937 가변", "마공 1042 가변", "독공 686 가변", "힘 75 가변", "지능 37 가변", 
				"물크 13", "마크 10", "스킬 보우건 마스터리 5", "추크증 40", null,
				/////자동권총
				"반자동 셔플렉터", "", "", Weapon_detailType.GUN_AUTOPISTOL, "", 80,  "",
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
				
				///////////레전더리
				/////핸드캐넌
				"돌격대장의 슈퍼 캐넌", Item_rarity.LEGENDARY, "", Weapon_detailType.GUN_HCANON, "", 85, "",
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
				"스킬 슈타이어 대전차포 % 20", "설명 M-137 개틀링건 초당 발사수 2발 증가, 발사시간 1초 증가(미구현)", "설명 FM-그레네이드 런처 유탄 발사 수 2 증가(미구현)",
				"설명 증뎀량이 써든 여부랑 AJ 여부랑 그레기는 남런/여런 차이랑 새봄 여부를 다 알아야되서 걍 안함 ", null,
				"진혼의 캐넌", "", "", "", "", "", "",
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 134 가변", 
				"물크 4", "마크 4", "추뎀 11", null,
				"리버레이션 캐넌", "", "", "", "", "", "",
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 162 가변", 
				"물크 7", "마크 7", "추뎀 16", null,
				"플루의 집념", "", "", "", "", "", "",
				"물공 1164 가변", "마공 699 가변", "독공 607 가변", "힘 101 가변", 
				"ㄷ 힘 250 선택", "ㄷ 물크 25 선택", "설명 예아 아이 엠 어 장난감", null,
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
				"물크 2", "수속강 30 가변", "수속부여", "수추뎀 8", "설명 패스트 드로우, 킬 포인트 쿨타임 25% 감소", null
		};
		
		return data;
	}
}
