package com.chess.ben;

import java.util.ArrayList;

public class Piece {
	/*
	 * class that stores the relevant values of each piece.
	 * Done so that Piece.color + Piece.type is unique for every combonation 
	 */
	public static final int emptySquare = 0;
	public static final int pawn = 1;
	public static final int rook = 2;
	public static final int knight = 3;
	public static final int bishop = 4;
	public static final int king = 5;
	public static final int queen = 6;
	
	public static final int black = 0;
	public static final int white = 16;
	
	public static ArrayList<Integer> getMoveOptions(int pieceType, int pieceLocation, Board game){
		ArrayList<Integer> options = new ArrayList<Integer>();
		switch(pieceType%16) {
		case Piece.rook:
			options.addAll(Piece.getRookMoveOptions(pieceType, pieceLocation, game));
			break;
		case Piece.bishop:
			options.addAll(Piece.getBishopMoveOptions(pieceType, pieceLocation, game));
			break;
		case Piece.queen:
			options.addAll(Piece.getBishopMoveOptions(pieceType, pieceLocation, game));
			options.addAll(Piece.getRookMoveOptions(pieceType, pieceLocation, game));
			break;
		case Piece.knight:
			options.addAll(Piece.getKnightMoveOptions(pieceType, pieceLocation, game));
			break;
		case Piece.pawn:
			options.addAll(Piece.getPawnMoveOptions(pieceType, pieceLocation, game));
			break;
		case Piece.king: 
			options.addAll(Piece.getKingMoveOptions(pieceType, pieceLocation, game));
			break;
		}
		return options;
		
	}
	
