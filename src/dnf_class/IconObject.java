package dnf_class;

public abstract class IconObject implements Cloneable, java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5078451964971480083L;
	protected String name;
	protected String iconAddress;
	
	protected IconObject()
	{
		name="없음";
		iconAddress=null;
	}
	
	public String getName() { return name;}
	public void setName(String name) { this.name = name;}
	
	public String getIcon() { return iconAddress;}
	public void setIcon(String icon) { iconAddress = icon;}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
