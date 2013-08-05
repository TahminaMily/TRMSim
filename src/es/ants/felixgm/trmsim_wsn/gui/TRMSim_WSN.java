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

import es.ants.felixgm.trmsim_wsn.Controller;

import es.ants.felixgm.trmsim_wsn.gui.legendpanels.EigenTrustLegendPanel;
import es.ants.felixgm.trmsim_wsn.gui.legendpanels.LegendPanel;
import es.ants.felixgm.trmsim_wsn.gui.legendpanels.PowerTrustLegendPanel;
import es.ants.felixgm.trmsim_wsn.gui.legendpanels.TRIPLegendPanel;
import es.ants.felixgm.trmsim_wsn.gui.networkpanels.EigenTrustNetworkPanel;
import es.ants.felixgm.trmsim_wsn.gui.networkpanels.NetworkPanel;
import es.ants.felixgm.trmsim_wsn.gui.networkpanels.PowerTrustNetworkPanel;
import es.ants.felixgm.trmsim_wsn.gui.networkpanels.TRIPNetworkPanel;
import es.ants.felixgm.trmsim_wsn.gui.outcomespanels.*;
import es.ants.felixgm.trmsim_wsn.gui.parameterpanels.TRMParametersPanel;

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;
import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;
import es.ants.felixgm.trmsim_wsn.trm.btrm_wsn.BTRM_WSN;
import es.ants.felixgm.trmsim_wsn.trm.eigentrust.EigenTrust;
import es.ants.felixgm.trmsim_wsn.trm.lftm.LFTM;
import es.ants.felixgm.trmsim_wsn.trm.peertrust.PeerTrust;
import es.ants.felixgm.trmsim_wsn.trm.powertrust.PowerTrust;
import es.ants.felixgm.trmsim_wsn.trm.templatetrm.TemplateTRM;
import es.ants.felixgm.trmsim_wsn.trm.trip.TRIP;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


/**
 * <p>This class represents the main window of TRMSim-WSN</p>
 * <ul>
 *   <li>F&eacute;lix G&oacute;mez M&aacute;rmol, Gregorio Mart&iacute;nez
 *       P&eacute;rez, "<strong>TRMSim-WSN, Trust and Reputation Models
 *       Simulator for Wireless Sensor Networks</strong>",
 *       <a href="http://www.ieee-icc.org/2009" target="_blank"> IEEE International
 *       Conference on Communications (IEEE ICC 2009), Communication and Information
 *       Systems Security Symposium</a>, Dresden, Germany, 14-18 June 2009
 *       <a href="http://ants.dif.um.es/~felixgm/pub/conferences/09/GomezMarmol-ICC09.pdf" target="_blank"><img src="http://ants.dif.um.es/~felixgm/img/adobe.gif" border="0"></a>
 *   </li>
 * </ul>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.1
 */
public class TRMSim_WSN extends javax.swing.JFrame implements Observer {
    
    /**
    * Current version of TRMSim-WSN: {@value}
    */
    public static final String CURRENT_VERSION = "0.5";
    
    protected static Controller C;

    /**
     * Creates new form TRMSim_WSN
     */
    public TRMSim_WSN() {
        try {
            initComponents();
            this.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*1.0),
                    (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.98));
            this.setLocationRelativeTo(null);
            
            initializeTRModels();

            C = Controller.C();
            TRModelComboBoxItemStateChanged(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void initializeTRModels() {
        Vector<String> trmodels = new Vector<String>();
        trmodels.add(BTRM_WSN.get_name());
        trmodels.add(EigenTrust.get_name());
        trmodels.add(PeerTrust.get_name());
        trmodels.add(PowerTrust.get_name());
        trmodels.add(LFTM.get_name());
        trmodels.add(TRIP.get_name());
        trmodels.add(TemplateTRM.get_name());
        TRModelComboBox.setModel(new javax.swing.DefaultComboBoxModel(trmodels));
        
        for (final String trmodel : trmodels) {
            JMenuItem trmodelMenuItem = new JMenuItem(trmodel);
            trmodelMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    TRModelComboBox.setSelectedItem(trmodel);
                    TRModelComboBoxItemStateChanged(null);
                }
            });
            TRModelMenu.add(trmodelMenuItem);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        parametersSourceButtonGroup = new javax.swing.ButtonGroup();
        tabbedPane = new javax.swing.JTabbedPane();
        simulationsPanel = new javax.swing.JPanel();
        simulationsSplitPane = new javax.swing.JSplitPane();
        upperPanel = new javax.swing.JPanel();
        upperSplitPane = new javax.swing.JSplitPane();
        controlsScrollPane = new javax.swing.JScrollPane();
        controlsPanel = new javax.swing.JPanel();
        legendPanelContainer = new javax.swing.JPanel();
        legendLabel = new javax.swing.JLabel();
        buttonsControlPanel = new javax.swing.JPanel();
        newWSNButton = new javax.swing.JButton();
        resetWSNButton = new javax.swing.JButton();
        runTRMButton = new javax.swing.JButton();
        stopTRMButton = new javax.swing.JButton();
        loadWSNButton = new javax.swing.JButton();
        saveWSNButton = new javax.swing.JButton();
        stopSimulationsButton = new javax.swing.JButton();
        runSimulationsButton = new javax.swing.JButton();
        spinnersControlPanel = new javax.swing.JPanel();
        numExecutionsLabel = new javax.swing.JLabel();
        numExecutionsSpinner = new javax.swing.JSpinner();
        numNetworksLabel = new javax.swing.JLabel();
        numNetworksSpinner = new javax.swing.JSpinner();
        minNumSensorsLabel = new javax.swing.JLabel();
        minNumSensorsSpinner = new javax.swing.JSpinner();
        maxNumSensorsLabel = new javax.swing.JLabel();
        maxNumSensorsSpinner = new javax.swing.JSpinner();
        slidersControlsPanel = new javax.swing.JPanel();
        percentageClientsLabel = new javax.swing.JLabel();
        percentageClientsSlider = new javax.swing.JSlider();
        percentageClientsTextField = new javax.swing.JTextField();
        percentageRelayServersLabel = new javax.swing.JLabel();
        percentageRelayServersSlider = new javax.swing.JSlider();
        percentageRelayServersTextField = new javax.swing.JTextField();
        percentageMaliciousServersLabel = new javax.swing.JLabel();
        percentageMaliciousServersSlider = new javax.swing.JSlider();
        percentageMaliciousServersTextField = new javax.swing.JTextField();
        radioRangeLabel = new javax.swing.JLabel();
        radioRangeSlider = new javax.swing.JSlider();
        radioRangeTextField = new javax.swing.JTextField();
        delayLabel = new javax.swing.JLabel();
        delaySlider = new javax.swing.JSlider();
        delayTextField = new javax.swing.JTextField();
        TRModelLabel = new javax.swing.JLabel();
        TRModelComboBox = new javax.swing.JComboBox();
        displayControlsPanel = new javax.swing.JPanel();
        showIdsCheckBox = new javax.swing.JCheckBox();
        showLinksCheckBox = new javax.swing.JCheckBox();
        showRangesCheckBox = new javax.swing.JCheckBox();
        showGridCheckBox = new javax.swing.JCheckBox();
        threatsControlsPanel = new javax.swing.JPanel();
        collusionCheckBox = new javax.swing.JCheckBox();
        oscillatingWSNsCheckBox = new javax.swing.JCheckBox();
        dynamicWSNsCheckBox = new javax.swing.JCheckBox();
        networkAndSensorPropertiesContainerPanel = new javax.swing.JPanel();
        networkPanelContainer = new javax.swing.JPanel();
        sensorPropertiesPanel = new javax.swing.JPanel();
        sensorIdLabel = new javax.swing.JLabel();
        sensorIdTextField = new javax.swing.JTextField();
        xCoordinateLabel = new javax.swing.JLabel();
        xCoordinateTextField = new javax.swing.JTextField();
        yCoordinateLabel = new javax.swing.JLabel();
        yCoordinateTextField = new javax.swing.JTextField();
        radioRangePropertyLabel = new javax.swing.JLabel();
        radioRangeSpinner = new javax.swing.JSpinner();
        neighborsLabel = new javax.swing.JLabel();
        neighborsScrollPane = new javax.swing.JScrollPane();
        neighborsList = new javax.swing.JList();
        sensorTypeLabel = new javax.swing.JLabel();
        sensorTypeComboBox = new javax.swing.JComboBox();
        applyChangesButton = new javax.swing.JButton();
        hideSensorPropertiesPanelButton = new javax.swing.JButton();
        bottomPanel = new javax.swing.JPanel();
        outcomesPanelsPanel = new javax.swing.JPanel();
        outcomesTabbedPane = new javax.swing.JTabbedPane();
        messagePanel = new javax.swing.JPanel();
        messagesScrollPane = new javax.swing.JScrollPane();
        messagesTextArea = new javax.swing.JTextArea();
        parametersPanel = new javax.swing.JPanel();
        parametersSettingsPanel = new javax.swing.JPanel();
        separator2 = new javax.swing.JSeparator();
        separator1 = new javax.swing.JSeparator();
        parametersFileTextField = new javax.swing.JTextField();
        parametersFileLabel = new javax.swing.JLabel();
        customizedParametersRadioButton = new javax.swing.JRadioButton();
        parametersFileRadioButton = new javax.swing.JRadioButton();
        parametersSourceLabel = new javax.swing.JLabel();
        applyParametersChangesButton = new javax.swing.JButton();
        saveParametersFileContentButton = new javax.swing.JButton();
        browseButton = new javax.swing.JButton();
        separator3 = new javax.swing.JSeparator();
        bottomParametersContainerPanel = new javax.swing.JPanel();
        bottomParametersSplitPane = new javax.swing.JSplitPane();
        TRMParametersScrollPane = new javax.swing.JScrollPane();
        TRM_ParametersPanelAux = new javax.swing.JPanel();
        parametersFileContentScrollPane = new javax.swing.JScrollPane();
        parametersFileContentTextArea = new javax.swing.JTextArea();
        separator4 = new javax.swing.JSeparator();
        menuBar = new javax.swing.JMenuBar();
        wsnMenu = new javax.swing.JMenu();
        newWSNmenuItem = new javax.swing.JMenuItem();
        resetWSNmenuItem = new javax.swing.JMenuItem();
        loadWSNmenuItem = new javax.swing.JMenuItem();
        saveWSNmenuItem = new javax.swing.JMenuItem();
        simulationsMenu = new javax.swing.JMenu();
        runTRMmenuItem = new javax.swing.JMenuItem();
        stopTRMmenuItem = new javax.swing.JMenuItem();
        runSimulationsMenuItem = new javax.swing.JMenuItem();
        stopSimulationsMenuItem = new javax.swing.JMenuItem();
        parametersMenu = new javax.swing.JMenu();
        loadParametersMenuItem = new javax.swing.JMenuItem();
        saveParametersMenuItem = new javax.swing.JMenuItem();
        applyParametersChangesMenuItem = new javax.swing.JMenuItem();
        TRModelMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        aboutTRMSim_WSNmenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TRMSim-WSN "+CURRENT_VERSION+" Trust & Reputation Models Simulator for Wireless Sensor Networks");
        setBounds(new java.awt.Rectangle(100, 100, 0, 0));
        setIconImage(Toolkit.getDefaultToolkit().getImage("resources/images/TRMSim-WSN-icon.gif"));
        setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        setPreferredSize(new java.awt.Dimension(100, 100));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        tabbedPane.setPreferredSize(getSize());

