*****************************************
* JavaDiKt Mirai /　未来                 *
* Version 1.1.6beta - 2011              *
* ©Luis Alfonso Arce González           *
* Licensed under GNU GPLv3              *
* For further information visit:        *
* www.javadikt.net                      *
*****************************************

Welcome to JavaDiKt. This is the start point
in order to look for something in JavaDikt.
If you can understand spanish, there are a
fancy and complete PDF dossier in 
"resources/diffusion/Dossier JavaDiKt.pdf". 

1.- Prologue

JavaDiKt is a Kanji dictionary aimed to 
be used by Japanese students of all levels.
  __
 /
|
|==The Name=
|
| +JavaDiKt is Java: and because it is 
|   Java, it will run in any OS with a 
|   Java Virutal Machine installed on it.
|
| +JavaDiKt is diKt: JavaDiKt uses the 
|   awesome collaborative kanji database 
|   KANJIDIC, part of the Jim Breen's 
|   project WWWJIC.
 \
  |
  |===The Tree Pillars==
  |
  | +Complex search capabilities: many 
  |   criterias can be mixed into a simple 
  |   natural language expression that defines
  |   a kanji or a group of kanjis using 
  |   disjunctions and conjunctions.
  |
  | +Combining kanji drawings and criteria: 
  |   drawing and ordinary criterias can be 
  |   mixed in order to improve search 
  |   results. The asterisk drawing model 
  |   panel makes easier to find the exact  
  |   kanji you are looking for.
  |
  |  +Advanced exporting features: kanjis can
  |   ("will be able to" would be better) be
  |   exported into many file formats(PDF, 
  |   ODT, HTML, PNG, SVG...) using a great 
  |   wide range of styles(FlashCard, Table, 
  |   Raw, QuickList, Dictionary entry...) 
  |
 /
|
|==The interesting stuff==	
|
| +Ready to run: no extra libraries or
|   installation procedures are needed to run
|   JavaDiKt except for the Java Virtual 
|   Machine. Not even a complete japanese font
|   is needed to be installed in the system.  
|   Just unzip it and enjoy.
|
| +Multilanguage support: JavaDiKt was thought
|  from the beginning as a fully translatable 
|  application. Just contact me if you want to
|  translate JavaDiKt into your language or if
|  you want to improve a JavaDiKt's currently
|  in-use translation (contact information at
|  the end)
 \_

2.- Change log

|-1.1.6beta  Version
|
| 	*Animated export dialog prompts while
|    exporting
|   *Solved bug in which when exporting in PDF
|    using unicode chars they appeared as "?"
|   *Solved bug in which unknown stroke "E"
|    was added in draw panel on many JVM
|   *Solved bug that prevented to execute
|    JavaDiKt in mac using .app launcher
|	*Solved many minor bugs
|
|-1.1.5beta  Version
|
| 	*Added HTML and PDF exporters
|	*Added Table and FlashCard stylers
|	*Added etid context menu to the 
|	 remaining components(dic list,
|	 variants, radical window,...)
|	*Improved launch time
|	*New manual in HTML
|	*Animated splash screen
|	*Solved many minor bugs
|
|-1.1.4beta  Version
|
| 	*Significantly improved database 
|	 performance
|	*Added help button that displays an info 
|	 window about the selected kanji field
|	*Graphic interface changed
|	*Solved bug that prevented some users to 
|	 add criteria using drawings 
|	*Improved Mac system's compatibility
|	*Corrected some mistakes in manual's kanji
|	 information
|	*Solved many minor bugs
|
|-1.1.3beta  Version
|
| 	*Added stroke order windows in 6000 kanjis
|	*Added edit context menus to the main and 
|	 kanji information windows
|	*Some visual improvements like the 
|	 reproportion of some window's sizes
|	*Added detailed information about kanji 
|	 fields in user's manual
|	*Added a launcher for mac
|	*Improved interface in mac systems
|	*Solved bug that prevented to see kanjis
|	 in a exported file in window's systems
|	*Solved many minor bugs
|
|-.1.2beta  Version
|
|	*Solved many bugs
|	*Improved icon quality
|	*Improved kanji information window 
| 	 management system
|	*Added "Close all Windows" button to 
| 	 results panel
|	*Added searching by inequalities feature
|	 to dictionary reference criteria
|
|-1.1.1beta Version
|
|	*Solved many bugs
|	*Added kanji basic book 1&2 references
|	*Added new version checker
|	*Added user's manual
|
|-1.0.1beta Version
|
|	*First version

3.- Building JavaDiKt

In order to build JavaDiKt you'll need to have
Java Development Kit installed in your OS.

To build JavaDiKt, open the command line and 
navigate to the folder where the JavaDiKt's 
"make.sh" file, source code folder "trunk" and
the resource folder "resources" are placed. 
Then, execute make.sh script typing:

	./make.sh

