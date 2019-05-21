package com.tef.reactor_tutorial;


import java.util.HashMap;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class TransformingReactiveFlux3 
{
	
	public static Flux<String> getFluxSample1() {
		return Flux.just("Apple", "Orange", "Grape", "Strawberry");
	}
	
	public static Flux<String> getFluxSample2() {
		return Flux.just("Onion", "Garlic", "Lettuce", "Cucumber");
	}
	
	public static Mono<String> getMonoSample3(String key) {
		Map<String, Mono<String>> hash = new HashMap<>();
		hash.put("Apple", Mono.just("Onion"));
		hash.put("Orange", Mono.just("Garlic"));
		hash.put("Grape", Mono.just("Lettuce"));
		hash.put("Strawberry", Mono.just("Cucumber"));
		return hash.get(key);
	}
	
	// Transform a Flux of String into a new one.
	// Transform a Flux of fruits int a Flux of veggies.
    public static void main( String[] args ) throws InterruptedException
    {
    	Flux<String> fruits = getFluxSample1();
    	
    	fruits.map(fruit -> "I like " + fruit).log().doOnTerminate(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
    	fruits.flatMap(fruit -> getMonoSample3(fruit)).log().doOnTerminate(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
