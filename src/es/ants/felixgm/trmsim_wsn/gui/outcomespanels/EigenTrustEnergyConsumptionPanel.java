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

import es.ants.felixgm.trmsim_wsn.outcomes.EigenTrustEnergyConsumptionOutcome;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;

/**
 * <p>This class represents the generic panel used to plot the energy
 * consumption of EigenTrust Model</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class EigenTrustEnergyConsumptionPanel extends EnergyConsumptionPanel {

    /** Color used to plot the pre-trusted peers energy consumption */
    protected Color preTrustedPeerEnergyColor = Color.MAGENTA;

    /**
     * Class EigenTrustEnergyConsumptionPanel constructor
     * @param outcomes Outcomes to be plotted in this outcomes panel
     */
    public EigenTrustEnergyConsumptionPanel(Collection<Outcome> outcomes) {
        super(outcomes);
    }

    /**
     * Class EigenTrustEnergyConsumptionPanel constructor
     */
    public EigenTrustEnergyConsumptionPanel() {
        super();
    }

    @Override
    protected void drawAxes(Graphics graphics) {
        int height = this.getHeight();
        int width = this.getWidth();

        graphics.drawLine(0, (int)(height*yAxisMargin), width, (int)(height*yAxisMargin));
        graphics.drawLine((int)(width*xAxisMargin), 0, (int)(width*xAxisMargin), height);

        graphics.drawString("Pre-Trusted", (int)((xAxisMargin+(1-xAxisMargin)*0.06*0.75)*width), (int)(0.5*height*(yAxisMargin+1))+5);
        graphics.drawString("Malicious", (int)((xAxisMargin+(1-xAxisMargin)*0.31*0.75)*width), (int)(0.5*height*(yAxisMargin+1))+5);
        graphics.drawString("Benevolent", (int)((xAxisMargin+(1-xAxisMargin)*0.52*0.75)*width), (int)(0.5*height*(yAxisMargin+1))+5);
        graphics.drawString("Relay", (int)((xAxisMargin+(1-xAxisMargin)*0.79*0.75)*width), (int)(0.5*height*(yAxisMargin+1))+5);
    }

    @Override
    protected void plotOutcomes(Collection<Outcome> outcomes, Graphics graphics) {
        this.outcomes = outcomes;

        int height = this.getHeight();
        int width = this.getWidth();

        clearPanel(graphics);
        drawAxes(graphics);

        if ((outcomes == null)  || (outcomes.size() == 0))
            return;

        Outcome outcome = Outcome.computeOutcomes(outcomes);
        double preTrustedPeerEnergyConsumption = ((EigenTrustEnergyConsumptionOutcome)outcome).get_preTrustedPeerEnergyConsumption();
        double maliciousServerEnergyConsumption = ((EigenTrustEnergyConsumptionOutcome)outcome).get_maliciousServerEnergyConsumption();
        double benevolentServerEnergyConsumption = ((EigenTrustEnergyConsumptionOutcome)outcome).get_benevolentServerEnergyConsumption();
        double relayServerEnergyConsumption = ((EigenTrustEnergyConsumptionOutcome)outcome).get_relayServerEnergyConsumption();
        double max = preTrustedPeerEnergyConsumption;

        if (maliciousServerEnergyConsumption > max)
            max = maliciousServerEnergyConsumption;
        if (benevolentServerEnergyConsumption > max)
            max = benevolentServerEnergyConsumption;
        if (relayServerEnergyConsumption > max)
            max = relayServerEnergyConsumption;

        int x1 = (int)((xAxisMargin+(1-xAxisMargin)*0.75)*width);
        int y1 = (int)(yAxisMargin*height*0.05);
        int w1 = Math.min((int)((xAxisMargin+(1-xAxisMargin)*0.2)*width),(int)(yAxisMargin*height*0.8));
        int h1 = w1;

        double avg = ((EigenTrustEnergyConsumptionOutcome)outcome).get_avgSensorEnergyConsumption();
        avg = 4*constant + Math.pow(avg,alpha);
        double power = Math.ceil(Math.log10(avg));
        avg = ((int)(avg/Math.pow(10, power-2)))/10.0;
        graphics.drawString(avg+"*10^"+(power-1),x1, (int)(yAxisMargin*height*0.95));

        preTrustedPeerEnergyConsumption = preTrustedPeerEnergyConsumption/max;
        maliciousServerEnergyConsumption = maliciousServerEnergyConsumption/max;
        benevolentServerEnergyConsumption = benevolentServerEnergyConsumption/max;
        relayServerEnergyConsumption = relayServerEnergyConsumption/max;

        double sum = preTrustedPeerEnergyConsumption + maliciousServerEnergyConsumption + benevolentServerEnergyConsumption + relayServerEnergyConsumption;
        int angle = (int)((preTrustedPeerEnergyConsumption/sum)*360);

        graphics.setColor(preTrustedPeerEnergyColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*0.08*0.75)*width),
                   (int)((yAxisMargin)*height*(1-preTrustedPeerEnergyConsumption)),
                   (int)((1-xAxisMargin)*0.15*0.75*width),
                   (int)((yAxisMargin)*height*preTrustedPeerEnergyConsumption));
        graphics.fillArc(x1,y1,w1,h1,0,angle);

        graphics.setColor(maliciousServerEnergyColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*0.31*0.75)*width),
                   (int)((yAxisMargin)*height*(1-maliciousServerEnergyConsumption)),
                   (int)((1-xAxisMargin)*0.15*0.75*width),
                   (int)((yAxisMargin)*height*maliciousServerEnergyConsumption));
        graphics.fillArc(x1,y1,w1,h1,angle,(int)((maliciousServerEnergyConsumption/sum)*360));
        angle += (int)((maliciousServerEnergyConsumption/sum)*360);

        graphics.setColor(benevolentServerEnergyColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*0.54*0.75)*width),
                   (int)((yAxisMargin)*height*(1-benevolentServerEnergyConsumption)),
                   (int)((1-xAxisMargin)*0.15*0.75*width),
                   (int)((yAxisMargin)*height*benevolentServerEnergyConsumption));
        graphics.fillArc(x1,y1,w1,h1,angle,(int)((benevolentServerEnergyConsumption/sum)*360));
        angle += (int)((benevolentServerEnergyConsumption/sum)*360);

        graphics.setColor(relayServerEnergyColor);
        graphics.fillRect((int)((xAxisMargin+(1-xAxisMargin)*0.77*0.75)*width),
                   (int)((yAxisMargin)*height*(1-relayServerEnergyConsumption)),
                   (int)((1-xAxisMargin)*0.15*0.75*width),
                   (int)((yAxisMargin)*height*relayServerEnergyConsumption));
        graphics.fillArc(x1,y1,w1,h1,angle,360-angle);
    }
}