TfL Road Demo
------------------------------------------------
This app fetches road data using the TfL Road API


API Usage
------------------------------------------------
In order to use it you will need to register for a developer key here: https://api-portal.TfL.gov.uk/

Please get a TfL Application ID and Application Key and copy it to $HOME/.gradle/gradle.properties as follows:

ROAD_API_ID="XXX"

ROAD_API_KEY="YYY"

Application Architecture
------------------------------------------------
Model View Presenter and Clean Architecture principles were utilised as architectural framework

Installation
------------------------------------------------
Clone the project and import it to Android Studio.
During development I used latest stable version of Android Studio 3.2.1
Gradle wrapper 4.6 

Run the application
------------------------------------------------
If you use Android Studio, after successful source code import the main application module should 
be ready to be deployed to a phone or an emulator. 

From the command line you can use:

./gradlew clean build

The command above will build the project and should produce one debug and one release apk. 
The apk can be installed from the command line as follows (assuming adb is installed):

adb install -r /path/to/apk

Note that gradle build command also runs the tests for all build variants

Run the tests
------------------------------------------------
If you use Android Studio, navigate to src/test/java folder. There you will find all relevant tests.
Right click on the package and execute the desired tests. The tests provided cover 100% our
Presenter class, which incorporates all our business logic.

From the command line you can use:

./gradlew clean test

API considerations
------------------------------------------------
Currently the API returns an array with all the relevant data, in the format:

```
[
  {
    "$type": "TfL.Api.Presentation.Entities.RoadCorridor, TfL.Api.Presentation.Entities",
    "id": "a2",
    "displayName": "A2",
    "statusSeverity": "Good",
    "statusSeverityDescription": "No Exceptional Delays",
    "bounds": "[[-0.0857,51.44091],[0.17118,51.49438]]",
    "envelope": "[[-0.0857,51.44091],[-0.0857,51.49438],[0.17118,51.49438],[0.17118,51.44091],[-0.0857,51.44091]]",
    "url": "/Road/a2"
  }
]
```

This requires an additional step on behalf of the developer so as to parse the json array. 
Alternatively, the API could return a json object with all the relevant information, as follows:

```
{
    "$type": "TfL.Api.Presentation.Entities.RoadCorridor, TfL.Api.Presentation.Entities",
    "id": "a2",
    "displayName": "A2",
    "statusSeverity": "Good",
    "statusSeverityDescription": "No Exceptional Delays",
    "bounds": "[[-0.0857,51.44091],[0.17118,51.49438]]",
    "envelope": "[[-0.0857,51.44091],[-0.0857,51.49438],[0.17118,51.49438],[0.17118,51.44091],[-0.0857,51.44091]]",
    "url": "/Road/a2"
}
```
 



