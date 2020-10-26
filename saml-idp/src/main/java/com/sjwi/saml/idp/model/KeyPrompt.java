package com.sjwi.saml.idp.model;


import java.security.SecureRandom;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class KeyPrompt {
	
	private List<Integer> prompt;

	public KeyPrompt() {
		
		Random rand = new SecureRandom();
		int size = rand.nextInt(2) + 4;
		Set<Integer> prompt = new HashSet<Integer>();
		while (prompt.size() < size) {
			prompt.add(rand.nextInt(16)+1);
		}
		List<Integer> list = new LinkedList<>();
		list.addAll(prompt);
		this.prompt = list;
	}
	
	public KeyPrompt(String hexString) {
		
		this.prompt = hexString.toLowerCase().chars()
				.mapToObj(p -> Integer.parseInt(String.valueOf((char) p),16) + 1)
				.map(i -> Integer.valueOf(i))
				.collect(Collectors.toList());
	}

	public List<Integer> getPrompt() {
		return prompt;
	}

	@Override
	public String toString() {
		return  String.join("", prompt.stream()
				.map(p -> Integer.toHexString(p - 1))
				.collect(Collectors.toList()));
	}
}
