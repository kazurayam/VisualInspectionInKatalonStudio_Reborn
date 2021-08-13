# Visual Testing in Katalon Studio Revived

- @date Aug 2021
- @authro kazurayam

## History

Sep 2018, I published a project named "Visual Testing in Katalon Studio" at

- https://github.com/kazurayam/VisualTestingInKatalonStudio

Please have a look at the "Motivation" section in its README document to know what Problems I wanted to solve. I tried to implement what I called "Visual Testing" in Katalon Studio. The project worked for me.

But I wasn't satisfied with it. Why? I can enumerate 3 problems about this project.

1. The codeset of [Visual Testing In katalon Studio](https://github.com/kazurayam/VisualTestingInKatalonStudio) project is too complicated. The project contains 28 Test Cases, 7 Test Suites, 4 Test Suite Collections, 1 Test Listener, 12 Keywords. After 3 years, I forgot them and unable to maintein them any longer.
2. The project enables me comparing a pair of 2 screenshots of Web pages in PNG image format. That's all. Poor functinality! More often I wanted to compare 2 texts obtained from web. E.g, 2 HTML files as Web Page source; 2 JSON (or XML) files downloaded from Web Services.
3. The project isn't packaged. It isn't distributable to others easily. It's too hard for people to reuse the codeset of the project to implement their "Visual Testing" for their own targets.

## Visual Testing revived

In 2021 July-August, I created a new project named [`materialstore`](https://github.com/kazurayam/materialstore). In this project I re-designed and re-implemented my idea from scratch. I have achieved the followings.

### Simpler codeset

The `materialstore` provides a self-contained API in Groovy, which encapsulates all the functionalities of the previous version of [Visual Testing In katalon Studio](https://github.com/kazurayam/VisualTestingInKatalonStudio). A single Test Case in Katalon Studio empowered by the `materialstore` library can achieve whole job equivalent to the previous one. No need to struggle with that many components (28 Test Cases + 7 Test Suites + 4 Test Suite Collections + 1 Test Listener + 12 Keywords).

### In a pre-designed directory structure, files are stored with index by "metadata"

The `materialstore` provides capability of materializing (storing) any files (PNG, HTML, JSON, XML, etc) downloaded from web sites in a pre-designed directory structure (I call it the "**store**").

An application writes files into the store associating "metatadata" with it. Files in the "store" are **indexed by the associcated metadata**. An application retrieves files from the store by "metadata" as key. An application does not find files by name. So *an application does not have to decide (and remember) the path of stored files*.

 A "metadata" of a file in the "store" is essencially an instance of `java.util.Map<String, String>` with arbitrary key and value pairs. You can programme any kind of "metadata" to be associted to files to make them clearly identified. For example, you can associate the URL from which the web resource was retrieved. Or you can associate the name of browser ("Chrome", "FireFox", "Safari", etc) which you used to take the screenshots. Or you can associate the name of "Execution Profile" you used when you executed your Test Case in Katalon Studio. 
 
 Metadata composition is entirely up to you. Composing metadata is a bit difficult part of the `materialstore` library. It looks similar to Database table design in SQL-backed application.

### Packaged in a jar

The artifact of `materialstore` is distributed as a jar file. The jar file is available at the [Maven Central Repository](). Therefore any Java/Groovy-based application can automate downloading the `materialssotre-x.x.x.jar` to resolve dependency using Gradle and Maven.

### Usable out of Katalon Studio

The `materialstore`'s jar has no immediate dependency on the Katalon Studio API. It can be used in any Java/Groovy project, not only in Katalon Studio. I can use it in a plain [Selenium WebDriver](https://www.selenium.dev/documentation/webdriver/)-based WebUI automated test project. I can use it in a plain [Appium Java Client](https://github.com/appium/java-client)-based Mobile UI automated test project. I can use it in a plain [Apache HttpClient](https://hc.apache.org/httpcomponents-client-5.1.x/)-based Web Service automated test project.

## Description

I would publish 2 sets of documentation how to use the `materialstore` library to implement "Visual Testing".

### Outside KS description

The following repository contains sample codes and documentation for "Out of KS" cases.

- [materialstore-esample](https://github.com/kazurayam/materialstore-examples)

The examples run on Gradle + Selenium WebDriver + Apache HttpClient, not inside Katalon Studio.

Detail tutorials of making use of the `materialstore` API will be included here. (yet TODO)

### Inside KS description

Here I will explatin 3 examples of "Visual Testing Revived" in Kataloon Studio.

#### How to start with

You can make a new Katalon Studio project, import the required external dependencies, and write your Test Cases for "Visual Testing". Let me describe it first.

1. Install "Gradle" build tool into your PC. Please follow this [guide](https://gradle.org/install/) to install Gradle on your machine.

2. create a new Katalon Studio project as usual in whichever directory you like. 

>I will write a symbol `$projectDir` to express this project directory.

3. create `$projectDir/build.gradle` as follows:

```
plugins {
    id "com.katalon.gradle-plugin" version "0.0.7"
}

ext {
    ExecutionProfilesLoaderVersion = "1.2.0"
    materialstoreVersion = '0.1.0'
    ashotVersion = '1.5.4'
    javadiffutilsVersion= '4.9'
    jsoupVersion = '1.7.2'
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compile group: 'com.kazurayam', name: 'materialstore', version: "${materialstoreVersion}"
    compile group: 'com.kazurayam', name: 'ExecutionProfilesLoader', version: "${ExecutionProfilesLoaderVersion}"
    // https://mvnrepository.com/artifact/ru.yandex.qatools.ashot/ashot
    compile group: 'ru.yandex.qatools.ashot', name: 'ashot', version: "${ashotVersion}"
    // https://mvnrepository.com/artifact/io.github.java-diff-utils/java-diff-utils
    compile group: 'io.github.java-diff-utils', name: 'java-diff-utils', version: "${javadiffutilsVersion}"
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    compile group: 'org.jsoup', name: 'jsoup', version: "${jsoupVersion}"
}

```

4. In the commandline you want to execute the following command:

```
$ cd $projectDir
$ gradle katalonCopyDependencies
```

5. The command will display bunch of messages for 10 or 20 seconds, and will finish successfully.

```
Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.4.1/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 1s
1 actionable task: 1 executed
```

>Please don't mind *"Deprecated Gradle features were used ..."* message. It's another issue. It will not annoy you.


6. Once the command finished, you will find a lot of jar files are automatically donwloded in the `$projectDir/Drivers` directory.

```
$ tree ./Drivers
./Drivers
├── katalon_generated_ExecutionProfilesLoader-1.2.0.jar
├── katalon_generated_animal-sniffer-annotations-1.14.jar
├── katalon_generated_ashot-1.5.4.jar
├── katalon_generated_byte-buddy-1.8.15.jar
├── katalon_generated_checker-compat-qual-2.0.0.jar
├── katalon_generated_commons-codec-1.13.jar
├── katalon_generated_commons-compress-1.20.jar
├── katalon_generated_commons-exec-1.3.jar
├── katalon_generated_commons-io-2.8.0.jar
├── katalon_generated_commons-lang3-3.12.0.jar
├── katalon_generated_commons-logging-1.2.jar
├── katalon_generated_error_prone_annotations-2.1.3.jar
├── katalon_generated_groovy-all-2.4.21.jar
├── katalon_generated_gson-2.8.6.jar
├── katalon_generated_guava-25.0-jre.jar
├── katalon_generated_hamcrest-core-1.3.jar
├── katalon_generated_httpclient-4.5.10.jar
├── katalon_generated_httpclient5-5.0.3.jar
├── katalon_generated_httpcore-4.4.12.jar
├── katalon_generated_httpcore5-5.0.2.jar
├── katalon_generated_httpcore5-h2-5.0.2.jar
├── katalon_generated_j2objc-annotations-1.1.jar
├── katalon_generated_jarchivelib-1.1.0.jar
├── katalon_generated_java-diff-utils-4.9.jar
├── katalon_generated_jsoup-1.13.1.jar
├── katalon_generated_jsr305-1.3.9.jar
├── katalon_generated_materialstore-0.1.0-SNAPSHOT.jar
├── katalon_generated_okhttp-3.11.0.jar
├── katalon_generated_okio-1.14.0.jar
├── katalon_generated_selenium-api-3.141.59.jar
├── katalon_generated_selenium-chrome-driver-3.141.59.jar
├── katalon_generated_selenium-edge-driver-3.141.59.jar
├── katalon_generated_selenium-firefox-driver-3.141.59.jar
├── katalon_generated_selenium-ie-driver-3.141.59.jar
├── katalon_generated_selenium-java-3.141.59.jar
├── katalon_generated_selenium-opera-driver-3.141.59.jar
├── katalon_generated_selenium-remote-driver-3.141.59.jar
├── katalon_generated_selenium-safari-driver-3.141.59.jar
├── katalon_generated_selenium-support-3.141.59.jar
├── katalon_generated_slf4j-api-1.7.31.jar
├── katalon_generated_subprocessj-0.1.0.jar
└── katalon_generated_webdrivermanager-4.4.3.jar
```

7. You have resolved external dependencies. Now you can start writing a Test Case.





