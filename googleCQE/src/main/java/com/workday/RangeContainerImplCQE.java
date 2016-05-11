package com.workday;

/**
 * @author suneel karnam
 * 
 * google collection query engine to search on collections using sql like queries
 * https://github.com/npgall/cqengine
 * 
 */
import static com.googlecode.cqengine.query.QueryFactory.ascending;
import static com.googlecode.cqengine.query.QueryFactory.between;
import static com.googlecode.cqengine.query.QueryFactory.orderBy;
import static com.googlecode.cqengine.query.QueryFactory.queryOptions;

import java.util.Objects;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;

public class RangeContainerImplCQE implements RangeContainer {

	private static final Logger log = LogManager.getLogger(RangeContainerImplCQE.class);
	private static final int DATA_THRESHOLD = 32000;
	private final IndexedCollection<PayRollResult> payRollData = new ConcurrentIndexedCollection<PayRollResult>();
	
	public RangeContainerImplCQE(long[] data) throws DataThresholdCrossedException {
		
		if(Objects.nonNull(data)) {
			if(data.length > DATA_THRESHOLD) {
				log.error("The maximum defined threshold (32000) of data size exceeded");
				throw new DataThresholdCrossedException(String.format("Data size crossed the threshold of %s", DATA_THRESHOLD));
			
			}
			
			loadData(data);
		}
		
	}
	
	/**
	 * search the collection on netSalary and the corresponding ids are presented in ascending order
	 */
	@Override
	public Ids findIdsInRange(long fromValue, long toValue, boolean fromInclusive, boolean toInclusive) {
		if(fromValue < 0 || toValue < 0 || toValue < fromValue) {
			throw new IllegalArgumentException(String.format("invalid inputs %s %s %s %s", 
					fromValue, toValue, fromInclusive, toInclusive));
		}
		
		// can use this info for analysis if required 
		log.info(String.format("Searching with params: %s %s %s %s ", fromValue, toValue, fromInclusive, toInclusive));
		
		Query<PayRollResult> query = between(PayRollResult.NET_SALARY, fromValue, fromInclusive, toValue, toInclusive);
		
		ResultSet<PayRollResult> result = payRollData.retrieve(query,
				queryOptions(orderBy(ascending(PayRollResult.WORKER_ID))));
		
		log.info(String.format("Search retirned %s records ", result.size()));
		
		Ids ids = new IdsImplCQE(result);
		
		return ids;
	}
	
	/**
	 * Load the data and create index on the attributes search is performed
	 */
	private void loadData(long[] data) {
		
		//Add index on the attributes search is performed
		payRollData.addIndex(NavigableIndex.onAttribute(PayRollResult.NET_SALARY));
		
		IntStream.range(0, data.length).forEach(idx -> {
			PayRollResult payRollResult = PayRollResultBuilder.build(
					p -> p.setNetSalary(data[idx]),
					p -> p.setId((short)idx)
			);
			
			payRollData.add(payRollResult);
			
		});
		
		log.info(String.format("Finished loading %s items ", data.length));
	}

}
