//    Copyright (C) 2010  Luis Alfonso Arce González, ajaest[@]gmail.com
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package net.ajaest.jdk.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.ajaest.jdk.data.auxi.KanjiStroke;
import net.ajaest.jdk.data.dict.KanjiDict;
import net.ajaest.jdk.data.dict.query.KanjiQuery;
import net.ajaest.jdk.data.kanji.KanjiGraph;
import net.ajaest.jdk.data.kanji.KanjiTag;
import net.ajaest.lib.swing.util.AllowedStrokeLineEnum;

//TODO: JavaDoc
public class SimilarKanjiStrokeMapBuilder {

    private Integer longestStroke = 0;

    private static Set<AllowedStrokeLineEnum> simil_A;
    private static Set<AllowedStrokeLineEnum> simil_B;
    private static Set<AllowedStrokeLineEnum> simil_C;
    private static Set<AllowedStrokeLineEnum> simil_D;
    private static Set<AllowedStrokeLineEnum> simil_E;
    private static Set<AllowedStrokeLineEnum> simil_F;
    private static Set<AllowedStrokeLineEnum> simil_G;
    private static Set<AllowedStrokeLineEnum> simil_H;
    private static Set<AllowedStrokeLineEnum> simil_I;
    static {
        simil_A = new HashSet<AllowedStrokeLineEnum>();
        simil_A.add(AllowedStrokeLineEnum.D);
        simil_A.add(AllowedStrokeLineEnum.A);
        simil_A.add(AllowedStrokeLineEnum.B);
        simil_B = new HashSet<AllowedStrokeLineEnum>();
        simil_B.add(AllowedStrokeLineEnum.A);
        simil_B.add(AllowedStrokeLineEnum.B);
        simil_B.add(AllowedStrokeLineEnum.C);
        simil_C = new HashSet<AllowedStrokeLineEnum>();
        simil_C.add(AllowedStrokeLineEnum.B);
        simil_C.add(AllowedStrokeLineEnum.C);
        simil_C.add(AllowedStrokeLineEnum.F);
        simil_D = new HashSet<AllowedStrokeLineEnum>();
        simil_D.add(AllowedStrokeLineEnum.A);
        simil_D.add(AllowedStrokeLineEnum.D);
        simil_D.add(AllowedStrokeLineEnum.G);
        simil_E = new HashSet<AllowedStrokeLineEnum>();
        simil_E.add(AllowedStrokeLineEnum.A);
        simil_E.add(AllowedStrokeLineEnum.B);
        simil_E.add(AllowedStrokeLineEnum.C);
        simil_E.add(AllowedStrokeLineEnum.F);
        simil_E.add(AllowedStrokeLineEnum.E);
        simil_E.add(AllowedStrokeLineEnum.F);
        simil_E.add(AllowedStrokeLineEnum.G);
        simil_E.add(AllowedStrokeLineEnum.H);
        simil_E.add(AllowedStrokeLineEnum.I);
        simil_F = new HashSet<AllowedStrokeLineEnum>();
        simil_F.add(AllowedStrokeLineEnum.I);
        simil_F.add(AllowedStrokeLineEnum.F);
        simil_F.add(AllowedStrokeLineEnum.C);
        simil_G = new HashSet<AllowedStrokeLineEnum>();
        simil_G.add(AllowedStrokeLineEnum.D);
        simil_G.add(AllowedStrokeLineEnum.G);
        simil_G.add(AllowedStrokeLineEnum.H);
        simil_H = new HashSet<AllowedStrokeLineEnum>();
        simil_H.add(AllowedStrokeLineEnum.G);
        simil_H.add(AllowedStrokeLineEnum.H);
        simil_H.add(AllowedStrokeLineEnum.I);
        simil_I = new HashSet<AllowedStrokeLineEnum>();
        simil_I.add(AllowedStrokeLineEnum.F);
        simil_I.add(AllowedStrokeLineEnum.I);
        simil_I.add(AllowedStrokeLineEnum.F);
    }

