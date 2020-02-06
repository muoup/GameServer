# Masterdust GameServer
### Because nobody really liked local play anyway

***

Current Status

| Component | Status |  
|:----------|-------:|
| Server    |![Java CI](https://github.com/Masterdust12/GameServer/workflows/Java%20CI/badge.svg?branch=master)|
| Client Stable |![Java CI](https://github.com/Masterdust12/Game/workflows/Java%20CI/badge.svg?branch=master)|
| Client Experimental |![Java CI](https://github.com/Masterdust12/Game/workflows/Java%20CI/badge.svg?branch=temp)|

***
### Installation:
Server releases are kept here on stable and experimental versions, 
as well as on the GitHub for the game client

***
### Building from Source

The GameServer is currently rather easy to build from source.

Steps:
* Clone the repository
* `mkdir build && cd build`
* `mvn package --file ../pom.xml`

And you're done!

## Authors:
Zachary Verlardi: Wrote the server basically from scratch, and continues to work on it to this day  
Justin Ecarma: <insert stuff here, justin>  
Connor McDermid: Wrote in the `security` package and implemented a build system.