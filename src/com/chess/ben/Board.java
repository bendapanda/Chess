package com.chess.ben;

import java.util.ArrayList;

import java.lang.*;
import java.util.Arrays;

public class Board {
	/*
	 * OVERALL GAME STRUCTURE: idea only. may or may not be accurate
	 * first analyse possible moves?
	 * then ask for user input
	 * if input is in the list of possible moves allow it
	 * otherwise do not
	 */
	private int[] position;
	
	private boolean whiteCanCastleQueenside;
	private boolean whiteCanCastleKingside;
	private boolean blackCanCastleQueenside;
	private boolean blackCanCastleKingside;
	private int enPassantRow = -1;
	private boolean twoPlayer = true;
	private int playerToMove;
	private ArrayList<Integer> blackCoverage = new ArrayList<Integer>();
	private ArrayList<Integer> whiteCoverage = new ArrayList<Integer>();
	private boolean whiteCheck;
	private boolean blackCheck;
	
	Board() {
		this.position = new int[64];
		
		for(int i=0; i<this.position.length; i++) {
			this.position[i] = 0;
		}
	}
	
	Board(int[] currentPosition, boolean whiteQueenside, boolean whiteKingside,
			boolean blackQueenside, boolean blackKingside, int player){
		this.position = currentPosition;
		this.whiteCanCastleKingside = whiteKingside;
		this.blackCanCastleKingside = blackKingside;
		this.blackCanCastleQueenside = blackQueenside;
		this.whiteCanCastleQueenside = whiteQueenside;
		this.playerToMove = player;
		
		this.setCoverage();
		
		this.setChecks();
		
	}
	
	public String toString() {
		return Arrays.toString(this.position) + '\n' + this.whiteCanCastleKingside +
				'\n'+ this.whiteCanCastleQueenside + '\n' + this.blackCanCastleKingside + 
				'\n' + this.blackCanCastleQueenside + '\n' + this.playerToMove;
	}
	
