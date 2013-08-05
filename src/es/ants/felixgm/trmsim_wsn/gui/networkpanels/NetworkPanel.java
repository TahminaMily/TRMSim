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

package es.ants.felixgm.trmsim_wsn.gui.networkpanels;

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * <p>This class implements a panel for plotting a network</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.4
 */
public class NetworkPanel extends javax.swing.JPanel {

    protected double axesMargin = 0.06;
    protected double xOrigin = 0.0;
    protected double yOrigin = 0.0;
    protected double xAxisLength = 100.0;
    protected double yAxisLength = 100.0;
    protected int numTicks = 10;

    /** Background color */
    protected Color backgroundColor = Color.WHITE;
    /** Clients color */
    protected Color clientColor = Color.ORANGE;
    /** Benevolent servers color */
    protected Color benevolentServerColor = Color.GREEN;
    /** Malicious servers color */
    protected Color maliciousServerColor = Color.RED;
    /** Relay servers color */
    protected Color relayServerColor = Color.BLUE;
    /** Idle clients color */
    protected Color idleClientColor = Color.GRAY;
    /** Idle servers color */
    protected Color idleServerColor = Color.DARK_GRAY;
    /** Links color */
    protected Color linksColor = Color.GRAY;
    /** Color used to plot the axes  */
    protected Color axesColor = Color.black;
    /** Color used to plot the grid  */
    protected Color gridColor = Color.LIGHT_GRAY;

    /** Network to be plotted */
    protected Network network;
    /** Service requested by the clients of the network */
    protected Service requiredService;
    /** Sensors radio range */
    protected double radioRange;
    /** Indicates whether to plot sensors radio ranges or not */
    protected boolean showRanges;
    /** Indicates whether to plot links between sensors or not */
    protected boolean showLinks;
    /** Indicates whether to plot sensors identifiers or not */
    protected boolean showIds;
    /** Indicates whether to plot a grid or not */
    protected boolean showGrid;

