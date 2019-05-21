package com.tef.reactor_tutorial;

import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.TopicProcessor;
import reactor.core.publisher.WorkQueueProcessor;


public class ProcessorReactiveTest 
{

	@Test
	public void testEmitterProcessor() {
		EmitterProcessor<Long> data = EmitterProcessor.create(1);
		FluxSink<Long> sink = data.sink();
		data.log().subscribe(t -> System.out.println(t));
		sink.next(10L);
		sink.next(11L);
		sink.next(12L);
		data.subscribe(t -> System.out.println(t));
		sink.next(13L);
		sink.next(14L);
		sink.next(15L);
	}

	@Test
	public void testReplayProcessor() {
		ReplayProcessor<Long> data = ReplayProcessor.create(3);
		data.log().subscribe(t -> System.out.println(t));
		data.onNext(10L);
		data.onNext(11L);
		data.onNext(12L);
		data.onNext(13L);
		data.onNext(14L);
		data.subscribe(t -> System.out.println(t));
	}

	@Test
	public void testTopicProcessor() {
		TopicProcessor<Long> data = TopicProcessor.<Long>builder().executor(Executors.newFixedThreadPool(2)).build();
		data.log().subscribe(t -> System.out.println(t));
		data.log().subscribe(t -> System.out.println(t));
		FluxSink<Long> sink = data.sink();
		sink.next(10L);
		sink.next(11L);
		sink.next(12L);
	}
	
	@Test
    public void testWorkQueueProcessor() {
        WorkQueueProcessor<Long> data = WorkQueueProcessor.<Long>builder().build();
        data.log().subscribe(t -> System.out.println("1. " + t));
        data.log().subscribe(t -> System.out.println("2. " + t));
        FluxSink<Long> sink = data.sink();
        sink.next(10L);
        sink.next(11L);
        sink.next(12L);
    }

}
