
package calculator;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SimpleCalculator Class implements a simple calculator.  In addition to performing the
 * basic arithmetic functions of addition, subtraction, multiplication, and division, it can
 * compute operation of exponents, square roots, inverse of numbers, and percentages.  Error
 * checking is accomplished for all mathematical computations.  The maximum number of digits
 * allowed for one entry is thirteen, and decimals are rounded off to the nearest fifth place.
 * @author:  MAbdurrahman
 * @date:  27 February 2016
 * @version:  1.0.0
 */
public class SimpleCalculator extends javax.swing.JFrame {
    //Instance Variables
    protected final static int MAX_LENGTH = 13;
    protected final static int INPUT_MODE = 0;
    protected final static int RESULT_MODE = 1;
    protected final static int ERROR_MODE = 2;
    protected int displayMode;
    protected boolean isClearForNextDigit;
    protected double lastNumber;
    protected String lastOperator;
    protected NumberFormat numberFormatter;
    
    /**
     * SimpleCalculator Constructor - Creates a new instance of the SimpleCalculator
     */
    @SuppressWarnings({"OverridableMethodCallInConstructor", "UseSpecificCatch"})
    public SimpleCalculator() {
        initComponents();
        getContentPane().setBackground(Color.decode("#3E3F40"));
        setCursor(HAND_CURSOR);
        setLocation(600,200);
        setResizable(false);
        setTitle("Calculator");
        
        /** The following of code creates and set a new icon for the frame */
        Image icon = Toolkit.getDefaultToolkit().getImage(SimpleCalculator.class.
                                            getResource("/images/calculator.png"));
        setIconImage(icon);
       
        /** Formats the textDisplay results */
        numberFormatter = new DecimalFormat("###.#####");
        
        /** This method initializes the instance variables */
        clearAll();
        
        /** Add the ActionListener to the number buttons */
        zeroButton.addActionListener(new NumberListener());
        oneButton.addActionListener(new NumberListener());
        twoButton.addActionListener(new NumberListener());
        threeButton.addActionListener(new NumberListener());
        fourButton.addActionListener(new NumberListener());
        fiveButton.addActionListener(new NumberListener());
        sixButton.addActionListener(new NumberListener());
        sevenButton.addActionListener(new NumberListener());
        eightButton.addActionListener(new NumberListener());
        nineButton.addActionListener(new NumberListener());
        
        /** Add the ActionListener to the operator buttons */
        addButton.addActionListener(new OperatorListener());
        exponentButton.addActionListener(new OperatorListener());
        subtractButton.addActionListener(new OperatorListener());
        multiplyButton.addActionListener(new OperatorListener());
        divideButton.addActionListener(new OperatorListener());
        
        /** Add the ActionListener to the equalButton */
        equalButton.addActionListener((ActionEvent ae) -> {
            double result;
                if (displayMode != ERROR_MODE) {
                    try {
                        result = processLastOperator();
                        displayResult(result);
                    
                    } catch (DivideByZeroException ex) {
                        displayError("Undefined ");
                        displayMode = ERROR_MODE;
                        Toolkit.getDefaultToolkit().beep();
                        
                    } catch (NonRealNumberException ex) {   
                        displayError("Non Real Number ");
                        displayMode = ERROR_MODE;
                        Toolkit.getDefaultToolkit().beep();
                        
                    } catch (InvalidEntryException ex) {
                        displayError("Invalid Entry ");
                        displayMode = ERROR_MODE;
                        Toolkit.getDefaultToolkit().beep();
                        
                    } catch (Exception ex) {
                        displayMode = ERROR_MODE;
                        Toolkit.getDefaultToolkit().beep();
                        
                    }
                    lastOperator = "0";
                    lastNumber = Double.parseDouble(textDisplay.getText());
                }
            });//end of the Lambda Expression for the equalButton
        
        /** Add the ActionListener to the clearButton */
        clearButton.addActionListener((ActionEvent ae) -> {
            clearAll();
            
        });//end of the Lambda Expression for the clearButton
        
        /** Add ActionListener to the backspaceButton */
        backspaceButton.addActionListener((ActionEvent ae) -> {
            if ((textDisplay.getText().contains("U")) || 
                 (textDisplay.getText().contains("N"))) {
                textDisplay.setText("0");
                
            }
            if (textDisplay.getText().length() >= 1) {
                textDisplay.setText(textDisplay.getText().substring(0, textDisplay.getText().length() -1));
                
            }
            if (textDisplay.getText().length() < 1) {
                textDisplay.setText("0");
                
            }
            if (textDisplay.getText().equals("-")) {
                textDisplay.setText("0");
                
            }
        });//end of the Lambda Expression for the backspaceButton
        
        /** Add the ActionListener to the plusMinusButton */
        plusMinusButton.addActionListener((ActionEvent ae) -> {
            if (displayMode == INPUT_MODE) {
                String input = textDisplay.getText();
                if ((input.length() > 0) && !(input.equals("0") ||
                        (input.equals(".0")))) {
                    if (input.indexOf("-") == 0) {
                        textDisplay.setText(input.substring(1));
                        
                    } else {
                        textDisplay.setText("-" + input);
                    }
                }
            } else if (displayMode == RESULT_MODE) {
                double number = Double.parseDouble(textDisplay.getText());
                if (number != 0) {
                    displayResult(-number);
                    
                }
            }
        });//end of the Lambda Expression for the plusMinusButton
        
        /** Add the ActionListener to the decimalButton */
        decimalButton.addActionListener((ActionEvent ae) -> {
           displayMode = INPUT_MODE;
           if (isClearForNextDigit) {
               textDisplay.setText("");
           }
           String number = textDisplay.getText();
           if (number.contains(".")) {//If the number contains a decimal, do not add another one
               if (isClearForNextDigit) {
                   textDisplay.setText(number);
                   isClearForNextDigit = false;
                   
               } else {
                   textDisplay.setText(textDisplay.getText());
                   
               }
           } else {//If the number does not contain a decimal, add one
               String decimal = ae.getActionCommand();//Get the text from the decimalButton
               if (isClearForNextDigit) {
                   textDisplay.setText(decimal);
                   isClearForNextDigit = false;
                   
               } else {
                   textDisplay.setText(textDisplay.getText() + decimal);
                   
               }
           }
        });//end of the Lambda Expression to add ActionListener for the decimalButton
        
        /** Add the ActionListener for the percentButton */
        percentButton.addActionListener((ActionEvent ae) -> {
           if (displayMode != ERROR_MODE) {
               double result = Double.parseDouble(textDisplay.getText());
               result = result * (.01);
               displayResult(result);
               
           } 
        });//end of the Lambda Expression to add ActionListener for the percentButton
        
        /** Add the ActionListener for the inverseButton */
        inverseButton.addActionListener((ActionEvent ae) -> {
            if (displayMode != ERROR_MODE) {
                try {
                    if ((textDisplay.getText().equals("0") ||
                            (textDisplay.getText().equals("0.0")))) {
                        displayError("Undefined ");
                        displayMode = ERROR_MODE;
                        Toolkit.getDefaultToolkit().beep();
                        
                    }
                    double result = Double.parseDouble(textDisplay.getText());
                    result = 1 / result;
                    displayResult(result);
                    
                } catch (Exception ex) {
                    displayError("Undefined ");
                    displayMode = ERROR_MODE;
                    Toolkit.getDefaultToolkit().beep();
                    
                }
            } 
        });//end of the Lambda Expression for the inverseButton
        
        /** Add the ActionListener to the squareRootButton */
        squareRootButton.addActionListener((ActionEvent ae) -> {
            if (displayMode != ERROR_MODE) {
                try {
                    if (!lastOperator.equals("0")) {
                        processEqualOperation();
                          
                    }
                    if (textDisplay.getText().indexOf("-") == 0) {
                        displayError("Non Real Number ");
                        displayMode = ERROR_MODE;
                        Toolkit.getDefaultToolkit().beep();
                        
                    }
                    double result = Double.parseDouble(textDisplay.getText());
                    result = Math.sqrt(result);
                    displayResult(result);
                    
                } catch (Exception ex) {
                    displayError("Non Real Number ");
                    displayMode = ERROR_MODE;
                    Toolkit.getDefaultToolkit().beep();
                    
                }
            }
        });//end of the Lambda Expression for the squareRootButton
    }//end of the SimpleCalculator Constructor
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        textDisplay = new javax.swing.JTextField();
        calculatorLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        subtractButton = new javax.swing.JButton();
        divideButton = new javax.swing.JButton();
        multiplyButton = new javax.swing.JButton();
        percentButton = new javax.swing.JButton();
        inverseButton = new javax.swing.JButton();
        exponentButton = new javax.swing.JButton();
        squareRootButton = new javax.swing.JButton();
        eightButton = new javax.swing.JButton();
        sevenButton = new javax.swing.JButton();
        nineButton = new javax.swing.JButton();
        fourButton = new javax.swing.JButton();
        fiveButton = new javax.swing.JButton();
        sixButton = new javax.swing.JButton();
        backspaceButton = new javax.swing.JButton();
        twoButton = new javax.swing.JButton();
        threeButton = new javax.swing.JButton();
        oneButton = new javax.swing.JButton();
        decimalButton = new javax.swing.JButton();
        plusMinusButton = new javax.swing.JButton();
        zeroButton = new javax.swing.JButton();
        equalButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));

        mainPanel.setBackground(Color.decode("#787574")
        );
        mainPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(51, 51, 51), new java.awt.Color(0, 0, 0), new java.awt.Color(51, 51, 51)));

        textDisplay.setEditable(false);
        textDisplay.setBackground(new java.awt.Color(51, 51, 51));
        textDisplay.setFont(new java.awt.Font("Bookman Old Style", 0, 22)); // NOI18N
        textDisplay.setForeground(new java.awt.Color(204, 204, 204));
        textDisplay.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textDisplay.setText("0");
        textDisplay.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(51, 51, 51), new java.awt.Color(0, 0, 0), new java.awt.Color(51, 51, 51)));

        calculatorLabel.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        calculatorLabel.setText("Simple Calculator");

        addButton.setBackground(new java.awt.Color(51, 51, 51));
        addButton.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/plusSign.png"))); // NOI18N
        addButton.setToolTipText("add");
        addButton.setActionCommand("+");

        subtractButton.setBackground(new java.awt.Color(51, 51, 51));
        subtractButton.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        subtractButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/minusSign.png"))); // NOI18N
        subtractButton.setToolTipText("substract");
        subtractButton.setActionCommand("-");

        divideButton.setBackground(new java.awt.Color(51, 51, 51));
        divideButton.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        divideButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/divideSign.png"))); // NOI18N
        divideButton.setToolTipText("divide");
        divideButton.setActionCommand("/");

        multiplyButton.setBackground(new java.awt.Color(51, 51, 51));
        multiplyButton.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        multiplyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/multiplySign.png"))); // NOI18N
        multiplyButton.setToolTipText("multiply");
        multiplyButton.setActionCommand("*");

        percentButton.setBackground(new java.awt.Color(51, 51, 51));
        percentButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        percentButton.setForeground(new java.awt.Color(0, 0, 0));
        percentButton.setText("%");
        percentButton.setToolTipText("percent");

        inverseButton.setBackground(new java.awt.Color(51, 51, 51));
        inverseButton.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        inverseButton.setForeground(new java.awt.Color(0, 0, 0));
        inverseButton.setText("1/x");
        inverseButton.setToolTipText("inverse");

        exponentButton.setBackground(new java.awt.Color(51, 51, 51));
        exponentButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        exponentButton.setForeground(new java.awt.Color(0, 0, 0));
        exponentButton.setText("^");
        exponentButton.setToolTipText("exponent");

        squareRootButton.setBackground(new java.awt.Color(51, 51, 51));
        squareRootButton.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        squareRootButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/squareRootSign.png"))); // NOI18N
        squareRootButton.setToolTipText("square root");

        eightButton.setBackground(new java.awt.Color(51, 51, 51));
        eightButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        eightButton.setForeground(new java.awt.Color(204, 204, 204));
        eightButton.setText("8");

        sevenButton.setBackground(new java.awt.Color(51, 51, 51));
        sevenButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        sevenButton.setForeground(new java.awt.Color(204, 204, 204));
        sevenButton.setText("7");

        nineButton.setBackground(new java.awt.Color(51, 51, 51));
        nineButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        nineButton.setForeground(new java.awt.Color(204, 204, 204));
        nineButton.setText("9");

        fourButton.setBackground(new java.awt.Color(51, 51, 51));
        fourButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        fourButton.setForeground(new java.awt.Color(204, 204, 204));
        fourButton.setText("4");

        fiveButton.setBackground(new java.awt.Color(51, 51, 51));
        fiveButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        fiveButton.setForeground(new java.awt.Color(204, 204, 204));
        fiveButton.setText("5");

        sixButton.setBackground(new java.awt.Color(51, 51, 51));
        sixButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        sixButton.setForeground(new java.awt.Color(204, 204, 204));
        sixButton.setText("6");

        backspaceButton.setBackground(new java.awt.Color(0, 102, 51));
        backspaceButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        backspaceButton.setForeground(new java.awt.Color(204, 204, 204));
        backspaceButton.setText("<<");
        backspaceButton.setToolTipText("backspace");

        twoButton.setBackground(new java.awt.Color(51, 51, 51));
        twoButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        twoButton.setForeground(new java.awt.Color(204, 204, 204));
        twoButton.setText("2");

        threeButton.setBackground(new java.awt.Color(51, 51, 51));
        threeButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        threeButton.setForeground(new java.awt.Color(204, 204, 204));
        threeButton.setText("3");

        oneButton.setBackground(new java.awt.Color(51, 51, 51));
        oneButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        oneButton.setForeground(new java.awt.Color(204, 204, 204));
        oneButton.setText("1");

        decimalButton.setBackground(new java.awt.Color(51, 51, 51));
        decimalButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        decimalButton.setForeground(new java.awt.Color(0, 0, 0));
        decimalButton.setText(".");
        decimalButton.setToolTipText("decimal");

        plusMinusButton.setBackground(new java.awt.Color(51, 51, 51));
        plusMinusButton.setFont(new java.awt.Font("Bookman Old Style", 1, 12)); // NOI18N
        plusMinusButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/plusMinusSign.png"))); // NOI18N
        plusMinusButton.setToolTipText("change sign");

        zeroButton.setBackground(new java.awt.Color(51, 51, 51));
        zeroButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        zeroButton.setForeground(new java.awt.Color(204, 204, 204));
        zeroButton.setText("0");

        equalButton.setBackground(new java.awt.Color(102, 51, 0));
        equalButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/equalSign.png"))); // NOI18N
        equalButton.setToolTipText("equals");

        clearButton.setBackground(new java.awt.Color(51, 0, 153));
        clearButton.setFont(new java.awt.Font("Bookman Old Style", 1, 18)); // NOI18N
        clearButton.setForeground(new java.awt.Color(204, 204, 204));
        clearButton.setText("CL");
        clearButton.setToolTipText("clear display");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(calculatorLabel)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addComponent(plusMinusButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(zeroButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(decimalButton))
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addComponent(oneButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(twoButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(threeButton)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(equalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(subtractButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(multiplyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(exponentButton, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(squareRootButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(inverseButton))
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(sevenButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(eightButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(nineButton)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(divideButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(percentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(clearButton)))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(fourButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fiveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sixButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(backspaceButton)))))
                .addGap(16, 16, 16))
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, backspaceButton, decimalButton, divideButton, eightButton, exponentButton, fiveButton, fourButton, inverseButton, multiplyButton, nineButton, oneButton, percentButton, plusMinusButton, sevenButton, sixButton, squareRootButton, threeButton, twoButton, zeroButton});

        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(textDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calculatorLabel)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(multiplyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(divideButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(subtractButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(percentButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(exponentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(squareRootButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inverseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sevenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(eightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nineButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fourButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fiveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sixButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backspaceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(oneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(twoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(threeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(plusMinusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zeroButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(decimalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(equalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addButton, backspaceButton, decimalButton, divideButton, eightButton, exponentButton, fiveButton, fourButton, inverseButton, multiplyButton, nineButton, oneButton, percentButton, plusMinusButton, sevenButton, sixButton, squareRootButton, threeButton, twoButton, zeroButton});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        
    /**
     * clearAll Method - Clears all and resets the calculator to the original initialization
     */
    private void clearAll() {
        textDisplay.setText("0");
        lastOperator = "0";
        lastNumber = 0;
        displayMode = INPUT_MODE;
        isClearForNextDigit = true;
        
    }//end of the clearAll Method
    /**
     * addNextDigitToDisplay Method - Adds the next digit to the display
     * @param nextDigit - integer representing the next digit
     */
    protected void addNextDigitToDisplay(int nextDigit) {
        if (isClearForNextDigit) {
            textDisplay.setText("");
            
        }
        String inputDigit = textDisplay.getText();
                
        if (inputDigit.indexOf("0") == 0) {
            inputDigit = inputDigit.substring(1);
            
        }  
        if ((!inputDigit.equals("0") || nextDigit >= 0) 
                                && inputDigit.length() < MAX_LENGTH) {
            textDisplay.setText(inputDigit + nextDigit);
            
        }
        displayMode = INPUT_MODE;
        isClearForNextDigit = false;
        
    }//end of the addNextDigitToDisplay Method
    /**
     * processOperator Method - Processes the operator by calling processLastOperator
     * method
     * @param operator - String representing the operator
     * @throws DivideByZeroException
     * @throws NonRealNumberException
     * @throws InvalidEntryException
     * @throws Exception
     */
    @SuppressWarnings("UseSpecificCatch")
    protected void processOperator(String operator) throws DivideByZeroException,
                                                           NonRealNumberException,
                                                           InvalidEntryException,
                                                           Exception {
        if (displayMode != ERROR_MODE) {
            double number = Double.parseDouble(textDisplay.getText());
            
            if (!lastOperator.equals("0")) {
                try {
                    double result = processLastOperator();
                    displayResult(result);
                    lastNumber = result;
                    
                } catch(DivideByZeroException ex) {
                    displayError("Undefined");
                    displayMode = ERROR_MODE;
                    Toolkit.getDefaultToolkit().beep();
                   
                } catch(NonRealNumberException ex) {
                    displayError("Non Real Number ");
                    displayMode = ERROR_MODE;
                    Toolkit.getDefaultToolkit().beep();
                    
                } catch (InvalidEntryException ex) {
                    displayError("Invalid Entry ");
                    displayMode = ERROR_MODE;
                    Toolkit.getDefaultToolkit().beep();
                    
                } catch(Exception ex) {
                    displayError("Number Format ");
                    displayMode = ERROR_MODE;
                    Toolkit.getDefaultToolkit().beep();
                }
                   
            } else {
                lastNumber = number;
            }
            isClearForNextDigit = true;
            lastOperator = operator;
        }
    }//end of the processOperator Method
    /**
     * displayError Method - Displays the appropriate error message
     * @param message - String representing the error message
     */
    protected void displayError(String message) {
        textDisplay.setText(message);
        lastNumber = 0;
        displayMode = ERROR_MODE;
        isClearForNextDigit = true;
        
    }//end of the displayError Method
    /**
     * displayResult Method - Displays the result in the number format
     * @param result - double representing the result
     */
    protected void displayResult(double result) {
        String answer = numberFormatter.format(result);
        textDisplay.setText(answer);
        lastNumber = result;
        displayMode = RESULT_MODE;
        isClearForNextDigit = true;
        
    }//end of the displayResult Method
    /**
     * processLastOperator Method - Performs the operations of addition, subtraction, multiplication,
     * division, and exponents.
     * @return result - a double representing the result
     * @throws DivideByZeroException
     * @throws InvalidEntryException
     * @throws NonRealNumberException
     */
    @SuppressWarnings("IncompatibleEquals")
    protected double processLastOperator() throws InvalidEntryException,
                                                  DivideByZeroException,
                                                  NonRealNumberException {
        double result = 0;
        double number = Double.parseDouble(textDisplay.getText());
        
        if (lastOperator.equals("+")) {
            if ((textDisplay.equals(".") || (textDisplay.equals(" ")))) {
                throw (new InvalidEntryException("Invalid Entry "));
            }
            result = lastNumber + number;
            
        }
        if (lastOperator.equals("-")) {
            if ((textDisplay.equals(".") || (textDisplay.equals(" ")))) {
                throw (new InvalidEntryException("Invalid Entry "));
            }
            result = lastNumber - number;
            
        }
        if (lastOperator.equals("*")) {
            if ((textDisplay.equals(".") || (textDisplay.equals(" ")))) {
                throw (new InvalidEntryException("Invalid Entry "));
            }
            result = lastNumber * number;
            
        }
        if (lastOperator.equals("^")) {
            if ((lastNumber < 0) && (textDisplay.getText().contains("."))) {
                throw (new NonRealNumberException("Non Real Number"));
                
            }
            if ((textDisplay.equals(".") || (textDisplay.equals(" ")))) {
                throw (new InvalidEntryException("Invalid Entry "));
            }
            
            result = processExponent(lastNumber, number);
                
        }
        if (lastOperator.equals("/")) {
            if (number == 0) {
                throw (new DivideByZeroException("Undefined "));
            }
            if ((textDisplay.equals(".") || (textDisplay.equals(" ")))) {
                throw (new InvalidEntryException("Invalid Entry "));
            }
            result = lastNumber / number;
            
        }
        return result;
        
    }//end of the processLastOperator Method
    /**
     * processExponent Method - Performs the exponent operations
     * @param base - a double representing the base number
     * @param exponent - a double representing the exponent number
     * @return double - Returns the first number raised to the second number as a
     * double
     */
    protected Double processExponent(double base, double exponent) {
        return Math.pow(base, exponent);
    
    }//end of the processExponent Method
    /**
     * processEqualOperation Method - Process the equal operation
     */
    @SuppressWarnings("UseSpecificCatch")
    protected void processEqualOperation() {
        double result;
        if (displayMode != ERROR_MODE) {
            try {
                result = processLastOperator();
                displayResult(result);

            } catch (DivideByZeroException ex) {
                displayError("Undefined ");
                displayMode = ERROR_MODE;
                Toolkit.getDefaultToolkit().beep();

            } catch (NonRealNumberException ex) {   
                displayError("Non Real Number ");
                displayMode = ERROR_MODE;
                Toolkit.getDefaultToolkit().beep();

            } catch (InvalidEntryException ex) {
                displayError("Invalid Entry ");
                displayMode = ERROR_MODE;
                Toolkit.getDefaultToolkit().beep();

            } catch (Exception ex) {
                displayMode = ERROR_MODE;
                Toolkit.getDefaultToolkit().beep();

            }
            lastOperator = "0";
            lastNumber = Double.parseDouble(textDisplay.getText());
        }
    }//end of the processEqualOperation Method 
    /**
     * NumberListener Class implements the ActionListener to respond to the events of the
     * the number buttons.
     */
    class NumberListener implements ActionListener {
        /**
         * actionPerformed Method - Overrides the actionPerformed method of the ActionListener
         * Interface, and redefines it to respond to the number buttons.
         * @param ae - the ActionEvent of clicking a number button
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            int digit = Integer.parseInt(ae.getActionCommand());
            
            switch (digit) {
                case 0:
                    addNextDigitToDisplay(digit);
                    break;
                case 1:
                    addNextDigitToDisplay(digit);
                    break;
                case 2:
                    addNextDigitToDisplay(digit);
                    break;
                case 3:
                    addNextDigitToDisplay(digit);
                    break;
                case 4:
                    addNextDigitToDisplay(digit);
                    break;
                case 5:
                    addNextDigitToDisplay(digit);
                    break;
                case 6:
                    addNextDigitToDisplay(digit);
                    break;
                case 7:
                    addNextDigitToDisplay(digit);
                    break;
                case 8:
                    addNextDigitToDisplay(digit);
                    break;
                case 9:
                    addNextDigitToDisplay(digit);
                    break;
                default:
                    textDisplay.setText("ERROR");
                    displayMode = ERROR_MODE;
                    Toolkit.getDefaultToolkit().beep();
                    
            }
        }//end of the actionPerformed Method for the NumberListener Class
    }//end of the NumberListener Class
    /**
     * OperatorListener Class implements the ActionListener to respond to the events of the
     * operant buttons
     */
    class OperatorListener implements ActionListener {
        /**
         * actionPerformed Method - Overrides the actionPerformed Method of the ActionListener
         * Interface, and redefines it to responds to the events of the following operant buttons:
         * addButton, subtractButton, multiplyButton, divideButton, and exponentButton,
         * @param ae - the ActionEvent of clicking one of the above mentioned operand buttons
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                String operator = ae.getActionCommand();
                switch (operator) {
                    case "+":
                        processOperator(operator);
                        break;
                    case "-":
                        processOperator(operator);
                        break;
                    case "*":
                        processOperator(operator);
                        break;
                    case "/":
                        processOperator(operator);
                        break;
                    case "^":
                        processOperator(operator);
                        break;
                    default:
                        textDisplay.setText("Invalid Operator");
                        Toolkit.getDefaultToolkit().beep();
                }
            } catch (DivideByZeroException ex) {
                try {
                    throw new DivideByZeroException();
                    
                } catch (DivideByZeroException ex1) {
                    Logger.getLogger(SimpleCalculator.class.getName()).log(Level.SEVERE, null, ex1);
                    Toolkit.getDefaultToolkit().beep();
                }
            } catch (NonRealNumberException ex) {
                try {
                    throw new NonRealNumberException();
                    
                } catch (NonRealNumberException ex1) {
                    Logger.getLogger(SimpleCalculator.class.getName()).log(Level.SEVERE, null, ex1);
                    Toolkit.getDefaultToolkit().beep();
                }
                
            } catch (InvalidEntryException ex) {
                try {
                    throw new InvalidEntryException();
                    
                } catch (InvalidEntryException ex1) {
                   Logger.getLogger(SimpleCalculator.class.getName()).log(Level.SEVERE, null, ex1);
                   Toolkit.getDefaultToolkit().beep();
                    
                } 
            } catch (Exception ex) {
                Logger.getLogger(SimpleCalculator.class.getName()).log(Level.SEVERE, null, ex);
                Toolkit.getDefaultToolkit().beep();
                
            }
        }//end of the actionPerformed Method for the OperatorListener Class
    }//end of the OperatorListener Class
    /**
     * DivideByZeroException Class - Custom exception for dividing by zero
     */
    class DivideByZeroException extends Exception {
        /**
         * Default DivideByZeroException Constructor - Creates an instance of the DivideByZeroException
         * with no parameter.
         */
        public DivideByZeroException() {
            super("Undefined ");
            
        }//end of Default DivideByZeroException Constructor
        /**
         * DivideByZeroException Constructor - Creates an instance of the DivideByZeroException with
         * one parameter.
         * @param message - a String representing the message
         */
        public DivideByZeroException(String message) {
            super(message);
            
        }//end of the DivideByZeroException Constructor
    }//end of the DivideByZeroException Class
    /**
     * NonRealNumberException Class - Custom exception for taking square root of a negative 
     * number
     */
    class NonRealNumberException extends Exception {
        /**
         * Default NonRealNumberException Constructor - Creates an instance of NonRealNumberException
         * with no parameter.
         */
        public NonRealNumberException() {
            super("Non Real Number ");
            
        }//end of Default NonRealNumberException Constructor
        /**
         * NonRealNumberException Constructor - Creates an instance of NonRealNumberException with one
         * parameter.
         * @param message - a String representing the message
         */
        public NonRealNumberException(String message) {
            super(message);
            
        }//end of the NonRealNumberException Constructor
    }//end of the NonRealNumberException Class
    /**
     * InvalidEntryException Class - Custom exception for invalid entries
     */
    class InvalidEntryException extends Exception {
        /**
         * Default InvalidEntryException Constructor - Creates an instance of the InvalidEntryException
         * with no parameter.
         */
        public InvalidEntryException() {
            super("Invalid Entry ");
            
        }//end of the Default InvalidEntryException Constructor
        /**
         * InvalidEntryException Constructor - Creates an instance of the InvalidEntryException with
         * one parameter
         * @param message - a String representing the message
         */
        public InvalidEntryException(String message) {
            super(message);
            
        }//end of the InValidEntryException Constructor
    }//end of the InValidEntryException Class
    /**
     * main Method - Contains the command line arguments
     * @param args - a String[] representing the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | 
                 InstantiationException |  
                 IllegalAccessException | 
                 javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SimpleCalculator.class.getName()).
                                    log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /** Create and display the SimpleCalculator */
        java.awt.EventQueue.invokeLater(() -> {
            new SimpleCalculator().setVisible(true);
        });
    }//end of the main Method

    // Variables declaration - do not modify                     
    private javax.swing.JButton addButton;
    private javax.swing.JButton backspaceButton;
    private javax.swing.JLabel calculatorLabel;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton decimalButton;
    private javax.swing.JButton divideButton;
    private javax.swing.JButton eightButton;
    private javax.swing.JButton equalButton;
    private javax.swing.JButton exponentButton;
    private javax.swing.JButton fiveButton;
    private javax.swing.JButton fourButton;
    private javax.swing.JButton inverseButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton multiplyButton;
    private javax.swing.JButton nineButton;
    private javax.swing.JButton oneButton;
    private javax.swing.JButton percentButton;
    private javax.swing.JButton plusMinusButton;
    private javax.swing.JButton sevenButton;
    private javax.swing.JButton sixButton;
    private javax.swing.JButton squareRootButton;
    private javax.swing.JButton subtractButton;
    private javax.swing.JTextField textDisplay;
    private javax.swing.JButton threeButton;
    private javax.swing.JButton twoButton;
    private javax.swing.JButton zeroButton;
    // End of variables declaration                   
}//end of the SimpleCalculator Class
