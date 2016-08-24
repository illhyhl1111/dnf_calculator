package dnf_calculator;

import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_class.Monster;

public class CalculateElement
{
	private double dmg_fire;
	private double dmg_water;
	private double dmg_light;
	private double dmg_darkness;
	private int mode;
	private double inc_elem;																				// 속강항
	
	public CalculateElement(Monster object, Status stat) throws StatusTypeMismatch
	{
		dmg_fire=element_dmg(object, stat, StatList.ELEM_FIRE);
		dmg_water=element_dmg(object, stat, StatList.ELEM_WATER);
		dmg_light=element_dmg(object, stat, StatList.ELEM_LIGHT);
		dmg_darkness=element_dmg(object, stat, StatList.ELEM_DARKNESS);
		mode = getElement(stat, dmg_fire, dmg_water, dmg_light, dmg_darkness);						// 적용 속성
		
		switch(mode)
		{
			case StatList.ELEM_FIRE:
				inc_elem=dmg_fire;
				break;
				
			case StatList.ELEM_WATER:
				inc_elem=dmg_water;
				break;
				
			case StatList.ELEM_LIGHT:
				inc_elem=dmg_light;
				break;
				
			case StatList.ELEM_DARKNESS:
				inc_elem=dmg_darkness;
				break;
				
			default:
				inc_elem=1;
		}
	}
	
	public double get_inc_elem() {return inc_elem;}
	public int get_mode() {return mode;}
	public double get_inc_fire() {return dmg_fire;}
	public double get_inc_water() {return dmg_water;}
	public double get_inc_light() {return dmg_light;}
	public double get_inc_darkness() {return dmg_darkness;}
	
	public static double element_dmg(Monster object, Status stat, int element) throws StatusTypeMismatch
	{
		int index = element-StatList.ELEM_FIRE;
		double temp = ( 1.05+0.0045*(stat.getStat(element)+stat.getStat(StatList.ELEM_FIRE_DEC+index)-object.getStat(Monster_StatList.FIRE_RESIST+index) ) );
					  // 1+0.05(속성부여)+0.0045*(속강-속저오라-몹속저)
	
		if(temp<0) return 0;
		else return temp;
	}
	
	public static int getElement(Status stat, double fire, double water, double light, double darkness)
	{
		int mode = -1;
		double temp = -0.5;
		try{
			if(temp<fire && (stat.getEnabled(StatList.ELEM_FIRE))){
				temp=fire;
				mode=StatList.ELEM_FIRE;
			}
			if(temp<water && (stat.getEnabled(StatList.ELEM_WATER))){
				temp=water;
				mode=StatList.ELEM_WATER;
			}
			if(temp<light && (stat.getEnabled(StatList.ELEM_LIGHT))){
				temp=light;
				mode=StatList.ELEM_LIGHT;
			}
			if(temp<darkness && (stat.getEnabled(StatList.ELEM_DARKNESS))){
				temp=darkness;
				mode=StatList.ELEM_DARKNESS;
			}
		}
		catch(StatusTypeMismatch e)
		{
			e.printStackTrace();
		}
		
		return mode;
	}
}
