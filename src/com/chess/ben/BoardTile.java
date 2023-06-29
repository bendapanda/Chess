package com.chess.ben;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class BoardTile extends JPanel implements MouseListener {
	/**
	 * 
	 */
	public static final Color BLACK = new Color(80, 0, 80);
	public static final Color WHITE = new Color(255, 220, 255);
	public static final Color BLACK_SELECTED = new Color(210, 210, 60);
	public static final Color WHITE_SELECTED = new Color(251, 255, 220);
	public static final Color CHECK = Color.red;
	private static final long serialVersionUID = 1L;
	private final Color defaultColor;
	private int index;
	
	private final Display display;
	private Color color = Color.red;
	private boolean isChecked = false;
	private boolean isActive = false;
	
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	BoardTile(Display disp, int index, Color defaultColor){
		
		this.addMouseListener(this);
		this.display = disp;
		this.index = index;
		this.defaultColor = defaultColor;
		this.color = defaultColor;
		this.setBackground(this.color);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stubs
		this.display.justClicked(this);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
		this.setBackground(this.color);
	}
	public void toggleColor() {
		/*
		 * toggles the tile's color between the selected and deselected colors, unless the piece is
		 * in check.
		 */
		if(this.isActive) {
			if(this.color == BoardTile.BLACK) {
				this.color = BoardTile.BLACK_SELECTED;
			} else if(this.color == BoardTile.WHITE) {
				this.color = BoardTile.WHITE_SELECTED;
			}
		} else {
			if(this.isChecked) {
				this.color = BoardTile.CHECK;
			}else {
				this.color = this.defaultColor;
			}
			
		}
		
		this.setBackground(this.color);
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		/*
		 * is called for each piece after each completed move. If the piece is a king,
		 * markes it as checked. Otherwise, changes the square to be the correct color.
		 */
		this.isChecked = isChecked;
		if(this.isChecked) {
			//we need to color red
			this.color = BoardTile.CHECK;
		} else {
			//we need to color to active or innactive
			if(this.isActive) {
				this.color = this.defaultColor;
				this.toggleColor();
			} else {
				this.color = this.defaultColor;
			}
		}
		this.setBackground(this.color);
		
		
	}

}
