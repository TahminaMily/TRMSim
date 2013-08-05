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

package es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib;
import java.util.Collection;
import javax.swing.JFrame;

/**
 * <p>This class models a window used to plot a Fuzzy Set through a {@link FuzzySetPanel}</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class FuzzySetWindow extends JFrame {

	public static int DEFAULT_CHART_NUMBER_OF_POINTS = 1024;
	public static int DEFAULT_WIDTH = 500;
	public static int DEFAULT_HEIGHT = 300;

        private static FuzzySetWindow plotWindow;

	private FuzzySetWindow(String windowTitle, Variable variable) {
		super(windowTitle);
		FuzzySetPanel chartPanel = new FuzzySetPanel(variable);
		chartPanel.setPreferredSize(new java.awt.Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setContentPane(chartPanel);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pack();
                setLocationRelativeTo(null);
	}


	private FuzzySetWindow(String windowTitle, Collection<LinguisticTerm> linguisticTerms) {
		super(windowTitle);
		FuzzySetPanel chartPanel = new FuzzySetPanel(linguisticTerms);
		chartPanel.setPreferredSize(new java.awt.Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setContentPane(chartPanel);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pack();
                setLocationRelativeTo(null);
	}

	public static FuzzySetWindow showIt(String windowTitle, Variable variable) {
		plotWindow = new FuzzySetWindow(windowTitle, variable);
		plotWindow.setVisible(true);
                return plotWindow;
	}

	public static FuzzySetWindow showIt(String windowTitle, Collection<LinguisticTerm> linguisticTerms) {
		plotWindow = new FuzzySetWindow(windowTitle, linguisticTerms);
		plotWindow.setVisible(true);
                return plotWindow;
	}
        
}