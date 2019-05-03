// Author: Dan Brondyke
//
// Program: Starship Bridge Console Version 1.0
//
// Timestamps:
// Version 1.0: 5/3/2019
//
// Serialization Options:
// I chose to not add any serialization options (-4 points, I know). But I did so because I could not find a logical
// reason to put them into this type of program besides the points. I hope you understand and are not upset by this.
// I am not upset by the point loss and intend to present to make up for my decision.
//
// Purpose:
// A bridge console for science, engineering, and communications for my sci-fi Fate tabletop RPG with friends.
// Life Panel - A series of different colored dots, the green dot means life and pressing scan will tell you where it is found.
// Engineering Panel - Energy cells placed into Engines, Shields, and Phasers. Press the buttons to add energy.
// Communications Panel - A dial that when spinned will change the frequency of communications. (See info panel for connection)
// 
// What to add in future versions:
// JMenu option to re-scan (create a new list of points to scan)
// JMenu option to select what to scan for, then when the scan button is pressed, it scans for different things that are different colors
// Add a deplete button or key event that empties all the energy from the 3 options
// Add a power overload "issue", if engineering puts max power in all 3 options, the batteries overload and the buttons are inactive on a timer for a minute
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

// This will be the basis for our different energy cells.
class Rectangle {
    private int width;
    private int height;
    private int x, y;
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        if (width < 0) {
            this.width = 0;
        } else {
            this.width = width;
        }
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        if (height < 0) {
            this.height = 0;
        } else {
            this.height = height;
        }
    }
    public void setX(int x) {
    	this.x = x;
    }
    public void setY(int y) {
    	this.y = y;
    }
    public int getX() {
    	return x;
    }
    public int getY() {
    	return y;
    }
    public Rectangle(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
    }

    @Override
    public String toString() {
        return String.format("x=%d, y=%d, width=%d, height=%d",x,y,width,height);
    }
}

//Point class acts as the different scan-able things. Green is life.
class Point {
 private int x;
 private int y;
 private int col;
 private int size;
 private int prevPoint;
 
 public int getX() {
     return x;
 }
 public void setX(int x) {
     this.x = x;
 }
 public int getY() {
     return y;
 }
 public void setY(int y) {
     this.y = y;
 }
 public void setColor(int col) {
 	this.col = col;
 }
 public int getColor() {
 	return col;
 }
 public void setSize(int size) {
 	this.size = size;
 }
 public int getSize() {
 	return size;
 }
 public void setPrevious(int prevPoint) {
 	this.prevPoint = prevPoint;
 }
 public int getPrevious() {
 	return prevPoint;
 }
 public Point(int x, int y, int col, int prevPoint, int size) {
     setX(x);
     setY(y);
     setColor(col);
     setPrevious(prevPoint);
     setSize(size);
 }
 public String toString() {
     return String.format("x=%d, y=%d, color=%d, size=%d, previous=%d",x,y,col,size,prevPoint);
 }
}

//Pointer generator got some new set and get functions to set the limitations of the points that are generated.
class PointGenerator {
 private Random rnd;
 private int maxX;  // furthest right
 private int maxY;  // furthest down
 private int maxColor;
 private int maxPrev;
	private int maxSize;
 public void setMaxX(int mx) {
     maxX = mx;
 }
 public int getMaxX() {
     return maxX;
 }
 public void setMaxY(int my) {
     maxY = my;
 }
 public int getMaxY() {
     return maxY;
 }
 public void setMaxColor(int mCol) {
 	maxColor = mCol;
 }
 public int getMaxColor() {
 	return maxColor;
 }
 public void setMaxPrev(int mPrev) {
 	maxPrev = mPrev;
 }
 public int getMaxPrev() {
 	return maxPrev;
 }
	public void setMaxSize(int mSize) {
		maxSize = mSize;
	}
	public int getMaxSize() {
		return maxSize;
	}
 public PointGenerator(int maxX, int maxY, int maxColor, int maxPrev, int maxSize) {
     rnd = new Random();
     setMaxX(maxX);
     setMaxY(maxY);
     setMaxColor(maxColor);
     setMaxPrev(maxPrev);
     setMaxSize(maxSize);
 }
 public ArrayList<Point> generatePoints(int howMany) {
     ArrayList<Point> result = new ArrayList<Point>();
     for (int i = 0; i < howMany; i++) {
     	// make sure that the first point in the array list does not have a previous point
     	if (i == 0) {
     		Point p = new Point(rnd.nextInt(maxX),rnd.nextInt(maxY), rnd.nextInt(maxColor), 0, rnd.nextInt(maxSize) + 1);
             result.add(p);
     	} else {
     		Point p = new Point(rnd.nextInt(maxX),rnd.nextInt(maxY), rnd.nextInt(maxColor), rnd.nextInt(maxPrev), rnd.nextInt(maxSize) + 1);
             result.add(p);
     	}
     }
     return result;
 }
}

