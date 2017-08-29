package national;

import java.util.Arrays;
import java.util.HashSet;

public class Location {
    HashSet<String> location;

    public void getlocation(){
        String loc[] = { "Andaman and Nicobar Islands","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh","Chhattisgarh","Dadra and Nagar Haveli","Daman and Diu","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Lakshadweep","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Puducherry","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttar Pradesh","Uttarakhand","West Bengal","Mumbai","Delhi","Chennai","Bangalore","Hyderabad","Ahmedabad","Kolkata","Surat","Pune","Jaipur","Cochin","Lucknow","Kanpur","Nagpur","Indore","Thane","Bhopal","Visakhapatnam","Patna","Vadodara","Ghaziabad","Ludhiana","Agra","Nashik","Faridabad","Meerut","Rajkot","Solapur","Varanasi","Srinagar","Aurangabad","Dhanbad","Amritsar", "Navi Mumbai"};
        location = new HashSet<>(Arrays.asList(loc));
        System.out.println(location.size());
        System.out.println(location);
    }
    public static void main(String[] args) {
        new Location().getlocation();
    }
}
