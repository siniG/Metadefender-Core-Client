
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
