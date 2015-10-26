import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.fazecast.jSerialComm.SerialPort;

public class SensorGraph {
	
	static SerialPort chosenPort;
	static int x = 0;

	public static void main(String[] args) {
		// create/configure the window
		JFrame window = new JFrame();
		window.setTitle("Sensor Graph GUI");
		window.setSize(600,400);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create drop down for connection options
		JComboBox<String> portList = new JComboBox<String>();
		JButton connectButton = new JButton("Connect");
		JPanel topPanel = new JPanel();
		topPanel.add(portList);
		topPanel.add(connectButton);
		window.add(topPanel, BorderLayout.NORTH);
		
		//populate drop down
		SerialPort[] portNames = SerialPort.getCommPorts(); //collect port names into array
		for (int i = 0; i < portNames.length; i++) {
			//add each port name item to the list of ports
			portList.addItem(portNames[i].getSystemPortName());
		}
		
		// create line graph
		XYSeries series = new XYSeries("Light Sensor Readings"); //create series unique to single set of data
		XYSeriesCollection dataset = new XYSeriesCollection(series); //collect all series into single collection
		//add collective data set to chart
		JFreeChart chart = ChartFactory.createXYLineChart("Light Sensor Chart", "Time (seconds)", "Light", dataset);
		window.add(new ChartPanel(chart), BorderLayout.CENTER);
		
		// configure Connect button - use another thread to listen for data
		connectButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				if(connectButton.getText().equals("Connect")) {
					//attempt connection
					chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
					chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					//attempt to open the port - if successful, set new text and disable drop down
					if (chosenPort.openPort()) {
						connectButton.setText("Disconnect");
						portList.setEnabled(false);
					}
					
					//create a new thread that listens on serial
					Thread thread = new Thread(){
						@Override public void run() {
							//define new Scanner receiving serial input
							Scanner scanner = new Scanner(chosenPort.getInputStream());
							while (scanner.hasNextLine()) {
								//if stream has another line, parse and add to series
								try {
									String line = scanner.nextLine(); //get text
									int number = Integer.parseInt(line); //parse text into integer
									series.add(x++, 1023 - number);	//add int to data series
								} catch(Exception e) {} //if error, skip
							} //if no more lines, exit loop and close Scanner
							scanner.close();
						}
					};
					thread.start();
				} else { //else, if button text equals "Disconnect" then perform these tasks
					//disconnect from serial port
					chosenPort.closePort();
					//re-enable list of ports
					portList.setEnabled(true);
					//switch button text back to "Connect"
					connectButton.setText("Connect");
					//if wanted to clear data: series.clear(); /n x = 0;
					// TODO: add button to clear data that is active only when stopped
				}
			}
			
		});
		
		// display window
		window.setVisible(true);
		
	}

}
