/*
 *  Program EdytorGraficzny - aplikacja z graficznym interfejsem
 *   - obsluga zdarzen od klawiatury, myszki i innych elementow GUI.
 *
 *  Autor: Miko³aj Brukiewicz
 *   Data: 17. 11, 2016 r.
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


abstract class Figure{

	static Random random = new Random();

	private boolean selected = false;

	public boolean isSelected() { return selected; }
	public void select() {	selected = true; }
	public void select(boolean z) { selected = z; }
	public void unselect() { selected = false; }

	protected void setColor(Graphics g) {
		if (selected) g.setColor(Color.RED);
		           else g.setColor(Color.BLACK);
	}

	public abstract boolean isInside(float px, float py);
	public boolean isInside(int px, int py) {
		return isInside((float) px, (float) py);
	}

	protected String properties() {
		String s = String.format("  Pole: %.0f  Obwod: %.0f", computeArea(), computePerimeter());
		if (isSelected()) s = s + "   [SELECTED]";
		return s;
	}

	abstract String getName();
	abstract float  getX();
	abstract float  getY();

    abstract float computeArea();
    abstract float computePerimeter();

    abstract void move(float dx, float dy);
    abstract void scale(float s);

    abstract void draw(Graphics g);

    @Override
    public String toString(){
        return getName();
    }

}


class Point extends Figure{

	protected float x, y;

	Point()
	{ this.x=random.nextFloat()*400;
	  this.y=random.nextFloat()*400;
	}

	Point(float x, float y)
	{ this.x=x;
	  this.y=y;
	}

	@Override
	public boolean isInside(float px, float py) {
		// by umozliwic zaznaczanie punktu myszka
		// miejsca odleglego nie wiecej niz 6 le wewnatrz
		return (Math.sqrt((x - px) * (x - px) + (y - py) * (y - py)) <= 6);
	}


    @Override
	String getName() {
		return "Point(" + x + ", " + y + ")";
	}

	@Override
	float getX() {
		return x;
	}

	@Override
	float getY() {
		return y;
	}

	@Override
    float computeArea(){ return 0; }

	@Override
	float computePerimeter(){ return 0; }

	@Override
    void move(float dx, float dy){ x+=dx; y+=dy; }

	@Override
    void scale(float s){ }

	@Override
    void draw(Graphics g){
		setColor(g);
		g.fillOval((int)(x-3),(int)(y-3), 6,6);
	}

    String toStringXY(){ return "(" + x + " , " + y + ")"; }

}


class Circle extends Point{
    float r;

    Circle(){
        super();
        r=random.nextFloat()*100;
    }

    Circle(float px, float py, float pr){
        super(px,py);
        r=pr;
    }

    @Override
	public boolean isInside(float px, float py) {
		return (Math.sqrt((x - px) * (x - px) + (y - py) * (y - py)) <= r);
	}

    @Override
   	String getName() {
   		return "Circle(" + x + ", " + y + ")";
   	}

    @Override
    float computeArea(){ return (float)Math.PI*r*r; }

    @Override
    float computePerimeter(){ return (float)Math.PI*r*2; }

    @Override
    void scale(float s){ r*=s; }

    @Override
    void draw(Graphics g){
    	setColor(g);
        g.drawOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
    }

}

class Elipse extends Circle{
	float r2;
	
	Elipse(){
		super();
		r2=random.nextFloat()*50;
	}
	
	Elipse(float px, float py, float pr){
		super(px,py,pr);
		r2=random.nextFloat()*50;
	}
	
	 @Override
		public boolean isInside(float px, float py) {
			return (((x-px)*(x-px))/(r*r) + ((y-py)*(y-py))/(r2*r2) <= 1);
		}
	 
	 @Override
	   	String getName() {
	   		return "Elipse(" + x + ", " + y + ")";
	   	}
	 
	 @Override
	    float computeArea(){ return (float)Math.PI*r2*r; }
	 
	 @Override
	    float computePerimeter(){ return (float)Math.PI*(((float)3 / (float) 2)*(r+r2)-(float)Math.sqrt(r*r2)); }
	 
	 @Override
	    void scale(float s){ r*=s; r2*=s; }
	 
	 @Override
	    void draw(Graphics g){
	    	setColor(g);
	        g.drawOval((int)(x-r), (int)(y-r2), (int)(2*r), (int)(2*r2));
	    }

}

class Plus extends Figure{
	Point p1, p2, p3, p11, p21, p31, p12, p22, p32, p13, p23, p33;
	int length;
	int width;
	
	Plus(){
		p1 = new Point();
		p2 = new Point();
		p3 = new Point();
		p11 = new Point();
		p21 = new Point();
		p31 = new Point();
		p12 = new Point();
		p22 = new Point();
		p32 = new Point();
		p13 = new Point();
		p23 = new Point();
		p33 = new Point();
		
		length = random.nextInt(100);
		width = length/4;
		
		p2.x = p1.x;
		p2.y = p1.y+length;
		p3.x = p1.x-length;
		p3.y = p1.y+length;
		p11.x = p1.x+width;
		p11.y = p1.y;
		p21.x = p11.x;
		p21.y = p11.y+length;
		p31.x = p11.x+length;
		p31.y = p11.y+length;
		p12.x = p3.x;
		p12.y = p3.y+width;
		p22.x = p12.x+length;
		p22.y = p12.y;
		p32.x = p22.x;
		p32.y = p22.y+length;
		p33.x = p32.x+width;
		p33.y = p32.y;
		p23.x = p33.x;
		p23.y = p33.y-length;
		p13.x = p23.x+length;
		p13.y = p23.y;
	}

	@Override
	public boolean isInside(float px, float py) {
		if(px>=p1.x && px<=p11.x)
			if(py>=p1.y && py<=p32.y)
				return true;
		if(px>=p3.x && px<= p31.x)
			if(py>=p3.y && py<=p12.y)
				return true;
		return false;
	}

	@Override
	String getName() {
		return "Plus(" + p1.toStringXY() + ", " + p2.toStringXY() + ")";
	}

	@Override
	float computeArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	float computePerimeter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	void move(float dx, float dy) {
		p1.move(dx,dy);
		p2.move(dx,dy);
		p3.move(dx,dy);
		p11.move(dx,dy);
		p12.move(dx,dy);
		p13.move(dx,dy);
		p21.move(dx,dy);
		p22.move(dx,dy);
		p23.move(dx,dy);
		p31.move(dx,dy);
		p32.move(dx,dy);
		p33.move(dx,dy);
		
	}

	@Override
	void scale(float s) {
		Point sr1 = new Point((p1.x+p2.x+p3.x+p11.x+p12.x+p13.x+p21.x+p22.x+p23.x+p31.x+p32.x+p33.x)/12,
                (p1.y+p2.y+p3.y+p11.y+p12.y+p13.y+p21.y+p22.y+p23.y+p31.y+p32.y+p33.y)/12);
		p1.x*=s; p1.y*=s;
		p2.x*=s; p2.y*=s;
		p3.x*=s; p3.y*=s;
		p11.x*=s; p11.y*=s;
		p12.x*=s; p12.y*=s;
		p13.x*=s; p13.y*=s;
		p21.x*=s; p21.y*=s;
		p22.x*=s; p22.y*=s;
		p23.x*=s; p23.y*=s;
		p31.x*=s; p31.y*=s;
		p32.x*=s; p32.y*=s;
		p33.x*=s; p33.y*=s;
		Point sr2 = new Point((p1.x+p2.x+p3.x+p11.x+p12.x+p13.x+p21.x+p22.x+p23.x+p31.x+p32.x+p33.x)/12,
                (p1.y+p2.y+p3.y+p11.y+p12.y+p13.y+p21.y+p22.y+p23.y+p31.y+p32.y+p33.y)/12);
		float dx=sr1.x-sr2.x;
		float dy=sr1.y-sr2.y;
		p1.move(dx,dy);
		p2.move(dx,dy);
		p3.move(dx,dy);
		p11.move(dx,dy);
		p12.move(dx,dy);
		p13.move(dx,dy);
		p21.move(dx,dy);
		p22.move(dx,dy);
		p23.move(dx,dy);
		p31.move(dx,dy);
		p32.move(dx,dy);
		p33.move(dx,dy);
	}

	@Override
	void draw(Graphics g) {
		setColor(g);
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		g.drawLine((int)p2.x, (int)p2.y, (int)p3.x, (int)p3.y);
		g.drawLine((int)p1.x, (int)p1.y, (int)p11.x, (int)p11.y);
		g.drawLine((int)p11.x, (int)p11.y, (int)p21.x, (int)p21.y);
		g.drawLine((int)p21.x, (int)p21.y, (int)p31.x, (int)p31.y);
		g.drawLine((int)p3.x, (int)p3.y, (int)p12.x, (int)p12.y);
		g.drawLine((int)p12.x, (int)p12.y, (int)p22.x, (int)p22.y);
		g.drawLine((int)p22.x, (int)p22.y, (int)p32.x, (int)p32.y);
		g.drawLine((int)p32.x, (int)p32.y, (int)p33.x, (int)p33.y);
		g.drawLine((int)p33.x, (int)p33.y, (int)p23.x, (int)p23.y);
		g.drawLine((int)p23.x, (int)p23.y, (int)p13.x, (int)p13.y);
		g.drawLine((int)p13.x, (int)p13.y, (int)p31.x, (int)p31.y);
		
	}

	@Override
	float getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	float getY() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

class Triangle extends Figure{
    Point point1, point2, point3;

    Triangle(){
    	point1 = new Point();
    	point2 = new Point();
    	point3 = new Point();
    }

    Triangle(Point p1, Point p2, Point p3){
        point1=p1; point2=p2; point3=p3;
    }

    @Override
    public boolean isInside(float px, float py)
    { float d1, d2, d3;
      d1 = px*(point1.y-point2.y) + py*(point2.x-point1.x) +
           (point1.x*point2.y-point1.y*point2.x);
      d2 = px*(point2.y-point3.y) + py*(point3.x-point2.x) +
           (point2.x*point3.y-point2.y*point3.x);
      d3 = px*(point3.y-point1.y) + py*(point1.x-point3.x) +
           (point3.x*point1.y-point3.y*point1.x);
      return ((d1<=0)&&(d2<=0)&&(d3<=0)) || ((d1>=0)&&(d2>=0)&&(d3>=0));
    }

    @Override
	String getName() {
    	return "Triangle{"+point1.toStringXY()+
                point2.toStringXY()+
                point3.toStringXY()+"}";
	}

	@Override
	float getX() {
		return (point1.x+point2.x+point3.x)/3;
	}

	@Override
	float getY() {
		return (point1.y+point2.y+point3.y)/3;
	}

	@Override
	float computeArea(){
        float a = (float)Math.sqrt( (point1.x-point2.x)*(point1.x-point2.x)+
                                    (point1.y-point2.y)*(point1.y-point2.y));
        float b = (float)Math.sqrt( (point2.x-point3.x)*(point2.x-point3.x)+
                                    (point2.y-point3.y)*(point2.y-point3.y));
        float c = (float)Math.sqrt( (point1.x-point3.x)*(point1.x-point3.x)+
                                    (point1.y-point3.y)*(point1.y-point3.y));
        float p=(a+b+c)/2;
        return (float)Math.sqrt(p*(p-a)*(p-b)*(p-c));
    }

	@Override
    float computePerimeter(){
        float a = (float)Math.sqrt( (point1.x-point2.x)*(point1.x-point2.x)+
                                    (point1.y-point2.y)*(point1.y-point2.y));
        float b = (float)Math.sqrt( (point2.x-point3.x)*(point2.x-point3.x)+
                                    (point2.y-point3.y)*(point2.y-point3.y));
        float c = (float)Math.sqrt( (point1.x-point3.x)*(point1.x-point3.x)+
                                    (point1.y-point3.y)*(point1.y-point3.y));
        return a+b+c;
    }

	@Override
    void move(float dx, float dy){
        point1.move(dx,dy);
        point2.move(dx,dy);
        point3.move(dx,dy);
    }

	@Override
    void scale(float s){
        Point sr1 = new Point((point1.x+point2.x+point3.x)/3,
                              (point1.y+point2.y+point3.y)/3);
        point1.x*=s; point1.y*=s;
        point2.x*=s; point2.y*=s;
        point3.x*=s; point3.y*=s;
        Point sr2 = new Point((point1.x+point2.x+point3.x)/3,
                              (point1.y+point2.y+point3.y)/3);
        float dx=sr1.x-sr2.x;
        float dy=sr1.y-sr2.y;
        point1.move(dx,dy);
        point2.move(dx,dy);
        point3.move(dx,dy);
    }

	@Override
    void draw(Graphics g){
		setColor(g);
        g.drawLine((int)point1.x, (int)point1.y,
                   (int)point2.x, (int)point2.y);
        g.drawLine((int)point2.x, (int)point2.y,
                   (int)point3.x, (int)point3.y);
        g.drawLine((int)point3.x, (int)point3.y,
                   (int)point1.x, (int)point1.y);
    }

}



class Picture extends JPanel implements KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;


	Vector<Figure> figures = new Vector<Figure>();



	/*
	 * UWAGA: ta metoda bedzie wywolywana automatycznie przy kazdej potrzebie
	 * odrysowania na ekranie zawartosci panelu
	 *
	 * W tej metodzie NIE WOLNO !!! wywolywac metody repaint()
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Figure f : figures)
			f.draw(g);
	}


    void addFigure(Figure fig)
    { for (Figure f : figures){ f.unselect(); }
      fig.select();
      figures.add(fig);
      repaint();
    }


    void moveAllFigures(float dx, float dy){
    	for (Figure f : figures){
    		if (f.isSelected()) f.move(dx,dy);
    	}
        repaint();
    }

    void scaleAllFigures(float s){
    	for (Figure f : figures)
        	{ if (f.isSelected()) f.scale(s);
        	}
          repaint();
    }

    public String toString(){
        String str = "Rysunek{ ";
        for(Figure f : figures)
            str+=f.toString() +"\n         ";
        str+="}";
        return str;
    }


    /*
     *  Impelentacja interfejsu KeyListener - obsluga zdarzen generowanych
     *  przez klawiature gdy focus jest ustawiony na ten obiekt.
     */
    public void keyPressed (KeyEvent evt)
    //Virtual keys (arrow keys, function keys, etc) - handled with keyPressed() listener.
    {  int dist;
       if (evt.isShiftDown()) dist = 10;
                         else dist = 1;
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
			moveAllFigures(0, -dist);
			break;
		case KeyEvent.VK_DOWN:
			moveAllFigures(0, dist);
			break;
		case KeyEvent.VK_LEFT:
			moveAllFigures(-dist,0);
			break;
		case KeyEvent.VK_RIGHT:
			moveAllFigures(dist,0);
			break;
		case KeyEvent.VK_DELETE:
			Iterator<Figure> i = figures.iterator();
			while (i.hasNext()) {
				Figure f = i.next();
				if (f.isSelected()) {
					i.remove();
				}
			}
			repaint();
			break;
		}
    }

   public void keyReleased (KeyEvent evt)
   {  }

   public void keyTyped (KeyEvent evt)
   //Characters (a, A, #, ...) - handled in the keyTyped() listener.
   {
     char znak=evt.getKeyChar(); //reakcja na przycisku na nacisniecie klawisza
		switch (znak) {
		case 'p':
			addFigure(new Point());
			break;
		case 'c':
			addFigure(new Circle());
			break;
		case 't':
			addFigure(new Triangle());
			break;
		case 'e' :
			addFigure(new Elipse());
			break;
		case 'l' :
			addFigure(new Plus());
			break;

		case '+':
			scaleAllFigures(1.1f);
			break;
		case '-':
			scaleAllFigures(0.9f);
			break;
		}
   }


   /*
    * Implementacja interfejsu MouseListener - obsluga zdarzen generowanych przez myszke
    * gdy kursor myszki jest na tym panelu
    */
   public void mouseClicked(MouseEvent e)
   // Invoked when the mouse button has been clicked (pressed and released) on a component.
   { int px = e.getX();
     int py = e.getY();
     for (Figure f : figures)
       { if (e.isAltDown()==false) f.unselect();
         if (f.isInside(px,py)) f.select( !f.isSelected() );
       }
     repaint();
   }

   public void mouseEntered(MouseEvent e)
   //Invoked when the mouse enters a component.
   { }

   public void mouseExited(MouseEvent e)
   //Invoked when the mouse exits a component.
   { }


	public void mousePressed(MouseEvent e)
	// Invoked when a mouse button has been pressed on a component.
	{
	}

   public void mouseReleased(MouseEvent e)
   //Invoked when a mouse button has been released on a component.
   { }

}