    public static void main(String... strings) throws IOException {

        // TODO: minimize file sizes
        String kanjPath = "resources/data/dict/kanjidict.jdk";
        String treeData = "resources/data/trees/trees.zobj";

        KanjiDict data = new KanjiDict(kanjPath, treeData);

        List<KanjiTag> kts = data.executeQuery(new KanjiQuery().unicode_value().greatherThan(0));

        SimilarKanjiStrokeMapBuilder skgd = new SimilarKanjiStrokeMapBuilder();

        // create graph set
        Set<KanjiGraph> allowedGraphs = skgd.createPossibleGraphSet(kts);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("resources/data/dict/graphset.obj"));
        oos.writeObject(allowedGraphs);
        oos.flush();
        oos.close();

        // create sub graph set
        Set<List<KanjiStroke>> allowedSubGraphs = skgd.createPossibleSubGraphSet(kts);

        oos = new ObjectOutputStream(new FileOutputStream("resources/data/dict/subgraphset.obj"));
        oos.writeObject(allowedSubGraphs);
        oos.flush();
        oos.close();

        // create similar stroke map
        Set<KanjiStroke> allowedStrokes = new HashSet<KanjiStroke>();

        for (KanjiTag kt : kts) {
            if (kt.getGraph() != null) {
                for (KanjiStroke ks : kt.getGraph().getStrokes()) {
                    allowedStrokes.add(ks);
                }
            }
        }

        Map<KanjiStroke, Set<KanjiStroke>> similarityMap = skgd.createSimilarityMap(allowedStrokes);

        oos = new ObjectOutputStream(new FileOutputStream("resources/data/dict/strokemap.obj"));
        oos.writeObject(similarityMap);
        oos.flush();
        oos.close();

