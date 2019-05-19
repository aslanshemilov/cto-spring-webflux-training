package com.tef.reactor_tutorial;


import java.util.HashMap;
import java.util.Map;

import reactor.core.publisher.Mono;


public class ExerciseCombiningReactiveMono1 
{
	
	public static Mono<String> getMonoSample1() {
		return Mono.just("Politicians");
	}
	
	public static Mono<String> getMonoSample2() {
		return Mono.just("Corruption");
	}
	
	public static Mono<String> getMonoSample3(String key) {
		Map<String, Mono<String>> hash = new HashMap<String, Mono<String>>();
		hash.put("Politicians", Mono.just("Corruption"));
		hash.put("Veggies", Mono.just("Good"));
		hash.put("Fruits", Mono.just("Yummy"));
		return hash.get(key);
	}
	
	
	// Combine the results Politicians and Corruption in a Tuple
	// Combine the results into a tuple using methods getMonoSample1 and getMonoSample3
	
    public static void main( String[] args ) throws InterruptedException
    {
    	Mono.zip(getMonoSample1(),getMonoSample2()).log().doOnTerminate(() -> {
			//System.exit(0);
		}).subscribe(System.out::println);
	
//		getMonoSample1().zipWith(getMonoSample2()).log().doOnTerminate(() -> {
//			//System.exit(0);
//		}).subscribe(System.out::println);
	
	
//		getMonoSample1().zipWhen((data1)-> getMonoSample2()).log().doOnTerminate(() -> {
//			//System.exit(0);
//		}).subscribe(System.out::println);
	
//		getMonoSample1().zipWhen((data1)-> getMonoSample3(data1)).log().doOnTerminate(() -> {
//		//System.exit(0);
//		}).subscribe(System.out::println);

    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
