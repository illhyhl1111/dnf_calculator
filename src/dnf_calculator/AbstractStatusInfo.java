package dnf_calculator;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public abstract class AbstractStatusInfo implements Cloneable				// 스탯정보 저장 class
{
	abstract public double getStatToDouble() throws StatusTypeMismatch;
	abstract public void setInfo(int strength) throws StatusTypeMismatch;
	abstract public void setInfo(double strength) throws StatusTypeMismatch;
	abstract public void setInfo(boolean bool) throws StatusTypeMismatch;
	@Override
	public Object clone() throws CloneNotSupportedException 
	{
		return super.clone();
	}
}

