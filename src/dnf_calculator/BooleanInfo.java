package dnf_calculator;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class BooleanInfo extends AbstractStatusInfo			// boolean형 스탯정보 저장 class
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -621900494951756795L;
	boolean bool;										// private으로 바꿔야하지만 몰라 귀찮다 그냥 조심해야지
	public BooleanInfo(boolean b)
	{
		bool=b;
	}
	
	public void setInfo(boolean b) { bool=b;}
	
	public void setInfo(int strength) throws StatusTypeMismatch { throw new StatusTypeMismatch("Integer->Boolean");}
	public void setInfo(double strength) throws StatusTypeMismatch {throw new StatusTypeMismatch("Double->Boolean");}
	public BooleanInfo getClone() {return new BooleanInfo(bool);}	// 복제
	public double getStatToDouble() throws StatusTypeMismatch {
		throw new StatusTypeMismatch("Double->Boolean");
	}
	public boolean getBooleanStat() {return bool;} 
	public void setBooleanStat(boolean bool) {this.bool=bool;}
	public String getStatToString() throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->Boolean");}
	public void setInfo(String name) throws StatusTypeMismatch { throw new StatusTypeMismatch("Skill->Boolean");}
	public void setInfo(int start, int end) throws StatusTypeMismatch { throw new StatusTypeMismatch("SkillRange->Boolean");}
}
