# Visual Testing in Katalon Studio Revived

- @date Aug 2021
- @authro kazurayam

## History

Sep 2018, I published a project named "Visual Testing in Katalon Studio" at

- https://github.com/kazurayam/VisualTestingInKatalonStudio

Please have a look at the "Motivation" sectin of  its README to know what Problems I wanted to solve. I tried to implement what I called "Visual Testing" in Katalon Studio. The project worked for me. But I wasn't satisfied with it. Why? 

I have had 3 unsolved problems about this project in mind.

1. The codeset of [Visual Testing In katalon Studio](https://github.com/kazurayam/VisualTestingInKatalonStudio) project is too complicated. The project contains 28 Test Cases, 7 Test Suites, 4 Test Suite Collections, 1 Test Listener, 12 Keywords. After 3 years, I forgot them and unable to maintein them any longer.
2. The project enables me comparing a pair of 2 screenshots of Web pages in PNG image format. That's all. Poor functinality! More often I wanted to compare 2 texts obtained from web. E.g, 2 HTML files as Web Page source; 2 JSON (or XML) files downloaded from Web Services.
3. The project isn't packaged. It isn't distributable to others easily. It's too hard for people to reuse the codeset of the project to implement their "Visual Testing" for their own targets.

## Visual Testing has revived

In 2021 July-August, I created a new project named [`materialstore`](https://github.com/kazurayam/materialstore). In this project I re-designed and re-implemented my idea from scratch. I have achieved the followings.

### Simple code structure

The `materialstore` provides a self-contained API in Groovy, which encapsulates all the functionalities of the previous version of [Visual Testing In katalon Studio](https://github.com/kazurayam/VisualTestingInKatalonStudio). A single Test Case in Katalon Studio empowered by the `materialstore` library can achieve whole job equivalent to the previous one. No need to struggle with that many components (28 Test Cases + 7 Test Suites + 4 Test Suite Collections + 1 Test Listener + 12 Keywords).

### In a pre-designed directory structure, files are indexed by "metadata"

The `materialstore` provides capability of materializing (storing) any files (PNG, HTML, JSON, XML, etc) downloaded from web sites in a pre-designed directory structure (I call it "**store**").

An application writes files into the store associating "metatadata" with it. Files in the "store" are **indexed by the associcated metadata**. An application retrieves files from the store by "metadata" as key. An application does not find files by name. So *an application does not have to decide (and remember) the path of stored files*.

 A "metadata" of a file in the "store" is essencially an instance of `java.util.Map<String, String>` with arbitrary key and value pairs. You can programme any kind of "metadata" to be associted to files to make them easily/clearly identified. For example, you can associate the URL from which the web resource was retrieved. Or you can associate the name of browser ("Chrome", "FireFox", "Safari", etc) which you used to take the screenshots. Decision is up to you.

### Packaged in a jar

The artifact of `materialstore` is distributed as a jar file. The jar file is available at the [Maven Central Repository](). Therefore any Java/Groovy-based application can automate downloading the `materialssotre-x.x.x.jar` to resolve dependency using Gradle and Maven.

### Usable out of KS

The `materialstore`'s jar has no immediate dependency on the Katalon Studio API. It can be used in any Java/Groovy project, not only in Katalon Studio. I can use it in a plain [Selenium WebDriver](https://www.selenium.dev/documentation/webdriver/)-based WebUI automated test project. I can use it in a plain [Appium Java Client](https://github.com/appium/java-client)-based Mobile UI automated test project. I can use it in a plain [Apache HttpClient](https://hc.apache.org/httpcomponents-client-5.1.x/)-based Web Service automated test project.

## Description


##



