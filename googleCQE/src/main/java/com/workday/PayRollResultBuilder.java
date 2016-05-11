package com.workday;

/**
 * @author suneel karnam
 * 
 * builder to set various attributes of the payRollResult
 */
import java.util.stream.Stream;

public class PayRollResultBuilder {

	public static PayRollResult build(PayRollResultSetter... payRollResultSetters) {
		final PayRollResult payRollResult = new PayRollResult();
		Stream.of(payRollResultSetters).forEach(
				s -> s.set(payRollResult)
		);
		
		return payRollResult;
		
	}
}
