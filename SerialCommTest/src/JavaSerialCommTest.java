import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JSlider;

import com.fazecast.jSerialComm.*;

public class JavaSerialCommTest {

	public static void main(String[] args) {
		// create window object and components
		JFrame window = new JFrame();
		JSlider slider = new JSlider();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		slider.setMaximum(1023);
		slider.setMinimum(0);
		window.add(slider);
		
		window.pack();
		window.setVisible(true);
		
		// gather comm ports into an array
		SerialPort ports[] = SerialPort.getCommPorts();
		// display the ports and their names
		System.out.println("Select the port:");
		for (int counter = 0; counter < ports.length; counter++) {
			System.out.println(counter+1 + ".) " + ports[counter].getSystemPortName() + " : " + ports[counter].getDescriptivePortName());
		}
		// ask for the user to select a port
		System.out.print("Type the port number: ");
		
		// collect user input
		Scanner scanner = new Scanner(System.in);
		int chosenPort = scanner.nextInt();
		// assign chosen port
		SerialPort port = ports[chosenPort - 1];
		
		// notify if the port was opened
		if (port.openPort()) {
			System.out.println("Port successfully opened");
		} else {
			System.out.println("Port was not successfully opened");
			scanner.close();
			return;
		}
		// set port timeout
		port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		
		// collect port data
		Scanner data = new Scanner(port.getInputStream());
		while (data.hasNextLine()) {
			int number = 0;
			try {
				number = Integer.parseInt(data.nextLine());
			} catch(Exception e) { /*do nothing if an error is found*/ }
			// output number to slider object
			slider.setValue(number);
			System.out.println(number);
		}
		
		// some Scanner object clean up
		scanner.close();
		data.close();
		
		// TODO: add "STOP" button to stop transmission
	}

}
