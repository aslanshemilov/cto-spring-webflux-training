package com.tef.reactor_tutorial;

import reactor.core.publisher.Flux;


public class CreatingReactiveFlux4 
{
	
	public static Flux<Integer> getFluxSample1() {
		return Flux.empty();
	}
	
    public static void main( String[] args )
    {
    	Flux<Integer> fluxSample = getFluxSample1();
    	
    	fluxSample.log().doOnTerminate(() -> {
    		System.out.println("Finish");
    		System.exit(0);
    	}).subscribe(System.out::println);
    }
}