	private static ArrayList<Integer> getRookMoveOptions(int pieceType, int pieceLocation, Board game){
		ArrayList<Integer> options = new ArrayList<Integer>();
		/*
		 * general structure for these functions:
		 * go forward in each direction until we hit a piece.
		 * once we hit a piece, check if we can take it, and then extend border once more*/
		
		//first, check left and right until a piece is found
		int leftIndex = 1;
		while((pieceLocation + leftIndex)%8 != 0 
				&& pieceLocation +leftIndex < game.getPosition().length
				&& game.getPosition()[pieceLocation + leftIndex] == 0) {
			options.add(pieceLocation + leftIndex);
			leftIndex += 1;
		}
		//check if piece is capturable
		if((pieceLocation + leftIndex)%8 != 0 
				&& pieceLocation +leftIndex < game.getPosition().length 
				&& Math.abs(game.getPosition()[pieceLocation]
				-game.getPosition()[pieceLocation + leftIndex]) >= 8) {
			options.add(pieceLocation + leftIndex);
		}
		
		int rightIndex = 1;
		while((pieceLocation - rightIndex)%8 != 7 
				&& pieceLocation -rightIndex >=0
				&& game.getPosition()[pieceLocation - rightIndex] == 0) {
			options.add(pieceLocation - rightIndex);
			rightIndex += 1;
		}
		if((pieceLocation - rightIndex)%8 != 7 
				&& pieceLocation -rightIndex >=0 
				&& Math.abs(game.getPosition()[pieceLocation]
				-game.getPosition()[pieceLocation -rightIndex]) >= 8) {
			options.add(pieceLocation - rightIndex);
		}
		
		//now, up and down
		int upIndex = 1;
		while(pieceLocation - (upIndex*8) >= 0 
				&& game.getPosition()[pieceLocation - upIndex*8] == 0) {
			options.add(pieceLocation - upIndex*8);
			upIndex += 1;
		}
		if(pieceLocation - (upIndex*8) >= 0 
				&& Math.abs(game.getPosition()[pieceLocation]
				-game.getPosition()[pieceLocation - upIndex*8]) >= 8) {
			options.add(pieceLocation - upIndex*8);
		}
		
		int downIndex = 1;
		while(pieceLocation + (downIndex*8) < game.getPosition().length
				&& game.getPosition()[pieceLocation + downIndex*8] == 0) {
			options.add(pieceLocation + downIndex*8);
			downIndex += 1;
		}
		if(pieceLocation + (downIndex*8) < game.getPosition().length 
				&& Math.abs(game.getPosition()[pieceLocation]
				-game.getPosition()[pieceLocation + downIndex*8]) >= 8) {
			options.add(pieceLocation + downIndex*8);
		}
		
		return options;
	}
	private static ArrayList<Integer> getBishopMoveOptions(int pieceType, int pieceLocation, Board game){
		ArrayList<Integer> options = new ArrayList<Integer>();
		
		int NWIndex = 1;
		while(pieceLocation - NWIndex*7 >= 0 
				&& (pieceLocation - NWIndex*7)%8 != 0
				&& game.getPosition()[pieceLocation - NWIndex*7] == 0) {
			options.add(pieceLocation - NWIndex*7);
			NWIndex++;
		}
		if(pieceLocation - NWIndex*7 >= 0 
				&& (pieceLocation - NWIndex*7)%8 != 0
				&& Math.abs(game.getPosition()[pieceLocation] - game.getPosition()[pieceLocation - NWIndex*7]) >= 8) {
			options.add(pieceLocation - NWIndex*7);
		}
		
		int SEIndex = 1;
		while(pieceLocation + SEIndex*7 < game.getPosition().length 
				&& (pieceLocation + SEIndex*7)%8 != 7
				&& game.getPosition()[pieceLocation + SEIndex*7] == 0) {
			options.add(pieceLocation + SEIndex*7);
			SEIndex++;
		}
		if(pieceLocation + SEIndex*7 < game.getPosition().length 
				&& (pieceLocation + SEIndex*7)%8 != 7
				&& Math.abs(game.getPosition()[pieceLocation]
						- game.getPosition()[pieceLocation + SEIndex*7]) >= 8) {
			options.add(pieceLocation + SEIndex*7);
		}
		
		int NEIndex = 1;
		while(pieceLocation - NEIndex*9 >= 0 
				&& (pieceLocation - NEIndex*9)%8 != 7
				&& game.getPosition()[pieceLocation - NEIndex*9] == 0) {
			options.add(pieceLocation - NEIndex*9);
			NEIndex++;
		}
		if(pieceLocation - NEIndex*9 >= 0 
				&& (pieceLocation - NEIndex*9)%8 != 7
				&& Math.abs(game.getPosition()[pieceLocation] 
						- game.getPosition()[pieceLocation - NEIndex*9]) >= 8) {
			options.add(pieceLocation - NEIndex*9);
		}
		
		int SWIndex = 1;
		while(pieceLocation + SWIndex*9 < game.getPosition().length
				&& (pieceLocation + SWIndex*9)%8 != 0
				&& game.getPosition()[pieceLocation + SWIndex*9] == 0) {
			options.add(pieceLocation + SWIndex*9);
			SWIndex++;
		}
		if(pieceLocation + SWIndex*9 < game.getPosition().length 
				&& (pieceLocation + SWIndex*9)%8 != 0
				&& Math.abs(game.getPosition()[pieceLocation] 
						- game.getPosition()[pieceLocation + SWIndex*9]) >= 8) {
			options.add(pieceLocation + SWIndex*9);
		}
		
		return options;
	}
	private static ArrayList<Integer> getKnightMoveOptions(int pieceType, int pieceLocation, Board game){
		ArrayList<Integer> options = new ArrayList<Integer>();
		int[] xMoves = {-2, -1, 1, 2, 2, 1, -1, -2};
		int[] yMoves = {-1, -2, -2, -1, 1, 2, 2, 1};
		for(int index=0; index < xMoves.length; index++) {
			// loops through all possible knight moves
			int newLocation = pieceLocation + xMoves[index] + yMoves[index]*8;
			
			if(newLocation >= 0 
				&& newLocation < game.getPosition().length
				&& ((pieceLocation%8 >= 6 && newLocation%8 > 1)
						|| (pieceLocation%8 <= 2 && newLocation%8 < 6
						|| (pieceLocation%8 > 2 && pieceLocation%8 < 6)))) {
				if(game.getPosition()[newLocation] == 0 
					|| Math.abs(game.getPosition()[newLocation] 
							- game.getPosition()[pieceLocation]) >= 8){
					options.add(newLocation);
				}								
			}
		}	
		return options;
	}
	private static ArrayList<Integer> getPawnMoveOptions(int pieceType, int pieceLocation, Board game){
		ArrayList<Integer> options = new ArrayList<Integer>();
		int colorModifier;
		if(pieceType - Piece.white > 0) {
			//piece is a white pawn
			colorModifier = -1;
		} else {
			//black pawn
			colorModifier = 1;
		}
		if(game.getPosition()[pieceLocation + colorModifier*8] == 0) {
			options.add(pieceLocation + colorModifier*8);
			if(((pieceLocation/8 == 6 && colorModifier == -1) || (pieceLocation/8 == 1 && colorModifier == 1))
					&& game.getPosition()[pieceLocation + colorModifier*16] == 0) {
				options.add(pieceLocation + colorModifier*16);
			}
		}
		
		if(pieceLocation + colorModifier*8 -1 >= 0
			&& pieceLocation + colorModifier*8 -1 < game.getPosition().length
			&& (pieceLocation + colorModifier*8 -1)%8 != 7
			&& Math.abs(game.getPosition()[pieceLocation + colorModifier*8 -1] 
					- game.getPosition()[pieceLocation]) >= 8
			&& game.getPosition()[pieceLocation + colorModifier*8 -1] != 0) {
			options.add(pieceLocation + colorModifier*8 -1);
		}
		if(pieceLocation + colorModifier*8 +1 >= 0
				&& pieceLocation + colorModifier*8 +1 < game.getPosition().length
				&& (pieceLocation + colorModifier*8 +1)%8 != 0
				&& Math.abs(game.getPosition()[pieceLocation + colorModifier*8 +1] 
						- game.getPosition()[pieceLocation]) >= 8
				&& game.getPosition()[pieceLocation + colorModifier*8 +1] != 0) {
				options.add(pieceLocation + colorModifier*8 +1);
			}
		if(Math.abs(game.getEnPassantRow()-pieceLocation%8) == 1) {
			if(colorModifier == -1 && pieceLocation/8 == 3) {
				//en passant available for white
				options.add(pieceLocation + colorModifier*8 
						+ (game.getEnPassantRow() - pieceLocation%8));
			}
			if(colorModifier == 1 && pieceLocation/8 == 4) {
				//en passant available for white
				options.add(pieceLocation + colorModifier*8 
						+ (game.getEnPassantRow() - pieceLocation%8));
			}
			
		}
		
		return options;
	}
	private static ArrayList<Integer> getKingMoveOptions(int pieceType, int pieceLocation, Board game){
		//TODO: add castling capabilities		
		ArrayList<Integer> options = new ArrayList<Integer>();
		int[] xMoves = {-1, 0, 1, -1, 1, -1, 0, 1};
		int[] yMoves = {-1, -1, -1, 0, 0, 1, 1, 1};
		for(int index = 0; index<xMoves.length; index++) {
			int newLocation = pieceLocation + xMoves[index] + yMoves[index]*8;
			//check if square is valid (ie in bounds
			if(newLocation >= 0
					&& newLocation < game.getPosition().length
					&&((xMoves[index] == -1 && newLocation%8 != 7)
							|| (xMoves[index] == 1 && newLocation%8 != 0)
							|| (xMoves[index] == 0))){
				if(game.getPosition()[newLocation] == 0 ||
						Math.abs(game.getPosition()[newLocation]
								-game.getPosition()[pieceLocation]) >= 8) {
					options.add(newLocation);
				}
				
			}
		}
		if(pieceType == Piece.white + Piece.king) {
			// White king
			if(game.getPosition()[61] == 0
					&& game.getPosition()[62] == 0
					&& game.isWhiteCanCastleKingside()) {
				options.add(62);
			}
			if(game.getPosition()[59] == 0
					&& game.getPosition()[58] == 0
					&& game.getPosition()[57] == 0
					&& game.isWhiteCanCastleQueenside()) {
				options.add(58);
			}
		} else if(pieceType == Piece.black + Piece.king) {
			// black king
			if(game.getPosition()[5] == 0
					&& game.getPosition()[6] == 0
					&& game.isBlackCanCastleKingside()) {
				options.add(6);
			}
			if(game.getPosition()[3] == 0
					&& game.getPosition()[2] == 0
					&& game.getPosition()[1] == 0
					&& game.isBlackCanCastleQueenside()) {
				options.add(2);
			}
		}
		
		return options;
	}
	
	
}
