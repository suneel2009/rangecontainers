package com.workday;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RangeQueryBasicTest {

	private  RangeContainer rc;
	private RangeContainer rcWithBigData;
	private RangeContainerFactory rf;
	
	private void setUpContainerWithLargeData() {
		long[] bigData = new long[32000];
		   for(int i=1; i<32000; i++) {
			   bigData[i-1] = i * 10;
		   }
		   rcWithBigData = rf.createContainer(bigData);
	}
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Before
	  public void setUp(){
	    rf = new RangeContainerFactoryImpl();
	    rc = rf.createContainer(new long[]{10,12,17,21,2,15,16});
	}
	
	@Test
	public void runARangeQuery(){
		Ids ids = rc.findIdsInRange(14, 17, true, true);
		assertEquals(2, ids.nextId());
		assertEquals(5, ids.nextId());
		assertEquals(6, ids.nextId());
		assertEquals(Ids.END_OF_IDS, ids.nextId());
		
		ids = rc.findIdsInRange(14, 17, true, false);
		assertEquals(5, ids.nextId());
		assertEquals(6, ids.nextId());
		assertEquals(Ids.END_OF_IDS, ids.nextId());
		
		ids = rc.findIdsInRange(20, Long.MAX_VALUE, false, true);
		assertEquals(3, ids.nextId());
		assertEquals(Ids.END_OF_IDS, ids.nextId());
	}
	
	@Test
	public void runARangeQueryWithBigData() {
		
		setUpContainerWithLargeData();
		   
		Ids ids = rcWithBigData.findIdsInRange(319990, 319990 , true, true);
		assertEquals(32000-2, ids.nextId());
		assertEquals(-1, ids.nextId());
	   
		ids = rcWithBigData.findIdsInRange(319990, 319990 , false, true);
		assertEquals(-1, ids.nextId());
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void runARangeQueryOutsideBoundary() {
		setUpContainerWithLargeData();
		Ids ids = rcWithBigData.findIdsInRange(-100, -50 , true, true);
		assertEquals(-1, ids.nextId());
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void negativeTest() {
		setUpContainerWithLargeData();
		Ids ids = rcWithBigData.findIdsInRange(-100, -150 , true, true);
		assertEquals(-1, ids.nextId());
	}
}