// Much like the point generator class, we have the energy generator
// Went to the store and bought one.
class EnergyGenerator {
	public EnergyGenerator() {
		
	}
	
	// The energy generator I bought came with an energy initializer, so that way each array list begins with
	// a point of energy
	public ArrayList<Rectangle> EnergyInitializer(int x, int y, int width, int height) {
		ArrayList<Rectangle> initializedList = new ArrayList<Rectangle>();
		
		Rectangle e = new Rectangle(x,y,width,height);
		initializedList.add(e);
		return initializedList;
	}
	
	// Finally, what good is an energy generator if it does not add more energy to one of the 3 options?
	public ArrayList<Rectangle> EnergyAdder(ArrayList<Rectangle> addMe, int x, int y, int width, int height) {
		
		Rectangle e = new Rectangle(x,y,width,height);
		addMe.add(e);
		return addMe;
		
	}
}

class DrawingPanel extends JPanel {
	private ArrayList<Point> points;
	private ArrayList<Rectangle> engines;
	private ArrayList<Rectangle> shields;
	private ArrayList<Rectangle> phasers;
    private int col;
    private Color color;
	private int prevPoint;
	private int size;
	private DrawingPanel panDrawing;
	private int dialX2, dialY2;
    
    public DrawingPanel(ArrayList<Point> points, ArrayList<Rectangle> engines, ArrayList<Rectangle> shields, ArrayList<Rectangle> phasers, int dialX2, int dialY2) {
    	
        this.points = points;
        this.engines = engines;
        this.shields = shields;
        this.phasers = phasers;
        this.dialX2 = dialX2;
        this.dialY2 = dialY2;
        
    }
    
    // allows repaint to work
    public void setDial(int dialX2, int dialY2) {
        this.dialX2 = dialX2;
        this.dialY2 = dialY2;
    }
    
    public void setEngines(ArrayList<Rectangle> engines) {
    	this.engines = engines;
    }
    
    public void setShields(ArrayList<Rectangle> shields) {
    	this.shields = shields;
    }
    
    public void setPhasers(ArrayList<Rectangle> phasers) {
    	this.phasers = phasers;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // This is the Scan Panel
        
        Point lastPoint = null;
        
        for (Point p : points) {
        	
        	// get the color of it
			// These are the color codes translated
        	if (p.getColor() == 0)
        	{
        		color = Color.BLACK;
        	} else if (p.getColor() == 1) {
        		color = Color.RED;
        	} else if (p.getColor() == 2) {
        		color = Color.GREEN;
        	} else {
        		color = Color.BLUE;
        	}
        	
        	g.setColor(color);
            g.fillOval(p.getX(), p.getY(), p.getSize(), p.getSize());
            
            if (p.getPrevious() == 1) {
                g.drawLine(lastPoint.getX()+5, lastPoint.getY()+5,
                        p.getX()+5, p.getY()+5);
            }
            
        	lastPoint = p;

        }
        
        // Engineering Panel
        g.setColor(Color.BLACK);
        g.drawString("Engines", 125, 65);
        g.drawString("Shields", 125, 95);
        g.drawString("Phasers", 125, 125);
        
        for (Rectangle e : engines) {
        	g.setColor(Color.BLUE);
        	g.fillRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        }
        
        for (Rectangle s : shields) {
        	g.setColor(Color.BLUE);
        	g.fillRect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
        }
        
        for (Rectangle ph : phasers) {
        	g.setColor(Color.BLUE);
        	g.fillRect(ph.getX(), ph.getY(), ph.getWidth(), ph.getHeight());
        }
        
        // the dial
        g.setColor(Color.GRAY);
        g.fillOval(350,50,100,100);
        g.drawOval(350,50,100,100);
        g.setColor(Color.BLACK);
        g.drawLine(400,100,dialX2,dialY2);
    }
}

