package com.tef.reactor_tutorial;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.tef.reactor_tutorial.CreatingReactiveFlux1;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


public class NormalReactiveTest 
{

	@Test
    public void testElements()
    {
		
		Flux<String> fruits = CreatingReactiveFlux1.getFluxSample1();
		
		StepVerifier.create(fruits)
			.expectNext("Apple")
			.expectNext("Orange")
			.expectNext("Grape", "Strawberry")
			.verifyComplete();
        
    }

	@Test
    public void testContains()
    {
		
		Flux<String> fruits = CreatingReactiveFlux1.getFluxSample1();
		
		List<String> fruitsArr = fruits.collectList().block();
		
		StepVerifier.create(fruits)
			.expectNextMatches(fruit -> fruitsArr.contains(fruit))
			.expectNextMatches(fruit -> fruitsArr.contains(fruit))
			.expectNextMatches(fruit -> fruitsArr.contains(fruit))
			.expectNextMatches(fruit -> fruitsArr.contains(fruit))
			.verifyComplete();
        
    }
	
	@Test
    public void testContains2()
    {
		
		Flux<String> fruits = CreatingReactiveFlux1.getFluxSample1();
		
		List<String> fruitsArr = fruits.collectList().block();
		
		StepVerifier.create(fruits)
			.thenConsumeWhile(fruit -> fruitsArr.contains(fruit))
			.verifyComplete();
        
    }
}
