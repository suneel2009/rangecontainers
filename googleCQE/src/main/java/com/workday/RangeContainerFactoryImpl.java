package com.workday;

import org.apache.logging.log4j.LogManager;

/**
 * 
 * @author suneel karnam
 * 
 * Implementation of RangeContainerFactory
 * returns an instance of RangeContainer implemented using google CQE
 * 
 */

import org.apache.logging.log4j.Logger;

public class RangeContainerFactoryImpl implements RangeContainerFactory {

	private static final Logger log = LogManager.getLogger(RangeContainerFactoryImpl.class);
	
	
	@Override
	public RangeContainer createContainer(long[] data) {
		try {
			return new RangeContainerImplCQE(data);
		}
		catch (DataThresholdCrossedException e) {
			log.error(e);
			
		}
		return null;
	}

}
