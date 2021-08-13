# Visual Testing in Katalon Studio Revived

- @date Aug 2021
- @author kazurayam

<!-- START doctoc -->
<!-- END doctoc -->

## History

Sep 2018, I published a project named "Visual Testing in Katalon Studio" at

- https://github.com/kazurayam/VisualTestingInKatalonStudio

Please have a look at the "Motivation" section in its README document to know what problems I wanted to solve. I tried to implement what I called "Visual Testing" in Katalon Studio. The project worked for me.

But I wasn't satisfied with it. Why? I would enumerate 3 problems about this project.

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


I would publish 2 sets of documentation with examples to describe how to use the `materialstore` library to implement "Visual Testing".

## mateialstore-examples project

The following repository contains sample codes and documentation for "Out of KS" cases.

- [materialstore-examples](https://github.com/kazurayam/materialstore-examples)

The examples run on Gradle + Selenium WebDriver + Apache HttpClient, not inside Katalon Studio.

Detail tutorials of making use of the `materialstore` API will be included here. (yet TODO)

## VisualTestingInKatalonStudio_revive project

Here I will explatin 3 examples of "Visual Testing Revived" in Kataloon Studio.

### create a project, resolve dependencies

You can make a new Katalon Studio project, import the required external dependencies, and write your Test Cases for "Visual Testing". Let me describe it first.

1. Install "Gradle" build tool into your PC. Please follow this [guide](https://gradle.org/install/) to install Gradle on your machine. Version 6.x is required.

>Using Gradle v7.x, the following instruction may fail.

2. Open Katalon Studio GUI. Create a new project as usual in whichever directory you like.

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

5. The command will display bunch of messages in 10-20 seconds, and will finish successfully.

```
Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.4.1/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 1s
1 actionable task: 1 executed
```

>Please don't mind *"Deprecated Gradle features were used ..."* message. It's another issue. It will not annoy you.


6. Once the command finished, you will find a lot of jar files (more then 40 in fact) are automatically donwloded in the `$projectDir/Drivers` directory. These are neccessary anyway. You don't have to worry about what's in there.

>If you are storing this project into Git repository, you should `.gitignore` the `Drivers/` directory, as those jars should not be included.

7. You have resolved external dependencies. Now you can start writing a Test Case.


### Sample1: simply visit a URL and scrape

At first, we will write a short Test Case in Katalon Studio that visits the [Google Search page](https://www.google.com/). We will take screenshots and HTML page sources of the Web page. We will store PNG files and HTML files into the `store` directory using the `materialstore` library. We will finally generate a HTML file in which we can view the stored files files.

You want to newly create a Test Case `Test Cases/main/GoogleSearch/scrapeGoogleSearch` in your project. Copy and paste the following sample source:

- [`Test Cases/main/GoogleSearch/searchGoogleSearch`](Scripts/main/GoogleSearch/scrapeGoogleSearch/Script1628518694544.groovy)

Once you have created the Test Case, you want to run it as usual by clicking the green button ![run button](docs/images/run_katalon_test.png) in Katalon Studio GUI.

When done, you will find a new directory `$projectDir/store` is newly created. In there you will find a tree of directories and files, like this:

```

```

### Sample2: Visual Testing in Chronos mode


### Sample3: Visual Testing in Twins mode






