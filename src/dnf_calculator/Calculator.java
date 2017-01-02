package dnf_calculator;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_class.Characters;
import dnf_class.Monster;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;

public class Calculator {
	
	public static long getDamage(Skill skill, Monster object, Characters character, int mode)
	{
		long deal=0;
		if(skill.isOptionSkill() && !skill.getBuffEnabled()) return 0;
		SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true, character.isBurning());
		Status stat=character.dungeonStatus;
		if(skillInfo.hasPhy_per() && !noPhysicalDamage(stat, skillInfo)) 
			deal += Calculator.percentDamage_physical(skillInfo.phy_atk, skill.element, object, character, skillInfo.indep_level, mode);
		if(skillInfo.hasPhy_fix() && !noPhysicalDamage(stat, skillInfo))
			deal += Calculator.fixedDamage_physical(skillInfo.phy_fix, skill.element, object, character, skillInfo.indep_level, mode);
		if(skillInfo.hasMag_per() && !noMagicalDamage(stat, skillInfo))
			deal += Calculator.percentDamage_magical(skillInfo.mag_atk, skill.element, object, character, skillInfo.indep_level, mode);
		if(skillInfo.hasMag_fix() && !noMagicalDamage(stat, skillInfo))
			deal += Calculator.fixedDamage_magical(skillInfo.mag_fix, skill.element, object, character, skillInfo.indep_level, mode);
		
		for(Entry<String, Integer> entry : skillInfo.percentList.entrySet()){
			if((!skill.hasBuff() && skill.getActiveEnabled()) || skill.buffEnabled(true))
				deal+= (getDamage(character.characterInfoList.getSkill(entry.getKey()), object, character, mode)*entry.getValue()*skill.dungeonIncrease)/100;
		}
		