A complete binary distribution from the 
current state of the source code will be 
generated in the new folder "out_javadikt".

4.- Building JavaDiKt's database

In order to build JavaDiKt you'll need to have
Java Development Kit installed in your OS.

To build JavaDiKt's database files, open the
command line and navigate to the folder where
the JavaDiKt's makeDatabase.sh" file, source
code folder "trunk" and the resource folder 
"resources" are placed. Then, execute make.sh
script typing:

	./makeDatabase.sh

The JavaDiKt's database files "kanjidict.jdk"
and "trees.zobj" will be created in folders
/resources/data/dict/ and resources/data/trees
respectively.

5.- The project structure

This is the basic description of JavaDiKts
file system structure. There are an extended
description in spanish in the following file

  resources/doc/diffusion/Dossier JavaDiKt.pdf

  +SVN repository/Bundled SRC
  |
  |__trunk: this is the folder that contains
  |   the source code stored in a Java  
  |   namespace structure. The main java
  |   packages are:
  |   |
  |   |_net.ajaest.jdk: JavaDiKt ad-hoc src
  |   |  |
  |   |  |__net.ajaest.jdk.data: API for low
  |   |  |   level kanji database management.
  |   |  |__net.ajaest.jdk.core: middle level
  |   |  |   classes between database and GUI
  |   |  |   interface. It contains Launcher,
  |   |  |   JavaDiKt's main class and windows
  |   |  |   handlers. 
  |   |  |__net.ajaest.jdk.gui: contains
  |   |  |   java SWING clases that defines
  |   |  |   JavaDiKt's user interface.
  |   |  |__net.ajaesy.jdk.tools: contains
  |   |     developers tools for JavaDiKt and
  |   |     JUnit test cases. No classes from
  |   |     this class wont ever be built 
  |   |     into user's release version.
  |   | 
  |   |_net.ajaest.lib: contains tools and
  |      non ad-hoc classes that can be 
  |      rereused in other projects.
  |
  |__resources: contains non java src 
  |	  resurces like documentation, images, 
  |	  binaries or libraries
  |		|
  |		|__data: contains files and dicts 
  |		|   needed to build JavaDiKt's kanji
  |		|   kanji database.
  |		|__doc: contains user and developer
  |		|   documentation
  |		|   |_checklists: contains files that
  |		|   |  must be checked before 
  |		|   |  releasing a new version.
  |		|   |_developing: extra resources and
  |		|   |  media for developers
  |		|   |_diffusion: media files(pdf, odt,
  |		|   |  pptx) used to spread JavaDiKt's 
  |		|   |  message all over the world :) 
  |		|   |_javadoc: JavaDiKt's java source 
  |		|   |   code formatted javadoc
  |		|   |   reference.
  |		|   |__man: contains user manuals
  |		|   |__neodatis:database documentation
  |		|  
  |		|__fonts: contains .ttf font files 
  |		|   used in JavaDiKt.
  |		|__icon: contains icon images and raws 
  |		|__jar: files needed to build
  |		|   javadikt.jar   
  |		|__launcher: contains mac, linux and
  |		|   Micro$oft's launchers for 
  |		|   JavaDiKt and the files and src
  |		|   need         
  |		|__library: contains binaries and 
  |		|   src's needed to build JavaDiKt.
  |		|   These are:
  |		|	  |
  |		|	  |__JGoodies: look&feel library
  |		|	  |   licensed under BSD.
  |		|	  |__Neodatis: object-relational
  |		|	  |   database library licensed
  |		|	  |   under GNU LGPL 2.1
  |		|	  |__xom.nu: XML document object
  |		|	  |   modeler database licensed 
  |		|	  |   under GNU LGPL 2.1  
  |		|	
  |		|__utils: contains useful external
  |		    tools that can be used in
  |		    JavaDiKt. 
  |		
  |__LICENSE.txt: GNU GPLv3 license content.
  |__make.sh: script that builds a JavaDiKt
  |   user distribution in the folder 
  |   "javadikt_out".
  |__README.txt: this file.

6.- How to collaborate
  
Either if you are a developer or you want
to translate JavaDiKt to your language you
can contact me through email or JavaDiKt's 
home page contact form. See contact section
for further details.

7.- Contact

You can contact me either by email or by form
in JavaDiKt's homepage:

email: ajaest[at]gmail[dot]com
webpage: javadikt.net/es/index.php?id=contacto

8.- Acknowledgements

To my family and friends, because you were
almost there,

To all the free software developers that made
the libraries used in JavaDiKt for their great
job,

To all the people who collaborated freely with
WWWJDIC project, because they built grain by
grain the best japanese database,

To Dani "The Jisho Man", because you tested 
JavaDiKt so far that your remarks were an 
inspiration for me, 

and finally, to the "47 character rule", for 
making my life simpler :)

