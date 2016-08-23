package dnf_calculator;

public abstract class AbstractStatusInfo 				// 스탯정보 저장 class
{
	abstract public double getStatToDouble() throws StatusTypeMismatch;
	abstract public void setInfo(int strength) throws StatusTypeMismatch;
	abstract public void setInfo(double strength) throws StatusTypeMismatch;
	abstract public void setInfo(boolean bool) throws StatusTypeMismatch;
}

class StatusInfo extends AbstractStatusInfo			// int형 스탯정보 저장 class
{
	private int str;										// private으로 바꿔야하지만 몰라 귀찮다 그냥 조심해야지
	public StatusInfo(int strength)
	{	
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	public void setInfo(double strength) { str=(int)strength;}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Integer");}
	public double getStatToDouble() {return (double)str;}
	
}

class DoubleStatusInfo extends AbstractStatusInfo
{
	private double str;
	public DoubleStatusInfo(int strength)
	{
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	public void setInfo(double strength) {str=strength;}
	public void setInfo(boolean bool) throws StatusTypeMismatch { throw new StatusTypeMismatch("Boolean->Double");}
	public double getStatToDouble() {return str;}
}

class ElementInfo extends StatusInfo				// 속성정보 저장 class
{
	private boolean hasElement;
	
	public ElementInfo(boolean activated, int strength)
	{
		super(strength);
		hasElement=activated;
	}
	
	public void setElementInfo(boolean activated, int strength)
	{
		super.setInfo(strength);
		hasElement=activated;
	}
	public void setInfo(boolean activated) { hasElement=activated;}
	
	public boolean getElementEnabled() {return hasElement;}
	
	public double getStatToDouble() {return super.getStatToDouble();}
}

class BooleanInfo extends AbstractStatusInfo			// boolean형 스탯정보 저장 class
{
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
}