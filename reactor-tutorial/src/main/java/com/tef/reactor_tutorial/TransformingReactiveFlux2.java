package com.tef.reactor_tutorial;

import reactor.core.publisher.Flux;


public class TransformingReactiveFlux2 
{
	
	public static Flux<String> getFluxSample1() {
		return Flux.just("Apple", "Orange", "Grape", "Strawberry", "Pinneapple", "Lemon", "Watermelon", "Banana", "Cherry");
	}
	
	public static Flux<String> getFluxSample2() {
		return Flux.just("Onion", "Garlic", "Lettuce", "Cucumber", "Onion", "Garlic");
	}
	
	
	// filter fruits inside a flux that contains letter e.
	// discard duplicated values inside a flux.
    public static void main( String[] args ) throws InterruptedException
    {
    	Flux<String> fruits = getFluxSample1();
    	
    	fruits.filter(fruit -> !fruit.contains("e")).log().doOnTerminate(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
//    	Flux<String> veggies = getFluxSample2();
//    	
//    	veggies.distinct().log().doOnTerminate(() -> {
//    		//System.exit(0);
//    	}).subscribe(System.out::println);
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
