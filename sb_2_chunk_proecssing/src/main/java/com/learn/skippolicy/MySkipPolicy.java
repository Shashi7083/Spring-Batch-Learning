package com.learn.skippolicy;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.validator.ValidationException;

public class MySkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
		System.out.println("Skip Count : "+skipCount);
		
		if( skipCount < 3) {
			
			if(t instanceof ValidationException) return true;
			
			if(t instanceof FlatFileParseException) {
				String line = ((FlatFileParseException) t).getInput();
				String []lineArray = line.split(",");
				
				//check if length of this is 4 or greater  means all neccessary value available
				if(lineArray.length >= 4) {
					System.out.println("skip policy"+line);
					return true;
				}
			}
			
		}
		return false;
	}

}
