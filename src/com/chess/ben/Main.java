package com.chess.ben;

public class Main {
	public static void main(String[] args) {
		String startFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		Board startingBoard = PositionReader.readFENPosition(startFEN);
		Display displayBoard = new Display(startingBoard);
		
	}
}
