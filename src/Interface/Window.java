package Interface;
/**
 * Author: Saul Colin
 * Date: 16/04/2022
 * I made this simple app just to better handle my money.
 * This version isnt the final one, i still want to add some features (like notes on what the incomes or wdraws were about)
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;    
import java.time.format.DateTimeFormatter;  
import java.util.ArrayList;



public class Window extends JFrame implements ActionListener{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	JPanel displayMoney = new JPanel();
	JPanel topPanel = new JPanel();
	JPanel firstHalf = new JPanel();
	JPanel secHalf = new JPanel();
	
	JLabel currMoney = new JLabel();
	JLabel whatsOn = new JLabel();
	
	JButton payIn = new JButton("Ingresar");
	JButton withdraw = new JButton("Retirar");
	JButton showLastIncomes = new JButton("Recibo de ingresos");
	JButton showLastWDraws = new JButton("Recibo de retiros");
	
	String amount = "";
	String amountPath = "amount.txt";
	String payInPath = "ingresosLog.txt";
	String wdrawPath = "retirosLog.txt";
	
	Window(){
		this.setSize((int)(screenSize.width/2), (int)(screenSize.height/2));

		
		topPanel.setBackground(new Color(245,245,245));
		
		whatsOn.setText("Dinero actual");
		whatsOn.setFont(new Font("Arial", Font.BOLD, 40));
		
		topPanel.add(whatsOn);
		
		firstHalf.setSize(this.getWidth(), this.getHeight()/2);
		firstHalf.add(topPanel);
		firstHalf.add(displayMoney);
		firstHalf.setLayout(new FlowLayout(FlowLayout.CENTER, 1000,10));
		
		secHalf.setSize(this.getWidth(), this.getHeight()/2);
		secHalf.setBounds(0, firstHalf.getHeight(), secHalf.getWidth(), secHalf.getHeight());
		secHalf.setPreferredSize(new Dimension(secHalf.getWidth()/2, secHalf.getHeight()/2));
		
		payIn.setPreferredSize(new Dimension(secHalf.getWidth()/4,secHalf.getHeight()/4));
		payIn.setFocusable(false);
		payIn.addActionListener(this);
		
		withdraw.setPreferredSize(new Dimension(secHalf.getWidth()/4,secHalf.getHeight()/4));
		withdraw.setFocusable(false);
		withdraw.addActionListener(this);
		
		showLastIncomes.setPreferredSize(new Dimension(secHalf.getWidth()/4,secHalf.getHeight()/4));
		showLastIncomes.setFocusable(false);
		showLastIncomes.addActionListener(this);
		
		showLastWDraws.setPreferredSize(new Dimension(secHalf.getWidth()/4,secHalf.getHeight()/4));
		showLastWDraws.setFocusable(false);
		showLastWDraws.addActionListener(this);
		
		
		secHalf.add(payIn);
		secHalf.add(withdraw);
		secHalf.add(showLastIncomes);
		secHalf.add(showLastWDraws);
		secHalf.setLayout(new FlowLayout(FlowLayout.CENTER, 100,10));
		secHalf.setBackground(Color.white);
		
		amount = readFile(amountPath);
		
		while(amount.isEmpty() || !amount.matches("-?(0|[1-9]\\d*)") || amount.contains("-")) {
			amount = 
					JOptionPane.showInputDialog("Ingresa la cantidad de dinero que actualmente tienes: ");
		}
		//save amount in the file amount.txt
		writeToFile(amount, amountPath);
	
		
		currMoney.setText(amount);
		currMoney.setFont(new Font("Arial", Font.BOLD, 80));
		displayMoney.add(currMoney);
		
		
		displayMoney.setSize(firstHalf.getWidth() , firstHalf.getHeight()/3);
		displayMoney.setBackground(new Color(255,255,255));
		displayMoney.setBounds(
				50, 50, firstHalf.getWidth() - 125, displayMoney.getHeight()
		);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.add(firstHalf);
		this.add(secHalf);
		this.setTitle("Administrador de Plata");
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == payIn) {
			addToAmount(
					inOnlyInteger("Ingresa la cantidad de dinero que quieres añadir: ")
					);

		}
		if(e.getSource() == withdraw) {
			subToAmount(
					inOnlyInteger("Ingresa la cantidad de dinero que quieres retirar")
					);
		}
		
		if(e.getSource() == showLastIncomes) {
			lastIncomesDisplay();
		}
		
		if(e.getSource() == showLastWDraws) {
			lastWDrawDisplay();
		}
		
	}
	
	public void addToAmount(int income) {
		amount = ""+(Integer.parseInt(amount) + income);
		writeToFile(amount, amountPath);
		currMoney.setText(amount);
		logToIncome(income);
	}
	
	public void subToAmount(int wdraw) {
		amount = "" + (Integer.parseInt(amount) - wdraw);
		writeToFile(amount, amountPath);
		currMoney.setText(amount);
		logToWDraw(wdraw);
	}
	
	public void writeToFile(String txt, String path) {
		try {
			FileWriter writer = new FileWriter(path);
			writer.write(txt);
			writer.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void appendToFile(String txt, String path) {
		try {
			FileWriter writer = new FileWriter(path, true);
			writer.append(txt);
			writer.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public String readFile(String path) {
		try {
			FileReader reader = new FileReader(path);
			int data = reader.read();
			String tmp = "";
			while(data != -1) {
				tmp += (char)data;
				data = reader.read();
			}
						
			reader.close();
			return tmp; 
		}catch(FileNotFoundException e) {
			System.out.println("No se encontro el archivo");
			return "";
		}catch(IOException e) {
			System.out.println("Error al leer el archivo");
			return "";
		}
	}
	
	public int inOnlyInteger(String msg) {
		String input = "";
		while(input.isEmpty() || !input.matches("-?(0|[1-9]\\d*)") || input.contains("-")) {
			input = 
					JOptionPane.showInputDialog(msg);
		}
		
		return Integer.parseInt(input);
	}
	
	public void logToIncome(int income) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		appendToFile(dtf.format(LocalDateTime.now())+" $"+income + ",", payInPath);
	}
	
	public void logToWDraw(int wdraw) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		appendToFile(dtf.format(LocalDateTime.now())+" $"+wdraw + ",", wdrawPath);
	}

	
	public void lastIncomesDisplay() {
		JFrame frame = new JFrame();
		String[] logsArr = readFile(payInPath).split(",");
		TextArea t = new TextArea();
		t.setEditable(false);
		t.setFont(new Font("Arial", Font.PLAIN, 20));
		JScrollPane scroll = new JScrollPane(t);
		
		
		if((logsArr.length > 0) && !logsArr[0].isEmpty()) {
			for(int i = logsArr.length - 1; i>=0;i--)
				t.setText(t.getText() + logsArr[i] + "\n");	
		}else {
			t.setText("No hay recibos que mostrar.");
		}

		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(0, 0, (screenSize.width/4) - 1000, (screenSize.height/4) - 1000 );

		frame.getContentPane().add(t);
		frame.setSize(screenSize.width/6, screenSize.height/4);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Recibo de ingresos");
		frame.setVisible(true);
		
	}
	
	public void lastWDrawDisplay() {
		JFrame frame = new JFrame();
		String[] logsArr = readFile(wdrawPath).split(",");
		TextArea t = new TextArea();
		t.setEditable(false);
		t.setFont(new Font("Arial", Font.PLAIN, 20));
		JScrollPane scroll = new JScrollPane(t);
		
		
		if((logsArr.length > 0) && !logsArr[0].isEmpty()) {
			for(int i = logsArr.length - 1; i>=0;i--)
				t.setText(t.getText() + logsArr[i] + "\n");	
		}else {
			t.setText("No hay recibos que mostrar.");
		}

		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(0, 0, (screenSize.width/4) - 1000, (screenSize.height/4) - 1000 );

		frame.getContentPane().add(t);
		frame.setSize(screenSize.width/6, screenSize.height/4);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Recibo de retiros");
		frame.setVisible(true);
	}
	

}
