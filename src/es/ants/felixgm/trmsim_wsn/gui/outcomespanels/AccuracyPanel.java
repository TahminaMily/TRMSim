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

package es.ants.felixgm.trmsim_wsn.gui.outcomespanels;

import es.ants.felixgm.trmsim_wsn.outcomes.BasicOutcome;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;

/**
 * <p>This class represents the generic panel used to plot the accuracy of
 * every Trust and Reputation Model, measured as the percentage of benevolent
 * servers suggested and selected by the current Trust and Reputation Model</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.4
 */
public class AccuracyPanel extends OutcomesPanel {
    /** Color used to plot the current accuracy of each outcome */
    protected Color currentValueColor = Color.green;
    /** Color used to plot the average accuracy of all the outcomes */
    protected Color averageValueColor = Color.red;

    /**
     * Class AccuracyPanel constructor
     * @param outcomes Outcomes to be plotted in this outcomes panel
     */
    public AccuracyPanel(Collection<Outcome> outcomes) {
        super(outcomes);
        initComponents();
    }

    /**
     * Class AccuracyPanel constructor
     */
    public AccuracyPanel() {
        super("Accuracy");
        initComponents();
    }

    @Override
    protected void drawAxes(Graphics graphics) {
        int height = this.getHeight();
        int width = this.getWidth();

        graphics.drawLine(0, (int)(height*yAxisMargin), width, (int)(height*yAxisMargin));
        graphics.drawLine((int)(width*xAxisMargin), 0, (int)(width*xAxisMargin), height);

        graphics.drawString("20", 1, (int)(height*yAxisMargin*0.8)+5);
        graphics.drawString("40", 1, (int)(height*yAxisMargin*0.6)+5);
        graphics.drawString("60", 1, (int)(height*yAxisMargin*0.4)+5);
        graphics.drawString("80", 1, (int)(height*yAxisMargin*0.2)+5);
        //graphics.drawString("100", 0, (int)(height*yAxisMargin*0.0)+10);
        
        graphics.drawLine((int)(width*xAxisMargin)-5,(int)(height*yAxisMargin*0.8),(int)(width*xAxisMargin)+5,(int)(height*yAxisMargin*0.8));
        graphics.drawLine((int)(width*xAxisMargin)-5,(int)(height*yAxisMargin*0.6),(int)(width*xAxisMargin)+5,(int)(height*yAxisMargin*0.6));
        graphics.drawLine((int)(width*xAxisMargin)-5,(int)(height*yAxisMargin*0.4),(int)(width*xAxisMargin)+5,(int)(height*yAxisMargin*0.4));
        graphics.drawLine((int)(width*xAxisMargin)-5,(int)(height*yAxisMargin*0.2),(int)(width*xAxisMargin)+5,(int)(height*yAxisMargin*0.2));
        graphics.drawLine((int)(width*xAxisMargin)-5,(int)(height*yAxisMargin*0.0),(int)(width*xAxisMargin)+5,(int)(height*yAxisMargin*0.0));

        graphics.drawString("Current: ", (int)(1.5*width*xAxisMargin*(2-xAxisMargin))+5,(int)(0.5*height*(yAxisMargin+1))+5);
        graphics.drawString("Average: ", (int)(width*0.5*(1.0-xAxisMargin))+5, (int)(0.5*height*(yAxisMargin+1))+5);

        graphics.setColor(currentValueColor);
        graphics.drawLine((int)(width*xAxisMargin*(2-xAxisMargin)),(int)(0.5*height*(yAxisMargin+1)),(int)(1.5*width*xAxisMargin*(2-xAxisMargin)),(int)(0.5*height*(yAxisMargin+1)));
        graphics.setColor(averageValueColor);
        graphics.drawLine((int)(width*0.45*(1.0-xAxisMargin)),(int)(0.5*height*(yAxisMargin+1)),(int)(width*0.5*(1.0-xAxisMargin)),(int)(0.5*height*(yAxisMargin+1)));
    }

    @Override
    protected void plotOutcomes(Collection<Outcome> outcomes, Graphics graphics) {
        this.outcomes = outcomes;

        int height = this.getHeight();
        int width = this.getWidth();
        int xIncr = ((int)(width*(1.0-xAxisMargin)))/windowsSize;
        int curr_x = (int)(width*xAxisMargin);
        int curr_y = (int)(height*yAxisMargin);
        int next_y = 0;
        int avg_y = (int)(height*yAxisMargin);
        int avg_next_y = 0;
        double avg = 0.0;
        int count = 0;
        double value = 0.0;
        Outcome lastOutcome = null;

        clearPanel(graphics);
        drawAxes(graphics);

        if ((outcomes == null)  || (outcomes.size() == 0))
            return;

        if (outcomes.size() <= windowsSize) {
            curr_x = width - outcomes.size()*xIncr;
            for (Outcome outcome : outcomes) {
                count++;
                value = ((BasicOutcome)outcome).get_avgSatisfaction();

                next_y = (int)((1.0-value)*(height*yAxisMargin));
                graphics.setColor(currentValueColor);
                graphics.drawLine(curr_x,curr_y,curr_x+xIncr,next_y);

                avg += value;
                avg_next_y = (int)((1.0-(avg/count))*(height*yAxisMargin));
                graphics.setColor(averageValueColor);
                graphics.drawLine(curr_x,avg_y,curr_x+xIncr,avg_next_y);

                curr_x += xIncr;
                curr_y = next_y;
                avg_y = avg_next_y;
                lastOutcome = outcome;
            }
        } else {
            for (Outcome outcome : outcomes) {
                count++;

                value = ((BasicOutcome)outcome).get_avgSatisfaction();
                avg += value;
                if (count <= (outcomes.size()-windowsSize)) {
                    curr_y = (int)((1.0-value)*(height*yAxisMargin));
                    avg_y = (int)((1.0-(avg/count))*height*yAxisMargin);
                    continue;
                }

                next_y = (int)((1.0-value)*(height*yAxisMargin));
                graphics.setColor(currentValueColor);
                graphics.drawLine(curr_x,curr_y,curr_x+xIncr,next_y);

                avg_next_y = (int)((1.0-(avg/count))*height*yAxisMargin);
                graphics.setColor(averageValueColor);
                graphics.drawLine(curr_x,avg_y,curr_x+xIncr,avg_next_y);

                curr_x += xIncr;
                curr_y = next_y;
                avg_y = avg_next_y;
                lastOutcome = outcome;
            }
        }

        graphics.setColor(axesColor);
        if (lastOutcome != null) {
            value = ((int)(((BasicOutcome)lastOutcome).get_avgSatisfaction()*10000))/100.0;
            graphics.drawString("Current: "+value+" %", (int)(1.5*width*xAxisMargin*(2-xAxisMargin))+5,(int)(0.5*height*(yAxisMargin+1))+5);
            graphics.drawString("Average: "+(int)((avg/count)*10000)/100.0+" %", (int)(width*0.5*(1.0-xAxisMargin))+5, (int)(0.5*height*(yAxisMargin+1))+5);
        } else {
            graphics.drawString("Current: ", (int)(1.5*width*xAxisMargin*(2-xAxisMargin))+5,(int)(0.5*height*(yAxisMargin+1))+5);
            graphics.drawString("Average: ", (int)(width*0.5*(1.0-xAxisMargin))+5, (int)(0.5*height*(yAxisMargin+1))+5);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}