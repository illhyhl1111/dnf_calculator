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
	public String getItemName() {
		if(name.contains("-복제")){
			int index = name.indexOf("-복제");
			return name.substring(0, index);
		}
		else return name;	
	}
	public void setName(String name) { this.name = name;}
	
	public String getIcon() { return iconAddress;}
	public void setIcon(String icon) { iconAddress = icon;}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
