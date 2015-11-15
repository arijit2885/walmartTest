This readme captures how to run the walmart search assignment along with design decisions and caveats to the solution provided.

# Getting started :
  This project has been developed on OSX using eclipse Luna IDE and JDK 1.8.0_25. To run the project you could do one of the following :
  OPTION 1 : 
  1. Import the project directly from github to Eclipse IDE 
  2. Add the selenium-server-standalone-2.47.1.jar in the CLASSPATH
  3. Download the chromedriver executable based on your platform (Default is for OSX. Required only if browser type is chrome)
  3. Update the test.properties to provide the test data as per your need.
  4. Run the WalmartStore.java as a Java application
  
  OPTION 2: 
  1. Checkout the code directly from github using git commands
  2. Add the selenium-server-standalone-2.47.1.jar in the CLASSPATH
  3. Update the test.properties to provide the test data as per your need
  4. Compile and run the WalmartStore.java 
  
# What does the test do ?
  The code executes the entire scenario to automate as stated in the assignment. (Steps 1-8). It has also been enabled to run on Android device (requires a separate library to be loaded in the class path). This has been tested on Chrome and Firefox browser on OSX.This has not been tested on Safari since I did not have the Apple developer certificate to enable selenium RC extension for that browser. However, the code is not browser dependent and should work for all browsers.

# Design decisions

  1. The design pattern intended to be used here is Page object model. In this model, each page of the application is represented as a class and the actions on that page would be the methods. For e.g. the Walmart home page is "WalmartStore.java" class and search option is searchItem (String item, String brand).
  2. Any scenario to be automated can be built by intantiating an object of each page that is to be visted and invoking the corresponding method for the action.
  3. Data driven approach has been used by leveraging Java properties to feed the test data to the test.
  4. TestNG asserts have been used to validate the verification points for the test.
  5. Robustness of the test has been acheived by using "WebDriverWait" method so that the web elements are ensured to be visible before clicking on them.
  6. Uniqueness of the product is found by extracting the product/inventory ID from the xpath.

# Caveats & TODOs
  1. Even though the desired design is to use the PageObject model, due to time constraints all the methods (actions) for different pages have been wrapped under a single class
  2. Search could be made more refined. Currently, the code has been tested with "socks" and a particular brand item as it appears in the DOM ("Hanes-Men-s-12-Pack-Crew-Socks"). This could be enhanced to make it more intelligent and user friendly.
  3. The web element identifiers (ID,CSS,xpath) etc. could be externalised into a separate constants/properties file.
  3. Exception handling could be better. Currently there is just on try/catch block for all the exceptions. This may be improved by implementing custom exception handling class to distinguish between product,framework and test exceptions.
  4. Logging could be implemented to capture the current execution state of the test run. 
  5. Reporting can be added for asserting the overall pass/fail status of the test along with the number of asserts that have succeeded/failed.
  5. Code has not been tested for all the browsers (Safari,iOS,Android)
  6. The code could be wrapped around a test framework (TestNG,Cucumber etc.) for better control and extensibility.
  
  
