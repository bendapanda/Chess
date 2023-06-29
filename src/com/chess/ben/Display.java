package com.chess.ben;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Display extends JFrame{
	/**
	 * class that handles the displaying of chess positions. Given a board layout,
	 * it draws a default board position, and the pieces on the board
	 */
private static final long serialVersionUID = 1L;
private final char BLACK_SQUARE = 'x';
private final char WHITE_SQUARE = 'o';
private final int NUM_SQUARES = 8;
private final int boardSize;

private int selectedPiece = -1;
private Board game;
private BoardTile[] squares;
private JPanel boardPanel;
private ArrayList<Integer> activeSquareLocations = new ArrayList<Integer>();

// Icon for frame, also black bishop
//then loading all the images required
private ImageIcon blackBishopIcon = new ImageIcon(Display.class.getResource("/resources/black_bishop.png"));
private ImageIcon blackPawnIcon = new ImageIcon(Display.class.getResource("/resources/black_pawn.png"));
private ImageIcon blackKnightIcon = new ImageIcon(Display.class.getResource("/resources/black_knight.png"));
private ImageIcon blackRookIcon = new ImageIcon(Display.class.getResource("/resources/black_rook.png"));
private ImageIcon blackKingIcon = new ImageIcon(Display.class.getResource("/resources/black_king.png"));
private ImageIcon blackQueenIcon = new ImageIcon(Display.class.getResource("/resources/black_queen.png"));
private ImageIcon whitePawnIcon = new ImageIcon(Display.class.getResource("/resources/white_pawn.png"));
private ImageIcon whiteRookIcon = new ImageIcon(Display.class.getResource("/resources/white_rook.png"));
private ImageIcon whiteKnightIcon = new ImageIcon(Display.class.getResource("/resources/white_knight.png"));
private ImageIcon whiteBishopIcon = new ImageIcon(Display.class.getResource("/resources/white_bishop.png"));
private ImageIcon whiteKingIcon = new ImageIcon(Display.class.getResource("/resources/white_king.png"));
private ImageIcon whiteQueenIcon = new ImageIcon(Display.class.getResource("/resources/white_queen.png"));



Display(Board gameLayout){
	/*
	 * creates a display frame that matches the board layout
	 */
	this.boardSize = 600;
	this.game = gameLayout;
	this.squares = new BoardTile[64];
	
	
	//creating panel for the game board
	this.boardPanel = new JPanel();
	this.boardPanel.setBackground(Color.cyan);
	this.boardPanel.setBounds(0, 0, this.boardSize, this.boardSize);
	this.boardPanel.setLayout(new GridLayout(8, 8));
	
	// Creating panels in the correct colors
	int index = 0;
	for(int i=0; i<this.NUM_SQUARES;i++) {
		for(int j=0; j<this.NUM_SQUARES;j++) {
			Color tileColor = Color.blue;
			if((i+j)%2 == 0) {
				tileColor = BoardTile.WHITE;
			} else {
				tileColor = BoardTile.BLACK;
			}
			this.squares[index] = new BoardTile(this, index, tileColor);
			
			this.boardPanel.add(this.squares[index]);
			index++;
		}
	}
	
	for(int i=0; i<this.game.getPosition().length; i++) {
		switch(this.game.getPosition()[i]) {
		case Piece.pawn + Piece.white: this.squares[i].add(new JLabel(this.whitePawnIcon));
		break;
		case Piece.pawn + Piece.black: this.squares[i].add(new JLabel(this.blackPawnIcon));
		break;
		case Piece.bishop + Piece.white: this.squares[i].add(new JLabel(this.whiteBishopIcon));
		break;
		case Piece.bishop + Piece.black: this.squares[i].add(new JLabel(this.blackBishopIcon));
		break;
		case Piece.knight + Piece.white: this.squares[i].add(new JLabel(this.whiteKnightIcon));
		break;
		case Piece.knight + Piece.black: this.squares[i].add(new JLabel(this.blackKnightIcon));
		break;
		case Piece.rook + Piece.white: this.squares[i].add(new JLabel(this.whiteRookIcon));
		break;
		case Piece.rook + Piece.black: this.squares[i].add(new JLabel(this.blackRookIcon));
		break;
		case Piece.king + Piece.white: this.squares[i].add(new JLabel(this.whiteKingIcon));
		break;
		case Piece.king + Piece.black: this.squares[i].add(new JLabel(this.blackKingIcon));
		break;
		case Piece.queen + Piece.white: this.squares[i].add(new JLabel(this.whiteQueenIcon));
		break;
		case Piece.queen + Piece.black: this.squares[i].add(new JLabel(this.blackQueenIcon));
		break;
		}
	}
	
	
	
	this.setIconImage(blackBishopIcon.getImage());
	this.setTitle("chess!");
	this.setSize(650, 650);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setResizable(false);
	this.setLayout(null);
	this.setVisible(true);	
	
	this.getContentPane().setBackground(Color.gray);
	this.add(this.boardPanel);
	
	
}

public void printDisplay(Board board) {
	/*
	 * takes in a Board object and prints a string representation of the board.
	 * 'o characters mark white squares, and 'x' characters mark a black square. 
	 * Number characters are used to represent a Piece, as given in the class.
	 * 
	 * @param board a Board representing the current game state
	 * @return null
	 * @see Board
	 * @see Piece*/
	String boardRepresentation  = "";
	int modifier = 1;
	for(int i=0; i<board.getPosition().length; i++) {
		if(i % 8 == 0) {
			modifier += 1;
			modifier = modifier % 2;
			boardRepresentation += '\n';
		}
		if(board.getPosition()[i] == 0) {
			if(((i+modifier) % 2) == 0) {
				boardRepresentation += BLACK_SQUARE;
			} else {
				boardRepresentation += WHITE_SQUARE;
			}
		} else {
			boardRepresentation += board.getPosition()[i];
		}
		
	}
	System.out.print(boardRepresentation);
}

public void justClicked(BoardTile tile) {
	/*
	 * Method called when a tile is clicked.
	 * If no piece is selected, selects the piece if is the player's and displays possible moves.
	 * If a piece is selected already, 
	 */
	//step 1: checks if a piece is currently selected
	//	Case 1: piece is currently selected
	//		Case 1.1: new piece is our color
	//			Deselect current piece, select new piece
	//		Case 1.2: new square is not a piece:
	//			if square in range, move piece, if not deselect current piece
	//		Case 1.3: new square is not our color:
	//			if piece in range, capture if not deselect
	//
	//	Case 2: piece is not currently selected
	//		if piece is our color, select it, if not, do nothing.
	int tileIndex = tile.getIndex();
	boolean turnMade = false;
	//first check if a piece is selected or not
	if(this.selectedPiece != -1) {
		// a piece is already selected
		if(this.game.getPosition()[tileIndex] == 0) {
			System.out.println("piece selected, empty square clicked");
			if(this.activeSquareLocations.contains(tileIndex) 
					&& tileIndex != this.selectedPiece) {
				System.out.println("square in range, piece moved");
				
				if(this.game.movePiece(this.selectedPiece, tileIndex)) {
					turnMade = true;
				}
				this.deselectPiece();
			} else {
				this.deselectPiece();
			}	
		} else if(this.pieceIsCurrentColor(tileIndex)) {
			System.out.println("new piece selected");
			this.deselectPiece();
			this.selectPiece(tileIndex);
		} else {
			System.out.println("other player's piece selected");
			if(this.activeSquareLocations.contains(tileIndex)) {
				System.out.println("piece taken!");
				if(this.game.movePiece(this.selectedPiece, tileIndex)) {
					turnMade = true;
				}
				this.deselectPiece();
			} else {
				System.out.println("piece not in range");
				this.deselectPiece();
			}
		}
	} else {// runs through if no piece is selected
		if(this.game.getPosition()[tileIndex] == 0) {
			// if no piece clicked do nothing
			System.out.println("no piece here!");
			
		} else if(this.pieceIsCurrentColor(tileIndex)) {
			System.out.println("pieceSelected");
			this.selectPiece(tileIndex);
			
		} else {
			// if not our piece, do nothing
		System.out.println("not ur piece");
		}
	}
	// Once piece has moved, refresh board
	if(turnMade) {
		this.nextPlayerTurn();
		this.colorCheckedSquares();
	}
	
	this.refresh();
}
private boolean pieceIsCurrentColor(int tileIndex) {
	/*
	 * takes in a position on the board, and returns true if it is in the current player's
	 * algorithm*/
	return (this.game.getPlayerToMove()/16 - this.game.getPosition()[tileIndex]/16) == 0;
}
private void toggleSquares(ArrayList<Integer> squaresToToggle) {
	/*
	 * given a list of squares, toggles the color of each one*/
	for(int squareIndex: squaresToToggle) {
		this.squares[squareIndex].toggleColor();
	}
}
private void refresh() {
	/*
	 * method that redraws the screen based on the boardstate
	 */
	//first remove all pieces
	for(BoardTile square: this.squares) {
		square.removeAll();
	}
	//then, redraw all pieces
	for(int i=0; i<this.game.getPosition().length; i++) {
		switch(this.game.getPosition()[i]) {
		case Piece.pawn + Piece.white: this.squares[i].add(new JLabel(whitePawnIcon));
		break;
		case Piece.pawn + Piece.black: this.squares[i].add(new JLabel(blackPawnIcon));
		break;
		case Piece.bishop + Piece.white: this.squares[i].add(new JLabel(whiteBishopIcon));
		break;
		case Piece.bishop + Piece.black: this.squares[i].add(new JLabel(blackBishopIcon));
		break;
		case Piece.knight + Piece.white: this.squares[i].add(new JLabel(whiteKnightIcon));
		break;
		case Piece.knight + Piece.black: this.squares[i].add(new JLabel(blackKnightIcon));
		break;
		case Piece.rook + Piece.white: this.squares[i].add(new JLabel(whiteRookIcon));
		break;
		case Piece.rook + Piece.black: this.squares[i].add(new JLabel(blackRookIcon));
		break;
		case Piece.king + Piece.white: this.squares[i].add(new JLabel(whiteKingIcon));
		break;
		case Piece.king + Piece.black: this.squares[i].add(new JLabel(blackKingIcon));
		break;
		case Piece.queen + Piece.white: this.squares[i].add(new JLabel(whiteQueenIcon));
		break;
		case Piece.queen + Piece.black: this.squares[i].add(new JLabel(blackQueenIcon));
		break;
		}
		this.squares[i].validate();
	}
}

private void deselectPiece() {
	/*
	 * toggles the color of all selected squares, and resets the selected and active squares
	 */
	for(int pos: this.activeSquareLocations) {
		this.squares[pos].setActive(false);
	}
	this.toggleSquares(this.activeSquareLocations);
	this.selectedPiece = -1;
	this.activeSquareLocations.removeAll(this.activeSquareLocations);
}
private void selectPiece(int pieceLocation) {
	/*
	 * selects the piece at the given location, and highlights all possible
	 * squares that can be moved to,
	 * as well as set them as active
	 */
	this.selectedPiece = pieceLocation;
	this.squares[this.selectedPiece].setActive(true);
	this.activeSquareLocations.add(this.selectedPiece);
	
	ArrayList<Integer> moveOptions = Piece.getMoveOptions(this.game.getPosition()[this.selectedPiece],
			pieceLocation, game);
	for(int pos: moveOptions) {
		this.squares[pos].setActive(true);
	}
	this.activeSquareLocations.addAll(moveOptions);
	this.toggleSquares(this.activeSquareLocations);
}

private void nextPlayerTurn() {
	this.game.toggleTurn();
	if(this.game.isTwoPlayer()) {
		this.rotateBoard();
	} else {
		System.out.println("ai turn");
	}
}

private void rotateBoard() {
	/*
	 * rotates board to face the player whose turn it is
	 */
	boolean playerIsWhite = this.game.getPlayerToMove() == Piece.white;
	
	if(playerIsWhite) {
		System.out.println("white's turn");	
		this.boardPanel.removeAll();
		for(int index=0; index<this.squares.length; index++) {
			this.boardPanel.add(this.squares[index]);
		}
	} else {
		
		this.boardPanel.removeAll();
		for(int index=this.squares.length-1; index>=0; index--) {
			this.boardPanel.add(this.squares[index]);
		}
	}
	
	this.boardPanel.validate();
}
private void colorCheckedSquares() {
	for(int i=0; i< this.squares.length; i++) {
		if(this.game.getPosition()[i] == Piece.white + Piece.king
				&& this.game.isWhiteCheck()) {
			this.squares[i].setChecked(true);
		} else if(this.game.getPosition()[i] == Piece.black + Piece.king
				&& this.game.isBlackCheck()) {
			this.squares[i].setChecked(true);
		} else {
			this.squares[i].setChecked(false);
		}
	}
	
}
}
