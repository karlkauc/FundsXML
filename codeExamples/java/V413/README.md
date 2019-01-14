# FundsXML 4.3.1 Example

This is an example of creating and reading FundsXML Files with [XML Beans](https://xmlbeans.apache.org/).  
A compiled schema (jar) is provided on [the FundsXML schema site](http://schema.fundsxml.org) and als library in this project.

## basics
* Language:   Java
* difficulty: Beginner


| PRO          | CON                    |
  ------------ | ------------------------
| static typed | lot of typing needed   |
| very fast    |                        |
| runs on all major operating systems | |
| schema validity can be checked inside the source code | |

## how to run the example
This example uses [gradle](https://gradle.org/) as build tool.  
Clone the source code or download it as a zip file. 

Windows: 
```Shell
gradlew.bat run
```

Linux:
```Shell
./gradlew run
```

## output 
Here is an example output of writing a FundsXML File with "CreateFundsXMLFile.java":

![Output](img/output_writing.PNG)


Here is an example output of reading a FundsXML File with "ReadFundsXMLFile.java":
![Output](img/output_reading.PNG)