class InfoPanel extends JPanel {
	private int infoType, x, y;
	public String msg;
	
	// when first created, it sets the message to welcome
	public InfoPanel(int infoType, int x, int y) {
		this.infoType = infoType;
		this.x = x;
		this.y = y;
		msg = ("Welcome to the Bridge");
	}
	
	public void setMessage(int infoType, int x, int y) {
		
		if (infoType == 1) {
			msg = ("Life found at: x - " + x + " y - " + y);
		 } else if (infoType == 2) {
			 msg = ("Ship to ICA");
		 } else if (infoType == 3) {
			 msg = ("Ship to Station");
		 } else if (infoType == 4) {
			 msg = ("Ship to Planet");
		 } else if (infoType == 5) {
			 msg = ("Ship to Ship");
		 } else if (infoType == 6) {
			 msg = ("Power to Engines");
		 } else if (infoType == 7) {
			 msg = ("Power to Shields");
		 } else if (infoType == 8) {
			 msg = ("Power to Phasers");
		 } else {
			msg = String.format("Life not found.");
		 }
	}
	
	// draw the string
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

			g.drawString(msg, 20, 10);
	
	}
	
}

class ConsoleFrame extends JFrame {
    private ArrayList<Point> points;
    private ArrayList<Rectangle> engines;
    private ArrayList<Rectangle> shields;
    private ArrayList<Rectangle> phasers;
    private int col;
	private int prevPoint;
	private int size;
	private int dialCounter = 0;
	private EnergyGenerator eGenerator;
	private int engineCounter = 0;
	private int xEngines = 205;
	private int shieldCounter = 0;
	private int xShields = 205;
	private int phaserCounter = 0;
	private int xPhasers = 205;
	
	private JButton btnSpin = new JButton("Spin");
	private JButton btnEngines = new JButton("Engines");
	private JButton btnShields = new JButton("Shields");
	private JButton btnPhasers = new JButton("Phasers");
	private JButton btnScan = new JButton("Scan");
	
	public void setupUI() {

		setTitle("Starship Bridge Console Version 1.0");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 300);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		DrawingPanel dPan = new DrawingPanel(points,engines,shields,phasers,400,50);
		c.add(dPan, BorderLayout.CENTER);
		
		InfoPanel iPan = new InfoPanel(0, 0, 0);
		c.add(iPan, BorderLayout.NORTH);
		
		// creates the buttons panel
		JPanel ButtonsPanel = new JPanel();
		
		// goes through the list of points and searches for the color green
		// if found, it sends "Life Found" if not "Life Not Found"
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				iPan.setMessage(-1, 0, 0);
				
				 for (Point p : points) {
					 if (p.getColor() == 2) {
						iPan.setMessage(1, p.getX(), p.getY());
					 }
				 }

