package gui;

import javax.swing.*;

import chessGame.chessGame;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Board {
	
	private Color whiteTiles = Color.decode("#edebd3");
	private Color blackTiles = Color.decode("#545239");
	private Color blueTiles = Color.decode("#09F6BB");
	
	public static int selected = -1; //when no piece is selected equals -1
	chessGame newGame = new chessGame();
	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	private final GravePanel grave; 
	public int graveCount = 0;
	public boolean[] highlighted = new boolean[64];
	
	private final static Dimension BOARD_PANEL = new Dimension(80, 80);
	private final static Dimension TILE_PANEL = new Dimension(20, 20);
	
	public Board() {
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setLayout(new BorderLayout());
		this.gameFrame.setSize(800, 800);
		this.grave = new GravePanel();
		this.boardPanel = new BoardPanel(grave);
		this.grave.setLayout(new BoxLayout(grave, BoxLayout.Y_AXIS));
		this.gameFrame.add(this.grave, BorderLayout.WEST);
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.gameFrame.setVisible(true);
	}

	
private class GravePanel extends JPanel{
	
	GravePanel(){
	  JLabel tittle = new JLabel("Grave Yard Tile");
	  this.add(tittle);
	}
	void graveYardAdd(String piece){
	  String description = (piece);
      JLabel add = new JLabel(description);
      this.add(add);
      revalidate();
	}
}
	
	
	
private class BoardPanel extends JPanel{
	final List<TilePanel> boardTiles;
	
	BoardPanel(final GravePanel grave){
		super(new GridLayout(8,8));
		this.boardTiles = new ArrayList<>();
		for(int i = 0; i < 64; i++) {
			highlighted[i] = false;
			final TilePanel tilePanel = new TilePanel(this, grave, i);
			this.boardTiles.add(tilePanel);
			add(tilePanel);
		}
		this.setSize(BOARD_PANEL);
		validate();
	}
	void drawBoard(final chessGame newGame) {
		for (final TilePanel tilePanel: boardTiles) {
			tilePanel.drawTile(newGame);
			add(tilePanel);
		}
		validate();
		repaint();
	}
}

private class TilePanel extends JPanel{
	
	private final int tileID;
	
	TilePanel(final BoardPanel boardPanel, final GravePanel grave,final int tileID){
	  super(new GridBagLayout());
	  this.tileID = tileID; 
	  this.setPreferredSize(TILE_PANEL);
	  assignTileColor();
	  assignTileSprite();
	  
	  addMouseListener(new MouseAdapter() {
		  @Override
		  public void mousePressed(MouseEvent e) {
			  
			 if (Board.selected==-1) {
				 //piece select
				 if(newGame.p[tileID].getName() != null && 
						 newGame.p[tileID].getAlly() == newGame.turn) {
				     Board.selected = tileID;
				     highlightTile(Board.selected);
				 }
				 else 
					 Board.selected = -1;
			 }
			 
			
			 else if(newGame.p[tileID].getLocation()==tileID) {
				 //deselect when moved to same spot
				 Board.selected = -1;
			 }
			  else if(newGame.p[tileID].getAlly() == newGame.p[Board.selected].getAlly()) {
				 System.out.println("cant take own piece");
				 Board.selected = -1;
			 }
			 else if ((Board.selected!=-1 && newGame.p[tileID].getName()!=null 
					 && newGame.p[Board.selected].validMove(Board.selected, tileID) == true)) {
				 //take piece 
				 grave.graveYardAdd(newGame.p[tileID].getName());
				 newGame.holder = newGame.grave[graveCount];
				 newGame.grave[graveCount] = newGame.p[Board.selected];
				 newGame.p[tileID] = newGame.grave[graveCount];
				 newGame.p[Board.selected] = newGame.holder;
				 newGame.changeTurn();
				 newGame.turnUp();
				 Board.selected = -1;
				 graveCount++;
				 newGame.p[tileID].hasMoved = true;
			 }
			  
			 else{
		 
				 if (newGame.p[Board.selected].validMove(Board.selected, tileID) == false) {
					 //invalid move deselect piece
					 Board.selected = -1;
				 }
				 else {
				   //move to empty 
				   newGame.holder = newGame.p[Board.selected];
				   newGame.p[Board.selected] = newGame.p[tileID];
				   newGame.p[tileID] = newGame.holder;
				   Board.selected = -1;
				   newGame.changeTurn();
				   newGame.turnUp();
				   newGame.p[tileID].hasMoved = true;
				 }
				 
			 }
			 SwingUtilities.invokeLater(new Runnable() {
				 @Override
				 public void run() {
					 boardPanel.drawBoard(newGame);
				 }
			 });
			  
		  }

	  });
	  validate();
	  	
    }
	void drawTile(final chessGame newGame) {
		removeAll();
		assignTileColor();
		assignTileSprite();
		validate();
		repaint();
	}
	
	private void assignTileSprite() {
		JLabel sprite = new JLabel();
		sprite.setIcon(new ImageIcon("sprites\\"+newGame.p[this.tileID].getImageName()+".png"));
		add(sprite);
		validate();
	}
		
	private void assignTileColor() {
		if(highlighted[this.tileID] == true) {
			setBackground(blueTiles);
			highlighted[this.tileID] = false;
		}
		else if((this.tileID/8)% 2==0 )
		    setBackground(this.tileID %2 == 0 ? whiteTiles : blackTiles);
		else 
			setBackground(this.tileID %2 == 0 ? blackTiles : whiteTiles);
	
	}
	private void highlightTile(int selected) {
		for(int i = 0; i<64; i++) {
			if(newGame.p[selected].validMove(selected, i) == true && (newGame.p[i].getAlly()==-1 
					|| newGame.p[selected].getAlly() != newGame.p[i].getAlly())) {
			   highlighted[i] = true;
			}
	    }
	}
		
  }
	
}
