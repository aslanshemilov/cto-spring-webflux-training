package com.tef.reactor_tutorial;



import reactor.core.publisher.Flux;


public class ExerciseTransformingReactiveFlux3 
{
	
	public static Flux<String> getFluxSample1() {
		return Flux.just("Peter", "Charles");
	}
	
	public static Flux<String> getFluxSample2() {
		return Flux.just("Onion", "Garlic", "Lettuce", "Cucumber");
	}
	
	// Transform the previous flux into a new one with the following content.
	// Peter hates Onion
	// Peter hates Garlic
	// .......
	// Charles hates Onion
	// ........
	
    public static void main( String[] args ) throws InterruptedException
    {
    	Flux<String> users = getFluxSample1();
    	Flux<String> veggies = getFluxSample2();
    	

    	users.flatMap(userStr -> veggies.map(veggie -> userStr + " hates " + veggie)).log().doOnTerminate(() -> {
    		//System.exit(0);
    	}).subscribe(System.out::println);
    	
//    	veggies.flatMap(veggie -> users.map(userStr -> userStr + " hates " + veggie)).log().doOnTerminate(() -> {
//    		//System.exit(0);
//    	}).subscribe(System.out::println);
    	
    	
    	long start = System.currentTimeMillis();
    	Thread.sleep(3000);
    	System.out.println("Sleep time in ms = "+(System.currentTimeMillis()-start));
    	
    }
}
