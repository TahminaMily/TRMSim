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

package es.ants.felixgm.trmsim_wsn.gui;

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import java.awt.Color;
import java.awt.Graphics;

/**
 * <p>This class implements a panel for plotting a network</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class NetworkPanel extends javax.swing.JPanel {

    protected double horizontalMargin = 0.05;
    protected double verticalMargin = 0.05;

    protected Color backgroundColor = Color.WHITE;
    protected Color clientColor = Color.ORANGE;
    protected Color benevolentServerColor = Color.GREEN;
    protected Color maliciousServerColor = Color.RED;
    protected Color relayServerColor = Color.BLUE;
    protected Color idleClientColor = Color.GRAY;
    protected Color idleServerColor = Color.DARK_GRAY;
    protected Color linksColor = Color.GRAY;

    protected Network network;
    protected Service requiredService;
    protected double radioRange;
    protected boolean showRanges;
    protected boolean showLinks;
    protected boolean showTraces;
    protected boolean showIds;

    /** Creates new form NetworkPanel */
    public NetworkPanel() {
        initComponents();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        try {
            if ((network != null) && (requiredService != null))
                paintNetwork(network, requiredService, radioRange, showRanges, showLinks, showTraces, showIds, graphics);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method plots a Wireless Sensor Network using the API2D library found in
     * <a href="http://www.omerique.net/twiki/bin/view/Calcumat/ApiBidimensionalJava" target="_blank">
     * http://www.omerique.net/twiki/bin/view/Calcumat/ApiBidimensionalJava</a> (only in Spanish)
     * @param network Wireless Sensor Network to be plotted
     * @param requiredService Service requested by the clients (needed in order to paint and
     * distinguish benevolent and malicious servers)
     * @throws Exception If any error occurs while plotting a WSN
     */
    public void paintNetwork(Network network, Service requiredService,
            double radioRange, boolean showRanges, boolean showLinks,
            boolean showTraces, boolean showIds) throws Exception {
        this.network = network;
        this.requiredService = requiredService;
        this.radioRange = radioRange;
        this.showRanges = showRanges;
        this.showLinks = showLinks;
        this.showTraces = showTraces;
        this.showIds = showIds;
        paintNetwork(network, requiredService, radioRange, showRanges, showLinks, showTraces, showIds, this.getGraphics());
    }

    private void paintNetwork(Network network, Service requiredService,
            double radioRange, boolean showRanges, boolean showLinks,
            boolean showTraces, boolean showIds, Graphics graphics) throws Exception {
        int height = this.getHeight();
        int width = this.getWidth();

        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        //graphics.setColor(linksColor);
        //graphics.drawRect((int)(width*horizontalMargin), (int)(height*verticalMargin), (int)(width*(1.0-2*horizontalMargin)), (int)(height*(1.0-2*verticalMargin)));

        int radio = (int)(radioRange*Math.sqrt(Math.pow(width*(1.0-2*horizontalMargin), 2.0)+Math.pow(height*(1.0-2*verticalMargin), 2.0)));

        for (Sensor client : network.get_clients()) {
            if (client.isActive())
                graphics.setColor(clientColor);
            else
                graphics.setColor(idleClientColor);

            int x = (int)(width*horizontalMargin+(1.0-(client.getX()/Network.get_maxDistance()))*width*(1.0-2*horizontalMargin));
            int y = (int)(height*verticalMargin+(1.0-(client.getY()/Network.get_maxDistance()))*height*(1.0-2*verticalMargin));
            graphics.fillArc(x-5, y-5, 10, 10, 0, 360);
            
            if ((showRanges) && (radio > 0))
                graphics.drawArc(x-radio, y-radio, radio*2, radio*2, 0, 360);
            if ((showLinks) && (client.isActive())) {
                graphics.setColor(linksColor);
                for (Sensor sensor : client.getNeighbors())
                    if (sensor.isActive()) {
                        int x1 = (int)(width*horizontalMargin+(1.0-(sensor.getX()/Network.get_maxDistance()))*width*(1.0-2*horizontalMargin));
                        int y1 = (int)(height*verticalMargin+(1.0-(sensor.getY()/Network.get_maxDistance()))*height*(1.0-2*verticalMargin));
                        graphics.drawLine(x, y, x1, y1);
                        if (showTraces) {
                            //double pheromone = ((int)(((BTRM_Sensor)client).getPheromone(sensor)*100))/100.0;
                            //graphics.drawString(String.valueOf(pheromone),WSNPanel.getPixelX(pm),WSNPanel.getPixelY(pm));
                        }
                    }
            }
            if (showIds) {
                graphics.setColor(linksColor);
                graphics.drawString(String.valueOf(client.id()),x-2,y-2);
           }
        }
        for (Sensor server : network.get_servers()) {
            graphics.setColor(relayServerColor);
            if (!server.isActive())
                graphics.setColor(idleServerColor);
            else if (server.offersService(requiredService)) {
                if (server.get_goodness(requiredService) >= 0.5)
                    graphics.setColor(benevolentServerColor);
                else
                    graphics.setColor(maliciousServerColor);
            }

            /*if ((((String)TRModelComboBox.getSelectedItem()).equals(EigenTrust.get_name()) &&
                    ((EigenTrust_Sensor)server).isPreTrustedPeer()) ||
                (((String)TRModelComboBox.getSelectedItem()).equals(PowerTrust.get_name()) &&
                    ((PowerTrust_Sensor)server).isPowerNode()))
                color = Color.MAGENTA;
            */
            int x = (int)(width*horizontalMargin+(1.0-(server.getX()/Network.get_maxDistance()))*width*(1.0-2*horizontalMargin));
            int y = (int)(height*verticalMargin+(1.0-(server.getY()/Network.get_maxDistance()))*height*(1.0-2*verticalMargin));
            graphics.fillArc(x-5, y-5, 10, 10, 0, 360);

            if ((showRanges) && (radio > 0))
                graphics.drawArc(x-radio, y-radio, radio*2, radio*2, 0, 360);
            if ((showLinks) && (server.isActive())) {
                graphics.setColor(linksColor);
                for (Sensor sensor : server.getNeighbors())
                    if (sensor.isActive()) {
                        int x1 = (int)(width*horizontalMargin+(1.0-(sensor.getX()/Network.get_maxDistance()))*width*(1.0-2*horizontalMargin));
                        int y1 = (int)(height*verticalMargin+(1.0-(sensor.getY()/Network.get_maxDistance()))*height*(1.0-2*verticalMargin));
                        graphics.drawLine(x, y, x1, y1);
                        if (showTraces) {
                            //double pheromone = ((int)(((BTRM_Sensor)server).getPheromone(sensor)*100))/100.0;
                            //WSNPanel.getGraphics().drawString(String.valueOf(pheromone),WSNPanel.getPixelX(pm),WSNPanel.getPixelY(pm));
                        }
                    }
            }
            if (showIds) {
                graphics.setColor(linksColor);
                graphics.drawString(String.valueOf(server.id()),x-2,y-2);
           }
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

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
