# AmortizationSchedule 

AmortizationSchedule is an amortization schedule programming using Java.

## Refactoring Description

1. The **AmortizationCalculator** class hides the details about how to calculate monthly payment schedule. It provides three **setter** functions, which pass values to the amortization calculation, such as the loan amount, the annual interest rate, and the loan term. Thus the amortization scheduler can **resue** the calculator object by seting different values. 
2. The **AmortizationPayment** class encapsulate the details of monthly payment. It provides an immutable object without offering any **setter** functions.
3. The **AmortizationScheduler** class provides the interface for input and output. It uses object composition to include a amortization calculator as its member variable.
4. The **AmortizationCalculatorTest** class is a unit test to verify the result of monthly payment calculation.

## Build and Test 

AmortizationSchedule uses the Ant build tool to compile the code and run tests. Make sure you have [Ant](http://ant.apache.org) installed.

To compile the AmortizationSchedule:
```sh
ant compile
```

To run the unit tests use the test build target:
```sh
ant test
```

To generate AmortizationSchedule Java documentation:
```sh
ant javadocs
```

To generate the java file and run the AmortizationSchedule:
```sh
ant dist
java -jar dist/amortization.jar
```

## Proposals for Further Refactoring

I have some proposal for futher refactoring which I don't have enough time to implement:
- Use the generic class [Range](http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/Range.html) from Google Guava library to replace the integer & double array for value ranage validation.
- Use a **factory** design patten to generate amortization calculator. Thus we can provide calculator for monthly, bi-weekley, weekley payment schedule.