		return deal;
	}
	public static long getDamage(Skill skill, Monster object, Characters character)
	{ return getDamage(skill, object, character, 1); }
	
	
	public static long percentDamage_physical(int skillPercent, Element_type element, Monster object, Characters character, int indepLevel, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			double dec_defence=(1-getPhysicalPercentDefence(character, object, indepLevel));							// 방어력에 의해 감소하는 비율
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			int strength = (int) (stat.getStat(StatList.STR)*(100.0+stat.getStat(StatList.STR_INC))/100.0);
			double inc_strength=1+strength/250.0;																					// 1+힘*(100+힘뻥)/100/250
			int inc_weapon=(int)((stat.getStat(StatList.WEP_PHY)*(stat.getStat(StatList.MAST_PHY)+100))/100);										// [[무기물공*(100+마스터리2)/100]*(100+마스터리1)/100]
			inc_weapon=(int) (inc_weapon*(100+stat.getStat(StatList.MAST_PHY_ITEM))/100);
			double defIgnore=((stat.getStat(StatList.WEP_NODEF_PHY)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100);	
			defIgnore=(int) (defIgnore*(100+stat.getStat(StatList.WEP_NODEF_PHY_INC))/100);										// 방어무시데미지
			double frontATK=inc_strength*inc_weapon*dec_defence*elementCal.get_inc_elem()+defIgnore;							// (힘*물공)*방어력*속강+방무뎀
			
			double inc_critical = getIncCrt_phy(object, stat);
			
			return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal, mode));
		}
		catch(StatusTypeMismatch e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	public static long percentDamage_physical(int skillPercent, Element_type element, Monster object, Characters character, int indepLevel)
	{ return percentDamage_physical(skillPercent, element, object, character, indepLevel, 1); }
	
	
	public static long fixedDamage_physical(double skillValue, Element_type element, Monster object, Characters character, int indepLevel, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			double dec_defence=(1-getPhysicalPercentDefence(character, object, indepLevel));						// 방어력에 의해 감소하는 비율
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			int strength = (int) (stat.getStat(StatList.STR)*(100.0+stat.getStat(StatList.STR_INC))/100.0);
			double inc_strength=1+strength/250.0;																// 1+힘*(100+힘뻥)/100/250
			int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(100+stat.getStat(StatList.MAST_IND))/100*(100+stat.getStat(StatList.MAST_INDEP_ITEM))/100);
			//inc_indep+= (int)(stat.getStat(StatList.WEP_IND_REFORGE)*(100+stat.getStat(StatList.MAST_REFORGE))/100);		// 독공*독공뻥+재련 *재련뻥
			inc_indep+= (int)(stat.getStat(StatList.WEP_IND_REFORGE)*(100+stat.getStat(StatList.MAST_IND))/100);		// 독공*독공뻥+재련 *재련뻥												
			double frontATK=inc_strength*inc_indep*dec_defence*elementCal.get_inc_elem();					// 힘*독공*방어력*속강
			
			double inc_critical = getIncCrt_phy(object, stat);
			
			return (long)((double)skillValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal, mode));
		} 
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static long fixedDamage_physical(double skillValue, Element_type element , Monster object, Characters character, int indepLevel)
	{ return fixedDamage_physical(skillValue, element, object, character, indepLevel, 1); }
	
	
	public static long percentDamage_magical(int skillPercent, Element_type element, Monster object, Characters character, int indepLevel, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			double dec_defence=(1-getMagicalPercentDefence(character, object, indepLevel));							// 방어력에 의해 감소하는 비율
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			int strength = (int) (stat.getStat(StatList.INT)*(100.0+stat.getStat(StatList.INT_INC))/100.0);
			double inc_strength=1+strength/250.0;																						// 1+지능/250
			int inc_weapon=(int)((stat.getStat(StatList.WEP_MAG)*(stat.getStat(StatList.MAST_MAG)+100))/100);							// [[무기마공*(100+마스터리2)/100]*(100+마스터리1)/100]
			inc_weapon=(int) (inc_weapon*(100+stat.getStat(StatList.MAST_MAG_ITEM))/100);
			int defIgnore=(int)((stat.getStat(StatList.WEP_NODEF_MAG)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100);
			defIgnore=(int) (defIgnore*(100+stat.getStat(StatList.WEP_NODEF_MAG_INC))/100);											// 방어무시데미지
			double frontATK=inc_strength*inc_weapon*dec_defence*elementCal.get_inc_elem()+defIgnore;								// (지능*마공)*방어력*속강+방무뎀
			
			double inc_critical = getIncCrt_mag(object, stat);
			
			return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal, mode));
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static long percentDamage_magical(int skillPercent, Element_type element, Monster object, Characters character, int indepLevel)
	{ return percentDamage_magical(skillPercent, element, object, character, indepLevel, 1); } 
	
	
	public static long fixedDamage_magical(double skillValue, Element_type element, Monster object, Characters character, int indepLevel, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			double dec_defence=(1-getMagicalPercentDefence(character, object, indepLevel));							// 방어력에 의해 감소하는 비율
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			int strength = (int) (stat.getStat(StatList.INT)*(100.0+stat.getStat(StatList.INT_INC))/100.0);
			double inc_strength=1+strength/250.0;																// 1+지능/250
			int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(100+stat.getStat(StatList.MAST_IND))/100*(100+stat.getStat(StatList.MAST_INDEP_ITEM))/100);
			//inc_indep+= (int)(stat.getStat(StatList.WEP_IND_REFORGE)*(100+stat.getStat(StatList.MAST_REFORGE))/100);		// 독공*독공뻥+재련 *재련뻥
			inc_indep+= (int)(stat.getStat(StatList.WEP_IND_REFORGE)*(100+stat.getStat(StatList.MAST_IND))/100);		// 독공*독공뻥+재련 *재련뻥 
			double frontATK=inc_strength*inc_indep*dec_defence*elementCal.get_inc_elem();					// 지능*독공*방어력*속강
			
			double inc_critical = getIncCrt_mag(object, stat);

			return (long)((double)skillValue*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal, mode));
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
			return -1;
		}
	}
	public static long fixedDamage_magical(double skillValue, Element_type element, Monster object, Characters character, int indepLevel)
	{ return fixedDamage_magical(skillValue, element, object, character, indepLevel, 1); } 
	

	private static double getIncCrt_phy(Monster object, Status stat) throws StatusTypeMismatch
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
	
	private static double getIncCrt_mag(Monster object, Status stat) throws StatusTypeMismatch
	{
		double avgCritical=stat.getStat(StatList.CRT_MAG)+stat.getStat(StatList.CRT_LOW)+3;			// 3+크리+크리저항
		if(object.getBool(Monster_StatList.BACKATK)) avgCritical+=stat.getStat(StatList.CRT_BACK_MAG);
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
	
	private static double damage_enhancing_addDamageCrt(Status stat, Monster object, CalculateElement elementCal, double inc_counter, int mode) throws StatusTypeMismatch{
		double inc_add;																												// 추뎀항
		double add_whole=(100.0+stat.getStat(StatList.DAM_ADD));
		if(object.getStat(Monster_StatList.DEFENSIVE_PHY)!=0){
			add_whole+=stat.getStat(StatList.DAM_ADD_FIRE)*elementCal.get_inc_fire()
					+stat.getStat(StatList.DAM_ADD_WATER)*elementCal.get_inc_water()
					+stat.getStat(StatList.DAM_ADD_LIGHT)*elementCal.get_inc_light()
					+stat.getStat(StatList.DAM_ADD_DARKNESS)*elementCal.get_inc_darkness();										// 추뎀+속추뎀 총합
		}
		add_whole/=100.0;
		if(object.getBool(Monster_StatList.BACKATK)) add_whole+=stat.getStat(StatList.DAM_ADD_BACK);								// 백어택 추뎀
		double avgCritical_add=stat.getStat(StatList.CRT_LOW)+3.0;																	// 추뎀크리 확률
		if(avgCritical_add>100.0 || mode==3) avgCritical_add=100.0;
		else if(avgCritical_add<0 || mode==2) avgCritical_add=0;
		double inc_critical_add=((100.0-avgCritical_add)+avgCritical_add*1.5)/100.0;	// 추뎀크리 기댓값 적용
		
		inc_add=(double)((add_whole-1.0)*inc_critical_add*inc_counter+1.0);
		//main variable :: inc_add
		///////////////////////////////
		
		return inc_add;
	}
	
	public static double damage_enhancing_avg(Status stat, Monster object, Characters character, CalculateElement elementCal, int mode) throws StatusTypeMismatch  	// 속강증크증스증추뎀카운터투함포기타등등
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
		
		double inc_skill = (100.0+stat.getStat(StatList.DAM_SKILL))/100.0;											// 스증뎀
		double inc_buf = (100.0+stat.getStat(StatList.DAM_BUF))/100.0;												// 버프 증뎀(투함포, 이그니스)
		double inc_all = (100.0+stat.getStat(StatList.DAM_INC_ALL))/100.0;											// 모공증
		//main variable :: inc_skill, inc_buf
		//////////////////////////////
		
		double inc_add = damage_enhancing_addDamageCrt(stat, object, elementCal, inc_counter, mode);
	
		return (double)(inc_damage*inc_counter*inc_add*inc_skill*inc_buf*inc_all);
	}
	public static double getPhysicalPercentDefence(Characters character, Monster object, int indepLevel) throws StatusTypeMismatch
	{
		Status stat=character.dungeonStatus;
		
		double fixedDef=object.getStat(Monster_StatList.DEFENSIVE_PHY);												// 기본방어력
		fixedDef-=stat.getStat(StatList.DEF_DEC_FIXED_PHY);															// 기본방어력-고정방깍
		if(fixedDef<0) fixedDef=0;
		fixedDef=(int)fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_PHY_SKILL))/100;								// (기본방어력-고정방깍)*(100-%방깍)/100
		fixedDef=(int)fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_PHY_ITEM)*(double)object.getStat(Monster_StatList.TYPE)/100.0)/100.0;		//(위)*(100-템%방깍*패널티(100~50)/100)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_IGN))/100);											// (위)*(100-적 방어력 무시)/100
		
		if(fixedDef<0) return 0;
		double result;
		if(indepLevel>0) result= ((double)(fixedDef))/((double)(fixedDef+indepLevel*200));										// %방어력=고정방어력/(고정방어력+독오레벨*200)
		else result= ((double)(fixedDef))/((double)(fixedDef+character.getLevel()*200));										// %방어력=고정방어력/(고정방어력+레벨*200)
		return result >object.getDoubleStat(Monster_StatList.DEFENCE_LIMIT)/100 ? result : object.getDoubleStat(Monster_StatList.DEFENCE_LIMIT)/100;
	}
	public static double getPhysicalPercentDefence(Characters character, Monster object) throws StatusTypeMismatch							// 몹의 물리퍼센트 방어력 구하기
	{
		return getPhysicalPercentDefence(character, object, character.getLevel());
	}
	
	public static double getMagicalPercentDefence(Characters character, Monster object, int indepLevel) throws StatusTypeMismatch
	{
		Status stat=character.dungeonStatus;
		
		int fixedDef=object.getStat(Monster_StatList.DEFENSIVE_MAG);												// 기본방어력
		fixedDef-=stat.getStat(StatList.DEF_DEC_FIXED_MAG);															// 기본방어력-고정방깍
		if(fixedDef<0) fixedDef=0;
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_MAG_SKILL))/100);						// (기본방어력-고정방깍)*(100-%방깍)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_PERCENT_MAG_ITEM)*(double)object.getStat(Monster_StatList.TYPE)/100.0)/100.0);		//(위)*(100-템%방깍*패널티(100~50)/100)/100
		fixedDef=(int)(fixedDef*(100.0-stat.getStat(StatList.DEF_DEC_IGN))/100);									// (위)*(100-적 방어력 무시)/100
		
		if(fixedDef<0) return 0;
		double result;
		if(indepLevel>0) result= ((double)(fixedDef))/((double)(fixedDef+indepLevel*200));										// %방어력=고정방어력/(고정방어력+독오레벨*200)
		else result= ((double)(fixedDef))/((double)(fixedDef+character.getLevel()*200));										// %방어력=고정방어력/(고정방어력+레벨*200)
		return result >object.getDoubleStat(Monster_StatList.DEFENCE_LIMIT)/100 ? result : object.getDoubleStat(Monster_StatList.DEFENCE_LIMIT)/100;
	}
	public static double getMagicalPercentDefence(Characters character, Monster object) throws StatusTypeMismatch			// 몹의 마법퍼센트 방어력 구하기
	{
		return getMagicalPercentDefence(character, object, character.getLevel());
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
			int inc_weapon1=(int)(stat.getStat(StatList.WEP_PHY));						// [무기물공*(100+마스터리2)/100]
			int inc_weapon2=(int)( (inc_weapon1*(stat.getStat(StatList.MAST_PHY)+100))/100 + 0.9999);							// [[무기물공*(100+마스터리2)/100]*(100+마스터리1)/100] - 소숫점포함
			inc_weapon2=(int) (inc_weapon2*(100+stat.getStat(StatList.MAST_PHY_ITEM))/100);
			return (int)((inc_weapon2*(1+getInfoStrength(stat)/250.0)+ stat.getStat("물리방무")*(100+stat.getStat(StatList.WEP_NODEF_PHY_INC))/100)*(100+stat.getStat("투함포항"))/100);
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
			int inc_weapon1=(int)(stat.getStat(StatList.WEP_MAG));						// [무기마공*(100+마스터리2)/100]
			int inc_weapon2=(int)( (inc_weapon1*(stat.getStat(StatList.MAST_MAG)+100))/100 + 0.9999);							// [[무기마공*(100+마스터리2)/100]*(100+마스터리1)/100] - 소숫점포함
			inc_weapon2=(int) (inc_weapon2*(100+stat.getStat(StatList.MAST_MAG_ITEM))/100);
			return (int)((inc_weapon2*(1+getInfoIntellegence(stat)/250.0)+ stat.getStat("마법방무")*(100+stat.getStat(StatList.WEP_NODEF_MAG_INC))/100)*(100+stat.getStat("투함포항"))/100);
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
			int inc_indep=(int)(stat.getStat(StatList.WEP_IND)*(100+stat.getStat(StatList.MAST_IND))/100*(100+stat.getStat(StatList.MAST_INDEP_ITEM))/100);
			inc_indep+= (int)(stat.getStat(StatList.WEP_IND_REFORGE)*(100+stat.getStat(StatList.MAST_IND))/100);		// 독공*독공뻥+재련 *재련뻥
			return (int) (inc_indep*(100+stat.getStat("투함포항"))/100);
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
	
	
	public static long percentDamage_physical_ign(int skillPercent, Element_type element, Monster object, Characters character, int indepLevel, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			double defIgnore=((stat.getStat(StatList.WEP_NODEF_PHY)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100);	
			defIgnore=(int) (defIgnore*(100+stat.getStat(StatList.WEP_NODEF_PHY_INC))/100);										// 방어무시데미지
			double frontATK=defIgnore;
			double inc_critical = getIncCrt_phy(object, stat);
			
			return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal, mode));
		}
		catch(StatusTypeMismatch e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public static long percentDamage_magical_ign(int skillPercent, Element_type element, Monster object, Characters character, int indepLevel, int mode)
	{
		//calculate with status
		try{
			Status stat=character.dungeonStatus;
			CalculateElement elementCal = new CalculateElement(object, stat, element);								// 각 속강별 데미지 계산
			
			double defIgnore=((stat.getStat(StatList.WEP_NODEF_MAG)*(100-object.getStat(Monster_StatList.DIFFICULTY)))/100);	
			defIgnore=(int) (defIgnore*(100+stat.getStat(StatList.WEP_NODEF_MAG_INC))/100);										// 방어무시데미지
			double frontATK=defIgnore;
			double inc_critical = getIncCrt_mag(object, stat);
			
			return (long)(skillPercent/100.0*frontATK*inc_critical*damage_enhancing_avg(stat, object, character, elementCal, mode));
		}
		catch(StatusTypeMismatch e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public static LinkedList<Entry<String, Double>> evaluateStatus(Skill skill, Monster object, Characters character, double skillInc)
	{
		LinkedList<Entry<String, Double>> result = new LinkedList<Entry<String, Double>>();		
		SkillLevelInfo skillInfo = skill.getSkillLevelInfo(true, character.isBurning());
		Status stat=character.dungeonStatus;
		
		double phy_per, phy_per_ign, phy_fix, mag_per, mag_per_ign, mag_fix, deal;
		double str, str_inc, intel, intel_inc, phy_atk, phy_atk_inc, mag_atk, mag_atk_inc;
		double ind, ind_inc, reforge, add_inc, all_inc, crt_inc, crt;
		double p, a, c, k, K1, K2;
		CalculateElement elementCal;
		skillInc /= 100;
		
		try{
			str = stat.getStat(StatList.STR); str_inc = 1+stat.getStat(StatList.STR_INC)/100;
			intel = stat.getStat(StatList.INT); intel_inc = 1+stat.getStat(StatList.INT_INC)/100;
			phy_atk = stat.getStat(StatList.WEP_PHY); phy_atk_inc = 1+stat.getStat(StatList.MAST_PHY_ITEM)/100;
			mag_atk = stat.getStat(StatList.WEP_MAG); mag_atk_inc = 1+stat.getStat(StatList.MAST_MAG_ITEM)/100;
			ind = stat.getStat(StatList.WEP_IND); ind_inc = 1+stat.getStat(StatList.MAST_INDEP_ITEM)/100;
			reforge = stat.getStat(StatList.WEP_IND_REFORGE);
			add_inc = 1+stat.getStat(StatList.DAM_INC)/100+stat.getStat(StatList.DAM_INC_ADD)/100;
			crt_inc = 1+stat.getStat(StatList.DAM_CRT)/100+stat.getStat(StatList.DAM_CRT_ADD)/100;
			all_inc = 1+stat.getStat(StatList.DAM_INC_ALL)/100;
			elementCal = new CalculateElement(object, stat, skill.element);
			if(skillInfo.hasPhy_fix() || skillInfo.hasPhy_per())
				crt = 3+stat.getStat(StatList.CRT_PHY)+stat.getStat(StatList.CRT_LOW);
			else crt = 3+stat.getStat(StatList.CRT_MAG)+stat.getStat(StatList.CRT_LOW);
			if(crt>100) crt=1;
			else if(crt<0) crt=0;
			else crt/=100;
			
			phy_per_ign = Calculator.percentDamage_physical_ign(skillInfo.phy_atk, skill.element, object, character, skillInfo.indep_level, 1);
			mag_per_ign = Calculator.percentDamage_magical_ign(skillInfo.mag_atk, skill.element, object, character, skillInfo.indep_level, 1);
			phy_per = Calculator.percentDamage_physical(skillInfo.phy_atk, skill.element, object, character, skillInfo.indep_level, 1)-phy_per_ign;
			mag_per = Calculator.percentDamage_magical(skillInfo.mag_atk, skill.element, object, character, skillInfo.indep_level, 1)-mag_per_ign;
			phy_fix = Calculator.fixedDamage_physical(skillInfo.phy_fix, skill.element, object, character, skillInfo.indep_level, 1);
			mag_fix = Calculator.fixedDamage_magical(skillInfo.mag_fix, skill.element, object, character, skillInfo.indep_level, 1);
			if(noPhysicalDamage(stat, skillInfo)){
				phy_per=0; phy_per_ign=0; phy_fix=0;
			}
			if(noMagicalDamage(stat, skillInfo)){
				mag_per=0; mag_per_ign=0; mag_fix=0;
			}
			
			deal = phy_per_ign+mag_per_ign+phy_per+mag_per+phy_fix+mag_fix;
			
			phy_per_ign/=deal; mag_per_ign/=deal; phy_per/=deal; mag_per/=deal; phy_fix/=deal; mag_fix/=deal;
			
			if(Double.compare(phy_per+phy_fix, 0)!=0){
				result.add(new AbstractMap.SimpleEntry<String, Double>("힘", skillInc/(phy_per+phy_fix)*(str*str_inc/250+1)/(str_inc/250)));
				result.add(new AbstractMap.SimpleEntry<String, Double>("힘 % 증가", skillInc/(phy_per+phy_fix)*(str*str_inc/250+1)/(str/250)*100));
			}
			if(Double.compare(mag_per+mag_fix, 0)!=0){
				result.add(new AbstractMap.SimpleEntry<String, Double>("지능", skillInc/(mag_per+mag_fix)*(intel*intel_inc/250+1)/(intel_inc/250)));
				result.add(new AbstractMap.SimpleEntry<String, Double>("지능 % 증가", skillInc/(mag_per+mag_fix)*(intel*intel_inc/250+1)/(intel/250)*100));
			}
			if(Double.compare(phy_per, 0)!=0){
				result.add(new AbstractMap.SimpleEntry<String, Double>("무기물공", skillInc/phy_per*phy_atk));
				result.add(new AbstractMap.SimpleEntry<String, Double>("물공 % 증가", skillInc/phy_per*phy_atk_inc*100));
			}
			if(Double.compare(mag_per, 0)!=0){
				result.add(new AbstractMap.SimpleEntry<String, Double>("무기마공", skillInc/mag_per*mag_atk));
				result.add(new AbstractMap.SimpleEntry<String, Double>("마공 % 증가", skillInc/mag_per*mag_atk_inc*100));
			}
			if(Double.compare(phy_fix+mag_fix, 0)!=0){
				result.add(new AbstractMap.SimpleEntry<String, Double>("독공", skillInc/(phy_fix+mag_fix)*(ind*ind_inc+reforge)/ind_inc));
				result.add(new AbstractMap.SimpleEntry<String, Double>("독공 % 증가", skillInc/(phy_fix+mag_fix)*(ind*ind_inc+reforge)/ind*100));
				result.add(new AbstractMap.SimpleEntry<String, Double>("재련독공", skillInc/(phy_fix+mag_fix)*(ind*ind_inc+reforge)));
			}
			result.add(new AbstractMap.SimpleEntry<String, Double>("데미지 추가증가", skillInc*add_inc*100));
			result.add(new AbstractMap.SimpleEntry<String, Double>("모든 공격력 증가", skillInc*all_inc*100));
			result.add(new AbstractMap.SimpleEntry<String, Double>("크리티컬 데미지 추가증가", skillInc*(crt*1.5*crt_inc+1-crt)/(1.5*crt)*100));
			
			p = phy_per+mag_per+phy_fix+mag_fix;
			c = 1;
			if(object.getBool(Monster_StatList.COUNTER)) c=1.25;
			c*=(1+0.5*(3+stat.getStat(StatList.CRT_LOW))/100);
			
			a = (stat.getStat(StatList.DAM_ADD)+stat.getStat(StatList.DAM_ADD_FIRE)*elementCal.get_inc_fire()
					+stat.getStat(StatList.DAM_ADD_WATER)*elementCal.get_inc_water()+stat.getStat(StatList.DAM_ADD_LIGHT)*elementCal.get_inc_light()
					+stat.getStat(StatList.DAM_ADD_DARKNESS)*elementCal.get_inc_darkness())/100;
			K1 = (stat.getStat(StatList.DAM_ADD_FIRE)+stat.getStat(StatList.DAM_ADD_WATER)
					+stat.getStat(StatList.DAM_ADD_LIGHT)+stat.getStat(StatList.DAM_ADD_DARKNESS))/100;
			switch(elementCal.get_type()){
			case FIRE:
				K2 = stat.getStat(StatList.DAM_ADD_FIRE)/100;
				if(character.getJob()==Job.LAUNCHER_F || character.getJob()==Job.LAUNCHER_M)
					K2 += stat.getStat(StatList.DAM_ADD_LIGHT)/100;
				break;
			case WATER: K2 = stat.getStat(StatList.DAM_ADD_WATER)/100; break;
			case LIGHT:
				K2 = stat.getStat(StatList.DAM_ADD_LIGHT)/100;
				if(character.getJob()==Job.LAUNCHER_F || character.getJob()==Job.LAUNCHER_M)
					K2 += stat.getStat(StatList.DAM_ADD_FIRE)/100;
				break;
			case DARKNESS: K2 = stat.getStat(StatList.DAM_ADD_DARKNESS)/100; break;
			default: K2=0;
			}
			k = 1/elementCal.get_inc_elem();
			
			if(elementCal.get_type()!=Element_type.NONE){
				result.add(new AbstractMap.SimpleEntry<String, Double>("주 속성강화", calculateElementValue(a, c, p, k, K2, skillInc)));
				result.add(new AbstractMap.SimpleEntry<String, Double>("모든 속성강화", calculateElementValue(a, c, p, k, K1, skillInc)));
			}
			
			result.add(new AbstractMap.SimpleEntry<String, Double>("추뎀", skillInc*(1+c*a)/c*100));
			result.add(new AbstractMap.SimpleEntry<String, Double>("주속성 속추뎀", skillInc*(1+c*a)/(c*elementCal.get_inc_elem())*100));
			
			skillInfo = skill.getSkillLevelInfo(skill.getSkillLevel(true, character.isBurning())+1);
			if(skillInfo!=null){
				phy_per = Calculator.percentDamage_physical(skillInfo.phy_atk, skill.element, object, character, skillInfo.indep_level, 1);
				mag_per = Calculator.percentDamage_magical(skillInfo.mag_atk, skill.element, object, character, skillInfo.indep_level, 1);
				phy_fix = Calculator.fixedDamage_physical(skillInfo.phy_fix, skill.element, object, character, skillInfo.indep_level, 1);
				mag_fix = Calculator.fixedDamage_magical(skillInfo.mag_fix, skill.element, object, character, skillInfo.indep_level, 1);
				if(noPhysicalDamage(stat, skillInfo)){
					phy_per=0; phy_fix=0;
				}
				if(noMagicalDamage(stat, skillInfo)){
					mag_per=0; mag_fix=0;
				}
				result.add(new AbstractMap.SimpleEntry<String, Double>("스킬 레벨", skillInc/((phy_per+mag_per+phy_fix+mag_fix)/deal-1)));
			}
			
		} catch(StatusTypeMismatch e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static double calculateElementValue(double a, double c, double p, double k, double K, double skillIncrease){
		if(K==0) return skillIncrease/0.0045/k/p;
		double A = 0.0045*0.0045*c*p*k*K;
		double B = 0.0045*c*K+0.0045*k*p+a*0.0045*c*k*p;
		double C = -skillIncrease*(1+c*a);
		
		double result = (-B+Math.sqrt(B*B-4*A*C))/(2*A);
		return result;
	}
	
	public static boolean noPhysicalDamage(Status stat, SkillLevelInfo skillInfo){
		try {
			if(stat.getEnabled(StatList.CONVERSION_NOPHY) && (skillInfo.hasMag_per() || skillInfo.hasMag_fix()))
				return true;
			else return false;
		} catch (StatusTypeMismatch e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean noMagicalDamage(Status stat, SkillLevelInfo skillInfo){
		try {
			if(stat.getEnabled(StatList.CONVERSION_NOMAG) && (skillInfo.hasPhy_per() || skillInfo.hasPhy_fix()))
				return true;
			else return false;
		} catch (StatusTypeMismatch e) {
			e.printStackTrace();
			return false;
		}
	}
}
