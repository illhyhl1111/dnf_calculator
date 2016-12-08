package dnf_infomation;

import java.util.ArrayList;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Character_type;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.ParsingException;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_calculator.FunctionStat;
import dnf_calculator.SkillStatusInfo;
import dnf_calculator.StatusList;
import dnf_class.Characters;
import dnf_class.Monster;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;
import dnf_class.SubSkill;
import dnf_class.SwitchingSkill;
import dnf_class.TPSkill;

public class SkillInfo {
	
	public static void getInfo(LinkedList<Skill> skillList, Object[] data) throws ParsingException
	{
		int i=0;
		String name=null;
		Skill_type type=null;
		String[] targets = null;
		boolean isTPSkill=false;
		boolean isSubSkill=false;
		Job job=null;
		Character_type charType=null;
		boolean jobDefined=true;
		int firstLevel=0;
		int maxLevel=0;
		int masterLevel=0;
		int interval=0;
		Element_type element=null;
		String version=null;
		Skill skill=null;
		
		SkillLevelInfo levelInfo = null;
		int skillLevel=0;
		double[] skillNum = new double[4];
		
		String[] stat=null;
		ArrayList<Double> prevStat=null;
		int statOrder=0;

		Object temp="first";
		
		try{
		while(i<data.length)
		{
			name = (String) data[i++];

			//타입
			temp = data[i++];
			if(temp instanceof Skill_type){
				type = (Skill_type) temp;
				isTPSkill=false;
				isSubSkill=false;
			}
			else if(temp.equals(""));	//이전 값 유지
			else if(temp instanceof String){
				if(((String) temp).contains("원본 - ")){
					targets = new String[]{((String)temp).substring(5)};
					isSubSkill=true;
					isTPSkill=false;
				}
				else{
					targets = ((String) temp).split(" & ");
					isTPSkill=true;
					isSubSkill=false;
				}
			}
			else throw new ParsingException(i-1, temp);
			
			//직업
			temp = data[i++];
			if(temp instanceof Job){
				job = (Job) temp;
				jobDefined=true;
			}
			else if(temp instanceof Character_type){
				charType = (Character_type) temp;
				jobDefined=false;
			}
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//시작레벨
			temp = data[i++];
			if(temp instanceof Integer) firstLevel = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//맥스레벨
			temp = data[i++];
			if(temp instanceof Integer) maxLevel = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			//마스터레벨
			temp = data[i++];
			if(temp instanceof Integer) masterLevel = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			
			//레벨구간 (TP스킬의 경우 증가폭)
			temp = data[i++];
			if(temp instanceof Integer) interval = (int) temp;
			else if(temp.equals(""));	//이전 값 유지
			else throw new ParsingException(i-1, temp);
			
			if(!isTPSkill && (type==Skill_type.DAMAGE_BUF || type==Skill_type.ACTIVE || type==Skill_type.OPTION)){
				//속성
				temp = data[i++];
				if(temp instanceof Element_type) element = (Element_type) temp;
				else if(temp.equals(""));	//이전 값 유지
				else{
					if(type!=Skill_type.OPTION) throw new ParsingException(i-1, temp);
					else i--;
				}
			}
			
			if(data[i] instanceof String && ((String)data[i]).contains("ver_"))
				version = (String) data[i++];
			else
				version = CalculatorVersion.VER_1_0_a;
			
			if(jobDefined){
				if(isTPSkill) skill = new TPSkill(name, targets, job, firstLevel, maxLevel, masterLevel, interval, version);
				else if(isSubSkill) skill = new SubSkill(name, targets[0], job, firstLevel, maxLevel, masterLevel, interval, version);
				else if(type==Skill_type.SWITCHING) skill = new SwitchingSkill(name, job, firstLevel, maxLevel, masterLevel, interval, version);
				else skill = new Skill(name, type, job, firstLevel, maxLevel, masterLevel, interval, element, version);
			}
			else{
				if(isTPSkill) skill = new TPSkill(name, targets, charType, firstLevel, maxLevel, masterLevel, interval, version);
				else if(isSubSkill) skill = new SubSkill(name, targets[0], charType, firstLevel, maxLevel, masterLevel, interval, version);
				else if(type==Skill_type.SWITCHING) skill = new SwitchingSkill(name, charType, firstLevel, maxLevel, masterLevel, interval, version);
				else skill = new Skill(name, type, charType, firstLevel, maxLevel, masterLevel, interval, element, version);
			}
			
			while(true)
			{
				if(data[i]!=null && ((String)data[i]).contains("설명")){
					temp = data[i++];
					String explanation = ((String)temp).substring(3);
					skill.explanation.add(explanation);
				}
				else break;
			}
			
			prevStat=new ArrayList<Double>();
			try{
				//스킬 수치
				skillLevel=0;
				while(true)
				{	
					temp = data[i++];
					if(temp==null) break;
					
					else if(((String)temp).contains("반복"))
					{
						i--;
						int compNum=2;
						if(levelInfo.fStat.statList.size()!=0) compNum=3;
						
						int repNum = Integer.valueOf(((String)temp).split(" ")[1]);
						int levelDiff = skill.maxLevel-skillLevel;
						int startIndex = i-compNum*levelDiff;
						
						String[] strData = new String[repNum];
						FunctionStat[] fstatData = new FunctionStat[repNum];
						for(int j=0; j<repNum; j++){
							if(!((String) data[i-compNum*repNum+compNum*j]).equals("+")) throw new ParsingException(i-1, temp); 
							if(compNum==2)
								strData[j] = (String) data[i-compNum*repNum+compNum*j+1];
							else{
								fstatData[j] = (FunctionStat) data[i-compNum*repNum+compNum*j+1];
								strData[j] = (String) data[i-compNum*repNum+compNum*j+2];
							}
						}
						
						for(int j=0; j<levelDiff; j++)
						{
							data[startIndex+j*compNum] = "+";
							if(compNum==2) data[startIndex+j*compNum+1] = strData[j%repNum];
							else{
								data[startIndex+j*compNum+1] = fstatData[j%repNum];
								data[startIndex+j*compNum+2] = strData[j%repNum];
							}
						}
						
						data[i]=null;
						i-=compNum*levelDiff;
						temp = data[i++];
					}

					stat = ((String)temp).split(" ");
					if(stat[0].equals("+")) skillLevel++;
					else if(stat[0].equals("-")) skillLevel--;
					else skillLevel = Integer.parseInt(stat[0]);
						
					if(skill.hasDamage() || skill.type==Skill_type.SWITCHING){
						if(stat.length==5){
							for(int j=0; j<4; j++)
								skillNum[j]=Parser.parseForm(stat[j+1], skillNum[j]);
						}
						else if(stat.length!=1){
							int index=1;
							for(int j=0; j<4; j++){
								if(skillNum[j]==0) continue;
								else skillNum[j]=Parser.parseForm(stat[index++], skillNum[j]);
							}
						}
						else for(int j=0; j<4; j++) skillNum[j]=0;
						levelInfo = new SkillLevelInfo(skillLevel, (int)Math.round(skillNum[0]), skillNum[1], (int)Math.round(skillNum[2]), skillNum[3]);
					}
					else levelInfo = new SkillLevelInfo(skillLevel);
					
					if(skill.hasBuff() || (data[i] instanceof String && ((String)data[i]).startsWith("귀속"))){
						temp = data[i++];
						
						if(temp instanceof FunctionStat){
							levelInfo.fStat.statList.add((FunctionStat) temp);
							temp = data[i++];
						}
						
						if(temp!=null){
							String[] statList = ((String)temp).split(" & ");
							for(String str : statList){
								if(str.isEmpty()) continue;
								if(str.contains("귀속")){
									String[] skillAndPercent = str.split("/");
									
									int result;
									if(skillAndPercent[skillAndPercent.length-1].startsWith("+"))
										result = (int) (Parser.parseForm(skillAndPercent[skillAndPercent.length-1], prevStat.get(statOrder))+0.0001);
									else result = (int) (Parser.parseForm(skillAndPercent[skillAndPercent.length-1], 0)+0.0001);
									levelInfo.percentList.put(skillAndPercent[1], result);
									if(statOrder==prevStat.size()){
										prevStat.add((double)result);
										statOrder++;
									}
									else prevStat.set(statOrder++, (double)result);
									continue;
								}
								
								stat = ((String)str).split(" ");
								if(stat[stat.length-1].startsWith("+")){
									String compareStat = stat[stat.length-1];
									stat[stat.length-1]=Double.toString(Double.valueOf(compareStat) + prevStat.get(statOrder));
								}
								double result = Parser.parseStat(stat, levelInfo.stat, levelInfo.fStat);
								if(statOrder==prevStat.size()){
									prevStat.add(result);
									statOrder++;
								}
								else prevStat.set(statOrder++, result);
							}
						}
						else i--;
					}
					skill.skillInfo.add(levelInfo);
					statOrder=0;
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
				
			skillList.add(skill);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("앞1 : "+data[i-2]);
			System.out.println("앞2 : "+data[i-3]);
			System.out.println("뒤1 : "+data[i]);
			System.out.println("뒤2 : "+data[i+1]);
			throw new ParsingException(i-1, temp);
		}
	}
	
	public static Object[] skillInfo_swordman()
	{
		FunctionStat fStat[] = new FunctionStat[2];
		
		//혈지군무 분신
		fStat[0] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				int level = character.characterInfoList.getSkill("혈지군무").getSkillLevel(true, character.isBurning());
				if(level!=0) skill.setSkillLevel(level);
				double skillEnhance=0;
				SkillLevelInfo skillInfo = character.characterInfoList.getSkill("마인의 검세").getSkillLevelInfo(true, character.isBurning());
				if(character.characterInfoList.getSkill("마인의 검세").getSkillLevel(true, character.isBurning())!=0){
					switch(skill.getName()){
					case "혈지군무 - 발":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(4).stat).getIncrease();
						break;
					case "혈지군무 - 무":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(5).stat).getIncrease();
						break;
					case "혈지군무 - 사형조수":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(6).stat).getIncrease();
						break;
					case "혈지군무 - 폭류나선":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(0).stat).getIncrease();
						break;
					case "혈지군무 - 혈화난무":
						skillEnhance = ((SkillStatusInfo)skillInfo.stat.statList.get(1).stat).getIncrease();
						break;
					}
				}
				skill.dungeonIncrease *= 100.0/(100+skillEnhance);
				return statList;
			}
		};
		
		Object[] data = new Object[] {
				
				///////////////검마
				"사복검 - 발", Skill_type.ACTIVE, Job.DEMONSLAYER, 15, 60, 50, 2, Element_type.NONE, 
				"38 1298*10 0 0 0", "+ 1325*10", "+ 1298*10","+ 1353*10","+ 1380*10","+ 1409*10","+ 1437*10","+ 1464*10",null,
				"사복검 - 무", Skill_type.ACTIVE, Job.DEMONSLAYER, 20, 60, 50, 2, Element_type.NONE, 
				"36 2750*2+3666 0 0 0", "+ 2811*2+3748" , "+ 2874*2+3831", "+ 2932*2+3911", "+ 2998*2+3997", "+ 3059*2+4078", "+ 3117*2+4158", "+ 3181*2+4241", null,
				"사복검 - 조", Skill_type.ACTIVE, Job.DEMONSLAYER, 25, 60, 50, 2, Element_type.NONE, 
				"33 6098+3050*3 0 0 0", "+ 6245+3122*3", "+ 6391+3196*3", "+ 6536+3269*3", "+ 6683+3340*3", "+ 6828+3414*3", "+ 6975+3488*3", "+ 7120+3561*3", "+ 7266+3632*3", null,
				//충격파 물리공격력은 직접타격엔 영향없고 독오 퍼뎀은 뜯기와 같음 (안맞은적만 적용)
				"사형조수", Skill_type.ACTIVE, Job.DEMONSLAYER, 25, 60, 50, 2, Element_type.NONE, 
				"33 5323+7987 0 0 0", "+ 5453+8178", "+ 5579+8370", "+ 5708+8564", "+ 5831+8749", "+ 5964+8944", "+ 6090+9133", "+ 6219+9329", "+ 6345+9517", null,
				"폭류나선", Skill_type.ACTIVE, Job.DEMONSLAYER, 30, 60, 50, 2, Element_type.NONE, 
				"31 1456*12 0 0 0", "+ 1491*12", "+ 1530*12", "+ 1564*12", "+ 1603*12", "+ 1639*12", "+ 1674*12", "+ 1712*12", "+ 1750*12", "+ 1785*12", "+ 1822*12", null,
				"혈화난무", Skill_type.ACTIVE, Job.DEMONSLAYER, 35, 60, 50, 2, Element_type.NONE, 
				"28 2092*11+5451 0 0 0", "+ 2150*11+5596", "+ 2206*11+5739", "+ 2264*11+5886", "+ 2320*11+6031", "+ 2378*11+6175",
				"+ 2435*11+6325", "+ 2492*11+6469", "+ 2549*11+6613", "+ 2604*11+6760", "+ 2663*11+6908",  "+ 2720*11+7050", null,
				"혈마인", Skill_type.ACTIVE, Job.DEMONSLAYER, 35, 60, 50, 2, Element_type.NONE, 
				"28 6091+14011+1942*16 0 0 0", "+ 6256+14391+1994*16", "+ 6256+14391+1994*16", "+ 6422+14771+2048*16", "+ 6587+15151+2099*16", "+ 6752+15531+2153*16", "+ 6917+15911+2205*16",
				"+ 7083+16291+2258*16", "+ 7248+16671+2311*16", "+ 7413+17052+2363*16", "+ 7578+17432+2416*16", "+ 7744+17812+2469*16", "+ 7909+18192+2521*16", null,
				"검마격살", Skill_type.ACTIVE, Job.DEMONSLAYER, 45, 60, 50, 2, Element_type.NONE, 
				"23 6693+17849+2232*8 0 0 0", "+ 6905+18413+2302*8", "+ 7106+18950+2369*8", "+ 7318+19515+2439*8", "+ 7529+20079+2510*8", "+ 7742+20644+2580*8",
				"+ 7953+21209+2652*8", "+ 8165+21773+2722*8", "+ 8376+22337+2792*8", null,
				"암연검 : 기가블레이드", Skill_type.ACTIVE, Job.DEMONSLAYER, 50, 40, 30, 5, Element_type.NONE, 
				"10 2867*20+2648*7+2042*7+11469+17203+22943 0 0 0", "+ 3082*20+2845*7+2159*7+12332+18496+24664 0 0 0", "+ 3297*20+3044*7+2280*7+13192+19790+26387 0 0 0", "+ 3514*20+3242*7+2280*7+14053+21082+28113 0 0 0",
				"+ 3729*20+3442*7+2403*7+14916+22374+29834 0 0 0", "+ 3945*20+3642*7+2523*7+15777+23666+31557 0 0 0", "9 2652*20+2451*7+1925*7+10606+15910+21222 0 0 0", null,
				"포식자 갈로아", Skill_type.ACTIVE, Job.DEMONSLAYER, 60, 40, 30, 2, Element_type.NONE, 
				"16 3239*12+5567*3 0 0 0", "+ 3420*12+5789*3", "+ 3553*12+6013*3", "+ 3685*12+6238*3", "+ 3818*12+6462*3", "+ 3950*12+6686*3",
				"+ 4081*12+6910*3", "+ 4214*12+7133*3", null,
				"역천의 프놈", Skill_type.ACTIVE, Job.DEMONSLAYER, 70, 40, 30, 2, Element_type.NONE, 
				"11 2873*5+2393*8+14361 0 0 0", "+ 3018*5+2513*8+15084", "+ 3162*5+2634*8+15808", "+ 3308*5+2754*8+16531", "+ 3453*5+2876*8+17254",
				"+ 3596*5+2996*8+17978", "+ 3742*5+3115*8+18701", "+ 3885*5+3237*8+19425", null,
				"암연격 : 기가슬래쉬", Skill_type.ACTIVE, Job.DEMONSLAYER, 75, 40, 30, 2, Element_type.NONE, 
				"8 64069 0 0 0", "+ 67870", "+ 71671", "+ 75472", "+ 79272", "+ 83073", "+ 86874", null,
				"비인외도 : 극", Skill_type.ACTIVE, Job.DEMONSLAYER, 80, 40, 30, 2, Element_type.NONE, 
				"6 64089 0 0 0", "+ 68402", "+ 72719", "+ 77032", "+ 81345", "+ 85659", null,
				"파계검 : 라그나로크", Skill_type.ACTIVE, Job.DEMONSLAYER, 85, 40, 30, 5, Element_type.NONE, 
				"2 3063*10+11336*3+42325 0 0 0", "+ 3640*10+13471*3+50293", "+ 4217*10+15604*3+58260", "+ 4794*10+17739*3+66228", "+ 5370*10+19874*3+74195", null,

				"탐욕의 번제", Skill_type.BUF_ACTIVE, "", 50, 40, 30, 3,
				"15", "증뎀버프 33", "+", "증뎀버프 +1.5", "반복 1",
				"마인의 검세", Skill_type.PASSIVE, "", 75, 40, 30, 3,
				"5", "스킬 폭류나선 % 32 & 스킬 혈화난무 % 48*0.596 & 스킬 사복검 - 조 % 32*0.6 & 스킬 검마격살 % 50*0.421+48*0.421 & 스킬 사복검 - 발 % 19 & 스킬 사복검 - 무 % 32 & 스킬 사형조수 % 32 & 스킬 암연검 : 기가블레이드 % 32 & "
						+ "스킬 포식자 갈로아 % 32 & 스킬 역천의 프놈 % 32 & 스킬 암연격 : 기가슬래쉬 % 32 & 스킬 비인외도 : 극 % 32 & 스킬 파계검 : 라그나로크 % 32 & 스킬 혈지군무 % 32",
				"6", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 50*0.421+50*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"7", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 62.5*0.421+52*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & " 
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"8", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 62.5*0.421+54*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"9", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 62.5*0.421+56*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"10", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 75*0.421+58*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"11", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 75*0.421+60*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"12", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 75*0.421+62*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2",
				"13", "스킬 폭류나선 % +2 & 스킬 혈화난무 % +1.192 & 스킬 사복검 - 조 % 1.2 & 스킬 검마격살 % 87.5*0.421+64*0.421 & 스킬 사복검 - 발 % +2 & 스킬 사복검 - 무 % +2 & 스킬 사형조수 % +2 & 스킬 암연검 : 기가블레이드 % +2 & "
						+ "스킬 포식자 갈로아 % +2 & 스킬 역천의 프놈 % +2 & 스킬 암연격 : 기가슬래쉬 % +2 & 스킬 비인외도 : 극 % +2 & 스킬 파계검 : 라그나로크 % +2 & 스킬 혈지군무 % +2", null,
									
				"광폭화", Skill_type.SWITCHING, "", 20, 30, 20, 3, 
				"20", "증뎀버프 40 & 스킬 혈마인 % 30 & 스킬 혈지군무 % 30 & 스킬 검마격살 % 30 & 스킬 포식자 갈로아 % 30 & 스킬 역천의 프놈 % 30 & 스킬 암연검 : 기가블레이드 % 30 & 스킬 암연격 : 기가슬래쉬 % 30 & 스킬 파계검 : 라그나로크 % 30", null,
				"사복검 - 강", Skill_type.SWITCHING, "", 30, 20, 10, 3, 
				"10", "스킬 사복검 - 발 % 25.7 & 스킬 사복검 - 조 % 25.7 & 스킬 사복검 - 무 % 25.7 & 스킬 사형조수 % 25.7 & 스킬 폭류나선 % 25.7 & 스킬 혈화난무 % 25.7 & 스킬 비인외도 : 극 % 25.7 & 스킬 파계검 : 라그나로크 % 25.7", null,

				"혈지군무", Skill_type.ACTIVE, "", 40, 30, 20, 3, Element_type.NONE,
				"17 0 0 0 0", "귀속/혈지군무 - 발/100 & 귀속/혈지군무 - 무/100 & 귀속/혈지군무 - 사형조수/100 & 귀속/혈지군무 - 폭류나선/100 & 귀속/혈지군무 - 혈화난무/100",
				"+", "귀속/혈지군무 - 발/100 & 귀속/혈지군무 - 무/100 & 귀속/혈지군무 - 사형조수/100 & 귀속/혈지군무 - 폭류나선/100 & 귀속/혈지군무 - 혈화난무/100", "반복 1",
				"혈지군무 - 발", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사복검 - 발 개체를 붙입니다",
				"17", fStat[0], "귀속/사복검 - 발/260", "+", fStat[0], "귀속/사복검 - 발/+4", "반복 1",
				"혈지군무 - 무", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사복검 - 무 개체를 붙입니다",
				"17", fStat[0], "귀속/사복검 - 무/236", "+", fStat[0], "귀속/사복검 - 무/240", "+", fStat[0], "귀속/사복검 - 무/244", "+", fStat[0], "귀속/사복검 - 무/247", "+", fStat[0], "귀속/사복검 - 무/251", 
				"+", fStat[0], "귀속/사복검 - 무/254", "+", fStat[0], "귀속/사복검 - 무/258", "+", fStat[0], "귀속/사복검 - 무/262", "+", fStat[0], "귀속/사복검 - 무/266", "+", fStat[0], "귀속/사복검 - 무/269", "반복 3",
				"혈지군무 - 사형조수", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 사형조수 개체를 붙입니다",
				"17", fStat[0], "귀속/사형조수/234", "+", fStat[0], "귀속/사형조수/238", "+", fStat[0], "귀속/사형조수/242", "+", fStat[0], "귀속/사형조수/245", "+", fStat[0], "귀속/사형조수/249", 
				"+", fStat[0], "귀속/사형조수/252", "+", fStat[0], "귀속/사형조수/256", "+", fStat[0], "귀속/사형조수/259", "+", fStat[0], "귀속/사형조수/263", "+", fStat[0], "귀속/사형조수/266", "반복 3",
				"혈지군무 - 폭류나선", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 폭류나선 개체를 붙입니다",
				"17", fStat[0], "귀속/폭류나선/130", "+", fStat[0], "귀속/폭류나선/+2", "반복 1",  
				"혈지군무 - 혈화난무", Skill_type.OPTION, "", 40, 30, 20, 3, "설명 몬스터에 혈화난무 개체를 붙입니다",
				"17", fStat[0], "귀속/혈화난무/65", "+", fStat[0], "귀속/혈화난무/+1", "반복 1",
				
				"사복검 강화", "사복검 - 발 & 사복검 - 조 & 사복검 - 무", "", 55, 7, 5, 11, CalculatorVersion.VER_1_0_e, null, 
				"폭류나선 강화", "폭류나선", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"사형조수 강화", "사형조수", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"혈마인 강화", "혈마인", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"혈화난무 강화", "혈화난무", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"검마격살 강화", "검마격살", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
		};
		return data;
	}
	
	public static Object[] skillInfo_gunner()
	{
		FunctionStat fStat[] = new FunctionStat[20];
		
		//듀얼트리거
		fStat[0] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				try {
					int fire = (int)Math.round(character.dungeonStatus.getStat(StatList.ELEM_FIRE));
					int light = (int)Math.round(character.dungeonStatus.getStat(StatList.ELEM_LIGHT));
					if(fire>light) statList.addStatList(Element_type.LIGHT, fire-light, false, false, false);
					else statList.addStatList(Element_type.FIRE, light-fire, false, false, false);
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			}
		};
		
		fStat[1] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				try {
					statList.addStatList("재련독공", character.dungeonStatus.getStat("재련독공")
							*skill.getSkillLevelInfo(true, character.isBurning()).stat.statList.get(0).stat.getStatToDouble()/100);
				} catch (UndefinedStatusKey | StatusTypeMismatch e) {
					e.printStackTrace();
				}
				return statList;
			}
		};
		
		//데바리
		fStat[2] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			double save;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Skill skill = (Skill)item;
				try {
					if(character.getItemSetting().weapon.weaponType!=Weapon_detailType.GUN_REVOLVER){
						double temp;
						temp = skill.skillInfo.getLast().stat.statList.getFirst().stat.getStatToDouble();
						if(temp!=0) save=temp;
						
						skill.skillInfo.getLast().stat.statList.getFirst().stat.setInfo(0.0);
					} 
					
					else{
						if(skill.skillInfo.getLast().stat.statList.getFirst().stat.getStatToDouble()==0)
							skill.skillInfo.getLast().stat.statList.getFirst().stat.setInfo(save);
					}
				}
				catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
				
				return statList;
			}
		};
		
		//강화탄
		fStat[4] = new FunctionStat(){
			private static final long serialVersionUID = 1L;

			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Skill skill = (Skill)item;
				StatusList statList = new StatusList();
				if(character.characterInfoList.getSkill("강화탄(무)").buffEnabled(true)){
					statList.addStatList("%물방깍_스킬", skill.getSkillLevel(true, character.isBurning()));
					statList.addStatList("%마방깍_스킬", skill.getSkillLevel(true, character.isBurning()));
				}
				else if(character.characterInfoList.getSkill("강화탄(화)").buffEnabled(true))
					statList.addStatList("화속깍", skill.getSkillLevel(true, character.isBurning())*2+2);
				else if(character.characterInfoList.getSkill("강화탄(수)").buffEnabled(true))
					statList.addStatList("수속깍", skill.getSkillLevel(true, character.isBurning())*2+2);
				else if(character.characterInfoList.getSkill("강화탄(명)").buffEnabled(true))
					statList.addStatList("명속깍", skill.getSkillLevel(true, character.isBurning())*2+2);
					
				if(character.characterInfoList.getSkill("강화탄(컨버전)").buffEnabled(true)){
					statList.addStatList("%물방깍_스킬", -100000);
					statList.addStatList("물리방무뻥", -100);
				}
				else{
					statList.addStatList("%마방깍_스킬", -100000);
					statList.addStatList("마법방무뻥", -100);
				}
				return statList;
			}
		};
		
		//병기숙련
		fStat[5] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Skill skill = (Skill)item;
				StatusList statList = new StatusList();
				if(character.getItemSetting().weapon.weaponType==Weapon_detailType.GUN_BOWGUN
						|| character.getItemSetting().weapon.weaponType==Weapon_detailType.GUN_MUSKET){
					int level = skill.getSkillLevel(true, character.isBurning());
					statList.addStatList("물리마스터리", level);
					statList.addStatList("마법마스터리", level);
					statList.addStatList("독공마스터리", level);
					statList.addStatList("물리방무뻥", level);
					statList.addStatList("마법방무뻥", level);
				}
					
				return statList;
			}
		};
		
		//매거진
		fStat[6] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				StatusList statList = new StatusList();
				Skill hit = character.characterInfoList.getSkill("평타(1사이클)");
				switch(character.getItemSetting().weapon.weaponType)
				{
				case GUN_BOWGUN:
					hit.setSkillLevel(21);
					hit.dungeonLevel=0;
					break;
				case GUN_MUSKET:
					hit.setSkillLevel(10);
					hit.dungeonLevel=0;
					break;
				case GUN_REVOLVER:
					hit.setSkillLevel(12);
					hit.dungeonLevel=0;
					break;
				case GUN_HCANON:
					hit.setSkillLevel(6);
					hit.dungeonLevel=0;
					break;
				case GUN_AUTOPISTOL:
					hit.setSkillLevel(18);
					hit.dungeonLevel=0;
					break;
				default:
					break;
				}
				return statList;
			}
		};
		
		//기숙
		fStat[7] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Skill skill = (Skill)item;
				if(character.hasContract()) skill.setSkillLevel(character.getLevel());
				return new StatusList();
			}
		};
		
		Object[] data = new Object[] {
				
				///////////////런처
				//캐넌볼
				"캐넌볼", Skill_type.ACTIVE, Job.LAUNCHER_F, 20, 60, 50, 2, Element_type.NONE,
				"36 3964*2 0 0 0", "+ 4052*2", "+ 4138*2", "+ 4225*2", "+ 4313*2", "+ 4398*2", null,
				//슈타
				"슈타이어 대전차포", "", "", 20, 60, 50, 2, Element_type.FIRE,
				"36 266+1592*3 2.659+15.92*3 0 0", "+ 271+1627*3 2.71+16.27*3", "+ 277+1663*3 2.77+16.63*3",
				"+ 284+1698*3 2.84+16.98*3", "+ 289+1735*3 2.889+17.35*3", "+ 295+1770*3 2.949+17.699*3", "+ 301+1806*3 3.010+18.060*3", null,
				//레이저
				"레이저 라이플", "", "", 25, 60, 50, 2, Element_type.LIGHT,
				"33 3876 25.83 0 0", "+ 3969 26.449", "+ 4060 27.08", "+ 4154 27.689", "+ 4246 28.310", "+ 4338 28.93", "+ 4431 29.539", null,
				//화강
				"화염 강타", "", "", 30, 60, 50, 2, Element_type.FIRE,
				"31 320*35 3.2*35 0 0", "+ 328*35 3.28*35", "+ 336*35 3.36*35", "+ 344*35 3.44*35", "+ 353*35 3.53*35", 
				"+ 361*35 3.61*35", "+ 369*35 3.69*35", "+ 377*35 3.77*35", "+ 385*35 3.85*35", null,
				//그레기
				"FM-31 그레네이드 런처", "", "", 35, 60, 50, 2, "",
				"28 1487*8 9.92*8 0 0", "+ 1527*8 10.19*8", "+ 1568*8 10.469*8", "+ 1609*8 10.719*8", "+ 1649*8 11*8",
				"+ 1690*8 11.28*8", "+ 1730*8 11.53*8", "+ 1773*8 11.82*8", "+ 1811*8 12.059*8", null,
				//랜서
				"FM-92 mk2 랜서", "", "", "", "", "", "", "",
				"28 1655*8 14.059*8 0 0", "+ 1700*8 14.45*8", "+ 1745*8 14.84*8", "+ 1787*8 15.19*8", "+ 1831*8 15.58*8",
				"+ 1876*8 15.95*8", "+ 1918*8 16.32*8", "+ 1964*8 16.71*8", "+ 2009*8 17.05*8", null,
				//양자
				"양자 폭탄", "", "", 40, "", "", "", Element_type.LIGHT,
				"26 838+10778 8.379+107.78 0 0", "+ 841+11091 8.41+110.909", "+ 842+11405 8.42+114.049", "+ 844+11715 8.44+117.149",
				"+ 845+12025 8.45+120.250", "+ 847+12335 8.469+123.350", "+ 848+12646 8.48+126.460", "+ 851+12956 8.51+129.560", null,
				//익스
				"X-1 익스트루더", "", "", 45, "", "", "", Element_type.NONE,
				"23 18229 182.29 0 0", "+ 18801 188.010", "+ 19374 193.739", "+ 19945 199.450", "+ 20517 205.170", "+ 21090 210.900",
				"+ 21662 216.620", "+ 22234 222.340", "+ 22807 228.069", "+ 23379 233.790", null,
				//엔션
				"에인션트 트리거", "", "", 50, 40, 30, 5, "", "설명 7초짜리 신의가호",
				"11 273*16+710+2841*11 2.73*16+7.1+28.41*11 0 0", "+ 292*16+757+3028*11 2.92*16+7.569+30.279*11", "+ 311*16+805+3218*11 3.11*16+8.049+32.180*11",
				"+ 330*16+852+3408*11 3.30*16+8.520+34.079*11", "+ 350*16+900+3597*11 3.50*16+9.000+35.970*11", "+ 369*16+947+3785*11 3.69*16+9.470+37.850*11", 
				"10 254*16+663+2654*11 2.54*16+6.63+26.54*11", null,
				//팜페로
				"팜페로 부스터", "", "", 60, "", "", 2, Element_type.WATER,
				"16 504*31 5.04*31 0 0", "+ 524*31 5.24*31", "+ 545*31 5.45*31", "+ 564*31 5.64*31", "+ 586*31 5.859*31", "+ 605*31 6.049*31", null,
				//특랜
				"FM-92 mk2 랜서 SW", "", "", 70, 40, 30, 2, Element_type.FIRE,
				"11 2832*8 28.32*8 0 0", "+ 2974*8 29.74*8", "+ 3116*8 31.16*8",
				"14 3259*8 32.59*8 0 0", "+ 3399*8 33.989*8", "+ 3544*8 35.440*8", "+ 3685*8 36.850*8", null,
				//공기포
				"PT-15 프로토타입", "", "", 80, 40, 30, 2, Element_type.NONE,
				"6 20942+8976 209.42+89.759 0 0", "+ 22352+9579 223.519+95.79",
				"+ 23762+10184 237.620+101.840", "+ 25172+10788 251.720+107.880",
				"+ 26580+11393 265.800+113.930", "+ 27990+11995 279.900+119.950", "+ 29400+12600 294.000+126.000", "+ 30811+13204 308.110+132.040", null,
				//오퍼
				"오퍼레이션 레이즈", "", "", 85, 40, 30, 5, Element_type.FIRE, "설명 진누골 기준 정타 타격",
				"4 1.794*37973 1.794*379.73 0 0", "3 1.794*32783 1.794*327.83", "5 1.794*43166 1.794*431.66", "6 1.794*48258 1.794*482.58",
				"7 1.794*53552 1.794*535.52", null,
				//개틀
				"M-137 개틀링건", Skill_type.ACTIVE, Character_type.GUNNER_F, 5, 60, 50, 2, Element_type.NONE,
				"42 223*20 1.859*20 0 0", "+ 228*20 1.900*20", "+ 232*20 1.940*20", "+ 237*20 1.970*20", "+ 241*20 2.010*20", "+ 246*20 2.06*20", "+ 251*20 2.09*20", null,
				//바베큐
				"바베~큐", "", "", 10, 60, 50, 2, "",
				"41 7200 72 0 0", "+ 7350 73.5", "+ 7480 74.8", "+ 7630 76.29", "+ 7790 77.9", null,
				//화방
				"M-3 화염방사기", "", "", 15, 60, 50, 2, Element_type.FIRE,
				"38 446*13 3.720*13 0 0", "+ 456*13 3.8*13", "+ 466*13 3.89*13", "+ 476*13 3.96*13", "+ 488*13 4.06*13", "+ 498*13 4.149*13", null,
				"기본기 숙련 강화", "기본기 숙련", Character_type.ALL, 50, 5, 3, 10, CalculatorVersion.VER_1_0_d, null,
				
				///////패시브
				//중화기다루기
				"중화기 다루기", Skill_type.PASSIVE, Job.LAUNCHER_F, 15, 1, 1, 3,
				"1", "스킬 M-137 개틀링건 1 & 스킬 바베~큐 1 & 스킬 M-3 화염방사기 1 & 스킬 슈타이어 대전차포 1 & 스킬 레이저 라이플 1 & 스킬 화염 강타 1 & 스킬 FM-31 그레네이드 런처 1 & 스킬 FM-92 mk2 랜서 1 & 스킬 캐넌볼 1 & "
				+ "스킬 양자 폭탄 1 & 스킬 X-1 익스트루더 1 & 스킬 에인션트 트리거 1 & 스킬 팜페로 부스터 1 & 스킬 FM-92 mk2 랜서 SW 1 & 스킬 PT-15 프로토타입 1 & 스킬 오퍼레이션 레이즈 1", null,
				//중화기 마스터리
				"중화기 마스터리", Skill_type.PASSIVE, "", 15, 20, 10, 3,
				"10", "무기마스터리 10 핸드캐넌 & 증뎀버프 20", "+", "무기마스터리 11 핸드캐넌 & 증뎀버프 22", "+", "무기마스터리 12 핸드캐넌 & 증뎀버프 24", "+", "무기마스터리 13 핸드캐넌 & 증뎀버프 26", 
				"+", "무기마스터리 14 핸드캐넌 & 증뎀버프 28", "+", "무기마스터리 15 핸드캐넌 & 증뎀버프 30", "+", "무기마스터리 16 핸드캐넌 & 증뎀버프 32", "+", "무기마스터리 17 핸드캐넌 & 증뎀버프 34",
				"+", "무기마스터리 18 핸드캐넌 & 증뎀버프 36", "+", "무기마스터리 19 핸드캐넌 & 증뎀버프 38", "+", "무기마스터리 20 핸드캐넌 & 증뎀버프 40", null,
				//옵힛
				"오버 히트", "", "", 48, 40, 30, 3, 
				"14", "증뎀버프 33", "15", "증뎀버프 34", "+", "증뎀버프 +2", "+", "증뎀버프 +1", "반복 2",
				//알파서폿
				"알파 서포트", "", "", 75, 40, 30, 3,
				"5", "물리마스터리 22 & 독공마스터리 22 & 물리방무뻥 22", "6", "물리마스터리 24 & 독공마스터리 24 & 물리방무뻥 24", "+", "물리마스터리 +2 & 독공마스터리 +2 & 물리방무뻥 +2", "반복 1",
				//애자파츠
				"AJ 강화파츠", "", "", "", "", "", "",
				"6", "스킬 M-3 화염방사기 % 61.18 & 스킬 화염 강타 % 28.74 & 스킬 팜페로 부스터 % 60.29 & 스킬 M-137 개틀링건 % 69.35 & 스킬 바베~큐 % 36 & 스킬 슈타이어 대전차포 % 36 & 스킬 FM-31 그레네이드 런처 % 36 & "
				+ "스킬 FM-92 mk2 랜서 % 36 & 스킬 FM-92 mk2 랜서 SW % 36 & 스킬 양자 폭탄 % 36 & 스킬 에인션트 트리거 % 36 & 스킬 PT-15 프로토타입 % 36 & 스킬 오퍼레이션 레이즈 % 36 & "
				+ "스킬 레이저 라이플 % 36 & 스킬 X-1 익스트루더 % 36",
				"+", "스킬 M-3 화염방사기 % +2.16 & 스킬 화염 강타 % +1.66 & 스킬 팜페로 부스터 % +2.16 & 스킬 M-137 개틀링건 % +2.15 & 스킬 바베~큐 % +2 & 스킬 슈타이어 대전차포 % +2 & 스킬 FM-31 그레네이드 런처 % +2 & "
				+ "스킬 FM-92 mk2 랜서 % +2 & 스킬 FM-92 mk2 랜서 SW % +2 & 스킬 양자 폭탄 % +2 & 스킬 에인션트 트리거 % +2 & 스킬 PT-15 프로토타입 % +2 & 스킬 오퍼레이션 레이즈 % +2 & "
				+ "스킬 레이저 라이플 % +6 & 스킬 X-1 익스트루더 % +6", "반복 1",
				//충레라
				"충전 레이저 라이플", "", "", 25, 11, 1, 2,
				"1", "스킬 레이저 라이플 % 60", "+", "스킬 레이저 라이플 % +10", "+", "스킬 레이저 라이플 % +5", "반복 2", 
				//미비
				"미라클 비전", Skill_type.SWITCHING, "", 30, 20, 10, 3,
				"10", "증뎀버프 41", null,
				//듀얼트리거
				"듀얼 트리거", Skill_type.PASSIVE, "", 35, 1, 1, 3, "설명 명, 화속성 중 높은 속성 강화 값으로 낮은 값이 상승한다.",
				"1", fStat[0], null,
				
				////////TP
				"M-137 개틀링건 강화", "M-137 개틀링건", "", 50, 7, 5, 8, null,
				"바베~큐 강화", "바베~큐", "", 50, 3, 1, 20, null,
				"M-3 화염방사기 강화", "M-3 화염방사기", "", 50, 7, 5, 8, "설명 AJ강화파츠로 인해 실제 증가율은 조금 더 높습니다", null,
				"슈타이어 대전차포 강화", "슈타이어 대전차포", "", 55, 7, 5, 10, null,
				"캐넌볼 강화", "캐넌볼", "", 50, 7, 5, 10, null,
				"레이저 라이플 강화", "레이저 라이플", "", 50, 7, 5, 10, null,
				"화염 강타 강화", "화염 강타", "", 50, 7, 5, 10, null,
				"FM-92 mk2 랜서 강화", "FM-92 mk2 랜서", "", 65, 7, 5, 10, null,
				"양자 폭탄 강화", "양자 폭탄", "", 65, 7, 5, 10, null,
				"X-1 익스트루더 강화", "X-1 익스트루더", "", 65, 7, 5, 10, null,
				"FM-31 그레네이드 런처 강화", "FM-31 그레네이드 런처", "", 50, 1, 1, 20, null,
				
				//////////////////레인저
				/////액티브
				"은탄", Skill_type.ACTIVE, Job.RANGER_F, 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e, "설명 한 탄창의 딜량입니다",
				"43 697*25 0 0 0", "귀속/은탄(언악정)/100", "+ 711*25", "귀속/은탄(언악정)/100", "+ 725*25", "귀속/은탄(언악정)/100", "+ 739*25", "귀속/은탄(언악정)/100", "+ 754*25 ", "귀속/은탄(언악정)/100",
				"+ 768*25", "귀속/은탄(언악정)/100", "+ 782*25", "귀속/은탄(언악정)/100", "+ 796*25", "귀속/은탄(언악정)/100", "+ 810*25", "귀속/은탄(언악정)/100", "+ 824*25", "귀속/은탄(언악정)/100",
				"+ 838*25", "귀속/은탄(언악정)/100", "+ 853*25", "귀속/은탄(언악정)/100", null,
				"은탄(언악정)", Skill_type.OPTION, "", 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e, "설명 언데드, 악마, 정령 타입 몬스터 공격 시 추가",
				"43 174*25 0 0 0", "", "+ 178*25", "", "+ 181*25", "", "+ 185*25", "", "+ 189*25", "", "+ 192*25", "", "+ 196*25", "", "+ 199*25", "", "+ 203*25", "", "+ 206*25", "", "+ 210*25", "", "+ 213*25", null,
				"헤드샷", Skill_type.ACTIVE, Job.RANGER_F, 15, 60, 50, 2, Element_type.NONE, 
				"38 3323 0 0 0", "+ 3394 ", "+ 3464", "+ 3535", "+ 3606 ", "+ 3677", "+ 3748 ", null,
				"웨스턴 파이어", Skill_type.ACTIVE, Job.RANGER_F, 30, 1, 1, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"1 0 0 0 0", "귀속/헤드샷/120", null,
				"권총의 춤", Skill_type.ACTIVE, Job.RANGER_F, 35, 60, 50, 2, Element_type.NONE,
				"28 654*20+1163*8 0 0 0", "+ 672*20+1194*8 ", "+ 689*20+1226*8", "+ 707*20+1257*8", "+ 725*20+1289*8 ", "+ 743*20+1321*8", "+ 760*20+1352*8", "+ 778*20+1384*8", "+ 796*20+1415*8", null,
				"이동사격", Skill_type.ACTIVE, Job.RANGER_F, 35, 60, 50, 2, Element_type.NONE,
				"28 1238*30 0 0 0", "+ 1271*30 ", "+ 1305*30", "+ 1338*30", "+ 1372*30 ", "+ 1406*30", "+ 1439*30 ", "+ 1473*30 ", "+ 1506*30", null,
				"멀티 헤드샷", Skill_type.ACTIVE, Job.RANGER_F, 40, 60, 50, 2, Element_type.NONE,
				"26 4752*5 0 0 0", "+ 4888*5 ", "+ 5025*5", "+ 5161*5", "+ 5297*5 ", "+ 5434*5", "+ 5570*5 ", "+ 5706*5 ", "+ 5843*5",null,
				"더블 건호크", Skill_type.ACTIVE, Job.RANGER_F, 45, 60, 50, 2, Element_type.NONE,
				"23 1050*14+1066*18+1155*30 0 0 0", "+ 1083*14+1100*18+1191*30", "+ 1116*14+1133*18+1228*30","+ 1149*14+1167*18+1264*30","+ 1182*14+1200*18+1300*30","+ 1215*14+1234*18+1337*30",
				"+ 1248*14+1267*18+1373*30","+ 1281*14+1301*18+1409*30","+ 1314*14+1334*18+1445*30", null,
				"블러디 카니발", Skill_type.ACTIVE, Job.RANGER_F, 50, 40, 30, 5, Element_type.NONE, 
				"10 4479*24+13286 0 0 0", "+ 4815*24+14284", "+ 5152*24+15282", "+ 5488*24+16280", "+ 5825*24+17278 ", "+ 6161*24+18276", "+ 6497*24+19274", null,
				"블러디 스파이크", Skill_type.ACTIVE, Job.RANGER_F, 60, 60, 50, 2, Element_type.NONE, 
				"16 2761*5+13806 0 0 0", "+ 2872*5+14362 ", "+ 2983*5+14917 ","+ 3094*5+15473 ","+ 3205*5+16028 ","+ 3316*5+16584 ","+ 3427*5+17139 ","+ 3539*5+17695",null,
				"제압 사격", Skill_type.ACTIVE, Job.RANGER_F, 70, 40, 30, 2, Element_type.NONE,
				"11 1906*19+4237 0 0 0", "+ 2002*19+4450","+ 2098*19+4663","+ 2194*19+4877","+ 2290*19+5090","+ 2386*19+5304","+ 2482*19+5517",
				"+ 2578*19+5730","+ 2674*19+5944","+ 2770*19+6157", null,
				"블러드 앤 체인", Skill_type.ACTIVE, Job.RANGER_F, 85, 40, 30, 5, Element_type.NONE, 
				"2 92547 0 0 0", "+ 109968 ", "+ 127388","+ 144809", null,
				"소닉 스파이크", Skill_type.ACTIVE, Job.RANGER_F, 30, 60, 50, 2, Element_type.NONE, 
				"25 3185*3 0 0 0", "+ 3265*3 ", "+ 3345*3", "+ 3425*3", "+ 3505*3 ", "+ 3585*3", "+ 3665*3 ", "+ 3745*3 ", "+ 3825*3",null,
				
				/////패시브
				"베일드 컷", Skill_type.DAMAGE_BUF, Job.RANGER_F, 48, 60, 50, 3, Element_type.NONE, 
				"15 2240 0 0 0", "증뎀버프 33","+ 2330", "증뎀버프 35","+ 2419", "증뎀버프 37","+ 2509", "증뎀버프 39","+ 2598", "증뎀버프 41","+ 2688", "증뎀버프 43", null,
				"킬 포인트", Skill_type.DAMAGE_BUF, Job.RANGER_F, 75, 40, 30, 3, Element_type.NONE, "설명 만크리를 가정한 공격력입니다",
				"6 6105*3*1.14+18317 0 0 0", "크증버프 14 & 스킬 킬 포인트 % -14/1.14","+ 6647*3*1.16+19942", "크증버프 16 & 스킬 킬 포인트 % -16/1.16","+ 7198*3*1.18+21567", "크증버프 18 & 스킬 킬 포인트 % -18/1.18",
				"+ 7730*3*1.2+23192", "크증버프 20 & 스킬 킬 포인트 % -20/1.20","+ 8272*3*1.22+24817", "크증버프 22 & 스킬 킬 포인트 % -22/1.22","+ 8813*3*1.24+26441", "크증버프 24 & 스킬 킬 포인트 % -24/1.24", null,
				"쏘우 블레이드", Skill_type.BUF_ACTIVE, "", 75, 20, 10, 3,
				"6", "증뎀버프 24", "+", "증뎀버프 +2", "반복 1",
				"체인 글린트", Skill_type.BUF_ACTIVE, "", 80, 20, 10, 3,
				"4", "증뎀버프 30", "+", "증뎀버프 +3", "반복 1",
				"데스 바이 리볼버", Skill_type.SWITCHING, "", 30, 20, 10, 3, 
				"10", fStat[2], "크증버프 43", null,
				"트리플 클러치", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 3,
				"1", "스킬 탑스핀 % 10 & 스킬 라이징샷 % 10 & 스킬 니들 소배트 % 10 & 스킬 헤드샷 % 15", "+", "스킬 탑스핀 % +2 & 스킬 라이징샷 % +2 & 스킬 니들 소배트 % +2 & 스킬 헤드샷 % +3", "반복 1",
				"체인 파우더", Skill_type.BUF_ACTIVE, "", 40, 20, 10, 2,
				"10", "스킬 권총의 춤 % 0.4156*48 & 스킬 더블 건호크 % 50*0.52 & 스킬 이동사격 % 29 & 스킬 제압 사격 % 20 & 스킬 멀티 헤드샷 % 20 & 스킬 헤드샷 % 20",
				"11", "스킬 권총의 춤 % 0.4156*50 & 스킬 더블 건호크 % 53*0.52 & 스킬 이동사격 % 30 & 스킬 제압 사격 % 21 & 스킬 멀티 헤드샷 % 21 & 스킬 헤드샷 % 21",
				"12", "스킬 권총의 춤 % 0.4156*52 & 스킬 더블 건호크 % 54*0.52 & 스킬 이동사격 % 32 & 스킬 제압 사격 % 22 & 스킬 멀티 헤드샷 % 22 & 스킬 헤드샷 % 22",
				"13", "스킬 권총의 춤 % 0.4156*56 & 스킬 더블 건호크 % 57*0.52 & 스킬 이동사격 % 34 & 스킬 제압 사격 % 23 & 스킬 멀티 헤드샷 % 23 & 스킬 헤드샷 % 23",
				"14", "스킬 권총의 춤 % 0.4156*58 & 스킬 더블 건호크 % 60*0.52 & 스킬 이동사격 % 35 & 스킬 제압 사격 % 24 & 스킬 멀티 헤드샷 % 24 & 스킬 헤드샷 % 24",
				"15", "스킬 권총의 춤 % 0.4156*62 & 스킬 더블 건호크 % 63*0.52 & 스킬 이동사격 % 37 & 스킬 제압 사격 % 25 & 스킬 멀티 헤드샷 % 25 & 스킬 헤드샷 % 25",
				"16", "스킬 권총의 춤 % 0.4156*64 & 스킬 더블 건호크 % 66*0.52 & 스킬 이동사격 % 39 & 스킬 제압 사격 % 26 & 스킬 멀티 헤드샷 % 26 & 스킬 헤드샷 % 26", null,
				
				//TP
				"은탄 강화", "은탄", Job.RANGER_F, 50, 7, 5, -1,
				"1", "스킬 은탄 % 8.16", "+", "스킬 은탄 % 16.64", "+", "스킬 은탄 % 25.44", "+", "스킬 은탄 % 34.56", "+", "스킬 은탄 % 44",
				 "+", "스킬 은탄 % 53.76", "+", "스킬 은탄 % 63.84", null,
				"마릴린 로즈 강화", "마릴린 로즈", Character_type.GUNNER_F, 50, 7, 5, 8, null,
				"라이징샷 강화", "라이징샷", Character_type.GUNNER_F, 50, 7, 5, 8, null,
				"탑스핀 강화", "탑스핀", Character_type.GUNNER_F, 50, 7, 5, 8, null,
				//"스프리건 강화", "스프리건", Character_type.GUNNER_F, 50, 3, 1, 8, null,
				"니들 소배트 강화", "니들 소배트", Character_type.GUNNER_F, 50, 7, 5, 8, null,
				"헤드샷 강화", "헤드샷", Job.RANGER_F, 60, 7, 5, 10, null,
				"리벤저 강화", "리벤저", Job.RANGER_F, 60, 7, 5, 10, null,
				"소닉 스파이크 강화", "소닉 스파이크", Job.RANGER_F, 50, 7, 5, 10, null,
				"이동사격 강화", "이동사격", Job.RANGER_F, 50, 7, 5, 10, null,
				"권총의 춤 강화", "권총의 춤", Job.RANGER_F, 65, 7, 5, 10, null,
				"멀티 헤드샷 강화", "멀티 헤드샷", Job.RANGER_F, 65, 7, 5, 10, null,
				"더블 건호크 강화", "더블 건호크", Job.RANGER_F, 65, 7, 5, 10, null,
				
				///////////////////////스핏
				///////액티브
				"교차 사격", Skill_type.ACTIVE, Job.SPITFIRE_F, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"33", "귀속/오버 차지/1311*3", "+", "귀속/오버 차지/1343*3", "+", "귀속/오버 차지/1374*3", "+", "귀속/오버 차지/1405*3", "+", "귀속/오버 차지/1437*3", "+", "귀속/오버 차지/1468*3", "+", "귀속/오버 차지/1499*3", null, 
				"버스터 샷", Skill_type.ACTIVE,  "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"28", "귀속/오버 차지/7418", "+", "귀속/오버 차지/7619", "+", "귀속/오버 차지/7820", "+", "귀속/오버 차지/8022", "+", "귀속/오버 차지/8223", "+", "귀속/오버 차지/8424", null,  
				"C4", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"28 0 239.35 0 239.35", "29 245.84 245.84", "30 252.34 252.34", "31 258.83 258.83", "32 265.32 265.32", "33 271.82 271.82", null,
				"네이팜 탄", Skill_type.ACTIVE, "", 40, 60, 50 , 2,  Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"26", "귀속/오버 차지/6595", "+", "귀속/오버 차지/6784", "+", "귀속/오버 차지/6973", "+", "귀속/오버 차지/7163", "+", "귀속/오버 차지/7352", null,
				"네이팜 탄(장판)", "원본 - 네이팜 탄", "", 40, 60, 50 , 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"26 0 14.50 0 14.50", "", "27 15.00 15.00", "", "28 15.40 15.40", "", "29 15.80 15.80", "", "30 16.20 16.20", "", null,
				"록 온 서포트", Skill_type.ACTIVE, "", 45, 60, 50, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"23 0 72.94*5 0 72.94*5", "24 75.23*5 75.23*5", "25 77.52*5 77.52*5", "26 79.81*5 79.81*5", "27 82.10*5 82.10*5", "28 84.39*5 84.39*5", "29 86.68*5 86.68*5", null,
				"EMP 스톰", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"9 0 999.836/0.96 0 999.836/0.96", "10 0 (270.316*3+13.507*20)/0.96 0 (270.316*3+13.507*20)/0.96", "11 (290.620*3+14.524*20)/0.96 (290.620*3+14.524*20)/0.96", 
				"12 (310.934*3+15.542*20)/0.96 (310.934*3+15.542*20)/0.96", null,
				"G-61 중력류탄", Skill_type.ACTIVE, "", 60, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"16 0 3.94*30+118.25 0 3.94*30+118.25", "17 4.10*30+123.01 4.10*30+123.01", "18 4.25*30+127.77 4.25*30+127.77",
				"19 4.41*30+132.53 4.41*30+132.53", "20 4.57*30+137.28 4.57*30+137.28", "21 4.73*30+142.04 4.73*30+142.04", null,
				"피스톨 카빈", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"11", "귀속/오버 차지/665*17", "+", "귀속/오버 차지/699*17",  "+", "귀속/오버 차지/732*17",  null,
				"G-96 열압력유탄", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"5 0 542.5792 0 542.5792", "+ 581.7292 581.7292", "+ 620.8896 620.8896", "+ 660.0396 660.0396", null,
				"디-데이", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"2 0 31.4594*40 0 31.4594*40", "3 37.3792*40 37.3792*40", "4 43.3*40 43.3*40", "5 49.22*40 49.22*40", null,
				"G-35L 섬광류탄", Skill_type.DAMAGE_BUF, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"33 0 25.26 0 25.26", "크증버프 15 & 크리저항감소 10", "34 25.86 25.86", "크증버프 16 & 크리저항감소 10", "35 26.46 26.46", "크증버프 17 & 크리저항감소 10", "36 27.07 27.07", "크증버프 18 & 크리저항감소 10",
				"37 27.67 27.67", "크증버프 19 & 크리저항감소 10", "38 28.27 28.27", "크증버프 20 & 크리저항감소 10", null, 
				"G-18C 빙결류탄", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_e,
				"31 0 52.2593 0 52.2593 ", "32 53.5792 53.5792", "33 54.8792 54.8792", "34 56.1990 56.1990", "35 60.375 60.3750", null,
				"M18 클레이모어", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"36 0 24.39*3 0 24.39", "37 24.94*3 24.94*3", "38 25.48*3 25.48*3", "39 26.03*3 26.03*3", "40 26.57*3 26.57*3", "41 27.11*3 27.11*3", null, 
				"평타", Skill_type.ACTIVE, "", 1, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_e, "설명 한발의 데미지입니다",
				"1 150 0 150 0", null,
				"폭발탄", Skill_type.ACTIVE, "", 30, 20, 10, 3, Element_type.NONE, CalculatorVersion.VER_1_0_e, "설명 한발의 데미지입니다",
				"10", "귀속/오버 차지/469", "+", "귀속/오버 차지/500", "+", "귀속/오버 차지/530", "+", "귀속/오버 차지/561", "+", "귀속/오버 차지/592", "+", "귀속/오버 차지/623", null,
				"평타(1사이클)", Skill_type.ACTIVE, "", 1, 25, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"1", "귀속/평타/200 & 귀속/폭발탄/100", "+", "귀속/평타/+100 & 귀속/폭발탄/+100", "반복 1",
				
				//////패시브
				"오버 차지", Skill_type.SWITCHING, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"설명 ※※※※※중요※※※※※\n물리/마법 고정데미지의 숫자는\n(스킬창의 수치)/(현재 자신의 독공)\n을 입력해주세요.\nex)10레벨의 기본수치는 4.27,\n 20레벨의 수치는 7.08입니다", "설명 스증 수치는 다 입력해주셔야합니다 죄송ㅎ",
				"10 0 4.27 0 4.27", "스킬 C4 % 34 & 스킬 네이팜 탄(장판) % 34 & 스킬 록 온 서포트 % 34 & 스킬 EMP 스톰 % 34 & 스킬 G-61 중력류탄 % 34 & 스킬 G-96 열압력유탄 % 34 & "
				+ "스킬 디-데이 % 34 & 스킬 G-35L 섬광류탄 % 34 & 스킬 G-18C 빙결류탄 % 34 & 스킬 M18 클레이모어 % 34 & 스킬 평타 % 34", null,
				"병기 숙련", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", fStat[5], "", "+", fStat[5], "", "반복 1",
				"강화탄", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e, "설명 강화탄을 찍지 않을 경우 물리/마법공격력이 모두 들어가는 참사가 발생합니다 빼지마세요",
				"10", fStat[4], "", "+", fStat[4], "", "반복 1",
				"강화탄(화)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(수)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(명)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(무)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(컨버전)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_e, "설명 컨버전을 킬 경우, 마공으로 전환됩니다",
				"1", "", null,
				"유탄 마스터리", Skill_type.PASSIVE, "", 20, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "스킬 G-14 파열류탄 % 100 & 스킬 G-35L 섬광류탄 % 100 & 스킬 G-18C 빙결류탄 % 010", "+", "스킬 G-14 파열류탄 % +10 & 스킬 G-35L 섬광류탄 % +10 & 스킬 G-18C 빙결류탄 % +10", "반복 1",
				"병기 강화", Skill_type.PASSIVE, "", 48, 30, 20, 3, CalculatorVersion.VER_1_0_e,
				"15", "스증뎀 33", "+", "스증뎀 +2", "+", "스증뎀 +1", "반복 2", 
				"증명의 열쇠", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"4", "스증뎀 26", "+", "스증뎀 +2", "반복 1",
				"패스티스트 건", Skill_type.PASSIVE, "", 30, 20, 5, 3, CalculatorVersion.VER_1_0_e,
				"5", "스킬 평타 % 12", "+", "스킬 평타 % +0.5", "반복 1",
				"공중사격", Skill_type.BUF_ACTIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", fStat[6], "스킬 평타 % 20", "+", fStat[6], "스킬 평타 % +2", "반복 1",
				"기본기 숙련", Skill_type.PASSIVE, "", 1, 100, 100, 1, CalculatorVersion.VER_1_0_e,
				"90", fStat[7], "스킬 평타 % 512.2", "+", fStat[7], "스킬 평타 % +5.8", "반복 1",
				
				/////TP
				"류탄 강화", "G-14 파열류탄 & G-35L 섬광류탄 & G-18C 빙결류탄", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"교차 사격 강화", "교차 사격", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"버스터 샷 강화", "버스터 샷", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"M18 클레이모어 강화", "M18 클레이모어", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"C4 강화", "C4", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"네이팜 탄 강화", "네이팜 탄", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				"록 온 서포트 강화", "록 온 서포트", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_e, null,
				
				
				
				//////////////////남스핏
				/////액티브
				"교차 사격", Skill_type.ACTIVE, Job.SPITFIRE_M, 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"33", "귀속/오버 차지/1311*3", "+", "귀속/오버 차지/1343*3", "+", "귀속/오버 차지/1374*3", "+", "귀속/오버 차지/1405*3", "+", "귀속/오버 차지/1437*3", "+", "귀속/오버 차지/1468*3", "+", "귀속/오버 차지/1499*3", null, 
				"버스터 샷", Skill_type.ACTIVE,  "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"28", "귀속/오버 차지/7418", "+", "귀속/오버 차지/7619", "+", "귀속/오버 차지/7820", "+", "귀속/오버 차지/8022", "+", "귀속/오버 차지/8223", "+", "귀속/오버 차지/8424", null,  
				"C4", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"28 23935 0 23935 0", "29 24584 24584", "30 25234 25234", "31 25883 25883", "32 26532 26532", "33 27182 27182", null,
				"네이팜 탄", Skill_type.ACTIVE, "", 40, 60, 50 , 2,  Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"26", "귀속/오버 차지/6595", "+", "귀속/오버 차지/6784", "+", "귀속/오버 차지/6973", "+", "귀속/오버 차지/7163", "+", "귀속/오버 차지/7352", null,
				"네이팜 탄(장판)", "원본 - 네이팜 탄", "", 40, 60, 50 , 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"26 1450 0 1450 0", "", "27 1500 1500", "", "28 1540 1540", "", "29 1580 1580", "", "30 1620 1620", "", null,
				"록 온 서포트", Skill_type.ACTIVE, "", 45, 60, 50, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"23 7294*5 0 7294*5 0", "24 7523*5 7523*5", "25 7752*5 7752*5", "26 7981*5 7981*5", "27 8210*5 8210*5", "28 8439*5 8439*5", "29 8668*5 8668*5", null,
				"특수기동전대 '블랙 로즈'", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"9 121735 0 121735 0", "10 131630 0 131630 0", "11 141524 141524", "12 151434 151434", "13 161328 161328", "14 171222 171222", "15 181116 181116", null,
				"G-61 중력류탄", Skill_type.ACTIVE, "", 60, 40, 30, 2,  Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"16 394*30+11825 0 394*30+11825 0", "17 410*30+12301 410*30+12301", "18 425*30+12777 425*30+12777", "19 441*30+13253 441*30+13253", "20 457*30+13728 457*30+13728", "21 473*30+14204 473*30+14204", null,
				"피스톨 카빈", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"11", "귀속/오버 차지/665*17", "+", "귀속/오버 차지/699*17",  "+", "귀속/오버 차지/721*17",  "+", "귀속/오버 차지/766*17",  
				"+", "귀속/오버 차지/799*17",  "+", "귀속/오버 차지/833*17",  "+", "귀속/오버 차지/866*17",  "+", "귀속/오버 차지/900*17", null,
				"데인저 클로즈", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"8", "귀속/오버 차지/9436", "+", "귀속/오버 차지/9996", "+", "귀속/오버 차지/10557", "+", "귀속/오버 차지/11116",    
				"+", "귀속/오버 차지/11675", "+", "귀속/오버 차지/12236", "+", "귀속/오버 차지/12795", null,   
				"G-38ARG 반응류탄", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"6 4101*10+17579 0 4101*10+17579 0", "7 4377*10+18762 4377*10+18762", "8 4653*10+19945 4653*10+19945",
				"9 4930*10+21128 4930*10+21128", "10 5206*10+22311 5206*10+22311", "11 5482*10+23495 5482*10+23495", null, 
				"슈퍼 노바", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"2 69827+1994*15 0 69827+1994*15 0", "3 82971+2370*15 82971+2370*15", "4 96115+2746*15 96115+2746*15", "5 109259+3121*15 109259+3121*15", "6 122403+3497*15 122403+3497*15", null,
				"G-35L 섬광류탄", Skill_type.DAMAGE_BUF, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"33 2526 0 2526 0", "크증버프 15 & 크리저항감소 10", "34 2586 2586", "크증버프 16 & 크리저항감소 10", "35 2646 2646", "크증버프 17 & 크리저항감소 10", "36 2707 2707", "크증버프 18 & 크리저항감소 10",
				"37 2767 2767", "크증버프 19 & 크리저항감소 10", "38 2827 2827", "크증버프 20 & 크리저항감소 10", null, 
				"G-18C 빙결류탄", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_d,
				"31 6010 0 6010 0", "32 6162 6162", "33 6311 6311", "34 6463 6463", "35 6613 6613", null,
				"M18 클레이모어", Skill_type.ACTIVE, "", 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"36 2439*3 0 2439 0", "37 2494*3 2494*3", "38 2548*3 2548*3", "39 2603*3 2603*3", "40 2657*3 2657*3", "41 2711*3 2711*3", null, 
				"평타", Skill_type.ACTIVE, "", 1, 1, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_d, "설명 한발의 데미지입니다",
				"1 150 0 150 0", null,
				"폭발탄", Skill_type.ACTIVE, "", 30, 20, 10, 3, Element_type.NONE, CalculatorVersion.VER_1_0_e, "설명 한발의 데미지입니다",
				"10", "귀속/오버 차지/469", "+", "귀속/오버 차지/500", "+", "귀속/오버 차지/530", "+", "귀속/오버 차지/561", "+", "귀속/오버 차지/592", "+", "귀속/오버 차지/623", null,
				"평타(1사이클)", Skill_type.ACTIVE, "", 1, 25, 1, 1, Element_type.NONE, CalculatorVersion.VER_1_0_d,
				"1", "귀속/평타/200 & 귀속/폭발탄/100", "+", "귀속/평타/+100 & 귀속/폭발탄/+100", "반복 1",
				
				//////패시브
				"오버 차지", Skill_type.SWITCHING, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d, "설명 수치 다 입력해주셔야합니다 죄송ㅎ",
				"10 427 0 427 0", "스킬 C4 % 34 & 스킬 네이팜 탄(장판) % 34 & 스킬 록 온 서포트 % 34 & 스킬 특수기동전대 '블랙 로즈' % 34 & 스킬 G-61 중력류탄 % 34 & 스킬 G-38ARG 반응류탄 % 34 & "
				+ "스킬 슈퍼 노바 % 34 & 스킬 G-35L 섬광류탄 % 34 & 스킬 G-18C 빙결류탄 % 34 & 스킬 M18 클레이모어 % 34 & 스킬 평타 % 34", null,
				"병기 숙련", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"10", fStat[5], "", "+", fStat[5], "", "반복 1",
				"강화탄", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d, "설명 강화탄을 찍지 않을 경우 물리/마법공격력이 모두 들어가는 참사가 발생합니다 빼지마세요",
				"10", fStat[4], "", "+", fStat[4], "", "반복 1",
				"강화탄(화)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(수)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(명)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(무)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 적용우선순위 : 무속->화->수->명",
				"1", "", null,
				"강화탄(컨버전)", Skill_type.OPTION, "", 1, 1, 1, 1, CalculatorVersion.VER_1_0_d, "설명 컨버전을 킬 경우, 마공으로 전환됩니다",
				"1", "", null,
				"유탄 마스터리", Skill_type.PASSIVE, "", 20, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "스킬 G-14 파열류탄 % 100 & 스킬 G-35L 섬광류탄 % 100 & 스킬 G-18C 빙결류탄 % 100", "+", "스킬 G-14 파열류탄 % +10 & 스킬 G-35L 섬광류탄 % +10 & 스킬 G-18C 빙결류탄 % +10", "반복 1",
				"듀얼 플리커", Skill_type.PASSIVE, "", 48, 30, 20, 3, CalculatorVersion.VER_1_0_d,
				"15", "스증뎀 33", "+", "스증뎀 +1.5", "반복 1", 
				"전장의 영웅", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"4", "스증뎀 26", "+", "스증뎀 +2", "반복 1",
				"패스티스트 건", Skill_type.PASSIVE, "", 30, 20, 5, 3, CalculatorVersion.VER_1_0_d,
				"5", "스킬 평타 % 12", "+", "스킬 평타 % +0.5", "반복 1",
				"매거진 드럼", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_d,
				"10", fStat[6], "스킬 평타 % 20", "+", fStat[6], "스킬 평타 % +2", "반복 1",
				"기본기 숙련", Skill_type.PASSIVE, "", 1, 100, 100, 1, CalculatorVersion.VER_1_0_d,
				"90", fStat[7], "스킬 평타 % 512.2", "+", fStat[7], "스킬 평타 % +5.8", "반복 1",
				
				/////TP
				"류탄 강화", "G-14 파열류탄 & G-35L 섬광류탄 & G-18C 빙결류탄", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"교차 사격 강화", "교차 사격", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"버스터 샷 강화", "버스터 샷", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"M18 클레이모어 강화", "M18 클레이모어", "", 50, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"C4 강화", "C4", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"네이팜 탄 강화", "네이팜 탄", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"록 온 서포트 강화", "록 온 서포트", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				
				
				////////////////////남런처
				//////액티브
				"M-137 개틀링건", Skill_type.ACTIVE, Character_type.GUNNER_M, 5, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e, 
				"44 213*20 1.78*20 0 0", "+ 218*20 1.819*20", "+ 222*20 1.849*20", "+ 226*20 1.88*20", "+ 230*20 1.913*20", null,
				"M-3 화염방사기", "", "", 15, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"39 575*10 4.779*10 0 0", "+ 588*10 4.899*10", "+ 602*10 4.99*10", "+ 615*10 5.12*10", "+ 628*10 5.228*10", null,
				"바베~큐", "", "", 10, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"42 7440 74.4 0 0", "+ 7570 81.75", "+ 7720 83.37", "+ 7880 85.10", null,
				"슈타이어 대전차포", Skill_type.ACTIVE, Job.LAUNCHER_M, 20, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"37 976+3900 8.149+32.59 0 0", "+ 997+3987 8.309+33.26", "+ 1018+4072 8.48+33.93", "+ 1039+4158 8.67+34.7", 
				"+ 1061+4246 8.84+35.36", "+ 1083+4331 45.094", null,
				"레이저 라이플", "", "", 25, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e,
				"34 3912 32.59 0 0", "+ 4001 33.34", "+ 4092 34.11", "+ 4187 34.89", "+ 4278 35.65", "+ 4369 36.407", null,
				"화염 강타", "", "", 30, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"32 398*26 3.46*26 0 0", "+ 407*26 3.54*26", "+ 416*26 3.62*26", "+ 427*26 3.71*26", "+ 437*26 3.8*26", 
				"+ 446*26 3.879*26", "+ 457*26 3.786", null,
				"FM-31 그레네이드 런처", "", "", 35, 60, 50, 2, "", CalculatorVersion.VER_1_0_e,
				"29 2612*4 17.4*4 0 0", "+ 2681*4 17.89*4", "+ 2752*4 18.34*4", "+ 2821*4 18.82*4", "+ 2891*4 19.26*4", "+ 2962*4 19.72*4", null,
				"FM-92 스팅어", "", "", "", "", "", "", "", CalculatorVersion.VER_1_0_e,
				"29 2170*5 25.189*5 0 0", "+ 2227*5 25.75*5", "+ 2282*5 26.34*5", "+ 2340*5 26.91*5", "+ 2394*5 27.49*5", "+ 2451*5 28.07*5", null,
				"양자 폭탄", "", "", 40, "", "", "", Element_type.LIGHT, CalculatorVersion.VER_1_0_e,
				"27 3762+11281 37.62+112.81 0 0", "+ 3866+11597 38.66+115.970", "+ 3973+11911 39.73+119.11", "+ 4076+12225 40.76+122.25",
				"+ 4180+12540 41.80+125.4", "+ 4285+12856 42.85+128.56", null,
				"X-1 익스트루더", "", "", 45, "", "", "", Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"24 20915 209.15 0 0", "+ 21555 215.550", "+ 22193 221.930", "+ 22834 228.340", "+ 23472 234.720", "+ 24113 241.130", "+ 24755 247.550", null,
				"새틀라이트 빔", Skill_type.DAMAGE_BUF, "", 50, 40, 30, 5, Element_type.LIGHT,  CalculatorVersion.VER_1_0_e,
				"11 7393+3192*42 0 0 0", "명속깍 26", "+ 7912+3387*43", "명속깍 +1", "+ 8433+3577*46", "명속깍 +1", "+ 8948+3763*46", 
				"명속깍 +1", "+ 9463+3951*47", "명속깍 +1", "+ 9982+4134*48", "명속깍 +1", "10 6874+2997*41 0 0 0", "명속깍 25", null,
				"팜페로 부스터", Skill_type.ACTIVE, "", 60, "", "", 2, Element_type.WATER, CalculatorVersion.VER_1_0_e,
				"17 1371*12 13.71*12 0 0", "+ 1436*12 14.36*12", "+ 1502*12 15.019*12", "+ 1567*12 15.67*12", "+ 1635*12 16.349*12", null,
				"FM-92 스팅어 SW", "", "", 70, 40, 30, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"12 5802*4 58.02*4 0 0", "+ 6081*4 60.809*4", "+ 6360*4 63.60*4", "+ 6638*4 66.38*4 0 0", "+ 6915*4 69.150*4", null,
				"사이즈믹 웨이브", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"8 2210 88.369+22.10+44.18*6+233.549 0 0", "+ 2340 93.332+23.34+46.67*6+246.640", "+ 2456 98.270+24.56+49.14*6+259.720", "+ 2580 103.229+25.80+51.60*6+272.800",
				"+ 2705 108.170+27.05+54.09*6+285.880", "+ 2829 113.119+28.289+56.55*6+298.96", null,
				"버스터 빔", "", "", 85, 40, 30, 5, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"2 20880+31293 208.80+312.93 0 0", "+ 24780+37184 247.79+371.84", "+ 28730+43074 287.30+430.74", "+ 32640+48966 326.40+489.66",
				"+ 36570+54854 365.70+548.54", "+ 40500+60746 405.00+607.459",null,

				///////패시브
				"중화기 다루기", Skill_type.PASSIVE, "", 15, 1, 1, 3, CalculatorVersion.VER_1_0_e,
				"1", "스킬 M-137 개틀링건 1 & 스킬 바베~큐 1 & 스킬 M-3 화염방사기 1 & 스킬 슈타이어 대전차포 1 & 스킬 레이저 라이플 1 & 스킬 화염 강타 1 & 스킬 FM-31 그레네이드 런처 1 & 스킬 FM-92 스팅어 1 & 스킬 캐넌볼 1 & "
				+ "스킬 양자 폭탄 1 & 스킬 X-1 익스트루더 1 & 스킬 새틀라이트 빔 1 & 스킬 팜페로 부스터 1 & 스킬 FM-92 스팅어 SW 1 & 스킬 사이즈믹 웨이브 1 & 스킬 버스터 빔 1", null,
				"중화기 마스터리", Skill_type.PASSIVE, "", 15, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "무기마스터리 10 핸드캐넌 & 증뎀버프 20", "+", "무기마스터리 11 핸드캐넌 & 증뎀버프 22", "+", "무기마스터리 12 핸드캐넌 & 증뎀버프 24", "+", "무기마스터리 13 핸드캐넌 & 증뎀버프 26", 
				"+", "무기마스터리 14 핸드캐넌 & 증뎀버프 28", "+", "무기마스터리 15 핸드캐넌 & 증뎀버프 30", "+", "무기마스터리 16 핸드캐넌 & 증뎀버프 32", "+", "무기마스터리 17 핸드캐넌 & 증뎀버프 34",
				"+", "무기마스터리 18 핸드캐넌 & 증뎀버프 36", "+", "무기마스터리 19 핸드캐넌 & 증뎀버프 38", "+", "무기마스터리 20 핸드캐넌 & 증뎀버프 40", null,
				"스펙트럴 서치 아이", "", "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_e,
				"14", "크리저항감소 21.6", "+", "크리저항감소 +1.6", "반복 1",
				"근력 강화", "", "", 75, 40, 30, 3, CalculatorVersion.VER_1_0_e,
				"5", "스증 24", "+", "스증 +4", "반복 1",
				"중화기 개조", "", "", 80, "", "", "", CalculatorVersion.VER_1_0_e,
				"4", "스킬 레이저 라이플 % 31 & 스킬 화염 강타 % 42 & 스킬 슈타이어 대전차포 % 34 & 스킬 그레네이드 런처 % 52 & 스킬 M-3 화염방사기 % 39 & 스킬 양자 폭탄 % 33 & 스킬 X-1 익스트루더 % 33 & "
				+ "스킬 새틀라이트 빔 % 33 & 스킬 FM-92 스팅어 % 33 & 스킬 팜페로 부스터 % 33 & 스킬 FM-92 스팅어 SW % 33 & 스킬 버스터 빔 % 33 & 스킬 바베~큐 % 33 & "
				+ "스킬 캐넌볼 % 33 & 스킬 사이즈믹 웨이브 % 33 & 스킬 M-137 개틀링건 % 58",
				"+", "스킬 레이저 라이플 % +2 & 스킬 화염 강타 % +2 & 스킬 슈타이어 대전차포 % +4 & 스킬 그레네이드 런처 % +3 & 스킬 M-3 화염방사기 % +3 & 스킬 양자 폭탄 % +3 & 스킬 X-1 익스트루더 % +3 & "
				+ "스킬 새틀라이트 빔 % +3 & 스킬 FM-92 스팅어 % +3 & 스킬 팜페로 부스터 % +3 & 스킬 FM-92 스팅어 SW % +3 & 스킬 버스터 빔 % +3 & 스킬 바베~큐 % +3 & "
				+ "스킬 캐넌볼 % +3 & 스킬 사이즈믹 웨이브 % +3 & 스킬 M-137 개틀링건 % +7.5", "반복 1",
				"충전 레이저 라이플", "", "", 25, 11, 1, 2, CalculatorVersion.VER_1_0_e,
				"1", "스킬 레이저 라이플 % 90", "+", "스킬 레이저 라이플 % +10", "+", "스킬 레이저 라이플 % +5", "반복 2", 
				"미라클 비전", Skill_type.SWITCHING, "", 30, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "증뎀버프 41", null,
				"듀얼 트리거", Skill_type.PASSIVE, "", 35, 1, 1, 3, CalculatorVersion.VER_1_0_e, "설명 명, 화속성 중 높은 속성 강화 값으로 낮은 값이 상승한다.", 
				"1", fStat[0], null,
				"집중포화", Skill_type.BUF_ACTIVE, "", 40, 1, 1, 3,  CalculatorVersion.VER_1_0_e, "설명 그냥 넣어봤음", 
				"1", "", null,
				
				//////TP
				"M-137 개틀링건 강화", "M-137 개틀링건", Character_type.GUNNER_M, 50, 7, 5, 8, null,
				"바베~큐 강화", "바베~큐", Character_type.GUNNER_M, 50, 3, 1, 20, null,
				"M-3 화염방사기 강화", "M-3 화염방사기", Character_type.GUNNER_M, 50, 7, 5, 8, null,
				"슈타이어 대전차포 강화", "슈타이어 대전차포", Job.LAUNCHER_M, 55, 7, 5, 10, null,
				"캐넌볼 강화", "캐넌볼", "", 50, 7, 5, 10, null,
				"레이저 라이플 강화", "레이저 라이플", "", 50, 7, 5, 10, null,
				"화염 강타 강화", "화염 강타", "", 50, 7, 5, 10, null,
				"FM-92 스팅어 강화", "FM-92 스팅어", "", 65, 7, 5, 10, null,
				"양자 폭탄 강화", "양자 폭탄", "", 65, 7, 5, 10, null,
				"X-1 익스트루더 강화", "X-1 익스트루더", "", 65, 7, 5, 10, null,
				"FM-31 그레네이드 런처 강화", "FM-31 그레네이드 런처", "", 50, 1, 1, 20, null,
				
				
				/////////////남렝거
				/////액티브
				"라이징샷", Skill_type.ACTIVE, Job.RANGER_M, 1, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"45 2206 4.5896 0 0 ", "+ 2249 4.6792", "+ 2292 4.7698", "+ 2335 4.8594", "+ 2378 4.95", "+ 2421 5.04", null,
				"잭스파이크", "", "", 1, 20, 10, 3, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"10 180 0.179 0 0", "+ 190 0.1896", "+ 200 0.2", "+ 210 0.2094", null,
				"은탄", Skill_type.ACTIVE, Job.RANGER_M, 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e, "설명 한 탄창의 딜량입니다",
				"43 697*25 0 0 0", "귀속/은탄(언악정)/100", "+ 711*25", "귀속/은탄(언악정)/100", "+ 725*25", "귀속/은탄(언악정)/100", "+ 739*25", "귀속/은탄(언악정)/100", "+ 754*25 ", "귀속/은탄(언악정)/100",
				"+ 768*25", "귀속/은탄(언악정)/100", "+ 782*25", "귀속/은탄(언악정)/100", "+ 796*25", "귀속/은탄(언악정)/100", "+ 810*25", "귀속/은탄(언악정)/100", "+ 824*25", "귀속/은탄(언악정)/100",
				"+ 838*25", "귀속/은탄(언악정)/100", "+ 853*25", "귀속/은탄(언악정)/100", null,
				"은탄(언악정)", Skill_type.OPTION, "", 5, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e, "설명 언데드, 악마, 정령 타입 몬스터 공격 시 추가",
				"43 174*25 0 0 0", "", "+ 178*25", "", "+ 181*25", "", "+ 185*25", "", "+ 189*25", "", "+ 192*25", "", "+ 196*25", "", "+ 199*25", "", "+ 203*25", "", "+ 206*25", "", "+ 210*25", "", "+ 213*25", null,
				"윈드밀", Skill_type.ACTIVE, "", 10, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"41 1098*2 18.3094*2 0 0", "+ 1120*2 18.6798*2", "+ 1142*2 19.0396*2", "+ 1164*2 19.4094*2", "+ 1186*2 19.7792*2", null,
				"퍼니셔", Skill_type.ACTIVE, "", 10, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"41 699*5 1.75*5 0 0", "+ 714*5 1.7792*5", "+ 728*5 1.8198*5", "+ 743*5 1.8594*5", "+ 757*5 1.8896*5", null,
				"헤드샷", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"38 3286 0 0 0", "+ 3359", "+ 3433", "+ 3506", "+ 3680", "+ 3654", "+ 3727", "+ 3800", null,
				"마하킥", Skill_type.ACTIVE, "", 15, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"38 3339 27.8792 0 0", "+ 3410 28.4698", "+ 3482 29.0698", "+ 3553 29.6656", "+ 3624 30.2594", "+ 3695 30.8490", null,
				"에어레이드", Skill_type.ACTIVE, "", 25, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"33 4488*2 0 0 0", "+ 4595*2", "+ 4702*2", "+ 4809*2", "+ 4917*2", "+ 5024*2", "+ 5132*2", null,
				"트리플 탭", Skill_type.ACTIVE, "", 30, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"31 12421 0 0 0", "+ 12732", "+ 13044", "+ 13355", "+ 13666", "+ 13980", "+ 14291", "+ 14602", "+ 14914", null,
				"웨스턴 파이어", Skill_type.ACTIVE, "", 30, 1, 1, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"1 0 0 0 0", "귀속/헤드샷/120", null,
				"이동사격", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"28 1283*30 0 0 0", "+ 1317*30", "+ 1352*30", "+ 1387*30", "+ 1422*30", "+ 1457*30", "+ 1492*30", "+ 1526*30", "+ 1561*30", "+ 1596*30", null,
				"난사", Skill_type.ACTIVE, "", 35, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"28 1587*20 0 0 0", "+ 1630*20", "+ 1673*20", "+ 1716*20", "+ 1759*20", "+ 1802*20", "+ 1845*20", "+ 1888*20", "+ 1931*20", "+ 1974*20",
				"+ 2017*20", "+ 2060*20", "+ 2103*20", null,
				"멀티 헤드샷", Skill_type.ACTIVE, "", 40, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"26 4925*5 0 0 0 ", "+ 5066*5", "+ 5207*5", "+ 5349*5", "+ 5490*5", "+ 5631*5", "+ 5772*5", "+ 5914*5", null,
				"더블 건호크", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"23 68030 0 0 0", "+ 70170", "+ 72304", "+ 74444", "+ 76582", "+ 78700", "+ 80856", "+ 82996", "+ 85112", "+ 87252", "+ 89408", "+ 91526", null,
				"스커드 제노사이드", Skill_type.ACTIVE, "", 50, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"9 113770 0 0 0", "+ 122988", "+ 132273", "+ 141491", "+ 150709", "+ 159994", "+ 169212", null,
				"데들리 어프로치", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"16 9884*3 0 0 0", "+ 10282*3", "+ 10680*3", "+ 11077*3", "+ 11475*3", null,
				"제압 사격", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"11 41935 0 0 0", "+ 44037", "+ 46158", "+ 48260", "+ 50381", null,
				"패스트 드로우", Skill_type.ACTIVE, "", 75, 40, 30, 2, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"8 2564*24 0 0 0", "+ 2716*24", "+ 2869*24", "+ 3021*24", "+ 3173*24", "+ 3325*24", "+ 3477*24", "+ 3629*24", null,
				"세븐스 플로우", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.NONE, CalculatorVersion.VER_1_0_e,
				"2 89315 0 0 0", "+ 106137", "+ 122946", "+ 139769", "+ 156578", null,
				
				/////패시브
				"트리플 클러치", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 1, CalculatorVersion.VER_1_0_e,
				"1", "스킬 마하킥 % 10 & 스킬 라이징샷 % 15 & 스킬 헤드샷 % 15 & 물크 10 & 스킬 마하킥 % 30 & 스킬 윈드밀 % 30",
				"+", "스킬 마하킥 % +2 & 스킬 라이징샷 % +3 & 스킬 헤드샷 % +3 & 물크 +5 & 스킬 마하킥 % +5 & 스킬 윈드밀 % +5", "반복 1",
				"스타일리쉬", Skill_type.BUF_ACTIVE, "", 25, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", "스킬 라이징샷 % 20 & 스킬 헤드샷 % 20 & 스킬 트리플 탭 % 20 & 스킬 난사 % 20 & 스킬 멀티 헤드샷 % 20 & 스킬 더블 건호크 % 20 & 스킬 제압 사격 % 20 & "
				+ "스킬 스커드 제노사이드 % 20 & 스킬 데들리 어프로치 % 20 & 스킬 패스트 드로우 % 20 & 스킬 세븐스 플로우 % 20 & 스킬 이동사격 % 20",
				"+", "스킬 라이징샷 % +1 & 스킬 헤드샷 % +1 & 스킬 트리플 탭 % +1 & 스킬 난사 % +1 & 스킬 멀티 헤드샷 % +1 & 스킬 더블 건호크 % +1 & 스킬 제압 사격 % +1 & "
				+ "스킬 스커드 제노사이드 % +1 & 스킬 데들리 어프로치 % +1 & 스킬 패스트 드로우 % +1 & 스킬 세븐스 플로우 % +1 & 스킬 이동사격 % +1", "반복 1",
				"데스 바이 리볼버", Skill_type.SWITCHING, "", 30, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"10", fStat[2], "크증버프 43", null,
				"죽음의 표식", Skill_type.BUF_ACTIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_e,
				"15", "증뎀버프 33 & 물크 17.3", "+", "증뎀버프 +2 & 물크 +1.1", "+", "증뎀버프 +2 & 물크 +1.1", "+", "증뎀버프 +2 & 물크 +1.2", "반복 3",
				"강화 리볼버", Skill_type.PASSIVE, "", 75, 20, 10, 3, CalculatorVersion.VER_1_0_e, "설명 리볼버 착용시 물공, 방무뎀 (6+3.5*n)% 증가(소숫점 버림)",
				"6", "무기마스터리 27 리볼버 & 무기마스터리_방무 27 리볼버", "+", "무기마스터리 30 리볼버 & 무기마스터리_방무 30 리볼버", "+", "무기마스터리 34 리볼버 & 무기마스터리_방무 34 리볼버", "+", "무기마스터리 37 리볼버 & 무기마스터리_방무 37 리볼버",
				"+", "무기마스터리 41 리볼버 & 무기마스터리_방무 41 리볼버", "+", "무기마스터리 44 리볼버 & 무기마스터리_방무 44 리볼버", "+", "무기마스터리 48 리볼버 & 무기마스터리_방무 48 리볼버", "+", "무기마스터리 51 리볼버 & 무기마스터리_방무 51 리볼버",
				"+", "무기마스터리 55 리볼버 & 무기마스터리_방무 55 리볼버", "+", "무기마스터리 58 리볼버 & 무기마스터리_방무 58 리볼버", "+", "무기마스터리 62 리볼버 & 무기마스터리_방무 62 리볼버", "+", "무기마스터리 65 리볼버 & 무기마스터리_방무 65 리볼버",
				"+", "무기마스터리 69 리볼버 & 무기마스터리_방무 69 리볼버", "+", "무기마스터리 72 리볼버 & 무기마스터리_방무 72 리볼버", "+", "무기마스터리 76 리볼버 & 무기마스터리_방무 76 리볼버", null,
				"사격술", Skill_type.PASSIVE, "", 80, 20, 10, 3, CalculatorVersion.VER_1_0_e,
				"4", "스킬 라이징샷 % 22 & 스킬 헤드샷 % 49 & 스킬 트리플 탭 % 22 & 스킬 난사 % 22 & 스킬 멀티 헤드샷 % 22 & 스킬 더블 건호크 % 22 & 스킬 제압 사격 % 22 & "
				+ "스킬 스커드 제노사이드 % 22 & 스킬 데들리 어프로치 % 22 & 스킬 패스트 드로우 % 22 & 스킬 세븐스 플로우 % 22 & 스킬 이동사격 % 22",
				"+", "스킬 라이징샷 % +2 & 스킬 헤드샷 % +4 & 스킬 트리플 탭 % +2 & 스킬 난사 % +2 & 스킬 멀티 헤드샷 % +2 & 스킬 더블 건호크 % +2 & 스킬 제압 사격 % +2 & "
				+ "스킬 스커드 제노사이드 % +2 & 스킬 데들리 어프로치 % +2 & 스킬 패스트 드로우 % +2 & 스킬 세븐스 플로우 % +2 & 스킬 이동사격 % +2",
				"+", "스킬 라이징샷 % +2 & 스킬 헤드샷 % +5 & 스킬 트리플 탭 % +2 & 스킬 난사 % +2 & 스킬 멀티 헤드샷 % +2 & 스킬 더블 건호크 % +2 & 스킬 제압 사격 % +2 & "
				+ "스킬 스커드 제노사이드 % +2 & 스킬 데들리 어프로치 % +2 & 스킬 패스트 드로우 % +2 & 스킬 세븐스 플로우 % +2 & 스킬 이동사격 % +2", "반복 2",
				
				/////TP
				"은탄 강화", "은탄", Job.RANGER_M, 50, 7, 5, -1,
				"1", "스킬 은탄 % 8.16", "+", "스킬 은탄 % 16.64", "+", "스킬 은탄 % 25.44", "+", "스킬 은탄 % 34.56", "+", "스킬 은탄 % 44",
				 "+", "스킬 은탄 % 53.76", "+", "스킬 은탄 % 63.84", null,
				"잭스파이크 강화", "잭스파이크", Job.RANGER_M, 50, 7, 5, 8, null,
				"라이징샷 강화", "라이징샷", Job.RANGER_M, 50, 7, 5, 8, null,
				"윈드밀 강화", "윈드밀", Job.RANGER_M, 50, 7, 5, 8, null,
				"퍼니셔 강화", "퍼니셔", Job.RANGER_M, 50, 3, 1, -1,
				"1", "스킬 퍼니셔 % 19", "+", "스킬 퍼니셔 % 26", "+", "스킬 퍼니셔 % 21", null,
				"마하킥 강화", "마하킥", Job.RANGER_M, 50, 7, 5, 8, null,
				"헤드샷 강화", "헤드샷", Job.RANGER_M, 60, 7, 5, 10, null,
				"에어레이드 강화", "에어레이드", Job.RANGER_M, 50, 7, 5, 10, null,
				"트리플 탭 강화", "트리플 탭", Job.RANGER_M, 55, 7, 5, 10, null,
				"이동사격 강화", "이동사격", Job.RANGER_M, 50, 7, 5, -1,
				"1", "스킬 이동사격 % 16.6", "+", "스킬 이동사격 % 34.4", "+", "스킬 이동사격 % 53.4", "+", "스킬 이동사격 % 73.6", "+", "스킬 이동사격 % 95.0",
				"+", "스킬 이동사격 % 117.6", "+", "스킬 이동사격 % 141.4", null,
				"난사 강화", "난사", Job.RANGER_M, 65, 7, 5, 10, null,
				"멀티 헤드샷 강화", "멀티 헤드샷", Job.RANGER_M, 65, 7, 5, 10, null,
				"더블 건호크 강화", "더블 건호크", Job.RANGER_M, 65, 7, 5, -1,
				"1", "스킬 더블 건호크 % 12.61", "+", "스킬 더블 건호크 % 22.06", "+", "스킬 더블 건호크 % 35.60", 
				"+", "스킬 더블 건호크 % 45.60", "+", "스킬 더블 건호크 % 65.37", "+", "스킬 더블 건호크 % 76.81", null,
				
				///공용스킬
				"고대의 기억", Skill_type.BUF_ACTIVE, Character_type.ALL, 15, 20, 10, 3,
				"1", "지능 15", "+", "지능 +15", "반복 1",
				"물리 크리티컬 히트", Skill_type.PASSIVE, "", 20, 20, 10, 3,
				"1", "물크 1", "+", "물크 +1", "반복 1",
				"마법 크리티컬 히트", Skill_type.PASSIVE, "", 20, 20, 10, 3,
				"1", "마크 1", "+", "마크 +1", "반복 1",
			
		};
		
		return data;
	}
	
	public static Object[] skillInfo_mage()
	{
		FunctionStat fStat[] = new FunctionStat[20];
		
		//원소폭격
		fStat[0] = new FunctionStat(){
			private static final long serialVersionUID = 1;
			@Override
			public StatusList function(Characters character, Monster monster, Object item) {
				Skill skill = (Skill)item;
				int level = skill.getSkillLevel(true, character.isBurning());
				StatusList statList = new StatusList();
				for(Skill s : character.getSkillList())
					if(s.getName().equals("검은 눈"))
						if(s.getActiveEnabled()){
							for(Skill dSkill : character.getSkillList()){
								if(dSkill.hasDamage()){
									switch(dSkill.getName()){
									case "속성발동":
										break;
									case "파이어 로드": case "아이스 크리스탈 샤워": case "엘레멘탈 캐넌":
										statList.addSkill_damage(dSkill.getName(), 13+level);
										break;
									default:
										statList.addSkill_damage(dSkill.getName(), 25+2*level);
										break;
									}
								}
							}
						}
				return statList;
			}
		};
		
		Object[] data = new Object[] {
				/////////////우럭
				/////액티브
				"크리스탈 어택", Skill_type.ACTIVE, Job.ELEMENTALBOMBER, 20, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_e,
				"36 0 0 1705*7 0", "+ 1740*7", "+ 1788*7", "+ 1817*7", "+ 1854*7", null,
				"파이어 로드", "", "", 25, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_e,
				"33 0 0 1032*12 0", "+ 1059*12", "+ 1083*12", "+ 1107*12", "+ 1133*12", "+ 1157*12", null,
				"체인 라이트닝", "", "", 30, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_e,
				"31 0 0 4242*4 0", "+ 4350*4", "+ 4458*4", "+ 4563*4", "+ 4669*4", null,
				"암전", "", "", 30, 60, 50, 2, Element_type.DARKNESS, CalculatorVersion.VER_1_0_d,
				"31 0 0 15251 0", "+ 15630", "+ 16017", "+ 16401", "+ 16783", null,
				"아이스 크리스탈 샤워", "", "", 35, 60, 50, 2, Element_type.WATER, CalculatorVersion.VER_1_0_d,
				"28 0 0 1241*14 0", "+ 1276*14", "+ 1310*14", "+ 1342*14", "+ 1377*14", "+ 1411*14", null,
				"플레임 서클", "", "", 35, 60, 50, 2, Element_type.FIRE, CalculatorVersion.VER_1_0_d,
				"28 0 0 1914*6+7654 0", "+ 1965*6+7863", "+ 2016*6+8071", "+ 2069*6+8280", "+ 2122*6+8489", "+ 2174*6+8697", null,
				"라이트닝 월", "", "", 40, 60, 50, 2, Element_type.LIGHT, CalculatorVersion.VER_1_0_d,
				"26 0 0 15025 0", "+ 15456", "+ 15893", "+ 16327", "+ 16757", null,
				"다크니스 맨틀", "", "", 40, 60, 50, 2, Element_type.DARKNESS, CalculatorVersion.VER_1_0_d,
				"26 0 0 20620 0", "+ 21224", "+ 21813", "+ 22411", "+ 23004", null,
				"엘레멘탈 레인", Skill_type.ACTIVE, "", 45, 60, 50, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d, "설명 검은 눈의 모속적용이 기본으로 되어있습니다",
				"23 0 0 902*45+3701+12213 0", "+ 929*45+3817+12598", "+ 957*45+3934+12982", "+ 986*45+4051+13369", "+ 1014*45+4170+13756", "+ 1043*45+4286+14142", null,
				"엘레멘탈 버스터", "", "", 50, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_0_d, "설명 검은 눈의 모속적용이 기본으로 되어있습니다",
				"9 0 0 3916*30 0", "+ 4225*30", "+ 4534*30", "+ 4841*30", null,
				"컨센트레이트", Skill_type.ACTIVE, "", 60, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"16 0 0 7297+17028 0", "+ 7592+17712", "+ 7886+18396", "+ 8179+19083", null,
				"엘레멘탈 스트라이크", Skill_type.ACTIVE, "", 70, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"11 0 0 1865+35424 0", "+ 1958+37210", "+ 2053+38991", "+ 2148+40776", null,
				"엘레멘탈 필드", Skill_type.DAMAGE_BUF, "", 75, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"8 0 0 35737 0", "화속깍 40 & 수속깍 40 & 명속깍 40 & 암속깍 40", "+ 37857", "화속깍 +2 & 수속깍 +2 & 명속깍 +2 & 암속깍 +2", "+ 39978", "화속깍 +2 & 수속깍 +2 & 명속깍 +2 & 암속깍 +2",
				"+ 42098", "화속깍 +2 & 수속깍 +2 & 명속깍 +2 & 암속깍 +2", "+ 44217", "화속깍 +2 & 수속깍 +2 & 명속깍 +2 & 암속깍 +2", null,
				"컨버젼스 캐넌", Skill_type.ACTIVE, "", 80, 40, 30, 2, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"6 0 0 56526 0", "+ 60330", "+ 64135", "+ 67940", null,
				"아마겟돈 스트라이크", Skill_type.ACTIVE, "", 85, 40, 30, 5, Element_type.ALL, CalculatorVersion.VER_1_0_d,
				"2 0 0 4988+416*13+89798 0", "+ 5928+494*13+106701", "+ 6867+571*13+123604", "+ 7806+651*13+140507", "+ 8745+729*13+157412", "+ 9684+808*13+174313", null,
				"속성 발동", "", "", 15, 60, 50, 5, Element_type.ALL, CalculatorVersion.VER_1_0_e,
				"16 0 0 1161 0", "+ 1221", "+ 1281", "+ 1341", null,
				"엘레멘탈 캐넌", "", "", 15, 11, 1, 1, Element_type.ALL, CalculatorVersion.VER_1_0_e,
				"1", "귀속/속성 발동/500", "+", "귀속/속성 발동/+10", "반복 1",
				
				/////패시브
				"원소 폭격", Skill_type.BUF_ACTIVE, "", 30, 5, 5, 5, CalculatorVersion.VER_1_0_d, "설명 검은 눈의 효과 적용",
				"5", fStat[0], "스킬 체인 라이트닝 % 10", null, 
				"페이탈 엘레멘트", Skill_type.PASSIVE, "", 48, 40, 30, 3, CalculatorVersion.VER_1_0_d,
				"15", "크증버프 24 & 마크 10.0", "+", "크증버프 +1.5 & 마크 +0.5", "반복 1",
				"검은 눈", "", "", 75, 40, 30, 3, CalculatorVersion.VER_1_0_d, "설명 원소폭격 사용 시 스킬 데미지 증가",
				"6", "", "+", "", null,
				"엘레멘탈 포스", Skill_type.BUF_ACTIVE, "", 20, 11, 1, 1, CalculatorVersion.VER_1_0_d,
				"1", "스킬 엘레멘탈 캐넌 % 50", "+", "스킬 엘레멘탈 캐넌 % +10", "반복 1",
				"마나 폭주", Skill_type.SWITCHING, "", 25, 30, 20, 2, CalculatorVersion.VER_1_0_d,
				"20", "스증뎀 42", null,
				"엘레멘탈 실드", Skill_type.BUF_ACTIVE, "", 25, 20, 10, 2, CalculatorVersion.VER_1_0_d,
				"10", "모속강 26", "+", "모속강 +2", "반복 1",
				
				//TP
				"속성 발동 강화", "속성 발동", "", 50, 7, 5, 8, CalculatorVersion.VER_1_0_d, null,
				"체인 라이트닝 강화", "체인 라이트닝", "", 55, 7, 5, 20, CalculatorVersion.VER_1_0_d, null,
				"크리스탈 어택 강화", "크리스탈 어택", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"암전 강화", "암전", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"아이스 크리스탈 샤워 강화", "아이스 크리스탈 샤워", "", 55, 1, 1, 0, CalculatorVersion.VER_1_0_d, "설명 찍으면 버그로 딜이 감소한답니다 세상에", null,
				"파이어 로드 강화", "파이어 로드", "", 55, 7, 5, 4, CalculatorVersion.VER_1_0_d, null,
				"플레임 서클 강화", "플레임 서클", "", 55, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"다크니스 맨틀 강화", "다크니스 맨틀", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"라이트닝 월 강화", "라이트닝 월", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null,
				"엘레멘탈 레인 강화", "엘레멘탈 레인", "", 65, 7, 5, 10, CalculatorVersion.VER_1_0_d, null
				
				////////////배메
				//////액티브
				
		};
		
		return data;
	}
}
