package com.workday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Implementation of RangeContainer which uses ConcurrentNavigableMap 
 * for data storage
 */

public class RangeContainerImpl implements RangeContainer {

	private static final Logger log = LogManager.getLogger(RangeContainerImpl.class);
	private static final int DATA_THRESHOLD = 32000;
	private final NavigableMap<Long, Object>  payRollData = Collections.synchronizedNavigableMap(new TreeMap<Long, Object>());

	RangeContainerImpl(long[] data) throws DataThresholdCrossedException {
		if(Objects.nonNull(data)) {
			if(data.length > DATA_THRESHOLD) {
				log.error("The maximum defined threshold (32000) of data size exceeded");
				throw new DataThresholdCrossedException(String.format("Data size crossed the threshold of %s", DATA_THRESHOLD));
			}
			
			loadData(data);
		}
		
	}
	public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
		
		if(fromValue < 0 || toValue < 0 || toValue < fromValue) {
			throw new IllegalArgumentException(String.format("invalid inputs %s %s %s %s", 
					fromValue, toValue, fromInclusive, toInclusive));
		}
		
		// can be used for analysis
		log.info(String.format("Searching with params: %s %s %s %s ", fromValue, toValue, fromInclusive, toInclusive));
		
		List<Short> ids = new ArrayList<Short>();
		payRollData.subMap(fromValue, fromInclusive, toValue, toInclusive).values().stream().forEach(
				id -> addToResult(id, ids));
		Collections.sort(ids);
		
		log.info(String.format("Search retirned %s records ", ids.size()));
		
		return new IdsImpl(ids);
	}
	
	/*
	 * Adds the worker id to the ids list. 
	 */
	private void addToResult(Object id, List<Short> ids) {
		if(id instanceof Short) {
			ids.add((short)id);
		}
		else {
			ids.addAll((List<Short>)id);
		}
	}
	
	private void loadData(long[] data) {
		//payRollData = new ConcurrentSkipListMap<Long, Object>();
		
		IntStream.range(0, data.length).forEach(id -> {
			Object payRollResultRecord = payRollData.get(data[id]);
			
			if(Objects.isNull(payRollResultRecord)) {
				payRollData.put(data[id], (short)id);
			}
			else if(payRollResultRecord instanceof Short) {
				//One record with same salary exists
				// create a list to append the new record
				List<Short> netSalaryList = new ArrayList<Short>(2);
				netSalaryList.add((short)payRollResultRecord);
				netSalaryList.add((short)id);
				
				payRollData.put(data[id], netSalaryList);
				
			}
			else {
				// There is already a list of records. Hence append to current list
				List<Short> netSalaryList = (List<Short>)payRollResultRecord;
				netSalaryList.add((short)id);
				
				payRollData.put(data[id], netSalaryList);
			}
		});
		
		log.info(String.format("Finished loading %s items ", data.length));
	}

}
