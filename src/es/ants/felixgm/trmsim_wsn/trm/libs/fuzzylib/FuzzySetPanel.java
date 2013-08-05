/**
 *  "TRMSim-WSN, Trust and Reputation Models Simulator for Wireless
 * Sensor Networks" is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version always keeping
 * the additional terms specified in this license.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 *
 * Additional Terms of this License
 * --------------------------------
 *
 * 1. It is Required the preservation of specified reasonable legal notices
 *   and author attributions in that material and in the Appropriate Legal
 *   Notices displayed by works containing it.
 *
 * 2. It is limited the use for publicity purposes of names of licensors or
 *   authors of the material.
 *
 * 3. It is Required indemnification of licensors and authors of that material
 *   by anyone who conveys the material (or modified versions of it) with
 *   contractual assumptions of liability to the recipient, for any liability
 *   that these contractual assumptions directly impose on those licensors
 *   and authors.
 *
 * 4. It is Prohibited misrepresentation of the origin of that material, and it is
 *   required that modified versions of such material be marked in reasonable
 *   ways as different from the original version.
 *
 * 5. It is Declined to grant rights under trademark law for use of some trade
 *   names, trademarks, or service marks.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (lgpl.txt).  If not, see <http://www.gnu.org/licenses/>
*/

package es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * <p>This class models a panel used to plot fuzzy sets</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class FuzzySetPanel extends JPanel {

    protected Variable variable;
    protected Collection<LinguisticTerm> linguisticTerms;
    protected Collection<double[][]> data;

    protected double universeMin;
    protected double universeMax;

    /** The default panel width. */
    public static final int DEFAULT_WIDTH = 680;
    /** The default panel height. */
    public static final int DEFAULT_HEIGHT = 420;
    /** The default limit below which chart scaling kicks in. */
    public static final int DEFAULT_MINIMUM_DRAW_WIDTH = 300;
    /** The default limit below which chart scaling kicks in. */
    public static final int DEFAULT_MINIMUM_DRAW_HEIGHT = 200;
    /** The default limit above which chart scaling kicks in. */
    public static final int DEFAULT_MAXIMUM_DRAW_WIDTH = 1024;
    /** The default limit above which chart scaling kicks in. */
    public static final int DEFAULT_MAXIMUM_DRAW_HEIGHT = 768;
    /** The minimum width for drawing a chart (uses scaling for smaller widths). */
    private int minimumDrawWidth = DEFAULT_MINIMUM_DRAW_WIDTH;
    /** The minimum height for drawing a chart (uses scaling for smaller heights). */
    private int minimumDrawHeight = DEFAULT_MINIMUM_DRAW_HEIGHT;
    /** The maximum width for drawing a chart (uses scaling for bigger widths). */
    private int maximumDrawWidth = DEFAULT_MAXIMUM_DRAW_WIDTH;
    /** The maximum height for drawing a chart (uses scaling for bigger heights). */
    private int maximumDrawHeight = DEFAULT_MAXIMUM_DRAW_HEIGHT;
    /** The scale factor used to draw the chart. */
    private double scaleX;
    /** The scale factor used to draw the chart. */
    private double scaleY;

    private Paint[] paintSequence = new Paint[] {
            Color.GREEN,
            Color.ORANGE,
            Color.BLUE,
            Color.RED,
            Color.BLACK,
            new Color(0xFF, 0x55, 0x55),
            new Color(0x55, 0x55, 0xFF),
            new Color(0x55, 0xFF, 0x55),
            new Color(0xFF, 0xFF, 0x55),
            new Color(0xFF, 0x55, 0xFF),
            new Color(0x55, 0xFF, 0xFF),
            Color.pink,
            Color.gray,
            Color.darkGray,
            Color.lightGray,
        };

    public FuzzySetPanel() {
        super();
        variable = null;
        linguisticTerms = null;
        data = new Vector<double[][]>();
    }

    public FuzzySetPanel(Variable variable) {
        super();
        this.variable = variable;
        linguisticTerms = null;
        data = new Vector<double[][]>();
        if (variable != null) {
            Defuzzifier defuzzifier = variable.getDefuzzifier();
            if (defuzzifier != null) {
                double[][] defuzzifierValues = new double[2][Defuzzifier.DEFAULT_NUMBER_OF_POINTS];
                universeMin = defuzzifier.getMin();
                universeMax = defuzzifier.getMax();
                double xx = universeMin;
                double step = (universeMax - universeMin) / ((double) Defuzzifier.DEFAULT_NUMBER_OF_POINTS);
                for (int i = 0; i < Defuzzifier.DEFAULT_NUMBER_OF_POINTS; i++, xx += step) {
                    defuzzifierValues[0][i] = xx;
                    defuzzifierValues[1][i] = defuzzifier.getValue(i);
                }
                data.add(defuzzifierValues);
            } else if (variable.getLinguisticTerms() != null) {
                linguisticTerms = variable.getLinguisticTerms();
                for (LinguisticTerm linguisticTerm : linguisticTerms) {
                    double[][] linguisticTermValues = new double[2][FuzzySetWindow.DEFAULT_CHART_NUMBER_OF_POINTS];
                    int numberOfPoints = FuzzySetWindow.DEFAULT_CHART_NUMBER_OF_POINTS;
                    universeMin = Math.min(universeMin, variable.getUniverseMin());
                    universeMax = Math.max(universeMax, variable.getUniverseMax());
                    double step = (universeMax - universeMin) / ((double) numberOfPoints);
                    double xx = universeMin;
                    for (int i = 0; i < numberOfPoints; i++, xx += step) {
                        linguisticTermValues[0][i] = xx;
                        linguisticTermValues[1][i] = linguisticTerm.membership(xx);
                    }
                    data.add(linguisticTermValues);
                }
            }
        }
    }

    public FuzzySetPanel(Collection<LinguisticTerm> linguisticTerms) {
        super();
        variable = null;
        this.linguisticTerms = linguisticTerms;
        data = new Vector<double[][]>();
        for (LinguisticTerm linguisticTerm : linguisticTerms) {
            double[][] linguisticTermValues = new double[2][FuzzySetWindow.DEFAULT_CHART_NUMBER_OF_POINTS];
            int numberOfPoints = FuzzySetWindow.DEFAULT_CHART_NUMBER_OF_POINTS;
            universeMin = Math.min(universeMin, linguisticTerm.get_membershipFunction().getUniverseMin());
            universeMax = Math.max(universeMax, linguisticTerm.get_membershipFunction().getUniverseMax());
            double step = (universeMax - universeMin) / ((double) numberOfPoints);
            double xx = universeMin;
            for (int i = 0; i < numberOfPoints; i++, xx += step) {
                linguisticTermValues[0][i] = xx;
                linguisticTermValues[1][i] = linguisticTerm.membership(xx);
            }
            data.add(linguisticTermValues);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        /////Chartpanel.paintComponent()
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        Dimension size = getSize();
        Insets insets = getInsets();
        Rectangle2D available = new Rectangle2D.Double(insets.left, insets.top,
                size.getWidth() - insets.left - insets.right,
                size.getHeight() - insets.top - insets.bottom);

        // work out if scaling is required...
        boolean scale = false;
        double drawWidth = available.getWidth();
        double drawHeight = available.getHeight();
        this.scaleX = 1.0;
        this.scaleY = 1.0;

        if (drawWidth < this.minimumDrawWidth) {
            this.scaleX = drawWidth / this.minimumDrawWidth;
            drawWidth = this.minimumDrawWidth;
            scale = true;
        }
        else if (drawWidth > this.maximumDrawWidth) {
            this.scaleX = drawWidth / this.maximumDrawWidth;
            drawWidth = this.maximumDrawWidth;
            scale = true;
        }

        if (drawHeight < this.minimumDrawHeight) {
            this.scaleY = drawHeight / this.minimumDrawHeight;
            drawHeight = this.minimumDrawHeight;
            scale = true;
        }
        else if (drawHeight > this.maximumDrawHeight) {
            this.scaleY = drawHeight / this.maximumDrawHeight;
            drawHeight = this.maximumDrawHeight;
            scale = true;
        }

        Rectangle2D chartArea = new Rectangle2D.Double(0.0, 0.0, drawWidth, drawHeight);

        AffineTransform saved = g2.getTransform();
        g2.translate(insets.left, insets.top);
        if (scale) {
            AffineTransform st = AffineTransform.getScaleInstance(
                    this.scaleX, this.scaleY);
            g2.transform(st);
        }

            ////JFreeChart.draw()
            Shape savedClip = g2.getClip();
            g2.clip(chartArea);

            g2.addRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

            // draw the chart background...
            g2.setPaint(UIManager.getColor("Panel.background"));
            g2.setPaint(Color.WHITE);
            g2.fill(chartArea);

            // draw the title and subtitles...
            Rectangle2D nonTitleArea = new Rectangle2D.Double();
            nonTitleArea.setRect(chartArea);
            /*final double w = nonTitleArea.getWidth();
            final double h = nonTitleArea.getHeight();
            final double l = calculateLeftInset(w);
            final double r = calculateRightInset(w);
            final double t = calculateTopInset(h);
            final double b = calculateBottomInset(h);
            nonTitleArea.setRect(nonTitleArea.getX() + l, nonTitleArea.getY() + t, w - l - r, h - t - b);*/

            // draw the title and subtitles...

            // draw the plot (axes and data visualisation)
            Rectangle2D plotArea = nonTitleArea;
            
                //XYPlot.draw()
                Rectangle2D area = plotArea;
                final double width = area.getWidth();
                final double height = area.getHeight();
                final double left = area.getWidth()*0.1;
                final double right = 0.0;
                final double top = 0.0;
                final double bottom = area.getHeight()*0.1;
                area.setRect(area.getX() + left, area.getY() + top, width - left - right, height - top - bottom);

                /*AxisSpace space = calculateAxisSpace(g2, area);
                Rectangle2D dataArea = space.shrink(area, null);*/
                Rectangle2D dataArea = new Rectangle2D.Double();
                /*dataArea.setRect(area.getX() + 0,
                                 area.getY() + 0,
                                 area.getWidth() - 0,
                                 area.getHeight() - 0);*/

                dataArea = area;


                Shape originalClip = g2.getClip();
                Composite originalComposite = g2.getComposite();

                g2.clip(dataArea);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));

                // render data items...
                render(g2, dataArea);

                g2.setClip(originalClip);
                g2.setComposite(originalComposite);

                    //drawOutline
                    g2.setStroke(new BasicStroke(0.5f));
                    g2.setPaint(Color.gray);
                    g2.draw(dataArea);
                //XYPlot.draw()

                // draw the plot background and axes...
                int numTics = 10;
                drawBackground(g2, dataArea, numTics);
                drawAxes(g2, dataArea);
                drawTics(g2,area,plotArea,numTics);

            g2.setClip(savedClip);
            ////JFreeChart.draw()

        /////Chartpanel.paintComponent()
        g2.setTransform(saved);
        g2.dispose();
    }

    protected void drawBackground(Graphics2D g2, Rectangle2D area, int numTics) {
        fillBackground(g2, area);
        drawQuadrants(g2, area, numTics);
    }

    protected void fillBackground(Graphics2D g2, Rectangle2D area) {
        GradientPaint gradientPaint = new GradientPaint((float) area.getCenterX(),
                    (float) area.getMaxY(), Color.WHITE,
                    (float) area.getCenterX(), (float) area.getMinY(),
                    Color.LIGHT_GRAY);
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.setPaint(gradientPaint);
        //g2.fill(area);
        g2.setComposite(originalComposite);

    }

    protected double valueToJava2D_TB(double value, Rectangle2D area) {
        double axisMin = 0.0;
        double axisMax = 1.0;

        double min = 0.0;
        double max = 0.0;
        min = area.getX();
        max = area.getMaxX();
        return min + ((value - axisMin) / (axisMax - axisMin)) * (max - min);
    }

    protected double valueToJava2D_LR(double value, Rectangle2D area) {
        double axisMin = 0.0;
        double axisMax = 1.0;

        double min = 0.0;
        double max = 0.0;
        max = area.getMinY();
        min = area.getMaxY();
        return min + ((value - axisMin) / (axisMax - axisMin)) * (max - min);
    }


    protected void drawQuadrants(Graphics2D g2, Rectangle2D area, int numTics) {

        Line2D[] horizontal = new Line2D[numTics];
        Line2D[] vertical = new Line2D[numTics];

        g2.setPaint(Color.lightGray);
        g2.setStroke(new BasicStroke(1.0f));
        for (int i = 1; i < numTics; i++) {
            horizontal[i] = new Line2D.Double(area.getX(), area.getMinY()+i*(area.getMaxY()/numTics),
                area.getMaxX(), area.getMinY()+i*(area.getMaxY()/numTics));
            vertical[i] = new Line2D.Double(area.getMinX()+i*((area.getMaxX()-area.getMinX())/numTics), area.getY(),
                    area.getMinX()+i*((area.getMaxX()-area.getMinX())/numTics), area.getMaxY());
            g2.draw(horizontal[i]);
            g2.draw(vertical[i]);
        }
    }

    protected void drawTics(Graphics2D g2, Rectangle2D area, Rectangle2D plotArea, int numTics) {

        Line2D[] horizontal = new Line2D[numTics];
        Line2D[] vertical = new Line2D[numTics];

        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(1.0f));
        for (int i = 1; i < numTics; i++) {
            horizontal[i] = new Line2D.Double(
                    area.getX()-5,
                    area.getMinY()+i*(area.getMaxY()/numTics),
                    area.getX()+5,
                    area.getMinY()+i*(area.getMaxY()/numTics));
            vertical[i] = new Line2D.Double(
                    area.getMinX()+i*((area.getMaxX()-area.getMinX())/numTics),
                    area.getMaxY()-5,
                    area.getMinX()+i*((area.getMaxX()-area.getMinX())/numTics),
                    area.getMaxY()+5);
            g2.draw(horizontal[i]);
            g2.draw(vertical[i]);
            double ticLabelValue = ((int)(i*((universeMax-universeMin)/numTics)*100))/100.0;
            //yTics
            g2.drawString(String.valueOf(ticLabelValue), (float)(area.getX()-25), (float) (area.getMinY()+(numTics-i)*(area.getMaxY()/numTics))+3);
            //xTics
            g2.drawString(String.valueOf(ticLabelValue), (float)(area.getMinX()+i*((area.getMaxX()-area.getMinX())/numTics))-8, (float) (area.getMaxY()+20));
        }
    }

    protected void drawAxes(Graphics2D g2,
                           Rectangle2D dataArea) {
        Line2D xAxisLine = new Line2D.Double(dataArea.getX(), dataArea.getMaxY(),
                dataArea.getMaxX(), dataArea.getMaxY());
        Line2D yAxisLine = new Line2D.Double(dataArea.getMinX(), dataArea.getY(), dataArea.getMinX(),
                dataArea.getMaxY());
        g2.setPaint(Color.gray);
        g2.setStroke(new BasicStroke(1.0f));
        g2.draw(xAxisLine);
        g2.draw(yAxisLine);
    }

    protected void render(Graphics2D g2, Rectangle2D dataArea) {
        for (int series = 0; series < data.size(); series ++) {
            double[][] values = ((Vector<double[][]>)data).get(series);
            int firstItem = 1;
            int lastItem = values[0].length-1;
            for (int item = firstItem; item <= lastItem; item++) {
                drawItem(g2, dataArea, values, item, paintSequence[series]);
            }
        }
    }

    protected void drawItem(Graphics2D g2,
                         Rectangle2D dataArea,
                         double[][] values,
                         int item,
                         Paint paint) {
        // get the data point...
        double x1 = values[0][item]; //dataset.getXValue(series, item);
        double y1 = values[1][item]; //dataset.getYValue(series, item);
        if (Double.isNaN(y1) || Double.isNaN(x1)) {
            return;
        }

        double x0 = values[0][item-1]; //dataset.getXValue(series, item - 1);
        double y0 = values[1][item-1]; //dataset.getYValue(series, item - 1);
        if (Double.isNaN(y0) || Double.isNaN(x0)) {
            return;
        }

        /*RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
        RectangleEdge yAxisLocation = plot.getRangeAxisEdge();*/

        double transX0 = valueToJava2D_TB(x0, dataArea);
        double transY0 = valueToJava2D_LR(y0, dataArea);

        double transX1 = valueToJava2D_TB(x1, dataArea);
        double transY1 = valueToJava2D_LR(y1, dataArea);

        // only draw if we have good values
        if (Double.isNaN(transX0) || Double.isNaN(transY0)
            || Double.isNaN(transX1) || Double.isNaN(transY1)) {
            return;
        }
        Line2D workingLine = new Line2D.Double();
        workingLine.setLine(transX0, transY0, transX1, transY1);

        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
        g2.setPaint(paint);
        g2.draw(workingLine);
    }
}