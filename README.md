# Visual Testing in Katalon Studio Revived

- @date Aug 2021
- @author kazurayam

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**

- [History](#history)
- [Visual Testing revived](#visual-testing-revived)
  - [(1) Simpler codeset](#1-simpler-codeset)
  - [(2) In a pre-designed directory structure, files are stored with index by "metadata"](#2-in-a-pre-designed-directory-structure-files-are-stored-with-index-by-metadata)
  - [(3) Packaged in a jar](#3-packaged-in-a-jar)
  - [(4) Usable outside Katalon Studio](#4-usable-outside-katalon-studio)
- [Examples in Katalon Studio](#examples-in-katalon-studio)
  - [create a project, resolve dependencies](#create-a-project-resolve-dependencies)
  - [Sample1: simply visit a URL and scrape](#sample1-simply-visit-a-url-and-scrape)
  - [Sample2: Visual Testing in Chronos mode](#sample2-visual-testing-in-chronos-mode)
  - [Sample3: Visual Testing in Twins mode](#sample3-visual-testing-in-twins-mode)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## History

Sep 2018, I published a project named "Visual Testing in Katalon Studio" at

- https://github.com/kazurayam/VisualTestingInKatalonStudio

Please have a look at the "Motivation" section in its README document to know why I made it. I tried to implement what I called "Visual Testing" in Katalon Studio. The project worked for me.

But I wasn't satisfied with it. Why? I would enumerate 3 problems about this project.

1. The codeset of [Visual Testing In katalon Studio](https://github.com/kazurayam/VisualTestingInKatalonStudio) project is too complicated. The project contains 28 Test Cases, 7 Test Suites, 4 Test Suite Collections, 1 Test Listener, 12 Keywords. After 3 years, I forgot them and unable to maintain them any longer.
2. The project enables me to compare a pair of 2 screenshots of Web pages in PNG image format. That is the only functionality it provides. More often I wanted to compare 2 texts scraped from web. E.g, 2 HTML files as Web Page source; 2 JSON files downloaded from a Web Service.
3. The project isn't packaged. It isn't distributable to others easily. It's too hard for people to reuse the codeset of the project to implement their own "Visual Testing" for their custom targets.

## Visual Testing revived

In 2021 July-August, I created a new project named [`materialstore`](https://github.com/kazurayam/materialstore). It is a small "object store" written in Groovy. In this project I re-designed and re-implemented my idea from scratch. I have achieved the followings.

### (1) Simpler codeset

The `materialstore` provides a self-contained API in Groovy, which encapsulates all of the useful functionalities of the previous [Visual Testing In katalon Studio](https://github.com/kazurayam/VisualTestingInKatalonStudio) project. A single Test Case in Katalon Studio empowered by the `materialstore` library can achieve whole job equivalent to the previous one. No need to struggle with that many components (28 Test Cases + 7 Test Suites + 4 Test Suite Collections + 1 Test Listener + 12 Keywords).

### (2) Material Object store indexed by metadata

By the word "material" I mean any type of files downloaded from Web applications during automated tests. Screenshot images (PNG), Web page source (HTML), JSON and XML responded by Web Services --- I call all of these "materials".

The `materialstore` provides capability of materializing (storing) files downloaded from web sites in a pre-designed directory structure (I call it the "**store**"). It is a small "object store".

An application writes files into the "store" associating "metatadata". The "materials" in the "store" are indexed by the associcated metadata. An application retrieves files from the store by "metadata" as key. An application does not look up files by name (Path). In turn, the application is not responsible for deciding and remembering the path of materials.

 A "metadata" of a material in the "store" is an instance of `java.util.Map<String, String>` with arbitrary key and value pairs. You can programme any kind of "metadata" and associate it to materials so that the materials are clearly identified. For example, you can associate the URL from which the web resource was retrieved; or you can associate the name of browser ("Chrome", "FireFox", "Safari", etc) which you used to take the screenshots; or you can associate the name of "Execution Profile" you used when you executed your Test Case in Katalon Studio.

Metadata composition is entirely up to the user application. Composing metadata is a bit difficult part of the `materialstore` library. It looks similar to the database table design in SQL-backed application.

### (3) Packaged in a jar

The artifact of `materialstore` is distributed as a single jar file. The jar file is available at the [Maven Central Repository](https://mvnrepository.com/artifact/com.kazurayam/materialstore). Therefore any Java/Groovy-based application can automate downloading the `materialstore-x.x.x.jar` to resolve dependency using Gradle and Maven.

### (4) Usable outside Katalon Studio

The `materialstore`'s jar has no immediate dependency on the Katalon Studio API. It is not dependent even on the Selenium Webdriver API. So the `materialstore` library can be used in any Java/Groovy project, not only in Katalon Studio. I can use it in a plain [Selenium WebDriver](https://www.selenium.dev/documentation/webdriver/)-based automated test project for Web UI on Gradle. I can use it in a plain [Appium Java Client](https://github.com/appium/java-client)-based automated test project for Mobile UI on Maven. I can use it in a plain [Apache HttpClient](https://hc.apache.org/httpcomponents-client-5.1.x/)-based automated test project for Web Services on Ant.

## "materialstore" documentation

I would publish 2 sets of documentation with examples to describe how to use the `materialstore` library to implement "Visual Testing". The following repository contains sample codes and documentation of the `materialstore` API (yet TODO). The examples run on Gradle + Selenium WebDriver + Apache HttpClient, outside Katalon Studio.

- [materialstore-examples](https://kazurayam.github.io/materialstore-examples/)

## Examples in Katalon Studio

Here I will present 3 examples which show how to use the `materialstore` library in Kataloon Studio. I aimed 2 of 3 examples replace the previous ["Visual Testing in Katalon Studio"](https://github.com/kazurayam/VisualTestingInKatalonStudio) achievements.

### create a project, resolve dependencies

You can make a new Katalon Studio project, import the required external dependencies, and write your Test Cases for "Visual Testing". Let me describe it first.

1. Install "Gradle" build tool into your PC. Please follow this [guide](https://gradle.org/install/) to install Gradle on your machine.

>You can use Gradle ver 7.0 as well as ver 6.x.

2. Open Katalon Studio GUI. Create a new project as usual in whichever directory you like.

>I will write a symbol `$projectDir` to express this project directory.

3. create `$projectDir/build.gradle`. You should copy and paste the source of:

- [build.gradle](build.gradle)

4. In the commandline you want to execute the following command:

```
$ cd $projectDir
$ gradle driver
```

5. The `gradle driver` will display some lines of messages in 10 seconds, and will finish successfully.

```
BUILD SUCCESSFUL in 1s
1 actionable task: 1 executed
```

6. Once the command finished, in the `$projectDir/Drivers` directory, you will find some jar files are automatically imported.

```
$ tree Drivers
Drivers
├── AI_ExecutionProfilesLoader-1.2.1.jar
├── AI_ashot-1.5.4.jar
├── AI_jsoup-1.13.1.jar
├── AI_materialstore-0.1.0.jar
└── AI_subprocessj-0.1.0.jar
```

These are downloaded from the [Maven Central Repositry](https://mvnrepository.com/). These are required to run the "Visual Testing" code in your new project in Katalon Studio locally.

>If you are going to push this project into Git repository, you should write the `.gitignore` file so that it ignores the `Drivers/` directory.

7. You have resolved external dependencies. Now you can start writing a Test Case.

### Sample1: simply visit a URL and scrape

At first, we will write a short Test Case in Katalon Studio that visits the [Google Search page](https://www.google.com/). We will take screenshots and HTML page sources of the Web page. We will store PNG files and HTML files into the `store` directory using the `materialstore` library. We will finally generate a HTML file in which we can view the stored files files.

You want to newly create a Test Case `Test Cases/main/GoogleSearch/scrapeGoogleSearch` in your project. Copy and paste the following sample source:

- [`Test Cases/main/GoogleSearch/searchGoogleSearch`](Scripts/main/GoogleSearch/scrapeGoogleSearch/Script1628518694544.groovy)

Once you have created the Test Case, you want to run it as usual by clicking the green button ![run button](docs/images/run_katalon_test.png) in Katalon Studio GUI.

When the Test Case finished, you will find a new directory `$projectDir/store` is created. In there you will find a tree of directories and files, like this:

```
$ tree store
store
├── scrapeGoogleSearch
│   └── 20210813_221052
│       ├── index
│       └── objects
│           ├── 01014deef318115a75ac1c3ab0f9844832c81c86.html
│           ├── 02625f7607199d99ca58b803d6fe51b7c94835e7.html
│           ├── 2563a225cb7bcd438ae12a6126b2091eb8e09e7d.png
│           ├── 5c002fbe44438341d3d92832d1e004198153976b.png
│           ├── 8370ecd0081e1fb9ce8aaecb1618ee0fc16b6924.html
│           └── efaed8443417a62faf35ee9d9b858592cd67bbae.png
└── scrapeGoogleSearch.html
```

- The `store/scrapeGoogleSearch.html` renders a view of the stored 6 files. You can see an working example here: [pls. click here](https://kazurayam.github.io/VisualTestingInKatalonStudio_revive/store/scrapeGoogleSearch.html).

![scrapeGoogleSearch.html](docs/images/scrapeGoogleSearch.html.png)

- Under the `store/scrapeGoogleSearch/yyyyMMdd_hhmmss/objects/` directory, there are 6 files. You can find 3 files with postfix `png`. As you can guess, these are PNG image files. These are the page screenshots. You can find 3 files with postfix `html`. These are Web page sources. The file name is 40 hex-decimal characters (SHA1 hash value of each file contents) appended with extension `.png`, `.html`.

- The `store/scrapeGoogleSearch/yyyyMMdd_hhmmss/index` file would be most interesting part. It look like [this](docs/store/scrapeGoogleSearch/20210813_221052/index):

```
8370ecd0081e1fb9ce8aaecb1618ee0fc16b6924	html	{"URL.host":"www.google.com", "URL.path":"/", "URL.protocol":"https"}
2563a225cb7bcd438ae12a6126b2091eb8e09e7d	png	{"URL.host":"www.google.com", "URL.path":"/", "URL.protocol":"https"}
...
```

The `index` file is a plain text file. Each lines corresponds to each files stored in the `objects` directory. An line of the `index` file has 3 parts delimited TAB characters.

```
<SHA1 Hash value of each file>\t<file type>\t<metadata>
```

In the test Case script, the code created  *metadata* for each objects. Typically a metadata will include information derived from the URL of the source Web Page. For example, the URL `http://www.google.com/` will be digested to for a metadata `{"URL.host":"www.google.com", "URL.path":"/", "URL.protocol":"https"}`. You can add any element into the metadata as you like.

The lines in the `index` file are sorted by the ascending order of *metadata* text.

The `materialstore` controls that the *metadata* text must be unique. Your code can not create multiple objects (multiple lines in the `index` file) with the same *metadata* value.

The 40 hex-decimal string (I call this "ID") of each file name is derived from the file content (without compression) by SHA1 algorithm. Yes, you can read every file by the ID but it is not convenient. The `materialstore` API provides method for your code to retrieve files in the `objects` directory by specifying a single *metadata* value. or a *pattern* that match the *metadata* of each objects.

The `index` file and the `materialstore` API provides a simple "object storage" indexed by *metadata*.

This object storage enables flexible control over the object stored in the `store` directory.

### Sample2: Visual Testing in Chronos mode


### Sample3: Visual Testing in Twins mode






