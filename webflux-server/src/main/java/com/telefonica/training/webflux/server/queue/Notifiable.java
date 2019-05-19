/*
 * Copyright (c) Telefonica I+D. All rights reserved.
 */

package com.telefonica.training.webflux.server.queue;

/**
 * Interface for queue producers.
 *
 */
public interface Notifiable {
	<T> void inserted(T current);
	<T> void deleted(T old);
	<T> void updated(T old, T current);
}
