
# Metadefender Core Client
> Java library for the Metadefender V4 REST API.


## Features

This library makes it easy to:
* Connect to a Metadefender Core API point
* Scan files for infections
* Retrieve previous file scan results by file hash, or data_id
* Login / Logout from the REST point
* Fetching Available Scan Rules
* Fetching Engine/Database Versions
* Get Current License Information
* Get the version of the Metadefender Core


## Usage with Maven

Add this dependency to you project:

**Gradle**
```
compile 'com.opswat:metadefender-core-client:4.0.0'
```

**Apache maven**
```
<dependency>
    <groupId>com.opswat</groupId>
    <artifactId>metadefender-core-client</artifactId>
    <version>4.0.0</version>
</dependency>
```


## Live example

```
$ git clone https://github.com/OPSWAT/Metadefender-Core-Client.git
$ cd Metadefender-Core-Client
$ ./gradlew run -PappArgs="['-h', 'http://localhost:8008', '-u', 'yourUsername', '-p', 'yourPassword', '-a', 'info']"
$ ./gradlew run -PappArgs="['-h', 'http://localhost:8008', '-u', 'yourUsername', '-p', 'yourPassword', '-a', 'scan', '-f', '/tmp/testFileToScan.zip']"
```

**Note**: you have to have a running Metadefender Core V4 API on localhost.



## Example usage

See example java code under the `com.opswat.metadefender.core.clientexample` package.
