package com.workday;


public class DataThresholdCrossedException extends Exception {
	
	private static final long serialVersionUID = 7962300047048424355L;

	public DataThresholdCrossedException()
	{
		super();
	}
	
	public DataThresholdCrossedException(String message)
	{
		super(message);
	}
	
	public DataThresholdCrossedException(Throwable throwable)
	{
		super(throwable);
	}

}