public class GraphicEditor extends JFrame implements ActionListener{


	private static final long serialVersionUID = 3727471814914970170L;


	private final String DESCRIPTION = "OPIS PROGRAMU\n\n" + "Aktywna klawisze:\n"
			+ "   strzalki ==> przesuwanie figur\n"
			+ "   SHIFT + strzalki ==> szybkie przesuwanie figur\n"
			+ "   +,-  ==> powiekszanie, pomniejszanie\n"
			+ "   DEL  ==> kasowanie figur\n"
			+ "   p  ==> dodanie nowego punktu\n"
			+ "   c  ==> dodanie nowego kola\n"
			+ "   t  ==> dodanie nowego trojkata\n"
			+ "	  e  ==> dodanie nowej elipsy\n"	
			+ "\nOperacje myszka:\n" + "   klik ==> zaznaczanie figur\n"
			+ "   ALT + klik ==> zmiana zaznaczenia figur\n"
			+ "   przeciaganie ==> przesuwanie figur";


	protected Picture picture;

	private JMenu[] menu = { new JMenu("Figury"),
			                 new JMenu("Edytuj"),
			                 new JMenu("Pomoc")};

	private JMenuItem[] items = { new JMenuItem("Punkt"),
			                      new JMenuItem("Kolo"),
			                      new JMenuItem("Trojkat"),
			                      new JMenuItem("Elipsa"),
			                      new JMenuItem("Wypisz wszystkie"),
			                      new JMenuItem("Przesun w gore"),
			                      new JMenuItem("Przesun w dol"),
			                      new JMenuItem("Powieksz"),
			                      new JMenuItem("Pomniejsz"),
			                      new JMenuItem("Skroty klawisowe")
			                      };

