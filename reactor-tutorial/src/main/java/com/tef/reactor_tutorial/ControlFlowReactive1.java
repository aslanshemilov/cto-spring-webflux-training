package com.tef.reactor_tutorial;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class ControlFlowReactive1 
{
	
	public static Flux<String> getAllUsers() {
		return Flux.just("Peter", "Charles");
	}
	
	public static Flux<String> getFluxSample2() {
		return Flux.just("Onion", "Garlic", "Lettuce", "Cucumber");
	}
	
	public static Mono<Boolean> deleteItem(String key) {
		return Mono.just(true);
	}
	
	
    public static void main( String[] args ) throws InterruptedException
    {
    	
    	getAllUsers().flatMap(user -> deleteItem(user)).log().doOnTerminate(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
    	// return other value after finish the last mono or flux
//    	getAllUsers().flatMap(user -> deleteItem(user).thenReturn(user)).log().doOnTerminate(() -> {
//    		//System.exit(0);
//    	}).subscribe(System.out::println);
    	
    	// return a custom value after finish the last mono or flux
//    	getAllUsers().flatMap(user -> deleteItem(user)).then(Mono.just("OK")).log().doOnTerminate(() -> {
//    		System.out.println("Finish");
//    	}).subscribe(System.out::println);
    	
    	// return a Mono<Void> after finish the last mono or flux
//    	getAllUsers().flatMap(user -> deleteItem(user)).then().log().doOnTerminate(() -> {
//    		System.out.println("Finish");
//    	}).subscribe(System.out::println);
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
