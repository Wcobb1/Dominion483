package dominionAgents;

public class SortableGiniCard extends Card implements Comparable<SortableGiniCard>{

	private double giniValue;
	
	public SortableGiniCard(String name, double giniValue) {
		super(name);
		this.giniValue = giniValue;
	}

	public double getGiniValue() {
		return giniValue;
	}
	
	@Override
	public int compareTo(SortableGiniCard otherCard) {
		if(giniValue < otherCard.getGiniValue()) {
			return -1;
		}else {
			return 1;
		}
	}

	
	
}
