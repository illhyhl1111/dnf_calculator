package dnf_InterfacesAndExceptions;

public enum Element_type {
	FIRE("화"), WATER("수"), LIGHT("명"), DARKNESS("암"), NONE("무"),
	FIRE_LIGHT("화명"), WATER_LIGHT("수명"), ALL("모속");
	
	String element;
	private Element_type(String str)
	{
		element=str;
	}
	
	public String getElement() {return element;}
}
