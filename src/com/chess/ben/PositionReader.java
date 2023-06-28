package com.chess.ben;

public class PositionReader {
	/*
	 * Class that reads positions in different formats
	 */
	
	public static Board readFENPosition(String input) {
		/*decodes FEN notation and returns the correct board with that position
		 * FEN input notation can be found here:
		 *  https://www.chessprogramming.org/Forsyth-Edwards_Notation*/
		
		int[] position = new int[64];
		for(int i=0;i<position.length;i++) {
			position[i] = 0;
		}
		
		int characterIndex = 0;
		int currentSquare = 0;
		while(characterIndex < input.length() && input.charAt(characterIndex) != ' '){
			char currentChar = input.charAt(characterIndex);
			if(Character.isDigit(currentChar)) {
				currentSquare += Character.getNumericValue(currentChar);
			} else if(currentChar != '/') {
				
				switch(Character.toLowerCase(currentChar)) {
				case 'p': position[currentSquare] += Piece.pawn;
				break;
				case 'r': position[currentSquare] += Piece.rook;
				break;
				case 'n': position[currentSquare] += Piece.knight;
				break;
				case 'b': position[currentSquare] += Piece.bishop;
				break;
				case 'k': position[currentSquare] += Piece.king;
				break;
				case 'q': position[currentSquare] += Piece.queen;
				break;
				}
				
				if(Character.isUpperCase(currentChar)) {
					position[currentSquare] += Piece.white;
				} else {
					position[currentSquare] += Piece.black;
				}
				currentSquare++;
			}
			characterIndex++;
		}
		
		characterIndex++;
		int playerToMove;
		if(input.charAt(characterIndex) == 'b') {
			playerToMove = 0;
		} else {
			playerToMove = 16;
		}
		
		characterIndex++;
		characterIndex++;
		boolean whiteQueen = false;
		boolean whiteKing = false;
		boolean blackQueen = false;
		boolean blackKing = false;
		while(characterIndex < input.length() && input.charAt(characterIndex) != ' ') {
			switch(input.charAt(characterIndex)) {
			case 'K': whiteKing = true;
			break;
			case 'Q': whiteQueen = true;
			break;
			case 'k': blackKing = true;
			break;
			case 'q': blackQueen = true;
			break;
			}
			characterIndex++;
		}
		
		return new Board(position, whiteQueen, whiteKing, blackQueen, blackKing, playerToMove);
	}
}
