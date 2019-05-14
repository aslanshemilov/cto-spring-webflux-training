package com.tef.reactor_tutorial;

import reactor.core.publisher.Flux;


public class CreatingReactiveFlux1 
{
	
	public static Flux<String> getFluxSample1() {
		return Flux.just("Apple", "Orange", "Grape", "Strawberry");
	}
	
    public static void main( String[] args ) throws InterruptedException
    {
    	Flux<String> fluxSample = getFluxSample1();
    	
    	// create a flux of strings and show the results
    	fluxSample.log().doOnTerminate(() -> {
    		System.exit(0);
    	}).subscribe(System.out::println);
    	
    }
}
