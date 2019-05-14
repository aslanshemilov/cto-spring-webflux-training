package com.tef.reactor_tutorial;

import reactor.core.publisher.Mono;


public class ExerciseCreatingMono1 
{
	
	public static Mono<String> getMonoSample1() {
		return Mono.just("Apple");
	}
	
	
	// create a Mono of string a print the value to Stdout.
    public static void main( String[] args ) throws InterruptedException
    {
    	Mono<String> monoSample = getMonoSample1();
    	
    	monoSample.log().doOnTerminate(() -> {
    		System.exit(0);
    	}).subscribe(System.out::println);
    	
    }
}
