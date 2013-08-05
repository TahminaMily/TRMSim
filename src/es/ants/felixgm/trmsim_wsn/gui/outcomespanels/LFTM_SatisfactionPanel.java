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

import es.ants.felixgm.trmsim_wsn.outcomes.FuzzyOutcome;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;

/**
 * <p>This class represents the generic panel used to plot the satisfaction
 * of the clients using the LFTM model</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class LFTM_SatisfactionPanel extends OutcomesPanel {

    protected Color veryHighSatisfactionColor = Color.GREEN;
    protected Color highSatisfactionColor = Color.ORANGE;
    protected Color mediumSatisfactionColor = Color.BLUE;
    protected Color lowSatisfactionColor = Color.RED;
    protected Color veryLowSatisfactionColor = Color.DARK_GRAY;

    /** Creates new form LFTM_SatisfactionPanel */
    public LFTM_SatisfactionPanel(Collection<Outcome> outcomes) {
        super(outcomes);
        initComponents();
    }

    public LFTM_SatisfactionPanel() {
        super("Satisfaction");
        initComponents();
    }

    protected void drawAxes(Graphics graphics) {
        int height = this.getHeight();
        int width = this.getWidth();

        graphics.drawLine(0, (int)(height*yAxisMargin), width, (int)(height*yAxisMargin));
        graphics.drawLine((int)(width*xAxisMargin), 0, (int)(width*xAxisMargin), height);

        graphics.drawString("Very High", (int)((xAxisMargin+(1-xAxisMargin)*(2.0/18.0))*width), (int)(0.5*height*(yAxisMargin+1))+5);
        graphics.drawString("High", (int)((xAxisMargin+(1-xAxisMargin)*(5.5/18.0))*width), (int)(0.5*height*(yAxisMargin+1))+5);
        graphics.drawString("Medium", (int)((xAxisMargin+(1-xAxisMargin)*(8.0/18.0))*width), (int)(0.5*height*(yAxisMargin+1))+5);
        graphics.drawString("Low", (int)((xAxisMargin+(1-xAxisMargin)*(11.5/18.0))*width), (int)(0.5*height*(yAxisMargin+1))+5);
        graphics.drawString("Very Low", (int)((xAxisMargin+(1-xAxisMargin)*(14.0/18.0))*width), (int)(0.5*height*(yAxisMargin+1))+5);
    }

    protected void plotOutcomes(Collection<Outcome> outcomes, Graphics graphics) {
        this.outcomes = outcomes;

        int height = this.getHeight();
        int width = this.getWidth();

        clearPanel(graphics);
        drawAxes(graphics);

        if ((outcomes == null)  || (outcomes.size() == 0))
            return;

        Outcome outcome = Outcome.computeOutcomes(outcomes);

        double veryHighSatisfaction = ((FuzzyOutcome)outcome).getSatisfactionPercentage("Very High");
        double highSatisfaction = ((FuzzyOutcome)outcome).getSatisfactionPercentage("High");
        double mediumSatisfaction = ((FuzzyOutcome)outcome).getSatisfactionPercentage("Medium");
        double lowSatisfaction = ((FuzzyOutcome)outcome).getSatisfactionPercentage("Low");
        double veryLowSatisfaction = ((FuzzyOutcome)outcome).getSatisfactionPercentage("Very Low");

        graphics.setColor(veryHighSatisfactionColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*(2.0/18.0))*width),
                   (int)((yAxisMargin)*height*(1-veryHighSatisfaction)),
                   (int)((1-xAxisMargin)*(2.0/18.0)*width),
                   (int)((yAxisMargin)*height*veryHighSatisfaction));

        graphics.setColor(highSatisfactionColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*(5.0/18.0))*width),
                   (int)((yAxisMargin)*height*(1-highSatisfaction)),
                   (int)((1-xAxisMargin)*(2.0/18.0)*width),
                   (int)((yAxisMargin)*height*highSatisfaction));

        graphics.setColor(mediumSatisfactionColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*(8.0/18.0))*width),
                   (int)((yAxisMargin)*height*(1-mediumSatisfaction)),
                   (int)((1-xAxisMargin)*(2.0/18.0)*width),
                   (int)((yAxisMargin)*height*mediumSatisfaction));

        graphics.setColor(lowSatisfactionColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*(11.0/18.0))*width),
                   (int)((yAxisMargin)*height*(1-lowSatisfaction)),
                   (int)((1-xAxisMargin)*(2.0/18.0)*width),
                   (int)((yAxisMargin)*height*lowSatisfaction));

        graphics.setColor(veryLowSatisfactionColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*(14.0/18.0))*width),
                   (int)((yAxisMargin)*height*(1-veryLowSatisfaction)),
                   (int)((1-xAxisMargin)*(2.0/18.0)*width),
                   (int)((yAxisMargin)*height*veryLowSatisfaction));

        graphics.setColor(axesColor);
        graphics.drawString(((int)(veryHighSatisfaction*10000.0))/100.0+" %", (int)((xAxisMargin+(1-xAxisMargin)*(2.0/18.0))*width), (int)((yAxisMargin)*height*(1-veryHighSatisfaction)));
        graphics.drawString(((int)(highSatisfaction*10000.0))/100.0+" %", (int)((xAxisMargin+(1-xAxisMargin)*(5.0/18.0))*width), (int)((yAxisMargin)*height*(1-highSatisfaction)));
        graphics.drawString(((int)(mediumSatisfaction*10000.0))/100.0+" %", (int)((xAxisMargin+(1-xAxisMargin)*(8.0/18.0))*width), (int)((yAxisMargin)*height*(1-mediumSatisfaction)));
        graphics.drawString(((int)(lowSatisfaction*10000.0))/100.0+" %", (int)((xAxisMargin+(1-xAxisMargin)*(11.0/18.0))*width), (int)((yAxisMargin)*height*(1-lowSatisfaction)));
        graphics.drawString(((int)(veryLowSatisfaction*10000.0))/100.0+" %", (int)((xAxisMargin+(1-xAxisMargin)*(14.0/18.0))*width), (int)((yAxisMargin)*height*(1-veryLowSatisfaction)));
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
