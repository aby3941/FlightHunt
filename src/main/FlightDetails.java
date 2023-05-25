package main;

public class FlightDetails {
	Integer price;
	String timing;
	String duration;
	String layover;
	String name;
	String date;

	public FlightDetails(
		Integer price_value,
		String timing_value,
		String duration_value,
		String name_value,
		String layover_value,
		String date_value
	) {
		price = price_value;
		timing = timing_value;
		duration = duration_value;
		layover = layover_value;
		name = name_value;
		date = date_value;
	}

	public void printFlightDetails() {
		System.out.println();
		System.out.println();
		System.out.println("Price: $" + price);
		System.out.println("Flight Timing:" + timing);
		System.out.println("Flight time duration:" + duration);
		System.out.println("Layover details:" + layover);
		System.out.println("Airline details:" + name);
		System.out.println("Date:" + date);
		System.out.println();
		System.out.println();

	}
}
