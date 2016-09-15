package dnf_InterfacesAndExceptions;

public class SkillInfoNotFounded extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6909677628567986276L;

	public SkillInfoNotFounded(String name, int level)
	{
		super("Skill "+name+", level "+level+" not found");
	}
}
