package dnf_calculator;


public class ElementInfo extends StatusInfo				// 속성정보 저장 class
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4839497639171877025L;
	private boolean hasElement;
	
	public ElementInfo(boolean activated, int strength)
	{
		super(strength);
		hasElement=activated;
	}
	public ElementInfo(int strength)
	{
		super(strength);
		hasElement=false;
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
