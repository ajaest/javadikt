#    Copyright (C) 2010  Luis Alfonso Arce González, ajaest[@]gmail.com
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http:#www.gnu.org/licenses/>.

#JavaDiKt distribution maker script version 1.4
#WARNING: only works in JavaDiKt's source code folder system


#Removing previous built
echo "**mkdir: Removing previous built"
rm -R out_javadikt

#Making folder structure
echo "**mkdir: making folder structure"
mkdir --verbose out_javadikt
mkdir --verbose out_javadikt/jar			#temp file
mkdir --verbose out_javadikt/jar/images
mkdir --verbose out_javadikt/jar/font
mkdir --verbose out_javadikt/lib
mkdir --verbose out_javadikt/config
mkdir --verbose out_javadikt/dict
mkdir --verbose out_javadikt/font

#Compiling classes
echo "**javac: compiling java classes"
javac -encoding UTF-8 -d out_javadikt/jar -cp src -extdirs resources/library/ajaest-common/:resources/library/jGoodies/:resources/library/xom/:resources/library/neodatis/:resources/library/flyingsaucer-R8/  src/net/ajaest/jdk/core/main/Launcher.java src/net/ajaest/jdk/data/kanji/Radical.java #src/net/ajaest/lib/data/SequenceEnumTree.java
 
#Building .jar
echo "**jar: building jar"
cp --verbose resources/images/splash/splash.gif resources/images/jdownloader/* resources/images/ejemploGrafo.png resources/images/Grafo_asterisco.png resources/images/drawGrid_150.png resources/icon/3.0/*.png out_javadikt/jar/images/
cp --verbose resources/fonts/KanjiStrokeOrders_v2.015.ttf out_javadikt/jar/font/
jar cvfm javadikt.jar resources/jar/META-INF/MANIFEST.MF -C out_javadikt/jar .
mv javadikt.jar out_javadikt/
rm -v -R out_javadikt/jar #removes temp file

#Filling folder structure
echo "**cp: filling folder structure"
cp --verbose resources/fonts/Sazanami-Hanazono-Mincho.ttf out_javadikt/font
cp --verbose resources/library/ajaest-common/ajaest-common-0.2.3.jar out_javadikt/lib
cp --verbose resources/library/jGoodies/jgoodies-looks-2.4.0.jar out_javadikt/lib
cp --verbose resources/library/jGoodies/jgoodies-common-1.1.1.jar out_javadikt/lib
cp --verbose resources/library/neodatis/neodatis-odb-1.9.24.679.jar out_javadikt/lib
cp --verbose resources/library/flyingsaucer-R8/core-renderer.jar out_javadikt/lib
cp --verbose resources/library/flyingsaucer-R8/core-renderer-minimal.jar out_javadikt/lib
cp --verbose resources/library/flyingsaucer-R8/iText-2.0.8.jar out_javadikt/lib
cp --verbose resources/library/flyingsaucer-R8/xml-apis-xerces-2.9.1.jar out_javadikt/lib
cp --verbose resources/library/xom/xom-1.2.6.jar out_javadikt/lib
cp --verbose resources/data/trees/trees.zobj resources/data/dict/*.obj resources/data/dict/*.jdk resources/data/dict/*.txt out_javadikt/dict
cp -R --verbose resources/launcher/JavaDiKt.app out_javadikt/
cp --verbose resources/launcher/javadikt.sh resources/launcher/JavaDiKt.exe out_javadikt/
cp --verbose README.txt LICENSE.txt out_javadikt/
echo "**done" 

 