	public int[] getPosition() {
		return this.position;
	}
	public int getPlayerToMove() {
		return this.playerToMove;
	}
	public boolean movePiece(int oldLocation, int newLocation) {
		/*
		 * Method that controls all the logic for processing moves
		 * If the new board state results in a check, then does not commit the move.
		 * Returns true if valid move made, false otherwise
		 */
		//we need to make sure that any move made does not put our piece in check
		int[] oldPosition = this.position.clone(); // to revert back to if new position is illegal

		//TODO: Can't castle through check
		//TODO: check markers
		//TODO: pawn to different piece type
		// We need to check special moves like castling and en pasunt
		boolean pieceIsWhiteKing = this.position[oldLocation] == Piece.white + Piece.king;
		boolean pieceIsBlackKing = this.position[oldLocation] == Piece.black + Piece.king;
		boolean pieceIsPawn = this.position[oldLocation]%16 == Piece.pawn;
		int defaultWhiteKingPosition = 60;
		int kingsideWhiteCastlePosition = 62;
		int queensideWhiteCastlePosition = 58;
		int defaultBlackKingPosition = 4;
		int queensideBlackCastlePosition = 2;
		int kingsideBlackCastlePosition = 6;
		
		boolean pieceHasMovedDiagonalOneSquare = Math.abs(oldLocation%8 - newLocation%8) == 1;
		if(pieceIsWhiteKing	
				&& oldLocation == defaultWhiteKingPosition 
				&& newLocation == kingsideWhiteCastlePosition) {
			//kingside Castle
			this.position[newLocation] = this.position[oldLocation];
			this.position[newLocation - 1] = this.position[63];
			this.position[63] = 0;
			this.position[oldLocation] = 0;

			this.disableWhiteCanCastleKingside();
			this.disableWhiteCanCastleQueenside();
			System.out.println("a castle");
		} else if(pieceIsWhiteKing
				&& oldLocation == defaultWhiteKingPosition 
				&& newLocation == queensideWhiteCastlePosition) {
			//Queenside Castle
			this.position[newLocation] = this.position[oldLocation];
			this.position[newLocation + 1] = this.position[56];
			this.position[56] = 0;
			this.position[oldLocation] = 0;
			
			this.disableWhiteCanCastleKingside();
			this.disableWhiteCanCastleQueenside();
			System.out.println("a castle");
		} else if(pieceIsBlackKing
				&& oldLocation == defaultBlackKingPosition 
				&& newLocation == kingsideBlackCastlePosition) {
			//kingside Castle
			this.position[newLocation] = this.position[oldLocation];
			this.position[newLocation - 1] = this.position[7];
			this.position[7] = 0;
			this.position[oldLocation] = 0;

			this.disableBlackCanCastleKingside();
			this.disableBlackCanCastleQueenside();
			System.out.println("a castle");
		} else if(pieceIsBlackKing
				&& oldLocation == defaultBlackKingPosition
				&& newLocation == queensideBlackCastlePosition) {
			//Queenside Castle
			this.position[newLocation] = this.position[oldLocation];
			this.position[newLocation + 1] = this.position[0];
			this.position[0] = 0;
			this.position[oldLocation] = 0;
			
			this.disableWhiteCanCastleKingside();
			this.disableWhiteCanCastleQueenside();
			System.out.println("a castle");
		} else if(pieceHasMovedDiagonalOneSquare
				&& pieceIsPawn
				&& this.position[newLocation] == Piece.emptySquare) {
			//En passant: If a diagonal move is available, and no piece in the new location
			//then do en passant
			int capturedPieceLocation = oldLocation - (oldLocation%8 - newLocation%8);
			this.position[capturedPieceLocation] = 0;
			
			this.position[newLocation] = this.position[oldLocation];
			this.position[oldLocation] = 0;
			System.out.println("en passant");
		}
		else {
			this.position[newLocation] = this.position[oldLocation];
			this.position[oldLocation] = 0;
		}	
		
		
		// Now, once all moves have been made, re-evaluate checks
		this.setCoverage();
		this.setChecks();
		if(this.playerToMove == Piece.white && this.whiteCheck) {
			//Invalid position
			this.position = oldPosition;
			this.setCoverage();
			this.setChecks();
			System.out.println("you are still in check!");
			return false;
		} else if (this.playerToMove == Piece.black && this.blackCheck) {
			//Invalid position
			this.position = oldPosition;
			this.setCoverage();
			this.setChecks();
			System.out.println("you are still in check!");
			return false;
		}
		// valid position
		this.updateCastlingRights(oldLocation, newLocation, oldPosition);
		this.updateEnPassantAvailability(oldLocation, newLocation, oldPosition);
		return true;
		
	}

	public boolean isWhiteCheck() {
		return whiteCheck;
	}

	public boolean isBlackCheck() {
		return blackCheck;
	}

	public void toggleTurn() {
		if(this.playerToMove == Piece.white) {
			this.playerToMove = Piece.black;
		} else {
			this.playerToMove = Piece.white;
		}
	}

	public boolean isTwoPlayer() {
		return twoPlayer;
	}

	public void setTwoPlayer(boolean twoPlayer) {
		this.twoPlayer = twoPlayer;
	}
	public boolean isWhiteCanCastleQueenside() {
		return whiteCanCastleQueenside;
	}

	public void disableWhiteCanCastleQueenside() {
		this.whiteCanCastleQueenside = false;
	}

	public boolean isWhiteCanCastleKingside() {
		return whiteCanCastleKingside;
	}

	public void disableWhiteCanCastleKingside() {
		this.whiteCanCastleKingside = false;
	}

	public boolean isBlackCanCastleQueenside() {
		return blackCanCastleQueenside;
	}

	public void disableBlackCanCastleQueenside() {
		this.blackCanCastleQueenside = false;
	}

	public boolean isBlackCanCastleKingside() {
		return blackCanCastleKingside;
	}

	public void disableBlackCanCastleKingside() {
		this.blackCanCastleKingside = false;
	}

	public int getEnPassantRow() {
		return enPassantRow;
	}

	public ArrayList<Integer> getBlackCoverage() {
		return blackCoverage;
	}

	public ArrayList<Integer> getWhiteCoverage() {
		return whiteCoverage;
	}
	