        simulationsPanel.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight())));
        simulationsPanel.setLayout(new javax.swing.BoxLayout(simulationsPanel, javax.swing.BoxLayout.Y_AXIS));

        simulationsSplitPane.setDividerLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.65));
        simulationsSplitPane.setDividerSize(3);
        simulationsSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        simulationsSplitPane.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight())));

        upperPanel.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.6)));
        upperPanel.setLayout(new javax.swing.BoxLayout(upperPanel, javax.swing.BoxLayout.X_AXIS));

        upperSplitPane.setDividerLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.49));
        upperSplitPane.setDividerSize(3);
        upperSplitPane.setPreferredSize(upperPanel.getPreferredSize());

        controlsScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));
        controlsScrollPane.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.5),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.7)));

        controlsPanel.setPreferredSize(new java.awt.Dimension(400, 460));

        legendPanelContainer.add(legendPanel,null);
        legendPanel.setBackground(Color.white);
        legendPanelContainer.setPreferredSize(new java.awt.Dimension(100, 98));

        javax.swing.GroupLayout legendPanelContainerLayout = new javax.swing.GroupLayout(legendPanelContainer);
        legendPanelContainer.setLayout(legendPanelContainerLayout);
        legendPanelContainerLayout.setHorizontalGroup(
            legendPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        legendPanelContainerLayout.setVerticalGroup(
            legendPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
        );

        legendLabel.setText("Legend");
        legendLabel.setPreferredSize(new java.awt.Dimension(100, 15));

        buttonsControlPanel.setMinimumSize(new java.awt.Dimension(250, 115));
        buttonsControlPanel.setLayout(new java.awt.GridLayout(4, 2));
        buttonsControlPanel.setLayout(new java.awt.GridLayout(4, 2,5,5));

        newWSNButton.setText("New WSN");
        newWSNButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        newWSNButton.setMaximumSize(new java.awt.Dimension(150, 25));
        newWSNButton.setMinimumSize(new java.awt.Dimension(120, 25));
        newWSNButton.setPreferredSize(new java.awt.Dimension(150, 25));
        newWSNButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWSNButtonActionPerformed(evt);
            }
        });
        buttonsControlPanel.add(newWSNButton);

        resetWSNButton.setText("Reset WSN");
        resetWSNButton.setEnabled(false);
        resetWSNButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        resetWSNButton.setMaximumSize(new java.awt.Dimension(150, 25));
        resetWSNButton.setMinimumSize(new java.awt.Dimension(120, 25));
        resetWSNButton.setPreferredSize(new java.awt.Dimension(150, 25));
        resetWSNButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetWSNButtonActionPerformed(evt);
            }
        });
        buttonsControlPanel.add(resetWSNButton);

        runTRMButton.setText("Run T&R Model");
        runTRMButton.setEnabled(false);
        runTRMButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        runTRMButton.setMaximumSize(new java.awt.Dimension(150, 25));
        runTRMButton.setMinimumSize(new java.awt.Dimension(120, 25));
        runTRMButton.setPreferredSize(new java.awt.Dimension(150, 25));
        runTRMButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runTRMButtonActionPerformed(evt);
            }
        });
        buttonsControlPanel.add(runTRMButton);

        stopTRMButton.setText("Stop T&R Model");
        stopTRMButton.setEnabled(false);
        stopTRMButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        stopTRMButton.setMaximumSize(new java.awt.Dimension(150, 25));
        stopTRMButton.setMinimumSize(new java.awt.Dimension(120, 25));
        stopTRMButton.setPreferredSize(new java.awt.Dimension(150, 25));
        stopTRMButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopTRMButtonActionPerformed(evt);
            }
        });
        buttonsControlPanel.add(stopTRMButton);

        loadWSNButton.setText("Load WSN");
        loadWSNButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        loadWSNButton.setMaximumSize(new java.awt.Dimension(150, 25));
        loadWSNButton.setMinimumSize(new java.awt.Dimension(120, 25));
        loadWSNButton.setPreferredSize(new java.awt.Dimension(150, 25));
        loadWSNButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadWSNButtonActionPerformed(evt);
            }
        });
        buttonsControlPanel.add(loadWSNButton);

        saveWSNButton.setText("Save WSN");
        saveWSNButton.setEnabled(false);
        saveWSNButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        saveWSNButton.setMaximumSize(new java.awt.Dimension(150, 25));
        saveWSNButton.setMinimumSize(new java.awt.Dimension(120, 25));
        saveWSNButton.setPreferredSize(new java.awt.Dimension(150, 25));
        saveWSNButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveWSNButtonActionPerformed(evt);
            }
        });
        buttonsControlPanel.add(saveWSNButton);

        stopSimulationsButton.setText("Stop Simulations");
        stopSimulationsButton.setEnabled(false);
        stopSimulationsButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        stopSimulationsButton.setMaximumSize(new java.awt.Dimension(150, 25));
        stopSimulationsButton.setMinimumSize(new java.awt.Dimension(120, 25));
        stopSimulationsButton.setPreferredSize(new java.awt.Dimension(150, 25));
        stopSimulationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopSimulationsButtonActionPerformed(evt);
            }
        });
        buttonsControlPanel.add(stopSimulationsButton);

        runSimulationsButton.setText("Run Simulations");
        runSimulationsButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        runSimulationsButton.setMaximumSize(new java.awt.Dimension(150, 25));
        runSimulationsButton.setMinimumSize(new java.awt.Dimension(120, 25));
        runSimulationsButton.setPreferredSize(new java.awt.Dimension(150, 25));
        runSimulationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runSimulationsButtonActionPerformed(evt);
            }
        });
        buttonsControlPanel.add(runSimulationsButton);

        spinnersControlPanel.setPreferredSize(new java.awt.Dimension(100, 205));
        spinnersControlPanel.setLayout(new javax.swing.BoxLayout(spinnersControlPanel, javax.swing.BoxLayout.Y_AXIS));

        numExecutionsLabel.setText("Num executions");
        numExecutionsLabel.setPreferredSize(new java.awt.Dimension(150, 25));
        spinnersControlPanel.add(numExecutionsLabel);

        numExecutionsSpinner.setModel(new javax.swing.SpinnerNumberModel(100,1,Integer.MAX_VALUE,1));
        numExecutionsSpinner.setAlignmentX(0.0F);
        numExecutionsSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        spinnersControlPanel.add(numExecutionsSpinner);

        numNetworksLabel.setText("Num networks");
        numNetworksLabel.setPreferredSize(new java.awt.Dimension(150, 25));
        spinnersControlPanel.add(numNetworksLabel);

        numNetworksSpinner.setModel(new javax.swing.SpinnerNumberModel(100,1,Integer.MAX_VALUE,1));
        numNetworksSpinner.setAlignmentX(0.0F);
        numNetworksSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        spinnersControlPanel.add(numNetworksSpinner);

        minNumSensorsLabel.setText("Min Num Sensors");
        minNumSensorsLabel.setPreferredSize(new java.awt.Dimension(150, 25));
        spinnersControlPanel.add(minNumSensorsLabel);

        minNumSensorsSpinner.setModel(new javax.swing.SpinnerNumberModel(50,1,Integer.MAX_VALUE,1));
        minNumSensorsSpinner.setAlignmentX(0.0F);
        minNumSensorsSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        minNumSensorsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                minNumSensorsSpinnerStateChanged(evt);
            }
        });
        spinnersControlPanel.add(minNumSensorsSpinner);

        maxNumSensorsLabel.setText("Max Num Sensors");
        maxNumSensorsLabel.setPreferredSize(new java.awt.Dimension(150, 25));
        spinnersControlPanel.add(maxNumSensorsLabel);

        maxNumSensorsSpinner.setModel(new javax.swing.SpinnerNumberModel(50,1,Integer.MAX_VALUE,1));
        maxNumSensorsSpinner.setAlignmentX(0.0F);
        maxNumSensorsSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        maxNumSensorsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                maxNumSensorsSpinnerStateChanged(evt);
            }
        });
        spinnersControlPanel.add(maxNumSensorsSpinner);

        slidersControlsPanel.setLayout(new java.awt.GridBagLayout());

        percentageClientsLabel.setText("% Clients");
        percentageClientsLabel.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageClientsLabel, gridBagConstraints);

        percentageClientsSlider.setValue(15);
        percentageClientsSlider.setAlignmentX(0.0F);
        percentageClientsSlider.setPreferredSize(new java.awt.Dimension(150, 25));
        percentageClientsSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                percentageClientsSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageClientsSlider, gridBagConstraints);

        percentageClientsTextField.setEditable(false);
        percentageClientsTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        percentageClientsTextField.setPreferredSize(new java.awt.Dimension(45, 25));
        percentageClientsTextField.setText(String.valueOf(percentageClientsSlider.getValue()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageClientsTextField, gridBagConstraints);

        percentageRelayServersLabel.setText("% Relay Servers");
        percentageRelayServersLabel.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageRelayServersLabel, gridBagConstraints);

        percentageRelayServersSlider.setValue(5);
        percentageRelayServersSlider.setAlignmentX(0.0F);
        percentageRelayServersSlider.setPreferredSize(new java.awt.Dimension(150, 25));
        percentageRelayServersSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                percentageRelayServersSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageRelayServersSlider, gridBagConstraints);

        percentageRelayServersTextField.setEditable(false);
        percentageRelayServersTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        percentageRelayServersTextField.setPreferredSize(new java.awt.Dimension(45, 25));
        percentageRelayServersTextField.setText(String.valueOf(percentageRelayServersSlider.getValue()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageRelayServersTextField, gridBagConstraints);

        percentageMaliciousServersLabel.setText("% Malicious Servers");
        percentageMaliciousServersLabel.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageMaliciousServersLabel, gridBagConstraints);

        percentageMaliciousServersSlider.setValue(70);
        percentageMaliciousServersSlider.setAlignmentX(0.0F);
        percentageMaliciousServersSlider.setPreferredSize(new java.awt.Dimension(150, 25));
        percentageMaliciousServersSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                percentageMaliciousServersSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageMaliciousServersSlider, gridBagConstraints);

        percentageMaliciousServersTextField.setEditable(false);
        percentageMaliciousServersTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        percentageMaliciousServersTextField.setPreferredSize(new java.awt.Dimension(45, 25));
        percentageMaliciousServersTextField.setText(String.valueOf(percentageMaliciousServersSlider.getValue()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(percentageMaliciousServersTextField, gridBagConstraints);

        radioRangeLabel.setText("Radio Range");
        radioRangeLabel.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(radioRangeLabel, gridBagConstraints);

        radioRangeSlider.setValue(12);
        radioRangeSlider.setAlignmentX(0.0F);
        radioRangeSlider.setPreferredSize(new java.awt.Dimension(150, 25));
        radioRangeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                radioRangeSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(radioRangeSlider, gridBagConstraints);

        radioRangeTextField.setEditable(false);
        radioRangeTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        radioRangeTextField.setPreferredSize(new java.awt.Dimension(45, 25));
        radioRangeTextField.setText(String.valueOf(radioRangeSlider.getValue()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(radioRangeTextField, gridBagConstraints);

        delayLabel.setText("Delay");
        delayLabel.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(delayLabel, gridBagConstraints);

        delaySlider.setValue(0);
        delaySlider.setAlignmentX(0.0F);
        delaySlider.setPreferredSize(new java.awt.Dimension(150, 25));
        delaySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                delaySliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(delaySlider, gridBagConstraints);

        delayTextField.setEditable(false);
        delayTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        delayTextField.setPreferredSize(new java.awt.Dimension(45, 25));
        delayTextField.setText(String.valueOf(delaySlider.getValue()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        slidersControlsPanel.add(delayTextField, gridBagConstraints);

        TRModelLabel.setText("Trust & Reputation Model");

        TRModelComboBox.setPreferredSize(new java.awt.Dimension(140, 25));
        TRModelComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                TRModelComboBoxItemStateChanged(evt);
            }
        });

        displayControlsPanel.setLayout(new javax.swing.BoxLayout(displayControlsPanel, javax.swing.BoxLayout.Y_AXIS));

        showIdsCheckBox.setText("Show ids");
        showIdsCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        showIdsCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        showIdsCheckBox.setMaximumSize(new java.awt.Dimension(120, 15));
        showIdsCheckBox.setMinimumSize(new java.awt.Dimension(120, 15));
        showIdsCheckBox.setPreferredSize(new java.awt.Dimension(120, 25));
        showIdsCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showIdsCheckBoxItemStateChanged(evt);
            }
        });
        displayControlsPanel.add(showIdsCheckBox);

        showLinksCheckBox.setSelected(true);
        showLinksCheckBox.setText("Show links");
        showLinksCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        showLinksCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        showLinksCheckBox.setMaximumSize(new java.awt.Dimension(120, 15));
        showLinksCheckBox.setMinimumSize(new java.awt.Dimension(120, 15));
        showLinksCheckBox.setPreferredSize(new java.awt.Dimension(120, 25));
        showLinksCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showLinksCheckBoxItemStateChanged(evt);
            }
        });
        displayControlsPanel.add(showLinksCheckBox);

        showRangesCheckBox.setText("Show ranges");
        showRangesCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        showRangesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        showRangesCheckBox.setMaximumSize(new java.awt.Dimension(120, 15));
        showRangesCheckBox.setMinimumSize(new java.awt.Dimension(120, 15));
        showRangesCheckBox.setPreferredSize(new java.awt.Dimension(120, 25));
        showRangesCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showRangesCheckBoxItemStateChanged(evt);
            }
        });
        displayControlsPanel.add(showRangesCheckBox);

        showGridCheckBox.setText("Show grid");
        showGridCheckBox.setBorder(null);
        showGridCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        showGridCheckBox.setMaximumSize(new java.awt.Dimension(120, 15));
        showGridCheckBox.setMinimumSize(new java.awt.Dimension(120, 15));
        showGridCheckBox.setPreferredSize(new java.awt.Dimension(120, 25));
        showGridCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                showGridCheckBoxItemStateChanged(evt);
            }
        });
        displayControlsPanel.add(showGridCheckBox);

        threatsControlsPanel.setLayout(new javax.swing.BoxLayout(threatsControlsPanel, javax.swing.BoxLayout.Y_AXIS));

        collusionCheckBox.setText("Collusion");
        collusionCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        collusionCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        collusionCheckBox.setMaximumSize(new java.awt.Dimension(120, 15));
        collusionCheckBox.setMinimumSize(new java.awt.Dimension(120, 15));
        collusionCheckBox.setPreferredSize(new java.awt.Dimension(120, 25));
        threatsControlsPanel.add(collusionCheckBox);

        oscillatingWSNsCheckBox.setText("Oscillating WSNs");
        oscillatingWSNsCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        oscillatingWSNsCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        oscillatingWSNsCheckBox.setMaximumSize(new java.awt.Dimension(120, 15));
        oscillatingWSNsCheckBox.setMinimumSize(new java.awt.Dimension(120, 15));
        oscillatingWSNsCheckBox.setPreferredSize(new java.awt.Dimension(120, 25));
        threatsControlsPanel.add(oscillatingWSNsCheckBox);

        dynamicWSNsCheckBox.setText("Dynamic WSNs");
        dynamicWSNsCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dynamicWSNsCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        dynamicWSNsCheckBox.setMaximumSize(new java.awt.Dimension(120, 15));
        dynamicWSNsCheckBox.setMinimumSize(new java.awt.Dimension(120, 15));
        dynamicWSNsCheckBox.setPreferredSize(new java.awt.Dimension(120, 25));
        threatsControlsPanel.add(dynamicWSNsCheckBox);

        javax.swing.GroupLayout controlsPanelLayout = new javax.swing.GroupLayout(controlsPanel);
        controlsPanel.setLayout(controlsPanelLayout);
        controlsPanelLayout.setHorizontalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addComponent(buttonsControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(legendPanelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(legendLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(TRModelLabel)
                    .addComponent(TRModelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(displayControlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spinnersControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(slidersControlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(threatsControlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        controlsPanelLayout.setVerticalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(controlsPanelLayout.createSequentialGroup()
                        .addComponent(legendLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(legendPanelContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                    .addComponent(buttonsControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spinnersControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(slidersControlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(threatsControlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(displayControlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TRModelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TRModelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        controlsScrollPane.setViewportView(controlsPanel);

        upperSplitPane.setLeftComponent(controlsScrollPane);

        networkAndSensorPropertiesContainerPanel.setLayout(new javax.swing.BoxLayout(networkAndSensorPropertiesContainerPanel, javax.swing.BoxLayout.LINE_AXIS));

        networkPanelContainer.add(networkPanel,null);
        networkPanel.setBackground(Color.white);
        networkPanelContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Network"));
        networkPanelContainer.setMinimumSize(new java.awt.Dimension(100, 100));
        networkPanelContainer.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.5),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.7)));
        networkPanelContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                networkPanelContainerMouseClicked(evt);
            }
        });
        networkPanelContainer.setLayout(new java.awt.BorderLayout());
        networkAndSensorPropertiesContainerPanel.add(networkPanelContainer);

        sensorPropertiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Sensor properties"));
        sensorPropertiesPanel.setMinimumSize(new java.awt.Dimension(120, 400));
        sensorPropertiesPanel.setPreferredSize(new java.awt.Dimension(120, 400));

        sensorIdLabel.setText("Sensor Id");

        sensorIdTextField.setAlignmentX(0.0F);
        sensorIdTextField.setEnabled(false);
        sensorIdTextField.setMinimumSize(new java.awt.Dimension(30, 20));
        sensorIdTextField.setPreferredSize(new java.awt.Dimension(30, 20));

        xCoordinateLabel.setText("X");

        xCoordinateTextField.setEditable(false);
        xCoordinateTextField.setMinimumSize(new java.awt.Dimension(30, 20));
        xCoordinateTextField.setPreferredSize(new java.awt.Dimension(30, 20));

        yCoordinateLabel.setText("Y");

        yCoordinateTextField.setEditable(false);
        yCoordinateTextField.setMinimumSize(new java.awt.Dimension(30, 20));
        yCoordinateTextField.setPreferredSize(new java.awt.Dimension(30, 20));

        radioRangePropertyLabel.setText("Radio range");

        neighborsLabel.setText("Neighbor(s)");

        neighborsScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        neighborsScrollPane.setAutoscrolls(true);
        neighborsScrollPane.setMinimumSize(new java.awt.Dimension(33, 80));
        neighborsScrollPane.setPreferredSize(new java.awt.Dimension(33, 80));

        neighborsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        neighborsList.setMaximumSize(new java.awt.Dimension(32767, 32767));
        neighborsList.setMinimumSize(new java.awt.Dimension(33, 80));
        neighborsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                neighborsListMouseClicked(evt);
            }
        });
        neighborsScrollPane.setViewportView(neighborsList);

        sensorTypeLabel.setText("Sensor type");

        sensorTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        applyChangesButton.setText("Apply");
        applyChangesButton.setMargin(new java.awt.Insets(2, 5, 2, 5));
        applyChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyChangesButtonActionPerformed(evt);
            }
        });

        hideSensorPropertiesPanelButton.setText("Hide panel");
        hideSensorPropertiesPanelButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        hideSensorPropertiesPanelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideSensorPropertiesPanelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sensorPropertiesPanelLayout = new javax.swing.GroupLayout(sensorPropertiesPanel);
        sensorPropertiesPanel.setLayout(sensorPropertiesPanelLayout);
        sensorPropertiesPanelLayout.setHorizontalGroup(
            sensorPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sensorPropertiesPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(sensorPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(sensorPropertiesPanelLayout.createSequentialGroup()
                        .addComponent(sensorIdLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sensorIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                    .addComponent(radioRangePropertyLabel)
                    .addComponent(sensorTypeLabel)
                    .addComponent(neighborsLabel)
                    .addGroup(sensorPropertiesPanelLayout.createSequentialGroup()
                        .addComponent(xCoordinateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xCoordinateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(yCoordinateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yCoordinateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(radioRangeSpinner)
                    .addComponent(neighborsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sensorTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(applyChangesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hideSensorPropertiesPanelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        sensorPropertiesPanelLayout.setVerticalGroup(
            sensorPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sensorPropertiesPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(sensorPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorIdLabel)
                    .addComponent(sensorIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sensorPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xCoordinateLabel)
                    .addComponent(xCoordinateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yCoordinateLabel)
                    .addComponent(yCoordinateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioRangePropertyLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioRangeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(neighborsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(neighborsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sensorTypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sensorTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(applyChangesButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hideSensorPropertiesPanelButton)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        radioRangePropertyLabel.setVisible(false);
        radioRangeSpinner.setVisible(false);
        sensorTypeLabel.setVisible(false);
        sensorTypeComboBox.setVisible(false);
        applyChangesButton.setVisible(false);

        networkAndSensorPropertiesContainerPanel.add(sensorPropertiesPanel);
        sensorPropertiesPanel.setVisible(false);

        upperSplitPane.setRightComponent(networkAndSensorPropertiesContainerPanel);

        upperPanel.add(upperSplitPane);

        simulationsSplitPane.setTopComponent(upperPanel);

        bottomPanel.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.3)));
        bottomPanel.setLayout(new javax.swing.BoxLayout(bottomPanel, javax.swing.BoxLayout.X_AXIS));

        outcomesPanelsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Outcomes"));
        outcomesPanelsPanel.setPreferredSize(new java.awt.Dimension(500, 260));

        javax.swing.GroupLayout outcomesPanelsPanelLayout = new javax.swing.GroupLayout(outcomesPanelsPanel);
        outcomesPanelsPanel.setLayout(outcomesPanelsPanelLayout);
        outcomesPanelsPanelLayout.setHorizontalGroup(
            outcomesPanelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outcomesPanelsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(outcomesTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addContainerGap())
        );
        outcomesPanelsPanelLayout.setVerticalGroup(
            outcomesPanelsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outcomesPanelsPanelLayout.createSequentialGroup()
                .addComponent(outcomesTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addContainerGap())
        );

        bottomPanel.add(outcomesPanelsPanel);

        messagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Messages"));
        messagePanel.setPreferredSize(new java.awt.Dimension(500, 260));

        messagesScrollPane.setAutoscrolls(true);

        messagesTextArea.setColumns(20);
        messagesTextArea.setEditable(false);
        messagesTextArea.setRows(5);
        messagesScrollPane.setViewportView(messagesTextArea);

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(messagesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addContainerGap())
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addComponent(messagesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addContainerGap())
        );

        bottomPanel.add(messagePanel);

        simulationsSplitPane.setBottomComponent(bottomPanel);
        bottomPanel.getAccessibleContext().setAccessibleParent(simulationsPanel);

        simulationsPanel.add(simulationsSplitPane);

        tabbedPane.addTab("Simulations", simulationsPanel);

        parametersPanel.setLayout(new javax.swing.BoxLayout(parametersPanel, javax.swing.BoxLayout.Y_AXIS));

        parametersSettingsPanel.setMinimumSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),130));
        parametersSettingsPanel.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),130));

        separator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        parametersFileTextField.setText("BTRM-WSNparameters.txt");

        parametersFileLabel.setText("Parameters file");

        parametersSourceButtonGroup.add(customizedParametersRadioButton);
        customizedParametersRadioButton.setText("Customized");
        customizedParametersRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        customizedParametersRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        parametersSourceButtonGroup.add(parametersFileRadioButton);
        parametersFileRadioButton.setSelected(true);
        parametersFileRadioButton.setText("File");
        parametersFileRadioButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        parametersFileRadioButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        parametersFileRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                parametersFileRadioButtonItemStateChanged(evt);
            }
        });

        parametersSourceLabel.setText("Parameters source");

        applyParametersChangesButton.setText("Apply changes");
        applyParametersChangesButton.setEnabled(false);
        applyParametersChangesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyParametersChangesButtonActionPerformed(evt);
            }
        });

        saveParametersFileContentButton.setText("Save file content");
        saveParametersFileContentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveParametersFileContentButtonActionPerformed(evt);
            }
        });

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout parametersSettingsPanelLayout = new javax.swing.GroupLayout(parametersSettingsPanel);
        parametersSettingsPanel.setLayout(parametersSettingsPanelLayout);
        parametersSettingsPanelLayout.setHorizontalGroup(
            parametersSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parametersSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(parametersSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(parametersSettingsPanelLayout.createSequentialGroup()
                        .addGroup(parametersSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(parametersSourceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customizedParametersRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(parametersSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(parametersFileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(parametersSettingsPanelLayout.createSequentialGroup()
                                .addComponent(parametersFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(browseButton))))
                    .addGroup(parametersSettingsPanelLayout.createSequentialGroup()
                        .addComponent(applyParametersChangesButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveParametersFileContentButton))
                    .addComponent(separator2)
                    .addComponent(parametersFileRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(589, Short.MAX_VALUE))
        );
        parametersSettingsPanelLayout.setVerticalGroup(
            parametersSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parametersSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(parametersSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(parametersSettingsPanelLayout.createSequentialGroup()
                        .addComponent(parametersSourceLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(parametersFileRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customizedParametersRadioButton))
                    .addGroup(parametersSettingsPanelLayout.createSequentialGroup()
                        .addComponent(parametersFileLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(parametersSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(parametersFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(browseButton)))
                    .addComponent(separator1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(parametersSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(applyParametersChangesButton)
                    .addComponent(saveParametersFileContentButton))
                .addContainerGap())
        );

        parametersPanel.add(parametersSettingsPanel);
        parametersPanel.add(separator3);

        bottomParametersContainerPanel.setLayout(new javax.swing.BoxLayout(bottomParametersContainerPanel, javax.swing.BoxLayout.LINE_AXIS));

        bottomParametersSplitPane.setBorder(null);
        bottomParametersSplitPane.setDividerLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.5));
        bottomParametersSplitPane.setDividerSize(3);

        TRMParametersScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Trust & Reputation model parameters"));

        javax.swing.GroupLayout TRM_ParametersPanelAuxLayout = new javax.swing.GroupLayout(TRM_ParametersPanelAux);
        TRM_ParametersPanelAux.setLayout(TRM_ParametersPanelAuxLayout);
        TRM_ParametersPanelAuxLayout.setHorizontalGroup(
            TRM_ParametersPanelAuxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 747, Short.MAX_VALUE)
        );
        TRM_ParametersPanelAuxLayout.setVerticalGroup(
            TRM_ParametersPanelAuxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );

        TRMParametersScrollPane.setViewportView(TRM_ParametersPanelAux);

        bottomParametersSplitPane.setLeftComponent(TRMParametersScrollPane);

        parametersFileContentScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters file content"));

        parametersFileContentTextArea.setColumns(20);
        parametersFileContentTextArea.setRows(5);
        parametersFileContentTextArea.setAutoscrolls(false);
        parametersFileContentScrollPane.setViewportView(parametersFileContentTextArea);

        bottomParametersSplitPane.setRightComponent(parametersFileContentScrollPane);

        bottomParametersContainerPanel.add(bottomParametersSplitPane);

        parametersPanel.add(bottomParametersContainerPanel);

        separator4.setMinimumSize(new java.awt.Dimension(0, 11));
        separator4.setPreferredSize(new java.awt.Dimension(50, 11));
        parametersPanel.add(separator4);

        tabbedPane.addTab("Parameters", parametersPanel);

        getContentPane().add(tabbedPane);

        menuBar.setPreferredSize(new java.awt.Dimension(300, 20));

        wsnMenu.setText("WSN");

        newWSNmenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newWSNmenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/new.gif")));
        newWSNmenuItem.setText("New WSN");
        newWSNmenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newWSNmenuItemActionPerformed(evt);
            }
        });
        wsnMenu.add(newWSNmenuItem);

        resetWSNmenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/reset.gif")));
        resetWSNmenuItem.setText("Reset WSN");
        resetWSNmenuItem.setEnabled(false);
        resetWSNmenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetWSNmenuItemActionPerformed(evt);
            }
        });
        wsnMenu.add(resetWSNmenuItem);

        loadWSNmenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadWSNmenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/load.gif")));
        loadWSNmenuItem.setText("Load WSN");
        loadWSNmenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadWSNmenuItemActionPerformed(evt);
            }
        });
        wsnMenu.add(loadWSNmenuItem);

        saveWSNmenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveWSNmenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/save.gif")));
        saveWSNmenuItem.setText("Save WSN");
        saveWSNmenuItem.setEnabled(false);
        saveWSNmenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveWSNmenuItemActionPerformed(evt);
            }
        });
        wsnMenu.add(saveWSNmenuItem);

        menuBar.add(wsnMenu);

        simulationsMenu.setText("Simulations");

        runTRMmenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/run.gif")));
        runTRMmenuItem.setText("Run T&R Model");
        runTRMmenuItem.setEnabled(false);
        runTRMmenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runTRMmenuItemActionPerformed(evt);
            }
        });
        simulationsMenu.add(runTRMmenuItem);

        stopTRMmenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/stop.gif")));
        stopTRMmenuItem.setText("Stop T&R Model");
        stopTRMmenuItem.setEnabled(false);
        stopTRMmenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopTRMmenuItemActionPerformed(evt);
            }
        });
        simulationsMenu.add(stopTRMmenuItem);

        runSimulationsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        runSimulationsMenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/run.gif")));
        runSimulationsMenuItem.setText("Run simulations");
        runSimulationsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runSimulationsMenuItemActionPerformed(evt);
            }
        });
        simulationsMenu.add(runSimulationsMenuItem);

        stopSimulationsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        stopSimulationsMenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/stop.gif")));
        stopSimulationsMenuItem.setText("Stop simulations");
        stopSimulationsMenuItem.setEnabled(false);
        stopSimulationsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopSimulationsMenuItemActionPerformed(evt);
            }
        });
        simulationsMenu.add(stopSimulationsMenuItem);

        menuBar.add(simulationsMenu);

        parametersMenu.setText("Parameters");

        loadParametersMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/load.gif"))); // NOI18N
        loadParametersMenuItem.setText("Load parameters");
        loadParametersMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadParametersMenuItemActionPerformed(evt);
            }
        });
        parametersMenu.add(loadParametersMenuItem);

        saveParametersMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/save.gif"))); // NOI18N
        saveParametersMenuItem.setText("Save parameters");
        saveParametersMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveParametersMenuItemActionPerformed(evt);
            }
        });
        parametersMenu.add(saveParametersMenuItem);

        applyParametersChangesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/apply.gif"))); // NOI18N
        applyParametersChangesMenuItem.setText("Apply changes");
        applyParametersChangesMenuItem.setEnabled(false);
        applyParametersChangesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyParametersChangesMenuItemActionPerformed(evt);
            }
        });
        parametersMenu.add(applyParametersChangesMenuItem);

        menuBar.add(parametersMenu);

        TRModelMenu.setText("T&R Model");
        menuBar.add(TRModelMenu);

        helpMenu.setText("Help");

        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/help.gif")));
        helpMenuItem.setText("Help");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        aboutTRMSim_WSNmenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        aboutTRMSim_WSNmenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/about.gif")));
        aboutTRMSim_WSNmenuItem.setText("About TRMSim-WSN");
        aboutTRMSim_WSNmenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutTRMSim_WSNmenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutTRMSim_WSNmenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aboutTRMSim_WSNmenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutTRMSim_WSNmenuItemActionPerformed
        AboutWindow aboutWindow = new AboutWindow();
        aboutWindow.setVisible(true);
    }//GEN-LAST:event_aboutTRMSim_WSNmenuItemActionPerformed

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.setVisible(true);
    }//GEN-LAST:event_helpMenuItemActionPerformed

    private void stopSimulationsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopSimulationsMenuItemActionPerformed
        this.stopSimulationsButtonActionPerformed(evt);
    }//GEN-LAST:event_stopSimulationsMenuItemActionPerformed

    private void runSimulationsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runSimulationsMenuItemActionPerformed
        try {
            if (!parametersFileRadioButton.isSelected())
                set_TRMParameters();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        this.runSimulationsButtonActionPerformed(evt);
    }//GEN-LAST:event_runSimulationsMenuItemActionPerformed

    private void saveWSNmenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveWSNmenuItemActionPerformed
        this.saveWSNButtonActionPerformed(evt);
    }//GEN-LAST:event_saveWSNmenuItemActionPerformed

    private void loadWSNmenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadWSNmenuItemActionPerformed
        this.loadWSNButtonActionPerformed(evt);
    }//GEN-LAST:event_loadWSNmenuItemActionPerformed

    private void stopTRMmenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopTRMmenuItemActionPerformed
        this.stopTRMButtonActionPerformed(evt);
    }//GEN-LAST:event_stopTRMmenuItemActionPerformed

    private void runTRMmenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runTRMmenuItemActionPerformed
        try {
            if (!parametersFileRadioButton.isSelected())
                set_TRMParameters();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        this.runTRMButtonActionPerformed(evt);
    }//GEN-LAST:event_runTRMmenuItemActionPerformed

    private void resetWSNmenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetWSNmenuItemActionPerformed
        this.resetWSNButtonActionPerformed(evt);
    }//GEN-LAST:event_resetWSNmenuItemActionPerformed

    private void newWSNmenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWSNmenuItemActionPerformed
        this.newWSNButtonActionPerformed(evt);
    }//GEN-LAST:event_newWSNmenuItemActionPerformed

    private void TRModelComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_TRModelComboBoxItemStateChanged
        try {
            String trModelName = (String)TRModelComboBox.getSelectedItem();
            String defaultParametersFileName = "";
            String packageName = "es.ants.felixgm.trmsim_wsn.";
            
            for (int i = 0; i < TRModelMenu.getItemCount(); i++) {
                JMenuItem trmMenuItem = TRModelMenu.getItem(i);
                trmMenuItem.setIcon(null);
                if (trmMenuItem.getText().equals(trModelName))
                    trmMenuItem.setIcon(new ImageIcon(ClassLoader.getSystemResource("resources/images/tick.gif")));
            }

            C.set_TRModel_WSN(trModelName);
            TRMParametersPanel trmParametersPanel = (TRMParametersPanel)Class.forName(packageName+"gui.parameterpanels."+trModelName+"_ParametersPanel").newInstance();
            trmParametersPanel.set_TRMParameters(C.get_TRMParameters());
            set_TRMParametersPanel(trmParametersPanel);
            set_TRMParameters();
            defaultParametersFileName = (String)Class.forName(packageName+"trm."+trModelName.toLowerCase()+"."+trModelName+"_Parameters").getDeclaredField("defaultParametersFileName").get(null);
            defaultParametersFileName = defaultParametersFileName.substring(defaultParametersFileName.lastIndexOf(File.separator)+1);
            parametersFileTextField.setText(defaultParametersFileName);
            parametersFileContentTextArea.setText(C.get_DefaultParametersFileContent(trModelName));
            
            legendPanelContainer.removeAll();
            networkPanelContainer.removeAll();
            outcomesTabbedPane.removeAll();
            outcomesPanels = new ArrayList<OutcomesPanel>();
            if (trModelName.equals(BTRM_WSN.get_name())) {
                percentageClientsLabel.setEnabled(true);
                percentageClientsSlider.setEnabled(true);
                percentageClientsTextField.setEnabled(true);

                legendPanel = new LegendPanel();
                networkPanel = new NetworkPanel();

                outcomesPanels.add(new AccuracyPanel());
                outcomesPanels.add(new PathLengthPanel());
                outcomesPanels.add(new EnergyConsumptionPanel());

            } else if (trModelName.equals(EigenTrust.get_name())){
                percentageClientsLabel.setEnabled(false);
                percentageClientsSlider.setEnabled(false);
                percentageClientsTextField.setEnabled(false);

                legendPanel = new EigenTrustLegendPanel();
                networkPanel = new EigenTrustNetworkPanel();

                outcomesPanels.add(new AccuracyPanel());
                outcomesPanels.add(new PathLengthPanel());
                outcomesPanels.add(new EigenTrustEnergyConsumptionPanel());

            } else if (trModelName.equals(PeerTrust.get_name())) {
                percentageClientsLabel.setEnabled(true);
                percentageClientsSlider.setEnabled(true);
                percentageClientsTextField.setEnabled(true);

                legendPanel = new LegendPanel();
                networkPanel = new NetworkPanel();

                outcomesPanels.add(new AccuracyPanel());
                outcomesPanels.add(new PathLengthPanel());
                outcomesPanels.add(new EnergyConsumptionPanel());

            } else if (trModelName.equals(PowerTrust.get_name())){
                percentageClientsLabel.setEnabled(true);
                percentageClientsSlider.setEnabled(true);
                percentageClientsTextField.setEnabled(true);

                legendPanel = new PowerTrustLegendPanel();
                networkPanel = new PowerTrustNetworkPanel();

                outcomesPanels.add(new AccuracyPanel());
                outcomesPanels.add(new PathLengthPanel());
                outcomesPanels.add(new PowerTrustEnergyConsumptionPanel());

            } else if (trModelName.equals(LFTM.get_name())){
                percentageClientsLabel.setEnabled(true);
                percentageClientsSlider.setEnabled(true);
                percentageClientsTextField.setEnabled(true);

                legendPanel = new LegendPanel();
                networkPanel = new NetworkPanel();

                outcomesPanels.add(new AccuracyPanel());
                outcomesPanels.add(new PathLengthPanel());
                outcomesPanels.add(new EnergyConsumptionPanel());
                outcomesPanels.add(new LFTM_SatisfactionPanel());

            } else if (trModelName.equals(TRIP.get_name())){
                percentageClientsLabel.setEnabled(false);
                percentageClientsSlider.setEnabled(false);
                percentageClientsTextField.setEnabled(false);

                legendPanel = new TRIPLegendPanel();
                networkPanel = new TRIPNetworkPanel();

                outcomesPanels.add(new AccuracyPanel());
                outcomesPanels.add(new PathLengthPanel());
                outcomesPanels.add(new EnergyConsumptionPanel());

            } else if (trModelName.equals(TemplateTRM.get_name())){
                percentageClientsLabel.setEnabled(true);
                percentageClientsSlider.setEnabled(true);
                percentageClientsTextField.setEnabled(true);

                legendPanel = new LegendPanel();
                networkPanel = new NetworkPanel();
                
                outcomesPanels.add(new AccuracyPanel());
                outcomesPanels.add(new PathLengthPanel());
            }

            legendPanelContainer.add(legendPanel,null);
            legendPanel.setBackground(Color.white);
            legendPanel.setSize(legendPanelContainer.getSize());
            legendPanel.plotLegend();

            networkPanelContainer.add(networkPanel);
            networkPanel.setBackground(Color.white);
            networkPanel.setSize(networkPanelContainer.getSize());

            for (OutcomesPanel outcomesPanel : outcomesPanels) {
                outcomesTabbedPane.addTab(outcomesPanel.getLabel(), outcomesPanel);
                outcomesPanel.setSize(outcomesTabbedPane.getSize());
            }

            resetWSNButton.setEnabled(false);
            runTRMButton.setEnabled(false);
            saveWSNButton.setEnabled(false);
            resetWSNmenuItem.setEnabled(false);
            runTRMmenuItem.setEnabled(false);
            saveWSNmenuItem.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }        
    }//GEN-LAST:event_TRModelComboBoxItemStateChanged

    private void set_TRMParametersPanel(TRMParametersPanel trmParametersPanel) {
        TRM_ParametersPanel = trmParametersPanel;
        TRM_ParametersPanelAux = TRM_ParametersPanel;
        TRMParametersScrollPane.setViewportView(TRM_ParametersPanelAux);
    }
    
    private void stopTRMButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopTRMButtonActionPerformed
        try {
            C.stopSimulations();
            simulationComponentsEnabling(false);
            stopTRMButton.setEnabled(false);
            stopTRMmenuItem.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_stopTRMButtonActionPerformed

    private void stopSimulationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopSimulationsButtonActionPerformed
        try {
            C.stopSimulations();
            simulationComponentsEnabling(false);
            stopSimulationsButton.setEnabled(false);
            stopSimulationsMenuItem.setEnabled(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }        
    }//GEN-LAST:event_stopSimulationsButtonActionPerformed

    private void loadWSNButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadWSNButtonActionPerformed
        try {
            JFileChooser fileChooser = new JFileChooser("./wsn");
            fileChooser.setDialogTitle("Load WSN");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter(){
                   public boolean accept(java.io.File f) { return (f.isDirectory() || f.getName().toLowerCase().endsWith(".xml")); }
                   public String getDescription() { return "XML Files"; }
            });
            fileChooser.showOpenDialog(this);
            
            Network network = null;
            if (fileChooser.getSelectedFile() != null) {
                network = C.loadCurrentNetwork(fileChooser.getSelectedFile().getCanonicalPath());
                if (network != null) {
                    JOptionPane.showMessageDialog(this,"WSN loaded successfully","Info",JOptionPane.INFORMATION_MESSAGE);
                    paintNetwork(network,C.get_requiredService());
                    saveWSNButton.setEnabled(true);
                    saveWSNmenuItem.setEnabled(true);
                    resetWSNButton.setEnabled(true);
                    resetWSNmenuItem.setEnabled(true);
                    runTRMButton.setEnabled(true);
                    runTRMmenuItem.setEnabled(true);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_loadWSNButtonActionPerformed

    private void saveWSNButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveWSNButtonActionPerformed
        try {
            JFileChooser fileChooser = new JFileChooser("./wsn");
            fileChooser.setDialogTitle("Save WSN");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter(){
                   public boolean accept(java.io.File f) { return (f.isDirectory() || f.getName().toLowerCase().endsWith(".xml")); }
                   public String getDescription() { return "XML Files"; }
            });
            fileChooser.showSaveDialog(this);
            if (fileChooser.getSelectedFile() != null){
                C.saveCurrentNetwork(fileChooser.getSelectedFile().getCanonicalPath());
                JOptionPane.showMessageDialog(this,"WSN saved successfully","Info",JOptionPane.INFORMATION_MESSAGE);
                paintNetwork(C.get_currentNetwork(),C.get_requiredService());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }
    }//GEN-LAST:event_saveWSNButtonActionPerformed

    private void percentageMaliciousServersSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_percentageMaliciousServersSliderStateChanged
        percentageMaliciousServersTextField.setText(String.valueOf(percentageMaliciousServersSlider.getValue()));
    }//GEN-LAST:event_percentageMaliciousServersSliderStateChanged

    private void percentageRelayServersSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_percentageRelayServersSliderStateChanged
        percentageRelayServersTextField.setText(String.valueOf(percentageRelayServersSlider.getValue()));        
    }//GEN-LAST:event_percentageRelayServersSliderStateChanged

    private void percentageClientsSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_percentageClientsSliderStateChanged
        percentageClientsTextField.setText(String.valueOf(percentageClientsSlider.getValue()));
    }//GEN-LAST:event_percentageClientsSliderStateChanged

    /**
     * This method is used in order to communicate the GUI with the Controller
     * @param observable
     * @param arg
     */
    public void update(Observable observable, Object arg) {
        try {
            if (arg instanceof Network)
                paintNetwork((Network)arg,C.get_requiredService());
            else if (arg instanceof Collection) {
                for (OutcomesPanel outcomesPanel : outcomesPanels) {
                    if (outcomesPanel.isShowing())
                        outcomesPanel.plotOutcomes((Collection<Outcome>) arg);
                    else
                        outcomesPanel.setOutcomes((Collection<Outcome>) arg);
                }
            } else if (arg instanceof String) {
                String msg = ((String)arg).replaceFirst("selected TRM",(String)TRModelComboBox.getSelectedItem());
                messagesTextArea.setText(msg+messagesTextArea.getText());
                if (msg.startsWith("Finishing")) {
                    simulationComponentsEnabling(false);
                    stopTRMButton.setEnabled(false);
                    stopTRMmenuItem.setEnabled(false);
                    stopSimulationsButton.setEnabled(false);
                    stopSimulationsMenuItem.setEnabled(false);
                }
            } else if (arg instanceof Exception)
                throw (Exception) arg;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            stopSimulationsButtonActionPerformed(null);
            stopTRMButtonActionPerformed(null);
            ex.printStackTrace();
        } finally {
            C.sleep();
        }
    }
    
    private void simulationComponentsEnabling(boolean enable) {
        runTRMButton.setEnabled(!enable);
        runTRMmenuItem.setEnabled(!enable);
        resetWSNButton.setEnabled(!enable);
        resetWSNmenuItem.setEnabled(!enable);
        saveWSNButton.setEnabled(!enable);
        saveWSNmenuItem.setEnabled(!enable);
        runSimulationsButton.setEnabled(!enable);
        runSimulationsMenuItem.setEnabled(!enable);
        loadWSNButton.setEnabled(!enable);
        loadWSNmenuItem.setEnabled(!enable);
        newWSNButton.setEnabled(!enable);
        newWSNmenuItem.setEnabled(!enable);
        percentageMaliciousServersSlider.setEnabled(!enable);
        percentageMaliciousServersLabel.setEnabled(!enable);
        percentageRelayServersSlider.setEnabled(!enable);
        percentageRelayServersLabel.setEnabled(!enable);
        radioRangeSlider.setEnabled(!enable);
        radioRangeLabel.setEnabled(!enable);
        numExecutionsSpinner.setEnabled(!enable);
        numExecutionsLabel.setEnabled(!enable);
        numNetworksSpinner.setEnabled(!enable);
        numNetworksLabel.setEnabled(!enable);
        minNumSensorsSpinner.setEnabled(!enable);
        minNumSensorsLabel.setEnabled(!enable);
        maxNumSensorsSpinner.setEnabled(!enable);
        maxNumSensorsLabel.setEnabled(!enable);
        dynamicWSNsCheckBox.setEnabled(!enable);
        oscillatingWSNsCheckBox.setEnabled(!enable);
        collusionCheckBox.setEnabled(!enable);
        TRModelLabel.setEnabled(!enable);
        TRModelComboBox.setEnabled(!enable);
        TRModelMenu.setEnabled(!enable);
        sensorPropertiesPanel.setVisible(false);
        if (!((String)TRModelComboBox.getSelectedItem()).equals(EigenTrust.get_name())) {
            percentageClientsSlider.setEnabled(!enable);
            percentageClientsLabel.setEnabled(!enable);
        }
    }
    
    private void runSimulationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runSimulationsButtonActionPerformed
        try {
            int numExecutions = (Integer)numExecutionsSpinner.getValue();
            int numNetworks = (Integer)numNetworksSpinner.getValue();
            int minNumSensors = (Integer)minNumSensorsSpinner.getValue();
            int maxNumSensors = (Integer)maxNumSensorsSpinner.getValue();

            double probClients = percentageClientsSlider.getValue()/(double)percentageClientsSlider.getMaximum();
            double probRelay = percentageRelayServersSlider.getValue()/(double)percentageRelayServersSlider.getMaximum();
            double probMalicious = percentageMaliciousServersSlider.getValue()/(double)percentageMaliciousServersSlider.getMaximum();
            double radioRange = radioRangeSlider.getValue()/(double)radioRangeSlider.getMaximum();

            boolean dynamic = dynamicWSNsCheckBox.isSelected();
            boolean oscillating = oscillatingWSNsCheckBox.isSelected();
            boolean collusion = collusionCheckBox.isSelected();
            C.set_delay(1000*delaySlider.getValue()/delaySlider.getMaximum());
            
            messagesTextArea.setText("Starting simulations at "+(new java.util.Date())+"...\n"+messagesTextArea.getText());
            simulationComponentsEnabling(true);
            stopSimulationsButton.setEnabled(true);
            stopSimulationsMenuItem.setEnabled(true);
            for (OutcomesPanel outcomesPanel : outcomesPanels) {
                outcomesPanel.clearPanel();
                outcomesPanel.drawAxes();
            }

            C.runSimulations(this,minNumSensors,maxNumSensors,
                        probClients,probRelay,probMalicious,radioRange,
                        dynamic,oscillating,collusion,numNetworks,numExecutions);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            stopSimulationsButtonActionPerformed(evt);
            ex.printStackTrace();
        } 
    }//GEN-LAST:event_runSimulationsButtonActionPerformed

    private void showIdsCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showIdsCheckBoxItemStateChanged
        try {
            Network network = C.get_currentNetwork();
            if (network != null) {
                boolean showIds = showIdsCheckBox.isSelected();
                paintNetwork(network, C.get_requiredService());
                if (showIds)
                    messagesTextArea.setText("Showing Ids\n"+messagesTextArea.getText());
                else
                    messagesTextArea.setText("Not showing Ids\n"+messagesTextArea.getText());
            }    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_showIdsCheckBoxItemStateChanged

    private void set_TRMParameters() throws Exception {
        TRM_ParametersPanel.setEnabled(!parametersFileRadioButton.isSelected());
        parametersFileContentTextArea.setEnabled(parametersFileRadioButton.isSelected());
        //Get T&R model's parameters from file
        if (parametersFileRadioButton.isSelected()) {
            TRMParameters trmParameters = C.set_TRMParameters(C.get_parametersFile());
            //Update parameters panel with values from file
            TRM_ParametersPanel.set_TRMParameters(trmParameters);
        } else
            //Get T&R model's parameters from panel
            C.set_TRMParameters(TRM_ParametersPanel);
    }

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        try {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setDialogTitle("Parameters file selection");
            if ((fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) &&
                (fileChooser.getSelectedFile() != null)) {
                parametersFileTextField.setText(fileChooser.getSelectedFile().getName());
                C.set_parametersFile(fileChooser.getSelectedFile().getCanonicalPath());
                set_TRMParameters();
                parametersFileContentTextArea.setText(C.get_ParametersFileContent());
                JOptionPane.showMessageDialog(this,"Parameters loaded successfully from file","Success",JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }                
    }//GEN-LAST:event_browseButtonActionPerformed

    private void parametersFileRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_parametersFileRadioButtonItemStateChanged
        parametersFileLabel.setEnabled(parametersFileRadioButton.isSelected());
        parametersFileTextField.setEnabled(parametersFileRadioButton.isSelected());
        browseButton.setEnabled(parametersFileRadioButton.isSelected());
        parametersFileContentTextArea.setEnabled(parametersFileRadioButton.isSelected());
        saveParametersFileContentButton.setEnabled(parametersFileRadioButton.isSelected());
        loadParametersMenuItem.setEnabled(parametersFileRadioButton.isSelected());
        saveParametersMenuItem.setEnabled(parametersFileRadioButton.isSelected());

        TRM_ParametersPanel.setEnabled(!parametersFileRadioButton.isSelected());
        applyParametersChangesButton.setEnabled(!parametersFileRadioButton.isSelected());
        applyParametersChangesMenuItem.setEnabled(!parametersFileRadioButton.isSelected());
    }//GEN-LAST:event_parametersFileRadioButtonItemStateChanged

    private void runTRMButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runTRMButtonActionPerformed
        try {
            int numExecutions = (Integer)numExecutionsSpinner.getValue();
            boolean dynamic = dynamicWSNsCheckBox.isSelected();
            boolean oscillating = oscillatingWSNsCheckBox.isSelected();
            boolean collusion = collusionCheckBox.isSelected();
            C.set_delay(1000*delaySlider.getValue()/delaySlider.getMaximum());
            
            simulationComponentsEnabling(true);
            stopTRMButton.setEnabled(true);
            stopTRMmenuItem.setEnabled(true);
            for (OutcomesPanel outcomesPanel : outcomesPanels) {
                outcomesPanel.clearPanel();
                outcomesPanel.drawAxes();
            }
            C.runTRM_WSN(this, dynamic, oscillating, collusion, numExecutions);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            stopTRMButtonActionPerformed(evt);
            ex.printStackTrace();
        }                
    }//GEN-LAST:event_runTRMButtonActionPerformed

    private void resetWSNButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetWSNButtonActionPerformed
        try {
            C.resetCurrentNetwork();
            messagesTextArea.setText("Current WSN reset\n"+messagesTextArea.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }                
    }//GEN-LAST:event_resetWSNButtonActionPerformed

    private void showLinksCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showLinksCheckBoxItemStateChanged
        try {
            Network network = C.get_currentNetwork();
            if (network != null) {
                boolean showLinks = showLinksCheckBox.isSelected();
                paintNetwork(network, C.get_requiredService());
                if (showLinks)
                    messagesTextArea.setText("Showing links\n"+messagesTextArea.getText());
                else
                    messagesTextArea.setText("Not showing links\n"+messagesTextArea.getText());
            }    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }        
    }//GEN-LAST:event_showLinksCheckBoxItemStateChanged

    private void showRangesCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showRangesCheckBoxItemStateChanged
        try {
            Network network = C.get_currentNetwork();
            if (network != null) {
                boolean showRanges = showRangesCheckBox.isSelected();
                paintNetwork(network, C.get_requiredService());
                if (showRanges)
                    messagesTextArea.setText("Showing ranges\n"+messagesTextArea.getText());
                else
                    messagesTextArea.setText("Not showing ranges\n"+messagesTextArea.getText());
            }    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }        
    }//GEN-LAST:event_showRangesCheckBoxItemStateChanged

    private void delaySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_delaySliderStateChanged
        delayTextField.setText(String.valueOf(delaySlider.getValue()));
        C.set_delay(1000*delaySlider.getValue()/delaySlider.getMaximum());
    }//GEN-LAST:event_delaySliderStateChanged

    private void radioRangeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_radioRangeSliderStateChanged
        try {
            radioRangeTextField.setText(String.valueOf(radioRangeSlider.getValue()));
            double radioRange = radioRangeSlider.getValue()/(double)radioRangeSlider.getMaximum();
            Network network = C.setNewNeighborsNetwork(radioRange);
            if (network != null)
                paintNetwork(network, C.get_requiredService());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }        
    }//GEN-LAST:event_radioRangeSliderStateChanged

    private void newWSNButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWSNButtonActionPerformed
        try {
            int minNumSensors = (Integer)minNumSensorsSpinner.getValue();
            int maxNumSensors = (Integer)maxNumSensorsSpinner.getValue();

            double probClients = percentageClientsSlider.getValue()/(double)percentageClientsSlider.getMaximum();
            double probRelay = percentageRelayServersSlider.getValue()/(double)percentageRelayServersSlider.getMaximum();
            double probMalicious = percentageMaliciousServersSlider.getValue()/(double)percentageMaliciousServersSlider.getMaximum();
            double radioRange = radioRangeSlider.getValue()/(double)radioRangeSlider.getMaximum();

            boolean dynamic = dynamicWSNsCheckBox.isSelected();
            boolean oscillating = oscillatingWSNsCheckBox.isSelected();
            boolean collusion = collusionCheckBox.isSelected();
            
            C.set_delay(1000*delaySlider.getValue()/delaySlider.getMaximum());

            Network network = C.createNewNetwork(minNumSensors,maxNumSensors,
                    probClients,probRelay,probMalicious,radioRange,
                    dynamic, oscillating, collusion);
            
            if (network != null) {
                paintNetwork(network, C.get_requiredService());
                resetWSNButton.setEnabled(true);
                resetWSNmenuItem.setEnabled(true);
                runTRMButton.setEnabled(true);
                runTRMmenuItem.setEnabled(true);
                saveWSNButton.setEnabled(true);
                saveWSNmenuItem.setEnabled(true);
                sensorPropertiesPanel.setVisible(false);
                messagesTextArea.setText("New WSN created\n"+messagesTextArea.getText());
                for (OutcomesPanel outcomesPanel : outcomesPanels) {
                    outcomesPanel.setOutcomes(null);
                    if (outcomesPanel.isShowing()) {
                        outcomesPanel.clearPanel();
                        outcomesPanel.drawAxes();
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_newWSNButtonActionPerformed

    private void minNumSensorsSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_minNumSensorsSpinnerStateChanged
        int minNumSensors = (Integer)minNumSensorsSpinner.getValue();
        int maxNumSensors = (Integer)maxNumSensorsSpinner.getValue();
        if (minNumSensors > maxNumSensors)
            maxNumSensorsSpinner.setValue(minNumSensors);
    }//GEN-LAST:event_minNumSensorsSpinnerStateChanged

    private void maxNumSensorsSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_maxNumSensorsSpinnerStateChanged
        int minNumSensors = (Integer)minNumSensorsSpinner.getValue();
        int maxNumSensors = (Integer)maxNumSensorsSpinner.getValue();
        if (minNumSensors > maxNumSensors)
            minNumSensorsSpinner.setValue(maxNumSensors);
    }//GEN-LAST:event_maxNumSensorsSpinnerStateChanged

    private void saveParametersFileContentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveParametersFileContentButtonActionPerformed
        try {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            fileChooser.setDialogTitle("Parameters file selection");
            if ((fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) &&
                (fileChooser.getSelectedFile() != null)) {
                C.saveParametersFileContent(fileChooser.getSelectedFile().getAbsolutePath(), parametersFileContentTextArea.getText());
                JOptionPane.showMessageDialog(this,"Parameters file successfully saved","Success",JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_saveParametersFileContentButtonActionPerformed

    private void applyParametersChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyParametersChangesButtonActionPerformed
        try {
            C.set_TRMParameters(TRM_ParametersPanel);
            TRM_ParametersPanel.set_TRMParameters(C.get_TRMParameters());
            parametersFileContentTextArea.setText(C.get_TRMParameters().toString());
            JOptionPane.showMessageDialog(this,"Parameters changes successfully saved","Success",JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex, "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_applyParametersChangesButtonActionPerformed

    private void loadParametersMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadParametersMenuItemActionPerformed
        browseButtonActionPerformed(evt);
    }//GEN-LAST:event_loadParametersMenuItemActionPerformed

    private void saveParametersMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveParametersMenuItemActionPerformed
        saveParametersFileContentButtonActionPerformed(evt);
    }//GEN-LAST:event_saveParametersMenuItemActionPerformed

    private void applyParametersChangesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyParametersChangesMenuItemActionPerformed
        applyParametersChangesButtonActionPerformed(evt);
    }//GEN-LAST:event_applyParametersChangesMenuItemActionPerformed

    private void networkPanelContainerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_networkPanelContainerMouseClicked
        try {
            if (!C.isSimulationRunning()) {
                int x = evt.getX();
                int y = evt.getY();

                Point coordinate = networkPanel.getCoordinateAtPosition(x, y);
                Sensor sensor = C.getSensorAtCoordinate(coordinate.getX(), coordinate.getY());
                if (sensor != null) {
                    sensorPropertiesPanel.setVisible(true);
                    sensorIdTextField.setText(String.valueOf(sensor.id()));
                    xCoordinateTextField.setText(String.valueOf((int)sensor.getX()));
                    yCoordinateTextField.setText(String.valueOf((int)sensor.getY()));
                    neighborsLabel.setText(sensor.getNeighbors().size()+" Neighbor(s)");
                    //javax.swing.DefaultListModel defaultListModel = new javax.swing.DefaultListModel();
                    Vector<String> neighborsIDs = new Vector<String>();
                    for (Sensor neighbor : sensor.getNeighbors()) {
                        neighborsIDs.add(String.valueOf(neighbor.id()));
                        //defaultListModel.addElement(neighbor.id());
                    }
                    //neighborsList.setModel(defaultListModel);
                    neighborsList.setListData(neighborsIDs);
                    neighborsScrollPane.setViewportView(neighborsList);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }        
    }//GEN-LAST:event_networkPanelContainerMouseClicked

    private void applyChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyChangesButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_applyChangesButtonActionPerformed

    private void hideSensorPropertiesPanelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideSensorPropertiesPanelButtonActionPerformed
        sensorPropertiesPanel.setVisible(false);
    }//GEN-LAST:event_hideSensorPropertiesPanelButtonActionPerformed

    private void showGridCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_showGridCheckBoxItemStateChanged
        try {
            Network network = C.get_currentNetwork();
            boolean showGrid = showGridCheckBox.isSelected();
            paintNetwork(network, C.get_requiredService());
            if (showGrid)
                messagesTextArea.setText("Showing grid\n"+messagesTextArea.getText());
            else
                messagesTextArea.setText("Not showing grid\n"+messagesTextArea.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }        
    }//GEN-LAST:event_showGridCheckBoxItemStateChanged

    private void neighborsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_neighborsListMouseClicked
        if (evt.getClickCount() == 2) {
            Sensor sensor = C.getSensor(Integer.valueOf((String)neighborsList.getSelectedValue()));
            sensorIdTextField.setText(String.valueOf(sensor.id()));
            xCoordinateTextField.setText(String.valueOf((int)sensor.getX()));
            yCoordinateTextField.setText(String.valueOf((int)sensor.getY()));
            Vector<String> neighborsIDs = new Vector<String>();
            for (Sensor neighbor : sensor.getNeighbors()) {
                neighborsIDs.add(String.valueOf(neighbor.id()));
            }
            neighborsList.setListData(neighborsIDs);
            neighborsScrollPane.setViewportView(neighborsList);
        }
    }//GEN-LAST:event_neighborsListMouseClicked
    
    /**
     * This method plots a Wireless Sensor Network
     * @param network Wireless Sensor Network to be plotted
     * @param requiredService Service requested by the clients (needed in order to paint and
     * distinguish benevolent and malicious servers)
     * @throws Exception If any error occurs while plotting a WSN
     */
    protected void paintNetwork(Network network, Service requiredService) throws Exception {
        double radioRange = radioRangeSlider.getValue()/(double)radioRangeSlider.getMaximum();
        boolean showRanges = showRangesCheckBox.isSelected();
        boolean showLinks = showLinksCheckBox.isSelected();
        boolean showIds = showIdsCheckBox.isSelected();
        boolean showGrid = showGridCheckBox.isSelected();

        if (networkPanel.isShowing())
            networkPanel.paintNetwork(network, requiredService, radioRange, showRanges, showLinks, showIds, showGrid);

        C.sleep();
    }
    
    /**
     * Main method
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        boolean verbose = false;

        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-v"))
                verbose = true;

        if (verbose) {
            String trustModelNames[] = {BTRM_WSN.get_name(), LFTM.get_name()};
            Service requiredService = new Service("My Service");
            int numNetworks = 100;
            int numExecutions = 100;
            int minNumSensors = 100;
            int maxNumSensors = 500;
            double probClients = 0.15;
            double probRelay = 0.05;
            double probMalicious = 0.5;
            boolean dynamic = false;
            boolean oscillating = false;
            boolean collusion = false;
            TRMSim_WSN_Verbose(trustModelNames, requiredService, numNetworks, numExecutions, minNumSensors, maxNumSensors, probClients, probRelay, probMalicious, dynamic, oscillating, collusion);
        } else
            TRMSim_WSN_GUI();
    }

    /**
     * This method displays the graphic user interface of the simulator
     */
    public static void TRMSim_WSN_GUI() {
        try {
            SplashScreen splashScreen = new SplashScreen(new ImageIcon(ClassLoader.getSystemResource("resources/images/splashScreenImage.gif")));
            splashScreen.setScreenVisible(true);
            splashScreen.setProgress("Please wait while loading TRMSim-WSN " + CURRENT_VERSION + "...", -1);
            Thread.sleep(2000+((int)Math.random()*2000));
            splashScreen.setScreenVisible(false);
            
            
            /*java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {*/
                    TRMSim_WSN trmsim_wsn = new TRMSim_WSN();
                    trmsim_wsn.setVisible(true);
                /*}
            });*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method executes the simulator in verbose mode
     * @param trustModelNames Trust and reputation model names
     * @param requiredService Service requested by every client of each WSN
     * @param numNetworks Number of wireless sensor networks to test
     * @param numExecutions Number of service requests of every client composing each WSN
     * @param minNumSensors Minimum number of sensors composing every WSN
     * @param maxNumSensors Maximum number of sensors composing every WSN
     * @param probClients The probability of a node to act as a client
     * @param probRelay The probability of a server to act just as a relay node (not offering the required service)
     * @param probMalicious The probability of a server offering the required service to act as a
     * malicious server (not providing the offered service, or providing a worse or different one)
     * @param dynamic It determines if the WSN will be dynamic (nodes sometimes switch off in order to save battery, breaking all their links)
     * @param oscillating It determines if the goodness of the servers belonging to the created WSN will change along the time
     * @param collusion It determines if the malicious servers belonging to the created WSN will form a collusion among them
     */
    public static void TRMSim_WSN_Verbose(String trustModelNames[], Service requiredService,
            int numNetworks, int numExecutions,
            int minNumSensors, int maxNumSensors,
            double probClients, double probRelay, double probMalicious,
            boolean dynamic, boolean oscillating, boolean collusion) {
        System.out.println("Executing TRMSim-WSN "+CURRENT_VERSION+" in verbose mode... ["+(new java.util.Date())+"]\n");
        try {
            Collection<Outcome> outcomes;
            int avgLinksPerSensor = 5;

            for (int i = 0; i < trustModelNames.length; i++) {
                String trustModelName = trustModelNames[i];
                String outcomesFileName = trustModelName+"_outcomes";
                for (int numSensors = minNumSensors; numSensors <= maxNumSensors; numSensors += 100) {
                    String _outcomesFileName = outcomesFileName+"_"+numSensors+".txt";
                    outcomes = new ArrayList<Outcome>();
                    double radioRange = Math.sqrt(avgLinksPerSensor/(2.0*Math.PI*numSensors));
                    for (double _probMalicious = probMalicious; _probMalicious < 0.9001; _probMalicious += 0.1) {
                        System.out.println("Running "+trustModelName+"; Ns = "+numSensors+"; %Mal = "+_probMalicious);System.out.flush();

                        Outcome outcome = runTRMSim_WSN(trustModelName, requiredService,
                                numNetworks, numExecutions,
                                minNumSensors, maxNumSensors,
                                probClients, probRelay, _probMalicious,
                                radioRange,
                                dynamic, oscillating, collusion);

                        if (outcome != null)
                            outcomes.add(outcome);

                        Outcome.writeToFile(outcomes, _outcomesFileName);
                    }
                }
                System.out.println();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method executes the specified trust and reputation model using the given parameters
     * @param trustModelName Trust and reputation model name
     * @param requiredService Service requested by every client of each WSN
     * @param numNetworks Number of wireless sensor networks to test
     * @param numExecutions Number of service requests of every client composing each WSN
     * @param minNumSensors Minimum number of sensors composing every WSN
     * @param maxNumSensors Maximum number of sensors composing every WSN
     * @param probClients The probability of a node to act as a client
     * @param probRelay The probability of a server to act just as a relay node (not offering the required service)
     * @param probMalicious The probability of a server offering the required service to act as a
     * malicious server (not providing the offered service, or providing a worse or different one)
     * @param radioRange Maximum wireless range of every sensor. It determines the neighborhood of every sensor
     * @param dynamic It determines if the WSN will be dynamic (nodes sometimes switch off in order to save battery, breaking all their links)
     * @param oscillating It determines if the goodness of the servers belonging to the created WSN will change along the time
     * @param collusion It determines if the malicious servers belonging to the created WSN will form a collusion among them
     * @return The outcome of the executed trust and reputation model
     * @throws Exception If the specified trust and reputation model name is not correct or any other error occurs
     */
    public static Outcome runTRMSim_WSN(String trustModelName, Service requiredService,
            int numNetworks, int numExecutions,
            int minNumSensors, int maxNumSensors,
            double probClients, double probRelay, double probMalicious,
            double radioRange,
            boolean dynamic, boolean oscillating, boolean collusion) throws Exception {
        TRMParameters trm_parameters = null;
        String parametersFile;
        TRModel_WSN trmodel_wsn;
        Network network = null;
        Collection<Outcome> globalOutcomes = new ArrayList<Outcome>();
        String packageName = "es.ants.felixgm.trmsim_wsn.trm."+trustModelName.toLowerCase()+".";

        parametersFile = (String)Class.forName(packageName+trustModelName+"_Parameters").getDeclaredField("defaultParametersFileName").get(null);
        Class[] trmParametersConstructorParametersTypes = {Class.forName("java.lang.String")};
        Object[] trmParametersConstructorParametersValues = {parametersFile};
        try {
            trm_parameters = (TRMParameters) Class.forName(packageName+trustModelName+"_Parameters").getConstructor(trmParametersConstructorParametersTypes).newInstance(trmParametersConstructorParametersValues);
        } catch (java.lang.reflect.InvocationTargetException ex) {
            trm_parameters = (TRMParameters) Class.forName(packageName+trustModelName+"_Parameters").newInstance();
            ex.printStackTrace();
        }
        Class[] trmodel_wsnConstructorParametersTypes = {Class.forName(packageName+trustModelName+"_Parameters")};
        Object[] trmodel_wsnConstructorParametersValues = {trm_parameters};
        trmodel_wsn = (TRModel_WSN) Class.forName(packageName+trustModelName).getConstructor(trmodel_wsnConstructorParametersTypes).newInstance(trmodel_wsnConstructorParametersValues);

        Sensor.set_TRModel_WSN(trmodel_wsn);

        for (int net = 0; net < numNetworks; net++) {
            if ((network == null) || (numNetworks != 1)) {
                int numSensors = (int)(minNumSensors + Math.random()*Math.abs(maxNumSensors-minNumSensors));
                ArrayList<Double> probServices = new ArrayList<Double>();
                ArrayList<Double> probGoodness = new ArrayList<Double>();
                ArrayList<Service> services = new ArrayList<Service>();

                services.add(new Service("Relay")); // Only two types of services: Relay or RequiredService.
                services.add(requiredService);

                probServices.add(1.0);
                probGoodness.add(1.0);
                probServices.add(1.0-probRelay);
                probGoodness.add(1.0-probMalicious);

                network = trmodel_wsn.generateRandomNetwork(numSensors, probClients, radioRange, probServices, probGoodness, services);

                network.set_collusion(collusion);
                network.set_dynamic(dynamic);
            }

            for (Sensor client : network.get_clients())
                client.set_requiredService(requiredService);

            Collection<Outcome> outcomes = new ArrayList<Outcome>();
            int Ne = 0;
            if ((net % 5) == 0)
                System.out.println("\tnet = "+net);System.out.flush();
            for (; Ne < numExecutions; Ne++) {
                //System.out.println("\tnet = "+net+"; Ne = "+Ne);System.out.flush();
                Thread[] clients = new Thread[network.get_numClients()];
                int j = 0;
                for (Sensor client : network.get_clients())
                    clients[j++] = new Thread(client);

                for (int i = 0; i < clients.length; i++)
                    clients[i].start();

                for (int i = 0; i < clients.length; i++)
                    clients[i].join();

                for (Sensor client : network.get_clients())
                    if (client.get_outcome() != null)
                        outcomes.add(client.get_outcome());

                if ((oscillating) && (Ne % 20 == 0))
                    network.oscillate(requiredService);

            }
            Outcome outcome = Outcome.computeOutcomes(outcomes,network,requiredService,Ne);
            if (outcome == null) {
                if (net > 0)
                    net--;
            } else
                globalOutcomes.add(outcome);
        }

        return Outcome.computeOutcomes(globalOutcomes);
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane TRMParametersScrollPane;
    private javax.swing.JPanel TRM_ParametersPanelAux;
    private javax.swing.JComboBox TRModelComboBox;
    private javax.swing.JLabel TRModelLabel;
    private javax.swing.JMenu TRModelMenu;
    private javax.swing.JMenuItem aboutTRMSim_WSNmenuItem;
    private javax.swing.JButton applyChangesButton;
    private javax.swing.JButton applyParametersChangesButton;
    private javax.swing.JMenuItem applyParametersChangesMenuItem;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel bottomParametersContainerPanel;
    private javax.swing.JSplitPane bottomParametersSplitPane;
    private javax.swing.JButton browseButton;
    private javax.swing.JPanel buttonsControlPanel;
    private javax.swing.JCheckBox collusionCheckBox;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JScrollPane controlsScrollPane;
    private javax.swing.JRadioButton customizedParametersRadioButton;
    private javax.swing.JLabel delayLabel;
    private javax.swing.JSlider delaySlider;
    private javax.swing.JTextField delayTextField;
    private javax.swing.JPanel displayControlsPanel;
    private javax.swing.JCheckBox dynamicWSNsCheckBox;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JButton hideSensorPropertiesPanelButton;
    private javax.swing.JLabel legendLabel;
    private javax.swing.JPanel legendPanelContainer;
    private javax.swing.JMenuItem loadParametersMenuItem;
    private javax.swing.JButton loadWSNButton;
    private javax.swing.JMenuItem loadWSNmenuItem;
    private javax.swing.JLabel maxNumSensorsLabel;
    private javax.swing.JSpinner maxNumSensorsSpinner;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JScrollPane messagesScrollPane;
    private javax.swing.JTextArea messagesTextArea;
    private javax.swing.JLabel minNumSensorsLabel;
    private javax.swing.JSpinner minNumSensorsSpinner;
    private javax.swing.JLabel neighborsLabel;
    private javax.swing.JList neighborsList;
    private javax.swing.JScrollPane neighborsScrollPane;
    private javax.swing.JPanel networkAndSensorPropertiesContainerPanel;
    private javax.swing.JPanel networkPanelContainer;
    private javax.swing.JButton newWSNButton;
    private javax.swing.JMenuItem newWSNmenuItem;
    private javax.swing.JLabel numExecutionsLabel;
    private javax.swing.JSpinner numExecutionsSpinner;
    private javax.swing.JLabel numNetworksLabel;
    private javax.swing.JSpinner numNetworksSpinner;
    private javax.swing.JCheckBox oscillatingWSNsCheckBox;
    private javax.swing.JPanel outcomesPanelsPanel;
    private javax.swing.JTabbedPane outcomesTabbedPane;
    private javax.swing.JScrollPane parametersFileContentScrollPane;
    private javax.swing.JTextArea parametersFileContentTextArea;
    private javax.swing.JLabel parametersFileLabel;
    private javax.swing.JRadioButton parametersFileRadioButton;
    private javax.swing.JTextField parametersFileTextField;
    private javax.swing.JMenu parametersMenu;
    private javax.swing.JPanel parametersPanel;
    private javax.swing.JPanel parametersSettingsPanel;
    private javax.swing.ButtonGroup parametersSourceButtonGroup;
    private javax.swing.JLabel parametersSourceLabel;
    private javax.swing.JLabel percentageClientsLabel;
    private javax.swing.JSlider percentageClientsSlider;
    private javax.swing.JTextField percentageClientsTextField;
    private javax.swing.JLabel percentageMaliciousServersLabel;
    private javax.swing.JSlider percentageMaliciousServersSlider;
    private javax.swing.JTextField percentageMaliciousServersTextField;
    private javax.swing.JLabel percentageRelayServersLabel;
    private javax.swing.JSlider percentageRelayServersSlider;
    private javax.swing.JTextField percentageRelayServersTextField;
    private javax.swing.JLabel radioRangeLabel;
    private javax.swing.JLabel radioRangePropertyLabel;
    private javax.swing.JSlider radioRangeSlider;
    private javax.swing.JSpinner radioRangeSpinner;
    private javax.swing.JTextField radioRangeTextField;
    private javax.swing.JButton resetWSNButton;
    private javax.swing.JMenuItem resetWSNmenuItem;
    private javax.swing.JButton runSimulationsButton;
    private javax.swing.JMenuItem runSimulationsMenuItem;
    private javax.swing.JButton runTRMButton;
    private javax.swing.JMenuItem runTRMmenuItem;
    private javax.swing.JButton saveParametersFileContentButton;
    private javax.swing.JMenuItem saveParametersMenuItem;
    private javax.swing.JButton saveWSNButton;
    private javax.swing.JMenuItem saveWSNmenuItem;
    private javax.swing.JLabel sensorIdLabel;
    private javax.swing.JTextField sensorIdTextField;
    private javax.swing.JPanel sensorPropertiesPanel;
    private javax.swing.JComboBox sensorTypeComboBox;
    private javax.swing.JLabel sensorTypeLabel;
    private javax.swing.JSeparator separator1;
    private javax.swing.JSeparator separator2;
    private javax.swing.JSeparator separator3;
    private javax.swing.JSeparator separator4;
    private javax.swing.JCheckBox showGridCheckBox;
    private javax.swing.JCheckBox showIdsCheckBox;
    private javax.swing.JCheckBox showLinksCheckBox;
    private javax.swing.JCheckBox showRangesCheckBox;
    private javax.swing.JMenu simulationsMenu;
    private javax.swing.JPanel simulationsPanel;
    private javax.swing.JSplitPane simulationsSplitPane;
    private javax.swing.JPanel slidersControlsPanel;
    private javax.swing.JPanel spinnersControlPanel;
    private javax.swing.JButton stopSimulationsButton;
    private javax.swing.JMenuItem stopSimulationsMenuItem;
    private javax.swing.JButton stopTRMButton;
    private javax.swing.JMenuItem stopTRMmenuItem;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel threatsControlsPanel;
    private javax.swing.JPanel upperPanel;
    private javax.swing.JSplitPane upperSplitPane;
    private javax.swing.JMenu wsnMenu;
    private javax.swing.JLabel xCoordinateLabel;
    private javax.swing.JTextField xCoordinateTextField;
    private javax.swing.JLabel yCoordinateLabel;
    private javax.swing.JTextField yCoordinateTextField;
    // End of variables declaration//GEN-END:variables
    private TRMParametersPanel TRM_ParametersPanel;
    private NetworkPanel networkPanel = new NetworkPanel();

    private Collection<OutcomesPanel> outcomesPanels;
    private LegendPanel legendPanel = new LegendPanel();
}