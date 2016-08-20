package dnf_calculator;

public class Calculator {
	
	public static long percentDamage_physical_avg(int skillPercent, Monster object, Char character)
	{
		//calculate with status
		long damage=skillPercent;
		Status.PublicStatus stat=character.finalStatus.new PublicStatus();
		
		double inc_strength=1+stat.getStat(StatList.STR)*stat.getStat(StatList.STR_INC)/250;			// 1+힘/250
		int inc_weapon1=(stat.getStat(StatList.WEP_PHY)*(100+stat.getStat(StatList.MAST_PHY_2)))/100;	// [무기물공*(100+마스터리2)/100]
		int inc_weapon2=(inc_weapon1*(stat.getStat(StatList.MAST_PHY)+100))/100;						// [[무기물공*(100+마스터리2)/100]*(100+마스터리1)/100]
		int defIgnore=(stat.getStat(StatList.WEP_NODEF_PHY)*(100-object.difficulty))/100;				// 방어무시데미지
		double frontATK=inc_strength*inc_weapon2+defIgnore;												// (힘)*(물공+방무뎀)
		
		double avgCritical=stat.getStat(StatList.CRT_PHY)+stat.getStat(StatList.CRT_LOW_PHY)+3;			// 3+크리+크리저항
		if(object.backattack) avgCritical+=stat.getStat(StatList.CRT_BACK_PHY);
		if(avgCritical>100) avgCritical=100;
		double inc_critical=(100+stat.getStat(StatList.BUF_CRT))/100;
		if(object.backattack && stat.getStat(StatList.DAM_CRT_BACK)>stat.getStat(StatList.DAM_CRT))
			inc_critical*=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT_BACK))/100);	// (100-크리확률)+크리확률*1.5*(100+백크증뎀)/100
		else inc_critical*=(100-avgCritical)+avgCritical*1.5*(int)((100+stat.getStat(StatList.DAM_CRT))/100);		// (100-크리확률)+크리확률*1.5*(100+크증뎀)/100
		
		int inc_damage=(100+stat.getStat(StatList.BUF_INC))/100;
		if(object.backattack && stat.getStat(StatList.DAM_INC_BACK)>stat.getStat(StatList.DAM_INC))
			inc_damage*=(100+stat.getStat(StatList.DAM_INC_BACK))/100;									// (100+증뎀)/100
		else inc_damage*=(100+stat.getStat(StatList.DAM_INC))/100;										// (100+증뎀)/100
		
		double counter=1;
		if(object.counter) counter=1.25;																// 카운터
	
		double elem_fire=
		
		return damage;
	}
	
	public static double element_dmg(Monster object, Status.PublicStatus stat, int element)
	{
		if(!((ElementInfo)stat.publicInfo[element]).hasElement) return -1.0;
		
		else return ( 1.05+0.0045*(stat.getStat(element)-object.getStat(Monster_StatList.FIRE_RESIST+element-StatList.ELEM_FIRE) ) )
	}
	
}

class Char
{
	Status finalStatus;
}
