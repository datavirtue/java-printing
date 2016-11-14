/*
 * DymoPrinter.java
 *
 * Created on April 14, 2007, 3:03 PM
 *
 *


 */


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.text.AttributedString;
import java.util.Vector;
//import com.datavirtue.tools.DV;
import java.awt.print.Paper;

/**
 *
 * @author Sean Kristen Anderson ~ Data Virtue 5-26-2007
 *
 * This class is public domain.  No restrictions.

 * This class is not perfect, if you fix it before I do please send me the updated version.
 * There are alignment issues and I have not tested this on a DYMO printer or Co Star.

 * there are two classes in here, one is the Printable


 *  email: seanka@datavirtue.com with questions or updates
 */

public class DymoPrinter {

    /* instance vars */

    private PageFormat pgFormat = new PageFormat();

    private Book labels = new Book();

    private boolean prompt = true;


    /* Create a new instance of DymoPrinter */

    public DymoPrinter(double W, double H, boolean prompt) {


		/* In my application that I used this in I swapped the actual Height and Width
		   See the main() for an example.
		   I spent a lot of time on this and was not able to get any help in several forums.
		   I started by following directions and docs then I just started trying crazy stuff until it
		   output somthing close to what I needed.

		   */

        this.prompt = prompt;

        Paper p = new Paper();

        W = W * 72;
        H = H * 72;

        p.setSize(W, H);

        p.setImageableArea(20, 20, W ,H);

        pgFormat.setPaper(p);


        pgFormat.setOrientation(PageFormat.LANDSCAPE);


    }


    /* Slam in some text */
    public void addLabel (String [] text) {

        labels.append(new DymoLabel(text), pgFormat);

    }

    public void addLabel (Printable p) {

        labels.append(p, pgFormat);

    }

    public void go () {

        /* Print the Book */
        PrinterJob printJob = PrinterJob.getPrinterJob();

        printJob.setPageable(labels);  //contains all pgFormats


        boolean doJob = true;

        if (prompt){

            doJob = printJob.printDialog();

        }

        if (doJob) {

            try {


                printJob.print();

             }catch (Exception PrintException) {

                PrintException.printStackTrace();

             }

        }




    }


    public static void main(String[] args) {


        /*EXAMPLE - TESTER*/
        DymoPrinter dp = new DymoPrinter(1.12, 3.50, true);  //Regular DYMO ~ Co Star LabelWriter Labels '30252'

        dp.addLabel(new String [] {"First Last", "Data Virtue", "1234 Main Street", "Hillsboro, OH  45133"});

        dp.go();


    }

}//END DYMOPRINTER CLASS


class DymoLabel implements Printable {

    /** Creates a new instance of DymoLabel */
    public DymoLabel(String [] text) {

        this.text = text;

    }

    private String [] text = new String [] {"", ""};


    public int print(Graphics g, PageFormat pageFormat, int page) {

      //--- Create the Graphics2D object
      Graphics2D g2d = (Graphics2D) g;

      //--- Translate the origin to 0,0 for the top left corner
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

      //--- Set the default drawing color to black
      g2d.setPaint(Color.black);

      //--- Print the title

      Font font = new Font("helvetica", Font.PLAIN, 12);

      g2d.setFont(font);


      //--- Compute the horizontal center of the page
      FontMetrics fontMetrics = g2d.getFontMetrics();

      double X = 0;//pageFormat.getImageableX();

      double fh = fontMetrics.getHeight();

      double Y = pageFormat.getImageableY();

      /*
      System.out.println("Page Height " + pageFormat.getHeight());
      System.out.println("Imageable X " + pageFormat.getImageableX());
      System.out.println("Imageable Y " + pageFormat.getImageableY());
      System.out.println("Page Orientation " + pageFormat.getOrientation());
      System.out.println("Landscape = " + PageFormat.LANDSCAPE);
*/


      java.awt.geom.AffineTransform at = new java.awt.geom.AffineTransform(pageFormat.getMatrix());

          //at.setToRotation(-Math.PI/2.0);  ???????????????????

          g2d.setTransform(at);


		g2d.setClip(0, 0, 252, 90);

		/* Don't ask me how but I got this to work, sort of. */


      for (int i = 0; i < text.length; i++){


          g2d.drawString(text[i], (float) X , (float) Y);

          Y += fh;

      }

      return (PAGE_EXISTS);
    }


  }




