package com.tef.reactor_tutorial;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


public class TransformingReactiveFlux4 
{
	
	public static Mono<String> getFluxSample1() {
		return Mono.just("Peter");
	}
	
	public static Flux<String> getFluxSample2() {
		return Flux.just("Onion", "Garlic", "Lettuce", "Cucumber");
	}
	
	
	// create a flux of string with the following elements:
	// Peter hates Onion
	// Peter hates Garlic
	// .......
	
    public static void main( String[] args ) throws InterruptedException
    {
    	Mono<String> user = getFluxSample1();
    	Flux<String> veggies = getFluxSample2();
    	
    	veggies.flatMap(veggie ->  user.map(userStr -> userStr + " hates " + veggie).subscribeOn(Schedulers.parallel())).log().doOnTerminate(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
//    	user.flatMapMany(userStr -> veggies.map(veggie -> userStr + " hates " + veggie).subscribeOn(Schedulers.newSingle("my-thread"))).log().doOnTerminate(() -> {
//    		//System.exit(0);
//    	}).subscribe(System.out::println);    	
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
