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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 * This is the GUI Class representing the initial Splash Screen of the simulator
 * @author Jose Maria Alcaraz jmalcaraz@gmail.com
 * @version 0.2
 * @since 0.2
 */
public class SplashScreen extends JWindow {
    /**
     * Is the layout of the GUI
     */
    BorderLayout borderLayout1 = new BorderLayout();
    /**
     * Is the image that are shown in the GUI
     */
    JLabel imageLabel = new JLabel();
    /**
     * Is the south internal panel
     */
    JPanel southPanel = new JPanel();
    /**
     * This is the layout associated to this south panel
     */
    FlowLayout southPanelFlowLayout = new FlowLayout();
    /**
     * This is the progress bar that appears in the GUI
     */
    JProgressBar progressBar = new JProgressBar();

    /**
     * This is the image that are shown in the correspond JLabel imageLabel
     */
    ImageIcon imageIcon;

    /**
     * This is the default constructor
     *
     * @param imageIcon is the image that will be shown in the splash screen
     */
    public SplashScreen(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
        try {
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this is the initialization and the construction of the main
     *
     * @throws Exception in case of any exception occurs
     */
    void jbInit() throws Exception {
        imageLabel.setIcon(imageIcon);
        this.getContentPane().setLayout(borderLayout1);
        southPanel.setLayout(southPanelFlowLayout);
        southPanel.setBackground(Color.BLACK);
        this.getContentPane().add(imageLabel, BorderLayout.CENTER);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
        southPanel.add(progressBar, null);
        progressBar.setSize((int)(getContentPane().getWidth()*0.9), progressBar.getHeight());
        progressBar.setBorderPainted(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * Stablish the progress max number in the splash screen in the case of this is determined
     *
     * @param maxProgress Is the progress max number that will be assigned to the progress bar.
     */
    public void setProgressMax(int maxProgress) {
        progressBar.setMaximum(maxProgress);
    }

    /**
     * Stablish the progress number in the splash screen in the case of this is determined
     *
     * @param progress Is the progress number that will be assigned to the progress bar.
     */
    public void setProgress(int progress) {
        if (progress == -1)
            progressBar.setIndeterminate(true);
        else {
            final int theProgress = progress;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setValue(theProgress);
                }
            });
        }
    }

    /**
     * Stablish the progress and the message in the splash screen
     *
     * @param message  Is the message that will be established
     * @param progress The progress number that will be established (in the case of is determined)
     */
    public void setProgress(String message, int progress) {
        final int theProgress = progress;
        final String theMessage = message;
        setProgress(progress);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (theProgress == -1)
                    progressBar.setIndeterminate(true);
                else
                    progressBar.setValue(theProgress);
                setMessage(theMessage);
            }
        });
    }

    /**
     * Run the splash screen in a thread
     *
     * @param b true to make the splash screen visible and false to hide it..
     */
    public void setScreenVisible(boolean b) {
        final boolean boo = b;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setVisible(boo);
            }
        });
    }

    /**
     * Stablish a message in the splash screen
     *
     * @param message Is the message that will be established
     */
    private void setMessage(String message) {
        if (message == null) {
            message = "";
            progressBar.setStringPainted(false);
        } else {
            progressBar.setStringPainted(true);
        }
        progressBar.setString(message);
        progressBar.setSize((int)(getContentPane().getWidth()*0.9), progressBar.getHeight());
    }
}