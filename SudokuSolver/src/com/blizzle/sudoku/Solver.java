package com.blizzle.sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solver {
	private BoardManager display;
	private char[][] board;
	
	public Solver(BoardManager display) {
		this.display = display;
		this.board = new char[9][9];
	}
	
	public void setStart(String[] compressed) {
		for (int i = 0; i < 9; i++) {
			String colString = compressed[i];
			for (int j = 0; j < colString.length(); j++) {
				this.board[i][j] = colString.charAt(j);
				if (colString.charAt(j) != '.') {
					this.display.setValue(i, j, Character.getNumericValue(colString.charAt(j)), true);
				}
			}
		}
	}
	
	public void solve() {
		solveSudoku(this.board, this.display);
	}

	public static void prettyPrint(char[][] board) {
		String[] rowString = new String[9];
		for (int i = 0; i < 9; i++) {
			rowString[i] = "";
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				rowString[j] = rowString[j] + board[i][j] + " ";
			}
		}

		for (int i = 0; i < 9; i++) {
			System.out.println(rowString[i]);
		}
	}

	public static void solveSudoku(char[][] board, BoardManager display) {
		Map<Integer, Map<Integer, Integer>> floors = new HashMap<>();

		for (int i = 0; i < 9; i++) {
			Map<Integer, Integer> yCoordMap = new HashMap<>();
			for (int j = 0; j < 9; j++) {
				yCoordMap.put(j, 1);
			}
			floors.put(i, yCoordMap);
		}

		prettyPrint(board);
		char[][] answer = solveSudokuHelper(board, floors, display);
		System.out.println("DONE");
		prettyPrint(answer);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j] = answer[i][j];
			}
		}
	}

	public static char[][] solveSudokuHelper(char[][] board, Map<Integer, Map<Integer, Integer>> floors, BoardManager display) {
		int[] firstEmptyCoords = firstEmpty(board);

		if (firstEmptyCoords[0] == -1 && firstEmptyCoords[1] == -1) {
			return board;
		}

		char[][] result = null;
		while (result == null) {
			//                      prettyPrint(board);
			char nextAvailableForFirstEmpty = nextAvailableNum(board, firstEmptyCoords[0], firstEmptyCoords[1], floors.get(firstEmptyCoords[0]).get(firstEmptyCoords[1]));
			if (nextAvailableForFirstEmpty == '.') {
				return null;
			}

			floors.get(firstEmptyCoords[0]).put(firstEmptyCoords[1], Character.getNumericValue(nextAvailableForFirstEmpty)+ 1);

			char[][] copy = copyBoard(board);
			copy[firstEmptyCoords[0]][firstEmptyCoords[1]] = nextAvailableForFirstEmpty;
			display.setValue(firstEmptyCoords[0], firstEmptyCoords[1], Character.getNumericValue(nextAvailableForFirstEmpty), false);
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//			}
			result = solveSudokuHelper(copy, copyFloors(floors), display);
		}

		return result;
	}

	public static Map<Integer, Map<Integer, Integer>> copyFloors(Map<Integer, Map<Integer, Integer>> floors) {
		Map<Integer, Map<Integer, Integer>> copy = new HashMap<>();
		for (Integer xCoord : floors.keySet()) {
			Map<Integer, Integer> inner = floors.get(xCoord);
			Map<Integer, Integer> innerCopy = new HashMap<>();
			for (Integer yCoord : inner.keySet()) {
				innerCopy.put(new Integer(yCoord), new Integer(inner.get(yCoord)));
			}
			copy.put(new Integer(xCoord), innerCopy);
		}
		return copy;
	}

	public static char[][] copyBoard(char[][] board) {
		char[][] returnArr = new char[9][9];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				returnArr[i][j] = board[i][j];
			}
		}

		return returnArr;
	}

	public static int[] firstEmpty(char[][] board) {
		int[] coords = new int[2];
		coords[0] = -1;
		coords[1] = -1;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == '.') {
					coords[0] = i;
					coords[1] = j;
					return coords;
				}
			}
		}
		return coords;
	}

	public static char nextAvailableNum(char[][] board, int x, int y, int floor) {
		Set<Integer> unavailableNumbers = new HashSet<>();

		for (int i = 0; i < 9; i++) {
			if (board[i][y] != '.') {
				unavailableNumbers.add(Character.getNumericValue(board[i][y]));
			}
		}
		for (int i = 0; i < 9; i++) {
			if (board[x][i] != '.') {
				unavailableNumbers.add(Character.getNumericValue(board[x][i]));
			}
		}

		List<Integer> xQuadCoords = new ArrayList<Integer>();
		List<Integer> yQuadCoords = new ArrayList<Integer>();

		if (x <= 2) {
			xQuadCoords.add(0);
			xQuadCoords.add(1);
			xQuadCoords.add(2);
		} else if (x <= 5) {
			xQuadCoords.add(3);
			xQuadCoords.add(4);
			xQuadCoords.add(5);
		} else {
			xQuadCoords.add(6);
			xQuadCoords.add(7);
			xQuadCoords.add(8);
		}

		if (y <= 2) {
			yQuadCoords.add(0);
			yQuadCoords.add(1);
			yQuadCoords.add(2);
		} else if (y <= 5) {
			yQuadCoords.add(3);
			yQuadCoords.add(4);
			yQuadCoords.add(5);
		} else {
			yQuadCoords.add(6);
			yQuadCoords.add(7);
			yQuadCoords.add(8);
		}

		for (Integer xCoord : xQuadCoords) {
			for (Integer yCoord : yQuadCoords) {
				if (board[xCoord][yCoord] != '.') {
					unavailableNumbers.add(Character.getNumericValue(board[xCoord][yCoord]));
				}
			}
		}

		for (int i = floor; i <= 9; i++) {
			if (!unavailableNumbers.contains(i)) {
				return Character.forDigit(i, 10);
			}
		}

		return '.';
	}

}