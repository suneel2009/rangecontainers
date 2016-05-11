package com.workday;

/**
 * @author suneel karnam
 */
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

public class IdsImplCQE implements Ids {

	
	private Iterator<PayRollResult> ids;
	
	IdsImplCQE(Iterable<PayRollResult>ids) {
		if(Objects.isNull(ids)) {
			this.ids = Collections.<PayRollResult>emptyList().iterator();
		}
		else {
			//no need to sort as the input comes sorted by id 
			this.ids = ids.iterator();
		}
	}
	
	/**
	 * return the next id in the sequence and -1 if there is no data
	 * The ids are already sorted 
	 */
	@Override
	public short nextId() {
		return ids.hasNext() ? ids.next().getId():END_OF_IDS;
	}

}
