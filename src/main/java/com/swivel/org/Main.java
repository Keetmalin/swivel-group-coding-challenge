package com.swivel.org;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swivel.org.models.Organization;
import com.swivel.org.models.Ticket;
import com.swivel.org.models.User;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Keet Sugathadasa
 * @project comswivelorg
 * <p>
 * This is the main class of the project
 * TODO add loggers to the file
 */
public class Main {

    // These Lists will contain the objects of each type.
    // Searching will cost O(n) in worst case
    private static List<Organization> organizations = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static List<Ticket> tickets = new ArrayList<>();

    // These Maps will contain the object references with the keys as ids
    // Searching will cost O(1) in worst case
    private static Map<String, Organization> orgMap = new HashMap<>();
    private static Map<String, User> userMap = new HashMap<>();
    private static Map<String, Ticket> ticketMap = new HashMap<>();

    public static void main(String[] args) {

        // Start the function by loading data from the give data sets
        loadData();

        // Give the user the option to exit by hittin CTRL + C
        while (true) {

            // Display template messages
            System.out.println("Press CTRL + C to exit at any time, Press any of the following to continue");
            System.out.println("\n\tSelect search options:\n");
            System.out.println("\t\tPress 1 to search\n");
            System.out.println("\t\tPress CTRL + C to exit\n");

            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            if ("1".equals(input)) {
                // Only the search functionality is available at the moment
                generateSearchMode();
            } else {
                System.out.println("Other functions are not supported yet");
            }

            System.out.println("\n\n");
        }

    }

    /**
     * Generate The template required for the search mode, and call necessary search methods
     */
    private static void generateSearchMode() {
        System.out.println("Select 1) User or 2) Tickets or 3) Organizations");
        Scanner sc = new Scanner(System.in);
        String searchId = sc.nextLine();

        System.out.println("Enter search term");
        sc = new Scanner(System.in);
        String term = sc.nextLine();

        System.out.println("Enter search value");
        sc = new Scanner(System.in);
        String value = sc.nextLine();

        try {
            // Search will only cost O(n) in the worst case and O(1) in the best case
            searchDataSet(searchId, term, value);
        } catch (NoSuchMethodException e) {
            System.out.println("Could not find the value entered by the user, in the searching object type");
        } catch (InvocationTargetException e) {
            System.out.println("Could not invoke method the user is requesting for");
        } catch (IllegalAccessException e) {
            System.out.println("Could not access method due to access permissions");
        }
    }

    /**
     * This function will carry out the basic searching as per the given requirements
     *
     * @param searchId Which object type needs to be searched
     * @param term     What is the term that needs to be searched
     * @param value    What is the value that needs to be searched
     */
    private static void searchDataSet(String searchId, String term, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // This is for User Search
        String methodName = "get" + term.substring(0, 1).toUpperCase() + term.substring(1);
        if ("1".equals(searchId)) {
            System.out.println("Searching users");

            Method fieldGetter = User.class.getMethod(methodName);
            User user = new User();

            for (User u : users) {
                if (String.valueOf(fieldGetter.invoke(u).toString()).equals(value)) {
                    user = u;
                    break;
                }
            }
            for (String a : user.getAssigneeTickets()) {
                System.out.println("Assignee ticket subject: " + ticketMap.get(String.valueOf(a)).getSubject());
            }
            for (String a : user.getSubmittedTickets()) {
                System.out.println("Assignee ticket subject: " + ticketMap.get(String.valueOf(a)).getSubject());
            }
            System.out.println("Organization Name: " + orgMap.get(String.valueOf(user.getOrganization_id())).getName());

            // This is for Ticket Search
        } else if ("2".equals(searchId)) {
            System.out.println("Searching tickets");

            Method fieldGetter = Ticket.class.getMethod(methodName);
            Ticket ticket = new Ticket();

            for (Ticket t : tickets
            ) {
                if (String.valueOf(fieldGetter.invoke(t).toString()).equals(value)) {
                    ticket = t;
                    break;
                }
            }
            System.out.println("Assigned User Name: " + userMap.get(String.valueOf(ticket.getAssignee_id())).getName());
            System.out.println("Submitted User Name: " + userMap.get(String.valueOf(ticket.getSubmitter_id())).getName());
            System.out.println("Organization Name: " + orgMap.get(String.valueOf(ticket.getOrganization_id())).getName());

            // This is for Organization Search
        } else if ("3".equals(searchId)) {
            System.out.println("Searching organizations");

            Method fieldGetter = Organization.class.getMethod(methodName);
            Organization org = new Organization();

            for (Organization o : organizations
            ) {
                if (String.valueOf(fieldGetter.invoke(o).toString()).equals(value)) {
                    org = o;
                    break;
                }
            }

            for (String a : org.getUsers()) {
                System.out.println("Associated user name: " + userMap.get(String.valueOf(a)).getName());
            }
            for (String a : org.getTickets()) {
                System.out.println("Ticket subject: " + ticketMap.get(String.valueOf(a)).getSubject());
            }

        }
    }

    /**
     * This function will load the necessary data at the initial loading
     * TODO Move this function to utilities module
     */
    private static void loadData() {
        // Read the JSON Arrays from the text files
        JSONArray jsonArrayOrgs = readJsonData("organizations.json");
        JSONArray jsonArrayUsers = readJsonData("users.json");
        JSONArray jsonArrayTickets = readJsonData("tickets.json");

        Gson gson = new Gson();

        // Load objects into array lists
        organizations = gson.fromJson(jsonArrayOrgs.toJSONString(), new TypeToken<List<Organization>>() {
        }.getType());
        users = gson.fromJson(jsonArrayUsers.toJSONString(), new TypeToken<List<User>>() {
        }.getType());
        tickets = gson.fromJson(jsonArrayTickets.toJSONString(), new TypeToken<List<Ticket>>() {
        }.getType());

        // Map the associated users and tickets to each organization
        for (Organization i : organizations) {
            // Create a map for easy access in O(1)
            orgMap.put(String.valueOf(i.get_id()), i);
            i.setTickets(tickets);
            i.setUsers(users);
        }

        // Map the submitted and assigned tickets of specific users
        for (User i : users) {
            // Create a map for easy access in O(1)
            userMap.put(String.valueOf(i.get_id()), i);
            i.setSubmittedTickets(tickets);
            i.setAssigneeTickets(tickets);
        }

        // Create a map for easy access in O(1)
        for (Ticket i : tickets) ticketMap.put(String.valueOf(i.get_id()), i);
    }

    /**
     * This function will read JSON data from a given file
     * TODO move this function into a util module
     *
     * @param fileName name of the file to be read
     * @return A JSONArray of objects
     */
    private static JSONArray readJsonData(String fileName) {
        JSONParser jsonParser = new JSONParser();

        String filePath = new File("").getAbsolutePath();

        Reader reader = null;
        try {
            reader = new FileReader(filePath.concat("/src/main/java/com/swivel/org/dataset/" + fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File cannot be located within the project.");
        }
        Object obj = null;
        try {
            obj = jsonParser.parse(reader);
        } catch (IOException e) {
            System.out.println("Error occurred when accessing the reader object");
        } catch (ParseException e) {
            System.out.println("Could not parse JSON into an Object in Java");
        }

        return (JSONArray) obj;

    }

}