				repaint();
			}
		});		
		ButtonsPanel.add(btnScan);
		
		// The Engineering panel buttons all operate very similarly. They take the ArrayList of each option and adds to them to the correct spot
		// they then send the info to the drawing panel and repaint onto the console.
		btnEngines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (engineCounter < 3) {
					Rectangle eng = new Rectangle(xEngines, 50, 25, 25);
					engines.add(eng);
					dPan.setEngines(engines);
					xEngines += 30;
					engineCounter++;
				} else {
					engines.clear();
					engines = eGenerator.EnergyInitializer(175, 50, 25, 25);
					dPan.setEngines(engines);
					xEngines = 205;
					engineCounter = 0;
				}
	        	
				iPan.setMessage(6,0,0);
				
				repaint();
			}
		});		
		ButtonsPanel.add(btnEngines);
		
		btnShields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (shieldCounter < 3) {
					Rectangle eng = new Rectangle(xShields, 80, 25, 25);
					shields.add(eng);
					dPan.setShields(shields);
					xShields += 30;
					shieldCounter++;
				} else {
					shields.clear();
					shields = eGenerator.EnergyInitializer(175, 80, 25, 25);
					dPan.setShields(shields);
					xShields = 205;
					shieldCounter = 0;
				}
	        	
				iPan.setMessage(7,0,0);
				
				repaint();
			}
		});		
		ButtonsPanel.add(btnShields);
		
		btnPhasers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (phaserCounter < 3) {
					Rectangle eng = new Rectangle(xPhasers, 110, 25, 25);
					phasers.add(eng);
					dPan.setPhasers(phasers);
					xPhasers += 30;
					phaserCounter++;
				} else {
					phasers.clear();
					phasers = eGenerator.EnergyInitializer(175, 110, 25, 25);
					dPan.setPhasers(phasers);
					xPhasers = 205;
					phaserCounter = 0;
				}
	        	
				iPan.setMessage(8,0,0);
				
				repaint();
			}
		});		
		ButtonsPanel.add(btnPhasers);
		
		// This controls the dial
		btnSpin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
				if (dialCounter == 0) {
					dPan.setDial(450,100); // this gets the dial turned to the right position
					iPan.setMessage(2,0,0);
					dialCounter++;
				} else if (dialCounter == 1) {
					dPan.setDial(400,150); // this gets the dial turned to the bottom position
					iPan.setMessage(3,0,0);
					dialCounter++;
				} else if (dialCounter == 2) {
					dPan.setDial(350,100); // this gets the dial turned to the left position
					iPan.setMessage(4,0,0);
					dialCounter++;
				} else {
					dPan.setDial(400,50);	// this gets the dial turned to the up position
					iPan.setMessage(5,0,0);
					dialCounter = 0;
				}
				
				
				repaint();
			}
		});
		ButtonsPanel.add(btnSpin);
		
		// adds buttons to the bottom of the screen
		c.add(ButtonsPanel, BorderLayout.SOUTH);
		
		
	}
	
	public ConsoleFrame(ArrayList<Point> points, ArrayList<Rectangle> engines, ArrayList<Rectangle> shields, ArrayList<Rectangle> phasers, int col, int prevPoint, int size, EnergyGenerator eGenerator) {
        this.points = points;
        this.col = col;
		this.size = size;
		this.prevPoint = prevPoint;	
		this.engines = engines;
		this.shields = shields;
		this.phasers = phasers;
		this.eGenerator = eGenerator;
		
		setupUI();
	}
}

public class BrondykeDanFinalProject {
	
	public static void main(String[] args) {
		
		// Initialize Everything
		PointGenerator generator = new PointGenerator(100,175, 4, 2, 25);
        ArrayList<Point> points = generator.generatePoints(5);
        ArrayList<Rectangle> engines = new ArrayList<Rectangle>();
        EnergyGenerator eGenerator = new EnergyGenerator();
        engines = eGenerator.EnergyInitializer(175, 50, 25, 25);
        ArrayList<Rectangle> shields = new ArrayList<Rectangle>();
        shields = eGenerator.EnergyInitializer(175, 80, 25, 25);
        ArrayList<Rectangle> phasers = new ArrayList<Rectangle>();
        phasers = eGenerator.EnergyInitializer(175, 110, 25, 25);

        int col = 0;
		int prevPoint = 1;
		int size = 10;
		
		ConsoleFrame cf = new ConsoleFrame(points, engines, shields, phasers, col, prevPoint, size, eGenerator);
		cf.setVisible(true);
	}

}
