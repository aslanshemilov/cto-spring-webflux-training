package com.tef.reactor_tutorial;

import org.junit.jupiter.api.Test;
import com.tef.reactor_tutorial.CreatingReactiveFlux1;
import com.tef.reactor_tutorial.ExerciseTransformingReactiveFlux3;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LogicReactiveTest 
{


	@Test
    public void testAny()
    {
		
		Mono<Boolean> hasY = CreatingReactiveFlux1.getFluxSample1().any(fruit -> fruit.contains("y"));
		StepVerifier.create(hasY)
			.expectNext(true)
			.verifyComplete();
        
    }
	
	
	@Test
    public void testAll()
    {
		
		Mono<Boolean> hasY = ExerciseTransformingReactiveFlux3.getFluxSample1().all(user -> user.contains("e"));
		StepVerifier.create(hasY)
			.expectNext(true)
			.verifyComplete();
        
    }
}
