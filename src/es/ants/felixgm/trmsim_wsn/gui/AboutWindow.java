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

import javax.swing.JOptionPane;
import javax.swing.JEditorPane;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * <p>This class represents the about window of TRMSim-WSN</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.2
 */
public class AboutWindow extends javax.swing.JFrame {
    
    /** 
     * Creates new form AboutWindow 
     */
    public AboutWindow() {
        initComponents();
        setLocationRelativeTo(null);
        try {
            aboutTextPanel.addHyperlinkListener(new Hyperactive());
            aboutTextPanel.setPage(ClassLoader.getSystemResource("resources/about.html"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex,"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aboutScrollPane = new javax.swing.JScrollPane();
        aboutTextPanel = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About TRMSim-WSN "+TRMSim_WSN.CURRENT_VERSION);
        setBounds(new java.awt.Rectangle(250, 160, 430, 460));

        aboutTextPanel.setEditable(false);
        aboutScrollPane.setViewportView(aboutTextPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aboutScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aboutScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane aboutScrollPane;
    private javax.swing.JEditorPane aboutTextPanel;
    // End of variables declaration//GEN-END:variables
    /**
     * Class used to allow navigating through hyperlinks
     */
    class Hyperactive implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                JEditorPane pane = (JEditorPane) e.getSource();
                try {
                    if ((e.getURL().getPath().endsWith(".pdf")) && (java.awt.Desktop.isDesktopSupported()))
                        java.awt.Desktop.getDesktop().open(new java.io.File(ClassLoader.getSystemResource("resources" + e.getURL().getPath().substring(e.getURL().getPath().lastIndexOf('/')).replaceAll("%20", " ")).toURI()));
                    else if (((e.getURL().getProtocol().equals("http")) || (e.getURL().getProtocol().equals("https"))) && (java.awt.Desktop.isDesktopSupported()))
                        java.awt.Desktop.getDesktop().browse(e.getURL().toURI());
                    else if ((e.getURL().getProtocol().equals("mailto")) && (java.awt.Desktop.isDesktopSupported()))
                        java.awt.Desktop.getDesktop().mail(e.getURL().toURI());
                    else
                        pane.setPage(e.getURL());
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }
}