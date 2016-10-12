package dnf_infomation;

import dnf_InterfacesAndExceptions.Job;

public class BriefCharacterInfo implements java.io.Serializable{
	
	private static final long serialVersionUID = 4736455360960806023L;
	public final String name;
	public final Job job;
	public final int level;
	
	public BriefCharacterInfo(String name, Job job, int level){
		this.name=name;
		this.job=job;
		this.level=level;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof BriefCharacterInfo) return this.equals(o);
		else if(o instanceof String) return name.equals(o);
		return false;
	}
}
