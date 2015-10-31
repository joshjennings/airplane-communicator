import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.fazecast.jSerialComm.SerialPort;

public class Window {
	
	JFrame windowFrame;
	
	static SerialPort commPort;
	static Scanner streamFromArduino;
	static PrintWriter streamToArduino;

	public Window() {
		Thread thread = new Thread(){
			@Override public void run() {
				windowFrame = new JFrame();
				windowFrame.setTitle("Serial Test - Send to Arduino");
				windowFrame.setSize(600,200);
				windowFrame.setLayout(new BorderLayout());
				windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// create drop down for connection options
				JComboBox<String> portList = new JComboBox<String>();
				JButton connectButton = new JButton("Connect");
				JPanel topPanel = new JPanel();
				topPanel.add(portList);
				topPanel.add(connectButton);
				windowFrame.add(topPanel, BorderLayout.NORTH);

				//populate drop down
				SerialPort[] portNames = SerialPort.getCommPorts(); //collect port names into array
				for (int i = 0; i < portNames.length; i++) {
					//add each port name item to the list of ports
					portList.addItem(portNames[i].getSystemPortName());
				}
				
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
				windowFrame.add(inputPanel, BorderLayout.SOUTH);
				windowFrame.add(outputPanel, BorderLayout.CENTER);
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
				windowFrame.setVisible(true);
			}
		};
		thread.start();
	}

	public JFrame CommWindow() {
		JFrame window = new JFrame();
		window.setTitle("Serial Test - Send to Arduino");
		window.setSize(600,200);
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

		return window;
	}

}
