package de.ora.neural.util;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * An idempotent (no dumb exceptions) stopwatch implementation based on nanoseconds.
 */
public class Stopwatch {
	private boolean isRunning;
	private long elapsedNanos;
	private long startTick;

	private Stopwatch() {
	}

	public static Stopwatch createStarted() {
		return new Stopwatch().start();
	}

	public static Stopwatch createUnstarted() {
		return new Stopwatch();
	}


	public Stopwatch start() {
		isRunning = true;
		startTick = read();
		return this;
	}

	public Stopwatch stop() {
		long tick = read();
		isRunning = false;
		elapsedNanos += tick - startTick;
		return this;
	}

	public Stopwatch reset() {
		elapsedNanos = 0;
		isRunning = false;
		return this;
	}

	public long elapsed(TimeUnit desiredUnit) {
		return desiredUnit.convert(elapsedNanos(), NANOSECONDS);
	}

	public String elapsedString(TimeUnit desiredUnit) {
		return String.format("%6s %3s", desiredUnit.convert(elapsedNanos(), NANOSECONDS), abbreviate(desiredUnit));
	}


	public boolean isRunning() {
		return isRunning;
	}


	@Override
	public String toString() {
		long nanos = elapsedNanos();

		TimeUnit unit = chooseUnit(nanos);
		double value = (double) nanos / NANOSECONDS.convert(1, unit);

		return String.format("%.4g %s", value, abbreviate(unit));
	}

	private static TimeUnit chooseUnit(long nanos) {
		if (DAYS.convert(nanos, NANOSECONDS) > 0) {
			return DAYS;
		}
		if (HOURS.convert(nanos, NANOSECONDS) > 0) {
			return HOURS;
		}
		if (MINUTES.convert(nanos, NANOSECONDS) > 0) {
			return MINUTES;
		}
		if (SECONDS.convert(nanos, NANOSECONDS) > 0) {
			return SECONDS;
		}
		if (MILLISECONDS.convert(nanos, NANOSECONDS) > 0) {
			return MILLISECONDS;
		}
		if (MICROSECONDS.convert(nanos, NANOSECONDS) > 0) {
			return MICROSECONDS;
		}
		return NANOSECONDS;
	}

	private static String abbreviate(TimeUnit unit) {
		switch (unit) {
			case NANOSECONDS:
				return "ns";
			case MICROSECONDS:
				return "\u03bcs"; // μs
			case MILLISECONDS:
				return "ms";
			case SECONDS:
				return "s";
			case MINUTES:
				return "min";
			case HOURS:
				return "h";
			case DAYS:
				return "d";
			default:
				throw new AssertionError();
		}
	}


	private long elapsedNanos() {
		return isRunning ? read() - startTick + elapsedNanos : elapsedNanos;
	}


	private long read() {
		return System.nanoTime();
	}
}
