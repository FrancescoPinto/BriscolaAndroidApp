All the source code for the first release is contained in:
1) it.ma.polimi.briscola.controller (contains the class that controls the execution of a match)
2) it.ma.polimi.briscola.model.briscola (the various tests)
3) it.ma.polimi.briscola.model.briscola.twoplayers
4) it.ma.polimi.briscola.model.deck

Some code for the full release has been developed (however it is not granted to work!), it is contained in:
it.ma.polimi.briscola.forfullrelease and its subpackages
it.ma.polimi.briscola.junk (junk files I should have removed)

Automated tests with JUnit are under 
it.ma.polimi.briscola (in the test folder in the Project View of android studio)

Javadoc is under the folder JavaDoc

Recommendation: I would recommend one to start reading the source code in the following order:
1) package it.ma.polimi.briscola.model.deck enums
2) package it.ma.polimi.briscola.model.deck interfaces
3) package it.ma.polimi.briscola.model.deck abstract classes
4) package it.ma.polimi.briscola.model.deck concrete classes
5) package it.ma.polimi.briscola.model.briscola enum
6) package it.ma.polimi.briscola.model.briscola.twoplayers classes (read for last Briscola2PMatchConfig), the classes Briscola2PMatchRecord and Briscola2PMatchScoreRanking are not necessary for this release (it is possible to ignore them)
7) package it.ma.polimi.briscola.controller class

THE METHOD moveTest REQUIRED FOR TESTING IS PROVIDED AS A STATIC METHOD UNDER it.ma.polimi.briscola in the class MoveTest
All the automated tests I performed can be executed by running the class called TestSuit (incomplete tests about feature not required to be working for first release, such as Briscola2PAITournament and Briscola2PMatchRecordTest are not included in the suit and not granted to execute correctly)