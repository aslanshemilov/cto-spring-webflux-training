package com.tef.reactor_tutorial;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;


public class CombiningReactiveFlux2 
{
	
	public static Flux<String> getFluxSample1() {
		return Flux.just("Apple", "Orange", "Grape", "Strawberry", "Pinneapple", "Lemon", "Watermelon", "Banana", "Cherry");
	}
	
	public static Flux<String> getFluxSample2() {
		return Flux.just("Onion", "Garlic", "Lettuce", "Cucumber");
	}
	
    public static void main( String[] args ) throws InterruptedException
    {
    	Flux<String> fruits = getFluxSample1();
    	Flux<String> veggies = getFluxSample2();
    	
    	
    	// Groups the two flux elements in a pair an the rests of the elements are discarded
    	Flux<Tuple2<String, String>> healthyFoods = Flux.zip(fruits, veggies);
    	
    	// Flux<Tuple2<String, String>> healthyFoods = fruits.zipWith(veggies);
    	
    	healthyFoods.log().doOnTerminate(() -> {
    		System.exit(0);
    	}).subscribe(data -> {
    		System.out.println(data);
//    		System.out.println(data.getT1());
//    		System.out.println(data.getT2());
    	});
    	
    }
}
