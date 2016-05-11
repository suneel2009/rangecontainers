package com.workday;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

public class IdsImpl implements Ids {

	private Iterator<Short> ids;
	
	IdsImpl(Iterable<Short>ids) {
		if(Objects.isNull(ids)) {
			this.ids = Collections.<Short>emptyList().iterator();
		}
		else {
			this.ids = ids.iterator();
		}
	}
	
	public short nextId() {
		return ids.hasNext() ? ids.next():END_OF_IDS;
	}

}
