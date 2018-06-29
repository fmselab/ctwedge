/******************************************************************************* 
 * Copyright (c) 2013 University of Bergamo - Italy 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Paolo Vavassori - initial API and implementation 
 *   Angelo Gargantini - utils and architecture 
 ******************************************************************************/ 
package ctwedge.util.filechecker; 
 
import java.io.BufferedReader; 
import java.io.FileNotFoundException; 
import java.io.FileReader; 
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.Iterator; 
import java.util.List; 
 
import ctwedge.util.Pair; 
import ctwedge.util.ModelUtils; 
 
 
/** 
 * main file to build a jar file capable of checking if a given file containing 
 * a test suite is adequate. Format: t,k,v tests 
 *  
 * having k factors each with v levels, with coverage at strength t. tests as 
 * integers from 0 to v-1, separated by comma 
 *  
 *  
 * @author garganti 
 *  
 */ 
public class FileChecker { 
 
  /** 
   * @param args 
   * @throws IOException 
   * @throws FileNotFoundException 
   */ 
  public static void main(String[] args) throws IOException { 
    if (args.length <= 0) { 
      System.out.println("usage : java -jar combchecker.jar filename"); 
      System.exit(-1); 
    } 
    FileReader fileToCheck = null; 
    try { 
      fileToCheck = new FileReader(args[0]); 
    } catch (FileNotFoundException e) { 
      System.out.println("file " + args[0] + " not found"); 
      System.exit(-1); 
    } 
    BufferedReader reader = new BufferedReader(fileToCheck); 
    String[] problem = reader.readLine().split(","); 
    check(problem.length == 3, 
        " first line must have t,k,v - k factors each with v levels, with coverage at strength t."); 
    // having k factors each with v levels, with coverage at strength t. 
    int t = Integer.parseInt(problem[0]); 
    int k = Integer.parseInt(problem[1]); 
    int v = Integer.parseInt(problem[2]); 
    System.out.println("checking problem with " + k + " factors each with " 
        + v + " levels, with coverage at strength " + t); 
    // check now the lines 
    List<int[]> testSuite = new ArrayList<int[]>(); 
    String test; 
    while ((test = reader.readLine()) != null) { 
      int[] testfrom = getTestfrom(test); 
      check(testfrom.length == k, "every test must have k= " + k 
          + " elements"); 
      testSuite.add(testfrom); 
    } 
    reader.close(); 
    fileToCheck.close(); 
    checkTestSuite(t, k, v, testSuite); 
  } 
 
  private static int[] getTestfrom(String testline) { 
    String[] arrayvalues = testline.split(","); 
    int[] test = new int[arrayvalues.length]; 
    for (int i = 0; i < arrayvalues.length; i++) { 
      test[i] = Integer.parseInt(arrayvalues[i]); 
    } 
    return test; 
  } 
 
  private static void check(boolean b, String string) { 
    if (!b) { 
      System.out.println(string); 
      System.exit(-1); 
    } 
  } 
 
  /** 
   * Check file.k factors each with v levels, with coverage at strength t. 
   *  
   * @param t 
   *            the t strength 
   * @param k 
   *            the k factors 
   * @param v 
   *            the v levels 
   * @param testSuite 
   *            the test suite 
   */ 
  static void checkTestSuite(int t, int k, int v, List<int[]> testSuite) { 
    // create test requirements 
    List<List<Pair<Integer, Integer>>> problem = new ArrayList<List<Pair<Integer, Integer>>>(); 
    // for every factor 
    for (int i = 0; i < k; i++) { 
      List<Pair<Integer, Integer>> vi = new ArrayList<Pair<Integer, Integer>>(); 
      for (int j = 0; j < v; j++) { 
        vi.add(new Pair<Integer, Integer>(i, j)); 
      } 
      problem.add(vi); 
    } 
    Iterator<List<Pair<Integer, Integer>>> requirements = ModelUtils 
        .getAllKWiseCombination(problem, t); 
    while (requirements.hasNext()) { 
      List<Pair<Integer, Integer>> req = requirements.next(); 
      // exists test that covers all the pairs in req 
      // check that req is covered 
      boolean covered = false; 
      for (int[] test : testSuite) { 
        assert test.length == k; 
        if (covers(test, req)) { 
          covered = true; 
          break; 
        } 
      } 
      if (!covered) { 
        System.err.println(req + " not covered"); 
      } else { 
        System.out.println(req + " covered"); 
 
      } 
    } 
  } 
 
  // check a test covers all the req in req 
  private static boolean covers(int[] test, List<Pair<Integer, Integer>> req) { 
    for (Pair<Integer, Integer> req_i : req) { 
      if (test[req_i.getFirst()] != req_i.getSecond()) 
        return false; 
    } 
    return true; 
  } 
} 