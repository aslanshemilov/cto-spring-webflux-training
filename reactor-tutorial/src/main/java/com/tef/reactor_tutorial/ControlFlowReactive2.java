package com.tef.reactor_tutorial;

import reactor.core.publisher.Mono;


public class ControlFlowReactive2 
{
	
	public static Mono<String> getUser(String key) {
		return Mono.empty();
	}
	
	
	public static Mono<String> getUser2(String key) {
		return Mono.just("Peter");
	}
	
    public static void main( String[] args ) throws InterruptedException
    {
    	
    
    	// emit an error when a mono or flux is empty
    	getUser("Peter").switchIfEmpty(
    			Mono.error(new Throwable("Resource not found"))
    	).log()
    	.doOnTerminate(() -> {
    		System.out.println("Finish");
    	}).subscribe(System.out::println);
    
    	// emit an error
//    	getUser2("Peter").handle((user, sink )-> {
//    		sink.error(new Throwable("Resource not found"));
//    	})
//    	.doOnTerminate(() -> {
//    		System.out.println("Finish");
//    	}).subscribe(System.out::println);
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
