package com.blizzle.sudoku;

public class Main {

	public static void main(String[] args) {
		BoardManager bm = new BoardManager();
		bm.show();
		
		Solver solver = new Solver(bm);
		
		String[] compressed = {"..9748...","7........",".2.1.9...","..7...24.",".64.1.59.",".98...3..","...8.3.2.","........6","...2759.."};
		solver.setStart(compressed);
		solver.solve();
	}

}
