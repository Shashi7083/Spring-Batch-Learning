package com.learn.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class MyChunkListener implements ChunkListener {
 
	
	@Override
	public void beforeChunk(ChunkContext context) {
		System.out.println("Before Chunk Executed ");
	}

	@Override
	public void afterChunk(ChunkContext context) {
		System.out.println("After chunk executed");
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		System.out.println("After chunk error");
	}

}