	private void setCoverage() {
		/*
		 * runs through all pieces on the board, and returns every square that piece can attack right
		 * now.
		 * Very many issues right now. 
		 */
		this.whiteCoverage.removeAll(this.whiteCoverage);
		this.blackCoverage.removeAll(this.blackCoverage);
		
		for(int i=0; i<this.position.length; i++) {
			int squareValue = this.position[i];
			if(squareValue != Piece.emptySquare) {
				boolean pieceIsWhite = squareValue/16 == 1;
				boolean pieceIsBlack = squareValue/16 == 0;
				if(pieceIsWhite) {
					this.whiteCoverage.addAll(Piece.getMoveOptions(squareValue, i, this));
				} else if(pieceIsBlack) {
					this.blackCoverage.addAll(Piece.getMoveOptions(squareValue, i, this));
				}
			}
		}
	}
	
	private void setChecks() {
		/*
		 * runs through all covered squares by each side. If the
		 * respective kings are in the covered squares from each side, sets the check
		 * to be false.
		 */
		this.whiteCheck = false;
		for(int squareLocation: this.blackCoverage) {
			if(this.position[squareLocation] == Piece.white + Piece.king) {
				this.whiteCheck = true;
			}
		}
		this.blackCheck = false;
		for(int squareLocation: this.whiteCoverage) {
			if(this.position[squareLocation] == Piece.black + Piece.king) {
				this.blackCheck = true;
			}
		}
		System.out.println("white in check: " + this.whiteCheck + " black in check: " + this.blackCheck);
	}
	private void updateCastlingRights(int oldLocation, int newLocation, int[] oldBoardState) {
		/*
		 * Given a committed move as input, will update castling rights if the move interferes
		 * with them
		 */
		boolean pieceMovedIsWhiteRook = oldBoardState[oldLocation] == Piece.white + Piece.rook;
		boolean pieceTakenIsWhiteRook = oldBoardState[newLocation] == Piece.white + Piece.rook;
		if(pieceMovedIsWhiteRook || pieceTakenIsWhiteRook) {
			int kingSide = 63;
			int queenSide = 56;
			if(oldLocation == kingSide || newLocation == kingSide) {
				this.disableWhiteCanCastleKingside();
			}
			if(oldLocation == queenSide || newLocation == queenSide) {
				this.disableWhiteCanCastleQueenside();
			}
		} 
		boolean pieceMovedIsBlackRook = oldBoardState[oldLocation] == Piece.black + Piece.rook;
		boolean pieceTakenIsBlackRook = oldBoardState[newLocation] == Piece.black + Piece.rook;
		if(pieceMovedIsBlackRook || pieceTakenIsBlackRook) {
			int queenSide = 0;
			int kingSide = 7;
			if(oldLocation == kingSide || newLocation == kingSide) {
				this.disableBlackCanCastleKingside();
			}
			if(oldLocation == queenSide || newLocation == queenSide) {
				this.disableBlackCanCastleQueenside();
			}
		}
		if(oldBoardState[oldLocation] == Piece.white + Piece.king) {
			this.disableWhiteCanCastleKingside();
			this.disableWhiteCanCastleQueenside();
		}
		if(oldBoardState[oldLocation] == Piece.black + Piece.king) {
			this.disableBlackCanCastleKingside();
			this.disableBlackCanCastleQueenside();
		}
	}
	private void updateEnPassantAvailability(int oldLocation, int newLocation, int[] oldBoardState) {
		boolean pieceIsWhitePawn = oldBoardState[oldLocation] == Piece.white + Piece.pawn;
		boolean pieceIsBlackPawn = oldBoardState[oldLocation] == Piece.black + Piece.pawn;
		int startRank = oldLocation/8;
		int newRank = newLocation/8;
		if(pieceIsWhitePawn &&  startRank == 6 && newRank == 4) {
			System.out.println("en passant available");
			this.enPassantRow = oldLocation%8;
			
		} else if(pieceIsBlackPawn && startRank == 1 && newRank == 3) {
			System.out.println("en passant available");
			this.enPassantRow = oldLocation%8;
			
		} else {
			this.enPassantRow = -1;
		}
	}
}
