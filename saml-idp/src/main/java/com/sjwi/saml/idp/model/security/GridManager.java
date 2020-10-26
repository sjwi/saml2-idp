package com.sjwi.saml.idp.model.security;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GridManager extends AuthenticationManager {
	
	private static final int GRID_WIDTH = 4;
	private static final int GRID_HEIGHT = 4;

	public static final String DEMO_USER_GRID_MAP_COOKIE = "DEMO_USER_GRID_MAP";
	private String[] grid;

	public GridManager(String encryptedAttribute, String userName, boolean encrypted) throws RuntimeException {
		super(encryptedAttribute, userName, encrypted);
	}

	@Override
	public boolean isValidAuthenticationAttempt(String attempt) {
		String[] attemptArray = attempt.split(",");
		String[] answer = getAnswer(getDecryptedUserAttribute().split(","), grid);
		return Arrays.equals(answer, attemptArray);
	}
	
	public static List<List<String>> getEnrollmentGrid() {
		List<List<String>> grid = new ArrayList<List<String>>();
		for (int i = 0; i < GRID_HEIGHT; i++) {
			List<String> row = new ArrayList<String>();
			for (int j = 0; j< GRID_WIDTH; j++) {
				row.add(Integer.toString((GRID_HEIGHT * i)+j));
			}
			grid.add(row);
		}
		return grid;
	}

	public static List<List<String>> getHashedGrid() throws RuntimeException {

		List<List<String>> grid = new ArrayList<List<String>>();
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < GRID_HEIGHT; i++) {
			List<String> row = new ArrayList<String>();
			for (int j = 0; j< GRID_WIDTH; j++) {
				String tempIdx = generateRandomHexIndex();
				while(values.contains(tempIdx)) {
					tempIdx = generateRandomHexIndex();
				}
				row.add(tempIdx);
				values.add(tempIdx);
			}
			grid.add(row);
		}
		return grid;
	}

	private String[] getAnswer(String[] decryptedKey, String[] grid) {
		String[] answer = new String[decryptedKey.length];
		for (int i = 0; i < decryptedKey.length; i++) {
			answer[i] = grid[Integer.parseInt(decryptedKey[i])];
		}
		return answer;
	}

	private static String generateRandomHexIndex() throws RuntimeException {
		int[] arr = new int[2];
		Random rand = new SecureRandom();
		String key = "";

		for (int i = 0; i < arr.length; i++) {

			arr[i] = rand.nextInt(16);
			key = key + Integer.toHexString(arr[i]);
		}

		return key;	
	}

	public static String gridToString(List<List<String>> grid) {
		String hexString = "";
		for (List<String> row: grid) {
			for (String idx: row) {
				hexString += idx + ",";
			}
		}
		return hexString.substring(0,hexString.length()-1);
	}

	public void setGrid(String[] grid) {
		this.grid = grid;
	}




}
