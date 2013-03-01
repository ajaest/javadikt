
echo "**mkdir: Removing previous built"
rmdir /S /Q out_javadikt

echo "**mkdir: making folder structure"
mkdir  out_javadikt
mkdir  out_javadikt\jar		
mkdir  out_javadikt\jar\images
mkdir  out_javadikt\jar\font
mkdir  out_javadikt\lib
mkdir  out_javadikt\config
mkdir  out_javadikt\dict
mkdir  out_javadikt\font

echo "**javac: compiling java classes"
javac -encoding UTF-8 -d out_javadikt/jar -cp src -extdirs resources/library/ajaest-common/;resources/library/jGoodies/;resources/library/xom/;resources/library/neodatis/;resources/library/flyingsaucer-R8/  src/net/ajaest/jdk/core/main/Launcher.java src/net/ajaest/jdk/data/kanji/Radical.java 
 
echo "**jar: building jar"
xcopy  resources\images\splash\splash.gif  out_javadikt\jar\images\
xcopy resources\images\jdownloader\*  out_javadikt\jar\images\
xcopy resources\images\ejemploGrafo.png  out_javadikt\jar\images\
xcopy resources\images\Grafo_asterisco.png  out_javadikt\jar\images\
xcopy resources\images\drawGrid_150.png  out_javadikt\jar\images\
xcopy resources\icon\3.0\*.png out_javadikt\jar\images\
xcopy  resources\fonts\KanjiStrokeOrders_v2.015.ttf out_javadikt\jar\font\
jar cvfm javadikt.jar resources\jar\META-INF\MANIFEST.MF -C out_javadikt\jar .
move javadikt.jar out_javadikt\
rmdir /s /Q out_javadikt\jar 

echo "**xcopy: filling folder structure"

xcopy  resources\fonts\Sazanami-Hanazono-Mincho.ttf out_javadikt\font

xcopy  resources\library\ajaest-common\ajaest-common-0.2.1.jar out_javadikt\lib
xcopy  resources\library\jGoodies\jgoodies-looks-2.4.0.jar out_javadikt\lib
xcopy  resources\library\jGoodies\jgoodies-common-1.1.1.jar out_javadikt\lib
xcopy  resources\library\neodatis\neodatis-odb-1.9.24.679.jar out_javadikt\lib
xcopy  resources\library\flyingsaucer-R8\core-renderer.jar out_javadikt\lib
xcopy  resources\library\flyingsaucer-R8\core-renderer-minimal.jar out_javadikt\lib
xcopy  resources\library\flyingsaucer-R8\iText-2.0.8.jar out_javadikt\lib
xcopy  resources\library\flyingsaucer-R8\xml-apis-xerces-2.9.1.jar out_javadikt\lib
xcopy  resources\library\xom\xom-1.2.6.jar out_javadikt\lib

xcopy  resources\data\trees\trees.zobj out_javadikt\dict
xcopy  resources\data\dict\*.obj out_javadikt\dict
xcopy  resources\data\dict\*.jdk out_javadikt\dict 
xcopy  resources\data\dict\*.txt out_javadikt\dict
xcopy  resources\launcher\JavaDiKt.app out_javadikt\
xcopy resources\launcher\javadikt.sh out_javadikt\
xcopy resources\launcher\JavaDiKt.exe out_javadikt\
xcopy  README.txt out_javadikt\
xcopy LICENSE.txt out_javadikt\
echo "**done" 

 

