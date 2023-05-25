package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import lib.BinaryHeap;
import main.SpellCheck;
import lib.TST;

public class Main {
	public static List<String> getRecomendations(TST<Integer> citiesTST, List<String> citiesList, String cityInput) {
		SpellCheck spellchecker = new SpellCheck();
		
		Iterable <String> citiesMatchItr = citiesTST.prefixMatch(cityInput);
		List<String> citiesMatchList = new ArrayList<String>();
		citiesMatchItr.forEach(citiesMatchList::add);
		
		List <String> citiesSimilar = spellchecker.findSimilar(citiesList, cityInput, 5);
		List<String> cities = new ArrayList<String>();
		cities = (citiesMatchList.size() > 0) ? citiesMatchList: (citiesSimilar.size() > 0) ? citiesSimilar : cities;
		return cities;
	}

	public static void main(String args[]){
		HtmlParser parser = new HtmlParser();
		SpellCheck spellchecker = new SpellCheck();
		AutoComplete autoCompletion = new AutoComplete();
		HashMap<String, AirportDetails> airportsDetails = new HashMap<>();
		
		Heap heap = new Heap();


		airportsDetails = parser.getAirportDetails();
		TST<Integer> citiesTST = autoCompletion.generateTST(airportsDetails);
		Scanner input = new Scanner(System.in);
        String ans = "Y";
        do {
        	List<String> citiesList = spellchecker.generateList(airportsDetails);
    		HashMap<Integer, FlightDetails> flightsDetails = new HashMap<>();
        	System.out.println("Enter departure city: ");
            String cityInput = input.nextLine();
            
            if (!cityInput.matches("[a-zA-Z ]+")) {
            	System.out.println("Text should contain only characters!");
                System.out.println("Do you want to restart: ");
                ans = input.nextLine();
                continue;
            }

           
    		List<String> departureCities = getRecomendations(citiesTST, citiesList, cityInput);
    		if(departureCities.size() != 0) {
    			for (int i = 0; i < departureCities.size(); i++) {
        			System.out.println(i + " : " + departureCities.get(i));
        		}
        		
    		} else {
    			System.out.println("Please provide valid city name!");
                System.out.println("Do you want to restart: ");
                ans = input.nextLine();
                continue;
    		}
    		
    		System.out.println("Select the correct city number:");
    		int cityPos = input.nextInt();
    		input.nextLine();
    		if(cityPos + 1 > departureCities.size()) {
    			System.out.println("Please provide valid city number!");
                System.out.println("Do you want to restart: ");
                ans = input.nextLine();
                continue;
    		}
    		
            String deptartureLocation = airportsDetails.get(departureCities.get(cityPos)).code;
    		
            
            System.out.println("Enter arrival city: ");
            cityInput = input.nextLine();
            
            if (!cityInput.matches("[a-zA-Z ]+")) {
            	System.out.println("Text should contain only characters!");
                System.out.println("Do you want to restart: ");
                ans = input.nextLine();
                continue;
            }
    		
    		List<String> arrivalCities = getRecomendations(citiesTST, citiesList, cityInput);
    		if(arrivalCities.size() != 0) {
    			for (int i = 0; i < arrivalCities.size(); i++) {
        			System.out.println(i + " : " + arrivalCities.get(i));
        		}
        		
    		} else {
    			System.out.println("Please provide valid city name!");
                System.out.println("Do you want to restart: ");
                ans = input.nextLine();
                continue;
    		}
    		
    		System.out.println("Select the correct city number:");
    		cityPos = input.nextInt();
    		input.nextLine();
    		if(cityPos + 1 > arrivalCities.size()) {
    			System.out.println("Please provide valid city number!");
                System.out.println("Do you want to restart: ");
                ans = input.nextLine();
                continue;
    		}
            String arrivalLocation = airportsDetails.get(arrivalCities.get(cityPos)).code;
    		
            System.out.println("Enter departure date: ");
            String departureDate = input.nextLine();
            
            if (!departureDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            	System.out.println("Date should be in 2022-01-01 format");
                System.out.println("Do you want to restart: ");
                ans = input.nextLine();
                continue;
            }
            String deptartureDate = departureDate;
            
            System.out.println("Enter adult count : ");
            String adultInput = input.nextLine();
            
            if (!adultInput.matches("\\d{1,2}")) {
            	System.out.println("count should be number format such that >0 and format");
                System.out.println("Do you want to restart: ");
                ans = input.nextLine();
                continue;
            }
    		String adultCount = adultInput;
    		LocalDate variableDate = LocalDate.parse(deptartureDate);
    		for(int i = 1; i <= 2; i++) {
    			String[] splittedDate = variableDate.toString().split("-");
    			String oneWayUrl = parser.generateOneWayUrl(
    				deptartureLocation,
    				arrivalLocation,
    				splittedDate[2],
    				splittedDate[1],
    				splittedDate[0],
    				adultCount
    			);
    			flightsDetails = parser.getFlightDetails(
    				oneWayUrl,
    				flightsDetails,
    				variableDate.toString()
    			);
    			variableDate = variableDate.plusDays(1);
    		}            

            int minprice = heap.getMininimum(flightsDetails);
            for(int i = 0; i < flightsDetails.size(); i++) {
            	FlightDetails flight = flightsDetails.get(i);
            	{
            		flight.printFlightDetails();
            	}
            }
    		System.out.println("Do you want to restart: ");
            ans = input.nextLine();
        } while(ans.equals("Y") || ans.equals("y"));
        
        input.close();

	}
}
