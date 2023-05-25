package main;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import lib.Sequences;
import lib.TST;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.base.Objects;
import main.AirportDetails;
import main.FlightDetails;
import lib.BinaryHeap;



public class HtmlParser {
	
	public HashMap<String, AirportDetails> getAirportDetails(){
		HashMap<String, AirportDetails> airports = new HashMap<>();
		
		//Uri which has list of all the airport in the world with their airport codes
		String url = "https://www.nationsonline.org/oneworld/IATA_Codes/airport_code_list.htm";
		try {
			Document document = Jsoup.connect(
					url
				).userAgent(
					"Mozilla"	
				).get();
			
			//Fetching all elements by tr.
			Elements trs = document.getElementsByTag("tr");
			
			//Getting the elements which has three divisions.
			for (Element tr: trs) {
				Elements tds = tr.getElementsByTag("td");
				String[] airportDetails = new String[3]; 
				if (tds.size() == 3) {
					int itr = 0;
					for(Element td: tds) {
						airportDetails[itr] = td.text();
						itr++;
					}
					AirportDetails airport = new AirportDetails(
						airportDetails[1],
						airportDetails[2],
						airportDetails[0]
					);
					airports.put(airportDetails[0], airport);	
				}				
			}
			return airports;

		} catch(Exception error) {
			System.out.println(error);
			return null;
		}
	}
	
	public String generateOneWayUrl(
		String deptartureLocation,
		String arrivalLocation,
		String deptartureDay,
		String deptartureMonth,
		String deptartureYear,
		String adultCount
	) {
		String oneWayUrl = "https://www.travelocity.ca/Flights-Search?leg1=from%3A"
				+ deptartureLocation
				+ "%2Cto%3A"
				+ arrivalLocation
				+ "%2Cdeparture%3A"
				+ deptartureDay
				+ "%2F"
				+ deptartureMonth
				+ "%2F"
				+ deptartureYear
				+ "TANYT&mode=search&options=carrier%3A*%2Ccabinclass%3A%2Cmaxhops%3A1%2Cnopenalty%3AN&pageId=0&passengers=adults%3A"
				+ adultCount
				+ "%2Cchildren%3A0%2Cinfantinlap%3AN&trip=oneway";
		return oneWayUrl;
	}
	
	public String generateRoundTripUrl(
			String deptartureLocation,
			String arrivalLocation,
			String deptartureDay,
			String deptartureMonth,
			String deptartureYear,
			String arrivalDay,
			String arrivalMonth,
			String arrivalYear,
			String adultCount
		) {
		String roundTripUrl = "https://www.travelocity.ca/Flights-Search?leg1=from%3A"
				+ deptartureLocation
				+ "%2Cto%3A"
				+ arrivalLocation
				+ "%2Cdeparture%3A"
				+ deptartureDay
				+ "%2F"
				+ deptartureMonth
				+ "%2F"
				+ deptartureYear
				+ "TANYT&leg2=from%3A"
				+ arrivalLocation
				+ "%2Cto%3A"
				+ deptartureLocation
				+ "%2Cdeparture%3A"
				+ arrivalDay
				+ "%2F"
				+ arrivalMonth
				+ "%2F"
				+ arrivalYear
				+ "TANYT&mode=search&options=carrier%3A*%2Ccabinclass%3A%2Cmaxhops%3A1%2Cnopenalty%3AN&pageId=0&passengers=adults%3A"
				+ adultCount
				+ "%2Cchildren%3A0%2Cinfantinlap%3AN&trip=roundtrip";
			return roundTripUrl;
		}

