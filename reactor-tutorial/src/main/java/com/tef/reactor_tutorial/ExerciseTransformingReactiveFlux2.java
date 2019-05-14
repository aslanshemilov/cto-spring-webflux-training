package com.tef.reactor_tutorial;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class ExerciseTransformingReactiveFlux2 
{
	
	public static Flux<String> getFluxSample1() {
		return Flux.just("Apple", "Orange", "Grape", "Strawberry");
	}
	
	public static Mono<String> getFluxSample2() {
		return Mono.just("Peter");
	}
	
	// Transform the user mono to other mono with the following element:
	// "Peter hates Apple, Orange, Grape, StrawBerry"
    public static void main( String[] args ) throws InterruptedException
    {
    	Mono<String> user = getFluxSample2();
    	Flux<String> fruits = getFluxSample1();
    	
    	
    	user.flatMap(userStr -> fruits.collectList().map(fruitsArray -> userStr + " hates " + String.join(", ", fruitsArray)))
    	.log().doOnTerminate(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
    	
//    	fruits.reduce("", (fruit1, fruit2) -> {
//    		if (fruit1.equals("")) {
//    			return fruit2;
//    		}
//    		return fruit1 + ", " + fruit2;
//    	}).flatMap(fruitsStr-> user.map(userStr -> userStr + " hates " + fruitsStr)).log().doOnTerminate(() -> {
//    		//System.exit(0);
//    	}).subscribe(System.out::println);
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
