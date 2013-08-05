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

package es.ants.felixgm.trmsim_wsn.gui.parameterpanels;

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;
import es.ants.felixgm.trmsim_wsn.trm.peertrust.PeerTrust_Parameters;

/**
 * <p>This class represents the panel used to retrieve the parameters of PeerTrust</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.4
 * @since 0.2
 */
public class PeerTrust_ParametersPanel extends TRMParametersPanel {
    
    /** 
     * Creates new form PeerTrust_ParametersPanel
     */
    public PeerTrust_ParametersPanel() {
        initComponents();

        /**/
        alphaLabel.setVisible(false);
        alphaSlider.setVisible(false);
        alphaTextField.setVisible(false);
        betaLabel.setVisible(false);
        betaSlider.setVisible(false);
        betaTextField.setVisible(false);
        /**/
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        alphaLabel = new javax.swing.JLabel();
        alphaTextField = new javax.swing.JTextField();
        betaLabel = new javax.swing.JLabel();
        betaTextField = new javax.swing.JTextField();
        windowSizeLabel = new javax.swing.JLabel();
        alphaSlider = new javax.swing.JSlider();
        betaSlider = new javax.swing.JSlider();
        windowSizeSpinner = new javax.swing.JSpinner();

        alphaLabel.setText("alpha");
        alphaLabel.setEnabled(false);

        alphaTextField.setEditable(false);
        alphaTextField.setText("0.7500");
        alphaTextField.setPreferredSize(new java.awt.Dimension(45, 25));

        betaLabel.setText("beta");
        betaLabel.setEnabled(false);

        betaTextField.setEditable(false);
        betaTextField.setText("0.2500");
        betaTextField.setPreferredSize(new java.awt.Dimension(45, 25));

        windowSizeLabel.setText("Window size");
        windowSizeLabel.setEnabled(false);

        alphaSlider.setMaximum(10000);
        alphaSlider.setValue(7500);
        alphaSlider.setEnabled(false);
        alphaSlider.setPreferredSize(new java.awt.Dimension(140, 25));
        alphaSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                alphaSliderStateChanged(evt);
            }
        });

        betaSlider.setMaximum(10000);
        betaSlider.setValue(2500);
        betaSlider.setEnabled(false);
        betaSlider.setPreferredSize(new java.awt.Dimension(140, 25));
        betaSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                betaSliderStateChanged(evt);
            }
        });

        windowSizeSpinner.setModel(new javax.swing.SpinnerNumberModel(5,1,Integer.MAX_VALUE,1));
        windowSizeSpinner.setEnabled(false);
        windowSizeSpinner.setPreferredSize(new java.awt.Dimension(140, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(windowSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alphaLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(betaLabel)
                            .addComponent(alphaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(betaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(betaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alphaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(windowSizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(windowSizeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(windowSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(alphaLabel)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(alphaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alphaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(betaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(betaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(betaSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void alphaSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_alphaSliderStateChanged
        alphaTextField.setText(String.valueOf(alphaSlider.getValue()/(double)alphaSlider.getMaximum()));
    }//GEN-LAST:event_alphaSliderStateChanged

    private void betaSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_betaSliderStateChanged
        betaTextField.setText(String.valueOf(betaSlider.getValue()/(double)betaSlider.getMaximum()));
    }//GEN-LAST:event_betaSliderStateChanged
    
    /**
     * Retrieves alpha parameter from its corresponding field in the panel
     * @return alpha parameter
     */
    protected double get_alpha() {
        return Double.parseDouble(alphaTextField.getText());
    }
    
    /**
     * Sets alpha parameter into its corresponding field in the panel
     * @param alpha alpha parameter value to be set
     */
    protected void set_alpha(double alpha) {
        alphaSlider.setValue((int)((alphaSlider.getMaximum()-alphaSlider.getMinimum())*alpha));
    }
    
    /**
     * Retrieves beta parameter from its corresponding field in the panel
     * @return beta parameter
     */
    protected double get_beta() {
        return Double.parseDouble(betaTextField.getText());
    }
    
    /**
     * Sets beta parameter into its corresponding field in the panel
     * @param beta beta parameter value to be set
     */
    protected void set_beta(double beta) {
        betaSlider.setValue((int)((betaSlider.getMaximum()-betaSlider.getMinimum())*beta));
    }
    
   
    /**
     * Retrieves windowSize parameter from its corresponding field in the panel
     * @return windowSize parameter
     */
    protected int get_windowSize() {
        return ((Integer)windowSizeSpinner.getValue());
    }    
    
    /**
     * Sets windowSize parameter into its corresponding field in the panel
     * @param windowSize windowSize parameter value to be set
     */
    protected void set_windowSize(int windowSize) {
        windowSizeSpinner.setValue(windowSize);
    }
    
    @Override
    public TRMParameters get_TRMParameters() {
        PeerTrust_Parameters peerTrustParameters = new PeerTrust_Parameters();
        
        peerTrustParameters.set_alpha(get_alpha());
        peerTrustParameters.set_beta(get_beta());
        peerTrustParameters.set_windowSize(get_windowSize());
        
        return peerTrustParameters;
    }

    @Override
    public void set_TRMParameters(TRMParameters trmParameters) {
        PeerTrust_Parameters PeerTrustParameters = (PeerTrust_Parameters)trmParameters;
        
        set_alpha(PeerTrustParameters.get_alpha());
        set_beta(PeerTrustParameters.get_beta());
        set_windowSize(PeerTrustParameters.get_windowSize());
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        alphaLabel.setEnabled(enabled);
        alphaSlider.setEnabled(enabled);
        betaLabel.setEnabled(enabled);
        betaSlider.setEnabled(enabled);
        windowSizeLabel.setEnabled(enabled);
        windowSizeSpinner.setEnabled(enabled);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alphaLabel;
    private javax.swing.JSlider alphaSlider;
    private javax.swing.JTextField alphaTextField;
    private javax.swing.JLabel betaLabel;
    private javax.swing.JSlider betaSlider;
    private javax.swing.JTextField betaTextField;
    private javax.swing.JLabel windowSizeLabel;
    private javax.swing.JSpinner windowSizeSpinner;
    // End of variables declaration//GEN-END:variables
    
}