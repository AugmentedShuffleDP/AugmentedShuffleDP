package lnf;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LNFUser {

	private int value;

	public LNFUser() {

	}

	public LNFUser(int value) {
		this.value = value;
	}

	public int getOriginalValue() {
		return value;
	}

	public void setPoisonedValue(Set<Integer> targets) {
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(targets);
		int rand = (int) (Math.random() * targets.size());
		value = list.get(rand);
	}
}
