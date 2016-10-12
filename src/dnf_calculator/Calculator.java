package dnf_calculator;

import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_class.Characters;
import dnf_class.Monster;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;

public class Calculator {
	
	public static long getDamage(Skill skill, Monster objcet, Characters character, int mode)
	{
		long deal=0;
		SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true, character.isBurning());
		if(skillInfo.hasPhy_per()) deal += Calculator.percentDamage_physical(skillInfo.phy_atk, skill.element, objcet, character, mode);
		if(skillInfo.hasPhy_fix()) deal += Calculator.fixedDamage_physical(skillInfo.phy_fix, skill.element, objcet, character, mode);
		if(skillInfo.hasMag_per()) deal += Calculator.percentDamage_magical(skillInfo.mag_atk, skill.element, objcet, character, mode);
		if(skillInfo.hasMag_fix()) deal += Calculator.fixedDamage_magical(skillInfo.phy_atk, skill.element, objcet, character, mode);
		
		return deal;
	}
	public static long getDamage(Skill skill, Monster object, Characters character)
	{ return getDamage(skill, object, character, 1); }
	
	
	public static long percentDamage_physical(int skillPercent, Element_type element, Monster object, Characters character, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			double dec_defence=(1-getPhysicalPercentDefence(character, object));							// 방어력에 의해 감소하는 비율
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			double inc_strength=1+stat.getStat(StatList.STR)*(100.0+stat.getStat(StatList.STR_INC))/100.0/250;					// 1+힘*(100+힘뻥)/100/250
			int inc_weapon1=(int)((stat.getStat(StatList.WEP_PHY)*(100+stat.getStat(StatList.MAST_PHY_2)))/100);				// [무기물공*(100+마스터리2)/100]
			int inc_weapon2=(int)((inc_weapon1*(stat.getStat(StatList.MAST_PHY)+100))/100);										// [[무기물공*(100+마스터리2)/100]*(100+마스터리1)/100]
			int defIgnore=(int)((stat.getStat(StatList.WEP_NODEF_PHY)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100);	
			defIgnore=(int) (defIgnore*(100+stat.getStat(StatList.WEP_NODEF_PHY_INC))/100);										// 방어무시데미지
			double frontATK=inc_strength*inc_weapon2*dec_defence*elementCal.get_inc_elem()+defIgnore;							// (힘*물공)*방어력*속강+방무뎀
			
			double inc_critical = getIncCrt(object, stat);
			
			if(mode==1)//average damage mode
				return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal));
			else // I DONT KNOW, SOMEWHAT ELSE
				return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal)); //TODO CHANGE TO ANOTHER MODE
		}
		catch(StatusTypeMismatch e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	public static long percentDamage_physical(int skillPercent, Element_type element, Monster object, Characters character)
	{ return percentDamage_physical(skillPercent, element, object, character, 1); }
	
	
	public static long fixedDamage_physical(double skillValue, Element_type element, Monster object, Characters character, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			double dec_defence=(1-getPhysicalPercentDefence(character, object));							// 방어력에 의해 감소하는 비율
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			double inc_strength=1+stat.getStat(StatList.STR)*(100.0+stat.getStat(StatList.STR_INC))/100.0/250;		// 1+힘*(100+힘뻥)/100/250
			int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(double)(100+stat.getStat(StatList.MAST_IND))/100
					+(double)stat.getStat(StatList.WEP_IND_REFORGE));										// 독공*독공뻥+재련 
			double frontATK=inc_strength*inc_indep*dec_defence*elementCal.get_inc_elem();					// 힘*독공*방어력*속강
			
			double inc_critical = getIncCrt(object, stat);
			
			
			if(mode==1)//average damage mode
				return (long)((double)skillValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal));
			else // I DONT KNOW, SOMEWHAT ELSE
				return (long)((double)skillValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal)); //TODO CHANGE TO ANOTHER MODE
		} 
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static long fixedDamage_physical(double skillValue, Element_type element , Monster object, Characters character)
	{ return fixedDamage_physical(skillValue, element, object, character, 1); }
	
	
	public static long percentDamage_magical(int skillPercent, Element_type element, Monster object, Characters character, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			double dec_defence=(1-getMagicalPercentDefence(character, object));							// 방어력에 의해 감소하는 비율
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			double inc_strength=1+stat.getStat(StatList.INT)*(100.0+stat.getStat(StatList.INT_INC))/100.0/250;									// 1+지능/250
			int inc_weapon1=(int)((stat.getStat(StatList.WEP_MAG)*(100+stat.getStat(StatList.MAST_MAG_2)))/100);					// [무기마공*(100+마스터리2)/100]
			int inc_weapon2=(int)((inc_weapon1*(stat.getStat(StatList.MAST_MAG)+100))/100);											// [[무기마공*(100+마스터리2)/100]*(100+마스터리1)/100]
			int defIgnore=(int)((stat.getStat(StatList.WEP_NODEF_MAG)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100);
			defIgnore=(int) (defIgnore*(100+stat.getStat(StatList.WEP_NODEF_MAG_INC))/100);											// 방어무시데미지
			double frontATK=inc_strength*inc_weapon2*dec_defence*elementCal.get_inc_elem()+defIgnore;								// (지능*마공)*방어력*속강+방무뎀
			
			double inc_critical = getIncCrt(object, stat);
			
			if(mode==1)//average damage mode
				return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal));
			else // I DONT KNOW, SOMEWHAT ELSE
				return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal)); //TODO CHANGE TO ANOTHER MODE
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static long percentDamage_magical(int skillPercent, Element_type element, Monster object, Characters character)
	{ return percentDamage_magical(skillPercent, element, object, character, 1); } 
	
	
	public static long fixedDamage_magical(double skillValue, Element_type element, Monster object, Characters character, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			double dec_defence=(1-getMagicalPercentDefence(character, object));							// 방어력에 의해 감소하는 비율
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			double inc_strength=1+stat.getStat(StatList.INT)*(100.0+stat.getStat(StatList.INT_INC))/100.0/250;		// 1+지능/250
			int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(double)(100+stat.getStat(StatList.MAST_IND))/100
					+(double)stat.getStat(StatList.WEP_IND_REFORGE));										// 독공*독공뻥+재련 
			double frontATK=inc_strength*inc_indep*dec_defence*elementCal.get_inc_elem();					// 지능*독공*방어력*속강
			
			double inc_critical = getIncCrt(object, stat);
			
			if(mode==1)//average damage mode
				return (long)((double)skillValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal));
			else // I DONT KNOW, SOMEWHAT ELSE
				return (long)((double)skillValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal)); //TODO CHANGE TO ANOTHER MODE
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static long fixedDamage_magical(double skillValue, Element_type element, Monster object, Characters character)
	{ return fixedDamage_magical(skillValue, element, object, character, 1); } 
	

	private static double getIncCrt(Monster object, Status stat) throws StatusTypeMismatch
	{
		double avgCritical=stat.getStat(StatList.CRT_PHY)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_PHY);
		if(avgCritical>100.0) avgCritical=100.0;
		else if(avgCritical<0) avgCritical=0;
		double inc_critical;																				// 크증뎀항
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT_BACK)+stat.getStat(StatList.DAM_CRT_ADD))/100.0
					*(100.0+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;											// ((100-크리확률)+크리확률*1.5*(100+백크증뎀+크추증)/100*(100+크증버프)/100)/100
		else inc_critical=((100-avgCritical)+avgCritical*1.5*(100.0+stat.getStat(StatList.DAM_CRT)+stat.getStat(StatList.DAM_CRT_ADD))/100.0
					*(100+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;												// ((100-크리확률)+크리확률*1.5*(100+크증뎀+크추증)/100*(100+크증버프)/100)/100
		
		return inc_critical;
	}
	
	
	public static double damage_enhancing_avg(Status stat, Monster object, Characters character, CalculateElement elementCal) throws StatusTypeMismatch  	// 속강증크증스증추뎀카운터투함포기타등등
	{
		double inc_damage=(100.0+stat.getStat(StatList.BUF_INC))/100.0;										// 증뎀버프
		if(object.getBool(Monster_StatList.BACKATK) && stat.getStat(StatList.DAM_INC_BACK)>stat.getStat(StatList.DAM_INC))
			inc_damage*=(100.0+stat.getStat(StatList.DAM_INC_BACK)+stat.getStat(StatList.DAM_INC_ADD))/100.0;				// (100+증뎀+추증뎀)/100
		else inc_damage*=(100.0+stat.getStat(StatList.DAM_INC)+stat.getStat(StatList.DAM_INC_ADD))/100.0;					// (100+증뎀+추증뎀)/100
		
		//main variable :: inc_damage
		/////////////////////////////
		
		double inc_counter=1;
		if(object.getBool(Monster_StatList.COUNTER)) inc_counter=1.25;																// 카운터
	
		//main variable :: inc_counter
		//////////////////////////////
		
		double inc_add;																												// 추뎀항
		double add_whole=(100.0+stat.getStat(StatList.DAM_ADD)+stat.getStat(StatList.DAM_ADD_FIRE)*elementCal.get_inc_fire()
				+stat.getStat(StatList.DAM_ADD_WATER)*elementCal.get_inc_water()
				+stat.getStat(StatList.DAM_ADD_LIGHT)*elementCal.get_inc_light()
				+stat.getStat(StatList.DAM_ADD_DARKNESS)*elementCal.get_inc_darkness())/100.0;										// 추뎀+속추뎀 총합
		if(object.getBool(Monster_StatList.BACKATK)) add_whole+=stat.getStat(StatList.DAM_ADD_BACK);								// 백어택 추뎀
		double avgCritical_add=stat.getStat(StatList.CRT_LOW)+3.0;																	// 추뎀크리 확률
		if(avgCritical_add>100.0) avgCritical_add=100.0;
		else if(avgCritical_add<0) avgCritical_add=0;
		double inc_critical_add=((100.0-avgCritical_add)+avgCritical_add*1.5*(int)(100+stat.getStat(StatList.BUF_CRT))/100.0)/100.0;	// 추뎀크리 기댓값 적용
		
		inc_add=(double)((add_whole-1.0)*inc_critical_add*inc_counter+1.0);
		
		//main variable :: inc_add
		///////////////////////////////
		
		double inc_skill = (100.0+stat.getStat(StatList.DAM_SKILL))/100.0;											// 스증뎀
		double inc_buf = (100.0+stat.getStat(StatList.DAM_BUF))/100.0;												// 버프 증뎀(투함포, 이그니스)
		double inc_all = (100.0+stat.getStat(StatList.DAM_INC_ALL))/100.0;											// 모공증
		//main variable :: inc_skill, inc_buf
		//////////////////////////////
	
		return (double)(inc_damage*inc_counter*inc_add*inc_skill*inc_buf*inc_all);
	}
	
	public static double getPhysicalPercentDefence(Characters character, Monster object) throws StatusTypeMismatch							// 몹의 물리퍼센트 방어력 구하기
	{
		int level = character.getLevel();
		Status stat=character.dungeonStatus;
		
		int fixedDef=object.getStat(Monster_StatList.DEFENSIVE_PHY);												// 기본방어력
		fixedDef-=stat.getStat(StatList.DEF_DEC_FIXED_PHY);															// 기본방어력-고정방깍
		if(fixedDef<0) fixedDef=0;
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_PHY_SKILL))/100);						// (기본방어력-고정방깍)*(100-%방깍)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_PHY_ITEM)*(double)object.getStat(Monster_StatList.TYPE)/100.0)/100.0);		//(위)*(100-템%방깍*패널티(100~50)/100)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_IGN))/100);									// (위)*(100-적 방어력 무시)/100
		
		if(fixedDef<0) return 0;
		double result = ((double)(fixedDef))/((double)(fixedDef+level*200));										// %방어력=고정방어력/(고정방어력+레벨*200)
		return result >object.getDoubleStat(Monster_StatList.DEFENCE_LIMIT)/100 ? result : object.getDoubleStat(Monster_StatList.DEFENCE_LIMIT)/100;
	}
	
	public static double getMagicalPercentDefence(Characters character, Monster object) throws StatusTypeMismatch			// 몹의 마법퍼센트 방어력 구하기
	{
		int level = character.getLevel();
		Status stat=character.dungeonStatus;
		
		int fixedDef=object.getStat(Monster_StatList.DEFENSIVE_MAG);												// 기본방어력
		fixedDef-=stat.getStat(StatList.DEF_DEC_FIXED_MAG);															// 기본방어력-고정방깍
		if(fixedDef<0) fixedDef=0;
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_MAG_SKILL))/100);						// (기본방어력-고정방깍)*(100-%방깍)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_MAG_ITEM)*(double)object.getStat(Monster_StatList.TYPE)/100.0)/100.0);		//(위)*(100-템%방깍*패널티(100~50)/100)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_IGN))/100);									// (위)*(100-적 방어력 무시)/100
		
		if(fixedDef<0) return 0;
		double result = ((double)(fixedDef))/((double)(fixedDef+level*200));												// %방어력=고정방어력/(고정방어력+레벨*200)
		return result/100 >object.getDoubleStat(Monster_StatList.DEFENCE_LIMIT) ? result/100 : object.getDoubleStat(Monster_StatList.DEFENCE_LIMIT);
	}
	
	public static int getInfoStrength(Status stat)
	{
		try{
			return (int)(stat.getStat("힘")*(100.0+stat.getStat("힘뻥"))/100.0);
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	public static int getInfoIntellegence(Status stat)
	{
		try{
			return (int)(stat.getStat("지능")*(100.0+stat.getStat("지능뻥"))/100.0);
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	public static int getInfoPhysicalATK(Status stat)
	{
		try{
			int inc_weapon1=(int)((stat.getStat(StatList.WEP_PHY)*(100+stat.getStat(StatList.MAST_PHY_2)))/100);				// [무기물공*(100+마스터리2)/100]
			int inc_weapon2=(int)((inc_weapon1*(stat.getStat(StatList.MAST_PHY)+100))/100);										// [[무기물공*(100+마스터리2)/100]*(100+마스터리1)/100]
			return (int)(inc_weapon2*(1+getInfoStrength(stat)/250.0)+ stat.getStat("물리방무")*(100+stat.getStat(StatList.WEP_NODEF_PHY_INC))/100);
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	public static int getInfoMagicalATK(Status stat)
	{
		try{
			int inc_weapon1=(int)((stat.getStat(StatList.WEP_MAG)*(100+stat.getStat(StatList.MAST_MAG_2)))/100);				// [무기마공*(100+마스터리2)/100]
			int inc_weapon2=(int)((inc_weapon1*(stat.getStat(StatList.MAST_MAG)+100))/100);										// [[무기마공*(100+마스터리2)/100]*(100+마스터리1)/100]
			return (int)(inc_weapon2*(1+getInfoStrength(stat)/250.0)+ stat.getStat("마법방무")*(100+stat.getStat(StatList.WEP_NODEF_MAG_INC))/100 );
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	public static int getInfoIndependentATK(Status stat)
	{
		try{
			return (int)(stat.getStat("독립공격")*(100.0+stat.getStat("독공뻥"))/100.0+stat.getStat("재련독공"));
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public static int getInfoElementReinforce(Status stat, String element)
	{
		try{
			if(element.equals("화속성강화")) return (int)(stat.getStat("화속")+stat.getStat("모속강"));
			else if(element.equals("수속성강화")) return (int)(stat.getStat("수속")+stat.getStat("모속강"));
			else if(element.equals("명속성강화")) return (int)(stat.getStat("명속")+stat.getStat("모속강"));
			else if(element.equals("암속성강화")) return (int)(stat.getStat("암속")+stat.getStat("모속강"));
			else return -1;
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
			return -1;
		}
	}
}
