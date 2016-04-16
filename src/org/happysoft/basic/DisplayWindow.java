
package org.happysoft.basic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.JFrame;

/**
 * @author Chris
 */
public class DisplayWindow extends JFrame {
  
  private Color background = Color.WHITE;
  private Color foreground = Color.BLACK;
  
  private boolean isOpen = false;
  private boolean isMoving = false;
  private Graphics g;
  private Font font;
  private FontMetrics fontMetrics; 
  private int printx = 0, printy = 10;
  private int plotx = 0, ploty = 0;
  private int width, height, fontHeight;
  
  private boolean isKeyPressed = false;
  private char lastKey;
  
  private final Program context;
  
  public DisplayWindow(Program context) {
    this.context = context;
  }
  
  public void openWindow(final String name, final int width, final int height) {
    this.width = width;
    this.height = height;
    //Create and set up the window.
    this.setTitle(name);
    this.setSize(width, height);
    this.getContentPane().setBackground(background);
    this.getContentPane().setForeground(foreground);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    if(!isOpen) {
      isOpen = true;
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          showWindow();
        }
      });
      try {
        System.out.println("wait");
        synchronized(context) {
          context.wait();
        }
      } catch(InterruptedException ie) {
        System.out.println("hmm");
      }
    }
  }
  
  public void drawString(String string) {
    String[] parts = string.split("\n");
    if (g != null) {
      for(String s : parts) {
        int fwidth = fontMetrics.stringWidth(s);
        int fheight = fontHeight;
        g.setColor(background);
        g.fillRect(printx, printy-fheight, fwidth, fheight);
        g.setColor(foreground);
        g.drawString(s, printx, printy);
        printy += fontHeight;
      }
    } else {
      System.out.println("not got me graphics yet");
    }
  }
  
  public void plot(int x, int y) {
    plotx = x; ploty = y;
  }
  
  public void at(int x, int y) {
    printx = x; printy = y;
  }
  
  public void setInk(int r, int g, int b) {
    this.foreground = new Color(r, g, b);
  }
  
  public void setPaper(int r, int g, int b) {
    this.background = new Color(r, g, b);
  }
  
  public boolean isKeyPressed() {
    return isKeyPressed;
  }
  
  public char getLastKey() {
    return lastKey;
  }
  
  public void drawEllipse(int width, int height) {
    if (g != null) {
      //g.setXORMode(Color.WHITE);
      g.setColor(foreground);
      g.drawOval(plotx-width/2, ploty-height/2, width, height);
      g.setPaintMode();
    }
  }
  
  public void drawLine(int x, int y) {
    if (g != null) {
      g.setXORMode(background);
      g.setColor(foreground);
      g.drawLine(plotx, ploty, x, y);
      g.setPaintMode();
    }
  }
  
  public void cls() {
    g.setColor(background);
    g.fillRect(0, 0, width, height);
    printx = 0; printy = 10;
    g.setColor(foreground);
  }
  
  
  
  private void showWindow() {
    this.addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        isKeyPressed = true;
        lastKey = e.getKeyChar();
      }

      @Override
      public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        isKeyPressed = false;
      }

      @Override
      public void keyTyped(KeyEvent e) {
        super.keyTyped(e);
        System.out.println("Key typed: " + e.paramString());
        lastKey = e.getKeyChar();
      }
      
    });
    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentMoved(ComponentEvent e) {
        super.componentMoved(e);
        isMoving = true;
        System.out.println("Moving");
//        synchronized(context) {
//          try {
//            context.wait();
//          } catch (InterruptedException ie) {
//            System.out.println("Hmm..");
//          }
//        }
      }
    });
    
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if(isMoving) {
          isMoving = false;
          System.out.println("Notfiy release");
//          synchronized(context) {
//            context.notify();
//          }
        }
      }     
    });
            
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        context.stop();
      }

      @Override
      public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        System.out.println("window opened: notify");
        synchronized(context) {
          context.notify();        
        }
      }

    });

    this.setVisible(true);
    g = this.getContentPane().getGraphics();
    font = g.getFont();
    fontMetrics = g.getFontMetrics(font);
    fontHeight = fontMetrics.getHeight();
  }
  
  public void finish() {
    this.setVisible(false);
    this.dispose();
  }

}
