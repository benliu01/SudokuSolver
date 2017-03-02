package com.blizzle.sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoardManager {
	private static class SudokuPanel extends JPanel {

        int digit;
        int x, y;

        JLabel label;
        
        SudokuPanel(int x, int y) {
            super();

            this.x = x;
            this.y = y;

            /** create a black border */
            setBorder(BorderFactory.createLineBorder(Color.black));

            /** set size to 50x50 pixel for one square */
            setPreferredSize(new Dimension(50,50));
            
            this.label = new JLabel("");
            this.add(this.label);
        }

        public int getDigit() { 
        	return digit; 
        }

        public void setDigit(int num, boolean bold) { 
        	digit = num;
        	this.label.setText(Integer.toString(num));
        	
        	if (bold) {
        		this.label.setFont(new Font("Courier", Font.BOLD,18));
        	} else {
        		this.label.setFont(new Font("Courier", Font.PLAIN,12));
        	}
        }

    }
	
	private static class SudokuGrid extends JPanel {
		private SudokuPanel[][] board;
		
        SudokuGrid(int w, int h) {
            super(new GridBagLayout());

            board = new SudokuPanel[w][h];
            
            GridBagConstraints c = new GridBagConstraints();
            /** construct the grid */
            for (int i=0; i<w; i++) {
                for (int j=0; j<h; j++) {
                    c.weightx = 1.0;
                    c.weighty = 1.0;
                    c.fill = GridBagConstraints.BOTH;
                    c.gridx = i;
                    c.gridy = j;
                    SudokuPanel panel = new SudokuPanel(i, j);
                    add(panel, c);
                    board[i][j] = panel;
                }
            }

            /** create a black border */ 
            setBorder(BorderFactory.createLineBorder(Color.black)); 

        }

        public void setValue(int x, int y, int v, boolean bold) {
        	board[x][y].setDigit(v, bold);
        }
    }
	
	private SudokuGrid sudokuGrid;
	
	public BoardManager() {
		sudokuGrid = new SudokuGrid(9, 9);
	}
	
	public void show() {
        JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(this.sudokuGrid);

        frame.pack();
        frame.setVisible(true);
	}
	
    public void setValue(int x, int y, int v, boolean bold) {
    	sudokuGrid.setValue(x, y, v, bold);
    }
}
