# Visual Testing in Katalon Studio Revived

- @date Aug 2021
- @authro kazurayam

## History

Sep 2018, I published a project named "Visual Testing in Katalon Studio" at

- https://github.com/kazurayam/VisualTestingInKatalonStudio

I tried to implement an idea that I called "Visual Testing" in Katalon Studio. The project worked for me. But I wasn't satisfied with it. Why? I have had in my mind 3 unsolved problems about this project.

1. The codeset of [Visual Testing In katalon Studio](https://github.com/kazurayam/VisualTestingInKatalonStudio) project is too complicated. The project contains has 28 Test Cases, 7 Test Suites, 4 Test Suite Collections, 1 Test Listener, 12 Keywords. After 3 years, the creator (me!) forgot them and unable to maintein them any longer.
2. The project enables me comparing a pair of 2 screenshots of Web pages in PNG image format. That's all. This is not enough. Often I wanted to compare 2 texts obtained from web. For example, 2 HTML files as Web Page source; 2 JSON (or XML) files downloaded from Web Services.
3. The project isn't packaged, isn't distributable to others. It's too hard for people (other than the creator) to reuse the codeset of the preoject to implement "Visual Testing" for their targets.

## Mantra of "Visual Testing Revived"

In 2021 July-August, I created a new repository named [`materialstore`](https://github.com/kazurayam/materialstore). In this project I re-designed and re-implemented my idea from scratch.

- The `materialstore` provides a simple self-contained API in Groovy, which encapsulates all the functionalities of the previous version of [Visual Testing In katalon Studio](https://github.com/kazurayam/VisualTestingInKatalonStudio). Using the `materialstore` libary, a single Test Case script can achieve whole job equivalent to previous one. No need to write that many components (Test Cases + Test Suites + Test Suite Collections + Test Listener + Keywords) as previous.

- The `materialstore` provides capability of materializing (storing) any files (PNG, HTML, JSON, XML, etc) downloaded from web sites in a pre-designed directory structure. You can store files associated with "metatadata". You can programme what kind of "metadata" to be associted to files. For example, you can associate the URL from which the web resource was retrieved.

- The artifact of `materialstore` is distributed as a jar file. The jar file is available at the [Maven Central Repository](). Therefore any Java/Groovy-based can easily resolve dependency to the `materialssotre-x.x.x.jar` using Gradle and Maven.

- The `materialstore`'s jar has no dependency on the Katalon Studio API. I can use it in any Java/Groovy project. I can use it in a plain [Selenium WebDriver](https://www.selenium.dev/documentation/webdriver/)-based WebUI automated test project. I can use it in a plain [Appium Java Client](https://github.com/appium/java-client)-based Mobile UI automated test project. I can use it in a plain [Apache HttpClient](https://hc.apache.org/httpcomponents-client-5.1.x/)-based Web Service automated test project.



