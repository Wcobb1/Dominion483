package dominionAgents;

import java.util.ArrayList;
import java.util.Comparator;

public class SortableGiniCardList extends ArrayList<SortableGiniCard>{
	
	public boolean add(SortableGiniCard sgc) {
		super.add(sgc);
		this.sort(new Comparator() {

			@Override
			public int compare(Object arg0, Object arg1) {
				SortableGiniCard a0 = (SortableGiniCard)arg0;
				SortableGiniCard a1 = (SortableGiniCard)arg1;
				return a0.compareTo(a1);
			}
			
		});
		return true;
	}
	
	public String toString() {
		String output = "";
		for(SortableGiniCard sgc:this) {
			output += sgc.getName() + ": " + sgc.getGiniValue() + "\n"; 
		}
		return output;
	}
	
}
