package main;

import java.util.HashMap;

import lib.BinaryHeap;

public class Heap {
	public int getMininimum(HashMap<Integer, FlightDetails> flightsDetails) {
		BinaryHeap<Integer> heap = new BinaryHeap<>( );
		for(int i = 0; i < flightsDetails.size(); i++) {
	    	heap.insert(flightsDetails.get(i).price);
	    }
	    return heap.findMin();
	}
}
