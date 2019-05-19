package com.tef.reactor_tutorial;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;


public class CreatingReactiveFlux2 
{

	// return a flux from an iterable
	public static Flux<String> getFluxSample1() {
		List<String> fruits = new ArrayList<>();
		fruits.add("Apple");
		fruits.add("Orange");
		fruits.add("Grape");
		fruits.add("Strawberry");
    	return Flux.fromIterable(fruits);
	}
	
	// return a flux from an stream
	public static Flux<String> getFluxSample2() {
		Stream<String> fruits = Stream.of("Apple", "Orange", "Grape", "Strawberry");
    	return Flux.fromStream(fruits);
	}
	
    public static void main( String[] args ) throws InterruptedException
    {
    	Flux<String> fluxSample = getFluxSample1();
//    	Flux<String> fluxSample = getFluxSample2().delayElements(Duration.ofMillis(500));
    	
    	fluxSample.log().doOnTerminate(() -> {
    	}).subscribe(System.out::println);
    	
//    	long start = System.currentTimeMillis();
//        Thread.sleep(3000);
//        System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    }
}
