package dnf_InterfacesAndExceptions;

public enum Emblem_type
{
	RED("붉은빛 엠블렘", 8), GREEN("녹색빛 엠블렘", 7), YELLOW("노란빛 엠블렘", 5), BLUE("푸른빛 엠블렘", 4),
	RED_GREEN("듀얼 엠블렘", 6), YELLOW_BLUE("듀얼 엠블렘", 3), MULTIPLE_COLOR("다색빛 엠블렘", 2), PLATINUM("플레티넘 엠블렘", 1), NONE("없음", 0);
	
	String name;
	public final int order;
	
	Emblem_type(String name, int order)
	{
		this.name=name;
		this.order=order;
	}
	
	public String getName() {return name;}
}
