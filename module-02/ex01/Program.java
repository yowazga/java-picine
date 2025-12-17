/* ************************************************************************** */
/*                                                                            */
/*                                                        :::      ::::::::   */
/*   Program.java                                       :+:      :+:    :+:   */
/*                                                    +:+ +:+         +:+     */
/*   By: yowazga <yowazga@student.42.fr>            +#+  +:+       +#+        */
/*                                                +#+#+#+#+#+   +#+           */
/*   Created: 2024/10/14 16:48:21 by yowazga           #+#    #+#             */
/*   Updated: 2025/12/16 10:39:38 by yowazga          ###   ########.fr       */
/*                                                                            */
/* ************************************************************************** */

import java.nio.file.*;
import java.util.*;
import java.io.*;


public class Program {

	private static List<String> getWordsInFile(String filePath) {

		List<String> words = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			while (scanner.hasNext()) {
				String word = scanner.next();
				words.add(word);
			}
			scanner.close();
		}
		catch (Exception e) {
			System.err.println("Cannot reading file: " + filePath);
			System.exit(-1);
		}
		return words;
	}

	private static Set<String> creatDictionary(List<String> words1, List<String> words2) {

		Set<String> dictionary = new HashSet<>();

		dictionary.addAll(words1);
		dictionary.addAll(words2);

		return dictionary;
	}

	private static Map<String, Long> getFreqMap(List<String> words) {
		
		Map<String, Long> freqMap = new HashMap<>();

		for (String word : words) {
			String lowerWord = word.toLowerCase();
			freqMap.put(lowerWord, freqMap.getOrDefault(lowerWord, 0L) + 1);
		}
		
		return freqMap;
	}

	private static List<Long> creatVector(Set<String> dictionary, Map<String, Long> freqMap) {
	
		List<Long> vector = new ArrayList<>(dictionary.size());

		for (String word : dictionary) {
			vector.add(freqMap.getOrDefault(word, 0L));
		}
		return vector;
	}

	private static double calculSimilarity(List<Long> list1, List<Long> list2) {
		
		double numerator = 0.0, denominatorA = 0.0, denominatorB = 0.0;

    for (int i = 0; i < list1.size(); i++) {
        numerator += list1.get(i) * list2.get(i);
        denominatorA += list1.get(i) * list1.get(i);
        denominatorB += list2.get(i) * list2.get(i);
    }

    double denominator = Math.sqrt(denominatorA) * Math.sqrt(denominatorB);

    return (denominator != 0.0) ? numerator / denominator : numerator;
	}

	private static void writeDictionary(Set<String> dictionary, String file) {
		
		try {
			Files.write(Paths.get(file), dictionary);
		}
		catch (Exception e) {
			System.err.println("Can't write to file: " + file);
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.out.println("Invalid argument: Program file1 file2.");
			return ;
		}
		
		List<String> words1 = getWordsInFile(args[0]);
		List<String> words2 = getWordsInFile(args[1]);

		Set<String> dictionary = creatDictionary(words1, words2);

		Map<String, Long> freq1 = getFreqMap(words1);
		Map<String, Long> freq2 = getFreqMap(words2);

		List<Long> list1 = creatVector(dictionary, freq1);
		List<Long> list2 = creatVector(dictionary, freq2);

		double similarity = calculSimilarity(list1, list2);

		writeDictionary(dictionary, "dictionary.txt");

		System.out.printf("Cosine similarity: %.3f\n", similarity);

	}
}