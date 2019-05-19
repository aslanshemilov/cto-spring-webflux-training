package com.tef.reactor_tutorial;


import java.time.Duration;

import reactor.core.publisher.Flux;


public class TransformingReactiveFlux1 
{
	
	public static Flux<String> getFluxSample1() {
		return Flux.just("Apple", "Orange", "Grape", "Strawberry", "Pinneapple", "Lemon", "Watermelon", "Banana", "Cherry");
	}
	
	public static Flux<String> getFluxSample2() {
		return Flux.just("Onion", "Garlic", "Lettuce", "Cucumber");
	}
	
	
	// skip 3 elements from the flux and take 2.
	// In one of the there is a delay on value emitting process.
    public static void main( String[] args ) throws InterruptedException
    {
    	Flux<String> fruits = getFluxSample1().skip(3).take(2);
    	
    	fruits.log().doOnComplete(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
    	
//    	Flux<String> fruits = getFluxSample1().skip(3).delayElements(Duration.ofMillis(800));
//    	
//    	fruits.log().doOnTerminate(() -> {
//    		//System.exit(0);
//    	}).subscribe(System.out::println);
    	
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