	private JButton buttonPoint = new JButton("Punkt");
	private JButton buttonCircle = new JButton("Kolo");
	private JButton buttonTriangle = new JButton("Trojkat");
	private JButton buttonElipse = new JButton("Elipsa");
	private JButton buttonPlus = new JButton("Plus");


    public GraphicEditor()
    { super ("Edytor graficzny");
      setSize(400,400);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      for (int i = 0; i < items.length; i++)
      	items[i].addActionListener(this);

      // dodanie opcji do menu "Figury"
      menu[0].add(items[0]);
      menu[0].add(items[1]);
      menu[0].add(items[2]);
      menu[0].add(items[3]);
      menu[0].addSeparator();
      menu[0].add(items[4]);

      // dodanie opcji do menu "Edytuj"
      menu[1].add(items[5]);
      menu[1].add(items[6]);
      menu[1].addSeparator();
      menu[1].add(items[7]);
      menu[1].add(items[8]);
      
      menu[2].add(items[9]);

      // dodanie do okna paska menu
      JMenuBar menubar = new JMenuBar();
      for (int i = 0; i < menu.length; i++)
      	menubar.add(menu[i]);
      setJMenuBar(menubar);

      picture=new Picture();
      picture.addKeyListener(picture);
      picture.setFocusable(true);
      picture.addMouseListener(picture);
      picture.setLayout(new FlowLayout());

      buttonPoint.addActionListener(this);
      buttonCircle.addActionListener(this);
      buttonTriangle.addActionListener(this);
      buttonElipse.addActionListener(this);
      buttonPlus.addActionListener(this);

      picture.add(buttonPoint);
      picture.add(buttonCircle);
      picture.add(buttonTriangle);
      picture.add(buttonElipse);
      picture.add(buttonPlus);

      setContentPane(picture);
      setVisible(true);
    }

