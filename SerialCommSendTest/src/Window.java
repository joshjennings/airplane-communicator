import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.fazecast.jSerialComm.SerialPort;

public class Window {
	
	public Window() {
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
