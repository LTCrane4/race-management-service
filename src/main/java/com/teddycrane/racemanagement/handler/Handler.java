package com.teddycrane.racemanagement.handler;

/**
 * Handler interface defining required handler methods.
 *
 * @param <I> The input type for the handler.
 * @param <R> The response type for the handler.
 */
public interface Handler<I, R> {

  /**
   * Resolves data based on the input from the service layer.
   *
   * @param input The input value to get data with.
   * @return The result from the database based on the input value(s).
   */
  R resolve(I input);
}