    /** Creates new form NetworkPanel */
    public NetworkPanel() {
        initComponents();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        try {
            paintNetwork(network, requiredService, radioRange, showRanges, showLinks, showIds, showGrid, graphics);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method plots a Wireless Sensor Network
     * @param network Wireless Sensor Network to be plotted
     * @param requiredService Service requested by the clients (needed in order to paint and
     * distinguish benevolent and malicious servers)
     * @param radioRange Sensors radio range
     * @param showRanges Indicates whether to plot sensors radio ranges or not
     * @param showLinks Indicates whether to plot links between sensors or not
     * @param showIds Indicates whether to plot sensors identifiers or not
     * @param showGrid Indicates whether to plot a grid or not
     * @throws Exception If any error occurs while plotting a WSN
     */
    public void paintNetwork(Network network, Service requiredService,
            double radioRange, boolean showRanges, boolean showLinks,
            boolean showIds, boolean showGrid) throws Exception {
        this.network = network;
        this.requiredService = requiredService;
        this.radioRange = radioRange;
        this.showRanges = showRanges;
        this.showLinks = showLinks;
        this.showIds = showIds;
        this.showGrid = showGrid;
        paintNetwork(network, requiredService, radioRange, showRanges, showLinks, showIds, showGrid, this.getGraphics());
    }

    /**
     * This method plots a wireless sensor
     * @param sensor Wireless sensor to be plotted
     * @param color Color of the sensor to be plotted
     * @param graphics Graphic object where to plot the sensor
     */
    protected void paintSensor(Sensor sensor, Color color, Graphics graphics) {
        int height = this.getHeight();
        int width = this.getWidth();
        int radio = (int)(radioRange*Math.sqrt(Math.pow(width*(1.0-2*axesMargin), 2.0)+Math.pow(height*(1.0-2*axesMargin), 2.0)));
        
        int x = (int)(width*axesMargin+(sensor.getX()/Network.get_maxDistance())*width*(1.0-2*axesMargin));
        int y = (int)(height*(1-axesMargin)-(sensor.getY()/Network.get_maxDistance())*height*(1.0-2*axesMargin));
        graphics.setColor(color);
        graphics.fillArc(x-5, y-5, 10, 10, 0, 360);

        if ((showRanges) && (radio > 0))
            graphics.drawArc(x-radio, y-radio, radio*2, radio*2, 0, 360);
        if ((showLinks) && (sensor.isActive())) {
            graphics.setColor(linksColor);
            for (Sensor neighbor : sensor.getNeighbors())
                if (neighbor.isActive()) {
                    int x1 = (int)(width*axesMargin+(neighbor.getX()/Network.get_maxDistance())*width*(1.0-2*axesMargin));
                    int y1 = (int)(height*(1-axesMargin)-(neighbor.getY()/Network.get_maxDistance())*height*(1.0-2*axesMargin));
                    graphics.drawLine(x, y, x1, y1);
                }
        }
        if (showIds) {
            graphics.setColor(linksColor);
            graphics.drawString(String.valueOf(sensor.id()),x-2,y-2);
       }
    }

    /**
     * This method plots a Wireless Sensor Network
     * @param network Wireless Sensor Network to be plotted
     * @param requiredService Service requested by the clients (needed in order to paint and
     * distinguish benevolent and malicious servers)
     * @param radioRange Sensors radio range
     * @param showRanges Indicates whether to plot sensors radio ranges or not
     * @param showLinks Indicates whether to plot links between sensors or not
     * @param showIds Indicates whether to plot sensors identifiers or not
     * @param showGrid Indicates whether to plot a grid or not
     * @param graphics Graphic object where to plot the wireless sensor network
     * @throws Exception If any error occurs while plotting a WSN
     */
    protected void paintNetwork(Network network, Service requiredService,
            double radioRange, boolean showRanges, boolean showLinks,
            boolean showIds, boolean showGrid, Graphics graphics) throws Exception {
        int height = this.getHeight();
        int width = this.getWidth();
        Color sensorColor;

        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        drawAxes(graphics);
        if (showGrid)
            drawGrid(graphics);

        if (network != null) {
            for (Sensor client : network.get_clients()) {
                if (client.isActive())
                    sensorColor = clientColor;
                else
                    sensorColor = idleClientColor;

                paintSensor(client,sensorColor,graphics);
            }
            
            if (requiredService != null)
                for (Sensor server : network.get_servers()) {
                    sensorColor = relayServerColor;
                    if (!server.isActive())
                        sensorColor = idleServerColor;
                    else if (server.offersService(requiredService)) {
                        if (server.get_goodness(requiredService) >= 0.5)
                            sensorColor = benevolentServerColor;
                        else
                            sensorColor = maliciousServerColor;
                    }

                    paintSensor(server,sensorColor,graphics);
                }
        }
    }
    
    protected void drawGrid(Graphics graphics) {
        int height = this.getHeight();
        int width = this.getWidth();

        graphics.setColor(gridColor);

        for (int i = 1; i <= numTicks; i++) {
            //horizontal grid
            graphics.drawLine((int)(width*axesMargin),(int)(height*(1.0-axesMargin)-height*(1.0-2*axesMargin)*(i/(double)numTicks)),(int)(width*(1.0-axesMargin)),(int)(height*(1.0-axesMargin)-height*(1.0-2*axesMargin)*(i/(double)numTicks)));
            //vertical grid
            graphics.drawLine((int)(width*axesMargin+width*(1.0-2*axesMargin)*(i/(double)numTicks)),(int)(height*(1.0-axesMargin)),(int)(width*axesMargin+width*(1.0-2*axesMargin)*(i/(double)numTicks)),(int)(height*axesMargin));
        }
    }
    
    protected void drawAxes(Graphics graphics) {
        int height = this.getHeight();
        int width = this.getWidth();

        graphics.setColor(axesColor);
        
        //X axis
        graphics.drawLine((int)(width*axesMargin), (int)(height*(1-axesMargin)), (int)(width*(1.0-axesMargin)), (int)(height*(1.0-axesMargin)));
        for (int i = 0; i <= numTicks; i++) {
            graphics.drawLine((int)(width*axesMargin+width*(1.0-2*axesMargin)*(i/(double)numTicks)),(int)(height*(1.0-axesMargin))+5,(int)(width*axesMargin+width*(1.0-2*axesMargin)*(i/(double)numTicks)),(int)(height*(1.0-axesMargin))-5);
            graphics.drawString(String.valueOf((int)((xOrigin+xAxisLength)*(i/(double)numTicks))), (int)(width*axesMargin+width*(1.0-2*axesMargin)*(i/(double)numTicks))-5, (int)(height)-5);
        }
        
        //Y axis
        graphics.drawLine((int)(width*axesMargin), (int)(height*(1-axesMargin)), (int)(width*axesMargin), (int)(height*axesMargin));
        for (int i = 0; i <= numTicks; i++) {
            graphics.drawLine((int)(width*axesMargin)-5,(int)(height*(1.0-axesMargin)-height*(1.0-2*axesMargin)*(i/(double)numTicks)),(int)(width*axesMargin)+5,(int)(height*(1.0-axesMargin)-height*(1.0-2*axesMargin)*(i/(double)numTicks)));
            graphics.drawString(String.valueOf((int)((xOrigin+xAxisLength)*(i/(double)numTicks))), 1, (int)(height*(1.0-axesMargin)-height*(1.0-2*axesMargin)*(i/(double)numTicks))+5);
        }
    }
    
    public Point getCoordinateAtPosition(int x, int y) {
        int height = this.getHeight();
        int width = this.getWidth();

        int X = (int)Math.round((x - getBounds().getX() - width*axesMargin)/((width*(1.0-2*axesMargin))/(xAxisLength-xOrigin)));
        int Y = (int)Math.round((height*(1.0-axesMargin) -(y - getBounds().getY()))/((height*(1.0-2*axesMargin))/(yAxisLength-yOrigin)));

        return new Point(X,Y);
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