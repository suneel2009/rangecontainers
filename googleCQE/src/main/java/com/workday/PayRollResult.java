package com.workday;

/**
 * @author suneel karnam
 * 
 * Domain object to hold the  payRoll record
 * The required attributes for google CQE is also defined here at the end
 * 
 */
import java.util.Date;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

public class PayRollResult {
	
	private long netSalary;
	private short id;
	private Date date;
	
	public long getNetSalary() {
		return netSalary;
	}
	public void setNetSalary(long netSalary) {
		this.netSalary = netSalary;
	}
	
	public short getId() {
		return id;
	}
	public void setId(short id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public int hashCode()
	{
		int hash = 31;
		hash = 7 * hash + (int) this.netSalary >>> 32 ;
		hash = 7 * hash + this.id >>> 32 ;
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			// both objects point to same address
			return true;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		final PayRollResult other = (PayRollResult) obj;
		
		if (netSalary != other.netSalary) {
			return false;
		}
		
		if(id != other.id) {
			return false;
		}
			
		return true;
	}
	
	// Attributes required for google CQE
	public static final Attribute<PayRollResult, Long> NET_SALARY = new SimpleAttribute<PayRollResult, Long>("netSalary") {
	    public Long getValue(PayRollResult payRollResult, QueryOptions queryOptions) { return payRollResult.getNetSalary(); }
	};
	
	public static final Attribute<PayRollResult, Short> WORKER_ID = new SimpleAttribute<PayRollResult, Short>("id") {
	    public Short getValue(PayRollResult payRollResult, QueryOptions queryOptions) { return payRollResult.getId(); }
	};

}