        data.close();

    }

    private Set<KanjiStroke> addSpecialCases(Set<KanjiStroke> lines) {

        Set<KanjiStroke> hooks = new HashSet<KanjiStroke>();

        hooks.addAll(this.demuxReciprocal("B", "Bf", lines));
        hooks.addAll(this.demuxReciprocal("b", "bf", lines));

        hooks.addAll(this.demuxReciprocal("B", "Bd", lines));
        hooks.addAll(this.demuxReciprocal("b", "bd", lines));

        hooks.addAll(this.demuxReciprocal("B", "Bg", lines));
        hooks.addAll(this.demuxReciprocal("b", "bg", lines));

        hooks.addAll(this.demuxReciprocal("B", "Bi", lines));
        hooks.addAll(this.demuxReciprocal("b", "bi", lines));

        hooks.addAll(this.demuxReciprocal("F", "Fb", lines));
        hooks.addAll(this.demuxReciprocal("f", "fb", lines));

        hooks.addAll(this.demuxReciprocal("F", "Fh", lines));
        hooks.addAll(this.demuxReciprocal("f", "fh", lines));

        hooks.addAll(this.demuxReciprocal("F", "Fg", lines));
        hooks.addAll(this.demuxReciprocal("f", "fg", lines));

        hooks.addAll(this.demuxReciprocal("F", "Fi", lines));
        hooks.addAll(this.demuxReciprocal("f", "fi", lines));

        lines.addAll(hooks);

        Set<KanjiStroke> slopes = new HashSet<KanjiStroke>();

        //TODO: add line continuations: Aa == a, bb == b, etc...
        slopes.addAll(this.demuxReciprocal("B", "Ba", lines));
        slopes.addAll(this.demuxReciprocal("b", "ba", lines));

        slopes.addAll(this.demuxReciprocal("B", "Bc", lines));
        slopes.addAll(this.demuxReciprocal("b", "bc", lines));

        slopes.addAll(this.demuxReciprocal("A", "Ab", lines));
        slopes.addAll(this.demuxReciprocal("a", "ab", lines));

        slopes.addAll(this.demuxReciprocal("C", "Cb", lines));
        slopes.addAll(this.demuxReciprocal("c", "cb", lines));

        slopes.addAll(this.demuxReciprocal("F", "Fc", lines));
        slopes.addAll(this.demuxReciprocal("f", "fc", lines));

        slopes.addAll(this.demuxReciprocal("C", "Cf", lines));
        slopes.addAll(this.demuxReciprocal("c", "cf", lines));

        slopes.addAll(this.demuxReciprocal("F", "Fa", lines));
        slopes.addAll(this.demuxReciprocal("f", "fa", lines));

        slopes.addAll(this.demuxReciprocal("A", "Af", lines));
        slopes.addAll(this.demuxReciprocal("a", "af", lines));

        lines.addAll(slopes);

        return lines;
    }

    private Set<KanjiStroke> buildAllStrokes() {

        Set<List<AllowedStrokeLineEnum>> posibleStrokes = new HashSet<List<AllowedStrokeLineEnum>>();
        List<AllowedStrokeLineEnum> expandedStroke;

        expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        expandedStroke.add(AllowedStrokeLineEnum.A);
        posibleStrokes.add(expandedStroke);

        expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        expandedStroke.add(AllowedStrokeLineEnum.B);
        posibleStrokes.add(expandedStroke);

        expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        expandedStroke.add(AllowedStrokeLineEnum.C);
        posibleStrokes.add(expandedStroke);

        expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        expandedStroke.add(AllowedStrokeLineEnum.D);
        posibleStrokes.add(expandedStroke);

        // expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        // expandedStroke.add(AllowedStrokeLineEnum.E);
        // posibleStrokes.add(expandedStroke);

        expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        expandedStroke.add(AllowedStrokeLineEnum.F);
        posibleStrokes.add(expandedStroke);

        expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        expandedStroke.add(AllowedStrokeLineEnum.G);
        posibleStrokes.add(expandedStroke);

        expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        expandedStroke.add(AllowedStrokeLineEnum.H);
        posibleStrokes.add(expandedStroke);

        expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
        expandedStroke.add(AllowedStrokeLineEnum.I);
        posibleStrokes.add(expandedStroke);

        Set<List<AllowedStrokeLineEnum>> toAdd = new HashSet<List<AllowedStrokeLineEnum>>();
        for (int i = 1; i < this.longestStroke; i++) {
            for (List<AllowedStrokeLineEnum> current : posibleStrokes) {
                expandedStroke = new ArrayList<AllowedStrokeLineEnum>(current);
                expandedStroke.add(AllowedStrokeLineEnum.A);
                toAdd.add(expandedStroke);

                expandedStroke = new ArrayList<AllowedStrokeLineEnum>(current);
                expandedStroke.add(AllowedStrokeLineEnum.B);
                toAdd.add(expandedStroke);

                expandedStroke = new ArrayList<AllowedStrokeLineEnum>(current);
                expandedStroke.add(AllowedStrokeLineEnum.C);
                toAdd.add(expandedStroke);

                expandedStroke = new ArrayList<AllowedStrokeLineEnum>(current);
                expandedStroke.add(AllowedStrokeLineEnum.D);
                toAdd.add(expandedStroke);

                // expandedStroke = new
                // ArrayList<AllowedStrokeLineEnum>(current);
                // expandedStroke.add(AllowedStrokeLineEnum.E);
                // toAdd.add(expandedStroke);

                expandedStroke = new ArrayList<AllowedStrokeLineEnum>(current);
                expandedStroke.add(AllowedStrokeLineEnum.F);
                toAdd.add(expandedStroke);

                expandedStroke = new ArrayList<AllowedStrokeLineEnum>(current);
                expandedStroke.add(AllowedStrokeLineEnum.G);
                toAdd.add(expandedStroke);

                expandedStroke = new ArrayList<AllowedStrokeLineEnum>(current);
                expandedStroke.add(AllowedStrokeLineEnum.H);
                toAdd.add(expandedStroke);

                expandedStroke = new ArrayList<AllowedStrokeLineEnum>(current);
                expandedStroke.add(AllowedStrokeLineEnum.I);
                toAdd.add(expandedStroke);
            }
            posibleStrokes.addAll(toAdd);
        }

        return this.translateToKanjiStroke(posibleStrokes, new KanjiStroke((List<AllowedStrokeLineEnum>) null, -1, -1));
    }

    public Set<KanjiGraph> createPossibleGraphSet(Collection<KanjiTag> kts) {

        Set<KanjiGraph> kg = new TreeSet<KanjiGraph>();

        for (KanjiTag kt : kts) {
            if (kt.getGraph() != null) {
                kg.add(new KanjiGraph(kt.getGraph().getStrokes(), null, -1));
            }
        }

        return kg;

    }

    public Set<List<KanjiStroke>> createPossibleSubGraphSet(Collection<KanjiTag> kts) {

        Set<List<KanjiStroke>> kgs = new HashSet<List<KanjiStroke>>();

        int semiStrokeIndex = 1;
        List<KanjiStroke> semiStroke;
        for (KanjiTag kt : kts) {
            if (kt.getGraph() != null) {
                for (int i = 0; i < kt.getGraph().strokeCount(); i++) {
                    semiStroke = kt.getGraph().getStrokes().subList(0, semiStrokeIndex);
                    kgs.add(semiStroke);
                    semiStrokeIndex++;
                }
                semiStrokeIndex = 1;
            }
        }

        // removes kanji info
        KanjiStroke tempKS;
        Set<List<KanjiStroke>> newKgs = new HashSet<List<KanjiStroke>>();
        // reduces file size
        Map<KanjiStroke, KanjiStroke> objectRecicler = new HashMap<KanjiStroke, KanjiStroke>();
        for (List<KanjiStroke> lkst : kgs) {
            semiStroke = new ArrayList<KanjiStroke>();
            for (KanjiStroke ks : lkst) {
                tempKS = objectRecicler.get(ks);
                if (tempKS == null) {
                    tempKS = new KanjiStroke(ks.getStrokeList(), -1, -1);
                    objectRecicler.put(ks, tempKS);
                }
                semiStroke.add(tempKS);
            }
            newKgs.add(semiStroke);
        }

        return newKgs;
    }



    public Map<KanjiStroke, Set<KanjiStroke>> createSimilarityMap(Collection<KanjiStroke> allowedStrokes) {

        for (KanjiStroke ks : allowedStrokes) {
            if (ks.getStrokeList().size() > this.longestStroke)
                this.longestStroke = ks.getStrokeList().size();
        }

        Map<KanjiStroke, Set<KanjiStroke>> builtSimilarityMap = new HashMap<KanjiStroke, Set<KanjiStroke>>();
        Set<KanjiStroke> posibleStrokes = this.buildAllStrokes();

        Set<KanjiStroke> similarStrokes;
        int i = 1;
        for (KanjiStroke ks : posibleStrokes) {
            similarStrokes = this.demuxStroke(ks);
            Set<KanjiStroke> current;
            for (KanjiStroke demuxed : similarStrokes) {
                current = builtSimilarityMap.get(demuxed);
                if (current == null) {
                    current = new HashSet<KanjiStroke>();
                    builtSimilarityMap.put(demuxed, current);
                }
                current.addAll(similarStrokes);
                current.retainAll(allowedStrokes);
            }

            System.out.println("1**" + (int) (100 * ((float) i / ((float) posibleStrokes.size()))) + "/100");
            i++;
        }

        Map<KanjiStroke, Set<KanjiStroke>> newbuiltSimilarityMap = new HashMap<KanjiStroke, Set<KanjiStroke>>();
        Set<KanjiStroke> newPosibleStrokes;

        // Adds special cases

        i = 1;
        for (KanjiStroke ks : builtSimilarityMap.keySet()) {
            newPosibleStrokes = builtSimilarityMap.get(ks);
            newPosibleStrokes.addAll(this.addSpecialCases(newPosibleStrokes));
            newbuiltSimilarityMap.put(ks, newPosibleStrokes);

            System.out.println("2**" + (int) (100 * ((float) i / ((float) builtSimilarityMap.keySet().size()))) + "/100");
            i++;
        }

        // Rebuilds map

        i = 1;
        builtSimilarityMap = new HashMap<KanjiStroke, Set<KanjiStroke>>();

        for (KanjiStroke ks1 : newbuiltSimilarityMap.keySet()) {
            posibleStrokes = newbuiltSimilarityMap.get(ks1);
            newPosibleStrokes = builtSimilarityMap.get(ks1);
            if (newPosibleStrokes == null) {
                builtSimilarityMap.put(ks1, new HashSet<KanjiStroke>());
                newPosibleStrokes = builtSimilarityMap.get(ks1);
            }
            newPosibleStrokes.addAll(posibleStrokes);
            for (KanjiStroke ks2 : posibleStrokes) {
                newPosibleStrokes = builtSimilarityMap.get(ks2);
                if (newPosibleStrokes == null) {
                    builtSimilarityMap.put(ks2, new HashSet<KanjiStroke>());
                    newPosibleStrokes = builtSimilarityMap.get(ks2);
                }
                newPosibleStrokes.addAll(posibleStrokes);
            }

            System.out.println("3**" + (int) (100 * ((float) i / ((float) newbuiltSimilarityMap.keySet().size()))) + "/100");
            i++;
        }

        // Removes not allowed strokes

        i = 1;

        for (KanjiStroke ks1 : builtSimilarityMap.keySet()) {
            posibleStrokes = builtSimilarityMap.get(ks1);

            posibleStrokes.retainAll(allowedStrokes);
            System.out.println("4**" + (int) (100 * ((float) i / ((float) builtSimilarityMap.keySet().size()))) + "/100");
            i++;
        }

        return builtSimilarityMap;
    }

    private Set<AllowedStrokeLineEnum> demuxLine(AllowedStrokeLineEnum asl) {

        Set<AllowedStrokeLineEnum> demuxed = null;

        switch (asl) {
        case A :
            demuxed = simil_A;
            break;
        case B:
            demuxed = simil_B;
            break;
        case C:
            demuxed = simil_C;
            break;
        case D :
            demuxed = simil_D;
            break;
        case E :
            demuxed = simil_E;
            break;
        case F :
            demuxed = simil_F;
            break;
        case G :
            demuxed = simil_G;
            break;
        case H :
            demuxed = simil_H;
            break;
        case I :
            demuxed = simil_I;
            break;
        default :
            break;
        }

        return demuxed;
    }

    private Set<KanjiStroke> demuxReciprocal(String ss1, String ss2, Set<KanjiStroke> lines) {

        Set<KanjiStroke> expanded = new HashSet<KanjiStroke>();

        String stroke;
        String[] splitted;
        Set<List<String>> newStrokes;
        List<String> semiStrokes;
        for (KanjiStroke ks : lines) {
            stroke = ks.toString();

            newStrokes = new HashSet<List<String>>();

            splitted = this.splitWithDelimiters(stroke, ss1);

            semiStrokes = new ArrayList<String>();
            semiStrokes.add(splitted[0]);
            newStrokes.add(semiStrokes);

            semiStrokes = new ArrayList<String>();
            semiStrokes.add(splitted[0].replace(ss1, ss2));
            newStrokes.add(semiStrokes);

            String s;
            Set<List<String>> addToNew = new HashSet<List<String>>();
            for (int i = 1; i < splitted.length; i++) {
                s = splitted[i];
                for (List<String> sss : newStrokes) {
                    semiStrokes = new ArrayList<String>(sss);
                    semiStrokes.add(s);
                    addToNew.add(semiStrokes);

                    semiStrokes = new ArrayList<String>(sss);
                    semiStrokes.add(s.replace(ss1, ss2));
                    addToNew.add(semiStrokes);
                }
            }

            newStrokes = addToNew;

            for (List<String> str : newStrokes) {
                stroke = "";
                for (String s1 : str) {
                    stroke += s1;
                }
                expanded.add(new KanjiStroke(stroke, -1, -1));
            }

            newStrokes = new HashSet<List<String>>();
            splitted = this.splitWithDelimiters(stroke, ss2);

            semiStrokes = new ArrayList<String>();
            semiStrokes.add(splitted[0]);
            newStrokes.add(semiStrokes);

            semiStrokes = new ArrayList<String>();
            semiStrokes.add(splitted[0].replace(ss2, ss1));
            newStrokes.add(semiStrokes);

            addToNew = new HashSet<List<String>>();
            for (int i = 1; i < splitted.length; i++) {
                s = splitted[i];
                for (List<String> sss : newStrokes) {
                    semiStrokes = new ArrayList<String>(sss);
                    semiStrokes.add(s);
                    addToNew.add(semiStrokes);

                    semiStrokes = new ArrayList<String>(sss);
                    semiStrokes.add(s.replace(ss2, ss1));
                    addToNew.add(semiStrokes);
                }
            }

            newStrokes = addToNew;

            for (List<String> str : newStrokes) {
                stroke = "";
                for (String s1 : str) {
                    stroke += s1;
                }
                expanded.add(new KanjiStroke(stroke, -1, -1));
            }

        }

        return expanded;
    }

    private Set<KanjiStroke> demuxStroke(KanjiStroke ks) {

        Set<KanjiStroke> demuxed;

        Set<List<AllowedStrokeLineEnum>> lines = null;
        Set<AllowedStrokeLineEnum> demuxedLine;
        for (AllowedStrokeLineEnum asl : ks.getStrokeList()) {
            demuxedLine = this.demuxLine(asl);
            lines = this.expandLines(lines, demuxedLine);
        }

        demuxed = this.translateToKanjiStroke(lines, ks);

        return demuxed;
    }

    private Set<List<AllowedStrokeLineEnum>> expandLines(Set<List<AllowedStrokeLineEnum>> lines, Set<AllowedStrokeLineEnum> demuxedLine) {

        Set<List<AllowedStrokeLineEnum>> expanded = new HashSet<List<AllowedStrokeLineEnum>>();

        if (lines == null) {
            List<AllowedStrokeLineEnum> expandedStroke;
            for (AllowedStrokeLineEnum asl : demuxedLine) {
                expandedStroke = new ArrayList<AllowedStrokeLineEnum>();
                expandedStroke.add(asl);
                expanded.add(new ArrayList<AllowedStrokeLineEnum>());
            }
        } else {
            List<AllowedStrokeLineEnum> expandedStroke;
            for (AllowedStrokeLineEnum asl : demuxedLine) {
                for (List<AllowedStrokeLineEnum> stroke : lines) {
                    expandedStroke = new ArrayList<AllowedStrokeLineEnum>(stroke);
                    expandedStroke.add(asl);
                    expanded.add(expandedStroke);
                }
            }
        }

        return expanded;
    }

    private String[] splitWithDelimiters(String s, String delimiter) {

        List<String> spplited = new ArrayList<String>();
        spplited.add(s);

        String previousStr;
        int index = spplited.get(0).indexOf(delimiter);
        while (index != -1) {
            previousStr = spplited.get(spplited.size() - 1);
            spplited.set(spplited.size() - 1, previousStr.substring(0, index + delimiter.length()));
            spplited.add(previousStr.substring(index + delimiter.length()));
            index = spplited.get(spplited.size() - 1).indexOf(delimiter);
        }

        String[] splittedArray = new String[spplited.size()];
        spplited.toArray(splittedArray);
        return splittedArray;
    }


    private Set<KanjiStroke> translateToKanjiStroke(Set<List<AllowedStrokeLineEnum>> lines, KanjiStroke ks) {

        Set<KanjiStroke> translated = new HashSet<KanjiStroke>();

        for (List<AllowedStrokeLineEnum> asls : lines) {
            translated.add(new KanjiStroke(asls, -1, -1));
        }

        return translated;
    }

}
