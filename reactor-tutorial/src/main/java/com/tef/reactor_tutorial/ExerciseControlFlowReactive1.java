package com.tef.reactor_tutorial;

import reactor.core.publisher.Mono;


public class ExerciseControlFlowReactive1 
{
	
	public static Mono<String> getUser(String key) {
		return Mono.empty();
	}
	
	// Handle the error produced in the switchIfEmpty stage. Write the error message to Stdout.
    public static void main( String[] args ) throws InterruptedException
    {
    	
    	getUser("Peter").switchIfEmpty(Mono.error(new Throwable("Resource not found"))).log()
    	.doOnTerminate(() -> {
    		System.out.println("Finish");
    	}).subscribe(System.out::println);
    	
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
