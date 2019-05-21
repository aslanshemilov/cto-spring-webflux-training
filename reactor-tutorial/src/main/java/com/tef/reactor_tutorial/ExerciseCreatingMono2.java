package com.tef.reactor_tutorial;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Mono;


public class ExerciseCreatingMono2 
{
	
	public static Mono<List<String>> getMonoSample1() {
		List<String> fruits = new ArrayList<>();
		fruits.add("Apple");
		fruits.add("Orange");
		fruits.add("Grape");
		fruits.add("Strawberry");
		return Mono.just(fruits);
	}
	
	
	// Create a Mono from an Iterable and show the data in the stdout.
    public static void main( String[] args ) throws InterruptedException
    {	
    	getMonoSample1().log().doOnTerminate(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    }
}