	public HashMap<Integer, FlightDetails> getFlightDetails(
		String url,
		HashMap<Integer, FlightDetails> flights,
		String date
	) {
		//Get driver chrome drive from file system.
		System.setProperty("webdriver.chrome.driver","C:\\Users\\AA\\Downloads\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		driver.get(url);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li[data-test-id='offer-listing']")));
        
        // Find all the offer listing
        List<WebElement> offerListings = driver.findElements(By.cssSelector("li[data-test-id='offer-listing']"));
        
        // Parsing the other details using appropriate tags.
        int flightNumber = flights.size();
        for(WebElement offerListing: offerListings) {
        	WebElement price = offerListing.findElement(By.cssSelector("span[class='uitk-lockup-price']"));
        	WebElement time = offerListing.findElement(By.cssSelector("span[data-test-id='departure-time']"));
        	WebElement duration = offerListing.findElement(By.cssSelector("div[data-test-id='journey-duration']"));        			
        	WebElement airlineDetails = offerListing.findElement(By.cssSelector("div[data-stid='airline-info']"));        			
        	
        	Integer priceInt = Integer.parseInt(price.getText().split("[$]")[1].replace(",", ""));
        	
        	// Splitting using regex to get whether the flight has layovers
        	Matcher m = Pattern.compile("\\((.*?)\\)").matcher(duration.getText());
    	    m.find();
    	    WebElement layoverDetails;
    	    String layovervalue;
    	    if(!Objects.equal(m.group(1), new String ("Nonstop"))) {
        		layoverDetails = offerListing.findElement(By.cssSelector("div[data-test-id='layovers']"));
        		layovervalue = layoverDetails.getText();
        	} else {
        		layovervalue = "";
        	}
    	    
			FlightDetails flight = new FlightDetails(
    			priceInt,
				time.getText(),
				duration.getText(),
				airlineDetails.getText(),
				layovervalue,
				date
    		);
    		flights.put(flightNumber, flight);
        	flightNumber++;
        }
        
        driver.quit();
        return flights;
	}
	


	public static void main(String args[]){
//		HtmlParser parser = new HtmlParser();
//		SpellCheck spellchecker = new SpellCheck();
//		AutoComplete autoCompletion = new AutoComplete();
//		HashMap<String, AirportDetails> airportsDetails = new HashMap<>();
//		HashMap<Integer, FlightDetails> flightsDetails = new HashMap<>();
//		BinaryHeap<Integer> heap = new BinaryHeap<>( );
//
//
//		airportsDetails = parser.getAirportDetails();
//		TST<Integer> citiesTST = autoCompletion.generateTST(airportsDetails);
//		List<String> citiesList = spellchecker.generateList(airportsDetails);
//		Scanner input = new Scanner(System.in);
//        String ans = "Y";
//        do {
//        	System.out.println("Enter departure city: ");
//            String cityInput = input.nextLine();
//            
//            if (!cityInput.matches("[a-zA-Z ]+")) {
//            	System.out.println("Text should contain only characters!");
//                System.out.println("Do you want to restart: ");
//                ans = input.nextLine();
//                continue;
//            }
//
//           
//    		// Auto fill details if 2 letters are given.
//            List<String> departureCitiesMatchList = autoCompletion.getMatchingList(citiesTST, cityInput);
//    		List <String> departureCitiesSimilar = spellchecker.findSimilar(citiesList, cityInput, 5);
//    		List<String> departureCities = new ArrayList<String>();
//    		departureCities = (departureCitiesMatchList.size() > 0) ? departureCitiesMatchList: (departureCitiesSimilar.size() > 0) ? departureCitiesSimilar : departureCities;
//    		if(departureCities.size() != 0) {
//    			for (int i = 0; i < departureCities.size(); i++) {
//        			System.out.println(i + " : " + departureCities.get(i));
//        		}
//        		
//    		} else {
//    			System.out.println("Please provide valid city name!");
//                System.out.println("Do you want to restart: ");
//                ans = input.nextLine();
//                continue;
//    		}
//    		
//    		print("Select the correct city number:");
//    		int cityPos = input.nextInt();
//    		input.nextLine();
//    		if(cityPos + 1 > departureCities.size()) {
//    			System.out.println("Please provide valid city number!");
//                System.out.println("Do you want to restart: ");
//                ans = input.nextLine();
//                continue;
//    		}
//    		
//            String deptartureLocation = airportsDetails.get(departureCities.get(cityPos)).code;
//    		
//            
//            System.out.println("Enter arrival city: ");
//            cityInput = input.nextLine();
//            
//            if (!cityInput.matches("[a-zA-Z ]+")) {
//            	System.out.println("Text should contain only characters!");
//                System.out.println("Do you want to restart: ");
//                ans = input.nextLine();
//                continue;
//            }
//         // Auto fill details if 2 letters are given.
//    		Iterable <String> arrivalCitiesMatchItr = citiesTST.prefixMatch(cityInput);
//    		List<String> arrivalCitiesMatchList = new ArrayList<String>();
//    		arrivalCitiesMatchItr.forEach(arrivalCitiesMatchList::add);
//    		List <String> arrivalCitiesSimilar = spellchecker.findSimilar(citiesList, cityInput, 5);
//    		List<String> arrivalCities = new ArrayList<String>();
//    		arrivalCities = (arrivalCitiesMatchList.size() > 0) ? arrivalCitiesMatchList: (arrivalCitiesSimilar.size() > 0) ? arrivalCitiesSimilar : arrivalCities;
//    		if(arrivalCities.size() != 0) {
//    			for (int i = 0; i < arrivalCities.size(); i++) {
//        			System.out.println(i + " : " + arrivalCities.get(i));
//        		}
//        		
//    		} else {
//    			System.out.println("Please provide valid city name!");
//                System.out.println("Do you want to restart: ");
//                ans = input.nextLine();
//                continue;
//    		}
//    		
//    		print("Select the correct city number:");
//    		cityPos = input.nextInt();
//    		input.nextLine();
//    		if(cityPos + 1 > arrivalCities.size()) {
//    			System.out.println("Please provide valid city number!");
//                System.out.println("Do you want to restart: ");
//                ans = input.nextLine();
//                continue;
//    		}
//            String arrivalLocation = airportsDetails.get(arrivalCities.get(cityPos)).code;
//    		
//            System.out.println("Enter departure date: ");
//            String departureDate = input.nextLine();
//            
//            if (!departureDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
//            	System.out.println("Date should be in 2022-01-01 format");
//                System.out.println("Do you want to restart: ");
//                ans = input.nextLine();
//                continue;
//            }
//            String deptartureDate = departureDate;
//            
//            System.out.println("Enter adult count : ");
//            String adultInput = input.nextLine();
//            
//            if (!adultInput.matches("\\d{1,2}")) {
//            	System.out.println("count should be number format such that >0 and format");
//                System.out.println("Do you want to restart: ");
//                ans = input.nextLine();
//                continue;
//            }
//    		String adultCount = adultInput;
//    		LocalDate variableDate = LocalDate.parse(deptartureDate);
//    		for(int i = 1; i <= 2; i++) {
//    			String[] splittedDate = variableDate.toString().split("-");
//    			String oneWayUrl = parser.generateOneWayUrl(
//    				deptartureLocation,
//    				arrivalLocation,
//    				splittedDate[2],
//    				splittedDate[1],
//    				splittedDate[0],
//    				adultCount
//    			);
//    			flightsDetails = parser.getFlightDetails(
//    				oneWayUrl,
//    				flightsDetails,
//    				variableDate.toString()
//    			);
//    			variableDate = variableDate.plusDays(1);
//    		}
//            System.out.println(flightsDetails.size());
//            
//            for(int i = 0; i < flightsDetails.size(); i++) {
//            	heap.insert(flightsDetails.get(i).price);
//            }
//            int minprice = heap.findMin();
//            for(int i = 0; i < flightsDetails.size(); i++) {
//            	FlightDetails flight = flightsDetails.get(i);
//            	if(flight.price == minprice) {
//            		flight.printFlightDetails();
//            	}
//            }
//    		System.out.println("Do you want to restart: ");
//            ans = input.nextLine();
//        } while(ans.equals("Y") || ans.equals("y"));
//        
//        input.close();
//
////		parser.generateRoundTripUrl(deptartureLocation,
////			arrivalLocation,
////			deptartureDay,
////			deptartureMonth,
////			deptartureYear,
////			arrivalDay,
////			arrivalMonth,
////			arrivalYear,
////			adultCount
////		);
//
//		
//		
	}

	public static void print(String string) {
		System.out.println(string);
	}

}
