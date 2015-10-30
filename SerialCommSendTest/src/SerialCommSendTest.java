import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.io.PrintWriter;

//import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;

import com.fazecast.jSerialComm.SerialPort;

public class SerialCommSendTest {

	static SerialPort commPort;
	static Scanner streamFromArduino;
	static PrintWriter streamToArduino;
	//static int seriesItemCounter = 0;

	public static void main(String[] args) {
		// create/configure the window :: MOVED CODE FROM HERE TO NEW CLASS "Window"
		JFrame window = new Window();
		
		// create line graph
		//XYSeries series = new XYSeries("Light Sensor Readings"); //create series unique to single set of data
		//XYSeriesCollection dataset = new XYSeriesCollection(series); //collect all series into single collection
		//add collective data set to chart
		//JFreeChart chart = ChartFactory.createXYLineChart("Light Sensor Chart", "Time (seconds)", "Light", dataset);
		//window.add(new ChartPanel(chart), BorderLayout.CENTER);
		
		// create text fields - Serial send and Serial receive
		JTextField textEntry = new JTextField("Enter text here.", 30);
		JTextArea textResult = new JTextArea("Text result", 1, 30);
		JButton sendButton = new JButton("Send");
		//add text fields to the window 
		JPanel inputPanel = new JPanel();
		JPanel outputPanel = new JPanel();
		inputPanel.add(textEntry);
		inputPanel.add(sendButton);
		outputPanel.add(textResult);
		window.add(inputPanel, BorderLayout.SOUTH);
		window.add(outputPanel, BorderLayout.CENTER);
		sendButton.setEnabled(false);
		
		// configure Connect button - use another thread to listen for data
		connectButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				if(connectButton.getText().equals("Connect")) {
					//attempt connection
					commPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
					commPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					//attempt to open the port - if successful, set new text and disable drop down
					if (commPort.openPort()) {
						connectButton.setText("Disconnect");
						portList.setEnabled(false);
						sendButton.setEnabled(true);
					}
					
					// TODO: Create a new thread to communicate on serial - is new thread necessary though?
					//create a new thread that listens on serial
					/*Thread thread = new Thread(){
						@Override public void run() {
							//define new Scanner receiving serial input
							Scanner scanner = new Scanner(chosenPort.getInputStream());
							while (scanner.hasNextLine()) {
								//if stream has another line, parse and add to series
								try {
									String line = scanner.nextLine(); //get text
									int number = Integer.parseInt(line); //parse text into integer
									series.add(seriesItemCounter++, 1023 - number);	//add int to data series
								} catch(Exception e) {} //if error, skip
							} //if no more lines, exit loop and close Scanner
							scanner.close();
						}
					};
					thread.start();*/
				} else { //else, if button text equals "Disconnect" then perform these tasks
					//disconnect from serial port
					commPort.closePort();
					//re-enable list of ports
					portList.setEnabled(true);
					//switch button text back to "Connect"
					connectButton.setText("Connect");
					//if wanted to clear data: series.clear(); /n x = 0;
					// TODO: add button to clear data that is active only when stopped
					sendButton.setEnabled(false);
				}
			}
			
		});
		
		sendButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent e) {
				String textReceived;
				//enter here if Send button is pushed
				if (commPort.isOpen()) {
					//send text to Arduino
					streamToArduino = new PrintWriter(commPort.getOutputStream());
					streamToArduino.print("Test");
					// TODO: configure streamToArduino for text input by user
					
					//receive response
					streamFromArduino = new Scanner(commPort.getInputStream());
					textReceived = streamFromArduino.nextLine();
					textResult.setText(textReceived);
				} else {
					textResult.setText("!!! Port is not configured !!!");
				}
			}
		});
		
		// display window
		window.setVisible(true);
		
	}

}
