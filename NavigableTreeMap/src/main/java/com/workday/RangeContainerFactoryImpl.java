package com.workday;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RangeContainerFactoryImpl implements RangeContainerFactory {

	private static final Logger log = LogManager.getLogger(RangeContainerFactoryImpl.class);
	
	public RangeContainer createContainer(long[] data) {
		try {
			return new RangeContainerImpl(data);
		}
		catch(DataThresholdCrossedException e) {
			log.error(e);
		} 
		catch(Exception e) {
			log.error(e);
		}
		return null;
	}

}