	public void actionPerformed(ActionEvent evt) {
		Object zrodlo = evt.getSource();

		if (zrodlo == buttonPoint)
			picture.addFigure(new Point());
		if (zrodlo == buttonCircle)
			picture.addFigure(new Circle());
		if (zrodlo == buttonTriangle)
			picture.addFigure(new Triangle());
		if (zrodlo == buttonElipse)
			picture.addFigure(new Elipse());
		if (zrodlo == buttonPlus)
			picture.addFigure(new Plus());

		if (zrodlo == items[0])
			picture.addFigure(new Point());
		if (zrodlo == items[1])
			picture.addFigure(new Circle());
		if (zrodlo == items[2])
			picture.addFigure(new Triangle());
		if (zrodlo == items[3])
			picture.addFigure(new Elipse());
		if (zrodlo == items[4])
			JOptionPane.showMessageDialog(null, picture.toString());

		if (zrodlo == items[5])
			picture.moveAllFigures(0, -10);
		if (zrodlo == items[6])
			picture.moveAllFigures(0, 10);
		if (zrodlo == items[7])
			picture.scaleAllFigures(1.1f);
		if (zrodlo == items[8])
			picture.scaleAllFigures(0.9f);
		if (zrodlo == items[9])
			JOptionPane.showMessageDialog(null, DESCRIPTION);

		picture.requestFocus(); // przywrocenie ogniskowania w celu przywrocenia
								// obslugi zadarzen od klawiatury
		repaint();
	}

    public static void main(String[] args)
    { new GraphicEditor();
    }

}