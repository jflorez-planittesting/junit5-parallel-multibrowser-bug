Pre-Conditions:
* Have chromedriver AND geckodriver in your PATH environment variable
* JDK 17 and maven 3.8.6

To run tests in sequential order set properties->parallel-test to false in the pom.xml file
To tun tests in parallel set properties->parallel-test to true in the pom.xml file

To see the expected result set parallel-test to false and run mvn test
To see the bug set parallel-test to true and run mvn test