/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */

public class ElementEdge {
	
	/* Class variables */
	private City element;
	private int count = 1;
	
	/**
	 * Constructor of an ElementEdge
	 * @param City c - the element
	 */
	public ElementEdge(City c) {
		element = c;
	}
	
	/**
	 * Gets the appropriate city.
	 * @return City element - the city in question
	 */
	public City getCity() {
		return element;
	}
	
	/**
	 * Gets the count.
	 * @return int count - the count
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Increments the counter.
	 */
	public void countUp() {
		count++;
	}
	
	/**
	 * Decrements the counter.
	 */
	public void countDown() {
		count--;
	}
}