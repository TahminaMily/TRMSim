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

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JPanel;

/**
 * <p>This class represents the generic panel used to plot the outcomes of
  every Trust and Reputation Model</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public abstract class OutcomesPanel extends JPanel {
    /** Outcomes to be plotted in this outcomes panel */
    protected Collection<Outcome> outcomes;
    /** Color used to plot the axes  */
    protected Color axesColor = Color.black;
    /** Background color */
    protected Color backgroundColor = Color.white;
    /** Margin of the 'X' (horizontal) axis */
    protected double xAxisMargin = 0.05;
    /** Margin of the 'Y' (vertical) axis */
    protected double yAxisMargin = 0.9;
    /** Number of the outcomes to be actually plotted (the last ones) */
    protected int windowsSize = 10;
    /** Label for this outcomes panel */
    protected String label;

    /**
     * Class OutcomesPanel constructor
     * @param outcomes Outcomes to be plotted in this outcomes panel
     */
    public OutcomesPanel(Collection<Outcome> outcomes) {
        label = "Outcomes";
        this.outcomes = outcomes;
    }

    /**
     * Class OutcomesPanel constructor
     * @param label Label for this outcomes panel
     */
    protected OutcomesPanel(String label) {
        this.label = label;
        this.outcomes = new ArrayList<Outcome>();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        plotOutcomes(outcomes,graphics);
    }

    /**
     * This method plots the given outcomes in this outcomes panel
     * @param outcomes Outcomes to be plotted in this outcomes panel
     */
    public void plotOutcomes(Collection<Outcome> outcomes) {
        plotOutcomes(outcomes, this.getGraphics());
    }

    /**
     * This method plots the given outcomes in this outcomes panel using the given graphics object
     * @param outcomes Outcomes to be plotted in this outcomes panel
     * @param graphics Graphics object where to plot the given outcomes
     */
    protected abstract void plotOutcomes(Collection<Outcome> outcomes, Graphics graphics);

    /**
     * This method plots the vertical and horizontal axes in this outcomes panel
     */
    public void drawAxes() {
        drawAxes(this.getGraphics());
    }

    /**
     * This method plots the vertical and horizontal axes in this outcomes panel using the given Graphics object
     * @param graphics Graphics object where to plot the axes of this outcomes panel
     */
    protected abstract void drawAxes(Graphics graphics);

    /**
     * This method clears this outcomes panel
     */
    public void clearPanel() {
        clearPanel(this.getGraphics());
    }

    /**
     * This method clears this outcomes panel using the given graphics object
     * @param graphics Graphics object used to clear this outcomes panel
     */
    protected void clearPanel(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        graphics.setColor(axesColor);
    }

    /**
     * This method returns the label for this outcomes panel
     * @return The label for this outcomes panel
     */
    public String getLabel() { return label; }

    /**
     * This method sets the outcomes to be plotted in this outcomes panel
     * @param outcomes Outcomes to be plotted in this outcomes panel
     */
    public void setOutcomes(Collection<Outcome> outcomes) { this.outcomes = outcomes; }
}