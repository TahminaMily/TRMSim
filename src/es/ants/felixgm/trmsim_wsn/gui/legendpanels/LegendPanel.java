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

package es.ants.felixgm.trmsim_wsn.gui.legendpanels;

import es.ants.felixgm.trmsim_wsn.gui.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

/**
 * <p>This class represents the generic legend of colors for plotting a network</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class LegendPanel extends javax.swing.JPanel {

    /** Background color */
    protected Color backgroundColor = Color.WHITE;
    /** Legend characters color */
    protected Color charactersColor = Color.BLACK;
    /** Clients color */
    protected Color clientColor = Color.ORANGE;
    /** Benevolent servers color */
    protected Color benevolentServerColor = Color.GREEN;
    /** Malicious servers color */
    protected Color maliciousServerColor = Color.RED;
    /** Relay servers color */
    protected Color relayServerColor = Color.BLUE;
    /** Idle sensors color*/
    protected Color idleSensorColor = Color.BLACK;
    /** Set of legend elements */
    private Vector<LegendElement> legendElements;

    /** Creates new form LegendPanel */
    public LegendPanel() {
        initComponents();
        init();
        addLegendElement("Client",clientColor);
        addLegendElement("Benevolent",benevolentServerColor);
        addLegendElement("Malicious",maliciousServerColor);
        addLegendElement("Relay",relayServerColor);
        addLegendElement("Idle",idleSensorColor);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        plotLegend(graphics);
    }

    /**
     * This method plots this legend
     */
    public void plotLegend() {
        plotLegend(this.getGraphics());
    }

    /**
     * This method plots this legend within the given graphics object
     * @param graphics Graphics object where to plot this legend
     */
    protected void plotLegend(Graphics graphics) {
        int height = this.getHeight();
        int width = this.getWidth();

        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);

        for (int i = 0; i < legendElements.size(); i++) {
            graphics.setColor(legendElements.get(i).getColor());
            graphics.fillArc(5, (int)(height*((i+1)/(double)(legendElements.size()+1)))-5, 10, 10, 0, 360);
            graphics.setColor(charactersColor);
            graphics.drawString(legendElements.get(i).getLabel(),20,(int)(height*((i+1)/(double)(legendElements.size()+1)))+5);
        }
    }

    /**
     * This method initializes the legend elements set
     */
    protected void init() {
        legendElements = new Vector<LegendElement>();
    }

    /**
     * This method adds a new legend element in the legend elements set
     * @param label Label of the new legend element to be added
     * @param color Color of the new legend element to be added
     */
    protected void addLegendElement(String label, Color color) {
        legendElements.add(new LegendElement(label,color));
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

/**
 * <p>This class represents a generic legend element composed by a label together
 * with its corresponding color</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
class LegendElement {
    /** Label of the legend element */
    private String label;
    /** Color of the legend element */
    private Color color;

    /**
     * Class LegendElement constructor
     * @param label Label of the legend element
     * @param color Color of the legend element
     */
    public LegendElement(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    /**
     * This method returns the label of this legend element
     * @return The label of this legend element
     */
    public String getLabel() { return label; }
    /**
     * This method returns the color of this legend element
     * @return The color of this legend element
     */
    public Color getColor() { return color; }
}